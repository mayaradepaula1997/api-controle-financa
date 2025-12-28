# 💰 API de Controle Financeiro

API REST para **controle financeiro pessoal**, desenvolvida em **Java com Spring Boot**, com autenticação via **JWT**, controle de usuários, categorias e despesas. O projeto está **em produção** e conta com **documentação interativa via Swagger**, permitindo testar todos os endpoints diretamente pelo navegador.

---

## 🚀 Status do Projeto

✅ Em produção  
✅ Documentação Swagger disponível  
✅ Autenticação e autorização com Spring Security + JWT

---

## 🔗 Links Importantes

- **API em produção:**  
  👉 [`https://api-controle-financa.onrender.com`](https://api-controle-financa.onrender.com/)

- **Swagger (documentação e testes):**  
  👉 [`https://api-controle-financa.onrender.com/swagger-ui.html`](https://api-controle-financa.onrender.com/swagger-ui/index.html#/)

> ⚠️ Observação: Para acessar endpoints protegidos, é necessário realizar login e informar o token JWT no Swagger.

---

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- JPA / Hibernate
- PostgreSQL
- Swagger / OpenAPI
- Maven

---

## 📌 Funcionalidades

### 👤 Usuários
- Cadastro de usuário
- Login com geração de token JWT
- Criação de categoria para os gastos
- Criação de gastos com ou sem categoria
- Controle de permissões por role (USER / ADMIN)

### 🗂️ Categorias
- Criar categoria
- Listar categorias do usuário
- Atualizar categoria
- Deletar categoria

### 💸 Despesas
- Registrar despesas
- Listar despesas do usuário logado
- Associar despesas a categorias
- Atualizar despesas
- Deletar despesas

> 🔐 Todas as operações são vinculadas ao **usuário autenticado**, garantindo segurança e isolamento dos dados.

---

## 🔐 Autenticação

A autenticação é feita via **JWT**:

1. O usuário realiza login
2. A API retorna um **token JWT**
3. O token deve ser informado no Swagger clicando em **Authorize**:
   
---

## 📖 Documentação com Swagger

O Swagger foi configurado para facilitar o uso e testes da API:

- Visualização clara dos endpoints
- Testes diretos no navegador
- Suporte a autenticação via Bearer Token

Acesse em:
👉 `https://api-controle-financa.onrender.com/swagger-ui.html`

---

## 🗄️ Banco de Dados

O projeto utiliza banco de dados relacional, com entidades como:

- User
- Category
- Expense

O mapeamento é feito com **JPA/Hibernate**, seguindo boas práticas de modelagem.

---

## ▶️ Como Executar Localmente

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/seu-repositorio.git

# Acesse o projeto
cd seu-repositorio

# Configure o application.properties

# Execute o projeto
mvn spring-boot:run
```

A aplicação estará disponível em:

```
http://localhost:8080
```

Swagger:
```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Testes

Os testes podem ser realizados diretamente pelo **Swagger**, utilizando um usuário autenticado.

---

## 👩‍💻 Autora

Desenvolvido por **Mayara Paula** 🚀  
Projeto com foco em aprendizado, boas práticas e aplicação real em produção.

---

## 📌 Observações Finais

Este projeto foi desenvolvido com foco em:

- Segurança
- Organização de código
- Boas práticas REST
- Experiência do desenvolvedor via Swagger

Sugestões e melhorias são sempre bem-vindas 😊

