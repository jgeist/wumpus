public class Player {
    private static final int INITIAL_ARROWS = 5;

    private int arrowInventory;
    private boolean won;

    public Player() {
        resetToStartOfGameState();
    }

    public void resetToStartOfGameState() {
        arrowInventory = INITIAL_ARROWS;
        won = false;
    }

    public int arrowInventory() {
        return arrowInventory;
    }
    
    public void setArrowInventory(int n) {
        arrowInventory = n;
    }
    
    public void setWon() {
        won = true;
    }

    public void setLost() {
        won = false;
    }
    
    public boolean won() {
        return won;
    }
}
