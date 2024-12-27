/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class is for handling the arraylist of crates. It handles the drawing of the crates
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class CrateHandler {
  private ArrayList<CrateSprite> crateArray;
  private CrateSprite tempCrate;
  private ArrayList<Integer> cratePositionArray;
  private ArrayList<Platform> platformArray;
  private Timer spawnBuffer;
  private boolean canSpawnCrate;

  /* CrateHandler Constructor */
  public CrateHandler(GameCanvas gc) {
    crateArray = new ArrayList<CrateSprite>();
    cratePositionArray = new ArrayList<Integer>();
    platformArray = gc.getPlatformArray();

    for(int i=0; i<platformArray.size();i++){
      cratePositionArray.add(i);
    }

    //Prevents spawning of more that 1 crate at once due to lag
    canSpawnCrate = true;

    spawnBuffer = new Timer(1500, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        canSpawnCrate = true;
      }

    });
  }

  /* Draws the crates in the ArrayList */
  public void draw(Graphics2D g2d) {
    for(int i=0; i<crateArray.size();i++){
      tempCrate = crateArray.get(i);
      tempCrate.draw(g2d);
    }
  }

  /* Handles the collision logic of the crates to the player */
  public void isColliding(Player player){
    for(int i=0; i<crateArray.size();i++){
      tempCrate = crateArray.get(i);

      //Checks if crate is in range of Target Player height
      if(tempCrate.getY() + tempCrate.getHeight() >= player.getY() && tempCrate.getY() <= player.getY() + player.getHeight()){
        //Left Collision
        if(player.getX()+player.getWidth() >= tempCrate.getX() && player.getX()+player.getWidth() <= tempCrate.getX() + tempCrate.getWidth()){
          if(tempCrate.getIsGunCrate())
            player.setGun(tempCrate.getStoredInCrateName());
          else
            player.applyPowerUp(tempCrate.getStoredInCrateName());

          cratePositionArray.add(tempCrate.getCratePosition());
          tempCrate.setAnimation();
        }
        //Right Collision
        else if(player.getX() <= tempCrate.getX() + tempCrate.getWidth() && player.getX() >= tempCrate.getX()){
          if(tempCrate.getIsGunCrate())
            player.setGun(tempCrate.getStoredInCrateName());
          else
            player.applyPowerUp(tempCrate.getStoredInCrateName());

          cratePositionArray.add(tempCrate.getCratePosition());
          tempCrate.setAnimation();
        }
      }
    }
  }

  /* Adds the crate to the ArrayList */
  public void addCrate(String gunInCrate, int gunPosition, double gunRandomNumber, boolean shouldPowerSpawn, String powerInCrate, int powerPosition, double powerRandomNumber){
    if(canSpawnCrate){
      canSpawnCrate = false;
      boolean isGunPositionAvailable = false;
      boolean isPowerPositionAvailable = false;

      //Determines if there are available platforms to spawn crates
      //If the given crate position is not available, the crate will spawn in next crate position
      while(cratePositionArray.size() != 0){ //If size = 0, no available platforms for spawning
        //For Spawning Gun Crate
        for(int i=0; i < cratePositionArray.size(); i++){
          if(gunPosition == cratePositionArray.get(i)){
            cratePositionArray.remove(i);
            isGunPositionAvailable = true;
            int crateX = (int) (gunRandomNumber*((platformArray.get(gunPosition).getX() + platformArray.get(gunPosition).getWidth()-36)-platformArray.get(gunPosition).getX()) + platformArray.get(gunPosition).getX()); //Generates a random x position based on the platform
            crateArray.add(new CrateSprite(crateX, platformArray.get(gunPosition).getY() - 43, this, gunInCrate, gunPosition, true));
            break;
          }
        }

        //For Spawning PowerUp Crate
        if(shouldPowerSpawn){
          for(int i=0; i < cratePositionArray.size(); i++){
            if(powerPosition == cratePositionArray.get(i)){
              cratePositionArray.remove(i);
              isPowerPositionAvailable = true;
              int crateX = (int) (powerRandomNumber*((platformArray.get(powerPosition).getX() + platformArray.get(powerPosition).getWidth()-36)-platformArray.get(powerPosition).getX()) + platformArray.get(powerPosition).getX()); //Generates a random x position based on the platform
              crateArray.add(new CrateSprite(crateX, platformArray.get(powerPosition).getY() - 43, this, powerInCrate, powerPosition, false));
              break;
            }
          }
        }

        if(!isGunPositionAvailable){
          if(gunPosition == platformArray.size())
            gunPosition = 0;
          else
            gunPosition++;
        }
        else if(!isPowerPositionAvailable && shouldPowerSpawn){
          if(powerPosition == platformArray.size())
            powerPosition = 0;
          else
            powerPosition++;
        }
        else
          break;
      }
      spawnBuffer.start();
    }
  }

  public void removeCrate(CrateSprite g){
    crateArray.remove(g);
  }
}
