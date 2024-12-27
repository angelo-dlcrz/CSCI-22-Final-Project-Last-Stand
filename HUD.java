/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class handles the drawing of the display on the top corners. It displays the gun, lives, and ammo of the player.
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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HUD{
  private Player leftPlayer;
  private Player rightPlayer;
  private BufferedImage gunHUD;
  private BufferedImage pistolImage;
  private BufferedImage smgImage;
  private BufferedImage rifleImage;
  private BufferedImage sniperImage;
  private BufferedImage lifeBarImage;
  private BufferedImage ammoCounterImage;
  private int rightHUDX;
  private int origin;

  /* HUD Constructor */
  public HUD(Player p1, Player p2) {
    if(p1.getColor().equals("Blue")){
      leftPlayer = p1;
      rightPlayer = p2;
    }
    else{
      leftPlayer = p2;
      rightPlayer = p1;
    }
    rightHUDX = 625;
    setImages();
  }

  /* Draws the Heads Up Display */
  public void draw(Graphics2D g2d) {
    //Draws the current gun player has
    g2d.drawImage(gunHUD,5, 5, 70, 70, null); 
    g2d.drawImage(gunHUD,rightHUDX + 5, 5, 70, 70, null); 

    setGun(g2d, 1);
    setGun(g2d, 2);
    

    g2d.drawImage(lifeBarImage,90, 8, 242, 30, null); 
    g2d.drawImage(lifeBarImage,rightHUDX + 90, 8, 242, 30, null); 
    g2d.drawImage(ammoCounterImage,90, 40, 65, 40, null); 
    g2d.drawImage(ammoCounterImage,rightHUDX + 90, 40, 65, 40, null); 

    //Draws the life bars
    int firstX = 123;
    int firstY = 13;
    int rWidth = 12;
    int rHeight = 20;

    g2d.setColor(Color.decode("#762A37"));
    for(int i=0; i<10; i++){
      g2d.fillRect(firstX + (i*(rWidth+3)), firstY,rWidth,rHeight);
    }

    for(int i=0; i<10; i++){
      g2d.fillRect(firstX + (i*(rWidth+3)) + rightHUDX, firstY,rWidth,rHeight);
    }

    g2d.setColor(Color.decode("#FF2546"));
    for(int i=0; i<leftPlayer.getLives(); i++){
      g2d.fillRect(firstX + (i*(rWidth+3)), firstY,rWidth,rHeight);
    }
    
    for(int i=0; i<rightPlayer.getLives(); i++){
      g2d.fillRect(firstX + (i*(rWidth+3)) + rightHUDX, firstY,rWidth,rHeight);
    }

    //Writing lives and ammo
    setLivesAndAmmo(g2d, 1);
    setLivesAndAmmo(g2d, 2);
  }

  /* Assigns the images needed 
   * Reading image sourced from: https://www.youtube.com/watch?v=wT9uNGzMEM4
  */
  public void setImages(){
    try {
      gunHUD = ImageIO.read(getClass().getResource("/Resources/HUD/GunHUD.png"));
      pistolImage = ImageIO.read(getClass().getResource("/Resources/Guns/Pistol.png"));
      smgImage = ImageIO.read(getClass().getResource("/Resources/Guns/Smg.png"));
      rifleImage = ImageIO.read(getClass().getResource("/Resources/Guns/Rifle.png"));
      sniperImage = ImageIO.read(getClass().getResource("/Resources/Guns/Sniper.png"));
      lifeBarImage = ImageIO.read(getClass().getResource("/Resources/HUD/LifeBar.png"));
      ammoCounterImage = ImageIO.read(getClass().getResource("/Resources/HUD/AmmoCounter.png"));
    } catch (IOException ex) {
      System.out.println("IOException from setImages()");
    }
  }

  /* Sets the gun to be displayed */
  public void setGun(Graphics2D g2d, int player){
    String gunName;
    if(player == 1){
      origin = 0;
      gunName = leftPlayer.getGun().getGunName();
    }
    else{
      origin = rightHUDX;
      gunName = rightPlayer.getGun().getGunName();
    }

    if(gunName.equals("pistol"))
      g2d.drawImage(pistolImage,origin + 15, 20, 50, 40, null); 
    else if(gunName.equals("smg"))
      g2d.drawImage(smgImage,origin + 15, 20, 50, 40, null); 
    else if(gunName.equals("rifle"))
      g2d.drawImage(rifleImage,origin + 13, 20, 55, 40, null); 
    else if(gunName.equals("sniper"))
      g2d.drawImage(sniperImage,origin + 17, 25, 50, 45, null); 
  }

  /* Sets the lives and ammo to be displayed */
  public void setLivesAndAmmo(Graphics g2d, int player){
    //Writes the lives
    Font oldFont = g2d.getFont();
    String livesString;
    String gunName;
    int lives;
    int ammo;

    g2d.setColor(Color.decode("#FF2546"));

    if(player == 1){
      origin = 0;
      livesString = Integer.toString(leftPlayer.getLives());
      lives = leftPlayer.getLives();
      ammo = leftPlayer.getGun().getAmmo();
      gunName = leftPlayer.getGun().getGunName();
    }
    else{
      origin = rightHUDX;
      livesString = Integer.toString(rightPlayer.getLives());
      lives = rightPlayer.getLives();
      ammo = rightPlayer.getGun().getAmmo();
      gunName = rightPlayer.getGun().getGunName();
    }
    
    g2d.setFont(oldFont.deriveFont(Font.PLAIN, 15));

    if(lives >= 10)
      g2d.drawString(livesString, origin + 290, 28);
    else
      g2d.drawString(livesString, origin + 297, 28);

    
    //Writes Ammo Left
    g2d.setFont(oldFont);
    g2d.setColor(Color.decode("#B39632"));

    if(gunName.equals("pistol")){
      g2d.drawString("--", origin + 122, 64);
    }
    else if(ammo >=10)
      g2d.drawString(Integer.toString(ammo), origin + 122, 64);
    else
      g2d.drawString(Integer.toString(ammo), origin + 127, 64);
  }
}
