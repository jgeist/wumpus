import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;


public class Wumpus {

	public static int currentLine = 5;
	private static Deque<Integer> returnLine = new ArrayDeque<Integer>();
	private static int nextLine;
	public static Random random = new Random();
    
    private static final int MAP_OBJECT_COUNT = 6;

    public static char i$ = '\0';
	public static int[][] s = {{0,0,0,0},
                        {0,2,5,8},		{0,1,3,10},		{0,2,4,12},		{0,3,5,14},		{0,1,4,6},
                        {0,5,7,15},		{0,6,8,17},		{0,1,7,9},		{0,8,10,18},	{0,2,9,11},
                        {0,10,12,19},	{0,3,11,13},	{0,12,14,20},	{0,4,13,15},	{0,6,14,16},
                        {0,15,17,20},	{0,7,16,18},	{0,9,17,19},	{0,11,18,20},	{0,13,16,19}};
    public static int[] l = new int[MAP_OBJECT_COUNT + 1];
    public static int[] m = new int[MAP_OBJECT_COUNT + 1];
    public static int[] p = new int[6];
    public static int aa = 5;
    public static int ll = aa;
    public static int o = 1;
    public static int f = 0;
			
    public static int j = 0;
    public static int k = 0;
    public static int k1 = 0;
    public static int j9 = 0;

