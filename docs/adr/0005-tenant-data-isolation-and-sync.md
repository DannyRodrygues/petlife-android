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
- suporte gradual a cenários offline;
- proteção contra alterações entre Tenants diferentes.

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

Os repositórios de domínio são instanciados vinculados ao Tenant autenticado.

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
consultas filtradas pelo Tenant
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

---

## Migração dos dados existentes

A introdução do Multi-Tenant no Room foi realizada por migrations explícitas.

Os registros existentes foram preservados.

Pets existentes antes da implementação Multi-Tenant foram associados ao Tenant PetLife, mantendo os dados históricos já cadastrados.

As Vacinas existentes receberam o Tenant correspondente ao Pet relacionado.

A estratégia evitou o uso de migração destrutiva e preservou dados já existentes no dispositivo.

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

        ↕

Supabase
id = a462652b-...
```

Essa relação é utilizada para criação, edição, exclusão e sincronização.

---

## Persistência remota

A tabela remota `public.pets` utiliza UUID como chave primária e possui:

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

O campo `tenant_id` referencia a tabela de Tenants.

O Supabase possui Row Level Security habilitado para `public.pets`.

As políticas de SELECT, INSERT, UPDATE e DELETE restringem o acesso ao Tenant associado ao Profile do usuário autenticado.

A segurança remota não depende apenas dos filtros enviados pelo aplicativo.

O aplicativo envia o `tenant_id`, mas o Supabase continua responsável pela validação de autorização por meio de RLS.

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

Esse fluxo foi validado com registros antigos já existentes no Room nos Tenants PetLife e Clínica Bicho Feliz.

---

## Download de Pets remotos

Pets existentes no Supabase e ainda inexistentes no Room são importados utilizando o `remoteId`.

Antes de inserir um registro remoto localmente, o aplicativo verifica se já existe uma entidade com o mesmo:

```text
tenantId + remoteId
```

Isso evita duplicações durante sincronizações repetidas.

O comportamento foi validado criando Pets diretamente no Supabase e sincronizando-os para o dispositivo.

---

## Edição de Pets

Quando um Pet já possui `remoteId`, uma edição realizada no aplicativo atualiza:

```text
Room
   ↓
Supabase
```

O UPDATE remoto utiliza simultaneamente:

```text
id = remoteId
tenant_id = Tenant autenticado
```

Além disso, o Supabase aplica RLS.

Isso fornece proteção tanto no aplicativo quanto no banco de dados.

---

## Edição offline

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

## Exclusão offline

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

## Exclusões realizadas remotamente

Durante a sincronização, registros remotos com:

```text
deleted_at != null
```

são tratados como excluídos.

Caso exista um Pet local com o mesmo `remoteId`, ele é removido do Room.

Assim, uma exclusão feita diretamente no Supabase ou futuramente em outro dispositivo também é refletida no dispositivo atual.

Esse comportamento foi validado realizando soft delete diretamente no Supabase e sincronizando novamente o aplicativo.

---

## Atualizações realizadas remotamente

Pets ativos recebidos do Supabase podem atualizar registros já existentes no Room.

A atualização remota é aplicada somente quando o registro local não possui:

```text
pendingSync = true
```

nem:

```text
pendingDelete = true
```

Isso evita que uma edição local ainda não sincronizada seja sobrescrita por dados remotos.

A regra atual é:

```text
alteração local pendente
→ prioridade local

nenhuma alteração local pendente
→ remoto pode atualizar o Room
```

Essa é a estratégia inicial de resolução de conflitos.

Uma estratégia baseada em timestamps poderá ser adotada futuramente para cenários concorrentes mais complexos.

---

## updated_at

O Supabase utiliza `updated_at` para registrar o instante da última alteração remota.

Foi criado um trigger PostgreSQL responsável por executar automaticamente:

```text
updated_at = now()
```

antes de cada UPDATE na tabela `public.pets`.

Os timestamps são armazenados em UTC.

Quando datas e horários precisarem ser exibidos ao usuário, o aplicativo deverá convertê-los para o fuso apropriado, como:

```text
America/Sao_Paulo
```

O banco continua utilizando UTC como referência para sincronização.

---

## Ordem atual de sincronização

Ao sincronizar Pets, a ordem adotada é:

```text
1. Enviar Pets novos
   remoteId = NULL

