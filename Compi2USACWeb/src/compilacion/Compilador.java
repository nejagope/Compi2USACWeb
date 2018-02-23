/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Nelson Jair
 */
public class Compilador {    
    //var de analisis
    public TablaSimbolos tablaSimbolos;
    ArrayList <ErrorCode> errores;
    String nombreArchivoFuente;
    
    static String carpetaFuente;
    
    

    public Compilador() {       
        this.tablaSimbolos = new TablaSimbolos();
        this.errores = new ArrayList();        
        //System.out.println(Analizador.carpetaFuente );        
    }
        
    
    /** Realiza el analisis lexico, sintactico y semántico del archivo fuente y 
     * retorna @true si no se encontraron errores o, de lo contrario, @false
     */
    public boolean compilar(String nombreArchivoFuente){
        this.nombreArchivoFuente = nombreArchivoFuente;
        File archivoFuente = new File(nombreArchivoFuente);        
        this.nombreArchivoFuente = archivoFuente.getName();
        Compilador.carpetaFuente = archivoFuente.getParent();
        //Análisis léxico y sintactico
        AnalisisSintactico sintactico = new AnalisisSintactico(this.nombreArchivoFuente, errores);
        //se analiza el archivo fuente y se produce un Arbol de sintaxis abstracta (ast)
        sintactico.generarAST();
        //se genera un grafo para visualizar el ast generado al final del analisis sintactico
        sintactico.generarGrafoAST();
        
        //Análisis semántco
        /*AnalisisSemantico a_sem = new AnalisisSemantico(tablaSimbolos, errores, sintactico.getAST());
        //se recopilan en en una lista los simbolos declarados en el archivo fuente y en sus archvios importados
        a_sem.llenarTablaSimbolos();  
        //se realiza la comprobación semantica de tipos
        a_sem.comprobarTipos();                
        */           
        return errores.isEmpty();
    }
    
    
    public void mostrarTablaSimbolosConsola(){
        System.out.println("---------------------TABLA DE SIMBOLOS------------------------");
        for (Simbolo sim : tablaSimbolos){
            System.out.println(sim.nombre + "; " + sim.tipo + "; ");
            /*
            if (sim.tablaSimbolos != null){
                System.out.println("\tContiene sub tabla con " + sim.tablaSimbolos.size() + " elementos");
                for(Simbolo subSim: sim.tablaSimbolos){
                    System.out.println("\t" + subSim.nombre + "; " + subSim.tipo + "; " + subSim.tipoRetorno);
                }
            }
            */
        }
    }
    
    public void mostrarErroresConsola(){
        if (errores.isEmpty()){
            System.out.println("No se hallaron errores");
            return;
        }
        System.out.println("---------------------ERRORES------------------------");
        for (ErrorCode error: errores){
            System.out.println("Error " + error.tipo + ": " + error.cadena + "; " + error.mensaje + "; " + error.linea + "," + error.columna + "; " + error.archivo);
        }
    }
    
   
        
}