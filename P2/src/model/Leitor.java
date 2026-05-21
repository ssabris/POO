package model;

public class Leitor extends Pessoa {
    private String matricula;

    public Leitor(int id, String nome, String matricula) {
        super(id, nome);
        this.matricula = matricula;
    }

    public String getMatricula() { return matricula; }

    @Override
    public String getIdentificacao() {
        return "Leitor | Matrícula: " + matricula;
    }
}
