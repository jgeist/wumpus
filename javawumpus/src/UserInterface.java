import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface {
	public int readInt() throws IOException {
		String line = "";
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		try {
			line = is.readLine();
		} catch (IOException e) {
			return 0;
		}
		return Integer.parseInt(line);
	}

    public void waitForUserToHitReturn() throws IOException {
        System.in.read();
    }

    public char promptAndReadSingleCharacterOption(String prompt) throws IOException {
        System.out.print(prompt);
        char option = (char)System.in.read();
        System.in.read();
        return option;
    }

    public void print(String line) {
        System.out.print(line);
    }

    public void println(String line) {
        System.out.println(line);
    }

    public void print(int i) {
        print(String.format("%d", i));
    }

    public void println(int i) {
        println(String.format("%d", i));
    }
}
