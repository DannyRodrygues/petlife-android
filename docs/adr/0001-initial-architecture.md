# ADR 0001 — Arquitetura inicial do PetLife

## Status

Aceito.

## Contexto

O PetLife será um aplicativo Android de portfólio com persistência local,
consumo de API REST, autenticação e funcionalidades offline.

O projeto precisa ser organizado, testável e capaz de crescer sem concentrar
regras de negócio nas telas.

## Decisão

O projeto utilizará arquitetura em camadas, iniciando com:

- camada de interface;
- camada de dados.

A camada de domínio será adicionada quando existirem regras de negócio
complexas ou reutilizáveis.

A interface será desenvolvida com Jetpack Compose.

Os estados das telas serão gerenciados por ViewModels utilizando StateFlow.

O acesso aos dados será realizado por meio de repositórios.

## Consequências

### Benefícios

- Separação de responsabilidades
- Facilidade de manutenção
- Melhor testabilidade
- Menor acoplamento entre interface e dados
- Evolução gradual da arquitetura

### Custos

- Maior quantidade de arquivos
- Necessidade de definir responsabilidades com clareza
- Curva de aprendizado maior que um projeto Android simples

## Evolução

Esta decisão define a arquitetura inicial do projeto.

Decisões posteriores detalham a evolução da infraestrutura e da arquitetura:

- ADR 0003 — Arquitetura SaaS Multi-Tenant
- ADR 0004 — Autenticação e Resolução do Tenant

A adoção do Supabase e da arquitetura Multi-Tenant complementa esta decisão sem alterar os princípios de separação de responsabilidades, uso de ViewModels, StateFlow e Repository Pattern.