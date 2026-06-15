package model;

/**
 * ALTERAÇÕES v2:
 *   - Campo `matricula_func` adicionado (UNIQUE no banco) — substitui cpf
 *   - Campo `ativo` para Soft Delete
 */
public class Funcionario extends Pessoa {
    private String  cargo;
    private String  matriculaFunc; // NOVO: identificador único do funcionário
    private boolean ativo;

    public Funcionario(int id, String nome, String cargo, String matriculaFunc, boolean ativo) {
        super(id, nome);
        this.cargo         = cargo;
        this.matriculaFunc = matriculaFunc;
        this.ativo         = ativo;
    }

    // Construtor de conveniência para formulário (ativo=true por padrão)
    public Funcionario(int id, String nome, String cargo, String matriculaFunc) {
        this(id, nome, cargo, matriculaFunc, true);
    }

    public String  getCargo()         { return cargo; }
    public String  getMatriculaFunc() { return matriculaFunc; }
    public boolean isAtivo()          { return ativo; }

    @Override
    public String getIdentificacao() {
        return "Funcionário | Cargo: " + cargo;
    }
}
