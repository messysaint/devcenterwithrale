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

//import com.likha.editorKit.*;
/**
 *
 * @author  test1
 */
public class CodeEditorPaneJSP extends JTextPane implements MouseListener, KeyListener, Runnable {
    
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
    
    private Color regularColor = new Color(0,0,0 );
    private Color keywordColor = new Color(4,4,240 );
    private Color stringColor = new Color(0,128,128 );
    private Color commentColor = new Color(64,128,128 );
    private Color characterColor = new Color(0,106,106 );
    private Color symbolColor = new Color(51,102,153 );
    private Color blockColor = new Color(216,48,5 );
    private Color otherColor = new Color(0,0,0 );
    
    // Color action events
    ActionEvent regularColorActionEvent = new ActionEvent( this, 0, "regularColor" );
    ActionListener regularColorAction = new StyledEditorKit.ForegroundAction("regularColor", regularColor);
    
    ActionEvent keywordColorActionEvent = new ActionEvent( this, 0, "keywordColor" );
    ActionListener keywordColorAction = new StyledEditorKit.ForegroundAction("keywordColor", keywordColor);
    
    ActionEvent stringColorActionEvent = new ActionEvent( this, 0, "stringColor" );
    ActionListener stringColorAction = new StyledEditorKit.ForegroundAction("stringColor", stringColor);
    
    ActionEvent commentColorActionEvent = new ActionEvent( this, 0, "commentColor" );
    ActionListener commentColorAction = new StyledEditorKit.ForegroundAction("commentColor", commentColor);
    
    ActionEvent characterColorActionEvent = new ActionEvent( this, 0, "characterColor" );
    ActionListener characterColorAction = new StyledEditorKit.ForegroundAction("characterColor", characterColor);
    
    ActionEvent symbolColorActionEvent = new ActionEvent( this, 0, "symbolColor" );
    ActionListener symbolColorAction = new StyledEditorKit.ForegroundAction("symbolColor", symbolColor);
    
    ActionEvent blockColorActionEvent = new ActionEvent( this, 0, "blockColor" );
    ActionListener blockColorAction = new StyledEditorKit.ForegroundAction("blockColor", blockColor);
    
    ActionEvent otherColorActionEvent = new ActionEvent( this, 0, "otherColor" );
    ActionListener otherColorAction = new StyledEditorKit.ForegroundAction("otherColor", otherColor);
    
    
    boolean keyHit = false;
    boolean refresh = false;
    Thread thread;
    
    /** Creates new form CodeEditorPane */
    public CodeEditorPaneJSP( mainForm main ) {
        
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
        initStylesForTextPane();
        
        thread = new Thread( this );
        thread.start();
        
        doc.putProperty( PlainDocument.tabSizeAttribute, new Integer(4) );                    
        firePropertyChange( PlainDocument.tabSizeAttribute, 8, 4);        
        
        
    }
     
    public void displayColoredCode( int startPos, String[] text, String[] style ) {
        
        try {
            for( int i = 0; i < text.length; i++ ) {
                doc.insertString( doc.getLength(), text[i], getStyle( style[i] ) );
            }
        } catch (BadLocationException ble) {
            parent.printMessage( ble.toString() );
        }
        
    }
    
    
    
    public void clearDocument() {
        try {
            doc.remove( 0, doc.getLength() );
        } catch( javax.swing.text.BadLocationException ble ) {
            parent.printMessage( ble.toString() );
        }
    }
    
    
    
