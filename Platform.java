/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class handles the platform object. It draws the platform to the canvas.
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

public class Platform extends Entity{
  BufferedImage pipeImage;
  BufferedImage pipeLongerImage;

  /* Platform Constructor */
  public Platform(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    //Reading image sourced from: https://www.youtube.com/watch?v=wT9uNGzMEM4
    try {
      pipeImage = ImageIO.read(getClass().getResource("/Resources/Platform/Platform.png"));
      pipeLongerImage = ImageIO.read(getClass().getResource("/Resources/Platform/PlatformLonger.png"));
    } catch (IOException ex) {
      System.out.println("IOException from Platform Constructor");
    }
  }

  /* Draws the platforms */
  public void draw(Graphics2D g2d) {
    if(width > 500)
      g2d.drawImage(pipeLongerImage, (int) x, (int) (y - 2), (int) width, 30, null); 
    else
      g2d.drawImage(pipeImage, (int) x, (int) (y-2), (int) width, 30, null); 
  }
}




