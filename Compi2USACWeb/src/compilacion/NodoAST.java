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
    
    //indica si debe omitirse al ejecutarse el código
    public boolean omitir;
    
    @Override
    public String toString(){
        String lexemaRed = lexema.concat("");        
        if (lexemaRed.length() > 10){
            lexemaRed = lexemaRed.substring(0,10) + "...";
        }
        String cad = " <table border=\"0\" cellborder=\"1\" cellspacing=\"0\">" +
                "       <tr><td bgcolor=\"yellow\">" + tipo.toString() + "</td></tr>" +
                "       <tr><td bgcolor=\"lightblue\"><font color=\"#0000ff\">" + lexemaRed + "</font></td></tr>" +
                "       <tr><td bgcolor=\"#f0e3ff\"><font color=\"#ff1020\">"+ (valor==null?"":(valor.getClass().getName())) + "</font></td></tr>" +
                "     </table>";
        
        //return cad;
        cad = tipo.toString() + "\n" + (lexema==null?"":lexema)+ "\n" + (valor==null?"":(valor.getClass().getName())) + "\n" + "Omitir: " + omitir  + "\n" + "id: " + id;        
        cad = cad.replace("\"", "\\\"");
        return cad;
    }
    
    public NodoAST(TipoNodo tipo, String lexema, int linea, int columna, String archivoFuente){
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea + 1;
        //this.columna = columna - lexema.length() + 1;        
        this.columna = columna + 1;
        this.archivoFuente = archivoFuente;
        id = this.hashCode();
        hijos = new ArrayList();
        omitir = false;
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
        omitir = false;
    }
    
    
    /**CREA UN nodo y le agrega 3 hijos*/
    public NodoAST(TipoNodo tipo, Object valor, int linea, int columna, String archivoFuente, NodoAST[] hijos){
        this.valor = valor;
        this.tipo = tipo;
        this.lexema = valor.toString();
        this.linea = linea + 1;        
        //this.columna = columna - lexema.length() + 1;        
        this.columna = columna + 1;
        id = this.hashCode();
        this.hijos = new ArrayList<NodoAST>();
        for (NodoAST hijo : hijos){
            this.hijos.add(hijo);
        }
        
        this.archivoFuente = archivoFuente;
        omitir = false;
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
        omitir = false;
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
        omitir = false;
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
    
    public ArrayList<NodoAST> getHijos(TipoNodo tipo){
        ArrayList<NodoAST> children = new ArrayList<>();
        for(NodoAST hijo : this.hijos)
            if (hijo.tipo == tipo)
                children.add(hijo);
        return children;
    }
    
    public NodoAST getHijo(TipoNodo tipo){
        for(NodoAST hijo : this.hijos)
            if (hijo.tipo == tipo)
                return hijo;
        return null;        
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
    
    public ArrayList<TipoNodo> getEstilosAplicables(){
        ArrayList<TipoNodo> estilos = new ArrayList<>();
        switch(tipo){
            case chtml:
            case encabezado:
            case cuerpo:
            case cjs:
            case ccss:
            case titulo:
            case salto:
                //ningún estilo aplicable
                break;
            case panel:
                estilos.add(TipoNodo.alineado);
                estilos.add(TipoNodo.fondoElemento);
                
                estilos.add(TipoNodo.borde);
                estilos.add(TipoNodo.opaque);
                
                estilos.add(TipoNodo.autoredimension);
                break;
                
            case texto:   
            case boton:
            case enlace:
            case tabla:
            case areaTexto:
            case cajaTexto:
            case cajaOpciones:
                estilos.add(TipoNodo.alineado);
                estilos.add(TipoNodo.texto);
                estilos.add(TipoNodo.formato);
                estilos.add(TipoNodo.fuente);
                estilos.add(TipoNodo.tamTex);
                estilos.add(TipoNodo.fondoElemento);
                
                estilos.add(TipoNodo.visible);
                estilos.add(TipoNodo.borde);
                estilos.add(TipoNodo.opaque);
                estilos.add(TipoNodo.colorTex);                
                
                estilos.add(TipoNodo.autoredimension);
                break;
            
            case imagen:
                estilos.add(TipoNodo.alineado);    
                
                estilos.add(TipoNodo.visible); 
                
                estilos.add(TipoNodo.autoredimension);
                break;
                        
                
            case fila:                
                estilos.add(TipoNodo.texto);                
                estilos.add(TipoNodo.fuente);
                estilos.add(TipoNodo.tamTex);
                estilos.add(TipoNodo.fondoElemento);
                
                estilos.add(TipoNodo.autoredimension);
                break;
                    
            case celda:                
                estilos.add(TipoNodo.texto);
            case celdaEnc:
                estilos.add(TipoNodo.formato);
                estilos.add(TipoNodo.fuente);
                estilos.add(TipoNodo.tamTex);
                estilos.add(TipoNodo.fondoElemento);
                
                estilos.add(TipoNodo.colorTex);
                
                estilos.add(TipoNodo.autoredimension);
                break;   
            
            case opcion:
                estilos.add(TipoNodo.alineado);
                estilos.add(TipoNodo.texto);
                estilos.add(TipoNodo.formato);
                estilos.add(TipoNodo.fuente);                
                estilos.add(TipoNodo.fondoElemento);
                                
                estilos.add(TipoNodo.opaque);    
                
                estilos.add(TipoNodo.autoredimension);
                break;
            
            case spinner:
                estilos.add(TipoNodo.alineado);
                
                estilos.add(TipoNodo.visible);                
                estilos.add(TipoNodo.opaque);    
                
                estilos.add(TipoNodo.autoredimension);                
                break;
        }        
        return estilos;
    }
    
}
