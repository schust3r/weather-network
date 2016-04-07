/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bellmanford;

/**
 *
 * @author Joel
 */
public class AristaBF {
 
    private NodoBF a;
    private NodoBF b;
    private int    peso;
     
    public AristaBF(NodoBF a, NodoBF b) {
        this(a, b, (int) Double.POSITIVE_INFINITY);
    }
     
    public AristaBF(NodoBF a, NodoBF b, int peso) {
        this.a = a;
        this.b = b;
        this.peso = peso;
    }
     
    /**
     * Getters & Setters
     */
    public int getPeso() {
        return peso;
    }
    
    public NodoBF getA() {
        return a;
    }
    
    public NodoBF getB() {
        return b;
    }

}