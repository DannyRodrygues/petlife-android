# PetLife

Aplicativo Android para organização da rotina e dos cuidados de pets.

## Status do projeto

Em desenvolvimento.

## Objetivo

O PetLife tem como objetivo auxiliar tutores no gerenciamento das
informações e dos cuidados dos seus animais de estimação.

O aplicativo permitirá registrar pets, consultas, vacinas, banhos,
medicamentos e outros compromissos relacionados à saúde e à rotina dos animais.

## Funcionalidades planejadas

- Cadastro e autenticação de usuários
- Cadastro e gerenciamento de pets
- Agenda de consultas e cuidados
- Controle de vacinas
- Registro de peso e informações de saúde
- Persistência local com Room
- Consumo de API REST
- Consulta automática de endereço por CEP
- Funcionamento offline
- Dashboard com próximos compromissos

## Tecnologias planejadas

- Kotlin
- Jetpack Compose
- Material Design 3
- MVVM
- Room
- Retrofit
- Hilt
- Coroutines
- Flow e StateFlow
- Firebase Authentication
- Navigation Compose
- JUnit

## Arquitetura

O projeto será desenvolvido utilizando uma arquitetura em camadas.

### Camada de interface

Responsável pelas telas, componentes visuais, estados da interface
e interação com o usuário.

### Camada de dados

Responsável pelo acesso ao banco de dados local, APIs e implementação
dos repositórios.

### Camada de domínio

Será adicionada quando houver regras de negócio reutilizáveis ou
suficientemente complexas para justificar sua separação.

## Requisitos atuais

- Android 8.0 ou superior
- Minimum SDK 26
- Kotlin
- Jetpack Compose

## Ambiente utilizado

- Ubuntu 24.04 LTS
- Android Studio Ladybug Feature Drop
- Java 21
- Dispositivo físico Android API 27

## Histórico inicial

- Configuração do ambiente Android
- Atualização do Java para a versão 21
- Ajuste de compatibilidade entre AndroidX, compileSdk e Android Gradle Plugin
- Configuração do Git
- Publicação inicial no GitHub
- Execução do aplicativo em dispositivo físico