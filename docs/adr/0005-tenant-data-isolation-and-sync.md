# ADR 0005 — Isolamento de dados por Tenant e estratégia de sincronização

## Status

Aceito

## Contexto

O PetLife evoluiu para uma arquitetura SaaS Multi-Tenant, na qual uma única aplicação atende múltiplas empresas.

A identificação visual e a autenticação já utilizam o Tenant associado ao usuário autenticado, conforme definido nos ADRs anteriores.

Entretanto, os dados locais armazenados no Room inicialmente não possuíam identificação de Tenant.

Isso fazia com que registros de Pets e Vacinas fossem compartilhados entre empresas no dispositivo.

Por exemplo, um Pet cadastrado no Tenant PetLife poderia aparecer após o login de um usuário pertencente à Clínica Bicho Feliz.

Essa situação não é aceitável em uma arquitetura Multi-Tenant.

Era necessário implementar:

- isolamento local por Tenant;
- isolamento remoto por Tenant;
- preservação dos dados existentes no Room;
- sincronização entre Room e Supabase;
- suporte a cenários offline;
- proteção contra alterações entre Tenants diferentes;
- uma base reutilizável de sincronização para novos módulos.

---

## Decisão

Cada registro de negócio armazenado localmente deve estar associado explicitamente a um Tenant.

O identificador utilizado é o mesmo UUID do Tenant existente no Supabase.

Para as entidades atualmente implementadas:

```text
PetEntity
└── tenantId

VaccineEntity
└── tenantId
```

Os repositórios são instanciados vinculados ao Tenant autenticado.

Exemplo conceitual:

```text
Usuário autenticado
        ↓
Profile
        ↓
tenant_id
        ↓
resolvedTenantId
        ↓
PetRepository / VaccineRepository
        ↓
consultas e operações filtradas pelo Tenant
```

O `resolvedTenantId` representa o Tenant autenticado efetivamente resolvido e deve ser utilizado para acesso aos dados de negócio.

O Tenant visual utilizado como fallback para tema, cores, logo ou banner não deve ser utilizado como fonte de autorização para acesso aos dados.

---

## Isolamento local com Room

As consultas de Pets e Vacinas devem sempre considerar o `tenantId`.

Exemplo conceitual:

```sql
SELECT *
FROM pets
WHERE tenantId = :tenantId;
```

Um usuário pertencente à Clínica Bicho Feliz não deve visualizar dados locais pertencentes ao PetLife, e vice-versa.

Os repositórios também validam operações de alteração e exclusão para impedir que uma entidade pertencente a outro Tenant seja manipulada.

As consultas normais de Pets e Vacinas também ignoram registros marcados com `pendingDelete`, evitando que itens em processo de exclusão continuem visíveis na interface.

---

## Migração dos dados existentes

A introdução do Multi-Tenant e da sincronização remota no Room foi realizada por migrations explícitas.

Os registros existentes foram preservados.

Pets existentes antes da implementação Multi-Tenant foram associados ao Tenant PetLife, mantendo os dados históricos já cadastrados.

As Vacinas existentes receberam o Tenant correspondente ao Pet relacionado.

Posteriormente, novas migrations adicionaram aos Pets e às Vacinas os campos necessários para sincronização remota e funcionamento offline.

Entre esses campos estão:

```text
remoteId
pendingSync
pendingDelete
```

A estratégia evitou o uso de migração destrutiva e preservou os dados já existentes no dispositivo.

A versão atual do banco local inclui a migration que adicionou identidade remota e flags de sincronização às Vacinas.

---

## Identidade local e remota de Pets

Os Pets possuem duas identidades:

```text
id
→ identificador local do Room

remoteId
→ UUID correspondente ao registro no Supabase
```

O `remoteId` permite relacionar um registro local ao mesmo registro remoto.

Exemplo:

```text
Room
id = 7
remoteId = a462652b-...
        ↓
Supabase
id = a462652b-...
```

Essa relação é utilizada para criação, edição, exclusão e sincronização.

---

## Identidade local e remota de Vacinas

As Vacinas também possuem duas identidades:

```text
id
→ identificador local do Room

remoteId
→ UUID correspondente ao registro no Supabase
```

Além disso, existe uma diferença importante entre a relação local e a relação remota com o Pet:

```text
Room
VaccineEntity.petId
→ PetEntity.id local

Supabase
vaccines.pet_id
→ pets.id remoto (UUID)
```

Antes de enviar uma Vacina ao Supabase, o aplicativo localiza o Pet correspondente no Room e utiliza o `remoteId` desse Pet como `pet_id` remoto.

