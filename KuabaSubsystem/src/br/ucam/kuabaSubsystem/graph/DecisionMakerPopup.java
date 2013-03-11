/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.graph;

import br.ucam.kuabaSubsystem.kuabaModel.Question;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Bruno
 */
public class DecisionMakerPopup extends MouseAdapter {

    private JPopupMenu popup;
    private mxGraph graph;
    private mxGraphComponent graphComp;

    public DecisionMakerPopup(mxGraph graph, mxGraphComponent graphComp) {
        super();
        
        this.graph = graph;
        this.graphComp = graphComp;

        popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Accept Selection");
        menuItem.setAction(new DecisionMakerPopup.PopupAcceptSelectionAction());
        popup.add(menuItem);
        menuItem = new JMenuItem("Reject Selection");
        menuItem.setAction(new DecisionMakerPopup.PopupRejectSelectionAction());
        popup.add(menuItem);
        menuItem = new JMenuItem("Clear Decision");
        menuItem.setAction(new DecisionMakerPopup.PopupClearDecisionsAction());
        popup.add(menuItem);
    }


    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        mxCell selection = (mxCell) graph.getSelectionCell();
        if (e.isPopupTrigger() && (graph.getSelectionCount() > 0) && selection.isEdge() && selection.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) {
            popup.show(e.getComponent(),
                        e.getX(), e.getY());
        }
    }
    
    //popup Action Classes

    class PopupAcceptSelectionAction extends AbstractAction {

        public PopupAcceptSelectionAction() {
            super("Accept Selection");
        }

        private void acceptRelation(mxCell isAddressedBy) {
            isAddressedBy.setStyle(KuabaGraph.ACCEPTED_IS_ADDRESSED_BY_STYLE);
            isAddressedBy.setValue(KuabaGraph.ACCEPTED_IS_ADDRESSED_BY_LABEL);
            
            mxICell sourceCell = isAddressedBy.getSource();
            
            if (((String)sourceCell.getValue()).contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)) {
                for (int x = 0; x<sourceCell.getEdgeCount(); x++) {
                    mxCell edge = (mxCell) sourceCell.getEdgeAt(x);
                    if (edge.getId().contains(KuabaGraph.SUGGESTS_ID_COMPONENT)) {
                        mxCell domainIdea = (mxCell) edge.getSource();
                        for (int y = 0; y<domainIdea.getEdgeCount(); y++) {
                            mxCell upperEdge = (mxCell) domainIdea.getEdgeAt(y);
                            if (upperEdge.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT) && upperEdge.getSource().getId().equals(Question.ROOT_QUESTION_ID)) {
                                upperEdge.setStyle(KuabaGraph.ACCEPTED_IS_ADDRESSED_BY_STYLE);
                                upperEdge.setValue(KuabaGraph.ACCEPTED_IS_ADDRESSED_BY_LABEL);
                            }
                        }
                    }
                }  
            } else if (sourceCell.getStyle().contains(KuabaGraph.IDEA_STYLE)) { //in case of showing only ideas
                for (int x = 0; x<sourceCell.getEdgeCount(); x++) {
                    mxCell edge = (mxCell) sourceCell.getEdgeAt(x);
                    if (edge.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT) && edge.getSource().getId().equals(Question.ROOT_QUESTION_ID)) { // only the edges linked with the root question have label
                        edge.setStyle(KuabaGraph.ACCEPTED_IS_ADDRESSED_BY_STYLE);
                        edge.setValue(KuabaGraph.ACCEPTED_IS_ADDRESSED_BY_LABEL);
                    }
                }
            }
        }

        public void actionPerformed(ActionEvent e) {
            mxCell cell = (mxCell) graph.getSelectionCell();

            if(cell.isEdge() && cell.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) {
                graph.getModel().beginUpdate();
                acceptRelation(cell);
                graph.getModel().endUpdate();
                graphComp.refresh();
            }
            
            graph.clearSelection();
        }

    }

    class PopupRejectSelectionAction extends AbstractAction {

        public PopupRejectSelectionAction() {
            super("Reject Selection");  
        }

        private void rejectIsAddressedBy(mxCell isAddressedBy) {
            isAddressedBy.setStyle(KuabaGraph.REJECTED_IS_ADDRESSED_BY_STYLE);
            isAddressedBy.setValue(KuabaGraph.REJECTED_IS_ADDRESSED_BY_LABEL);

            mxICell idea = isAddressedBy.getTarget();
            
            if (isAddressedBy.getSource().getId().equals(Question.ROOT_QUESTION_ID) || idea.getValue().equals("Association") || idea.getValue().equals("AssociationEnd") || idea.getValue().equals("Attribute")) {               
                for(int x = 0; x<idea.getEdgeCount(); x++) {
                    mxCell edge = (mxCell) idea.getEdgeAt(x);
                    if (edge.getId().contains(KuabaGraph.SUGGESTS_ID_COMPONENT)) 
                        rejectSuggests(edge);
                    else if (edge.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT) && (!edge.getTarget().equals(idea))) //in case of showing only ideas
                        rejectIsAddressedBy(edge);
                }
            }

        }

        private void rejectSuggests(mxCell suggests) {
            mxICell question = suggests.getTarget();

                for(int x = 0; x<question.getEdgeCount(); x++) {
                    mxCell edge = (mxCell) question.getEdgeAt(x);

                    if (edge.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) {
                        if(!question.getValue().equals("participant?") && !question.getValue().equals("owner?")) 
                            rejectIsAddressedBy(edge);
                        else {
                            edge.setStyle(KuabaGraph.REJECTED_IS_ADDRESSED_BY_STYLE); //avoid rejecting the participants and its relations when rejecting an association
                            edge.setValue(KuabaGraph.REJECTED_IS_ADDRESSED_BY_LABEL);
                        }
                        
                    }
                }
        }

        public void actionPerformed(ActionEvent e) {

            mxCell cell = (mxCell) graph.getSelectionCell();

            if(cell.isEdge() && cell.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) {
                graph.getModel().beginUpdate();
                rejectIsAddressedBy(cell);
                graph.getModel().endUpdate();
                graphComp.refresh();
            }

            graph.clearSelection();
        }
    }

    class PopupClearDecisionsAction extends AbstractAction {

        public PopupClearDecisionsAction() {
            super("Clear Decisions");
        }


        public void actionPerformed(ActionEvent e) {
            
            mxCell cell = (mxCell) graph.getSelectionCell();

            if(cell.isEdge() && cell.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) {
                graph.getModel().beginUpdate();
                cell.setStyle(KuabaGraph.NEUTRAL_IS_ADDRESSED_BY_STYLE);
                cell.setValue("");
                graph.getModel().endUpdate();
                graphComp.refresh();
            }

            graph.clearSelection();
        }

    }
}
