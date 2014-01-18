/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.GradientPaint;

public class Connect4 extends JFrame implements Runnable {
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 25;
    static final int WINDOW_WIDTH = 840;
    static final int WINDOW_HEIGHT = 865;    
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;
    
    boolean player1Turn;
    boolean gameOver;
    final static int NUM_ROWS = 8;
    final static int NUM_COLUMNS = 8;  
    Piece board[][] = new Piece[NUM_ROWS][NUM_COLUMNS];
    
    int mostRecentRow;
    int mostRecentCol;
    enum WIN {
        PLAYER1,PLAYER2,TIE,NOWIN
    }
    WIN winState;
    int tokensPlaced;  //number of tokens placed so far.
    
    enum GRAVITY {
        UP, DOWN, LEFT, RIGHT
    }
    GRAVITY gravity;
    
    public static void main(String[] args) {
        Connect4 frame = new Connect4();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Connect Gravity by Aaroh Mankad");
    }

    public Connect4() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                
   
                if (e.BUTTON1 == e.getButton() && !gameOver) {

                    int ydelta = getHeight2()/NUM_ROWS;
                    int xdelta = getWidth2()/NUM_COLUMNS;

                    int xpos = e.getX();
                    int ypos = e.getY();
                    
                    
                    if (xpos < getX(0) || xpos > getX(getWidth2()) ||
                        ypos < getY(0) || ypos > getY(getHeight2()))
                        return;
                    
                    
                    int zcol = 0;
                    boolean keepGoing = true;
                    while (keepGoing)
                    {
                        if (xpos < getX((zcol+1)*xdelta))
                        {
                            keepGoing = false;
                        }   
                        else
                            zcol++;
                    }
                    
                    
    
                    int zrow = 0;
                    keepGoing = true;
                    while (keepGoing)
                    {
                        if (ypos < getY((zrow+1)*ydelta))
                        {
                            keepGoing = false;
                        }   
                        else
                            zrow++;
                    }

                    if(gravity == GRAVITY.UP)
                        while(zrow > 0 && board[zrow-1][zcol] == null)
                            zrow--;
                    
                    if(gravity == GRAVITY.DOWN)
                        while(zrow < NUM_ROWS-1 && board[zrow+1][zcol] == null)
                            zrow++;
                    
                    if(gravity == GRAVITY.LEFT)
                        while(zcol > 0 && board[zrow][zcol-1] == null)
                            zcol--;
                    
                    if(gravity == GRAVITY.RIGHT)
                        while(zcol < NUM_COLUMNS - 1 && board[zrow][zcol+1] == null)
                            zcol++;
                    
                    
                    
                    if (board[zrow][zcol] == null)     // make sure that there is no piece already there
                    {
                        if (player1Turn)
                        {
                            board[zrow][zcol] = new Piece(Color.red);    // create a piece for player 1
                        }
                        else
                        {
                           board[zrow][zcol] = new Piece(Color.black);    // create a piece for player 2
                        }
                        mostRecentRow = zrow;
                        mostRecentCol = zcol;
                        tokensPlaced++;
                        
                        int random = (int)(Math.random()*4+1);//1, 2, 3, 4
                        if(random == 1)
                            gravity = GRAVITY.UP;
                        if(random == 2)
                            gravity = GRAVITY.DOWN;
                        if(random == 3)
                            gravity = GRAVITY.LEFT;
                        if(random == 4)
                            gravity = GRAVITY.RIGHT;
                        
                        player1Turn = !player1Turn;
                    }
                    
                    
                }
                
                if(e.BUTTON2 == e.getButton())
                {
                    int ydelta = getHeight2()/NUM_ROWS;
                    int xdelta = getWidth2()/NUM_COLUMNS;

                    int xpos = e.getX();
                    int ypos = e.getY();
                    
                    
                    if (xpos < getX(0) || xpos > getX(getWidth2()) ||
                        ypos < getY(0) || ypos > getY(getHeight2()))
                        return;
                    
                    
                    int zcol = 0;
                    boolean keepGoing = true;
                    while (keepGoing)
                    {
                        if (xpos < getX((zcol+1)*xdelta))
                        {
                            keepGoing = false;
                        }   
                        else
                            zcol++;
                    }
                    
                    
    
                    int zrow = 0;
                    keepGoing = true;
                    while (keepGoing)
                    {
                        if (ypos < getY((zrow+1)*ydelta))
                        {
                            keepGoing = false;
                        }   
                        else
                            zrow++;
                    }
                    
                    board[zrow][zcol] = null;
                }

