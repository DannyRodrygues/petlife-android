# Changelog

Todas as alterações relevantes do **PetLife** serão documentadas neste arquivo.

O projeto encontra-se em desenvolvimento ativo. Enquanto não houver uma primeira versão estável publicada, as alterações permanecem agrupadas em **[Não publicado]**.

---

## [Não publicado]

### ✨ Adicionado

#### Fundação do projeto

- Projeto Android inicial desenvolvido em Kotlin.
- Interface construída com Jetpack Compose.
- Configuração do Git e integração com GitHub.
- Criação do repositório do projeto.
- Configuração para execução em dispositivo Android físico.
- Estrutura inicial organizada por funcionalidades.
- Organização baseada em Feature Packages.
- Componente raiz da aplicação com `PetLifeApp`.
- Configuração do Kotlin Serialization.
- Navegação tipada utilizando Navigation Compose.
- Recursos de texto centralizados em `strings.xml`.

#### Design System e identidade visual

- Definição da identidade visual inicial do PetLife.
- Paleta oficial baseada em roxo, lilás e azul Tiffany.
- Tema personalizado para Light Theme e Dark Theme.
- Tokens reutilizáveis de espaçamento.
- Formas personalizadas.
- Componentes reutilizáveis de botão.
- Documentação inicial do Design System.
- Diretrizes para utilização de imagens.
- Especificação inicial da logomarca.
- Logomarca aplicada às principais telas de autenticação.
- Banner mobile desenvolvido para a Home.
- Componentes visuais reutilizáveis para formulários.

#### Autenticação e navegação inicial

- Tela de boas-vindas (`WelcomeScreen`).
- Tela de login.
- Campos de e-mail e senha.
- Tela de cadastro de usuário.
- Campos de nome, e-mail, senha e confirmação de senha.
- Tela de recuperação de senha.
- Navegação entre Welcome, Login, Cadastro e Recuperação de Senha.
- Links para criação de conta e recuperação de senha.
- Suporte a rolagem vertical nas telas de autenticação.
- Ajuste das telas ao teclado virtual.

#### Home

- Tela Home inicial.
- Banner visual na Home.
- Estado vazio para ausência de pets cadastrados.
- Botão para cadastro de Pet.
- Listagem dinâmica dos pets cadastrados.
- Cards clicáveis para abertura dos detalhes do Pet.
- Sincronização solicitada novamente ao retornar para a Home.

#### Pets

- Tela de cadastro de Pets.
- Formulário estruturado para informações do Pet.
- Seleção de espécie.
- Seleção de sexo.
- Date Picker para data de nascimento.
- Photo Picker para seleção de imagem da galeria.
- Pré-visualização circular da foto utilizando Coil.
- Validação dos campos obrigatórios.
- Persistência local utilizando Room.
- `PetDao`.
- `PetRepository`.
- `AddPetViewModel`.
- `HomeViewModel`.
- Persistência do acesso à foto selecionada após reiniciar o aplicativo.
- Tela de detalhes do Pet.
- `PetDetailsViewModel`.
- Tela de edição dos dados do Pet.
- `EditPetViewModel`.
- Persistência das alterações realizadas no Pet.
- Exclusão de Pet com confirmação.
- Exclusão local em cascata dos dados relacionados.
- Identificação local através de `id`.
- Identificação remota através de `remoteId`.
- Estado de sincronização através de `pendingSync`.
- Estado de exclusão pendente através de `pendingDelete`.

#### Vacinas

