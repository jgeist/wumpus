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
				case 5: break;								 													// 5 rem *** HUNT THE WUMPUS ***
				case 10: break;				 																	// 10 dim p(5)
				case 15: promptAndShowInstructions(); break;	    									        // 15 print "INSTRUCTIONS (Y-N)";
																												// 85 dim s(20,3)
																												// 90 for j = 1 to 20
																												// 95 for k = 1 to 3
																												// 100 read s(j,k)
																												// 105 next k
																												// 110 next j
																												// 115 data 2,5,8,1,3,10,2,4,12,3,5,14,1,4,6
																												// 120 data 5,7,15,6,8,17,1,7,9,8,10,18,2,9,11
																												// 125 data 10,12,19,3,11,13,12,14,20,4,13,15,6,14,16
																												// 130 data 15,17,20,7,16,18,9,17,19,11,18,20,13,16,19
																												// 135 def fnA(X)=INT(20*RND(1))+1
																												// 140 def fnB(X)=INT(3*RND(1))+1
																												// 145 def fnC(X)=INT(4*RND(1))+1
				case 150: break;																				// 150 rem *** LOCATE L ARRAY ITEMS ***
				case 155: break;																				// 155 rem *** 1-YOU, 2-WUMPUS, 3&4-PITS, 5&6-BATS ***
				case 160: break;																				// 160 dim l(6)
				case 165: break;																				// 165 dim m(6)
				case 170: j = 1; break;																			// 170 for j = 1 to 6
				case 175: l[j] = fnA(); break;																	// 175 l(j) = fna(0)
				case 180: m[j] = l[j]; break;																	// 180 m(j) = l(j)
				case 185: ++j; if (j <= MAP_OBJECT_COUNT) nextLine = 175; break;								// 185 next j
				case 190: break;																				// 190 rem *** CHECK FOR CROSSOVERS (IE l(1)=l(2), ETC) ***
				case 195: j = 1; break;																			// 195 for j = 1 to 6
				case 200: k = 1; break;																			// 200 for k = 1 to 6
				case 205: if (j == k ) nextLine = 215; break;													// 205 if j = k then 215
				case 210: if (l[j] == l[k]) nextLine = 170; break;												// 210 if l(j) = l(k) then 170
				case 215: ++k; if (k <= MAP_OBJECT_COUNT) nextLine = 205; break;	    						// 215 next k
				case 220: ++j; if (j <= MAP_OBJECT_COUNT) nextLine = 200; break;								// 220 next j
				case 225: break;																				// 225 rem *** SET NO. OF ARROWS ***
				case 230: aa = 5; break;																		// 230 a = 5
				case 235: ll = l[1]; break;																		// 235 l = l(1)
				case 240: break;																				// 240 rem *** RUN THE GAME ***
				case 245: System.out.println("HUNT THE WUMPUS"); break;											// 245 print "HUNT THE WUMPUS"
				case 250: break;																				// 250 rem *** HAZARD WARNING AND LOCATION ***
				case 255: gosub(585, 260); break;																// 255 gosub 585
				case 260: break;																				// 260 rem *** MOVE OR SHOOT ***
				case 265: gosub(670, 270); break;																// 265 gosub 670
				case 270: switch(o) {case 1: nextLine = 280; break; case 2: nextLine = 300; break;} break;		// 270 on o goto 280,300
				case 275: break;																				// 275 rem *** SHOOT ***
				case 280: gosub(715, 285); break;																// 280 gosub 715
				case 285: if (f == 0) nextLine = 255; break;													// 285 if f = 0 then 255
				case 290: nextLine = 310; break;																// 290 goto 310
				case 295: break;																				// 295 rem *** MOVE ***
				case 300: gosub(975, 305); break;																// 300 gosub 975
				case 305: if (f == 0) nextLine = 255; break;													// 305 if f = 0 then 255
				case 310: if (f > 0) nextLine = 335; break;														// 310 if f > 0 then 335
				case 315: break;																				// 315 rem *** LOSE ***
				case 320: System.out.println ("HA HA HA - YOU LOSE!"); break;									// 320 print "HA HA HA - YOU LOSE!"
				case 325: nextLine = 340; break;																// 325 goto 340
				case 330: break;																				// 330 rem *** WIN ***
				case 335: System.out.println("HEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME!!"); break;			// 335 print "HEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME!!"
				case 340: j = 1; break;																			// 340 for j = 1 to 6
				case 345: l[j] = m[j]; break;																	// 345 l(j) = m(j)
				case 350: ++j; if (j <= MAP_OBJECT_COUNT) nextLine = 345; break;								// 350 next j
				case 355: System.out.print("SAME SETUP (Y-N)"); break;											// 355 print "SAME SETUP (Y-N)";
				case 360: i$ = (char) System.in.read(); System.in.read(); break;								// 360 input i$
				case 365: if (i$ != 'Y' && i$ != 'y') nextLine = 170; break;									// 365 if (i$ <> "Y") and (i$ <> "y") then 170
				case 370: nextLine = 230; break;																// 370 goto 230
				case 585: printRoomDescription(); break;														// 585 rem *** PRINT LOCATION & HAZARD WARNINGS ***
				case 665: returnFromGosub(); break;																// 665 return
				case 670: promptForShootOrMove(); break;														// 670 rem *** CHOOSE OPTION ***
				case 710: returnFromGosub(); break;																// 710 return
				case 715: promptAndShootArrow(); returnFromGosub(); break;										// 715 rem *** ARROW ROUTINE ***

				case 935: moveWumpus(); break;																	// 935 rem *** MOVE WUMPUS ROUTINE ***
				case 970: returnFromGosub(); break;																// 970 return

				case 975: break;																				// 975 rem *** MOVE ROUTINE ***
				case 980: f = 0; break;																			// 980 f = 0
				case 985: System.out.print("WHERE TO "); break;													// 985 print "WHERE TO";
				case 990: ll = readInt(); break;																// 990 input l
				case 995: if (ll < 1) nextLine = 985; break;													// 995 if l < 1 then 985
				case 1000: if (ll > 20) nextLine = 985; break;													// 1000 if l > 20 then 985
				case 1005: k = 1; break;																		// 1005 for k = 1 to 3
				case 1010: break;																				// 1010 rem *** CHECK IF LEGAL MOVE ***
				case 1015: if (s[l[1]][k] == ll) nextLine = 1045; break;										// 1015 if s(l(1),k) = l then 1045
				case 1020: ++k; if (k <= 3) nextLine = 1010; break;												// 1020 next k
				case 1025: if (ll == l[1]) nextLine = 1045; break;												// 1025 if l = l(1) then 1045
				case 1030: System.out.print("NOT POSSIBLE - "); break;											// 1030 print "NOT POSSIBLE -";
				case 1035: nextLine = 985; break;																// 1035 goto 985
				case 1040: break;																				// 1040 rem *** CHECK FOR HAZARDS ***
				case 1045: l[1] = ll; break;																	// 1045 l(1) = l
				case 1050: break;																				// 1050 rem *** WUMPUS ***
				case 1055: if (ll != l[2]) nextLine = 1090; break;												// 1055 if l <> l(2) then 1090
				case 1060: System.out.println("... OOPS! BUMPED A WUMPUS!"); break;								// 1060 print "... OOPS! BUMPED A WUMPUS!"
				case 1065: break;																				// 1065 rem *** MOVE WUMPUS ***
				case 1070: gosub(935, 1075); break;																// 1070 gosub 935
				case 1075: if (f == 0) nextLine = 1090; break;													// 1075 if f = 0 then 1090
				case 1080: returnFromGosub(); break;															// 1080 return
				case 1085: break;																				// 1085 rem *** PIT ***
				case 1090: if (ll == l[3]) nextLine = 1100; break;												// 1090 if l = l(3) then 1100
				case 1095: if (ll != l[4]) nextLine = 1120; break;												// 1095 if l <> l(4) then 1120
				case 1100: System.out.println("YYYYIIIIEEEE . . . FELL IN PIT"); break;							// 1100 print "YYYYIIIIEEEE . . . FELL IN PIT"
				case 1105: f = -1; break;																		// 1105 f = -1
				case 1110: returnFromGosub(); break;															// 1110 return
				case 1115: break;																				// 1115 rem *** BATS ***
				case 1120: if (ll == l[5]) nextLine = 1130; break;												// 1120 if l = l(5) then 1130
				case 1125: if (ll != l[6]) nextLine = 1145; break;												// 1125 if l <> l(6) then 1145
				case 1130: System.out.println("ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!"); break;			// 1130 print "ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!"
				case 1135: ll = fnA(); break;																	// 1135 l = fna(1)
				case 1140: nextLine = 1045; break;																// 1140 goto 1045
				case 1145: returnFromGosub(); break;															// 1145 return
				case 1150: break;																				// 1150 end
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
}
      