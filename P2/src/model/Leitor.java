package model;

/**
 * ALTERAÇÕES v2:
 *   - Adicionado campo `ativo` para Soft Delete
 *   - `matricula` já tinha UNIQUE no banco; sem mudança no model
 */
public class Leitor extends Pessoa {
    private String  matricula;
    private boolean ativo; // NOVO: Soft Delete

    public Leitor(int id, String nome, String matricula, boolean ativo) {
        super(id, nome);
        this.matricula = matricula;
        this.ativo     = ativo;
    }

    // Construtor de conveniência (ativo=true por padrão)
    public Leitor(int id, String nome, String matricula) {
        this(id, nome, matricula, true);
    }

    public String  getMatricula() { return matricula; }
    public boolean isAtivo()      { return ativo; }

    @Override
    public String getIdentificacao() {
        return "Leitor | Matrícula: " + matricula;
    }
}
