/*
 * @(#)JavaKitTest.java	1.2 99/05/27
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package com.likha.editorKitJava;

import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;


/**
 * Simple wrapper around JEditorPane to browse java text
 * using the JavaEditorKit plug-in.
 *
 * java JavaKitTest filename
 */
public class JavaKitTest {

    public static void main(String[] args) {
        
        args = new String[1];        
        args[0] = "C:\\BMutia\\Protoypes\\JEditorKit\\src\\Token.java";
                
        
	try {
	    JEditorPane editor = new JEditorPane();
	    JavaEditorKit kit = new JavaEditorKit();
	    editor.setEditorKitForContentType("text/java", kit);
	    editor.setContentType("text/java");
	    editor.setBackground(Color.white);
	    editor.setFont(new Font("Courier", 0, 12));
	    editor.setEditable(true);

	    // PENDING(prinz) This should have a customizer and
	    // be serialized.  This is a bogus initialization.
	    JavaContext styles = kit.getStylePreferences();
	    Style s;
            // set style for comments
	    s = styles.getStyleForScanValue(Token.COMMENT.getScanValue());
	    StyleConstants.setForeground(s, new Color(64,128,128 ) );
            // set style for strings
	    s = styles.getStyleForScanValue(Token.STRINGVAL.getScanValue());
	    StyleConstants.setForeground(s, new Color(0,128,128 ));
            // set style for keywords
	    Color keyword = new Color(4,4,240 );
	    for (int code = 70; code <= 130; code++) {
		s = styles.getStyleForScanValue(code);
		if (s != null) {
		    StyleConstants.setForeground(s, keyword);
		}
	    }
            // set style for operators
            Color operator = new Color(216,48,5 );
            for (int code = 0; code <= 58; code++) {
		s = styles.getStyleForScanValue(code);
		if (s != null) {
		    StyleConstants.setForeground(s, operator);
		}
	    }
            // set style for value
            Color value = new Color(0,0,0 );
            for (int code = 60; code <= 69; code++) {
		s = styles.getStyleForScanValue(code);
		if (s != null) {
		    StyleConstants.setForeground(s, value);
		}
	    }
            
            // set style for punctuation
            Color punctuation = new Color(216,48,5 );
            for (int code = 135; code <= 144; code++) {
		s = styles.getStyleForScanValue(code);
		if (s != null) {
		    StyleConstants.setForeground(s, punctuation);
		}
	    }
            
            /*
            // set style for special
            Color special = new Color(216,48,5 );
            for (int code = 145; code <= 152; code++) {
		s = styles.getStyleForScanValue(code);
		if (s != null) {
		    StyleConstants.setForeground(s, special);
		}
	    }
             */

	    //File file = new File(args[0]);
            File file = new File( args[0] );
	    editor.read(new FileReader(file), file);
	    JScrollPane scroller = new JScrollPane();
	    JViewport vp = scroller.getViewport();
	    vp.add(editor);
	    vp.setBackingStoreEnabled(true);
            

	    JFrame f = new JFrame("JavaEditorKit: " + args[0]);
	    f.getContentPane().setLayout(new BorderLayout());
	    f.getContentPane().add("Center", scroller);
	    f.pack();
	    f.setSize(600, 600);
	    f.setVisible(true);
	} catch (Throwable e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

}
