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
import java.util.*;
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
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.GraphNodeRenderer;
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
        // end of the "new" action
        
        //starting the wizard
        DRUnionWizard druWiz = new DRUnionWizard(ArgoFrame.getInstance());
        int ret = druWiz.showModalDialog();
        
        if (ret == DRUnionWizard.FINISH_RETURN_CODE) {
            KuabaRepository result = druWiz.getUnionResult();
            KuabaSubsystem.gateway.save(result, new File("designRationale/tempDrUnion.xml"));
            
            //begin of the creation of the class diagram
            KuabaSubsystem.eventPump.stopPumpingEvents();
            
            try {
                result = KuabaSubsystem.gateway.load("designRationale/tempDrUnion.xml");
            } catch (RepositoryLoadException ex) {
                Logger.getLogger(ActionNewDRBasedProject.class.getName()).log(Level.SEVERE, "Ontology file missing - Aborting");
                return;
            }
            
            buildClassDiagram(diag, result);
            
            KuabaSubsystem.gateway.save(result, new File("designRationale/tempDrUnion.xml"));
            Main.initKuabaSubsystem(true, "designRationale/tempDrUnion.xml");
        }
        
        KuabaSubsystem.eventPump.startPumpingEvents();
    }
    
    public void buildClassDiagram(ArgoDiagram diag, KuabaRepository unionResult) {
        
        Question root = unionResult.getQuestion(Question.ROOT_QUESTION_ID);
            
        GraphEdgeRenderer edgeRend = diag.getLayer().getGraphEdgeRenderer();
        GraphNodeRenderer nodeRend = diag.getLayer().getGraphNodeRenderer();

        Object ns = diag.getNamespace();
        if (ns != null) {
            int count = 0;
            List<Idea> acceptedDomainIdeas = unionResult.getAcceptedIdeas(root);

            Map<Idea, Object> diagramElementsMap = new HashMap<Idea, Object>();

            //creating classes on the diagram
            for (Idea i: acceptedDomainIdeas) {
                Idea designIdea = KuabaHelper.getAcceptedDesignIdea(i, "Class");
                if(designIdea != null) {

                    Object peer = Model.getCoreFactory().buildClass(i.getHasText(),ns);

                    Fig f = nodeRend.getFigNodeFor(diag.getGraphModel(), diag.getLayer(), peer, Collections.EMPTY_MAP);
                    f.setLocation(140*(count%3)+60, (int)(120*((count/3)+1)));
                    count++;
                    diagramElementsMap.put(designIdea, peer);

                    i.setId(UUID.randomUUID().toString()+"_"+Model.getFacade().getUUID(peer).split(":")[3]);
                    ((ClassDiagramGraphModel) diag.getGraphModel()).addNode(peer);
                    diag.add(f);              

                    //adding the classes' attributes and operations
                    for (Idea i2: acceptedDomainIdeas) {
                        Idea designIdea2 = KuabaHelper.getAcceptedDesignIdea(i2, "Attribute");
                        if(designIdea2 != null && designIdea2.listSuggests().next().listIsAddressedBy().next().equals(designIdea)) {
                            Project project = ProjectManager.getManager().getCurrentProject();
                            Object attrType = project.getDefaultAttributeType();
                            Object attr =
                                Model.getCoreFactory().buildAttribute2(
                                    peer,
                                    attrType);
                            Model.getCoreHelper().setName(attr, i2.getHasText());
                            i2.setId(UUID.randomUUID().toString()+"_"+Model.getFacade().getUUID(attr).split(":")[3]);
                        } else {
                            designIdea2 = KuabaHelper.getAcceptedDesignIdea(i2, "Operation");
                            if(designIdea2 != null && designIdea2.listSuggests().next().listIsAddressedBy().next().equals(designIdea)) {
                                Project project = ProjectManager.getManager().getCurrentProject();
                                Object returnType = project.getDefaultReturnType();
                                Object oper = Model.getCoreFactory().buildOperation(peer, returnType);
                                Model.getCoreHelper().setName(oper, i2.getHasText());
                                i2.setId(UUID.randomUUID().toString()+"_"+Model.getFacade().getUUID(oper).split(":")[3]);
                            }   
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
                    Fig newEdge = edgeRend.getFigEdgeFor(diag.getGraphModel(), diag.getLayer(), association, Collections.EMPTY_MAP);

                    diag.add(newEdge);

                    Model.getCoreHelper().setName(association, i.getHasText());
                }
            }
        }
    }
    
}
