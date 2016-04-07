/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import model.RedServidores;

/**
 *
 * @author Joel
 */
public class Splash extends javax.swing.JFrame {

    // Red de servidores, se carga durante el Splash
    private RedServidores red;
    
    // Animación de carga
    private ImageIcon cargando;
    private boolean detener;
    
    // Favicon
    private BufferedImage favImage;
    private ImageIcon     favIcon;   
    
    
    /**
     * Creates new form Splash
     */
    public Splash() {
        initComponents();
        initImagenes();
        jLabel4.setIcon(cargando);
        setLocationRelativeTo(null);
        this.setIconImage(favIcon.getImage());
        setVisible(true);
        this.red = new RedServidores();
        esperarRed();
    }
    
    // Carga imágenes como recursos
    private void initImagenes() {
        try {
            java.net.URL imageURL = MapaMundi.class.getResource("images/loading.gif");
            cargando = new ImageIcon(imageURL);

            java.net.URL imageURL2 = MapaMundi.class.getResource("images/favicon.png");
            favImage = ImageIO.read(imageURL2);
            favIcon = new ImageIcon(favImage);
        }
        catch (Exception e) { }
    }
    
    private void esperarRed() {
        detener = false;
        SwingWorker worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                //Execute your logic
                while (red.getStatus() != 1) {
                    wait(500);
                }
                
                if (red.getStatus() == 2) {
                    detener = true;
                }
                
                if (red.getStatus() == 3) {
                    detener = true;
                }
                return null;
            }
        };
        worker.execute();
        worker.cancel(detener);
        MapaMundi mapaMundi = new MapaMundi(red);
        this.dispose();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 102, 255));
        jLabel2.setText("WEATHER");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 150, 90, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/googleLogo.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, -1, 109));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Obteniendo información sobre el tiempo...");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 330, 40));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 243, 40, 33));

        jLabel5.setForeground(new java.awt.Color(153, 153, 153));
        jLabel5.setText("Powered by webserviceX.net");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 267, 210, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Splash().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}