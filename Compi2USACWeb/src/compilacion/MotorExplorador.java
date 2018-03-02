/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Njgonzalez
 */
public class MotorExplorador {
    Container documento;
    TablaSimbolos ts;    
    public String tituloPestaña;
    
    public MotorExplorador(Container documento, TablaSimbolos ts){        
        this.documento = documento;
        this.ts = ts;        
        tituloPestaña = null;
    }
    
    public void iniciar(){
        tituloPestaña = null;
        if (ts != null){
            NodoAST nodoCHTML = ts.getComponenteByID("chtml").nodo;        
            agregarComponentes(documento, nodoCHTML);        
        }
    }
    
    private void agregarComponentes(Container container, NodoAST nodo){
        if (nodo == null)
            return;
        int x=0, y=0;
        int widthContainer = 0, heightContainer = 0;
        for (NodoAST n: nodo.hijos){
            Component nuevoComponent = getComponent(n);
            if (nuevoComponent != null){
                container.add(nuevoComponent);
                nuevoComponent.setLocation(x, y);                
                y += nuevoComponent.getPreferredSize().height;
                heightContainer += y;
                int anchoNuevoComponente = nuevoComponent.getPreferredSize().width;
                if (anchoNuevoComponente > widthContainer)
                    widthContainer = anchoNuevoComponente;
                
                if (nuevoComponent instanceof Container){
                    agregarComponentes((Container)nuevoComponent, n);                
                    continue;
                }
            }
            agregarComponentes(container, n);
        }
        if (heightContainer > container.getPreferredSize().height)
            container.setPreferredSize(new Dimension(container.getPreferredSize().width, heightContainer));
        if (widthContainer > container.getPreferredSize().width)
            container.setPreferredSize(new Dimension(widthContainer, container.getPreferredSize().height));
    }
    
    private Component getComponent(NodoAST nodo){
        switch(nodo.tipo){
            case panel:
                JPanel panel = new JPanel(null);                
                panel.setSize(getDimension(nodo));
                panel.setPreferredSize(getDimension(nodo));
                panel.setBackground(getBackgroud(nodo));                
                return panel;
            case titulo:
                try{                    
                    NodoAST nodoTitTex = nodo.getHijo(TipoNodo.textoPlano);
                    if (nodoTitTex != null)
                        this.tituloPestaña = nodoTitTex.lexema;
                }catch(Exception ex){}
                break;
            case texto:
                    NodoAST nodoTex = nodo.getHijo(TipoNodo.textoPlano);
                    if (nodoTex != null){
                        JTextArea ta = new JTextArea(nodoTex.lexema);
                        ta.setEditable(false);
                        ta.setSize(getDimension(nodo));
                        ta.setPreferredSize(getDimension(nodo));
                        return ta;
                    }
                    break;
            case imagen:
                    String ruta = getValorAtributo(nodo, TipoNodo.ruta, true);
                    Object alto = getValorAtributo(nodo, TipoNodo.alto, false, Double.class);
                    Object ancho = getValorAtributo(nodo, TipoNodo.ancho, false, Double.class);                    
                    int width =0, height =0;
                    if (alto != null)
                        height = (int)Math.ceil((double)alto);
                    if (ancho != null)
                        width = (int)Math.ceil((double)ancho);
                    
                    if (ruta != null){
                        File archivo = new File(ruta);
                        Image imagen = Toolkit.getDefaultToolkit().getImage( archivo.getAbsolutePath() );
                        
                        if (alto != null && ancho != null)                            
                            imagen = imagen.getScaledInstance(width, height, 1);
                        
                        ImageIcon icono = new ImageIcon(imagen);
                        JLabel labelImg = new JLabel(icono);
                        labelImg.setPreferredSize(new Dimension(icono.getIconWidth(), icono.getIconHeight()));
                        labelImg.setSize(labelImg.getPreferredSize());
                        return labelImg;
                    }
                    break;
        }        
        return null;
    }
    
    private Object getValorAtributo(NodoAST nodo, TipoNodo tipo, boolean buscarEnContenido, Class clase){
        String valStr = getValorAtributo(nodo, tipo, buscarEnContenido);
        try{
            if (clase == Double.class){
                return Double.parseDouble(valStr);
            }
        }catch(Exception ex){
        }
        return null;
    }
    
    private String getValorAtributo(NodoAST nodo, TipoNodo tipo, boolean buscarEnContenido){
        NodoAST atribs = nodo.getHijo(TipoNodo.atribs);
        if (atribs != null){
            NodoAST nodoAtrib = atribs.getHijo(tipo);
            if (nodoAtrib != null){
                return nodoAtrib.getHijo(TipoNodo.cadenaValor).lexema;                
            }else if (buscarEnContenido){
                nodoAtrib = atribs.getHijo(TipoNodo.textoPlano);
                if (nodoAtrib != null)
                    return nodoAtrib.lexema;
            }
        }
        return null;
    }
    
    private Dimension getDimension(NodoAST nodo){
        int ancho = 500;
        int alto = 20;
        NodoAST atribs = nodo.getHijo(TipoNodo.atribs);
        if (atribs != null){
            NodoAST nodoAlto = atribs.getHijo(TipoNodo.alto);
            if (nodoAlto != null){
                String valStr = nodoAlto.getHijo(TipoNodo.cadenaValor).lexema;
                try{
                    alto = (int)(Double.parseDouble(valStr));
                }catch(Exception ex){}
            }
            NodoAST nodoAncho = atribs.getHijo(TipoNodo.ancho);
            if (nodoAlto != null){
                String valStr = nodoAncho.getHijo(TipoNodo.cadenaValor).lexema;
                try{
                    ancho = (int)(Double.parseDouble(valStr));
                }catch(Exception ex){}
            }
        }
        return new Dimension(ancho, alto);
    }
    
    private Color getBackgroud(NodoAST nodo){
        Color color = Color.WHITE;
        NodoAST atribs = nodo.getHijo(TipoNodo.atribs);
        if (atribs != null){
            NodoAST nodoFondo = atribs.getHijo(TipoNodo.fondo);
            if (nodoFondo != null){
                String valStr = nodoFondo.getHijo(TipoNodo.cadenaValor).lexema;
                try{
                    java.lang.reflect.Field field = Color.class.getField(valStr);
                    color = (Color)field.get(null);
                }catch(Exception ex){}
            }
        }
        return color;
    }
    
    
    
}
