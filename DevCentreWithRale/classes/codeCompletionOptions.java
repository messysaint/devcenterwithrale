/*
 * treePopUp.java
 *
 * Created on March 29, 2003, 2:11 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.tree.*;
import javax.swing.event.*;
import java.io.*;

/**
 *
 * @author  test1
 */
public class codeCompletionOptions extends JPopupMenu implements ActionListener, ItemListener {
           
    private String filePath;
    popUpMenu editMenuItem = new popUpMenu("Edit");
    popUpMenu anotherMenuItem = new popUpMenu("Other");
    
    JSeparator separator;
    mainForm main;
    
    /** Creates a new instance of treePopUp */
    public codeCompletionOptions(mainForm parentFrame, String someClass ) {
                
        main = parentFrame;                
        
        setBorder( new javax.swing.border.LineBorder( new java.awt.Color( 147,139,125 ) ) );
        
        editMenuItem.addActionListener(this);
        editMenuItem.setEnabled( true );
        add( editMenuItem );
        
        separator = new javax.swing.JSeparator();
        add( separator );
        
        anotherMenuItem.addActionListener(this);
        anotherMenuItem.setEnabled( true );
        add( anotherMenuItem );                
                
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        String actionCmd = actionEvent.getActionCommand();
        
        if( actionCmd.equalsIgnoreCase( "edit" ) ) {
            // do something here
        } 
                
    }
    
    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
    }
    
    
    // popUpMenu
    private class popUpMenu extends JMenuItem  {
        
        public popUpMenu( String label) {
            super( label );
            setBackground( new java.awt.Color( 235,234,231 ) );
        }
        
        
    }
    
    
    
    
    
}
