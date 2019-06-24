/*
 * This code is based on an example provided by Richard Stanford,
 * a tutorial reader.
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class PackageTree extends JPanel {
    
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    private String rootDir = "";
    private String currentPath = "";
    
    public PackageTree() {
        
        rootNode = new DefaultMutableTreeNode("Java Packages");
        
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());
        
        tree = new JTree(treeModel);
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
                    if( new File( currentPath + '\\' + names[i] ).isDirectory() ) {
                        fs.add( names[i] );
                        if( !hs.contains( names[i] ) ) {
                            addObject( parentNode, names[i], true );
                        }
                    }
                }//for
                                
                boolean deleted = false;
                //if( fs.size() > 0 ) {  // anything to check
                String[] childArray = new String[ hs.size() ];
                Iterator iter = hs.iterator();
                for( int i = 0 ; i < childArray.length ; i++ ) {
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
                                System.out.println( "Deleted: " + childArray[i] );
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
        System.out.println( currentPath );
        
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
            parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
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
    
    public String getCurrentPath() {
        return currentPath;
    }
    
    public void setRootDir( String dir ) {
        rootDir = dir;
    }
    
    public String getRootDir() {
        return rootDir;
    }
    
}


