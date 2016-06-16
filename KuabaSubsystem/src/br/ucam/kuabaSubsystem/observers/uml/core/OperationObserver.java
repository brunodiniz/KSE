package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import javax.jmi.reflect.RefObject;


import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.controller.ObjectsToArgumentController;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import java.util.List;

public class OperationObserver extends ModelElementObserver{
	
	public OperationObserver() {
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
                //Idea ownedDomainIdea = KuabaHelper.getDomainIdea(KuabaSubsystem.facade.modelRepository(),src, this.getNamePropertyValue(sourceClass));
                Idea ownedDomainIdea = KuabaHelper.getDomainIdea(this.getNamePropertyValue(sourceClass));
                //Caso nao encontre, significa que a ideia de dominio ja foi cadastrada antes
                //Neste caso, tenho que percorrer todas as ideias de design que sao dominadas pela classe
                //if(ownedDomainIdea==null)
                //   ownedDomainIdea = KuabaSubsystem.getSession().unitOfWork().getConsideredIdea((AttributeObserver.lastAccDmnIdeaId));
                   
                    
                
                Idea ownedDesignIdea = KuabaSubsystem.facade.getDesignIdea(ownedDomainIdea, "Operation");
                
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
                     
                    
                    Idea OperationIdea = null;
                    for(Idea d: KuabaSubsystem.facade.modelRepository().getDomainIdeas()){
                        String txt1 = d.getId().split("_")[1];
                        String txt2 = sourceClass.refMofId().split(":")[3];
                        if(txt1.equals(txt2))
                            OperationIdea = d;
                    }
                    Idea attributeDesign = KuabaSubsystem.facade.getDesignIdea(OperationIdea, "Operation");
                    ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{attributeDesign},
                    "Why not make "+classIdea.getHasText()+" owner of "+
                            OperationIdea.getHasText()+"?",OperationIdea.getSuggests().iterator().next());
                    controller.render();
                }
                 
                else{ 
                    String rejectedId = KuabaSubsystem.resolver.resolveXmiId(rejectedValue);
                    Idea rejOwnedDomainIdea = KuabaHelper.getDomainIdea(KuabaSubsystem.facade.modelRepository(),rejectedId, this.getNamePropertyValue(sourceClass));
                    Idea rejOwnedDesignIdea = KuabaSubsystem.facade.getDesignIdea(rejOwnedDomainIdea, propertyName);
                
                    String acceptedId = KuabaSubsystem.resolver.resolveXmiId(acceptedValue);
                    Idea accOwnedDomainIdea = KuabaHelper.getDomainIdea(KuabaSubsystem.facade.modelRepository(),acceptedId, this.getNamePropertyValue(sourceClass));
                    Idea accOwnedDesignIdea = KuabaSubsystem.facade.getDesignIdea(accOwnedDomainIdea, propertyName);
                
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
		if(oldName.equals("newOperation"))
                {
                    boolean isDomainIdea = false;
                    //Percorre a lista de ideias de dominio para ver se ele ja existe
                    for(Idea domainIdea : KuabaSubsystem.facade.getRootQuestion().getIsAddressedBy())
                    {
                       if(domainIdea.getHasText().equals(newName))
                       {
                           //Se existir, considera a ideia de dominio e armazena o argumento
                            boolean isExistent = KuabaSubsystem.facade.reAcceptDomainIdea(domainIdea,"Operation");
                            Idea acceptedDesignIdea = null;
                            if(isExistent) 
                                acceptedDesignIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, designIdeaText);
                            else{
                            //Se nao conseguiu encontrar a ideia de design, deve criar uma nova e aceita-la
                                acceptedDesignIdea = KuabaSubsystem.facade.createIdea(domainIdea.getSuggests().iterator().next(), "Operation");
                                KuabaSubsystem.facade.makeDecision(domainIdea.getSuggests().iterator().next(), acceptedDesignIdea, true);
                           //AttributeObserver.lastAccDmnIdeaId = domainIdea.getId();
                            }
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
                        //Obtem a ideia de dominio rejeitada e a remove da lista de ideias de dominio aprovadas
                        Idea rejectedDomainIdea = KuabaSubsystem.getSession().unitOfWork().getConsideredIdeaByName(oldName);
                        Idea rejectedDesignIdea = KuabaHelper.getAcceptedDesignIdea(rejectedDomainIdea,"Operation");
                        
                        
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
                        Idea rejectedDesign = KuabaSubsystem.facade.getDesignIdea(rejectedDomain, "Operation");
                        //newName esta na lista de ideias consideradas porque foi inserido mais acima. Preciso ler de la
                        Idea acceptedDomain = KuabaSubsystem.getSession().unitOfWork().getConsideredIdeaByName(newName);
                        Idea acceptedDesign = KuabaSubsystem.facade.getDesignIdea(acceptedDomain,"Operation");
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
                        Idea rejectedDesignIdea = KuabaHelper.getRejectedDesignIdea(rejectedDomainIdea, "Operation");
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
