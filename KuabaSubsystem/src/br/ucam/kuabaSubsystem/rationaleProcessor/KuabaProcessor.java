/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.rationaleProcessor;

import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.UUID;

/**
 *
 * @author Bruno
 */
public class KuabaProcessor {
    
    private EquivalenceRuleManager equivalences;
    
    public boolean compareQuestions(Question q1, Question q2) {
        if(isTextEquivalent(q1.getHasText(), q2.getHasText())) {
            Collection<ReasoningElement> suggestedBy1 = q1.getIsSuggestedBy();
            Collection<ReasoningElement> suggestedBy2 = q2.getIsSuggestedBy();
            
            for (ReasoningElement r1 : suggestedBy1) {
               if(!(r1 instanceof Idea)) continue;
               
               boolean hasEquivalent = false;
               
               for (ReasoningElement r2 : suggestedBy2) {
                   if(!(r2 instanceof Idea)) continue;
                   
                   if (compareIdeas((Idea) r1, (Idea) r2)) hasEquivalent = true;
               }
               
               if(!hasEquivalent) return false;
            }
            
            return true;
        }
            
        return false;
    }
    
    public boolean compareIdeas(Idea i1, Idea i2) {
        FormalModel f1,f2;
        f1=i1.getIsDefinedBy();
        f2=i2.getIsDefinedBy();
        
        String text1,text2;
        text1 = i1.getHasText();
        text2 = i2.getHasText();
        
        if (f1==null && f2==null) { //domain ideas
            if(isTextEquivalent(text1, text2)) {
//               System.out.println("compare domain => "+text1+" - "+text2);
               Collection<Question> addressedQuestions1 = i1.getAddress();
               Collection<Question> addressedQuestions2 = i2.getAddress();
               
               for (Question q1 : addressedQuestions1) {
                   boolean hasEquivalent = false;
                   for (Question q2 : addressedQuestions2) {
                       if (isTextEquivalent(q1.getHasText(), q2.getHasText())) hasEquivalent = true;
                   }
                   if(!hasEquivalent) return false;
               }
               
               return true;
                
            }
            else return false;
        }
        
        if (f1!=null && f2!=null) { //design ideas
            if(isTextEquivalent(text1, text2)) {
//               System.out.println("compare design => "+text1+" - "+text2);
               Collection<Question> addressedQuestions1 = i1.getAddress();
               Collection<Question> addressedQuestions2 = i2.getAddress();
               
               for (Question q1 : addressedQuestions1) {
                   for (Question q2 : addressedQuestions2) {
//                       if (isTextEquivalent(q1.getHasText(), q2.getHasText())) return true;
                       if (compareQuestions(q1, q2)) return true;
                   }
               }        
            }
        }
        return false;
    }
    
    private boolean isTextEquivalent(String text1, String text2) {
        if(text1.toLowerCase().equals(text2.toLowerCase())) return true;
        
        String s1,s2;
        
        if(text1.startsWith(Question.DOMAIN_QUESTION_TEXT_PREFIX) && text2.startsWith(Question.DOMAIN_QUESTION_TEXT_PREFIX)) {
            s1 = text1.replaceAll("\\?(?!.*\\?)", "").substring(Question.DOMAIN_QUESTION_TEXT_PREFIX.length()).toLowerCase();
            s2 = text2.replaceAll("\\?(?!.*\\?)", "").substring(Question.DOMAIN_QUESTION_TEXT_PREFIX.length()).toLowerCase();
        } else {
            s1 = text1.toLowerCase();
            s2 = text2.toLowerCase();
        }
        
        return equivalences.isEquivalent(s1, s2);
    }
    
    public KuabaRepository union (KuabaRepository base, Set<KuabaRepository> originals) {
        KuabaRepository result = null, previous=null;
        
        for (KuabaRepository original : originals) {
            if (base.equals(original)) continue;
            
            if(previous == null) {
                previous = base;
            } else {
                if (!previous.equals(base)) previous.dispose();
                previous = result;
            }
                
            result = union(previous, original);
        }
        
        if(previous!=null && !previous.equals(base)) previous.dispose();
        return result;
    }
    
