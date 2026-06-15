package model;

import java.time.LocalDate;

/**
 * Emprestimo associa Leitor, Obra e Funcionario.
 *
 * ALTERAÇÕES v2:
 *   - Substituído `Copia copia` por `Obra obra` (tabela copia foi eliminada)
 *   - 3 datas: dataEmprestimo, dataPrevistaDevolucao, dataDevolucaoReal
 *   - dataDevolucaoReal == null  →  empréstimo em aberto
 */
public class Emprestimo {
    private int        id;
    private LocalDate  dataEmprestimo;
    private LocalDate  dataPrevistaDevolucao; // NOVO
    private LocalDate  dataDevolucaoReal;     // NOVO (substitui dataDevolucao)
    private Leitor     leitor;
    private Obra       obra;                  // MUDANÇA: antes era Copia
    private Funcionario funcionario;

    public Emprestimo(int id,
                      LocalDate dataEmprestimo,
                      LocalDate dataPrevistaDevolucao,
                      LocalDate dataDevolucaoReal,
                      Leitor leitor, Obra obra, Funcionario funcionario) {
        this.id = id;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.leitor = leitor;
        this.obra   = obra;
        this.funcionario = funcionario;
    }

    public int        getId()                    { return id; }
    public LocalDate  getDataEmprestimo()        { return dataEmprestimo; }
    public LocalDate  getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public LocalDate  getDataDevolucaoReal()     { return dataDevolucaoReal; }
    public Leitor     getLeitor()                { return leitor; }
    public Obra       getObra()                  { return obra; }
    public Funcionario getFuncionario()          { return funcionario; }

    public boolean isDevolvido() {
        return dataDevolucaoReal != null;
    }

    /** Verifica se o empréstimo está atrasado (em aberto e passou da data prevista). */
    public boolean isAtrasado() {
        return !isDevolvido() && LocalDate.now().isAfter(dataPrevistaDevolucao);
    }

    @Override
    public String toString() {
        String status = isDevolvido()
                ? "Devolvido em " + dataDevolucaoReal
                : (isAtrasado() ? "ATRASADO" : "Em aberto");
        return "[" + id + "] " + obra.getTitulo()
                + " | " + leitor.getNome()
                + " | " + dataEmprestimo + " → " + dataPrevistaDevolucao
                + " | " + status;
    }
}
