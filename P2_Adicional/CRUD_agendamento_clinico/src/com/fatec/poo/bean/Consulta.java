package com.fatec.poo.bean;

public class Consulta {
    private int codigo;
    private int codigoMedico;
    private int codigoPaciente;
    private String data;       // formato: yyyy-MM-dd
    private String hora;       // formato: HH:mm
    private String status;     // AGENDADA, REALIZADA, CANCELADA
    private String observacao;

    // Campos auxiliares (nomes para exibição na tela)
    private String nomeMedico;
    private String nomePaciente;

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public int getCodigoMedico() { return codigoMedico; }
    public void setCodigoMedico(int codigoMedico) { this.codigoMedico = codigoMedico; }
    public int getCodigoPaciente() { return codigoPaciente; }
    public void setCodigoPaciente(int codigoPaciente) { this.codigoPaciente = codigoPaciente; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public String getNomeMedico() { return nomeMedico; }
    public void setNomeMedico(String nomeMedico) { this.nomeMedico = nomeMedico; }
    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }

    @Override
    public String toString() {
        return codigo + " - " + data + " " + hora + " | " + nomeMedico + " x " + nomePaciente;
    }
}
