# ADR 001 — Arquitetura SaaS Multi-Tenant

## Status

Aceito

## Data

2026-08-28

---

## Contexto

O PetLife foi inicialmente desenvolvido como um aplicativo Android com identidade visual própria, utilizando cores, logomarca, banner e recursos definidos diretamente no projeto.

Com a evolução do produto, surgiu a possibilidade de disponibilizar o PetLife para diferentes empresas do segmento pet, como:

- pet shops;
- clínicas veterinárias;
- hospitais veterinários;
- centros de banho e tosa;
- outros estabelecimentos do setor pet.

Manter uma versão separada do aplicativo para cada empresa aumentaria significativamente o custo de desenvolvimento e manutenção.

Cada correção de bug, nova funcionalidade ou alteração estrutural teria que ser replicada em várias versões do aplicativo.

Além disso, diferentes empresas podem precisar de:

- logomarca própria;
- banner próprio;
- cores próprias;
- nome da empresa;
- configurações específicas;
- módulos habilitados ou desabilitados.

Por esse motivo, o PetLife precisa permitir personalização por empresa sem exigir uma nova base de código para cada cliente.

---

## Decisão

O PetLife será desenvolvido como uma plataforma:

**SaaS Multi-Tenant**

Uma única aplicação atenderá múltiplas empresas.

Cada empresa será representada por um **Tenant**.

Cada usuário estará associado a um `tenantId`, que permitirá identificar a empresa à qual pertence.

O aplicativo utilizará esse `tenantId` para carregar:

- identidade visual;
- configurações;
- permissões;
- módulos disponíveis;
- dados pertencentes à empresa.

O modelo principal de distribuição será uma única aplicação PetLife publicada na Google Play Store.

A personalização será carregada dinamicamente de acordo com o Tenant do usuário autenticado.

---

## Tenant

O Tenant representa uma empresa dentro da plataforma.

Exemplo conceitual:

```text
Tenant
-------------------------
id
name
active
createdAt
updatedAt
```

Exemplos:

```text
Tenant 1
PetLife

Tenant 2
Clínica Bicho Feliz

Tenant 3
Pet Shop Amigo Fiel
```

O Tenant será o ponto central para relacionar:

- usuários;
- identidade visual;
- configurações;
- pets;
- vacinas;
- consultas;
- medicamentos;
- histórico de peso;
- demais dados da empresa.

---

## Identidade visual

Cada Tenant poderá possuir uma configuração de identidade visual própria.

Essa configuração será representada por um `BrandConfig`.

Exemplo conceitual:

```text
BrandConfig
-------------------------
tenantId
primaryColor
secondaryColor
tertiaryColor
logoPath
bannerPath
```

A configuração permitirá alterar dinamicamente:

- cor principal;
- cor secundária;
- cor terciária;
- logomarca;
- banner;
- nome exibido da empresa.

As telas do aplicativo não deverão conhecer diretamente as cores específicas de uma empresa.

Elas continuarão utilizando o tema da aplicação, por exemplo:

```kotlin
MaterialTheme.colorScheme.primary
```

O Tenant ativo será responsável por fornecer a identidade visual correspondente.

---

## Armazenamento dos recursos visuais

Logomarcas, banners e demais imagens específicas dos Tenants não serão incorporados diretamente aos recursos do aplicativo Android.

Os arquivos específicos das empresas serão armazenados no **Supabase Storage**.

Organização prevista:

```text
tenants/
│
├── {tenantId}/
│   ├── branding/
│   │   ├── logo.png
│   │   └── banner.jpg
│   │
│   └── pets/
│       └── ...
```

O banco de dados não armazenará os arquivos de imagem diretamente.

O `BrandConfig` armazenará apenas as referências necessárias para localizar esses arquivos.

Exemplo:

```text
logoPath =
tenants/{tenantId}/branding/logo.png

bannerPath =
tenants/{tenantId}/branding/banner.jpg
```

Os recursos atuais do PetLife permanecerão no aplicativo inicialmente como:

- identidade visual padrão;
- configuração do Tenant PetLife;
- fallback local caso a configuração remota esteja indisponível.

---

## Persistência e backend

O PetLife utilizará duas camadas principais de persistência:

### Persistência local

O Room continuará sendo utilizado no aplicativo Android para:

- armazenamento local;
- funcionamento offline;
- cache dos dados;
- resposta rápida da interface;
- persistência durante períodos sem conexão.

