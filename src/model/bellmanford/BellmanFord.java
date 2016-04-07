/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bellmanford;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

/**
 * @author Joel
 */
public class BellmanFord {
    
    private GrafoBF grafo;
    
    public BellmanFord() { }
 
    public ArrayList recuperarRuta(int inicio, int fin) {
        ArrayList resultado = new ArrayList<>();
        
        // Comenzar a contar el tiempo
        long startTime = System.nanoTime();
        
        Vector nodos = grafo.getNodos();
 
        for (Object source : nodos) {
            NodoBF nSource = (NodoBF) source;
            
            if ((int) nSource.getData() == inicio) {

                // 1. Inicializar el grafo
                for (Object destino : nodos) {
                    NodoBF nDestino = (NodoBF) destino;
                    if (nSource == destino)
                        nDestino.setDistancia(0);
                    else
                        nDestino.setDistancia(Double.POSITIVE_INFINITY);

                    nDestino.setAnterior(null);
                }

                Vector aristas = grafo.getAristas();

                // 2. "Relajar" las aristas varias veces
                for (int j = 0; j < nodos.size(); j++) {
                    for (Object arista : aristas) {
                        AristaBF aArista = (AristaBF) arista;
                        NodoBF a = aArista.getA();
                        NodoBF b = aArista.getB();

                        if (Double.compare(a.getDistancia() + aArista.getPeso(), b.getDistancia()) < 0) {
                            b.setDistancia(a.getDistancia() + aArista.getPeso());
                            b.setAnterior(a);
                        }
                    }
                }

                for (int j = 0; j < nodos.size(); j++) {
                    NodoBF node = (NodoBF) nodos.elementAt(j);

                    if ((int) node.getData() == fin) {

                        if (Double.compare(node.getDistancia(), Double.POSITIVE_INFINITY) != 0) {
                            Stack stack = new Stack();
                            NodoBF ptr = node;

                            do {
                                stack.push(ptr);
                                ptr = ptr.getAnterior();
                            } while(ptr != null);

                            ArrayList camino = new ArrayList<>();
                            
                            while(!stack.isEmpty()) {
                                NodoBF nodo = (NodoBF) stack.pop();
                                camino.add(nodo.getData());
                            }
                            
                            // Calcular tiempo de ejecución
                            long stopTime = System.nanoTime();
                            long elapsedTime = stopTime - startTime;

                            // Si hay ruta, retorna el camino y distancia
                            if (node.getDistancia() != Double.POSITIVE_INFINITY) {
                                resultado.add(camino);
                                resultado.add((int) node.getDistancia());
                                resultado.add(elapsedTime);
                                return resultado;
                            }
                            else {
                                return null;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Getters & Setters
     */
    public void setGrafo(GrafoBF gbf) {
        this.grafo = gbf;
    }
}