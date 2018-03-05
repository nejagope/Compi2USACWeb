/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi2usacweb;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;

/**
 *
 * @author NELSONJAIR
 */
public class Estilo {
    //css
    public String fondo;    
    public int ancho;
    public int alto;
    public String alineado; 
    
    public String texto;
    public String formato;
    public String fuente;
    public String tamTex;
    public boolean visible;
    public String borde;
    public String opaque;
    public String colorTex;
    public String autoRedimension;
    
    /**
     * Mezcla este estilo con el complementario para resolver conflictos se da prioridad a este estilo
     * @param complementario
     * @return Estilo
     */
    public Estilo mezclar(Estilo complementario){
        Estilo e = new Estilo();
        e.alineado = this.alineado != null ? this.alineado : complementario.alineado;
        e.fondo = this.fondo != null ? this.fondo : complementario.fondo;
        
        e.texto = this.texto != null ? this.texto : complementario.texto;
        e.formato = this.formato != null ? this.formato : complementario.formato;
        e.fuente = this.fuente != null ? this.fuente : complementario.fuente;
        e.tamTex = this.tamTex != null ? this.tamTex : complementario.tamTex;
        //e.visible = this.visible != null ? this.visible : complementario.visible;
        e.borde = this.borde != null ? this.borde : complementario.borde;
        e.opaque = this.opaque != null ? this.opaque : complementario.opaque;
        e.colorTex = this.colorTex != null ? this.colorTex : complementario.colorTex;
        e.autoRedimension = this.autoRedimension != null ? this.autoRedimension : complementario.autoRedimension;
        return e;
    }
    
    public Color getColorFondo(){
        return getColor(fondo);
    }
    
    public Color getColorTexto(){
        return getColor(colorTex);
    }
    
    public Color getColor(String colorStr){  
        try{
            if (colorStr.startsWith("#")){
                int colorInt = Integer.parseInt(colorStr.substring(1, colorStr.length()),  16);
                return new Color(colorInt);
            }else{
                Field field = Color.class.getField(colorStr);
                return (Color)field.get(null);
            }
        }catch(Exception ex){}
        return null;
    }
    
    public Font getFuente(){
        String nombre = fuente == null ? "Arial": fuente;
        int tamFuente = 12;
        int estilo = Font.PLAIN;
        if (formato != null){
            if (formato.contains("negrilla"))
                estilo = Font.BOLD;
            else if (formato.contains("cursiva")){
                estilo = Font.ITALIC;
            }
        }
        try{
            tamFuente = tamTex == null ? 12 : Math.round(Float.parseFloat(tamTex));
        }catch(Exception ex){}
        return getFuente(nombre, estilo, tamFuente);
    }
    
    public Font getFuente(String nombre, int estilo, int tamaño){
        try{
            /*
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font[] fuentes = ge.getAllFonts();
            for(Font _fuente : fuentes){
                System.out.println(_fuente);
                boolean coincideEstilo = _fuente.isBold() && estilo.equals("negrilla")
                        || _fuente.isItalic() && estilo.equals("cursiva")
                        || _fuente.isPlain() && !estilo.equals("negrilla") && !estilo.equals("cursiva");
                if (_fuente.getName().equals(nombre) && _fuente.getSize() == 1 && coincideEstilo)
                    return _fuente;
            }
            */                        
            return  new Font(nombre, estilo, tamaño);
            
        }catch(Exception ex){
            return  new Font("Tahoma", Font.PLAIN, 12);
        }
    }
    
}
