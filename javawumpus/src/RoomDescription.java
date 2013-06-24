import java.util.ArrayList;
import java.util.Iterator;

public class RoomDescription {  
    private int roomNumber;
    private ArrayList<Integer> connectedRooms = new ArrayList<Integer>();
    private int wumpiiNearby = 0;
    private int pitsNearby = 0;
    private int batsNearby = 0;

    public RoomDescription(int room) {
        roomNumber = room;
    }

    public void addConnectedRoom(int room) {
        connectedRooms.add(room);
    }

    public void addNearbyWumpus() {
        wumpiiNearby++;
    }

    public void addNearbyPit() {
        pitsNearby++;
    }

    public void addNearbyBat() {
        batsNearby++;
    }

    public int roomNumber() {
        return roomNumber;
    }

    public Iterator<Integer> connectedRooms() {
        return connectedRooms.iterator();
    }
    
    public int nearbyWumpii() {
        return wumpiiNearby;
    }

    public int nearbyPits() {
        return pitsNearby;
    }

    public int nearbyBats() {
        return batsNearby;
    }
}