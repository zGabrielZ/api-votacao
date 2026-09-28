# API Votação

API para cadastro de pautas, abertura de sessões de votação, recebimento de votos e cálculo do resultado final.

## Visão geral

Este projeto foi desenvolvido em Java com Spring Boot e usa:

- Java 17
- Spring Boot 4.x
- PostgreSQL
- Spring Data JPA
- Flyway
- MapStruct
- SpringDoc OpenAPI (Swagger)
- Docker Compose para subir o banco

## Estrutura do projeto

- `backend/` — aplicação principal
- `backend/src/main/java` — código fonte da API
- `backend/src/main/resources` — arquivos de configuração e migrations
- `backend/src/test/java` — testes unitários e de integração
- `infrastructure/docker-compose.yaml` — ambiente do banco PostgreSQL
- `PostmanCollections/` — coleções de testes da API

## Pré-requisitos

- Java 17+
- Maven
- Docker e Docker Compose
- PostgreSQL (opcional, se você não usar o Docker)

## Banco de dados

A aplicação usa PostgreSQL com as configurações definidas em:

- `backend/src/main/resources/application.yaml`

Configuração principal:

- URL: `jdbc:postgresql://localhost:5000/votacao-dev?currentSchema=dev`
- Usuário: `admin`
- Senha: `admin`
- Porta: `5000`

O Flyway executa os scripts em:

- `backend/src/main/resources/db/migration`

## Subindo o banco com Docker Compose

No diretório `infrastructure`, execute:

```bash
docker compose up -d
```

Ou, se preferir usar o arquivo explicitamente:

```bash
docker compose -f infrastructure/docker-compose.yaml up -d
```

Esse comando sobe um container PostgreSQL com:

- banco: `votacao-dev`
- usuário: `admin`
- senha: `admin`
- porta exposta: `5000`

## Executando a API

Na raiz do projeto backend:

```bash
cd backend
mvn spring-boot:run
```

A aplicação inicia na porta `8080` e com contexto `/api`.

Exemplo de URL base:

```text
http://localhost:8080/api
```

Swagger/OpenAPI:

```text
http://localhost:8080/api/swagger-ui/index.html
```

## Antes de testar as APIs

Antes de chamar endpoints que dependem de associado, você precisa inserir um registro na tabela `TB_ASSOCIATE`.

Exemplo:

```sql
INSERT INTO TB_ASSOCIATE
(ID_EXTERNAL_UUID, NAME, EMAIL, PASSWORD, DOCUMENT_NUMBER, CREATED_AT, UPDATED_AT)
VALUES
    (
        '10dce706-0eae-4693-9593-ab817dddd143',
        'Associate Test',
        'associate@test.com',
        'test',
        '12345678900',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );
```

Isso é necessário porque os votos são associados a um usuário/associado único, e a API valida esse identificador antes de registrar o voto.

## Endpoints da API

A seguir, os endpoints principais do projeto.

### 1) Cadastrar pauta

```http
POST /api/v1/agenda
```

Body de exemplo:

```json
{
  "title": "Pauta de Teste",
  "description": "Descrição da pauta"
}
```

### 2) Abrir sessão de votação

```http
POST /api/v1/agenda/:agendaId/voting-sessions
```

Body de exemplo:

```json
{
  "votingStartTime": "2026-09-27T18:00:00Z",
  "votingEndTime": "2026-09-27T19:00:00Z"
}
```

Essa rota cria uma sessão de votação vinculada a uma pauta.

### 3) Registrar voto

```http
POST /api/v1/voting-sessions/:votingSessionId/votes
```

Body de exemplo:

```json
{
  "associateId": "10dce706-0eae-4693-9593-ab817dddd143",
  "voteOption": "YES"
}
```

Observações:

- os votos aceitos são apenas `YES` ou `NO`
- cada associado pode votar apenas uma vez por pauta
- o associado deve existir previamente no banco

### 4) Consultar resultado da votação

```http
GET /api/v1/voting-sessions/:votingSessionId/results
```

Resposta esperada:

```json
{
  "yesVotes": 2,
  "noVotes": 1,
  "totalVotes": 3
}
```

## Regras de negócio principais

- uma pauta pode ter uma ou mais sessões de votação
- a sessão só pode ser aberta se as datas forem válidas
- a data final precisa ser posterior à data inicial
- um associado não pode votar mais de uma vez na mesma pauta
- o resultado só pode ser consultado quando a sessão já estiver fechada

## Scheduler de fechamento de sessões

A aplicação também possui um scheduler responsável por fechar automaticamente as sessões de votação que já passaram do horário final.

Esse comportamento está implementado em:

- `backend/src/main/java/br/com/gabrielferreira/votacao/domain/scheduler/VotingSessionScheduler.java`
- `backend/src/main/java/br/com/gabrielferreira/votacao/domain/services/VotingSessionService.java`

A classe `VotingSessionScheduler` é um componente Spring com `@Scheduled` e executa periodicamente a lógica:

```java
@Scheduled(
        initialDelayString = "${voting-session.scheduler.interval}",
        fixedRateString = "${voting-session.scheduler.interval}"
)
public void closeExpiredVotingSessions() {
    log.info("Closing expired voting sessions...");
    votingSessionService.closeExpiredVotingSessions();
    log.info("Closed expired voting sessions...");
}
```

A configuração do intervalo está em:

```yaml
voting-session:
  scheduler:
    enabled: true
    interval: 60000
```

Isso significa que a aplicação tenta fechar sessões expirada a cada 60 segundos.

O método real da regra fica no serviço:

```java
@Transactional
public void closeExpiredVotingSessions() {
    log.info("Closing expired voting sessions...");
    int closedSessions = repository.closeExpiredSessions(OffsetDateTime.now(ZoneOffset.UTC));
    log.info("Closed {} expired voting sessions", closedSessions);
}
```

Em outras palavras, o scheduler consulta o repositório para atualizar todas as sessões em aberto cuja `VOTING_END_TIME` já passou, marcando-as como `CLOSED`.

Essa automação é útil para garantir que a API não dependa de intervenção manual para encerrar votações vencidas.

## Testes

Para rodar a suíte de testes:

```bash
cd backend
mvn test
```

## Observações importantes

- a API usa contexto `/api`, então todos os endpoints começam com `/api`.
- o ambiente local de desenvolvimento usa PostgreSQL em Docker.
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