- Módulo de Vacinas associado ao Pet.
- Cadastro de vacinas.
- Histórico de vacinas aplicadas.
- Exibição da próxima dose.
- Controle de próximas doses.
- Relacionamento entre Pets e Vacinas através de `petId`.
- `VaccineRepository`.
- ViewModels específicos para Vacinas.
- Persistência local das vacinas utilizando Room.
- Persistência remota das vacinas utilizando Supabase PostgreSQL.
- Evolução do banco através de migrations preservando dados existentes.
- Inclusão de `remoteId` em `VaccineEntity`.
- Inclusão de `pendingSync` em `VaccineEntity`.
- Inclusão de `pendingDelete` em `VaccineEntity`.
- Índice único local para `remoteId`.
- Isolamento de Vacinas por Tenant.
- Associação automática da Vacina ao Tenant autenticado.
- Associação remota da Vacina ao UUID do respectivo Pet.
- `VaccineRemoteDataSource`.
- DTOs remotos para criação, atualização, leitura e soft delete.
- Mapper entre `VaccineEntity` e DTOs remotos.
- Conversão de datas entre o formato local e ISO `yyyy-MM-dd`.
- Swipe horizontal nos cards do histórico.
- Ações de edição e exclusão reveladas ao arrastar o card.
- Tooltip orientando o usuário a arrastar o card para editar ou excluir.
- Edição de Vacinas através de `ModalBottomSheet`.
- Formulário de edição preenchido automaticamente com os dados atuais.
- Confirmação antes da exclusão.
- Data de aplicação passou a ser obrigatória no cadastro de Vacinas.
- Data de aplicação passou a ser obrigatória também na edição de Vacinas.
- Feedback visual exibido quando a data de aplicação obrigatória não é informada.

#### Arquitetura SaaS Multi-Tenant

- Definição do PetLife como aplicação SaaS Multi-Tenant.
- Criação do modelo `TenantConfig`.
- Criação do modelo `BrandConfig`.
- Criação do `PetLifeDefaultTenant`.
- Criação de Tenant adicional para validação da infraestrutura.
- Implementação do `TenantProvider`.
- Disponibilização do Tenant ativo através de `LocalTenantConfig`.
- Suporte a tema dinâmico baseado no `BrandConfig`.
- Conversão de cores HEX remotas para cores do Jetpack Compose.
- Fallback local utilizando a identidade visual padrão do PetLife.
- Estrutura preparada para múltiplas empresas utilizando o mesmo aplicativo.
- Separação entre Tenant visual de fallback e Tenant autenticado efetivamente resolvido.
- `resolvedTenantId` utilizado para acesso aos dados de negócio.

#### Supabase

- Integração do projeto Android com Supabase.
- Configuração segura da Project URL e Publishable Key através de `local.properties`.
- Criação do `SupabaseProvider`.
- Integração com Supabase PostgREST.
- Integração com Supabase Auth.
- Integração com Supabase Storage.
- Integração com Ktor Client Android.
- Criação da tabela `tenants`.
- Criação da tabela `brand_configs`.
- Criação da tabela `profiles`.
- Criação da tabela remota `public.pets`.
- Criação da tabela remota `public.vaccines`.
- Relacionamento remoto entre Vacinas e Pets através de `pet_id`.
- Índices remotos para `tenant_id`, `pet_id` e combinação Tenant/Pet.
- Políticas RLS de SELECT, INSERT, UPDATE e DELETE para Vacinas.
- Validação do Pet pertencente ao mesmo Tenant durante criação e atualização de Vacinas.
- Trigger PostgreSQL para atualização automática de `updated_at` em Vacinas.
- Configuração de Row Level Security nas tabelas remotas.
- Políticas RLS de SELECT, INSERT, UPDATE e DELETE para Pets.
- Criação do bucket público `tenant-branding`.
- Organização dos arquivos de branding por UUID do Tenant.
- Armazenamento remoto de logomarca e banner por empresa.
- Criação de DTOs para Tenant e BrandConfig.
- Criação de DTOs remotos para Pets.
- Criação de DTOs remotos para Vacinas.
- Criação de mappers entre entidades locais e DTOs remotos.
- Criação do `TenantRemoteDataSource`.
- Criação do `PetRemoteDataSource`.
- Criação do `VaccineRemoteDataSource`.

#### Branding remoto