    public KuabaRepository union (KuabaRepository base, KuabaRepository original) {
        

        KuabaRepository result = base.copy();
        
        HashMap<ReasoningElement, ReasoningElement> copyMap = new HashMap<ReasoningElement, ReasoningElement>();
        HashMap<ReasoningElement, ReasoningElement> fatherMap = new HashMap<ReasoningElement, ReasoningElement>();
        
        Queue<ReasoningElement> q = new LinkedList<ReasoningElement>();
        
        q.add(original.getQuestion(Question.ROOT_QUESTION_ID));
        
        
        while (!q.isEmpty()) {
            ReasoningElement re = q.remove();
//            System.out.println(re.getHasText());
            
            ReasoningElement mappedRE = copyMap.get(re);
            
            if (mappedRE!=null) continue;
            

            
            if (re instanceof Question) { 
                
                if(!re.getId().equals(Question.ROOT_QUESTION_ID)) { //caso seja a questao raiz, pula o mapeamento comum

                    Collection<Question> baseFatherSuggestedQuestions = ((Idea)fatherMap.get(re)).getSuggests();

                    for (Question baseQuestion : baseFatherSuggestedQuestions) {
                        if (compareQuestions((Question) re, baseQuestion)) {
                            copyMap.put(re, baseQuestion);
                            break;
                        }
                    }
                    
                    if (!copyMap.containsKey(re)) { //copia pra base
                        Question copiedQuestion = result.getModelFactory().createQuestion(UUID.randomUUID().toString());
                        copiedQuestion.setHasText(re.getHasText());
                        Idea baseFatherIdea = (Idea) fatherMap.get(re);
                        baseFatherIdea.addSuggests(copiedQuestion);
                        
                        copyMap.put(re, copiedQuestion);
                        
                        // cria relações com elementos já mapeados
                        Collection<Idea> ideasThatAddress = ((Question) re).getIsAddressedBy();
                        for (Idea idea : ideasThatAddress) {
                            if(copyMap.containsKey(idea)) {
                                Idea baseIdea = (Idea) copyMap.get(idea);
                                copiedQuestion.addIsAddressedBy(baseIdea);
                            }
                        }
                    }
                }
                else 
                    copyMap.put(original.getQuestion(Question.ROOT_QUESTION_ID), result.getQuestion(Question.ROOT_QUESTION_ID));
                
                q.addAll(((Question) re).getIsAddressedBy());
                
                for (Idea i : ((Question) re).getIsAddressedBy()) 
                    if (!fatherMap.containsKey(i)) fatherMap.put(i, copyMap.get(re));
                
            } else if (re instanceof Idea) {

                Collection<Idea> baseFatherIdeasThatAddress = ((Question)fatherMap.get(re)).getIsAddressedBy();

                for (Idea baseId : baseFatherIdeasThatAddress) {
                    if (compareIdeas((Idea) re, baseId)) {
                        copyMap.put(re, baseId);
                        break;
                    }
                }

                if (!copyMap.containsKey(re)) { //copia pra base
                    Idea copiedIdea = result.getModelFactory().createIdea(UUID.randomUUID().toString());
                    copiedIdea.setHasText(re.getHasText());
                    Question baseFatherQuestion = (Question) fatherMap.get(re);
                    baseFatherQuestion.addIsAddressedBy(copiedIdea);

                    copyMap.put(re, copiedIdea);
                    
                    // cria relações com elementos já mapeados
                    Collection<Question> suggestedQuestions = ((Idea) re).getSuggests();
                    for (Question question : suggestedQuestions) {
                        if(copyMap.containsKey(question)) {
                            Question baseQuestion = (Question) copyMap.get(question);
                            copiedIdea.addSuggests(baseQuestion);
                        }
                    }
                }
                
                q.addAll(((Idea) re).getSuggests());
                
                for (Question question : ((Idea) re).getSuggests()) 
                    if (!fatherMap.containsKey(question)) fatherMap.put(question, copyMap.get(re));
                
            }
            else return null; //erro
            
        }
               
        
        copyArguments(original.getAllArguments(), result, copyMap);
        
        for(Decision d : result.getAllDecisions()) {
            d.remove();
        }
        
        return result;
    }
    
    private void copyArguments(Collection<Argument> arguments, KuabaRepository destination, Map<ReasoningElement, ReasoningElement> copyMap) {
        
        for (Argument arg : arguments) {
            Argument copiedArgument;
            
//            if (copyMap.containsKey(arg))
//                copiedArgument = (Argument) copyMap.get(arg);
//            else 
                copiedArgument = destination.getModelFactory().createArgument(UUID.randomUUID().toString());
                
            copiedArgument.setHasText(arg.getHasText());
            Question consideredQuestion = arg.getConsiders();
            if(consideredQuestion != null && copyMap.containsKey(consideredQuestion)) {
                copiedArgument.setConsiders((Question) copyMap.get(consideredQuestion));
            }
            
            if(arg.hasInFavorOf())
                for (Idea i : arg.getInFavorOf()) {
                    if (copyMap.containsKey(i)) {
                        Idea mappedIdea = (Idea) copyMap.get(i);
                        copiedArgument.addInFavorOf(mappedIdea);
                        mappedIdea.addHasArgument(copiedArgument);
                    }
                }
            
            if(arg.hasObjectsTo())
                for (Idea i : arg.getObjectsTo()) {
                    if (copyMap.containsKey(i)) {
                        Idea mappedIdea = (Idea) copyMap.get(i);
                        copiedArgument.addObjectsTo(mappedIdea);
                        mappedIdea.addHasArgument(copiedArgument);
                    }
                }
            
//            não é necessário, a menos que copie o isVersionOf
//            copyMap.put(arg, copiedArgument);
        }
        
    }

    public void setEquivalences(EquivalenceRuleManager equivalences) {
        this.equivalences = equivalences;
    }
    
}