2. Enviar edições pendentes
   pendingSync = true

3. Enviar exclusões pendentes
   pendingDelete = true

4. Buscar dados remotos
```

Essa ordem evita buscar dados remotos antes de processar alterações locais ainda pendentes.

---

## Sincronização ao retornar para a Home

A sincronização não deve ocorrer apenas na inicialização do `HomeViewModel`.

A Home solicita nova sincronização quando volta a entrar na composição.

O fluxo atual é:

```text
Home exibida
→ onSync()
→ HomeViewModel.syncPets()
```

Isso permite que alterações ou exclusões realizadas remotamente sejam refletidas no aplicativo sem exigir que o usuário encerre e abra novamente o app.

---

## Isolamento entre Tenants validado

Os fluxos foram testados com dois Tenants reais:

```text
PetLife

Clínica Bicho Feliz
```

Pets pertencentes a um Tenant permanecem isolados dos Pets pertencentes ao outro Tenant tanto no Room quanto no Supabase.

O mesmo princípio de isolamento local já foi aplicado às Vacinas.

---

## Estratégia offline atual

O PetLife segue uma estratégia local-first para Pets.

O Room é utilizado como fonte local para apresentação dos dados e funcionamento offline.

O Supabase atua como backend remoto, fonte compartilhada entre dispositivos e camada de segurança por meio de RLS.

Fluxos atualmente suportados para Pets:

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

Durante atualizações vindas do Supabase, a foto local existente é preservada.

---

### Vacinas

Vacinas já possuem isolamento por Tenant no Room.

Entretanto, a sincronização completa Room ↔ Supabase para Vacinas ainda não foi implementada.

A estratégia utilizada para Pets deverá servir como referência para essa implementação.

---

### Conflitos entre dispositivos

A estratégia atual protege alterações locais pendentes, porém ainda não implementa resolução avançada de conflitos baseada em versões ou timestamps locais/remotos.

Em uma evolução futura poderão ser utilizados:

```text
updated_at remoto
updatedAt local
lastSyncedAt
versão do registro
```

para determinar qual alteração deve prevalecer.

---

### Autenticação offline

Atualmente o login inicial depende da conexão com o Supabase.

Depois de autenticado, o usuário pode utilizar funcionalidades locais enquanto estiver sem internet.

Uma evolução futura poderá permitir reabrir o aplicativo offline utilizando uma sessão previamente autenticada e um Tenant previamente resolvido e armazenado localmente.

Nenhuma senha deverá ser armazenada localmente para esse objetivo.

---

## Consequências

### Positivas

- isolamento de dados por empresa;
- proteção adicional com Supabase RLS;
- preservação de dados existentes durante migrations;
- suporte a funcionamento offline;
- sincronização bidirecional de Pets;
- suporte a soft delete;
- suporte a alterações realizadas em outros dispositivos;
- menor acoplamento entre UI e backend;
- base arquitetural reutilizável para outras entidades;
- arquitetura mais compatível com um SaaS Multi-Tenant real.

### Negativas

- aumento da complexidade do repositório;
- necessidade de controle de estado de sincronização;
- necessidade de migrations adicionais no Room;
- necessidade futura de estratégia mais robusta para conflitos;
- necessidade de implementar sincronização equivalente para outras entidades.

---

## Resultado

A arquitetura atual permite que Pets sejam armazenados e manipulados localmente por Tenant e sincronizados com segurança com o Supabase.

O aplicativo mantém separação entre empresas tanto no banco local quanto no backend remoto.

O PetLife passa a possuir uma base funcional para operação offline e sincronização entre múltiplos dispositivos.

A mesma estratégia poderá ser progressivamente aplicada a Vacinas e demais entidades de negócio.