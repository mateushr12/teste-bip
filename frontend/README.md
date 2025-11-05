# Frontend Angular - Sistema de Benefícios

Aplicação frontend em Angular para gerenciamento de benefícios, consumindo a API REST Spring Boot.


## Tecnologias

- Angular 16+


## Pré-requisitos

- Node.js 18+
- npm 9+
- Angular CLI 16+


## Estrutura do Projeto

```
bip-teste-ui/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   └── beneficio/
│   │   │       ├── beneficio.component.ts
│   │   │       ├── beneficio.component.html
│   │   │       ├── beneficio.component.css
│   │   │       └── beneficio.component.spec.ts
│   │   ├── services/
│   │   │   └── beneficio.service.ts
│   │   ├── app.component.ts
│   │   ├── app.component.html
│   │   └── app.module.ts
│   └── index.html
├── package.json
└── angular.json
```


### Funcionalidades

O serviço `BeneficioService` fornece métodos para:

| Método | Descrição | Retorno |
|--------|-----------|---------|
| `listarTodos()` | Lista todos os benefícios | `Observable<Beneficio[]>` |
| `listarPorId(id)` | Busca benefício por ID | `Observable<Beneficio>` |
| `salvar(beneficio)` | Cria novo benefício | `Observable<Beneficio>` |
| `atualizar(id, beneficio)` | Atualiza benefício existente | `Observable<Beneficio>` |
| `deletar(id)` | Remove benefício | `Observable<string>` |
| `transferir(request)` | Transfere valor entre benefícios | `Observable<string>` |



## Scripts Disponíveis

```bash
# Desenvolvimento
npm start           # Inicia servidor de desenvolvimento
ng serve            # Alternativa ao npm start

# Build
npm run build       # Build de produção
ng build --prod     # Build otimizado

# Testes
npm test            # Executa testes unitários
ng test             # Alternativa

```

## Executando o Sistema Completo

### 1. Inicie o Backend (Spring Boot)
```bash
cd ../spring-module
mvn spring-boot:run
```

### 2. Inicie o Frontend (Angular)
```bash
cd ../angular-frontend
ng serve
```

### 3. Acesse a aplicação
Abra o navegador em: `http://localhost:4200`


## Testando as Funcionalidades

### 1. Listar Benefícios
- Acesse a página principal
- Os benefícios devem ser carregados automaticamente

### 2. Criar Benefício
- Preencha o formulário
- Clique em "Salvar"
- Verifique a mensagem de sucesso

### 3. Transferir
- Informe ID de origem e destino
- Digite o valor
- Clique em "Transferir Beneficio"
- A operação é processada pelo EJB

### 4. Deletar
- Clique no botão "Excluir" ao lado do benefício
- O item será removido da lista

### 4. Atualizar
- Clique no botão "Editar" ao lado do benefício
- Altere algum dado no formulário
- Clique em "Salvar"


## Autor
Mateus Henrique - mateushr12@gmail.com


**Documentação relacionada:**
- README do Backend Spring Boot
- README do Módulo EJB

