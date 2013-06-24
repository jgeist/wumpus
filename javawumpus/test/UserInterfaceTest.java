import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class UserInterfaceTest {
    private final ByteArrayOutputStream textOutput = new ByteArrayOutputStream();
    private final String CR = "\r";
    private UserInterface ui;

	@Before
	public void setUp() {
        ui = new UserInterface();
        System.setOut(new PrintStream(textOutput));
	}

    @After
    public void tearDown() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
    
    @Test
    public void testReadInt() {
        String input = "42" + CR;
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        int value = 0;

        try {
            value = ui.readInt();
        } catch (IOException ex) {
            fail("Unexpected IOException from user interface");
        }

        assertEquals(42, value);
    }

    @Test
    public void testPromptAndRead() {
        String input = "X" + CR;
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));

        char value = 0;
        try {
            value = ui.promptAndReadSingleCharacterOption("ENTER VALUE");
        } catch (IOException ex) {
            fail("Unexpected IOException from user interface");
        }
        
        assertEquals('X', value);
        System.out.flush();
        assertEquals("ENTER VALUE", textOutput.toString());    
    }

    @Test
    public void testThrowingReadInt() {
        String input = "";
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        int value = 0;

        UserInterface ui = new UserInterfaceWithThrow();
        try {
            value = ui.readInt();
        } catch (IOException ex) {
            return;
        }

        fail("Test did not throw expected exception");
    }

    @Test
    public void testShootOrMovePromptShoot() throws IOException {   
        String input = "S\r";
        String expectedOutput = "SHOOT OR MOVE (S-M) ";
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        UserInterface.PlayerAction o = ui.promptForShootOrMove();

        System.out.flush();
        assertEquals(expectedOutput, textOutput.toString());    
        assertEquals(UserInterface.PlayerAction.SHOOT, o);
    }

    @Test
    public void testShootOrMovePromptMove() throws IOException {   
        String input = "M\r";
        String expectedOutput = "SHOOT OR MOVE (S-M) ";
        System.setIn(new ThrowingByteArrayInputStream(input.getBytes()));
        
        UserInterface.PlayerAction o = ui.promptForShootOrMove();

        System.out.flush();
        assertEquals(expectedOutput, textOutput.toString());    
        assertEquals(UserInterface.PlayerAction.MOVE, o);
    }

    @Test
    public void testRoomDescriptionNormal() throws IOException {
        // player is in room 1, everything else is off the map
        Wumpus.map.setPlayerPosition(1);
        Wumpus.map.setWumpusPosition(999);
        Wumpus.map.addPit(998);
        Wumpus.map.addPit(999);
        Wumpus.map.addBat(998);
        Wumpus.map.addBat(999);
        
        String expectedOutput =     
            "\n" +
            "YOUR ARE IN ROOM 1\n" +
            "TUNNELS LEAD TO 2 5 8\n" +
            "\n";
        
        ui.printRoomDescription(Wumpus.buildRoomDescription());
        
        System.out.flush();        
        assertEquals(expectedOutput, textOutput.toString());
    }

    @Test
    public void testRoomDescriptionWumpus() throws IOException {
        RoomDescription desc = new RoomDescription(1);
        desc.addConnectedRoom(2);
        desc.addConnectedRoom(5);
        desc.addConnectedRoom(8);
        desc.addNearbyWumpus();
        
        String expectedOutput =     
            "\n" +
            "I SMELL A WUMPUS!\n" + 
            "YOUR ARE IN ROOM 1\n" +
            "TUNNELS LEAD TO 2 5 8\n" +
            "\n";
        
        ui.printRoomDescription(desc);
        
        System.out.flush();        
        assertEquals(expectedOutput, textOutput.toString());
    }

    @Test
    public void testRoomDescriptionPits() throws IOException {
        RoomDescription desc = new RoomDescription(1);
        desc.addConnectedRoom(2);
        desc.addConnectedRoom(5);
        desc.addConnectedRoom(8);
        desc.addNearbyPit();
        
        String expectedOutput =     
            "\n" +
            "I FEEL A DRAFT\n" + 
            "YOUR ARE IN ROOM 1\n" +
            "TUNNELS LEAD TO 2 5 8\n" +
            "\n";
        
        ui.printRoomDescription(desc);
        
        System.out.flush();        
        assertEquals(expectedOutput, textOutput.toString());
    }

    @Test
    public void testRoomDescriptionBats() throws IOException {
        RoomDescription desc = new RoomDescription(1);
        desc.addConnectedRoom(2);
        desc.addConnectedRoom(5);
        desc.addConnectedRoom(8);
        desc.addNearbyBat();
        
        String expectedOutput =     
            "\n" +
            "BATS NEARBY!\n" + 
            "YOUR ARE IN ROOM 1\n" +
            "TUNNELS LEAD TO 2 5 8\n" +
            "\n";
        
        ui.printRoomDescription(desc);
        
        System.out.flush();        
        assertEquals(expectedOutput, textOutput.toString());
    }

    @Test
    public void testRoomDescriptionEverything() throws IOException {
        RoomDescription desc = new RoomDescription(1);
        desc.addConnectedRoom(2);
        desc.addConnectedRoom(5);
        desc.addConnectedRoom(8);
        desc.addNearbyWumpus();
        desc.addNearbyPit();
        desc.addNearbyBat();

        String expectedOutput =     
            "\n" +
            "I SMELL A WUMPUS!\n" + 
            "I FEEL A DRAFT\n" +
            "BATS NEARBY!\n" + 
            "YOUR ARE IN ROOM 1\n" +
            "TUNNELS LEAD TO 2 5 8\n" +
            "\n";

        ui.printRoomDescription(desc);
        
        System.out.flush();        
        assertEquals(expectedOutput, textOutput.toString());
    }
}