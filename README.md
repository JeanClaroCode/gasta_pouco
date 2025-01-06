# Gasta Pouco - Sistema de Gerenciamento de Ganhos e Despesas

## Descrição

O **Gasta Pouco** é um sistema completo para gerenciamento de ganhos e despesas pessoais. Com ele, você pode registrar, consultar e organizar suas finanças por categoria e por mês. Este projeto foi desenvolvido com **Spring Boot** e oferece APIs para o gerenciamento de transações financeiras de ganhos e despesas.

### Funcionalidades Principais

- **Cadastro de Transações**: Permite o cadastro de novos ganhos e despesas.
- **Consulta de Totais**: Permite consultar o total de ganhos e despesas em diferentes intervalos de tempo.
- **Filtragem por Categoria e Mês**: Oferece filtragem por categoria (ex: salário, mercado, transporte) e por mês.

## Como Rodar o Projeto

### Pré-requisitos

- Java 17 ou superior
- Spring Boot 2.7 ou superior
- Maven ou Gradle (recomendado Maven)

### Configuração

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/gasta-pouco.git
   ```

2. Navegue até o diretório do projeto:
   ```bash
   cd gasta-pouco
   ```

3. Abra o projeto em sua IDE de preferência (ex: IntelliJ IDEA, Eclipse).

4. Execute o projeto com o seguinte comando:
   ```bash
   ./mvnw spring-boot:run
   ```

### Autenticação

A autenticação é feita através de **JWT**. Para acessar as rotas de ganhos e despesas, é necessário obter um token JWT válido. Você pode obter esse token ao fazer login no sistema, utilizando a rota de login.

---

## Testes

O projeto já inclui testes para garantir que os dados dos usuários estão sendo gerenciados corretamente. Você pode rodar os testes usando o Maven:

```bash
./mvnw test
```

## Autor  
Desenvolvido por Jean.  
- [LinkedIn](https://www.linkedin.com/in/jeanclaro/)   
--- 
