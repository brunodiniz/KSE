package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import java.util.Collection;


public class GeneralizationObserver extends ModelElementObserver{
	public GeneralizationObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"child"};
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
		//super.propertyChange(evt);
                
                //Se for evento do tipo name, devo criar (ou aceitar numa ideia recusada) a ideia de dominio
                if(evt.getPropertyName().equals("name")){
                    Idea domainIdea = KuabaHelper.getDomainIdea((String)evt.getNewValue());
                    
                    if(domainIdea==null){
                        RefObject source = (RefObject)evt.getSource();
                        domainIdea = KuabaSubsystem.facade.domainIdeaAdded((String)evt.getNewValue(), "Generalization", KuabaSubsystem.resolver.resolveXmiId(source));
                        Idea acceptedDesign = KuabaHelper.getAcceptedDesignIdea(domainIdea, "Generalization");
                        KuabaSubsystem.facade.obtainQuestion(acceptedDesign, "child?");
                        ArgumentController controller = new InFavorArgumentController(new Idea[]{acceptedDesign},null);
                        controller.render();
                    }
                }
                //Se for evento do tipo "child", vou criar a relacao entre a classe filha e a ideia de dominio da generalizacao
                else if(evt.getPropertyName().equals("child"))
                {
                    RefObject gener = (RefObject)evt.getSource();
                    String txt = this.getNamePropertyValue(gener);
                    Idea generDomain = KuabaHelper.getDomainIdea(txt);
                    
                    RefObject child = (RefObject)evt.getNewValue();
                    txt = this.getNamePropertyValue(child);
                    Idea childDomain = KuabaHelper.getDomainIdea(txt);
                    
                    Idea generDesign = KuabaHelper.getAcceptedDesignIdea(generDomain, "Generalization");
                    Idea childDesign = KuabaHelper.getAcceptedDesignIdea(childDomain, "Class");
                    
                    Question question = generDesign.getSuggests().iterator().next();
                    boolean exists = false;
                    for(Decision d: question.getHasDecision()){
                        if(d.getConcludes().equals(childDesign)&&!d.getIsAccepted()){
                            d.setIsAccepted(true);
                            exists=true;        
                        }
                    }
                    if(!exists){
                        question.addIsAddressedBy(childDesign);
                        childDesign.addAddress(question);
                        KuabaSubsystem.facade.makeDecision(question, childDesign, true);
                    }
                    
                    ArgumentController controller = new InFavorArgumentController(new Idea[]{childDesign},null,
                       "Why make "+ childDomain.getHasText()+" a generalization of "+ generDomain.getHasText(),question);
                    controller.render();
                }
                //Vou desconsiderar a ideia de dominio
                else if(evt.getPropertyName().equals("remove")){
                    RefObject gen = (RefObject)evt.getSource();;
                    Idea genDomain = null;
                    for(Idea d: KuabaSubsystem.facade.modelRepository().getDomainIdeas()){
                        String txt1 = d.getId().split("_")[0];
                        String txt2 = gen.refMofId().split(":")[3];
                        if(txt1.equals(txt2))
                            genDomain = d;
                    }
                    KuabaSubsystem.facade.domainIdeaSubTreeCycleDecision(genDomain, false, 1);
                    
                    //Idea genDomain = KuabaSubsystem.getSession().unitOfWork().getConsideredIdea(gen.);
                   // KuabaSubsystem.facade.domainIdeaSubTreeCycleDecision(genDomain, false, 1);
                    
                    
                }
	}
	
	@Override
	public PropertyChangeListener applyFilter(RefObject subject) {
		return new EventFilter(this);
	}

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,
			PropertyChangeEvent evt) {
		RefObject source = (RefObject)evt.getSource();
		RefObject parent = (RefObject)source.refGetValue("parent");
		String xmiId = KuabaSubsystem.resolver.resolveXmiId(parent);
		Idea supplierDomainIdea = KuabaHelper.getDomainIdea(this.modelRepository(),
				xmiId, (String)parent.refGetValue("name"));
		Idea supplierDesignIdea = null;
		if(supplierDomainIdea != null)
			supplierDesignIdea = KuabaHelper.getAcceptedDesignIdea(
					supplierDomainIdea, (String)parent.refMetaObject(
							).refGetValue("name"));			
		String text = "";
		if(evt.getPropertyName().equals("child")){			
			String targetName =
				(String)((RefObject)evt.getNewValue()).refGetValue("name");
			text = "Why does make " + targetName + 
			" " + evt.getPropertyName() + " of " + parent.refGetValue("name");
		}
		Question supplierQuestion = null;						
		Idea addressedIdea = KuabaHelper.getIdeaByText(
				(Collection)question.getIsSuggestedBy(),
				(String)source.refMetaObject().refGetValue("name"));
		supplierQuestion = 
			(Question)KuabaHelper.getReasoningElementInTree(addressedIdea, 
					"parent?");			
		HashMap<Idea, Question> ideaQuestionMap = new HashMap<Idea, Question>();
		ideaQuestionMap.put(idea, question);
		ideaQuestionMap.put(supplierDesignIdea, supplierQuestion);
		KuabaSubsystem.facade.renderInFavorArgumentView(ideaQuestionMap, text);
		
	}

}
