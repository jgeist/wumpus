import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Iterator;

public class Wumpus {
    private static final int MAX_ROOMS_FOR_ARROW_SHOT = 5;

	public static Random random = new Random();
    public static Cave cave = new Cave(random);
    public static WumpusMap map = new WumpusMap();
    public static WumpusMap startingMap = new WumpusMap();
    public static Player player = new Player();
    public static UserInterface ui = new UserInterface();
    public static boolean gameOver = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
            runMainLoop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int randomWumpusMove() {
        int connections = cave.connectionsFromRoom(map.wumpusPosition());
        int move = random.nextInt(connections + 1) + 1;
        return (move <= connections) ? move : 0;
	}
    
    public static boolean isGameOver() {
        return gameOver;
    }

    public static boolean didPlayerWin() {
        return player.won();
    }

    public static void endGameWithPlayerLosing() {
        gameOver = true;
        player.setLost();
    }

    public static void endGameWithPlayerWinning() {
        gameOver = true;
        player.setWon();
    }

    public static RoomDescription buildRoomDescription() {
        int room = map.playerPosition();
        RoomDescription desc = new RoomDescription(room);
        
        int connections = cave.connectionsFromRoom(room);

        for (int i = 1; i <= connections; i++) {
            int nearbyRoom = cave.connection(room, i);

            desc.addConnectedRoom(nearbyRoom);

            if (nearbyRoom == map.wumpusPosition()) desc.addNearbyWumpus();
            if (map.hasPitAt(nearbyRoom)) desc.addNearbyPit();
            if (map.hasBatAt(nearbyRoom)) desc.addNearbyBat();
        }

        return desc;
    }

    public static void moveWumpus() {
        int k = randomWumpusMove();
        if (k != 0) {
            map.setWumpusPosition(cave.connection(map.wumpusPosition(),k));
        }
        if (map.wumpusPosition() == map.playerPosition()) {
            ui.println("TSK TSK TSK - WUMPUS GOT YOU!");
            endGameWithPlayerLosing();
        }
    }

    public static void evaluateArrowShoot(int pathLength, int[] rooms) {
        int arrowPosition = map.playerPosition();
        for (int k = 1; k <= pathLength; k++) {
            int k1;
            for (k1 = 1; k1 <= cave.connectionsFromRoom(arrowPosition); k1++) {
                if (cave.connection(arrowPosition,k1) == rooms[k]) {
                    arrowPosition = rooms[k];  
                    break;
                }
            }
            if (k1 > cave.connectionsFromRoom(arrowPosition)) {
                arrowPosition = cave.connection(arrowPosition, cave.randomTunnel(arrowPosition));
            }
             
            if (arrowPosition == map.wumpusPosition()) {
                ui.println("AHA! YOU GOT THE WUMPUS!");
                endGameWithPlayerWinning();
            }   
            if (arrowPosition == map.playerPosition()) {
                ui.println("OUCH! ARROW GOT YOU!");
                endGameWithPlayerLosing();
            }
        }
    }

    public static void promptAndShootArrow() throws IOException {
        int[] p = new int[MAX_ROOMS_FOR_ARROW_SHOT+1];
        int roomCount;

        // path of arrow
        roomCount = ui.promptForArrowPathRoomCount(MAX_ROOMS_FOR_ARROW_SHOT);
        ui.promptForArrowPathRooms(roomCount, p);

        evaluateArrowShoot(roomCount, p);

        if (!isGameOver()) {
            ui.println("MISSED");
            moveWumpus();
            if (player.consumeArrowAndTestIfOut()) {
                endGameWithPlayerLosing();
            }
        }
    }

    public static void movePlayerAndTestHazards(int roomToMoveTo) {
        while (true) {
            map.setPlayerPosition(roomToMoveTo);
            if (roomToMoveTo == map.wumpusPosition()) {
                ui.println("... OOPS! BUMPED A WUMPUS!");
                moveWumpus();
                if (isGameOver()) {
                    return;
                }
            }
        
            if (map.hasPitAt(roomToMoveTo)) {
                ui.println("YYYYIIIIEEEE . . . FELL IN PIT");
                endGameWithPlayerLosing();
                return;
            }
        
            if (map.hasBatAt(roomToMoveTo)) {
                ui.println("ZAP--SUPER BAT SNATCH! ELSEWHEREVILLE FOR YOU!");
                roomToMoveTo = cave.randomRoom();
                continue;
            }
            
            break;
        }
    }

    public static void promptAndMovePlayer(RoomDescription currentRoom) throws IOException {
        int roomToMoveTo = ui.promptForRoomToMoveTo(currentRoom, cave.numberOfRooms());
        movePlayerAndTestHazards(roomToMoveTo);
    }

    public static void placeObjectsRandomlyOnMap() {
        while (true) {
            map = new WumpusMap();
            map.setPlayerPosition(cave.randomRoom());
            map.setWumpusPosition(cave.randomRoom());
            map.addPit(cave.randomRoom());
            map.addPit(cave.randomRoom());
            map.addBat(cave.randomRoom());
            map.addBat(cave.randomRoom());                    
            
            if (!map.hasOverlap()) {
                break;
            }
        }
    }

    public static void promptAndExecutePlayerAction(RoomDescription currentRoom) throws IOException{
        switch (ui.promptForShootOrMove()) {
        case SHOOT:
            promptAndShootArrow();
            break;
            
        case MOVE:
            promptAndMovePlayer(currentRoom);
            break;
        }            
    }
    
    public static void printWinLoseState() {
        if (didPlayerWin()) {
            ui.println("HEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME!!");
        } else {
            ui.println ("HA HA HA - YOU LOSE!");
        }
    }

    public static boolean promptToReplaySameMap() throws IOException {
        char i$ = ui.promptAndReadSingleCharacterOption("SAME SETUP (Y-N)");        
        return i$ == 'y' || i$ == 'Y';
    }
    
    public static void saveStartingMapFromGameMap() {
        startingMap = (WumpusMap)map.clone();
    }

    public static void loadGameMapFromStartingMap() {
        map = (WumpusMap)startingMap.clone();
    }
    
    public static void setUpGame(boolean regenerateLevel) {
        if (regenerateLevel) {
            placeObjectsRandomlyOnMap();
            saveStartingMapFromGameMap();
        }
        
        gameOver = false;
        player.resetToStartOfGameState();
    }

    public static void runMainLoop() throws IOException {
        boolean randomize = true;

        ui.promptAndShowInstructions(cave.numberOfRooms());

        while (true) {
            setUpGame(randomize);

            ui.println("HUNT THE WUMPUS");

            do {
                RoomDescription currentRoom = buildRoomDescription();
                ui.printRoomDescription(currentRoom);
                promptAndExecutePlayerAction(currentRoom);
            } while (!isGameOver());
            
            printWinLoseState();
            loadGameMapFromStartingMap();

            randomize = !promptToReplaySameMap();            
        }
    }
}
