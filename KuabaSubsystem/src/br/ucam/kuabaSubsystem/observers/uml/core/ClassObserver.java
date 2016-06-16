package br.ucam.kuabaSubsystem.observers.uml.core;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.controller.ObjectsToArgumentController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.jmi.model.MofClass;

public class ClassObserver extends ModelElementObserver {

	public ClassObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"feature"};
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {				
		//super.propertyChange(evt);
                
                RefObject source = (RefObject)evt.getSource();
                if(evt.getPropertyName().equals("remove"))
                    this.removedElement(source);
                //Se houve mudanca no nome da classe

                if(evt.getPropertyName().equals("name"))
                {
                    //Fazer o tratamento especial para classes
                    //Se esta declaracao estiver antes do if anterior, vai dar erro
                    MofClass metaClass = (MofClass)source.refMetaObject();
                    String designIdeaText = (String)metaClass.refGetValue("name");
                    
                    if(!evt.getNewValue().equals(""))
                        this.nameChanged((String)evt.getNewValue(),(String)evt.getOldValue(),designIdeaText, "_"+KuabaSubsystem.resolver.resolveXmiId(source));
                }
                //Troca a referencia quando se poe o nome de uma classe em outra classe no diagrama.
                if (evt.getNewValue() instanceof RefObject) 
                        this.referenceModified((RefObject)evt.getNewValue(), evt);
                
                /*if(evt.getPropertyName().equals("association")){
			this.associationAdded((RefObject)evt.getNewValue(), 
					(RefObject)evt.getSource());*/
//			try {
//				attr.refGetValue("association");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			List<Reference> refs = MofHelper.referencesOfClass((MofClass)attr.refMetaObject(), true);
//			for (Reference reference : refs) {
//				System.out.println(reference.getName());
//			}
//			List<Attribute> attrs = MofHelper.attributesOfClass((MofClass)attr.refMetaObject(), true);
//			for (Attribute attribute : attrs) {
//				System.out.println(attribute.getName());
//			}
			
		//}


	}
        
        //Eesta funcao remove o elemento da lista de observers e rejeita a ideia
        protected void removedElement(RefObject source)
        {
            KuabaSubsystem.eventPump.removeObservers(source);
            
            Idea domainIdea = null;
            for(Idea d: KuabaSubsystem.facade.modelRepository().getDomainIdeas()){
                String txt1 = d.getId().split("_")[1];
                String txt2 = source.refMofId().split(":")[3];
                if(txt1.equals(txt2))
                    domainIdea= d;
            }
            
            Idea designIdea = KuabaSubsystem.facade.getDesignIdea(domainIdea, "Class");
            ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{designIdea},
                "Why not model "+ domainIdea.getHasText() +" as class?",domainIdea.getSuggests().iterator().next());
            controller.render();
            
