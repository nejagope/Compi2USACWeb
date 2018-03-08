/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import java.awt.Component;
import java.util.ArrayList;

/**
 *
 * @author Nelson Jair
 */
public class Simbolo { 
    
    public boolean agregarATabla;
    
    public Component componente;
    
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
    
    public Simbolo(){
        
    }
    
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
            case texto:
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
                    NodoAST nodoID = atribs.getHijo(TipoNodo.id);
                    NodoAST nodoGrupo = atribs.getHijo(TipoNodo.grupo);
                    if (nodoID != null){                        
                        NodoAST nodoValor = nodoID.getHijo(TipoNodo.cadenaValor);
                        if (nodoValor != null)
                            this.id = nodoValor.lexema;
                    }else{
                        this.id = String.valueOf(nodo.id);                         
                    }
                    if (nodoGrupo != null){                        
                        NodoAST nodoValor = nodoGrupo.getHijo(TipoNodo.cadenaValor);
                        if (nodoValor != null)
                            this.grupo = nodoValor.lexema;
                    }
                    if (this.id != null || this.grupo != null){
                        this.tipo = TipoSimbolo.componente;
                        agregarATabla = true;
                        this.nodo = nodo;                        
                    }
                }
                break;
            
            //ccss
            case regla:
                NodoAST nodoConjunto = nodo.getHijo(TipoNodo.conjunto);
                if (nodoConjunto != null){
                    NodoAST nodoID = nodoConjunto.getHijo(TipoNodo.id);
                    NodoAST nodoGrupo = nodoConjunto.getHijo(TipoNodo.grupo);
                    NodoAST nodoValor = nodoConjunto.getHijo(TipoNodo.identificador);
                    if (nodoID != null){                                               
                        if (nodoValor != null)
                            this.id = nodoValor.lexema;
                    }
                    if (nodoGrupo != null){                                                
                        if (nodoValor != null)
                            this.grupo = nodoValor.lexema;
                    }
                    
                    if (id != null || grupo != null){
                        this.nodo = nodo;
                        this.tipo = TipoSimbolo.estilo;
                        this.agregarATabla = true;
                    }
                }
                                
                break;
           
        }
        
    }
    
    public String toString(){
        String val = String.format("Tipo: %s; Id: %s; Grupo: %s; Ámbito: %s; ComponenteJava: %s", tipo, id, grupo, ambito, componente == null? "-" : componente.toString());
        
        return val;
    }
        
}
