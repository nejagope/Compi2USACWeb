/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi2usacweb;

import compilacion.Compilador;
import compilacion.MotorExplorador;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import javafx.scene.layout.Border;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Njgonzalez
 */
public class Explorador extends javax.swing.JFrame {
    
    public MotorExplorador motor;
    /**
     * Creates new form Explorador
     */
    public Explorador() {        
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUrl = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();
        tabsContainer = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("USAC-Web");
        setSize(new java.awt.Dimension(1700, 1200));

        txtUrl.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        txtUrl.setText("C:/Users/Njgonzalez.AZUCAR/Documents/NetBeansProjects/Compi2USACWeb/Compi2USACWeb/src/compilacion/entrada1.chtml");
        txtUrl.setToolTipText("");
        txtUrl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUrlActionPerformed(evt);
            }
        });

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        btnRefresh.setBorder(null);
        btnRefresh.setMaximumSize(new java.awt.Dimension(48, 48));
        btnRefresh.setMinimumSize(new java.awt.Dimension(32, 32));
        btnRefresh.setPreferredSize(new java.awt.Dimension(48, 48));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tabsContainer)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtUrl, javax.swing.GroupLayout.PREFERRED_SIZE, 975, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 14, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refrescar();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txtUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUrlActionPerformed
        refrescar();
    }//GEN-LAST:event_txtUrlActionPerformed

    private void refrescar(){

        String rutaArchivo = txtUrl.getText();
        File file = new File(rutaArchivo);
        String nombreArchivo = file.getName();
        
        Compilador c = new Compilador();
        c.compilar(rutaArchivo);
        c.mostrarTablaSimbolosConsola();
        c.mostrarErroresConsola();   
        
        JPanel contenedor = new JPanel(null);
        //contenedor.setPreferredSize(new Dimension(20000, 20000));
        System.out.println(contenedor.getSize().toString());
        contenedor.setBackground(Color.WHITE);
        motor = new MotorExplorador(contenedor, c.tablaSimbolos);
        motor.iniciar();        
        
        String tabTitle = nombreArchivo;
        if (motor.tituloPestaña != null);
            tabTitle = motor.tituloPestaña;
        JScrollPane tab = new JScrollPane(contenedor ,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);        
        
        boolean addNewTab = true;
        int cantTabs = tabsContainer.getTabCount();
        int indiceTab = cantTabs;
        for (int i= 0; i< cantTabs; i++){
            if (tabTitle.equals(tabsContainer.getTitleAt(i))){                
                addNewTab = false;
                indiceTab = i;
            }
        }        
        if (addNewTab)
            tabsContainer.addTab(tabTitle, tab);
        else{
            tabsContainer.remove(indiceTab);
            tabsContainer.insertTab(tabTitle, null, tab, rutaArchivo, indiceTab);
        }
        
        
        
    }
    
    private void compilar(){
        motor.iniciar();
        pack();
        repaint();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Explorador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Explorador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Explorador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Explorador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Explorador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTabbedPane tabsContainer;
    private javax.swing.JTextField txtUrl;
    // End of variables declaration//GEN-END:variables
}
