package br.ucam.kuabaSubsystem.action;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import javax.jmi.model.MofClass;
import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.util.KuabaHelper;


//-------------------- CODIGO NAO EXECUTADO----------------------------------

public class DesignElementNameChangedAction implements DesignAction {
	
	@Override
	public void execute(PropertyChangeEvent event) {
            System.out.println("FUI EXECUTADO");
		KuabaModelFactory factory = KuabaSubsystem.facade.getKuabaFactory();
                KuabaRepository repo = KuabaSubsystem.facade.modelRepository();
                
		RefObject designElementRefObject = (RefObject)event.getSource();
		MofClass designElementMetaClass = (MofClass)designElementRefObject.refMetaObject();
		String newName = (String) event.getNewValue();
		String oldName = (String) event.getOldValue();
		
		Question whatElementsQuestion = KuabaSubsystem.facade.getRootQuestion();
	    if(repo.getIdea(newName) != null){	    	
	    	Idea returnedElementDomainIdea = repo.getIdea(newName);
	    	Idea rejectedElementDomainIdea = repo.getIdea(oldName);
	    	KuabaHelper.makeDecisionForAllIdeasInSubtree(returnedElementDomainIdea, true);
	    	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	KuabaHelper.makeDecisionForAllIdeasInSubtree(rejectedElementDomainIdea, false);
	    	
	        
	        //Ask Arguments
	        
	    }
	    else{
	    	Idea rejectedDomainIdea = repo.getIdea(oldName);
	    	Idea newElementDomainIdea = factory.createIdea(newName);
	    	newElementDomainIdea.setHasText(newName);	    	
	    	Question prototypeHowModelQuestion = 
				(Question)rejectedDomainIdea.listSuggests().next();
	    	List<Idea> disconnectedElementMap = 
	    		KuabaHelper.setCancopyOnConnectionIdea(rejectedDomainIdea, false);	    	
			prototypeHowModelQuestion.removeIsSuggestedBy(rejectedDomainIdea);			
			Question howModelNewElementCloneQuestion = 
				KuabaSubsystem.facade.clonePrototypeQuestion(prototypeHowModelQuestion);
			prototypeHowModelQuestion.addIsSuggestedBy(rejectedDomainIdea);
			KuabaHelper.setCanCopyOnIdeaList(disconnectedElementMap);
			howModelNewElementCloneQuestion.setId("how_model_" + newName);
			howModelNewElementCloneQuestion.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX + newName + "?");
			Idea newElementDesignIdea = KuabaHelper.getIdeaByText(
					howModelNewElementCloneQuestion.getIsAddressedBy(), 
					designElementMetaClass.getName());
			
			whatElementsQuestion.addIsAddressedBy(newElementDomainIdea);
			KuabaSubsystem.facade.makeDecision(whatElementsQuestion, newElementDomainIdea, true);
			
			
			newElementDomainIdea.addSuggests(howModelNewElementCloneQuestion);
			
			KuabaHelper.makeDecisionForAllIdeasInSubtree(rejectedDomainIdea, false);
			
			KuabaSubsystem.facade.makeDecision(howModelNewElementCloneQuestion, newElementDesignIdea, true);
			
			//ask arguments
			
					
	    }
	    
	    

	}

}
