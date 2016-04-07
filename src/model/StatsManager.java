/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Maneja los conteos de usos, victorias y promedios de los algoritmos.
 * 
 * @author Joel
 */
public class StatsManager {
    
    private String ultimoGanador;
    
    // contadores de uso
    private int usosDijkstra;
    private int usosBellman;
    private int usosAestrella;
    
    // ArrayList de pesos
    private ArrayList pesosDijkstra;
    private ArrayList pesosBellman;
    private ArrayList pesosAestrella;
    
    // contadores de ocasiones en las que cada algoritmo fue el mejor
    private int topDijkstra;
    private int topBellman;
    private int topAestrella;
    
    // ArrayList de tiempos
    private ArrayList tiemposDijkstra;
    private ArrayList tiemposBellman;
    private ArrayList tiemposAestrella;

    public StatsManager() {
        this.ultimoGanador = "";
        
        this.usosDijkstra    = 0;
        this.usosBellman     = 0;
        this.usosAestrella   = 0;

        this.topDijkstra     = 0;
        this.topBellman      = 0;
        this.topAestrella    = 0;

        this.pesosDijkstra  = new ArrayList();
        this.pesosBellman   = new ArrayList();
        this.pesosAestrella = new ArrayList();
        
        this.tiemposDijkstra  = new ArrayList();
        this.tiemposBellman   = new ArrayList();
        this.tiemposAestrella = new ArrayList();
    }
    
    /**
     * Cada vez que se ejecutan los algoritmos de busqueda estos valores
     * se aumentan en 1
     */
    public void aumentarUsos() {
        this.usosDijkstra  += 1;
        this.usosBellman   += 1;
        this.usosAestrella += 1;
    }
    
    /**
     * Toma las rutas desde la selección de usuario en MapaMundi, y selecciona
     * cuál fue el algoritmo ganador y suma sus pesos.
     * @param dj - array de Dijkstra 
     * @param bf - array de Bellman Ford
     * @param aS - array de A Star
     */
    public void recibirRutas(ArrayList dj, ArrayList bf, ArrayList aS) {
        
        int pesoDj = (int) dj.get(1);
        int pesoBf = (int) bf.get(1);
        int pesoAs = (int) aS.get(1);
        
        // Agregar pesos a listas
        pesosDijkstra.add(pesoDj);
        pesosBellman.add(pesoBf);
        pesosAestrella.add(pesoAs);
        
        ArrayList pesos = new ArrayList();
        
        pesos.add(pesoDj);
        pesos.add(pesoBf);
        pesos.add(pesoAs);
        
        long tiempoDj = (long) dj.get(2);
        long tiempoBf = (long) bf.get(2);
        long tiempoAs = (long) aS.get(2);
        
        // Agregar tiempos en ns a listas
        tiemposDijkstra.add(tiempoDj);
        tiemposBellman.add(tiempoBf);
        tiemposAestrella.add(tiempoAs);
        
        ArrayList tiempos = new ArrayList();
        
        tiempos.add(tiempoDj);
        tiempos.add(tiempoBf);
        tiempos.add(tiempoAs);
        
        // Comprobar si todos los pesos son iguales
        if (pesoDj == pesoBf && pesoBf == pesoAs) {
           
            // Si no, hacer comparación por tiempos
            long min = (long) Collections.min(tiempos);
           
            if (tiempos.indexOf(min) == 0) {  
                topDijkstra += 1;
                ultimoGanador = "Dijkstra";
            }
 
            if (tiempos.indexOf(min) == 1) {
                topBellman += 1;
                ultimoGanador = "Bellman-Ford";
            }
 
            if (tiempos.indexOf(min) == 2) {
                topAestrella += 1;
                ultimoGanador = "A*";
 
            }
        }
        // Si alguno de los pesos es distinto
        else {
           
            int min = (int) Collections.min(pesos);
           
            if (pesos.indexOf(min) == 0) {  
                topDijkstra += 1;
                ultimoGanador = "Dijkstra";
            }
 
            if (pesos.indexOf(min) == 1) {
                topBellman += 1;
                ultimoGanador = "Bellman-Ford";
            }
 
            if (pesos.indexOf(min) == 2) {
                topAestrella += 1;
                ultimoGanador = "A*";                
            }            
        }
        System.out.println(ultimoGanador);
        aumentarUsos();
    }
    
    /**
     * Getters & Setters
     */
    
    public String getUltimoGanador() {
        return ultimoGanador;
    }
    
    /**
     * Recuperar promedio de lista tiempos de cada algoritmo
     */
    public long getTiempoMedDijkstra() {
        if (!tiemposDijkstra.isEmpty()) {
            long suma = 0;
            for (Object tiemposDijkstra1 : tiemposDijkstra)
                suma += (long) tiemposDijkstra1;
            return suma / tiemposDijkstra.size();
        }
        return 0;
    }
    
    public long getTiempoMedBellman() {
        if (!tiemposBellman.isEmpty()) {
            long suma = 0;
            for (Object tiemposBellman1 : tiemposBellman)
                suma += (long) tiemposBellman1;
            return suma / tiemposBellman.size();
        }
        return 0;
    }
        
    public long getTiempoMedAEstrella() {
        if (!tiemposAestrella.isEmpty()) {
            long suma = 0;
            for (Object tiemposAestrella1 : tiemposAestrella)
                suma += (long) tiemposAestrella1;
            return suma / tiemposAestrella.size();      
        }
        return 0;
    }
    
    /**
     * Recuperar promedio de lista pesos de cada algoritmo
     */
    public int getPesosMedDijkstra() {
        if (!pesosDijkstra.isEmpty()) {
            int suma = 0;
            for (Object pesosDijkstra1 : pesosDijkstra)
                suma += (int) pesosDijkstra1;
            return suma / pesosDijkstra.size();
        }
        return 0;
    }
    
    public int getPesosMedBellman() {
        if (!pesosBellman.isEmpty()) {
            int suma = 0;
            for (Object pesosBellman1 : pesosBellman)
                suma += (int) pesosBellman1;
            return suma / pesosBellman.size();
        }
        return 0;
    }
        
    public int getPesosMedAEstrella() {
        if (!pesosAestrella.isEmpty()) {
            int suma = 0;
            for (Object pesosAestrella1 : pesosAestrella)
                suma += (int) pesosAestrella1;
            return suma / pesosAestrella.size();      
        }
        return 0;
    }    
    
    /**
     * Ejecuciones
     */
    public int getUsosDijkstra() {
        return usosDijkstra;
    }

    public int getUsosBellman() {
        return usosBellman;
    }

    public int getUsosAestrella() {
        return usosAestrella;
    }

    /**
     * Victorias
     */
    
    public int getTopDijkstra() {
        return topDijkstra;
    }

    public int getTopBellman() {
        return topBellman;
    }

    public int getTopAestrella() {
        return topAestrella;
    }

}
