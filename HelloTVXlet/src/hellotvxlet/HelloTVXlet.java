package hellotvxlet;

import javax.tv.xlet.*;
import javax.tv.xlet.XletContext;
import javax.tv.xlet.XletStateChangeException;
import java.awt.*;
import java.util.Random;
import org.havi.ui.*;
import java.awt.event.*;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
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
    HScene gameScene = HSceneFactory.getInstance().getDefaultHScene(); 
     //random
    int randomCode[] = new int[4];
    Random random = new Random();
 //board
    int rowCounter = 1;
    int bRows = 10;
    int bColumns = 4;
    int maxTiles = bRows * bColumns; 
    int tSize = 45;
    int gY = 540;
    int bPegs,tX,tY,wins,losses;
   //bool
    boolean lock = false;
    //tegels
    HTextButton bTiles[] = new HTextButton[maxTiles];
    HTextButton bTilesCode[] = new HTextButton[bColumns];
    HTextButton bTilesPegs;
    HStaticText board,hst,hide;
    public HelloTVXlet() {
    }
    public void initXlet(XletContext ctx) throws XletStateChangeException{
     //scene
        setScene(gameScene);
     //event knoppen
        UserEventRepository repo = new UserEventRepository("vb");     
        EventManager manager = EventManager.getInstance();
        //events toev
        repo.addAllNumericKeys();
        repo.addKey(HRcEvent.VK_ENTER);
        repo.addKey(HRcEvent.VK_H);
        repo.addAllArrowKeys();
        //add bij Emanager
        manager.addUserEventListener(this, repo);
        
      //de te kiezen pegs display
      pegLayout();
     //verborgencode
      newCode();
     //playerside      
      newRow();
      
           
      gameScene.validate();
      gameScene.setVisible(true);
    } 
    
    public void newCode()
    {
        //de verborgen code boardset
     tX = 50;
     tY = 35;
     hideCode();
     board = new HStaticText("A",5,tY,tSize,tSize);
              board.setBackground(Color.WHITE);  
              gameScene.add(board);
      for (int i = 0;i<bColumns;i++){          
              bTilesCode[i] = new Peg(tX, tY, randomizer(i));
              gameScene.add(bTilesCode[i]);
              tX += tSize;
          }   
    }
    public void hideCode()
    {
        int hX = 50;
        hide = new HStaticText("?",hX,tY,tSize*4,tSize);
              hide.setBackgroundMode(HVisible.BACKGROUND_FILL);
              hide.setBackground(Color.DARK_GRAY);  
              gameScene.add(hide);
    }
    public void toggleHide()
    {
        
        if(hide.isVisible()==true)
        {
            hide.setVisible(false);
        }   
        else
        {
            hide.setVisible(true);
        }   
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
        gY -= tSize;
        tX=50;
        //rij cijfer
              board = new HStaticText(Integer.toString(rowCounter),5,gY,tSize,tSize);
              board.setBackground(Color.WHITE);  
              gameScene.add(board);
        //maakt rij aan
        for (int i = 0 ; i<bColumns;i++)
        {
             bTiles[i] = new Peg(tX, gY, 0);
             gameScene.add(bTiles[i]);
             tX += tSize;             
        }
        //zorgt voor focus en traversal tussen vakken
        for (int j = 0 ; j<bColumns;j++)
        {
            renewTraversal(j);   
            bTiles[j].requestFocus();
        }
        gameScene.repaint();
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
            if(!lock)
            {
            switch (e.getCode())
            {
                case HRcEvent.VK_ENTER:
                    if (rowCounter != bRows)
                    {
                        if(checkForWhite())
                        {
                          checkRightCode();
                        }
                    } 
                    else
                    {
                        displayWin(false);
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
                    case HRcEvent.VK_H:       
                    toggleHide();
                    break;
            }
            }
            else if (e.getCode() == HRcEvent.VK_ENTER)
            {
                reset(gameScene);
            }
        }
    }
    public boolean checkForWhite()
    {
        for (int i = 0 ; i<bColumns;i++)
        {
        if (bTiles[i].getBackground() == Color.WHITE)
        {
            bTiles[i].requestFocus();
            return false;
        }
        }
        return true;
    }
    public void checkRightCode()
    {
        //methode die kijkt of gegeven rij juist is en of hoeveel er juist zijn 
        //temp checkers
        Color[] tempCode = new Color[bColumns];
        Color[] tempGuess = new Color[bColumns];

        for (int h = 0;h<bColumns;h++)
        {
            tempCode[h] = bTilesCode[h].getBackground();
            tempGuess[h] = bTiles[h].getBackground();            
        }
        
        int rGuess = 0;
        int rColor = 0;
        
        //kijkt of overeenkomende arrays volledig hetzelfde zijn
        for (int i = 0 ; i<bColumns;i++)
        {
            if (tempCode[i] == tempGuess[i])
                {
                   rGuess ++;
                   tempCode[i] = Color.white;
                   tempGuess[i] = Color.white;
                }
            }
        //kijkt voor aantal zelfde kleuren
       for (int j = 0;j<bColumns;j++)
            {
           if(tempCode[j]== Color.white)
           {
               continue;
           }
            for (int k = 0;k<bColumns;k++)
            {
                if(tempGuess[k]==Color.white)
                {
                    continue;
                }
                if (tempCode[j] == tempGuess[k])
                {
                rColor ++;
                tempCode[j]=Color.white;
                tempGuess[k]=Color.white;
                }
            }
        }
        System.out.println(rColor);
        displayControlPegs(rGuess,rColor);
        if (rGuess == 4)
        {
            displayWin(true);
        }
        else
        {
            rowCounter ++;  
            newRow();             
        }
    }
    public void displayWin(boolean win)
    {
        board = new HStaticText("PRESS 'ENTER' TO PLAY AGAIN",400,185,300,45);
        board.setBackground(Color.WHITE);
        gameScene.add(board);
        if(win)
        {
            wins++;
            toggleHide();
            board = new HStaticText("YOU WON!",400,35,300,150);
              board.setBackgroundMode(HVisible.BACKGROUND_FILL);
              board.setBackground(Color.DARK_GRAY);  
              gameScene.add(board);              
        }
        else
        {
            toggleHide();
            losses++;
          board = new HStaticText("YOU LOSE!",400,35,300,150);
              board.setBackgroundMode(HVisible.BACKGROUND_FILL);
              board.setBackground(Color.DARK_GRAY);  
              gameScene.add(board);  
        }  
            lock = true;
            gameScene.repaint();
    }
    public void displayControlPegs(int red, int white)
    {
        int r = red;
        int w = white;
        int total = red+white;
        int count=0;
        int cX  = 240;      
        int cY = gY;
        for (int i = 0;i<total;i++)
        {          
          if (r != 0)
            {
              board = new HStaticText("",cX,cY,15,15);
              board.setBackgroundMode(HVisible.BACKGROUND_FILL);
              board.setBackground(Color.RED);  
              gameScene.add(board);
              cX += 18;  
                r --;
            }
            else if (w != 0)
            {
              board = new HStaticText("",cX,cY,15,15);
              board.setBackgroundMode(HVisible.BACKGROUND_FILL);
              board.setBackground(Color.WHITE);  
              gameScene.add(board);
              cX += 18;  
                w--;  
            } 
          count++;
          if(count == 2)
            {
              cX = 240;
              cY += 18;
              count = 0;
          }
        }
        gameScene.repaint();
    }
    public void renewTraversal(int index)
    {
        for (int i = 0;i<bColumns;i++)
        {
        bTiles[i].setFocusTraversal(null, null, bTiles[i-checkArray(i,0)], bTiles[i+checkArray(i,1)]);
        }        
    }
    public void checkFocus(int nr)
    {
        //methode die kijkt welk component de focus heeft en maakt een nieuw object aan met de meegegeven kleur
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
               bTiles[i].requestFocus();
           }           
       }
    }
    public void pegLayout()
    {
     //init
     tY=400;
     //text peg colors
      hst = new HStaticText("Peg Colors",500,305,180,50);
      hst.setBackgroundMode(HVisible.BACKGROUND_FILL);
      hst.setBackground(Color.BLACK);
      gameScene.add(hst);
     //de te kiezen pegs
      bPegs =1;
      for (int i = 0;i<2;i++){
          tX=500;
          for (int j = 0;j<bColumns;j++){
              bTilesPegs = new Peg(tX, tY, bPegs);
              gameScene.add(bTilesPegs);
              
              board = new HStaticText(Integer.toString(bPegs),tX,tY-tSize,tSize,tSize);
              board.setBackground(Color.WHITE);  
              gameScene.add(board);
              
              tX += tSize;              
              bPegs++;
          }
          tY += (tSize*2);
     }
     //wins/losses
      board = new HStaticText("wins: "+wins,350,455,120,tSize);
      board.setBackground(Color.WHITE);  
      gameScene.add(board);
      
      board = new HStaticText("losses: "+losses,350,500,120,tSize);
      board.setBackground(Color.WHITE);
      gameScene.add(board);
    }
    public void reset(HScene sc)
    {
        sc.removeAll();
        gY=540;
        rowCounter = 1;
        lock = false;
        newCode();
        newRow();
        pegLayout();
        sc.repaint();
    }
    public void setScene(HScene s)
    {
     s = HSceneFactory.getInstance().getDefaultHScene();  
     s.setBackgroundMode(HVisible.BACKGROUND_FILL);
     s.setBackground(Color.GRAY);
    }
    public void startXlet() throws XletStateChangeException{ 
    }
    public void pauseXlet() {
    }
    public void destroyXlet(boolean unconditional) throws XletStateChangeException{
    }
}
