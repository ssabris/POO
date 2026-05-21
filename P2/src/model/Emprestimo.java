package model;

import java.time.LocalDate;

/**
 * Emprestimo associa Leitor, Copia e Funcionario (associação):
 * todos existem de forma independente e se relacionam neste contexto.
 */
public class Emprestimo {
    private int id;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao; // null enquanto não devolvido
    private Leitor leitor;           // ASSOCIAÇÃO
    private Copia copia;             // ASSOCIAÇÃO
    private Funcionario funcionario; // ASSOCIAÇÃO

    public Emprestimo(int id, LocalDate dataEmprestimo, LocalDate dataDevolucao,
                    Leitor leitor, Copia copia, Funcionario funcionario) {
        this.id = id;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.leitor = leitor;
        this.copia = copia;
        this.funcionario = funcionario;
    }

    public int getId() { return id; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public Leitor getLeitor() { return leitor; }
    public Copia getCopia() { return copia; }
    public Funcionario getFuncionario() { return funcionario; }

    public boolean isDevolvido() {
        return dataDevolucao != null;
    }

    @Override
    public String toString() {
        String status = isDevolvido() ? "Devolvido em " + dataDevolucao : "Em aberto";
        return "[" + id + "] " + copia.getObra().getTitulo()
                + " | " + leitor.getNome()
                + " | " + dataEmprestimo
                + " | " + status;
    }
}
