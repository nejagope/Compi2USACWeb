/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
            Simbolo sCHTML = ts.getComponenteByID("chtml");
            if (sCHTML != null){
                NodoAST nodoCHTML = ts.getComponenteByID("chtml").nodo;
                agregarComponentes(documento, nodoCHTML);        
            }else{
                JLabel l404 = getImageLabel(new File("src/images/404.jpg").getAbsolutePath(), null, null);
                if (l404 != null)
                    documento.add(l404);
                tituloPestaña = "No encontrado";
            }
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
                if (n.tipo != TipoNodo.celda && n.tipo != TipoNodo.celdaEnc){ //
                    y += nuevoComponent.getPreferredSize().height;
                    heightContainer += y;
                }else
                    x += nuevoComponent.getPreferredSize().width;
                
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
        if (nodo.omitir)
            return null;
        
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
                    Object width = null, height = null;
                    if (alto != null)
                        height = (int)Math.ceil((double)alto);
                    if (ancho != null)
                        width = (int)Math.ceil((double)ancho);
                    return getImageLabel(ruta, width, height); 
            
            case enlace:
                Object rutaEnlace = getValorAtributo(nodo, TipoNodo.ruta, false, String.class);
                Object textoEnlace = getValorAtributo(nodo, TipoNodo.textoPlano, true, String.class);
                
                if (textoEnlace != null && rutaEnlace != null){                
                    JLabel lEnlace = new JLabel((String)textoEnlace);
                    lEnlace.setPreferredSize(getDimension(nodo));
                    lEnlace.setSize(getDimension(nodo));
                    lEnlace.setForeground(Color.BLUE);
                    Font font = lEnlace.getFont();
                    Map attributes = font.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    lEnlace.setFont(font.deriveFont(attributes));
                    lEnlace.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    lEnlace.addMouseListener(new MouseAdapter() {
        
                        @Override
                        public void mouseClicked(MouseEvent arg0) {
                             System.out.println("Diste clic en el enlace");
                        }
                    });
                    return lEnlace;
                }
                break;
                
            case boton:
                Object rutaBoton = getValorAtributo(nodo, TipoNodo.ruta, false, String.class);
                Object textoBoton = getValorAtributo(nodo, TipoNodo.textoPlano, true, String.class);
                if (textoBoton != null){
                    JButton boton = new JButton((String)textoBoton);
                    boton.setSize(getDimension(nodo));
                    boton.setPreferredSize(getDimension(nodo));
                    boton.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            System.out.println("Diste clic en el botón");
                        }
                    });
                    return boton;
                }
                break;
            
            case tabla:
                JPanel panelTbl = new JPanel(null);                
                panelTbl.setSize(getDimension(nodo, ComponentSize.MEDIUM, ComponentSize.MEDIUM));
                panelTbl.setPreferredSize(getDimension(nodo, ComponentSize.MEDIUM, ComponentSize.MEDIUM));
                panelTbl.setBackground(Color.BLACK);                
                return panelTbl;                                
            
            case fila:
                JPanel panelFila = new JPanel(null);                
                Dimension dim = getDimension(nodo, ComponentSize.MEDIUM, ComponentSize.MEDIUM);
                panelFila.setSize(dim);
                panelFila.setPreferredSize(getDimension(nodo, ComponentSize.MEDIUM, ComponentSize.MEDIUM));
                panelFila.setBackground(Color.BLUE);                
                return panelFila;                                
            
            case celda: 
            case celdaEnc:
                JPanel panelCelda = new JPanel(null);                
                panelCelda.setSize(getDimension(nodo, ComponentSize.SMALL, ComponentSize.SMALL));
                panelCelda.setPreferredSize(getDimension(nodo, ComponentSize.SMALL, ComponentSize.SMALL));
                panelCelda.setBackground(Color.RED);   
                                                  
                String textoPlanoCelda = getValorAtributo(nodo, TipoNodo.textoPlano, true);
                if (textoPlanoCelda != null){
                    JLabel lTextoPlano = new JLabel(textoPlanoCelda);
                    lTextoPlano.setPreferredSize(getDimension(nodo));
                    lTextoPlano.setSize(getDimension(nodo));                           
                    lTextoPlano.setBackground(Color.red);
                    panelCelda.add(lTextoPlano);
                }
                return panelCelda;                
        }        
        return null;
    }
    
    public JLabel getImageLabel(String ruta, Object width, Object height){        
         if (ruta != null){
            File archivo = new File(ruta);
            Image imagen = Toolkit.getDefaultToolkit().getImage( archivo.getAbsolutePath() );

            if (width != null && height != null)                            
                imagen = imagen.getScaledInstance((int)width, (int)height, 1);            

            ImageIcon icono = new ImageIcon(imagen);
            JLabel labelImg = new JLabel(icono);
            labelImg.setPreferredSize(new Dimension(icono.getIconWidth(), icono.getIconHeight()));
            labelImg.setSize(labelImg.getPreferredSize());
            return labelImg;
        }
        return null;
    }
    
    private Object getValorAtributo(NodoAST nodo, TipoNodo tipo, boolean buscarEnContenido, Class clase){
        String valStr = getValorAtributo(nodo, tipo, buscarEnContenido);
        try{
            if (clase == Integer.class){
                return Integer.parseInt(valStr);
            }
            if (clase == Double.class){
                return Double.parseDouble(valStr);
            }
            if (clase == String.class){
                return valStr;
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
            }
        }
        if (buscarEnContenido){
            NodoAST nodoCont = nodo.getHijo(TipoNodo.textoPlano);
            if (nodoCont != null)
                return nodoCont.lexema;
        }
        return null;
    }
    
    private Dimension getDimension(NodoAST nodo){        
        return getDimension(nodo, ComponentSize.MEDIUM, ComponentSize.SMALL);
    }
    
    private Dimension getDimension(NodoAST nodo, ComponentSize sAncho, ComponentSize sAlto){        
        int ancho;
        int alto;
        
        switch (sAlto){            
            case BIG:
                alto = 1000;                
                break;
                
            case MEDIUM:
                alto = 500;                
                break;
                
            case SMALL:                
                alto = 20;
                break;                
            default:                
                alto = 20;
        }
        
        switch (sAncho){            
            case BIG:
                ancho = 1000;                
                break;
                
            case MEDIUM:
                ancho = 500;                
                break;
                
            case SMALL:                
                ancho = 20;
                break;                
            default:                
                ancho = 20;
        }
        
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
    
    private enum ComponentSize{
        BIG, MEDIUM, SMALL
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
