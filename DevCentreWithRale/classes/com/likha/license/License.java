/*
 * check.java
 *
 * Created on May 24, 2004, 8:12 PM
 */

package com.likha.license;

import org.apache.tools.ant.DirectoryScanner;

import java.io.*;
import java.util.*;
import encryptor.*;

/**
 *
 * @author  Administrator
 */
public class License {
    
    private String expiredMsg = "PLEASE BUY - DEMO LICENSE EXPIRED";
    private String file = null;
    private String file2 = null;
    private String message = new String();
    private String licenseIDEMessage = "Demo License";
    
    private String firstRunDate = null;
    private String todaysDate = null;
    private long nDaysInstalled = -1L;
    private int keyLen = 32;
    
    /** Creates a new instance of check */
    public License() {
        Properties p = System.getProperties();
        file = p.getProperty( "user.home" ) + "\\.~dclx";
        file2 = "License.dat";
    }
    
    
    
    
    public void writeDemoLicense() {
        
        DESedeEncryptor psec = new DESedeEncryptor();
        String key = getKey();
        String dateNow = new Date().toString();
        message = key + psec.encryptToBase64( dateNow, key );
        
        try {
            
            File f = new File( file );
            // write <user.dir>.devcentre
            if( !f.exists() ) {
                OutputStream client = new FileOutputStream( file );
                client.write( message.getBytes() );
                client.close();
            }
            
        } catch( java.io.IOException ioe ) {
            System.out.println( ioe.toString() );
        }
        
    }
    
    public boolean writePaidLicense( String license ) {
        
        boolean rvalue = false;
        DESedeEncryptor psec = new DESedeEncryptor();
        
        try {
            
            // test if java.lang.NullPointerException npe will occur
            String key = license.substring( 0, keyLen );
            String enctext = license.substring( keyLen );
            enctext = psec.decryptFromBase64( enctext, key );
            
            // write the License.dat
            File f = new File( file2 );
            
            key = getKey();
            message = key + psec.encryptToBase64( enctext, key );
            OutputStream client = new FileOutputStream( file2 );
            client.write( message.getBytes() );
            client.close();
            
            rvalue = true;
            
        } catch( java.io.IOException ioe ) {
            rvalue = false;
        } catch( java.lang.NullPointerException npe ) {
            rvalue = false;
        }
        
        return rvalue;
    }
    
    public void writeDemoExpiredLicense() {
        
        DESedeEncryptor psec = new DESedeEncryptor();
        String key = getKey();
        message = key + psec.encryptToBase64( expiredMsg, key );
        
        try {
            
            File f = new File( file);
            
            // overwrite
            if( f.exists() ) {
                
                // check if already expired
                f.delete();
                
                OutputStream client = new FileOutputStream( file );
                client.write( message.getBytes() );
                client.close();
            }
            
        } catch( java.io.IOException ioe ) {
            System.out.println( ioe.toString() );
        }
        
    }
    
    
    public String getLicenseMessage() {
        return licenseIDEMessage;
    }
    
    
    public String getKey() {
        DESedeKeyGenerator kgen = new DESedeKeyGenerator();
        return kgen.generate();
    }
    
    public String getKeyFile() {
        return file;
    }
    
    public String getPaidLicenseFile() {
        return file2;
    }
    
    public String getFirstRunDate() {
        return firstRunDate;
    }
    
    public String getTodaysDate() {
        return todaysDate;
    }
    
    public long getDaysInstalled() {
        return nDaysInstalled;
    }
    
    // is demo copy still valid
    public boolean isLicenseValid() {
        
        boolean rvalue = false;
        DataInputStream in = null;
        String s = null;
        int keyLen = 32;
        DESedeEncryptor psec = new DESedeEncryptor();
        
        // -----------------------------
        // check Paid License
        // if License.dat exist
        //    then, check if valid
        // -----------------------------------------------------------
        if( new File( file2 ).exists() ) {
            rvalue = isPaidLicenseValid();
        } else {
            // else, License.dat do not exist
            // then, check Demo License
            try {
                in = new DataInputStream( new BufferedInputStream( new FileInputStream( getKeyFile() ) ) );
                s = in.readLine();
                in.close();
                
                String key = s.substring( 0, keyLen );
                String firstRun = s.substring( keyLen );
                firstRun = psec.decryptFromBase64( firstRun, key );
                
                // if expired return false
                if( firstRun.endsWith( "EXPIRED" ) ) {
                    rvalue = false;
                } else {
                    Date base = new Date( firstRun );
                    Date today = new Date();
                    
                    long nDaysBase = base.getTime()/1000/60/60/24;
                    long nDaysToday = today.getTime()/1000/60/60/24;
                    
                    long diff = nDaysToday - nDaysBase;
                    
                    firstRunDate = base.toString();
                    todaysDate = today.toString();
                    nDaysInstalled = diff;
                    
                    if( diff > 30 ) {
                        rvalue = false;
                    } else if( diff < 0 ) {
                        rvalue = false;
                    } else {
                        rvalue = true;
                    }
                }
                
            } catch( IOException e) {
                rvalue = false;
            } catch( java.lang.IllegalArgumentException iae ) {
                rvalue = false;
            } catch( java.lang.NullPointerException npe ) {
                rvalue = false;
            }
        }
        
        
        return rvalue;
    }
    
    
    
    public boolean isPaidLicenseValid() {
        
        boolean rvalue = false;
        DataInputStream in = null;
        String s = null;
        int keyLen = 32;
        DESedeEncryptor psec = new DESedeEncryptor();
        
        File f = new File( getPaidLicenseFile() );
        if( f.exists() ) {
            
            try {
                in = new DataInputStream( new BufferedInputStream( new FileInputStream( getPaidLicenseFile() ) ) );
                s = in.readLine();
                in.close();
                
                String key = s.substring( 0, keyLen );
                String licenseText = s.substring( keyLen );
                licenseIDEMessage = psec.decryptFromBase64( licenseText, key );
                rvalue = true;                                
            } catch( IOException e) {
                rvalue = false;
            } catch( java.lang.IllegalArgumentException iae ) {
                rvalue = false;
            } catch( java.lang.NullPointerException npe ) {
                rvalue = false;
            }
            
        } else {
            rvalue = false;
            licenseIDEMessage = "License file does not exist.\nPlease re-activate license key.";
        }
        
        return rvalue;
    }
    
}
