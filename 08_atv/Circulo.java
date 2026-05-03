public class Circulo implements FormasGeometricas {

    private double raio;

    public Circulo(double raio) {
        this.raio = raio;
    }

    @Override
    public int quantidadeArestas() {
        // Círculo não possui arestas
        return 0;
    }

    @Override
    public double calculaArea() {
        // Área = π * r²
        return Math.PI * raio * raio;
    }

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        this.raio = raio;
    }

    @Override
    public String toString() {
        return String.format("Círculo [raio=%.2f, arestas=%d, área=%.2f]",
                raio, quantidadeArestas(), calculaArea());
    }
}