                if (e.BUTTON3 == e.getButton()) {

                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {

        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {

        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_UP == e.getKeyCode()) {
                } else if (e.VK_DOWN == e.getKeyCode()) {
                } else if (e.VK_LEFT == e.getKeyCode()) {
                } else if (e.VK_RIGHT == e.getKeyCode()) {
                }
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        
        g.setColor(Color.cyan);
        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
        

 //draw grid
        g.setColor(Color.black);
        int ydelta = getHeight2()/NUM_ROWS;
        for (int zi = 1;zi<NUM_ROWS;zi++)
        {
            g.drawLine(getX(0),getY(zi*ydelta),
                    getX(getWidth2()),getY(zi*ydelta));
        }
        
        
        int xdelta = getWidth2()/NUM_COLUMNS;
        for (int zi = 1;zi<NUM_COLUMNS;zi++)
        {
            g.drawLine(getX(zi*xdelta),getY(0),
                    getX(zi*xdelta),getY(getHeight2()));
        }
        
        
        for (int zi = 0;zi<NUM_ROWS;zi++)
        {
            for (int zx = 0;zx<NUM_COLUMNS;zx++)
            {
                if (board[zi][zx] != null)
                {
                    int shift = 10;
                    g.setColor(board[zi][zx].getColor());
                    g.fillOval(getX(zx*xdelta)+shift, getY(zi*ydelta)+shift, xdelta-(shift*2), ydelta-(shift*2));
                    
                }
            }
        }
        
        g.setColor(Color.black);
        if (gameOver)
        {
            g.drawString("game Over", 20, 40);
            if (winState == WIN.PLAYER1)
                g.drawString("Player 1 is the winner", 690, 40);
            else if (winState == WIN.PLAYER2)
                g.drawString("Player 2 is the winner", 690, 40);
            else if (winState == WIN.TIE)
                g.drawString("It is a tie", 690, 40);
        }
        else
        {
            if (player1Turn)
               g.drawString("player 1", 20, 40);
            else
               g.drawString("player 2", 20, 40);   
            
            if(gravity == GRAVITY.UP)
                g.drawString("Gravity is up", 745, 40);
            if(gravity == GRAVITY.DOWN)
                g.drawString("Gravity is down", 745, 40);
            if(gravity == GRAVITY.LEFT)
                g.drawString("Gravity is left", 745, 40);
            if(gravity == GRAVITY.RIGHT)
                g.drawString("Gravity is right", 745, 40);
        }
                
        gOld.drawImage(image, 0, 0, null);
    }

public  void drawHouse(Graphics2D g,double xscale,double yscale,
double rot,int x,int y)
{
   g.translate(x,y);
   g.rotate(rot  * Math.PI/180.0);
   g.scale( xscale , yscale );

   g.setColor(Color.red);

   int xvals[] = {0,-10,-10,10,10,0};
   int yvals[] = {-20,-10,10,10,-10,-20};

   g.fillPolygon(xvals, yvals, xvals.length);

   g.scale( 1.0/xscale,1.0/yscale );
   g.rotate(-rot  * Math.PI/180.0);
   g.translate(-x,-y);
}    
    

////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = .1;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {

        for (int zi = 0;zi<NUM_ROWS;zi++)
        {
            for (int zx = 0;zx<NUM_COLUMNS;zx++)
            {
                
                board[zi][zx] = null;
            }
        }
        mostRecentRow = -1;
        mostRecentCol = -1;

        player1Turn = true;
        gameOver = false;
        winState = WIN.NOWIN;
        tokensPlaced = 0;
        
        int random = (int)(Math.random()*4+1);//1, 2, 3, 4
        if(random == 1)
            gravity = GRAVITY.UP;
        if(random == 2)
            gravity = GRAVITY.DOWN;
        if(random == 3)
            gravity = GRAVITY.LEFT;
        if(random == 4)
            gravity = GRAVITY.RIGHT;
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }

            reset();

        }

        if (gameOver)
            return;
        

        gameOver = checkWin();
        
    }
