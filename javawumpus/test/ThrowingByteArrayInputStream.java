import java.io.InputStream;
import java.io.EOFException;
import java.io.IOException;

public class ThrowingByteArrayInputStream extends InputStream
{
    private byte[] buf;
    private int index;
    private int mark;

    public ThrowingByteArrayInputStream(byte[] aBuf) {
        buf = aBuf;
        index = 0;
        mark = 0;
    }

    public int read() throws EOFException {
        if (index >= buf.length) {
            throw new EOFException();
        }

        int val = buf[index++];        
        return val;
    }

    public int read(byte[] b) throws IOException {
        return super.read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        // to simulate a user at the console, only return up to the next carriage return's worth of data
        //
        int outputIndex = off;
        
        while (outputIndex - off < len) {
            if (index >= buf.length) {
                break;
            }
            
            byte value = buf[index++];
            b[outputIndex++] = value;
            if ( (char)value == '\r' ) {
                break;
            }
        }
        
        return outputIndex - off;
    }

    public boolean markSupported() {
        return true;
    }

    public void mark(int readlimit) {
        mark = index;
    }
    
    public void reset() {
        index = mark;
    }
    
    public int available() {
        return 0;
    }
}