- Carregamento remoto das cores do Tenant.
- Alteração da identidade visual sem necessidade de recompilar o aplicativo.
- Carregamento remoto da logomarca através do Supabase Storage.
- Carregamento remoto do banner através do Supabase Storage.
- Criação do `BrandAssetUrlProvider`.
- Criação do componente reutilizável `PetLifeBrandLogo`.
- Criação do componente reutilizável `PetLifeBrandBanner`.
- Aplicação do banner dinâmico nas telas que anteriormente utilizavam o banner local fixo.
- Fallback para `R.drawable.logo_petlife` quando não existir logomarca remota.
- Fallback para `R.drawable.home_banner` quando não existir banner remoto.
- Nome da empresa exibido dinamicamente na Home.
- Elementos gráficos configurados para acompanhar as cores do Tenant.

#### Supabase Auth e resolução do Tenant

- Login real por e-mail e senha utilizando Supabase Auth.
- Criação do `AuthRemoteDataSource`.
- Criação do `AuthRepository`.
- Criação do `LoginViewModel`.
- Estado de interface através de `LoginUiState`.
- Validação de campos vazios no login.
- Tratamento de credenciais inválidas.
- Feedback visual durante autenticação.
- Associação entre `profiles.id` e `auth.users.id`.
- Associação de usuários a empresas através de `profiles.tenant_id`.
- Campo `role` preparado para futura implementação de permissões.
- Campo `active` para controle de usuários ativos.
- Row Level Security na tabela `profiles`.
- Policy permitindo que o usuário autenticado consulte apenas o próprio Profile.
- Criação do `ProfileDto`.
- Criação do `ProfileRemoteDataSource`.
- Busca do Tenant através de UUID.
- Criação de `getTenantById()` na camada remota.
- Criação de `getTenantById()` no `TenantRepository`.
- Criação do `CurrentTenantRepository`.
- Resolução automática do Tenant através do usuário autenticado.
- Atualização do Tenant ativo após login através de `refreshCurrentTenant()`.
- Utilização da mesma instância de `TenantViewModel` criada no `PetLifeApp`.

#### Isolamento de dados por Tenant

- Inclusão de `tenantId` em `PetEntity`.
- Inclusão de `tenantId` em `VaccineEntity`.
- Consultas do Room filtradas pelo Tenant autenticado.
- Repositórios vinculados ao Tenant resolvido.
- Validação de Tenant em operações de alteração e exclusão.
- Migrations Room preservando Pets existentes.
- Migrations Room preservando Vacinas existentes.
- Associação dos Pets históricos ao Tenant PetLife.
- Associação das Vacinas históricas ao Tenant dos respectivos Pets.
- Índices locais para consultas por Tenant.
- Validação do isolamento local entre PetLife e Clínica Bicho Feliz.
- Validação do isolamento remoto de Pets através de RLS.

#### Sincronização Room ↔ Supabase para Pets

- Persistência remota de Pets no Supabase.
- Criação local-first de Pets.
- Envio de Pets novos para o Supabase.
- Armazenamento do UUID remoto em `remoteId`.
- Sincronização de Pets antigos que já existiam no Room.
- Importação para o Room de Pets criados diretamente no Supabase.
- Prevenção de duplicação através de `remoteId`.
- Atualização remota de Pets editados no aplicativo.
- Atualização local de Pets alterados no Supabase.
- Conversão de data entre `dd/MM/yyyy` no aplicativo e `yyyy-MM-dd` no backend.
- Proteção por `tenantId` durante UPDATE remoto.
- Sincronização disparada novamente ao retornar para a Home.

#### Edição offline de Pets

- Inclusão da flag `pendingSync` no Room.
- Migration Room para inclusão de `pendingSync`.
- Marcação explícita de alterações pendentes.
- Persistência local de edições mesmo sem conexão.
- Busca de Pets com edição pendente.
- Reenvio automático das alterações após retorno da conexão.
- Limpeza de `pendingSync` apenas após confirmação do Supabase.
- Proteção contra sobrescrita remota quando existir alteração local pendente.
- Validação real do fluxo desligando e restabelecendo a conexão.

