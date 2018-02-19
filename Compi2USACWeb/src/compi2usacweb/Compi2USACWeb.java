/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi2usacweb;

import compilacion.Compilador;

/**
 *
 * @author NELSONJAIR
 */
public class Compi2USACWeb {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Compilador c = new Compilador();
        String rutaArchivo = "C:\\Users\\NELSONJAIR\\Documents\\NetBeansProjects\\Compi2USACWeb\\Compi2USACWeb\\src\\compilacion\\entrada1.chtml";
        if (!c.compilar(rutaArchivo)){
            c.mostrarErroresConsola();
        }else{
            c.mostrarTablaSimbolosConsola();
        }   
    }
    
}
