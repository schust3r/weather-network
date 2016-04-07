/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.grafo;

/**
 * Nodo que representa una conexion entre paises
 * @author Joel
 */
public class Nodo {
    
    private int idPais1;
    private int idPais2;
    private int peso;
    
    public Nodo(int idp1, int idp2, int p) {
        this.idPais1 = idp1;
        this.idPais2 = idp2;
        this.peso    = p;
    }

    /**
     * Getters para atributos privados
     * @return id del primer pais, id del segundo pais o peso entre ambos
     */
    public int getIdPais1() {
        return idPais1;
    }

    public int getIdPais2() {
        return idPais2;
    }

    public int getPeso() {
        return peso;
    }
}
