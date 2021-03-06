/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi2usacweb;

import compilacion.MotorExplorador;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Njgonzalez
 */
public class Explorador extends javax.swing.JFrame {
    
    public String archivoPorDefecto = Env.PAGINA_POR_DEFECTO;
    public MotorExplorador motor;
    public int WIDTH_EXPLORER;
    /**
     * Creates new form Explorador
     */
    public Explorador() {        
        initComponents();
        this.txtUrl.setText(archivoPorDefecto);
        this.motor = new MotorExplorador(this);
        
        WIDTH_EXPLORER = this.tabsContainer.getWidth();   
        
         ChangeListener changeListener = (ChangeEvent changeEvent) -> {
             javax.swing.JTabbedPane sourceTabbedPane = (javax.swing.JTabbedPane) changeEvent.getSource();
             int index = sourceTabbedPane.getSelectedIndex();
             //System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
             motor.cambiarCompilador(index);
        };
        tabsContainer.addChangeListener(changeListener);
        
        this.tabsConsolaContainer.setPreferredSize(new Dimension(tabsConsolaContainer.getWidth(), 0));
        this.tabsConsolaContainer.setSize(tabsConsolaContainer.getWidth(), 0);
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
        tabsConsolaContainer = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtConsolaSalida = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuItemRefresh = new javax.swing.JMenuItem();
        menuItemNewTab = new javax.swing.JMenuItem();
        menuItemCloseTab = new javax.swing.JMenuItem();
        menuShowConsolas = new javax.swing.JMenuItem();
        menuItemLimpiarConsolas = new javax.swing.JMenuItem();
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

        tabsContainer.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                tabsContainerComponentResized(evt);
            }
        });

        txtConsolaSalida.setColumns(20);
        txtConsolaSalida.setRows(5);
        jScrollPane1.setViewportView(txtConsolaSalida);

        tabsConsolaContainer.addTab("Consola", jScrollPane1);

        jMenu1.setText("File");

        menuItemRefresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        menuItemRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        menuItemRefresh.setText("Refresh");
        menuItemRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRefreshActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemRefresh);

        menuItemNewTab.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        menuItemNewTab.setText("Nueva Pestaña");
        menuItemNewTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemNewTabActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemNewTab);

        menuItemCloseTab.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        menuItemCloseTab.setText("Cerrar Pestaña");
        menuItemCloseTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCloseTabActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemCloseTab);

        menuShowConsolas.setText("Mostrar Consolas");
        menuShowConsolas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuShowConsolasActionPerformed(evt);
            }
        });
        jMenu1.add(menuShowConsolas);

        menuItemLimpiarConsolas.setText("Limpiar Consolas");
        menuItemLimpiarConsolas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLimpiarConsolasActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemLimpiarConsolas);

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
                    .addComponent(tabsContainer)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tabsConsolaContainer))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabsConsolaContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
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

    private void menuItemRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRefreshActionPerformed
        refrescar();
    }//GEN-LAST:event_menuItemRefreshActionPerformed

    private void tabsContainerComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tabsContainerComponentResized
                
    }//GEN-LAST:event_tabsContainerComponentResized

    private void menuItemNewTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNewTabActionPerformed
        agregarNuevaPestaña();
    }//GEN-LAST:event_menuItemNewTabActionPerformed

    private void menuItemCloseTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCloseTabActionPerformed
        closeSelectedTab();
    }//GEN-LAST:event_menuItemCloseTabActionPerformed

    private void menuShowConsolasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuShowConsolasActionPerformed
        this.tabsConsolaContainer.setSize(tabsConsolaContainer.getWidth(), 200);
    }//GEN-LAST:event_menuShowConsolasActionPerformed

    private void menuItemLimpiarConsolasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLimpiarConsolasActionPerformed
        limpiarConsolas();
    }//GEN-LAST:event_menuItemLimpiarConsolasActionPerformed

    public void alert(String msj){
        JOptionPane.showMessageDialog(this, msj);        
    }
    
    public void limpiarConsolas(){
        txtConsolaSalida.setText("");
    }
    
    public int getDocumentWidth(){
        return this.tabsContainer.getWidth();
    }
    
    private void refrescar(){        
        requestTab(txtUrl.getText(), Navegacion.CARGAR);        
    }
    
    public void requestTab(String rutaArchivo, Navegacion navegacion){
        loadTab(motor.getTab(rutaArchivo, this.tabsContainer.getSelectedIndex(), navegacion));        
    }
    
    public void closeSelectedTab(){
        int selTab = tabsContainer.getSelectedIndex();
        if (selTab > -1){
            motor.eliminarCompilador(selTab);
            tabsContainer.remove(selTab);
        }
    }
    
    public boolean loadTab(TabNavegador tab){
        try{
            JScrollPane scroll = new JScrollPane(tab.contenido 
                ,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                ,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
            int indxTabSel = this.tabsContainer.getSelectedIndex();
            if (indxTabSel == -1){
                tabsContainer.addTab(tab.titulo, scroll);
            }else{
                tabsContainer.remove(indxTabSel);
                tabsContainer.insertTab(tab.titulo, null, scroll, "", indxTabSel);
                this.tabsContainer.setSelectedIndex(indxTabSel);
            }
            return true;
        }catch(Exception ex){
            return false;
        }        
    }
    
    private void agregarNuevaPestaña() {
        this.tabsContainer.addTab("Nuevo", new JPanel());
        this.tabsContainer.setSelectedIndex(tabsContainer.getTabCount()-1);        
    }
    
    public void appendConsolaSalida(String s){
        if (s== null)
            return;
        txtConsolaSalida.append(s+"\n");
            
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem menuItemCloseTab;
    private javax.swing.JMenuItem menuItemLimpiarConsolas;
    private javax.swing.JMenuItem menuItemNewTab;
    private javax.swing.JMenuItem menuItemRefresh;
    private javax.swing.JMenuItem menuShowConsolas;
    private javax.swing.JTabbedPane tabsConsolaContainer;
    private javax.swing.JTabbedPane tabsContainer;
    private javax.swing.JTextArea txtConsolaSalida;
    private javax.swing.JTextField txtUrl;
    // End of variables declaration//GEN-END:variables
}
