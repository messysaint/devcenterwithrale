

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class treeCellRenderer extends DefaultTreeCellRenderer {
    
    ImageIcon folderIcon;
    ImageIcon classIcon;
    ImageIcon htmlIcon;
    ImageIcon jspIcon;
    ImageIcon propertiesIcon;
    ImageIcon imageIcon;
    ImageIcon txtIcon;
    ImageIcon jarIcon;
    ImageIcon xmlIcon;
    ImageIcon javaIcon;
    ImageIcon cssIcon;
    ImageIcon imgIcon;
    ImageIcon otherIcon;
    ImageIcon jsIcon;
    ImageIcon dwtIcon;
    ImageIcon mfIcon;
    
    mainForm main = null;
    
    public treeCellRenderer( mainForm mf ) {
        
        main = mf;
        
        folderIcon = new ImageIcon("icons/tree/folder.gif");
        classIcon = new ImageIcon("icons/tree/class.gif");
        htmlIcon = new ImageIcon("icons/tree/html.gif");
        propertiesIcon = new ImageIcon("icons/tree/properties.gif");
        imageIcon = new ImageIcon("icons/tree/image.gif");
        txtIcon = new ImageIcon("icons/tree/txt.gif");
        jspIcon = new ImageIcon("icons/tree/jsp.gif");
        jarIcon = new ImageIcon("icons/tree/jar.gif");
        xmlIcon = new ImageIcon("icons/tree/xml.gif");
        javaIcon = new ImageIcon("icons/tree/java.gif");
        cssIcon = new ImageIcon("icons/tree/css.gif");
        imgIcon = new ImageIcon("icons/tree/image.gif");
        otherIcon = new ImageIcon("icons/tree/other.gif");
        jsIcon = new ImageIcon("icons/tree/js.gif");
        dwtIcon = new ImageIcon("icons/tree/dwt.gif");
        mfIcon = new ImageIcon("icons/tree/mf.gif");
    }
    
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel,  boolean expanded, boolean leaf, int row, boolean hasFocus) {
                        
        super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus);
        
        if( leaf ) {
            
            if( isDirectory( tree, value) ) {
                setIcon(folderIcon);
            } else if( isClass(value) ) {
                setIcon(classIcon);
            } else if( isJSP(value)) {
                setIcon(jspIcon);
            } else if( isHTML(value)) {
                setIcon(htmlIcon);
            } else if( isProperties(value)) {
                setIcon(propertiesIcon);
            } else if( isText(value)) {
                setIcon(txtIcon);
            } else if( isJar(value)) {
                setIcon(jarIcon);
            } else if( isXml(value)) {
                setIcon(xmlIcon);
            } else if( isJava(value)) {
                setIcon(javaIcon);
            } else if( isJws(value)) {
                setIcon(javaIcon);
            } else if( isCss(value)) {
                setIcon(cssIcon);
            } else if( isJs(value)) {
                setIcon(jsIcon);
            } else if( isImage(value)) {
                setIcon(imgIcon);
            } else if( isDwt(value)) {
                setIcon(dwtIcon);
            } else if( isManifest(value) ) {
                setIcon( mfIcon );
            } else {
                setIcon(folderIcon);
                //setIcon(otherIcon);
            }
            
        } 
        
        return this;
    }
    
    
    protected boolean isDirectory(JTree tree, Object value) {
        
        File nodeFile = null;
        boolean rvalue = false;
        TreePath treePath = null;
        String nodePath = null;
        String filePath = null;
        
        treePath = tree.getSelectionPath();        
        
        if( treePath != null ) {
            nodePath = treePath.toString();
            int lastIndex = nodePath.length()-1;
            nodePath = nodePath.substring( 1, lastIndex );            
            
            StringTokenizer nodeTokenizer = new StringTokenizer( nodePath, "," );
            filePath = main.getBuildDirectory();
            
            String token = null;
            for( int i = 0 ; nodeTokenizer.hasMoreTokens() ; i++ ) {
                token = nodeTokenizer.nextToken().trim();
                if( i != 0) {
                    filePath += '\\' + token;
                }                
            }                       
            filePath += '\\' + value.toString().trim();         
            nodeFile = new File( filePath );
            
            //main.printMessage( '['+nodeFile.toString()+']' ); 
            
            if( nodeFile.exists() ) {
                
                if( nodeFile.isDirectory() ) {
                    rvalue = true;
                    
                }
            }            
        }
        
        return rvalue;
    }
    
    
    protected boolean isClass(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".class") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isJava(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".java") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isJws(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".jws") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isJSP(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".jsp") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isHTML(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".html") ||
        node.toString().toLowerCase().endsWith(".htm")) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isProperties(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".properties") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    
    protected boolean isText(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        // .txt, .lst, .bat, .sh, ksh, .cmd, .
        
        if( node.toString().toLowerCase().endsWith(".txt") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".lst") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".bat") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".sh") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".ksh") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".cmd") ) {
            rvalue = true;
        } 
        
        return rvalue;
    }
    
    protected boolean isJar(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".jar") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".war") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".ear") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isXml(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".xml") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".wsdd") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".wsdl") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".tld") ) {
            rvalue = true;
        } else if( node.toString().toLowerCase().endsWith(".tag") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isCss(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".css") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isImage(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".gif") ||
        node.toString().toLowerCase().endsWith(".jpg") || 
        node.toString().toLowerCase().endsWith(".bmp") ||
        node.toString().toLowerCase().endsWith(".png") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isJs(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".js") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isDwt(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".dwt") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    protected boolean isManifest(Object value) {
        
        boolean rvalue = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if( node.toString().toLowerCase().endsWith(".mf") ) {
            rvalue = true;
        }
        
        return rvalue;
    }
    
    
    
} // MyRenderer
