/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Layout.TelaPrincipal;
import java.io.File;
import javax.swing.WindowConstants;
import org.opencv.core.Core;

/**
 *
 * @author Daniel
 */
public class ReguaEsperta {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        File folder = new File("imagens");

        if (folder.exists() == false) {
            folder.mkdir();
        }
        for (String file : folder.list()) {
            new File(folder, file).delete();
        }
        folder = new File("imagens/frames");
        if (folder.exists() == false) {
            folder.mkdir();
        }
        for (String file : folder.list()) {
            new File(folder, file).delete();
        }
        
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        TelaPrincipal frame = new TelaPrincipal();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocation(70,70);
        frame.setVisible(true);
    }
    
}
