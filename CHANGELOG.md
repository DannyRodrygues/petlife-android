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
- `PetDatabase`.
- `PetRepository`.
- `AddPetViewModel`.
- `HomeViewModel`.
- Persistência do acesso à foto selecionada após reiniciar o aplicativo.
- Tela de detalhes do Pet.
- `PetDetailsViewModel`.
- Tela de edição dos dados do Pet.
- `EditPetViewModel`.
- Persistência das alterações realizadas no Pet.

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
- Migration do banco Room da versão 1 para 2 preservando dados existentes.

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
- Configuração de Row Level Security nas tabelas remotas.
- Criação do bucket público `tenant-branding`.
- Organização dos arquivos de branding por UUID do Tenant.
- Armazenamento remoto de logomarca e banner por empresa.
- Criação de DTOs para Tenant e BrandConfig.
- Criação de mapper entre DTOs remotos e modelos da aplicação.
- Criação do `TenantRemoteDataSource`.
- Criação do `TenantRepository`.

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
- Criação da tabela `profiles`.
- Associação entre `profiles.id` e `auth.users.id`.
- Associação de usuários a empresas através de `profiles.tenant_id`.
- Campo `role` para futura implementação de permissões.
- Campo `active` para controle de usuários ativos.
- Row Level Security na tabela `profiles`.
- Policy permitindo que o usuário autenticado consulte apenas o próprio profile.
- Criação do `ProfileDto`.
- Criação do `ProfileRemoteDataSource`.
- Busca do Tenant através de UUID.
- Criação de `getTenantById()` na camada remota.
- Criação de `getTenantById()` no `TenantRepository`.
- Criação do `CurrentTenantRepository`.
- Resolução automática do Tenant através do usuário autenticado.
- Atualização do Tenant ativo após login através de `refreshCurrentTenant()`.
- Utilização da mesma instância de `TenantViewModel` criada no `PetLifeApp`.

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

#### Documentação

- Criação do `CHANGELOG.md`.
- Organização da documentação do projeto.
- Criação da estrutura `docs/adr/`.
- Criação da estrutura `docs/architecture/`.
- Criação da estrutura `docs/design/`.
- Criação do ADR 001 sobre a arquitetura SaaS Multi-Tenant.
- Criação do ADR 002 sobre autenticação e resolução do Tenant.
- Criação de diagramas da arquitetura Multi-Tenant.
- Criação de diagramas de distribuição e administração.
- Criação de versões editáveis dos diagramas em Excalidraw.
- Atualização do README para refletir Supabase, autenticação e arquitetura Multi-Tenant.

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

#### Home e Pets

- Home passou a consumir Pets persistidos no Room.
- Cards da Home passaram a abrir os detalhes do Pet.
- Cadastro de Pet passou a utilizar `AddPetViewModel`.
- Tela de detalhes passou a utilizar `PetDetailsViewModel`.
- Tela de edição passou a utilizar `EditPetViewModel`.
- Dados da tela de detalhes passaram a ser atualizados após edição.
- Fluxo de imagens foi ajustado para preservar o acesso após reiniciar o aplicativo.

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

#### README

- README reorganizado para refletir o estado atual do projeto.
- Firebase removido da documentação planejada.
- Supabase adicionado às tecnologias do projeto.
- Arquitetura SaaS Multi-Tenant adicionada ao README.
- Funcionalidades implementadas reorganizadas por categoria.
- Funcionalidades planejadas atualizadas.
- Arquitetura atualizada com Room e Supabase.
- Estrutura de diretórios atualizada.
- Histórico do projeto consolidado e sem duplicações.
- ADR 0001, ADR 0002, ADR 0003 e ADR 0004 adicionados à seção de documentação.

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

#### Vacinas

- Corrigidos problemas visuais em textos longos dos cards de Vacinas.
- Ajustado layout do histórico de Vacinas.
- Corrigido relacionamento e carregamento das Vacinas associadas ao Pet.

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

---

### 📌 Estado atual conhecido

A autenticação e o branding Multi-Tenant já foram implementados e validados com usuários pertencentes a empresas diferentes.

Atualmente, o isolamento visual funciona corretamente:

```text
Usuário
   ↓
Supabase Auth
   ↓
Profile
   ↓
tenant_id
   ↓
Tenant
   ↓
BrandConfig
   ↓
Nome + Cores + Logo + Banner
```

Entretanto, os dados locais de negócio ainda precisam ser isolados por Tenant.

No estado atual, registros persistidos no Room, como Pets e Vacinas, ainda podem ser visualizados por usuários de Tenants diferentes quando utilizam o mesmo dispositivo.

A próxima etapa arquitetural será adicionar `tenantId` às entidades locais e implementar o isolamento dos dados sem perder os registros existentes.

---

### 🚧 Em desenvolvimento

- Isolamento de Pets por Tenant.
- Isolamento de Vacinas por Tenant.
- Migration do Room para inclusão de `tenantId`.
- Sincronização Room ↔ Supabase.
- Políticas RLS para dados privados de negócio.
- Logout.
- Gerenciamento e restauração de sessão.
- Recuperação e redefinição de senha.
- Estratégia segura para criação de novos usuários.
- Controle de acesso baseado em roles.