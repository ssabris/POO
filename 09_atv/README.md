# Clínica Médica — DAO com SQLite (Java)

Projeto Java com padrão **DAO (Data Access Object)** integrado ao banco de dados **SQLite**, desenvolvido para gerenciar as entidades de uma clínica médica.

---

## 📁 Estrutura do Projeto

```
src/
└── fatec/poo/09_atv
    ├── bean/
    │   ├── Medico.java
    │   └── Recepcionista.java
    ├── dao/
    │   ├── MedicoDAO.java
    │   └── RecepcionistaDAO.java
    └── Main.java
```

---

## 🗃️ Banco de Dados

- Tecnologia: **SQLite**
- Arquivo gerado automaticamente: `database.db` (na raiz do projeto)
- As tabelas são criadas automaticamente na primeira execução via `CREATE TABLE IF NOT EXISTS`

---

## 📦 Dependência Maven

Adicione ao seu `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.46.0.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

---

## 🧩 Classes Bean

### `Medico`
| Atributo | Tipo | Descrição |
|---|---|---|
| `codigo` | int | Chave primária (autoincremento) |
| `nome` | String | Nome completo |
| `email` | String | E-mail |
| `senha` | String | Senha de acesso |
| `documento` | String | CPF |
| `telefone` | String | Telefone |
| `crm` | String | Registro no CRM (ex: CRM/SP 12345) |
| `especialidade` | String | Especialidade médica |

### `Recepcionista`
| Atributo | Tipo | Descrição |
|---|---|---|
| `codigo` | int | Chave primária (autoincremento) |
| `nome` | String | Nome completo |
| `email` | String | E-mail |
| `senha` | String | Senha de acesso |
| `documento` | String | CPF |
| `telefone` | String | Telefone |
| `turno` | String | Turno de trabalho (MANHÃ, TARDE, NOITE) |

---

## 🔧 Classes DAO — Métodos CRUD

Ambas as DAOs (`MedicoDAO` e `RecepcionistaDAO`) seguem a mesma estrutura:

| Método | SQL | Descrição |
|---|---|---|
| `MedicoDAO()` / `RecepcionistaDAO()` | `CREATE TABLE IF NOT EXISTS` | Abre conexão e cria a tabela se não existir |
| `create(obj)` | `INSERT` | Insere um novo registro |
| `read(codigo)` | `SELECT ... WHERE codigo = ?` | Busca um registro pelo código; retorna `null` se não encontrado |
| `readAll()` | `SELECT *` | Retorna uma `List<>` com todos os registros |
| `update(obj)` | `UPDATE` | Atualiza todos os campos pelo `codigo` |
| `delete(codigo)` | `DELETE` | Remove o registro pelo `codigo` |

---

## ▶️ Exemplo de Uso

```java
// --- MÉDICO ---
MedicoDAO medicoDAO = new MedicoDAO();

Medico m = new Medico();
m.setNome("Dr. Carlos Silva");
m.setEmail("carlos@clinica.com");
m.setCrm("CRM/SP 12345");
m.setEspecialidade("Cardiologia");
medicoDAO.create(m);                      // INSERT

Medico encontrado = medicoDAO.read(1);    // SELECT por código
encontrado.setEspecialidade("Neurologia");
medicoDAO.update(encontrado);             // UPDATE
medicoDAO.delete(1);                      // DELETE

List<Medico> todos = medicoDAO.readAll(); // SELECT *

// --- RECEPCIONISTA ---
RecepcionistaDAO recepDAO = new RecepcionistaDAO();

Recepcionista r = new Recepcionista();
r.setNome("Ana Paula");
r.setEmail("ana@clinica.com");
r.setTurno("MANHÃ");
recepDAO.create(r);                              // INSERT

Recepcionista rec = recepDAO.read(1);            // SELECT por código
rec.setTurno("TARDE");
recepDAO.update(rec);                            // UPDATE
recepDAO.delete(1);                              // DELETE

List<Recepcionista> lista = recepDAO.readAll();  // SELECT *
```

---

## ⚙️ Configuração do Ambiente

1. **Java 17+** instalado
2. **Maven** configurado
3. Adicionar a dependência `sqlite-jdbc` no `pom.xml`
4. Executar `mvn install` para baixar as dependências
5. Rodar a classe `Main.java`

---

## 📝 Observações

- O arquivo `database.db` é criado automaticamente na raiz do projeto na primeira execução.
- Não é necessário instalar o SQLite separadamente — o driver JDBC já inclui o banco embutido.
- O método `read()` retorna `null` caso o código informado não exista na tabela.
