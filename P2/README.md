# Sistema de Biblioteca — Projeto P2

[Sistema de controle de biblioteca] (https://github.com/ssabris/POO/blob/main/P2/explicacao-projeto.html) desenvolvido em Java com interface gráfica Swing e integração com banco de dados MySQL via camada DAO.

---

## Estrutura do projeto

```
src/
├── model/
│   ├── Pessoa.java          # Classe abstrata (herança e polimorfismo)
│   ├── Leitor.java          # Herda de Pessoa
│   ├── Funcionario.java     # Herda de Pessoa
│   ├── Obra.java
│   ├── Copia.java           # Agregação com Obra
│   ├── Emprestimo.java      # Associação entre Leitor, Copia e Funcionario
│   └── Reserva.java         # Associação entre Leitor e Obra
├── dao/
│   ├── ConexaoDAO.java      # Centraliza a conexão com o banco
│   ├── LeitorDAO.java
│   ├── FuncionarioDAO.java
│   ├── ObraDAO.java
│   ├── CopiaDAO.java
│   ├── EmprestimoDAO.java
│   └── ReservaDAO.java
└── ui/
    ├── Main.java             # Ponto de entrada
    ├── TelaPrincipal.java    # JFrame com abas
    ├── PainelLeitor.java
    ├── PainelFuncionario.java
    ├── PainelObra.java
    ├── PainelCopia.java
    ├── PainelEmprestimo.java
    ├── PainelDevolucao.java
    └── PainelReserva.java
sql/
└── banco.sql                 # Script de criação do banco e dados de exemplo
```

---

## Conceitos de orientação a objetos aplicados

**Herança** — `Leitor` e `Funcionario` herdam de `Pessoa`, reaproveitando `id` e `nome` sem repetição de código.

**Polimorfismo** — o método abstrato `getIdentificacao()` é implementado de forma diferente em cada subclasse. O resultado aparece na coluna "Identificação" das tabelas da interface.

**Agregação** — `Copia` agrega `Obra`: uma cópia não pode existir sem uma obra, mas a obra existe de forma independente. Refletido no banco como FK NOT NULL.

**Associação** — `Emprestimo` associa `Leitor`, `Copia` e `Funcionario`, todos existindo de forma independente. `Reserva` associa `Leitor` e `Obra` da mesma forma.

**Tratamento de exceções na UI** — todos os métodos DAO declaram `throws SQLException`. A interface captura dois tipos separados: `IllegalArgumentException` para validação de formulário e `SQLException` para erros de banco, exibindo mensagens distintas para cada caso.

---

## Pré-requisitos

- Java 17 ou superior
- MySQL 8.0 ou superior
- Conector JDBC do MySQL (`mysql-connector-j-x.x.x.jar`)

---

## Como configurar e rodar

### 1. Criar o banco de dados

Execute o script abaixo no MySQL Workbench ou via terminal:

```bash
mysql -u root -p < sql/banco.sql
```

O script cria o banco `biblioteca`, as 6 tabelas e insere dados de exemplo (2 funcionários, 2 leitores, 2 obras e 3 cópias).

### 2. Configurar a conexão

Abra `src/dao/ConexaoDAO.java` e ajuste as credenciais:

```java
private static final String URL  = "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASS = "suasenha"; // altere aqui
```

### 3. Adicionar o conector MySQL ao projeto

**IntelliJ IDEA:** `File → Project Structure → Libraries → + → Java` e selecione o `.jar` do conector.

**Eclipse:** clique com o botão direito no projeto → `Build Path → Add External JARs` e selecione o `.jar`.

### 4. Executar

Rode a classe `ui.Main`. A janela principal abre com 7 abas.

---

## Funcionalidades por aba

| Aba | O que faz |
|---|---|
| Leitores | Cadastrar, listar e excluir leitores |
| Funcionários | Cadastrar, listar e excluir funcionários |
| Obras | Cadastrar, listar e excluir obras |
| Cópias | Cadastrar cópias vinculadas a uma obra (ComboBox) |
| Empréstimos | Registrar empréstimos selecionando leitor, cópia e funcionário |
| Devoluções | Listar empréstimos em aberto e registrar devolução |
| Reservas | Registrar e cancelar reservas de obras |

---

## Ordem recomendada de cadastro

Para usar o sistema do zero (sem os dados de exemplo), siga esta ordem:

1. Cadastrar **Funcionários**
2. Cadastrar **Leitores**
3. Cadastrar **Obras**
4. Cadastrar **Cópias** (dependem de obras existirem)
5. Registrar **Empréstimos**
6. Registrar **Devoluções** quando o livro for devolvido
7. Registrar **Reservas** conforme necessário

---

## Estrutura do banco de dados

```sql
funcionario  (id, nome, cargo)
leitor       (id, nome, matricula)
obra         (id, titulo, autor)
copia        (id, id_obra FK, codigo_barras)
emprestimo   (id, id_leitor FK, id_copia FK, id_funcionario FK, data_emprestimo, data_devolucao)
reserva      (id, id_leitor FK, id_obra FK, data_reserva)
```

`data_devolucao` fica `NULL` enquanto o livro não é devolvido — isso é o que diferencia empréstimos em aberto dos já encerrados.

## Autoria

| Nome | GitHub |
|---|---|
| [Sabrina Bernardi] | [@ssabris](https://github.com/ssabris) |
| Guilherme Cordovil | [@guilherme9727](https://github.com/guilherme9727) |
