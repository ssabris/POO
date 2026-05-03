# 📐 Projeto Formas Geométricas (Java)

Este projeto é uma implementação em **Java** focada em Programação Orientada a Objetos (POO). Ele demonstra conceitos essenciais como **Interfaces**, **Polimorfismo**, **Encapsulamento** e **Tratamento de Exceções** por meio da modelagem de diferentes formas geométricas.

---

## 📂 Estrutura do Projeto

O sistema é centralizado na interface `FormasGeometricas`, que estabelece um contrato para as classes que a implementam.

### Interface: `FormasGeometricas`
Define dois métodos obrigatórios:
* `quantidadeArestas()`: Retorna o número de arestas (lados) da forma (`int`).
* `calculaArea()`: Calcula e retorna o tamanho da área baseada nas dimensões informadas (`double`).

---

## 🧩 Classes Implementadas

O projeto conta com quatro formas geométricas, cada uma implementando sua própria lógica matemática para o cálculo de área.

| Classe | Qtd. Arestas | Fórmula da Área | Observações |
| :--- | :---: | :--- | :--- |
| **Círculo** | 0 | $A = \pi \cdot r^2$ | Recebe o raio ($r$) no construtor. Utiliza a constante `Math.PI` no cálculo. |
| **Retângulo** | 4 | $A = \text{largura} \cdot \text{altura}$ | Recebe largura e altura separadamente no construtor. |
| **Quadrado** | 4 | $A = l^2$ | Recebe a medida de apenas um lado ($l$), já que todos são iguais. |
| **Triângulo** | 3 | $A = \sqrt{s(s-a)(s-b)(s-c)}$ | Utiliza a Fórmula de Heron. Recebe os 3 lados ($a, b, c$). |

> **Nota sobre o Triângulo:** A Fórmula de Heron exige o cálculo prévio do semiperímetro da forma, representado pela variável $s$, onde $s = \frac{a + b + c}{2}$.

---

## 🚀 Destaques Técnicos

* **Validação Matemática:** A classe `Triangulo` valida os três lados informados para garantir que eles obedeçam à regra da *desigualdade triangular*. Caso as medidas não formem um triângulo válido, o construtor lança uma exceção `IllegalArgumentException`.
* **Boas Práticas de POO:** Todas as classes de domínio contam com atributos encapsulados (privados), acessados via métodos *Getters* e *Setters*, além da implementação do método `toString()` para facilitar a depuração e exibição dos dados.
* **Polimorfismo em Ação:** A classe `Main.java` serve como ponto de entrada da aplicação, instanciando as diferentes formas geométricas e chamando seus métodos de forma genérica através de referências do tipo da interface `FormasGeometricas`.

---

## 🛠️ Como Executar o Projeto

**Pré-requisitos:** É necessário ter o [Java JDK](https://www.oracle.com/br/java/technologies/downloads/) instalado na sua máquina (versão 8 ou superior).

1. Clone este repositório ou faça o download dos arquivos `.java`.
2. Abra um terminal e navegue até o diretório contendo os arquivos fonte.
3. Compile todos os arquivos Java utilizando o comando:
   ```bash
   javac *.java