#### Exclusão e soft delete de Pets

- Inclusão da flag `pendingDelete` no Room.
- Migration Room para inclusão de `pendingDelete`.
- Pets pendentes de exclusão deixam de aparecer na Home.
- Preservação temporária do registro local para manter o `remoteId`.
- Implementação de soft delete remoto através de `deleted_at`.
- Reenvio de exclusões pendentes após retorno da conexão.
- Remoção física do Room somente após confirmação do Supabase.
- Remoção local de Pets excluídos remotamente.
- Exclusão em cascata das Vacinas locais relacionadas ao Pet.
- Validação real de exclusão offline e posterior sincronização.

#### Sincronização Room ↔ Supabase para Vacinas

- Persistência remota de Vacinas no Supabase.
- Criação local-first de Vacinas.
- Envio de Vacinas novas para o Supabase.
- Armazenamento do UUID remoto em `remoteId`.
- Sincronização de Vacinas antigas que já existiam no Room.
- Associação remota da Vacina utilizando o `remoteId` do Pet.
- Importação para o Room de Vacinas criadas diretamente no Supabase.
- Prevenção de duplicação através de `remoteId`.
- Atualização remota de Vacinas editadas no aplicativo.
- Atualização local de Vacinas alteradas diretamente no Supabase.
- Conversão de datas utilizando UTC para preservar corretamente datas sem horário.
- Proteção por `tenantId` durante operações remotas.
- Sincronização solicitada ao entrar novamente na tela de Vacinas.
- Ordem de sincronização definida como:
    1. Vacinas novas;
    2. edições pendentes;
    3. exclusões pendentes;
    4. dados remotos.

#### Edição offline de Vacinas

- Utilização de `pendingSync` para alterações locais.
- Persistência imediata das edições no Room.
- Tentativa de UPDATE remoto após alteração local.
- Manutenção de `pendingSync = true` quando não houver conexão.
- Reenvio automático de edições pendentes após retorno da conexão.
- Limpeza de `pendingSync` apenas após confirmação do Supabase.
- Proteção contra sobrescrita remota enquanto existir edição local pendente.
- Validação real do fluxo de edição offline e sincronização posterior.

#### Exclusão e soft delete de Vacinas

- Utilização de `pendingDelete` para exclusões locais.
- Vacinas pendentes de exclusão deixam de aparecer na interface imediatamente.
- Vacinas sem `remoteId` são removidas diretamente do Room.
- Vacinas já sincronizadas utilizam soft delete remoto através de `deleted_at`.
- Preservação temporária do registro local enquanto a exclusão remota estiver pendente.
- Reenvio automático de exclusões pendentes após retorno da conexão.
- Remoção física do Room apenas após confirmação do Supabase.
- Remoção local de Vacinas excluídas diretamente no Supabase.
- Validação real de exclusão online.
- Validação real de exclusão offline seguida de sincronização posterior.

#### Controle de timestamps

- Uso de `created_at` e `updated_at` nas tabelas remotas de Pets e Vacinas.
- Criação de trigger PostgreSQL para atualizar `updated_at` automaticamente.
- Armazenamento dos timestamps em UTC.
- Definição de conversão para o fuso local apenas na camada de apresentação.
- Preparação da arquitetura para futura resolução de conflitos por timestamp.

#### Validação Multi-Tenant

- Criação de dois Tenants reais para validação da arquitetura.
- Configuração do Tenant PetLife.
- Configuração do Tenant Clínica Bicho Feliz.
- Associação de usuários diferentes a Tenants diferentes.
- Validação da troca automática de nome da empresa após autenticação.
- Validação da troca automática das cores.
- Validação da troca automática da logomarca.
- Validação da troca automática do banner.
- Validação do mesmo aplicativo utilizando identidades visuais diferentes sem alteração de código ou novo build.
- Validação de Pets isolados localmente por Tenant.
- Validação de Pets isolados remotamente por Tenant.
- Validação de criação, edição e exclusão sincronizadas nos dois Tenants.