    public static boolean throwOnIOErrorForTests = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			while (currentLine <= 1150) {
				nextLine = currentLine + 1;
				switch (currentLine) {
				case 5: runMainLoop(); break;				 													// 5 rem *** HUNT THE WUMPUS ***

				case 670: promptForShootOrMove(); break;														// 670 rem *** CHOOSE OPTION ***
				case 710: returnFromGosub(); break;																// 710 return
				case 715: promptAndShootArrow(); returnFromGosub(); break;										// 715 rem *** ARROW ROUTINE ***

				case 935: moveWumpus(); break;																	// 935 rem *** MOVE WUMPUS ROUTINE ***
				case 970: returnFromGosub(); break;																// 970 return

				case 975: promptAndMovePlayer(); returnFromGosub(); break;    									// 975 rem *** MOVE ROUTINE ***
				}
				currentLine = nextLine;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void gosub(int gosubLine, int lineToReturnTo) {
		nextLine = gosubLine;
		returnLine.addLast(lineToReturnTo);
	}
	private static void returnFromGosub() {
		if (returnLine.isEmpty())
			nextLine = 1151;
		else
			nextLine = returnLine.pollLast();
	}
    public static void pushGosubReturnAddressForTests(int lineToReturnTo) {
        returnLine.addLast(lineToReturnTo);
    }
	public static int fnA() {
		return random.nextInt(20) + 1;
	}
	public static int fnB() {
		return random.nextInt(3) + 1;
	}
	public static int fnC() {
		return random.nextInt(4) + 1;
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
        System.out.println("  THE WUMPUS LIVES IN A CAVE OF 20 ROOMS. EACH ROOM");	
        System.out.println("HAS 3 TUNNELS LEADING TO OTHER ROOMS. (LOOK AT A");
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
        for (j = 2; j <= MAP_OBJECT_COUNT; j++) {   
            for (k = 1; k <= 3; k++) {
                if (s[l[1]][k] == l[j]) {
                    switch (j-1) {
                    case 1:
                        System.out.println("I SMELL A WUMPUS!");
                        break;

                    case 2:                        
                    case 3:
                        System.out.println("I FEEL A DRAFT");
                        break;

                    case 4:
                    case 5:
                        System.out.println("BATS NEARBY!");
                        break;
                    }
                }
            }
        }
        
        System.out.print("YOUR ARE IN ROOM ");
        System.out.println(l[1]);
        System.out.print("TUNNELS LEAD TO ");
        System.out.print(s[ll][1]);
        System.out.print(" ");
        System.out.print(s[ll][2]);
        System.out.print(" ");
        System.out.print(s[ll][3]);
        System.out.println("");
        System.out.println("");
    } 

    public static void promptForShootOrMove() throws IOException {
        while (true) {
            System.out.print("SHOOT OR MOVE (S-M) ");
            i$ = (char) System.in.read();
            System.in.read();
            if (i$ == 'S' || i$ == 's') {
                o = 1;
                break;
            } else if (i$ == 'M' || i$ == 'm') {
                o = 2;
                break;
            }
        }
    }

    public static void moveWumpus() {
        k = fnC();
        if (k != 4) {
            l[2] = s[l[2]][k]; 
        }
        if (l[2] == ll) {
            System.out.println("TSK TSK TSK - WUMPUS GOT YOU!");
            f = -1;
        }
    }

    public static void promptAndShootArrow() throws IOException {
        f = 0;
        
        // path of arrow
        System.out.print("NO. OF ROOMS (1-5) ");
        while (true) {
            j9 = readInt();
            if (j9 >= 1 && j9 <= 5) {
                break;
            }
        }
        
        for (k = 1; k <= j9; k++) {
            System.out.print("ROOM # ");    
            p[k] = readInt();
            if (k > 2 && p[k] == p[k-2]) {
                System.out.println("ARROWS AREN'T THAT CROOKED - TRY ANOTHER ROOM");
                k--;
                continue;
            }
        }

        ll = l[1];
        for (k = 1; k <= j9; k++) {
            for (k1 = 1; k1 <= 3; k1++) {
                if (s[ll][k1] == p[k]) {
                    ll = p[k];  
                    break;
                }
            }
            if (k1 == 4) {
                ll = s[ll][fnB()];
            }
             
            if (ll == l[2]) {
                System.out.println("AHA! YOU GOT THE WUMPUS!");
                f = 1;
                return;
            }   
            if (ll == l[1]) {
                System.out.println("OUCH! ARROW GOT YOU!");
                f = -1;
                return;
            }
        }

        System.out.println("MISSED");
        ll = l[1];
        moveWumpus();
        aa--;
        if (aa <= 0) {
            f = -1;
        }
    }

    public static void promptAndMovePlayer() throws IOException {
        f = 0;
        
        while (true) {
            System.out.print("WHERE TO "); 
            ll = readInt(); 
            if (ll < 1 || ll > 20) {
                continue;
            }
            for (k = 1; k <= 3; k++) {
                if (s[l[1]][k] == ll) {
                    break;
                }
            }
            if (k > 3 && ll != l[1]) {
                System.out.print("NOT POSSIBLE - ");
                continue;
            }
            break;
        }
        
        while (true) {
            l[1] = ll;
            if (ll == l[2]) {
                System.out.println("... OOPS! BUMPED A WUMPUS!");
                moveWumpus();
                if (f != 0) {
                    return;
                }
            }
        
            if (ll == l[3] || ll == l[4]) {
                System.out.println("YYYYIIIIEEEE . . . FELL IN PIT");
                f = -1;
                return;
            }
        
            if (ll == l[5] || ll == l[6]) {
                System.out.println("ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!");
                ll = fnA();
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
                    for (j = 1; j <= MAP_OBJECT_COUNT; j++) {
                        l[j] = fnA();
                        m[j] = l[j];
                    }

                    for (j = 1; j <= MAP_OBJECT_COUNT; j++) {
                        for (k = 1; k <= MAP_OBJECT_COUNT; k++) {
                            if (j == k) {
                                continue;
                            }   
                            if (l[j] == l[k]) {
                                break;
                            }
                        }
                        if (k <= MAP_OBJECT_COUNT) {
                            break;
                        }
                    }
            
                    if (k > MAP_OBJECT_COUNT) {
                        break;
                    }
                }
            }

            aa = 5; 
            ll = l[1];

            System.out.println("HUNT THE WUMPUS");

            while (true) {
                printRoomDescription();
                promptForShootOrMove();

                switch (o) {
                case 1:
                    promptAndShootArrow();
                    break;

                case 2:
                    promptAndMovePlayer();
                    break;
                }
            
                if (f != 0) {
                    break;
                }
            }

            if (f > 0) {
                System.out.println("HEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME!!");
            } else {
                System.out.println ("HA HA HA - YOU LOSE!");
            }
        
            for (j = 1; j < MAP_OBJECT_COUNT; j++) {
                l[j] = m[j];
            }
            
            System.out.print("SAME SETUP (Y-N)"); 
            i$ = (char) System.in.read(); System.in.read();
            
            randomize = true;
            if (i$ == 'y' || i$ == 'Y') {
                randomize = false;
            }
        }
    }
}
      