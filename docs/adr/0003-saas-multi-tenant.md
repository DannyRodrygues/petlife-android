# ADR 0003 — Arquitetura SaaS Multi-Tenant

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

Manter uma versão separada do aplicativo para cada empresa aumentaria significativamente o custo de desenvolvimento, manutenção, testes e publicação.

Cada correção de bug, nova funcionalidade ou alteração estrutural teria que ser replicada em múltiplas versões do aplicativo.

Além disso, diferentes empresas podem precisar de:

- logomarca própria;
- banner próprio;
- cores próprias;
- nome próprio;
- usuários próprios;
- configurações específicas;
- módulos habilitados ou desabilitados;
- dados isolados das demais empresas.

Por esse motivo, o PetLife precisa permitir personalização e isolamento por empresa sem exigir uma nova base de código para cada cliente.

---

## Problema

O modelo inicial do PetLife possuía uma identidade visual única e não precisava distinguir empresas diferentes.

Esse modelo não seria suficiente para atender múltiplos clientes.

Precisamos permitir que:

```text
Empresa A
├── usuários próprios
├── identidade visual própria
└── dados próprios

Empresa B
├── usuários próprios
├── identidade visual própria
└── dados próprios
```

utilizem o mesmo aplicativo Android.

A solução também precisa evitar que:

```text
Empresa A
```

consiga acessar dados pertencentes a:

```text
Empresa B
```

A separação precisa existir tanto na aplicação quanto no backend.

---

## Decisão

O PetLife será desenvolvido como uma plataforma:

**SaaS Multi-Tenant**

Uma única aplicação atenderá múltiplas empresas.

Cada empresa será representada por um:

```text
Tenant
```

Cada usuário estará associado a um `tenantId`, responsável por identificar a empresa à qual pertence.

O aplicativo utilizará esse `tenantId` para determinar:

- identidade visual;
- configurações;
- permissões;
- módulos disponíveis;
- dados pertencentes à empresa.

O modelo principal de distribuição será uma única aplicação PetLife publicada na Google Play Store.

A personalização será carregada dinamicamente de acordo com o Tenant associado ao usuário autenticado.

---

## Modelo conceitual

A estrutura principal será:

```text
PetLife
   │
   ├── Tenant A
   │    ├── usuários
   │    ├── branding
   │    └── dados
   │
   ├── Tenant B
   │    ├── usuários
   │    ├── branding
   │    └── dados
   │
   └── Tenant C
        ├── usuários
        ├── branding
        └── dados
```

A aplicação Android permanece a mesma para todos os Tenants.

---

## Tenant

O Tenant representa uma empresa dentro da plataforma.

Estrutura conceitual:

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
- demais dados de negócio.

---

## Identificação do Tenant

O Tenant não deve ser identificado através de um nome fixo no código Android.

A identificação deverá ocorrer através do usuário autenticado.

Fluxo:

```text
Login
  ↓
Supabase Auth
  ↓
auth.users
  ↓
profiles
  ↓
tenant_id
  ↓
Tenant
```

A implementação detalhada desse fluxo é documentada no:

```text
ADR 0004 — Autenticação e Resolução do Tenant
```

---

## BrandConfig

Cada Tenant poderá possuir sua própria identidade visual.

Essa configuração será representada pelo:

```text
BrandConfig
```

Estrutura conceitual:

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

A configuração permite alterar dinamicamente:

- cor principal;
- cor secundária;
- cor terciária;
- logomarca;
- banner;
- nome exibido da empresa.

---

## Tema dinâmico

As telas não devem conhecer diretamente as cores específicas de cada empresa.

Os componentes continuam utilizando o tema da aplicação:

```kotlin
MaterialTheme.colorScheme.primary
```

ou:

```kotlin
MaterialTheme.colorScheme.secondary
```

O `BrandConfig` do Tenant ativo é utilizado para configurar o `PetLifeTheme`.

Fluxo:

```text
Tenant
   ↓
BrandConfig
   ↓
PetLifeTheme
   ↓
MaterialTheme
   ↓
UI
```

Isso permite alterar a identidade visual sem modificar individualmente cada tela.

---

## Identidade visual padrão do PetLife

A identidade visual original do PetLife continuará existindo dentro do aplicativo.

Ela funciona como:

