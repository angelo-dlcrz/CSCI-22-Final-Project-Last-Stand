/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This is the superclass for the enitites in the game. All the entities in the game have similar attributes
and is stored in this class.
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

public class Entity {
  protected double x;
  protected double y;
  protected double width;
  protected double height;
  protected double speedValue;
  protected boolean isFacingRight;

  /* Returns x value of the entity */
  public double getX() {
    return x;
  }

  /* Returns y value of the entity */
  public double getY() {
    return y;
  }

  /* Returns width of the entity */
  public double getWidth() {
    return width;
  }

  /* Returns height of the entity */
  public double getHeight() {
    return height;
  }
}
