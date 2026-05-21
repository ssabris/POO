package model;

/**
 * Copia agrega Obra (agregação): uma Cópia não pode existir sem uma Obra,
 * mas a Obra existe independentemente da Cópia.
 */
public class Copia {
    private int id;
    private String codigoBarras;
    private Obra obra; // AGREGAÇÃO

    public Copia(int id, String codigoBarras, Obra obra) {
        this.id = id;
        this.codigoBarras = codigoBarras;
        this.obra = obra;
    }

    public int getId() { return id; }
    public String getCodigoBarras() { return codigoBarras; }
    public Obra getObra() { return obra; }

    @Override
    public String toString() {
        return "[" + id + "] Cód: " + codigoBarras + " | " + obra.getTitulo();
    }
}
