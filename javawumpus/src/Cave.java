import java.util.Random;

public class Cave {
	public final static int[][] caveStructure = {{0,0,0,0},
                                                 {0,2,5,8},		{0,1,3,10},		{0,2,4,12},		{0,3,5,14},		{0,1,4,6},
                                                 {0,5,7,15},	{0,6,8,17},		{0,1,7,9},		{0,8,10,18},	{0,2,9,11},
                                                 {0,10,12,19},	{0,3,11,13},	{0,12,14,20},	{0,4,13,15},	{0,6,14,16},
                                                 {0,15,17,20},	{0,7,16,18},	{0,9,17,19},	{0,11,18,20},	{0,13,16,19}};
    
    private Random random;

    public Cave(Random random) {
        this.random = random;
    }

    public int numberOfRooms() {
        return caveStructure.length - 1;
    }

    public int connectionsFromRoom(int room) {
        return caveStructure[room].length - 1;
    }

    public int connection(int room, int tunnel) {
        return caveStructure[room][tunnel];
    }

    public int randomRoom() {
		return random.nextInt(numberOfRooms()) + 1;
    }

	public int randomTunnel(int room) {
		return random.nextInt(connectionsFromRoom(room)) + 1;
	}
}
