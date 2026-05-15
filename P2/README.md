# 📚 Sistema de Controle de Biblioteca (Library Management System)

Um sistema completo de gerenciamento de bibliotecas desenvolvido em **Java**, utilizando interface gráfica **Swing** e integração com banco de dados relacional via padrão **DAO**. Este projeto foi construído com forte base em Programação Orientada a Objetos (POO).

## 🚀 Funcionalidades

*   **Gestão de Pessoas:** Cadastro, atualização e exclusão de Leitores e Funcionários.
*   **Gestão de Acervo:** Controle de Obras (títulos) e suas respectivas Cópias (itens físicos).
*   **Empréstimos:** Registro de saída de cópias, associando o leitor, a cópia física e o funcionário responsável, com cálculo automático de prazos.
*   **Reservas:** Sistema de fila para obras que não possuem cópias disponíveis no momento.
*   **Tratamento de Erros:** Feedbacks visuais e amigáveis diretamente na interface do usuário em caso de falhas de validação ou de conexão com o banco.

## 🛠️ Tecnologias e Arquitetura

*   **Linguagem:** Java 17+
*   **Interface Gráfica (UI):** Java Swing
*   **Banco de Dados:** MySQL (via JDBC)
*   **Arquitetura:** MVC (Model-View-Controller) + DAO (Data Access Object)

## 🧠 Conceitos de POO Aplicados

O sistema foi modelado para refletir boas práticas de engenharia de software:

1.  **Herança:** As entidades `Leitor` e `Funcionario` estendem atributos e comportamentos da classe base abstrata `Pessoa`.
2.  **Polimorfismo:** Prazos de empréstimo e cálculos de multa variam dinamicamente dependendo do tipo de instância envolvida no empréstimo.
3.  **Associação:** Classes transacionais como `Emprestimo` e `Reserva` conectam as entidades principais de forma consistente.
4.  **Agregação/Composição:** A relação estrita entre `Obra` (catálogo) e `Copia` (inventário físico).

## 📂 Estrutura de Pacotes

```text
src/
 ├── model/         # Classes de domínio (Pessoa, Leitor, Obra, Copia, Emprestimo)
 ├── dao/           # Interfaces e implementações de acesso a dados (LeitorDAO, etc.)
 ├── controller/    # Lógica de negócio e intermediação entre UI e DAO
 ├── view/          # Telas em Java Swing (JFrame, JPanel)
 ├── exception/     # Exceções personalizadas (DatabaseException, ValidationException)
 └── util/          # Classes utilitárias (ConnectionFactory, formatadores)
````

## 👨‍💻 Autores

| Nome | GitHub |
|---|---|
| [Sabrina Bernardi] | [@ssabris](https://github.com/ssabris) |
| Guilherme Cordovil | [@guilherme9727](https://github.com/guilherme9727) |
