/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reconhecimento;

import Layout.TelaPrincipal;
import Layout.Video;
import Layout.TelaSegmentarCirculo;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;

/**
 *
 */
public class Circulo {

    public static void segmentarCirculo(int minRaio, int maxRaio) {

        class threadSegmentar extends Thread {
            
            public boolean closed = false; 
            public double CentroX; 
            public double CentroY; 
            
            @Override
            public void run() {
                int contador = 0;
                
                File folder = new File("imagens/frames");
                for (String file : folder.list()) {
                    new File(folder, file).delete();
                }

                JFrame frame = new JFrame();
                JLabel label = new JLabel();
                frame.add(label);
                frame.setBounds(10, 10, 640, 480);
                label.setSize(640, 480);
                frame.setVisible(true);
                closed = false; 
                
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        closed = true; 
                    }
                });

                Mat img = new Mat();
                Mat circles = new Mat();
                Mat grayImg = new Mat();
                Mat gravar = new Mat();
                Mat element = new Mat();

                VideoCapture cap = new VideoCapture(Video.videoAtual);

                // capturar primeiro frame do video 
                cap.read(img);
                
                Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);
                
                Imgproc.medianBlur(grayImg, grayImg, 5);
                
                Imgproc.HoughCircles(grayImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 100,
                            220, 10, minRaio, maxRaio);
                
                double Circle[] = circles.get(0, 0);

                Point center = new Point(Math.round(Circle[0]), Math.round(Circle[1]));
                
                int radius = (int) Math.round(Circle[2]);
                
                CentroX = center.x; 
                CentroY = center.y; 
                
                cap.read(img);
                
                boolean continuar = true;

                while (continuar) {

                    // passar a imagem para tons de cinza
                    Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);

                    Imgproc.threshold(grayImg, grayImg, 190, 255, THRESH_BINARY_INV);

                    Core.bitwise_not(grayImg, grayImg);

                    Imgproc.medianBlur(grayImg, grayImg, 5);

                    Imgproc.Canny(grayImg, grayImg, 100, 255);

                    Highgui.imwrite("imagens/threshold_inv.jpg", grayImg);

                    // aplicar transformada circular de hough
                    Imgproc.HoughCircles(grayImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 100,
                            220, 9, minRaio, maxRaio);

                    try {
                        for (int x = 0; x <circles.cols(); x++) {
                            double vCircle[] = circles.get(0, x);

                            center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
                            radius = (int) Math.round(vCircle[2]);

                            // draw the circle outline
                            if(((center.x <= CentroX) || (center.x - CentroX <= 5)) && 
                                    (Math.sqrt(CentroX*CentroX + CentroY*CentroY) - 
                                    Math.sqrt(center.x*center.x + center.y*center.y)<=70.0)
                            && (Math.sqrt(CentroX*CentroX + CentroY*CentroY) - 
                                    Math.sqrt(center.x*center.x + center.y*center.y)>=-70.0)){
                            
                                    Core.circle(img, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
                                    
                                    CentroX = center.x;
                                    CentroY = center.y; 
                            }
                        }
                    } catch (Exception e) {
                    }

                    Imgproc.resize(img, gravar, new Size(640, 480));
                    Highgui.imwrite("imagens/frames/houghcircles" + contador + ".jpg", gravar);

                    label.setIcon(new ImageIcon("imagens/frames/houghcircles" + contador + ".jpg"));

                    contador++;

                    continuar = cap.read(img) && !closed;
                }
            }
        }

        if (Video.videoAtual == null) {
            JOptionPane.showMessageDialog(null, "Selecione um arquivo de video!",
                    "Nenhum vídeo selecionado", JOptionPane.WARNING_MESSAGE);
            Video.abrirVideo();
        }
        
        threadSegmentar t = new threadSegmentar();
        t.start();
    }
}
