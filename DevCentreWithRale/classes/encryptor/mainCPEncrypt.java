package encryptor;

/*
 * Testing for reliability of CPEncryption.java
 *
 * Created on October 3, 2001, 10:45 AM
 */

import sun.misc.*;
import java.lang.*;
import java.security.*;
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;

/**
 *
 * @author  BMutia
 * @version
 */
public class mainCPEncrypt {
    
    /** Creates new testPsec */
    public mainCPEncrypt() {
        Security.addProvider( new com.sun.crypto.provider.SunJCE() );
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) throws NoSuchAlgorithmException {                
        
        String data = "unwar admin.war and the whole other stuff";
        
        DESedeKeyGenerator kgen = new DESedeKeyGenerator();
        
        String staticKey = "GWHEwoqoifQxBwdbZwQLbhlhxMKKqIn0"; //kgen.generate();
        
        DESedeEncryptor psec = new DESedeEncryptor(); //
        
        System.out.println( "Key  : " + staticKey );
        
        System.out.println( "Original  : " + data );
                
        String encrypted = psec.encryptToBase64( data, staticKey ); // pass encryption key                        
        
        System.out.println( "Encrypted  : " + encrypted );
        
        System.out.println( "Decrypted : " + psec.decryptFromBase64( encrypted, staticKey ) );
        
        
    } // main
    
}
