/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reconhecimento;

import Layout.TelaSegmentarRegua;
import Layout.TelaSegmentarRegua2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
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
        
        long tempoInicio = System.currentTimeMillis();
        
        // coordenadas do retangulo de selecao 
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
        // cortar imagem de acordo com a selecao
        Mat img = new Mat(bigImage,new Rect(x0,y0,x-x0,y-y0));
        
        Mat grayImg = new Mat();
        // passar imagem para tons de cinza
        Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);
        // limiarizacao 
        Imgproc.threshold(grayImg, grayImg, 190, 255, THRESH_BINARY_INV);
        Core.bitwise_not(grayImg, grayImg);
        
        List <Point> pontos = new ArrayList<Point>(); 
        
        // adicionar todos os pontos da referentes a regua em um vetor
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
        
        String filename = "imagens/regua_segmentada" + Math.random()*1000 + ".jpg";
        
        Mat img2 = new Mat();
        Imgproc.resize(img, img2, new Size (img.size().width*3.0,img.size().height*3.0));
        Highgui.imwrite(filename,img2);
        
        int xMin = 5000, yMin = 5000; 
        int xMax = 0, yMax = 0;
    
        // pontos extremos da regua
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
        
        // regua na posicao horizontal
        if(xMax - xMin > yMax - yMin)
        {
            /*
            a proporcao da imagem utilizada no processamento torna necessario
            a multiplicacao por 2 para manter a proporcao das medidas 
            */
            larguraPixels = (xMax - xMin)*2; 
        }
        // regua na posicao vertical
        else 
        {
            larguraPixels = (yMax - yMin)*2; 
        }
        
        long tempoFim = System.currentTimeMillis() - tempoInicio;

        centimetrosPorPixel = 30.0/larguraPixels;
        
        TelaSegmentarRegua2 telaResposta = new TelaSegmentarRegua2();
        telaResposta.jLabel1.setIcon(new ImageIcon(filename));
        telaResposta.jLabel4.setText(larguraPixels + " pixels");
        telaResposta.jLabel5.setText(String.valueOf(centimetrosPorPixel).substring(0, 5));
        telaResposta.jLabel7.setText(tempoFim + " ms");
        telaResposta.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        telaResposta.setLocation(200, 200);
        telaResposta.setVisible(true);
        
    }
}
