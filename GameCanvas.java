/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class is the main canvas of the game. It handles the animation and drawing of the main game and end game screen.
**/
/*
    I have not discussed the Java language code in my program
    with anyone other than my instructor or the teaching assistants
    assigned to this course.

    I have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.

    If any Java language code or documentation used in my program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of my program.
*/

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class GameCanvas extends JComponent {

  private int width, height;

  private Player p1;
  private Player p2;

  private BulletHandler p1BulletHandler;
  private BulletHandler p2BulletHandler;

  private CrateHandler gunCrateHandler;

  private HUD hud;

  private ArrayList<Platform> platformArray;

  private int p2Health;

  /* GameCanvas Constructor */
  public GameCanvas(int width, int height, int playerID) {
    this.width = width;
    this.height = height;

    //Creates the characters depending on the player ID
    if(playerID == 1){
      p1 = new Player(50,220,true, "Blue", true, this);
      p2 = new Player(width - 90,220,false, "Red", false, this);
    }
    else{
      p1 = new Player(width - 90,220,false, "Red", true, this);
      p2 = new Player(50,220,true, "Blue", false, this);
    }

    platformArray = new ArrayList<Platform>();

    generatePlatforms();

    hud = new HUD(p1,p2);

    p1BulletHandler = new BulletHandler(p1, this);
    p2BulletHandler = new BulletHandler(p2, this);

    gunCrateHandler = new CrateHandler(this);
    p2Health = 100;
  }

  /* Draws the objects for the canvas */
  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHints(rh);

    //Game End Screen
    if(p1.isGameOver() || p2.isGameOver()){
      drawEndGame(g2d);
    }
    //Main Game Screen
    else{
      drawGame(g2d);
    }
  }

  /* Draws the main game */
  public void drawGame(Graphics2D g2d){
    //For the background and font
    try {
      BufferedImage background = ImageIO.read(getClass().getResource("/Resources/Background/Background.png"));
      InputStream fontStream = getClass().getResourceAsStream("/Resources/Font/pixelation.ttf");
      Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
      Font font = customFont.deriveFont(Font.PLAIN, 10);
      fontStream.close();
      
      g2d.drawImage(background,0, 0, width, height, null); 
      g2d.setFont(font);
    } catch (IOException ex) {
      System.out.println("IOException from GameCanvas drawGame()");
    } catch (FontFormatException ex) {
      System.out.println("FontFormatException from GameCanvas drawGame()");
    }

    //Draws the platforms
    for(Platform platform : platformArray)
      platform.draw(g2d);

    p1BulletHandler.draw(g2d);
    p2BulletHandler.draw(g2d);
    gunCrateHandler.draw(g2d);
    p2.draw(g2d);
    p1.draw(g2d);
    hud.draw(g2d);
  }

  /* Draws the end game */
  public void drawEndGame(Graphics2D g2d){
    //Draws the GameOver Screen
    BufferedImage gameStartCanvasBG = null;
    try {
      if(p1.isGameOver())
        gameStartCanvasBG = ImageIO.read(getClass().getResource("/Resources/GameOver/GameOverCanvasBG.png"));
      else
        gameStartCanvasBG = ImageIO.read(getClass().getResource("/Resources/GameOver/YouWinCanvasBG.png"));
    } catch (IOException e) {
        System.out.println("IOException on GameCanvas drawEndGame()");
    }
    g2d.drawImage(gameStartCanvasBG, 0, 0, null);
  }

  /* Starts the animation thread */
  public void startAnimation(){
    new AnimationThread();
  }

  /* This inner class handles the Animation of the canvas
   * The animationThread was sourced from: https://www.youtube.com/watch?v=VpH33Uw-_0E
   */
  private class AnimationThread implements Runnable{
    Thread animationThread;

    public AnimationThread(){
      animationThread = new Thread(this);
      animationThread.start();
    }

    //Animation will run on 60 FPS
    @Override
    public void run() {
      double frameInterval = 1000000000/60; //1 second in nanoseconds divided by 60 fps
      double delta = 0;
      long lastTime = System.nanoTime();
      long currentTime;
      while(animationThread != null){
        currentTime = System.nanoTime();

        //Passed time divided by interval when frame should change
        delta += (currentTime - lastTime) / frameInterval;
        lastTime = currentTime;

        //If passed time has elapsed the interval, draw the next frame
        if(delta >= 1){
          //Collision for right and left edges of canvas
          if(p1.getX() + p1.getWidth() >= width) {
            p1.setX(width-p1.getWidth());
          } 
          else if(p1.getX() <= 0) {
            p1.setX(0);
          }
          
          p1.move();
          p1BulletHandler.isColliding(p2);
          p2BulletHandler.isColliding(p1);

          //Synchronizes Player 2 Health in case the health differs from the 2 clients due to lag 
          //(ex. Player 2 teleports instead of dying because Player 1 in other client has already respawned)
          if(p2.getHealth() != p2Health){
            p2.setHealth(p2Health);
          }

          gunCrateHandler.isColliding(p1);
          gunCrateHandler.isColliding(p2);
          p1.isGunNoAmmo();
          p2.isGunNoAmmo();
          p1.isCharacterDead();
          p2.isCharacterDead();
          p1BulletHandler.move();
          p2BulletHandler.move();
          repaint();
          delta--;
        }
      }
    }

  }

  /* Adds the platforms in the platform ArrayList */
  public void generatePlatforms(){
    //row 1
    platformArray.add(new Platform(90, 500, 200, 1));
    platformArray.add(new Platform(380, 500, 200, 1));
    platformArray.add(new Platform(670, 500, 200, 1));
    
    //row 2
    platformArray.add(new Platform(200, 400, 230, 1));
    platformArray.add(new Platform(530, 400, 230, 1));

    //row 3
    platformArray.add(new Platform(0, 300, 290, 1));
    platformArray.add(new Platform(390, 300, 180, 1));
    platformArray.add(new Platform(670, 300, 290, 1));

    //row 4
    platformArray.add(new Platform(200, 200, 560, 1));

    //row 5
    platformArray.add(new Platform(400, 100, 160, 1));
}

  /* Returns the canvas width */
  public double getCanvasWidth() {
    return width;
  }

  /* Returns the canvas height */
  public double getCanvasHeight() {
    return height;
  }

  /* Returns player 1 */
  public Player getPlayer1() {
    return p1;
  }

  /* Returns player 2 */
  public Player getPlayer2() {
    return p2;
  }

  /* Returns the bullet handler for player 1 */
  public BulletHandler getPlayer1BulletHandler(){
    return p1BulletHandler;
  }

  /* Returns the bullet handler for player 2 */
  public BulletHandler getPlayer2BulletHandler(){
    return p2BulletHandler;
  }

  /* Returns the crate handler*/
  public CrateHandler getCrateHandler(){
    return gunCrateHandler;
  }
  /* Returns the platform array */
  public ArrayList<Platform> getPlatformArray(){
    return platformArray;
  }

  /* Sets player 2 health */
  public void setPlayer2Health(int n){
    p2Health = n;
  }
}