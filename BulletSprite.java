/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class handles the attributes and methods of the Bullet object. It also draws the bullet.
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
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class BulletSprite extends Entity{
  private GunSprite gunFired;
  private BufferedImage bulletImage;

  /* BulletSprite Constructor */
  public BulletSprite(double x, double y, boolean bulletFacingRight, String gunFired) {
    this.x = x;
    this.y = y;
    this.width = 28;
    this.height = 5;
    this.isFacingRight = bulletFacingRight;
    this.gunFired = new GunSprite(gunFired);
    speedValue = 10;
    setBulletImage();
  }

  /* Draws the bullets */
  public void draw(Graphics2D g2d) {
    if(isFacingRight)
      g2d.drawImage(bulletImage, (int) x-4, (int) y - 7,40,20, null);
    else{
      //Flips the image horizontally
      //Sourced from: http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Fliptheimagehorizontally.htm
      AffineTransform reflectTransform = AffineTransform.getScaleInstance(-1, 1);
      reflectTransform.translate((-x-32), y-7);
      reflectTransform.scale(.16, 0.14);
  
      g2d.drawImage(bulletImage, reflectTransform, null);
    }
  }
  
  /* Returns true if bullet is facing right */
  public boolean getIsFacingRight() {
    return isFacingRight;
  }

  /* Returns the gun the bullet was fired from */
  public GunSprite getGun(){
    return gunFired;
  }

  /* Moves the bullet horizontally */
  public void move() {
    if(isFacingRight)
      x += speedValue;
    else
      x -= speedValue;
  }

  /* Sets the image for the bullet 
   * Reading image sourced from: https://www.youtube.com/watch?v=wT9uNGzMEM4
  */
  public void setBulletImage(){
    try {
      bulletImage = ImageIO.read(getClass().getResource("/Resources/Guns/Bullet.png"));
    } catch (IOException ex) {
        System.out.println("IOException from setCrateImage");
    }
  }

}
