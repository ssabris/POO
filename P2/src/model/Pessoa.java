package model;

public abstract class Pessoa {
    protected int id;
    protected String nome;

    public Pessoa(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    // Método abstrato: força herança a implementar polimorfismo
    public abstract String getIdentificacao();

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " - " + getIdentificacao();
    }
}
