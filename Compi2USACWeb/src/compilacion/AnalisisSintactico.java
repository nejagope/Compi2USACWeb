/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 *
 * @author Nelson Jair
 */
public class AnalisisSintactico {
    AST ast;
    String nombreArchivoFuente;
    ArrayList <ErrorCode> errores;
    java_cup.runtime.Scanner scanner;
    java_cup.runtime.lr_parser parser;
    boolean analisisExitoso;
    
    boolean archivoFuenteExistente;
    
    //se utiliza para darles un id a los nodos del arbol
    private int indiceNodo;

    public AnalisisSintactico(String nombreArchivoFuente, ArrayList<ErrorCode> errores) {
        ast = null;
        this.nombreArchivoFuente = Compilador.carpetaFuente + "\\" + nombreArchivoFuente;        
        this.errores = errores;
        File archivoFuente = new File(this.nombreArchivoFuente);        
        archivoFuenteExistente = archivoFuente.exists();
        scanner = null;
        parser = null;
        
        //indica que se realizó el análisis con éxito (no indica si se hallaron errores o no)
        analisisExitoso = false; 
    }
    
    public boolean existeArchivoFuente(){
        return archivoFuenteExistente;
    }
    
    public boolean analizarArchivo(){
        analisisExitoso = false;
        
        File archivoFuente = new File(nombreArchivoFuente); 
        if (archivoFuente.exists()) {
            //verificar la extensión del archivo
            if (nombreArchivoFuente.endsWith(".chtml")
                    || nombreArchivoFuente.endsWith(".cjs")
                    || nombreArchivoFuente.endsWith(".ccss")) {
                
                if (!archivoFuente.canRead()){                    
                    errores.add(new ErrorCode(TipoError.archivoNoPuedeLeerse, nombreArchivoFuente, "No puede leerse el archivo especificado"));
                    return false;
                }
            }else{
                errores.add(new ErrorCode(TipoError.extensionNoPermida, nombreArchivoFuente, "La extensión del archivo no es una extensión permitida"));
                return false;
            }
        }else{ //archivo a compilar no existe
            errores.add(new ErrorCode(TipoError.archivoNoEncontrado, nombreArchivoFuente, "No se halló el archivo especificado"));
            return false;
        }
        
        try{
            if (nombreArchivoFuente.endsWith(".chtml"))
                scanner= new ScannerCHTML(new FileInputStream(nombreArchivoFuente), nombreArchivoFuente, errores);
            else if (nombreArchivoFuente.endsWith(".cjs"))
                scanner= new ScannerCJS(new FileInputStream(nombreArchivoFuente), nombreArchivoFuente, errores);
            else if (nombreArchivoFuente.endsWith(".ccss"))
                scanner= new ScannerCCSS(new FileInputStream(nombreArchivoFuente), nombreArchivoFuente, errores);
        }catch(Exception ex){ 
            errores.add(new ErrorCode(TipoError.otro, "No se pudo instanciar el scanner para analizar el archivo" + nombreArchivoFuente, ex.toString()));
            scanner = null;
            parser = null;
            return false;
        }
                
        try{
            if (nombreArchivoFuente.endsWith(".chtml"))
                parser = new ParserCHTML(scanner, nombreArchivoFuente, errores);
            else if (nombreArchivoFuente.endsWith(".cjs"))
                parser = new ParserCJS(scanner, nombreArchivoFuente, errores);
            else if (nombreArchivoFuente.endsWith(".ccss"))
                parser = new ParserCCSS(scanner, nombreArchivoFuente, errores);
        
            parser.parse();
            analisisExitoso = true;
        }catch(Exception ex){
            errores.add(new ErrorCode(TipoError.otro, "No se pudo ejecutar el parseado del archivo" + nombreArchivoFuente, ex.toString()));            
            scanner = null;
            parser = null;
            return false;
        }
        return true;
    }
    
    public boolean generarAST(){          
        analizarArchivo();
        try{
            if (analisisExitoso){

                if (parser instanceof ParserCHTML)
                    ast = ((ParserCHTML)parser).getAST();
                else if (parser instanceof ParserCJS)
                    ast = ((ParserCJS)parser).getAST();
                else if (parser instanceof ParserCCSS)
                    ast = ((ParserCCSS)parser).getAST();

                indiceNodo = 0;
                if (ast!= null)
                    asignarIDs(ast.raiz);                       
                return true;
            }
            errores.add(new ErrorCode(TipoError.otro, "No se pudo generar el AST para el análisis del archivo " + nombreArchivoFuente, "No se ha realizado un análisis sintáctico exitoso del archivo"));            
        }catch(Exception ex){
            errores.add(new ErrorCode(TipoError.otro, "No se pudo generar el AST para el análisis del archivo " + nombreArchivoFuente, ex.toString()));            
        }
        return false;        
    }                
    
    public void generarGrafoAST(){
        if (ast!= null)
            ast.generarGrafo();
    }
    
    public AST getAST(){
        return ast;
    }
    
    private void asignarIDs(NodoAST nodo){        
        if (nodo == null)
            return;                
        for (NodoAST hijo : nodo.hijos){            
            asignarIDs(hijo);            
        }          
        nodo.id = indiceNodo;
        indiceNodo ++;                    
    }
    
    
}