- identidade padrão;
- configuração local inicial;
- fallback visual;
- proteção contra falhas temporárias de rede;
- proteção contra ausência de branding remoto.

A configuração padrão é representada por:

```text
PetLifeDefaultTenant
```

---

## Fallback local

O fallback poderá ser utilizado quando:

- ainda não houver usuário autenticado;
- o Tenant ainda estiver carregando;
- a configuração remota estiver indisponível;
- ocorrer erro de rede;
- `logoPath` estiver ausente;
- `bannerPath` estiver ausente.

Exemplo:

```text
Tenant remoto indisponível
        ↓
PetLifeDefaultTenant
        ↓
interface continua funcionando
```

O fallback existe para estabilidade visual.

Ele não deve ser utilizado como mecanismo de autorização.

---

## Branding remoto

Cores e referências dos recursos visuais são armazenadas no Supabase.

A tabela:

```text
brand_configs
```

possui os dados necessários para montar o branding do Tenant.

Exemplo conceitual:

```text
brand_configs
-------------------------
tenant_id
primary_color
secondary_color
tertiary_color
logo_path
banner_path
created_at
updated_at
```

---

## Supabase Storage

Logomarcas e banners específicos dos Tenants são armazenados no Supabase Storage.

Foi criado o bucket:

```text
tenant-branding
```

A estrutura adotada é:

```text
tenant-branding/
│
├── {tenantId}/
│   └── branding/
│       ├── logo.png
│       └── banner.png
│
└── {outroTenantId}/
    └── branding/
        ├── logo.png
        └── banner.png
```

Cada Tenant possui sua própria pasta.

O PostgreSQL não armazena os arquivos diretamente.

A tabela `brand_configs` armazena apenas os paths relativos.

Exemplo:

```text
logo_path =
{tenantId}/branding/logo.png

banner_path =
{tenantId}/branding/banner.png
```

---

## Bucket de branding

O bucket `tenant-branding` é público porque contém apenas recursos institucionais destinados à exibição pública, como:

- logomarcas;
- banners;
- imagens de identidade visual.

Arquivos privados de negócio não deverão utilizar esse mesmo modelo.

Por exemplo, fotos de Pets deverão futuramente utilizar uma estratégia separada e protegida.

---

## Componentes de branding

Para evitar duplicação da lógica de Storage nas telas, foram criados componentes reutilizáveis.

### PetLifeBrandLogo

Responsável por:

```text
Tenant
  ↓
logoPath
  ↓
Supabase Storage
  ↓
logomarca
```

Se não existir logomarca remota:

```text
R.drawable.logo_petlife
```

é utilizado como fallback.

---

### PetLifeBrandBanner

Responsável por:

```text
Tenant
  ↓
bannerPath
  ↓
Supabase Storage
  ↓
banner
```

Se não existir banner remoto:

```text
R.drawable.home_banner
```

é utilizado como fallback.

O banner mantém a proporção visual definida pelo aplicativo:

```text
1080 × 420
```

---

## Persistência

O PetLife utilizará duas camadas principais de persistência:

```text
Room
+
Supabase
```

Cada uma possui responsabilidades diferentes.

---

## Persistência local

O Room continuará sendo utilizado no Android para:

- armazenamento local;
- resposta rápida da interface;
- cache;
- funcionamento offline;
- persistência temporária durante indisponibilidade de rede.

O Room não será removido com a adoção do Supabase.

---

## Persistência remota

O Supabase será utilizado como backend principal da plataforma.

Será responsável por:

- banco PostgreSQL;
- autenticação;
- armazenamento remoto;
- controle de acesso;
- dados compartilhados entre dispositivos;
- isolamento remoto entre Tenants.

---

## Arquitetura de persistência

Modelo pretendido:

```text
                     Repository
                         │
              ┌──────────┴──────────┐
              │                     │
              ▼                     ▼
       LocalDataSource        RemoteDataSource
              │                     │
             Room                Supabase
```

O Supabase deverá funcionar como fonte oficial dos dados compartilhados.

O Room funcionará como camada local.

---

## Offline-first

A arquitetura deverá evoluir para permitir funcionamento offline.

Modelo pretendido:

```text
Supabase
   ↓
sincronização
   ↓
Room
   ↓
UI
```

Sem conexão:

```text
Sem internet
    ↓
Room
    ↓
dados locais
    ↓
aplicativo continua funcionando
```

