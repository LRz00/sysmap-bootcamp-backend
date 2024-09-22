
# Projeto de E-commerce de Discos de Vinil - README

## Visão Geral

Este projeto é um serviço de back-end para um e-commerce de discos de vinil. Ele implementa um programa de fidelidade baseado em pontos para aumentar o volume de vendas e fidelizar clientes. O sistema aplica pontos aos clientes com base no dia da semana em que a compra é realizada, conforme uma tabela de pontos definida.

## Funcionalidades Principais

- **Sistema de Pontos:** O valor dos pontos atribuídos a uma compra varia de acordo com o dia da semana.
- **Wallet:** Ao debitar o valor da compra da carteira (Wallet) do cliente, os pontos são adicionados.
- **Controle de Transações:** Permite a visualização de métricas de vendas e transações.
- **Autenticação JWT:** Para segurança e autenticação do usuário.
- **Manipulação de Álbuns do Spotify:** A integração com a API do Spotify é usada para buscar e exibir álbuns para compra.
- **Documentação Swagger:** Todos os endpoints estão documentados usando Swagger.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Security com JWT**
- **Spring Data JPA**
- **PostgreSQL** como banco de dados
- **Spotify API** para integração de álbuns
- **Swagger** para documentação de API
- **Docker** para containerização
- **JUnit e Mockito** para testes unitários

## Endpoints Principais

1. **Autenticação:**
   - `POST /user/auth`: Autentica o usuário e retorna um token JWT.
   - `POST /user/signUp`: Cria um novo usuário no sistema.

2. **Álbuns:**
   - `GET /albums/all`: Retorna todos os álbuns do Spotify baseados em uma string de busca.
   - `POST /albums/sale`: Realiza a compra de um álbum e debita o valor da carteira do cliente, adicionando pontos.

3. **Transações:**
   - `GET /transactions/metrics`: Retorna todas as métricas de transações.
   - `GET /transactions/metrics/high`: Retorna métricas de transações com valor acima de 50.
   - `GET /transactions/metrics/low`: Retorna métricas de transações com valor abaixo de 50.
   - `GET /transactions/metrics/sunday-hits`: Retorna transações feitas aos domingos.

4. **Wallet:**
   - `POST /wallet/credit`: Adiciona crédito à carteira de um cliente.
   - `GET /wallet/{walletId}`: Retorna as informações de uma carteira específica.

## Validações Implementadas

- **Álbum não duplicado:** Não é permitido salvar um álbum com o mesmo `idSpotify` para o mesmo usuário.
- **E-mail único:** Não é permitido salvar um usuário com um e-mail já cadastrado.
- **Pontos automáticos:** O sistema de pontos é implementado com base no dia da semana da compra.
- **Não exclusão de álbuns:** Os álbuns não são excluídos do sistema, mas marcados como removidos.
- **ExceptionHandler:** Um controlador de exceções foi implementado para gerenciar erros adequadamente.

## Como Executar o Projeto

### Pré-requisitos

- **Java 17**
- **PostgreSQL**
- **Maven**
- **Docker**

### Build da Aplicação (Com Docker)

1. Clone o repositório:

   ```bash
   git clone <link-do-repositorio>
   ```

2. Configure o banco de dados no arquivo `application.yml` com suas credenciais do PostgreSQL.

3. **Gerar o JAR do projeto** usando o Maven:

   ```bash
   mvn clean install
   ```


### Swagger

Acesse a documentação Swagger em `http://localhost:8080/swagger-ui.html` para visualizar e testar os endpoints.

## Testes Unitários

- A camada de serviço e controladores está coberta por testes unitários utilizando **JUnit** e **Mockito**.
- Para rodar os testes: 

  ```bash
  mvn test
  ```

## Contato

Para mais informações ou assistência, entre em contato através do e-mail do desenvolvedor: **lararods73@gmail.com**.
