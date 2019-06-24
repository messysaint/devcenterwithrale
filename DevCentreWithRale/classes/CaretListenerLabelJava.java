/*
 * caretListenerLabel.java
 *
 * Created on March 17, 2003, 8:59 PM
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;


/**
 *
 * @author  test1
 */
//This listens for and reports caret movements.
//This listens for and reports caret movements.
public class CaretListenerLabelJava extends JLabel implements CaretListener {
    
    // caret text positions
    private int textPosBegin = 0;
    private int textPosEnd = 0;
    private int caretX = 0;
    private int caretY = 0;
    private int linePos = 0; // the positin of the last line feed character
    
     // to activate color code refresh  
    int prevLength = 0;
    int currLength = 0;
    
    String newline = "\n";
    int dot = 0;
    int mark = 0;
    String text = "";
    int column = 0;
    int lnum = 0;
    int textPos = 0;
    CodeEditorPaneJava codeTextArea;
    
    public CaretListenerLabelJava(String label, CodeEditorPaneJava editPane) {        
        super(label);
        codeTextArea = editPane;
    }
    
    public void caretUpdate(CaretEvent e) {
        //Get the location in the text
        dot = e.getDot();
        mark = e.getMark();
        
        
        try {
            Rectangle caretCoords = codeTextArea.modelToView(dot);
            
            
            caretX = caretCoords.x;
            caretY = caretCoords.y;
            
            //Convert it to view coordinates.
            text = codeTextArea.getText( 0, codeTextArea.getCaretPosition() );
            
            // 11/14/03
            // get current length after caret update is triggered
            //currLength = codeTextArea.getText().length();
            
            // do the following on the gui thread
            SwingUtilities.invokeLater(new Runnable() {
                
                public void run() {
                    
                    textPosBegin = dot;
                    textPosEnd = mark;
                    
                    column = countColumn( text );
                    linePos = lnum = countLine( text );
                    setText( "Line: " + countLine( text ) + " Column: " + column );
                                        
                    // add code to refresh code color
                    /*
                    if( currLength != prevLength ) {
                        
                        codeTextArea.refreshSyntaxColor();
                        
                        prevLength = currLength;                        
                    }
                     */
                    
                }
                
            });
            
            
            codeTextArea.caretX = caretX;
            codeTextArea.caretY = caretY;
            
        } catch (BadLocationException ble) {
            setText("caret: text position: " + dot + newline);
        }
        
        
    }
    
    
    private int countLine( String txt ) {
        
        int rvalue = 1;
        int len = txt.length();
        
        for( int i = 0 ; i < len ; i++ ) {
            if( txt.charAt(i) == '\n' ) {
                //linePos = ( i > 0 ) ? i-1 : i; // 
                rvalue++;
            }
        }
        
        return rvalue;
    }
    
    private int countColumn( String txt ) {
        
        int rvalue = 0;
        
        if( txt.indexOf( '\n' ) == -1 ) {
            rvalue = txt.length()+1;
        } else {
            rvalue = txt.substring( txt.lastIndexOf( '\n' ) ).length();
        }
        return rvalue;
    }
    
    public int[] getCaretXY() {
        int[] rvalue = { caretX, caretY };
        return rvalue;
    }
    
    
    public int[] getTextPosition() {
        int[] rvalue = { textPosBegin, textPosEnd };
        return rvalue;
    }
    
    public int getCaretTextPosition() {
        return textPosBegin;
    }
    
    public void setCaretTextPosition( int pos ) {
        String txt = codeTextArea.getText();
        if( pos > txt.length() ) {
            codeTextArea.setCaretPosition( txt.length() );
        } else if( pos >= 0 ) {
            codeTextArea.setCaretPosition( pos );
        }
        codeTextArea.requestFocus();
    }
    
  
    
    public void setSelection( int start, int end ) {
        codeTextArea.setSelectionStart( start );
        codeTextArea.setSelectionEnd( end );
        codeTextArea.requestFocus();
    }

    public void setText( String code, int start, int end ) {
        
        // editor pane
        codeTextArea.setSelectionStart( start );
        codeTextArea.setSelectionEnd( end );        
        codeTextArea.replaceSelection( code );
        
        // JTextArea
        //codeTextArea.replaceRange( code, start, end );
        codeTextArea.requestFocus();                       
    }
    
    public void replaceSelection( String replacement ) {
        codeTextArea.replaceSelection( replacement );
    }
    
}