A estratégia completa de sincronização ainda será definida em decisão arquitetural específica.

---

## Supabase

O Supabase foi escolhido como backend remoto principal.

Os recursos utilizados são:

```text
Supabase
├── PostgreSQL
├── Auth
├── Storage
└── Row Level Security
```

---

## PostgreSQL

O PostgreSQL armazenará os dados estruturados.

Entre as entidades previstas estão:

- Tenants;
- BrandConfigs;
- Profiles;
- Pets;
- Vacinas;
- Consultas;
- Medicamentos;
- Histórico de peso;
- Configurações;
- demais dados de negócio.

O modelo relacional é adequado ao PetLife devido aos relacionamentos entre essas entidades.

---

## Modelo relacional conceitual

```text
Tenant
   │
   ├── Profiles
   │
   ├── BrandConfig
   │
   └── Pets
        │
        ├── Vaccines
        ├── Appointments
        ├── Medications
        └── WeightHistory
```

---

## Supabase Auth

O Supabase Auth será utilizado para autenticação.

Após a autenticação, o usuário será relacionado ao Tenant correspondente.

Fluxo:

```text
Usuário
   ↓
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
Aplicativo personalizado
```

A implementação de autenticação e resolução do Tenant é detalhada no ADR 0004.

---

## Profiles

Os dados complementares dos usuários são armazenados na tabela:

```text
profiles
```

O relacionamento principal é:

```text
auth.users.id
      ↓
profiles.id
      ↓
profiles.tenant_id
      ↓
tenants.id
```

Cada Profile pertence a um Tenant.

---

## Roles

Inicialmente estão previstas as roles:

```text
SUPER_ADMIN
COMPANY_ADMIN
USER
```

As permissões associadas às roles serão evoluídas conforme as necessidades da plataforma.

---

## Segurança Multi-Tenant

O isolamento dos dados entre empresas é obrigatório.

O objetivo é garantir:

```text
Tenant A
   ↓
somente dados do Tenant A

Tenant B
   ↓
somente dados do Tenant B
```

Um Tenant não deverá acessar dados pertencentes a outro Tenant.

---

## tenantId nos dados de negócio

Entidades de negócio deverão possuir associação com um Tenant.

Exemplo futuro:

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

O mesmo princípio deverá ser aplicado a:

- Vacinas;
- Consultas;
- Medicamentos;
- Peso;
- demais entidades pertencentes à empresa.

---

## Estado atual do isolamento de dados

O isolamento da identidade visual já está implementado e validado.

O isolamento dos dados de negócio ainda não está concluído.

Atualmente, dados locais persistidos no Room, como Pets e Vacinas, ainda não possuem isolamento completo por `tenantId`.

Isso significa que, no mesmo dispositivo, um usuário de outro Tenant ainda pode visualizar registros anteriormente armazenados localmente.

Exemplo atual:

```text
PetLife
   ↓
Jackie

Clínica Bicho Feliz
   ↓
Jackie também aparece
```

Esse comportamento é conhecido e será tratado na próxima etapa arquitetural.

---

## Row Level Security — RLS

O PetLife utilizará Row Level Security do PostgreSQL/Supabase para garantir o isolamento remoto.

A segurança não deve depender apenas de filtros no Android.

Por exemplo:

```kotlin
pets.filter {
    it.tenantId == currentTenantId
}
```

não é suficiente como mecanismo de segurança.

O backend também deverá impedir acesso indevido.

---

## Princípio de segurança

O objetivo é:

```text
Usuário autenticado
tenantId = Tenant A
        ↓
pode acessar
        ↓
dados do Tenant A
```

mas não:

```text
dados do Tenant B
```

As políticas RLS serão responsáveis por reforçar essa restrição no backend.

---

## Segurança do cliente Android

O aplicativo Android utiliza apenas a:

```text
Supabase Publishable Key
```

O aplicativo não deve conter:

- service_role;
- secret key;
- credenciais administrativas;
- acesso irrestrito ao banco.

Operações administrativas não devem depender de credenciais armazenadas no aplicativo.

---

## Fluxo de autenticação e carregamento da empresa

O fluxo adotado é:

```text
Usuário
   ↓
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
Tema
   ↓
Logo
   ↓
Banner
   ↓
PetLife personalizado
```

---

## Distribuição

O modelo principal de distribuição será:

**um único aplicativo PetLife publicado na Google Play Store.**

Todas as empresas utilizarão a mesma aplicação.

Após a autenticação, o aplicativo identifica o Tenant e carrega sua configuração.

```text
Google Play Store
       ↓
PetLife
       ↓
Login
       ↓
tenantId
   ┌────┼────┐
   ↓    ↓    ↓
   A    B    C
   ↓    ↓    ↓
Brand Brand Brand
```

Correções e novas funcionalidades poderão ser distribuídas através de uma única versão.

---

## White-label completo

Poderá existir futuramente uma modalidade especial de white-label.

Nesse modelo, determinadas empresas poderão possuir:

- nome próprio do aplicativo;
- ícone próprio;
- `applicationId` próprio;
- configuração própria de publicação;
- publicação independente na Google Play Store.

Essas versões deverão reutilizar a mesma base de código.

Product Flavors poderão ser utilizados caso essa modalidade seja necessária.

O white-label completo não será a estratégia principal.

A estratégia principal continuará sendo o SaaS Multi-Tenant.

---

## Administração da plataforma

Será prevista futuramente uma área administrativa.

O administrador principal poderá:

- cadastrar empresas;
- editar empresas;
- configurar branding;
- enviar logomarcas;
- enviar banners;
- configurar cores;
- cadastrar administradores;
- ativar ou desativar empresas;
- administrar módulos;
- administrar configurações.

Fluxo conceitual:

```text
Super Admin
    ↓
Cadastrar empresa
    ↓
Tenant
    ↓
BrandConfig
    ↓
Administrador da empresa
    ↓
Supabase
```

Esse painel poderá futuramente ser desenvolvido como aplicação web separada.

---

## Configuração de módulos por Tenant

A arquitetura poderá futuramente permitir módulos diferentes para cada empresa.

Exemplo:

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

Vacinas       = true
Consultas     = true
Medicamentos  = true
Peso          = true
```

Outra empresa poderia utilizar:

```text
Pet Shop Amigo Fiel

Vacinas       = true
Consultas     = true
Medicamentos  = false
Peso          = false
```

Essa funcionalidade será implementada apenas quando houver necessidade concreta.

---

## Estratégia de migração

A transformação do PetLife em SaaS Multi-Tenant será incremental.

O aplicativo existente não será reescrito.

O primeiro Tenant da plataforma é o próprio:

```text
PetLife
```

Sua identidade visual atual permanece como configuração padrão e fallback.

---

## Etapas da migração

A estratégia definida foi:

1. criar `TenantConfig`;
2. criar `BrandConfig`;
3. criar `PetLifeDefaultTenant`;
4. adaptar o tema para branding dinâmico;
5. preservar logo e banner atuais como fallback;
6. integrar Android ao Supabase;
7. criar estrutura remota de Tenant;
8. criar BrandConfig remoto;
9. configurar Supabase Storage;
10. implementar Supabase Auth;
11. criar `profiles`;
12. associar usuários a `tenantId`;
13. resolver Tenant através do usuário autenticado;
14. carregar branding remotamente;
15. validar múltiplos Tenants;
16. adicionar `tenantId` aos dados de negócio;
17. implementar isolamento local;
18. implementar RLS para dados privados;
19. implementar sincronização entre Supabase e Room.

---

## Estado da migração

Atualmente:

```text
TenantConfig                         ✅
BrandConfig                          ✅
PetLifeDefaultTenant                 ✅
Tema dinâmico                        ✅
Supabase                             ✅
PostgreSQL                           ✅
Storage                              ✅
Supabase Auth                        ✅
Profiles                             ✅
Resolução do Tenant                  ✅
Branding remoto                      ✅
Segundo Tenant real                  ✅
Teste de múltiplas empresas          ✅
Isolamento visual                    ✅

