package compilacion;
import java_cup.runtime.Symbol;
import java.util.ArrayList;
%%

%cupsym TokensCCSS
%class ScannerCCSS
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

    public ScannerCCSS(java.io.InputStream is, String archivoFuente, ArrayList <ErrorCode> errores){
        this(new java.io.InputStreamReader(is));      
        this.sourceFile = archivoFuente;
        this.errores = errores;
    }    

    public ArrayList <ErrorCode> getErrores(){
        return errores;
    }

    StringBuffer string = new StringBuffer();

    private Symbol simbolo(int type) {
        System.out.println(TokensCCSS.terminalNames[type] + " - " + (yyline+1)+ " - " + (yycolumn+1));
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol simbolo(int type, Object value) {
        System.out.println(TokensCCSS.terminalNames[type] + " - " + (yyline+1)+ " - " + (yycolumn+1) + " - " + value);
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol errorLexico(String cadena, String mensaje){
        System.out.println("Error léxico - " + (yyline+1) + " - " + (yycolumn+1) + " - " + cadena);
        errores.add(new ErrorCode(TipoError.lexico, yyline + 1, yycolumn +1, cadena, mensaje, this.sourceFile));
        //System.err.println(mensaje + ": \""+ cadena + "\" (" + (yyline + 1) + "," + (yycolumn +1) + ")");
        return new Symbol(TokensCCSS.errorLex, yyline, yycolumn, cadena);
    }
%}

finLinea = \r|\n|\r\n
blank = [ \t\f]|{finLinea}

entero = [0-9]+
doble = {entero} "." {entero}
letra = [a-zA-Z]
id = {letra} ({letra} | "_" | {entero})*

%state STRING, STRING_APOSTROFO, COMENTARIO, COMENTARIO_MULTILINEA

%%

<YYINITIAL> {
    /*inicio de un comentario multilinea */
    "//"          { yybegin(COMENTARIO); }    
    "/*"          { yybegin(COMENTARIO_MULTILINEA); }    

    /*inicio de un string, borra el buffer para poder crear la cadena nueva*/
    \"             { string.setLength(0); yybegin(STRING); }    
    
    /*inicio de un nombre de fuente, borra el buffer para poder crear la cadena nueva*/
    "'"          { string.setLength(0); yybegin(STRING_APOSTROFO); }    

    /* PALABRAS RESERVADAS */    
    ":="          { return simbolo(TokensCCSS.asignacion, yytext());}
    ";"          { return simbolo(TokensCCSS.ptoComa, yytext());}
    ","          { return simbolo(TokensCCSS.coma, yytext());}
        
    "*"             { return simbolo(TokensCCSS.por, yytext());}
    "+"             { return simbolo(TokensCCSS.mas, yytext());}
    "-"             { return simbolo(TokensCCSS.menos, yytext());}
    "/"             { return simbolo(TokensCCSS.entre, yytext());}  
    "["             { return simbolo(TokensCCSS.corcheteA, yytext());}
    "]"             { return simbolo(TokensCCSS.corcheteC, yytext());}
    "("             { return simbolo(TokensCCSS.parenA, yytext());}
    ")"             { return simbolo(TokensCCSS.parenC, yytext());}
    
    "alineado"             { return simbolo(TokensCCSS.alineado, yytext());}    
    "izquierda"             { return simbolo(TokensCCSS.izquierda, yytext());}    
    "derecha"             { return simbolo(TokensCCSS.derecha, yytext());}    
    "centrado"             { return simbolo(TokensCCSS.centrado, yytext());}    
    "justificado"             { return simbolo(TokensCCSS.justificado, yytext());}    

    "texto"             { return simbolo(TokensCCSS.alineado, yytext());}    
    
    "formato"             { return simbolo(TokensCCSS.formato, yytext());}    
    "negrilla"             { return simbolo(TokensCCSS.negrilla, yytext());}    
    "cursiva"             { return simbolo(TokensCCSS.cursiva, yytext());}    
    "mayuscula"             { return simbolo(TokensCCSS.mayuscula, yytext());}    
    "minuscula"             { return simbolo(TokensCCSS.minuscula, yytext());}    
    "capital-t"             { return simbolo(TokensCCSS.capital, yytext());}    

    "letra"             { return simbolo(TokensCCSS.fuente, yytext());}    
    "tamtex"             { return simbolo(TokensCCSS.tamTex, yytext());}    
    "fondoElemento"             { return simbolo(TokensCCSS.fondoElemento, yytext());}    
    
    "autoredimension"             { return simbolo(TokensCCSS.autoredimension, yytext());}    
    "horizontal"             { return simbolo(TokensCCSS.horizontal, yytext());}    
    "vertical"             { return simbolo(TokensCCSS.vertical, yytext());}    
    "area"             { return simbolo(TokensCCSS.area, yytext());}    
    
    "visible"             { return simbolo(TokensCCSS.visible   , yytext());}    
    "borde"             { return simbolo(TokensCCSS.borde, yytext());}    
    "opaque"          { return simbolo(TokensCCSS.opaque, yytext());}
    "colortext"          { return simbolo(TokensCCSS.colorTex, yytext());}
    
    "grupo"          { return simbolo(TokensCCSS.grupo, yytext());}
    "id"          { return simbolo(TokensCCSS.id, yytext());}

    /* valore literales */
    "true"           { return simbolo(TokensCCSS.booleanoLiteral, true);}
    "false"           { return simbolo(TokensCCSS.booleanoLiteral, false);}
    {id}           { return simbolo(TokensCCSS.identificador, yytext());}
    {entero}       { return simbolo(TokensCCSS.enteroLiteral, Integer.parseInt( yytext() ) );}
    {doble}        { return simbolo(TokensCCSS.dobleLiteral, Double.parseDouble( yytext() ) );}
       
    {blank}+        { /* ignorar */ }
    .              {return errorLexico(yytext(), "Caracter no reconocido");}   
    <<EOF>>         {
                        return simbolo(TokensCHTML.EOF);
                    }
}

<STRING> {
    \"  { yybegin(YYINITIAL); 
            /*System.out.println("Cadena: " + string.toString());*/
            return simbolo(TokensCCSS.cadenaLiteral, string.toString()); }

    [^"\08""\09""\10""\34""\39""\92"\"\b\n\r\t\\]+    { string.append( yytext() ); }
    \\b             { string.append('\b'); }
    \\t             { string.append('\t'); }
    \\n             { string.append('\n'); }
    \\r             { string.append('\r'); }
    \\\"            { string.append('\"'); }
    \\\\            { string.append('\\'); }
    \\'             { string.append('\''); }
    \\08            { string.append('\b'); }
    \\09            { string.append('\t'); }
    \\10            { string.append('\n'); }
    \\34            { string.append('\"'); }    
    \\39            { string.append('\''); }
    \\92            { string.append('\\'); }
    
    \\.             { /*throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); */  
                        yybegin(YYINITIAL);
                        return errorLexico(yytext(), "Secuencia de escape no valida");
                    }
    {finLinea}      { /*throw new RuntimeException("Cadena de caracteres no terminada en la linea"); */ 
                        yybegin(YYINITIAL);
                        return errorLexico(" ", "Cadena no terminada antes del fin de línea");
                    }
    <<EOF>>         {
                        yybegin(YYINITIAL);
                        return errorLexico(" ", "Cadena no terminada antes del fin de línea");
                    }
}