//            KuabaSubsystem.facade.getIdeaOnSession(
//            KuabaSubsystem.resolver.resolveXmiId(source));			
/*            Question howModelQuestion = (Question)domainIdea.listSuggests().next();
            List<Idea> acceptedDesignIdeas = 
            KuabaHelper.getAcceptedAddressedIdeas(howModelQuestion);
            for (Idea idea : acceptedDesignIdeas) 
            {
                ArgumentController controller = new ObjectsToArgumentController(null, new Idea[]{idea},howModelQuestion);
                controller.render();
            }        */
            KuabaSubsystem.facade.domainIdeaDesconsidered(domainIdea);        
            
            //KuabaSubsystem.facade.makeDecisionRootElements(domainIdea,false);
        }
        
        
        @Override
        protected void nameChanged(String newName, String oldName,String designIdeaText, String elementId){
                //Se e a primeira ideia
		if(oldName.equals(""))
                {
                    Idea domainIdea = KuabaSubsystem.facade.domainIdeaAdded(newName, designIdeaText, elementId);
                    Idea acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, designIdeaText);
                    if(!(newName.equals(""))) 
                    {
                        ArgumentController controller = new InFavorArgumentController(new Idea[]{acceptedDesignIdea}, null);
                        controller.render();
                    }
                }
                //Se nao for a primeira ideia
                else{
                    
                    Boolean isDomainIdea = false;
                    //Percorre a lista de ideias de dominio para ver se ele ja existe
                    
                    Idea acceptedDesignIdea = null;
                    for(Idea domainIdea : KuabaSubsystem.facade.getRootQuestion().getIsAddressedBy())
                    {
                       if(domainIdea.getHasText().equals(newName))
                       {
                           //Se existir, considera a ideia de dominio e armazena o argumento
                           KuabaSubsystem.facade.domainIdeaConsidered(domainIdea);
                           isDomainIdea = true;
                           
                           acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, designIdeaText);
                           ArgumentController controller = new InFavorArgumentController(new Idea[]{acceptedDesignIdea}, null);
                           controller.render();
                       }
                    }
                    //Se nao existir, cria uma ideia de dominio para a nova ideia
                    if(!isDomainIdea)
                    {
                        Idea domainIdea = KuabaSubsystem.facade.domainIdeaAdded(newName, designIdeaText, elementId);
                        acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, designIdeaText);
                        
                        ArgumentController controller = new InFavorArgumentController(new Idea[]{acceptedDesignIdea}, null);
                        controller.render();
                    }
                    //Se houver uma ideia de dominio com o nome antigo, deve rejeita-lo
                    Idea rejectedDomainIdea = KuabaSubsystem.facade.findDomainIdea(oldName, "Class");
                    if(rejectedDomainIdea != null){
                        
                        List<Question> acceptedAddressed = new ArrayList<Question>();
                        List<Question> aux= new ArrayList<Question>();
                        for(Question q: KuabaSubsystem.facade.getDesignIdea(rejectedDomainIdea, "Class").getAddress())
                            if(!q.getHasText().equals("How to Model "+ rejectedDomainIdea.getHasText()+"?"))
                                acceptedAddressed.add(q);
                                
                        for(Question q: acceptedAddressed){
                            for(Decision d: q.getHasDecision()){
                                if(d.getConcludes().equals(KuabaSubsystem.facade.getDesignIdea(rejectedDomainIdea, "Class"))){
                                    if(d.getIsAccepted())
                                        aux.add(q);
                                }
                            }
                        }
                        acceptedAddressed = aux;
                        
                        for(Question q: acceptedAddressed){
                            acceptedDesignIdea.addAddress(q);
                            KuabaSubsystem.facade.makeDecision(q, acceptedDesignIdea, true);
                        }
                        
                        
                        
                        KuabaSubsystem.facade.domainIdeaDesconsidered(rejectedDomainIdea);
                        Idea rejectedDesignIdea = KuabaHelper.getRejectedDesignIdea(rejectedDomainIdea, "Class");
                        Question howModelQuestion = (Question)rejectedDomainIdea.listSuggests().next();
                        ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{rejectedDesignIdea},howModelQuestion);
                        controller.render();
                        
                        
                        
                        
                    }
                }
                
                
	}
	
	public void associationAdded(RefObject ascEnd, RefObject umlClass ){
	/*	String ascEndMofId = ascEnd.refMofId();
		List<Idea> domainIdeas = 
			this.modelRepository.findDomainIdeasWhereIdLike(ascEndMofId);
		Idea ascEndDomainIdea = KuabaHelper.getIdeaByText(domainIdeas,
				(String)ascEnd.refGetValue("name"));
		Idea ascEndDesignIdea = 
			this.modelRepository.findFirstIdeaByText(
				(Question)ascEndDomainIdea.listSuggests().next(),
				"AssociationEnd");
		
		String classMofId = umlClass.refMofId();
		List<Idea> classDomainIdeas = 
			this.modelRepository.findDomainIdeasWhereIdLike(classMofId);
		Idea classDomainIdea = KuabaHelper.getIdeaByText(classDomainIdeas,
				(String)umlClass.refGetValue("name"));
		Question howModel = (Question)classDomainIdea.listSuggests().next();
		Question participant = (Question)KuabaHelper.getReasoningElementInTree(
				howModel, "participant?");
		
		participant.addIsAddressedBy(ascEndDesignIdea);
		this.facade.makeDecision(participant, classDomainIdea, true);*/
		
	}

	@Override
	public PropertyChangeListener applyFilter(RefObject subject) {		
		return new NameEventFilter(subject, this);
	}

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,
			PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
}
