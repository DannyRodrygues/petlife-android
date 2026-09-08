<p align="center">
  <img src="assets/logo/logomarca.png" alt="PetLife Logo" width="170">
</p>

<h1 align="center">PetLife</h1>

<p align="center">
Organize a vida do seu pet em um só lugar.
</p>

<p align="center">

![Android](https://img.shields.io/badge/Android-15-81D8D0?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7656B5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack-Compose-7656B5?style=for-the-badge)
![Supabase](https://img.shields.io/badge/Supabase-Backend-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-81D8D0?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-7656B5?style=for-the-badge)

</p>

---

## 📱 Sobre o projeto

O **PetLife** é um aplicativo Android desenvolvido em **Kotlin** para centralizar informações relacionadas à saúde, rotina e bem-estar de animais de estimação.

Além das funcionalidades de gerenciamento de pets, o projeto evoluiu para uma arquitetura **SaaS Multi-Tenant**, permitindo que diferentes empresas utilizem a mesma aplicação com identidade visual, usuários e dados próprios.

O projeto é desenvolvido como parte do meu portfólio profissional e utiliza práticas modernas de desenvolvimento Android, arquitetura em camadas, persistência local, backend remoto, autenticação, segurança com Row Level Security, sincronização offline-first e documentação contínua das decisões arquiteturais.

---

## 🚧 Status do projeto

Projeto em desenvolvimento ativo.

Atualmente estão implementados:

- módulo de Pets;
- módulo de Vacinas;
- autenticação real com Supabase Auth;
- arquitetura SaaS Multi-Tenant;
- branding dinâmico por empresa;
- isolamento local de Pets e Vacinas por Tenant;
- persistência remota de Pets no Supabase;
- Row Level Security para Pets;
- sincronização bidirecional de Pets entre Room e Supabase;
- criação, edição e exclusão offline de Pets;
- sincronização de alterações realizadas remotamente.

O módulo de **Pets** já possui integração completa entre Room e Supabase para os principais fluxos de criação, atualização, exclusão e sincronização.

O módulo de **Vacinas** já possui isolamento local por Tenant, mas sua sincronização remota ainda será implementada.

---

## 🎯 Objetivo

O PetLife tem como objetivo centralizar informações e cuidados relacionados aos animais de estimação em uma única aplicação.

O aplicativo permite registrar e acompanhar pets, vacinas e informações de saúde, e será expandido com módulos de consultas veterinárias, medicamentos, peso, agenda e outros cuidados.

Como evolução arquitetural, o PetLife também está sendo preparado como uma plataforma **SaaS Multi-Tenant**, permitindo que clínicas, pet shops e outras empresas utilizem o mesmo aplicativo com identidade visual e ambiente próprios.

---

## 🏢 SaaS Multi-Tenant

O PetLife utiliza uma arquitetura **Multi-Tenant**, permitindo que diferentes empresas compartilhem a mesma aplicação sem necessidade de manter versões diferentes do código.

Após a autenticação, o aplicativo identifica automaticamente a empresa associada ao usuário:

```text
Login
  ↓
Supabase Auth
  ↓
Profile
  ↓
tenantId
  ↓
Tenant
  ↓
BrandConfig
  ↓
Identidade visual da empresa
```

Cada Tenant pode possuir:

- nome próprio;
- cores personalizadas;
- logomarca;
- banner;
- usuários associados;
- dados de negócio isolados.

O branding é carregado remotamente através do **Supabase PostgreSQL + Storage**, permitindo alterar a identidade visual de uma empresa sem recompilar o aplicativo.

A arquitetura foi validada utilizando dois Tenants distintos:

```text
PetLife
Clínica Bicho Feliz
```

Usuários diferentes carregam automaticamente a identidade visual correspondente ao seu Tenant.

O isolamento de dados é realizado em duas camadas:

```text
Room
→ tenantId em cada registro
→ consultas filtradas por Tenant

Supabase
→ tenant_id
→ Row Level Security
→ usuário acessa apenas dados do próprio Tenant
```

A separação entre PetLife e Clínica Bicho Feliz foi validada tanto localmente quanto no backend remoto.

---

## ✨ Funcionalidades implementadas

### 🔐 Autenticação

- Login real por e-mail e senha com Supabase Auth
- Validação de campos obrigatórios
- Tratamento de credenciais inválidas
- Associação entre usuário e Tenant através de `profiles`
- Resolução automática do Tenant após autenticação
- Validação de Profile ativo
- Uso do Tenant autenticado para acesso aos dados de negócio

### 🏢 Multi-Tenant

- Arquitetura SaaS Multi-Tenant
- Tenant identificado pelo usuário autenticado
- Branding remoto por empresa
- Cores dinâmicas por Tenant
- Logomarca dinâmica
- Banner dinâmico
- Fallback visual local do PetLife
- Validação utilizando múltiplos Tenants
- Isolamento local de Pets por Tenant
- Isolamento local de Vacinas por Tenant
- Isolamento remoto de Pets por Tenant
- `tenantId` associado aos dados locais
- `tenant_id` associado aos dados remotos
- Proteção de leitura, criação, alteração e exclusão por Tenant
- Row Level Security no Supabase
- Migrations Room preservando dados existentes
- Validação real de isolamento entre PetLife e Clínica Bicho Feliz

### 🐶 Pets

- Home com listagem dos pets cadastrados
- Cadastro de pets
- Seleção de espécie e sexo
- Data de nascimento com Date Picker
- Seleção de foto pela galeria
- Persistência local da foto do pet
- Persistência local com Room
- Persistência remota com Supabase PostgreSQL
- Tela de detalhes
- Edição dos dados do pet
- Exclusão de pet com confirmação
- Exclusão em cascata dos dados locais relacionados
- Isolamento por Tenant
- RLS no backend
- Identidade local e remota através de `id` e `remoteId`
- Sincronização Room → Supabase
- Sincronização Supabase → Room
- Importação de Pets criados remotamente
- Atualização local de Pets alterados remotamente
- Criação offline com sincronização posterior
- Edição offline com `pendingSync`
- Exclusão offline com `pendingDelete`
- Soft delete remoto através de `deleted_at`
- Remoção local de Pets excluídos remotamente
- Atualização automática de `updated_at`
- Sincronização ao retornar para a Home
- Proteção de alterações locais pendentes durante sincronização remota

### 💉 Vacinas

- Cadastro de vacinas por pet
- Histórico de vacinas
- Controle de próximas doses
- Relacionamento entre Pets e Vacinas
- Persistência local com Room
- Evolução do banco utilizando migrations
- Isolamento das Vacinas por Tenant
- Associação automática da vacina ao Tenant autenticado
- Preservação das vacinas existentes durante migration
- Exclusão local em cascata quando o Pet é removido

### 🎨 Interface

- Jetpack Compose
- Material Design 3
- Design System próprio
- Componentes reutilizáveis
- Tema dinâmico por Tenant
- Navegação tipada com Navigation Compose
- Branding dinâmico utilizando MaterialTheme
- Componentes visuais adaptados às cores de cada empresa

---

## 🔄 Sincronização de Pets

O PetLife utiliza uma estratégia **local-first**.

O Room é utilizado como banco local e fonte dos dados exibidos pela interface.

O Supabase atua como backend remoto compartilhado entre dispositivos e como camada de segurança através de RLS.

### Criação

```text
Cadastrar Pet
     ↓
Room
     ↓
INSERT Supabase
     ↓
UUID remoto
     ↓
remoteId salvo no Room
```

Se o Pet for criado sem conexão:

```text
remoteId = NULL
```

Quando a sincronização for executada novamente, o registro é enviado ao Supabase.

### Edição offline

```text
Editar Pet
     ↓
Room
     ↓
pendingSync = true
     ↓
tenta Supabase
```

Se a atualização remota for confirmada:

```text
pendingSync = false
```

Caso contrário, a alteração permanece localmente até a próxima sincronização.

### Exclusão offline

```text
Excluir Pet
     ↓
pendingDelete = true
     ↓
Pet some da interface
     ↓
internet volta
     ↓
deleted_at no Supabase
     ↓
Pet removido definitivamente do Room
```

### Alterações remotas

Alterações feitas no Supabase ou futuramente em outro dispositivo são sincronizadas para o Room quando o registro local não possui alterações pendentes.

A regra atual de prioridade é:

```text
pendingSync = true
→ alteração local tem prioridade

pendingDelete = true
→ exclusão local tem prioridade

sem pendências
→ dados remotos podem atualizar o Room
```

---

## 📴 Estratégia offline

Os principais fluxos de Pets já funcionam com comportamento offline-first.

Após um login válido e carregamento do Tenant, o usuário pode continuar utilizando os dados locais do Room sem conexão.

Atualmente são suportados:

```text
criação offline
edição offline
exclusão offline
sincronização posterior
```

O login inicial ainda depende de conexão com o Supabase.

Uma evolução futura deverá permitir reabrir o aplicativo offline utilizando uma sessão previamente autenticada e o último Tenant resolvido localmente.

---

## 🚀 Funcionalidades planejadas

- 🚪 Logout e gerenciamento completo de sessão
- 🔑 Recuperação e redefinição de senha com Supabase Auth
- 👤 Criação de contas e associação segura ao Tenant
- 🔐 Controle de permissões por roles
- 📴 Recuperação de sessão para abertura do app offline
- 💉 Persistência remota de Vacinas no Supabase
- 💉 RLS para Vacinas
- 🔄 Sincronização Room ↔ Supabase para Vacinas
- 🖼️ Upload das fotos dos Pets para Supabase Storage
- 🔀 Estratégia avançada de resolução de conflitos por timestamp ou versão
- 🩺 Registro e histórico de consultas veterinárias
- 💊 Registro de medicamentos e tratamentos
- ⚖️ Histórico de peso e medidas
- 📅 Agenda de consultas e cuidados
- 📊 Dashboard com próximos compromissos
- 🛠️ Painel administrativo para gerenciamento de empresas e usuários

---

## 🛠 Tecnologias

### Android

- Kotlin
- Jetpack Compose
- Material Design 3
- Navigation Compose
- Coroutines
- Flow / StateFlow
- Coil

### Arquitetura

- MVVM
- Repository Pattern
- Data Sources
- DTOs e Mappers
- CompositionLocal para Tenant ativo
- Estratégia local-first
- Sincronização bidirecional

### Persistência local

- Room
- Room Migrations
- SQLite
- Foreign Keys
- Flow

### Backend

- Supabase
- Supabase Auth
- Supabase PostgreSQL
- Supabase Storage
- PostgREST
- Row Level Security (RLS)
- PostgreSQL Triggers
- Ktor Client

### Ferramentas

- Git
- GitHub
- Gradle Kotlin DSL
- KSP

---

## 🏛 Arquitetura

O PetLife utiliza uma arquitetura baseada em separação de responsabilidades, combinando **MVVM**, **Repository Pattern**, persistência local e Data Sources remotos.

```text
UI / Compose
      ↓
ViewModel
      ↓
Repository
   ↙       ↘
Room     Supabase
```

### Interface

Responsável pelas telas Compose, interação do usuário e observação dos estados expostos pelos ViewModels.

### Presentation

Os ViewModels controlam o estado das telas utilizando `StateFlow` e coordenam as ações da interface.

### Data

Repositories isolam as fontes de dados utilizadas pela aplicação.

```text
Repository
   ↓
   ├── Room
   │   └── persistência local / offline
   │
   └── Supabase
       └── persistência remota / sincronização
```

### Sincronização

A ordem atual da sincronização de Pets é:

```text
1. Pets novos
   remoteId = NULL

2. Edições pendentes
   pendingSync = true

3. Exclusões pendentes
   pendingDelete = true

4. Dados remotos
   Supabase → Room
```

### Multi-Tenant

A resolução do Tenant utiliza:

```text
Supabase Auth
      ↓
profiles
      ↓
tenant_id
      ↓
CurrentTenantRepository
      ↓
resolvedTenantId
      ↓
Repositories vinculados ao Tenant
      ↓
Room + Supabase
```

O fallback visual do PetLife é utilizado somente para branding.

Dados de negócio são acessados apenas após a resolução real do Tenant autenticado.

Uma camada de domínio dedicada será adicionada quando regras de negócio suficientemente complexas justificarem sua utilização.

---

## 📂 Estrutura do projeto

```text
PetLife
│
├── app
│   └── src/main/java/com/dannyrodrygues/petlife
│       │
│       ├── core
│       │   ├── auth
│       │   ├── components
│       │   ├── data
│       │   ├── navigation
│       │   └── tenant
│       │
│       ├── feature
│       │   ├── auth
│       │   ├── home
│       │   ├── pet
│       │   └── vaccine
│       │
│       └── ui
│           └── theme
│
├── docs
│   ├── adr
│   │   ├── 0001-initial-architecture.md
│   │   ├── 0002-visual-identity-and-design-system.md
│   │   ├── 0003-saas-multi-tenant.md
│   │   ├── 0004-authentication-and-tenant-resolution.md
│   │   └── 0005-tenant-data-isolation-and-sync.md
│   │
│   ├── architecture
│   └── design
│
├── assets
│   ├── logo
│   ├── banner
│   └── screenshots
│
├── CHANGELOG.md
└── README.md
```

---

## 📸 Screenshots

<a href="assets/screenshots/welcome-screen.png">
  <img src="assets/screenshots/welcome-screen.png"
       alt="Tela de Boas-vindas"
       width="140">
</a> |
<a href="assets/screenshots/login-screen.png">
  <img src="assets/screenshots/login-screen.png"
       alt="Tela de Login"
       width="140">
</a> |
<a href="assets/screenshots/register-screen.png">
  <img src="assets/screenshots/register-screen.png"
       alt="Tela de Cadastro"
       width="140">
</a> |
<a href="assets/screenshots/forgot-password-screen.png">
  <img src="assets/screenshots/forgot-password-screen.png"
       alt="Tela de Recuperação de Senha"
       width="140">
</a> |
<a href="assets/screenshots/home-screen.png">
  <img src="assets/screenshots/home-screen.png"
       alt="Tela Home"
       width="140">
</a> |
<a href="assets/screenshots/add-pet-screen.png">
  <img src="assets/screenshots/add-pet-screen.png"
       alt="Tela de Cadastro de Pet"
       width="140">
</a>

> Os screenshots serão atualizados conforme a evolução da aplicação. Uma futura demonstração também deverá destacar o mesmo aplicativo carregando identidades visuais de Tenants diferentes.

---

## ⚙️ Requisitos

- Android 8.0 (API 26) ou superior
- Kotlin
- Jetpack Compose

---

## 💻 Ambiente de desenvolvimento

- Ubuntu 24.04 LTS
- Android Studio Ladybug Feature Drop
- Java 21
- Dispositivo físico Android (API 27+)

---

## 📖 Histórico do projeto

- ✅ Configuração do ambiente Android
- ✅ Configuração do Git e integração com GitHub
- ✅ Organização por Feature Packages
- ✅ Criação do Design System
- ✅ Definição da identidade visual PetLife
- ✅ Implementação da navegação principal
- ✅ Cadastro e persistência local de Pets
- ✅ Persistência de fotos dos Pets
- ✅ Home com listagem dos Pets
- ✅ Tela de detalhes e edição do Pet
- ✅ Implementação do módulo de Vacinas
- ✅ Relacionamento entre Pets e Vacinas
- ✅ Room migrations preservando dados existentes
- ✅ Definição da arquitetura SaaS Multi-Tenant
- ✅ Criação de Tenant e BrandConfig
- ✅ Integração Android com Supabase
- ✅ PostgreSQL para configurações remotas
- ✅ Supabase Storage para branding
- ✅ Branding dinâmico por Tenant
- ✅ Supabase Auth
- ✅ Login real por e-mail e senha
- ✅ Tabela `profiles`
- ✅ Associação usuário → Tenant
- ✅ Resolução automática do Tenant após login
- ✅ Teste real com múltiplas empresas
- ✅ Logo, banner, cores e nome dinâmicos por Tenant
- ✅ Isolamento local de Pets por Tenant
- ✅ Isolamento local de Vacinas por Tenant
- ✅ Migrations Room preservando Pets e Vacinas existentes
- ✅ Persistência remota de Pets no Supabase
- ✅ RLS para Pets
- ✅ Sincronização Room → Supabase
- ✅ Sincronização Supabase → Room
- ✅ Identidade local/remota com `remoteId`
- ✅ Sincronização de Pets locais antigos
- ✅ Importação de Pets criados remotamente
- ✅ Edição remota de Pets
- ✅ Edição offline com `pendingSync`
- ✅ Soft delete com `deleted_at`
- ✅ Exclusão offline com `pendingDelete`
- ✅ Exclusão remota refletida no Room
- ✅ Atualizações remotas refletidas no Room
- ✅ Trigger automático para `updated_at`
- ✅ Sincronização ao retornar para a Home
- ✅ Validação dos fluxos nos Tenants PetLife e Clínica Bicho Feliz
- 🚧 Sincronização remota de Vacinas
- 🚧 Upload das fotos dos Pets para Supabase Storage
- 🚧 Estratégia avançada de resolução de conflitos
- 🚧 Recuperação de sessão para funcionamento totalmente offline

---

## 📚 Documentação

A evolução técnica e arquitetural do PetLife é documentada durante o desenvolvimento.

- `CHANGELOG.md` — histórico das alterações realizadas.
- `docs/adr/0001-initial-architecture.md` — arquitetura inicial do aplicativo.
- `docs/adr/0002-visual-identity-and-design-system.md` — identidade visual e Design System.
- `docs/adr/0003-saas-multi-tenant.md` — adoção da arquitetura SaaS Multi-Tenant.
- `docs/adr/0004-authentication-and-tenant-resolution.md` — autenticação e resolução automática do Tenant.
- `docs/adr/0005-tenant-data-isolation-and-sync.md` — isolamento de dados por Tenant e estratégia de sincronização Room ↔ Supabase.
- `docs/architecture/` — diagramas da arquitetura e distribuição.
- `docs/design/` — documentação do Design System.

Novos ADRs são criados quando decisões arquiteturais relevantes precisam ser registradas.

---

<h2>
  <img src="docs/images/daniella.png" width="42" alt="Daniella Rodrigues">
  Desenvolvedora
</h2>

**Daniella Rodrigues**

Desenvolvedora Android • Kotlin • Jetpack Compose

Desenvolvido como projeto de estudo e portfólio para demonstrar conhecimentos em desenvolvimento Android moderno, arquitetura, persistência local, backend remoto, sincronização offline-first e evolução de um produto SaaS Multi-Tenant.