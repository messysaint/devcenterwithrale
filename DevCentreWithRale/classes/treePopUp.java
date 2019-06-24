/*
 * treePopUp.java
 *
 * Created on March 29, 2003, 2:11 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.tree.*;
import javax.swing.event.*;
import java.io.*;

/**
 *
 * @author  test1
 */
public class treePopUp extends JPopupMenu implements ActionListener, ItemListener {
    
    DefaultMutableTreeNode selectedNode;
    //DynamicTree dirTree;
    
    private String filePath;
    popUpMenu editMenuItem = new popUpMenu("Edit");
    popUpMenu renameMenuItem = new popUpMenu("Rename");
    popUpMenu deleteMenuItem = new popUpMenu("Delete");
    popUpMenu cutMenuItem = new popUpMenu("Cut");
    popUpMenu copyMenuItem = new popUpMenu("Copy");
    popUpMenu pasteMenuItem = new popUpMenu("Paste");
    popUpMenu compileMenuItem = new popUpMenu("Compile");
    popUpMenu browserViewMenuItem = new popUpMenu("Browser view");
    popUpMenu sendMenuItem = new popUpMenu("Send file to server");
    popUpMenu newDirectoryMenuItem = new popUpMenu("New directory");
    popUpMenu newPackageMenuItem = new popUpMenu("New package");
    
    JSeparator separator;
    mainForm main;
    
