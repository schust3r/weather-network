/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import model.ParserXML;
import model.RedServidores;
import model.StatsManager;
import model.aestrella.AEstrella;
import model.aestrella.GrafoA;
import model.bellmanford.AristaBF;
import model.bellmanford.BellmanFord;
import model.bellmanford.GrafoBF;
import model.bellmanford.NodoBF;
import model.dijkstra.Dijkstra;
import model.grafo.Grafo;
import model.grafo.Nodo;

/**
 * Frame con apartado gráfico del programa, muestra un mapa en pantalla completa.
 * @author Ronald
 * @author Joel
 */
public final class MapaMundi extends javax.swing.JFrame {
    
    // Modelo controlador
    private static RedServidores red;
    private final  Random        generador;
    private final  Grafo         grafo;
    private        StatsManager  estadisticas;
    private        ParserXML     xmlParser;
    
    Dimension screenSize;
    private final float zoom = 0.8f;  // factor de zoom
    
    // Gráficos y variables para re-escalar
    private BufferedImage origImage;
    private ImageIcon     mapIcon;
    
    // Favicon
    private BufferedImage favImage;
    private ImageIcon     favIcon;   
    
    // Punto para grabar la posición inicial de algún click
    private Point origenClick;
    
    // Atributos para Algoritmos de Búsqueda
    private AEstrella   algoritmoA;
    private BellmanFord algoritmoBF;
    private Dijkstra    algoritmoD;
    private ArrayList   disponibles;
    
    private int serverInicio;
    private int serverDestino;
    
    /**
     * Creates new form MapaMundi
     */
    public MapaMundi(RedServidores redServidores) {
        MapaMundi.red  = redServidores;
        this.grafo     = new Grafo();
        this.generador = new Random();
        
        this.xmlParser    = new ParserXML();
        this.estadisticas = new StatsManager();
        this.serverInicio  = -1;
        this.serverDestino = -1;
        
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        initImagenes();
        
        initComponents();
        reubicarComponentes();
        this.setIconImage(favIcon.getImage());
        
        jScrollPane1.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        jScrollPane1.setSize(screenSize);
        
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setSize(screenSize);
        ubicarServidores();
        
        // Generar lista de nodos disponibles
        generarListaDisponibles();
        conectarServidoresGUI();
        
        // Preparar algoritmos de búsqueda
        setDijkstra();
        setBellmanFord();
        setAEstrella();
        
        setVisible(true);
    }
    
    private void limpiarRutasVisitadas() {
        restaurarImagen();
        ubicarServidores();
        conectarServidoresGUI();
        System.gc(); // limpiar la basura
        repaint();
    }
    
    /**
     * Crear líneas que simbolicen la conexión entre servidores
     */
    private void conectarServidoresGUI() {
        ArrayList puntos      = grafo.getModelo();
        ArrayList ubicaciones = new ArrayList<>();
        
        for (int i = 0; i < 30; i++)
            ubicaciones.add(red.getAccesoDatos().obtenerPais(i));
        
        System.out.println("Largo de lista ubicaciones: " + ubicaciones.size());
        System.out.println("Largo de lista puntos: " + puntos.size());
        
        for (int j = 0; j < puntos.size(); j++) {
            Nodo miNodo = (Nodo) puntos.get(j);
            ArrayList datosPais1 = (ArrayList) ubicaciones.get(miNodo.getIdPais1());
            ArrayList datosPais2 = (ArrayList) ubicaciones.get(miNodo.getIdPais2());

            /**
             * Dibujar línea
             */
            Graphics2D g2d = (Graphics2D) mapIcon.getImage().getGraphics();
            g2d.setStroke(new BasicStroke(0.8f)); 

            // Generar color aleatorio para la línea
            Color c = new Color(generador.nextInt(256), 
                                generador.nextInt(256), 
                                generador.nextInt(256));
            g2d.setColor(c);
            g2d.draw(new Line2D.Double((int) ((int) datosPais1.get(2)*zoom),
                                       (int) ((int) datosPais1.get(3)*zoom),
                                       (int) ((int) datosPais2.get(2)*zoom),
                                       (int) ((int) datosPais2.get(3)*zoom)));
        }
        repaint();
    }
    
