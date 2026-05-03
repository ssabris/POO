public class Main {
    public static void main(String[] args) {

        FormasGeometricas circulo    = new Circulo(5);
        FormasGeometricas retangulo  = new Retangulo(8, 4);
        FormasGeometricas quadrado   = new Quadrado(6);
        FormasGeometricas triangulo  = new Triangulo(3, 4, 5);

        FormasGeometricas[] formas = { circulo, retangulo, quadrado, triangulo };

        System.out.println("=== Formas Geométricas ===\n");
        for (FormasGeometricas forma : formas) {
            System.out.println(forma);
            System.out.printf("  Arestas : %d%n", forma.quantidadeArestas());
            System.out.printf("  Área    : %.2f%n%n", forma.calculaArea());
        }
    }
}
