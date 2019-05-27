package hellotvxlet;

import java.awt.Color;
import java.awt.Image;
import java.awt.MediaTracker;
import org.havi.ui.HGraphicButton;
import org.havi.ui.HVisible;
import org.havi.ui.HState;
/**
 *
 * @author René Hosselet
 */
public class Peg extends HGraphicButton
{
    Image image;    
    int number, size;
    
    public Peg(int x, int y, String imgstring, int number) {
        MediaTracker mt= new MediaTracker(this);
        this.number = number;
        this.size = 45;
        this.setLocation(x, y);
        this.setSize(size, size);
        this.setBackground(Color.WHITE);
        this.setBackgroundMode(HVisible.BACKGROUND_FILL);
        this.setForeground(Color.BLACK);
        image = this.getToolkit().getImage(imgstring);
        mt.addImage(image, 1);
           try {
            mt.waitForAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.setGraphicContent(image, HState.NORMAL_STATE);      
    }
}