    /**
     * Crear líneas que simbolicen las rutas generadas por los algoritmos
     */
    private void conectarRutasGUI(ArrayList ruta, Color c, float ancho) {
        ArrayList ubicaciones = new ArrayList<>();
        
        for (int i = 0; i < 30; i++)
            ubicaciones.add(red.getAccesoDatos().obtenerPais(i));
        
        for (int j = 0; j < ruta.size() - 1; j++) {
            int pais1 = (int) ruta.get(j);
            int pais2 = (int) ruta.get(j + 1);
            ArrayList datosPais1 = (ArrayList) ubicaciones.get(pais1);
            ArrayList datosPais2 = (ArrayList) ubicaciones.get(pais2);

            /**
             * Dibujar línea
             */
            Graphics2D g2d = (Graphics2D) mapIcon.getImage().getGraphics();
            g2d = (Graphics2D) mapIcon.getImage().getGraphics(); 
            g2d.setStroke(new BasicStroke(ancho)); 
    
            g2d.setColor(c);
            g2d.draw(new Line2D.Double((int) ((int) datosPais1.get(2)*zoom),
                                       (int) ((int) datosPais1.get(3)*zoom),
                                       (int) ((int) datosPais2.get(2)*zoom),
                                       (int) ((int) datosPais2.get(3)*zoom)));
        }
        repaint();
    }    
    
    /**
     * Genera lista de nodos disponibles (países)
     */
    private void generarListaDisponibles() {
        this.disponibles = new ArrayList<>();
        Nodo temp;
        int inicio, destino;
        for (int i = 0; i < grafo.getModelo().size() ; i++){
            temp    = (Nodo) grafo.getModelo().get(i);
            inicio  = temp.getIdPais1();
            destino = temp.getIdPais2();
            
            if (!disponibles.contains(destino)){
                disponibles.add(destino);
            }
            if (!disponibles.contains(inicio)){
                disponibles.add(inicio);
            }
        }
    }
    
    /**
     * Cambiar tamaño de la imagen según el factor de zoom
     */
    private void restaurarImagen() {
        mapIcon.getImage().getGraphics().clearRect(0, 0, origImage.getWidth(), 
                                                         origImage.getHeight());
        mapIcon.setImage(clonarImagen(origImage));
    }
    
    /**
     * Clon de imagen, copia profunda de BufferedImage (no solo referencia)
     * @param bi
     * @return 
     */
    private BufferedImage clonarImagen(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    /**
     * Crear recursos visuales del mapa
     */
    private void initImagenes() {
        // cargar y crear imagenes
        try {
            java.net.URL imageURL = MapaMundi.class.getResource("images/mapa.png");
            origImage = ImageIO.read(imageURL);
            mapIcon = new ImageIcon(clonarImagen(origImage));
            
            java.net.URL imageURL2 = MapaMundi.class.getResource("images/favicon.png");
            favImage = ImageIO.read(imageURL2);
            favIcon = new ImageIcon(favImage);
        } catch (IOException ie) { }
    }
    
    /**
     * Colocar servidores en sus respectivas posiciones
     */
    private void ubicarServidores() {
        for (int i = 0; i < 30; i++) {
            ArrayList info = red.getAccesoDatos().obtenerPais(i);
            JButton miServer = new JButton();
            try {
                Image img = ImageIO.read(getClass().getResource("images/server.png"));
                miServer.setSize(30, 34);
                miServer.setIcon(new ImageIcon(img));
                
                Image new_img = ImageIO.read(getClass().getResource("images/server_dark.png"));
                miServer.setPressedIcon(new ImageIcon(new_img));
            } catch (Exception e) { }
            
            miServer.setName(i + "");
            lblMapa.add(miServer);
            miServer.setOpaque(false);
            miServer.setFocusPainted(false);
            miServer.setBorderPainted(false);
            miServer.setContentAreaFilled(false);
            miServer.setCursor(new Cursor(Cursor.HAND_CURSOR));

            miServer.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    click(Integer.parseInt(miServer.getName()));
                }
            });
            
