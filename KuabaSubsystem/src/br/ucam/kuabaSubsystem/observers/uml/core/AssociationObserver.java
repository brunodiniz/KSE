package br.ucam.kuabaSubsystem.observers.uml.core;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.controller.ObjectsToArgumentController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.observers.AbstractObserver;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.MofHelper;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;

public class AssociationObserver extends ModelElementObserver{
	public static final String[] referencesAvailable = 
		new String[]{"connection"};
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		//super.propertyChange(evt);
            
                
            RefObject source = (RefObject)evt.getSource();
            //Fazer o tratamento especial para classes
            if(evt.getPropertyName().equals("remove"))            
                this.removedElement(evt,source);
		
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
//               AssociationEndObserver.verifyAssociationChange((RefObject) evt.getNewValue(),
//                       (RefObject)evt.getOldValue(),(RefObject)evt.getSource(),evt.getPropertyName());
               this.referenceModified((RefObject)evt.getNewValue(), evt);

            //Nao fazer o tratamento generico para classes
            /*MofClass metaClass = (MofClass)source.refMetaObject();
            String designIdeaText = (String)metaClass.refGetValue("name");
            if(evt.getPropertyName().equals("name"))
            {			
                String newName = "";
                String oldName = "";			
                if(evt.getNewValue() != null)
                    newName = (String)evt.getNewValue();
                if(evt.getOldValue() != null)
                    oldName = (String)evt.getOldValue();
                this.nameChanged(newName, oldName, designIdeaText,"_"+KuabaSubsystem.resolver.resolveXmiId(source));			
            }
            if (evt.getNewValue() instanceof RefObject) 
                this.referenceModified((RefObject)evt.getNewValue(), evt);*/
	}


        @Override
        public void referenceModified(RefObject target, PropertyChangeEvent evt)
        {
            if(((RefObject)evt.getNewValue()).refMetaObject().refGetValue("name").equals("Model"))
                return;
            if(!(makeKuaba(evt.getPropertyName())))
                return;
                
                
            //TODO CRIAR O OBSERVER PARA UMA CLASSE QUANDO SE MUDA O NOME DA MESMA NA CLASSE DE ATRIBUTOS
            RefObject source = (RefObject)evt.getSource();
            if(!KuabaSubsystem.facade.existsDomainIdea(target)){		
                PropertyChangeEvent nameEvt = new PropertyChangeEvent(target, "name", "", target.refGetValue("name"));			
		PropertyChangeListener listener = KuabaSubsystem.eventPump.getObserver(target);
		if(listener == null){
                    listener = this.createListener(target);
                    if (listener == null)
                        return;
                    KuabaSubsystem.eventPump.addModelElementObserver(listener, target);
		}
                listener.propertyChange(nameEvt);
		Reference ref = MofHelper.getReference(evt.getPropertyName(), (MofClass)source.refMetaObject());
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
                    Idea acceptedDesignIdea = null;
                    //Percorre a lista de ideias de dominio para ver se ele ja existe
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
                    //Se houver uma ideia de dominio com o nome antigo, deve rejeita-lo e copiar seus filhos para a nova arvore
                    Idea rejectedDomainIdea = KuabaSubsystem.facade.findDomainIdea(oldName, "Association");
                    if(rejectedDomainIdea != null){
                        KuabaSubsystem.facade.domainIdeaDesconsidered(rejectedDomainIdea);
                        Idea rejectedDesignIdea = KuabaHelper.getRejectedDesignIdea(rejectedDomainIdea, "Association");
                        Question howModelQuestion = (Question)rejectedDomainIdea.listSuggests().next();
                        ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{rejectedDesignIdea},howModelQuestion);
                        controller.render();
                        
                        
                        this.copyQuestions(rejectedDesignIdea,acceptedDesignIdea,3);
                    }
                }
                
	}
        
        //Copia as questoes associadas a uma ideia e depois copia as ideias associadas
        public void copyQuestions(Idea rejIdea,Idea accIdea,int limit){
            
            if(limit>0){
                System.out.println("Question: "+Integer.toString(limit));
                //Se a ideia de design aceita nao sugere nenhuma questao, copia a da ideia rejeitada
                if(!accIdea.hasSuggests())
                {
                    //Map<Question,Question> addedReject = new HashMap<Question,Question>();
                    //Para cada questao sugerida pela ideia rejeitada
                    limit--;
                    for(Question rejQ : rejIdea.getSuggests())
                    {
                        //Copia a questao sugerida e em seguida copia as ideias
                        Question newQuestion = KuabaSubsystem.facade.copyQuestion(rejQ);
                        //Adiciona à lista de ideias enderecadas da questao
                        accIdea.addSuggests(newQuestion);
                        //Inserir em profundidade
                        this.copyIdeas(rejQ,newQuestion,limit);
                    }
                }
                //Se a ideia de design aceita sugere alguma questao...
                else{
                    List<String> accSuggests = new ArrayList<String>();
                    //leio todas as questoes sugeridas
                    for(Question accQ : accIdea.getSuggests())
                        accSuggests.add(accQ.getHasText());
                    
                    limit--;
                    //Se alguma delas for igual a uma ideia rejeitada
                    for(Question rejQ : rejIdea.getSuggests())
                    {
                        //Nao copio a questao, apenas vou copiar as ideias dentro delas
                        if(accSuggests.contains(rejQ.getHasText()))
                        {
                            for(Question accQ : accIdea.getSuggests())
                                if(accQ.getHasText().equals(rejQ.getHasText()))
                                {
                                    
                                    this.copyIdeas(rejQ,accQ,limit);
                                    break;
                                }
                            //break;
                        }
                        else{
                            Question newQuestion = KuabaSubsystem.facade.copyQuestion(rejQ);
                            //Adiciona à lista de ideias enderecadas da questao
                            accIdea.addSuggests(newQuestion);
                            limit--;
                            this.copyIdeas(rejQ, newQuestion, limit);
                        }
                    }
                }
            
            }
        }
        
        //Copia as questoes associadas a uma ideia e depois copia as ideias associadas
        public void copyIdeas(Question rejQuestion,Question accQuestion,int limit)
        {
            //Se estou no ultimo nivel, entao devo copiar as associacoes das questoes, e nao as ideias em si
            if(limit==0)
            {
                //Se a questao possui decisoes que devem ser rejeitadas
                if(rejQuestion.hasHasDecision()){
                    //rejeito cada uma das decisoes
                    for(Decision dec : rejQuestion.getHasDecision()){
                        dec.setIsAccepted(false);
                    }
                }
                //se nao possui decisoes, crio as decisoes e configuro como falsas
                else{
                    for(Idea idea : rejQuestion.getIsAddressedBy()){
                        KuabaSubsystem.facade.makeDecision(rejQuestion, idea, false);
                    }
                }
                
                //Se as questoes das ideias aceitas tem decisoes, configuro cada uma como aceita
                boolean isAddressed = false;
                for(Idea rejIdea : rejQuestion.getIsAddressedBy()){
                   for(Idea accIdea : accQuestion.getIsAddressedBy())
                   {
                       if(accIdea.getId().equals(rejIdea.getId())){
                           isAddressed=true;
                           break;
                       }
                   }
                   if(!isAddressed)
                       accQuestion.addIsAddressedBy(rejIdea);
                   isAddressed=false;
                }
                boolean isDecided = false;
                for(Idea idea : accQuestion.getIsAddressedBy()){
                    for(Decision dec : accQuestion.getHasDecision()){
                        if(dec.getConcludes().getId().equals(idea.getId())){
                            dec.setIsAccepted(true);
                            isDecided = true;
                        }
                    }
                    if(!isDecided)
                        KuabaSubsystem.facade.makeDecision(accQuestion, idea, true);
                }
                
                
            }
            
            else if(limit>0){
                System.out.println("Idea: "+Integer.toString(limit));
           
                //Se a ideia de design aceita nao sugere nenhuma questao, copia a da ideia rejeitada
                if(!accQuestion.hasIsAddressedBy())
                {
                    limit--;
                    //Para cada questao sugerida pela ideia rejeitada
                    for(Idea rejIdea : rejQuestion.getIsAddressedBy())
                    {
                        //recusa as ideia para rejQuestion
                        for(Decision dec : rejIdea.getIsConcludedBy())
                            dec.setIsAccepted(false);
                        //Copia a questao sugerida e em seguida copia as ideias
                        Idea newIdea = KuabaSubsystem.facade.copyIdea(rejIdea,accQuestion,true);    
                        //Aceita a ideia para as accQuestions
                        KuabaSubsystem.facade.makeDecision(accQuestion, newIdea, true);
                        this.copyQuestions(rejIdea,newIdea,limit);
                        
                    }
                }
                //Se a ideia de design aceita sugere alguma questao...
                else{
                    Map<String,String> accAddressed = new HashMap<String,String>();
                    //leio todas as questoes sugeridas
                    for(Idea accIdea : accQuestion.getIsAddressedBy())
                        accAddressed.put(accIdea.getId(),accIdea.getHasText());
                    
                    limit--;
                    //Se alguma delas for igual a uma ideia rejeitada
                    
                    //Recuso todas as ideias rejeitadas
                    for(Idea rejIdea : rejQuestion.getIsAddressedBy())
                    {
                        //recusa as ideia para rejQuestion
                        for(Decision dec : rejIdea.getIsConcludedBy())
                            dec.setIsAccepted(false);
                    } 
                    //Aceito todas as ideias sugeridas
                    for(Idea accIdea : accQuestion.getIsAddressedBy())    
                    {
                        if(!accIdea.hasIsConcludedBy())
                            KuabaSubsystem.facade.makeDecision(accQuestion, accIdea, true);
                        else
                            for(Decision dec : accIdea.getIsConcludedBy())
                                dec.setIsAccepted(true);
                    }
                 
                    boolean isEqual = false;
                    //Para cada ideia rejeitada e aceita que nao tenha sido copiada...
                    for(Idea rejIdea : rejQuestion.getIsAddressedBy())
                    {
                        
                        for(Idea accIdea : accQuestion.getIsAddressedBy())
                        {
                            if(accIdea.getHasText().equals(rejIdea.getHasText())){
                                isEqual=true;
                                this.copyQuestions(rejIdea, accIdea, limit);
                                break;
                            }
                            
                        }
                        if(!isEqual)
                        {
                            Idea newIdea = KuabaSubsystem.facade.copyIdea(rejIdea,accQuestion,true);
                            this.copyQuestions(rejIdea, newIdea, limit);
                        }
                        isEqual=false;
                    }
                    
                }
            }
        }
        
        
        public void removedElement(PropertyChangeEvent evt,RefObject source){
        
            KuabaSubsystem.eventPump.removeObservers(source);
            Idea domainIdea = KuabaSubsystem.facade.getIdeaOnSession(KuabaSubsystem.resolver.resolveXmiId(source));	
            
            KuabaSubsystem.getSession().unitOfWork().removeAcceptedIdea(domainIdea.getId());
            KuabaSubsystem.getSession().unitOfWork().addRejectedIdea(domainIdea);
            
            ArgumentController controller = new ObjectsToArgumentController(null, new Idea[]{KuabaSubsystem.facade.getDesignIdea(domainIdea, "Association")},domainIdea.getSuggests().iterator().next());
            controller.render();
            
            domainIdeaSubTreeCycleDecision(domainIdea, false, 2);
        }
        
        public void domainIdeaSubTreeCycleDecision(Idea ideia,boolean isAccepted,int limit){
            
            if(limit==0){
                for(Question q :ideia.getSuggests()){
                    for(Decision d : q.getHasDecision()){
                        if(d.getIsAccepted()){
                            d.setIsAccepted(isAccepted);
                            String text = "Why not make " + KuabaHelper.getDomainIdea(d.getConcludes()).getHasText()
                                    + " part of this association?";
                            ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{ideia},text,q);
                            controller.render();
            
                        }
                    }
                }
            }
            if(limit>0){
                limit--;
                for(Question q: ideia.getSuggests())
                {
                    for(Idea d : q.getIsAddressedBy())
                    {
                        if(!d.hasIsConcludedBy())
                            KuabaSubsystem.facade.makeDecision(q, d, isAccepted);
                        else
                        {
                            for(Decision dec : d.getIsConcludedBy())
                                dec.setIsAccepted(isAccepted);
                        }
                            domainIdeaSubTreeCycleDecision(d,isAccepted,limit);
                    }   
                }
            }
        }
        
        
        
	
	@Override
	public PropertyChangeListener applyFilter(RefObject subject) {
		return new EventFilter(this);
	}

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean makeKuaba(String referenceName) {
		if((Arrays.asList(referencesAvailable).contains(referenceName)||(Arrays.asList(referencesAvailable).isEmpty())))
			return true;
		return false;
	}
	

}
