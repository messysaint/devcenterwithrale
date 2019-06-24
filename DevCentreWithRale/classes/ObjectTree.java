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

public class ObjectTree extends JPanel {
    
    private String LnF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    private String rootDir = "";
    private static String currentPath = "";
    private mainForm main;
    
    private boolean templateSet = false;
    
    public ObjectTree(mainForm parent) {
        
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
        
        rootNode = new DefaultMutableTreeNode("Class file object");
        
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());
        
        tree = new JTree(treeModel);
        //tree.setCellRenderer( new treeCellRenderer() );
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
        treeModel.reload(); // redisplay tree UI
    }
    
    public void refreshNodeChildren() {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();
        
        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
        }
        
        if( !parentNode.equals( rootNode ) ) {
            
            Enumeration children = parentNode.children();
            HashSet hs = new HashSet();
            while( children.hasMoreElements() ) {
                hs.add( children.nextElement().toString() );
            }
            // get dir files and add to node
            File file = new File( currentPath );
            if( file.isDirectory() ) {
                String[] names = file.list();
                //if( names.length > 0 ) {
                //parentNode.removeAllChildren();  // does not work without reloading tree
                //treeModel.reload(); // redisplay tree UI
                HashSet fs = new HashSet();
                for( int i = 0 ; i < names.length ; i++ ) {
                    //if( new File( currentPath + '\\' + names[i] ).isDirectory() ) {
                    fs.add( names[i] );
                    if( !hs.contains( names[i] ) ) {
                        addObject( parentNode, names[i], true );
                    }
                    //}
                }//for
                
                boolean deleted = false;
                //if( fs.size() > 0 ) {  // anything to check
                String[] childArray = new String[ hs.size() ];
                Iterator iter = hs.iterator();
                for( int i = 0 ; i < hs.size() ; i++ ) {
                    childArray[i] = iter.next().toString();
                }
                for( int i = 0 ; i < childArray.length ; i++ ) {
                    if( !fs.contains( childArray[i] ) ) {
                        //remove from parent node
                        //parentNode.remove( i );
                        children = parentNode.children();
                        while( children.hasMoreElements() ) {
                            MutableTreeNode mtn = (MutableTreeNode) children.nextElement();
                            if( mtn.toString().startsWith( childArray[i] ) ) {
                                parentNode.remove( mtn ); //----------------------------->
                                //System.out.println( "Deleted: " + childArray[i] );
                                deleted = true;
                            }
                        }
                    }
                }//for
                if( deleted ) { //
                    treeModel.reload(); // redisplay tree UI
                }
                
                //}
                
                //}
            }
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
    
    public static String getCurrentPath() {
        return currentPath;
    }
    
    public void setRootDir( String dir ) {
        rootDir = dir;
    }
    
    public String getRootDir() {
        return rootDir;
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
    
    
    private class ItemInfo {
        
        public String bookName;
        public String filePath;
        
        public ItemInfo(String book, String path) {
            bookName = book;
            filePath = path;
        }
        
        public String toString() {
            return bookName;
        }
        
        public String getFilePath() {
            return filePath;
        }
        
    }
    
    
    public void initTree() {
        
        DefaultMutableTreeNode nameNode = new DefaultMutableTreeNode(new ItemInfo( "Name", null) );
        DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(new ItemInfo( "Package", null) );
        DefaultMutableTreeNode interfaceNode = new DefaultMutableTreeNode(new ItemInfo( "Interfaces", null) );
        DefaultMutableTreeNode fieldsNode = new DefaultMutableTreeNode(new ItemInfo( "Fields", null) );
        DefaultMutableTreeNode methodsNode = new DefaultMutableTreeNode(new ItemInfo( "Methods", null) );
        
        DefaultMutableTreeNode p1;
        DefaultMutableTreeNode p2;
        DefaultMutableTreeNode p3;
        DefaultMutableTreeNode p4;
        DefaultMutableTreeNode p5;
        
        
        p1 = addObject(null, nameNode);
        p2 = addObject(null, packageNode);
        p3 = addObject(null, interfaceNode);
        p4 = addObject(null, fieldsNode);
        p5 = addObject(null, methodsNode);
        
        enable( true );
    }
    
    
    public void initTree( String classNameFullPath ) {
        
        try {
            
            ClassParser cp = new ClassParser( classNameFullPath );
            JavaClass jc = cp.parse();
                        
            DefaultMutableTreeNode nameNode = new DefaultMutableTreeNode(new ItemInfo( "Name", null) );
            DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(new ItemInfo( "Package", null) );
            DefaultMutableTreeNode interfacesNode = new DefaultMutableTreeNode(new ItemInfo( "Interfaces", null) );
            DefaultMutableTreeNode fieldsNode = new DefaultMutableTreeNode(new ItemInfo( "Fields", null) );
            DefaultMutableTreeNode methodsNode = new DefaultMutableTreeNode(new ItemInfo( "Methods", null) );

            String className = classNameFullPath.substring( classNameFullPath.lastIndexOf('\\')+1 );
            // className = className.substring( className.lastIndexOf('\\')+1 );
            DefaultMutableTreeNode classNameFullPathNode = new DefaultMutableTreeNode(new ItemInfo( className, null) );
            nameNode.add( classNameFullPathNode );                                                            
                        
            DefaultMutableTreeNode packageNameNode = new DefaultMutableTreeNode(new ItemInfo( jc.getPackageName(), null) );
            packageNode.add( packageNameNode );
            
            // display all interfaces
            String[] allInterfaces = jc.getInterfaceNames();            
            for( int i = 0 ; i < allInterfaces.length ; i++ ) {
                interfacesNode.add( new DefaultMutableTreeNode(new ItemInfo( allInterfaces[i], null) ) );
            }
            
            org.apache.bcel.classfile.Field[] allFields = jc.getFields();
            for( int i = 0 ; i < allFields.length ; i++ ) {
                fieldsNode.add( new DefaultMutableTreeNode(new ItemInfo( allFields[i].toString(), null) ) );
            }
            
            org.apache.bcel.classfile.Method[] allMethods = jc. getMethods();
            for( int i = 0 ; i < allMethods.length ; i++ ) {
                methodsNode.add( new DefaultMutableTreeNode(new ItemInfo( allMethods[i].toString(), null) ) ); 
            }
                                                                    
            rootNode.add(nameNode);
            rootNode.add(packageNode);
            rootNode.add(interfacesNode);
            rootNode.add(fieldsNode);
            rootNode.add(methodsNode);
                                   
            tree.expandPath( new TreePath( nameNode.getPath() ) );
            tree.expandPath( new TreePath( packageNode.getPath() ) );            
            
            enable( true );                        
            
        } catch( java.io.IOException ioe ) {
            main.printMessage( ioe.toString() );
        }
        
        
    }
    
    
    /*
    public void addTemplateToTree() {
     
        if( !templateSet ) {
            DefaultMutableTreeNode p1Name = new DefaultMutableTreeNode(new ItemInfo( "Templates", null) );
            DefaultMutableTreeNode p1;
     
            p1 = addObject(null, p1Name);
            templateSet = true;
        }
     
    }
     */
    
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
    
}


