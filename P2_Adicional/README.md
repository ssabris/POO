# Sistema Clínico — Java Swing + MySQL

Projeto de cadastro de médicos, pacientes e agendamento de consultas (CRUD).

## Pré-requisitos

- Java 21+
- Maven 3.8+
- MySQL 8+

## Configuração do banco de dados

1. Importe o script de criação do banco:
   ```bash
   mysql -u root -p < setup_mysql.sql
   ```

2. Edite o arquivo `src/com/fatec/poo/dao/ConexaoMySQL.java` e ajuste as constantes:
   ```java
   private static final String HOST     = "localhost";
   private static final String PORT     = "3306";
   private static final String DATABASE = "clinica";
   private static final String USER     = "root";
   private static final String PASSWORD = "sua_senha_aqui";
   ```

## Compilar e executar

```bash
mvn package
java -jar target/clinica.jar
```

Ou use os scripts prontos:

- **Windows:** `build.bat`
- **Linux/Mac:** `./build.sh`

## Estrutura do projeto

```
src/com/fatec/poo/
├── bean/
│   ├── Consulta.java
│   ├── Medico.java
│   └── Paciente.java
├── dao/
│   ├── ConexaoMySQL.java   ← configuração da conexão
│   ├── ConsultaDAO.java
│   ├── MedicoDAO.java
│   └── PacienteDAO.java
├── view/
│   ├── ConsultaView.java
│   ├── MedicoView.java
│   └── PacienteView.java
└── Main.java
```