#### Documentação

- Criação do `CHANGELOG.md`.
- Organização da documentação do projeto.
- Criação da estrutura `docs/adr/`.
- Criação da estrutura `docs/architecture/`.
- Criação da estrutura `docs/design/`.
- Criação do ADR 0003 sobre arquitetura SaaS Multi-Tenant.
- Criação do ADR 0004 sobre autenticação e resolução do Tenant.
- Criação do ADR 0005 sobre isolamento de dados por Tenant e estratégia de sincronização.
- Criação de diagramas da arquitetura Multi-Tenant.
- Criação de diagramas de distribuição e administração.
- Criação de versões editáveis dos diagramas em Excalidraw.
- Atualização do README para refletir Supabase, autenticação, Multi-Tenant e sincronização offline-first.

---

### 🔄 Alterado

#### Interface e Design System

- Refinamento visual da tela de boas-vindas.
- Refinamento visual da tela de login.
- Refinamento das telas de autenticação.
- Ajuste de espaçamentos e hierarquia tipográfica.
- Padronização da identidade visual entre as telas.
- Remoção de elementos visuais duplicados.
- Atualização da cor secundária para o azul da identidade visual.
- Refatoração dos formulários para reutilização de componentes.
- Refatoração da interface de Vacinas utilizando componentes reutilizáveis.
- Cards do histórico de Vacinas passaram a suportar swipe horizontal.
- Ações de editar e excluir passaram a permanecer ocultas até o gesto de swipe.
- Adicionado tooltip explicando o gesto de arrastar para a esquerda.
- Edição de Vacinas passou a utilizar `ModalBottomSheet`.
- Exclusão de Vacinas passou a utilizar diálogo de confirmação com nome da Vacina e do Pet.
- Ícones e elementos gráficos passaram a utilizar `MaterialTheme.colorScheme.primary`.
- Botões principais tiveram largura e altura refinadas por contexto de tela.

#### Home e Pets

- Home passou a consumir Pets persistidos no Room.
- Cards da Home passaram a abrir os detalhes do Pet.
- Cadastro de Pet passou a utilizar `AddPetViewModel`.
- Tela de detalhes passou a utilizar `PetDetailsViewModel`.
- Tela de edição passou a utilizar `EditPetViewModel`.
- Dados da tela de detalhes passaram a ser atualizados após edição.
- Fluxo de imagens foi ajustado para preservar o acesso após reiniciar o aplicativo.
- Home passou a solicitar sincronização quando entra novamente na composição.
- Pets marcados com `pendingDelete` deixaram de aparecer nas consultas normais.
- `PetRepository` passou a coordenar persistência local e remota.

#### Arquitetura Multi-Tenant

- `PetLifeTheme` deixou de depender exclusivamente de cores estáticas.
- `PetLifeTheme` passou a receber o `BrandConfig` do Tenant ativo.
- `PetLifeApp` passou a observar o `TenantViewModel`.
- `TenantProvider` passou a distribuir a configuração do Tenant pela árvore do Compose.
- Nome fixo do PetLife na Home foi substituído pelo nome do Tenant atual.
- Ícone da patinha da Home passou a utilizar `MaterialTheme.colorScheme.primary`.
- Banner local fixo foi substituído por componente de branding dinâmico.
- Logomarca local fixa foi substituída por componente de branding dinâmico.
- Telas passaram a compartilhar os componentes `PetLifeBrandLogo` e `PetLifeBrandBanner`.
- Acesso a dados de negócio deixou de utilizar o Tenant visual de fallback.
- `resolvedTenantId` passou a controlar os repositórios e rotas de dados.

#### Autenticação e Tenant

