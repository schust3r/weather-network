
import gui.Splash;
import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Clase main del programa
 * @author Joel
 */
public class Main {
    
    public static void main(String args[]) {
        
        // TODO CODE HERE
        File fileTemp = new File("paises.db");
        if (fileTemp.exists()) { 
            fileTemp.delete(); 
        }
        Splash splash = new Splash();
    }
}
