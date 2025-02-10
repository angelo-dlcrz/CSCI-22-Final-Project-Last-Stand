# The Last Stand

The Last Stand is a 2D platformer game where two survivors must fight to the end. It is set in a post-apocalyptic world where the human race has almost been wiped out by a combination of climate change, global wars, and years of industrial pollution. The two players must eliminate each other to compete for the limited resources of this toxic and dangerous world.

The game is a PvP (Player-vs-Player) style of game where they are given a gun to eliminate each other. Players must use their platforming skills to keep themselves alive, while also shooting and chunking down their opponent’s health. The game also features a variety of weapon crates and power-ups that players can use to help them survive and kill.

The game’s format can make for some memorable, rage-inducing, or friendship-hurting (if you’re playing with friends) moments which make the game fun, enjoyable, and most importantly replayable for hours on end.

Be ready, aim steady, and do what’s necessary to survive. Do you have what it takes to make The Last Stand?

## How to Run - Method 1: Local

1. [Clone](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository) this repository into your working machine.
2. Make sure that you have [Java](https://www.oracle.com/java/technologies/downloads/) installed in your machine. It is recommended to use Java 8 or older.
3. Run the command line. Change your working directory into the directory where you cloned this repository. A bunch of [.java]() files can be found there. Check by typing `dir` on Windows or `ls` on macOS/Linux in your command line. Once there, enter the following commands:

```console
$ javac *.java
$ java GameServer.java
```

4. On that same machine, open another command line. Change your working directory into the directory where you cloned this repository. Once there, enter the following commands:

```console
$ java GameStarter.java
```

5. Repeat Step 4 again.

You can now play the game locally!

> **Note**: You can only play the game on the game tab that you're focused/active on at the moment. This is not ideal, which calls for Method 2.

## How to Run - Method 2: Two Machines

1. [Clone](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository) this repository into your working machines.
2. Make sure that you have [Java](https://www.oracle.com/java/technologies/downloads/) installed in your machines. It is recommended to use Java 8 or older.
3. Make sure both machines are connected to the same Wi-Fi network.
4. One machine will be dedicated as the server machine. Find out the IPv4 Address of your server machine whether it is on [Windows](https://support.microsoft.com/en-us/windows/find-your-ip-address-in-windows-f21a9bbc-c582-55cd-35e0-73431160a1b9), [MacOS](https://www.wikihow.com/Find-Your-IP-Address-on-a-Mac), or [Linux](https://opensource.com/article/18/5/how-find-ip-address-linux). Remember this address.
5. On your server machine, run the command line. Change your working directory into the directory where you cloned this repository. A bunch of [.java]() files can be found there. Check by typing `dir` on Windows or `ls` on MacOS/Linux in your command line. Once there, enter the following commands:

```console
$ javac *.java
$ java GameServer.java
```

6. Run another command line. Change your working directory into the directory where you cloned this repository. Once there, enter the following commands:

```console
$ java GameStarter.java
```

7. You will be prompted to enter an IP Address. Enter the IPv4 Address that you got from your server machine earlier.
8. Repeat Steps 6-7 on your other machine. You should now be able to play the game from two separate machines.

## Mechanics

**Traversing the Platforms**

1. The game features ten platforms which the players may use to their disadvantage or advantage.
2. The players can jump up or drop down to any of those platforms. However, be careful not to drop down from the bottommost platform. Else, you’ll fall into the scorched and polluted Earth down below.

**Making the Last Stand**

1. Guns
   1. Gun crates respawn randomly around the battlefield. The player can obtain them by getting close to the crates.
   2. There are 4 types of guns that the player may use. The Pistol is the player’s default gun. The player can opt to upgrade their gun by obtaining a crate.
   3. Pistol
      1. Fire rate: ★★
      2. Damage: ★
      3. Ammo: ∞
   4. SMG
      1. Fire rate: ★★★★★
      2. Damage: ★★
      3. Ammo: ★★★★★
   5. Rifle
      1. Fire rate: ★★★★
      2. Damage: ★★★★
      3. Ammo: ★★★★
   6. Sniper
      1. Fire rate: ★
      2. Damage: ★★★★★
      3. Ammo: ★
2. Power-ups
   1. Power-ups respawn randomly around the battlefield. The player can obtain them by getting close to the power-ups.
      1. One Up
         1. Adds one (1) life to the player’s lives
      2. Heal
         1. Heals the player’s current life back to 100
3. Lives
   1. The player has ten lives at the start of the game
      1. The player can extend their life by using power-ups but is only limited to a maximum of 10 lives
1. Surviving
   1. To win and survive, the player must eliminate their enemy until they have no health and lives left. One can do this by firing their gun at their opponent.
   2. The player may use the platform to their advantage by jumping or dropping down to evade the opponent’s bullets.

## Instructions

**Moving Around the Wasteland**

1. Movement
   1. Use the arrow keys to move the character
      1. Left arrow key is used for moving to the left
      2. Right arrow key is used for moving to the right
      3. Up arrow key is used to jump
      4. Down arrow key is used to drop down from a platform

**Shooting to Survive**

1. Shooting
   1. Use the space key to shoot in the direction of the character’s movement
   2. Tap the space key one at a time for controlled management of bullets or,
   3. Hold down the space key to fire the bullets in continuous succession
   4. Note that tapping or holding down the space key doesn’t have any effect on the accuracy of shots.

Make your Last Stand! 

## Credits
Big thanks to [Dustin](https://github.com/DustinAgner27) for co-creating this with me!
