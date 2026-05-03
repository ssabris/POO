public class Quadrado implements FormasGeometricas {

    private double lado;

    public Quadrado(double lado) {
        this.lado = lado;
    }

    @Override
    public int quantidadeArestas() {
        // Quadrado possui 4 arestas
        return 4;
    }

    @Override
    public double calculaArea() {
        // Área = lado²
        return lado * lado;
    }

    public double getLado() {
        return lado;
    }

    public void setLado(double lado) {
        this.lado = lado;
    }

    @Override
    public String toString() {
        return String.format("Quadrado [lado=%.2f, arestas=%d, área=%.2f]",
                lado, quantidadeArestas(), calculaArea());
    }
}
