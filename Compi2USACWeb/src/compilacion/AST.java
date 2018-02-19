/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import java.io.FileWriter;

/**
 *
 * @author Nelson Jair
 */
public class AST {
    NodoAST raiz;     
    private String rutaGrafo = "src/compilacion/";
    
    public void generarGrafo(){
        generarArchivoDot();
        try {
            Runtime.getRuntime().exec("dot " + rutaGrafo + "AST.dot -o " + rutaGrafo + "AST.png -Tpng");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    
    public void generarArchivoDot(){
        try{
            FileWriter escritor = new FileWriter( rutaGrafo + "AST.dot");
            escritor.write("graph AST{\n");
            agregarNodosArchivoDot(raiz, escritor);
            escritor.write("}");
            escritor.close();
        }catch(Exception ex){
            System.err.println(ex + " En generarArchivoDot()");
        }             
    }
    private void agregarNodosArchivoDot(NodoAST nodo, FileWriter escritor) throws Exception{
        if (nodo == null)
            return;        
        escritor.write(nodo.id + " [label=\"" + nodo.lexema + "\"];\n");
        for(NodoAST hijo: nodo.hijos){
            escritor.write(nodo.id + " -- " + hijo.id + ";\n");
            agregarNodosArchivoDot(hijo, escritor);
        }
    }
}


