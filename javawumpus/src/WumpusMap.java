import java.util.HashSet;

public class WumpusMap {
    private int playerPosition;
    private int wumpusPosition;
    private HashSet<Integer> pitPositions = new HashSet<Integer>();
    private HashSet<Integer> batPositions = new HashSet<Integer>();
    private int batsAdded = 0;
    private int pitsAdded = 0;

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
        batsAdded++;
    }
    
     public int batCount() {
        return batPositions.size();
    }
    
    public boolean hasBatAt(int p) {
        return batPositions.contains(p);
    }

    public void addPit(int p) {
        pitPositions.add(p);
        pitsAdded++;
    }
    
    public boolean hasPitAt(int p) {
        return pitPositions.contains(p);
    }

    public int pitCount() {
        return pitPositions.size();
    }

    @Override
    public Object clone() {
        WumpusMap map = new WumpusMap();
        
        map.setPlayerPosition(playerPosition());
        map.setWumpusPosition(wumpusPosition());
        
        for (Integer i : batPositions) {
            map.addBat(i);
        }

        for (Integer i : pitPositions) {
            map.addPit(i);
        }

        return map;
    }
    
    @Override 
    public boolean equals(Object other) {
        if (other.getClass() != getClass()) {
            return false;
        }
        
        WumpusMap otherMap = (WumpusMap)other;
        
        if (playerPosition != otherMap.playerPosition || wumpusPosition != otherMap.wumpusPosition) {
            return false;
        }
        
        if (batPositions.size() != otherMap.batPositions.size() || pitPositions.size() != otherMap.pitPositions.size()) {
            return false;
        }
        
        for (Integer i : batPositions) {
            if (!otherMap.batPositions.contains(i)) {
                return false;
            }
        }
        
        for (Integer i : pitPositions) {
            if (!otherMap.pitPositions.contains(i)) {
                return false;
            }
        }
        
        return true;
    }

    public boolean hasOverlap() {
        if (batPositions.size() < batsAdded || pitPositions.size() < pitsAdded) {
            return true;
        }

        if (playerPosition == wumpusPosition || hasBatAt(playerPosition) || hasPitAt(playerPosition)) {
            return true;
        }

        if (hasBatAt(wumpusPosition) || hasPitAt(wumpusPosition)) {
            return true;
        }
        
        for (Integer i : batPositions) {
            if (hasPitAt(i)) {
                return true;
            }
        }
        
        for (Integer i : pitPositions) {
            if (hasBatAt(i)) {
                return true;
            }
        }

        return false;
    }
}