### Persistência remota

O Supabase será utilizado como backend principal da plataforma.

O Supabase será responsável por:

- banco de dados remoto PostgreSQL;
- autenticação de usuários;
- armazenamento de imagens e arquivos;
- controle de acesso;
- isolamento dos dados entre Tenants.

Arquitetura prevista:

```text
                    PETLIFE ANDROID
                          │
                     Repositories
                          │
             ┌────────────┴────────────┐
             │                         │
             ▼                         ▼
      LocalDataSource           RemoteDataSource
             │                         │
            Room                    Supabase
                                       │
                         ┌─────────────┼─────────────┐
                         │             │             │
                         ▼             ▼             ▼
                    PostgreSQL        Auth         Storage
```

O Supabase será considerado a fonte oficial dos dados compartilhados entre dispositivos.

O Room funcionará como persistência local e suporte ao funcionamento offline.

---

## Supabase

O Supabase foi escolhido como plataforma de backend remoto do PetLife.

A plataforma será utilizada principalmente através de três recursos:

1. PostgreSQL;
2. Supabase Auth;
3. Supabase Storage.

---

### PostgreSQL

O PostgreSQL armazenará os dados estruturados da plataforma.

Entre as entidades previstas estão:

- Tenants;
- configurações de identidade visual;
- perfis de usuários;
- pets;
- vacinas;
- consultas;
- medicamentos;
- histórico de peso;
- demais dados de negócio.

O modelo relacional é adequado ao PetLife devido aos relacionamentos existentes entre as entidades.

Exemplo conceitual:

```text
Tenant
   │
   ├── Users
   │
   └── Pets
        │
        ├── Vaccines
        ├── Appointments
        ├── Medications
        └── WeightHistory
```

---

### Supabase Auth

O Supabase Auth será utilizado para autenticação dos usuários.

Após a autenticação, o usuário deverá ser associado ao Tenant correspondente.

Fluxo previsto:

```text
Usuário
   ↓
Login
   ↓
Supabase Auth
   ↓
User/Profile
   ↓
tenantId
   ↓
Tenant
   ↓
BrandConfig
   ↓
Aplicativo personalizado
```

O usuário poderá também possuir um perfil ou papel de acesso, por exemplo:

```text
SUPER_ADMIN
COMPANY_ADMIN
USER
```

As permissões serão evoluídas conforme as necessidades da plataforma.

---

### Supabase Storage

O Supabase Storage será utilizado para armazenar arquivos que não devem ficar incorporados diretamente ao aplicativo Android.

Entre esses arquivos estarão:

- logomarcas das empresas;
- banners;
- imagens institucionais;
- futuramente fotos dos pets.

Exemplo:

```text
Supabase Storage

tenants/
│
├── tenant_001/
│   ├── branding/
│   │   ├── logo.png
│   │   └── banner.jpg
│   │
│   └── pets/
│       └── ...
│
└── tenant_002/
    ├── branding/
    │   ├── logo.png
    │   └── banner.jpg
    │
    └── pets/
        └── ...
```

O PostgreSQL armazenará somente os caminhos ou referências necessárias para localizar os arquivos.

---

## Segurança Multi-Tenant

O isolamento dos dados entre empresas será obrigatório.

Um Tenant nunca poderá acessar dados pertencentes a outro Tenant.

Exemplo:

```text
Empresa A
├── pets
├── vacinas
├── consultas
└── medicamentos

Empresa B
├── pets
├── vacinas
├── consultas
└── medicamentos
```

Os registros de negócio deverão futuramente possuir associação com um `tenantId`.

Exemplo:

```text
Pet
-------------------------
id
tenantId
name
species
breed
...
```

Exemplo de regra conceitual:

```text
Usuário autenticado
tenantId = tenant_001
        ↓
pode acessar
        ↓
dados com tenantId = tenant_001
```

O mesmo usuário não deverá acessar:

```text
tenantId = tenant_002
```

---

## Row Level Security — RLS

O PetLife utilizará os mecanismos de **Row Level Security (RLS)** do PostgreSQL/Supabase para reforçar o isolamento dos dados entre empresas.

A segurança não deverá depender somente de filtros implementados no aplicativo Android.

Por exemplo, um filtro local como:

```kotlin
pets.filter {
    it.tenantId == currentTenantId
}
```

não será considerado suficiente como mecanismo de segurança.

As regras também deverão existir no backend.

