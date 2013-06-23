public class Player {
    private static final int INITIAL_ARROWS = 5;

    private int arrowInventory;

    public enum WinLoseState {
        LOST,
        PLAYING,
        WON    
    };
    
    private WinLoseState winLoseState;

    public Player() {
        resetToStartOfGameState();
    }

    public void resetToStartOfGameState() {
        arrowInventory = INITIAL_ARROWS;
        winLoseState = WinLoseState.PLAYING;
    }

    public int arrowInventory() {
        return arrowInventory;
    }
    
    public void setArrowInventory(int n) {
        arrowInventory = n;
    }

    public WinLoseState winLoseState() {
        return winLoseState;
    }

    public void setWinLoseState(WinLoseState state) {
        winLoseState = state;
    }
}
