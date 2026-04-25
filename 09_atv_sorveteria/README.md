# 🍦 Sorveteria Inteligente

Sistema de vendas de uma loja de sorvetes desenvolvido em **Java** com interface gráfica **Swing** e integração com banco de dados **MySQL**. O sistema identifica o usuário e personaliza o cardápio de acordo com o gosto favorito cadastrado.

> Trabalho desenvolvido em dupla com **Guilherme Cordovil**  
> Disciplina: Programação Orientada a Objetos

---

## 📋 Funcionalidades

- **Login inteligente** — o sistema reconhece usuários que já compraram antes e lembra o gosto favorito deles
- **Cadastro automático** — novos usuários são cadastrados na primeira visita
- **Cardápio personalizado** — os produtos da categoria favorita do usuário aparecem destacados no topo
- **Realizar pedido** — registra a venda no banco com confirmação antes de finalizar
- **Histórico de compras** — o usuário pode ver todos os pedidos anteriores com data e hora

---

## 🗂️ Estrutura do Projeto

```
src/
├── Usuario.java          → Entidade: representa o cliente
├── Produto.java          → Entidade: representa os sorvetes do cardápio
├── SorveteriaDAO.java    → Persistência: toda a lógica SQL e conexão com MySQL
└── SistemaSorveteria.java → Visão/Controller: interface gráfica Swing
```

### Por que essa separação? (Conceito de POO)

| Camada | Arquivo | Responsabilidade |
|---|---|---|
| **Model (Entidade)** | `Usuario.java`, `Produto.java` | Representam os dados do sistema |
| **DAO (Persistência)** | `SorveteriaDAO.java` | Acesso ao banco de dados (SQL) |
| **View/Controller** | `SistemaSorveteria.java` | Interface gráfica e fluxo do programa |

Essa separação facilita a manutenção: se precisar trocar o banco de dados, por exemplo, só mexe no `SorveteriaDAO.java` sem tocar na interface.

---

## 🛠️ Pré-requisitos

Antes de rodar o projeto, você precisa ter instalado:

- **Java JDK 11** ou superior
- **MySQL 8.0** ou superior (rodando localmente)
- **Conector MySQL/J** (arquivo `.jar`) adicionado nas `Referenced Libraries` do VS Code

---

## ⚙️ Configuração do Banco de Dados

Você **não precisa criar o banco manualmente**. O sistema cria tudo automaticamente na primeira execução, incluindo:

- Banco de dados `sorveteria_db`
- Tabelas `usuarios`, `produtos` e `vendas`
- 8 produtos iniciais no cardápio

Só verifique as credenciais no arquivo `SorveteriaDAO.java`:

```java
private final String USER = "root"; // Seu usuário do MySQL
private final String PASS = "root"; // Sua senha do MySQL
```

---

## ▶️ Como Rodar

1. Abra a pasta do projeto no **VS Code**
2. Certifique-se de que o `.jar` do MySQL está em **Referenced Libraries**
3. Abra o arquivo `SistemaSorveteria.java`
4. Clique em **Run** acima do método `public static void main`

### Adicionando o conector MySQL/J

1. Baixe o conector em: https://dev.mysql.com/downloads/connector/j/
2. No VS Code, clique em `+` em **Referenced Libraries** (painel Java Projects)
3. Selecione o arquivo `.jar` baixado

---

## 🗄️ Tabelas do Banco de Dados

**usuarios**
| Campo | Tipo | Descrição |
|---|---|---|
| id | INT AUTO_INCREMENT | Chave primária |
| nome | VARCHAR(100) | Nome do cliente |
| gosto_favorito | VARCHAR(50) | Categoria preferida (Tradicional, Frutas ou Chocolate) |

**produtos**
| Campo | Tipo | Descrição |
|---|---|---|
| id | INT AUTO_INCREMENT | Chave primária |
| nome | VARCHAR(100) | Nome do sorvete |
| categoria | VARCHAR(50) | Categoria do produto |
| preco | DECIMAL(5,2) | Preço em reais |

**vendas**
| Campo | Tipo | Descrição |
|---|---|---|
| id | INT AUTO_INCREMENT | Chave primária |
| usuario_id | INT | Referência ao cliente |
| produto_id | INT | Referência ao produto |
| data_hora | TIMESTAMP | Data e hora da venda |

---

## 💡 Algoritmo de Personalização

O sistema usa uma query SQL simples e eficiente para ordenar o cardápio:

```sql
SELECT * FROM produtos
ORDER BY (categoria = ?) DESC, nome ASC
```

A expressão `(categoria = ?)` retorna `1` (verdadeiro) quando a categoria bate com o gosto do usuário e `0` quando não bate. Ordenando de forma decrescente (`DESC`), os produtos favoritos aparecem primeiro. O restante é ordenado alfabeticamente.

---

## 👨‍💻 Autores

| Nome | GitHub |
|---|---|
| [Seu Nome] | [@seuusuario](https://github.com) |
| Guilherme Cordovil | [@guilhermecordovil](https://github.com) |
