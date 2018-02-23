package compilacion;
import java_cup.runtime.Symbol;
import java.util.ArrayList;
%%

%cupsym TokensCHTML
%class ScannerCHTML
%cup
%public
%unicode
%line
%column
%char
%ignorecase

%{
    ArrayList <ErrorCode> errores;
    String sourceFile;

    public ScannerCHTML(java.io.InputStream is, String archivoFuente, ArrayList <ErrorCode> errores){
        this(new java.io.InputStreamReader(is));      
        this.sourceFile = archivoFuente;
        this.errores = errores;
    }    

    public ArrayList <ErrorCode> getErrores(){
        return errores;
    }

    StringBuffer string = new StringBuffer();

    private Symbol simbolo(int type) {
        System.out.println(TokensCHTML.terminalNames[type] + " - " + (yyline+1)+ " - " + (yycolumn+1));
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol simbolo(int type, Object value) {
        System.out.println(TokensCHTML.terminalNames[type] + " - " + (yyline+1)+ " - " + (yycolumn+1) + " - " + value);
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol errorLexico(String cadena, String mensaje){
        System.out.println("Error léxico - " + (yyline+1) + " - " + (yycolumn+1) + " - " + cadena);
        errores.add(new ErrorCode(TipoError.lexico, yyline + 1, yycolumn +1, cadena, mensaje, this.sourceFile));
        //System.err.println(mensaje + ": \""+ cadena + "\" (" + (yyline + 1) + "," + (yycolumn +1) + ")");
        return new Symbol(TokensCHTML.errorLex, yyline, yycolumn, cadena);
    }
%}

finLinea = \r|\n|\r\n
blank = [ \t\f]

%state STRING, COMENTARIO, COMENTARIO_MULTILINEA, ETIQUETA, VALOR_ELEMENTO

%%

<YYINITIAL> {
    /*inicio de un comentario multilinea */
    "//"          { yybegin(COMENTARIO); }    
    "/*"          { yybegin(COMENTARIO_MULTILINEA); }    

    "<" {blank}* "fin-salto" {blank}* ">"       {                         
                        return simbolo(TokensCHTML.salto, "fin-salto");
                    }

    /*inicio de una etiqueta CSHTML */
    "<"             { 
                        yybegin(ETIQUETA); 
                        return simbolo(TokensCHTML.LT, yytext());
                    }
    
    {blank}+         {}

    [^<]({blank}|[^<])*          {return simbolo(TokensCHTML.textoPlano, yytext().trim().replaceAll("\t", "").replaceAll("  ",""));}
    
    .              {return errorLexico(yytext(), "Caracter no reconocido");}    

    <<EOF>>         {
                        return simbolo(TokensCHTML.EOF);
                    }
}

<ETIQUETA> {
    /* SALIDA DEL ESTADO ETIQUETA */
    ">" { 
            yybegin(YYINITIAL);                         
            return simbolo(TokensCHTML.GT, yytext());
        }

    /* PALABRAS RESERVADAS DENTRO DE ETIQUETAS*/
    /* ETIQUETAS */
    "chtml"                 { return simbolo(TokensCHTML.chtml, yytext());}
    "fin-chtml"             { return simbolo(TokensCHTML.chtmlF, yytext());}
    "encabezado"                 { return simbolo(TokensCHTML.encabezado, yytext());}
    "fin-encabezado"                 { return simbolo(TokensCHTML.encabezadoF, yytext());}    
    "cjs"                 { return simbolo(TokensCHTML.cjs, yytext());}
    "fin-cjs"                 { return simbolo(TokensCHTML.cjsF, yytext());}
    "cuerpo"                 { return simbolo(TokensCHTML.cuerpo, yytext());}
    "fin-cuerpo"                 { return simbolo(TokensCHTML.cuerpoF, yytext());}
    "ccss"                 { return simbolo(TokensCHTML.ccss, yytext());}
    "fin-ccss"                 { return simbolo(TokensCHTML.ccssF, yytext());}
    "titulo"                 { return simbolo(TokensCHTML.titulo, yytext());}
    "fin-titulo"                 { return simbolo(TokensCHTML.tituloF, yytext());}
    "panel"                 { return simbolo(TokensCHTML.panel, yytext());}
    "fin-panel"                 { return simbolo(TokensCHTML.panelF, yytext());}
    "texto"                 { return simbolo(TokensCHTML.texto, yytext());}
    "fin-texto"                 { return simbolo(TokensCHTML.textoF, yytext());}
    "imagen"                 { return simbolo(TokensCHTML.imagen, yytext());}
    "fin-imagen"                 { return simbolo(TokensCHTML.imagenF, yytext());}
    "boton"                 { return simbolo(TokensCHTML.boton, yytext());}
    "fin-boton"                 { return simbolo(TokensCHTML.botonF, yytext());}
    "enlace"                 { return simbolo(TokensCHTML.enlace, yytext());}
    "fin-enlace"                 { return simbolo(TokensCHTML.enlaceF, yytext());}
    "tabla"                 { return simbolo(TokensCHTML.tabla, yytext());}
    "fin-tabla"                 { return simbolo(TokensCHTML.tablaF, yytext());}
    "fil_t"                 { return simbolo(TokensCHTML.fila, yytext());}
    "fin-fil_t"                 { return simbolo(TokensCHTML.filaF, yytext());}
    "cb"                 { return simbolo(TokensCHTML.celdaEnc, yytext());}
    "fin-cb"                 { return simbolo(TokensCHTML.celdaEncF, yytext());}
    "ct"                 { return simbolo(TokensCHTML.celda, yytext());}
    "fin-ct"                 { return simbolo(TokensCHTML.celdaF, yytext());}
    "texto_a"                 { return simbolo(TokensCHTML.areaTexto, yytext());}
    "fin-texto_a"                 { return simbolo(TokensCHTML.areaTextoF, yytext());}
    "caja_texto"                 { return simbolo(TokensCHTML.cajaTexto, yytext());}
    "fin-caja_texto"                 { return simbolo(TokensCHTML.cajaTextoF, yytext());}
    "caja"                 { return simbolo(TokensCHTML.cajaOpciones, yytext());}
    "fin-caja"                 { return simbolo(TokensCHTML.cajaOpcionesF, yytext());}
    "opcion"                 { return simbolo(TokensCHTML.opcion, yytext());}
    "fin-opcion"                 { return simbolo(TokensCHTML.opcionF, yytext());}
    "spinner"                 { return simbolo(TokensCHTML.spinner, yytext());}
    "fin-spinner"                 { return simbolo(TokensCHTML.spinnerF, yytext());}    
    //"fin-salto"                 { return simbolo(TokensCHTML.salto, yytext());}
    /* ELEMENTOS */
    
    "ruta"                 { return simbolo(TokensCHTML.ruta, yytext());}
    "fondo"                 { return simbolo(TokensCHTML.fondo, yytext());}
    "click"                 { return simbolo(TokensCHTML.click, yytext());}
    "id"                 { return simbolo(TokensCHTML.id, yytext());}
    "grupo"                 { return simbolo(TokensCHTML.grupo, yytext());}
    "alto"                 { return simbolo(TokensCHTML.alto, yytext());}
    "ancho"                 { return simbolo(TokensCHTML.ancho, yytext());}
    "alineado"                 { return simbolo(TokensCHTML.alineado, yytext());}    
    "valor"                 { return simbolo(TokensCHTML.valor, yytext());} 
    
    [a-zA-Z0-9][0-9a-zA-Z_-]*          {return simbolo(TokensCHTML.elementoDesconocido, yytext());}
    
    /* VALORES DE ELEMENTOS */
    /*inicio de un string, borra el buffer para poder crear la cadena nueva*/
    
    \"             { string.setLength(0); yybegin(VALOR_ELEMENTO); }

    ";"                 { return simbolo(TokensCHTML.ptoComa, yytext());}
    "="                 { return simbolo(TokensCHTML.EQ, yytext());}

    {blank}+         {}

    /*inicio de una etiqueta CSHTML */
    "<"             { 
                        yybegin(ETIQUETA); 
                        return simbolo(TokensCHTML.LT, yytext());
                    }
    
    
    .              {return errorLexico(yytext(), "Caracter no reconocido");}
    <<EOF>>         {
                        return simbolo(TokensCHTML.EOF);                      
                    }

}

<VALOR_ELEMENTO> {
    \"  { 
            yybegin(ETIQUETA);             
            return simbolo(TokensCHTML.cadenaValor, string.toString()); 
        }
        
    [^\"]+    { string.append( yytext() ); }

    <<EOF>>         {
                        yybegin(YYINITIAL);
                        return errorLexico(string.toString(), "Cadena no terminada, faltan comillas de cierre");
                    }
}

<COMENTARIO> {
    {finLinea}  { yybegin(YYINITIAL);}             
    [^°]  {}
    <<EOF>>         {
                        yybegin(YYINITIAL);
                        return simbolo(TokensCHTML.EOF);
                    }
}

<COMENTARIO_MULTILINEA> {
    "*/"  { yybegin(YYINITIAL);}             
    [^°]  {}
    <<EOF>>         {
                        yybegin(YYINITIAL);
                        return simbolo(TokensCHTML.EOF);
                    }
}
