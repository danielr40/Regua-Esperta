/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reconhecimento;

import Layout.LocalizarReguaPanel;
import Layout.TelaSegmentarRegua;
import Layout.Video;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;

/**
 *
 */
public class Regua {

    public static void segmentarRegua() {
        
        int x0 = TelaSegmentarRegua.localizarReguaPanel1.x0;
        int y0 = TelaSegmentarRegua.localizarReguaPanel1.y0;
        int x = TelaSegmentarRegua.localizarReguaPanel1.xf;
        int y = TelaSegmentarRegua.localizarReguaPanel1.yf;
        
        if(x0>x)
        { 
            int aux = x0;
            x0 = x;
            x = aux;
        }
        
        if(y0>y)
        { 
            int aux = y0;
            y0 = y;
            y = aux;
        }
        
        System.out.println(x0 + " " + y0 + " " + x + " " + y);
    }
}
