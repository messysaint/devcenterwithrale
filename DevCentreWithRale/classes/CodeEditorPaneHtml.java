/*
 * StyledEditor.java
 *
 * Created on September 7, 2003, 9:12 PM
 */

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.JPopupMenu;

import java.awt.Color;
import java.awt.*;              //for layout managers
import java.awt.event.*;        //for action and window events
import java.awt.datatransfer.Clipboard;

import java.util.*;
import java.net.URL;

import java.io.*;
import com.likha.editorKitJava.*;

/**
 *
 * @author  test1
 */
public class CodeEditorPaneHtml extends JEditorPane implements MouseListener, KeyListener, Runnable {
    
    mainForm parent;
    static public int caretX = 0;
    static public int caretY = 0;
    //static public int line = 0;
    //static public int column = 0;
    static public String srcText = "";
    
    Document doc = getDocument();
    
    String typedString = "";
    String sourceCode = "";
    
    private String fileSource = "";
    
    
    
    boolean keyHit = false;
    boolean refresh = false;
    Thread thread;
    
    /** Creates new form CodeEditorPane */
    public CodeEditorPaneHtml(mainForm main) {
        
        super();
        
        addKeyListener( this );
        addMouseListener( this );
        parent = main;
        
        setEditable( true );
        setEnabled( true );
        setBackground(new java.awt.Color(255, 255, 255));
        setSelectionColor(new java.awt.Color(204, 204, 204));
        setFont(new java.awt.Font("Courier New", 0, 12));
        setForeground(java.awt.Color.black);
        
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 3, true));
        
        //
        //Document doc = getDocument();
        //initStylesForTextPane();
        
        thread = new Thread( this );
        thread.start();
        
        doc.putProperty( PlainDocument.tabSizeAttribute, new Integer(4) );
        firePropertyChange( PlainDocument.tabSizeAttribute, 8, 4);
        
        //
        JavaEditorKit kit = new JavaEditorKit();
        
        setEditorKitForContentType("text/java", kit);
        setContentType("text/java");
        setBackground(Color.white);
        setFont(new Font("Courier", 0, 12));
        setEditable(true);
        setEnabled( true );
        setSelectionColor(new java.awt.Color(204, 204, 204));
        setForeground(java.awt.Color.black);
        setMaximumSize( new Dimension( 64000, 64000 ) );
        
        JavaContext styles = kit.getStylePreferences();
        Style s;
        // set style for comments
        s = styles.getStyleForScanValue(Token.COMMENT.getScanValue());
        StyleConstants.setForeground(s, new Color(64,128,128 ) );
        // set style for strings
        s = styles.getStyleForScanValue(Token.STRINGVAL.getScanValue());
        StyleConstants.setForeground(s, new Color(0,128,128 ));
        // greater than
        s = styles.getStyleForScanValue(Token.GT.getScanValue());
        StyleConstants.setForeground(s, new Color(216,48,5 ));
        // less than
        s = styles.getStyleForScanValue(Token.LT.getScanValue());
        StyleConstants.setForeground(s, new Color(216,48,5 ));
        // divide
        s = styles.getStyleForScanValue(Token.DIV.getScanValue());
        StyleConstants.setForeground(s, new Color(216,48,5 ));
          
        
        
        // set style for operators
        Color operator = new Color(216,48,5 );
        for (int code = 0; code <= 58; code++) {
            s = styles.getStyleForScanValue(code);
            if (s != null) {
                StyleConstants.setForeground(s, operator);
            }
        }
        
        
        
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
            } else if( file.startsWith( validBase + "classes" ) ) {
                rvalue = true;
            } else if( file.startsWith( validBase + "src" ) ) {
                rvalue = true;
            } else if( file.startsWith( validBase + "tld" ) ) {
                rvalue = true;
            }
        }
        
        return rvalue;
        
    }
    
    
    
    
    
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
        // right click
        if( e.getButton() == MouseEvent.BUTTON3 ) {
            rightClickPopUp popup = new rightClickPopUp();
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    
    // ------------------------------------------
    
    class rightClickPopUp extends JPopupMenu implements ActionListener, ItemListener {
        
        //popUpMenu refreshColorMenuItem = new popUpMenu("Refresh");
        //popUpMenu reloadMenuItem = new popUpMenu("Reload");
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
            
            //refreshColorMenuItem
            //refreshColorMenuItem.addActionListener(this);
            //refreshColorMenuItem.setEnabled( true );
            //add( refreshColorMenuItem );
            
            // reloadMenuItem
            // reloadMenuItem.addActionListener(this);
            // reloadMenuItem.setEnabled( true );
            // add( reloadMenuItem );
            
            // separator
            JSeparator separator = new javax.swing.JSeparator();
            add( separator );
            
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
            separator = new javax.swing.JSeparator();
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
                archiveSendFile asf = new archiveSendFile( parent, false, file );
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
    
    
    
    // required for non-scrolling test pane
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
    
    public void setSize(Dimension d) {
        if(d.width < getParent().getSize().width) {
            d.width = getParent().getSize().width;
        }
        super.setSize(d);
    }
    
    
    // read data
    public String readSourceFile() {
        
        String rvalue = new String();
        String s = new String();
        DataInputStream in;
        
        try {
            in = new DataInputStream( new BufferedInputStream( new FileInputStream( fileSource ) ) );
            try {
                while( (s = in.readLine())!= null ) {
                    rvalue += s + '\n';
                }
                
                try {
                    in.close();
                } catch( IOException exc ){
                    parent.printMessage( exc.toString());
                }
            } catch( IOException exc ){
                parent.printMessage( exc.toString());
            }
        } catch( IOException exc ) {
            parent.printMessage( exc.toString());
        }
        
        return rvalue.trim();
        
    }
    
    
    public void setFileSource( String src ) {
        fileSource = src;
    }
    
    
    
    
    
    // ----------------------------
    // ----------------------------
    public void keyPressed(KeyEvent e) {
    }
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void keyTyped(KeyEvent e) {
        
        int key = e.getKeyCode();
        
        if( !e.isActionKey() ) {
            
            switch( key ) {
                case KeyEvent.VK_RIGHT:
                    break;
                case KeyEvent.VK_LEFT:
                    break;
                case KeyEvent.VK_UP:
                    break;
                case KeyEvent.VK_DOWN:
                    break;
                case KeyEvent.VK_PAGE_UP:
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    break;
                case KeyEvent.VK_HOME:
                    break;
                case KeyEvent.VK_END:
                    break;
                default:
                    keyHit = true;
                    refresh = false;
            }
            
        }
        
        
    }
    
    //
    public void run() {
        
       
        
    }
    
}

