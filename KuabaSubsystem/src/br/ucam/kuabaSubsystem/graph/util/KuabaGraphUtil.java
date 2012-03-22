package br.ucam.kuabaSubsystem.graph.util;

import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class KuabaGraphUtil {

	public static HashMap<ReasoningElement, Integer> getReasoningElementStageMap(Question rootQuestion){
		
		HashMap<ReasoningElement, Integer> reasoningElementStageMap = new HashMap<ReasoningElement, Integer>();
                
		reasoningElementStageMap.put(rootQuestion, 1);
                
		Queue<ReasoningElement> elementsQueue = new LinkedList<ReasoningElement>();
		elementsQueue.offer(rootQuestion);
		Collection adjacentReasoningElementList = new ArrayList();
		
		while (!elementsQueue.isEmpty()){
			ReasoningElement reasoningElement = elementsQueue.poll();
			if(reasoningElement instanceof Question) {                              
				adjacentReasoningElementList =  ((Question)reasoningElement).getIsAddressedBy();
                        } else {
				adjacentReasoningElementList =  ((Idea)reasoningElement).getSuggests();
                        }
			for (Object obj : adjacentReasoningElementList) {
                            ReasoningElement adjacentReasoningElement = (ReasoningElement) obj;
				int reasoningElementStage = reasoningElementStageMap.get(reasoningElement);
				if(!reasoningElementStageMap.containsKey(adjacentReasoningElement)) {
					reasoningElementStageMap.put(adjacentReasoningElement, reasoningElementStage + 1);
                                        elementsQueue.offer(adjacentReasoningElement);	
                                }
			}
		}
		return reasoningElementStageMap;	
	}
	
	public static HashMap<Question, List<Idea>> getSubtreeConnectionIdeaMap(Idea rootDomainIdea){
		HashMap<Question, List<Idea>> connectionQuestionIdeaMap = 
			new HashMap<Question, List<Idea>>();
		Question rootWhatElementsQuestion = 
			(Question)rootDomainIdea.listAddress().next();
		Collection<ReasoningElement> adjacentReasoningElementList =
			new ArrayList<ReasoningElement>();
		Map<ReasoningElement, Integer> reasoningElementStageMap = 
			getReasoningElementStageMap(rootWhatElementsQuestion);		
		int lastReasoningElementStage = -1;		
		Stack<ReasoningElement> dFSReasoningElementStack = new Stack<ReasoningElement>();
		dFSReasoningElementStack.push(rootDomainIdea.listSuggests().next());
		ReasoningElement fatherReasoningElement = null;
		while (!dFSReasoningElementStack.isEmpty()) {
			ReasoningElement reasoningElement = dFSReasoningElementStack.pop();
			
			if(reasoningElementStageMap.get(reasoningElement) < lastReasoningElementStage){
				if(connectionQuestionIdeaMap.containsKey(fatherReasoningElement)){
					List<Idea> connectionIdeaList = connectionQuestionIdeaMap.get(fatherReasoningElement);
					connectionIdeaList.add((Idea)reasoningElement);					
				}
				else{
					List<Idea> connectionIdeaList = new ArrayList<Idea>();
					connectionIdeaList.add((Idea)reasoningElement);
					connectionQuestionIdeaMap.put((Question)fatherReasoningElement, connectionIdeaList);
				}
			}
			else{
				if (reasoningElement instanceof Question) {
					Question fatherQuestion = (Question) reasoningElement;
					adjacentReasoningElementList= (Collection) fatherQuestion.getIsAddressedBy();
					fatherReasoningElement = fatherQuestion;
				}
				else{
					if (reasoningElement instanceof Idea) {
						Idea fatherIdea = (Idea) reasoningElement;
						adjacentReasoningElementList = (Collection) fatherIdea.getSuggests();
						fatherReasoningElement = fatherIdea;
					}					
				}
				for (ReasoningElement adjacentReasoningElement : adjacentReasoningElementList)
					dFSReasoningElementStack.push(adjacentReasoningElement);
				lastReasoningElementStage = reasoningElementStageMap.get(reasoningElement);
			}			
		}
		return connectionQuestionIdeaMap;		
	}
        
//        public void copyDesignIdeaSubtree(Idea rejectedDesignIdea, Idea acceptedDesignIdea, Map<ReasoningElement, Integer> stageMap) {
//            KuabaModelFactory factory = acceptedDesignIdea.getRepository().getModelFactory();
//            
//            Queue<ReasoningElement> q = new LinkedList<ReasoningElement>();
//            q.addAll(rejectedDesignIdea.getSuggests());
//            
//            ReasoningElement pai = rejectedDesignIdea;
//            int lvl = stageMap.get(rejectedDesignIdea);
//            
//            while (!q.isEmpty()) {
//                ReasoningElement cRE = q.poll();
//                int cLvl = stageMap.get(cRE);
//                
//                if(cLvl<lvl)
//                    continue;
//                else if(cLvl>lvl) {
//                    lvl = cLvl;
//                }
//                
//                if (cRE instanceof Idea) {
//                    Idea clone = cloneIdea((Idea) cRE);
//                } else {
//                    
//                }
//            }
//        }
//        
//        private Idea cloneIdea(Idea idea) {
//            KuabaModelFactory factory = idea.getRepository().getModelFactory();
//            Idea resp = factory.createIdea(UUID.randomUUID().toString());
//            resp.setHasText(idea.getHasText());
//            
//            for (Decision d: idea.getIsConcludedBy()) {
//                d.setConcludes(resp);
//                resp.addIsConcludedBy(d);
//            }
//            
//            return resp;
//        }
//        
//        private Question cloneQuestion(Question question) {
//            KuabaModelFactory factory = question.getRepository().getModelFactory();
//            Question resp = factory.createQuestion(UUID.randomUUID().toString());
//            resp.setHasText(question.getHasText());
//            
//            return resp;
//        }
	
}