tenantId em Pets                     🚧
tenantId em Vacinas                  🚧
Isolamento local de dados            🚧
RLS para dados privados              🚧
Sincronização Room ↔ Supabase        🚧
```

---

## Validação realizada

A arquitetura foi validada utilizando dois Tenants reais:

```text
PetLife
```

e:

```text
Clínica Bicho Feliz
```

Usuários diferentes foram associados a Tenants diferentes.

Após a autenticação, o mesmo aplicativo conseguiu carregar automaticamente:

- nome da empresa;
- cor principal;
- cor secundária;
- cor terciária;
- logomarca;
- banner.

Não foi necessária alteração específica de código entre as empresas.

---

## Consequências positivas

A arquitetura SaaS Multi-Tenant oferece:

- uma única base de código;
- menor custo de manutenção;
- correções compartilhadas;
- distribuição centralizada;
- facilidade para cadastrar novas empresas;
- branding configurável;
- possibilidade de módulos por Tenant;
- redução de builds individuais;
- melhor escalabilidade;
- backend relacional;
- autenticação integrada;
- Storage centralizado;
- possibilidade de segurança com RLS;
- manutenção do Room;
- preparação para offline-first.

---

## Consequências negativas

A arquitetura aumenta a complexidade do sistema.

Agora é necessário lidar com:

- Tenant;
- Auth;
- Profiles;
- BrandConfig;
- RLS;
- Storage;
- cache local;
- sincronização;
- sessão;
- troca de usuário;
- roles;
- isolamento de dados.

Também aumenta a necessidade de testes de segurança e consistência.

---

## Pontos de atenção

Será necessário cuidado especial com:

- isolamento rigoroso dos dados;
- autenticação;
- autorização;
- policies RLS;
- associação usuário → Tenant;
- cache associado ao Tenant correto;
- logout;
- troca de usuário;
- sincronização;
- falhas de conexão;
- imagens privadas;
- custos de infraestrutura.

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

Não adotado como estratégia principal.

Pode ser utilizado futuramente para clientes específicos que necessitem de white-label completo.

---

### SaaS Multi-Tenant

Escolhido como arquitetura principal.

Permite que múltiplas empresas utilizem uma única aplicação e uma única base de código, mantendo configuração e identidade próprias.

O isolamento dos dados será implementado progressivamente.

---

## Tecnologias relacionadas à decisão

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

## Relação com outros ADRs

### ADR 0001 — Arquitetura inicial do PetLife

Define os princípios iniciais de organização do aplicativo Android, incluindo separação de responsabilidades, ViewModels, StateFlow e Repositories.

---

### ADR 0002 — Identidade visual e Design System

Define a identidade visual original e o Design System do PetLife.

No modelo Multi-Tenant, essa identidade também funciona como configuração padrão e fallback.

---

### ADR 0004 — Autenticação e Resolução do Tenant

Detalha:

```text
Supabase Auth
      ↓
profiles
      ↓
tenant_id
      ↓
Tenant
```

---

### Futuro ADR 0005 — Isolamento de Dados por Tenant e Sincronização

Deverá definir:

- inclusão de `tenantId` nas entidades locais;
- migrations do Room;
- isolamento de Pets e Vacinas;
- policies RLS para dados privados;
- estratégia de sincronização Room ↔ Supabase;
- comportamento offline;
- troca de Tenant e usuário.

---

## Resultado

O PetLife passa a ser projetado como uma plataforma:

**SaaS Multi-Tenant**

com:

- uma única aplicação principal;
- múltiplas empresas;
- identidade visual dinâmica;
- autenticação por usuário;
- associação usuário → Tenant;
- Supabase como backend remoto;
- PostgreSQL como banco remoto;
- Supabase Auth para autenticação;
- Supabase Storage para branding;
- Row Level Security como mecanismo de segurança;
- Room como persistência local;
- arquitetura preparada para isolamento completo dos dados por Tenant;
- possibilidade futura de white-label.

A evolução continuará de forma incremental, preservando a aplicação existente e os dados já cadastrados durante a migração.

---

## Implementation Status

A arquitetura Multi-Tenant já foi parcialmente implementada e validada.

Já estão funcionando:

- Tenant e BrandConfig no Supabase;
- Supabase Auth;
- profiles associados aos usuários;
- resolução automática do Tenant;
- branding remoto;
- cores dinâmicas;
- logomarca dinâmica;
- banner dinâmico;
- fallback local;
- múltiplos Tenants reais;
- troca automática de identidade visual após login.

Ainda estão em implementação:

- isolamento de Pets por Tenant;
- isolamento de Vacinas por Tenant;
- `tenantId` nas entidades do Room;
- policies RLS para dados privados;
- sincronização Room ↔ Supabase.

Portanto, neste momento, o isolamento da identidade visual já está validado, enquanto o isolamento completo dos dados de negócio ainda constitui a próxima etapa da arquitetura.