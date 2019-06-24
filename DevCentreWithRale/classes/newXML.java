/*
 * saveYesNo.java
 *
 * Created on December 28, 2002, 11:02 AM
 */
import java.util.*;
import java.awt.*;
import javax.swing.tree.*;
import java.io.*;

/**
 *
 * @author  test1
 */
public class newXML extends javax.swing.JDialog {
    
    /** Creates new form saveYesNo */
    public newXML(mainForm parent, boolean modal, String treeRoot) {
        super(parent, modal);
        initComponents();
        main = parent;
        treeRootDirectory = treeRoot;
        setTreeRootDirectory( treeRootDirectory );
        
        setTemplates();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        selectionGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        treeScrollPane = new javax.swing.JScrollPane();
        populateTree();
        treePanel.setPreferredSize(new Dimension(77, 150));
        treeScrollPane.setViewportView( treePanel );
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        newXMLTextField = new javax.swing.JTextField();
        jLabel111 = new javax.swing.JLabel();
        xmlTemplateComboBox = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        doneButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setTitle("New XML File");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jSplitPane1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        treeScrollPane.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        treeScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        treeScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jSplitPane1.setLeftComponent(treeScrollPane);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel11.setFont(new java.awt.Font("Default", 0, 11));
        jLabel11.setText("Class Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jLabel11, gridBagConstraints);

        newXMLTextField.setFont(new java.awt.Font("Default", 0, 11));
        newXMLTextField.setMinimumSize(new java.awt.Dimension(200, 19));
        newXMLTextField.setPreferredSize(new java.awt.Dimension(200, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanel6.add(newXMLTextField, gridBagConstraints);

        jLabel111.setFont(new java.awt.Font("Default", 0, 11));
        jLabel111.setText("Template:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jLabel111, gridBagConstraints);

        xmlTemplateComboBox.setFont(new java.awt.Font("Default", 0, 11));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(xmlTemplateComboBox, gridBagConstraints);

        jSplitPane1.setRightComponent(jPanel6);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        createButton.setFont(new java.awt.Font("Default", 0, 11));
        createButton.setText("Create");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        jPanel3.add(createButton);

        doneButton.setFont(new java.awt.Font("Default", 0, 11));
        doneButton.setText("Close");
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });

        jPanel3.add(doneButton);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel4, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel5, java.awt.BorderLayout.WEST);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-431)/2, (screenSize.height-317)/2, 431, 317);
    }//GEN-END:initComponents
    
    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        // Add your handling code here:
        String newXML = newXMLTextField.getText().trim();        
        String xmlTemplate = (String) xmlTemplateComboBox.getSelectedItem();
        
        if( newXML.length() == 0 || getTreeCurrentPath().length() == 0 ) {
            return;
        } else {                    
            if( newXML.indexOf( '.' ) == -1  ) {
                newXML += ".xml";
            } 
            newXML = getTreeCurrentPath() + '\\' + newXML;                 
            main.createXMLTemplate( xmlTemplate, newXML );    
            if( new File( newXML  ).exists() ) {
                main.editSourceCode( newXML );
            }
        }
        
        setVisible(false);
        dispose();
    }//GEN-LAST:event_createButtonActionPerformed
    
    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        // Add your handling code here:
        setVisible(false);
        dispose();
    }//GEN-LAST:event_doneButtonActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //new saveYesNo( mainForm parent, true).show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createButton;
    private javax.swing.JButton doneButton;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField newXMLTextField;
    private javax.swing.ButtonGroup selectionGroup;
    private javax.swing.JScrollPane treeScrollPane;
    private javax.swing.JComboBox xmlTemplateComboBox;
    // End of variables declaration//GEN-END:variables
    
    private mainForm main;
    private JavaBeanTree treePanel = new JavaBeanTree();    
    private String treeRootDirectory;
    private String mountDir1 = "metadata";
    private String mountDir2 = "lib";
    private String mountDir3 = "classes";
    private String mountDir4 = "src";
    private String mountDir5 = "tld";
    private String mountDir6 = "Templates";
    
    
    public void populateTree() {
        
        DefaultMutableTreeNode p1;        
        DefaultMutableTreeNode p2;        
        DefaultMutableTreeNode p3;        
        DefaultMutableTreeNode p4;        
        DefaultMutableTreeNode p5;        
        DefaultMutableTreeNode p6;        
                        
        p1 = treePanel.addObject(null, new String( mountDir1 ) );
        p2 = treePanel.addObject(null, new String( mountDir2 ) );
        p3 = treePanel.addObject(null, new String( mountDir3 ) );
        p4 = treePanel.addObject(null, new String( mountDir4 ) );
        p5 = treePanel.addObject(null, new String( mountDir5 ) );
        p6 = treePanel.addObject(null, new String( mountDir6 ) );
                
        treePanel.enable( true );
    }
    
    
    public void setTreeRootDirectory( String rootdir ) {
        treePanel.setRootDir( rootdir );
    }
    
    public void clearTree() {
        treePanel.clear();
    }
    
    public String getTreeCurrentPath() {
        return treePanel.getCurrentPath();
    }
    
    public void setTemplates() {
                
        String[] temp = main.listFiles( main.getArchiverBaseDir() + "\\templates\\xml", new String[] {}, new String[] { "*.tpt" } );         
        xmlTemplateComboBox.setModel(new javax.swing.DefaultComboBoxModel( temp ) );
    }
}