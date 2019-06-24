/*
 * javaIdentifierParser.java
 *
 * Created on June 30, 2003, 9:55 PM
 */

package com.likha.codeParser;

/**
 *
 * @author  test1
 */
public class identifierParser {
    
    private String sourceCode = "";
    final String validCharAsString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$";
    final char[] validChars = validCharAsString.toCharArray();
    
    /** Creates a new instance of javaIdentifierParser */
    public identifierParser() {
    }
    
    public String getIdentifierName( String codeLine ) {
        
        String reverse = reverse( codeLine );     
        int len = reverse.length();  
        int index = 0;
        boolean completed = true;
        for( int i = 0 ; i < len ; i++ ) {
            if( !isValidChar( reverse.charAt( i ) ) ) {
                index = i;
                completed = false;
                break;
            }
        }
        index = completed ? len : index;
        reverse = reverse.substring( 0, index );
        reverse = reverse( reverse );
                
        return reverse;
    }
    
    public String reverse( String orig ) {
        
        String rvalue = "";
        char[] origAsChar = orig.toCharArray();
        char[] reverse = new char[ orig.length() ];
        
        int end = orig.length()-1;        
        int start = 0;
        for( int i = end ; i > -1 ; i-- ) {
            reverse[start++] = origAsChar[i];
        }
        
        return new String( reverse );
    }
    
    public boolean isValidChar( char c ) {
        
        boolean rvalue = false;
        int len = validChars.length;
        for( int i = 0 ; i < len ; i++ ) {
            if( c == validChars[i] ) {
                rvalue = true;
                break;
            }
        }
        return rvalue;
    }
    
}
