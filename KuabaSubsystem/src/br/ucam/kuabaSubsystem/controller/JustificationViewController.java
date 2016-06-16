/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ucam.kuabaSubsystem.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.ucam.kuabaSubsystem.gui.JustificationView;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;
import javax.swing.JDialog;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Thiago
 */
public class JustificationViewController extends JustificationController {
    
    public JustificationViewController(Justification justification) {
    	System.out.println("Justification");
    	System.out.println(justification.getHasText());
        this.inFavorTableModel = new ArgumentTableModel();
        this.inFavorTableModel.setColumnIdentifiers(new String[]{
            "In Favor Arguments"});
        this.objectsToTableModel = new ArgumentTableModel();
        this.objectsToTableModel.setColumnIdentifiers(new String[]{
            "objects To Arguments"});
        this.justification = justification;
    }    
    
    /*public JFrame render(){
        JustificationView view = new JustificationView(this);
        view.setVisible(true);
        return view;
    }*/

	@Override
	public List<Argument> findInFavorArguments() {		
		return new ArrayList<Argument>(this.justification.getIsDerivedOf());
	}

	@Override
	public List<Argument> findObjectsToArgument() {		
		return new ArrayList<Argument>(this.justification.getIsDerivedOf());
	}

    @Override
    public JDialog getView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
