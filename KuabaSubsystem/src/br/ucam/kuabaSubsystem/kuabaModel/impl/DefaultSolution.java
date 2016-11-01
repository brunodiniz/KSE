/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.kuabaModel.impl;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;
import br.ucam.kuabaSubsystem.kuabaModel.Solution;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import java.util.Collection;
import java.util.Collections;


/**
 *
 * @author Kildare
 */
public class DefaultSolution extends DefaultKuabaElement implements Solution {

    
    /*String question;
    
    public void setQuestion(String question){
        this.question=question;
    }
    public String getQuestion(){
        return this.question;
    }*/
    
    public DefaultSolution(String id, KuabaRepository repo) {
        super(id, repo);
    }

    public void addContains(Justification newJustification) {
        addObjectPropertyValue("contains", newJustification);
    }

    public void addIncludes(Idea newIdea) {
        addObjectPropertyValue("includes", newIdea);
    }
    
    public void removeIncludes(Idea oldIdea) {
        removeObjectPropertyValue("includes", oldIdea);
    }
    
    

    public boolean hasContains() {
        return hasProperty("contains");
    }

    public boolean hasIncludes() {
        return hasProperty("includes");
    }


    public Collection<Justification> getContains() {
        Collection c = getObjectPropertyValues("contains");
        if (c.isEmpty()) return null;
        
        return c;
    }

    public Collection<Idea> getIncludes() {
        Collection c = getObjectPropertyValues("includes");
        if(c.isEmpty()) return null;
        return c;
    }

    public boolean hasAccepted() {
        return hasProperty("accepted");
    }

    public boolean getAccepted() {
        Collection<String> c = getDataPropertyValues("accepted");
        if (c.isEmpty()) return false;
        
        return c.iterator().next().equals(Boolean.toString(true));
    }

    public void setAccepted(boolean accepted){
        Collection c = Collections.singleton(Boolean.toString(accepted));
        setDataPropertyValues("accepted", c);
    }




}
