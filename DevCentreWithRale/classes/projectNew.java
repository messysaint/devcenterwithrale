/*
 * projectOpen.java
 *
 * Created on December 16, 2002, 7:05 AM
 */
import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.filechooser.*;

import java.awt.*;
//import java.io.File;
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
public class projectNew extends javax.swing.JDialog {
    
    /** Creates new form projectOpen */
    public projectNew(mainForm parent, boolean modal) {
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buildxmlPath = new javax.swing.JTextField();
        getDirectory = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        archiveName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        ok = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        chooser.setDialogTitle("Select Build.xml Directory");
        chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        chooser.setFont(new java.awt.Font("Default", 0, 11));
        chooser.setMultiSelectionEnabled(true);

        setTitle("New Project");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Default", 0, 11));
        jLabel1.setText("Project Directory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        buildxmlPath.setEditable(false);
        buildxmlPath.setFont(new java.awt.Font("Default", 0, 11));
        buildxmlPath.setPreferredSize(new java.awt.Dimension(300, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(buildxmlPath, gridBagConstraints);

        getDirectory.setFont(new java.awt.Font("Default", 0, 11));
        getDirectory.setText("Browse ...");
        getDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getDirectoryActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        jPanel1.add(getDirectory, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("Default", 0, 11));
        jLabel11.setText("Archive Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel11, gridBagConstraints);

        archiveName.setFont(new java.awt.Font("Default", 0, 11));
        archiveName.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(archiveName, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        ok.setFont(new java.awt.Font("Default", 0, 11));
        ok.setText("Ok");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        jPanel3.add(ok);

        cancel.setFont(new java.awt.Font("Default", 0, 11));
        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        jPanel3.add(cancel);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel4, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel5, java.awt.BorderLayout.WEST);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-507)/2, (screenSize.height-227)/2, 507, 227);
    }//GEN-END:initComponents
        
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        // Add your handling code here:
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelActionPerformed
    
    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        // Add your handling code here:
        DESedeEncryptor psec = new DESedeEncryptor();
        
        // check directory for src, metadata, lib, classes, build.xml, client.properties
        String selectedDirectory = buildxmlPath.getText().trim();
        if( selectedDirectory.length() == 0 ) {
            return;
        } else if( !(new File( selectedDirectory ).isDirectory()) ) {
            return;
        }
        
        // check presence of required directories and files
        // if any one is present do not continue
        File selectedFile = new File( selectedDirectory );
        String[] files = selectedFile.list();
        
        for( int i = 0 ; i < files.length ; i++ ) {
            if( files[i].equalsIgnoreCase( "metadata" ) ||
            files[i].equalsIgnoreCase( "lib" ) ||
            files[i].equalsIgnoreCase( "classes" ) ||
            files[i].equalsIgnoreCase( "src" ) ||
            files[i].equalsIgnoreCase( "Templates" ) ||
            files[i].equalsIgnoreCase( "client.properties" ) ||
            files[i].equalsIgnoreCase( "build.xml" )
            ) {
                myParent.printMessage( "Some required files are already existing." );
                myParent.printMessage( "Please select another directory." );
                return;
            }
        }
        
        // check required parameters entered in UI
        // buildxmlPath - done
        
        if( archiveName.getText().trim().length() == 0 ) {
            myParent.printMessage( "Archive name is invalid" );
            return;
        }
        
        // remove .war from text
        if( archiveName.getText().trim().indexOf( ".war" ) != -1) {
            String archive = archiveName.getText().trim();
            archiveName.setText( archive.substring( 0, archive.indexOf( ".war" ) ) );
        }
        warFileName = archiveName.getText().trim() + ".war";
        
        
        
        // ----------------------------------------------
        // create required directories
        new File( selectedDirectory + "\\metadata").mkdir();            
        
        new File( selectedDirectory + "\\lib").mkdir();
        
        if( new File( selectedDirectory + "\\classes").mkdir() ) {            
            new File( selectedDirectory + "\\classes\\model").mkdir();
            new File( selectedDirectory + "\\classes\\view").mkdir();
            new File( selectedDirectory + "\\classes\\tags").mkdir();
        }
        
        new File( selectedDirectory + "\\workarea").mkdir();        
        new File( selectedDirectory + "\\Templates").mkdir();        
        new File( selectedDirectory + "\\backup").mkdir();        
        new File( selectedDirectory + "\\tld").mkdir();        
        new File( selectedDirectory + "\\WEB-INF").mkdir();
        
        if( new File( selectedDirectory + "\\src").mkdir() ) {            
            new File( selectedDirectory + "\\src\\html").mkdir();
            new File( selectedDirectory + "\\src\\image").mkdir();
            if( new File( selectedDirectory + "\\src\\jsp").mkdir() ) {                
                new File( selectedDirectory + "\\src\\jsp\\model").mkdir();
                new File( selectedDirectory + "\\src\\jsp\\view").mkdir();
                new File( selectedDirectory + "\\src\\jsp\\error").mkdir();
                new File( selectedDirectory + "\\src\\jsp\\debug").mkdir();
            }
            new File( selectedDirectory + "\\src\\css").mkdir();
            new File( selectedDirectory + "\\src\\others").mkdir();
        }
        
        // create client.properties
        String key = new String();
        String[] configline = new String[14];
        configline[0] = "# SSH Server connection settings:";
        key = getKey();
        configline[1] = "sshServerHost = " + key+psec.encryptToBase64( "locahost", key );
        key = getKey();
        configline[2] = "sshPort = " + key+psec.encryptToBase64( "22", key );
        key = getKey();
        configline[3] = "sshRemoteDirectory = " + key+psec.encryptToBase64( "/tomcat/webapps", key );
        key = getKey();
        configline[4] = "sshUserName = " + key+psec.encryptToBase64( "tomcat", key );
        key = getKey();
        configline[5] = "sshPassword = " + key+psec.encryptToBase64( "password", key );
        key = getKey();
        configline[6] = "sshVersion =  " + key+psec.encryptToBase64( "2", key );
        
        configline[7] = " ";
        
        key = getKey();
        configline[8] = "# Tomcat connection: ";
        key = getKey();
        configline[9]  = "tomcatPort = " + key+psec.encryptToBase64( "8080", key );
        key = getKey();
        configline[10]  = "tomcatAuthURI = " + key+psec.encryptToBase64( "/manager/html", key );
        key = getKey();
        configline[11] = "tomcatRealm = " + key+psec.encryptToBase64( "Tomcat Manager Application", key );        
        key = getKey();
        configline[12] = "tomcatAdmin = " + key+psec.encryptToBase64( "admin", key );        
        key = getKey();
        configline[13] = "tomcatPassword = " + key+psec.encryptToBase64( "password", key );                
        
        createConfigFile( selectedDirectory + "\\client.properties", configline );
        
        
        String fileSource = selectedDirectory + "\\build.xml";
        String buildTemplate = myParent.getArchiverBaseDir() + "templates\\build.xml";
        String src = myParent.readBuildXml( buildTemplate, warFileName );
        myParent.saveCodeToFile( src, fileSource );
        
        //System.out.println( fileSource );
        //System.out.println( buildTemplate );
        //System.out.println( src );
        
        // copy /classes/controller.class
        myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\*.java\"", '\"' + selectedDirectory + "\\classes\\\"" );
        
        // copy /classes/baseBroker.class
        //myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\baseBroker.java\"", '\"' + selectedDirectory + "\\classes\\\"" );
        
        // copy /metadata/web.xml
        myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\web.xml\"", '\"' + selectedDirectory + "\\metadata\\\"" );
        
