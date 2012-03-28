/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.argouml.module.kuabaModule;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.rationaleProcessor.unionui.DRUnionWizard;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.repositories.RepositoryLoadException;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import org.argouml.application.Main;
import org.argouml.cognitive.Designer;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.static_structure.ui.ClassDiagramRenderer;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.presentation.Fig;

/**
 *
 * @author Bruno
 */
public class ActionNewDRBasedProject extends AbstractAction {

    /**
     * The constructor.
     */
    public ActionNewDRBasedProject() {
//        // Set the name and icon:
//        super(Translator.localize("action.new"),
//                ResourceLoaderWrapper.lookupIcon("action.new"));
//        // Set the tooltip string:
//        putValue(Action.SHORT_DESCRIPTION,
//                Translator.localize("action.new"));
        
        super("New Design Rationale Based Project");
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        
        DRUnionWizard druWiz = new DRUnionWizard(ArgoFrame.getInstance());
        int ret = druWiz.showModalDialog();
        
        if (ret == DRUnionWizard.FINISH_RETURN_CODE) {
            KuabaRepository result = druWiz.getUnionResult();
            KuabaSubsystem.gateway.save(result, new File("designRationale/tempDrUnion.xml"));
            
            //"new" action (creating a new empty project)
            Model.getPump().flushModelEvents();
            Model.getPump().stopPumpingEvents();
            Model.getPump().flushModelEvents();
            Project p = ProjectManager.getManager().getCurrentProject();

            if (getValue("non-interactive") == null) {
                if (!ProjectBrowser.getInstance().askConfirmationAndSave()) {
                    return;
                }
            }

            ProjectBrowser.getInstance().clearDialogs();
            Designer.disableCritiquing();
            Designer.clearCritiquing();
            // clean the history
            TargetManager.getInstance().cleanHistory();
            p.remove();
            p = ProjectManager.getManager().makeEmptyProject();
            ArgoDiagram diag = p.getDiagramList().get(0);
            TargetManager.getInstance().setTarget(diag);
            Designer.enableCritiquing();
            Model.getPump().startPumpingEvents();
            
            
            //begin of the creation of the class diagram
            KuabaSubsystem.eventPump.stopPumpingEvents();
            
            try {
                result = KuabaSubsystem.gateway.load("designRationale/tempDrUnion.xml");
            } catch (RepositoryLoadException ex) {
                Logger.getLogger(ActionNewDRBasedProject.class.getName()).log(Level.SEVERE, "Ontology file missing - Aborting");
                return;
            }
            Question root = result.getQuestion(Question.ROOT_QUESTION_ID);
            
            ClassDiagramRenderer renderer = new ClassDiagramRenderer();
            
            Object ns = diag.getNamespace();
            if (ns != null) {
                int count = 0;
                List<Idea> acceptedDomainIdeas = result.getAcceptedIdeas(root);
                
                Map<Idea, Object> diagramElementsMap = new HashMap<Idea, Object>();
                
                //creating classes on the diagram
                for (Idea i: acceptedDomainIdeas) {
                    Idea designIdea = KuabaHelper.getAcceptedDesignIdea(i, "Class");
                    if(designIdea != null) {
                        
                        Object peer = Model.getCoreFactory().buildClass(i.getHasText(),ns);
                        
                        Fig f = renderer.getFigNodeFor(diag.getGraphModel(), diag.getLayer(), peer, null);
                        f.setLocation(140*(count%3)+60, (int)(120*((count/3)+1)));
                        count++;
                        diagramElementsMap.put(designIdea, peer);

                        i.setId(UUID.randomUUID().toString()+"_"+Model.getFacade().getUUID(peer).split(":")[3]);
                        diag.add(f);              
                        
                        //adding the classes' attributes
                        for (Idea i2: acceptedDomainIdeas) {
                            Idea designIdea2 = KuabaHelper.getAcceptedDesignIdea(i2, "Attribute");
                            if(designIdea2 != null && designIdea2.getSuggests().iterator().next().getIsAddressedBy().iterator().next().equals(designIdea)) {
                                Project project = ProjectManager.getManager().getCurrentProject();
                                Object attrType = project.getDefaultAttributeType();
                                Object attr =
                                    Model.getCoreFactory().buildAttribute2(
                                        peer,
                                        attrType);
                                Model.getCoreHelper().setName(attr, i2.getHasText());
                                i2.setId(UUID.randomUUID().toString()+"_"+Model.getFacade().getUUID(attr).split(":")[3]);
                            }
                        }
                    }
                }
                
                //creating associations on the diagram
                for (Idea i: acceptedDomainIdeas) {
                    Idea designIdea = KuabaHelper.getAcceptedDesignIdea(i, "Association");
                    if(designIdea != null) {
                        Object[] associationEnds = designIdea.listSuggests().next().getIsAddressedBy().toArray();
                        Idea associationParticipant1 = ((Idea) associationEnds[0]).listSuggests().next().listIsAddressedBy().next();
                        Idea associationParticipant2 = ((Idea) associationEnds[1]).listSuggests().next().listIsAddressedBy().next();
                        Object association = Model.getCoreFactory().buildAssociation(diagramElementsMap.get(associationParticipant1), diagramElementsMap.get(associationParticipant2));                       
                        i.setId(UUID.randomUUID().toString()+"_"+Model.getFacade().getUUID(association).split(":")[3]);
                        Fig newEdge = renderer.getFigEdgeFor(diag.getGraphModel(), diag.getLayer(), association, null);

                        diag.add(newEdge);
                        
                        Model.getCoreHelper().setName(association, i.getHasText());
                    }
                }
            }
            KuabaSubsystem.gateway.save(result, new File("designRationale/tempDrUnion.xml"));
            Main.initKuabaSubsystem(true, "designRationale/tempDrUnion.xml");
        }
        KuabaSubsystem.eventPump.startPumpingEvents();
    }
      
}
