package model;

import java.time.LocalDate;

/**
 * Reserva associa Leitor e Obra (associação).
 */
public class Reserva {
    private int id;
    private LocalDate dataReserva;
    private Leitor leitor; // ASSOCIAÇÃO
    private Obra obra;     // ASSOCIAÇÃO

    public Reserva(int id, LocalDate dataReserva, Leitor leitor, Obra obra) {
        this.id = id;
        this.dataReserva = dataReserva;
        this.leitor = leitor;
        this.obra = obra;
    }

    public int getId() { return id; }
    public LocalDate getDataReserva() { return dataReserva; }
    public Leitor getLeitor() { return leitor; }
    public Obra getObra() { return obra; }

    @Override
    public String toString() {
        return "[" + id + "] " + obra.getTitulo()
                + " | " + leitor.getNome()
                + " | Reservado em: " + dataReserva;
    }
}
