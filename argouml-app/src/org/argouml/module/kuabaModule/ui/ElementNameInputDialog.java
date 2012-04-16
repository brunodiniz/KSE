/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.argouml.module.kuabaModule.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.argouml.util.ArgoFrame;

/**
 *
 * @author Bruno
 */
public class ElementNameInputDialog extends JDialog implements PropertyChangeListener{

    private ElementNameInputPanel pane = new ElementNameInputPanel();
    private String enteredName = null;
    
    public ElementNameInputDialog() {
        super(ArgoFrame.getInstance(), "Element Name", true);
        pane.setNamePropertyListener(this);
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(300, 145);
        this.setLocationRelativeTo(ArgoFrame.getInstance());
        
        this.setLayout(new BorderLayout());
        this.add(new Box.Filler(null, new Dimension(0, 10), null),BorderLayout.NORTH);
        this.add(new Box.Filler(null, new Dimension(0, 10), null),BorderLayout.SOUTH);
        this.add(pane,BorderLayout.CENTER);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String enteredText = (String) evt.getNewValue();
        if(enteredText == null || enteredText.equals("")) 
            pane.notifyInvalidName();
        else {
            this.enteredName = enteredText;
            this.dispose();
        }   
    }

    public String getEnteredName() {
        return enteredName;
    }
}
