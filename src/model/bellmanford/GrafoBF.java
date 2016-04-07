/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bellmanford;

import java.util.*;

/**
 *
 * @author Joel
 */
public class GrafoBF {
 
    private Vector<NodoBF> nodos;
    private Vector<AristaBF> aristas;
    private boolean dirigido;
    private boolean vecinosOrdenados;
    
    public GrafoBF() {
        this.nodos    = new Vector<>();
        this.aristas  = new Vector<>();
        this.dirigido = false;
        this.vecinosOrdenados = false;
    }
    
    // Agrega un nodo al grafo
    public int agregarNodo(NodoBF a) {
        nodos.add(a);
        return nodos.size() - 1;
    }
     
    // Agrega una arista al grafo
    public void agregarArista(AristaBF a) {
        aristas.add(a);
         
        if (!dirigido)
            aristas.add(new AristaBF(a.getB(), a.getA(), a.getPeso()));
    }
    
    public int indexOf(NodoBF a) {
        for(int i = 0; i < nodos.size(); i++)
            if(nodos.elementAt(i).getData().equals(a.getData()))
                return i;
                 
        return -1;
    }    
    
    /**
     * Getters & Setters
     */
    public double[][] getMatrizAdj() {
        double[][] matrizAdj = new double[nodos.size()][nodos.size()];
         
        for(int i = 0; i < nodos.size(); i++)
            for(int j = 0; j < nodos.size(); j++)
                if(i == j)
                    matrizAdj[i][j] = 0;
                else
                    matrizAdj[i][j] = Double.POSITIVE_INFINITY;
                 
        for(int i = 0; i < nodos.size(); i++) {
            NodoBF node = nodos.elementAt(i);
            //System.out.println("Current node: " + node);
             
            for(int j = 0; j < aristas.size(); j++) {
                AristaBF edge = aristas.elementAt(j);
                 
                if(edge.getA() == node) {
                    int indexOfNeighbor = nodos.indexOf(edge.getB());
                     
                    matrizAdj[i][indexOfNeighbor] = edge.getPeso();
                }
            }
        }
         
        return matrizAdj;
    }
     
    public boolean esDirigido() {
        return dirigido;
    }
     
    public boolean esOrdenado() {
        return vecinosOrdenados;
    }
     
    public void setVecinosOrdenados(boolean flag) {
        vecinosOrdenados = flag;
    }
     
    public Vector<NodoBF> getNodos() {
        return nodos;
    }
     
    public Vector<AristaBF> getAristas() {
        return aristas;
    }
     
    public NodoBF getNodoEn(int i) {
        return nodos.elementAt(i);
    }
     
    public void setNodosNoVisitados() {
        for(int i = 0; i < nodos.size(); i++)
            nodos.elementAt(i).setNoVisitado();
    }
     
    public Vector<NodoBF> getVecinos(NodoBF a) {
        Vector<NodoBF> vecinos = new Vector<>();
         
        for(int i = 0; i < aristas.size(); i++) {
            AristaBF edge = aristas.elementAt(i);
             
            if(edge.getA() == a)
                vecinos.add(edge.getB());
                 
            if(!dirigido && edge.getB() == a)
                vecinos.add(edge.getA());
        }
         
        if(vecinosOrdenados)
            Collections.sort(vecinos);
         
        return vecinos;
    }
     
}