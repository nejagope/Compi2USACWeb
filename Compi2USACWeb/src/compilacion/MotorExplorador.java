/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import compi2usacweb.Estilo;
import compi2usacweb.Explorador;
import compi2usacweb.Navegacion;
import compi2usacweb.TabNavegador;
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
import java.util.ArrayList;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Njgonzalez
 */
public class MotorExplorador {
    //Container documento;    
    TablaSimbolos ts;    
    public String tituloPestaña;
    public Compilador compilador;
    public ArrayList <Compilador> compiladores;
    public Explorador explorador;
    
    public MotorExplorador(Explorador explorador){           
        this.explorador = explorador;
        compiladores = new ArrayList<>();
    }
    
    public void cambiarCompilador(int indxTab){ 
        System.out.println(indxTab);
        if ( indxTab == -1){
            this.compilador = null;
            this.ts = null;
        }
        else if ( indxTab < compiladores.size()){
            Compilador compiNuevo = compiladores.get(indxTab);
            if (compiNuevo != null){
                compilador = compiNuevo;
                ts = compiNuevo.tablaSimbolos;
            }
        }else{
            Compilador compiNuevo = new Compilador();
            compiladores.add(compiNuevo);
            
            this.compilador = compiNuevo;
            this.ts = compiNuevo.tablaSimbolos;            
        }
        System.out.println(compilador);
    }
    
    public void eliminarCompilador(int indxTab){
        if ( indxTab < compiladores.size() && indxTab > -1)
            compiladores.remove(indxTab);
    }
    
