import java.util.HashSet;

public class WumpusMap {
    private int playerPosition;
    private int wumpusPosition;
    private HashSet<Integer> pitPositions = new HashSet<Integer>();
    private HashSet<Integer> batPositions = new HashSet<Integer>();

    public void setPlayerPosition(int p) {
        playerPosition = p;
    }
    
    public int playerPosition() {
        return playerPosition;
    }
    
    public void setWumpusPosition(int p) {
        wumpusPosition = p;
    }

    public int wumpusPosition() {
        return wumpusPosition;
    }
    
    public void addBat(int p) {
        batPositions.add(p);
    }
    
    public boolean hasBatAt(int p) {
        return batPositions.contains(p);
    }

    public void addPit(int p) {
        pitPositions.add(p);
    }
    
    public boolean hasPitAt(int p) {
        return pitPositions.contains(p);
    }
}
