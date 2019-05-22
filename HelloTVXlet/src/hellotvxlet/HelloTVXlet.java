package hellotvxlet;

import javax.tv.xlet.*;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;
import java.awt.Color;
import org.havi.ui.*;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;


public class HelloTVXlet implements Xlet {
    
//menu knoppen
  HTextButton btn1;
  HTextButton btn2;
 //background
  //spel scene
  private HScene gameScene;
  
    public HelloTVXlet() {
        
    }

    public void initXlet(XletContext ctx) throws XletStateChangeException{
     //menu init
      gameScene = HSceneFactory.getInstance().getDefaultHScene();
      
      //titel
      HStaticText hst = new HStaticText("Mastermind",25,25,700,100);
      hst.setBackgroundMode(HVisible.BACKGROUND_FILL);
      hst.setBackground(Color.BLACK);
      gameScene.add(hst);
      //menu knoppen 
      //start knop
     btn1 = new HTextButton("START",250,150,250,100);
     btn1.setBackgroundMode(HVisible.BACKGROUND_FILL);
     btn1.setBackground(Color.BLACK);
     //exit knop
    btn2 = new HTextButton("EXIT",250,275,250,100);
    btn2.setBackgroundMode(HVisible.BACKGROUND_FILL);
    btn2.setBackground(Color.BLACK);
    //toevoegen aan scene
    gameScene.add(btn1);
    gameScene.add(btn2);
    //schakelen tussen knoppen
    btn1.setFocusTraversal(null, btn2, null, null);
    btn2.setFocusTraversal(btn1, null, null, null);
    //focus op knop 1 bij start
    btn1.requestFocus();
    }

    public void startXlet() throws XletStateChangeException{
    
    gameScene.validate();
    gameScene.setVisible(true);
    }

    public void pauseXlet() {
     
    }

    public void destroyXlet(boolean unconditional) throws XletStateChangeException{
     
    }
}
