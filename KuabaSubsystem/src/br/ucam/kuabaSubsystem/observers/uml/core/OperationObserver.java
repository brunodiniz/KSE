package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.util.KuabaHelper;

public class OperationObserver extends ModelElementObserver{
	
	public OperationObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"owner"};
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}

	@Override
	protected PropertyChangeListener applyFilter(RefObject subject) {
		return new NameEventFilter(subject, this);
	}

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,
			PropertyChangeEvent evt) {
		String sourceName = (String)((RefObject)evt.getSource()).refGetValue("name");
		String targetName = (String)((RefObject)evt.getNewValue()).refGetValue("name");
		if(evt.getPropertyName().equals("owner")){
			String text = "Why make " + targetName
			+ " owner of " + sourceName + " operation?";
			KuabaSubsystem.facade.renderInFavorArgumentView(question, idea, text);
		}
	}
	
	

}
