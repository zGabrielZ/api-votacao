# API Votação

API para cadastro de pautas, abertura de sessões de votação, registro de votos e consulta do resultado final.

## Visão geral

O projeto foi desenvolvido em Java com Spring Boot e segue o padrão MVC + JPA + Flyway. Ele permite:

- cadastrar pautas;
- abrir sessões de votação vinculadas a uma pauta;
- registrar votos de associados;
- consultar o resultado final da sessão;
- fechar automaticamente sessões expiradas por scheduler.

## Stack

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Flyway
- MapStruct
- Lombok
- JUnit 5 + Spring Test
- H2 para testes

## Estrutura do projeto

- `backend/` — aplicação principal
- `backend/src/main/java` — código da API
- `backend/src/main/resources` — configurações, migrations e dados de teste
- `backend/src/test/java` — testes unitários e de integração
- `backend/src/test/resources` — configuração e scripts do profile de teste
- `infrastructure/` — configuração do banco em Docker
- `PostmanCollections/` — coleções para testes manuais

## Pré-requisitos

- Java 17+
- Maven 3.9+
- Docker e Docker Compose

## Banco de dados

O projeto usa PostgreSQL em desenvolvimento e H2 em testes.

### PostgreSQL local via Docker

No diretório `infrastructure`, execute:

```bash
docker compose up -d
```

A configuração do banco do compose está em `infrastructure/docker-compose.yaml` e expõe:

- host: `localhost`
- porta: `5000`
- banco: `votacao-dev`
- usuário: `admin`
- senha: `admin`

A aplicação usa o profile `dev`, configurado em `backend/src/main/resources/application-dev.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5000/votacao-dev?currentSchema=dev
    username: admin
    password: admin
  flyway:
    enabled: true
    schemas: dev
```

As migrations ficam em:

- `backend/src/main/resources/db/migration`

## Executando a API

Na pasta `backend`:

```bash
mvn spring-boot:run
```

A aplicação inicia com:

- porta: `8080`
- contexto: `/api`

Base URL:

```text
http://localhost:8080/api
```

## Endpoints da API

Todos os endpoints são acessados com o prefixo `/api`.

### 1) Criar pauta

```http
POST /api/v1/agenda
```

Exemplo de body:

```json
{
  "title": "Pauta de Teste",
  "description": "Descrição da pauta"
}
```

Resposta: `201 Created` com o objeto da pauta criada.

### 2) Abrir sessão de votação

```http
POST /api/v1/agenda/{agendaId}/voting-sessions
```

Exemplo de body:

```json
{
  "votingStartTime": "2026-09-27T18:00:00Z",
  "votingEndTime": "2026-09-27T19:00:00Z"
}
```

Resposta: `201 Created` com os dados da sessão criada.

### 3) Registrar voto

```http
POST /api/v1/voting-sessions/{votingSessionId}/votes
```

Exemplo de body:

```json
{
  "associateId": "4f0d9d4f-7c5c-4d7d-bfa4-0d7f0a13ee2a",
  "voteOption": "YES"
}
```

Observações:

- `voteOption` aceita apenas `YES` ou `NO`;
- cada associado só pode votar uma vez por sessão;
- o associado precisa existir previamente no banco;
- a sessão precisa estar aberta para receber votos.

### 4) Consultar resultado da votação

```http
GET /api/v1/voting-sessions/{votingSessionId}/results
```

Resposta esperada:

```json
{
  "yesVotes": 2,
  "noVotes": 1,
  "totalVotes": 3
}
```

## Regras de negócio

- uma pauta pode ter uma ou mais sessões de votação;
- a sessão só pode ser criada se a data final for posterior à data inicial;
- um associado não pode votar mais de uma vez na mesma sessão;
- o cálculo do resultado depende do status da sessão;
- as sessões expiradas são fechadas automaticamente pelo scheduler.

## Associado

A API não expõe um endpoint público para criação de associado no momento. O registro do associado deve ser inserido no banco, por seed/migration ou via dado de teste, antes de registrar um voto.

Exemplo de insert:

```sql
INSERT INTO TB_ASSOCIATE (
  ID_EXTERNAL_UUID,
  NAME,
  EMAIL,
  PASSWORD,
  DOCUMENT_NUMBER,
  CREATED_AT,
  UPDATED_AT
) VALUES (
  '4f0d9d4f-7c5c-4d7d-bfa4-0d7f0a13ee2a',
  'Maria Silva',
  'maria@email.com',
  '123456',
  '06579044063',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);
```

## Scheduler de fechamento de sessões

A aplicação possui um scheduler para encerrar automaticamente sessões vencidas. A configuração está em `backend/src/main/resources/application-dev.yaml`:

```yaml
voting-session:
  scheduler:
    enabled: true
    interval: 60000
```

Esse scheduler verifica periodicamente sessões em aberto cujo tempo de encerramento já passou e atualiza o status para `CLOSED`.

## Testes

Para rodar a suíte de testes:

```bash
cd backend
mvn test
```

Para um teste específico:

```bash
cd backend
mvn -Dtest=VoteControllerIntegrationTest test
```

## Profile de teste

O perfil de teste usa H2 em memória e desativa o Flyway:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create-drop
  flyway:
    enabled: false
```

Os scripts de seed do H2 ficam em `backend/src/test/resources/import.sql`.

## Observações

- o projeto usa `/api` como contexto global;
- o banco local de desenvolvimento é executado via Docker;
- o README pode ser atualizado conforme novas rotas e recursos forem adicionados.
- os scripts SQL de migration ficam em `backend/src/main/resources/db/migration` e são executados automaticamente pelo Flyway.
- o Swagger facilita o teste manual dos endpoints diretamente no navegador.

## Dica de uso

A maneira mais prática para testar a API é:

1. subir o banco com Docker
2. inserir um associado no banco
3. criar uma pauta
4. abrir uma sessão de votação
5. registrar votos
6. consultar resultado da votação

## Autor

Gabriel Ferreira

https://www.linkedin.com/in/gabriel-ferreira-4b817717b/
