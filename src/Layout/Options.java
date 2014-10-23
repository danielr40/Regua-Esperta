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
import org.opencv.highgui.VideoCapture;

/**
 * Contém as configuracoes de vídeo e processamento, escolhidas pelo usuário
 */
public class Options extends JFrame1 {

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
            exibirMensagemSaida("Novo video: " + videoAtual);

            Mat img = new Mat();
            VideoCapture capture = new VideoCapture();
            capture.open(videoAtual);
            capture.read(img);
            
            exibirImagemTelaPrincipal(img);
            

        } else {
            JOptionPane.showMessageDialog(fileChooser, "Selecione um arquivo de vídeo válido!",
                    "Arquivo inválido", JOptionPane.WARNING_MESSAGE);
        }
    }
}
