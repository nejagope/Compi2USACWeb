package compilacion;
import java_cup.runtime.Symbol;
import java.util.ArrayList;
%%

%cupsym TokensCJS
%class ScannerCJS
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

    public ScannerCJS(java.io.InputStream is, String archivoFuente, ArrayList <ErrorCode> errores){
        this(new java.io.InputStreamReader(is));      
        this.sourceFile = archivoFuente;
        this.errores = errores;
    }    

    public ArrayList <ErrorCode> getErrores(){
        return errores;
    }

    StringBuffer string = new StringBuffer();

    private Symbol simbolo(int type) {
        System.out.println(TokensCJS.terminalNames[type] + " - " + (yyline+1)+ " - " + (yycolumn+1));
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol simbolo(int type, Object value) {
        System.out.println(TokensCJS.terminalNames[type] + " - " + (yyline+1)+ " - " + (yycolumn+1) + " - " + value);
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol errorLexico(String cadena, String mensaje){
        System.out.println("Error léxico - " + (yyline+1) + " - " + (yycolumn+1) + " - " + cadena);
        errores.add(new ErrorCode(TipoError.lexico, yyline + 1, yycolumn +1, cadena, "Caracter no reconocido", this.sourceFile));
        //System.err.println(mensaje + ": \""+ cadena + "\" (" + (yyline + 1) + "," + (yycolumn +1) + ")");
        return new Symbol(TokensCJS.errorLex, yyline, yycolumn, cadena);
    }
%}
finLinea = \r|\n|\r\n
blank = [ \t\f]|{finLinea}

//date = ( ("0"[1-9]) | ([1-2][0-9]) |([3][0-1])) "/" ( ("0"[1-9]) | ([1-2][0-2]) ) "/" 
entero = [0-9]+
doble = {entero} "." {entero}
letra = [a-zA-Z]
id = {letra} ({letra} | "_" | {entero})*


%state STRING, CHAR, COMENTARIO_MULTI, COMENTARIO_LINEA

%%
<YYINITIAL> {
    /* booleanos */
    \'true\'             { return simbolo(TokensCJS.booleanoLiteral, true);}
    \'false\'             { return simbolo(TokensCJS.booleanoLiteral, false);}
    \'{entero}"/"{entero}"/"{entero}\'             { return simbolo(TokensCJS.dateLiteral, yytext().substring(1, yytext().length() - 1));}
    \'{entero}"/"{entero}"/"{entero} " " {entero}":"{entero}":"{entero}\'             { return simbolo(TokensCJS.dateTimeLiteral, yytext().substring(1, yytext().length() - 1));}
    /*inicio de un comentario multilinea */
    "'/" { yybegin(COMENTARIO_MULTI); }

    /*inicio de comentario de línea */
    "'" { yybegin(COMENTARIO_LINEA); }

    /*inicio de un string, borra el buffer para poder crear la cadena nueva*/
    \"             { string.setLength(0); yybegin(STRING); }    

    /* PALABRAS RESERVADAS */
    "dimv"          { return simbolo(TokensCJS.dimV, yytext());}
    ":"          { return simbolo(TokensCJS.dosPtos, yytext());}
    ";"          { return simbolo(TokensCJS.ptoComa, yytext());}
    
    "++"             { return simbolo(TokensCJS.inc, yytext());}
    "--"             { return simbolo(TokensCJS.dec, yytext());}
    "*"             { return simbolo(TokensCJS.por, yytext());}
    "+"             { return simbolo(TokensCJS.mas, yytext());}
    "-"             { return simbolo(TokensCJS.menos, yytext());}
    "/"             { return simbolo(TokensCJS.entre, yytext());}
    "*"             { return simbolo(TokensCJS.por, yytext());}
    "^"             { return simbolo(TokensCJS.potencia, yytext());}
    "%"             { return simbolo(TokensCJS.modulo, yytext());}
    
    ">="             { return simbolo(TokensCJS.mayorI, yytext());}
    "<="             { return simbolo(TokensCJS.menorI, yytext());}
    ">"              { return simbolo(TokensCJS.mayor, yytext());}
    "<"              { return simbolo(TokensCJS.menor, yytext());}    
    ">"              { return simbolo(TokensCJS.mayor, yytext());}
    "=="             { return simbolo(TokensCJS.igual, yytext());}
    "!="             { return simbolo(TokensCJS.diferente, yytext());}
    "&&"             { return simbolo(TokensCJS.and, yytext());}
    "||"             { return simbolo(TokensCJS.or, yytext());}
    "!"             { return simbolo(TokensCJS.not, yytext());}

    "("             { return simbolo(TokensCJS.parenA, yytext());}
    ")"             { return simbolo(TokensCJS.parenC, yytext());}
    "{"             { return simbolo(TokensCJS.llaveA, yytext());}
    "}"             { return simbolo(TokensCJS.llaveC, yytext());}
    ","          { return simbolo(TokensCJS.coma, yytext());}

    //"conteo"          { return simbolo(TokensCJS.conteo, yytext());}

    "si"            { return simbolo(TokensCJS.si, yytext());}      
    "sino"          { return simbolo(TokensCJS.sino, yytext());}
    "selecciona"          { return simbolo(TokensCJS.select, yytext());}  
    "caso"          { return simbolo(TokensCJS.caso, yytext());}  
    "defecto"          { return simbolo(TokensCJS.defecto, yytext());}  
    
    "para"          { return simbolo(TokensCJS.para, yytext());}    
    "mientras"          { return simbolo(TokensCJS.mientras, yytext());}
    "detener"          { return simbolo(TokensCJS.detener, yytext());}  

    "funcion"          { return simbolo(TokensCJS.funcion, yytext());}
    "retornar"          { return simbolo(TokensCJS.retornar, yytext());}
    
    "."          { return simbolo(TokensCJS.punto, yytext());}
    
    {id}           { return simbolo(TokensCJS.id, yytext());}
    {entero}       { return simbolo(TokensCJS.enteroLiteral, Integer.parseInt( yytext() ) );}
    {doble}        { return simbolo(TokensCJS.dobleLiteral, Double.parseDouble( yytext() ) );}
       
    {blank}+        { /* ignorar */ }
    .              {return errorLexico(yytext(), "Caracter no reconocido");}
}
<STRING> {
    \"  { yybegin(YYINITIAL); 
            /*System.out.println("Cadena: " + string.toString());*/
            return simbolo(TokensCJS.cadenaLiteral, string.toString()); }

    [^\"]+    { string.append( yytext() ); }

    {finLinea}      { /*throw new RuntimeException("Cadena de caracteres no terminada en la linea"); */ 
                        yybegin(YYINITIAL);
                        return errorLexico(" ", "Cadena no terminada antes del fin de línea");
                    }
    <<EOF>>         {
                        yybegin(YYINITIAL);
                        return errorLexico(" ", "Cadena no terminada antes del fin de línea");
                    }
}

<COMENTARIO_MULTI> {
    "/'"  { yybegin(YYINITIAL);}      
    <<EOF>> { yybegin(YYINITIAL);}    
    [^°]  {}
}

<COMENTARIO_LINEA> {
    {finLinea}  { yybegin(YYINITIAL);}    
    <<EOF>> { yybegin(YYINITIAL);}    
    [^°]  {}
}
