import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Wumpus {
    private static final int MAX_ROOMS_FOR_ARROW_SHOT = 5;

	public static Random random = new Random();
    public static Cave cave = new Cave(random);
    public static WumpusMap map = new WumpusMap();
    public static WumpusMap startingMap = new WumpusMap();
    public static Player player = new Player();
    public static UserInterface ui = new UserInterface();
    public static boolean gameOver = false;

    public enum PlayerAction {
        SHOOT,
        MOVE
    };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
            runMainLoop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int randomWumpusMove() {
        int connections = cave.connectionsFromRoom(map.wumpusPosition());
        int move = random.nextInt(connections + 1) + 1;
        return (move <= connections) ? move : 0;
	}
    
    public static boolean isGameOver() {
        return gameOver;
    }

    public static boolean didPlayerWin() {
        return player.won();
    }

    public static void endGameWithPlayerLosing() {
        gameOver = true;
        player.setLost();
    }

    public static void endGameWithPlayerWinning() {
        gameOver = true;
        player.setWon();
    }

    private static void printInstructions() throws IOException {
        ui.println("WELCOME TO 'HUNT THE WUMPUS'");
        ui.println(String.format("  THE WUMPUS LIVES IN A CAVE OF %d ROOMS. EACH ROOM", cave.numberOfRooms()));	
        ui.println("HAS 3 TUNNELS LEADING TO OTHER ROOMS. (LOOK AT A");
        ui.println("DODECAHEDRON TO SEE HOW THIS WORKS-IF YOU DON'T KNOW");
        ui.println("WHAT A DODECAHEDRON IS, ASK SOMEONE)");
        ui.println("");
        ui.println("     HAZARDS:");
        ui.println(" BOTTOMLESS PITS - TWO ROOMS HAVE BOTTOMLESS PITS IN THEM");
        ui.println("     IF YOU GO THERE, YOU FALL INTO THE PIT (& LOSE!)");
        ui.println(" SUPER BATS - TWO OTHER ROOMS HAVE SUPER BATS. IF YOU");
        ui.println("     GO THERE, A BAT GRABS YOU AND TAKES YOU TO SOME OTHER");
        ui.println("     ROOM AT RANDOM. (WHICH MAY BE TROUBLESOME)");
        ui.println("HIT RETURN TO CONTINUE"); 
        
        ui.waitForUserToHitReturn();

        ui.println("     WUMPUS:");
        ui.println(" THE WUMPUS IS NOT BOTHERED BY HAZARDS (HE HAS SUCKER");
        ui.println(" FEET AND IS TOO BIG FOR A BAT TO LIFT).  USUALLY");
        ui.println(" HE IS ASLEEP.  TWO THINGS WAKE HIM UP: YOU SHOOTING AN");
        ui.println("ARROW OR YOU ENTERING HIS ROOM.");
        ui.println("     IF THE WUMPUS WAKES HE MOVES (P=.75) ONE ROOM");
        ui.println(" OR STAYS STILL (P=.25).  AFTER THAT, IF HE IS WHERE YOU");
        ui.println(" ARE, HE EATS YOU UP AND YOU LOSE!");
        ui.println("");
        ui.println("     YOU:");
        ui.println(" EACH TURN YOU MAY MOVE OR SHOOT A CROOKED ARROW");
        ui.println("   MOVING:  YOU CAN MOVE ONE ROOM (THRU ONE TUNNEL)");
        ui.println("   ARROWS:  YOU HAVE 5 ARROWS.  YOU LOSE WHEN YOU RUN OUT");
        ui.println("   EACH ARROW CAN GO FROM 1 TO 5 ROOMS. YOU AIM BY TELLING");
        ui.println("   THE COMPUTER THE ROOM#S YOU WANT THE ARROW TO GO TO.");
        ui.println("   IF THE ARROW CAN'T GO THAT WAY (IF NO TUNNEL) IT MOVES");
        ui.println("   AT RANDOM TO THE NEXT ROOM.");
        ui.println("     IF THE ARROW HITS THE WUMPUS, YOU WIN.");
        ui.println("     IF THE ARROW HITS YOU, YOU LOSE.");
        ui.println("HIT RETURN TO CONTINUE"); 

        ui.waitForUserToHitReturn();

        ui.println("    WARNINGS:");
        ui.println("     WHEN YOU ARE ONE ROOM AWAY FROM A WUMPUS OR HAZARD,");
        ui.println("     THE COMPUTER SAYS:");
        ui.println(" WUMPUS:  'I SMELL A WUMPUS'");
        ui.println(" BAT   :  'BATS NEARBY'");
        ui.println(" PIT   :  'I FEEL A DRAFT'");
        ui.println("");
    }

    private static void promptAndShowInstructions() throws IOException {
        char i$ = ui.promptAndReadSingleCharacterOption("INSTRUCTIONS (Y-N) ");

        if (i$ != 'N' && i$ != 'n') {    
            printInstructions();
        }
    }
    
    public static void printHazardDescriptions() {
        int connections = cave.connectionsFromRoom(map.playerPosition());
        for (int k = 1; k <= connections; k++) {
            if (cave.connection(map.playerPosition(), k) == map.wumpusPosition()) {
                ui.println("I SMELL A WUMPUS!");
            }
        }
        for (int k = 1; k <= connections; k++) {
            int room = cave.connection(map.playerPosition(), k);
            if (map.hasPitAt(room)) {
                ui.println("I FEEL A DRAFT");
            }
        }
        for (int k = 1; k <= connections; k++) {
            if (map.hasBatAt(cave.connection(map.playerPosition(),k))) {
                ui.println("BATS NEARBY!");
            }
        }
    }

    public static void printRoomGeometry() {
        ui.print("YOUR ARE IN ROOM ");
        ui.println(map.playerPosition());
        ui.print("TUNNELS LEAD TO");
        
        int connections = cave.connectionsFromRoom(map.playerPosition());
        for (int k = 1; k <= connections; k++) {
            ui.print(" ");
            ui.print(cave.connection(map.playerPosition(),k));
        }
    }

    public static void printRoomDescription() throws IOException {
        ui.println("");

        printHazardDescriptions(); 
        printRoomGeometry();
        
        ui.println("");
        ui.println("");
    } 

    public static PlayerAction promptForShootOrMove() throws IOException {
        while (true) {
            char i$ = ui.promptAndReadSingleCharacterOption("SHOOT OR MOVE (S-M) ");
            if (i$ == 'S' || i$ == 's') {
                return PlayerAction.SHOOT;
            } else if (i$ == 'M' || i$ == 'm') {
                return PlayerAction.MOVE;
            }
        }
    }

    public static void moveWumpus() {
        int k = randomWumpusMove();
        if (k != 0) {
            map.setWumpusPosition(cave.connection(map.wumpusPosition(),k));
        }
        if (map.wumpusPosition() == map.playerPosition()) {
            ui.println("TSK TSK TSK - WUMPUS GOT YOU!");
            endGameWithPlayerLosing();
        }
    }

    public static int promptForArrowPathRoomCount(int maxPathLength) throws IOException {
        int roomCount;

        ui.print(String.format("NO. OF ROOMS (1-%d) ", maxPathLength));
        while (true) {
            roomCount = ui.readInt();
            if (roomCount >= 1 && roomCount <= maxPathLength) {
                break;
            }
        }

        return roomCount;
    }
    
    public static void promptForArrowPathRooms(int pathLength, int[] roomsOut) throws IOException {
        for (int k = 1; k <= pathLength; k++) {
            ui.print("ROOM # ");    
            roomsOut[k] = ui.readInt();
            if (k > 2 && roomsOut[k] == roomsOut[k-2]) {
                ui.println("ARROWS AREN'T THAT CROOKED - TRY ANOTHER ROOM");
                k--;
                continue;
            }
        }
    }

    public static void evaluateArrowShoot(int pathLength, int[] rooms) {
        int arrowPosition = map.playerPosition();
        for (int k = 1; k <= pathLength; k++) {
            int k1;
            for (k1 = 1; k1 <= cave.connectionsFromRoom(arrowPosition); k1++) {
                if (cave.connection(arrowPosition,k1) == rooms[k]) {
                    arrowPosition = rooms[k];  
                    break;
                }
            }
            if (k1 > cave.connectionsFromRoom(arrowPosition)) {
                arrowPosition = cave.connection(arrowPosition, cave.randomTunnel(arrowPosition));
            }
             
            if (arrowPosition == map.wumpusPosition()) {
                ui.println("AHA! YOU GOT THE WUMPUS!");
                endGameWithPlayerWinning();
            }   
            if (arrowPosition == map.playerPosition()) {
                ui.println("OUCH! ARROW GOT YOU!");
                endGameWithPlayerLosing();
            }
        }
    }

    public static void promptAndShootArrow() throws IOException {
        int[] p = new int[MAX_ROOMS_FOR_ARROW_SHOT+1];
        int roomCount;

        // path of arrow
        roomCount = promptForArrowPathRoomCount(MAX_ROOMS_FOR_ARROW_SHOT);
        promptForArrowPathRooms(roomCount, p);

        evaluateArrowShoot(roomCount, p);

        if (!isGameOver()) {
            ui.println("MISSED");
            moveWumpus();
            if (player.consumeArrowAndTestIfOut()) {
                endGameWithPlayerLosing();
            }
        }
    }

    public static int promptForRoomToMoveTo() throws IOException {
        int roomToMoveTo = 0;

        while (true) {
            ui.print("WHERE TO "); 
            roomToMoveTo = ui.readInt(); 
            if (roomToMoveTo < 1 || roomToMoveTo > cave.numberOfRooms()) {
                continue;
            }

            int k;
            for (k = 1; k <= cave.connectionsFromRoom(map.playerPosition()); k++) {
                if (cave.connection(map.playerPosition(),k) == roomToMoveTo) {
                    break;
                }
            }
            if (k > cave.connectionsFromRoom(map.playerPosition()) && roomToMoveTo != map.playerPosition()) {
                ui.print("NOT POSSIBLE - ");
                continue;
            }
            break;
        }
        
        return roomToMoveTo;
    }
    
    public static void movePlayerAndTestHazards(int roomToMoveTo) {
        while (true) {
            map.setPlayerPosition(roomToMoveTo);
            if (roomToMoveTo == map.wumpusPosition()) {
                ui.println("... OOPS! BUMPED A WUMPUS!");
                moveWumpus();
                if (isGameOver()) {
                    return;
                }
            }
        
            if (map.hasPitAt(roomToMoveTo)) {
                ui.println("YYYYIIIIEEEE . . . FELL IN PIT");
                endGameWithPlayerLosing();
                return;
            }
        
            if (map.hasBatAt(roomToMoveTo)) {
                ui.println("ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!");
                roomToMoveTo = cave.randomRoom();
                continue;
            }
            
            break;
        }
    }

    public static void promptAndMovePlayer() throws IOException {
        int roomToMoveTo = promptForRoomToMoveTo();
        movePlayerAndTestHazards(roomToMoveTo);
    }

    public static void placeObjectsRandomlyOnMap() {
        while (true) {
            map = new WumpusMap();
            map.setPlayerPosition(cave.randomRoom());
            map.setWumpusPosition(cave.randomRoom());
            map.addPit(cave.randomRoom());
            map.addPit(cave.randomRoom());
            map.addBat(cave.randomRoom());
            map.addBat(cave.randomRoom());                    
            
            if (!map.hasOverlap()) {
                break;
            }
        }
    }

    public static void promptAndExecutePlayerAction() throws IOException{
        switch (promptForShootOrMove()) {
        case SHOOT:
            promptAndShootArrow();
            break;
            
        case MOVE:
            promptAndMovePlayer();
            break;
        }            
    }
    
    public static void printWinLoseState() {
        if (didPlayerWin()) {
            ui.println("HEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME!!");
        } else {
            ui.println ("HA HA HA - YOU LOSE!");
        }
    }

    public static boolean promptToReplaySameMap() throws IOException {
        char i$ = ui.promptAndReadSingleCharacterOption("SAME SETUP (Y-N)");        
        return i$ == 'y' || i$ == 'Y';
    }
    
    public static void saveStartingMapFromGameMap() {
        startingMap = (WumpusMap)map.clone();
    }

    public static void loadGameMapFromStartingMap() {
        map = (WumpusMap)startingMap.clone();
    }
    
    public static void setUpGame(boolean regenerateLevel) {
        if (regenerateLevel) {
            placeObjectsRandomlyOnMap();
            saveStartingMapFromGameMap();
        }
        
        gameOver = false;
        player.resetToStartOfGameState();
    }

    public static void runMainLoop() throws IOException {
        boolean randomize = true;

        promptAndShowInstructions();

        while (true) {
            setUpGame(randomize);

            ui.println("HUNT THE WUMPUS");

            do {
                printRoomDescription();
                promptAndExecutePlayerAction();
            } while (!isGameOver());
            
            printWinLoseState();
            loadGameMapFromStartingMap();

            randomize = !promptToReplaySameMap();            
        }
    }
}
