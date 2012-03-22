package br.ucam.kuabaSubsystem.action;

import java.beans.PropertyChangeEvent;

import javax.jmi.model.MofClass;
import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;

public class DesignElementNameAddedAction implements DesignAction {

	@Override
	public void execute(PropertyChangeEvent event) {
		KuabaModelFactory factory = KuabaSubsystem.facade.getKuabaFactory();
		RefObject designElementRefObject = (RefObject)event.getSource();
		MofClass designElementMetaClass = (MofClass)designElementRefObject.refMetaObject();
		String elementName = event.getNewValue()!= null?(String)event.getNewValue(): "";
		String metaClassName = designElementMetaClass.getName();
		
		Question whatElementsQuestion = KuabaSubsystem.facade.getRootQuestion();
		Idea designElementDomainIdea = factory.createIdea(elementName);
		designElementDomainIdea.setHasText(elementName);
		designElementDomainIdea.setHasText(metaClassName);
		Question howModelDesignElementQuestion = factory.createQuestion("how_model_" + elementName);
		howModelDesignElementQuestion.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX + elementName + "?");
		Idea designElementDesignIdea = factory.createIdea(metaClassName);
		designElementDesignIdea.setHasText(metaClassName);
		
		whatElementsQuestion.addIsAddressedBy(designElementDomainIdea);
		designElementDomainIdea.addSuggests(howModelDesignElementQuestion);
		howModelDesignElementQuestion.addIsAddressedBy(designElementDesignIdea);
		
		KuabaSubsystem.facade.makeDecision(whatElementsQuestion, designElementDomainIdea, true);
		KuabaSubsystem.facade.makeDecision(howModelDesignElementQuestion, designElementDesignIdea, true);
		
		ArgumentController controller = new InFavorArgumentController(
				new Idea[]{designElementDesignIdea}, null);
		controller.render();

	}

}
