/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

/**
 *
 * @author Nelson Jair
 */
public enum TipoNodo {
    chtml, encabezado, cjs, ccss, cuerpo, titulo, panel, texto, imagen, boton, enlace, tabla,
    fila, celdaEnc, celda, areaTexto, cajaTexto, cajaOpciones, opcion, spinner, salto,
    chtmlF, encabezadoF, cjsF, ccssF, cuerpoF, tituloF, panelF, textoF, imagenF, botonF, enlaceF, tablaF,
    filaF, celdaEncF, celdaF, areaTextoF, cajaTextoF, cajaOpcionesF, opcionF, spinnerF,
    ruta, fondo, click, id, grupo, alto, ancho, alineado, valor,
    elemento, cadenaValor,
    elementoDesconocido,
    atrib, atribs,
    textoPlano,
    errorSintactico,
    
    //cjs
    programa, divV, salir, defecto, sentencia, sentencias, funcion, parametros, llamadaFuncion, identificador,
    
    enteroLit, dobleLit, booleanoLit, caracterLit, cadenaLit,
    not, or, and,
    igual, diferente, mayor, mayorI, menorI, menor,
    inc, dec, negativo, potencia, entre, por, mas, menos,
    asignacion,
}
