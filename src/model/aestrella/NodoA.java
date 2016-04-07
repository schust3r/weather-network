/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.aestrella;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Nodo para algoritmo de búsqueda A*
 * @author Joel
 */
public class NodoA implements Comparable {

    private String nombre;
    private ArrayList<AristaA> listaAdj;
    private NodoA anterior = null;

    // VAlores de costo de búsqueda A*
    int[] costos = new int[3];
    public static final int costo_f = 0;
    public static final int costo_g = 1;
    public static final int costo_h = 2;

    public NodoA(String s) { 
        this.nombre = s; 
        this.listaAdj = new ArrayList<>();
        for (int i = 0; i < 3; i++) 
            costos[i] = 0;  	
    }
    
    public void agregaArista(AristaA a) { 
        listaAdj.add(a); 
    }
    
    public int compareTo(Object o) {
        NodoA otro = (NodoA) o;
        if (costos[costo_f] < otro.costos[costo_f])
            return -1;
        else if (costos[costo_f] > otro.costos[costo_f])
            return 1;
        else
            return 0;
    }
    
    /**
     * Getters & Setters
     */
    public ArrayList<AristaA> getListaAdj() {
        return listaAdj;
    }

    public String getNombre() { 
        return nombre; 
    }

    public Iterator<AristaA> getAristas() {
        return listaAdj.iterator();
    }

    public AristaA getAristaHasta(NodoA n) {
        for (AristaA e : listaAdj) {
           if (e.getDest().equals(n))
                return e;
        }
        return null;
    }

    public void setCosto(int idx, int c) {
        costos[idx] = c;
    }

    public int getCosto(int idx) {
        return costos[idx];
    }

    public void setAnterior(NodoA n) {
        anterior = n;
    }

    public NodoA getAnterior() {
        return anterior;
    }

}