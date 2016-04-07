/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.aestrella;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 
 * @author Joel
 */
public class AEstrella {

    private GrafoA grafo;
    private PriorityQueue<NodoA> listaPendientes;
    private List<NodoA> finalizados;

    public AEstrella() { 
        this.listaPendientes = new PriorityQueue<>();
        this.finalizados     = new LinkedList<>();
    }

    private int estimarCosto(NodoA node, NodoA target) {
        return node.getCosto(NodoA.costo_h);
    }
    
    private void expandirNodo(NodoA node, NodoA target) {

        // Navegar por todos los nodos vecinos
        for (AristaA e : node.getListaAdj()) {
            NodoA nodActual = e.getNodoDestino();
            
            // Verificar si el nodo ya fue manipulado
            if (finalizados.contains(nodActual))
                continue;

            AristaA ee = node.getAristaHasta(nodActual);

            // preparar coste-f
            int tf = node.getCosto(NodoA.costo_g) + ee.getCosto() + estimarCosto(nodActual, target);
            
            // Ignorar la ruta si es más costosa que la anterior
            if (listaPendientes.contains(nodActual) && tf > nodActual.getCosto(NodoA.costo_f))
                continue;

            nodActual.setAnterior(node);

            // actualizar costo-f
            nodActual.setCosto(NodoA.costo_f, tf);
            
            // actualizar costo-g
            nodActual.setCosto(NodoA.costo_g, node.getCosto(NodoA.costo_g) + ee.getCosto());

            // agregar a lista pendientes 
            if (listaPendientes.contains(nodActual))
                listaPendientes.remove(nodActual);
            
            listaPendientes.offer(nodActual);
        }
    }

    private List<NodoA> reconstruirRuta(NodoA node) {
        List<NodoA> path = new LinkedList<>();
        path.add(node);

        while (node.getAnterior() != null) {
            node = node.getAnterior();
            path.add(0, node);
        }
        
        return path;
    }

    public List<NodoA> buscar(String origen, String dest) {
        NodoA nodoOrig = grafo.getNodo(origen);
        NodoA nodoDest = grafo.getNodo(dest);

        listaPendientes.offer(nodoOrig);

        while(!listaPendientes.isEmpty()) {

            // Tomar los nodos con el menor coste f
            NodoA node = listaPendientes.poll();
            
            // Verificar si se alcanzó el nodo destino
            if (node.equals(nodoDest)) 
                return reconstruirRuta(node);
        
            // Buscar en los nodos siguientes
            expandirNodo(node, nodoDest);

            // Agregar nodo a la lista de finalizados
            finalizados.add(node);
        }
        return null;
    }
    
    /**
     * Recuperar ruta como ArrayList desde una 
     * @param origen  - nodo de partida
     * @param destino - nodo de llegada u objetivo
     * @return - ruta entre ambos nodos si la hay.
     */
    public ArrayList recuperarRuta(int origen, int destino) {
        
        // Arrays para guardar resultado final y camino
        ArrayList resultado = new ArrayList();
        ArrayList lblCamino = new ArrayList();
        
        // Comenzar a contar el tiempo
        long startTime = System.nanoTime();
        
        List<NodoA> ruta = buscar(origen + "", destino + "");
        
        // Recuperar ruta algoritmo A*
        for (NodoA n : ruta)
            lblCamino.add(Integer.parseInt(n.getNombre()));
        
        // Coste final de la ruta
        int finalCost = 0;
        for (int i = 0; i < ruta.size() - 1; i++) {
            finalCost += ruta.get(i).getAristaHasta(ruta.get(i + 1)).getCosto();
        }
        
        // Calcular tiempo de ejecución
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        
        resultado.add(lblCamino);
        resultado.add(finalCost);
        resultado.add(elapsedTime);
        return resultado;
    }
    
    /**
     * Getters & Setters
     */
    public void setGrafo(GrafoA ga) {
        this.grafo = ga;
    }
}
