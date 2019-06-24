/*
 * BuildMessage.java
 *
 * Created on June 21, 2003, 7:20 AM
 */
import java.awt.datatransfer.Clipboard;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPopupMenu;

import java.io.*;


/**
 *
 * @author  test1
 */
public class CodeTextArea extends JTextArea implements KeyListener, MouseListener {
    
    mainForm parent;
    static public int caretX = 0;
    static public int caretY = 0;
    static public int column = 0;
    
    /** Creates a new instance of BuildMessage */
    public CodeTextArea( mainForm main ) {
        
        addMouseListener( this );
        parent = main;
        
        setEditable( true );
        setEnabled( true );
        setBackground(new java.awt.Color(255, 255, 255));
        setSelectionColor(new java.awt.Color(204, 204, 204));
        setFont(new java.awt.Font("Courier New", 0, 12));
        setForeground(java.awt.Color.black);
        setTabSize(4);
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 3, true));
        
        //setMargin( new Insets( 10, 10, 10, 10 ) );
    }
    
    /** Creates a new instance of BuildMessage */
    public CodeTextArea( mainForm main, boolean javaOption ) {
        addMouseListener( this );
        if( javaOption ) {
            addKeyListener( this );
        }
        
        parent = main;
        
        setEditable( true );
        setEnabled( true );
        setBackground(new java.awt.Color(255, 255, 255));
        setSelectionColor(new java.awt.Color(204, 204, 204));
        setFont(new java.awt.Font("Courier New", 0, 12));
        setForeground(java.awt.Color.black);
        setTabSize(4);
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 3, true));
        
        //setMargin( new Insets( 10, 10, 10, 10 ) );
    }
    
    
    public void cutText() {
        cut();
    }
    
    public void copyText() {
        copy();
    }
    
    public void pasteText() {
        paste();
    }
    
    public void selectAll() {
        setSelectionStart(0);
        setSelectionEnd( getText().length() );
    }
    
    public void save() {
        saveFile sf = new saveFile( parent, false );
        sf.show();
    }
    
    public void undo() {
        parent.undoEdit();
    }
    
    public void redo() {
        parent.redoEdit();
    }
    
    public void search() {
        if( parent.hasOpenIF() ) {
            searchText st = new searchText( parent, false);
            st.show();
        }
    }
    
    public void compile() {
        if( parent.isOpenCompilable() ) {
            saveFile sf = new saveFile( parent, false );
            sf.show();
            compileFile cf = new compileFile( parent, false );
            cf.show();
        }
    }
    
    public boolean hasSelectedText() {
        return getSelectedText() != null ? true : false;
    }
    
    
    public boolean isBrowserViewable() {
        return parent.isBrowserViewable();
    }
    
    public void viewInBrowser() {
        //parent.printMessage( parent.getSelectedFileFromIF() );
        String file = parent.getSelectedFileFromIF();
        parent.viewInBrowser( file );
    }
    
    
    public boolean isSendable() {
        
        boolean rvalue = false;
        String file = parent.getSelectedFileFromIF();
        String validBase = parent.getBuildDirectory() + "\\";
        
        if( new File( file ).isFile() ) {
            if( file.startsWith( validBase + "metadata" ) ) {
                rvalue = true;
            } else if( file.startsWith( validBase + "lib" ) ) {
                rvalue = true;
            } if( file.startsWith( validBase + "classes" ) ) {
                rvalue = true;
            } if( file.startsWith( validBase + "src" ) ) {
                rvalue = true;
            }
        }
        
        return rvalue;
        
    }
    
    // needed by KeyListener ----------------------------------
    public void keyPressed(java.awt.event.KeyEvent e) {
        if( e.getKeyChar() == '.' ) {
            String someClass = getReferredClass();
            codeCompletionOptions popup = new codeCompletionOptions( parent, someClass );
            popup.show( e.getComponent(), caretX, caretY + 15 );
        }
    }
    
    public void keyReleased(java.awt.event.KeyEvent e) {
    }
    
    public void keyTyped(java.awt.event.KeyEvent e) {
    }
    
    public void mouseClicked(java.awt.event.MouseEvent e) {
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
        // right click
        if( e.getButton() == MouseEvent.BUTTON3 ) {
            rightClickPopUp popup = new rightClickPopUp();
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }
    
    // ------------------------------------------
    
    class rightClickPopUp extends JPopupMenu implements ActionListener, ItemListener {
        
        popUpMenu cutMenuItem = new popUpMenu("Cut");
        popUpMenu copyMenuItem = new popUpMenu("Copy");
        popUpMenu pasteMenuItem = new popUpMenu("Paste");
        // ----------------------------------------------
        popUpMenu undoMenuItem = new popUpMenu("Undo");
        popUpMenu redoMenuItem = new popUpMenu("Redo");
        // ----------------------------------------------
        popUpMenu selectAllMenuItem = new popUpMenu("Select All");
        // ----------------------------------------------
        popUpMenu saveMenuItem = new popUpMenu("Save");
        popUpMenu compileMenuItem = new popUpMenu("Compile");
        // ----------------------------------------------
        popUpMenu browserViewMenuItem = new popUpMenu("Browser view");
        popUpMenu searchMenuItem = new popUpMenu("Search");
        popUpMenu sendMenuItem = new popUpMenu("Send file to server");
        
        /** Creates a new instance of treePopUp */
        public rightClickPopUp() {
            
            setBorder( new javax.swing.border.LineBorder( new java.awt.Color( 147,139,125 ) ) );
            
            // copy
            copyMenuItem.addActionListener(this);
            copyMenuItem.setEnabled( hasSelectedText() );
            add( copyMenuItem );
            
            // paste
            pasteMenuItem.addActionListener(this);
            pasteMenuItem.setEnabled( true );
            add( pasteMenuItem );
            
            // cut
            cutMenuItem.addActionListener(this);
            cutMenuItem.setEnabled( hasSelectedText() );
            add( cutMenuItem );
            
            // separator
            JSeparator separator = new javax.swing.JSeparator();
            add( separator );
            
            // paste
            undoMenuItem.addActionListener(this);
            undoMenuItem.setEnabled( true );
            add( undoMenuItem );
            
            // cut
            redoMenuItem.addActionListener(this);
            redoMenuItem.setEnabled( true );
            add( redoMenuItem );
            
            // separator
            separator = new javax.swing.JSeparator();
            add( separator );
            
            // select all
            selectAllMenuItem.addActionListener(this);
            selectAllMenuItem.setEnabled( true );
            add( selectAllMenuItem );
            
            // separator
            separator = new javax.swing.JSeparator();
            add( separator );
            
            // save
            saveMenuItem.addActionListener(this);
            saveMenuItem.setEnabled( true );
            add( saveMenuItem );
            
            // compile
            compileMenuItem.addActionListener(this);
            compileMenuItem.setEnabled( parent.isOpenCompilable() );
            add( compileMenuItem );
            
            // separator
            separator = new javax.swing.JSeparator();
            add( separator );
            
            // browser View
            browserViewMenuItem.addActionListener(this);
            browserViewMenuItem.setEnabled( isBrowserViewable() );
            add( browserViewMenuItem );
            
            // search
            searchMenuItem.addActionListener(this);
            searchMenuItem.setEnabled( true );
            add( searchMenuItem );
            
            // separator
            separator = new javax.swing.JSeparator();
            add( separator );
            
            // send
            sendMenuItem.addActionListener(this);
            sendMenuItem.setEnabled( isSendable() );
            add( sendMenuItem );
            
            
        }
        
        
        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            
            String actionCmd = actionEvent.getActionCommand();
            
            if( actionCmd.equalsIgnoreCase( "cut" ) ) {
                cutText();
            } else if( actionCmd.equalsIgnoreCase( "copy" ) ) {
                copyText();
            } else if( actionCmd.equalsIgnoreCase( "paste" ) ) {
                pasteText();
            } else if( actionCmd.equalsIgnoreCase( "select all" ) ) {
                selectAll();
            } else if( actionCmd.equalsIgnoreCase( "save" ) ) {
                save();
            } else if( actionCmd.equalsIgnoreCase( "undo" ) ) {
                undo();
            } else if( actionCmd.equalsIgnoreCase( "redo" ) ) {
                redo();
            } else if( actionCmd.equalsIgnoreCase( "compile" ) ) {
                compile();
            } else if( actionCmd.equalsIgnoreCase( "search" ) ) {
                search();
            } else if( actionCmd.equalsIgnoreCase( "browser view" ) ) {
                viewInBrowser();
            } else if( actionCmd.equalsIgnoreCase( "send file to server" )  ) {   
                String file = parent.getSelectedFileFromIF();
                archiveSendFile asf = new archiveSendFile( parent, true, file );
                asf.show();
            } else {
                parent.printMessage( "Error: Action not defined: " + actionEvent.toString() );
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
    
    
    
    private String getReferredClass() {
        
        String rvalue = "";
        int caretPos = this.getCaretPosition();
        int beginPos = 0;
        
        try {
            rvalue = getText( beginPos, caretPos );
            // enhance this code to capture exact classname
            if( rvalue.indexOf( '\n' ) != -1) {
                rvalue = rvalue.substring( rvalue.lastIndexOf( '\n' ) );
            }
            parent.printMessage( rvalue );
        } catch( javax.swing.text.BadLocationException ble ) {
            rvalue = "";
        }
        
        return rvalue;
    }
    
}


