public class Usuario {
    private int id;
    private String nome;
    private String gostoFavorito;

    public Usuario(int id, String nome, String gostoFavorito) {
        this.id = id;
        this.nome = nome;
        this.gostoFavorito = gostoFavorito;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getGostoFavorito() { return gostoFavorito; }
}