////////////////////////////////////////////////////////////////////////////
    public boolean checkWin()
    {
        if (mostRecentRow == -1)
            return false;
        if (tokensPlaced >= NUM_COLUMNS * NUM_ROWS)
        {
            winState = WIN.TIE;
            return(true);
        }
        Color _color;
        if (player1Turn)   //needs to be the opposite
            _color = Color.black;
        else
            _color = Color.red;

        int numConnected = 4;
           
        int _column = mostRecentCol;
        int _row = mostRecentRow;
        
        Piece winningPieces[] = new Piece[numConnected];
        Color winningColor = Color.green;
    
        //check horizontal
                int startCol = _column-3;
                if (startCol < 0)
                    startCol = 0;
                int count = 0;
                for (int zi=0;count < numConnected && zi<(2*numConnected-1) && 
                        startCol+zi < NUM_COLUMNS;zi++)
                {
                    if (board[_row][startCol+zi] != null && board[_row][startCol+zi].getColor() == _color)
                    {
                        winningPieces[count] = board[_row][startCol+zi];
                        count++;
                    }
                    else
                        count = 0;
                }
                if (count == 4)
                {
                    for(int i = 0; i < winningPieces.length; i++)
                        winningPieces[i].setColor(winningColor);
                    if (_color == Color.red)
                        winState = WIN.PLAYER1;
                    else
                        winState = WIN.PLAYER2;
                    return (true);
                }
        //check vertical 
                int startRow = _row-3;
                if (startRow < 0)
                    startRow = 0;
                count = 0;
                for (int zi=0;count < numConnected && zi<(2*numConnected-1) && startRow+zi < NUM_ROWS;zi++)
                {
                    if (board[startRow+zi][_column] != null && board[startRow+zi][_column].getColor() == _color)
                    {
                        winningPieces[count] = board[startRow+zi][_column];
                        count++;
                    }
                    else
                        count = 0;
                }
                if (count == 4)
                {
                    for(int i = 0; i < winningPieces.length; i++)
                        winningPieces[i].setColor(winningColor);
                    if (_color == Color.red)
                        winState = WIN.PLAYER1;
                    else
                        winState = WIN.PLAYER2;
                    return (true);
                }
          //check diag down right.         
                startCol = _column;
                startRow = _row;
                boolean keepGoing = true;
                for (int zi = 0;keepGoing && zi<3;zi++)
                {
                    startCol--;
                    startRow--;
                    if (startRow < 0 || startCol < 0)
                    {
                        keepGoing = false;
                        startCol++;
                        startRow++;
                    }
                }
                    
                count = 0;
                for (int zi=0;count < numConnected && zi<(2*numConnected-1) && 
                startRow+zi < NUM_ROWS && startCol+zi < NUM_COLUMNS;zi++)
                {
                    if (board[startRow+zi][startCol+zi] != null && board[startRow+zi][startCol+zi].getColor() == _color)
                    {
                        winningPieces[count] = board[startRow+zi][startCol+zi];
                        count++;
                    }
                    else
                        count = 0;
                }
                if (count == 4)
                {
                    for(int i = 0; i < winningPieces.length; i++)
                        winningPieces[i].setColor(winningColor);
                    if (_color == Color.red)
                        winState = WIN.PLAYER1;
                    else
                        winState = WIN.PLAYER2;
                    return (true);
                }
               
                
          //check diag up right.         
                startCol = _column;
                startRow = _row;
                keepGoing = true;
                for (int zi = 0;keepGoing && zi<3;zi++)
                {
                    startCol--;
                    startRow++;
                    if (startRow >= NUM_ROWS || startCol < 0)
                    {
                        keepGoing = false;
                        startCol++;
                        startRow--;
                    }
                }
                    
                count = 0;
                for (int zi=0;count < numConnected && zi<(2*numConnected-1) && 
                startRow-zi >= 0 && startCol+zi < NUM_COLUMNS;zi++)
                {
                    if (board[startRow-zi][startCol+zi] != null && board[startRow-zi][startCol+zi].getColor() == _color)
                    {
                        winningPieces[count] = board[startRow-zi][startCol+zi];
                        count++;
                    }
                    else
                        count = 0;
                }
                if (count == 4)
                {
                    for(int i = 0; i < winningPieces.length; i++)
                        winningPieces[i].setColor(winningColor);
                    if (_color == Color.red)
                        winState = WIN.PLAYER1;
                    else
                        winState = WIN.PLAYER2;
                    return (true);
                }
           
       
                return(false);
    }    
    
////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE);
    }
    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE+getHeight2());
    }

    public int getWidth2() {
        return (xsize - getX(0) - XBORDER);
    }

    public int getHeight2() {
        return (ysize - getY(0) - YBORDER);
    }
}