    public void loadDocument( String codeText, int pos ) {
        
        String[] initString = {};
        String[] initStyles = {};
        
        ArrayList initStringToken = new ArrayList();
        ArrayList initStylesToken = new ArrayList();
        
        char[] contents = codeText.toCharArray();
        char next = ' ';
        String token = "";
        String temp = "";
        String openDelim = "";
        
        for( int i = 0 ; i < contents.length ; i++ ) {
            
            next = contents[i];
            token += next;
            
            // check comment line
            if( token.startsWith( "//" ) ) {
                if( next == '\n') {
                    initStringToken.add( token );
                    initStylesToken.add( "comment" );
                    token = "";
                } else {
                    continue;
                }
            }
            
            // check comment line
            if( token.startsWith( "/*" )  ) {
                if( token.endsWith( "*/" ) ) {
                    initStringToken.add( token );
                    initStylesToken.add( "comment" );
                    token = "";
                } else {
                    continue;
                }
            }
            
            
            // check String literals
            if( token.startsWith( "\"" )  ) {
                if( token.length() > 1 && token.endsWith( "\"" ) ) {
                    if( token.endsWith( "\\\"" ) ) {
                        continue;
                    } else {
                        initStringToken.add( token );
                        initStylesToken.add( "string" );
                        token = "";
                    }
                } else {
                    continue;
                }
            }
            
            // check cahracter literals
            if( token.startsWith( "\'" )  ) {
                if( token.length() > 1 && token.endsWith( "\'" ) ) {
                    initStringToken.add( token );
                    initStylesToken.add( "character" );
                    token = "";
                } else {
                    continue;
                }
            }
            
            // if( token is all white spaces && next is not white space )
            // add as normal
            if( ( token.trim().length() == 0 ) && ( (next != ' ') || (next != '\n') || (next != '\t') ) ) {
                initStringToken.add( token );
                initStylesToken.add( "normal" );
                token = "";
                // if( token is not white spaces && next is white space \n \t)
                // add as normal
            } else if( ( token.trim().length() != 0 ) && (next == ' ') || (next == '\n') || (next == '\t') ) {
                
                    initStringToken.add( token );
                    initStylesToken.add( "normal" );
                    token = "";
                
            } else if( ( token.trim().length() != 0 ) && ( isBlock( next ) ) ) {
                temp = token.substring(0, token.length()-1);                                     
                    
                    initStringToken.add( temp );
                    initStylesToken.add( "normal" );
                    token = "";
                    if( next == '(' ) {
                        initStringToken.add( "(" );
                        initStylesToken.add( "block" );
                    } else if( next == '{' ) {
                        initStringToken.add( "{" );
                        initStylesToken.add( "block" );
                    } else if( next == ')' ) {
                        initStringToken.add( ")" );
                        initStylesToken.add( "block" );
                    } else if( next == '}' ) {
                        initStringToken.add( "}" );
                        initStylesToken.add( "block" );
                    } else if( next == ']' ) {
                        initStringToken.add( "]" );
                        initStylesToken.add( "block" );
                    } else if( next == '[' ) {
                        initStringToken.add( "[" );
                        initStylesToken.add( "block" );
                    } else if( next == ',' ) {
                        initStringToken.add( "," );
                        initStylesToken.add( "block" );
                    } else if( next == ';' ) {
                        initStringToken.add( ";" );
                        initStylesToken.add( "block" );
                    } else if( next == ':' ) {
                        initStringToken.add( ":" );
                        initStylesToken.add( "block" );
                    } else if( next == '?' ) {
                        initStringToken.add( "?" );
                        initStylesToken.add( "block" );
                    } else if( next == '!' ) {
                        initStringToken.add( "!" );
                        initStylesToken.add( "block" );
                    } else if( next == '&' ) {
                        initStringToken.add( "&" );
                        initStylesToken.add( "block" );
                    } else if( next == '|' ) {
                        initStringToken.add( "|" );
                        initStylesToken.add( "block" );
                    } else if( next == '+' ) {
                        initStringToken.add( "+" );
                        initStylesToken.add( "block" );
                    } else if( next == '-' ) {
                        initStringToken.add( "-" );
                        initStylesToken.add( "block" );
                    } else if( next == '=' ) {
                        initStringToken.add( "=" );
                        initStylesToken.add( "block" );
                    } else if( next == '*' ) {
                        initStringToken.add( "*" );
                        initStylesToken.add( "block" );
                    } else if( next == '@' ) {
                        initStringToken.add( "@" );
                        initStylesToken.add( "block" );
                    } else if( next == '#' ) {
                        initStringToken.add( "#" );
                        initStylesToken.add( "block" );
                    } else if( next == '$' ) {
                        initStringToken.add( "$" );
                        initStylesToken.add( "block" );
                    } else if( next == '%' ) {
                        initStringToken.add( "%" );
                        initStylesToken.add( "block" );
                    } else if( next == '^' ) {
                        initStringToken.add( "^" );
                        initStylesToken.add( "block" );
                    } else if( next == '<' ) {
                        initStringToken.add( "<" );
                        initStylesToken.add( "block" );
                    } else if( next == '>' ) {
                        initStringToken.add( ">" );
                        initStylesToken.add( "block" );
                    }
                    
                
                
            }
            
            
        } // end for
        
        // check whatever remains in token
        if( token.length() > 0  ) {
            
            if( token.startsWith( "//" ) ) {
                initStringToken.add( token );
                initStylesToken.add( "comment" );
            } else if( token.startsWith( "/*" ) ) {
                initStringToken.add( token );
                initStylesToken.add( "comment" );
            } else if( token.startsWith( "\"" ) ) {
                initStringToken.add( token );
                initStylesToken.add( "string" );
            } else if( token.startsWith( "\'" ) ) {
                initStringToken.add( token );
                initStylesToken.add( "character" );
            } else {
                initStringToken.add( token );
                initStylesToken.add( "normal" );
            }
            
        }
        
        
        Object[] initObjString = initStringToken.toArray();
        Object[] initObjStyles = initStylesToken.toArray();
        
        // int len = initObjStyles.length;
        int len = initObjString.length;
        initString = new String[ len ];
        initStyles = new String[ len ];
        
        for( int i = 0 ; i < len ; i++ ) {
            initString[i] = (String) initObjString[i];
            initStyles[i] = (String) initObjStyles[i];
        }
        
        // display color coded src
        
        try {
            for( int i = 0; i < initString.length; i++ ) {
                doc.insertString( doc.getLength(), initString[i], getStyle( initStyles[i] ) );
            }
        } catch (BadLocationException ble) {
            parent.printMessage( ble.toString() );
        }
        
    }
    
    
    
   
    
    
    // -----------------------
    char[] blockChars = { '(', ')', '{', '}', '[', ']', ',', ';', ':', '?', '!', '&', '|', '+', '=', '-', '*', '@', '#', '$', '%', '^', '<', '>' };
    
