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

Além das funcionalidades de gerenciamento de pets, o projeto está evoluindo para uma arquitetura **SaaS Multi-Tenant**, permitindo que diferentes empresas utilizem a mesma aplicação com identidade visual, usuários e dados próprios.

O projeto é desenvolvido como parte do meu portfólio profissional e utiliza práticas modernas de desenvolvimento Android, arquitetura em camadas, persistência local, backend remoto, autenticação, segurança com Row Level Security e documentação contínua das decisões arquiteturais.

---

## 🚧 Status do projeto

Projeto em desenvolvimento ativo.

Atualmente estão implementados os módulos de **Pets** e **Vacinas**, autenticação por e-mail e senha, integração com Supabase e a fundação da arquitetura SaaS Multi-Tenant.

O isolamento completo dos dados de negócio por Tenant e a sincronização **Room ↔ Supabase** estão entre as próximas etapas do desenvolvimento.

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
- usuários associados.

O branding é carregado remotamente através do **Supabase PostgreSQL + Storage**, permitindo alterar a identidade visual de uma empresa sem recompilar o aplicativo.

A arquitetura foi validada utilizando dois Tenants distintos:

```text
PetLife
Clínica Bicho Feliz
```

Usuários diferentes carregam automaticamente a identidade visual correspondente ao seu Tenant.

> O isolamento dos dados de negócio, como Pets e Vacinas, ainda está em desenvolvimento e será realizado através de `tenantId`, Room e políticas RLS no backend.

---

## ✨ Funcionalidades implementadas

### 🔐 Autenticação

- Login real por e-mail e senha com Supabase Auth
- Validação de campos obrigatórios
- Tratamento de credenciais inválidas
- Associação entre usuário e Tenant através de `profiles`
- Resolução automática do Tenant após autenticação

### 🏢 Multi-Tenant

- Arquitetura SaaS Multi-Tenant
- Tenant identificado pelo usuário autenticado
- Branding remoto por empresa
- Cores dinâmicas por Tenant
- Logomarca dinâmica
- Banner dinâmico
- Fallback visual local do PetLife
- Validação utilizando múltiplos Tenants

### 🐶 Pets

- Home com listagem dos pets cadastrados
- Cadastro de pets
- Seleção de espécie e sexo
- Data de nascimento com Date Picker
- Seleção de foto pela galeria
- Persistência da foto do pet
- Persistência local com Room
- Tela de detalhes
- Edição dos dados do pet

### 💉 Vacinas

- Cadastro de vacinas por pet
- Histórico de vacinas
- Controle de próximas doses
- Relacionamento entre Pets e Vacinas
- Persistência local com Room
- Evolução do banco utilizando migrations

### 🎨 Interface

- Jetpack Compose
- Material Design 3
- Design System próprio
- Componentes reutilizáveis
- Tema dinâmico por Tenant
- Navegação tipada com Navigation Compose

---

## 🚀 Funcionalidades planejadas

- 🚪 Logout e gerenciamento completo de sessão
- 🔑 Recuperação e redefinição de senha com Supabase Auth
- 👤 Criação de contas e associação segura ao Tenant
- 🔐 Controle de permissões por roles
- 🏢 Isolamento de Pets, Vacinas e demais dados por Tenant
- 🔄 Sincronização entre Room e Supabase
- 📴 Estratégia offline-first
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

### Persistência local

- Room
- Room Migrations

### Backend

- Supabase
- Supabase Auth
- Supabase PostgreSQL
- Supabase Storage
- PostgREST
- Row Level Security (RLS)
- Ktor Client

### Ferramentas

- Git
- GitHub
- Gradle Kotlin DSL
- KSP

---

## 🏛 Arquitetura

O PetLife utiliza uma arquitetura baseada em separação de responsabilidades, combinando **MVVM**, **Repository Pattern** e **Data Sources**.

```text
UI / Compose
      ↓
ViewModel
      ↓
Repository
      ↓
Data Source
   ↙       ↘
 Room    Supabase
```

### Interface

Responsável pelas telas Compose, interação do usuário e observação dos estados expostos pelos ViewModels.

### Presentation

Os ViewModels controlam o estado das telas utilizando `StateFlow` e coordenam as ações da interface.

### Data

Repositories isolam as fontes de dados utilizadas pela aplicação.

Atualmente o projeto utiliza:

```text
Room
→ persistência local

Supabase
→ autenticação
→ PostgreSQL
→ Storage
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
TenantRepository
      ↓
TenantConfig
      ↓
TenantProvider
      ↓
UI
```

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
│       │   └── pet
│       │
│       └── ui
│           └── theme
│
├── docs
│   ├── adr
│   │   ├── 0001-initial-architecture.md
│   │   ├── 0002-visual-identity-and-design-system.md
│   │   ├── 0003-saas-multi-tenant.md
│   │   └── 0004-authentication-and-tenant-resolution.md
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
- ✅ Row Level Security
- ✅ Branding dinâmico por Tenant
- ✅ Supabase Auth
- ✅ Login real por e-mail e senha
- ✅ Tabela `profiles`
- ✅ Associação usuário → Tenant
- ✅ Resolução automática do Tenant após login
- ✅ Teste real com múltiplas empresas
- ✅ Logo, banner, cores e nome dinâmicos por Tenant
- 🚧 Isolamento dos dados locais por Tenant
- 🚧 Sincronização Room ↔ Supabase

---

## 📚 Documentação

A evolução técnica e arquitetural do PetLife é documentada durante o desenvolvimento.

- `CHANGELOG.md` — histórico das alterações realizadas.
- `docs/adr/0001-initial-architecture.md` — arquitetura inicial do aplicativo.
- `docs/adr/0002-visual-identity-and-design-system.md` — identidade visual e Design System.
- `docs/adr/0003-saas-multi-tenant.md` — adoção da arquitetura SaaS Multi-Tenant.
- `docs/adr/0004-authentication-and-tenant-resolution.md` — autenticação e resolução automática do Tenant.
- `docs/architecture/` — diagramas da arquitetura e distribuição.
- `docs/design/` — documentação do Design System.

Novos ADRs são criados quando decisões arquiteturais relevantes precisam ser registradas.

O próximo ADR previsto é:

- `ADR 0005` — isolamento dos dados por Tenant e estratégia de sincronização Room ↔ Supabase.

---

<h2>
  <img src="docs/images/daniella.png" width="42" alt="Daniella Rodrigues">
  Desenvolvedora
</h2>

**Daniella Rodrigues**

Desenvolvedora Android • Kotlin • Jetpack Compose

Desenvolvido como projeto de estudo e portfólio para demonstrar conhecimentos em desenvolvimento Android moderno, arquitetura, persistência local, backend remoto e evolução de um produto SaaS Multi-Tenant.