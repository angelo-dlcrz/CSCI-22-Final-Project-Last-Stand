/**
@author Neil Dustin Benedict A. Agner (220129) & Angelo B. Dela Cruz (222086)
@version April 22, 2023

This class handles the server part. It sends and receives data from the client. It also handles the
random number generation for the guns and crates.
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

import java.net.*;
import java.io.*;

public class GameServer{
    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;

    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private WriteToClient p1WriteRunnable;
    private WriteToClient p2WriteRunnable;

    private String p1Data, p2Data;
    private boolean spawnCrate, p1Sent, p2Sent;
    private String crateData;
    private int crateCounter;
    private boolean spawnPowerUp;

    private boolean bulletShot1;
    private boolean bulletShot2;

    /* GameServer Constructor */
    public GameServer(){
        System.out.println("===== GAME SERVER =====");
        numPlayers = 0;
        maxPlayers = 2;
        p1Data = "";
        p2Data = "";
        spawnCrate = false;
        p1Sent = false;
        p2Sent = false;
        crateData = "";
        crateCounter = 1;
        spawnPowerUp = false;
        bulletShot1 = false;
        bulletShot2 = false;

        try{
            ss = new ServerSocket(55555);
        } catch(IOException ex){
            System.out.println("IOException from GameServer Constructor");
        }
    }

    /* Accepts connections from the clients */
    public void acceptConnections(){
        try {
            System.out.println("Waiting for connections...");

            while(numPlayers < maxPlayers){
                Socket s = ss.accept();
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player " + numPlayers + " has connected.");

                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers,out);
                WriteSpawningData wsd = new WriteSpawningData();

                if(numPlayers == 1){
                    //First Player Has Connected
                    p1ReadRunnable = rfc;
                    p1WriteRunnable = wtc;
                }
                else{
                    //Both Players Have Connected
                    p2ReadRunnable = rfc;
                    p2WriteRunnable = wtc;

                    //Sends Start Message for GUI to Open
                    p1WriteRunnable.sendStartMessage();
                    p2WriteRunnable.sendStartMessage();

                    //Starts the thread for reading and writing
                    Thread readThread1 = new Thread(p1ReadRunnable);
                    Thread readThread2 = new Thread(p2ReadRunnable);
                    readThread1.start();
                    readThread2.start();

                    Thread writeThread1 = new Thread(p1WriteRunnable);
                    Thread writeThread2 = new Thread(p2WriteRunnable);
                    writeThread1.start();
                    writeThread2.start();

                    Thread wsoThread = new Thread(wsd);
                    wsoThread.start();
                 }
            }

            System.out.println("No longer accepting connections");
        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }
    }

    /* Inner class for receiving data from client */
    private class ReadFromClient implements Runnable{
        private int playerID;
        private DataInputStream dataIn;

        /* ReadFromClient Constructor */
        public ReadFromClient(int playerID, DataInputStream dataIn){
            this.playerID = playerID;
            this.dataIn = dataIn;
            System.out.println("Read From Client " + playerID + " Runnable Created");
        }

        /* Contains what will be run in the thread */
        @Override
        public void run() {
            try {
                while(true){
                    if(playerID == 1){
                        p1Data = dataIn.readUTF();
                        if(Boolean.parseBoolean(p1Data.split(",")[2]))
                            bulletShot1 = true;
                    }
                    else{
                        p2Data = dataIn.readUTF();
                        if(Boolean.parseBoolean(p2Data.split(",")[2]))
                            bulletShot2 = true;
                    }                
                }
            } catch (IOException ex) {
                System.out.println("IOException from ReadFromClient run()");
            }
        }

    }

    /* Inner class for writing data to client */
    private class WriteToClient implements Runnable{
        private int playerID;
        private DataOutputStream dataOut;

        /* WriteToClient Constructor */
        public WriteToClient(int playerID, DataOutputStream dataOut){
            this.playerID = playerID;
            this.dataOut = dataOut;
            System.out.println("Write To Client Player " + playerID + " Runnable Created");
        }

        /* Contains what will be run in the thread */
        @Override
        public void run() {
            try {
                while(true){
                    if(playerID == 1 && !p2Data.equals("")){
                        if(bulletShot2){
                        String[] stringArray = p2Data.split(",");
                        stringArray[2] = "true";
                        p2Data = String.join(",", stringArray);

                        dataOut.writeUTF(p2Data  + "," + spawnCrate + "," + crateData);
                        dataOut.flush();

                        bulletShot2 = false;
                        }
                        else{
                            dataOut.writeUTF(p2Data  + "," + spawnCrate + "," + crateData);
                            dataOut.flush();
                        }
                        
                        if(spawnCrate)
                            p1Sent = true;
                    }
                    else if(!p1Data.equals("")){
                        if(bulletShot1){
                            String[] stringArray = p1Data.split(",");
                            stringArray[2] = "true";
                            p1Data = String.join(",", stringArray);

                            dataOut.writeUTF(p1Data  + "," + spawnCrate + "," + crateData);
                            dataOut.flush();

                            bulletShot1 = false;
                            }
                            else{
                                dataOut.writeUTF(p1Data  + "," + spawnCrate + "," + crateData);
                                dataOut.flush();
                            }

                        if(spawnCrate)
                            p2Sent = true;
                    }
                    if(p1Sent && p2Sent){
                        spawnCrate = false;
                        p1Sent = false;
                        p2Sent = false;
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        System.out.println("InterruptedException from WriteToClient run()");
                    }
                }
            } catch (IOException ex) {
                System.out.println("IOException from WriteToClient run()");
            }
        }

        /* Sends the start message to the clients */
        public void sendStartMessage(){
            try {
                dataOut.writeUTF("We now have 2 players. Begin!");
                dataOut.flush();
              } catch (IOException ex) {
                System.out.println("IOException from WriteToClient sendStartMessage()");
              }
        }

    }

    /* Inner class for writing spawning data of crates and powerups to client */
    private class WriteSpawningData implements Runnable{
        private int crateRate;

        /* WriteSpawningData Constructor */
        public WriteSpawningData(){
            crateRate = 4;
            System.out.println("Write Spawning Objects Player Runnable Created");
        }

        /* Contains what will be run in the thread */
        @Override
        public void run() {
            while(true){
                String gunInCrate = "pistol";
                String powerInCrate = "heal";

                int dropDelay = ((int) (Math.random()*(12-7)) + 7)*1000; //Generates a number from 7000 to 12000 (7-12 seconds)
                
                int gunChooser = (int) (Math.random() * 100); //Generates a number from 1 - 100
                int cratePosition = ((int) (Math.random()*(9-0)) + 0); //Generates a number from 0 - 9 (10 platforms)
                double crateSpawnPoint = Math.random(); //Generates a random number to multiply for the spawn position
                
                int powerChooser = (int) (Math.random() * 100); //Generates a number from 1 - 100
                int powerUpPosition = ((int) (Math.random()*(9-0)) + 0); //Generates a number from 0 - 9 (10 platforms)
                double powerUpSpawnPoint = Math.random(); //Generates a random number to multiply for the spawn position

                //60% smg, 30% rifle, 10% sniper
                if(gunChooser <= 60)
                    gunInCrate = "smg";
                else if(gunChooser <= 90)
                    gunInCrate = "rifle";
                else
                    gunInCrate = "sniper";

                //80% heal, 20% One Up
                if(crateCounter == crateRate){
                    crateRate = (int) (Math.random()*(5-3) + 3); //Generates a number from 3 - 5
                    crateCounter = 0;
                    spawnPowerUp = true;
                    if(powerChooser <= 80)
                        powerInCrate = "heal";
                    else
                        powerInCrate = "oneUp";
                }
                try {
                    Thread.sleep(dropDelay);
                    spawnCrate = true;
                    crateData = gunInCrate + "," + cratePosition + "," + crateSpawnPoint + "," + spawnPowerUp + "," + powerInCrate + "," + powerUpPosition + "," + powerUpSpawnPoint;
                    spawnPowerUp = false;
                    crateCounter++;
                    Thread.sleep(100);//Makes sure crateData doesn't get overwritten before sending to client
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException from spawningObjects()");
                }
            }
        }
    }

    /* Contains the main method of the server class */
    public static void main(String[] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}