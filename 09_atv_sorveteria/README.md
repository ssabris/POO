# 🍦 Sorveteria Floquinho 

Sistema de vendas da Sorveteria Floquinho desenvolvido em **Java** com interface gráfica **Swing** e integração com banco de dados **MySQL**. O sistema identifica o cliente e personaliza o cardápio automaticamente com base no histórico de compras de cada usuário.

> Trabalho desenvolvido em dupla por mim **Sabrina Bernardi** e **Guilherme Cordovil**  
> Disciplina: Programação Orientada a Objetos

---

## 📋 Funcionalidades

- **Login inteligente** — reconhece clientes que já compraram antes e lembra suas preferências
- **Cadastro automático** — novos clientes são cadastrados na primeira visita escolhendo sua categoria favorita
- **Cardápio personalizado** — produtos são organizados em seções: "Recomendações" (categoria favorita) e "Outros" (agrupados por categoria)
- **Algoritmo de recomendação** — após cada compra, o sistema recalcula automaticamente a categoria favorita do cliente com base no histórico de pedidos
- **Realizar pedido** — selecione um produto e confirme o pedido antes de finalizar
- **Histórico de compras** — visualize todos os pedidos anteriores com data e hora

---

## 🗂️ Estrutura do Projeto

```
src/
├── Usuario.java              → Entidade: representa o cliente
├── Produto.java              → Entidade: representa os sorvetes do cardápio
├── SorveteriaFloquinho.java  → Persistência: lógica SQL e conexão com MySQL
└── SistemaSorveteria.java    → Interface gráfica Swing e fluxo do programa
```

### Separação em camadas (POO)

| Camada | Arquivo | Responsabilidade |
|---|---|---|
| **Model (Entidade)** | `Usuario.java`, `Produto.java` | Representam os dados do sistema |
| **DAO (Persistência)** | `SorveteriaFloquinho.java` | Acesso ao banco de dados (SQL) |
| **View/Controller** | `SistemaSorveteria.java` | Interface gráfica e fluxo do programa |

---

## 🛠️ Pré-requisitos

- **Java JDK 11** ou superior
- **MySQL 8.0** ou superior (rodando localmente)
- **Conector MySQL/J** (arquivo `.jar`) adicionado nas `Referenced Libraries` do VS Code

---

## ⚙️ Configuração

As credenciais do banco ficam no arquivo `SorveteriaFloquinho.java`:

```java
private final String DB_NAME = "sorveteria_floquinho";
private final String USER    = "root";
private final String PASS    = "root"; // Altere do acordo com seu usuário
```

O banco de dados e todas as tabelas são criados automaticamente na primeira execução. Nenhuma configuração manual no MySQL é necessária.

---

## ▶️ Como Rodar

1. Abra a pasta do projeto no **VS Code** via **File → Open Folder**
2. Certifique-se de que o `.jar` do MySQL está em **Referenced Libraries** (painel Java Projects)
3. Abra o arquivo `SistemaSorveteria.java`
4. Clique em **Run** acima do método `public static void main`

### Como adicionar o conector MySQL/J

1. Baixe em: https://dev.mysql.com/downloads/connector/j/ → selecione **Platform Independent** → baixe o `.zip`
2. Extraia e localize o arquivo `mysql-connector-j-x.x.x.jar`
3. No VS Code, clique no `+` em **Referenced Libraries** e selecione o `.jar`

---

## 🗄️ Banco de Dados

**Nome do banco:** `sorveteria_floquinho`

| Tabela | Descrição |
|---|---|
| `usuarios` | Clientes cadastrados com nome e categoria favorita |
| `produtos` | Cardápio com nome, categoria e preço |
| `vendas` | Registro de cada pedido com data e hora |

**Categorias disponíveis:** Tradicional, Frutas, Chocolate

---

## ❄️ Algoritmo de Recomendação

A cada compra realizada, o sistema executa a seguinte consulta para determinar a categoria favorita atualizada do cliente:

```sql
SELECT p.categoria, COUNT(*) AS total
FROM vendas v
JOIN produtos p ON v.produto_id = p.id
WHERE v.usuario_id = ?
GROUP BY p.categoria
ORDER BY total DESC
LIMIT 1
```

A categoria com mais pedidos passa a ser a favorita. O cardápio é reordenado automaticamente na mesma sessão, sem precisar reiniciar o sistema.

---

## 👨‍💻 Autores

| Nome | GitHub |
|---|---|
| [Sabrina Bernardi] | [@ssabris](https://github.com/ssabris) |
| Guilherme Cordovil | [@guilherme9727](https://github.com/guilherme9727) |
