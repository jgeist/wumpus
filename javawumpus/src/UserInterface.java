import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Iterator;

public class UserInterface {
    public enum PlayerAction {
        SHOOT,
        MOVE
    };

	public int readInt() throws IOException {
		String line = "";
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		try {
			line = is.readLine();
		} catch (IOException e) {
			return 0;
		}
		return Integer.parseInt(line);
	}

    public void waitForUserToHitReturn() throws IOException {
        System.in.read();
    }

    public char promptAndReadSingleCharacterOption(String prompt) throws IOException {
        System.out.print(prompt);
        char option = (char)System.in.read();
        System.in.read();
        return option;
    }

    private void print(String line) {
        System.out.print(line);
    }

    private void println(String line) {
        System.out.println(line);
    }

    private void print(int i) {
        print(String.format("%d", i));
    }

    private void println(int i) {
        println(String.format("%d", i));
    }

    public void printInstructions(int numberOfRooms) throws IOException {
        println("WELCOME TO 'HUNT THE WUMPUS'");
        println(String.format("  THE WUMPUS LIVES IN A CAVE OF %d ROOMS. EACH ROOM", numberOfRooms));
        println("HAS 3 TUNNELS LEADING TO OTHER ROOMS. (LOOK AT A");
        println("DODECAHEDRON TO SEE HOW THIS WORKS-IF YOU DON'T KNOW");
        println("WHAT A DODECAHEDRON IS, ASK SOMEONE)");
        println("");
        println("     HAZARDS:");
        println(" BOTTOMLESS PITS - TWO ROOMS HAVE BOTTOMLESS PITS IN THEM");
        println("     IF YOU GO THERE, YOU FALL INTO THE PIT (& LOSE!)");
        println(" SUPER BATS - TWO OTHER ROOMS HAVE SUPER BATS. IF YOU");
        println("     GO THERE, A BAT GRABS YOU AND TAKES YOU TO SOME OTHER");
        println("     ROOM AT RANDOM. (WHICH MAY BE TROUBLESOME)");
        println("HIT RETURN TO CONTINUE"); 
        
        waitForUserToHitReturn();

        println("     WUMPUS:");
        println(" THE WUMPUS IS NOT BOTHERED BY HAZARDS (HE HAS SUCKER");
        println(" FEET AND IS TOO BIG FOR A BAT TO LIFT).  USUALLY");
        println(" HE IS ASLEEP.  TWO THINGS WAKE HIM UP: YOU SHOOTING AN");
        println("ARROW OR YOU ENTERING HIS ROOM.");
        println("     IF THE WUMPUS WAKES HE MOVES (P=.75) ONE ROOM");
        println(" OR STAYS STILL (P=.25).  AFTER THAT, IF HE IS WHERE YOU");
        println(" ARE, HE EATS YOU UP AND YOU LOSE!");
        println("");
        println("     YOU:");
        println(" EACH TURN YOU MAY MOVE OR SHOOT A CROOKED ARROW");
        println("   MOVING:  YOU CAN MOVE ONE ROOM (THRU ONE TUNNEL)");
        println("   ARROWS:  YOU HAVE 5 ARROWS.  YOU LOSE WHEN YOU RUN OUT");
        println("   EACH ARROW CAN GO FROM 1 TO 5 ROOMS. YOU AIM BY TELLING");
        println("   THE COMPUTER THE ROOM#S YOU WANT THE ARROW TO GO TO.");
        println("   IF THE ARROW CAN'T GO THAT WAY (IF NO TUNNEL) IT MOVES");
        println("   AT RANDOM TO THE NEXT ROOM.");
        println("     IF THE ARROW HITS THE WUMPUS, YOU WIN.");
        println("     IF THE ARROW HITS YOU, YOU LOSE.");
        println("HIT RETURN TO CONTINUE"); 

        waitForUserToHitReturn();

        println("    WARNINGS:");
        println("     WHEN YOU ARE ONE ROOM AWAY FROM A WUMPUS OR HAZARD,");
        println("     THE COMPUTER SAYS:");
        println(" WUMPUS:  'I SMELL A WUMPUS'");
        println(" BAT   :  'BATS NEARBY'");
        println(" PIT   :  'I FEEL A DRAFT'");
        println("");
    }

