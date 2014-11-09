/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reconhecimento;

import Layout.TelaMedirDistSegmentacaoCirculo;
import Layout.Video;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

/**
 *
 */
public class SaltoSegmentacaoCirculo {

    public static double extensao; 
    
    private static Point segmentarCirculo(Mat img) {
        Mat grayImg = new Mat();
        Mat circles = new Mat();
        Mat element = new Mat();

        Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);

        // filtro da mediana
        Imgproc.medianBlur(grayImg, grayImg, 5);

        // transformada circular de hough
        Imgproc.HoughCircles(grayImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 100,
                220, 17, 5, 10);

        Point centro = new Point(0,0); 
        Point center; 
        
        for (int x = 0; x < circles.cols(); x++) {
            double vCircle[] = circles.get(0, x);

            center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
            
            // pegar o circulo mais embaixo
            if(centro.y<center.y)
            {
                centro = center.clone();
            }
            
            int radius = (int) Math.round(vCircle[2]);

            Core.circle(img, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
        }
        return centro;
    }

    public static void medirExtensao() {
        
        long tempoInicial = System.currentTimeMillis();
        
        Mat img = new Mat();
        Mat img2 = new Mat();

        VideoCapture cap = new VideoCapture(Video.videoAtual);

        // capturar primeiro frame do video
        cap.read(img);

        // capturar ultimo frame do video
        boolean continuar = true;
        Mat aux = new Mat();
        while (continuar) {
            cap.read(aux);

            if (aux.size().area() > 0) {
                img2 = aux.clone();
            } else {
                continuar = false;
            }
        }
        
        Point centroCirculoInicial = segmentarCirculo(img);
        Point centroCirculoFinal = segmentarCirculo(img2);
        
        // extensao do salto em centimetros
        extensao = (centroCirculoInicial.x - centroCirculoFinal.x)*Regua.centimetrosPorPixel;
        // converter extensao do salto para metros
        extensao = extensao/100.0;
        
        System.out.println(String.valueOf(extensao).substring(0,4) + " m");

        Mat show1 = new Mat();
        Mat show2 = new Mat();
        Imgproc.resize(img, show1, new Size(420, 240));
        Imgproc.resize(img2, show2, new Size(420, 240));
        Highgui.imwrite("imagens/" + Video.nomeVideo + 420 + "x" + 2400 + ".jpg", show1);
        Highgui.imwrite("imagens/" + Video.nomeVideo + 420 + "x" + 240 + "u.jpg", show2);

        TelaMedirDistSegmentacaoCirculo tela = new TelaMedirDistSegmentacaoCirculo();
        tela.setLocation(200, 200);
        tela.jLabel1.setIcon(new ImageIcon("imagens/" + Video.nomeVideo + 420 + "x" + 2400 + ".jpg"));
        tela.jLabel2.setIcon(new ImageIcon("imagens/" + Video.nomeVideo + 420 + "x" + 240 + "u.jpg"));
        tela.jLabel4.setText(String.valueOf(extensao).substring(0,4) + " m");
        tela.jLabel6.setText(String.valueOf(System.currentTimeMillis()-tempoInicial) + " ms");
        tela.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        tela.setVisible(true);
    }
}