    /** Creates a new instance of treePopUp */
    public treePopUp( mainForm parentFrame, String path, DefaultMutableTreeNode node ) {
        
        selectedNode = node;
        //dirTree = tree;
        filePath = path;
        main = parentFrame;
        
        setBorder( new javax.swing.border.LineBorder( new java.awt.Color( 147,139,125 ) ) );
        
        editMenuItem.addActionListener(this);
        editMenuItem.setEnabled( checkEnable( "edit", path ) );
        add( editMenuItem );
        
        renameMenuItem.addActionListener(this);
        renameMenuItem.setEnabled( checkEnable( "rename", path ) );
        add( renameMenuItem );
        
        deleteMenuItem.addActionListener(this);
        deleteMenuItem.setEnabled( checkEnable( "delete", path ) );
        add( deleteMenuItem );
        
        separator = new javax.swing.JSeparator();
        add( separator );
        
        cutMenuItem.addActionListener(this);
        cutMenuItem.setEnabled( checkEnable( "cut", path ) );
        add( cutMenuItem );
        
        copyMenuItem.addActionListener(this);
        copyMenuItem.setEnabled( checkEnable( "copy", path ) );
        add( copyMenuItem );
        
        pasteMenuItem.addActionListener(this);
        pasteMenuItem.setEnabled( checkEnable( "paste", path ) );
        add( pasteMenuItem );
        
        separator = new javax.swing.JSeparator();
        add( separator );
        
        compileMenuItem.addActionListener(this);
        compileMenuItem.setEnabled( checkEnable( "compile", path ) );
        add( compileMenuItem );
        
        separator = new javax.swing.JSeparator();
        add( separator );
        
        browserViewMenuItem.addActionListener(this);
        browserViewMenuItem.setEnabled( checkEnable( "browser view", path ) );
        add( browserViewMenuItem );
        
        separator = new javax.swing.JSeparator();
        add( separator );
        
        sendMenuItem.addActionListener(this);
        sendMenuItem.setEnabled( checkEnable( "send", path ) );
        add( sendMenuItem );
        
        separator = new javax.swing.JSeparator();
        add( separator );
        
        newDirectoryMenuItem.addActionListener(this);
        newDirectoryMenuItem.setEnabled( checkEnable( "new directory", path ) );
        add( newDirectoryMenuItem );
        
        newPackageMenuItem.addActionListener(this);
        newPackageMenuItem.setEnabled( checkEnable( "new package", path ) );
        add( newPackageMenuItem );
        
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        String actionCmd = actionEvent.getActionCommand();
        
        if( actionCmd.equalsIgnoreCase( "edit" ) ) {
            main.editSourceCode( filePath );
        } else if( actionCmd.equalsIgnoreCase( "rename" ) ) {
            main.renameItem();
        } else if( actionCmd.equalsIgnoreCase( "delete" ) ) {                                    
            main.deleteItem();
            refreshTreeNode();
        }  else if( actionCmd.equalsIgnoreCase( "compile" ) ) {
            compileFilePopUp cfpu = new compileFilePopUp( main, false, filePath );
            cfpu.show();            
        } else if( actionCmd.equalsIgnoreCase( "send file to server" ) ) {
            archiveSendFile asf = new archiveSendFile( main, false );
            asf.show();
        } else if( actionCmd.equalsIgnoreCase( "cut" ) ) {
            String[] cmd = { "cut", filePath };
            main.setCopyCutTree( cmd );
            main.setCanPaste( true );
        } else if( actionCmd.equalsIgnoreCase( "copy" ) ) {
            String[] cmd = { "copy", filePath };
            main.setCopyCutTree( cmd );
            main.setCanPaste( true );
        } else if( actionCmd.equalsIgnoreCase( "paste" ) ) {
            doCopyCut(); // copy or cut
            String[] cmd = { null, null };
            main.setCanPaste( false );
            main.setCopyCutTree( cmd );
        }  else if( actionCmd.equalsIgnoreCase( "browser view" ) ) {
            main.viewInBrowser( filePath );
        } else if( actionCmd.equalsIgnoreCase( "new directory" ) ) {
            newJavaDirectory njd = new newJavaDirectory( main, true );
            njd.show();
        } else if( actionCmd.equalsIgnoreCase( "new package" ) ) {
            newJavaPackage njp = new newJavaPackage( main, true );
            njp.show();
        }
        
        
    }
    
    
    public void refreshTreeNode() {
        TreeNode[] path = selectedNode.getPath(); 
        for( int i = 0 ; i < path.length ; i++ ) {
            //main.printMessage( path[i].toString() );
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
    
    public boolean isSendable( String file ) {
        
        boolean rvalue = false;
        String validBase = main.getBuildDirectory() + "\\";
        
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
    
    public boolean checkEnable( String action, String path ) {
        
        boolean rvalue = true;
        
        if( new File( path ).isFile() ) {  // is File
            
            if( path.toLowerCase().endsWith( ".class" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else if( path.toLowerCase().endsWith( ".jar" ) || 
                path.toLowerCase().endsWith( ".war" ) || 
                path.toLowerCase().endsWith( ".zip" ) || 
                path.toLowerCase().endsWith( ".ear" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else if( path.toLowerCase().endsWith( ".java" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else if( path.toLowerCase().endsWith( ".jsp" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else if( path.toLowerCase().endsWith( ".htm" ) || path.toLowerCase().endsWith( ".html" ) || path.toLowerCase().endsWith( ".dwt" )) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = true;
                }
                
            } else if( path.toLowerCase().endsWith( ".xml" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else if( path.toLowerCase().endsWith( ".jar" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = main.getCanPaste();
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else if( path.toLowerCase().endsWith( ".properties" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else if( path.toLowerCase().endsWith( ".css" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else if( path.toLowerCase().endsWith( ".gif" ) || path.toLowerCase().endsWith( ".jpg" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = true;
                }
                
            } else if( path.toLowerCase().endsWith( ".txt" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else if( path.toLowerCase().endsWith( ".jws" ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            } else { // al other files
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                }
                
            }
            
            if( action.equalsIgnoreCase( "new directory" ) ) {
                rvalue = false;
            } else if( action.equalsIgnoreCase( "new package" ) ) {
                rvalue = false;
            }
            
        } else {  // is Directory
            
            if( path.equalsIgnoreCase( main.getBuildDirectory()+ "\\metadata" ) ||
            path.equalsIgnoreCase( main.getBuildDirectory()+ "\\lib" ) ||
            path.equalsIgnoreCase( main.getBuildDirectory()+ "\\classes" ) ||
            path.equalsIgnoreCase( main.getBuildDirectory()+ "\\src" ) ||
            path.equalsIgnoreCase( main.getBuildDirectory()+ "\\tld" ) ||
            path.equalsIgnoreCase( main.getBuildDirectory()+ "\\Templates" ) ||
            path.equalsIgnoreCase( main.getBuildDirectory()+ "\\workarea" ) ||
            path.equalsIgnoreCase( main.getBuildDirectory()+ "\\backup" ) ||
            path.equalsIgnoreCase( main.getBuildDirectory() ) ) {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = main.getCanPaste();
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "new directory" ) ) {
                    if( !path.equalsIgnoreCase( main.getBuildDirectory()+ "\\classes" ) ) {
                        rvalue = true;
                    } else {
                        rvalue = false;
                    }                    
                } else if( action.equalsIgnoreCase( "new package" ) ) {
                    if( path.equalsIgnoreCase( main.getBuildDirectory()+ "\\classes" ) ) {
                        rvalue = true;
                    } else {
                        rvalue = false;
                    }                    
                }
                
            } else {
                
                if( action.equalsIgnoreCase( "edit" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "rename" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "delete" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "cut" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "copy" ) ) {
                    rvalue = true;
                } else if( action.equalsIgnoreCase( "paste" ) ) {
                    rvalue = main.getCanPaste();
                } else if( action.equalsIgnoreCase( "compile" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "send" ) ) {
                    rvalue = isSendable( path );
                } else if( action.equalsIgnoreCase( "browser view" ) ) {
                    rvalue = false;
                } else if( action.equalsIgnoreCase( "new directory" ) ) {
                    if( !path.startsWith( main.getBuildDirectory()+ "\\classes" ) ) {
                        rvalue = true;
                    } else {
                        rvalue = false;
                    }                    
                } else if( action.equalsIgnoreCase( "new package" ) ) {
                    if( path.startsWith( main.getBuildDirectory()+ "\\classes" ) ) {
                        rvalue = true;
                    } else {
                        rvalue = false;
                    }                    
                }
                
            }
            
        }
        
        return rvalue;
        
    }
    
    
    private boolean doCopyCut() {
        
        boolean rvalue = true;
        
        String[] command = main.getCopyCutTree();
        String operation = command[0];
        File file = new File( command[1] );
        File targetDir = new File( filePath );
        
        if( operation.equalsIgnoreCase( "cut" ) ) {
            String fileStr = file.toString();
            String targetFile = targetDir + fileStr.substring( fileStr.lastIndexOf( '\\' ) );
            
            if( !file.renameTo( new File( targetFile ) ) ) {
                rvalue = false;
            }
            
        } else if( operation.equalsIgnoreCase( "copy" ) ) {
            
            String fileStr = file.toString();
            String targetFile = targetDir + fileStr.substring( fileStr.lastIndexOf( '\\' ) );
            
            if( new File( targetFile ).exists() ) {
                targetFile = targetFile + '1';
            }
            
            //main.printMessage( "copy " + fileStr + "  " + targetFile );
            main.copyFile( '\"'+fileStr+'\"', '\"'+targetFile+'\"' );
            
        }
        
        return rvalue;
    }
    
}
