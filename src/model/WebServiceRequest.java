/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Enviar peticiones HTTP GET a WebserviceX.net
 * Se recibe respuesta para la ciudad y el pais solicitado en un String XML
 * @author Joel
 */
public class WebServiceRequest {
   
    public WebServiceRequest() {}
   
    public String getResponse(String pais, String ciudad) {
        pais   = pais.replace(" ", "%20");
        ciudad = ciudad.replace(" ", "%20");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet
            = new HttpGet("http://www.webservicex.net/globalweather.asmx/GetWeather?CityName=" 
                          + ciudad + "&CountryName="+ pais);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
           
            System.out.println("GET Response Status: " + httpResponse.getStatusLine().getStatusCode());
           
            StringBuilder response;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return response.toString();
            }
            else {
                return "Error.";
            }
            
        } catch (IOException ex) {
            Logger.getLogger(WebServiceRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(null, "Se ha presentado un error al acceder al servicio web.");
        return null;
    }
    
}