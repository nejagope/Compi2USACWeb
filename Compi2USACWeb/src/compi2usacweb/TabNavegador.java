/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi2usacweb;

import javax.swing.JPanel;

/**
 *
 * @author NELSONJAIR
 */
public class TabNavegador {
    public String titulo;
    public JPanel contenido;
    public int indice;

    public TabNavegador(String titulo, JPanel contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }        
}