O objetivo é garantir que o próprio Supabase impeça um usuário de consultar ou modificar registros pertencentes a outro Tenant.

---

## Fluxo de autenticação e carregamento da empresa

O fluxo previsto será:

```text
Usuário
   ↓
Login
   ↓
Supabase Auth
   ↓
Perfil do usuário
   ↓
tenantId
   ↓
Tenant
   ↓
BrandConfig
   ↓
Tema da empresa
   ↓
Dados da empresa
   ↓
PetLife personalizado
```

Exemplo:

```text
admin@bichofeliz.com
        ↓
Supabase Auth
        ↓
tenantId = tenant_002
        ↓
Clínica Bicho Feliz
        ↓
logo da clínica
cores da clínica
banner da clínica
dados da clínica
```

---

## Room e Supabase

O Room não será removido.

A arquitetura deverá evoluir para permitir o uso conjunto de persistência local e remota.

Modelo previsto:

```text
                    Repository
                       │
            ┌──────────┴──────────┐
            │                     │
            ▼                     ▼
      LocalDataSource       RemoteDataSource
            │                     │
           Room                Supabase
```

O objetivo futuro será oferecer uma experiência offline-first.

Exemplo:

```text
Supabase
   ↓
sincronização
   ↓
Room
   ↓
UI
```

Quando não houver conexão:

```text
Sem internet
    ↓
Room
    ↓
dados locais
    ↓
aplicativo continua funcionando
```

A estratégia completa de sincronização será definida posteriormente em uma decisão arquitetural específica.

---

## Distribuição

O modelo principal de distribuição será:

**um único aplicativo PetLife publicado na Google Play Store.**

Todas as empresas utilizarão a mesma aplicação.

Após a autenticação e identificação do Tenant, o aplicativo carregará dinamicamente sua configuração.

Exemplo:

```text
                 GOOGLE PLAY STORE
                        │
                        ▼
                     PetLife
                        │
                        ▼
                      Login
                        │
                     tenantId
                  ┌─────┼─────┐
                  │     │     │
                  ▼     ▼     ▼
              Empresa A B     C
                  │     │     │
                  ▼     ▼     ▼
              Branding Branding Branding
                 A       B       C
```

Dessa forma, correções de bugs e novas funcionalidades poderão ser distribuídas através de uma única versão do aplicativo.

---

## White-label completo

Poderá existir futuramente uma modalidade especial de distribuição white-label.

Nesse modelo, determinadas empresas poderão possuir:

- nome próprio do aplicativo;
- ícone próprio;
- `applicationId` próprio;
- configuração própria de publicação;
- publicação própria na Google Play Store.

Essas versões deverão reutilizar a mesma base de código do PetLife.

O Android poderá utilizar mecanismos como Product Flavors caso essa modalidade seja necessária.

O white-label completo não será a estratégia principal.

O modelo principal continuará sendo o SaaS Multi-Tenant através de uma única aplicação PetLife.

---

## Administração da plataforma

Será prevista futuramente uma área administrativa da plataforma.

O administrador principal poderá:

- cadastrar empresas;
- alterar dados da empresa;
- fazer upload de logomarca;
- fazer upload de banner;
- configurar cores;
- cadastrar administrador da empresa;
- ativar ou desativar empresas;
- configurar módulos disponíveis;
- administrar configurações da plataforma.

Fluxo conceitual:

```text
Super Admin PetLife
        ↓
Cadastrar empresa
        ↓
Tenant
        ↓
BrandConfig
        ↓
Usuário administrador
        ↓
Supabase
```

O painel administrativo poderá futuramente ser desenvolvido separadamente do aplicativo Android, preferencialmente como aplicação web administrativa.

---

## Configuração de funcionalidades por Tenant

A arquitetura poderá futuramente permitir que módulos sejam habilitados ou desabilitados conforme a empresa.

Exemplo conceitual:

```text
CompanySettings
-------------------------
tenantId
enableVaccines
enableAppointments
enableMedications
enableWeightTracking
```

Exemplo:

```text
Clínica Bicho Feliz

Vacinas          = true
Consultas        = true
Medicamentos     = true
Peso             = true
```

Outra empresa poderia possuir:

```text
Pet Shop Amigo Fiel

Vacinas          = true
Consultas        = true
Medicamentos     = false
Peso             = false
```

Essa funcionalidade será implementada somente quando houver necessidade concreta.

---

## Estratégia de migração

