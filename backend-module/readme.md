# API REST - Sistema de Benefícios

API REST desenvolvida com Spring Boot para gerenciamento de benefícios, integrada com módulo EJB para operações transacionais.

## Arquitetura

Este projeto faz parte de uma arquitetura híbrida:

```
┌──────────────────────┐
│   Modulo Backend     │  
│   (Camada REST)      │
│   - CRUD Benefícios  │
│   - Documentação     │
└──────────┬───────────┘
           │ 
           │ Chamada Remota (JNDI)
           │
           ▼
┌──────────────────────┐
│    Módulo EJB        │
│  (Lógica Negócio)    │
│  - Transferências    │
│  - Transações        │
│  - Lock Otimista     │
└──────────────────────┘
```

## Tecnologias

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Web
- H2
- Springdoc OpenAPI (Swagger)
- Maven

## Pré-requisitos

- JDK 17 ou superior
- Maven

## Instalação

### 1. Instale as dependências
```bash
mvn clean install
```
### 2. Execute a aplicação
```bash
mvn spring-boot:run
```



## Documentação (Swagger)
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## Endpoints

### Benefícios

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/beneficios/listar_todos` | Lista todos os benefícios |
| GET | `/beneficios/listar/{id}` | Busca benefício por ID |
| POST | `/beneficios` | Cria novo benefício |
| PUT | `/beneficios/atualizar/{id}` | Atualiza benefício |
| DELETE | `/beneficios/deletar/{id}` | Remove benefício |
| POST | `/beneficios/transferir` | **Transfere benefício (via EJB)** |

## Exemplos de Uso

### Criar Benefício
```bash
curl -X POST http://localhost:8081/beneficios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Vale Alimentação",
    "valor": 500.00,
    "descricao": "Benefício mensal"
  }'
```

### Listar Todos
```bash
curl http://localhost:8081/beneficios/listar_todos
```

### Transferir Benefício (usando EJB)
```bash
curl -X POST http://localhost:8081/beneficios/transferir \
  -H "Content-Type: application/json" \
  -d '{
    "beneficioOrigemId": 1,
    "beneficioDestinoId": 2,
    "valor": 100.00
  }'
```

**Nota**: A transferência é processada pelo módulo EJB, garantindo a transação e controle de concorrência.

## Estrutura do Projeto

```
spring-module/
├── src/main/java/
│   └── com/seuapp/beneficios/
│       |── BeneficioController.java
│       ├── service/
│       │   └── BeneficioService.java
│       ├── repository/
│       │   └── BeneficioRepository.java
│       ├── dto/
│       │   └── TransferenciaRequest.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## Integração com EJB

1. **API recebe requisição** de transferência via REST
2. **Spring Boot valida** os dados básicos
3. **Service localiza o EJB** via JNDI lookup
4. **EJB executa** a transferência com controle transacional
5. **API retorna** o resultado ao cliente

## Testes

```bash
mvn test
```


## Dependências Principais

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Swagger/OpenAPI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>

<!-- Wildfly EJB Client -->
<dependency>
    <groupId>org.wildfly</groupId>
    <artifactId>wildfly-ejb-client-bom</artifactId>
    <version>26.1.3.Final</version>
    <type>pom</type>
</dependency>

<!-- Database -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```


## Autor

Mateus Henrique - mateushr12@gmail.com



**Documentação relacionada:**
- README do Módulo EJB
