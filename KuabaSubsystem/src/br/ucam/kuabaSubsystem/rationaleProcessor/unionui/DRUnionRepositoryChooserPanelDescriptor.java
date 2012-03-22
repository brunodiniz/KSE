/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.rationaleProcessor.unionui;

import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Bruno
 */
public class DRUnionRepositoryChooserPanelDescriptor extends DRUnionWizardPanelDescriptor implements ListDataListener, PropertyChangeListener {
    
    public static final String IDENTIFIER = "REPOSITORY_CHOOSER_PANEL";
    
    public DRUnionRepositoryChooserPanelDescriptor() {
        super(IDENTIFIER, new DRUnionRepositoryChooserPanel());
        DRUnionRepositoryChooserPanel panel = (DRUnionRepositoryChooserPanel) getPanelComponent();
        panel.getRepositoryListModel().addListDataListener(this);
        panel.addPropertyChangeListener(DRUnionRepositoryChooserPanel.BASE_PROPERTY, this);
    }
    
    @Override
    public Object getNextPanelDescriptor() {
        if(((DRUnionRepositoryChooserPanel) getPanelComponent()).getRepositoryListModel().size() < 2) return null; 
        return DRUnionEquivalenceSetUpPanelDescriptor.IDENTIFIER;
    }
    
    @Override
    public Object getBackPanelDescriptor() {
        return null;
    }  
    
    @Override
    public void aboutToDisplayPanel() {
        Set<KuabaRepository> set = getWizardModel().getRepositorySet();
        if(set != null) {
            DefaultListModel<KuabaRepository> listModel = ((DRUnionRepositoryChooserPanel) getPanelComponent()).getRepositoryListModel();
            listModel.removeAllElements();
            for (KuabaRepository kr : set) {
                listModel.addElement(kr);
            }
        }
    }
    
    @Override
    public void aboutToHidePanel() {
        DefaultListModel<KuabaRepository> listModel = ((DRUnionRepositoryChooserPanel) getPanelComponent()).getRepositoryListModel();
        Set<KuabaRepository> set = new HashSet<KuabaRepository>();
        for (int x=0; x<listModel.size(); x++) {
            set.add(listModel.get(x));
        }
        
        getWizardModel().setRepositorySet(((DRUnionRepositoryChooserPanel) getPanelComponent()).getBaseRepository(),set);
    }

    public void intervalAdded(ListDataEvent e) {
        if (!getWizard().getNextFinishButtonEnabled()) updateNextFinishButton();
    }

    public void intervalRemoved(ListDataEvent e) {
        if (getWizard().getNextFinishButtonEnabled()) updateNextFinishButton();
    }

    public void contentsChanged(ListDataEvent e) {
        
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DRUnionRepositoryChooserPanel.BASE_PROPERTY)) {
            updateNextFinishButton();
        } 
    }
    
    private void updateNextFinishButton() {
        DRUnionRepositoryChooserPanel panel = (DRUnionRepositoryChooserPanel) getPanelComponent();
        if (panel.getRepositoryListModel().size() >= 2 && panel.getBaseRepository() != null) 
            getWizard().setNextFinishButtonEnabled(true);
        else
            getWizard().setNextFinishButtonEnabled(false);
    }
    
}
