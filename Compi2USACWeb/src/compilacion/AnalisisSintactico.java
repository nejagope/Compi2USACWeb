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
    
    boolean archivoFuenteExistente;
    
    //se utiliza para darles un id a los nodos del arbol
    private int indiceNodo;

    public AnalisisSintactico(String nombreArchivoFuente, ArrayList<ErrorCode> errores) {
        ast = null;
        this.nombreArchivoFuente = Compilador.carpetaFuente + "\\" + nombreArchivoFuente;        
        this.errores = errores;
        File archivoFuente = new File(this.nombreArchivoFuente);        
        archivoFuenteExistente = archivoFuente.exists();
    }
    
    public boolean existeArchivoFuente(){
        return archivoFuenteExistente;
    }
    
    public void generarAST(){          
        ScannerCHTML scanner;
        try{
            scanner= new ScannerCHTML(new FileInputStream(nombreArchivoFuente), nombreArchivoFuente, errores);
        }catch(Exception ex){             
            return;
        }
        ParserCHTML parser = new ParserCHTML(scanner, nombreArchivoFuente, errores);
        try{
            parser.parse();        
        }catch(Exception ex){
            //agregar error a lista de errores
            System.err.println(ex);
            return;
        }
        ast = parser.getAST();
        
        indiceNodo = 0;
        if (ast!= null)
            asignarIDs(ast.raiz);                       
                
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
