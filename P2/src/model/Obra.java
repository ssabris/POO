package model;

/**
 * Obra representa um livro/título na biblioteca.
 *
 * ALTERAÇÕES v2:
 *   - Unificada com Copia: agora carrega codigoBarras, quantidadeTotal e quantidadeDisponivel
 *   - A tabela `copia` foi eliminada do banco
 */
public class Obra {
    private int    id;
    private String titulo;
    private String autor;
    private String codigoBarras;       // NOVO
    private int    quantidadeTotal;    // NOVO
    private int    quantidadeDisponivel; // NOVO

    // Construtor completo (usado ao carregar do banco)
    public Obra(int id, String titulo, String autor,
                String codigoBarras, int quantidadeTotal, int quantidadeDisponivel) {
        this.id = id;
        this.titulo = titulo;
        this.autor  = autor;
        this.codigoBarras = codigoBarras;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    // Construtor simplificado para INSERT (id=0, qtds definidas pelo banco)
    public Obra(int id, String titulo, String autor, String codigoBarras, int quantidadeTotal) {
        this(id, titulo, autor, codigoBarras, quantidadeTotal, quantidadeTotal);
    }

    public int    getId()                   { return id; }
    public String getTitulo()               { return titulo; }
    public String getAutor()                { return autor; }
    public String getCodigoBarras()         { return codigoBarras; }
    public int    getQuantidadeTotal()      { return quantidadeTotal; }
    public int    getQuantidadeDisponivel() { return quantidadeDisponivel; }

    public boolean isDisponivel() {
        return quantidadeDisponivel > 0;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo + " — " + autor
                + " (disp: " + quantidadeDisponivel + "/" + quantidadeTotal + ")";
    }
}
