/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class handles the attributes and methods of the Crate object. It also draws the crate.
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
import javax.imageio.ImageIO;
import java.io.IOException;

public class CrateSprite extends Entity{
  private CrateHandler gch;
  private String storedInCrate;
  private int cratePosition;
  private boolean isGunCrate;
  private BufferedImage gunCrateImage;
  private BufferedImage powerUpCrateImage;
  private BufferedImage gunImage;
  private BufferedImage powerUpImage;
  private int spriteFrame;
  private int spriteCounter;
  private int imageX;
  private int imageY;
  private int imageWidth;
  private int imageHeight;
  private boolean triggerAnimation;

  /* CrateSprite Constructor */
  public CrateSprite(double x, double y, CrateHandler gch, String storedInCrate, int cratePosition, boolean isGunCrate) {
    this.x = x;
    this.y = y;
    this.gch = gch;
    this.storedInCrate = storedInCrate;
    this.cratePosition = cratePosition;
    this.isGunCrate = isGunCrate;
    imageX = (int) x;
    imageY = (int) y;
    imageWidth = 64;
    imageHeight = 64;
    this.width = imageWidth*0.6-2;
    this.height = imageHeight*0.7-1;
    spriteFrame = 1;
    spriteCounter = 0;
    triggerAnimation = false;
    setCrateImage();
  }

  /* Draws the crates 
   * Drawing string on canvas was sourced from: http://www.java2s.com/Code/Java/2D-Graphics-GUI/Drawcanvaswithcolorandtext.htm
  */
  public void draw(Graphics2D g2d) {
    if(isGunCrate){
      if(triggerAnimation){
        //Writes the gun name on top of crate
        Font oldFont = g2d.getFont();
        g2d.setFont(oldFont.deriveFont(Font.BOLD, 15));
        FontMetrics fm = g2d.getFontMetrics();
        int stringWidth = fm.stringWidth(storedInCrate);
        g2d.setColor(Color.WHITE);
        g2d.drawString(storedInCrate.toUpperCase(), (int) (imageX + width/2 - stringWidth/2), (int) (imageY - 10));
        g2d.setFont(oldFont);
        
        //Draws the gun in crate
        setGunImage(g2d);      
  
        //Checks how many frames must load until the next animation frame is loaded
        spriteCounter++;
        if(spriteCounter % 4 == 0){
          setCrateImage();
          spriteFrame++;
          spriteCounter = 0;
        }
      }
  
      if(spriteFrame > 6){
        spriteFrame = 0;
        triggerAnimation = false;
        gch.removeCrate(this);
      }
  
      g2d.drawImage(gunCrateImage, (int) imageX - 23, (int) imageY - 38, (int) (imageWidth*1.3), (int) (imageHeight*1.3), null); 
    }
    else{
      if(triggerAnimation){
        //Writes the powerup name on top of crate
        Font oldFont = g2d.getFont();
        g2d.setFont(oldFont.deriveFont(Font.BOLD, 15));
        FontMetrics fm = g2d.getFontMetrics();
        int stringWidth = fm.stringWidth(storedInCrate);
        g2d.setColor(Color.WHITE);
        g2d.drawString(storedInCrate.toUpperCase(), (int) (imageX + width/2 - stringWidth/2), (int) (imageY - 40));
        g2d.setFont(oldFont);
        
        //Draws the powerUp in crate
        setPowerUpImage(g2d);      
  
        //Checks how many frames must load until the next animation frame is loaded
        spriteCounter++;
        if(spriteCounter % 4 == 0){
          setCrateImage();
          spriteFrame++;
          spriteCounter = 0;
        }
      }
  
      if(spriteFrame > 4){
        spriteFrame = 0;
        triggerAnimation = false;
        gch.removeCrate(this);
      }
      g2d.drawImage(powerUpCrateImage, (int) imageX, (int) imageY+1, (int) (imageWidth*0.55), (int) (imageHeight*0.65), null);
    }
    
  }

  /* Sets the animations for the crate sprite
   * Reading image sourced from: https://www.youtube.com/watch?v=wT9uNGzMEM4
   */
  public void setCrateImage(){
    try {
      BufferedImage crateFile;
      if(isGunCrate)
        crateFile = ImageIO.read(getClass().getResource("/Resources/Crates/GunCrate.png"));
      else
        crateFile = ImageIO.read(getClass().getResource("/Resources/Crates/PowerUp.png"));

      //Capitalizing the first letter sourced from: https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java
      String tempName = storedInCrate.substring(0, 1).toUpperCase() + storedInCrate.substring(1);

      if(isGunCrate){
        if(spriteFrame > 3)
          gunCrateImage = crateFile.getSubimage((imageWidth * (spriteFrame-3))-imageWidth,imageHeight,imageWidth,imageHeight);
        else
          gunCrateImage = crateFile.getSubimage((imageWidth * spriteFrame)-imageWidth,0,imageWidth,imageHeight);
        
        gunImage = ImageIO.read(getClass().getResource("/Resources/Guns/" + tempName + ".png"));
      }
      else{
        powerUpCrateImage = crateFile.getSubimage((40 * (spriteFrame))-40,0,40,40);
        powerUpImage = ImageIO.read(getClass().getResource("/Resources/PowerUps/" + tempName + ".png"));
      }
      
    } catch (IOException ex) {
        System.out.println("IOException from setCrateImage");
    }
  }

  /* Sets the images for the guns in crate
   * Reading image sourced from: https://www.youtube.com/watch?v=wT9uNGzMEM4
   */
  public void setGunImage(Graphics2D g2d){
    if(storedInCrate.equals("smg"))
      g2d.drawImage(gunImage,(int) (imageX - 8), (int) (imageY + 5), 50, 40, null); 
    else if(storedInCrate.equals("rifle"))
      g2d.drawImage(gunImage,(int) (imageX - 8), (int) (imageY + 5), 55, 40, null); 
    else if(storedInCrate.equals("sniper"))
      g2d.drawImage(gunImage,(int) (imageX - 5), (int) (imageY + 5), 50, 45, null); 
  }

  /* Sets the images for the powerups in crate
   * Reading image sourced from: https://www.youtube.com/watch?v=wT9uNGzMEM4
   */
  public void setPowerUpImage(Graphics2D g2d){
    if(storedInCrate.equals("heal"))
      g2d.drawImage(powerUpImage,(int) (imageX + 3), (int) (imageY - 30), 30, 30, null); 
    else if(storedInCrate.equals("oneUp"))
      g2d.drawImage(powerUpImage,(int) (imageX + 2), (int) (imageY - 25), 40, 30, null); 
  }

  /* Triggers the breaking/shrinking animation */
  public void setAnimation(){
    triggerAnimation = true;
    x = -100;
    y = -100;
  }

  /* Returns the name of gun/powerup stored in crate */
  public String getStoredInCrateName(){
    return storedInCrate;
  }

  /* Returns the position of crate in the ArrayList in the crateHandler */
  public int getCratePosition(){
    return cratePosition;
  }

  /* Returns true if gun is stored in crate */
  public boolean getIsGunCrate(){
    return isGunCrate;
  }
}





