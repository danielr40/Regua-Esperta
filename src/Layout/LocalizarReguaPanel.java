package Layout;

import static Layout.Video.videoAtual;
import java.awt.Graphics;  
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;  
import java.awt.event.MouseListener;  
import java.awt.event.MouseMotionListener;  
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;  
  
  
public class LocalizarReguaPanel extends JPanel implements MouseListener, MouseMotionListener {  
 
    //String imagem = Video.getImagem(640,360);
    
    public LocalizarReguaPanel(){;
//Construtor  
        this.addMouseListener(this);//adicona o ouvinte do mouse ... ao frame, isso sempre é necessário, pra pegar as coordenadas  
        this.addMouseMotionListener(this);  
        this.setVisible(true);//muda o frame pra visivel   
    }  
          
    public static int x0, y0, xf, yf;  
          
    public void mouseClicked(MouseEvent arg0) {  
    }  
  
    public void mouseEntered(MouseEvent arg0) {  
    }  
  
    public void mouseExited(MouseEvent arg0) {  
    }  
  
    public void mousePressed(MouseEvent arg0) {  
        x0 = arg0.getX();//Pegam o X e Y inicial do mouse  
        y0 = arg0.getY(); 
    }  
  
    public void mouseReleased(MouseEvent arg0) { 
        xf = arg0.getX();//Pegam o X e Y inicial do mouse  
        yf = arg0.getY(); 
        repaint();
    }  
  
    public void mouseDragged(MouseEvent arg0) {  
        // ao arrasstar o mouse, ele fica redesenhando   
        xf = arg0.getX();  
        yf = arg0.getY();  
        repaint(); //conforme vai arrastando.... ele fica repintando   
    }  
  
    public void mouseMoved(MouseEvent arg0) {  
    }  
      
    public void paint (Graphics g){  
        Graphics2D g2 = (Graphics2D) g; 
        g.clearRect(0, 0, this.getWidth(),this.getHeight());//Limpa o frame, pra num ficar uma forma por cima da outra....  
        Shape r = makeRectangle(x0,y0,xf,yf);
        /*
        Image img1 = Toolkit.getDefaultToolkit().getImage(imagem);
        g2.drawImage(img1, 0, 0, this);
                */
        g2.draw(r);  
    }  
    
    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
      return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
  
public static void main(String args[]){  
    LocalizarReguaPanel panel = new LocalizarReguaPanel();  
}  
}//End class  