/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.aestrella;

import java.util.*;

/**
 * Grafo específico para algoritmo de búsqueda A Estrella
 * @author Joel
 */
public class GrafoA {
    
    // Guardar todos los nodos en grafo
    private HashMap<String, NodoA> setNodos;

    public GrafoA() {
        this.setNodos = new HashMap<>();
    }

    public NodoA agregarNodo(int nombre) throws ExcepcionNodo {
        if (setNodos.containsKey(nombre + ""))
            throw new ExcepcionNodo();
        NodoA n = new NodoA(nombre + "");
        setNodos.put(nombre + "", n);
        return n;
    }

    public NodoA getNodo(String nombre) {
        NodoA n = setNodos.get(nombre);
        if (n == null)
            return null;
        return n;
    }

    public void agregarArista(int inicio, int dest, int costo) {
        NodoA nodoInicio = getNodo(inicio + "");
        NodoA nodoDest   = getNodo(dest + "");
        nodoInicio.agregaArista(new AristaA(nodoDest, costo));
    }
    
}
