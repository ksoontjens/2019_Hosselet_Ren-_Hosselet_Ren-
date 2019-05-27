package hellotvxlet;

import javax.tv.xlet.*;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;
import java.awt.Color;
import java.awt.Image;
import org.havi.ui.*;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
import org.havi.ui.event.HActionListener;
import org.havi.ui.event.HRcEvent;
import org.dvb.event.UserEvent;
import org.dvb.event.UserEventListener;
import org.dvb.event.EventManager;
import org.dvb.event.UserEventRepository;

/**
 *
 * @author René Hosselet
 */
public class HelloTVXlet extends HComponent implements Xlet {
        //scene
    HScene gameScene; 
 //board
    int bRows = 10;
    int bColumns = 4;
    int aImages = 9;
    int maxTiles = bRows * bColumns; 
    int tSize = 45;
    int bIndex;
    int bIndexCode;
    int bPegs;
    int tX,tY;
    //images
    Image background;
    Image images[] = new Image[aImages];    
    Pegs[] pegs = {
        new Pegs(0,"images/0.png"),
        new Pegs(1,"images/1.png"),
        new Pegs(2,"images/2.png"),
        new Pegs(3,"images/3.png"),
        new Pegs(4,"images/4.png"),
        new Pegs(5,"images/5.png"),
        new Pegs(6,"images/6.png"),
        new Pegs(7,"images/7.png"),
        new Pegs(8,"images/8.png")
    };
    //tegels
    HGraphicButton bTiles[] = new HGraphicButton[maxTiles];
    
    
  
  class Pegs 
    {
        int number;
        String imgString;        
        public Pegs(int number, String imgString)        {
            this.number = number;
            this.imgString = imgString;
        }      
    }
    public HelloTVXlet() {
        
    }
    public void initXlet(XletContext ctx) throws XletStateChangeException{
     //scene init
     gameScene = HSceneFactory.getInstance().getDefaultHScene();  
     //gameScene.setBackgroundMode(HVisible.BACKGROUND_FILL);
     //gameScene.setBackground(Color.GRAY);
     background = this.getToolkit().getImage("images/test.jpg");
     gameScene.setBackgroundImage(background);
     gameScene.setRenderMode(HScene.IMAGE_CENTER);
      //init
     tY=400;
     //de te kiezen pegs
     for (int i = 0;i<2;i++){
          tX=500;
          for (int j = 0;j<bColumns;j++){
              
              bTiles[bPegs] = new Peg(tX, tY, pegs[bPegs].imgString, pegs[bPegs].number);
              gameScene.add(bTiles[bPegs]);
              tX += tSize;       
              bPegs++;
          }
          tY += tSize;
     }
     //boardset array
      tY=100;
     for (int i = 0;i<bRows;i++){
          tX=50;
          for (int j = 0;j<bColumns;j++){
              
              bTiles[bIndex] = new Peg(tX, tY, pegs[bIndex].imgString, pegs[bIndex].number);
              gameScene.add(bTiles[bIndex]);
              tX += tSize;              
          }
          tY += tSize;
     }
     //de verborgen code boardset
     tX = 50;
     tY = 35;
      for (int i = 0;i<bColumns;i++){          
              bTiles[bIndexCode] = new Peg(tX, tY, pegs[bIndexCode].imgString, pegs[bIndexCode].number);
              gameScene.add(bTiles[bIndexCode]);
              tX += tSize;
              bIndex++;
          }     
      //text peg colors
      HStaticText hst = new HStaticText("Peg Colors",500,350,180,50);
      hst.setBackgroundMode(HVisible.BACKGROUND_FILL);
      hst.setBackground(Color.BLACK);
      gameScene.add(hst);

      gameScene.validate();
      gameScene.setVisible(true);
    }

    public void startXlet() throws XletStateChangeException{
    
    
    }

    public void pauseXlet() {
     
    }

    public void destroyXlet(boolean unconditional) throws XletStateChangeException{
     
    }
}
