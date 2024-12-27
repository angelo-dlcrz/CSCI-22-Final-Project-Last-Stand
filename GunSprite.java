/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class handles the gun object. This is where the attributes of the gun can be found and altered.
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

public class GunSprite {
  private String gunName;
  private int fireRate;
  private int damage;
  private int ammo;

  /* GunSprite Constructor */
  public GunSprite(String gunName) {
    this.gunName = gunName;
    setAttributes();
  }

  /* Sets the attributes accoring to the gun */
  public void setAttributes(){
    if(gunName.equals("pistol")){
      fireRate = 500;
      damage = 5;
      ammo = 12;
    }
    else if(gunName.equals("smg")){
      fireRate = 150;
      damage = 7;
      ammo = 30;
    }
    else if(gunName.equals("rifle")){
      fireRate = 250;
      damage = 12;
      ammo = 24;
    }
    else if(gunName.equals("sniper")){
      fireRate = 1200;
      damage = 90;
      ammo = 3;
    }
  }

  /* Returns the gun name */
  public String getGunName(){
    return gunName;
  }

  /* Returns the fire rate */
  public int getFireRate(){
    return fireRate;
  }

  /* Returns the damage */
  public int getDamage(){
    return damage;
  }

  /* Returns the ammo */
  public int getAmmo(){
    return ammo;
  }

  /* Changes the gun */
  public void changeGun(String gun){
    gunName = gun;
    setAttributes();
  }

  /* Subtracts the ammo of gun */
  public void minusAmmo(){
    ammo--;
  }

  /* Sets the ammo of the gun */
  public void setAmmo(int n){
    ammo = n;
  }
}


