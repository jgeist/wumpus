import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WumpusMapTest {
    public WumpusMap map;

    @Before
    public void setUp() {
        map = new WumpusMap();
        
        map.setPlayerPosition(1);
        map.setWumpusPosition(2);
        map.addBat(3);
        map.addBat(4);
        map.addPit(5);
        map.addPit(6);            
    }

    @Test
    public void testMap() {
        assertEquals(1, map.playerPosition());
        assertEquals(2, map.wumpusPosition());
        
        assertEquals(true, map.hasBatAt(3));
        assertEquals(true, map.hasBatAt(4));
        assertEquals(false, map.hasBatAt(5));
        assertEquals(false, map.hasBatAt(6));
        
        assertEquals(false, map.hasPitAt(3));
        assertEquals(false, map.hasPitAt(4));
        assertEquals(true, map.hasPitAt(5));
        assertEquals(true, map.hasPitAt(6));
    }
}
