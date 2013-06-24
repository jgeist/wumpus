import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
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
        System.setOut(null);
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
    public void testPrint() {
        ui.print("HELLO WORLD");
        System.out.flush();
        assertEquals("HELLO WORLD", textOutput.toString());    
    }

    @Test
    public void testPrintLine() {
        ui.println("HELLO WORLD");
        System.out.flush();
        assertEquals("HELLO WORLD\n", textOutput.toString());    
    }

    @Test 
    public void testPrintLineInteger() {
        ui.println(42);
        System.out.flush();
        assertEquals("42\n", textOutput.toString());    
    }

    @Test 
    public void testPrintInteger() {
        ui.print(42);
        System.out.flush();
        assertEquals("42", textOutput.toString());    
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
}