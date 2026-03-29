package com.example.demo;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try{
           Recepcionista maria = new Recepcionista();
           Medico joao = new Medico();

           Agenda agenda20260401 = maria.marcarAgenda();
           joao.realizarConsulta(agenda20260401);     
        }
        catch(Exception e){
            System.out.println("Ocorreu um erro "+ e.getMessage());    
        }
        
    }
}
