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
public class Simbolo { 
    public boolean agregarATabla;
    
    public TipoSimbolo tipo; 
    public String id;
    public NodoAST nodo;
    //chtml        
    public String grupo;
    
    public String ruta;
    public String fondo;
    public String click;
    public Object valor;
    public int ancho;
    public int alto;
    public String alineado; 
    
    public String evtListo;
    public String evtModificado;

    //public ArrayList<Simbolo> elementos;
    
    // var js
    public String ambito;
    public int longitud;
    
    //funcion js
    public ArrayList<Simbolo> parametros;
    public NodoAST bloqueSentencias;
    
    //css
    public String texto;
    public String formato;
    public String fuente;
    public String tamTex;
    public boolean visible;
    public String borde;
    public String opaque;
    public String colorTex;
    public String autoRedimension;
    
    public Simbolo(NodoAST nodo){
        agregarATabla = false;
        
        switch(nodo.tipo){
            case chtml:
                this.tipo = TipoSimbolo.componente;        
                this.nodo = nodo;
                this.id = "chtml";
                agregarATabla = true;
                break;                
            case panel:
            case areaTexto:
            case imagen:
            case boton:
            case enlace:
            case tabla:
            case fila:
            case celdaEnc:
            case celda:
            case cajaTexto:
            case cajaOpciones:
            case opcion:
            case spinner:
                //Obtener el id o clase, en dado caso se agrega a la tabla de símbolos
                NodoAST atribs = nodo.getHijo(TipoNodo.atribs);
                if (atribs != null){
                    NodoAST nodoID = atribs.getHijo(TipoNodo.identificador);
                    NodoAST nodoGrupo = atribs.getHijo(TipoNodo.grupo);
                    if (nodoID != null){                        
                        NodoAST nodoValor = nodoID.getHijo(TipoNodo.cadenaValor);
                        if (nodoValor != null)
                            this.id = nodoValor.lexema;
                    }
                    if (nodoGrupo != null){                        
                        NodoAST nodoValor = nodoGrupo.getHijo(TipoNodo.cadenaValor);
                        if (nodoValor != null)
                            this.grupo = nodoValor.lexema;
                    }
                    if (this.id != null || this.grupo != null)
                        agregarATabla = true;
                }
                break;
            
            //cjs
            
        }
        
    }
        
}
