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
public class sendJarLibrary extends javax.swing.JDialog implements Runnable {
    
    
    
    /** Creates new form projectOpen */
    public sendJarLibrary(mainForm parent, JDialog toDialog, boolean modal, String cmd, String warFile, String file ) {
        super(parent, modal);
        initComponents();
        myParent = parent;
        dialog = toDialog;
        command = cmd;
        appName = warFile;
        fileName = file;
        
        try {
            UIManager.setLookAndFeel( LnF );
            SwingUtilities.updateComponentTreeUI( this );
        } catch (UnsupportedLookAndFeelException exc) {
        } catch (IllegalAccessException exc) {
            myParent.printMessage("IllegalAccessException Error:" + exc);
        } catch (ClassNotFoundException exc) {
            myParent.printMessage("ClassNotFoundException Error:" + exc);
        } catch (InstantiationException exc) {
            myParent.printMessage("InstantiateException Error:" + exc);
        }
        
        thread.start();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        timer1 = new org.netbeans.examples.lib.timerbean.Timer();
        jPanel1 = new javax.swing.JPanel();
        messageLabel = new javax.swing.JLabel();
        processProgressBar = new javax.swing.JProgressBar();

        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        setTitle("Sending File");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        messageLabel.setFont(new java.awt.Font("Default", 0, 11));
        messageLabel.setText("Message");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(messageLabel, gridBagConstraints);

        processProgressBar.setFont(new java.awt.Font("Default", 0, 11));
        processProgressBar.setPreferredSize(new java.awt.Dimension(200, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(processProgressBar, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new java.awt.Dimension(372, 129));
        setLocation((screenSize.width-372)/2,(screenSize.height-129)/2);
    }//GEN-END:initComponents
    
    private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
        // Add your handling code here:
        if( paint ) {
            if( progressBarMin > progressBarMax) {
                progressBarMin = 0;
            }
            processProgressBar.setValue( ++progressBarMin );
            processProgressBar.setStringPainted(true);
        }
    }//GEN-LAST:event_timer1OnTime
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //new archiveRefresh( new mainForm(), true ).show();
    }
    
    // create archive when trhead runs
    public void run() {
        
        try {
            if( command.equalsIgnoreCase( "getcommonlibpath") ) {
                messageLabel.setText( "Sending JAR to server, please wait ..." );
                String remoteDirectory = myParent.remoteOtherCommand( command, appName );
                remoteDirectory = remoteDirectory.replace( nextLine, '\n' );                
                myParent.sendJarLibrary( fileName, remoteDirectory );
                
            } else if(command.equalsIgnoreCase( "deletecommonlib") ) {
                messageLabel.setText( "Deleting JAR from server, please wait ..." );
                String remoteDirectory = myParent.remoteOtherCommand( command, appName, fileName );
                remoteDirectory = remoteDirectory.replace( nextLine, '\n' );                
                
            } else if(command.equalsIgnoreCase( "getcommonclassespath") ) {
                messageLabel.setText( "Sending Java class to server, please wait ..." );
                String remoteDirectory = myParent.remoteOtherCommand( command, appName );
                remoteDirectory = remoteDirectory.replace( nextLine, '\n' );                
                myParent.sendJarLibrary( fileName, remoteDirectory );              
            } else if(command.equalsIgnoreCase( "deletecommonclass") ) {
                messageLabel.setText( "Deleting common Java class from server, please wait ..." );
                String remoteDirectory = myParent.remoteOtherCommand( command, appName, fileName );
                remoteDirectory = remoteDirectory.replace( nextLine, '\n' );                
                
            }                        
            
            processProgressBar.setValue( progressBarMax );
            paint = false;
            thread.sleep( 1000);
            processProgressBar.setStringPainted(true);
        } catch( java.lang.InterruptedException ie ) {
            myParent.printMessage( ie.toString() );
        }
        
        setVisible(false);
        dispose();
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar processProgressBar;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel messageLabel;
    // End of variables declaration//GEN-END:variables
    
    private String LnF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    mainForm myParent; // mainForm parent
    JDialog dialog = null;
    private boolean paint = true;
    private int progressBarMin = 0;
    private int progressBarMax = 100;
    private char nextLine = '|';
    
    String command = new String();
    String appName = new String();
    
    String fileName = new String();
    
    Thread thread = new Thread( this );
    
}
