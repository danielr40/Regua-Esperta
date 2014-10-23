/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Reconhecimento;

import Layout.Options;
import Layout.SegmentarCirculoFrame;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
public class Circulo {
    
    /**
     * coordenadas do circulo segmentado
     */
    public static Point Centro;
    /**
     * raio do circulo segmentado
     */
    public static int Raio; 
    
    public static void segmentarCirculo(int minRaio, int maxRaio)
    {
        if(Options.videoAtual==null)
        {
            JOptionPane.showMessageDialog(null, "Selecione um arquivo de video!", 
                    "Nenhum vídeo selecionado",JOptionPane.WARNING_MESSAGE);
            Options.abrirVideo();
        }
        
        Mat img = new Mat();
        
        // capturar primeiro frame do video 
        VideoCapture cap = new VideoCapture(Options.videoAtual);
        cap.read(img);
        
        // passar a imagem para tons de cinza
        Mat grayImg = new Mat();
        Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);
        
        // aplicar filtro da media para reduzir os ruidos
        Imgproc.medianBlur(grayImg, grayImg, 7);
        Highgui.imwrite("tmp/img1.jpg", grayImg);
        
        // aplicar transformada circular de hough
        Mat circles = new Mat();
        Imgproc.HoughCircles(grayImg, circles, Imgproc.CV_HOUGH_GRADIENT,1,20, 
                180,18,minRaio,maxRaio);
        
        for (int x = 0; x < circles.cols(); x++) {
            double vCircle[] = circles.get(0, x);

            Point center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
            int radius = (int) Math.round(vCircle[2]);
            
            Centro = center;
            Raio = radius;
            
            // draw the circle outline
            Core.circle(img, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
        }
        
        Highgui.imwrite("houghcircles.jpg", img);
        JFrame frame = new JFrame();
        JLabel label = new JLabel();
        frame.add(label);
        Imgproc.resize(img, img, new Size(640,480));
        Highgui.imwrite("houghcircles1.jpg", img);
        frame.setBounds(10,10,640,480);
        label.setSize(640,480);
        label.setIcon(new ImageIcon("houghcircles1.jpg"));
        frame.setVisible(true);
        
        System.out.println(Centro.toString());
        System.out.println(Raio);
    }
}