    public void promptAndShowInstructions(int numberOfRooms) throws IOException {
        char i$ = promptAndReadSingleCharacterOption("INSTRUCTIONS (Y-N) ");

        if (i$ != 'N' && i$ != 'n') {    
            printInstructions(numberOfRooms);
        }
    }
    
    public void printHazardWarning(String line) {
        println(line);
    }
    
    public void printHazards(int nearbyWumpii, int nearbyPits, int nearbyBats) {
        while (nearbyWumpii-- > 0) {
            printHazardWarning("I SMELL A WUMPUS!");
        }

        while (nearbyPits-- > 0) {
            printHazardWarning("I FEEL A DRAFT");
        }

        while (nearbyBats-- > 0) {
            printHazardWarning("BATS NEARBY!");
        }
    }

    public void printRoomGeometry(RoomDescription desc) {
        print("YOUR ARE IN ROOM ");
        println(desc.roomNumber());
        print("TUNNELS LEAD TO");
        
        Iterator<Integer> connections = desc.connectedRooms();
        while (connections.hasNext()) {
            print(" ");
            print((int)connections.next());
        }
    }

    public void printRoomDescription(RoomDescription desc) {
        println("");

        printHazards(desc.nearbyWumpii(), desc.nearbyPits(), desc.nearbyBats());
        printRoomGeometry(desc);

        println("");
        println("");
    }

    public PlayerAction promptForShootOrMove() throws IOException {
        while (true) {
            char i$ = promptAndReadSingleCharacterOption("SHOOT OR MOVE (S-M) ");
            if (i$ == 'S' || i$ == 's') {
                return PlayerAction.SHOOT;
            } else if (i$ == 'M' || i$ == 'm') {
                return PlayerAction.MOVE;
            }
        }
    }

    public boolean promptToReplaySameMap() throws IOException {
        char i$ = promptAndReadSingleCharacterOption("SAME SETUP (Y-N)");        
        return i$ == 'y' || i$ == 'Y';
    }
    
    public int promptForArrowPathRoomCount(int maxPathLength) throws IOException {
        int roomCount;

        print(String.format("NO. OF ROOMS (1-%d) ", maxPathLength));
        while (true) {
            roomCount = readInt();
            if (roomCount >= 1 && roomCount <= maxPathLength) {
                break;
            }
        }

        return roomCount;
    }    

    public void promptForArrowPathRooms(int pathLength, int[] roomsOut) throws IOException {
        for (int k = 1; k <= pathLength; k++) {
            print("ROOM # ");    
            roomsOut[k] = readInt();
            if (k > 2 && roomsOut[k] == roomsOut[k-2]) {
                println("ARROWS AREN'T THAT CROOKED - TRY ANOTHER ROOM");
                k--;
                continue;
            }
        }
    }

    public int promptForRoomToMoveTo(RoomDescription currentRoom, int numberOfRooms) throws IOException {
        int roomToMoveTo = 0;

        while (true) {
            print("WHERE TO "); 
            roomToMoveTo = readInt(); 
            if (roomToMoveTo < 1 || roomToMoveTo > numberOfRooms) {
                continue;
            }
            
            boolean validRoom = false;
            Iterator<Integer> rooms = currentRoom.connectedRooms();
            while (rooms.hasNext()) {
                if (roomToMoveTo == rooms.next()) {
                    validRoom = true;
                    break;
                }
            }

            if (!validRoom && roomToMoveTo != currentRoom.roomNumber()) {
                print("NOT POSSIBLE - ");
                continue;
            }

            break;
        }
        
        return roomToMoveTo;
    }    

    public void printWinLoseState(boolean won) {
        if (won) {
            println("HEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME!!");
        } else {
            println ("HA HA HA - YOU LOSE!");
        }
    }

    public void printWinLoseMessage(String message) {
        println(message);
    }
    
    public void printStatusMessage(String message) {
        println(message);
    }
}
