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
        assertEquals(Player.WinLoseState.PLAYING, player.winLoseState());
        
        player.setArrowInventory(3);
        assertEquals(3, player.arrowInventory());
        assertEquals(Player.WinLoseState.PLAYING, player.winLoseState());

        player.setWinLoseState(Player.WinLoseState.LOST);
        assertEquals(3, player.arrowInventory());
        assertEquals(Player.WinLoseState.LOST, player.winLoseState());
    }
}
