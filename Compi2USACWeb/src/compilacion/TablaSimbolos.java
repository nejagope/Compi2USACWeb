/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import java.util.ArrayList;

/**
 *
 * @author Nelson Jair
 */
public class TablaSimbolos extends ArrayList <Simbolo>{
    
    public TablaSimbolos(){
        super();
    }
    
    public Simbolo getVariable(String id, TipoSimbolo tipo, String ambito){
        for (Simbolo sim : this){
            try{
                if (sim.id.equals(id) && sim.tipo == tipo && (ambito.equals(sim.ambito)))
                    return sim;
            }catch(Exception ex){}
        }
        return null;        
    }
    
    
    public boolean agregarVariable(Simbolo s){
        if (getVariable(s.id, s.tipo, s.ambito) == null){
            this.add(s);
            return true;
        }
        return false;        
    }
    
    public Simbolo getFuncion(String id,  int cantParametros){
        for (Simbolo sim : this){
            try{
                if (sim.id.equals(id) && sim.tipo == TipoSimbolo.funcion && sim.parametros.size() == cantParametros)
                    return sim;
            }catch(Exception ex){}
        }
        return null;        
    }
    
    public boolean agregarFuncion(Simbolo s){
        if (getFuncion(s.id, s.parametros.size()) == null){
            this.add(s);
            return true;
        }
        return false;        
    }
    
    public void quitarFuncion(Simbolo s){
        Simbolo fun = getFuncion(s.id, s.parametros.size());
        if (fun != null){
            this.removeAll(fun.parametros);
        }
        this.remove(fun);
    }
    
    public Simbolo getComponenteByID(String id){
         for (Simbolo sim : this){
            try{
                if (sim.id.equals(id) && sim.tipo == TipoSimbolo.componente)
                    return sim;
            }catch(Exception ex){}
        }
        return null;  
    }
    
    public void agregarComponente(Simbolo s){
        if (s!= null)
            this.add(s);
    }

    
    @Override
    public String toString(){
        String val = "----------------------- TABLA SÍMBOLOS ---------------------------\n";
        for (Simbolo thi : this) {
            val += thi.toString() +  "\n";
        }
        val += "----------------------- FIN TABLA SÍMBOLOS ---------------------------\n";
        return val;
    }
}