Uma Vacina somente pode ser criada remotamente quando o Pet relacionado já possui identidade remota.

O `remoteId` da Vacina é utilizado posteriormente para edição, exclusão e sincronização bidirecional.

---

## Persistência remota

Pets e Vacinas possuem representação remota no Supabase PostgreSQL.

A tabela `public.pets` utiliza UUID como chave primária e contém:

```text
id
tenant_id
name
species
breed
gender
birth_date
weight
observations
photo_path
created_at
updated_at
deleted_at
```

A tabela `public.vaccines` também utiliza UUID como chave primária:

```text
id
tenant_id
pet_id
name
dose_description
application_date
next_dose_date
observations
created_at
updated_at
deleted_at
```

`vaccines.pet_id` referencia o UUID do Pet correspondente em `public.pets`.

As duas tabelas utilizam `tenant_id` para identificar a empresa proprietária do registro.

Row Level Security está habilitado para Pets e Vacinas.

As políticas remotas restringem o acesso aos registros do Tenant associado ao Profile do usuário autenticado.

A segurança remota não depende apenas dos filtros enviados pelo aplicativo.

O Android informa `tenant_id`, IDs remotos e demais filtros necessários, mas a autorização continua sendo responsabilidade do Supabase por meio de RLS.

### Observação de segurança sobre Vacinas

A política atual de Vacinas protege o `tenant_id` pelo Tenant autenticado, porém a validação do relacionamento entre `vaccines.pet_id` e o Tenant do Pet ainda precisa ser revisada antes de produção.

A condição de validação do Pet atualmente existente no banco não deve ser considerada suficiente para garantir, sozinha, que o Pet relacionado pertença ao mesmo Tenant da Vacina.

Essa revisão permanece como pendência de endurecimento da política RLS.

---

## Criação de Pets

A criação segue uma estratégia local-first.

```text
Cadastrar Pet
     ↓
salvar no Room
     ↓
tentar INSERT no Supabase
     ↓
Supabase retorna UUID
     ↓
salvar UUID em remoteId
```

Se a comunicação com o Supabase falhar, o Pet continua preservado no Room com:

```text
remoteId = NULL
```

Esses registros são identificados posteriormente para nova tentativa de sincronização.

---

## Sincronização de Pets novos

Pets que ainda não possuem identidade remota são localizados através da condição:

```text
remoteId = NULL
```

Ao sincronizar:

```text
Room
remoteId = NULL
      ↓
INSERT Supabase
      ↓
UUID remoto
      ↓
Room.remoteId = UUID
```

Esse fluxo foi validado com registros antigos já existentes no Room.

---

## Download de Pets remotos

Pets existentes no Supabase e ainda inexistentes no Room são importados utilizando o `remoteId`.

Antes de inserir um registro remoto localmente, o aplicativo verifica se já existe uma entidade com o mesmo:

```text
tenantId + remoteId
```

Isso evita duplicações durante sincronizações repetidas em condições normais.

O comportamento foi validado criando Pets diretamente no Supabase e sincronizando-os para o dispositivo.

---

## Edição de Pets

Quando um Pet já possui `remoteId`, uma edição realizada no aplicativo atualiza primeiro o Room e depois tenta atualizar o Supabase.

O UPDATE remoto utiliza simultaneamente:

```text
id = remoteId
tenant_id = Tenant autenticado
```

Além disso, o Supabase aplica RLS.

Isso fornece proteção tanto no aplicativo quanto no banco de dados.

---

## Edição offline de Pets

Para suportar edições realizadas sem conexão, `PetEntity` possui:

```text
pendingSync
```

O fluxo é:

```text
usuário edita
      ↓
Room atualiza
      ↓
pendingSync = true
      ↓
tenta atualizar Supabase
```

Se o Supabase confirmar a atualização:

```text
pendingSync = false
```

Se estiver offline ou ocorrer falha:

```text
pendingSync = true
```

permanece no Room.

Quando uma sincronização posterior é executada, registros com:

```text
pendingSync = true
remoteId != NULL
```

são reenviados ao Supabase.

Após confirmação remota, a flag é removida.

Esse fluxo foi validado desligando a conexão, alterando um Pet, restabelecendo a internet e confirmando a atualização no Supabase.

---

## Exclusão de Pets

A exclusão utiliza soft delete no backend.

A tabela `public.pets` possui:

```text
deleted_at
```

Um Pet excluído remotamente não é removido fisicamente do Supabase.

Em vez disso:

```text
deleted_at = timestamp
```

Isso permite sincronizar corretamente exclusões entre dispositivos.

---

## Exclusão offline de Pets

`PetEntity` possui:

