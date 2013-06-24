import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterfaceWithThrow extends UserInterface {
    @Override
	public int readInt() throws IOException {
		String line = "";
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		try {
			line = is.readLine();
		} catch (IOException e) {
            throw e;
		}
		return Integer.parseInt(line);
	}
}