    protected boolean isBlock( char c ) {
        
        boolean rvalue = false;
        
        for( int i = 0 ; i < blockChars.length ; i++ ) {
            if( c == blockChars[i] ) {
                rvalue = true;
                break;
            }
            
        }
        
        
        return rvalue;
    }
    
    
    // -----------------------
    protected boolean isXMLBlock( char c ) {
        
        boolean rvalue = false;
        
        char[] blockChars = { '<', '>' };
        
        for( int i = 0 ; i < blockChars.length ; i++ ) {
            if( c == blockChars[i] ) {
                rvalue = true;
                break;
            }
            
        }
        
        
        return rvalue;
    }
    
    // -----------------------
    protected boolean hasComment( String token ) {
        
        boolean rvalue = false;
        
        String[] keywords = { "//", "/*", "*/" };
        
        for( int i = 0 ; i < keywords.length ; i++ ) {
            if( token.indexOf(keywords[i]) != -1 ) {
                rvalue = true;
                break;
            }
            
        }
        
        return rvalue;
    }
    
    // -----------------------
    protected boolean isStartOfStringLiteral( char c ) {
        
        boolean rvalue = false;
        
        char[] blockChars = { '\"', '\'' };
        
        for( int i = 0 ; i < blockChars.length ; i++ ) {
            if( c == blockChars[i] ) {
                rvalue = true;
                break;
            }
            
        }
        
        return rvalue;
    }
    
