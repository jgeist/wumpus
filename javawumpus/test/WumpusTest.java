import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class WumpusTest {
    private final ByteArrayOutputStream textOutput = new ByteArrayOutputStream();
    private final String CR = "\r";


	@Before
	public void setUp() {
		Wumpus.random = new Random(0); //seed random for expected results
        Wumpus.cave = new Cave(Wumpus.random);
        Wumpus.ui = new UserInterfaceWithThrow();
        Wumpus.map = new WumpusMap();
        System.setOut(new PrintStream(textOutput));
	}

    @After
    public void tearDown() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
	
	@Test
	public void randomRoom() throws Exception {
		assertEquals(1, Wumpus.cave.randomRoom());
		assertEquals(9, Wumpus.cave.randomRoom());
		assertEquals(10, Wumpus.cave.randomRoom());
		assertEquals(8, Wumpus.cave.randomRoom());
		assertEquals(16, Wumpus.cave.randomRoom());
		assertEquals(14, Wumpus.cave.randomRoom());
		assertEquals(12, Wumpus.cave.randomRoom());
		assertEquals(2, Wumpus.cave.randomRoom());
		assertEquals(20, Wumpus.cave.randomRoom());
		assertEquals(15, Wumpus.cave.randomRoom());
	}

	@Test
	public void randomConnection() throws Exception {
		assertEquals(1, Wumpus.cave.randomTunnel(3));
		assertEquals(2, Wumpus.cave.randomTunnel(3));
		assertEquals(2, Wumpus.cave.randomTunnel(3));
		assertEquals(3, Wumpus.cave.randomTunnel(3));
		assertEquals(3, Wumpus.cave.randomTunnel(3));
		assertEquals(3, Wumpus.cave.randomTunnel(3));
		assertEquals(3, Wumpus.cave.randomTunnel(3));
		assertEquals(1, Wumpus.cave.randomTunnel(3));
		assertEquals(1, Wumpus.cave.randomTunnel(3));
		assertEquals(3, Wumpus.cave.randomTunnel(3));
	}
	
	@Test
	public void randomWumpusMove() throws Exception {
		assertEquals(3, Wumpus.randomWumpusMove());
		assertEquals(0, Wumpus.randomWumpusMove());
		assertEquals(1, Wumpus.randomWumpusMove());
		assertEquals(3, Wumpus.randomWumpusMove());
		assertEquals(3, Wumpus.randomWumpusMove());
		assertEquals(2, Wumpus.randomWumpusMove());
		assertEquals(3, Wumpus.randomWumpusMove());
		assertEquals(1, Wumpus.randomWumpusMove());
		assertEquals(3, Wumpus.randomWumpusMove());
		assertEquals(0, Wumpus.randomWumpusMove());
	}

    @Test
    public void testShootArrowMissing() throws IOException {   
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(19);
        Wumpus.map.addPit(18);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(18);
        Wumpus.map.addBat(19);

        String input = "1" + CR + "2" + CR;     // shoot arrow into room 2
        
        String expectedOutput = 
            "NO. OF ROOMS (1-5) " +
            "ROOM # " + 
            "MISSED\n";
        
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        Wumpus.promptAndShootArrow();

        System.out.flush();
        assertEquals(expectedOutput, textOutput.toString());    
        assertEquals(false, Wumpus.isGameOver());
        assertEquals(20, Wumpus.map.wumpusPosition());      // on missed, Wumpus moves
    }

    @Test
    public void testShootArrowHitting() throws IOException {   
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(2);
        Wumpus.map.addPit(18);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(18);
        Wumpus.map.addBat(19);

        String input = "1" + CR + "2" + CR;     // shoot arrow into room 2
        
        String expectedOutput = 
            "NO. OF ROOMS (1-5) " +
            "ROOM # " + 
            "AHA! YOU GOT THE WUMPUS!\n";
        
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        Wumpus.promptAndShootArrow();

        System.out.flush();
        assertEquals(expectedOutput, textOutput.toString());    
        assertEquals(true, Wumpus.isGameOver());
        assertEquals(true, Wumpus.didPlayerWin());
    }

    @Test
    public void testShootArrowAtSelf() throws IOException {   
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(19);
        Wumpus.map.addPit(18);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(18);
        Wumpus.map.addBat(19);

        String input = "2" + CR + "2" + CR + "1" + CR;     // shoot arrow into next room and back
        
        String expectedOutput = 
            "NO. OF ROOMS (1-5) " +
            "ROOM # " +
            "ROOM # " + 
            "OUCH! ARROW GOT YOU!\n";
        
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        Wumpus.promptAndShootArrow();

        System.out.flush();
        assertEquals(expectedOutput, textOutput.toString());    
        assertEquals(true, Wumpus.isGameOver());
        assertEquals(false, Wumpus.didPlayerWin());
        assertEquals(19, Wumpus.map.wumpusPosition());
    }

    @Test
    public void testShootArrowBackwards() throws IOException  {   
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(19);
        Wumpus.map.addPit(18);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(18);
        Wumpus.map.addBat(19);

        String input = "3" + CR + "2" + CR + "3" + CR + "2" + CR;     // shoot arrow into next room and back
        
        String expectedOutput = 
            "NO. OF ROOMS (1-5) " +
            "ROOM # " +
            "ROOM # " + 
            "ROOM # " + 
            "ARROWS AREN'T THAT CROOKED - TRY ANOTHER ROOM\n" +
            "ROOM # ";
        
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        

        try {
            Wumpus.promptAndShootArrow();
        } catch (IOException e) {
        }

        System.out.flush();
        assertEquals(expectedOutput, textOutput.toString());    
        assertEquals(false, Wumpus.isGameOver());
        assertEquals(19, Wumpus.map.wumpusPosition());
    }

    @Test
    public void testShootArrowWall() throws IOException {   
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(19);
        Wumpus.map.addPit(18);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(18);
        Wumpus.map.addBat(19);

        String input = "1" + CR + "12" + CR;
        
        String expectedOutput = 
            "NO. OF ROOMS (1-5) " +
            "ROOM # " +
            "MISSED\n";
        
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        Wumpus.promptAndShootArrow();

        System.out.flush();
        assertEquals(expectedOutput, textOutput.toString());    
        assertEquals(false, Wumpus.isGameOver());
        assertEquals(19, Wumpus.map.wumpusPosition());
    }

    @Test
    public void testMoveWumpus() {
        Wumpus.map.setWumpusPosition(1);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.moveWumpus();
        assertEquals(8, Wumpus.map.wumpusPosition());

        Wumpus.moveWumpus();
        assertEquals(8, Wumpus.map.wumpusPosition());

        Wumpus.moveWumpus();
        System.out.flush();

        String expectedOutput = "TSK TSK TSK - WUMPUS GOT YOU!\n";
        
        assertEquals(expectedOutput, textOutput.toString());
        assertEquals(1, Wumpus.map.wumpusPosition());
        assertEquals(true, Wumpus.isGameOver());
        assertEquals(false, Wumpus.didPlayerWin());
    }

    @Test
    public void testMovePlayerNotPossible() throws IOException {
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(19);
        Wumpus.map.addPit(18);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(18);
        Wumpus.map.addBat(19);
        
        String input = "10" + CR;
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        try {
            Wumpus.promptAndMovePlayer(Wumpus.buildRoomDescription());
        } catch (IOException e) {
        }

        System.out.flush();
        
        String expectedOutput = 
            "WHERE TO " +
            "NOT POSSIBLE - WHERE TO ";
        
        assertEquals(expectedOutput, textOutput.toString());
        assertEquals(false, Wumpus.isGameOver());
        assertEquals(1, Wumpus.map.playerPosition());
    }

    @Test
    public void testMovePlayerNothingHappens() throws IOException{
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(19);
        Wumpus.map.addPit(18);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(18);
        Wumpus.map.addBat(19);
        
        String input = "2" + CR;
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        Wumpus.promptAndMovePlayer(Wumpus.buildRoomDescription());

        System.out.flush();
        
        String expectedOutput = 
            "WHERE TO ";
        
        assertEquals(expectedOutput, textOutput.toString());
        assertEquals(false, Wumpus.isGameOver());
        assertEquals(2, Wumpus.map.playerPosition());
    }

    @Test
    public void testMovePlayerRunIntoWumpus() throws IOException {
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(2);
        Wumpus.map.addPit(18);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(18);
        Wumpus.map.addBat(19);
        
        String input = "2" + CR;
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        Wumpus.promptAndMovePlayer(Wumpus.buildRoomDescription());

        System.out.flush();
        
        String expectedOutput = 
            "WHERE TO " +
            "... OOPS! BUMPED A WUMPUS!\n";
        
        assertEquals(expectedOutput, textOutput.toString());
        assertEquals(false, Wumpus.isGameOver());
        assertEquals(2, Wumpus.map.playerPosition());
    }

    @Test
    public void testMovePlayerRunIntoPit() throws IOException {
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(19);
        Wumpus.map.addPit(2);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(18);
        Wumpus.map.addBat(19);
        
        String input = "2" + CR;
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        Wumpus.promptAndMovePlayer(Wumpus.buildRoomDescription());

        System.out.flush();
        
        String expectedOutput = 
            "WHERE TO " +
            "YYYYIIIIEEEE . . . FELL IN PIT\n";
        
        assertEquals(expectedOutput, textOutput.toString());
        assertEquals(true, Wumpus.isGameOver());
        assertEquals(false, Wumpus.didPlayerWin());
        assertEquals(2, Wumpus.map.playerPosition());
    }

    @Test
    public void testMovePlayerRunIntoBat() throws IOException {
        Wumpus.setUpGame(false);
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(19);
        Wumpus.map.addPit(18);
        Wumpus.map.addPit(19);
        Wumpus.map.addBat(2);
        Wumpus.map.addBat(19);
        
        String input = "2" + CR;
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        Wumpus.promptAndMovePlayer(Wumpus.buildRoomDescription());

        System.out.flush();
        
        String expectedOutput = 
            "WHERE TO " +
            "ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!\n";
        
        assertEquals(expectedOutput, textOutput.toString());
        assertEquals(false, Wumpus.isGameOver());
        assertEquals(1, Wumpus.map.playerPosition());
    }

    @Test
    public void gameSessionLosing() throws Exception {
        String input = 
            "Y" + CR + CR + CR + 
            "M" + CR + "2" + CR + 
            "M" + CR + "3" + CR + 
            "M" + CR + "4" + CR +
            "M" + CR + "5" + CR +
            "M" + CR + "6" + CR +
            "M" + CR + "7" + CR +
            "M" + CR + "8" + CR;

        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
   
        String expectedScript = 
            "INSTRUCTIONS (Y-N) " +
            "WELCOME TO 'HUNT THE WUMPUS'\n" +
            "  THE WUMPUS LIVES IN A CAVE OF 20 ROOMS. EACH ROOM\n" +
            "HAS 3 TUNNELS LEADING TO OTHER ROOMS. (LOOK AT A\n" +
            "DODECAHEDRON TO SEE HOW THIS WORKS-IF YOU DON'T KNOW\n" +
            "WHAT A DODECAHEDRON IS, ASK SOMEONE)\n" +
            "\n" +
            "     HAZARDS:\n" +
            " BOTTOMLESS PITS - TWO ROOMS HAVE BOTTOMLESS PITS IN THEM\n" +
            "     IF YOU GO THERE, YOU FALL INTO THE PIT (& LOSE!)\n" +
            " SUPER BATS - TWO OTHER ROOMS HAVE SUPER BATS. IF YOU\n" +
            "     GO THERE, A BAT GRABS YOU AND TAKES YOU TO SOME OTHER\n" +
            "     ROOM AT RANDOM. (WHICH MAY BE TROUBLESOME)\n" +
            "HIT RETURN TO CONTINUE\n" +
            "     WUMPUS:\n" +
            " THE WUMPUS IS NOT BOTHERED BY HAZARDS (HE HAS SUCKER\n" +
            " FEET AND IS TOO BIG FOR A BAT TO LIFT).  USUALLY\n" +
            " HE IS ASLEEP.  TWO THINGS WAKE HIM UP: YOU SHOOTING AN\n" +
            "ARROW OR YOU ENTERING HIS ROOM.\n" +
            "     IF THE WUMPUS WAKES HE MOVES (P=.75) ONE ROOM\n" +
            " OR STAYS STILL (P=.25).  AFTER THAT, IF HE IS WHERE YOU\n" +
            " ARE, HE EATS YOU UP AND YOU LOSE!\n" +
            "\n" +
            "     YOU:\n" +
            " EACH TURN YOU MAY MOVE OR SHOOT A CROOKED ARROW\n" +
            "   MOVING:  YOU CAN MOVE ONE ROOM (THRU ONE TUNNEL)\n" +
            "   ARROWS:  YOU HAVE 5 ARROWS.  YOU LOSE WHEN YOU RUN OUT\n" +
            "   EACH ARROW CAN GO FROM 1 TO 5 ROOMS. YOU AIM BY TELLING\n" +
            "   THE COMPUTER THE ROOM#S YOU WANT THE ARROW TO GO TO.\n" +
            "   IF THE ARROW CAN'T GO THAT WAY (IF NO TUNNEL) IT MOVES\n" +
            "   AT RANDOM TO THE NEXT ROOM.\n" +
            "     IF THE ARROW HITS THE WUMPUS, YOU WIN.\n" +
            "     IF THE ARROW HITS YOU, YOU LOSE.\n" +
            "HIT RETURN TO CONTINUE\n" +
            "    WARNINGS:\n" +
            "     WHEN YOU ARE ONE ROOM AWAY FROM A WUMPUS OR HAZARD,\n" +
            "     THE COMPUTER SAYS:\n" +
            " WUMPUS:  'I SMELL A WUMPUS'\n" +
            " BAT   :  'BATS NEARBY'\n" +
            " PIT   :  'I FEEL A DRAFT'\n" +
            "\n" +
            "HUNT THE WUMPUS\n" +
            "\n" +
            "I FEEL A DRAFT\n" +
            "YOUR ARE IN ROOM 1\n" + 
            "TUNNELS LEAD TO 2 5 8\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "I FEEL A DRAFT\n" +
            "YOUR ARE IN ROOM 2\n" +
            "TUNNELS LEAD TO 1 3 10\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "YOUR ARE IN ROOM 3\n" +
            "TUNNELS LEAD TO 2 4 12\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "BATS NEARBY!\n" +
            "YOUR ARE IN ROOM 4\n" +
            "TUNNELS LEAD TO 3 5 14\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "YOUR ARE IN ROOM 5\n" +
            "TUNNELS LEAD TO 1 4 6\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "YOUR ARE IN ROOM 6\n" +
            "TUNNELS LEAD TO 5 7 15\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "I FEEL A DRAFT\n" +
            "YOUR ARE IN ROOM 7\n" +
            "TUNNELS LEAD TO 6 8 17\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO YYYYIIIIEEEE . . . FELL IN PIT\n" +
            "HA HA HA - YOU LOSE!\n" +
            "SAME SETUP (Y-N)";
        
        Wumpus.main(null);
        System.out.flush();        
        assertEquals(expectedScript, textOutput.toString());
    }

    @Test
    public void gameSessionWinning() throws Exception {
        String input = 
            "N" + CR +
            "M" + CR + "5" + CR + 
            "M" + CR + "6" + CR + 
            "M" + CR + "15" + CR +
            "M" + CR + "16" + CR +
            "M" + CR + "13" + CR +
            "M" + CR + "20" + CR +
            "M" + CR + "19" + CR +
            "M" + CR + "18" + CR +
            "S" + CR + "1" + CR + "9" + CR;

        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
   
        String expectedScript = 
            "INSTRUCTIONS (Y-N) " +
            "HUNT THE WUMPUS\n" +
            "\n" +
            "I FEEL A DRAFT\n" +
            "YOUR ARE IN ROOM 1\n" +
            "TUNNELS LEAD TO 2 5 8\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "YOUR ARE IN ROOM 5\n" +
            "TUNNELS LEAD TO 1 4 6\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "YOUR ARE IN ROOM 6\n" +
            "TUNNELS LEAD TO 5 7 15\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "BATS NEARBY!\n" +
            "BATS NEARBY!\n" +
            "YOUR ARE IN ROOM 15\n" +
            "TUNNELS LEAD TO 6 14 16\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO " +
            "ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!\n" +
            "\n" +
            "YOUR ARE IN ROOM 12\n" +
            "TUNNELS LEAD TO 3 11 13\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "BATS NEARBY!\n" +
            "YOUR ARE IN ROOM 13\n" +
            "TUNNELS LEAD TO 12 14 20\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "BATS NEARBY!\n" +
            "YOUR ARE IN ROOM 20\n" +
            "TUNNELS LEAD TO 13 16 19\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "YOUR ARE IN ROOM 19\n" +
            "TUNNELS LEAD TO 11 18 20\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "WHERE TO \n" +
            "I SMELL A WUMPUS!\n" +
            "YOUR ARE IN ROOM 18\n" +
            "TUNNELS LEAD TO 9 17 19\n" +
            "\n" +
            "SHOOT OR MOVE (S-M) " +
            "NO. OF ROOMS (1-5) " +
            "ROOM # " +
            "AHA! YOU GOT THE WUMPUS!\n" +
            "HEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME!!\n" +
            "SAME SETUP (Y-N)";

        Wumpus.main(null);
        System.out.flush();        
        assertEquals(expectedScript, textOutput.toString());
    }
}

