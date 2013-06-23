public class Player {
    private static final int INITIAL_ARROWS = 5;

    private int arrowInventory;
    private boolean gameOver;
    private boolean won;

    public Player() {
        resetToStartOfGameState();
    }

    public void resetToStartOfGameState() {
        arrowInventory = INITIAL_ARROWS;
        gameOver = false;
        won = false;
    }

    public int arrowInventory() {
        return arrowInventory;
    }
    
    public void setArrowInventory(int n) {
        arrowInventory = n;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void setWon() {
        won = true;
        gameOver = true;
    }

    public void setLost() {
        won = false;
        gameOver = true;
    }
    
    public boolean won() {
        return won;
    }
}
