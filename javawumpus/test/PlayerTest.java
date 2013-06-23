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
        assertEquals(false, player.isGameOver());

        player.setArrowInventory(3);
        assertEquals(3, player.arrowInventory());
        assertEquals(false, player.isGameOver());

        player.setWon();
        assertEquals(3, player.arrowInventory());
        assertEquals(true, player.isGameOver());
        assertEquals(true, player.won());
        
        player.resetToStartOfGameState();
        assertEquals(5, player.arrowInventory());
        assertEquals(false, player.isGameOver());

        player.setLost();
        assertEquals(5, player.arrowInventory());
        assertEquals(true, player.isGameOver());
        assertEquals(false, player.won());
    }
}
