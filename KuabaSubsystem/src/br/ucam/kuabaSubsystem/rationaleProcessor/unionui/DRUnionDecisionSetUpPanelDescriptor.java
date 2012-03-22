/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.rationaleProcessor.unionui;

import br.ucam.kuabaSubsystem.rationaleProcessor.EquivalenceRuleManager;
import br.ucam.kuabaSubsystem.rationaleProcessor.KuabaProcessor;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 *
 * @author Bruno
 */
public class DRUnionDecisionSetUpPanelDescriptor extends DRUnionWizardPanelDescriptor implements PropertyChangeListener{
    
    public static final String IDENTIFIER = "DECISION_MAKER_PANEL";
    private KuabaProcessor kp = new KuabaProcessor();
    private KuabaRepository union = null;
    
    public DRUnionDecisionSetUpPanelDescriptor() {
        super(IDENTIFIER, new DRUnionDecisionSetUpPanel());
    }
    
    @Override
    public Object getNextPanelDescriptor() {
//        return null;
        return DRUnionWizardPanelDescriptor.FINISH;
    }
    
    @Override
    public Object getBackPanelDescriptor() {
        return DRUnionEquivalenceSetUpPanelDescriptor.IDENTIFIER;
    }

    @Override
    public void aboutToDisplayPanel() {     
        ((DRUnionDecisionSetUpPanel) this.getPanelComponent()).setPreferredSize(new Dimension(800, 550));
        this.getWizard().getDialog().pack();
    }

    @Override
    public void aboutToHidePanel() {
        DRUnionDecisionSetUpPanel panel = (DRUnionDecisionSetUpPanel) this.getPanelComponent();
        panel.setPreferredSize(new Dimension(400, 300));
        panel.applyDecisions();
        this.getWizard().getDialog().pack();
        this.getWizardModel().setUnionResult(union);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(DRUnionWizardModel.EQUIVALENCES_PROPERTY)) {
            
            kp.setEquivalences((EquivalenceRuleManager) evt.getNewValue());

            union = kp.union(this.getWizardModel().getBaseRepository(), this.getWizardModel().getRepositorySet());

            ((DRUnionDecisionSetUpPanel) this.getPanelComponent()).showDecisionMakingGraph(union);
        }
    }
    
    
}
