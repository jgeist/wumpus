import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    public Player player;
    
    @Before
    public void setUp() {
        player = new Player();
    }

    @Test
    public void testProperties() {
        assertEquals(5, player.arrowInventory());

        player.setArrowInventory(3);
        assertEquals(3, player.arrowInventory());

        player.setWon();
        assertEquals(3, player.arrowInventory());
        assertEquals(true, player.won());
        
        player.resetToStartOfGameState();
        assertEquals(5, player.arrowInventory());

        player.setLost();
        assertEquals(5, player.arrowInventory());
        assertEquals(false, player.won());
        
        player.setArrowInventory(2);
        player.setWon();
        
        assertEquals(false, player.consumeArrowAndTestIfOut());
        assertEquals(true, player.won());

        assertEquals(true, player.consumeArrowAndTestIfOut());
        assertEquals(false, player.won());
    }
}
