// this class is created to create a jPanel
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //it wiil be used for storing the segments of snake body
import java.util.Random; //use to store random X and Y values to place our food on the screen
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {  //basically we are inheriting, we r creating a new class called snakegame that will take on the properties ofJPanel,basically its a version of jPanel with more things that we can add
   private class Tile{                                                          //keyListner is implemented to set the arrow so that we can control the snake
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
   }
   int boardWidth;
   int boardHeight;
   int tileSize = 25;
   
   //snake
   Tile snakeHead;
   ArrayList<Tile> snakeBody; //to store the snack body

   //food
   Tile food;
   Random random;

   //game logic
   Timer gameLoop;
   int velocityX; //to move the snake first statement
   int velocityY;
   boolean gameOver = false;

   SnakeGame(int boardWidth, int boardHeight) {  //construstor
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);  //to listen to the key press
        setFocusable(true);  //to listen to the key press

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX=0;
        velocityY=0; //if velocityY is 1 then it is moving downwards

        gameLoop = new Timer(100, this);  //use implements action listner to the main class
        gameLoop.start();  
   }

   public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
   }
   public void draw(Graphics g) {
    //grid
    //for (int i = 0; i < boardWidth/tileSize; i++) {
        //(x1, y1, x2, y2)
    //    g.drawLine(i*tileSize, 0, i*tileSize, boardHeight); //vertical lines
    //    g.drawLine(0, i*tileSize, boardWidth, i*tileSize);  //horizontal lines
    //}

    //food
    g.setColor(Color.red);
    g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
    g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

    //snake head
    g.setColor(Color.green);
    g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize); // *tilesize brings the green color tile to some position
    g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

    //snakebody
    for(int i = 0; i < snakeBody.size(); i++) {
     Tile snakePart = snakeBody.get(i);
     g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
     g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
    }

    //score
    g.setFont(new Font("Arial", Font.PLAIN, 16));
    if(gameOver) {
     g.setColor(Color.red);
     g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
    }
    else {
     g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
    }

   }
   public void placeFood() {    //this will search x and y coordinates of the food
     food.x = random.nextInt(boardWidth/tileSize);   //600/25 = 24 x position will be any random location from 0 to 24
     food.y = random.nextInt(boardHeight/tileSize);  //600/25 = 24 y position will be any random location from 0 to 24
   }

   public boolean collision(Tile tile1, Tile tile2) {
     return tile1.x == tile2.x && tile1.y == tile2.y;
   }

   public void move() {
     //eat food (for collision of snake with itself)
     if(collision(snakeHead, food)) {
          snakeBody.add(new Tile(food.x, food.y));
          placeFood();
     }

     //snake Body
     for (int i = snakeBody.size()-1; i >= 0; i--) {
          Tile snakePart = snakeBody.get(i);
          if(i == 0) {
               snakePart.x = snakeHead.x;
               snakePart.y = snakeHead.y;
          }
          else {
               Tile prevSnakePart = snakeBody.get(i-1);
               snakePart.x = prevSnakePart.x;
               snakePart.y = prevSnakePart.y;
          }
     }

     //snake head
     snakeHead.x += velocityX; //will be called every 100 milliseconds
     snakeHead.y += velocityY; 

     //game over condition
     for (int i = 0; i < snakeBody.size(); i++) {
          Tile snakePart = snakeBody.get(i);
          //collide with snake head
          if(collision(snakeHead, snakePart)) {
               gameOver = true;
          }
     }
     if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight) {
          gameOver = true;
     }
   }

@Override
public void actionPerformed(ActionEvent e) {
     move();
     repaint();  //repaint basically calls draw method over and over again
     if (gameOver) {        //this will end the game right there
          gameLoop.stop();  //this will end the game right there
     }
}
@Override
public void keyPressed(KeyEvent e) {
     if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {   // this condition && velocityY != 1 is used to move the snake in single direction at a time
          velocityX = 0;
          velocityY = -1;
     }
     else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
          velocityX = 0;
          velocityY = 1;
     }
     else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
          velocityX = -1;
          velocityY = 0;
     }
     else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
          velocityX = 1;
          velocityY = 0;
     }
}

@Override
public void keyTyped(KeyEvent e) {
}

@Override
public void keyReleased(KeyEvent e) {
}
}