```text
pendingDelete
```

Quando um Pet já sincronizado é excluído sem conexão:

```text
usuário exclui
      ↓
pendingDelete = true
      ↓
Pet deixa de aparecer na interface
      ↓
registro continua no Room
```

A permanência temporária no Room é necessária porque o aplicativo ainda precisa preservar o `remoteId` para sincronizar a exclusão posteriormente.

Quando a conexão volta:

```text
pendingDelete = true
      ↓
UPDATE Supabase
deleted_at = timestamp
      ↓
Supabase confirma
      ↓
registro é removido fisicamente do Room
```

A exclusão física local continua respeitando as Foreign Keys existentes.

Vacinas relacionadas ao Pet são removidas localmente por `ON DELETE CASCADE`.

---

## Exclusões e atualizações remotas de Pets

Durante a sincronização, registros remotos com:

```text
deleted_at != null
```

são tratados como excluídos.

Caso exista um Pet local com o mesmo `remoteId`, ele é removido do Room.

Pets ativos recebidos do Supabase podem atualizar registros já existentes no Room quando o registro local não possui alterações pendentes.

A atualização remota é aplicada somente quando o registro local não possui:

```text
pendingSync = true
```

nem:

```text
pendingDelete = true
```

A regra atual para Pets é:

```text
alteração local pendente
→ prioridade local

nenhuma alteração local pendente
→ remoto pode atualizar o Room
```

Essa é uma estratégia inicial de resolução de conflitos.

Uma estratégia baseada em timestamps ou versões poderá ser adotada futuramente para cenários concorrentes mais complexos.

---

## Sincronização de Vacinas

Vacinas utilizam a mesma estratégia local-first adotada para Pets.

### Criação de Vacinas

Ao cadastrar uma Vacina:

```text
usuário cadastra Vacina
        ↓
salvar no Room
        ↓
localizar Pet local
        ↓
obter Pet.remoteId
        ↓
INSERT public.vaccines
        ↓
Supabase retorna UUID
        ↓
salvar UUID em VaccineEntity.remoteId
```

Se não houver conexão ou se o Pet ainda não possuir `remoteId`, a Vacina permanece preservada no Room.

Registros com:

```text
remoteId = NULL
```

são processados novamente em uma sincronização posterior.

### Download de Vacinas remotas

Vacinas existentes no Supabase e inexistentes no Room são importadas utilizando o `remoteId`.

O `pet_id` remoto é utilizado para localizar o Pet local através de `PetEntity.remoteId`.

Somente depois desse relacionamento ser resolvido a Vacina é inserida no Room.

```text
Supabase vaccines.pet_id
        ↓
PetEntity.remoteId
        ↓
PetEntity.id local
        ↓
VaccineEntity.petId
```

### Edição de Vacinas

Quando uma Vacina possui `remoteId`, alterações realizadas no aplicativo são persistidas primeiro no Room.

O registro é marcado com:

```text
pendingSync = true
```

Em seguida o aplicativo tenta executar UPDATE no Supabase.

Quando o backend confirma a atualização:

```text
pendingSync = false
```

Se estiver offline, a edição permanece no Room e será reenviada posteriormente.

Esse fluxo foi validado realizando uma edição sem conexão, verificando `pendingSync = true`, restabelecendo a internet e confirmando a atualização no Supabase e o retorno de `pendingSync = false`.

Vacinas ainda sem `remoteId` não precisam de uma atualização remota separada: quando forem finalmente criadas no Supabase, o estado local mais recente será utilizado.

### Exclusão de Vacinas

Vacinas utilizam soft delete remoto.

Uma Vacina sincronizada excluída pelo usuário recebe localmente:

```text
pendingDelete = true
```

e deixa imediatamente de aparecer nas consultas normais da interface.

Enquanto não houver confirmação do Supabase, o registro continua fisicamente no Room para preservar o `remoteId`.

Quando a conexão está disponível:

```text
pendingDelete = true
        ↓
UPDATE Supabase
        ↓
deleted_at = timestamp
        ↓
Supabase confirma
        ↓
registro removido fisicamente do Room
```

Vacinas que ainda não possuem `remoteId` podem ser removidas diretamente do Room.

O fluxo de exclusão offline foi validado desligando a conexão, verificando `pendingDelete = true`, confirmando que `deleted_at` ainda estava nulo no Supabase, restabelecendo a internet e confirmando o preenchimento de `deleted_at` e a remoção física do registro local.

### Alterações realizadas remotamente em Vacinas

Vacinas ativas recebidas do Supabase podem atualizar registros já existentes no Room.

