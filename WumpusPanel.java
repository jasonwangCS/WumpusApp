import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WumpusPanel extends JPanel implements KeyListener {
    //JPanel panel = new JPanel();
    public static final int PLAYING = 0;
    public static final int DEAD = 1;
    public static final int WON = 2;
    private int status;
    private WumpusPlayer player;
    private WumpusMap map;
    private BufferedImage floor, arrow, fog, gold, ladder, pit, breeze,
    wumpus, deadWumpus, stench, playerUp, playerDown, playerLeft, playerRight;

    private BufferedImage buffer;
    private boolean moveUp, moveLeft, moveRight, moveDown;
    private boolean start = true;
    private  boolean shotUp = false;
    private  boolean shotDown = false;
    private  boolean shotLeft = false;
    private  boolean shotRight = false;
    private  boolean wumpusDied = false;
    private char lastMove = 'D';
    private int wumpusRow = -1;
    private int wumpusCol = -1;

    private boolean win = false;
    private boolean lose = false;

    private boolean goldPickUp = false;
    private boolean goldInInventory = false;
    public boolean updateRight = false;
    private boolean cheatToggle = false;
    private boolean toggleChanged = false;
    private boolean resetGame = false;

    public char [][] moveGrid = new char[10][10];
    Font f =  new Font("Times New Roman", Font.BOLD, 13);


    public WumpusPanel() throws IOException {
        //panel = new JPanel();
        setLayout(null);
        setVisible(true);
        setSize(750, 800);

        //setBounds(200, 200, 700, 700);
        //setBackground(Color.GRAY);
        //create buffer
        buffer = new BufferedImage(50, 50, 1);
        //loads images
        floor = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\Floor.gif")));
        arrow = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\arrow.gif")));
        fog = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\black.gif")));
        gold = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\gold.gif")));
        ladder = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\ladder.gif")));
        pit = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\pit.gif")));
        breeze = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\breeze.gif")));
        wumpus = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\wumpus.gif")));
        deadWumpus = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\deadwumpus.gif")));
        stench = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\stench.gif")));
        playerUp = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\playerUp.PNG")));
        playerDown = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\playerDown.PNG")));
        playerLeft = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\playerLeft.PNG")));
        playerRight = ImageIO.read((new File("C:\\Users\\w1102353\\Wumpus World Images\\Wumpus World Images\\playerRight.PNG")));
        addKeyListener(this);
        for(int s = 0; s < 10; s++){
            for(int c = 0 ; c < 10; c++){
                moveGrid[s][c] = '-';
            }
        }
        reset();
        repaint();
        //start = false;
    }
    public void reset()
    {
        status = PLAYING;
        map = new WumpusMap();

        // place player at position of ladder
        player = new WumpusPlayer();
        player.setRow(map.getLadderR());
        player.setCol(map.getLadderC());
        //repaint();

        System.out.println(player.getRow());
        System.out.println(player.getCol());

    }
    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(f);

        if(resetGame)
        {
            for(int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    g2d.drawImage(fog, null, j * 50, i * 50);
                }
            }
            resetGame = false;
        }
        if(player.getArrow())
        {
            System.out.println("Brhu");
            g2d.drawImage(arrow, null, 400, 525);
        }
        else if(!win && !lose)
        {
            g2d.setColor(Color.GRAY);
            g2d.fillRect(400, 520, 50, 50);
        }
        if(goldInInventory)
        {
            g2d.drawImage(gold, null, 450, 525);
        }
        else
        {
            g2d.setColor(Color.GRAY);
            g2d.fillRect(450, 500, 50, 50);
        }

        if(win)
        {
            g2d.setColor(Color.WHITE);
            g2d.drawString("You win!", 250, 550);
            g2d.drawString("Press n to restart!", 250, 575);
        }
        if(!lose && !win)
        {
            if(toggleChanged)
            {
                if(cheatToggle)
                {
                    for(int i = 0; i < 10; i++)
                    {
                        for(int j = 0; j < 10; j++)
                        {
                            WumpusSquare tmp = map.getSquare(i, j);
                            g2d.drawImage(floor, null, j * 50, i * 50);
                            if(tmp.isPit())
                            {
                                g2d.drawImage(pit, null, j * 50, i * 50);
                            }
                            if(tmp.isWumpus())
                            {
                                g2d.drawImage(wumpus, null, j * 50, i * 50);
                            }
                            if(tmp.isDeadWumpus())
                            {
                                g2d.drawImage(deadWumpus, null, j * 50, i * 50);
                            }
                            if(tmp.isGold())
                            {
                                g2d.drawImage(gold, null, j * 50, i * 50);
                            }
                            if(tmp.isLadder())
                            {
                                g2d.drawImage(ladder, null, j * 50, i * 50);
                            }
                            if(tmp.isBreeze() && !tmp.isPit())
                            {
                                g2d.drawImage(breeze, null, j * 50, i * 50);
                            }
                            if(tmp.isStench() && !tmp.isPit())
                            {
                                g2d.drawImage(stench, null, j * 50, i * 50);
                            }
                            if(player.getCol() == j && player.getRow() == i)
                            {
                                System.out.println("WEEWEEE");
                            }
                        }
                    }
                }
                else
                {
                    for(int i = 0; i < 10; i++)
                    {
                        for(int j = 0; j < 10; j++) {
                            g2d.drawImage(fog, null, j * 50, i * 50);
                            if (moveGrid[i][j] != '-') {
                                WumpusSquare tmp = map.getSquare(i, j);
                                g2d.drawImage(floor, null, j * 50, i * 50);
                                if(tmp.isPit())
                                {
                                    g2d.drawImage(pit, null, j * 50, i * 50);
                                }
                                if(tmp.isWumpus())
                                {
                                    g2d.drawImage(wumpus, null, j * 50, i * 50);
                                }
                                if(tmp.isDeadWumpus())
                                {
                                    g2d.drawImage(deadWumpus, null, j * 50, i * 50);
                                }
                                if(tmp.isGold())
                                {
                                    g2d.drawImage(gold, null, j * 50, i * 50);
                                }
                                if(tmp.isLadder())
                                {
                                    g2d.drawImage(ladder, null, j * 50, i * 50);
                                }
                                if(tmp.isBreeze() && !tmp.isPit())
                                {
                                    g2d.drawImage(breeze, null, j * 50, i * 50);
                                }
                                if(tmp.isStench() && !tmp.isPit())
                                {
                                    g2d.drawImage(stench, null, j * 50, i * 50);
                                }
//                                if(player.getCol() == j && player.getRow() == i)
//                                {
//                                    System.out.println("WEEWEEE");
//                                    if(lastMove == 'D')
//                                    {
//                                        System.out.println("DOWN " + i +" " +  j);
//                                        g2d.drawImage(playerDown, null, j * 50, i * 50);
//                                    }
//                                    if(lastMove == 'U')
//                                    {
//                                        g2d.drawImage(playerUp, null, j * 50, i * 50);
//                                    }
//                                    if(lastMove == 'L')
//                                    {
//                                        g2d.drawImage(playerLeft, null, j * 50, i * 50);
//                                    }
//                                    if(lastMove == 'R')
//                                    {
//                                        g2d.drawImage(playerRight, null, j * 50, i * 50);
//                                    }
//                                }
                            }
                        }
                    }
                }
                toggleChanged = false;
            }
            if(goldPickUp)
            {


                g2d.drawImage(floor, null, player.getCol() * 50, player.getRow() * 50);
                WumpusSquare tmp = map.getSquare(player.getRow(), player.getCol());
                if(tmp.isWumpus())
                {
                    g2d.drawImage(wumpus, null, player.getCol() * 50, player.getRow() * 50);
                }
                if(tmp.isDeadWumpus())
                {
                    g2d.drawImage(deadWumpus, null, player.getCol() * 50, player.getRow() * 50);
                }
                if(tmp.isBreeze() && !tmp.isPit())
                {
                    g2d.drawImage(breeze, null, player.getCol() * 50, player.getRow() * 50);
                }
                if(tmp.isStench() && !tmp.isPit())
                {
                    g2d.drawImage(stench, null, player.getCol() * 50, player.getRow() * 50);
                }
                goldPickUp = false;
                g2d.drawImage(gold, null, 400, 525);
            }
            if(shotUp)
            {
                System.out.println("PEE");

                WumpusSquare tmp = map.getSquare(player.getRow(), player.getCol());
                //shotRight = false;
                g2d.setColor(Color.WHITE);
                g2d.drawString("Inventory", 400, 515);
                g2d.drawImage(floor, null, player.getCol() * 50, player.getRow() * 50);

                if(tmp.isPit())
                {
                    g2d.drawImage(pit, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You fell down a hole and died lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    System.out.println("died by hole");
                    lose = true;
                }
                if(tmp.isWumpus())
                {
                    g2d.drawImage(wumpus, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("died by wumpus");
                    g2d.drawString("You died to Wumpus lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    lose = true;
                }
                if(tmp.isDeadWumpus())
                {
                    g2d.drawImage(deadWumpus, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                if(tmp.isGold())
                {
                    g2d.drawImage(gold, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel gold", 250, 525);
                }
                if(tmp.isLadder())
                {
                    g2d.drawImage(ladder, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel a cold ladder", 25, 550);
                }
                if(tmp.isBreeze() && !tmp.isPit())
                {
                    g2d.drawImage(breeze, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("you feel breeze jit");
                    g2d.drawString("You feel a breeze", 25, 525);
                }
                if(tmp.isStench() && !tmp.isPit())
                {
                    g2d.drawImage(stench, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                lastMove = 'U';
                g2d.drawImage(playerUp, null, player.getCol() * 50, player.getRow() * 50);
                //shotUp = false;
            }
            else if(shotDown)
            {
                System.out.println("PEE");
                lastMove = 'D';
                WumpusSquare tmp = map.getSquare(player.getRow(), player.getCol());
                //shotRight = false;
                g2d.setColor(Color.WHITE);
                g2d.drawString("Inventory", 400, 515);
                g2d.drawImage(floor, null, player.getCol() * 50, player.getRow() * 50);

                if(tmp.isPit())
                {
                    g2d.drawImage(pit, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You fell down a hole and died lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    System.out.println("died by hole");
                    lose = true;
                }
                if(tmp.isWumpus())
                {
                    g2d.drawImage(wumpus, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("died by wumpus");
                    g2d.drawString("You died to Wumpus lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    lose = true;
                }
                if(tmp.isDeadWumpus())
                {
                    g2d.drawImage(deadWumpus, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                if(tmp.isGold())
                {
                    g2d.drawImage(gold, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel gold", 250, 525);
                }
                if(tmp.isLadder())
                {
                    g2d.drawImage(ladder, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel a cold ladder", 25, 550);
                }
                if(tmp.isBreeze() && !tmp.isPit())
                {
                    g2d.drawImage(breeze, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("you feel breeze jit");
                    g2d.drawString("You feel a breeze", 25, 525);
                }
                if(tmp.isStench() && !tmp.isPit())
                {
                    g2d.drawImage(stench, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                g2d.drawImage(playerDown, null, player.getCol() * 50, player.getRow() * 50);

                //shotDown = false;
            }
            else if(shotLeft)
            {
                System.out.println("PEE");
                lastMove = 'L';
                WumpusSquare tmp = map.getSquare(player.getRow(), player.getCol());
                //shotRight = false;
                g2d.setColor(Color.WHITE);
                g2d.drawString("Inventory", 400, 515);
                g2d.drawImage(floor, null, player.getCol() * 50, player.getRow() * 50);

                if(tmp.isPit())
                {
                    g2d.drawImage(pit, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You fell down a hole and died lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    System.out.println("died by hole");
                    lose = true;
                }
                if(tmp.isWumpus())
                {
                    g2d.drawImage(wumpus, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("died by wumpus");
                    g2d.drawString("You died to Wumpus lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    lose = true;
                }
                if(tmp.isDeadWumpus())
                {
                    g2d.drawImage(deadWumpus, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                if(tmp.isGold())
                {
                    g2d.drawImage(gold, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel gold", 250, 525);
                }
                if(tmp.isLadder())
                {
                    g2d.drawImage(ladder, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel a cold ladder", 25, 550);
                }
                if(tmp.isBreeze() && !tmp.isPit())
                {
                    g2d.drawImage(breeze, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("you feel breeze jit");
                    g2d.drawString("You feel a breeze", 25, 525);
                }
                if(tmp.isStench() && !tmp.isPit())
                {
                    g2d.drawImage(stench, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                //shotLeft = false;
                g2d.drawImage(playerLeft, null, player.getCol() * 50, player.getRow() * 50);


            }
            else if(shotRight)
            {
                System.out.println("PEE");

                lastMove = 'R';
                WumpusSquare tmp = map.getSquare(player.getRow(), player.getCol());
                //shotRight = false;
                g2d.setColor(Color.WHITE);
                g2d.drawString("Inventory", 400, 515);
                g2d.drawImage(floor, null, player.getCol() * 50, player.getRow() * 50);

                if(tmp.isPit())
                {
                    g2d.drawImage(pit, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You fell down a hole and died lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    System.out.println("died by hole");
                    lose = true;
                }
                if(tmp.isWumpus())
                {
                    g2d.drawImage(wumpus, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("died by wumpus");
                    g2d.drawString("You died to Wumpus lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    lose = true;
                }
                if(tmp.isDeadWumpus())
                {
                    g2d.drawImage(deadWumpus, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                if(tmp.isGold())
                {
                    g2d.drawImage(gold, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel gold", 250, 525);
                }
                if(tmp.isLadder())
                {
                    g2d.drawImage(ladder, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel a cold ladder", 25, 550);
                }
                if(tmp.isBreeze() && !tmp.isPit())
                {
                    g2d.drawImage(breeze, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("you feel breeze jit");
                    g2d.drawString("You feel a breeze", 25, 525);
                }
                if(tmp.isStench() && !tmp.isPit())
                {
                    g2d.drawImage(stench, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                g2d.drawImage(playerRight, null, player.getCol() * 50, player.getRow() * 50);

            }
            if(wumpusDied)
            {
                g2d.setColor(Color.WHITE);
                System.out.println("DeadWumpus paint");
                g2d.drawString("You hear a screech", 250, 575);
                WumpusSquare tmp = map.getSquare(wumpusRow, wumpusCol);
                tmp.setWumpus(false);
                tmp.setDeadWumpus(true);
                if(cheatToggle)
                {
                    g2d.drawImage(floor, null, wumpusCol * 50, wumpusRow * 50);
                    if(tmp.isGold())
                    {
                        g2d.drawImage(gold, null, wumpusCol * 50, wumpusRow * 50);
                    }
                    if(tmp.isBreeze())
                    {
                        g2d.drawImage(breeze, null, wumpusCol * 50, wumpusRow * 50);
                    }
                    if(tmp.isStench())
                    {
                        g2d.drawImage(stench, null, wumpusCol * 50, wumpusRow * 50);
                    }
                    g2d.drawImage(deadWumpus, null, wumpusCol * 50, wumpusRow * 50);
                }
                  wumpusDied = false;
                shotLeft = false;
                shotRight = false;
                shotUp = false;
                shotDown = false;
            }
            else
            {
                System.out.println("draw curr swquare");
                WumpusSquare tmp = map.getSquare(player.getRow(), player.getCol());
                g2d.setColor(Color.GRAY);
                g2d.fillRect(0, 500, 700, 100);
                if(player.getArrow())
                {
                    System.out.println("Brhu");
                    g2d.drawImage(arrow, null, 400, 525);
                }
                else
                {
                    g2d.setColor(Color.GRAY);
                    g2d.fillRect(400, 500, 50, 50);
                }
                if(goldInInventory)
                {
                    System.out.println("Brhu");
                    g2d.drawImage(gold, null, 450, 525);
                }
                else
                {
                    g2d.setColor(Color.GRAY);
                    g2d.fillRect(450, 500, 50, 50);
                }
                g2d.setColor(Color.WHITE);
                g2d.drawString("Inventory", 400, 515);
                g2d.drawImage(floor, null, player.getCol() * 50, player.getRow() * 50);

                if(tmp.isPit())
                {
                    g2d.drawImage(pit, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You fell down a hole and died lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    System.out.println("died by hole");
                    lose = true;
                }
                if(tmp.isWumpus())
                {
                    g2d.drawImage(wumpus, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("died by wumpus");
                    g2d.drawString("You died to Wumpus lol", 250, 550);
                    g2d.drawString("You Lose press n to restart", 250, 575);
                    lose = true;
                }
                if(tmp.isDeadWumpus())
                {
                    g2d.drawImage(deadWumpus, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                if(tmp.isGold())
                {
                    g2d.drawImage(gold, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel gold", 250, 525);
                }
                if(tmp.isLadder())
                {
                    g2d.drawImage(ladder, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel a cold ladder", 25, 550);
                }
                if(tmp.isBreeze() && !tmp.isPit())
                {
                    g2d.drawImage(breeze, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("you feel breeze jit");
                    g2d.drawString("You feel a breeze", 25, 525);
                }
                if(tmp.isStench() && !tmp.isPit())
                {
                    g2d.drawImage(stench, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawString("You feel stinky", 25, 575);
                }
                if(lastMove == 'D' && !moveDown && !moveUp && !moveLeft && !moveRight)
                {
                    //System.out.println("DOWN " + i +" " +  j);
                    g2d.drawImage(playerDown, null, player.getCol() * 50, player.getRow() * 50);
                    System.out.println("bruh");
                }
                if(lastMove == 'U'&& !moveDown && !moveUp && !moveLeft && !moveRight)
                {
                    //g2d.drawImage(playerUp, null, player.getCol() * 50, player.getRow() * 50);
                }
                if(lastMove == 'L'&& !moveDown && !moveUp && !moveLeft && !moveRight)
                {
                   // g2d.drawImage(playerLeft, null, player.getCol() * 50, player.getRow() * 50);
                }
                if(lastMove == 'R'&& !moveDown && !moveUp && !moveLeft && !moveRight)
                {
                    //g2d.drawImage(playerRight, null, player.getCol() * 50, player.getRow() * 50);
                }
                if(shotLeft)
                {
                    g2d.drawImage(playerLeft, null, player.getCol() * 50, player.getRow() * 50);
                    shotLeft = false;
                }
                if(shotRight)
                {
                    g2d.drawImage(playerRight, null, player.getCol() * 50, player.getRow() * 50);
                    shotRight = false;
                }
                if(shotUp)
                {
                    g2d.drawImage(playerUp, null, player.getCol() * 50, player.getRow() * 50);
                    shotUp = false;
                }
                if(shotDown)
                {
                    g2d.drawImage(playerDown, null, player.getCol() * 50, player.getRow() * 50);
                    shotDown = false;
                }
                if(moveDown)
                {
                    System.out.println("move down");
                    lastMove = 'D';
                    moveGrid[player.getRow()-1][player.getCol()] = 'D';
                    for(int i = 0; i < 10; i++)
                    {
                        for(int j = 0; j < 10; j++)
                        {
                            System.out.print(moveGrid[i][j] + " ");
                        }
                        System.out.println();
                    }
                    g2d.drawImage(playerDown, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawImage(floor, null, player.getCol() * 50, (player.getRow()-1) * 50);
                    tmp = map.getSquare((player.getRow()-1), player.getCol());
                    if(tmp.isPit())
                    {
                        g2d.drawImage(pit, null, player.getCol() * 50, (player.getRow()-1) * 50);
                    }
                    if(tmp.isWumpus())
                    {
                        g2d.drawImage(wumpus, null, player.getCol() * 50, (player.getRow()-1) * 50);
                    }
                    if(tmp.isDeadWumpus())
                    {
                        g2d.drawImage(deadWumpus, null, player.getCol() * 50, (player.getRow()-1) * 50);
                    }
                    if(tmp.isGold())
                    {
                        g2d.drawImage(gold, null, player.getCol() * 50, (player.getRow()-1) * 50);
                    }
                    if(tmp.isLadder())
                    {
                        g2d.drawImage(ladder, null, player.getCol() * 50, (player.getRow()-1) * 50);
                    }
                    if(tmp.isBreeze() && !tmp.isPit())
                    {
                        g2d.drawImage(breeze, null, player.getCol() * 50, (player.getRow()-1) * 50);
                    }
                    if(tmp.isStench() && !tmp.isPit())
                    {
                        g2d.drawImage(stench, null, player.getCol() * 50, (player.getRow()-1) * 50);
                    }
                }
                if(moveUp)
                {
                    lastMove = 'U';
                    moveGrid[player.getRow()+1][player.getCol()] = 'U';
                    for(int i = 0; i < 10; i++)
                    {
                        for(int j = 0; j < 10; j++)
                        {
                            System.out.print(moveGrid[i][j] + " ");
                        }
                        System.out.println();
                    }
                    g2d.drawImage(playerUp, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawImage(floor, null, player.getCol() * 50, (player.getRow()+1) * 50);
                    tmp = map.getSquare(player.getRow()+1, player.getCol());
                    if(tmp.isPit())
                    {
                        g2d.drawImage(pit, null, player.getCol() * 50, (player.getRow()+1) * 50);
                    }
                    if(tmp.isWumpus())
                    {
                        g2d.drawImage(wumpus, null, player.getCol() * 50, (player.getRow()+1) * 50);
                    }
                    if(tmp.isDeadWumpus())
                    {
                        g2d.drawImage(deadWumpus, null, player.getCol() * 50, (player.getRow()+1) * 50);
                    }
                    if(tmp.isGold())
                    {
                        g2d.drawImage(gold, null, player.getCol() * 50, (player.getRow()+1) * 50);
                    }
                    if(tmp.isLadder())
                    {
                        g2d.drawImage(ladder, null, player.getCol() * 50, (player.getRow()+1) * 50);
                    }
                    if(tmp.isBreeze() && !tmp.isPit())
                    {
                        g2d.drawImage(breeze, null, player.getCol() * 50, (player.getRow()+1) * 50);
                    }
                    if(tmp.isStench() && !tmp.isPit())
                    {
                        g2d.drawImage(stench, null, player.getCol() * 50, (player.getRow()+1) * 50);
                    }
                }
                if(moveLeft)
                {
                    lastMove = 'L';
                    moveGrid[player.getRow()][player.getCol()+1] = 'L';
                    for(int i = 0; i < 10; i++)
                    {
                        for(int j = 0; j < 10; j++)
                        {
                            System.out.print(moveGrid[i][j] + " ");
                        }
                        System.out.println();
                    }
                    g2d.drawImage(playerLeft, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawImage(floor, null, (player.getCol()+1) * 50, player.getRow() * 50);
                    tmp = map.getSquare( player.getRow(), player.getCol()+1);
                    if(tmp.isPit())
                    {
                        g2d.drawImage(pit, null, (player.getCol()+1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isWumpus())
                    {
                        g2d.drawImage(wumpus, null, (player.getCol()+1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isDeadWumpus())
                    {
                        g2d.drawImage(deadWumpus, null, (player.getCol()+1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isGold())
                    {
                        g2d.drawImage(gold, null, (player.getCol()+1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isLadder())
                    {
                        g2d.drawImage(ladder, null, (player.getCol()+1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isBreeze() && !tmp.isPit())
                    {
                        g2d.drawImage(breeze, null, (player.getCol()+1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isStench() && !tmp.isPit())
                    {
                        g2d.drawImage(stench, null, (player.getCol()+1) * 50, player.getRow() * 50);
                    }

                }
                if(moveRight)
                {
                    lastMove = 'R';
                    moveGrid[player.getRow()][player.getCol()-1] = 'R';
                    for(int i = 0; i < 10; i++)
                    {
                        for(int j = 0; j < 10; j++)
                        {
                            System.out.print(moveGrid[i][j] + " ");
                        }
                        System.out.println();
                    }
                    g2d.drawImage(playerRight, null, player.getCol() * 50, player.getRow() * 50);
                    g2d.drawImage(floor, null, (player.getCol()-1) * 50, player.getRow() * 50);
                    tmp = map.getSquare( player.getRow(),player.getCol()-1);
                    if(tmp.isPit())
                    {
                        g2d.drawImage(pit, null, (player.getCol()-1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isWumpus())
                    {
                        g2d.drawImage(wumpus, null, (player.getCol()-1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isDeadWumpus())
                    {
                        g2d.drawImage(deadWumpus, null, (player.getCol()-1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isGold())
                    {
                        g2d.drawImage(gold, null, (player.getCol()-1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isLadder())
                    {
                        g2d.drawImage(ladder, null, (player.getCol()-1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isBreeze() && !tmp.isPit())
                    {
                        g2d.drawImage(breeze, null, (player.getCol()-1) * 50, player.getRow() * 50);
                    }
                    if(tmp.isStench() && !tmp.isPit())
                    {
                        g2d.drawImage(stench, null, (player.getCol()-1) * 50, player.getRow() * 50);
                    }
                }

            }
            if(start){
                System.out.println("POOP");
                for(int i = 0; i < 10; i++)
                {
                    for(int j = 0; j < 10; j++)
                    {
                        g2d.drawImage(fog, null, 50 * i, 50 * j);
                    }
                }
                g2d.drawImage(floor, null, player.getCol() * 50, player.getRow() * 50);
                g2d.drawImage(ladder, null, player.getCol() * 50, player.getRow() * 50);
                //g2d.drawImage(playerDown, null, player.getCol() * 50, player.getRow() * 50);
                WumpusSquare tmp = map.getSquare(player.getRow(), player.getCol());
                if(tmp.isBreeze())
                {
                    g2d.drawImage(breeze, null, player.getCol() * 50, player.getRow() * 50);
                }
                if(tmp.isStench())
                {
                    g2d.drawImage(stench, null, player.getCol() * 50, player.getRow() * 50);
                }
                g2d.drawImage(playerDown, null, player.getCol() * 50, player.getRow() * 50);
                if(player.getArrow())
                {
                    System.out.println("Brhu");
                    g2d.drawImage(arrow, null, 400, 525);
                }
                else
                {
                    //g2d.setColor(Color.GRAY);
                    //g2d.fillRect(400, 500, 50, 50);
                }
                start = false;
                //g2d.drawImage(gold, null, 450, 525);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    public void addNotify()
    {
        super.addNotify();
        requestFocus();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == 83 && map.getCurrentR() < 9){
            map.moveDown();
            int newR = map.getCurrentR();
            player.setRow(newR);
            System.out.println("( " + player.getRow() + ", " + player.getCol() + ")");
            moveUp = false;
            moveDown = true;
            moveLeft = false;
            moveRight = false;
            repaint();
        }
        if(keyCode == 87 && map.getCurrentR() >0){
            map.moveUp();
            int newR = map.getCurrentR();
            player.setRow(newR);
            System.out.println("( " + player.getRow() + ", " + player.getCol() + ")");
            moveUp = true;
            moveDown = false;
            moveLeft = false;
            moveRight = false;
            repaint();
        }
        if(keyCode == 65 && map.getCurrentC() > 0){
            map.moveLeft();
            int newC = map.getCurrentC();
            player.setCol(newC);
            System.out.println("( " + player.getRow() + ", " + player.getCol() + ")");
            moveUp = false;
            moveDown = false;
            moveLeft = true;
            moveRight = false;
            repaint();
        }
        if(keyCode == 68 && map.getCurrentC() < 9){
            map.moveRight();
            int newC = map.getCurrentC();
            player.setCol(newC);
            System.out.println("( " + player.getRow() + ", " + player.getCol() + ")");
            moveUp = false;
            moveDown = false;
            moveLeft = false;
            moveRight = true;
            repaint();
        }
        if(keyCode == 73 && player.getArrow()) // i
        {
            //remove arrow from inventory
            shotUp = true;
            player.setArrow(false);
            moveUp = false;
            moveDown = false;
            moveLeft = false;
            moveRight = false;
            repaint();
            //shotUp = false;
            for(int i = 0; i < player.getRow(); i++)
            {
                System.out.println("shoot up");
                if(map.getSquare(i, player.getCol()).isWumpus())
                {
                    System.out.println(i + " " + player.getCol() + "died");
                    map.getSquare(i, player.getCol()).setDeadWumpus(true);
                    map.getSquare(i, player.getCol()).setWumpus(false);
                    wumpusDied = true;
                    wumpusRow = i;
                    wumpusCol = player.getCol();
                    repaint();
                }
            }
            repaint();
        }
        if(keyCode == 75 && player.getArrow()) // k
        {
            System.out.println("shoot down");
            //remove arrow from inventory
            shotDown = true;
            player.setArrow(false);
            moveUp = false;
            moveDown = false;
            moveLeft = false;
            moveRight = false;
            repaint();
            //shotUp = false;
            for(int i = player.getRow(); i <= 9; i++)
            {
                System.out.println("shoot down");
                if(map.getSquare(i, player.getCol()).isWumpus())
                {
                    System.out.println(i + " " + player.getCol() + "died");
                    map.getSquare(i, player.getCol()).setDeadWumpus(true);
                    map.getSquare(i, player.getCol()).setWumpus(false);
                    wumpusDied = true;
                    wumpusRow = i;
                    wumpusCol = player.getCol();
                    repaint();
                }
            }
            repaint();
        }
        if(keyCode == 74 && player.getArrow()) // j
        {
            System.out.println("shoot left");
            //remove arrow from inventory
            shotLeft = true;
            player.setArrow(false);
            moveUp = false;
            moveDown = false;
            moveLeft = false;
            moveRight = false;
            repaint();
            //shotUp = false;
            for(int i = 0; i < player.getCol(); i++)
            {
                System.out.println("shoot left");
                if(map.getSquare(player.getRow(), i).isWumpus())
                {
                    System.out.println(player.getRow() + " " + i + "died");
                    map.getSquare(player.getRow(), i).setDeadWumpus(true);
                    map.getSquare(player.getRow(), i).setWumpus(false);
                    wumpusDied = true;
                    wumpusRow = player.getRow();
                    wumpusCol = i;
                    repaint();
                }
            }
            repaint();
        }
        if(keyCode == 76 && player.getArrow()) // l
        {
            System.out.println("shoot right");
            //remove arrow from inventory
            shotRight = true;
            player.setArrow(false);
            moveUp = false;
            moveDown = false;
            moveLeft = false;
            moveRight = false;
            repaint();
            //shotUp = false;
            for(int i = player.getCol(); i <=9 ; i++)
            {
                System.out.println("shoot right");
                if(map.getSquare(player.getRow(), i).isWumpus())
                {
                    System.out.println(player.getRow() + " " + i + "died");
                    map.getSquare(player.getRow(), i).setDeadWumpus(true);
                    map.getSquare(player.getRow(), i).setWumpus(false);
                    wumpusDied = true;
                    wumpusRow = player.getRow();
                    wumpusCol = i;
                    repaint();
                    break;
                }
            }
            //repaint();
        }
        if(keyCode == 80) // p
        {
            System.out.println("try pickup");
            WumpusSquare tmp = map.getSquare(player.getRow(), player.getCol());
            if(tmp.isGold())
            {
                goldPickUp = true;
                goldInInventory = true;
                tmp.setGold(false);
                repaint();
            }
            repaint();
        }
        if(keyCode == 67) // c
        {
            System.out.println("ladder");
            WumpusSquare tmp = map.getSquare(player.getRow(), player.getCol());
            if(tmp.isLadder() && goldInInventory)
            {
                win = true;
                System.out.println("YOU WIN!");
                repaint();
            }
            repaint();
        }
        if(keyCode == 106) // *
        {
            System.out.println("cheat toggle");
            if(cheatToggle == true)
            {
                cheatToggle = false;
                System.out.print(" off");
            }
            else
            {
                cheatToggle = true;
                System.out.print(" on");
            }
            toggleChanged = true;
            //repaint();
            repaint();
        }
        if(keyCode == 78 && (win || lose)) // n
        {
            System.out.println("new game");
            toggleChanged = false;
            cheatToggle = false;
            lose = false;
            win = false;
            resetGame = true;
            moveDown = false;
            moveLeft = false;
            moveRight = false;
            moveUp = false;
            start = true;
            goldInInventory = false;
            for(int s = 0; s < 10; s++){
                for(int c = 0 ; c < 10; c++){
                    moveGrid[s][c] = '-';
                }
            }
            lastMove = 'D';
            reset();
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //ignore ts
    }
}
