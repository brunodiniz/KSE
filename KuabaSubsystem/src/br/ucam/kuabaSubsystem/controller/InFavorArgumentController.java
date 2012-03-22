package br.ucam.kuabaSubsystem.controller;

import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;

import br.ucam.kuabaSubsystem.gui.NewArgument;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.util.KuabaHelper;

public class InFavorArgumentController extends ArgumentController {
	private String text;
	public InFavorArgumentController(Idea[] inFavoridea, Idea[] objectsToIdea,
			String text, Question consideredQuestion) {
		super(inFavoridea, objectsToIdea, consideredQuestion);
		this.text = text;
	}
	public InFavorArgumentController(Idea[] inFavoridea, Idea[] objectsToIdea) {
		super(inFavoridea, objectsToIdea);
	}	

	public InFavorArgumentController(HashMap<Idea, Question> inFavorIdeaHash,
			HashMap<Idea, Question> againstIdeaHash, String text) {
		super(inFavorIdeaHash, againstIdeaHash);
		this.text = text;
	}
	
	@Override
	public JDialog getView() {
		NewArgument newArgumentView = new NewArgument(this);
		newArgumentView.setTitle("New In Favor Arguments View");		
		ReasoningElement domainIdea = null;
		if((this.inFavoridea != null) && (this.inFavoridea.length > 0))	{	
			domainIdea = KuabaHelper.getDomainIdea(this.inFavoridea[0]);	
                }
		if(this.text != null){
			newArgumentView.setIdeaText(this.text);			
		}		
		else
			newArgumentView.setIdeaText("Why model " + domainIdea.getHasText()+" as " + this.inFavoridea[0].getHasText() + "?");
		newArgumentView.setArgumentListLabel("Arguments");
		return newArgumentView;
	}

	
	
}
