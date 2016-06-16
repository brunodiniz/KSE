package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.controller.ObjectsToArgumentController;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.MofHelper;
import java.util.Collection;

public class AssociationEndObserver extends ModelElementObserver{
	
    
    private static Idea associationEnd;
    
    
	public AssociationEndObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"participant"};
	}
        
        // TRATAR TROCA DE ASSOCIACAO NO DR

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
            //super.propertyChange(evt);	
            
            //System.out.println("PropertyName: "+evt.getPropertyName());
            
            
            if(evt.getPropertyName().equals("participant")){
                
                //Obtenho o nome da classe e o codigo XMI para carregar a ideia de design da classe participante
                if(evt.getNewValue()!=null){
                    String newName = this.getNamePropertyValue((RefObject)evt.getNewValue());
                    String newXmiId = KuabaSubsystem.resolver.resolveXmiId((RefObject)evt.getNewValue());
                    Idea newDomain = KuabaHelper.getDomainIdea(this.modelRepository(), newXmiId, newName);
                    Idea newDesign = KuabaSubsystem.facade.getDesignIdea(newDomain, "Class");
                
                    String str = newDomain.getHasText();
                    
                    //Faco o mesmo que o anterior para a ideia de design associationEnd
                
                    //Pego a questao levantada pela ponta da associacao
                    Question connection = AssociationEndObserver.associationEnd.getSuggests().iterator().next();
                    
                    boolean isConnected = false;
                    //Recuso todas as decisoes previamente configuradas
                    for(Decision d : connection.getHasDecision()){
                        if(d.getIsAccepted()){
                            d.setIsAccepted(false);
                            ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{d.getConcludes()},
                                    "Why not make "+KuabaHelper.getDomainIdea(d.getConcludes()).getHasText()+" participant of this association?",connection);
                            controller.render();
                        }
                        
                    }
                    //Verifico se ja existe uma decisao entre a ideia de design da associacao e da classe
                    for(Idea d : connection.getIsAddressedBy())
                        if(d.equals(newDesign))
                            isConnected=true;
                    //Se nao existir, crio uma nova
                    if(!isConnected){
                        connection.addIsAddressedBy(newDesign);
                        newDesign.addAddress(connection);
                        KuabaSubsystem.facade.makeDecision(connection, newDesign, true);
                    }
                    //Se ja existisse, configuro a antiga como aceita
                    else{
                        for(Decision d: connection.getHasDecision())
                            if(d.getConcludes().equals(newDesign))
                                d.setIsAccepted(true);
                    }
                    
                    ArgumentController controller = new InFavorArgumentController(new Idea[]{newDesign}, null, 
                       "Why make "+KuabaHelper.getDomainIdea(newDesign).getHasText()+" part of this association?",connection);
                    controller.render();
                    
                    //Devido ao erro na montagem do grafo, e preciso comprovar as relacoes no DR a partir deste
                    //comando
                    for(Decision d : connection.getHasDecision())
                        System.out.println("A decisao "+ KuabaHelper.getDomainIdea(d.getConcludes()).getHasText()+" foi " +Boolean.toString(d.getIsAccepted()));
                
                }
                else{
                    //Obtenho a ideia de design de associacao que faz par com
                    String newName = this.getNamePropertyValue((RefObject)evt.getOldValue());
                    Idea domainIdea = KuabaSubsystem.getSession().unitOfWork().getConsideredIdeaByName(newName);
                    Idea designIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, "Class");
                    
                    RefObject ref = (RefObject)evt.getSource();
                    String str = ref.refMofId();
                    
                    
                    Idea assocEnd = KuabaSubsystem.getSession().unitOfWork().getAssociationEndIdea(str);
                    Question participant = assocEnd.getSuggests().iterator().next();
                    for(Decision d: participant.getHasDecision())
                        if(d.getConcludes().equals(designIdea))
                            d.setIsAccepted(false);
                    AssociationEndObserver.associationEnd=assocEnd;
                    
                }
            }
            
            
            else if(evt.getPropertyName().equals("name"))
               System.out.println();
            //Se chega aqui, entao deve verificar se deve criar uma nova ideia de design AssociationEnd para uma Assocition
            else if(evt.getPropertyName().equals("association")){
                if(evt.getNewValue()==null){
               /*     String str = (RefObject)evt.getSource().
                    Idea designIdea = KuabaSubsystem.getSession().unitOfWork().getAssociationEndIdea(observersPack)
                    */
                    return;
                }
                
                String associationName = this.getNamePropertyValue((RefObject)evt.getNewValue());
                //Obtenho a ideia de dominio que representa a associacao pai
                Idea domainIdea = KuabaHelper.getDomainIdea(KuabaSubsystem.facade.modelRepository(),
                        KuabaSubsystem.resolver.resolveXmiId((RefObject)evt.getNewValue()),associationName);
                Idea designIdea = null;
                //Busco a ideia de design de Associacao para esta ideia de dominio
                for(Idea d : domainIdea.getSuggests().iterator().next().getIsAddressedBy())
                    if(d.getHasText().equals("Association"))
                        designIdea = d;
                
                if(!KuabaSubsystem.facade.hasAllAssociationEnds(designIdea)){
                   //Aqui vou criar uma nova ponta de associacao, depois vou atualizar a lista de referencias
                   //de associationEnds por associadcao em KuabaSession
                    
                    Question suggested = KuabaSubsystem.facade.obtainQuestion(designIdea, "Participant?");
                    Idea designAssocEnd = KuabaSubsystem.facade.createIdea(suggested, "AssociationEnd", "connection?");
                    KuabaSubsystem.facade.makeDecision(suggested, designAssocEnd, true);
                    AssociationEndObserver.associationEnd = designAssocEnd;
                    //Aqui vai o par: design idea para associacao e codigo xmi da associacao
                    RefObject ref = (RefObject)evt.getSource();
                    String str = ref.refMofId();
                    boolean b = KuabaSubsystem.getSession().unitOfWork().addAssociationEnd(designAssocEnd,str);
                    
                }
            }
            
            
	}
        
	
	@Override
	public PropertyChangeListener applyFilter(RefObject subject) {
		return new EventFilter(this);
	}
        
        @Override
        public void referenceModified(RefObject target, PropertyChangeEvent evt){
            if(((RefObject)evt.getNewValue()).refMetaObject().refGetValue("name").equals("Model"))
		return;
            if(!(makeKuaba(evt.getPropertyName())))
		return;
            RefObject source = (RefObject)evt.getSource();
            if(!KuabaSubsystem.facade.existsDomainIdea(target))
            {		
                PropertyChangeEvent nameEvt = new PropertyChangeEvent(target, "name", "", target.refGetValue("name"));			
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
                        PropertyChangeEvent referenceEvt = new PropertyChangeEvent(target, otherSide, null, target.refGetValue(otherSide));
                        listener.propertyChange(referenceEvt);
                    }catch (Exception e) {
		// TODO: handle exception
                    }
                }			
            }
            //para evitar a criação do feature
            if(evt.getPropertyName().equals("feature")) 
                return;    
            //aqui que cria as outras questões do tipo participant e owner
            Decision d = KuabaSubsystem.facade.referenceChanged((RefObject)evt.getSource(),	target, evt.getPropertyName());
            Idea sourceDomainIdea = KuabaHelper.getDomainIdea(
            modelRepository(), KuabaSubsystem.resolver.resolveXmiId((RefObject)evt.getSource()), this.getNamePropertyValue((RefObject)evt.getSource()));
            Idea sourceDesignIdea = KuabaHelper.getAcceptedDesignIdea(sourceDomainIdea,(String)((RefObject)evt.getSource()).refMetaObject().refGetValue("name"));
            askReferenceChangeArguments(d.getConcludes(),(Question)KuabaHelper.getReasoningElementInTree(sourceDesignIdea, evt.getPropertyName() + "?"), evt);
            //tirando as ideias de dominio extras
            if (sourceDesignIdea.getHasText().equals("AssociationEnd")) 
            {
                sourceDomainIdea.listSuggests().next().remove();
                sourceDomainIdea.remove();
            }
	}
        
        @Override
        protected void nameChanged(String newName, String oldName,String designIdeaText, String elementId){		
            
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
                    for(Idea domainIdea : KuabaSubsystem.facade.getRootQuestion().getIsAddressedBy())
                    {
                       if(domainIdea.getHasText().equals(newName))
                       {
                           //Se existir, considera a ideia de dominio e armazena o argumento
                           KuabaSubsystem.facade.domainIdeaConsidered(domainIdea);
                           isDomainIdea = true;
                           
                           Idea acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, designIdeaText);
                           ArgumentController controller = new InFavorArgumentController(new Idea[]{acceptedDesignIdea}, null);
                           controller.render();
                       }
                    }
                    //Se nao existir, cria uma ideia de dominio para a nova ideia
                    if(!isDomainIdea)
                    {
                        Idea domainIdea = KuabaSubsystem.facade.domainIdeaAdded(newName, designIdeaText, elementId);
                        Idea acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, designIdeaText);
                        
                        ArgumentController controller = new InFavorArgumentController(new Idea[]{acceptedDesignIdea}, null);
                        controller.render();
                    }
                    //Se houver uma ideia de dominio com o nome antigo, deve rejeita-lo
                    Idea rejectedDomainIdea = KuabaSubsystem.facade.findDomainIdea(oldName, "Association");
                    if(rejectedDomainIdea != null){
                        KuabaSubsystem.facade.domainIdeaDesconsidered(rejectedDomainIdea);
                        Idea rejectedDesignIdea = KuabaHelper.getRejectedDesignIdea(rejectedDomainIdea, "Association");
                        Question howModelQuestion = (Question)rejectedDomainIdea.listSuggests().next();
                        ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{rejectedDesignIdea},howModelQuestion);
                        controller.render();
                    }
                }
                
                				
	}
        

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,PropertyChangeEvent evt) {
		RefObject participant = (RefObject)evt.getNewValue();
		String text = ""; 
		if(evt.getPropertyName().equals("participant")){
			text = "Why does make " + participant.refGetValue("name") + 
			" participant of this association?";
			KuabaSubsystem.facade.renderInFavorArgumentView(question, idea, text);
		}		
	}	
	
}
