package hellotvxlet;

import javax.tv.xlet.*;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.util.Collections;
import java.util.Random;
import org.havi.ui.*;
import java.awt.event.*;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
import java.awt.MediaTracker;
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
public class HelloTVXlet extends HComponent implements Xlet,UserEventListener {
        //scene
    HScene gameScene; 
      //random
    int randomCode[] = new int[4];
    Random random = new Random();
 //board
    int rowCounter = 1;
    int bRows = 10;
    int bColumns = 4;
    int aImages = 9;
    int maxTiles = bRows * bColumns; 
    int tSize = 45;
    int bIndex,bIndexCode,bPegs,bEmptyIndex,tX,tY;
    //images
    Image background;
    MediaTracker mtrack;
    Image images[] = new Image[aImages];    
   
    //tegels
    HTextButton bTiles[] = new HTextButton[maxTiles];
    HTextButton bTilesCode[] = new HTextButton[bColumns];
    HTextButton bTilesPegs[] = new HTextButton[aImages];
    HStaticText board;
    public HelloTVXlet() {
    }
    public void initXlet(XletContext ctx) throws XletStateChangeException{
     //scene init
     gameScene = HSceneFactory.getInstance().getDefaultHScene();  
     gameScene.setBackgroundMode(HVisible.BACKGROUND_FILL);
     gameScene.setBackground(Color.GRAY);
     //event knoppen
        UserEventRepository repo = new UserEventRepository("vb");     
        EventManager manager = EventManager.getInstance();
        //events toev
        repo.addAllNumericKeys();
        repo.addKey(HRcEvent.VK_ENTER);
        repo.addAllArrowKeys();
        //add bij Emanager
        manager.addUserEventListener(this, repo);
        
      //init
     tY=400;
     
     //text peg colors
      HStaticText hst = new HStaticText("Peg Colors",500,350,180,50);
      hst.setBackgroundMode(HVisible.BACKGROUND_FILL);
      hst.setBackground(Color.BLACK);
      gameScene.add(hst);
     //de te kiezen pegs
     bPegs =1;
     for (int i = 0;i<2;i++){
          tX=500;
          for (int j = 0;j<bColumns;j++){
              bTilesPegs[bPegs] = new Peg(tX, tY, bPegs);
              gameScene.add(bTilesPegs[bPegs]);
              tX += tSize;       
              bPegs++;
          }
          tY += tSize;
     }     
     //de verborgen code boardset
     tX = 50;
     tY = 35;
      for (int i = 0;i<bColumns;i++){          
              bTilesCode[i] = new Peg(tX, tY, randomizer(i));
              gameScene.add(bTilesCode[i]);
              bIndexCode++;
              tX += tSize;
          }     
     //boardset array
      tY=100;
     for (int i = 0;i<bRows;i++){
          tX=50;
          for (int j = 0;j<bColumns;j++){
              board = new HStaticText("",tX,tY,tSize,tSize);
              board.setBackgroundMode(HVisible.BACKGROUND_FILL);
              board.setBackground(Color.LIGHT_GRAY);  
              //gameScene.add(board);
              tX += tSize;              
          }
          tY += tSize;          
     }      
      newRow();
      
      gameScene.validate();
      gameScene.setVisible(true);
    } 
    public int randomizer(int index)
    {      
        //random code
     for (int i = 0;i < randomCode.length ;i++)
     {
         randomCode[i] = random.nextInt(8)+1;
     }     
     return randomCode[index];
    }
    public void newRow()
    {
        tY -= tSize;
        tX=50;
        //maakt rij aan
        for (int i = 0 ; i<bColumns;i++)
        {
             bTiles[i] = new Peg(tX, tY, 0);
             gameScene.add(bTiles[i]);
             bIndex++;
             tX += tSize;             
        }
        //zorgt voor focus en traversal tussen vakken
        for (int j = 0 ; j<bColumns;j++)
        {
            renewTraversal(j);          
        }
    }
    public int checkArray(int i , int lr)
    {
        //kijkt of focus op het begin of einde van de array staat
        if (i == 0 & lr == 0)
        {
            return 0;
        }
        else if (i == 3 & lr == 1)
        {
            return 0;
        }
        else{
            return 1;
        }
    }
    public void userEventReceived(UserEvent e)
    {
        if(e.getType()== KeyEvent.KEY_PRESSED)
        {
            switch (e.getCode())
            {
                case HRcEvent.VK_ENTER:
                    if (rowCounter != 10)
                    {
                          checkRightCode();
                            newRow();
                            rowCounter ++;                      
                    }                  
                    break;
                    case HRcEvent.VK_1:       
                    checkFocus(1);
                    break;
                    case HRcEvent.VK_2:       
                    checkFocus(2);
                    break;
                    case HRcEvent.VK_3:       
                    checkFocus(3);
                    break;
                    case HRcEvent.VK_4:       
                    checkFocus(4);
                    break;
                    case HRcEvent.VK_5:       
                    checkFocus(5);
                    break;
                    case HRcEvent.VK_6:       
                    checkFocus(6);
                    break;
                    case HRcEvent.VK_7:       
                    checkFocus(7);
                    break;
                    case HRcEvent.VK_8:       
                    checkFocus(8);
                    break;
            }
        }
    }
    //methode die kijkt of gegeven rij juist is en of hoeveel er juist zijn 
    public void checkRightCode()
    {
        //temp checkers
        int[] tempCode = new int[bColumns];
        int[] tempGuess = new int[bColumns];
        
        int rGuess = 0;
        int rColor = 0;
        
        //kijkt of overeenkomende arrays volledig hetzelfde zijn
        for (int i = 0 ; i<bColumns;i++)
        {
            if (bTilesCode[i].getBackground() == bTiles[i].getBackground())
                {
                   rGuess ++;
                   tempCode[i] = -1;
                   tempGuess[i] = -1;
                }
            }
        //kijkt voor aantal zelfde kleuren
       for (int j = 0;j<bColumns;j++)
            {
           if(tempCode[j]==-1)
           {
               continue;
           }
            for (int k = 0;k<bColumns;k++)
            {
                if(tempGuess[k]==-1)
                {
                    continue;
                }
           if (bTilesCode[j].getBackground() == bTiles[k].getBackground())
               rColor ++;
                tempCode[j]=-1;
                tempGuess[k]=-1;
            }
        }
        System.out.println(rGuess);
        System.out.println(rColor);
    }
    public void renewTraversal(int index)
    {
        for (int i = 0;i<bColumns;i++)
        {
        bTiles[i].setFocusTraversal(null, null, bTiles[i-checkArray(i,0)], bTiles[i+checkArray(i,1)]);
        }
        bTiles[index].requestFocus();
    }
    //methode die kijkt welk component de focus heeft en maakt een nieuw object aan met de meegegeven kleur
    public void checkFocus(int nr)
    {
       for (int i = 0;i<bColumns;i++)
       {          
           if(bTiles[i].isFocusOwner())
           {
               //component locaties opvragen
               int xl = bTiles[i].getLocation().x;
               int yl = bTiles[i].getLocation().y;
               //vorig component verzijderen
               gameScene.remove(bTiles[i]);
               //nieuw component maken en toev aan scene
               bTiles[i] = new Peg(xl,yl,nr);               
               gameScene.add(bTiles[i]);
               //traversal voor nieuwe knop maken
               renewTraversal(i);
           }           
       }
    }
    public void startXlet() throws XletStateChangeException{ 
    }
    public void pauseXlet() {
    }
    public void destroyXlet(boolean unconditional) throws XletStateChangeException{
    }
}
