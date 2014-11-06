/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Layout;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

/**
 * Contém as configuracoes de vídeo, escolhidas pelo usuário
 */
public class Video{

    /**
     * Arquivo de vídeo que está sendo processado pelo programa
     */
    public static String videoAtual;

    /**
     * Seleciona novo arquivo de vídeo para ser processado
     */
    public static void abrirVideo() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(fileChooser);

        FileFilter filterAvi = new FileNameExtensionFilter("AVI",
                "avi");

        fileChooser.addChoosableFileFilter(filterAvi);
        File novoVideo = fileChooser.getSelectedFile();
        if (filterAvi.accept(novoVideo)) {
            videoAtual = novoVideo.getPath();
            TelaPrincipal.exibirMensagemSaida("Novo video: " + videoAtual);

            Mat img = new Mat();
            VideoCapture capture = new VideoCapture();
            capture.open(videoAtual);
            capture.read(img);
          
            TelaPrincipal.exibirImagemTelaPrincipal(img,videoAtual.substring(videoAtual.lastIndexOf("\\")+1,
                    videoAtual.indexOf("."))+".jpg");
            

        } else {
            JOptionPane.showMessageDialog(fileChooser, "Selecione um arquivo de vídeo válido!",
                    "Arquivo inválido", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Escreve o primeiro frame do video em um arquivo de imagem e retorna seu endereco
     * @param comprimento - comprimento da imagem
     * @param altura - altura da imagem
     * @return - endereco do arquivo de imagem escrito 
     */
    public static String getImagem (int x, int y)
    {
        if(videoAtual.isEmpty()==false)
        {
            Mat img = new Mat();

            VideoCapture cap = new VideoCapture(Video.videoAtual);
            cap.read(img);

            Imgproc.resize(img, img, new Size(x, y));
            String filename = "imagens/" + videoAtual.substring(videoAtual.lastIndexOf("\\")+1,
                        videoAtual.indexOf("."))+ x + "x" + y + ".jpg";

            Highgui.imwrite(filename, img);
            return filename; 
        }
        else
        {
            return "";
        }
    }

    static class videoAtual {

        public videoAtual() {
        }
    }
}
