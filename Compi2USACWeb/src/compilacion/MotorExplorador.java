/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilacion;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Njgonzalez
 */
public class MotorExplorador {
    Container documento;
    TablaSimbolos ts;
    int x = -100;
    int y = -100;
    
    public MotorExplorador(Container documento, TablaSimbolos ts){
        this.documento = documento;
        this.ts = ts;        
    }
    
    public void iniciar(){
        NodoAST nodoCHTML = ts.getComponenteByID("chtml").nodo;        
        agregarComponentes(documento, nodoCHTML);        
    }
    
    private Component getComponent(NodoAST nodo){
        switch(nodo.tipo){
            case panel:
                JPanel panel = new JPanel();
                //panel.setSize(200, 100);
                //panel.setBackground(Color.BLACK);
                //panel.setLocation(x, y);
                x+= 100;
                y += 100;
                return panel;
        }
        return null;
    }
    
    private void agregarComponentes(Container container, NodoAST nodo){
        for (NodoAST n: nodo.hijos){
            Component nuevoComponent = getComponent(n);
            if (nuevoComponent != null){
                container.add(nuevoComponent);
                if (nuevoComponent instanceof Container)
                    agregarComponentes((Container)nuevoComponent, n);                
            }
            agregarComponentes(container, n);
        }
    }
    
}
