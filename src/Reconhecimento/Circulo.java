/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reconhecimento;

import Layout.JFrame1;
import Layout.Video;
import Layout.SegmentarCirculoFrame;
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

            public void run() {

                int contador = 0;

                if (Video.videoAtual == null) {
                    JOptionPane.showMessageDialog(null, "Selecione um arquivo de video!",
                            "Nenhum vídeo selecionado", JOptionPane.WARNING_MESSAGE);
                    Video.abrirVideo();
                }

                JFrame frame = new JFrame();
                JLabel label = new JLabel();
                frame.add(label);
                frame.setBounds(10, 10, 640, 480);
                label.setSize(640, 480);
                frame.setVisible(true);

                Mat img = new Mat();
                Mat circles = new Mat();
                Mat grayImg = new Mat();
                Mat gravar = new Mat();
                Mat element = new Mat();
                Mat invertcolormatrix= new Mat();

                int erosion_type = Imgproc.MORPH_CROSS;
                int erosion_size = 1; 
                
                VideoCapture cap = new VideoCapture(Video.videoAtual);

                // capturar primeiro frame do video 
                cap.read(img);

                boolean continuar = true;

                while (continuar) {

                    // passar a imagem para tons de cinza
                    Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);
                    
                    Imgproc.equalizeHist(grayImg, grayImg);

                    Imgproc.threshold(grayImg, grayImg, 220, 255, THRESH_BINARY_INV);
                    
                    invertcolormatrix= new Mat(grayImg.rows(),grayImg.cols(), grayImg.type(), new Scalar(255,255,255));
                    
                    Core.subtract(invertcolormatrix, grayImg, grayImg);
                    
                    element = Imgproc.getStructuringElement(erosion_type,new Size(2*erosion_size + 1,
                    2*erosion_size+1),new Point(erosion_size,erosion_size));
                    
                    Imgproc.medianBlur(grayImg, grayImg, 5);
                    
                    Imgproc.erode(grayImg, grayImg, element);
                    
                    Imgproc.Canny(grayImg, grayImg, 250, 255);
                    
                    Highgui.imwrite("imagens/threshold_inv.jpg", grayImg);
                    
                    // aplicar transformada circular de hough
                    Imgproc.HoughCircles(grayImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 100,
                            180, 10, minRaio, maxRaio);

                    try {
                        for (int x = 0; x < circles.cols(); x++) {
                            double vCircle[] = circles.get(0, x);

                            Point center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
                            int radius = (int) Math.round(vCircle[2]);

                            // draw the circle outline
                            Core.circle(img, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
                        }
                    } catch (Exception e) {
                    }

                    Imgproc.resize(img, gravar, new Size(640, 480));
                    Highgui.imwrite("imagens/frames/houghcircles" + contador + ".jpg", gravar);

                    label.setIcon(new ImageIcon("imagens/frames/houghcircles" + contador + ".jpg"));

                    contador++;

                    continuar = cap.read(img);
                }
            }
        }
        threadSegmentar t = new threadSegmentar();
        t.start();

    }
}
