import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.Iterator;

public class RoomDescriptionTest {
    RoomDescription desc;

    @Before 
    public void setUp() {
        desc = new RoomDescription(10);
    }

    @Test
    public void testDescription() {
        int[] connected = { 3, 5, 7 };

        for (int i : connected) {
            desc.addConnectedRoom(i);
        }
        
        desc.addNearbyWumpus();
        desc.addNearbyPit();
        desc.addNearbyPit();
        desc.addNearbyBat();
        desc.addNearbyBat();
        desc.addNearbyBat();

        assertEquals(10, desc.roomNumber());
        
        int index = 0;
        Iterator<Integer> rooms = desc.connectedRooms();
        while (rooms.hasNext()) {
            int room = (int)rooms.next();
            assertEquals(connected[index], room);
            index++;
        }
        assertEquals(index, connected.length);

        assertEquals(1, desc.nearbyWumpii());
        assertEquals(2, desc.nearbyPits());
        assertEquals(3, desc.nearbyBats());
    }    
}