/*
 * This code is based on an example provided by Richard Stanford,
 * a tutorial reader.
 */
import java.awt.event.*;
import javax.swing.JPopupMenu;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

// from apache BCEL
import org.apache.bcel.classfile.*;
import java.lang.reflect.*;

import java.util.zip.ZipFile;

import com.likha.codeCompletion.*;

public class LibTree extends JPanel {
    
    private String LnF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    private String rootDir = "";
    private static String currentPath = "";
    private mainForm main;
    
    private boolean templateSet = false;
    
    public LibTree(mainForm parent) {
        
        try {
            UIManager.setLookAndFeel( LnF );
            SwingUtilities.updateComponentTreeUI( this );
            //if(chooser != null) {
            SwingUtilities.updateComponentTreeUI( this );
            //}
        } catch (UnsupportedLookAndFeelException exc) {
            main.printMessage( exc.toString() );
        } catch (IllegalAccessException exc) {
            main.printMessage( exc.toString() );
        } catch (ClassNotFoundException exc) {
            main.printMessage( exc.toString() );
        } catch (InstantiationException exc) {
            main.printMessage( exc.toString() );
        }
        
        main = parent;
        
        rootNode = new DefaultMutableTreeNode("Jar Libraries");
        
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());
        
        tree = new JTree(treeModel);
        tree.setCellRenderer( new treeCellRenderer( main ) );
        tree.addMouseListener( new treeMouseListener() );
        //tree.addMouseListener( new PopupListener() );
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.setRootVisible( false );
        //Listen for when the selection changes.
        
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            
            public void valueChanged(TreeSelectionEvent e) {
                
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                
                if (node == null) return;
                
                // get path
                TreeNode[] tn = node.getPath();
                String relativePath = new String();
                for( int i = 1 ; i < tn.length ; i++ ) {
                    relativePath += '\\' + tn[i].toString();
                }
                
                currentPath = rootDir + relativePath;
                refreshNodeChildren();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tree);
        setLayout(new GridLayout(1,0));
        add(scrollPane);
    }
    
    /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }
    
    public void refreshNodeChildren() {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();
        
        String fullPath = parentPath.toString();
        String jarPath = "";
        String jarName = "";
        String jarElementPath = "";
        
        fullPath = fullPath.substring( 1, fullPath.length()-1 ).replaceAll( ", ", "/" );
        jarPath = fullPath.substring( 0, fullPath.indexOf( ".jar" )+4 );
        jarName = jarPath.substring( jarPath.indexOf( '/')+1 );
        jarElementPath = fullPath.substring( jarPath.length() );
        
        if( jarElementPath.startsWith( "/" ) ) {  // remove '/'
            jarElementPath = jarElementPath.substring( 1 );
        }
        
        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
        }
        
        if( !parentNode.equals( rootNode ) ) {
            
            Enumeration children = parentNode.children();
            HashSet hset = new HashSet();
            
            if( !children.hasMoreElements() ) { // does not have children
                
                // get jar items
                String jarAbsolutePath = getLibPath( jarName );
                
                try {
                    //main.printMessage( "Full Path: " + fullPath );
                    //main.printMessage( "Jar Path: " + jarPath );
                    //main.printMessage( "Jar Name: " + jarName );
                    //main.printMessage( "Jar Element Path: " + jarElementPath );
                    //main.printMessage( "Absolute Jar Path: " + jarAbsolutePath );
                    
                    ZipFile jarFile = new ZipFile( jarAbsolutePath );
                    String[] validElements = getValidElements( jarFile.entries(), jarElementPath );
                    
                    boolean reload = false;
                    DefaultMutableTreeNode node = null;
                    
                    if( validElements.length > 0 ) {
                        
                        for( int i = 0 ; i < validElements.length ; i ++ ) {
                            //main.printMessage( validElements[i] );
                            // add hashset values to tree
                            node = new DefaultMutableTreeNode( validElements[i] );
                            addObject( parentNode, node, true ); // add some node
                            reload = true;
                        }
                        
                    } else {
                        // check if jarElementPath end in ".class"
                        if( jarElementPath.endsWith( ".class" ) ) {
                                                        
                            DefaultMutableTreeNode[] details = getClassDetailsHeaders();
                            
                            if( details.length > 0 ) {
                                for( int i = 0 ; i < details.length ; i++ ) {
                                    addObject( parentNode, details[i] , true ); // add some node
                                }
                            }
                            
                            reload = true;
                        } else if( jarElementPath.endsWith( ".class/Interfaces" ) ) {
                            
                            jarElementPath = jarElementPath.substring( 0, jarElementPath.lastIndexOf( '/' ) );
                            
                            DefaultMutableTreeNode[] details = getClassDetails( jarAbsolutePath, jarElementPath, "Interfaces" );
                            
                            if( details.length > 0 ) {
                                for( int i = 0 ; i < details.length ; i++ ) {
                                    addObject( parentNode, details[i] , true ); // add some node
                                }
                            }
                            
                            //reload = true;
                        } else if( jarElementPath.endsWith( ".class/Fields" ) ) {
                            
                            jarElementPath = jarElementPath.substring( 0, jarElementPath.lastIndexOf( '/' ) );
                            
                            DefaultMutableTreeNode[] details = getClassDetails( jarAbsolutePath, jarElementPath, "Fields" );
                            
                            if( details.length > 0 ) {
                                for( int i = 0 ; i < details.length ; i++ ) {
                                    addObject( parentNode, details[i] , true ); // add some node
                                }
                            }
                            
                            //reload = true;
                        } else if( jarElementPath.endsWith( ".class/Methods" ) ) {
                            
                            jarElementPath = jarElementPath.substring( 0, jarElementPath.lastIndexOf( '/' ) );                            
                            
                            DefaultMutableTreeNode[] details = getClassDetails( jarAbsolutePath, jarElementPath, "Methods" );
                            
                            if( details.length > 0 ) {
                                for( int i = 0 ; i < details.length ; i++ ) {
                                    addObject( parentNode, details[i] , true ); // add some node
                                }
                            }
                            
                            //reload = true;
                        }
                        
                        // add class details
                    }
                    
                    
                    if( reload ) {
                        treeModel.reload(); // redisplay tree UI
                    }
                    
                } catch( java.io.IOException ioe ) {
                    main.printMessage( ioe.toString() );
                }
                
                
            }
            
            //treeModel.reload(); // redisplay tree UI
            
        } else {
            currentPath = rootDir;
        }
        
        tree.expandPath( parentPath );
        //System.out.println( currentPath );
        
    }
    
    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        }
        
        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }
    
    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();
        
        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
            (parentPath.getLastPathComponent());
        }
        
        return addObject(parentNode, child, true);
    }
    
    /** Add child to a specified parent node. */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
        return addObject(parent, child, true);
    }
    
    /** Add child to a specified parent node, with visible option */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
        
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
        
        if (parent == null) {
            parent = rootNode;
        }
        
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
        
        // Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }
    
    
    
    public static String getCurrentPath() {
        return currentPath;
    }
    
    public void setRootDir( String dir ) {
        rootDir = dir;
    }
    
    public String getRootDir() {
        return rootDir;
    }
    
    
    public void refreshTree( String libPath, String[] jarFiles ) {
        
        File lib = new File( libPath );
        ZipFile libJar = null;
        HashSet hset = new HashSet();
        DefaultMutableTreeNode[] jarNodes = new DefaultMutableTreeNode[ jarFiles.length ];
        
        if( lib.exists() ) {
            
            if( libChanged( jarFiles ) ) {
                
                clear();
                
                for( int i = 0 ; i < jarFiles.length ; i++ ) {
                    
                    jarNodes[i]  = new DefaultMutableTreeNode( jarFiles[i] );
                    
                    rootNode.add( jarNodes[i] );
                    
                }
                
                tree.expandPath( new TreePath( rootNode.getPath() ) );
                enable( true );
            }
            
        }
        
    }
    
    
    
    // /node1/node2/node3
    public String[] tokenizeClassPath( String path ){
        
        StringTokenizer tokenizer = new StringTokenizer( path, "/" );
        String[] rvalue = new String[ tokenizer.countTokens() ];
        
        for( int i = 0 ; tokenizer.hasMoreTokens() ; i++ ) {
            rvalue[i] = tokenizer.nextToken();
        }
        return rvalue;
    }
    
    // popUp Listener for each cell
    class PopupListener extends MouseAdapter {
        
        public void mousePressed(MouseEvent e) {
            //maybeShowPopup(e);
        }
        
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
        
        private void maybeShowPopup(MouseEvent e) {
            
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            treePopUp popup = new treePopUp( main, DynamicTree.getCurrentPath(), node );
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
            
        }
    }
    
    class treeMouseListener implements java.awt.event.MouseListener {
        
        public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
            int cnt = mouseEvent.getClickCount();
            String selectedFile = getCurrentPath(); // file from tree
            // do something here to dispay interfaces, fields, methods
        }
        
        public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
        }
        
        public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
        }
        
        public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
        }
        
        public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
        }
        
    }
    
    
    class MyTreeModelListener implements TreeModelListener {
        
        public void treeNodesChanged(TreeModelEvent e) {
            
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());
            
            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)
                (node.getChildAt(index));
            } catch (NullPointerException exc) {
            }
            
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
    
    
    public String getLibPath( String jarName ) {
        
        String rvalue = main.getBuildDirectory() + "\\lib\\" + jarName;
        return rvalue;
        
    }
    
    
    public String[] getValidElements( Enumeration enum, String jarElementPath ) {
        
        HashSet hset = new HashSet();
        String[] rvalue = {};
        String[] tokens = {};
        String entry = "";
        
        // filter out only those that start with the tree path value
        // substring first element to '/'
        // add to hashset
        
        jarElementPath = jarElementPath.trim();
        
        if( jarElementPath.length() == 0 ) { // return all elements
            
            while( enum.hasMoreElements() ) {
                entry = enum.nextElement().toString();
                tokens = tokenizeClassPath( entry );
                hset.add( tokens[0] ); // only add first element in token
            }
            Object[] arr = hset.toArray();
            rvalue = new String[ arr.length ];
            
            for( int i = 0 ; i < arr.length ; i++ ) {
                rvalue[i] = (String) arr[i];
            }
            
        } else { // return only those that start with jarElementPath
            
            while( enum.hasMoreElements() ) {
                entry = enum.nextElement().toString();
                if( entry.trim().startsWith( jarElementPath ) ) {
                    entry = entry.substring( jarElementPath.length() );
                    if( entry.startsWith( "/" ) ) {
                        entry = entry.substring( 1 );
                    }
                    
                    if( entry.trim().length() > 0 ) {
                        tokens = tokenizeClassPath( entry );
                        hset.add( tokens[0] );
                        //hset.add( entry );
                    }
                }
            }
            Object[] arr = hset.toArray();
            rvalue = new String[ arr.length ];
            
            for( int i = 0 ; i < arr.length ; i++ ) {
                rvalue[i] = (String) arr[i];
            }
            
        }
        
        
        return rvalue;
    }
    
    
    public boolean libChanged( String[] jarFiles ) {
        
        boolean rvalue = false;
        String lib = "";
        HashSet hset1 = new HashSet();
        HashSet hset2 = new HashSet();
        Enumeration enum = rootNode.children();
        // test 1
        while( enum.hasMoreElements() ) {
            lib = enum.nextElement().toString();
            hset1.add( lib );
        }
        
        for( int i = 0 ; i < jarFiles.length ; i++ ) {
            if( hset1.add( jarFiles[i].trim() ) ) {
                rvalue = true;
                break;
            }
        }
        
        // test 2
        if( !rvalue ) {
            
            for( int i = 0 ; i < jarFiles.length ; i++ ) {
                hset2.add( jarFiles[i].trim() );
            }
            
            enum = rootNode.children();
            
            while( enum.hasMoreElements() ) {
                lib = enum.nextElement().toString();
                if( hset2.add( lib ) ) {
                    rvalue = true;
                    break;
                }
            }
        }
        
        return rvalue;
    }
    
    public DefaultMutableTreeNode[] getClassDetailsHeaders() {
        
        DefaultMutableTreeNode[] rvalue = new DefaultMutableTreeNode[3];
        
        DefaultMutableTreeNode interfacesNode = new DefaultMutableTreeNode( "Interfaces" );
        DefaultMutableTreeNode fieldsNode = new DefaultMutableTreeNode( "Fields" );
        DefaultMutableTreeNode methodsNode = new DefaultMutableTreeNode( "Methods" );
        
        rvalue[0] = interfacesNode;
        rvalue[1] = fieldsNode;
        rvalue[2] = methodsNode;
        
        return rvalue;
    }
    
    public DefaultMutableTreeNode[] getClassDetails( String jarFilePath, String className, String type ) {
        
        DefaultMutableTreeNode[] rvalue = null;
        
        try {
            
            //InputStream in = (InputStream) new BufferedInputStream( new FileInputStream( jarFilePath ) ) ;
            FileInputStream fileIn = new FileInputStream( jarFilePath );
            InputStream in = (InputStream) fileIn;
            
            //ClassParser cp = new ClassParser( in, className );
            ClassParser cp = new ClassParser( jarFilePath, className );
            JavaClass jc = cp.parse();
            
            
            if( type.equalsIgnoreCase( "Interfaces" ) ) {
                // display all interfaces
                String[] allInterfaces = jc.getInterfaceNames();
                rvalue = new DefaultMutableTreeNode[ allInterfaces.length ];
                for( int i = 0 ; i < allInterfaces.length ; i++ ) {
                    rvalue[i] = new DefaultMutableTreeNode( allInterfaces[i] );
                }
            } else if( type.equalsIgnoreCase( "Fields" ) ) {
                org.apache.bcel.classfile.Field[] allFields = jc.getFields();
                rvalue = new DefaultMutableTreeNode[ allFields.length ];
                for( int i = 0 ; i < allFields.length ; i++ ) {
                    rvalue[i] = new DefaultMutableTreeNode( allFields[i].toString() );
                }
            } else if( type.equalsIgnoreCase( "Methods" ) ) {
                org.apache.bcel.classfile.Method[] allMethods = jc. getMethods();
                rvalue = new DefaultMutableTreeNode[ allMethods.length ];
                for( int i = 0 ; i < allMethods.length ; i++ ) {
                    rvalue[i] = new DefaultMutableTreeNode( allMethods[i].toString() );
                }
            }
                        
            in.close();
            fileIn.close();                      
            
        } catch( java.io.IOException ioe ) {
            main.printMessage( ioe.toString() );
        }
        
        return rvalue;
        
    }
    
}