    //
    protected void initStylesForTextPane() {
        
        Style def = getStyle(StyleContext.DEFAULT_STYLE);
        
        Style regular = addStyle( "regular", def );
        StyleConstants.setFontFamily( def, "Courier" );
        
        Style none = addStyle( "none", regular );
        
        none = addStyle( "keyword", regular );
        //StyleConstants.setItalic( none, false );
        StyleConstants.setForeground( none, keywordColor );
        
        none = addStyle( "string", regular );
        StyleConstants.setForeground( none, stringColor );
        
        none = addStyle( "character", regular );
        StyleConstants.setForeground( none, characterColor );
        
        none = addStyle( "symbol", regular );
        StyleConstants.setForeground( none, symbolColor );
        
        none = addStyle( "block", regular );
        StyleConstants.setForeground( none, blockColor );
        
        none = addStyle( "comment", regular );
        StyleConstants.setForeground( none, commentColor );
        
        none = addStyle( "other", regular );
        StyleConstants.setForeground( none, otherColor );
        
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
    
    
    
    public boolean refreshColor() {
        
        boolean rvalue = false;
        int pos = getCaretPosition();
        int len = doc.getLength();
        String text = "";
        
        try {
            text = doc.getText( 0, len );
            loadDocument( text, 0 );
            doc.remove( 0, len );
            setCaretPosition( pos );
        } catch( javax.swing.text.BadLocationException ble ) {
            parent.printMessage( ble.toString() );
        }
        
        return rvalue;
        
    }
    
    
    public boolean reload( String src ) {
        
        boolean rvalue = false;
        String text = src;
        
        try {
            int len = doc.getLength();
            doc.remove( 0, len );
            loadDocument( text, 0 );
            setCaretPosition( 0 );
        } catch( javax.swing.text.BadLocationException ble ) {
            parent.printMessage( ble.toString() );
        }
        
        parent.printMessage( "Reloaded from " + fileSource);
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
            
            if( actionCmd.equalsIgnoreCase( "refresh" ) ) {
                refreshSyntaxColor();
            } else if( actionCmd.equalsIgnoreCase( "cut" ) ) {
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
    
    
    public void refreshSyntaxColor() {
        
        SwingUtilities.invokeLater(new Runnable() {
            
            public void run() {
                reColor();
            }
            
        });
        
    }
    
    synchronized private void reColor() {
        
        int caretPos = getCaretPosition();
        
        String codeText = "";
        String[] initString = {};
        String[] initStyles = {};
        
        ArrayList initStringToken = new ArrayList();
        ArrayList initStylesToken = new ArrayList();
        
        try {
            codeText = doc.getText( 0, doc.getLength() );
        } catch( javax.swing.text.BadLocationException ble ) {
            parent.printMessage( ble.toString() );
            return;
        }
        
        char[] contents = codeText.toCharArray();
        char next = ' ';
        String token = "";
        String temp = "";
        String openDelim = "";
        
        for( int i = 0 ; i < contents.length ; i++ ) {
            
            next = contents[i];
            token += next;
            
            // check comment line
            if( token.startsWith( "//" ) ) {
                if( next == '\n') {
                    initStringToken.add( token );
                    initStylesToken.add( "comment" );
                    token = "";
                } else {
                    continue;
                }
            }
            
            // check comment line
            if( token.startsWith( "/*" )  ) {
                if( token.endsWith( "*/" ) ) {
                    initStringToken.add( token );
                    initStylesToken.add( "comment" );
                    token = "";
                } else {
                    continue;
                }
            }
            
            
            // check String literals
            if( token.startsWith( "\"" )  ) {
                if( token.length() > 1 && token.endsWith( "\"" ) ) {
                    if( token.endsWith( "\\\"" ) ) {
                        continue;
                    } else {
                        initStringToken.add( token );
                        initStylesToken.add( "string" );
                        token = "";
                    }
                } else {
                    continue;
                }
            }
            
            // check cahracter literals
            if( token.startsWith( "\'" )  ) {
                if( token.length() > 1 && token.endsWith( "\'" ) ) {
                    initStringToken.add( token );
                    initStylesToken.add( "character" );
                    token = "";
                } else {
                    continue;
                }
            }
            
            // if( token is all white spaces && next is not white space )
            // add as normal
            if( ( token.trim().length() == 0 ) && ( (next != ' ') || (next != '\n') || (next != '\t') ) ) {
                initStringToken.add( token );
                initStylesToken.add( "normal" );
                token = "";
                // if( token is not white spaces && next is white space \n \t)
                // add as normal
            } else if( ( token.trim().length() != 0 ) && (next == ' ') || (next == '\n') || (next == '\t') ) {
                
                    initStringToken.add( token );
                    initStylesToken.add( "normal" );
                    token = "";
                
            } else if( ( token.trim().length() != 0 ) && ( isBlock( next ) ) ) {
                temp = token.substring(0, token.length()-1);
                    
                    initStringToken.add( temp );
                    initStylesToken.add( "normal" );
                    token = "";
                    if( next == '(' ) {
                        initStringToken.add( "(" );
                        initStylesToken.add( "block" );
                    } else if( next == '{' ) {
                        initStringToken.add( "{" );
                        initStylesToken.add( "block" );
                    } else if( next == ')' ) {
                        initStringToken.add( ")" );
                        initStylesToken.add( "block" );
                    } else if( next == '}' ) {
                        initStringToken.add( "}" );
                        initStylesToken.add( "block" );
                    } else if( next == ']' ) {
                        initStringToken.add( "]" );
                        initStylesToken.add( "block" );
                    } else if( next == '[' ) {
                        initStringToken.add( "[" );
                        initStylesToken.add( "block" );
                    } else if( next == ',' ) {
                        initStringToken.add( "," );
                        initStylesToken.add( "block" );
                    } else if( next == ';' ) {
                        initStringToken.add( ";" );
                        initStylesToken.add( "block" );
                    } else if( next == ':' ) {
                        initStringToken.add( ":" );
                        initStylesToken.add( "block" );
                    } else if( next == '?' ) {
                        initStringToken.add( "?" );
                        initStylesToken.add( "block" );
                    } else if( next == '!' ) {
                        initStringToken.add( "!" );
                        initStylesToken.add( "block" );
                    } else if( next == '&' ) {
                        initStringToken.add( "&" );
                        initStylesToken.add( "block" );
                    } else if( next == '|' ) {
                        initStringToken.add( "|" );
                        initStylesToken.add( "block" );
                    } else if( next == '+' ) {
                        initStringToken.add( "+" );
                        initStylesToken.add( "block" );
                    } else if( next == '-' ) {
                        initStringToken.add( "-" );
                        initStylesToken.add( "block" );
                    } else if( next == '=' ) {
                        initStringToken.add( "=" );
                        initStylesToken.add( "block" );
                    } else if( next == '*' ) {
                        initStringToken.add( "*" );
                        initStylesToken.add( "block" );
                    } else if( next == '@' ) {
                        initStringToken.add( "@" );
                        initStylesToken.add( "block" );
                    } else if( next == '#' ) {
                        initStringToken.add( "#" );
                        initStylesToken.add( "block" );
                    } else if( next == '$' ) {
                        initStringToken.add( "$" );
                        initStylesToken.add( "block" );
                    } else if( next == '%' ) {
                        initStringToken.add( "%" );
                        initStylesToken.add( "block" );
                    } else if( next == '^' ) {
                        initStringToken.add( "^" );
                        initStylesToken.add( "block" );
                    } else if( next == '<' ) {
                        initStringToken.add( "<" );
                        initStylesToken.add( "block" );
                    } else if( next == '>' ) {
                        initStringToken.add( ">" );
                        initStylesToken.add( "block" );
                    }
                    
                
                
            }
            
            
        } // end for
        
        // check whatever remains in token
        if( token.length() > 0  ) {
            
            if( token.startsWith( "//" ) ) {
                initStringToken.add( token );
                initStylesToken.add( "comment" );
            } else if( token.startsWith( "/*" ) ) {
                initStringToken.add( token );
                initStylesToken.add( "comment" );
            } else if( token.startsWith( "\"" ) ) {
                initStringToken.add( token );
                initStylesToken.add( "string" );
            } else if( token.startsWith( "\'" ) ) {
                initStringToken.add( token );
                initStylesToken.add( "character" );
            } else {
                initStringToken.add( token );
                initStylesToken.add( "normal" );
            }
            
        }
        
        
        Object[] initObjString = initStringToken.toArray();
        Object[] initObjStyles = initStylesToken.toArray();
        
        // int len = initObjStyles.length;
        int len = initObjString.length;
        initString = new String[ len ];
        initStyles = new String[ len ];
        
        for( int i = 0 ; i < len ; i++ ) {
            initString[i] = (String) initObjString[i];
            initStyles[i] = (String) initObjStyles[i];
        }
        
        // display color coded src
        int beginPos = 0;
        int endPos = 0;
        int tokenLength = 0;
        for( int i = 0; i < initString.length; i++ ) {
            tokenLength = initString[i].length();
            endPos = beginPos + tokenLength;
            select( beginPos, endPos );
            beginPos += tokenLength;
            
            if( tokenLength != 0 ) {
                if( getSelectedText().trim().length() != 0 ) {
                    //System.out.println( tokenLength + " >>> " + getSelectedText() );
                    
                    if( initStyles[i].equalsIgnoreCase( "comment" ) ) {
                        commentColorAction.actionPerformed( commentColorActionEvent );
                    } else if( initStyles[i].equalsIgnoreCase( "string" ) ) {
                        stringColorAction.actionPerformed( stringColorActionEvent );
                    } else if( initStyles[i].equalsIgnoreCase( "character" ) ) {
                        characterColorAction.actionPerformed( characterColorActionEvent );
                    } else if( initStyles[i].equalsIgnoreCase( "normal" ) ) {
                        regularColorAction.actionPerformed( regularColorActionEvent );
                    } else if( initStyles[i].equalsIgnoreCase( "keyword" ) ) {
                        keywordColorAction.actionPerformed( keywordColorActionEvent );
                    } else if( initStyles[i].equalsIgnoreCase( "block" ) ) {
                        blockColorAction.actionPerformed( blockColorActionEvent );
                    }
                    
                }
            }
            
        }
        
        
        setCaretPosition( caretPos ); // restore caret position
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
        
        while( true ) {            
            refresh = true;
            try {
                thread.sleep( 1000 );            
                if( keyHit && refresh ) {
                    if( !hasSelectedText() ) {
                        refreshSyntaxColor();                    
                        keyHit = false;
                    }                    
                }
            } catch( java.lang.InterruptedException ie ) {
            }
            
        }
        
    }
    
}

