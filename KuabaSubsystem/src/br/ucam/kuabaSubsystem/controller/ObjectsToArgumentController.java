package br.ucam.kuabaSubsystem.controller;

import javax.swing.JDialog;
import javax.swing.JFrame;

import br.ucam.kuabaSubsystem.gui.NewArgument;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;

public class ObjectsToArgumentController extends ArgumentController {

	
	public ObjectsToArgumentController(Idea[] inFavoridea, Idea[] objectsToIdea, 
			Question considereQuestion) {
		super(inFavoridea, objectsToIdea, considereQuestion);
	}
	public ObjectsToArgumentController(Idea[] inFavoridea, Idea[] objectsToIdea){
		super(inFavoridea, objectsToIdea);
	}
	@Override
	public JDialog getView() {
		NewArgument newArgumentView = new NewArgument(this);
		newArgumentView.setTitle("New Objects To Arguments View");
		ReasoningElement domainIdea = null;
		if(this.objectsToIdea[0].getAddress().isEmpty())
			throw new RuntimeException("Theres an inconsistence on kuaba structure! " +
					"The design Idea " + this.objectsToIdea[0].getHasText() +
					" must address at least one Question.");		
		Question howModel = (Question)this.objectsToIdea[0].listAddress().next();
		if(howModel.getIsSuggestedBy().isEmpty())
			throw new RuntimeException("Theres an inconsistence on kuaba structure! " +
					"The Question " + howModel.getHasText() +
					" must be suggested by at least one Idea.");
		domainIdea = (ReasoningElement)howModel.listIsSuggestedBy().next();
		
		newArgumentView.setIdeaText("Why not model " + domainIdea.getHasText() +
				" as " + this.objectsToIdea[0].getHasText() + "?");
		newArgumentView.setArgumentListLabel("Arguments");
		return newArgumentView;
		
	}

		

}
