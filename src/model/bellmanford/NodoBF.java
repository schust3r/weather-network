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
public class NodoBF<T> implements Comparable<NodoBF<T>> {
     
    private T        data;
    private boolean  visitado;
    
    private Integer   index     = null;
    public  double    distancia = Double.POSITIVE_INFINITY;
    
    private NodoBF<T> anterior = null;
    
    public NodoBF(T data) {
        this.data = data;
    }

    /**
     * Getters & Setters
     */
    public T getData() {
        return data;
    }
    
    public boolean isVisitado() {
        return visitado;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public NodoBF<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoBF<T> anterior) {
        this.anterior = anterior;
    }    
    
    public void setVisitado() {
        visitado = true;
    }
     
    public void setNoVisitado() {
        visitado = false;
    }
     
    public int compareTo(NodoBF<T> ob) {
        String tempA = this.toString();
        String tempB = ob.toString();
         
        return tempA.compareTo(tempB);
    }
     
}