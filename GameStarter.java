import java.util.Scanner;

/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

Starts the client. It calls the necessary methods needed in the GameFrame.
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

public class GameStarter {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter IP Address: ");
    String ipAdd = sc.next();
    sc.close();
    GameFrame gf = new GameFrame(960,540, ipAdd);
    gf.connectToServer();
    gf.setUpGUI();
    gf.addKeyBindings();
  }
}
