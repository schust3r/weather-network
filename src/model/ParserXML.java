/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Clase ParserXML para parsear valores desde String XML a ArrayList utilizable
 * por el resto del programa
 * @author Ronald
 * @author Joel
 */
public class ParserXML {
    
    public ParserXML() { }

    public ArrayList retornarTiempo(String xml) {
        try {
            ArrayList infoClima = new ArrayList<>();
            /**
             * Implementar recursos para manipular un String como XML
             */
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);

            // Obtener la raiz del XML
            Node root = doc.getFirstChild();

            // Aplicar correcciones sobre el primer resultado
            String data = root.getChildNodes().item(0).getNodeValue();
            data = data.replaceAll(">  <", "><");
            
            /**
             * Depurar datos de nuevo para que sean utilizables
             * El string XML obtenido desde el web service presenta espacios incorrectos
             * y este paso es para eliminar dichos espacios y poder parsear de nuevo.
             */
            if (!"Data Not Found".equals(data)) {
                
                InputSource is2 = new InputSource(new StringReader(data));
                Document   doc2 = builder.parse(is2);
            
                // Obtener la raiz del XML depurado
                NodeList root2 = doc2.getChildNodes();
            
                Node comp = obtenerNodo("CurrentWeather", root2);

                NodeList nodos = comp.getChildNodes();

                // Agregar los valores recuperados al ArrayList
                infoClima.add(obtenerValorNodo("Location", nodos));
                infoClima.add(obtenerValorNodo("Time", nodos));
                infoClima.add(obtenerValorNodo("Wind", nodos));
                infoClima.add(obtenerValorNodo("Visibility", nodos));
                infoClima.add(obtenerValorNodo("SkyConditions", nodos));
                infoClima.add(obtenerValorNodo("Temperature", nodos));
                infoClima.add(obtenerValorNodo("DewPoint", nodos));
                infoClima.add(obtenerValorNodo("RelativeHumidity", nodos));
                infoClima.add(obtenerValorNodo("Pressure", nodos));

                return infoClima; // Retornar ArrayList con todos los datos
            }
            else {
                for (int i = 0; i < 9; i++)
                    infoClima.add("No hay datos disponibles");
                return infoClima;
            }
        } 
        catch (ParserConfigurationException | SAXException | IOException | DOMException ex) { 
            System.out.println("Excepción regulada.");
        }
        return null;
    }
    
    /**
     * Busca un nodo que tenga un Tag específico
     * @param tag
     * @param nodos
     * @return Nodo - con el tag buscado si lo hay
     */
    protected Node obtenerNodo(String tag, NodeList nodos) {
        for (int i = 0; i < nodos.getLength(); i++) {
            Node nodo = nodos.item(i);
            if (nodo.getNodeName().equalsIgnoreCase(tag)) {
                return nodo;
            }
        }
        return null;
    }    
    
    /**
     * Busca el contenido de un nodo que tenga un Tag específico
     * @param tag
     * @param nodos
     * @return String - con el valor que contiene un nodo si lo hay
     */
    protected String obtenerValorNodo(String tag, NodeList nodos) {
        for (int i = 0; i < nodos.getLength(); i++) {
            Node nodo = nodos.item(i);
            if (nodo.getNodeName().equalsIgnoreCase(tag)) {
                NodeList childNodes = nodo.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if (data.getNodeType() == Node.TEXT_NODE)
                        return data.getNodeValue();
                }
            }
        }
        return "";
    }
}