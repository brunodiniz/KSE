package br.ucam.kuabaSubsystem.observers.uml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.jmi.model.ModelElement;
import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.controller.ObjectsToArgumentController;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.graph.util.KuabaGraphUtil;
import br.ucam.kuabaSubsystem.kuabaFacades.KuabaFacade;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.mofParser.UmlClassDirector;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.core.AttributeObserver;
import br.ucam.kuabaSubsystem.observers.uml.core.ClassObserver;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.MofHelper;
import java.util.Map;

public abstract class ModelElementObserver implements PropertyChangeListener {
	protected KuabaRepository modelRepository;
	protected String[] referencesAvailable = new String[]{};
	public static String observersPack =  "br.ucam.kuabaSubsystem.observers.uml.core";	
	private HashMap<RefObject, List<ModelElementObserver>> observersMap;
	
	public ModelElementObserver() {		
	}
	protected void renderInFavorArgumentView(Idea idea,
			Question consideredQuestion, String text){
		ArgumentController inFavorController = new InFavorArgumentController(
			new Idea[]{idea}, null, text, consideredQuestion);
		inFavorController.render();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		RefObject source = (RefObject)evt.getSource();
		
		if(evt.getPropertyName().equals("remove")){
			KuabaSubsystem.eventPump.removeObservers(source);
			Idea domainIdea = KuabaSubsystem.facade.getIdeaOnSession(
					KuabaSubsystem.resolver.resolveXmiId(source));			
			Question howModelQuestion = (Question)domainIdea.listSuggests().next();
			List<Idea> acceptedDesignIdeas = 
				KuabaHelper.getAcceptedAddressedIdeas(howModelQuestion);
			for (Idea idea : acceptedDesignIdeas) {
				ArgumentController controller = 
					new ObjectsToArgumentController(null, new Idea[]{idea},
							howModelQuestion);
				controller.render();
			}
			KuabaSubsystem.facade.domainIdeaDesconsidered(domainIdea);
			return;
		}
		
		MofClass metaClass = (MofClass)source.refMetaObject();
		
		String designIdeaText = (String)metaClass.refGetValue("name");
		if(evt.getPropertyName().equals("name")){			
			String newName = "";
			String oldName = "";			
			if(evt.getNewValue() != null)
				newName = (String)evt.getNewValue();
			if(evt.getOldValue() != null)
				oldName = (String)evt.getOldValue();			
			this.nameChanged(newName, oldName, designIdeaText,
			          			"_"+KuabaSubsystem.resolver.resolveXmiId(
			          					source));			
		}
		if (evt.getNewValue() instanceof RefObject) {
			this.referenceModified((RefObject)evt.getNewValue(), evt);			
			
		}	
	}
	
	public String getNamePropertyValue(RefObject refObject){
		String newName = "";
		if(refObject.refGetValue("name") != null)
			newName = (String)refObject.refGetValue("name");
		return newName;
	}
	
	public boolean makeKuaba(String referenceName){
		if((Arrays.asList(referencesAvailable).contains(referenceName)
				||(Arrays.asList(referencesAvailable).isEmpty())))
			return true;
		return false;
	}
	
