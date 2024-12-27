/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class handles the player object. It contains the attributes of the player, such as health, lives, ammo, and gun.
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
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Player extends Entity{
  private String color;
  private boolean isMainCharacter;
  private GameCanvas gc;
  private boolean up, left, right, down;
  private double speedX;
  private double speedY;
  private double jumpY;
  private double gravityScale;
  private int health;
  private String healthString;
  private int lives;
  private GunSprite gun;
  private BufferedImage playerImage;
  private int imageWidth;
  private int imageHeight;
  private int spriteCounter;
  private int spriteFrame;
  private int spriteState;

  /* Player Constructor */
  public Player(double x, double y, boolean isFacingRight, String color, boolean isMainCharacter, GameCanvas gc) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.isMainCharacter = isMainCharacter;
    this.gc = gc;
    this.up = false;
    this.left = false;
    this.right = false;
    this.isFacingRight = isFacingRight;
    speedValue = 5;
    speedX = speedValue;
    speedY = speedValue;
    gravityScale = 1.3;
    health = 100;
    healthString = "Health: 100/100"; 
    lives = 10;
    gun = new GunSprite("pistol");
    spriteCounter = 0;
    spriteFrame = 1;
    spriteState = 1;
    imageWidth = 48;
    imageHeight = 48;
    this.width = imageWidth*0.85;
    this.height = imageHeight*1.4;
    setCharacterImage(1);
  }

  /* Draws the player and everything linked to it */
  public void draw(Graphics2D g2d) {
    healthBar(g2d);

    if(AnimationSprite() || !isFacingRight){
      //Flips the image horizontally
      //Sourced from: http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Fliptheimagehorizontally.htm
      AffineTransform reflectTransform = AffineTransform.getScaleInstance(-1, 1);
      reflectTransform.translate((-x-playerImage.getWidth()*2+width*0.81), y-height*0.15);
      reflectTransform.scale(2, 2);
  
      g2d.drawImage(playerImage, reflectTransform, null);
    }
    else{
      g2d.drawImage(playerImage, (int) (x-width*0.55), (int) (y-height*0.15), imageWidth*2, imageHeight*2, null);
    }
  }

  /* Handles the animation sprites */
  public boolean AnimationSprite(){
    spriteCounter++;

    //Checks how many frames must load until next the animation frame is loaded
    if(up){
      setCharacterImage(3);

      if(spriteCounter % 30 == 0){
        spriteFrame++;
        spriteCounter = 0;
      }

      return false;
    } 
    else if(right || left){
      setCharacterImage(2);

      if(spriteCounter % 10 == 0){
        spriteFrame++;
        spriteCounter = 0;
      }
      
      if(right){
        return false;
      }
      else{
        return true;
      }
    } 
    else{
      setCharacterImage(1);
      
      if(spriteCounter % 12 == 0){
        spriteFrame++;
        spriteCounter = 0;
      }

      return false;
    }
  }

  /* Draws the health bar above the player
   * Drawing string on canvas was sourced from: http://www.java2s.com/Code/Java/2D-Graphics-GUI/Drawcanvaswithcolorandtext.htm
   */
  public void healthBar(Graphics2D g2d){
    //Writes the health
    FontMetrics fm = g2d.getFontMetrics();
    healthString = health + "/100";

    int healthStringWidth = fm.stringWidth(healthString);
    int healthBarOutlineWidth = 106;
    int healthBarOutlineX = (int) (x - healthBarOutlineWidth/3);
    int healthBarOutlineY = (int) (y - 32);

    g2d.setColor(Color.RED);
    g2d.drawString(healthString, (int) (healthBarOutlineX + healthBarOutlineWidth - healthStringWidth - 3), healthBarOutlineY + 23);

    //Draws the outline and health bar
    Rectangle2D.Double healthBarOutline = new Rectangle2D.Double(healthBarOutlineX,healthBarOutlineY, healthBarOutlineWidth,12);
    Rectangle2D.Double healthBar = new Rectangle2D.Double(x + 1 - 100/3, healthBarOutlineY + 2,health,8);
    g2d.setColor(Color.BLACK);
    g2d.fill(healthBarOutline);
    g2d.setColor(Color.RED);
    g2d.fill(healthBar);
  }

  /* Returns true if player is facing right */
  public boolean getIsFacingRight() {
    return isFacingRight;
  }

  /* Returns color of the player */
  public String getColor(){
    return color;
  }

  /* Returns player health */
  public int getHealth() {
    return health;
  }

  /* Returns player lives */
  public int getLives(){
    return lives;
  }

  /* Returns player gun */
  public GunSprite getGun() {
    return gun;
  }

  /* Sets the animation values for other player in client*/
  public String getAnimationValues(){
    String values = "";
    values = Boolean.toString(up) + "," + Boolean.toString(right) + "," + Boolean.toString(left);
    return values;
  }

  /* Sets player X position */
  public void setX(double n) {
    x = n;
  }

  /* Sets player Y position */
  public void setY(double n) {
    y = n;
  }

  /* Sets animation values of player */
  public void setAnimationValues(boolean up, boolean right, boolean left, boolean isFacingRight){
    this.up = up;
    this.right = right;
    this.left = left;
    this.isFacingRight = isFacingRight;
  }

  /* Sets player health */
  public void setHealth(int n){
    health = n;
  }

  /* Sets player lives */
  public void setLives(int n){
    lives = n;
  }

  /* Adds/Subtracts player health */
  public void addHealth(int n){
    health += n;
  }

  /* Sets player gun */
  public void setGun(String gunName){
    gun.changeGun(gunName);
  }

  /* Applies powerup to player */
  public void applyPowerUp(String powerUpName){
    if(powerUpName.equals("heal"))
      health = 100;
    else if(powerUpName.equals("oneUp")){
      if(lives < 10 && lives > 0)
        lives++;
    }
  }

  /* Handles the movement of player in canvas and the collision for platforms*/
  public void move() {
    if(up) {
      //Jumps until a certain height then falls down
      speedY = speedValue*gravityScale;
      y -= speedY;
      if(jumpY - y >= height*2)
        up = false;
    }
    if(left) {
      x -= speedX;
      if(up != true)
        y += speedY;
    } 
    else if(right) {
      x += speedX;
      if(up != true)
        y += speedY;
    } 
    else if(up != true){
      y += speedY;
    }
  
    resolvePlatformCollisions(gc.getPlatformArray());
  }

  /* Handles the key event sent from the GameFrame */
  public void setDirection(String dir) {
    if(dir.equals("up")) {
      if(speedY == 0){
        jumpY = y;
        up = true;
      }

      if(spriteState != 3)
        spriteFrame = 1;
    } else if (dir.equals("left")) {
      if(speedX < 0)
        speedX *= -1;
      left = true;
      right = false;
      isFacingRight = false;
      
      if(spriteState != 2)
        spriteFrame = 1;
    } else if (dir.equals("right")) {
      if(speedX < 0)
        speedX *= -1;
      left = false;
      right = true;
      isFacingRight = true;

      if(spriteState != 2)
        spriteFrame = 1;
    } else if (dir.equals("down")) {
      down = true;
    } else if (dir.equals("downReleased")) {
      down = false;
    } else {
      left = false;
      right = false;
      down = false;

      if(spriteState != 1)
        spriteFrame = 1;
    }
  }

  /* Sets the gun to pistol if ammo reaches 0 */
  public void isGunNoAmmo(){
    if(gun.getAmmo() <= 0){
      gun.changeGun("pistol");
    }
  }

  /* Checks if player has died */
  public void isCharacterDead(){
    if(health <= 0 || y > gc.getHeight()){
      health = 100;
      lives--;
      setGun("pistol");

      if(isMainCharacter){
        int platformPosition = ((int) (Math.random()*(9-0)) + 0); //Generates a number from 0 - 9 (10 platforms)
        Platform platform = gc.getPlatformArray().get(platformPosition);
        x = (int) (Math.random()*((platform.getX()+platform.getWidth()-width)-platform.getX()) + platform.getX()); //Random x coordinate from platform x to platform width minus player width
        y = platform.getY() - height;
      }
    }
  }

  /* Checks if player has no lives remaining */
  public boolean isGameOver(){
    return lives < 0;
  }

  /* Sets the animations for the character sprite
   * Reading image sourced from: https://www.youtube.com/watch?v=wT9uNGzMEM4
   */
  public void setCharacterImage(int spriteState){
    BufferedImage playerFile = null;
    this.spriteState = spriteState;
    try {
      switch (spriteState) {
        case 1:
          playerFile = ImageIO.read(getClass().getResource("/Resources/" + color + "/Character_Idle.png"));
          if(spriteFrame > 5)
            spriteFrame = 1;
          break;
        case 2:
          playerFile = ImageIO.read(getClass().getResource("/Resources/" + color + "/Character_Run.png"));
          if(spriteFrame > 6)
            spriteFrame = 1;
          break;
        case 3:
          playerFile = ImageIO.read(getClass().getResource("/Resources/" + color + "/Character_Jump.png"));
          if(spriteFrame > 2)
            spriteFrame = 1;
          break;
      }
      playerImage = playerFile.getSubimage((imageWidth * spriteFrame)-imageWidth,0,imageWidth,imageHeight);
    } catch (IOException ex) {
        System.out.println("IOException from setCharacterImage");
    }
  }

  /* Checks for player collision with platform */
  public boolean checkCollision(Platform platform){
    if(y + height < platform.getY()){
        return false;
    }
    else{
        return ((x > platform.getX() && x < platform.getX() + platform.getWidth()) || (x + width > platform.getX() && x + width < platform.getX() + platform.getWidth()));
    }
  }

  /* Maintains the player on the platform if player is colliding with it 
   * Sourced from: https://www.youtube.com/watch?v=nFOlo6F60FA
  */
  public void resolvePlatformCollisions(ArrayList<Platform> platformList){
    for (Platform platform: gc.getPlatformArray()){
      if (checkCollision(platform) && !up && (y + height*.8 <= platform.getY()) && !down){
        speedY = 0;
        y = platform.getY() - height;
        break;
      }
      else
        speedY = speedValue*gravityScale;
    }
  }
}