A transformação do PetLife atual para SaaS Multi-Tenant será incremental.

O aplicativo existente não será reescrito.

A primeira configuração da plataforma será o próprio PetLife.

Exemplo:

```text
Tenant padrão
-------------------------
name = PetLife
active = true
```

```text
BrandConfig
-------------------------
primaryColor = roxo atual
secondaryColor = Tiffany atual
logo = logo atual
banner = banner atual
```

Inicialmente, essa configuração poderá existir localmente no aplicativo.

Posteriormente será criada sua representação remota no Supabase.

A migração deverá seguir aproximadamente esta ordem:

1. criar `TenantConfig`;
2. criar `BrandConfig`;
3. criar configuração padrão do PetLife;
4. adaptar o tema para branding configurável;
5. preservar logo e banner atuais como fallback;
6. integrar o projeto Android ao Supabase;
7. configurar Supabase Auth;
8. criar estrutura remota de Tenant;
9. associar usuários ao `tenantId`;
10. carregar `BrandConfig` remotamente;
11. armazenar logos e banners no Supabase Storage;
12. adicionar `tenantId` aos dados de negócio;
13. configurar políticas RLS;
14. implementar sincronização entre Supabase e Room.

Durante todo o processo:

- o aplicativo deverá continuar funcionando;
- a identidade visual atual deverá ser preservada;
- os pets existentes deverão continuar disponíveis;
- as vacinas existentes deverão continuar disponíveis;
- as alterações deverão ser feitas de forma incremental e testável.

---

## Consequências positivas

A adoção da arquitetura SaaS Multi-Tenant oferece:

- uma única base de código;
- menor custo de manutenção;
- correções aplicadas para todos os clientes;
- facilidade para cadastrar novas empresas;
- identidade visual configurável;
- possibilidade de habilitar funcionalidades por empresa;
- redução da necessidade de builds individuais;
- melhor escalabilidade do produto;
- backend baseado em PostgreSQL;
- autenticação integrada;
- armazenamento centralizado de imagens;
- possibilidade de aplicar segurança por Tenant utilizando RLS;
- manutenção do Room para funcionamento offline;
- arquitetura preparada para futuras integrações.

---

## Pontos de atenção

A arquitetura Multi-Tenant também aumenta algumas responsabilidades.

Será necessário cuidado especial com:

- isolamento rigoroso dos dados;
- autenticação;
- autorização;
- regras RLS;
- associação correta entre usuário e Tenant;
- sincronização entre Room e Supabase;
- cache local associado ao Tenant correto;
- troca de usuário;
- logout;
- gerenciamento seguro de imagens;
- tratamento de falhas de conexão;
- custos de infraestrutura conforme o crescimento da plataforma.

---

## Alternativas consideradas

### Projeto separado para cada empresa

Rejeitado como estratégia principal.

Esse modelo aumentaria:

- duplicação de código;
- quantidade de builds;
- custo de manutenção;
- risco de versões divergentes;
- esforço de publicação.

---

### Product Flavors para todas as empresas

Não será utilizado como estratégia principal.

Poderá ser utilizado futuramente para clientes específicos que necessitem de white-label completo.

---

### SaaS Multi-Tenant

Escolhido como arquitetura principal.

Esse modelo permite que múltiplas empresas utilizem uma única aplicação e uma única base de código, mantendo identidade visual, configurações e dados separados por Tenant.

---

## Tecnologias relacionadas à decisão

A arquitetura utilizará inicialmente:

```text
Android
├── Kotlin
├── Jetpack Compose
├── Material Design 3
├── Navigation Compose
├── MVVM
├── Coroutines
├── Flow / StateFlow
├── Room
└── Coil

Backend
└── Supabase
    ├── PostgreSQL
    ├── Auth
    ├── Storage
    └── Row Level Security
```

---

## Resultado

O PetLife passa a ser projetado como uma plataforma:

**SaaS Multi-Tenant**

com:

- uma única aplicação principal;
- múltiplas empresas;
- identidade visual dinâmica;
- dados isolados por Tenant;
- Supabase como backend remoto;
- PostgreSQL como banco remoto;
- Supabase Auth para autenticação;
- Supabase Storage para logos, banners e imagens;
- Row Level Security para isolamento dos dados;
- Room como persistência local e suporte offline;
- possibilidade futura de distribuição white-label.

A evolução será incremental, preservando a aplicação existente durante toda a migração.