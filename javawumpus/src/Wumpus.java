import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class Wumpus {
    private static final int INITIAL_ARROWS = 5;
    private static final int NUMBER_OF_ROOMS = 20;
    private static final int NUMBER_OF_CONNECTIONS_PER_ROOM = 3;
    private static final int MAX_ROOMS_FOR_ARROW_SHOT = 5;

	public static Random random = new Random();
	public static int[][] caveStructure = {{0,0,0,0},
                        {0,2,5,8},		{0,1,3,10},		{0,2,4,12},		{0,3,5,14},		{0,1,4,6},
                        {0,5,7,15},		{0,6,8,17},		{0,1,7,9},		{0,8,10,18},	{0,2,9,11},
                        {0,10,12,19},	{0,3,11,13},	{0,12,14,20},	{0,4,13,15},	{0,6,14,16},
                        {0,15,17,20},	{0,7,16,18},	{0,9,17,19},	{0,11,18,20},	{0,13,16,19}};

    public static WumpusMap map = new WumpusMap();
    public static WumpusMap startingMap = new WumpusMap();
    public static Player player = new Player();

    public enum PlayerAction {
        SHOOT,
        MOVE
    };


    public static boolean throwOnIOErrorForTests = false;

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

	public static int randomRoom() {
		return random.nextInt(NUMBER_OF_ROOMS) + 1;
	}

	public static int randomConnection(int room) {
		return random.nextInt(connectionsFromRoom(room)) + 1;
	}

	public static int randomWumpusMove() {
        int connections = connectionsFromRoom(map.wumpusPosition());
        int move = random.nextInt(connections + 1) + 1;
        return (move <= connections) ? move : 0;
	}
    
    public static int connectionsFromRoom(int n) {
        return caveStructure[n].length - 1;
    }
    
    public static boolean isGameOver() {
        return player.winLoseState() != Player.WinLoseState.PLAYING;
    }

    public static boolean didPlayerWin() {
        return player.winLoseState() == Player.WinLoseState.WON;
    }

    public static void endGameWithPlayerLosing() {
        player.setWinLoseState(Player.WinLoseState.LOST);
    }

    public static void endGameWithPlayerWinning() {
        player.setWinLoseState(Player.WinLoseState.WON);
    }

	public static int readInt() throws IOException {
		String line = "";
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		try {
			line = is.readLine();
		} catch (IOException e) {
            if (throwOnIOErrorForTests) {
                throw e;
            }
			return 0;
		}
		return Integer.parseInt(line);
	}

    private static void waitForUserToHitReturn() throws IOException {
        System.in.read();
    }

    private static char promptAndReadSingleCharacterOption(String prompt) throws IOException {
        System.out.print(prompt);
        char option = (char)System.in.read();
        System.in.read();
        return option;
    }

    private static void printInstructions() throws IOException {
        System.out.println("WELCOME TO 'HUNT THE WUMPUS'");
        System.out.println(String.format("  THE WUMPUS LIVES IN A CAVE OF %d ROOMS. EACH ROOM", NUMBER_OF_ROOMS));	
        System.out.println(String.format("HAS %d TUNNELS LEADING TO OTHER ROOMS. (LOOK AT A", NUMBER_OF_CONNECTIONS_PER_ROOM));
        System.out.println("DODECAHEDRON TO SEE HOW THIS WORKS-IF YOU DON'T KNOW");
        System.out.println("WHAT A DODECAHEDRON IS, ASK SOMEONE)");
        System.out.println("");
        System.out.println("     HAZARDS:");
        System.out.println(" BOTTOMLESS PITS - TWO ROOMS HAVE BOTTOMLESS PITS IN THEM");
        System.out.println("     IF YOU GO THERE, YOU FALL INTO THE PIT (& LOSE!)");
        System.out.println(" SUPER BATS - TWO OTHER ROOMS HAVE SUPER BATS. IF YOU");
        System.out.println("     GO THERE, A BAT GRABS YOU AND TAKES YOU TO SOME OTHER");
        System.out.println("     ROOM AT RANDOM. (WHICH MAY BE TROUBLESOME)");
        System.out.println("HIT RETURN TO CONTINUE"); 
        
        waitForUserToHitReturn();

        System.out.println("     WUMPUS:");
        System.out.println(" THE WUMPUS IS NOT BOTHERED BY HAZARDS (HE HAS SUCKER");
        System.out.println(" FEET AND IS TOO BIG FOR A BAT TO LIFT).  USUALLY");
        System.out.println(" HE IS ASLEEP.  TWO THINGS WAKE HIM UP: YOU SHOOTING AN");
        System.out.println("ARROW OR YOU ENTERING HIS ROOM.");
        System.out.println("     IF THE WUMPUS WAKES HE MOVES (P=.75) ONE ROOM");
        System.out.println(" OR STAYS STILL (P=.25).  AFTER THAT, IF HE IS WHERE YOU");
        System.out.println(" ARE, HE EATS YOU UP AND YOU LOSE!");
        System.out.println("");
        System.out.println("     YOU:");
        System.out.println(" EACH TURN YOU MAY MOVE OR SHOOT A CROOKED ARROW");
        System.out.println("   MOVING:  YOU CAN MOVE ONE ROOM (THRU ONE TUNNEL)");
        System.out.println("   ARROWS:  YOU HAVE 5 ARROWS.  YOU LOSE WHEN YOU RUN OUT");
        System.out.println("   EACH ARROW CAN GO FROM 1 TO 5 ROOMS. YOU AIM BY TELLING");
        System.out.println("   THE COMPUTER THE ROOM#S YOU WANT THE ARROW TO GO TO.");
        System.out.println("   IF THE ARROW CAN'T GO THAT WAY (IF NO TUNNEL) IT MOVES");
        System.out.println("   AT RANDOM TO THE NEXT ROOM.");
        System.out.println("     IF THE ARROW HITS THE WUMPUS, YOU WIN.");
        System.out.println("     IF THE ARROW HITS YOU, YOU LOSE.");
        System.out.println("HIT RETURN TO CONTINUE"); 

        waitForUserToHitReturn();

        System.out.println("    WARNINGS:");
        System.out.println("     WHEN YOU ARE ONE ROOM AWAY FROM A WUMPUS OR HAZARD,");
        System.out.println("     THE COMPUTER SAYS:");
        System.out.println(" WUMPUS:  'I SMELL A WUMPUS'");
        System.out.println(" BAT   :  'BATS NEARBY'");
        System.out.println(" PIT   :  'I FEEL A DRAFT'");
        System.out.println("");
    }

    private static void promptAndShowInstructions() throws IOException {
        char i$ = promptAndReadSingleCharacterOption("INSTRUCTIONS (Y-N) ");

        if (i$ != 'N' && i$ != 'n') {    
            printInstructions();
        }
    }
    
    public static void printHazardDescriptions() {
        int connections = connectionsFromRoom(map.playerPosition());
        for (int k = 1; k <= connections; k++) {
            if (caveStructure[map.playerPosition()][k] == map.wumpusPosition()) {
                System.out.println("I SMELL A WUMPUS!");
            }
        }
        for (int k = 1; k <= connections; k++) {
            int room = caveStructure[map.playerPosition()][k];
            if (map.hasPitAt(room)) {
                System.out.println("I FEEL A DRAFT");
            }
        }
        for (int k = 1; k <= connections; k++) {
            if (map.hasBatAt(caveStructure[map.playerPosition()][k])) {
                System.out.println("BATS NEARBY!");
            }
        }
    }

    public static void printRoomGeometry() {
        System.out.print("YOUR ARE IN ROOM ");
        System.out.println(map.playerPosition());
        System.out.print("TUNNELS LEAD TO");
        
        int connections = connectionsFromRoom(map.playerPosition());
        for (int k = 1; k <= connections; k++) {
            System.out.print(" ");
            System.out.print(caveStructure[map.playerPosition()][k]);
        }
    }

    public static void printRoomDescription() throws IOException {
        System.out.println("");

        printHazardDescriptions(); 
        printRoomGeometry();
        
        System.out.println("");
        System.out.println("");
    } 

    public static PlayerAction promptForShootOrMove() throws IOException {
        while (true) {
            char i$ = promptAndReadSingleCharacterOption("SHOOT OR MOVE (S-M) ");
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
            map.setWumpusPosition( caveStructure[map.wumpusPosition()][k] );
        }
        if (map.wumpusPosition() == map.playerPosition()) {
            System.out.println("TSK TSK TSK - WUMPUS GOT YOU!");
            endGameWithPlayerLosing();
        }
    }

    public static int promptForArrowPathRoomCount(int maxPathLength) throws IOException {
        int roomCount;

        System.out.print(String.format("NO. OF ROOMS (1-%d) ", maxPathLength));
        while (true) {
            roomCount = readInt();
            if (roomCount >= 1 && roomCount <= maxPathLength) {
                break;
            }
        }

        return roomCount;
    }
    
    public static void promptForArrowPathRooms(int pathLength, int[] roomsOut) throws IOException {
        for (int k = 1; k <= pathLength; k++) {
            System.out.print("ROOM # ");    
            roomsOut[k] = readInt();
            if (k > 2 && roomsOut[k] == roomsOut[k-2]) {
                System.out.println("ARROWS AREN'T THAT CROOKED - TRY ANOTHER ROOM");
                k--;
                continue;
            }
        }
    }

    public static void evaluateArrowShoot(int pathLength, int[] rooms) {
        int arrowPosition = map.playerPosition();
        for (int k = 1; k <= pathLength; k++) {
            int k1;
            for (k1 = 1; k1 <= connectionsFromRoom(arrowPosition); k1++) {
                if (caveStructure[arrowPosition][k1] == rooms[k]) {
                    arrowPosition = rooms[k];  
                    break;
                }
            }
            if (k1 > connectionsFromRoom(arrowPosition)) {
                arrowPosition = caveStructure[arrowPosition][randomConnection(arrowPosition)];
            }
             
            if (arrowPosition == map.wumpusPosition()) {
                System.out.println("AHA! YOU GOT THE WUMPUS!");
                endGameWithPlayerWinning();
            }   
            if (arrowPosition == map.playerPosition()) {
                System.out.println("OUCH! ARROW GOT YOU!");
                endGameWithPlayerLosing();
            }
        }
    }

    public static boolean consumeArrowAndTestIfOut() {
        player.setArrowInventory(player.arrowInventory() - 1);
        return player.arrowInventory() <= 0;
    }

    public static void promptAndShootArrow() throws IOException {
        int[] p = new int[MAX_ROOMS_FOR_ARROW_SHOT+1];
        int roomCount;

        // path of arrow
        roomCount = promptForArrowPathRoomCount(MAX_ROOMS_FOR_ARROW_SHOT);
        promptForArrowPathRooms(roomCount, p);

        evaluateArrowShoot(roomCount, p);

        if (!isGameOver()) {
            System.out.println("MISSED");
            moveWumpus();
            if (consumeArrowAndTestIfOut()) {
                endGameWithPlayerLosing();
            }
        }
    }

    public static int promptForRoomToMoveTo() throws IOException {
        int roomToMoveTo = 0;

        while (true) {
            System.out.print("WHERE TO "); 
            roomToMoveTo = readInt(); 
            if (roomToMoveTo < 1 || roomToMoveTo > NUMBER_OF_ROOMS) {
                continue;
            }

            int k;
            for (k = 1; k <= connectionsFromRoom(map.playerPosition()); k++) {
                if (caveStructure[map.playerPosition()][k] == roomToMoveTo) {
                    break;
                }
            }
            if (k > connectionsFromRoom(map.playerPosition()) && roomToMoveTo != map.playerPosition()) {
                System.out.print("NOT POSSIBLE - ");
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
                System.out.println("... OOPS! BUMPED A WUMPUS!");
                moveWumpus();
                if (isGameOver()) {
                    return;
                }
            }
        
            if (map.hasPitAt(roomToMoveTo)) {
                System.out.println("YYYYIIIIEEEE . . . FELL IN PIT");
                endGameWithPlayerLosing();
                return;
            }
        
            if (map.hasBatAt(roomToMoveTo)) {
                System.out.println("ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!");
                roomToMoveTo = randomRoom();
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
            map.setPlayerPosition(randomRoom());
            map.setWumpusPosition(randomRoom());
            map.addPit(randomRoom());
            map.addPit(randomRoom());
            map.addBat(randomRoom());
            map.addBat(randomRoom());                    
            
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
    
    public static void printWinLoseState(Player.WinLoseState state) {
        if (didPlayerWin()) {
            System.out.println("HEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME!!");
        } else {
            System.out.println ("HA HA HA - YOU LOSE!");
        }
    }

    public static boolean promptToReplaySameMap() throws IOException {
        char i$ = promptAndReadSingleCharacterOption("SAME SETUP (Y-N)");        
        return i$ == 'y' || i$ == 'Y';
    }
    
    public static void saveStartingMapFromGameMap() {
        startingMap = (WumpusMap)map.clone();
    }

    public static void loadGameMapFromStartingMap() {
        map = (WumpusMap)startingMap.clone();
    }

    public static void runMainLoop() throws IOException {
        boolean randomize = true;

        promptAndShowInstructions();

        while (true) {
            if (randomize) {
                placeObjectsRandomlyOnMap();
                saveStartingMapFromGameMap();
            }
            
            player.resetToStartOfGameState();

            System.out.println("HUNT THE WUMPUS");

            do {
                printRoomDescription();
                promptAndExecutePlayerAction();
            } while (!isGameOver());
            
            printWinLoseState(player.winLoseState());
            loadGameMapFromStartingMap();

            randomize = !promptToReplaySameMap();            
        }
    }
}