            miServer.setLocation((int) ((int) info.get(2) * zoom) - 15, 
                                 (int) ((int) info.get(3) * zoom) - 17);
        }
    }
    
    /**
     * Recibe un click del servidor, obtiene su nombre y una vez, que el origen y 
     * destino son elegidos, lo utiliza para localizar la información del tiempo.
     * @param param 
     */
    public void click(int param) {
        if (Reporte.enabled == true) {
            infoLabel.setText("No puede realizar otra consulta en este momento."
                              + " Cierre el reporte actual para seguir.");
        }
        else {
            if (this.serverInicio == -1) {
                this.serverInicio = param;
                infoLabel.setText("Servidor de origen seleccionado (" + 
                        red.getAccesoDatos().obtenerPais(serverInicio).get(0) +
                                   "). Haga clic en el destino.");
            }
            else {
                if (param != serverInicio) {
                    this.serverDestino = param;
                    infoLabel.setText("Servidor destino seleccionado (" + 
                         red.getAccesoDatos().obtenerPais(serverDestino).get(0) +
                                      "). Se muestran los resultados.");
                    generarBusqueda();
                }
                else {
                    infoLabel.setText("El servidor destino no puede ser el mismo " + 
                                      "que el de origen. Vuelva a intentarlo.");
                }
            }
        }
    }
    
    /**
     * Genera una búsqueda entre dos servidores. Se verifica si existe una ruta
     * con Dijkstra, si la hay se procede con esta.
     */
    public void generarBusqueda() {
        if (algoritmoD.recuperarRuta(serverInicio, serverDestino) == null) {
            infoLabel.setText("Lo sentimos, no hay conexión entre ambos países en este momento.");
        }
        else {
            ArrayList rutaD = algoritmoD.recuperarRuta(serverInicio, serverDestino);
            ArrayList rutaB = algoritmoBF.recuperarRuta(serverInicio, serverDestino);
            ArrayList rutaA = algoritmoA.recuperarRuta(serverInicio, serverDestino);
            
            conectarRutasGUI((ArrayList) rutaD.get(0), new Color(255, 0, 0), 2.5f);
            conectarRutasGUI((ArrayList) rutaB.get(0), new Color(0, 255, 0), 2.0f);
            conectarRutasGUI((ArrayList) rutaA.get(0), new Color(0, 0, 255), 1.5f);
            
            String    xmlTiempo   = red.getAccesoDatos().obtenerClima(serverDestino);
            ArrayList arrayTiempo = xmlParser.retornarTiempo(xmlTiempo);
            
            estadisticas.recibirRutas(rutaD, rutaB, rutaA);
            
            if (Reporte.enabled == false) {
                Reporte reporte = new Reporte(rutaD, rutaB, rutaA, arrayTiempo,
                                              estadisticas.getUltimoGanador());
            }
        }
        serverInicio = -1; serverDestino = -1;   
        
        // Reiniciar grafo de algoritmo A Estrella
        setAEstrella();
    }
    
    /**
     * Metodo para preparar algoritmo Dijkstra sobre el grafo aleatorio generado
     */
    public void setDijkstra(){
        Nodo temp;
        int inicio, destino, peso;
        algoritmoD = new Dijkstra(grafo.getModelo());
        Collections.sort(disponibles);
        algoritmoD.setNodos(disponibles);
        
        for (int i = 0; i < grafo.getModelo().size(); i++) {
            temp = (Nodo) grafo.getModelo().get(i);
            inicio  = temp.getIdPais1();
            destino = temp.getIdPais2();
            peso    = temp.getPeso();
            algoritmoD.agregaCamino(inicio, destino, peso);
            algoritmoD.agregaCamino(destino, inicio, peso);
        }
    }
    
    /**
     * Método para preparar algoritmo Bellman Ford sobre el grafo aleatorio generado
     */
    public void setBellmanFord() {
        algoritmoBF = new BellmanFord();
        Nodo temp;
        int inicio, destino, peso;
        GrafoBF grafobf = new GrafoBF();
        
        for (int i = 0; i < grafo.getModelo().size(); i++) {
            temp = (Nodo) grafo.getModelo().get(i);
            inicio  = temp.getIdPais1();            
            destino = temp.getIdPais2();
            peso    = temp.getPeso();
            
            NodoBF a = new NodoBF(inicio);
            NodoBF b = new NodoBF(destino);
            
            int aPos = grafobf.indexOf(a);
            int bPos = grafobf.indexOf(b);
 
            // Comprueba que nodo "a" no existe en el grafo y agrega
            if (aPos == -1)
                aPos = grafobf.agregarNodo(a);
 
            // Comprueba que nodo "b" no existe en el grafo y agrega
            if (bPos == -1)
                bPos = grafobf.agregarNodo(b);
 
            AristaBF arista = new AristaBF(grafobf.getNodoEn(aPos), 
                                           grafobf.getNodoEn(bPos), 
                                           peso);
            grafobf.agregarArista(arista);
        }
        algoritmoBF.setGrafo(grafobf);
    }
    
    /**
     * Método para preparar algoritmo A* sobre el grafo aleatorio generado
     */
    public void setAEstrella() {
        algoritmoA = new AEstrella();
        Nodo temp;
        int inicio, destino, peso;
        GrafoA grafo_a = new GrafoA();
        
        for (int i = 0; i < disponibles.size(); i++)
            grafo_a.agregarNodo((int) disponibles.get(i));
        
        for (int i = 0; i < grafo.getModelo().size(); i++) {
            temp = (Nodo) grafo.getModelo().get(i);
            inicio  = temp.getIdPais1();            
            destino = temp.getIdPais2();
            peso    = temp.getPeso();
            
            grafo_a.agregarArista(inicio, destino, peso);
            grafo_a.agregarArista(destino, inicio, peso);
        }
        algoritmoA.setGrafo(grafo_a);
    }
     
    /**
     * Inicializar componentes en sus posiciones respectivas (responsive)
     */
    private void reubicarComponentes() {
        // Centrar la imagen del mapa
        JViewport viewPort = jScrollPane1.getViewport();
        Point vpp = viewPort.getViewPosition();
        vpp.translate(300, 300);
        lblMapa.scrollRectToVisible(new Rectangle(vpp, viewPort.getSize()));
        
        // Colocar botones en sus posiciones
        btnSalir.setBounds(screenSize.width - 75, 30, 32, 32);
        btnMinimizar.setBounds(screenSize.width - 120, 30, 32, 32);
        infoLabel.setBounds(screenSize.width/2 - infoLabel.getWidth() - 50, 
                            screenSize.height - 80, 800, 50);
        btnRandom.setBounds(screenSize.width/2 - infoLabel.getWidth() + 350, 
                            screenSize.height - 85, 64, 64);
        btnStats.setBounds(screenSize.width/2 - infoLabel.getWidth() + 270, 
                            screenSize.height - 85, 64, 64);        
        btnLimpiar.setBounds(screenSize.width/2 - infoLabel.getWidth() + 190, 
                             screenSize.height - 85, 64, 64);    
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        btnStats = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnMinimizar = new javax.swing.JButton();
        btnRandom = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblMapa = new javax.swing.JLabel();
        lblMapa.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Google Weather ©");
        setBackground(new java.awt.Color(0, 0, 0));
        setUndecorated(true);
        getContentPane().setLayout(null);

        infoLabel.setBackground(new java.awt.Color(255, 255, 255));
        infoLabel.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("Esperando acción");
        infoLabel.setOpaque(true);
        infoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                infoLabelMouseEntered(evt);
            }
        });
        getContentPane().add(infoLabel);
        infoLabel.setBounds(150, 350, 310, 40);

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/btnLimpiar.png"))); // NOI18N
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setContentAreaFilled(false);
        btnLimpiar.setDefaultCapable(false);
        btnLimpiar.setMaximumSize(new java.awt.Dimension(64, 64));
        btnLimpiar.setMinimumSize(new java.awt.Dimension(64, 64));
        btnLimpiar.setPreferredSize(new java.awt.Dimension(64, 64));
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseEntered(evt);
            }
        });
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnLimpiar);
        btnLimpiar.setBounds(50, 250, 64, 64);

        btnStats.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/btnStats.png"))); // NOI18N
        btnStats.setBorderPainted(false);
        btnStats.setContentAreaFilled(false);
        btnStats.setDefaultCapable(false);
        btnStats.setPreferredSize(new java.awt.Dimension(64, 64));
        btnStats.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnStatsMouseEntered(evt);
            }
        });
        btnStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatsActionPerformed(evt);
            }
        });
        getContentPane().add(btnStats);
        btnStats.setBounds(-10, 310, 64, 64);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/btnSalir.png"))); // NOI18N
        btnSalir.setAlignmentY(0.0F);
        btnSalir.setBorder(null);
        btnSalir.setBorderPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir);
        btnSalir.setBounds(510, 20, 64, 64);

        btnMinimizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/btnMinimizar.png"))); // NOI18N
        btnMinimizar.setBorder(null);
        btnMinimizar.setBorderPainted(false);
        btnMinimizar.setContentAreaFilled(false);
        btnMinimizar.setFocusPainted(false);
        btnMinimizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinimizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnMinimizar);
        btnMinimizar.setBounds(410, 30, 60, 50);

        btnRandom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/btnRandom.png"))); // NOI18N
        btnRandom.setBorderPainted(false);
        btnRandom.setContentAreaFilled(false);
        btnRandom.setFocusPainted(false);
        btnRandom.setMinimumSize(new java.awt.Dimension(32, 32));
        btnRandom.setPreferredSize(new java.awt.Dimension(64, 64));
        btnRandom.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/btnRandomPressed.png"))); // NOI18N
        btnRandom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRandomMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRandomMouseEntered(evt);
            }
        });
        getContentPane().add(btnRandom);
        btnRandom.setBounds(80, 350, 64, 64);

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setWheelScrollingEnabled(false);
        jScrollPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseDragged(evt);
            }
        });
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jScrollPane1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseReleased(evt);
            }
        });

        lblMapa.setBackground(new java.awt.Color(0, 0, 0));
        lblMapa.setIcon(mapIcon);
        lblMapa.setOpaque(true);
        jScrollPane1.setViewportView(lblMapa);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 600, 400);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jScrollPane1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseDragged
        // TODO add your handling code here:
        if (origenClick != null) {
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, lblMapa);
            if (viewPort != null) {
                int deltaX = origenClick.x - evt.getX();
                int deltaY = origenClick.y - evt.getY();

                Rectangle view = viewPort.getViewRect();
                view.x += deltaX * 0.1;
                view.y += deltaY * 0.1;

                lblMapa.scrollRectToVisible(view);
            }
        }
    }//GEN-LAST:event_jScrollPane1MouseDragged

    private void jScrollPane1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MousePressed
        // TODO add your handling code here:
        origenClick = new Point(evt.getPoint());
    }//GEN-LAST:event_jScrollPane1MousePressed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        int dialogResult = JOptionPane.showConfirmDialog (null, "¿Realmente desea salir del programa?"+
                           " Se perderá el estado actual de la red.");
        if (dialogResult == JOptionPane.YES_OPTION)
            System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnMinimizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimizarActionPerformed
        // TODO add your handling code here:
        setState(ICONIFIED);
    }//GEN-LAST:event_btnMinimizarActionPerformed

    private void jScrollPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseReleased
        // TODO add your handling code here:
        btnSalir.repaint();
        btnMinimizar.repaint();
    }//GEN-LAST:event_jScrollPane1MouseReleased

    private void infoLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_infoLabelMouseEntered
        // TODO add your handling code here:
        infoLabel.repaint();
        btnRandom.repaint();
        btnLimpiar.repaint();
        btnStats.repaint();
    }//GEN-LAST:event_infoLabelMouseEntered

    private void btnRandomMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRandomMouseEntered
        // TODO add your handling code here:
        infoLabel.repaint();
        btnRandom.repaint();
        btnLimpiar.repaint();
        btnStats.repaint();
    }//GEN-LAST:event_btnRandomMouseEntered

    private void btnRandomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRandomMouseClicked
        // TODO add your handling code here:
        int index = generador.nextInt(disponibles.size());
        serverInicio = (int) disponibles.get(index);
        ArrayList pais = red.getAccesoDatos().obtenerPais(serverInicio);
        String nombrePais = (String) pais.get(0);
        infoLabel.setText("País de origen seleccionado al azar (" + 
                           nombrePais +
                           "). Elija el destino.");
    }//GEN-LAST:event_btnRandomMouseClicked

    private void btnStatsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStatsMouseEntered
        // TODO add your handling code here:
        infoLabel.repaint();
        btnRandom.repaint();
        btnLimpiar.repaint();
        btnStats.repaint();
    }//GEN-LAST:event_btnStatsMouseEntered

    private void btnStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatsActionPerformed
        // TODO add your handling code here:
        if (Estadisticas.enabled == false && Reporte.enabled == false)
            new Estadisticas(estadisticas);
    }//GEN-LAST:event_btnStatsActionPerformed

    private void btnLimpiarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseEntered
        // TODO add your handling code here:
        infoLabel.repaint();
        btnRandom.repaint();
        btnLimpiar.repaint();
        btnStats.repaint();
    }//GEN-LAST:event_btnLimpiarMouseEntered

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        infoLabel.setText("Por favor espere mientras se limpia el mapa...");
        limpiarRutasVisitadas();
        infoLabel.setText("Las rutas del mapa han sido restablecidas.");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    /**
     * @param args
     */
    public static void main(String args[]) {
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MapaMundi(red).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMinimizar;
    private javax.swing.JButton btnRandom;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnStats;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMapa;
    // End of variables declaration//GEN-END:variables
}
