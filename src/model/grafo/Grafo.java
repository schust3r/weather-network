/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.grafo;

import java.util.*;

/**
 * Grafo para generar un estado de red
 * 
 * @author Joel
 */
public class Grafo {
    
    private ArrayList modelo;
    private Random    generador;
    
    public Grafo() {
        this.modelo    = new ArrayList<>();
        this.generador = new Random();
        llenarArreglo();
        seleccionarNodos();
    }
    
    /**
     * Guardar nodos-conexión en el arreglo modelo.
     */
    private void llenarArreglo() {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if (j > i) {
                    modelo.add(new Nodo(i, j, 1 + generador.nextInt(99)));
                }
            }
        }
        System.out.println("Cantidad de nodos: " + modelo.size());
    }
    
    /**
     * Elige índices para escoger los nodos que representarán las conexiones "vivas"
     * Elección aleatoria sin repetición de números, se preservan 239 nodos.
     * @param inicio
     * @param fin
     * @param contador
     * @return arreglo de enteros int[] con posiciones de nodos
     */
    public int[] numerosSinRepetir(int inicio, int fin, int contador) {
        int[] resultado = new int[contador];
        int cur = 0;
        int restantes = fin - inicio;
        for (int i = inicio; i < fin && contador > 0; i++) {
            double probabilidad = generador.nextDouble();
            if (probabilidad < ((double) contador) / (double) restantes) {
                contador--;
                resultado[cur++] = i;
            }
            restantes--;
        }
        return resultado;
    }
    
    /**
     * Método para modificar los nodos de la lista "modelo". Se escogen aleatoriamente
     * y se sobreescribe la lista "modelo" para reducir los nodos a 44, con la ayuda
     * del método "numerosSinRepetir"
     */
    private void seleccionarNodos() {
        ArrayList temp       = new ArrayList<>();
        int[]     obtenerPos = numerosSinRepetir(0, 435, 44);
        for (int i = 0; i < obtenerPos.length; i++)
            temp.add(modelo.get(obtenerPos[i]));
        modelo = temp;
    }
    
    // Getters & Setters
    public ArrayList getModelo() {
        return modelo;
    }
}    
