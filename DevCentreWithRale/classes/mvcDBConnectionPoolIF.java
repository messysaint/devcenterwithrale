/*
 * mvcController.java
 *
 * Created on December 24, 2002, 3:47 PM
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 *
 * @author  test1
 */
public class mvcDBConnectionPoolIF extends javax.swing.JInternalFrame {
    
    
    /** Creates new form mvcController */
    public mvcDBConnectionPoolIF(mainForm mainSource) {
        
        initComponents();
        
        try {
            UIManager.setLookAndFeel( LnF );
            SwingUtilities.updateComponentTreeUI( this );
            
        } catch (UnsupportedLookAndFeelException exc) {
            main.printMessage( exc.toString() );
        } catch (IllegalAccessException exc) {
            main.printMessage( exc.toString() );
        } catch (ClassNotFoundException exc) {
            main.printMessage( exc.toString() );
        } catch (InstantiationException exc) {
            main.printMessage( exc.toString() );
        }
        
        main = mainSource;
        fileSource = main.getBuildDirectory() + "\\classes\\dbConnectionPool.properties";
        setTitle( getTitle() + " [ " + fileSource.substring( main.getBuildDirectory().length() ) + " ]");
        
        initCodeTextArea();
        codeTextArea.setText( readFile().trim() );
        //codeTextArea.refreshColor();
        //codeTextArea.loadDocument( readFile().trim(), 0 );    // color coded
        codeTextArea.setCaretPosition( 0 );
        cll = new CaretListenerLabelOther( "Ready", codeTextArea );
        codeTextArea.addCaretListener( cll );
        
        linePosPanel.add( cll );
        
        setUndoRedo();
        
        codeTextArea.setFileSource( fileSource ); // set source file
    }
    
    
    private void initCodeTextArea() {
        codeTextArea = new CodeEditorPaneOther( main ); // color coded
        JScrollPane scroller = new JScrollPane();
	JViewport vp = scroller.getViewport();
	vp.add(codeTextArea);        
        scroller.getViewport().setScrollMode( JViewport.BACKINGSTORE_SCROLL_MODE );        
        CodePanel.add( scroller, BorderLayout.CENTER );
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        CodePanel = new javax.swing.JPanel();
        linePosPanel = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Database Connection Pool Setting");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/favorites.gif")));
        setVisible(true);
        CodePanel.setLayout(new java.awt.GridLayout(1, 0));

        getContentPane().add(CodePanel, java.awt.BorderLayout.CENTER);

        linePosPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        linePosPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        getContentPane().add(linePosPanel, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-700)/2, (screenSize.height-500)/2, 700, 500);
    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CodePanel;
    private javax.swing.JPanel linePosPanel;
    // End of variables declaration//GEN-END:variables
    private String LnF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    private mainForm main;
    private String fileSource = new String();
    private DataInputStream in;
    
    protected UndoManager undoManager = new UndoManager();
    private boolean canUndo = false;
    private boolean canRedo = false;
    
    private CodeEditorPaneOther codeTextArea;  // color coded
    
    private int textPosBegin = 0;
    private int textPosEnd = 0;
    
    public CaretListenerLabelOther cll;
    
    // read data
    public String readFile() {
        
        String rvalue = new String();
        String s = new String();
        
        try {
            in = new DataInputStream( new BufferedInputStream( new FileInputStream( fileSource ) ) );
            try {
                while( (s = in.readLine())!= null ) {
                    rvalue += s + '\n';
                }
                
                try {
                    in.close();
                } catch( IOException exc ){
                    main.printMessage( exc.toString());
                }
            } catch( IOException exc ){
                main.printMessage( exc.toString());
            }
        } catch( IOException exc ) {
            main.printMessage( exc.toString());
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
    
    public String getText() {
        return codeTextArea.getText();
    }
    
    public String getFileSource() {
        return fileSource;
    }
    
    
    // undo/redo
    private void updateUndoRedoFlags() {
        canUndo = undoManager.canUndo();
        canRedo = undoManager.canRedo();
    }
    
    public void setUndoRedo() {
        // test undo/redo
        codeTextArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                UndoableEdit ue = e.getEdit();
                if( !ue.getPresentationName().equalsIgnoreCase( "style change" ) ) {
                    undoManager.addEdit( ue );
                }                
                updateUndoRedoFlags();
            }
        });
        
    }
    
    
    public void undoEdit() {
        
        if( canUndo ) {
            try {
                undoManager.undo();
            } catch (CannotRedoException cre) {
            }
            updateUndoRedoFlags();
        }
    }
    
    
    public void redoEdit() {
        
        if( canRedo ) {
            try {
                undoManager.redo();
            } catch (CannotRedoException cre) {
            }
            updateUndoRedoFlags();
        }
    }
    
    
    
}