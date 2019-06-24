/*
 * StreamGobbler.java
 *
 * Created on November 19, 2002, 10:25 PM
 */
import java.util.*;
import java.io.*;
/**
 *
 * @author  test1
 */
class StreamGobbler extends Thread {
    
    InputStream is;
    String type;
    mainForm main;
    
    StreamGobbler(InputStream is, String type, mainForm parent ) {
        this.is = is;
        this.type = type;
        main = parent;
    }
    
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                main.printMessage(line);
        } catch (IOException ioe) {
            main.printMessage( ioe.toString() );
        }
    }
}

