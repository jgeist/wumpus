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

        assertEquals(false, map.hasOverlap());
        
        map.addPit(7);
        assertEquals(2, map.batCount());
        assertEquals(3, map.pitCount());
        
        assertEquals(true, map.equals(map));
    }

    @Test
    public void testOverlap() {
        map.setPlayerPosition(2);
        assertEquals(true, map.hasOverlap());

        map.setPlayerPosition(3);
        assertEquals(true, map.hasOverlap());

        map.setPlayerPosition(4);
        assertEquals(true, map.hasOverlap());

        map.setPlayerPosition(5);
        assertEquals(true, map.hasOverlap());

        map.setPlayerPosition(6);
        assertEquals(true, map.hasOverlap());

        map.setPlayerPosition(1);
        map.setWumpusPosition(3);
        assertEquals(true, map.hasOverlap());

        map.setWumpusPosition(4);
        assertEquals(true, map.hasOverlap());

        map.setWumpusPosition(5);
        assertEquals(true, map.hasOverlap());

        map.setWumpusPosition(6);
        assertEquals(true, map.hasOverlap());
        
        map.setWumpusPosition(2);
        assertEquals(false, map.hasOverlap());
        
        map.addBat(3);
        assertEquals(true, map.hasOverlap());

        map = new WumpusMap();
        
        map.setPlayerPosition(1);
        map.setWumpusPosition(2);
        map.addBat(3);
        map.addBat(4);
        map.addPit(3);
        map.addPit(6);            
        assertEquals(true, map.hasOverlap());
    }

    @Test
    public void testClone() {
        WumpusMap mapTwo = (WumpusMap)map.clone();

        assertEquals(true, map.equals(mapTwo));
    }

    @Test
    public void testEquals() {
        WumpusMap mapTwo = (WumpusMap)map.clone();

        assertEquals(true, map.equals(mapTwo));
        mapTwo.addPit(42);
        assertEquals(false, map.equals(mapTwo));
    }    
}
