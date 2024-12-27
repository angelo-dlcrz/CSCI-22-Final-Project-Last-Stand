/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class handles the connection of client to server, key handling, and setting up of gui.
It also calls the methods that sets attributes of the second player.
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
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class GameFrame {
  private JFrame frame;
  private GameCanvas gc;
  private int width;
  private int height;
  private String ipAdd;
  private Socket s;
  private int playerID;
  private ReadFromServer rfsRunnable;
  private WriteToServer wtsRunnable;
  private String[] player2Data;

  /* ClientFrame Constructor */
  public GameFrame(int w, int h, String ipAddress) {
    width = w;
    height = h;
    ipAdd = ipAddress;
    frame = new JFrame();
  }

  /* Sets Up the Frame and Canvas */
  public void setUpGUI() {
    frame.setTitle("The Last Stand: Player " + playerID);
    gc = new GameCanvas(width,height,playerID);
    gc.setPreferredSize(new Dimension(width,height));
    frame.add(gc);
    frame.pack();
    rfsRunnable.waitForStartMessage();
    gc.startAnimation();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  /* Sets up the key listeners for character movement */
  public void addKeyBindings() {
    JPanel cp = (JPanel) frame.getContentPane();
    cp.setFocusable(true);

    ShootBullets sb = new ShootBullets();
    Thread sbThread = new Thread(sb);
    sbThread.start();

    KeyListener kl = new KeyListener() {

      @Override
      public void keyTyped(KeyEvent ke) {
      }

      @Override
      public void keyPressed(KeyEvent ke) {
        //Player moves when the key is pressed
        int keyCode = ke.getKeyCode();

        switch (keyCode){
          case KeyEvent.VK_UP :
            gc.getPlayer1().setDirection("up");
            break;
          case KeyEvent.VK_LEFT :
            gc.getPlayer1().setDirection("left");
            break;
          case KeyEvent.VK_RIGHT :
            gc.getPlayer1().setDirection("right");
            break;
          case KeyEvent.VK_DOWN :
            gc.getPlayer1().setDirection("down");
            break;
          case KeyEvent.VK_SPACE :
            sb.setSpaceBarPressed(true);
            break;
        }
      }

      @Override
      public void keyReleased(KeyEvent ke) {
        //Player stops when key is released
        int keyCode = ke.getKeyCode();

        switch (keyCode){
          case KeyEvent.VK_LEFT :
            gc.getPlayer1().setDirection("");
            break;
          case KeyEvent.VK_RIGHT :
            gc.getPlayer1().setDirection("");
            break;
          case KeyEvent.VK_DOWN :
            gc.getPlayer1().setDirection("downReleased");
            break;
          case KeyEvent.VK_SPACE :
            sb.setSpaceBarPressed(false);
            break;
        }
      }
    };

    cp.addKeyListener(kl);
  }

  /* This inner class is for the thread that
    handles the continuous shooting of bullets when pressing/holding spacebar */
  private class ShootBullets implements Runnable{
    private long lastSpaceBarTime;
    private long currentTime;
    private boolean isSpaceBarPressed;

    /* ShootBullets Constructor */
    public ShootBullets(){
      lastSpaceBarTime = 0;
      isSpaceBarPressed = false;
    }

    /* Contains what will be run in the thread */
    @Override
    public void run() {
      while(true){
        if(isCooldownOver() && isSpaceBarPressed){
          gc.getPlayer1BulletHandler().addBullet(gc.getPlayer1().getIsFacingRight());
          wtsRunnable.setBulletCreated();
          lastSpaceBarTime = System.currentTimeMillis(); // Update last spacebar time
        }

        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          System.out.println("InterruptedException in ShootBullets run()");
        }
      }
    }

    /* Checks if firerate time has passed and bullet may be shot again */
    public boolean isCooldownOver() {
      //Checks if fire rate time has passed
      // Getting System Time sourced from: https://www.tutorialspoint.com/java/lang/system_currenttimemillis.htm
      currentTime = System.currentTimeMillis();
      return currentTime - lastSpaceBarTime > gc.getPlayer1().getGun().getFireRate();
    }

    /* isPaceBarPressed is true when spacebar is pressed */
    public void setSpaceBarPressed(boolean pressed){
      isSpaceBarPressed = pressed;
    }
    
  }

  /* Handles the initial connection to server */
  public void connectToServer(){
    try{
      s = new Socket(ipAdd,55555);
      DataInputStream in = new DataInputStream(s.getInputStream());
      DataOutputStream out = new DataOutputStream(s.getOutputStream());

      playerID = in.readInt();
      System.out.println("You are Player " + playerID);

      if(playerID == 1)
        System.out.println("Waiting for Player 2 to connect...");

      //Creates the class for writing and reading threads
      rfsRunnable = new ReadFromServer(in);
      wtsRunnable = new WriteToServer(out);

      } catch(IOException ex){
        System.out.println("IOException from connectToServer()");
      }
  }

  /* Inner class for receiving data from server */
  private class ReadFromServer implements Runnable{
    private DataInputStream dataIn;

    /* ReadFromServer Constructor */
    public ReadFromServer(DataInputStream dataIn){
      this.dataIn = dataIn;
    }

    /* Contains what will be run in the thread */
    @Override
    public void run() {
      try {
        while(true){
          if(gc != null){
            /*
            Receives the data and splits in according to the needed data:
            [0] X Position,[1] Y Position,[2] Is Bullet Fired,[3] Is Character Facing Right Direction, 
            [4] Health, [5] Lives, [6] Gun Name, [7] Gun Ammo,[8] Up,[9] Right,[10] Left, 
            [11] Should Crate be Spawned, [12] Gun in Crate, [13] Gun Crate Position. [14] Gun Crate Spawn Point
            [15] Should PowerUp be Spawned [16] PowerUp in Crate, [17] PowerUp Crate Position, [18] PowerUp Crate Spawn Point
            */
            String dataReceived = dataIn.readUTF();
            player2Data = dataReceived.split(",");
            if(dataReceived.length() != 0){  
              //Sets Coordinates of Player 2
              gc.getPlayer2().setX(Double.parseDouble(player2Data[0]));
              gc.getPlayer2().setY(Double.parseDouble(player2Data[1]));

              //Checks if Player 2 shot a bullet
              if(Boolean.parseBoolean(player2Data[2]))
                gc.getPlayer2BulletHandler().addBullet(Boolean.parseBoolean(player2Data[3]));

              //Sends Player 2 Health to canvas
              gc.setPlayer2Health(Integer.parseInt(player2Data[4]));

              // //Sets Player 2 Lives
              gc.getPlayer2().setLives(Integer.parseInt(player2Data[5]));

              //Sets Player 2 Gun
              gc.getPlayer2().setGun(player2Data[6]);

              //Sets Player 2 Ammo
              gc.getPlayer2().getGun().setAmmo(Integer.parseInt(player2Data[7]));

              //Sets Animation Values for Player 2 Character Sprite
              gc.getPlayer2().setAnimationValues(Boolean.parseBoolean(player2Data[8]), Boolean.parseBoolean(player2Data[9]), Boolean.parseBoolean(player2Data[10]), Boolean.parseBoolean(player2Data[3]));

              //Checks if server has sent signal to spawn crate
              if(Boolean.parseBoolean(player2Data[11]) && player2Data.length > 12){
                gc.getCrateHandler().addCrate(player2Data[12], Integer.parseInt(player2Data[13]), Double.parseDouble(player2Data[14]),Boolean.parseBoolean(player2Data[15]),player2Data[16],Integer.parseInt(player2Data[17]),Double.parseDouble(player2Data[18]));
              }
            }
            
          }
        }
      } catch (IOException ex) {
          System.out.println("IOException from ReadFromServer run()");
      } 
    }

    /* Waits for the start message from the server */
    public void waitForStartMessage(){
      try {
        String msg = dataIn.readUTF();
        System.out.println("Message from server: " + msg);

        //Starts the threads after both players have connected
        Thread readThread = new Thread(rfsRunnable);
        Thread writeThread = new Thread(wtsRunnable);
        readThread.start();
        writeThread.start();
      } catch (IOException ex) {
          System.out.println("IOException from ReadFromServer waitForStartMessage()");
      }
    }
  }

  /* Inner class for sending data to server */
  private class WriteToServer implements Runnable{
    private DataOutputStream dataOut;
    private Boolean bulletCreated;

    /* WriteToServer Constructor */
    public WriteToServer(DataOutputStream dataOut){
      this.dataOut = dataOut;
      bulletCreated = false;
    }

    /* Contains what will be run in the thread */
    @Override
    public void run() {
      try {
        while(true){
          if(gc != null){
            //Sends the data and splits in according to the needed data:
            //X Position, Y Position, Is Bullet Fired, Is Player Facing Right Direction, Health,
            //Lives,Player Gun, Up, Right, Left,
            String dataSent = gc.getPlayer1().getX() + "," + gc.getPlayer1().getY() + "," + 
            bulletCreated + "," + gc.getPlayer1().getIsFacingRight() + "," + gc.getPlayer1().getHealth()
            + "," + gc.getPlayer1().getLives() + "," + gc.getPlayer1().getGun().getGunName() + "," + gc.getPlayer1().getGun().getAmmo()
            + "," + gc.getPlayer1().getAnimationValues(); 
            
            dataOut.writeUTF(dataSent);
            dataOut.flush();
            bulletCreated = false;
          }

          try {
            Thread.sleep(25);
          } catch (InterruptedException ex) {
            System.out.println("InterruptedException from WriteToServer run()");
          }
        }
      } catch (IOException ex) {
        System.out.println("IOException from WriteToServer run()");
      }
    }

    /* Sets bulletCreated to true when bullet is shot */
    public void setBulletCreated(){
      bulletCreated = true;
    }
  }
}
