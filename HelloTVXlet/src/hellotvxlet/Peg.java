package hellotvxlet;

import java.awt.Color;
import org.havi.ui.HTextButton;
import org.havi.ui.HVisible;
/**
 *
 * @author René Hosselet
 */
public class Peg extends HTextButton
{
    int number, size;
    
    public Peg(int x, int y, int number) {
        this.number = number;
        this.size = 45;
        this.setLocation(x, y);
        this.setSize(size, size);
        this.setBackgroundMode(HVisible.BACKGROUND_FILL);
        switch(number)
        {
            case 0:
                this.setBackground(Color.WHITE);
                break;
            case 1:
                this.setBackground(Color.RED);
                break;
            case 2:
                this.setBackground(Color.BLUE);
                break;
            case 3:
                this.setBackground(Color.GREEN);
                break;
            case 4:
                this.setBackground(Color.YELLOW);
                break;  
            case 5:
                this.setBackground(Color.ORANGE);
                break;
            case 6:
                this.setBackground(Color.CYAN);
                break; 
            case 7:
                this.setBackground(Color.PINK);
                break; 
            case 8:
                this.setBackground(Color.BLACK);
                break; 
        }
        
    }
}
