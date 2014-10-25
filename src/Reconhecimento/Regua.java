/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reconhecimento;

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
 * @author Daniel
 */
public class Regua {

    public static void segmentarRegua(double rho, double theta, int limiar) {
        if (Video.videoAtual == null) {
            JOptionPane.showMessageDialog(null, "Selecione um arquivo de video!",
                    "Nenhum vídeo selecionado", JOptionPane.WARNING_MESSAGE);
            Video.abrirVideo();
        }

        Mat img = new Mat();

        // capturar primeiro frame do video 
        VideoCapture cap = new VideoCapture(Video.videoAtual);
        cap.read(img);
      
        Mat edges = new Mat();
        Mat thr = new Mat();
        
        //Imgproc.medianBlur(img,img, 5);
        
        Imgproc.threshold(img, thr, 250, 255, THRESH_BINARY_INV);
        
        Highgui.imwrite("imagens/threshold.jpg",thr);
        
        Imgproc.Canny(thr, edges, 250, 255);
        
        Highgui.imwrite("imagens/edges.jpg", edges);
        
        Imgproc.cvtColor(thr, thr, Imgproc.COLOR_BGR2GRAY);
        
        // transformada de hough 
        Mat lines = new Mat(); 
        Imgproc.HoughLinesP(thr,lines,rho,theta,limiar,1,2);

        Scalar color = new Scalar(0, 0, 255);

        double[] data;
        Point pt1 = new Point();
        Point pt2 = new Point();
        double a, b;
        double x0, y0;
        for (int i = 0; i < lines.cols(); i++) {
            data = lines.get(0, i);
            rho = data[0];
            theta = data[1];
            a = Math.cos(theta);
            b = Math.sin(theta);
            x0 = a * rho;
            y0 = b * rho;
            pt1.x = Math.round(x0 + 1000 * (-b));
            pt1.y = Math.round(y0 + 1000 * a);
            pt2.x = Math.round(x0 - 1000 * (-b));
            pt2.y = Math.round(y0 - 1000 * a);
            Core.line(img, pt1, pt2, color, 3);
        }
        
        Highgui.imwrite("imagens/houghlines.jpg", img);
    }
}
