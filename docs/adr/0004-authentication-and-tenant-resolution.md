# ADR 0004 — Autenticação e Resolução do Tenant

## Status

Aceito

## Contexto

O PetLife passou a utilizar uma arquitetura SaaS Multi-Tenant.

Cada usuário autenticado deve acessar apenas o ambiente da empresa à qual pertence.

Não é suficiente identificar o Tenant por nome fixo no aplicativo, pois diferentes usuários podem pertencer a empresas diferentes.

Também não é adequado armazenar essa associação apenas na interface ou em memória local.

Precisamos de uma forma persistente e segura de relacionar:

- usuário autenticado;
- empresa;
- tenantId;
- permissões do usuário;
- identidade visual correspondente.

## Decisão

Será utilizado o Supabase Auth para autenticação dos usuários.

Os dados complementares do usuário serão armazenados na tabela pública `profiles`.

A tabela `profiles` será vinculada a `auth.users` através do mesmo UUID do usuário.

Cada profile terá um `tenant_id`, responsável por identificar a empresa à qual o usuário pertence.

O fluxo adotado será:

```text
Login
  ↓
Supabase Auth
  ↓
auth.users.id
  ↓
profiles.id
  ↓
profiles.tenant_id
  ↓
tenants.id
  ↓
brand_configs
  ↓
TenantConfig
  ↓
UI personalizada