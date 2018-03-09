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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;


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
        //System.out.println(indxTab);
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
        //System.out.println(compilador);
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
        contenedor.setPreferredSize(new Dimension(explorador.getDocumentWidth(), 600)); 
        File file = new File(rutaArchivoCHTML);
        String nombreArchivo = file.getName();
        boolean compilacionExitosa = compilador.compilar(rutaArchivoCHTML);
        //compilador.mostrarTablaSimbolosConsola();
        //compilador.mostrarErroresConsola();   
        
        if (compilacionExitosa){
            Simbolo sCHTML = ts.getComponenteByID("chtml");
            if (sCHTML != null){
                NodoAST nodoCHTML = ts.getComponenteByID("chtml").nodo;                
                NodoAST nodoBody = null;
                NodoAST nodoEnc = null;
                
                if (nodoCHTML != null){
                    nodoBody = nodoCHTML.getHijo(TipoNodo.cuerpo);
                    nodoEnc = nodoCHTML.getHijo(TipoNodo.encabezado);
                    if (nodoBody != null){
                        Object fondo = getValorAtributo(nodoBody, TipoNodo.fondo, false, String.class);
                        if (fondo != null){
                            Color c = new Estilo().getColor((String)fondo);
                            if (c != null){
                                contenedor.setBackground(c);
                            }
                        }
                    }
                }
                //crear los componentes de la página
                agregarComponentes(contenedor, nodoBody, new Estilo());
                //ejecutar scripts
                ejecutar(nodoEnc, "global");
                
                System.out.println("------------------ fin programa cjs -----------------");
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
                l404.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));                
                l404.setHorizontalAlignment(JLabel.CENTER);                
                contenedor.add(l404);
            }
            
            tab.titulo = "Archivo no encontrado";
        }
        
        //contenedor.setPreferredSize(new Dimension(20000, 20000));
        //System.out.println(contenedor.getSize().toString());
        
        return tab;
    }

    private void agregarComponentes(Container container, NodoAST nodo, Estilo estiloHeredado){
        if (nodo == null || nodo.tipo == TipoNodo.titulo || nodo.tipo == TipoNodo.atribs)
            return;
        int x=0, y=0;
        int widthContainer = 0, heightContainer = 0;
        
        int indxNodo = -1;
        for (NodoAST n: nodo.hijos){
            
            Component nuevoComponent = getComponent(n);
            if (nuevoComponent != null){
                //agregar referencia al componente en la tabla de símbolos, si es que el nodo tiene id
                Object idComponente = getValorAtributo(n, TipoNodo.id, false, String.class);
                Object grupoComponente = getValorAtributo(n, TipoNodo.grupo, false, String.class);
                if (idComponente != null){
                    Simbolo s = ts.getComponenteByID((String)idComponente);
                    if (s != null){
                        s.componente = nuevoComponent;
                    }
                }
                if(grupoComponente != null){
                    Simbolo s = ts.getComponenteByID(String.valueOf(n.id));
                    if (s != null){
                        s.componente = nuevoComponent;
                    }
                }
                indxNodo ++;
                //estilo definido para el componente
                Estilo estiloNodo = getEstilo(n);
                Estilo estiloAplicar = estiloNodo.mezclar(estiloHeredado);                                
                
                aplicarEstilos(n, nuevoComponent, estiloAplicar);
                
                if (nuevoComponent instanceof Container 
                        && !(nuevoComponent instanceof JLabel) 
                        && !(nuevoComponent instanceof JTextArea)
                        && !(nuevoComponent instanceof JTextField)
                        && !(nuevoComponent instanceof JComboBox)
                        && !(nuevoComponent instanceof JSpinner)
                        ){
                    agregarComponentes((Container)nuevoComponent, n, estiloAplicar);                                    
                }
                
                
                                
                container.add(nuevoComponent);
                nuevoComponent.setLocation(x, y);
                
                
                if (n.tipo == TipoNodo.celda 
                        || n.tipo == TipoNodo.celdaEnc){
                    if (nodo.getHijo(TipoNodo.atribs) == null && nodo.cantidadHijos()-1 == indxNodo
                            || nodo.getHijo(TipoNodo.atribs) != null && nodo.cantidadHijos() == indxNodo){                    
                        heightContainer += nuevoComponent.getPreferredSize().height;
                    }
                    
                    widthContainer += nuevoComponent.getPreferredSize().width;
                    x += nuevoComponent.getPreferredSize().width;
                }else{
                    y += nuevoComponent.getPreferredSize().height;
                    
                    heightContainer = y;
                    if (nuevoComponent.getPreferredSize().width > widthContainer)
                        widthContainer =nuevoComponent.getPreferredSize().width;
                }
                                
            }else
                agregarComponentes(container, n, estiloHeredado);
        }
        
        //container.setPreferredSize(new Dimension(widthContainer, heightContainer));
        Dimension dimCont = getDimension(nodo);
        if (nodo.tipo == TipoNodo.cuerpo)
            dimCont.width = explorador.getWidth();
        
        if (heightContainer > dimCont.height){
            container.setPreferredSize(new Dimension(container.getPreferredSize().width, heightContainer));
            container.setSize(new Dimension(container.getPreferredSize().width, heightContainer));
        }else{
            container.setPreferredSize(new Dimension(container.getPreferredSize().width, dimCont.height));
            container.setSize(new Dimension(container.getPreferredSize().width, dimCont.height));
        }
        if (widthContainer > dimCont.width){
            container.setPreferredSize(new Dimension(widthContainer, container.getPreferredSize().height));
            container.setSize(new Dimension(widthContainer, container.getPreferredSize().height));
        }else{
            container.setPreferredSize(new Dimension(dimCont.width, container.getPreferredSize().height));
            container.setSize(new Dimension(dimCont.width, container.getPreferredSize().height));            
        }
        
        //alineación            
        if (nodo.tipo != TipoNodo.celda && nodo.tipo != TipoNodo.fila && nodo.tipo != TipoNodo.celdaEnc && nodo.tipo != TipoNodo.chtml){
            
            int anchoContenedor = container.getPreferredSize().width;   
            

            if (estiloHeredado.alineado != null){
                switch(estiloHeredado.alineado){
                    case "derecha":                        
                        for (Component comp : container.getComponents()){
                            int dif = anchoContenedor - comp.getWidth();
                            comp.setLocation(dif, comp.getLocation().y);                                                                 
                        }     
                        break;
                    case "centrado":                        
                        for (Component comp : container.getComponents()){
                            int dif = anchoContenedor - comp.getWidth();
                            comp.setLocation((int)dif/2, comp.getLocation().y);                            

                        }
                        break;
                    case "justificado":
                        for (Component comp : container.getComponents()){                            
                            comp.setLocation(0, 0);                            
                            comp.setPreferredSize(new Dimension(anchoContenedor ,comp.getHeight()));
                            comp.setSize(new Dimension(anchoContenedor ,comp.getHeight()));

                        }
                        break;
                }
            }
        }
        
        
    }
    
    private Component getComponent(NodoAST nodo){
        if (nodo.omitir)
            return null;
        
        switch(nodo.tipo){
            
            case panel:
                JPanel panel = new JPanel(null);                
                panel.setSize(getDimension(nodo));
                panel.setPreferredSize(getDimension(nodo));                
                return panel;
            
            case titulo:
                try{                    
                    NodoAST nodoTitTex = nodo.getHijo(TipoNodo.textoPlano);
                    if (nodoTitTex != null)
                        this.tituloPestaña = nodoTitTex.lexema;
                }catch(Exception ex){}
                break;
            
            case texto:
            case areaTexto:
                    NodoAST nodoTex = nodo.getHijo(TipoNodo.textoPlano);
                    if (nodoTex != null){
                        JTextArea ta = new JTextArea(nodoTex.lexema);
                        if (nodo.tipo == TipoNodo.texto)
                            ta.setEditable(false);
                        ta.setSize(getDimension(nodo));
                        ta.setPreferredSize(getDimension(nodo));
                        ta.setWrapStyleWord(true);
                        ta.setAutoscrolls(true);
                        return ta;
                    }
                    break;
            case cajaTexto:
                NodoAST nodoTexCaja = nodo.getHijo(TipoNodo.textoPlano);
                if (nodoTexCaja != null){ 
                    JTextField tf = new JTextField(nodoTexCaja.lexema.replace("\n", " "));                    
                    tf.setSize(getDimension(nodo));
                    tf.setPreferredSize(getDimension(nodo));                    
                    return tf;
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
                
                Object textoEnlace = getValorAtributo(nodo, TipoNodo.textoPlano, true, String.class);
                
                if (textoEnlace != null){                
                    JLabel lEnlace = new JLabel((String)textoEnlace);
                    lEnlace.setPreferredSize(getDimension(nodo));
                    lEnlace.setSize(getDimension(nodo));
                    lEnlace.setForeground(Color.BLUE);
                    Font font = lEnlace.getFont();
                    Map attributes = font.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    lEnlace.setFont(font.deriveFont(attributes));
                    lEnlace.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    Object rutaEnlace = getValorAtributo(nodo, TipoNodo.ruta, false, String.class);
                    if (rutaEnlace != null){
                        lEnlace.addMouseListener(new MouseAdapter() {

                            @Override
                            public void mouseClicked(MouseEvent arg0) {                             
                                 explorador.requestTab((String)rutaEnlace, Navegacion.CARGAR);
                            }
                        });
                    }
                    return lEnlace;
                }
                break;
            
            case cajaOpciones:                
                ArrayList<NodoAST> nodosOp = nodo.getHijos(TipoNodo.opcion);
                String opsCombo[] = new String[nodosOp.size()];
                int i = 0;
                for (NodoAST nodoOp : nodosOp){
                    NodoAST txtOpNodo = nodoOp.getHijo(TipoNodo.textoPlano);
                    String txtOp = "";
                    if (txtOpNodo != null){
                        txtOp = txtOpNodo.lexema;
                    }
                    opsCombo[i] = txtOp;
                    i++;
                }
                JComboBox combo = new JComboBox(opsCombo);
                combo.setSize(getDimension(nodo));
                combo.setPreferredSize(getDimension(nodo));
                return combo;
                            
            case spinner:
                JSpinner spinner = new JSpinner();
                spinner.setPreferredSize(getDimension(nodo));
                spinner.setSize(getDimension(nodo));
                JFormattedTextField txt = ((JSpinner.NumberEditor)spinner.getEditor()).getTextField();
                ((NumberFormatter)txt.getFormatter()).setAllowsInvalid(false);
                Object textoSpinner = getValorAtributo(nodo, TipoNodo.textoPlano, true, String.class);
                if (textoSpinner != null){
                    try{
                        int val = Integer.parseInt((String)textoSpinner);
                        spinner.setValue(val);
                    }catch(Exception ex){
                        spinner.setValue(0);
                    }
                }
                return spinner;
            case salto:
                JPanel panelSalto = new JPanel(null);                
                panelSalto.setPreferredSize(new Dimension(200,1));
                panelSalto.setSize(new Dimension(200,1));
                panelSalto.setVisible(false);
                
                return panelSalto;
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
                //panelTbl.setSize(getDimension(nodo));
                //panelTbl.setPreferredSize(getDimension(nodo));
                //panelTbl.setBackground(Color.BLACK);                
                return panelTbl;                                
            
            case fila:
                JPanel panelFila = new JPanel(null);                
                Dimension dim = getDimension(nodo);
                //panelFila.setSize(dim);
                //panelFila.setPreferredSize(getDimension(nodo));
                //panelFila.setBackground(Color.BLUE);                
                return panelFila;                                
            
            case celda: 
            case celdaEnc:
                JPanel panelCelda = new JPanel(null); 
                return panelCelda;
                
            case textoPlano:
                String textoPlanoCelda = nodo.lexema;
                JLabel lTextoPlano = new JLabel(textoPlanoCelda);
                if (textoPlanoCelda != null){                    
                    lTextoPlano.setPreferredSize(getDimension(nodo));
                    lTextoPlano.setSize(getDimension(nodo));                                                                                       
                }
                return lTextoPlano;
            
                                
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
        int ancho = 0;
        int alto = 0;
        
        
        NodoAST atribs = nodo.getHijo(TipoNodo.atribs);
        NodoAST nodoAlto= null, nodoAncho= null;
        
        if (atribs != null){
            nodoAlto = atribs.getHijo(TipoNodo.alto);
            if (nodoAlto != null){
                String valStr = nodoAlto.getHijo(TipoNodo.cadenaValor).lexema;
                try{
                    alto = (int)(Double.parseDouble(valStr));
                }catch(Exception ex){}
            }
            nodoAncho = atribs.getHijo(TipoNodo.ancho);
            if (nodoAncho != null){
                String valStr = nodoAncho.getHijo(TipoNodo.cadenaValor).lexema;
                try{
                    ancho = (int)(Double.parseDouble(valStr));
                }catch(Exception ex){}
            }
        }
        if (nodoAncho == null){
            ancho = 200;
        }
        if (nodoAlto == null){
            alto = 30;
        }
        return new Dimension(ancho, alto);
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
        Color colorTexto = estilo.getColorTexto();
        Font fuente = estilo.getFuente();
        Boolean opaque = estilo.opaque;
        Boolean visible = estilo.visible;
        String formato = estilo.formato;
        String alineado = estilo.alineado;
        Border borde = estilo.getBorde();
        String textoEstilo = estilo.texto;
        //fuente.
        switch (nodo.tipo){
            case panel:
            case tabla:
            case fila:
            case celda:
                if (colorFondo != null){
                    componente.setBackground(colorFondo);
                }
                if (opaque != null){
                    ((JPanel)componente).setOpaque(opaque);
                }
                if (visible != null)
                    componente.setVisible(visible);
                
                if (borde != null){
                    ((JPanel)componente).setBorder(borde);
                }
                break;
            case enlace:
                if (opaque != null){
                    ((JLabel)componente).setOpaque(opaque);
                }
                
                if (fuente != null){                
                    componente.setFont(fuente);
                }
                if (colorFondo != null){                
                    componente.setBackground(colorFondo);
                }
                if (colorTexto != null){                
                    componente.setForeground(colorTexto);
                }
                if (opaque != null){
                    ((JLabel)componente).setOpaque(opaque);
                } 
                if (visible != null)
                    componente.setVisible(visible);
                if (formato != null){
                    if (formato.contains("mayuscula"))
                        ((JLabel)componente).setText(((JTextArea)componente).getText().toUpperCase());
                    if (formato.contains("minuscula"))
                        ((JLabel)componente).setText(((JLabel)componente).getText().toLowerCase());
                    if (formato.contains("capital-t"))
                        ((JLabel)componente).setText(Estilo.getCapital(((JLabel)componente).getText()));
                }
                
                if (alineado != null){
                    if (alineado.equals("centrado"))
                        ((JLabel)componente).setHorizontalAlignment(JLabel.CENTER); 
                    else if (alineado.equals("derecha"))
                        ((JLabel)componente).setHorizontalAlignment(JLabel.RIGHT); 
                }
                
                if (borde != null){
                    ((JLabel)componente).setBorder(borde);
                }
                
                if (textoEstilo != null){
                    ((JLabel)componente).setText(textoEstilo);
                }
                break;
            case texto:
            case areaTexto:
                if (fuente != null){                
                    componente.setFont(fuente);
                }
                if (colorFondo != null){                
                    componente.setBackground(colorFondo);
                }
                if (colorTexto != null){                
                    componente.setForeground(colorTexto);
                }
                if (opaque != null){
                    ((JTextArea)componente).setOpaque(opaque);
                }
                
                if (visible != null)
                    componente.setVisible(visible);
                if (formato != null){
                    String textoArea = ((JTextArea)componente).getText();
                    if (formato.contains("mayuscula"))                        
                        ((JTextArea)componente).setText(textoArea.toUpperCase());
                    if (formato.contains("minuscula"))
                        ((JTextArea)componente).setText(textoArea.toLowerCase());
                    if (formato.contains("capital-t"))
                        ((JTextArea)componente).setText(Estilo.getCapital(textoArea));                    
                }
                
                if (borde != null){
                    ((JTextArea)componente).setBorder(borde);
                }
                if (textoEstilo != null){
                    ((JTextArea)componente).setText(textoEstilo);
                }
                break;
                
            case cajaTexto:
                if (fuente != null){                
                    componente.setFont(fuente);
                }
                if (colorFondo != null){                
                    componente.setBackground(colorFondo);
                }
                if (colorTexto != null){                
                    componente.setForeground(colorTexto);
                }
                if (opaque != null){
                    ((JTextField)componente).setOpaque(opaque);
                }
                
                if (visible != null)
                    componente.setVisible(visible);
                if (formato != null){
                    String textoArea = ((JTextArea)componente).getText();
                    if (formato.contains("mayuscula"))                        
                        ((JTextField)componente).setText(textoArea.toUpperCase());
                    if (formato.contains("minuscula"))
                        ((JTextField)componente).setText(textoArea.toLowerCase());
                    if (formato.contains("capital-t"))
                        ((JTextField)componente).setText(Estilo.getCapital(textoArea));                    
                }
                
                if (borde != null){
                    ((JTextField)componente).setBorder(borde);
                }
                if (textoEstilo != null){
                    ((JTextField)componente).setText(textoEstilo);
                }
                break;
                
            case cajaOpciones:
                if (fuente != null){                
                    componente.setFont(fuente);
                }
                if (colorFondo != null){                
                    componente.setBackground(colorFondo);
                }
                if (colorTexto != null){                
                    componente.setForeground(colorTexto);
                }
                if (opaque != null){
                    ((JComboBox)componente).setOpaque(opaque);
                }
                
                if (visible != null)
                    componente.setVisible(visible);
                                
                if (borde != null){
                    ((JComboBox)componente).setBorder(borde);
                }
                break;
            case spinner:
                if (fuente != null){                
                    componente.setFont(fuente);
                }
                if (colorFondo != null){                
                    componente.setBackground(colorFondo);
                }
                if (colorTexto != null){                
                    componente.setForeground(colorTexto);
                }
                if (opaque != null){
                    ((JSpinner)componente).setOpaque(opaque);
                }
                
                if (visible != null)
                    componente.setVisible(visible);                
                
                if (borde != null){
                    ((JSpinner)componente).setBorder(borde);
                }
                if (textoEstilo != null){
                    try{
                        ((JSpinner)componente).setValue(Integer.parseInt(textoEstilo));
                    }catch(Exception ex){
                        ((JSpinner)componente).setValue(0);
                    }
                }
                break;
        }
    }
      
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
        
        Object alineado = getValorAtributo(nodo, TipoNodo.alineado, false, String.class);
        if (alineado != null)
            estilo.alineado = alineado.toString();
        
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
                    case colorTex:
                        estilo.colorTex = (String)evaluarCCSS(item);
                        break;   
                    case fondoElemento:                          
                        estilo.fondo = (String)evaluarCCSS(item);
                        break;   
                    case fuente:                    
                        estilo.fuente = (String)evaluarCCSS(item);
                        break;                     
                    case alineado:
                        estilo.alineado = (String)evaluarCCSS(item);   
                        break;
                    case tamTex:
                        estilo.tamTex = evaluarCCSS(item).toString();
                        break;
                    case visible:
                        estilo.visible = (Boolean)evaluarCCSS(item);
                        break;
                    case opaque:                    
                        estilo.opaque = (Boolean)evaluarCCSS(item);
                        break;
                    case formato:
                        estilo.formato = (String)evaluarCCSS(item);
                        break;
                    case texto:
                        estilo.texto = (String)evaluarCCSS(item);
                        break;
                    case borde:
                        estilo.borde = (String)evaluarCCSS(item);
                        break;
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
                return evaluarCCSS(nodo.getHijo(0));
            case alineado:
                return evaluarCCSS(nodo.getHijo(0));
            case izquierda:
                return "izquierda";
            case derecha:
                return "derecha";
            case centrado:
                return "centrado";
            case justificado:
                return "justificado";
            case negrilla:
                return "negrilla";
            case cursiva:
                return "cursiva";
            case minuscula:
                return "minuscula";
            case mayuscula:
                return "mayuscula";
            case capital:
                return "capital-t";
            case fuente:                    
                return evaluarCCSS(nodo.getHijo(0));
            case tamTex:
                return evaluarCCSS(nodo.getHijo(0));
            case colorTex:
                return evaluarCCSS(nodo.getHijo(0));            
            case cadenaLit:
            case cadenaLit2:
                return nodo.lexema;
            case enteroLit:
                return nodo.valor;
            case dobleLit:
                return nodo.valor;
            case booleanoLit:
                return nodo.valor;
            case visible:
            case opaque:
                return evaluarCCSS(nodo.getHijo(0));
            case formato:
            case borde:
                String valForm = "";
                for (NodoAST nodoForm : nodo.hijos){
                    if (!valForm.isEmpty())
                        valForm += "-";
                    valForm += (evaluarCCSS(nodoForm).toString()).toLowerCase();
                }
                return valForm;            
                
            case autoredimension:
                return evaluarCCSS(nodo.getHijo(0)); 
            
            case mas:
            case menos:
            case por:
            case entre:
                Object op1 = evaluarCCSS(nodo.getHijo(0));
                Object op2 = evaluarCCSS(nodo.getHijo(1));                
                switch(nodo.tipo){
                    case mas:
                        if (op1 instanceof Number && op2 instanceof Number)
                            return Double.parseDouble(op1.toString()) + Double.parseDouble(op2.toString());
                        else if (op1 instanceof String || op2 instanceof String){
                            return (op1.toString()) + (op2.toString());
                        }
                    case menos:
                        if (op1 instanceof Number && op2 instanceof Number)
                            return Double.parseDouble(op1.toString()) - Double.parseDouble(op2.toString());
                    case por:
                        if (op1 instanceof Number && op2 instanceof Number)
                            return Double.parseDouble(op1.toString()) * Double.parseDouble(op2.toString());
                    case entre:
                        if (op1 instanceof Number && op2 instanceof Number){
                            if (Double.parseDouble(op2.toString()) != 0)
                                return Double.parseDouble(op1.toString()) / Double.parseDouble(op2.toString());
                            else{
                                //TODO division entre 0
                            }
                        }
                } 
        }
        return null;
    }
    
    public void ejecutar(NodoAST n, String ambito){
        if (n == null)
            return;
        
        switch (n.tipo) {
            case asignacion:
                NodoAST nVar = n.getHijo(0);
                Object val = eval(n.getHijo(1), ambito);
                Simbolo sVar = ts.getVariable(nVar.lexema, ambito);
                if (sVar != null) {
                    sVar.valor = val;
                    //System.out.println(String.format("se asignó %s a %s", val, sVar.id));
                    System.out.println(String.format(" = %s", val));
                } else {
                    //ERR                    
                }
                return;
                
            case funcion:                
                NodoAST nParams = n.getHijo(TipoNodo.parametros);
                //se crea el símbolo de la funcion
                Simbolo sFun = new Simbolo();
                sFun.id = n.lexema;
                sFun.tipo = TipoSimbolo.funcion;
                sFun.ambito = ambito;
                sFun.parametros = new ArrayList<>();

                if (nParams != null) {
                    String ambitoParam = sFun.id + nParams.hijos.size();
                    for (NodoAST param : nParams.hijos) {
                        Simbolo sParam = new Simbolo();
                        sParam.id = param.lexema;
                        sParam.tipo = TipoSimbolo.variable;
                        sParam.ambito = ambitoParam;
                        sParam.nodo = param;
                        sFun.parametros.add(sParam);
                    }
                }

                if (!ts.agregarFuncion(sFun)) {
                    //errores.add(new ErrorCode(TipoError.semantico, n.linea, n.columna, n.lexema, "La función " + n.lexema + " ya ha sido declarada", sourceFile));
                    n.omitir = true;
                } else {
                    for (Simbolo sParam : sFun.parametros) {
                        if (!ts.agregarVariable(sParam)) {
                            //errores.add(new ErrorCode(TipoError.semantico, sParam.n.linea, sParam.n.columna, sParam.id, "Parámetro " + sParam.id + " ya ha sido declarado", sourceFile));
                            n.omitir = true;
                        }
                    }
                }
                return;

            case declaracion:
                NodoAST nID = n.getHijo(0);                
                if (nID.tipo == TipoNodo.asignacion) {
                    nID = n.getHijo(0).getHijo(0); //nID = asignacion
                    Simbolo sID = ts.getVariable(nID.lexema, ambito);
                    if (sID == null){
                        sID = new Simbolo();
                        sID.id = nID.lexema;
                        sID.ambito = ambito;
                        sID.tipo = TipoSimbolo.variable;                                                
                        ts.agregarVariable(sID);
                    }else{
                        //ERR var ya declarada
                    }
                }else{
                    //nID = identificador
                    Simbolo sID = ts.getVariable(nID.lexema, ambito);
                    
                    if (sID == null){
                        sID = new Simbolo();
                        sID.id = nID.lexema;
                        sID.ambito = ambito;
                        sID.tipo = TipoSimbolo.variable;
                        
                        boolean addSim = true;
                        if (n.cantidadHijos() == 2){
                            Object longArray = eval(n.getHijo(1), ambito);
                            if (longArray instanceof Number){
                                sID.longitud = (int)Math.round((Double)longArray);                                
                                ArrayList array =  new ArrayList();
                                sID.valor = array;
                                //se inicializa el array con null
                                for (int i=0; i<sID.longitud; i++)
                                    array.add(null);
                                sID.isArray = true;
                            }else{
                                //ERR
                                addSim = false;
                            }
                        }
                        if (addSim)
                            ts.agregarVariable(sID);
                    }else{
                        //ERR var ya declarada
                    }
                } 
                return;

            default:
                for (NodoAST hijo : n.hijos) {
                    ejecutar(hijo, ambito);
                }
        }
    }
    
    public Object eval(NodoAST n, String ambito){
        switch(n.tipo){
            
            case identificador:
                if (n.cantidadHijos() == 0){ //variable sencilla
                    Simbolo sVar = ts.getVariable(n.lexema, ambito);
                    if (sVar != null){
                        return sVar.valor;
                    }else{
                        //ERR
                        return null;
                    }
                }
                else{
                    //ERR
                        return null;
                }
            //*********************** literales *********************
            case cadenaLit:  
                return n.lexema;
            case enteroLit:                
            case dobleLit:                
            case booleanoLit:                
            case fechaLit:
            case fechaTiempoLit:
                return n.valor;
            //*********************** fin literales *********************
                
            //*********************** mono operaciones *********************  
            case not:
            case negativo:
            case inc:
            case dec:                
                Object opMono = eval(n.getHijo(0), ambito);
                if (opMono == null){
                    //ERR
                    return null;
                }
                switch (n.tipo){
                    case negativo:
                        if (opMono instanceof Number){
                            return -1 * Double.parseDouble(String.valueOf(opMono));
                        }else{
                            //ERR
                            return null;
                        }
                    case inc:
                        if (opMono instanceof Number){
                            return Double.parseDouble(String.valueOf(opMono))+1;
                        }
                        else if (opMono instanceof Boolean){
                            return ((boolean)opMono) ? 2:1;
                        }
                        else{
                            //ERR
                            return null;
                        }
                    case dec:
                        if (opMono instanceof Number){
                            return Double.parseDouble(String.valueOf(opMono))-1;
                        }
                        else if (opMono instanceof Boolean){
                            return ((boolean)opMono) ? 0:-1;
                        }
                        else{
                            //ERR
                            return null;
                        }
                        
                    case not:
                        if (opMono instanceof Boolean){
                            return !(boolean)opMono;
                        }else{
                            //ERR
                            return null;
                        }
                }
                break;
                
            //*********************** binarias *********************  
            //*********************** aritmeticas *********************  
                
            case mas:
            case menos:
            case por:
            case entre:
            case modulo:
            case potencia:  
            
            case igual:
            case diferente:
            case mayor:
            case menor:
            case mayorI:
            case menorI:
                
            case and:
            case or:
                  
                Object op1 = eval(n.getHijo(0), ambito);
                Object op2 = eval(n.getHijo(1), ambito); 
                System.out.print(String.format("%s %s %s", op1, n.tipo, op2));
                if (op1 == null || op2 == null){
                    //ERR
                    return null;
                }
                switch(n.tipo){
                    case mas:
                        if (op1 instanceof Boolean){
                            
                            if (op2 instanceof Boolean)
                                return (boolean)op1 || (boolean)op2;
                            else if (op2 instanceof Number)
                                return Double.parseDouble(String.valueOf(op2)) + (((boolean)op1) ? 1 : 0);
                            else if (op2 instanceof String)
                                return String.valueOf(op1) + (String)op2;
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else if (op1 instanceof Number){
                            
                            if (op2 instanceof Boolean)
                                return Double.parseDouble(String.valueOf(op1)) + (((boolean)op2) ? 1 : 0);
                            else if (op2 instanceof Number)
                                return Double.parseDouble(String.valueOf(op2)) + Double.parseDouble(String.valueOf(op1));
                            else if (op2 instanceof String)
                                return String.valueOf(op1) + (String)op2;
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else if (op1 instanceof String){
                            
                            if (op2 instanceof Boolean)
                                return  (String)op1 + String.valueOf(op2);
                            else if (op2 instanceof Number)
                                return (String)op1 + String.valueOf(op2);
                            else if (op2 instanceof String)
                                return (String)op1 + String.valueOf(op2);
                            else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime)
                                return (String)op1 + String.valueOf(op2);
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else if (op1 instanceof LocalDate){
                            
                            if (op2 instanceof String)
                                return String.valueOf(op1) + (String)op2;
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else if (op1 instanceof LocalDateTime){
                            if (op2 instanceof String)
                                return String.valueOf(op1) + (String)op2;
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else{
                            //ERR
                            return null;
                        }
                        
                    case menos:
                        
                        if (op1 instanceof Boolean){
                                                        
                            if (op2 instanceof Number)
                                return (((boolean)op1) ? 1 : 0) - Double.parseDouble(String.valueOf(op2));                            
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else if (op1 instanceof Number){
                            
                            if (op2 instanceof Boolean)
                                return Double.parseDouble(String.valueOf(op1)) - (((boolean)op2) ? 1 : 0);
                            else if (op2 instanceof Number)
                                return Double.parseDouble(String.valueOf(op1)) - Double.parseDouble(String.valueOf(op2));
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else{
                            //ERR
                            return null;
                        }
                        
                    case por:
                        if (op1 instanceof Boolean){
                            
                            if (op2 instanceof Boolean)
                                return (boolean)op1 && (boolean)op2;
                            else if (op2 instanceof Number)
                                return (((boolean)op1) ? 1 : 0) * Double.parseDouble(String.valueOf(op2));                            
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else if (op1 instanceof Number){
                            
                            if (op2 instanceof Boolean)
                                return Double.parseDouble(String.valueOf(op1)) * (((boolean)op2) ? 1 : 0);
                            else if (op2 instanceof Number)
                                return Double.parseDouble(String.valueOf(op1)) * Double.parseDouble(String.valueOf(op2));
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else{
                            //ERR
                            return null;
                        }
                        
                    case entre:
                        if (op1 instanceof Boolean){
                            
                            if (op2 instanceof Number){
                                if (Double.parseDouble(String.valueOf(op2)) == 0){
                                    //ERR
                                    return null;
                                }
                                return (((boolean)op1) ? 1 : 0) / Double.parseDouble(String.valueOf(op2));                            
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else if (op1 instanceof Number){
                            
                            if (op2 instanceof Boolean){
                                if (!(boolean)op2){
                                    //ERR
                                    return null;
                                }
                                return Double.parseDouble(String.valueOf(op1));
                            } else if (op2 instanceof Number)
                                return Double.parseDouble(String.valueOf(op1)) / Double.parseDouble(String.valueOf(op2));
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else{
                            //ERR
                            return null;
                        }
                        
                    case potencia:
                        if (op1 instanceof Boolean){
                            
                            if (op2 instanceof Number){                                
                                return (((boolean)op1) ? 1 : 0);     
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else if (op1 instanceof Number){
                            
                            if (op2 instanceof Boolean){
                                if (!(boolean)op2){
                                    //ERR
                                    return 1;
                                }
                                return Double.parseDouble(String.valueOf(op1));
                            } else if (op2 instanceof Number)
                                return Math.pow(Double.parseDouble(String.valueOf(op1)), Double.parseDouble(String.valueOf(op2)));
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else{
                            //ERR
                            return null;
                        }
                        
                    case modulo:
                        if (op1 instanceof Boolean){
                            
                            if (op2 instanceof Boolean)
                                if (!(boolean)op2){
                                    //ERR
                                    return null;
                                }else
                                    return (((boolean)op1) ? 1 : 0) % (((boolean)op2) ? 1 : 0);
                            else if (op2 instanceof Number){
                                double val2 = Double.parseDouble(String.valueOf(op2));
                                //System.out.println(String.format("%s --- %s ----- %s",(((boolean)op1) ? 1 : 0), val2, (((boolean)op1) ? 1 : 0) % val2));
                                if (val2 == 0){
                                    //ERR
                                    return null;
                                }else
                                    return (((boolean)op1) ? 1 : 0) % val2;
                            }else{
                                //ERR
                                return null;
                            }
                        }
                        else if (op1 instanceof Number){
                            
                            if (op2 instanceof Boolean)
                                if (!(boolean)op2){
                                    //ERR
                                    return null;
                                }else{
                                    double val1 = Double.parseDouble(String.valueOf(op1));
                                    return val1 % (((boolean)op2) ? 1 : 0);
                                }
                            else if (op2 instanceof Number)
                                return Double.parseDouble(String.valueOf(op1)) % Double.parseDouble(String.valueOf(op2));
                            else{
                                //ERR
                                return null;
                            }
                        }
                        else{
                            //ERR
                            return null;
                        }
                        
                    //***********************fin aritmeticas *********************
                
                    //*********************** relacionales  *********************
                    case igual:
                        if (op1 instanceof Boolean){
                            if (op2 instanceof Boolean){
                                return (boolean)op1 == (boolean)op2;
                            }else if (op2 instanceof Number){
                                return (((boolean)op1) ? 1 : 0) == Double.parseDouble(String.valueOf(op2));
                            }else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof Number){
                            if (op2 instanceof Boolean){
                                return Double.parseDouble(String.valueOf(op1)) == (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return Double.parseDouble(String.valueOf(op1)) == Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return Double.parseDouble(String.valueOf(op1)) == ((String)op2).length();
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof String){
                            if (op2 instanceof Number){
                                return ((String)op1).length() == Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return (String.valueOf(op1)).equals((String)op2);
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (String.valueOf(op1)).equals(op2.toString());
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof LocalDate || op1 instanceof LocalDateTime){
                            if (op2 instanceof String){
                                return (op1.toString()).equals((String)op2);
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (op1.toString()).equals(op2.toString());
                            }
                        }else{
                            //ERR
                            return null;
                        }
                    
                    case diferente:
                        if (op1 instanceof Boolean){
                            if (op2 instanceof Boolean){
                                return (boolean)op1 != (boolean)op2;
                            }else if (op2 instanceof Number){
                                return (((boolean)op1) ? 1 : 0) != Double.parseDouble(String.valueOf(op2));
                            }else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof Number){
                            if (op2 instanceof Boolean){
                                return Double.parseDouble(String.valueOf(op1)) != (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return Double.parseDouble(String.valueOf(op1)) != Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return Double.parseDouble(String.valueOf(op1)) != ((String)op2).length();
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof String){
                            if (op2 instanceof Number){
                                return ((String)op1).length() != Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return !(String.valueOf(op1)).equals((String)op2);
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return !(String.valueOf(op1)).equals(op2.toString());
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof LocalDate || op1 instanceof LocalDateTime){
                            if (op2 instanceof String){
                                return !(op1.toString()).equals((String)op2);
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return !(op1.toString()).equals(op2.toString());
                            }
                        }else{
                            //ERR
                            return null;
                        }
                        
                    case mayor:
                        if (op1 instanceof Boolean){
                            if (op2 instanceof Boolean){
                                return (((boolean)op1) ? 1 : 0) > (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return (((boolean)op1) ? 1 : 0) > Double.parseDouble(String.valueOf(op2));
                            }else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof Number){
                            if (op2 instanceof Boolean){
                                return Double.parseDouble(String.valueOf(op1)) > (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return Double.parseDouble(String.valueOf(op1)) > Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return Double.parseDouble(String.valueOf(op1)) > ((String)op2).length();
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof String){
                            if (op2 instanceof Number){
                                return ((String)op1).length() > Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return (String.valueOf(op1)).length() > (String.valueOf(op2)).length();
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (String.valueOf(op1)).length() > (op2.toString().length());
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof LocalDate || op1 instanceof LocalDateTime){
                            if (op2 instanceof String){
                                return (op1.toString().length()) > ((String)op2).length();
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (op1.toString()).length() > (op2.toString()).length();
                            }
                        }else{
                            //ERR
                            return null;
                        }
                    
                    case menor:
                        if (op1 instanceof Boolean){
                            if (op2 instanceof Boolean){
                                return (((boolean)op1) ? 1 : 0) < (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return (((boolean)op1) ? 1 : 0) < Double.parseDouble(String.valueOf(op2));
                            }else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof Number){
                            if (op2 instanceof Boolean){
                                return Double.parseDouble(String.valueOf(op1)) < (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return Double.parseDouble(String.valueOf(op1)) < Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return Double.parseDouble(String.valueOf(op1)) < ((String)op2).length();
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof String){
                            if (op2 instanceof Number){
                                return ((String)op1).length() < Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return (String.valueOf(op1)).length() < (String.valueOf(op2)).length();
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (String.valueOf(op1)).length() < (op2.toString().length());
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof LocalDate || op1 instanceof LocalDateTime){
                            if (op2 instanceof String){
                                return (op1.toString().length()) < ((String)op2).length();
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (op1.toString()).length() < (op2.toString()).length();
                            }
                        }else{
                            //ERR
                            return null;
                        }
                    case mayorI:
                        if (op1 instanceof Boolean){
                            if (op2 instanceof Boolean){
                                return (((boolean)op1) ? 1 : 0) >= (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return (((boolean)op1) ? 1 : 0) >= Double.parseDouble(String.valueOf(op2));
                            }else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof Number){
                            if (op2 instanceof Boolean){
                                return Double.parseDouble(String.valueOf(op1)) >= (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return Double.parseDouble(String.valueOf(op1)) >= Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return Double.parseDouble(String.valueOf(op1)) >= ((String)op2).length();
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof String){
                            if (op2 instanceof Number){
                                return ((String)op1).length() >= Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return (String.valueOf(op1)).length() >= (String.valueOf(op2)).length();
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (String.valueOf(op1)).length() >= (op2.toString().length());
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof LocalDate || op1 instanceof LocalDateTime){
                            if (op2 instanceof String){
                                return (op1.toString().length()) >= ((String)op2).length();
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (op1.toString()).length() >= (op2.toString()).length();
                            }
                        }else{
                            //ERR
                            return null;
                        }
                    
                    case menorI:
                        if (op1 instanceof Boolean){
                            if (op2 instanceof Boolean){
                                return (((boolean)op1) ? 1 : 0) <= (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return (((boolean)op1) ? 1 : 0) <= Double.parseDouble(String.valueOf(op2));
                            }else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof Number){
                            if (op2 instanceof Boolean){
                                return Double.parseDouble(String.valueOf(op1)) <= (((boolean)op2) ? 1 : 0);
                            }else if (op2 instanceof Number){
                                return Double.parseDouble(String.valueOf(op1)) <= Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return Double.parseDouble(String.valueOf(op1)) <= ((String)op2).length();
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof String){
                            if (op2 instanceof Number){
                                return ((String)op1).length() <= Double.parseDouble(String.valueOf(op2));
                            }else if (op2 instanceof String){
                                return (String.valueOf(op1)).length() <= (String.valueOf(op2)).length();
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (String.valueOf(op1)).length() <= (op2.toString().length());
                            }
                            else{
                                //ERR
                                return null;
                            }
                        }else if (op1 instanceof LocalDate || op1 instanceof LocalDateTime){
                            if (op2 instanceof String){
                                return (op1.toString().length()) <= ((String)op2).length();
                            }else if (op2 instanceof LocalDate || op2 instanceof LocalDateTime){
                                return (op1.toString()).length() <= (op2.toString()).length();
                            }
                        }else{
                            //ERR
                            return null;
                        }                        
                        
                    //********************** fin relacionales *****************************
                        
                    //*********************** lógicas **********************************
                    case and:
                    case or:
                        if (op1 instanceof Boolean && op2 instanceof Boolean){
                            if (n.tipo == TipoNodo.and)
                                return (boolean)op1 && (boolean)op2;
                            else if (n.tipo == TipoNodo.or)
                                return (boolean)op1 || (boolean)op2;
                        }else{
                            //ERR
                            return null;
                        }
                            
                    //********************* fin lógicas *******************************
                }
                
                
                
        }
        return null;
    }
}
