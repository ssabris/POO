public class Retangulo implements FormasGeometricas {

    private double largura;
    private double altura;

    public Retangulo(double largura, double altura) {
        this.largura = largura;
        this.altura = altura;
    }

    @Override
    public int quantidadeArestas() {
        // Retângulo possui 4 arestas
        return 4;
    }

    @Override
    public double calculaArea() {
        // Área = largura * altura
        return largura * altura;
    }

    public double getLargura() {
        return largura;
    }

    public void setLargura(double largura) {
        this.largura = largura;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    @Override
    public String toString() {
        return String.format("Retângulo [largura=%.2f, altura=%.2f, arestas=%d, área=%.2f]",
                largura, altura, quantidadeArestas(), calculaArea());
    }
}
