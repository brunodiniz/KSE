/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.rationaleProcessor.unionui;

import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Bruno
 */
public class DRUnionRepositoryChooserPanelDescriptor extends DRUnionWizardPanelDescriptor implements ListDataListener, PropertyChangeListener {
    
    public static final String IDENTIFIER = "REPOSITORY_CHOOSER_PANEL";
    private static final int MIN_REPOSITORY_SELECTION = 1;
    
    public DRUnionRepositoryChooserPanelDescriptor() {
        super(IDENTIFIER, new DRUnionRepositoryChooserPanel());
        DRUnionRepositoryChooserPanel panel = (DRUnionRepositoryChooserPanel) getPanelComponent();
        panel.getRepositoryListModel().addListDataListener(this);
        panel.addPropertyChangeListener(DRUnionRepositoryChooserPanel.BASE_PROPERTY, this);
    }
    
    @Override
    public Object getNextPanelDescriptor() {
        if(((DRUnionRepositoryChooserPanel) getPanelComponent()).getRepositoryListModel().size() < MIN_REPOSITORY_SELECTION) return null; 
        return DRUnionEquivalenceSetUpPanelDescriptor.IDENTIFIER;
    }
    
    @Override
    public Object getBackPanelDescriptor() {
        return null;
    }  
    
    @Override
    public void aboutToDisplayPanel() {
//        Set<KuabaRepository> set = getWizardModel().getRepositorySet();
//        if(set != null) {
//            Map<String, KuabaRepository> repoMap = ((DRUnionRepositoryChooserPanel) getPanelComponent()).getRepositoryMap();
//            DefaultListModel<String> listModel = ((DRUnionRepositoryChooserPanel) getPanelComponent()).getRepositoryListModel();
//            listModel.removeAllElements();
//            for (KuabaRepository kr : set) {
//                listModel.addElement(kr);
//            }
//        }
    }
    
    @Override
    public void aboutToHidePanel() {
        Map<String, KuabaRepository> repoMap = ((DRUnionRepositoryChooserPanel) getPanelComponent()).getRepositoryMap();
        Set<KuabaRepository> set = new HashSet<KuabaRepository>();
        Set<String> keys = repoMap.keySet();
        for (String key : keys) {
            set.add(repoMap.get(key));
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
        if (panel.getRepositoryListModel().size() >= MIN_REPOSITORY_SELECTION && panel.getBaseRepository() != null) 
            getWizard().setNextFinishButtonEnabled(true);
        else
            getWizard().setNextFinishButtonEnabled(false);
    }
    
}
