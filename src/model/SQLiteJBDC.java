/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Acceso a base de datos
 * @author Joel
 */
public class SQLiteJBDC {
    
    public void crearConexion() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:paises.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Base de datos accesada con éxito");
    }
    
    public void crearTabla() {
        Connection c   = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:paises.db");
          System.out.println("Base de datos accesada con éxito");

          stmt = c.createStatement();
          String sql = "CREATE TABLE PAISES " +
                       "(ID INT PRIMARY KEY       NOT NULL, " +
                       " PAIS           CHAR(50)  NOT NULL, " + 
                       " CIUDAD         CHAR(50)  NOT NULL, " + 
                       " CLIMA          CHAR(50)  NOT NULL, " + 
                       " X              INT       NOT NULL, " +
                       " Y              INT       NOT NULL) ";
                  
          stmt.executeUpdate(sql);
          stmt.close();
          c.close();
        } catch ( Exception e ) {
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
        System.out.println("Tabla creada con éxito");
    }
    
    public void insertarTupla(int ID, String PAIS, String CIUDAD, 
                              String CLIMA, int X, int Y) {
        Connection c   = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:paises.db");
          c.setAutoCommit(false);
          System.out.println("Base de datos accesada con éxito");

          stmt = c.createStatement();
          
          String sql = "INSERT INTO PAISES (ID,PAIS,CIUDAD,CLIMA,X,Y) " +
                       "VALUES ("  + ID     + ",'" 
                                   + PAIS   + "','"
                                   + CIUDAD + "','" 
                                   + CLIMA  + "',"
                                   + X      + ","
                                   + Y      + ");"; 
          stmt.executeUpdate(sql);

          stmt.close();
          c.commit();
          c.close();
        } catch ( Exception e ) {
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
        System.out.println("Tupla insertada con éxito");
    }
    
    public String obtenerClima(int ID) {
        Connection c   = null;
        try {
            
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:paises.db");
            c.setAutoCommit(false);
            System.out.println("Base de datos accedida con éxito");

            ResultSet rs = c.createStatement().executeQuery( "SELECT CLIMA FROM PAISES WHERE ID="+ID+";" );
            String clima = rs.getString("CLIMA");
            
            System.out.println(clima);
            
            //rs.close();
            
            c.close();
            
            return clima;
            
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operación exitosa");
        return null;
    }
    
    /**
     * 
     * @param ID
     * @return 
     */
    public ArrayList obtenerPais(int ID) {
        Connection c   = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:paises.db");
            c.setAutoCommit(false);
            System.out.println("Base de datos accedida con éxito");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM PAISES WHERE ID="+ID+";" );
            String nombre = rs.getString("PAIS");
            String ciudad = rs.getString("CIUDAD");
            int    posX   = rs.getInt("X");
            int    posY   = rs.getInt("Y");
            
            rs.close();
            stmt.close();
            c.close();
            
            // Modelar el vector
            ArrayList dataPack = new ArrayList<>();
            dataPack.add(nombre);
            dataPack.add(ciudad);
            dataPack.add(posX);
            dataPack.add(posY);
            return dataPack;
            
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operación exitosa");
        return null;
    }
}    