/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.rationaleProcessor.unionui;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Bruno
 */
public class DRUnionEquivalenceSetUpPanelDescriptor extends DRUnionWizardPanelDescriptor implements PropertyChangeListener {
    
    public static final String IDENTIFIER = "EQUIVALENCE_DEFINER_PANEL";
    
    public DRUnionEquivalenceSetUpPanelDescriptor() {
        super(IDENTIFIER, new DRUnionEquivalenceSetUpPanel());
    }
    
    @Override
    public Object getNextPanelDescriptor() {
        return DRUnionDecisionSetUpPanelDescriptor.IDENTIFIER;
    }
    
    @Override
    public Object getBackPanelDescriptor() {
        return DRUnionRepositoryChooserPanelDescriptor.IDENTIFIER;
    }  
    
    @Override
    public void aboutToDisplayPanel() {

    }

    @Override
    public void aboutToHidePanel() {
        DRUnionEquivalenceSetUpPanel panel = ((DRUnionEquivalenceSetUpPanel) this.getPanelComponent());
        if(panel.doChanges()) {
            getWizardModel().setEquivalences(panel.getEquivalences());
        }
    }
    
    public void setDomainIdeasText(KuabaRepository base, Set<KuabaRepository> repositoryList) {
        SortedSet<String> baseDomainIdeasText = new TreeSet<String>();
        SortedSet<String> otherDomainIdeasText = new TreeSet<String>();
        
        //base
        Question baseRoot = base.getQuestion(Question.ROOT_QUESTION_ID);
        Collection<Idea> baseDomainIdeas = baseRoot.getIsAddressedBy();
        for (Idea domainIdea : baseDomainIdeas) {
            baseDomainIdeasText.add(domainIdea.getHasText().toLowerCase());
        }
        
        //others
        for (KuabaRepository repo : repositoryList) {
            if(repo.equals(base)) continue;
            Question root = repo.getQuestion(Question.ROOT_QUESTION_ID);
            Collection<Idea> domainIdeas = root.getIsAddressedBy();
            for (Idea domainIdea : domainIdeas) {
                String ideaText = domainIdea.getHasText().toLowerCase();
                
                //ignoring ideas that share the same text of base domain ideas
                if (baseDomainIdeasText.contains(ideaText)) continue;
                otherDomainIdeasText.add(ideaText);
            }
        }
        
        ((DRUnionEquivalenceSetUpPanel) this.getPanelComponent()).setDomainIdeasText(baseDomainIdeasText, otherDomainIdeasText);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(DRUnionWizardModel.REPOSITORY_SET_PROPERTY)) {
            setDomainIdeasText(this.getWizardModel().getBaseRepository(), this.getWizardModel().getRepositorySet());
        }
    }
    
}
