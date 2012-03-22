/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ucam.kuabaSubsystem.controller;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;

/**
 *
 * @author Thiago
 */
public abstract class JustificationController extends AbstractController{
    protected ArgumentTableModel inFavorTableModel;
    protected ArgumentTableModel objectsToTableModel;
    protected Justification justification;
    
    
    public JustificationController() {
		super();
		this.inFavorTableModel = new ArgumentTableModel();
		this.inFavorTableModel.setColumnIdentifiers(
				new String[]{"In Favor Arguments"});
		
		this.objectsToTableModel = new ArgumentTableModel();
		this.objectsToTableModel.setColumnIdentifiers(
				new String[]{"Objects To Arguments"});
	}

	public Justification getJustification() {
        return justification;
    }

    public void setJustification(Justification justification) {
        this.justification = justification;
    }
    public DefaultTableModel getInFavorTableModel() {
        return inFavorTableModel;
    }

    public void setInFavorTableModel(ArgumentTableModel inFavorTableModel) {
        this.inFavorTableModel = inFavorTableModel;
    }

    public DefaultTableModel getObjectsToTableModel() {
        return objectsToTableModel;
    }

    public void setObjectsToTableModel(ArgumentTableModel objectsToTableModel) {
        this.objectsToTableModel = objectsToTableModel;
    }
    
    public void insertInFavorArguments(){
        List<Argument> arguments = findInFavorArguments();
        this.inFavorTableModel.insertArguments(arguments);
    }   
    public abstract List<Argument> findInFavorArguments();
    
    public void insertObjectsToArguments(){
    	List<Argument> arguments = findObjectsToArgument();
        this.objectsToTableModel.insertArguments(arguments);
    } 
    public abstract List<Argument> findObjectsToArgument();
    
    public String getInFavorElement(int row, int column){
        String element = (String)this.inFavorTableModel.getValueAt(row, column);
        return element;
    }
    public String getObjectsToElement(int row, int column){
        String element = (String)this.objectsToTableModel.getValueAt(
                row, column);
        return element;
    }
    public abstract JFrame render();
}
