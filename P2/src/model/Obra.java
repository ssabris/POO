package model;

public class Obra {
    private int id;
    private String titulo;
    private String autor;

    public Obra(int id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo + " - " + autor;
    }
}
