/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reconhecimento;

import Layout.TelaSegmentarRegua;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
/**
 *
 */
public class Regua {
    
    public static int larguraPixels = 0; 
    public static double centimetrosPorPixel = 0;

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
        
        Mat bigImage = Highgui.imread(TelaSegmentarRegua.localizarReguaPanel1.imagem);
        Mat img = new Mat(bigImage,new Rect(x0,y0,x-x0,y-y0));
        
        Mat grayImg = new Mat();
        Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.medianBlur(grayImg, grayImg, 3);
        Imgproc.threshold(grayImg, grayImg, 190, 255, THRESH_BINARY_INV);
        Core.bitwise_not(grayImg, grayImg);
        
        List <Point> pontos = new ArrayList<Point>(); 
        
        for(int i = 0; i<grayImg.rows(); i++)
        {
            for(int j = 0; j<grayImg.cols();j++)
            {
                if(Arrays.toString(grayImg.get(i,j)).equals("[255.0]"))
                {
                    pontos.add(new Point(j,i));
                    Core.line(img, new Point(j,i), new Point(j,i), new Scalar(255,0,0));
                }
            }
        }
        
        int xMin = 5000, yMin = 5000; 
        int xMax = 0, yMax = 0;
    
        for (Point ponto : pontos) {
            if (ponto.x > xMax) {
                xMax = (int) ponto.x;
            }
            if (ponto.x < xMin) {
                xMin = (int) ponto.x;
            }
            if (ponto.y > yMax) {
                yMax = (int) ponto.y;
            }
            if (ponto.y < yMin) {
                yMin = (int) ponto.y;
            }
        }
        
        if(xMax - xMin > yMax - yMin)
        {
            /*
            a proporcao da imagem utilizada no processamento torna necessario
            a multiplicacao por 2 para manter a proporcao das medidas 
            */
            larguraPixels = (xMax - xMin)*2; 
        }
        else 
        {
            larguraPixels = (yMax - yMin)*2; 
        }
        
        centimetrosPorPixel = 30.0/larguraPixels;
        
        System.out.println(larguraPixels + " pixels");
        System.out.println(centimetrosPorPixel);
    }
}