A atualização remota de uma Vacina ativa somente é aplicada quando a entidade local não possui:

```text
pendingSync = true
```

nem:

```text
pendingDelete = true
```

Assim, uma edição local ativa ainda não sincronizada não é sobrescrita por uma atualização remota ativa.

Vacinas recebidas com:

```text
deleted_at != null
```

são consideradas excluídas e removidas do Room quando existe um registro local correspondente ao mesmo `remoteId`.

Foram validados os fluxos de:

```text
Supabase → criação local
Supabase → atualização local
Supabase → exclusão local
```

### Regra atual em conflito entre edição local e exclusão remota

Existe uma diferença importante no tratamento atual de Vacinas:

```text
edição local pendente + atualização remota ativa
→ alteração local é preservada

edição local pendente + exclusão remota
→ exclusão remota remove o registro local
```

Portanto, no conflito específico entre uma edição offline local e um soft delete remoto do mesmo registro, a exclusão remota atualmente prevalece.

Essa decisão ainda deve ser formalizada ou evoluída em uma estratégia de conflitos mais robusta.

---

## updated_at

O Supabase utiliza `updated_at` para registrar o instante da última alteração remota em Pets e Vacinas.

Triggers PostgreSQL executam automaticamente:

```text
updated_at = now()
```

antes dos UPDATEs nas tabelas correspondentes.

Os timestamps são armazenados em UTC.

Quando datas e horários precisarem ser apresentados ao usuário, a camada de apresentação poderá convertê-los para o fuso apropriado.

Datas de Vacinas como `application_date` e `next_dose_date` são tratadas como datas sem horário. No Android, a conversão utiliza UTC para evitar deslocamentos de dia causados pelo fuso local.

O banco continua utilizando UTC como referência para timestamps de sincronização.

---

## Ordem atual de sincronização

Pets e Vacinas seguem a mesma ordem conceitual:

```text
1. Enviar registros novos
   remoteId = NULL

2. Enviar edições pendentes
   pendingSync = true

3. Enviar exclusões pendentes
   pendingDelete = true

4. Buscar dados remotos
```

Essa ordem processa primeiro o estado local pendente antes de aplicar dados vindos do backend.

No caso de Vacinas existe também dependência do Pet relacionado: uma Vacina nova somente pode ser enviada depois que o Pet possui `remoteId`.

---

## Sincronização ao retornar para as telas

A sincronização não deve depender apenas da inicialização dos ViewModels.

A Home solicita nova sincronização ao entrar novamente na composição:

```text
Home exibida
→ onSync()
→ HomeViewModel.syncPets()
```

A tela de Vacinas utiliza o mesmo princípio:

```text
VaccinesScreen exibida
→ LaunchedEffect(Unit)
→ onSync()
→ VaccinesViewModel.syncVaccines()
```

Isso permite receber alterações remotas e reenviar operações offline pendentes quando a rota é novamente composta, sem exigir que o usuário encerre completamente o aplicativo.

Uma evolução futura poderá utilizar observação explícita de lifecycle/resume para tornar a sincronização ainda menos dependente da recriação da composição.

---

## Isolamento entre Tenants validado

A arquitetura foi testada com dois Tenants reais:

```text
PetLife

Clínica Bicho Feliz
```

Pets pertencentes a um Tenant permanecem isolados dos Pets pertencentes ao outro Tenant tanto no Room quanto no Supabase.

Vacinas também possuem `tenantId` local, `tenant_id` remoto e políticas RLS próprias.

A sincronização remota completa de Vacinas foi validada no Tenant PetLife, incluindo criação, atualização, exclusão, operações offline e alterações realizadas diretamente no Supabase.

A validação equivalente do ciclo completo de Vacinas utilizando também a Clínica Bicho Feliz permanece como teste adicional antes de considerar esse cenário totalmente validado entre múltiplos Tenants.

---

## Estratégia offline atual

O PetLife segue uma estratégia local-first para Pets e Vacinas.

O Room é utilizado como fonte local para apresentação dos dados e funcionamento offline.

O Supabase atua como backend remoto compartilhado entre dispositivos e camada de segurança através de RLS.

Fluxos atualmente suportados:

```text
criação local → remoto
remoto → local
edição online
edição offline
exclusão online
exclusão offline
exclusão remota → local
edição remota → local
```

Os estados principais utilizados pela sincronização são:

```text
remoteId = NULL
→ registro ainda não criado remotamente

pendingSync = true
→ edição aguardando sincronização

pendingDelete = true
→ exclusão aguardando confirmação remota
```

O login inicial ainda depende de conexão com o Supabase.