	public void referenceModified(RefObject target, PropertyChangeEvent evt){
		if(((RefObject)evt.getNewValue()).refMetaObject().refGetValue(
				"name").equals("Model"))
			return;
		if(!(makeKuaba(evt.getPropertyName())))
			return;
                
		RefObject source = (RefObject)evt.getSource();
		if(!KuabaSubsystem.facade.existsDomainIdea(target)){		
			PropertyChangeEvent nameEvt = new PropertyChangeEvent(
					target, "name", "", target.refGetValue(
							"name"));			
			PropertyChangeListener listener = 
				KuabaSubsystem.eventPump.getObserver(target);
			if(listener == null){
				listener = this.createListener(target);
				if (listener == null)
					return;
				KuabaSubsystem.eventPump.addModelElementObserver(listener, target);
			}
			
				listener.propertyChange(nameEvt);
				
				Reference ref = MofHelper.getReference(evt.getPropertyName(), 
						(MofClass)source.refMetaObject());
				if(!(ref == null)){
					String otherSide = MofHelper.getExposedEndName(ref);
					try{
						PropertyChangeEvent referenceEvt = new PropertyChangeEvent(
								target, otherSide, null, target.refGetValue(
											otherSide));
						listener.propertyChange(referenceEvt);
					}catch (Exception e) {
						// TODO: handle exception
					}
				}			
		}
                //para evitar a criação do feature
                if(evt.getPropertyName().equals("feature")) return;
                
                //aqui que cria as outras questões do tipo participant e owner
		Decision d = KuabaSubsystem.facade.referenceChanged(
				(RefObject)evt.getSource(),	target, evt.getPropertyName());
		Idea sourceDomainIdea = KuabaHelper.getDomainIdea(
			modelRepository(), KuabaSubsystem.resolver.resolveXmiId(
					(RefObject)evt.getSource()), this.getNamePropertyValue(
							(RefObject)evt.getSource()));
		Idea sourceDesignIdea = KuabaHelper.getAcceptedDesignIdea(
				sourceDomainIdea,(String)((RefObject)evt.getSource()
						).refMetaObject().refGetValue("name"));
		
		askReferenceChangeArguments(d.getConcludes(), 
				(Question)KuabaHelper.getReasoningElementInTree(
						sourceDesignIdea, evt.getPropertyName() + "?"), evt);
                
                //tirando as ideias de dominio extras
                if (sourceDesignIdea.getHasText().equals("AssociationEnd")) {
                    sourceDomainIdea.listSuggests().next().remove();
                    sourceDomainIdea.remove();
                }
	}
	protected void nameChanged(String newName, String oldName,
			String designIdeaText, String elementId){		
			
		Idea rejectedDomainIdea = KuabaSubsystem.facade.domainIdeaRejected(
				elementId, oldName);
		Idea domainIdea = null;
		Idea acceptedDesignIdea = null;
		if(rejectedDomainIdea != null){
			domainIdea = KuabaSubsystem.facade.domainIdeaRejectedAndAdded(
					rejectedDomainIdea, newName, designIdeaText, elementId);
			acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(
					domainIdea, designIdeaText);			
			Idea rejectedDesignIdea = KuabaHelper.getRejectedDesignIdea(
					rejectedDomainIdea, designIdeaText);
                        
//                        Map<ReasoningElement, Integer> stageMap = KuabaGraphUtil.getReasoningElementStageMap(KuabaSubsystem.facade.getRootQuestion());
//                        Idea resp = rejectedDesignIdea.deepCopy(stageMap, KuabaSubsystem.facade.modelRepository(), rejectedDomainIdea.listSuggests().next());
//                        Question how = acceptedDesignIdea.listAddress().next();
//                        how.addIsAddressedBy(resp);
//
//                        for (Decision d: acceptedDesignIdea.getIsConcludedBy()) {
//                            d.setConcludes(resp);
//                            resp.addIsConcludedBy(d);
//                        }
//                        
//                        acceptedDesignIdea.remove();
//                        acceptedDesignIdea = resp;
                        
                        
                        
			if(/*(rejectedDomainIdea.getHasText().equals("")) || */
				(rejectedDomainIdea.getHasText().equals("newAttr"))||
				((rejectedDomainIdea.getHasText().equals("newOperation"))||
						(rejectedDomainIdea.getHasText().equals("return"))||
						(rejectedDomainIdea.getHasText().equals("void"))||
						(rejectedDomainIdea.getHasText().equals("arg1"))||
						(rejectedDomainIdea.getHasText().equals("int"))||
						(rejectedDomainIdea.getHasText().equals("String")))){				
				ArgumentController controller = new InFavorArgumentController(
						new Idea[]{acceptedDesignIdea}, null);
				controller.render();
			}else{
                            
                            // copiando as referencias antigas para a nova ideia
                                acceptedDesignIdea.setSuggests(rejectedDesignIdea.getSuggests());

                                Collection<Question> addressedQuestions = rejectedDesignIdea.getAddress();
                                addressedQuestions.remove(rejectedDomainIdea.listSuggests().next());

                                for (Question q: addressedQuestions) {
                                    q.removeIsAddressedBy(rejectedDesignIdea);
                                    q.addIsAddressedBy(acceptedDesignIdea);
                                }
                                
                                //tratamento especial para as associações
                                if((rejectedDomainIdea.getHasText().equals(""))) {
                                    ArgumentController controller = new InFavorArgumentController(
						new Idea[]{acceptedDesignIdea}, null);
                                    controller.render();
                                }
                                else {                            
                                    ArgumentController controller = new ObjectsToArgumentController(
                                                    new Idea[]{acceptedDesignIdea}, new Idea[]{rejectedDesignIdea});
                                    controller.render();
                                }
			}
		}
		else{
			
                        
                     
			if(!( /*(domainIdea.getHasText().equals("")) || */
					(newName.equals("newAttr"))||
					(newName.equals("newOperation"))||
					(newName.equals("void"))||
					(newName.equals("return"))||
					(newName.equals("arg1"))||
					(newName.equals("int"))||
					(newName.equals("String")))){
                            
                            domainIdea = KuabaSubsystem.facade.domainIdeaAdded(
					newName, designIdeaText, elementId);		
                                          
                            acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(
                                                domainIdea, designIdeaText);
                            
                            if(!(newName.equals(""))) {
				ArgumentController controller = new InFavorArgumentController(
						new Idea[]{acceptedDesignIdea}, null);
				controller.render();
                            }
			}
		}				
	}
	public void registerObserver(RefObject target, ModelElementObserver observer){
		if(this.observersMap.containsKey(target))
			this.observersMap.get(target).add(observer);
		else{
			List<ModelElementObserver> listeners = 
				new ArrayList<ModelElementObserver>();
			listeners.add(observer);
			this.observersMap.put(target, listeners);
		}			
	}
	
	protected KuabaRepository modelRepository(){
		return KuabaSubsystem.facade.modelRepository();
	}	
	protected PropertyChangeListener createListener(RefObject subject){
		String metaClassName =
			(String)subject.refMetaObject().refGetValue("name");
		ModelElementObserver subjectListener = null;
		String observerName = observersPack + "."
		+ metaClassName + "Observer";
			try {
				subjectListener = 
					(ModelElementObserver)Class.forName(observerName).newInstance();
			} catch (ClassNotFoundException e) {				
				System.err.println("Observer " + observerName + " not found!");
			} catch (InstantiationException e) {				
				e.printStackTrace();
			} catch (IllegalAccessException e) {				
				e.printStackTrace();
			}
		if(subjectListener != null)	
			return subjectListener.applyFilter(subject);
		else
			return null;
		
	}
	protected abstract PropertyChangeListener applyFilter(RefObject subject);
	
	protected abstract void askReferenceChangeArguments(Idea idea,
			Question question, PropertyChangeEvent evt);	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	

}
