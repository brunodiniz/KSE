/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.kuabaModel;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Kildare
 */
public interface Solution extends KuabaElement{
    
    //TODO Criar atributos e referencias aos outros elementos da ontologia. hehehe
    
    //TODO mudar para addSolutionElements solutionIncludesIdea
    //TODO solutionJustification 
    
    //void setQuestion(String question);
    //String getQuestion();
    
    void addContains(Justification newJustification);
    void addIncludes(Idea newIdea);
    /*void addReasoningElement(ReasoningElement newReasoningElement);
    
    void setReasoningElement(Collection<ReasoningElement> newReasoningElement);
    */
    boolean hasContains();
    boolean hasIncludes();
    
    void removeIncludes(Idea oldIdea);

//boolean hasReasoningElement();
    
    Collection<Justification> getContains();
    Collection<Idea> getIncludes();
    /*Collection<ReasoningElement> getReasoningElement();
    
    Iterator<Collection> listReasoningElement();
*/  
    boolean hasAccepted();
    boolean getAccepted();
//    RDFProperty getSuggestsProperty();



    void setAccepted(boolean accepted);
    
    
    
}
