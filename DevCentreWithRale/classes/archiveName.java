/*
 * weblogReader.java
 *
 * Created on October 24, 2000, 3:14 PM
 */

import java.io.*;
import java.util.*;


public class archiveName extends Object {
    
    DataInputStream in;
    String buildXml;
    
    // class constructor
    public archiveName( String fileName ) {
        buildXml = fileName;
    }
    
    public boolean exists() {
        File xmlFile = new File( buildXml );
        return xmlFile.exists()? true : false;
    }
    
    
    // read data
    public String getArchiveName() {
        
        boolean warFileFound = false;
        String s = new String();
        String rvalue = new String();
        
        try {
            in = new DataInputStream( new BufferedInputStream( new FileInputStream( buildXml ) ) );
            try {
                while( (s = in.readLine()) != null ) {
                    if( ( s.indexOf( "warfile" ) != -1 ) || (s.indexOf( "destfile" ) != -1) ) {
                        rvalue = s;
                        warFileFound = true;
                        break;
                    }
                }
                
                try {
                    in.close();
                } catch( IOException e ){
                }
            } catch( IOException e){
            }
            
            if( !warFileFound ) {
                rvalue = null;
            } else {
                rvalue = rvalue.substring( rvalue.indexOf( '\"')+1, rvalue.indexOf( ".war" )+4 );
            }
            
        } catch( IOException e) {
            rvalue = "Error";
        }
                
        return rvalue;
        
    }
    
    
    // inner class
    class outFile extends DataOutputStream {
        
        public outFile( String filename ) throws IOException {
            super( new BufferedOutputStream( new FileOutputStream( filename ) ) );
        }
        
        public outFile(File file) throws IOException {
            this(file.getPath());
        }
        
    }
    
    
    // inner class
    class filter extends Object implements FilenameFilter {
        
        String afn;
        
        public filter( String afn ) {
            this.afn = afn;
        }
        
        public boolean accept(File dir, String name) {
            // Strip path information:
            String f = new File(name).getName();
            return f.indexOf(afn) != -1;
        }
        
    }
    
    
    
}