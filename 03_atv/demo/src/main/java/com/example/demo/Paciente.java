package com.example.demo;

public class Paciente {
    // Atributos privados conforme o diagrama
    private int codigo;
    private String nome;
    private String cpf;
    private String telefone;
    private String genero;
    private int idade;
    private String email;

    // Construtor Padrão (Vazio)
    public Paciente() {
        this.codigo = 0;
        this.nome = "";
        this.cpf = "";
        this.telefone = "";
        this.genero = "";
        this.idade = 0;
        this.email = "";
    }

    // Construtor com Parâmetros
    public Paciente(int codigo, String nome, String cpf, String telefone, String genero, int idade, String email) {
        setCodigo(codigo);
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setGenero(genero);
        setIdade(idade);
        setEmail(email);
    }

    // Getters e Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Métodos de Regra de Negócio (conforme o diagrama)
    public void cadastrar() {
        System.out.println("Paciente " + getNome() + " cadastrado com sucesso!");
    }

    public void consultar() {
        System.out.println("Consultando prontuário do paciente: " + getNome());
    }

    // Método Mostrar ajustado
    public void mostrar() {
        System.out.println("---- Dados: Paciente ----");
        System.out.println("Código = " + getCodigo());
        System.out.println("Nome = " + getNome());
        System.out.println("CPF = " + getCpf());
        System.out.println("Telefone = " + getTelefone());
        System.out.println("Gênero = " + getGenero());
        System.out.println("Idade = " + getIdade());
        System.out.println("Email = " + getEmail());
    }
}