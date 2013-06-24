import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class CaveTest {
    public Cave cave;   
    
    @Before 
    public void setUp() {
        cave = new Cave(new Random(0));
    }

    @Test
    public void testCave() {
        assertEquals(20, cave.numberOfRooms());
        
        for (int i = 1; i <= 20; i++) {
            assertEquals(3, cave.connectionsFromRoom(i));
        }
        
        assertFalse(cave.connection(1,1) == cave.connection(1,2));
        assertFalse(cave.connection(1,1) == cave.connection(1,3));
        assertFalse(cave.connection(1,2) == cave.connection(1,3));
    }

    @Test
    public void testRandomRooms() {
        assertEquals(1, cave.randomRoom());
        assertEquals(9, cave.randomRoom());
        assertEquals(10, cave.randomRoom());
    }
}
