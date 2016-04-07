/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.aestrella;

/**
 * Arista o camino entre nodos para algoritmo A Estrella
 * @author Joel
 */
public class AristaA {

    private NodoA dest; // nodo objetivo
    private int   costo; // peso de la arista

    public AristaA(NodoA n, int c) { 
        dest  = n; 
        costo = c; 
    }

    public NodoA getNodoDestino() {
        return dest; 
    }

    /**
     * Getters & Setters
     */
    public int getCosto() { 
        return costo; 
    }

    public NodoA getDest() {
        return dest;
    }
    
}