        // copy /classes/controllerActionMap.properties
        myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\controllerActionMap.properties\"", '\"' + selectedDirectory + "\\classes\\\"" );
        
        // copy /classes/controllerActionMap.properties
        // myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\dbConnectionPool.properties\"", '\"' + selectedDirectory + "\\classes\\\"" );
        
        // copy index.html
        myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\index.html\"", '\"' + selectedDirectory + "\\src\\\"" );
        
        // copy /src/jsp/error/controllerTargetNotFound.jsp
        myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\controllerTargetNotFound.jsp\"", '\"' + selectedDirectory + "\\src\\jsp\\error\\\"" );
        
        // copy /src/jsp/debug/debug.jsp
        myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\debug.jsp\"", '\"' + selectedDirectory + "\\src\\jsp\\debug\\\"" );
        
        // copy /src/jsp/model/upload.jsp
        myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\upload.jsp\"", '\"' + selectedDirectory + "\\src\\jsp\\model\\\"" );
        
        // copy /*.jar
        myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\jar\\*.jar\"", '\"' + selectedDirectory + "\\lib\\\"" );
        
        myParent.copyFile( '\"' + myParent.getArchiverBaseDir() + "templates\\otherjar.properties\"", '\"' + selectedDirectory + '\"' );
        
        myParent.setBuildDirectory( selectedDirectory );
        myParent.setWarFileName( warFileName );
        myParent.setTreeRootDirectory( selectedDirectory );
        myParent.setBatchDirectory( selectedDirectory + "\\upload.bat" );
        myParent.clearTree();
        myParent.populateTree();
        myParent.enableButtons( true );
        myParent.setTitle();
        myParent.printMessage( "New Project" + selectedDirectory );
        
        myParent.closeAllFrames(); // close whatever open internal frames
        
        setVisible(false);
        dispose();
    }//GEN-LAST:event_okActionPerformed
    
    private void getDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getDirectoryActionPerformed
        
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
            if (theFile != null) {
                if (theFile.isDirectory()) {
                    buildDirectory = theFile.getPath();
                    buildxmlPath.setText( buildDirectory );
                }
            }
            
        } else if (retval == JFileChooser.CANCEL_OPTION) {
            myParent.printMessage( "Canceled");
        } else if (retval == JFileChooser.ERROR_OPTION) {
            myParent.printMessage( "Error");
        } else {
            
        }
        
    }//GEN-LAST:event_getDirectoryActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new projectNew( new mainForm(), true ).show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField archiveName;
    private javax.swing.JTextField buildxmlPath;
    private javax.swing.JButton cancel;
    private javax.swing.JFileChooser chooser;
    private javax.swing.JButton getDirectory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton ok;
    // End of variables declaration//GEN-END:variables
    
    private boolean valid = false;
    private String buildDirectory = new String();
    private String warFileName = new String();
    private FilePreviewer previewer;
    private String LnF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    
    mainForm myParent; // mainForm parent
    
    
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
    
    
    private boolean createConfigFile( String filename, String[] configline ) {
        
        boolean rvalue = false;
        
        try {
            FileWriter fw = new FileWriter( filename, false);
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(br);
            
            for( int i = 0 ; i < configline.length ; i++ ) {
                pw.println( configline[i] );
            }
            
            pw.close();
            br.close();
            fw.close();
            
        } catch (IOException io) {
            myParent.printMessage( io.toString() );
        } catch (Exception e) {
            myParent.printMessage( e.toString() );
        }
        
        return rvalue;
    }
    
    public String getKey() {
        DESedeKeyGenerator kgen = new DESedeKeyGenerator();
        return kgen.generate();
    }
    
}