- Login deixou de navegar diretamente para a Home sem autenticação.
- Login passou a autenticar realmente através do Supabase Auth.
- `TenantViewModel` deixou de buscar o Tenant pelo nome fixo `"PetLife"`.
- Tenant ativo passou a ser resolvido através de `profiles.tenant_id`.
- `TenantRemoteDataSource` passou a suportar busca por UUID.
- `TenantRepository` passou a carregar Tenant e BrandConfig através do UUID.
- `AppNavHost` passou a informar ao `PetLifeApp` quando um login foi concluído.
- `TenantViewModel` passou a atualizar o Tenant após uma autenticação bem-sucedida.
- Tema da aplicação passou a refletir automaticamente o Tenant do usuário autenticado.

#### Persistência local

- Banco Room evoluído através de migrations sem perda dos dados existentes.
- Pets passaram a possuir `tenantId`.
- Vacinas passaram a possuir `tenantId`.
- Pets passaram a possuir `remoteId`.
- Pets passaram a possuir `pendingSync`.
- Pets passaram a possuir `pendingDelete`.
- DAOs passaram a filtrar registros pelo Tenant autenticado.
- Consultas passaram a ocultar Pets com exclusão pendente.
- Vacinas passaram a possuir `remoteId`.
- Vacinas passaram a possuir `pendingSync`.
- Vacinas passaram a possuir `pendingDelete`.
- Banco Room evoluído para versão 8.
- Migration adicionada para preservar Vacinas existentes durante inclusão dos campos de sincronização.
- Consultas normais de Vacinas passaram a ocultar registros com `pendingDelete`.
- Índice único criado para `remoteId` das Vacinas.

#### Sincronização

- Criação de Pets e Vacinas passou a utilizar estratégia local-first.
- Edição de Pets e Vacinas passou a preservar alterações localmente antes do envio remoto.
- Exclusão passou de remoção exclusivamente local para soft delete sincronizado.
- Dados remotos passaram a atualizar registros locais já existentes.
- Exclusões remotas passaram a ser refletidas no Room.
- Alterações locais pendentes passaram a ter prioridade sobre atualizações remotas.
- Fluxos de sincronização de Pets e Vacinas passaram a seguir a ordem:

```text
1. Registros novos
2. Edições pendentes
3. Exclusões pendentes
4. Dados remotos
```

#### README

- README reorganizado para refletir o estado atual do projeto.
- Supabase consolidado como backend remoto.
- Arquitetura SaaS Multi-Tenant documentada.
- Persistência remota de Pets documentada como implementada.
- RLS para Pets documentado como implementado.
- Estratégia offline-first adicionada.
- Sincronização Room ↔ Supabase detalhada.
- Limitações atuais documentadas.
- Funcionalidades planejadas revisadas.
- Histórico do projeto atualizado.
- ADR 0005 adicionado à seção de documentação.

---

### 🐛 Corrigido

#### Ambiente

- Compatibilidade entre AndroidX, Android Gradle Plugin e `compileSdk`.
- Configuração necessária para execução do projeto em dispositivo Android físico.

#### Navegação

- Corrigido crash ao abrir a tela de detalhes do Pet.
- Rota `PetDetails` reposicionada corretamente no `NavHost`.
- Removida duplicação da rota de detalhes.
- Fluxo de navegação entre cadastro, detalhes e edição ajustado.

#### Pets

- Corrigido problema de perda da foto do Pet após fechar e abrir o aplicativo.
- Corrigido fluxo de edição dos dados do Pet.
- Persistência das alterações do Pet ajustada.
- Corrigido acesso a Pets de Tenant diferente no Room.
- Corrigida ausência do vínculo entre registro local e UUID remoto.
- Corrigido fluxo de sincronização de Pets antigos com `remoteId = NULL`.
- Corrigido estado `pendingSync` que não permanecia marcado durante falha de conexão.
- Corrigida exclusão offline para não perder o `remoteId` antes da sincronização.
- Corrigido comportamento de Pets excluídos remotamente permanecerem no Room.
- Corrigido comportamento de atualizações remotas não serem refletidas em Pets locais.
- Corrigida necessidade de reiniciar completamente o aplicativo para receber algumas alterações remotas.
- Corrigido `updated_at` remoto que não era alterado automaticamente após UPDATE.

