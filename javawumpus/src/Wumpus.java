import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class Wumpus {

	public static Random random = new Random();
    
    private static final int MAP_OBJECT_COUNT = 6;
    private static final int INITIAL_ARROWS = 5;
    private static final int NUMBER_OF_ROOMS = 20;
    private static final int NUMBER_OF_CONNECTIONS_PER_ROOM = 3;

	public static int[][] caveStructure = {{0,0,0,0},
                        {0,2,5,8},		{0,1,3,10},		{0,2,4,12},		{0,3,5,14},		{0,1,4,6},
                        {0,5,7,15},		{0,6,8,17},		{0,1,7,9},		{0,8,10,18},	{0,2,9,11},
                        {0,10,12,19},	{0,3,11,13},	{0,12,14,20},	{0,4,13,15},	{0,6,14,16},
                        {0,15,17,20},	{0,7,16,18},	{0,9,17,19},	{0,11,18,20},	{0,13,16,19}};

    public static WumpusMap map = new WumpusMap();
    public static WumpusMap startingMap = new WumpusMap();
    public static int arrowInventory = INITIAL_ARROWS;

    public enum WinLoseState {
        LOST,
        PLAYING,
        WON    
    };

    public static WinLoseState winLoseState = WinLoseState.PLAYING;


    public static boolean throwOnIOErrorForTests = false;

    public static int playerPosition() {
        return map.playerPosition();
    }

    public static void setWumpusPosition(int n) {
        map.setWumpusPosition(n);
    }   

    public static int wumpusPosition() {
        return map.wumpusPosition();
    }

    public static boolean isPitAt(int n) {
        return map.hasPitAt(n);
    }

    public static boolean isBatAt(int n) {
        return map.hasBatAt(n);
    }

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
	public static int randomConnection() {
		return random.nextInt(NUMBER_OF_CONNECTIONS_PER_ROOM) + 1;
	}
	public static int randomWumpusMove() {
		return random.nextInt(NUMBER_OF_CONNECTIONS_PER_ROOM + 1) + 1;
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
        char i$ = '\0';

        System.out.print("INSTRUCTIONS (Y-N) ");
        i$ = (char) System.in.read(); 
        System.in.read(); 
        if (i$ == 'N' || i$ =='n') {    
            return;
        }

        printInstructions();
    }

    public static void printRoomDescription() throws IOException {
        System.out.println("");
        for (int k = 1; k <= NUMBER_OF_CONNECTIONS_PER_ROOM; k++) {
            if (caveStructure[playerPosition()][k] == wumpusPosition()) {
                System.out.println("I SMELL A WUMPUS!");
            }
        }
        for (int k = 1; k <= NUMBER_OF_CONNECTIONS_PER_ROOM; k++) {
            int room = caveStructure[playerPosition()][k];
            if (isPitAt(room)) {
                System.out.println("I FEEL A DRAFT");
            }
        }
        for (int k = 1; k <= NUMBER_OF_CONNECTIONS_PER_ROOM; k++) {
            if (isBatAt(caveStructure[playerPosition()][k])) {
                System.out.println("BATS NEARBY!");
            }
        }
        
        System.out.print("YOUR ARE IN ROOM ");
        System.out.println(playerPosition());
        System.out.print("TUNNELS LEAD TO");
        
        for (int k = 1; k <= NUMBER_OF_CONNECTIONS_PER_ROOM; k++) {
            System.out.print(" ");
            System.out.print(caveStructure[playerPosition()][k]);
        }
        System.out.println("");
        System.out.println("");
    } 

    public static int promptForShootOrMove() throws IOException {
        while (true) {
            System.out.print("SHOOT OR MOVE (S-M) ");
            char i$ = (char) System.in.read();
            System.in.read();
            if (i$ == 'S' || i$ == 's') {
                return 1;
            } else if (i$ == 'M' || i$ == 'm') {
                return 2;
            }
        }
    }

    public static void moveWumpus() {
        int k = randomWumpusMove();
        if (k != NUMBER_OF_CONNECTIONS_PER_ROOM + 1) {
            setWumpusPosition( caveStructure[wumpusPosition()][k] );
        }
        if (wumpusPosition() == playerPosition()) {
            System.out.println("TSK TSK TSK - WUMPUS GOT YOU!");
            winLoseState = WinLoseState.LOST;
        }
    }

    public static void promptAndShootArrow() throws IOException {
        int[] p = new int[6];
        int roomCount;

        winLoseState = WinLoseState.PLAYING;
        
        // path of arrow
        System.out.print("NO. OF ROOMS (1-5) ");
        while (true) {
            roomCount = readInt();
            if (roomCount >= 1 && roomCount <= 5) {
                break;
            }
        }
        
        for (int k = 1; k <= roomCount; k++) {
            System.out.print("ROOM # ");    
            p[k] = readInt();
            if (k > 2 && p[k] == p[k-2]) {
                System.out.println("ARROWS AREN'T THAT CROOKED - TRY ANOTHER ROOM");
                k--;
                continue;
            }
        }

        int arrowPosition = playerPosition();
        for (int k = 1; k <= roomCount; k++) {
            int k1;
            for (k1 = 1; k1 <= NUMBER_OF_CONNECTIONS_PER_ROOM; k1++) {
                if (caveStructure[arrowPosition][k1] == p[k]) {
                    arrowPosition = p[k];  
                    break;
                }
            }
            if (k1 > NUMBER_OF_CONNECTIONS_PER_ROOM) {
                arrowPosition = caveStructure[arrowPosition][randomConnection()];
            }
             
            if (arrowPosition == wumpusPosition()) {
                System.out.println("AHA! YOU GOT THE WUMPUS!");
                winLoseState = WinLoseState.WON;
                return;
            }   
            if (arrowPosition == playerPosition()) {
                System.out.println("OUCH! ARROW GOT YOU!");
                winLoseState = WinLoseState.LOST;
                return;
            }
        }

        System.out.println("MISSED");

        moveWumpus();
        arrowInventory--;
        if (arrowInventory <= 0) {
            winLoseState =  WinLoseState.LOST;
        }
    }

    public static void promptAndMovePlayer() throws IOException {
        winLoseState = WinLoseState.PLAYING;

        int roomToMoveTo;

        while (true) {
            System.out.print("WHERE TO "); 
            roomToMoveTo = readInt(); 
            if (roomToMoveTo < 1 || roomToMoveTo > NUMBER_OF_ROOMS) {
                continue;
            }

            int k;
            for (k = 1; k <= NUMBER_OF_CONNECTIONS_PER_ROOM; k++) {
                if (caveStructure[playerPosition()][k] == roomToMoveTo) {
                    break;
                }
            }
            if (k > NUMBER_OF_CONNECTIONS_PER_ROOM && roomToMoveTo != playerPosition()) {
                System.out.print("NOT POSSIBLE - ");
                continue;
            }
            break;
        }
        
        while (true) {
            map.setPlayerPosition(roomToMoveTo);
            if (roomToMoveTo == wumpusPosition()) {
                System.out.println("... OOPS! BUMPED A WUMPUS!");
                moveWumpus();
                if (winLoseState != WinLoseState.PLAYING) {
                    return;
                }
            }
        
            if (isPitAt(roomToMoveTo)) {
                System.out.println("YYYYIIIIEEEE . . . FELL IN PIT");
                winLoseState = WinLoseState.LOST;
                return;
            }
        
            if (isBatAt(roomToMoveTo)) {
                System.out.println("ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!");
                roomToMoveTo = randomRoom();
                continue;
            }
            
            break;
        }
    }

    public static void runMainLoop() throws IOException {
        boolean randomize = true;

        promptAndShowInstructions();


        while (true) {
            if (randomize) {
                while (true) {
                    map = new WumpusMap();
                    map.setPlayerPosition(randomRoom());
                    map.setWumpusPosition(randomRoom());
                    map.addPit(randomRoom());
                    map.addPit(randomRoom());
                    map.addBat(randomRoom());
                    map.addBat(randomRoom());
                    
                    startingMap = (WumpusMap)map.clone();
                    
                    if (!map.hasOverlap()) {
                        break;
                    }
                }
            }

            arrowInventory = INITIAL_ARROWS; 

            System.out.println("HUNT THE WUMPUS");

            while (true) {
                printRoomDescription();

                switch (promptForShootOrMove()) {
                case 1:
                    promptAndShootArrow();
                    break;

                case 2:
                    promptAndMovePlayer();
                    break;
                }
            
                if (winLoseState != WinLoseState.PLAYING) {
                    break;
                }
            }

            if (winLoseState == WinLoseState.WON) {
                System.out.println("HEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME!!");
            } else {
                System.out.println ("HA HA HA - YOU LOSE!");
            }
        
            map = (WumpusMap)startingMap.clone();
            
            System.out.print("SAME SETUP (Y-N)"); 
            char i$ = (char) System.in.read(); System.in.read();
            
            randomize = true;
            if (i$ == 'y' || i$ == 'Y') {
                randomize = false;
            }
        }
    }
}
