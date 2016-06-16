/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ucam.kuabaSubsystem.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import br.ucam.kuabaSubsystem.gui.NewJustification;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;
import javax.swing.JDialog;



/**
 *
 * @author Thiago
 */
public class NewJustificationController extends JustificationController {
    private Decision decision;
	public NewJustificationController(Decision d) {
		super();
		this.decision = d;
		
	}
        
    public Justification create(String text){
    	System.out.println("Text: " + text);
    	this.justification = this.modelFactory.createJustification(
    			"_"+ this.modelSequence.nextVal());
    	this.justification.setHasText(text);
    	System.out.println("in favor: " +this.inFavorTableModel);
    	Set<Argument> argumentSet = new HashSet<Argument>(
    			this.inFavorTableModel.getArguments());
    	argumentSet.addAll(this.objectsToTableModel.getArguments());
    	this.justification.setIsDerivedOf(argumentSet);
    	this.decision.setHasJustification(this.justification);
        return this.justification;
    }
    
    

	@Override
	public List<Argument> findInFavorArguments() {
		Idea idea = this.decision.getConcludes();		
		List<Argument> inFavorOfList = new ArrayList<Argument>();
		Collection<Argument> arguments = idea.getHasArgument();
		for (Argument argument : arguments) {
			if(argument.getInFavorOf().contains(idea))
				inFavorOfList.add(argument);
		}	
		
		return inFavorOfList;
	}

	@Override
	public List<Argument> findObjectsToArgument() {
		Idea idea = this.decision.getConcludes();		
		List<Argument> objectsToList = new ArrayList<Argument>();
		Collection<Argument> arguments = idea.getHasArgument();
		for (Argument argument : arguments) {
			if(argument.getObjectsTo().contains(idea))
				objectsToList.add(argument);
		}	
		
		return objectsToList;
	}

    @Override
    public JDialog getView() {
        /*InFavorJustificationView jv = new InFavorJustificationView(this);
        return jv;*/
        return null;
    }

}