    public TabNavegador getTab(String rutaArchivoCHTML, int indxTab, Navegacion navegacion){  
        Compilador compiNuevo = new Compilador();
        this.compilador = compiNuevo;
        this.ts = compiNuevo.tablaSimbolos;
        if (indxTab == -1){
            compiladores.add(compiNuevo);
        }else{
            if ( indxTab < compiladores.size()){
                compiladores.set(indxTab, compiNuevo);
            }
        }
        
        JPanel contenedor = new JPanel(null);
        TabNavegador tab = new TabNavegador("", contenedor);
        
        contenedor.setBackground(Color.WHITE);         
        File file = new File(rutaArchivoCHTML);
        String nombreArchivo = file.getName();
        
        if (compilador.compilar(rutaArchivoCHTML)){
            Simbolo sCHTML = ts.getComponenteByID("chtml");
            if (sCHTML != null){
                NodoAST nodoCHTML = ts.getComponenteByID("chtml").nodo;
                agregarComponentes(contenedor, nodoCHTML, new Estilo());
                String titulo = this.tituloPestaña;
                if (titulo == null)
                    titulo = nombreArchivo;
                
                tab.titulo = titulo;
            }else{
                JLabel l404 = getImageLabel(new File("src/images/404.jpg").getAbsolutePath(), null, null);
                if (l404 != null)
                    contenedor.add(l404);
                
                tab.titulo = "No se hallo el tag CHTML";
            }
        }else{
            contenedor.setPreferredSize(new Dimension(explorador.getDocumentWidth(), 600));            
            JLabel l404 = getImageLabel(new File("src/images/404.jpg").getAbsolutePath(), null, null);
            
            if (l404 != null){
                l404.setSize(new Dimension(explorador.getDocumentWidth(), 500));
                l404.setPreferredSize(new Dimension(explorador.getDocumentWidth(), 500));
                l404.setBorder(new EtchedBorder(EtchedBorder.RAISED));                
                l404.setHorizontalAlignment(JLabel.CENTER);                
                contenedor.add(l404);
            }
            
            tab.titulo = "Archivo no encontrado";
        }
        compilador.mostrarTablaSimbolosConsola();
        compilador.mostrarErroresConsola();   
        //contenedor.setPreferredSize(new Dimension(20000, 20000));
        //System.out.println(contenedor.getSize().toString());
        
        return tab;
    }
    /*

    private void agregarComponentes(Container container, NodoAST nodo){
        if (nodo == null)
            return;
        int x=0, y=0;
        int widthContainer = 0, heightContainer = 0;
        for (NodoAST n: nodo.hijos){
            Component nuevoComponent = getComponent(n);
            if (nuevoComponent != null){
                //aplicar estilos ccss
                aplicarEstilos(n, nuevoComponent);
                
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
    */
    private void agregarComponentes(Container container, NodoAST nodo, Estilo estiloHeredado){
        if (nodo == null)
            return;
        int x=0, y=0;
        int widthContainer = 0, heightContainer = 0;
        for (NodoAST n: nodo.hijos){
            Component nuevoComponent = getComponent(n);
            if (nuevoComponent != null){
                //estilo definido para el componente
                Estilo estiloNodo = getEstilo(n);
                Estilo estiloAplicar = estiloNodo.mezclar(estiloHeredado);
                
                aplicarEstilos(n, nuevoComponent, estiloAplicar);
                
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
                    agregarComponentes((Container)nuevoComponent, n, estiloAplicar);                
                    continue;
                }
            }
            agregarComponentes(container, n, estiloHeredado);
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
                //panel.setBackground(getBackgroud(nodo));                
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
                             explorador.requestTab((String)rutaEnlace, Navegacion.CARGAR);
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
                            if (rutaBoton != null){
                                explorador.requestTab((String)rutaBoton, Navegacion.CARGAR);
                            }
                        }
                    });
                    return boton;
                }
                break;
            
            case tabla:
                JPanel panelTbl = new JPanel(null);                
                panelTbl.setSize(getDimension(nodo, ComponentSize.MEDIUM, ComponentSize.MEDIUM));
                panelTbl.setPreferredSize(getDimension(nodo, ComponentSize.MEDIUM, ComponentSize.MEDIUM));
                //panelTbl.setBackground(Color.BLACK);                
                return panelTbl;                                
            
            case fila:
                JPanel panelFila = new JPanel(null);                
                Dimension dim = getDimension(nodo, ComponentSize.MEDIUM, ComponentSize.MEDIUM);
                panelFila.setSize(dim);
                panelFila.setPreferredSize(getDimension(nodo, ComponentSize.MEDIUM, ComponentSize.MEDIUM));
                //panelFila.setBackground(Color.BLUE);                
                return panelFila;                                
            
            case celda: 
            case celdaEnc:
                JPanel panelCelda = new JPanel(null);                
                panelCelda.setSize(getDimension(nodo, ComponentSize.SMALL, ComponentSize.SMALL));
                panelCelda.setPreferredSize(getDimension(nodo, ComponentSize.SMALL, ComponentSize.SMALL));
                //panelCelda.setBackground(Color.RED);   
                                                  
                String textoPlanoCelda = getValorAtributo(nodo, TipoNodo.textoPlano, true);
                if (textoPlanoCelda != null){
                    JLabel lTextoPlano = new JLabel(textoPlanoCelda);
                    lTextoPlano.setPreferredSize(getDimension(nodo));
                    lTextoPlano.setSize(getDimension(nodo));                           
                    //lTextoPlano.setBackground(Color.red);
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
    
    /**
     * Examina los atributos de un tag chtml 
     * por el id y el grupo ccss del nodo si los tuviera     
     * @param nodo que tenga como hijo el nodo de tipo atribs
     * @param tipo de nodo correpondiente al atributo que desea obtenerse
     * @param buscarEnContenido buscar el tipo especificado en el contenido de los tags chtml de apertura y cierre
     * @param clase clase a la que se debe castear el valor del atributo si se halla
     * @return el Objeto de casteado a la clase especificada que cooresponde al valor del atributo con el tipo especificado
    */
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
    
    /**
     * Examina los atributos de un tag chtml 
     * por el id y el grupo ccss del nodo si los tuviera     
     * @param nodo que tenga como hijo el nodo de tipo atribs
     * @param tipo de nodo correpondiente al atributo que desea obtenerse
     * @param buscarEnContenido buscar el tipo especificado en el contenido de los tags chtml de apertura y cierre
     * @return el String que cooresponde al valor del atributo con el tipo especificado
    */
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
    
    /*
    private Color getBackgroud(NodoAST nodo){           
        try{
            Color colorVal = getColor((String)getValorAtributo(nodo, TipoNodo.fondo, false, String.class));
            return colorVal != null ? colorVal : Color.WHITE;
        }catch(Exception ex){
            return Color.WHITE;
        }        
    }
    */
    private void aplicarEstilos(NodoAST nodo, Component componente, Estilo estilo){
        Color colorFondo = estilo.getColorFondo();
        Font fuente = estilo.getFuente();
        //fuente.
        switch (nodo.tipo){
            case panel:
                if (colorFondo != null){
                    componente.setBackground(colorFondo);
                }
                break;
            case enlace:
            case texto:
                if (fuente != null){
                    //componente.setFont(fuente); 
                    componente.setFont(fuente);
                }
                break;
        }
    }
    /*
    private void aplicarEstilos(NodoAST nodo, Component componente){
        Object id = getValorAtributo(nodo, TipoNodo.id, false, String.class);
        Object grupo = getValorAtributo(nodo, TipoNodo.grupo, false, String.class);
        ArrayList<Simbolo> estilos = new ArrayList<>();
        
        if (grupo != null){
            ArrayList<Simbolo> sims = ts.getEstilosByGrupo((String)grupo);
            estilos.addAll(sims);
        }
        
        if (id != null){
            estilos.addAll(ts.getEstilosByID((String)id));
        }
        
        ArrayList <TipoNodo> estilosAplicables = nodo.getEstilosAplicables();        
        for (Simbolo estilo : estilos){
            NodoAST nodoRela = estilo.nodo;
            for (NodoAST item : nodoRela.hijos){
                if (estilosAplicables.contains(item.tipo)){
                    //si el estilo es aplicable al componente
                    switch(item.tipo){
                        case fondoElemento:
                            Color colorFondo = getColor(getValorItemCCSS(item));                            
                            if (colorFondo != null){
                                componente.setBackground(colorFondo);
                            }
                            break;
                            
                        case alineado:
                            break;
                    }
                }
            }
        }
    }

    public String getValorItemCCSS(NodoAST item){
        String strValor = null;
        if (item != null){
            NodoAST nodoCad = item.getHijo(TipoNodo.cadenaLit);
            if (nodoCad == null)
                nodoCad = item.getHijo(TipoNodo.cadenaLit2);
            
            if (nodoCad != null){
                strValor = nodoCad.lexema;
            }
        }
        return strValor;
    }
*/    
    /**
     * Examina los atributos de un tab chtml y examina los estilos determinados
     * por el id y el grupo ccss del nodo si los tuviera
     * Unifica toda esta información en un sólo Estilo
     * @param nodo correpondiente al tag chtml
     * @return un Estilo unificadod
    */
    public Estilo getEstilo(NodoAST nodo){
        Estilo estilo = new Estilo();
        //fondo
        estilo.fondo = (String)getValorAtributo(nodo, TipoNodo.fondo, false, String.class);
        //alto
        Object alto = getValorAtributo(nodo, TipoNodo.alto, false, Double.class);
        if (alto != null)
            estilo.alto = (int)Math.ceil((double)alto);
        //ancho
        Object ancho = getValorAtributo(nodo, TipoNodo.ancho, false, Double.class);
        if (ancho != null)
            estilo.ancho = (int)Math.ceil((double)ancho);
        //id
        String id = (String)getValorAtributo(nodo, TipoNodo.id, false, String.class);
        //grupo
        String grupo = (String)getValorAtributo(nodo, TipoNodo.grupo, false, String.class);
        
        ArrayList<Simbolo> estilos = new ArrayList<>();
        
        if (grupo != null){
            ArrayList<Simbolo> sims = ts.getEstilosByGrupo((String)grupo);
            estilos.addAll(sims);
        }
        
        if (id != null){
            estilos.addAll(ts.getEstilosByID((String)id));
        }
                
        for (Simbolo sEstilo : estilos){
            NodoAST nodoRegla = sEstilo.nodo;
            for (NodoAST item : nodoRegla.hijos){
                switch(item.tipo){
                    case fondoElemento:                     
                        estilo.fondo = (String)evaluarCCSS(item);
                        break;   
                    case fuente:                    
                        estilo.fuente = (String)evaluarCCSS(item);
                        break;
                    case texto: 
                    case alineado:
                    case formato:                    
                    case tamTex:
                    case visible:
                    case borde:
                    case opaque:
                    case colorTex:
                    case autoredimension:
                        
                        break;
                }
            }
        }
        return estilo;
    }
    
    
    public Object evaluarCCSS(NodoAST nodo){
        if (nodo == null)
            return null;
        
        switch (nodo.tipo){
            case fondoElemento:
                return evaluarCCSS(nodo.getHijo(0));
            case texto: 
            case alineado:
            case formato:
            case fuente:                    
                return evaluarCCSS(nodo.getHijo(0));
            case tamTex:
            case visible:
            case borde:
            case opaque:
            case colorTex:
            case autoredimension:
            case cadenaLit:
            case cadenaLit2:
                return nodo.lexema;
        }
        return null;
    }
}


