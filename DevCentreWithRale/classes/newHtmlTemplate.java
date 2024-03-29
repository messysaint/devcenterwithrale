/*
 * projectOpen.java
 *
 * Created on December 16, 2002, 7:05 AM
 */
import org.apache.tools.ant.DirectoryScanner;

import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;

import java.io.*;
import java.awt.Point;
import java.net.*;

//import HTTPClient.*;
import java.io.*;
import encryptor.*;
/**
 *
 * @author  test1
 */
public class newHtmlTemplate extends javax.swing.JDialog {
    
    //static ResourceBundle config;
    
    /** Creates new form projectOpen */
    public newHtmlTemplate(mainForm parent, boolean modal) {
        super(parent, modal);
        initComponents();
        myParent = parent;
        
        previewer = new FilePreviewer( chooser );
        
        
        
        try {
            UIManager.setLookAndFeel( LnF );
            SwingUtilities.updateComponentTreeUI( this );
            //if(chooser != null) {
            SwingUtilities.updateComponentTreeUI( chooser );
            //}
        } catch (UnsupportedLookAndFeelException exc) {
        } catch (IllegalAccessException exc) {
            myParent.printMessage("IllegalAccessException Error:" + exc);
        } catch (ClassNotFoundException exc) {
            myParent.printMessage("ClassNotFoundException Error:" + exc);
        } catch (InstantiationException exc) {
            myParent.printMessage("InstantiateException Error:" + exc);
        }
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        chooser = new javax.swing.JFileChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        browseTemplateArchive = new javax.swing.JButton();
        dwtNameTextField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        chooser.setDialogTitle("Select Web Archive");
        chooser.setFont(new java.awt.Font("Default", 0, 11));

        setTitle("HTML Template");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jPanel6.setPreferredSize(new java.awt.Dimension(400, 125));
        browseTemplateArchive.setFont(new java.awt.Font("Default", 0, 11));
        browseTemplateArchive.setText("Browse DreamWeaver  template ...");
        browseTemplateArchive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseTemplateArchiveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(browseTemplateArchive, gridBagConstraints);

        dwtNameTextField.setBackground(new java.awt.Color(255, 255, 255));
        dwtNameTextField.setEditable(false);
        dwtNameTextField.setFont(new java.awt.Font("Default", 0, 11));
        dwtNameTextField.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        dwtNameTextField.setMinimumSize(new java.awt.Dimension(350, 19));
        dwtNameTextField.setPreferredSize(new java.awt.Dimension(350, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanel6.add(dwtNameTextField, gridBagConstraints);

        getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel4, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel5, java.awt.BorderLayout.WEST);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        okButton.setFont(new java.awt.Font("Default", 0, 11));
        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jPanel3.add(okButton);

        cancelButton.setFont(new java.awt.Font("Default", 0, 11));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel3.add(cancelButton);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-432)/2, (screenSize.height-228)/2, 432, 228);
    }//GEN-END:initComponents
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // Add your handling code here:
        createTemplatesDirectory();
        
        setVisible(false);
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void browseTemplateArchiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseTemplateArchiveActionPerformed
        // Add your handling code here:
        if (chooser.isMultiSelectionEnabled()) {
            chooser.setSelectedFiles(null);
        } else {
            chooser.setSelectedFile(null);
        }
        
        // clear the preview from the previous display of the chooser
        JComponent accessory = chooser.getAccessory();
        if (accessory != null) {
            ((FilePreviewer)accessory).loadImage(null);
        }
        int retval = chooser.showDialog(this, null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            
            File theFile = chooser.getSelectedFile();
            if( theFile != null ) {
                if( !theFile.isDirectory() ) {
                    myParent.printMessage( theFile.getAbsolutePath() );
                    dwtNameTextField.setText( theFile.getAbsolutePath() );
                }
            }
            
        } else if (retval == JFileChooser.CANCEL_OPTION) {
            myParent.printMessage( "Canceled");
        } else if (retval == JFileChooser.ERROR_OPTION) {
            myParent.printMessage( "Error");
        } else {
            
        }
    }//GEN-LAST:event_browseTemplateArchiveActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // Add your handling code here:
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // new projectOpen( new mainForm(), true ).show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseTemplateArchive;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JFileChooser chooser;
    private javax.swing.JTextField dwtNameTextField;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    
    private boolean valid = false;
    private FilePreviewer previewer;
    private String LnF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    
    mainForm myParent; // mainForm parent
    
    
    String[] templateList = new String[1];
    String[] templateArchive = new String[ templateList.length ];
    DataInputStream in;
    
    HashMap map = new HashMap();
    
    class FilePreviewer extends JComponent implements PropertyChangeListener {
        ImageIcon thumbnail = null;
        
        public FilePreviewer(JFileChooser fc) {
            setPreferredSize(new Dimension(100, 50));
            fc.addPropertyChangeListener(this);
        }
        
        public void loadImage(File f) {
            if (f == null) {
                thumbnail = null;
            } else {
                ImageIcon tmpIcon = new ImageIcon(f.getPath());
                if(tmpIcon.getIconWidth() > 90) {
                    thumbnail = new ImageIcon(
                    tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
                } else {
                    thumbnail = tmpIcon;
                }
            }
        }
        
        public void propertyChange(PropertyChangeEvent e) {
            String prop = e.getPropertyName();
            if(prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
                if(isShowing()) {
                    loadImage((File) e.getNewValue());
                    repaint();
                }
            }
        }
        
    }
    
    
    // read data
    public String[] getTemplates( String propertiesFile ) {
        
        String[] rvalue = null;
        String s = new String();
        String temp = null;
        String label = null;
        String file = null;
        
        try {
            in = new DataInputStream( new BufferedInputStream( new FileInputStream( propertiesFile ) ) );
            try {
                while( (s = in.readLine())!= null ) {
                    
                    if( !s.trim().startsWith( "#" ) ) {
                        if( s.length() > 0 && s.indexOf( '=' ) != -1 ) {
                            file = s.substring( 0, s.lastIndexOf( '=' ) ).trim();
                            label = s.substring( s.lastIndexOf( '=' )+1 ).trim();
                            map.put( label, file );
                        }
                    }
                }
                
                
                //rvalue = new String[] { "label1", "label2" };
                
                // keys
                Collection col = map.values();
                //col.toArray();
                
                // labels
                Object[] obj = map.keySet().toArray();
                rvalue = new String[ map.size() ];
                
                for( int i = 0 ; i < map.size() ; i++ ) {
                    rvalue[i] = (String) obj[i];
                }
                
                try {
                    in.close();
                } catch( IOException e ) {}
            } catch( IOException e){}
        } catch( IOException e) {}
        
        return rvalue;
        
    }
    
    
    public void createHTMLTemplate( String archive ) {
        
        String jarFile = archive.substring( archive.lastIndexOf( '\\' )+1 );
        String backupDir = myParent.getBuildDirectory() + "\\workarea\\";
        String workingDir = myParent.getBuildDirectory() + "\\workarea\\" + jarFile;
        String projectDir = myParent.getBuildDirectory();
        String timestamp = timestamp = new Timestamp( Calendar.getInstance().getTimeInMillis() ).toString() ;
        timestamp = '.' + timestamp.replace( ' ', '_').replace( ':', '-' );
        
        if( new File( archive ).exists() ) {
            
            // create working dir
            File wdir = new File( workingDir );
            if( wdir.exists() ) {
                File backup = new File( workingDir + timestamp );
                wdir.renameTo( backup );
            }
            
            if( wdir.mkdir() ) {
                
                //copy war file to working dir
                myParent.copyFile( '\"' +  archive + '\"', '\"' + workingDir + '\"' );
                // unjar
                if( myParent.unjar( workingDir, jarFile ) ) {
                    
                    // delete jar file
                    File jarFileToDelete = new File( workingDir + '\\' + jarFile );
                    if( jarFileToDelete.exists() ) {
                        jarFileToDelete.delete();
                    }
                                                            
                    //create additional subdirectories
                    // create dir structure from new web app
                    String[] dirs = listDirs( workingDir, new String[] { "META-INF" } );
                    for( int i = 1 ; i < dirs.length ; i++ ) {
                        //myParent.printMessage( "template dir: " + dirs[i] );                        
                        myParent.osMakeDir( '\"' + projectDir + "\\src\\" + dirs[i] +'\"' );
                    }
                    
                    //copy all templates to \Templates
                    String[] files = listFiles( workingDir, new String[] { "META-INF\\**" }, new String[] { "**\\*.dwt" } );
                    for( int i = 0 ; i < files.length ; i++ ) {
                        //myParent.printMessage( "DreamWeaver template file: " + files[i] );
                        myParent.copyFile( '\"' + workingDir + '\\' + files[i] + '\"', '\"' + projectDir + "\\Templates\\" + files[i] + '\"' );
                        myParent.osDeleteFile( '\"' + workingDir + '\\' + files[i] + '\"' );
                    }
                    
                    // copy all src files except templates to \src                    
                    //warFile = workingDir.substring( workingDir.lastIndexOf( '\\' )+1 );
                    files = listFiles( workingDir, new String[] { "META-INF\\**" } );
                    for( int i = 0 ; i < files.length ; i++ ) {
                        //myParent.printMessage( "template file: " + files[i] );
                        myParent.copyFile( '\"' + workingDir + '\\' + files[i] + '\"', '\"' + projectDir + "\\src\\" + files[i] + '\"' );
                    }                                        
                    
                } else {
                    myParent.printMessage( "Error unwarring the template " + jarFile );
                }
                
                
            } else {
                myParent.printMessage( "Error: Failed to create backup directory "  + wdir.toString() );
            }
            
        } else {
            myParent.printMessage( "Error: " + archive );
        }
        
    }
    
    
    
    public String[] listFiles( String baseDir ) {
        
        DirectoryScanner ds = new DirectoryScanner();
        String[] includes = { "**" };
        
        ds.setBasedir( baseDir );
        ds.setCaseSensitive( true );
        ds.setIncludes( includes );
        
        ds.scan();
        
        String[] files = ds.getIncludedFiles();
        
        return files;
    }
    
    
    public String[] listFiles( String baseDir, String[] excludes ) {
        
        DirectoryScanner ds = new DirectoryScanner();
        String[] includes = { "**" };
        
        ds.setBasedir( baseDir );
        ds.setCaseSensitive( true );
        ds.setIncludes( includes );
        ds.setExcludes( excludes );
        
        ds.scan();
        
        String[] files = ds.getIncludedFiles();
        
        return files;
    }
    
    
    public String[] listFiles( String baseDir, String[] excludes, String[] includes ) {
        
        DirectoryScanner ds = new DirectoryScanner();        
        
        ds.setBasedir( baseDir );
        ds.setCaseSensitive( true );
        ds.setIncludes( includes );
        ds.setExcludes( excludes );
        
        ds.scan();
        
        String[] files = ds.getIncludedFiles();
        
        return files;
    }
    
    public String[] listDirs( String baseDir ) {
        
        DirectoryScanner ds = new DirectoryScanner();
        String[] includes = { "**" };
        
        ds.setBasedir( baseDir );
        ds.setCaseSensitive( true );
        ds.setIncludes( includes );
        
        ds.scan();
        
        String[] dirs = ds.getIncludedDirectories();
        
        return dirs;
    }
    
    public String[] listDirs( String baseDir, String[] excludes ) {
        
        DirectoryScanner ds = new DirectoryScanner();
        String[] includes = { "**" };
        
        ds.setBasedir( baseDir );
        ds.setCaseSensitive( true );
        ds.setIncludes( includes );
        ds.setExcludes( excludes );
        
        ds.scan();
        
        String[] dirs = ds.getIncludedDirectories();
        
        return dirs;
    }
    
    
    private void createTemplatesDirectory() {
        
        String jarfile = null;
        
        //myParent.addHTMLTemplateDirectory();
        // create physical directory
        File templatesDir = new File( myParent.getBuildDirectory() + "\\Templates" );
        if( !templatesDir.exists() ) {
            if( templatesDir.mkdir() ) {
                myParent.printMessage( "Created Templates directory" );
            } else {
                myParent.printMessage( "Error: Templates directory not created" );
            }
        }
        
        if( templatesDir.exists() ) {
                        
            String dwtfile = dwtNameTextField.getText().trim();
            if( dwtfile.length() > 5 && dwtfile.toLowerCase().endsWith( ".dwt" ) ) {
                myParent.copyFile( '\"' + dwtfile + '\"', '\"' + myParent.getBuildDirectory() + "\\Templates" + '\"' );
            } else{
                myParent.printMessage( "Error: Invalid Dreamweaver template name" );
            }
            
            
        }
        
    }
    
}
