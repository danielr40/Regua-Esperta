/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Layout.TelaPrincipal;
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
        
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        TelaPrincipal frame = new TelaPrincipal();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
}