#### Vacinas

- Corrigidos problemas visuais em textos longos dos cards de Vacinas.
- Ajustado layout do histórico de Vacinas.
- Corrigido relacionamento e carregamento das Vacinas associadas ao Pet.
- Corrigido isolamento de Vacinas por Tenant.
- Corrigida ausência de vínculo entre Vacinas locais e UUIDs remotos.
- Corrigida sincronização de Vacinas existentes antes da integração com Supabase.
- Corrigida atualização de Vacinas existentes quando alteradas remotamente.
- Corrigido comportamento de Vacinas excluídas remotamente permanecerem no Room.
- Corrigido fluxo de edição offline para manter `pendingSync`.
- Corrigido fluxo de exclusão offline para preservar `remoteId` até confirmação remota.
- Corrigida possibilidade de sobrescrever alteração local pendente durante sincronização remota.
- Ajustado disparo da sincronização ao entrar novamente na tela de Vacinas.
- Corrigida validação da data de aplicação para impedir salvamento sem `application_date`.
- Corrigida política RLS de INSERT de Vacinas para garantir que o Pet pertença ao mesmo Tenant.
- Corrigida política RLS de UPDATE de Vacinas para garantir que o Pet pertença ao mesmo Tenant.
- Validado cadastro de Vacina após endurecimento da RLS.
- Validada edição de Vacina após endurecimento da RLS.

#### Autenticação

- Corrigido estado de sucesso do login que poderia provocar navegação sem uma nova autenticação.
- Adicionado consumo do evento de sucesso através de `consumeLoginSuccess()`.
- Campos vazios passaram a impedir autenticação.
- Senhas incorretas passaram a impedir navegação para a Home.
- Mensagens de erro passaram a ser apresentadas corretamente.


#### Multi-Tenant

- Corrigido carregamento do Tenant após autenticação utilizando `refreshCurrentTenant()`.
- Corrigido uso do Tenant padrão após login de usuários pertencentes a outra empresa.
- Corrigidos banners que permaneciam com a identidade visual PetLife em telas secundárias.
- Corrigido ícone da patinha que permanecia com cor fixa.
- Corrigido carregamento remoto de branding após identificação do Tenant.
- Ajustado fallback de logomarca e banner quando os paths remotos forem nulos ou apresentarem falha.
- Corrigido risco de utilizar o Tenant visual de fallback como Tenant de dados.
- Corrigido compartilhamento local de Pets entre empresas.
- Corrigido compartilhamento local de Vacinas entre empresas.

---

### 📌 Estado atual conhecido

A autenticação, o branding e o isolamento Multi-Tenant estão implementados e validados com usuários pertencentes a empresas diferentes.

O fluxo atual de resolução é:

```text
Usuário
   ↓
Supabase Auth
   ↓
Profile
   ↓
tenant_id
   ↓
resolvedTenantId
   ↓
Repositories vinculados ao Tenant
   ↓
Room + Supabase
```

O módulo de Pets possui atualmente:

```text
Room
↕
Supabase
```

com suporte a:

- criação local e remota;
- sincronização de Pets antigos;
- importação de Pets remotos;
- edição online;
- edição offline;
- exclusão online;
- exclusão offline;
- soft delete;
- atualização remota refletida localmente;
- exclusão remota refletida localmente;
- isolamento por Tenant;
- Row Level Security;
- sincronização ao retornar para a Home.

O módulo de Vacinas possui atualmente:

```text
Room
↕
Supabase
---

### 🔧 Em desenvolvimento

- Upload das fotos dos Pets para Supabase Storage.
- Estratégia avançada de resolução de conflitos baseada em timestamp ou versão.
- Recuperação de sessão para abertura do app sem conexão.
- Logout.
- Gerenciamento e restauração completa de sessão.
- Recuperação e redefinição de senha.
- Estratégia segura para criação de novos usuários.
- Controle de acesso baseado em roles.
- Validação completa da sincronização remota de Vacinas utilizando múltiplos Tenants..