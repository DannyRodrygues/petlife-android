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

Era necessário implementar isolamento local dos dados sem perder registros existentes.

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