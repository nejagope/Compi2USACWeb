/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import java.util.ArrayList;

/**
 *
 * @author Nelson Jair
 */
public class NodoAST {
    String lexema, archivoFuente;
    int linea, columna, id;
    TipoNodo tipo;
    Object valor;    
    ArrayList <NodoAST> hijos;
    
    public NodoAST(TipoNodo tipo, String lexema, int linea, int columna, String archivoFuente){
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea + 1;
        //this.columna = columna - lexema.length() + 1;        
        this.columna = columna + 1;
        this.archivoFuente = archivoFuente;
        id = this.hashCode();
        hijos = new ArrayList();
    }
    
    
    public NodoAST(TipoNodo tipo, Object valor, int linea, int columna, String archivoFuente){
        this.valor = valor;
        this.tipo = tipo;
        this.lexema = valor.toString();
        this.linea = linea + 1;        
        //this.columna = columna - lexema.length() + 1;        
        this.columna = columna + 1;
        id = this.hashCode();
        hijos = new ArrayList();
        this.archivoFuente = archivoFuente;
    }
    
    /**CREA UN nodo y le agrega dos hijos*/
    public NodoAST(TipoNodo tipo, Object valor, int linea, int columna, String archivoFuente, NodoAST hijo1, NodoAST hijo2){
        this.valor = valor;
        this.tipo = tipo;
        this.lexema = valor.toString();
        this.linea = linea + 1;        
        //this.columna = columna - lexema.length() + 1;        
        this.columna = columna + 1;
        id = this.hashCode();
        hijos = new ArrayList();
        hijos.add(hijo1);
        hijos.add(hijo2);
        this.archivoFuente = archivoFuente;
    }
    
    /**CREA UN nodo y agrega un hijo*/
    public NodoAST(TipoNodo tipo, Object valor, int linea, int columna, String archivoFuente, NodoAST hijo1){
        this.valor = valor;
        this.tipo = tipo;
        this.lexema = valor.toString();
        this.linea = linea + 1;        
        //this.columna = columna - lexema.length() + 1;        
        this.columna = columna + 1;
        id = this.hashCode();
        hijos = new ArrayList();
        hijos.add(hijo1);     
        this.archivoFuente = archivoFuente;
    }
    
    public void agregarHijo(NodoAST nodo){
        if (nodo != null)
            this.hijos.add(nodo);
    }
    
    public NodoAST getHijo(int indice){
        try{
            return hijos.get(indice);
        }catch(Exception ex){
            return null;
        }
    }
    
    public boolean tieneHijos(){
        if (hijos.size() > 0)
            return true;
        return false;
    }
    
    public int cantidadHijos(){
        if (hijos != null)
            return hijos.size();
        return 0;
    }
}
