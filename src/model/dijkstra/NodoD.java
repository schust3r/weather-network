/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dijkstra;

/**
 * Clase Nodo para Dijkstra
 * @author Joel
 */
public class NodoD implements Comparable<NodoD> {
    
    int vertice;
    int  distancia = Integer.MAX_VALUE;
    NodoD procedencia = null;
    
    public NodoD(int nombre, int distancia, NodoD nodo) { 
        this.vertice     = nombre; 
        this.distancia   = distancia; 
        this.procedencia = nodo; 
    }
    
    public NodoD(int nombre) { 
        this(nombre, 0, null); 
    }
    
    public int compareTo(NodoD temp) { 
        return this.distancia - temp.distancia; 
    }
    
    public boolean equals(Object objeto) {
        NodoD temp = (NodoD) objeto;
        return this.vertice == temp.vertice;
    }
}