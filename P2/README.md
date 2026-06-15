# Sistema de Biblioteca — Projeto P2

Sistema de controle de biblioteca desenvolvido em Java com arquitetura em 3 camadas (UI, DAO e Model), interface gráfica Swing e integração com banco de dados MySQL.
Acesse https://bibliotecasystem.netlify.app/ entender melhor nosso projeto através de uma documentação interativa.

## Estrutura do projeto

```
src/
├── model/
│   ├── Pessoa.java          # Classe abstrata (herança e polimorfismo)
│   ├── Leitor.java          # Herda de Pessoa (com Soft Delete)
│   ├── Funcionario.java     # Herda de Pessoa (com Soft Delete)
│   ├── Obra.java            # Contém estoque e código de barras
│   ├── Emprestimo.java      # Associação entre Leitor, Obra e Funcionario
│   └── Reserva.java         # Associação entre Leitor e Obra
├── dao/
│   ├── ConexaoDAO.java      # Centraliza a conexão com o banco
│   ├── LeitorDAO.java
│   ├── FuncionarioDAO.java
│   ├── ObraDAO.java
│   ├── EmprestimoDAO.java   # Implementa transações atômicas
│   └── ReservaDAO.java
└── ui/
    ├── Main.java            # Ponto de entrada
    ├── TelaPrincipal.java   # JFrame com 6 abas
    ├── PainelLeitor.java
    ├── PainelFuncionario.java
    ├── PainelObra.java
    ├── PainelEmprestimo.java
    ├── PainelDevolucao.java
    └── PainelReserva.java
sql/
└── banco.sql                # Script de criação do banco e dados de exemplo
```
---

## Conceitos de Orientação a Objetos aplicados

* **Herança** — `Leitor` e `Funcionario` herdam de `Pessoa` utilizando a palavra-chave `extends`, reaproveitando atributos como `id` e `nome` sem repetição de código. A classe `Pessoa` é abstrata e não pode ser instanciada diretamente.
  
* **Polimorfismo** — O método `getIdentificacao()` é **abstrato** em `Pessoa` e implementado de forma diferente em cada subclasse. O resultado aparece formatado de forma distinta na coluna "Identificação" das tabelas da interface.
  
* **Associação** — `Emprestimo` associa `Leitor`, `Obra` e `Funcionario`: todos existem de forma independente no sistema. `Reserva` associa `Leitor` e `Obra` sob o mesmo princípio.
  
* **Tratamento de exceções na UI** — A interface captura **três tipos separados** de exceções: `IllegalArgumentException` para validação de formulário, `IllegalStateException` para regras de negócio (como limite de reservas, obra sem estoque ou exclusão de leitor com empréstimo ativo) e `SQLException` para erros do banco, exibindo mensagens de alerta ou erro apropriadas para cada caso.
  
* **Transações Atômicas (DAO)** — Operações críticas, como registrar empréstimo ou devolução, executam comandos combinados (ex: `INSERT` no empréstimo + `UPDATE` subtraindo o estoque da obra). Se um comando falhar, ocorre um `rollback()`, garantindo a consistência do sistema.

---

## Pré-requisitos

* Java 17 ou superior
* MySQL 8.0 ou superior
* Conector JDBC do MySQL (`mysql-connector-j-9.7.0.jar` ou similar)

---

## Como configurar e rodar

### 1. Criar o banco de dados

Execute o script abaixo no MySQL Workbench ou via terminal:

```bash
mysql -u root -p < sql/banco.sql

```

O script cria o banco `biblioteca`, as 5 tabelas e insere dados de exemplo iniciais.

### 2. Configurar a conexão

Abra `src/dao/ConexaoDAO.java` e ajuste as credenciais (único arquivo que precisa ser alterado para apontar para o seu banco):

```java
private static final String URL  = "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASS = "suasenha"; // altere aqui

```

### 3. Adicionar o conector MySQL ao projeto

* **VSCode:** Adicione o `.jar` na pasta `lib/` às *Referenced Libraries* (Painel JAVA PROJECTS → + ao lado de Referenced Libraries).
* **IntelliJ IDEA:** Vá em `File → Project Structure → Libraries → + → Java` e selecione o `.jar`.
* **Eclipse:** Clique com o botão direito no projeto → `Build Path → Add External JARs` e selecione o `.jar`.

### 4. Executar

Rode a classe `ui.Main`. A janela principal abrirá com as 6 abas de navegação.

---

## Funcionalidades por aba

| Aba | O que faz |
| --- | --- |
| **Leitores** | Cadastrar, listar, editar e inativar (Soft Delete) leitores. Filtro e edição inline na tabela. |
| **Funcionários** | Cadastrar, listar, editar e inativar funcionários. Validação de matrícula única (UNIQUE). |
| **Obras** | Cadastrar, listar e editar obras, controlando a quantidade de exemplares em tempo real. |
| **Empréstimos** | Registrar empréstimos cruzando Leitor, Obra (apenas disponíveis) e Funcionário. Calcula data prevista via prazo em dias. |
| **Devoluções** | Listar apenas empréstimos em aberto (com sinalização de atrasos) e registrar devolução, atualizando o estoque de forma automática. |
| **Reservas** | Registrar e cancelar reservas. Limite imposto pela regra de negócio: máximo de 3 reservas ativas por leitor. |

---

## Ordem recomendada de cadastro

Para usar o sistema do zero (sem os dados de exemplo), siga esta ordem estrutural de dependências:

1. Cadastrar **Funcionários**
2. Cadastrar **Leitores**
3. Cadastrar **Obras** (informando código de barras e quantidade total)
4. Registrar **Empréstimos**
5. Registrar **Devoluções** quando o livro retornar
6. Registrar **Reservas** conforme necessidade

---

## Estrutura do banco de dados

O banco agora unifica cópias e obras, reduzindo a complexidade estrutural e controlando a disponibilidade via estoque:

```sql
funcionario  (id PK, nome, cargo, matricula_func UNIQUE, ativo)
leitor       (id PK, nome, matricula UNIQUE, ativo)
obra         (id PK, titulo, autor, codigo_barras UNIQUE, quantidade_total, quantidade_disponivel)
emprestimo   (id PK, id_leitor FK, id_obra FK, id_funcionario FK, data_emprestimo, data_prevista_devolucao, data_devolucao_real)
reserva      (id PK, id_leitor FK, id_obra FK, data_reserva)

```

> **Nota:** A coluna `data_devolucao_real` permanece `NULL` enquanto a obra não for devolvida, sendo a chave de identificação do DAO para listar "empréstimos em aberto".

## Autoria

| Nome | GitHub |
|---|---|
| Sabrina Bernardi | [@ssabris](https://github.com/ssabris) |
| Guilherme Cordovil | [@guilherme9727](https://github.com/guilherme9727) |
