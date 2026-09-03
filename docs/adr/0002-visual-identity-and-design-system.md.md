# ADR 0002 — Identidade visual e Design System

## Status

Aceita.

## Contexto

O PetLife precisa apresentar uma identidade própria e consistente, evitando
a aparência de um projeto baseado apenas no template padrão do Android Studio.

O aplicativo deverá utilizar cores, componentes, imagens e uma logomarca
coerentes entre todas as funcionalidades.

## Decisão

A identidade visual inicial utilizará:

- roxo e lilás como cores principais;
- azul Tiffany como cor secundária;
- fundos claros e neutros;
- componentes com formas arredondadas;
- imagens e ilustrações de pets;
- logomarca própria baseada em símbolos de cuidado e animais.

O projeto utilizará um Design System documentado e implementado
progressivamente com Jetpack Compose e Material Design 3.

As cores dinâmicas do Android permanecerão desativadas para preservar
a identidade da marca.

## Consequências

### Benefícios

- identidade visual consistente;
- componentes reutilizáveis;
- melhor experiência de uso;
- maior qualidade visual para o portfólio;
- facilidade para evoluir e revisar as telas.

### Custos

- necessidade de manter documentação e componentes sincronizados;
- trabalho adicional na criação e otimização de imagens;
- necessidade de testar contraste e acessibilidade;
- criação de variações da logomarca.

## Evolução Multi-Tenant

Com a adoção da arquitetura SaaS Multi-Tenant, definida no ADR 0003, a identidade visual original do PetLife passa a funcionar também como identidade padrão e fallback da aplicação.

Cada Tenant poderá sobrescrever dinamicamente:

- cor principal;
- cor secundária;
- cor terciária;
- logomarca;
- banner;
- nome exibido da empresa.

As telas continuarão utilizando `MaterialTheme` e os componentes do Design System, sem conhecer diretamente as cores específicas de cada empresa.

As cores dinâmicas fornecidas pelo Android permanecem desativadas. Isso não impede o branding dinâmico por Tenant, que é controlado pela própria aplicação através de `BrandConfig`.