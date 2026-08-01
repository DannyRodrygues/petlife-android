# Design System — PetLife

## Status

Versão inicial em desenvolvimento.

## Objetivo

O Design System do PetLife define os padrões visuais e de interação
utilizados no aplicativo.

Seu objetivo é manter consistência entre telas, componentes, cores,
tipografia, espaçamentos, imagens e elementos da marca.

---

## Identidade da marca

O PetLife é um aplicativo voltado à organização dos cuidados e da rotina
de animais de estimação.

A identidade visual deve transmitir:

- cuidado;
- confiança;
- acolhimento;
- organização;
- leveza;
- proximidade com os animais.

---

## Cores

### Roxo principal

Utilizado em ações primárias, títulos de destaque e elementos principais
da marca.

| Nome | Hexadecimal |
|---|---|
| PetLife Purple | `#7656B5` |
| PetLife Purple Dark | `#4F378B` |
| PetLife Purple Light | `#E9DFF7` |

### Azul PetLife

Cor secundária inspirada na logomarca do projeto.

| Nome | Hexadecimal |
|---|---|
| PetLife Blue | `#5ACDDA` |
| PetLife Blue Dark | `#33B8C8` |
| PetLife Blue Light | `#DDF7FB` |

### Cores neutras

| Nome | Hexadecimal |
|---|---|
| Background | `#F9F7FC` |
| Surface | `#FFFFFF` |
| Text Primary | `#25232A` |
| Text Secondary | `#625F69` |

---

## Aplicação das cores

### Primary

Usada em:

- botão principal;
- títulos de destaque;
- estado ativo;
- indicadores de navegação;
- elementos principais da logomarca.

### Secondary

Usada em:

- ações secundárias;
- detalhes visuais;
- ícones complementares;
- indicadores positivos;
- elementos de apoio da logomarca.

### Background e Surface

- `Background` representa o fundo geral das telas.
- `Surface` representa cards, caixas de diálogo e componentes elevados.

---

## Tipografia

A primeira versão utilizará a tipografia padrão do Material Design 3.

Hierarquia inicial:

| Uso | Estilo Compose |
|---|---|
| Título principal | `headlineMedium` |
| Título de seção | `titleLarge` |
| Título de card | `titleMedium` |
| Texto principal | `bodyLarge` |
| Texto auxiliar | `bodyMedium` |
| Texto pequeno | `bodySmall` |
| Botões | `labelLarge` |

Uma fonte personalizada poderá ser adicionada futuramente após validação
de legibilidade, licença e impacto no tamanho do aplicativo.

---

## Espaçamentos

O projeto utilizará uma escala baseada em múltiplos de 4 dp.

| Token | Valor |
|---|---|
| Extra Small | `4.dp` |
| Small | `8.dp` |
| Medium | `16.dp` |
| Large | `24.dp` |
| Extra Large | `32.dp` |
| Extra Extra Large | `48.dp` |

O espaçamento padrão horizontal das telas será inicialmente de `24.dp`.

---

## Formas

Os componentes devem transmitir leveza e acolhimento.

Diretrizes iniciais:

- botões com cantos arredondados;
- cards com cantos entre 16 dp e 24 dp;
- imagens de pets com recorte arredondado;
- campos de formulário com aparência suave;
- evitar componentes excessivamente quadrados.

---

## Tela de Boas-vindas

Diretrizes aplicadas:

- logomarca centralizada;
- ausência de título textual duplicado;
- roxo como cor principal;
- descrição centralizada;
- ação principal em botão preenchido;
- ação secundária em botão com contorno;
- layout centralizado e limitado em largura.

---

## Componentes planejados

### Botão primário

Usado para a principal ação da tela.

Exemplos:

- Entrar;
- Salvar;
- Cadastrar pet;
- Confirmar agendamento.

## Componentes implementados

## Tela de Login

Diretrizes aplicadas:

- logomarca centralizada no topo;
- roxo para ações principais;
- azul PetLife para links e ações secundárias;
- campos com cantos arredondados;
- suporte a rolagem e teclado;
- layout limitado a 420 dp de largura.

### PetLifePrimaryButton

Botão utilizado para a ação principal da tela.

Características:

- largura adaptável;
- altura padronizada;
- cantos arredondados;
- tipografia `labelLarge`;
- utiliza a cor primária do tema.

### PetLifeOutlinedButton

Botão utilizado para ações secundárias.

Características:

- largura adaptável;
- altura padronizada;
- aparência de contorno;
- cantos arredondados;
- tipografia `labelLarge`.

### Botão secundário

Usado para ações alternativas.

Exemplos:

- Criar conta;
- Voltar;
- Cancelar;
- Editar.

### Pet Card

Componente para apresentar:

- foto;
- nome;
- espécie;
- raça;
- próximo compromisso.

### Appointment Card

Componente para apresentar:

- pet relacionado;
- tipo de compromisso;
- data;
- horário;
- status.

### Empty State

Componente utilizado quando não existem dados cadastrados.

Deve conter:

- ilustração;
- mensagem curta;
- ação recomendada.

---

## Imagens e ilustrações

O PetLife utilizará imagens ou ilustrações de animais para tornar
a experiência mais acolhedora.

Diretrizes:

- utilizar apenas imagens próprias ou com licença compatível;
- manter consistência de estilo;
- otimizar arquivos para dispositivos móveis;
- fornecer descrição para acessibilidade;
- evitar imagens meramente decorativas quando não agregarem valor;
- armazenar a origem e a licença das imagens utilizadas.

---

## Logomarca

A logomarca deverá combinar elementos relacionados a:

- cuidado;
- vida;
- animais;
- organização.

Direção visual inicial:

- símbolo baseado em pata;
- elemento de coração integrado ao símbolo;
- roxo como cor predominante;
- verde Tiffany como cor complementar;
- versão completa com o nome PetLife;
- versão reduzida para ícone do aplicativo;
- versão monocromática;
- versão para fundo claro e fundo escuro.

---

## Acessibilidade

Todos os componentes deverão considerar:

- contraste adequado;
- textos legíveis;
- áreas de toque suficientes;
- descrição de imagens relevantes;
- estados que não dependam apenas de cor;
- suporte a modo claro e modo escuro.