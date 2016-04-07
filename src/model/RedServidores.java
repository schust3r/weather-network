/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Red de servidores donde se obtiene la información del tiempo y se posiciona
 * cada país según sus coordenadas en el mapa.
 * @author Ronald
 * @author Joel
 */
public class RedServidores {
    
    // Hacer Requests para obtener información del tiempo
    private final WebServiceRequest webservice;
    
    // Manejar status
    private int status;
    private boolean breakRequests;
    private boolean backupDbCargada;
    
    // Almacenar información en una tabla
    private final SQLiteJBDC  accesoDatos;
    private int               IdCount;
    
    // Backup de la BD y destino
    File fuenteDB;
    File destinoDB;
    
    public RedServidores() {
        this.webservice    = new WebServiceRequest();
        this.accesoDatos   = new SQLiteJBDC();
        this.IdCount       = 0;
        this.status        = 0; // No se ha finalizado
        this.breakRequests = false;
        this.backupDbCargada = false;
        
        // Cargar atributos de Backup
        this.fuenteDB = new File("default_db/paises.db");
        this.destinoDB = new File("paises.db");
        
        inicializarBaseDatos();
        insertarPaises();

        this.status = 1; // Carga finalizada sin errores
    }
    
    private void inicializarBaseDatos() {
        accesoDatos.crearTabla();
    }
    
    /**
     * Recuperar una base de datos pregrabada
     */
    public void cargarBackup() {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(fuenteDB);
            output = new FileOutputStream(destinoDB);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
            }
        } catch (IOException ex) {
            Logger.getLogger(RedServidores.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
                output.close();
            } catch (Exception e) { }
        }
    }
    
    /**
     * Inserción de 30 países a la base de datos
     */
    private void insertarPaises() {
        crearNuevaTupla("ANGOLA", "LUANDA", 2231, 1695);     // 1
        crearNuevaTupla("ARGENTINA", "ROSARIO", 1267, 2100);
        crearNuevaTupla("AUSTRALIA", "SYDNEY", 3713, 2010); 
        crearNuevaTupla("BOLIVIA", "SUCRE", 1257, 1803);
        crearNuevaTupla("BRAZIL", "BRASILIA", 1459, 1717);   // 5
        crearNuevaTupla("CANADA", "QUEBEC", 902, 762);
        crearNuevaTupla("CHINA", "BEIJING", 3239, 1127);
        crearNuevaTupla("COLOMBIA", "PROVIDENCIA", 1143, 1481); 
        crearNuevaTupla("CUBA", "HABANA", 1115, 1300);
        crearNuevaTupla("ETHIOPIA", "ADDIS ABABA", 2500, 1420); // 10
        crearNuevaTupla("FINLAND", "HELSINKI", 2293, 614);
        crearNuevaTupla("GERMANY", "HAMBURG", 2143, 843);
        crearNuevaTupla("GREENLAND", "NUUK", 1700, 400);
        crearNuevaTupla("HONDURAS", "TEGUCIGALPA", 1001, 1374);
        crearNuevaTupla("INDIA", "BOMBAY", 2960, 1337);         // 15
        crearNuevaTupla("INDONESIA", "JAKARTA", 3390, 1646);
        crearNuevaTupla("IRAN", "SHIRAZ", 2690, 1175);
        crearNuevaTupla("JAPAN", "OSAKA", 3633, 1060);
        crearNuevaTupla("MADAGASCAR", "ANTANANARIVO", 2573, 1821);
        crearNuevaTupla("MALAYSIA", "KUALA LUMPUR", 3265, 1504);   // 20
        crearNuevaTupla("MEXICO", "TIJUANA", 815, 1288); 
        crearNuevaTupla("NEW ZEALAND", "AUCKLAND", 3963, 2262); 
        crearNuevaTupla("NIGERIA", "LAGOS", 2120, 1466); 
        crearNuevaTupla("PAKISTAN", "KARACHI", 2815, 1207);       // 25
        crearNuevaTupla("RUSSIA", "MOSCOW", 2964, 532);
        crearNuevaTupla("SOUTH AFRICA", "PRETORIA", 2308, 1920);
        crearNuevaTupla("SPAIN", "MADRID", 1985, 1066);   
        crearNuevaTupla("SWEDEN", "STOCKHOLM", 2177, 680);
        crearNuevaTupla("TURKEY", "ANKARA", 2407, 1080);
        crearNuevaTupla("UNITED STATES OF AMERICA", "NEW YORK", 1058, 1106); // 30 B-)   
    }
    
    /**
     * Crea una tupla para un par Pais - Ciudad. Recupera los datos desde el WebService.
     * @param PAIS
     * @param CIUDAD 
     */
    public void crearNuevaTupla(String PAIS, String CIUDAD, int X, int Y) {
        if (!breakRequests) {
            if (internetAlcanzable()) {
                String clima = webservice.getResponse(PAIS, CIUDAD);
                if (clima.equals("Error.")) {
                    status = 2; // Se cayó la conexión 
                    breakRequests = true;
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error." + 
                    " Compruebe su conexión a Internet e inténtelo de nuevo más tarde.");
                }
                System.out.println(PAIS + ": " + clima);
                accesoDatos.insertarTupla(IdCount, PAIS, CIUDAD, clima, X, Y);
                IdCount++;
            }
            else {
                status = 3; // Internet inalcanzable
                breakRequests = true;
                JOptionPane.showMessageDialog(null, "No se ha detectado una conexión a la red." + 
                             " Compruebe su conexión a Internet e inténtelo de nuevo más tarde .");            
            }
        }
        else {
            if (!backupDbCargada) {
                JOptionPane.showMessageDialog(null, "Se ha cargado una base de datos de prueba." + 
                         " Puede utilizar el programa, pero los datos no estarán actualizados.");   
                cargarBackup();
                backupDbCargada = true;
            }
        }
    }
    
    /**
     * Verificar si el Internet puede se accedido
     * @return true si hay conexión
     */
    public static boolean internetAlcanzable() {
        try {
            //make a URL to a known source
            URL url = new URL("http://www.google.com");

            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();

        } catch (Exception e) { 
            return false;
        }
        return true;
    }
    
    /**
     * Getter para valor boolean cargaCompleta
     * @return true o false
     */
    public int getStatus() {
        return status;
    }
    
    public SQLiteJBDC getAccesoDatos() {
        return accesoDatos;
    }
    
}
