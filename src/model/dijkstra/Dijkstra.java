/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.dijkstra;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Clase Dijkstra con algoritmo
 * @author Ronald
 */
public class Dijkstra {

    private ArrayList  nodos;  // Letras de identificación de nodo
    private int[][] grafo;  // Matriz de distancias entre nodos
    String  rutaMasCorta;         // distancia más corta
    List<NodoD> visitados = null;  // nodos visitados por algoritmo 
    
    
    public Dijkstra(ArrayList serieNodos) {
        nodos = serieNodos;
        grafo = new int[nodos.size()][nodos.size()];
    }

    // Imprime la ruta más corta desde “A” hasta otros nodos.
    public ArrayList recuperarRuta(int inicio, int destino) {
        
        ArrayList rutaConPeso = new ArrayList<>();

        // Comenzar a contar el tiempo
        long startTime = System.nanoTime();
        
        calcularRutasDesdeInicio(inicio);
        
        // Obtiene el nodo final de la lista de visitados
        NodoD temp = new NodoD(destino);
        if (!visitados.contains(temp)) {
            return null;
        }
        
        temp = visitados.get(visitados.indexOf(temp));
        int distancia = temp.distancia;
        
        // Almacena la ruta en una pila
        Stack<NodoD> pila = new Stack<>();

        while (temp != null) {
            pila.add(temp);
            temp = temp.procedencia;
        }
        
        ArrayList ruta = new ArrayList();
        // Obtiene datos de la pila en el orden correcto
        while(!pila.isEmpty()){
            ruta.add(pila.pop().vertice);
        }
        
        // Calcular tiempo de ejecución
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        
        rutaConPeso.add(ruta);
        rutaConPeso.add(distancia);
        rutaConPeso.add(elapsedTime);
        
        return rutaConPeso;
    }

    // ** Implementación de Algoritmo de Dijkstra ** 
    // Encuentra las rutas mas cortas desde 'A' hasta los demás
    public void calcularRutasDesdeInicio(int inicio) {
        try {
            Queue<NodoD> cola = new PriorityQueue<>(); // Cola de prioridad
            NodoD inicial = new NodoD(inicio);  // Nodo inicial

            visitados = new LinkedList<>(); // Lista de nodos visitados
            cola.add(inicial);              // Agregar el nodo 'A' a la cola
            while(!cola.isEmpty()) {       
                NodoD temp = cola.poll();  // Obtiene el primer elemento
                visitados.add(temp);              // Agregar a la lista de visitados
                int pos = posicionNodo(temp.vertice);   
                for (int i = 0; i < grafo[pos].length; i++) { // Ver hijos de nodo              
                if (grafo[pos][i]==0) { continue; }
                if (fueVisitado(i))   { continue; } 

                NodoD nodo = new NodoD((int) nodos.get(i),temp.distancia+grafo[pos][i],temp);

                if (!cola.contains(nodo)) {
                    cola.add(nodo); 
                }

                for(NodoD nodo1: cola) {
                    if (nodo1.vertice == nodo.vertice 
                        && nodo1.distancia > nodo.distancia) {
                        cola.remove(nodo1); 
                        cola.add(nodo);
                        break;         
        } } } } } 
        catch (Exception e) { System.out.println("Acción ilegal controlada.");}
    }
    
    // Asignar la arista entre dos nodos
    public void agregaCamino(int origen, int destino, int distancia) {
        int posNodoOrigen = posicionNodo(origen);
        int posNodoDest = posicionNodo(destino);
        grafo[posNodoOrigen][posNodoDest] = distancia;
        grafo[posNodoDest][posNodoOrigen] = distancia;
    }

    public int posicionNodo(int nodo) {
        for(int i=0; i < nodos.size(); i++) {
            if ((int) nodos.get(i) == nodo) {
                return i; 
            }}
        return -1; }
    
    // Ver si un nodo está en la lista de visitados
    public boolean fueVisitado(int index) {
        NodoD temp = new NodoD((int) nodos.get(index));
        return visitados.contains(temp);
    }

    public void setNodos(ArrayList nodos) {
        this.nodos = nodos;
    }

    public int[][] getGrafo() {
        return grafo;
    }

    public ArrayList getNodos() {
        return nodos;
    }
    
}