<STRING_APOSTROFO> {
    \'  { yybegin(YYINITIAL); 
            /*System.out.println("Cadena: " + string.toString());*/
            return simbolo(TokensCCSS.cadenaLiteral2, string.toString()); }

    [^"\08""\09""\10""\34""\39""\92"\"\b\n\r\t\\]+    { string.append( yytext() ); }
    \\b             { string.append('\b'); }
    \\t             { string.append('\t'); }
    \\n             { string.append('\n'); }
    \\r             { string.append('\r'); }
    \\\"            { string.append('\"'); }
    \\\\            { string.append('\\'); }
    \\'             { string.append('\''); }
    \\08            { string.append('\b'); }
    \\09            { string.append('\t'); }
    \\10            { string.append('\n'); }
    \\34            { string.append('\"'); }    
    \\39            { string.append('\''); }
    \\92            { string.append('\\'); }
    
    \\.             { /*throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); */  
                        yybegin(YYINITIAL);
                        return errorLexico(yytext(), "Secuencia de escape no valida");
                    }
    {finLinea}      { /*throw new RuntimeException("Cadena de caracteres no terminada en la linea"); */ 
                        yybegin(YYINITIAL);
                        return errorLexico(" ", "Cadena no terminada antes del fin de línea");
                    }
    <<EOF>>         {
                        yybegin(YYINITIAL);
                        return errorLexico(" ", "Cadena no terminada antes del fin de línea");
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
