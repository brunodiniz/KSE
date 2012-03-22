/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.rationaleProcessor.unionui;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Bruno
 */
public class DRUnionWizardPanelDescriptor {
    
    private static final String DEFAULT_PANEL_IDENTIFIER = "defaultPanelIdentifier";
    
    
    /**
     * Identifier returned by getNextPanelDescriptor() to indicate that this is the
     * last panel and the text of the 'Next' button should change to 'Finish'.
     */    
    public static final FinishIdentifier FINISH = new FinishIdentifier();
    
    private DRUnionWizard druWizard;
    private Component targetPanel;
    private Object panelIdentifier;
    
    /**
     * Default constructor. The id and the Component panel must be set separately.
     */    
    public DRUnionWizardPanelDescriptor() {
        panelIdentifier = DEFAULT_PANEL_IDENTIFIER;
        targetPanel = new JPanel();
    }
    
    /**
     * Constructor which accepts both the Object-based identifier and a reference to
     * the Component class which makes up the panel.
     * @param id Object-based identifier
     * @param panel A class which extends java.awt.Component that will be inserted as a
     * panel into the druWizard dialog.
     */    
    public DRUnionWizardPanelDescriptor(Object id, Component panel) {
        panelIdentifier = id;
        targetPanel = panel;
    }
   
    /**
     * Returns to java.awt.Component that serves as the actual panel.
     * @return A reference to the java.awt.Component that serves as the panel
     */    
    public final Component getPanelComponent() {
        return targetPanel;
    }
    
    /**
     * Sets the panel's component as a class that extends java.awt.Component
     * @param panel java.awt.Component which serves as the druWizard panel
     */    
    public final void setPanelComponent(Component panel) {
        targetPanel = panel;
    }
    
    /**
     * Returns the unique Object-based identifier for this panel descriptor.
     * @return The Object-based identifier
     */    
    public final Object getPanelDescriptorIdentifier() {
        return panelIdentifier;
    }

    /**
     * Sets the Object-based identifier for this panel. The identifier must be unique
     * from all the other identifiers in the panel.
     * @param id Object-based identifier for this panel.
     */    
    public final void setPanelDescriptorIdentifier(Object id) {
        panelIdentifier = id;
    }
    
    final void setWizard(DRUnionWizard w) {
        druWizard = w;
    }
    
    /**
     * Returns a reference to the druWizard component.
     * @return The druWizard class hosting this descriptor.
     */    
    public final DRUnionWizard getWizard() {
        return druWizard;
    }   

    /**
     * Returns a reference to the current WizardModel for this druWizard component.
     * @return The current WizardModel for this druWizard component.
     */    
    public DRUnionWizardModel getWizardModel() {
        return druWizard.getModel();
    }
    
    //  Override this method to provide an Object-based identifier
    //  for the next panel.
    
    /**
     * Override this class to provide the Object-based identifier of the panel that the
     * user should traverse to when the Next button is pressed. Note that this method
     * is only called when the button is actually pressed, so that the panel can change
     * the next panel's identifier dynamically at runtime if necessary. Return null if
     * the button should be disabled. Return FinishIdentfier if the button text
     * should change to 'Finish' and the dialog should end.
     * @return Object-based identifier.
     */    
    public Object getNextPanelDescriptor() {
        return null;
    }

    //  Override this method to provide an Object-based identifier
    //  for the previous panel.
    
    /**
     * Override this class to provide the Object-based identifier of the panel that the
     * user should traverse to when the Back button is pressed. Note that this method
     * is only called when the button is actually pressed, so that the panel can change
     * the previous panel's identifier dynamically at runtime if necessary. Return null if
     * the button should be disabled.
     * @return Object-based identifier
     */    
    public Object getBackPanelDescriptor() {
        return null;
    }
    
    //  Override this method in the subclass if you wish it to be called
    //  just before the panel is displayed.
    
    /**
     * Override this method to provide functionality that will be performed just before
     * the panel is to be displayed.
     */    
    public void aboutToDisplayPanel() {

    }
 
    //  Override this method in the subclass if you wish to do something
    //  while the panel is displaying.
    
    /**
     * Override this method to perform functionality when the panel itself is displayed.
     */    
    public void displayingPanel() {

    }
 
    //  Override this method in the subclass if you wish it to be called
    //  just before the panel is switched to another or finished.
    
    /**
     * Override this method to perform functionality just before the panel is to be
     * hidden.
     */    
    public void aboutToHidePanel() {

    }    
    
    
    
    static class FinishIdentifier {
        public static final String ID = "FINISH";
    }
    
}
