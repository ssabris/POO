public class Triangulo implements FormasGeometricas {

    private double ladoA;
    private double ladoB;
    private double ladoC;

    public Triangulo(double ladoA, double ladoB, double ladoC) {
        if (!isValido(ladoA, ladoB, ladoC)) {
            throw new IllegalArgumentException(
                "Triângulo inválido: a soma de dois lados deve ser maior que o terceiro lado.");
        }
        this.ladoA = ladoA;
        this.ladoB = ladoB;
        this.ladoC = ladoC;
    }

    private boolean isValido(double a, double b, double c) {
        return (a + b > c) && (a + c > b) && (b + c > a);
    }

    @Override
    public int quantidadeArestas() {
        // Triângulo possui 3 arestas
        return 3;
    }

    @Override
    public double calculaArea() {
        // Fórmula de Heron: √(s*(s-a)*(s-b)*(s-c))
        // onde s = semiperímetro = (a + b + c) / 2
        double s = (ladoA + ladoB + ladoC) / 2;
        return Math.sqrt(s * (s - ladoA) * (s - ladoB) * (s - ladoC));
    }

    public double getLadoA() {
        return ladoA;
    }

    public void setLadoA(double ladoA) {
        this.ladoA = ladoA;
    }

    public double getLadoB() {
        return ladoB;
    }

    public void setLadoB(double ladoB) {
        this.ladoB = ladoB;
    }

    public double getLadoC() {
        return ladoC;
    }

    public void setLadoC(double ladoC) {
        this.ladoC = ladoC;
    }

    @Override
    public String toString() {
        return String.format("Triângulo [a=%.2f, b=%.2f, c=%.2f, arestas=%d, área=%.2f]",
                ladoA, ladoB, ladoC, quantidadeArestas(), calculaArea());
    }
}