Depois de autenticado e com o Tenant resolvido, o aplicativo consegue continuar utilizando os dados locais disponíveis no Room durante períodos sem conexão.

---

## Limitações atuais

A sincronização ainda possui limitações conhecidas.

### Fotos

`photoUri` representa atualmente um recurso local do dispositivo.

O envio de imagens para Supabase Storage ainda não foi implementado.

Por isso:

```text
photoUri
→ local

photo_path
→ ainda não sincronizado
```

Durante atualizações de Pets vindas do Supabase, a foto local existente é preservada.

### Idempotência de criação

O fluxo atual utiliza criação local seguida de INSERT remoto e posterior armazenamento do UUID retornado pelo Supabase.

Existe uma pequena janela entre:

```text
INSERT remoto concluído
        ↓
remoteId ainda não salvo no Room
```

Se o processo do aplicativo for encerrado exatamente nesse intervalo, uma tentativa posterior pode criar um registro remoto duplicado.

Essa limitação existe nos fluxos local-first que dependem do UUID retornado pelo backend, incluindo Pets e Vacinas.

Uma evolução futura poderá utilizar UUID gerado pelo cliente, uma chave de idempotência ou outro mecanismo equivalente.

### Conflitos entre dispositivos

A estratégia atual protege alterações locais pendentes em vários fluxos, porém ainda não implementa resolução avançada de conflitos baseada em versões ou timestamps locais/remotos.

Uma evolução futura poderá utilizar:

```text
updated_at remoto
updatedAt local
lastSyncedAt
versão do registro
```

para determinar qual alteração deve prevalecer.

O conflito entre edição local de Vacina e exclusão remota também deverá ser explicitamente definido nessa estratégia.

### Autenticação offline

Atualmente o login inicial depende da conexão com o Supabase.

Depois de autenticado, o usuário pode utilizar funcionalidades locais enquanto estiver sem internet.

Uma evolução futura poderá permitir reabrir o aplicativo offline utilizando uma sessão previamente autenticada e um Tenant previamente resolvido e armazenado localmente.

Nenhuma senha deverá ser armazenada localmente para esse objetivo.

### Política RLS do relacionamento Vacina–Pet

Embora `tenant_id` de Vacinas esteja protegido pelo Tenant autenticado, a política que valida o Pet relacionado deve ser revisada para garantir explicitamente que:

```text
vaccines.tenant_id = pets.tenant_id
```

ou regra equivalente seja aplicada corretamente.

Essa revisão é necessária antes de considerar a política pronta para produção.

### Data obrigatória de aplicação da Vacina

No backend, `application_date` é obrigatória.

A interface atual ainda deve ser alinhada para tornar essa obrigatoriedade explícita também na validação do formulário, evitando que um registro local sem data só falhe no momento da sincronização remota.

---

## Consequências

### Positivas

- isolamento de dados por empresa;
- proteção adicional com Supabase RLS;
- preservação de dados existentes durante migrations;
- suporte a funcionamento offline;
- sincronização bidirecional de Pets e Vacinas;
- suporte a soft delete;
- suporte a alterações realizadas em outros dispositivos;
- menor acoplamento entre UI e backend;
- base arquitetural reutilizável para outras entidades;
- arquitetura mais compatível com um SaaS Multi-Tenant real.

### Negativas

- aumento da complexidade dos repositórios;
- necessidade de controle de estado de sincronização;
- necessidade de migrations adicionais no Room;
- necessidade futura de estratégia mais robusta para conflitos;
- necessidade de revisar idempotência na criação;
- necessidade de endurecer regras de segurança antes de produção;
- dependência atual de conexão para o login inicial.

---

## Resultado

A arquitetura atual permite que Pets e Vacinas sejam armazenados e manipulados localmente por Tenant e sincronizados com o Supabase.

Os dois módulos suportam criação, edição e exclusão seguindo estratégia local-first, incluindo operações realizadas temporariamente sem conexão.

Estados locais pendentes são preservados até confirmação remota, enquanto alterações realizadas no Supabase podem ser refletidas no Room conforme as regras atuais de conflito.

O aplicativo mantém separação entre empresas tanto no banco local quanto no backend remoto através de identificação explícita de Tenant e Row Level Security.

O PetLife passa a possuir uma base reutilizável de sincronização offline-first que poderá ser aplicada aos próximos módulos de negócio, como Consultas, Medicamentos e Histórico de Peso.

A resolução avançada de conflitos, a idempotência de criação, a revisão da política RLS de Vacinas, a validação obrigatória da data de aplicação, a sincronização de fotos e a restauração completa da sessão para abertura totalmente offline permanecem como evoluções futuras.