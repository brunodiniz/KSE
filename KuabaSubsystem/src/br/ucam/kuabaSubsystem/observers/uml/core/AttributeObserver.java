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
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.MofHelper;

public class AttributeObserver extends ModelElementObserver {

        //Esta variavel interna e usada para indicar, em um caso especifico,
        //a ultima ideia de design que foi rejeitada e depois aceita)
        private static String lastAccDmnIdeaId;
    
	public AttributeObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"owner"};
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		//super.propertyChange(evt);
		
                RefObject source = (RefObject)evt.getSource();
                if(evt.getPropertyName().equals("remove"))
                    this.removedElement(source);
                //Se houve mudanca no nome da classe

                if(evt.getPropertyName().equals("name")&&(evt.getNewValue() instanceof String && !evt.getNewValue().equals("newAttr")))
                {
                    //Fazer o tratamento especial para classes
                    //Se esta declaracao estiver antes do if anterior, vai dar erro
                    MofClass metaClass = (MofClass)source.refMetaObject();
                    String designIdeaText = (String)metaClass.refGetValue("name");
                    
                    if(!evt.getNewValue().equals("")){
                        
                            this.nameChanged((String)evt.getNewValue(),(String)evt.getOldValue(),designIdeaText, "_"+KuabaSubsystem.resolver.resolveXmiId(source));
                        
                        
                    }
                }
               
                
                if (evt.getPropertyName().equals("owner"))   
                    this.referenceModified((RefObject)evt.getNewValue(),(RefObject)evt.getOldValue(),(RefObject)source,evt.getPropertyName());
                
             		//CONFERIR SE PEGA A IDEIA DE DOMINIO :O
	
	}
        //Trata o evento de mudanca de "ownership" do atributo
        public void referenceModified(RefObject acceptedValue,RefObject rejectedValue,RefObject sourceClass,String propertyName)
        {
            //Se o elemento fonte for nulo, crio o par questão (Owner?)- ideia, associando a ideia de design correspondente a uma classe
            if(rejectedValue==null){
                
                //Busco a ideia de dominio que e dominada
                String src = KuabaSubsystem.resolver.resolveXmiId(sourceClass);
                String name = this.getNamePropertyValue(sourceClass);
                //Idea ownedDomainIdea = KuabaHelper.getDomainIdea(KuabaSubsystem.facade.modelRepository(),src, this.getNamePropertyValue(sourceClass));
                Idea ownedDomainIdea = KuabaHelper.getDomainIdea(name);
                //Caso nao encontre, significa que a ideia de dominio ja foi cadastrada antes
                //Neste caso, tenho que percorrer todas as ideias de design que sao dominadas pela classe
                //if(ownedDomainIdea==null)
                //   ownedDomainIdea = KuabaSubsystem.getSession().unitOfWork().getConsideredIdea((AttributeObserver.lastAccDmnIdeaId));
                   
                    
                
                Idea ownedDesignIdea = KuabaSubsystem.facade.getDesignIdea(ownedDomainIdea, "Attribute");
                
                Idea acceptedDomainIdea = KuabaHelper.getDomainIdea(KuabaSubsystem.facade.modelRepository(), KuabaSubsystem.resolver.resolveXmiId(acceptedValue),this.getNamePropertyValue(acceptedValue));
                
                KuabaSubsystem.facade.referenceChanged(ownedDesignIdea,acceptedValue, propertyName);
            
                String text = "Why does make " + acceptedDomainIdea.getHasText() + " " + propertyName	+ " of " + ownedDomainIdea.getHasText() + "?";
                
                ArgumentController controller = new InFavorArgumentController(new Idea[]{ownedDesignIdea},null,text,ownedDesignIdea.getAddress().iterator().next()); 
                controller.render();
            }
            //Se o elemento fonte nao for nulo, devo copiar as associacoes para o proximo elemento
            else{
                
                if(acceptedValue==null){
                    Idea classIdea = null;
                    for(Idea d: KuabaSubsystem.facade.modelRepository().getDomainIdeas()){
                        String txt1 = d.getId().split("_")[1];
                        String txt2 = rejectedValue.refMofId().split(":")[3];
                        if(txt1.equals(txt2))
                            classIdea = d;
                    }
                     
                    
                    Idea attributeIdea = null;
                    for(Idea d: KuabaSubsystem.facade.modelRepository().getDomainIdeas()){
                        String txt1 = d.getId().split("_")[1];
                        String txt2 = sourceClass.refMofId().split(":")[3];
                        if(txt1.equals(txt2))
                            attributeIdea = d;
                    }
                    Idea attributeDesign = KuabaSubsystem.facade.getDesignIdea(attributeIdea, "Attribute");
                    ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{attributeDesign},
                    "Why not make "+classIdea.getHasText()+" owner of "+
                            attributeIdea.getHasText()+"?",attributeIdea.getSuggests().iterator().next());
                    controller.render();
                }
                else{

                    Idea rejOwnedDomainIdea = null;
                    for(Idea d: KuabaSubsystem.facade.modelRepository().getDomainIdeas()){
                        String txt1 = d.getId().split("_")[1];
                        String txt2 = rejectedValue.refMofId().split(":")[3];
                        if(txt1.equals(txt2))
                            rejOwnedDomainIdea = d;
                    }
                
                    //Idea rejOwnedDomainIdea = KuabaHelper.getDomainIdea(KuabaSubsystem.facade.modelRepository(),rejectedId, this.getNamePropertyValue(sourceClass));
                    Idea rejOwnedDesignIdea = KuabaSubsystem.facade.getDesignIdea(rejOwnedDomainIdea, "Class");
                
                    Idea accOwnedDomainIdea = null;
                    for(Idea d: KuabaSubsystem.facade.modelRepository().getDomainIdeas()){
                        String txt1 = d.getId().split("_")[1];
                        String txt2 = acceptedValue.refMofId().split(":")[3];
                        if(txt1.equals(txt2))
                            accOwnedDomainIdea = d;
                    }
                
                //String acceptedId = KuabaSubsystem.resolver.resolveXmiId(acceptedValue);
                //Idea accOwnedDomainIdea = KuabaHelper.getDomainIdea(KuabaSubsystem.facade.modelRepository(),acceptedId, this.getNamePropertyValue(sourceClass));
                Idea accOwnedDesignIdea = KuabaSubsystem.facade.getDesignIdea(accOwnedDomainIdea, "Attribute");
                
                KuabaSubsystem.facade.referenceChanged(sourceClass, acceptedValue, propertyName);
                
                KuabaSubsystem.facade.copyQuestions(rejOwnedDomainIdea, accOwnedDomainIdea, 1);
                
                Idea acceptedDomainIdea = KuabaHelper.getDomainIdea(KuabaSubsystem.facade.modelRepository(), KuabaSubsystem.resolver.resolveXmiId(acceptedValue),this.getNamePropertyValue(acceptedValue));
                
                String text = "Why does make " + acceptedDomainIdea.getHasText() + " " + propertyName	+ " of " + accOwnedDomainIdea.getHasText() + "?";
                ArgumentController controller = new InFavorArgumentController(new Idea[]{accOwnedDesignIdea},null,text,accOwnedDesignIdea.getAddress().iterator().next()); 
                controller.render();
                
                
                controller = new ObjectsToArgumentController(null,new Idea[]{rejOwnedDesignIdea});
                controller.render();
                
                }
            }
         
        }
        
        
        @Override
        public void referenceModified(RefObject target, PropertyChangeEvent evt){
		if(((RefObject)evt.getNewValue()).refMetaObject().refGetValue("name").equals("Model"))
			return;
		if(!(makeKuaba(evt.getPropertyName())))
			return;
                
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
                
                //aqui que cria as outras questões do tipo participant e owner
		Decision d = KuabaSubsystem.facade.referenceChanged((RefObject)evt.getSource(),	target, evt.getPropertyName());
		Idea sourceDomainIdea = KuabaHelper.getDomainIdea(modelRepository(), KuabaSubsystem.resolver.resolveXmiId((RefObject)evt.getSource()), this.getNamePropertyValue((RefObject)evt.getSource()));
		Idea sourceDesignIdea = KuabaHelper.getAcceptedDesignIdea(sourceDomainIdea,(String)((RefObject)evt.getSource()).refMetaObject().refGetValue("name"));
		
		askReferenceChangeArguments(d.getConcludes(),(Question)KuabaHelper.getReasoningElementInTree(sourceDesignIdea, evt.getPropertyName() + "?"), evt);
                
                //tirando as ideias de dominio extras
                if (sourceDesignIdea.getHasText().equals("AssociationEnd")) {
                    sourceDomainIdea.listSuggests().next().remove();
                    sourceDomainIdea.remove();
                }
	}
        
        protected void removedElement(RefObject source)
        {
            KuabaSubsystem.eventPump.removeObservers(source);
            Idea domainIdea = KuabaSubsystem.facade.getIdeaOnSession(KuabaSubsystem.resolver.resolveXmiId(source));			
            Question howModelQuestion = (Question)domainIdea.listSuggests().next();
            List<Idea> acceptedDesignIdeas = 
            KuabaHelper.getAcceptedAddressedIdeas(howModelQuestion);
            for (Idea idea : acceptedDesignIdeas) 
            {
                ArgumentController controller = new ObjectsToArgumentController(null, new Idea[]{idea},howModelQuestion);
                controller.render();
            }        
            
            KuabaSubsystem.facade.domainIdeaDesconsidered(domainIdea);        
            KuabaSubsystem.facade.domainIdeaSubTreeCycleDecision(domainIdea, false, 1);
            //KuabaSubsystem.facade.makeDecisionRootElements(domainIdea,false);
        }
        
        
         @Override
        protected void nameChanged(String newName, String oldName,String designIdeaText, String elementId){
                //Se e a primeira ideia
		if(oldName.equals("newAttr"))
                {
                    boolean isDomainIdea = false;
                    //Percorre a lista de ideias de dominio para ver se ele ja existe
                    for(Idea domainIdea : KuabaSubsystem.facade.getRootQuestion().getIsAddressedBy())
                    {
                       if(domainIdea.getHasText().equals(newName))
                       {
                           //Se existir, considera a ideia de dominio e armazena o argumento
                           boolean isExistent = KuabaSubsystem.facade.reAcceptDomainIdea(domainIdea,"Attribute");
                           Idea acceptedDesignIdea = null;
                           if(isExistent)
                               acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, designIdeaText);
                           else{
                                acceptedDesignIdea = KuabaSubsystem.facade.createIdea(domainIdea.getSuggests().iterator().next(), "Attribute");
                                KuabaSubsystem.facade.makeDecision(domainIdea.getSuggests().iterator().next(), acceptedDesignIdea, true);
                           }
                           KuabaHelper.getAcceptedDesignIdea(domainIdea, designIdeaText);
                           
                           AttributeObserver.lastAccDmnIdeaId = domainIdea.getId();
                           
                           ArgumentController controller = new InFavorArgumentController(new Idea[]{acceptedDesignIdea}, null);
                           isDomainIdea=true;
                           controller.render();
                       }
                    }
                    if(!isDomainIdea){
                        Idea domainIdea = KuabaSubsystem.facade.domainIdeaAdded(newName, designIdeaText, elementId);
                        Idea acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, designIdeaText);
                        if(!(newName.equals(""))) 
                        {
                            ArgumentController controller = new InFavorArgumentController(new Idea[]{acceptedDesignIdea}, null);
                            controller.render();
                        }
                    }
                }
                //Se nao for a primeira ideia
                else if(!oldName.equals("")){
                    Boolean isDomainIdea = false;
                    //Percorre a lista de ideias de dominio para ver se ele ja existe
                    for(Idea domainIdea : KuabaSubsystem.facade.getRootQuestion().getIsAddressedBy())
                    {
                       if(domainIdea.getHasText().equals(newName))
                       {
                           //Se existir, considera a ideia de dominio e armazena o argumento
                           //KuabaSubsystem.facade.domainIdeaConsidered(domainIdea);
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
                        //Obtem a ideia de dominio rejeitada e a remove da lista de ideias de dominio aprovadas
                        Idea rejectedDomainIdea = KuabaSubsystem.getSession().unitOfWork().getConsideredIdeaByName(oldName);
                        Idea rejectedDesignIdea = KuabaHelper.getAcceptedDesignIdea(rejectedDomainIdea,"Attribute");
                        
                        
                        //Recusa a decisao e copia as questoes
                        rejectedDesignIdea.getAddress().iterator().next().getHasDecision().iterator().next().setIsAccepted(false);
                        KuabaSubsystem.facade.copyQuestions(rejectedDesignIdea, acceptedDesignIdea, 1);
                        
                        //Obtem a ideia de dominio que e o "owner" da ideia de design aceita
                        //ps:Percorri a estrutura -> atributo -> (Owner?) -> Class e chamei a func de KuabaHelper pra me buscar a ideia de dominio correspondente a Class obtida
                        Idea owner = KuabaHelper.getDomainIdea(acceptedDesignIdea.getSuggests().iterator().next().getIsAddressedBy().iterator().next());
                        String text = "Why make " + owner.getHasText() + " owner of " + KuabaHelper.getDomainIdea(acceptedDesignIdea).getHasText() + "?";
                        controller = new InFavorArgumentController(new Idea[]{acceptedDesignIdea},null,text,acceptedDesignIdea.getSuggests().iterator().next());
                        controller.render();
                        
                        
                        KuabaSubsystem.facade.domainIdeaDesconsidered(rejectedDomainIdea);
                        controller = new ObjectsToArgumentController(null,new Idea[]{rejectedDesignIdea});
                        controller.render();
                    }
                    //Se ja existir, entao deve inverter as decisoes da subarvore
                    else{
                        
                        //Primeiro verifico se ja existia uma ideia de design. Caso nao existisse, devo copiar esta subarvore para a ideia de dominio,
                        //senao, devo apenas percorrer a subarvore mudando a decisao
                        Idea rejectedDomain = KuabaSubsystem.getSession().unitOfWork().getConsideredIdeaByName(oldName);
                        Idea rejectedDesign = KuabaSubsystem.facade.getDesignIdea(rejectedDomain, "Attribute");
                        //newName esta na lista de ideias consideradas porque foi inserido mais acima. Preciso ler de la
                        Idea acceptedDomain = KuabaSubsystem.getSession().unitOfWork().getConsideredIdeaByName(newName);
                        Idea acceptedDesign = KuabaSubsystem.facade.getDesignIdea(acceptedDomain,"Attribute");
                        //Se a ideia de dominio existia, mas a ideia de design nao, devo copiar a ideia de design da ideia de dominio rejeitada
                        //e enderecar para a questao sugerida pela ideia de dominio aceita.
                        if(acceptedDesign==null)
                        {
                            acceptedDesign = KuabaSubsystem.facade.copyIdea(rejectedDesign);
                            acceptedDesign.addAddress(acceptedDomain.getSuggests().iterator().next());
                            KuabaSubsystem.facade.copyQuestions(rejectedDesign, acceptedDesign, 1);
                            KuabaSubsystem.facade.domainIdeaDesconsidered(rejectedDomain);
                        }
                        //Se ja existe ideia de design, devo percorrer a arvore modificando a decisao
                        else
                            KuabaSubsystem.facade.rejectAndAcceptOwnership(rejectedDesign,acceptedDesign);
                        
                    }
                    //Se houver uma ideia de dominio com o nome antigo, deve rejeita-lo
                    Idea rejectedDomainIdea = KuabaSubsystem.facade.findDomainIdea(oldName, "Class");
                    if(rejectedDomainIdea != null){
                        KuabaSubsystem.facade.domainIdeaDesconsidered(rejectedDomainIdea);
                        Idea rejectedDesignIdea = KuabaHelper.getRejectedDesignIdea(rejectedDomainIdea, "Attribute");
                        Question howModelQuestion = (Question)rejectedDomainIdea.listSuggests().next();
                        ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{rejectedDesignIdea},howModelQuestion);
                        controller.render();
                    }
                }
	}
        
	
	@Override
	public PropertyChangeListener applyFilter(RefObject subject) {

		return new NameEventFilter(subject, this);
	}
	@Override
	public void askReferenceChangeArguments(Idea idea,Question question, PropertyChangeEvent evt) {
            String targetName = (String)((RefObject)evt.getNewValue()).refGetValue("name");
            String sourceName = (String)((RefObject)evt.getSource()).refGetValue("name");
            String text = "Why does make " + targetName + " " + evt.getPropertyName()	+ " of " + sourceName + "?";
            KuabaSubsystem.facade.renderInFavorArgumentView(question, idea, text);  
	}
	

}
