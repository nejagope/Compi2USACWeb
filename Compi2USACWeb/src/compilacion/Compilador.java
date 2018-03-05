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
     * retorna @true si se pudo generar el AST, de lo contrario, @false
     */
    public boolean compilar(String nombreArchivoFuente){
        this.nombreArchivoFuente = nombreArchivoFuente;
        File archivoFuente = new File(nombreArchivoFuente); 
        if (archivoFuente.exists()) {
            //verificar la extensión del archivo
            if (nombreArchivoFuente.endsWith(".chtml")
                    || nombreArchivoFuente.endsWith(".cjs")
                    || nombreArchivoFuente.endsWith(".ccss")) {
                
                if (archivoFuente.canRead()){
                    this.nombreArchivoFuente = archivoFuente.getName();
                    carpetaFuente = archivoFuente.getParent();
                    //Análisis léxico y sintactico
                    AnalisisSintactico sintactico = new AnalisisSintactico(nombreArchivoFuente, errores, tablaSimbolos);
                    //se analiza el archivo fuente y se produce un Arbol de sintaxis abstracta (ast)
                    boolean resultado = sintactico.generarAST();
                    //se genera un grafo para visualizar el ast generado al final del analisis sintactico
                    sintactico.generarGrafoAST();
                    
                    return resultado;
                    //Análisis semántco
                    /*AnalisisSemantico a_sem = new AnalisisSemantico(tablaSimbolos, errores, sintactico.getAST());
                        //se recopilan en en una lista los simbolos declarados en el archivo fuente y en sus archvios importados
                        a_sem.llenarTablaSimbolos();  
                        //se realiza la comprobación semantica de tipos
                        a_sem.comprobarTipos();                
                     */
                }else{
                    errores.add(new ErrorCode(TipoError.archivoNoPuedeLeerse, nombreArchivoFuente, "No puede leerse el archivo especificado"));                    
                }
            }else{
                errores.add(new ErrorCode(TipoError.extensionNoPermida, nombreArchivoFuente, "La extensión del archivo no es una extensión permitida"));
            }
        }else{ //archivo a compilar no existe
            errores.add(new ErrorCode(TipoError.archivoNoEncontrado, nombreArchivoFuente, "No se halló el archivo especificado"));
        }
        return false;
    }
    
    
    public void mostrarTablaSimbolosConsola(){
       System.out.println(tablaSimbolos.toString());
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
