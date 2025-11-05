# Módulo EJB - Sistema de Transferência de Benefícios

Módulo EJB responsável pela lógica de negócio de transferência de benefícios entre usuários, com controle transacional e bloqueio.

## Objetivo

Este módulo fornece serviços (EJB) para operações de transferência de benefícios, garantindo:
- **Controle de Concorrência**: Bloqueio otimista (OPTIMISTIC LOCK)
- **Validações de Negócio**: Regras de transferência e saldo
- **Integração**: Interface remota para comunicação com o módulo Spring Boot

## Tecnologias

- Java EE 8+ / Jakarta EE
- Wildfly / JBoss / Payara (Application Server)

## Pré-requisitos

- JDK 11 ou superior
- Application Server (Wildfly, JBoss EAP, Payara, etc.)
- Banco de dados (H2)
- Módulo Spring Boot (cliente)


## Estrutura do Projeto

```
ejb-module/
├── src/main/java/
│   └── com/example/ejb/
│       ├── BeneficioEjbService.java        # Implementação EJB
│       ├── BeneficioEjbServiceRemote.java  # Interface
├── src/main/resources/
│   └── META-INF/
│       ├── persistence.xml                  # Configuração JPA
└── pom.xml
```

## Configuração

### 1. Dependências Maven (pom.xml)

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.example</groupId>
        <artifactId>bip-teste-integrado</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>ejb-module</artifactId>
    <packaging>ejb</packaging>

    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>common-module</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>jakarta.ejb</groupId>
            <artifactId>jakarta.ejb-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>jakarta.persistence</groupId>
            <artifactId>jakarta.persistence-api</artifactId>
            <version>3.1.0</version>
        </dependency>

        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.2.224</version>
        </dependency>

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>5.11.0</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>

    </dependencies>
</project>
```

### 2. Configuração JPA (persistence.xml)

```xml
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             version="3.0">
    <persistence-unit name="beneficioPU" transaction-type="JTA">
        <class>com.example.entity.Beneficio</class>
        <properties>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:h2:tcp://localhost:9092/mem:beneficiosdb"/>
            <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="jakarta.persistence.jdbc.user" value="sa"/>
            <property name="jakarta.persistence.jdbc.password" value=""/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```


## Interface Remota

```java
@Remote
public interface BeneficioEjbServiceRemote {
    void transfer(Long fromId, Long toId, BigDecimal amount);
}
```

## Regras de Negócio

### Validações Implementadas

1. **Valor Positivo**: O valor da transferência deve ser maior que zero
2. **Usuários Diferentes**: Não é permitido transferir para o mesmo usuário
3. **Existência**: Ambos benefícios (origem e destino) devem existir
4. **Saldo Suficiente**: O benefício de origem deve ter saldo maior ou igual ao valor

### Controle de Concorrência

- **LockModeType.OPTIMISTIC**: Impede conflitos em transferências simultâneas
- **Versionamento**: Utiliza campo `@Version` na entidade Beneficio

## Integração com Spring Boot

### 1. Cliente Spring Boot

```java
@Service
public class BeneficioService {
    
    private BeneficioEjbServiceRemote ejbService;

    /**
     *
     * @param repository
     */
    public BeneficioService(BeneficioRepository repository) {
        try {
            InitialContext ctx = new InitialContext();
            ejbService = (BeneficioEjbServiceRemote) ctx.lookup("java:global/ejb-module/BeneficioEjbService!com.example.ejb.BeneficioEjbServiceRemote");
        } catch (NamingException e) {
            throw new RuntimeException("Não foi possivel conectar com o EJB", e);
        }
    }

    /**
     *
     * @param transferenciaRequest
     */
    public void tranfer(TransferenciaRequest transferenciaRequest){
        ejbService.transfer(transferenciaRequest.getFromId(), 
                transferenciaRequest.getToId(), transferenciaRequest.getAmount());
    }
}
```

### 3. Dependências Spring (pom.xml)

```xml
 <dependency>
    <groupId>com.example</groupId>
    <artifactId>ejb-module</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Deploy

### 1. Build do módulo EJB

```bash
mvn clean package
```

Isso gerará o arquivo `beneficios-ejb.jar` em `target/`

### 2. Deploy no Wildfly

**Via CLI:**
```bash
./jboss-cli.sh --connect
deploy /caminho/para/beneficios-ejb.jar
```

## Testes


## Tratamento de Erros

| Exceção | Causa | Solução |
|---------|-------|---------|
| `IllegalArgumentException` | Valor ≤ 0 ou mesma conta | Validar dados antes do envio |
| `EntityNotFoundException` | Benefício não existe | Verificar IDs no banco |
| `IllegalStateException` | Saldo insuficiente | Verificar saldo antes da transferência |
| `OptimisticLockException` | Conflito de versão | Implementar retry logic |



## Autor

Mateus Henrique - mateushr12@gmail.com


**Nota**: Este módulo é parte de uma arquitetura híbrida Spring Boot + Java EE. Há também um modulo common compartilhado entre os modulos spring e ejb que contem a classe Beneficio .