package br.ucam.kuabaSubsystem.util;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.graph.util.KuabaGraphUtil;
import br.ucam.kuabaSubsystem.graph.util.ReasoningElementIterator;
import br.ucam.kuabaSubsystem.kuabaModel.Solution;

public class KuabaHelper {
	
	
	
	public static Idea getIdeaByText(Collection<Idea> ideaCollection,String text){
		for (Idea idea : ideaCollection) {
			if (idea.getHasText().equals(text)) { //Case sensitive, verificar se isso é relevante
				return idea;
			}
		}
		return null;		
	}
	
	public static Decision getDecision(Question question, Idea idea){
		Collection<Decision> ideaDecisionList = idea.getIsConcludedBy();
		for (Decision decision : ideaDecisionList) {
			if(question.getHasDecision().contains(decision))
				return decision;
		}
		return null;
	}
	public static List<Idea> getAcceptedAddressedIdeas(Question question){
		Collection<Idea> addressedIdeaCollection = 
			question.getIsAddressedBy();		
		List<Idea> acceptedAddressedIdeas = new ArrayList<Idea>(); 
		for (Idea idea : addressedIdeaCollection) {
			Decision decision = getDecision(question, idea);
			if((decision != null) && (decision.getIsAccepted()))
				acceptedAddressedIdeas.add(idea);
		}
		return acceptedAddressedIdeas;
	}
	public static ReasoningElement getReasoningElementInTree(
			ReasoningElement initElement, String text){
		Iterator<ReasoningElement> reasoningElementIterator = null;
		if (initElement instanceof Question) {
			Question question = (Question) initElement;
			reasoningElementIterator = (Iterator) question.listIsAddressedBy();			
		}
		if (initElement instanceof Idea) {
			Idea idea = (Idea) initElement;
			reasoningElementIterator = (Iterator) idea.listSuggests();
			
		}
		if(reasoningElementIterator == null)
			return null;
		
		Iterator<ReasoningElement> iterator = 
			new ReasoningElementIterator(reasoningElementIterator);
		while (iterator.hasNext()) {
			ReasoningElement reasoningElement = (ReasoningElement) iterator
					.next();
			if(text.equals(reasoningElement.getHasText()))
				return reasoningElement;		
		}
		return null;
	}
	public static Idea getDomainIdea(Idea designIdea){
            
                Collection<Question> questions = designIdea.getAddress();
		for (Question question : questions) 
			if(question.getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX))
				return (Idea)question.getIsSuggestedBy().iterator().next();
                        /*else{
                            //Um pequeno atalho pras associacoes no diagrama de classes da UML
                            for(Question q : designIdea.getAddress()){
                                if(q.getHasText().equals("Participant?"))
                                    return KuabaHelper.getDomainIdea((Idea)q.getIsSuggestedBy().iterator().next());
                            }
                        }*/
		return null;
	}

	public static Idea getMostRecentDomainIdea(KuabaRepository repository,
			String elementId){
		List<Idea> elementDomainIdeas = repository.getDomainIdeasWhereIdLike(elementId);
		Idea mostRecent = null;
		if(!elementDomainIdeas.isEmpty()){
			mostRecent = elementDomainIdeas.iterator().next();
			for (Idea idea : elementDomainIdeas)			 
				if(idea.getHasCreationDate().after(mostRecent.getHasCreationDate()))
					mostRecent = idea;
			return mostRecent;
		}	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Idea getAcceptedDesignIdea(Idea domainIdea, String designIdeaText){
		Question existentHowModel =(Question)getReasoningElementInTree(domainIdea,Question.DOMAIN_QUESTION_TEXT_PREFIX + domainIdea.getHasText() + "?");

		Collection<Decision> decisions = existentHowModel.getHasDecision();

		for (Decision decision : decisions) 
			if((decision.getIsAccepted()) && 
					(decision.getConcludes().getHasText().equals(
							designIdeaText)))
				return decision.getConcludes();
		return null;
	}
        
        public static Idea getAcceptedDesignIdea(Idea domainIdea){
            Question suggested = domainIdea.getSuggests().iterator().next();
            
            for(Decision d: suggested.getHasDecision()){
                if(d.getIsAccepted())
                    return d.getConcludes();
            }
            return null;
        }
        
	
	public static Idea getRejectedDesignIdea(Idea domainIdea,String designIdeaText){
		Question existentHowModel =(Question)getReasoningElementInTree(domainIdea,Question.DOMAIN_QUESTION_TEXT_PREFIX + domainIdea.getHasText() + "?");
		Collection<Decision> decisions = existentHowModel.getHasDecision();
		for (Decision decision : decisions) {
                    //boolean b = decision.getIsAccepted();
                    //String s1 = decision.getConcludes().getHasText();
			if((!decision.getIsAccepted()) && (decision.getConcludes().getHasText().equals(designIdeaText)))
				return decision.getConcludes();
                }
		return null;
	}	
	
	public static Idea getDomainIdea(KuabaRepository repository, String mofId, String text){
		if((text == null) || (text.equals("null")))
			text = "";
		List<Idea> domainIdeaList = repository.getDomainIdeasWhereIdLike(mofId);
		for (Idea idea : domainIdeaList)
			if(idea.getHasText().equals(text))
				return idea;		
		return null;	
	}
        
        
        
        
	
	public static List<Argument> filterConsidersArguments(
			Collection<Argument> arguments, Question consideredQuestion){		
		List<Argument> filteredArgumentList = new ArrayList<Argument>();
		for (Argument argument : arguments)
			if((argument.getConsiders() != null) 
					&& (argument.getConsiders().equals(consideredQuestion)) ||
					(argument.getConsiders() == consideredQuestion))
				filteredArgumentList.add(argument);
		return filteredArgumentList;		
	}
	public static void connectQuestionsToIdeas(Map<Question, Idea> disconnectedQuestionIdeaMap){
		Set<Entry<Question, Idea>> disconnectedIdeaQuestionEntrySet = disconnectedQuestionIdeaMap.entrySet();
		for (Map.Entry<Question, Idea> entry : disconnectedIdeaQuestionEntrySet) {
			entry.getKey().addIsAddressedBy(entry.getValue());
			entry.getValue().addAddress(entry.getKey());	
		}
	}

	public static HashMap<Question, Idea> disconnectSubtree(Idea domainIdea){
		List<ReasoningElement> visitedElementList = new ArrayList<ReasoningElement>();
		
		HashMap<Question, Idea> disconnectedQuestionIdeaMap = new HashMap<Question, Idea>();
		List<Idea> subtreeConnectionIdeaList = new ArrayList<Idea>();		
		Question rootQuestion = (Question)domainIdea.listAddress().next();
		domainIdea.removeAddress(rootQuestion);		
				
		Iterator<ReasoningElement> reasoningElementCompositeIterator = 
			new ReasoningElementIterator((Iterator)rootQuestion.listIsAddressedBy());
//		boolean isConnectionElement = false;		
		ReasoningElement beforeReasoningElement = null;
		ReasoningElement reasoningElement = null;
		while (reasoningElementCompositeIterator.hasNext()) {
			reasoningElement = 
				(ReasoningElement) reasoningElementCompositeIterator
					.next();			
			if(!visitedElementList.contains(reasoningElement))
				visitedElementList.add(reasoningElement);				
                        else if (reasoningElement instanceof Idea) {				
				Idea connectionIdea = (Idea) reasoningElement;
				if(!connectionIdea.getAddress().contains(rootQuestion)){
					subtreeConnectionIdeaList.add((Idea) reasoningElement);
					Question addressedQuestion = (Question) beforeReasoningElement;
					addressedQuestion.removeIsAddressedBy(connectionIdea);				
					connectionIdea.removeAddress(addressedQuestion);
					disconnectedQuestionIdeaMap.put(addressedQuestion, connectionIdea);
				}		
			}
			beforeReasoningElement = reasoningElement;
		}
			
		domainIdea.addAddress(rootQuestion);		
		beforeReasoningElement = null;
		List<ReasoningElement> domainIdeaList = new ArrayList<ReasoningElement>();
		domainIdeaList.add(domainIdea);
		reasoningElementCompositeIterator = 
			new ReasoningElementIterator(domainIdeaList.iterator());
		while (reasoningElementCompositeIterator.hasNext()) {
			reasoningElement = reasoningElementCompositeIterator.next();
                        
			if(!visitedElementList.contains(reasoningElement))
				visitedElementList.add(reasoningElement);				
                        else if (reasoningElement instanceof Idea) {				
				Idea connectionIdea = (Idea) reasoningElement;
				if(!connectionIdea.getAddress().contains(rootQuestion)){
					subtreeConnectionIdeaList.add((Idea) reasoningElement);
					Question addressedQuestion = (Question) beforeReasoningElement;
					addressedQuestion.removeIsAddressedBy(connectionIdea);				
					connectionIdea.removeAddress(addressedQuestion);
					disconnectedQuestionIdeaMap.put(addressedQuestion, connectionIdea);
				}
				
			}
			
			beforeReasoningElement = reasoningElement;			
		}
		return disconnectedQuestionIdeaMap;
	 
	}
	
	public static void makeDecisionForAllIdeasInSubtree(Idea domainIdea, boolean isAccepted){
		Question rootQuestion = (Question)domainIdea.listAddress().next();
		KuabaSubsystem.facade.makeDecision(
				rootQuestion,domainIdea, isAccepted);
		HashMap<Question, List<Idea>> connectionQuestionIdeaMap = 
			KuabaGraphUtil.getSubtreeConnectionIdeaMap(domainIdea);
//		System.out.println("conncetionQuestionIdeaMap: " + connectionQuestionIdeaMap);
		HashMap<Idea, Collection<Question>> suggestedQuestionMap = 
			new HashMap<Idea, Collection<Question>>();
		Set<Question> connectionQuestionKeySet = connectionQuestionIdeaMap.keySet();
		for (Question question : connectionQuestionKeySet) {
			List<Idea> connectionIdeaList = connectionQuestionIdeaMap.get(question);
			for (Idea idea : connectionIdeaList) {
				if(!suggestedQuestionMap.containsKey(idea))
					suggestedQuestionMap.put(idea, idea.getSuggests());
				idea.setSuggests(new ArrayList<Question>());
			}			
		}
		 
		ReasoningElementIterator reasoningElementIterator = 
			new ReasoningElementIterator(domainIdea);
		
		while (reasoningElementIterator.hasNext()) {
			ReasoningElement reasoningElement = (ReasoningElement) reasoningElementIterator.next();						
			if (reasoningElement instanceof Question) {				
				Question question = (Question) reasoningElement;							
				Map<Idea, List<Decision>> ideaDecisionMap = constructIdeaDecisionMap(question);
				Set<Idea> ideaKeySet = ideaDecisionMap.keySet();
				for (Idea idea : ideaKeySet) {
					List<Decision> isConcludedByDecisionList = ideaDecisionMap.get(idea);					
					if(!isAccepted){
//						System.out.println(idea);
//						System.out.println(question);
						if(!isConcludedByDecisionList.isEmpty()){
							Decision mostRecentDecision = 
								isConcludedByDecisionList.get(isConcludedByDecisionList.size() -1); 
							if(mostRecentDecision.getIsAccepted())				
								KuabaSubsystem.facade.makeDecision(question, 
										idea, isAccepted);
						}
						else
							KuabaSubsystem.facade.makeDecision(question, 
									idea, isAccepted);
							
					}
					else{
						if(isConcludedByDecisionList.isEmpty())
							KuabaSubsystem.facade.makeDecision(question, 
									idea, isAccepted);
						else{
							Decision mostRecentDecision = 
								isConcludedByDecisionList.get(isConcludedByDecisionList.size() - 1);
							if(isConcludedByDecisionList.size() <= 1 &&	!mostRecentDecision.getIsAccepted())
								KuabaSubsystem.facade.makeDecision(question, 
										idea, isAccepted);
							if(isConcludedByDecisionList.size() > 1 && !mostRecentDecision.getIsAccepted()){
								Decision beforeMostRecentDecision = isConcludedByDecisionList.get(
										isConcludedByDecisionList.size() - 2);
								if(beforeMostRecentDecision.getIsAccepted())
									KuabaSubsystem.facade.makeDecision(question, 
											idea, isAccepted);
							}								
						}
					}
				}			
			}			
		}
		Set<Idea> connectionIdeaKeySet = suggestedQuestionMap.keySet();
		for (Idea idea : connectionIdeaKeySet) 
			idea.setSuggests(suggestedQuestionMap.get(idea));
		
	}
	
	public static HashMap<Idea, List<Decision>> constructIdeaDecisionMap(Question question){
		List<Decision> decisionList = (List<Decision>)question.getHasDecision();
		Collection<Idea> isAddressedByIdea = question.getIsAddressedBy();		
		Collections.sort(decisionList, new DecisionByDateComparator());
		HashMap<Idea, List<Decision>> ideaDecisionMap = new HashMap<Idea, List<Decision>>();
		for (Idea idea : isAddressedByIdea)
			ideaDecisionMap.put(idea, new ArrayList<Decision>());
		
		for (Decision decision : decisionList) {
			if(!ideaDecisionMap.containsKey(decision.getConcludes())){
				List<Decision> newDecisionList = new ArrayList<Decision>();
				newDecisionList.add(decision);
				ideaDecisionMap.put(decision.getConcludes(), newDecisionList);
			}
			else
				ideaDecisionMap.get(decision.getConcludes()).add(decision);				
		}
		return ideaDecisionMap;
	}
	public static List<Idea> setCancopyOnConnectionIdea(Idea domainIdea, boolean canCopy){
		HashMap<Question, List<Idea>> connectionQuestionIdeaMap = 
			KuabaGraphUtil.getSubtreeConnectionIdeaMap(domainIdea);
		List<Idea> subtreeConnectionIdeaList = new ArrayList<Idea>();
		Collection<List<Idea>> connectionIdeaListList = connectionQuestionIdeaMap.values();
		for (List<Idea> list : connectionIdeaListList)
			for (Idea idea : list) {
				idea.setCanCopy(canCopy);
				subtreeConnectionIdeaList.add(idea);
			}
		return subtreeConnectionIdeaList;		
	}
	
	public static void setCanCopyOnIdeaList(List<Idea> ideaList){
		for (Idea idea : ideaList) {
			idea.setCanCopy(true);
		}
	}
	public static boolean containsIdea(Collection<Decision> decisions,
			Idea idea){
		for (Decision decision : decisions) {
			if(decision.getConcludes().equals(idea))
				return true;
		}
		return false;
	}

    public static Idea upperDomainIdea(Idea idea) {
        Question address = idea.getAddress().iterator().next();
        String txt= address.getHasText();
        if(address.getHasText().equals("What are the Model Elements?"))
            return idea;
        else
            return KuabaHelper.upperDomainIdea((Idea)address.getIsSuggestedBy().iterator().next());
    }
        
        
        //TODO finalizar esta funcao
        public void sweepAcceptedSubTree(Idea X,Solution A)
        {
            //Marcar X como um elemento da solução A
            //A.addIdea(X);
            //Se X sugere uma questao Q qualquer
            if(X.hasSuggests()){
                
                Iterator<Question> Itq = X.getSuggests().iterator();
                Question Q;
                //E para cada questao Q:
                while(Itq.hasNext())
                {
                    Q = Itq.next();
                    //Q tem uma decisao D onde d conclui X 
                    Decision D = KuabaHelper.getDecision(Q, X);
                    //e D é aceita
                    if(D.getIsAccepted()){
                        //entao
                        
                    }
                }
            }
            
        }
        
        //Retorna uma ideia de deominio baseado somente no texto
        public static Idea getDomainIdea(String domainText){
        
            Question q = KuabaSubsystem.facade.modelRepository().getQuestionByText("What are the Model Elements?").iterator().next();
            for(Idea d : q.getIsAddressedBy())
                if(d.getHasText().equals(domainText))
                    return d;
            return null;
        }
        
        
}

