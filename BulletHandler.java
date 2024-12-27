/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class is for handling the arraylist of bullets. It handles the drawing of the bullets
and its collision with the players.
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
import java.util.ArrayList;

public class BulletHandler {
  private ArrayList<BulletSprite> bulletArray;
  private BulletSprite tempBullet;
  private Player player;
  private GameCanvas gc;
  private boolean bulletFacingRight;

  /* BullerHandler Constructor 
   * The idea for this class was based from youtube: https://www.youtube.com/watch?v=FjTDgspqIBo
   * Excluding the collision logic
  */
  public BulletHandler(Player player, GameCanvas gc) {
    bulletArray = new ArrayList<BulletSprite>();
    this.player = player;
    this.gc = gc;
    bulletFacingRight = false;
  }

  /* Draws the bullets in the ArrayList */
  public void draw(Graphics2D g2d) {
    for(int i=0; i<bulletArray.size();i++){
      tempBullet = bulletArray.get(i);
      tempBullet.draw(g2d);
    }
  }

  /* Handles the updating of the bullet positions */
  public void move() {
    for(int i=0; i<bulletArray.size();i++){
      tempBullet = bulletArray.get(i);

      //Checks if bullet is beyond the canvas
      if(tempBullet.getX() >= gc.getCanvasWidth())
        removeBullet(tempBullet);
      else if(tempBullet.getX()+tempBullet.getWidth() <= 0)
        removeBullet(tempBullet);

      tempBullet.move();
    }    
  }

  /* Handles the collision logic of the bullets to the player */
  public void isColliding(Player playerTarget) {
    for(int i=0; i<bulletArray.size();i++){
      tempBullet = bulletArray.get(i);
      //Checks if bullet is in range of Target Player height
      if(tempBullet.getY() + tempBullet.getHeight() >= playerTarget.getY() && tempBullet.getY() <= playerTarget.getY() + playerTarget.getHeight()){
        //Checks if bullet is going right
        if(tempBullet.getIsFacingRight()){
          if(tempBullet.getX() + tempBullet.getWidth() >= playerTarget.getX() && tempBullet.getX() + tempBullet.getWidth() <= playerTarget.getX() + playerTarget.getWidth()){
            playerTarget.addHealth(-tempBullet.getGun().getDamage());
            removeBullet(tempBullet);
          }
        }
        else{
          //Checks if bullet is going left
          if(tempBullet.getX() >= playerTarget.getX() && tempBullet.getX() <= playerTarget.getX() + playerTarget.getWidth()){
            playerTarget.addHealth(-tempBullet.getGun().getDamage());
            removeBullet(tempBullet);
          }
        }
      }
    }
  }

  /* Adds the bullet to the ArrayList */
  public void addBullet(boolean direction){
    bulletFacingRight = direction;
      if(bulletFacingRight){
        bulletArray.add(new BulletSprite(player.getX()+player.getWidth()-10,player.getY()+player.getHeight()/3+8,bulletFacingRight, player.getGun().getGunName()));
        player.getGun().minusAmmo();
      }
      else{
        bulletArray.add(new BulletSprite(player.getX()-10,player.getY()+player.getHeight()/3+8,bulletFacingRight, player.getGun().getGunName()));
        player.getGun().minusAmmo();
      }
  }

  /* Removes the bullet from the ArrayList */
  public void removeBullet(BulletSprite b){
    bulletArray.remove(b);
  }

  /* Returns the bullet ArrayList */
  public ArrayList<BulletSprite> getBulletArray(){
    return bulletArray;
  }
}
