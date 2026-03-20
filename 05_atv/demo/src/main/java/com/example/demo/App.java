package com.example.demo;

public class App {
    public static void main( String[] args ) {
        try {
            int x = 1;
            var p1 = new Paciente();
            p1.setCodigo(x++);
            p1.setNome("jose da silva");
            
            var p2 = new Paciente(x++, "maria souza","maria@norton.net.br");
            p2.mostrar();

            Medico m1 = new Medico();
            // m1.setCrm("234"); // Lançaria exception pelo tamanho < 7

            var r1 = new Recepcionista("mariazinha", "345345345", "3244-2344", "234234");
            r1.mostrar();
            
        } catch(Exception e) {
            System.out.println("Ocorreu um erro: " + e.getMessage());    
        }
    }
}