public class Produto {
    private int id;
    private String nome;
    private String categoria;
    private double preco;

    public Produto(int id, String nome, String categoria, double preco) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public double getPreco() { return preco; }

    @Override
    public String toString() {
        // MELHORIA: Usando locale pt-BR para formatar o preço corretamente com vírgula
        return String.format("[%d] %s - R$ %.2f (%s)", id, nome, preco, categoria)
                     .replace(".", ",");
    }
}
