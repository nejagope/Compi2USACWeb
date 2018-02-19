/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

/**
 *
 * @author Nelson Jair
 */
public class ErrorCode {
    TipoError tipo;
    String archivo;
    int linea, columna;
    String cadena;
    String mensaje;

    public ErrorCode(TipoError tipo, int linea, int columna, String cadena, String mensaje, String archivo) {
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
        this.cadena = cadena;
        this.mensaje = mensaje;
        this.archivo = archivo;
    }
    
    public ErrorCode(TipoError tipo, int linea, int columna, String cadena, String mensaje) {
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
        this.cadena = cadena;
        this.mensaje = mensaje;
    }

    public ErrorCode(TipoError tipo, int linea, int columna, String mensaje) {
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
        this.mensaje = mensaje;
        cadena = "";
    }

    public ErrorCode(TipoError tipo, String cadena, String mensaje) {
        this.tipo = tipo;
        this.cadena = cadena;
        this.mensaje = mensaje;
        cadena = "";
        mensaje = "";
    }
    
    
}
