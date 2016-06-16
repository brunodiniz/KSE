/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.graph;

import br.ucam.kuabaSubsystem.graph.util.KuabaGraphUtil;
import br.ucam.kuabaSubsystem.kuabaModel.*;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.repositories.OwlApiFileGateway;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.TemplateGenerator;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author Bruno
 */
public class KuabaGraph {
    
    //labels
    protected static final String QUESTION_LABEL = "Question";
    protected static final String IDEA_LABEL = "Idea";
    protected static final String ARGUMENT_LABEL = "Argument";
    protected static final String IS_ADDRESSED_BY_LABEL = "Is Addressed By";
    protected static final String ACCEPTED_IS_ADDRESSED_BY_LABEL = "A";
    protected static final String REJECTED_IS_ADDRESSED_BY_LABEL = "R";
    protected static final String SUGGESTS_LABEL = "Suggests";
    protected static final String OBJECTS_TO_LABEL = "Against";
    protected static final String IN_FAVOR_OF_LABEL = "For";
    
    //ID components
    protected static final String IS_ADDRESSED_BY_ID_COMPONENT = ":is_addressed_by:";
    protected static final String SUGGESTS_ID_COMPONENT = ":suggests:";
    protected static final String ARGUMENT_ID_COMPONENT = ":has_argument:";
    
    //Styles
    protected static final String ACCEPTED_IS_ADDRESSED_BY_STYLE = "AcceptedIsAddressedBy";
    protected static final String REJECTED_IS_ADDRESSED_BY_STYLE = "RejectedIsAddressedBy";
    protected static final String NEUTRAL_IS_ADDRESSED_BY_STYLE = "NeutralIsAddressedBy";
    protected static final String CONDENSED_IS_ADDRESSED_BY_STYLE = "CondensedIsAddressedBy";
    protected static final String IDEA_STYLE = "Idea";
    protected static final String QUESTION_STYLE = "Question";
    protected static final String ARGUMENT_STYLE = "Argument";
    protected static final String SUGGESTS_STYLE = "Suggests";
    protected static final String OBJECTS_TO_STYLE = "ObjectsTo";
    protected static final String IN_FAVOR_OF_STYLE = "InFavorOf";
    protected static final String TREE_BOX_STYLE = "TreeBox";
    
    //GAP for tree mount
    private static final int Y_GAP = 180;
    private static final int X_START_GAP = 200;
    private static final int X_GAP = 100;
    private static final int CONTROL_POINTS_OFFSET = 20;
    
    //
    private KuabaRepository source;
    private Map<String, Boolean> decisions = new HashMap<String, Boolean>();
    private Map<ReasoningElement, Integer> stageMap;
    private Map<ReasoningElement, Idea> pendentLinks = new HashMap<ReasoningElement, Idea>();
    private Set<Idea> arguments = new HashSet<Idea>();
    
    private mxGraph graph = null;
    private mxGraphComponent graphComp = null;
    
    private Set<Double> collisionControlSet= new HashSet<Double>();
    private double longestTreeEndPos = 0; // used to ensure that the edge routing do not make edges crossing larger trees

    
    public KuabaGraph(KuabaRepository source) {
        this.source = source;
        stageMap = KuabaGraphUtil.getReasoningElementStageMap(source.getQuestion(Question.ROOT_QUESTION_ID));
    }
        
    public JScrollPane generateFullGraph(boolean decisionMaking, boolean showOnlyDecidedIdeas, boolean preserveDecisions, boolean showArguments, boolean isHorizontal) {   
        return generateGraph(decisionMaking, showOnlyDecidedIdeas, false, preserveDecisions, showArguments, isHorizontal);
    }
    
    public JScrollPane generateIdeaOnlyGraph(boolean decisionMaking, boolean preserveDecisions, boolean showArguments, boolean isHorizontal) {   
        return generateGraph(decisionMaking, false, true, preserveDecisions, showArguments, isHorizontal);
    }
    
    private JScrollPane generateGraph(boolean decisionMaking, boolean showOnlyDecidedIdeas, boolean showOnlyIdeas, boolean preserveDecisions, boolean showArguments, boolean isHorizontal) {       		
        
        if(preserveDecisions && graph != null)
            refreshDecisions(); 
        else
            decisions.clear();
        
        try{
            graphComp = mountFullGraph(showOnlyDecidedIdeas,showOnlyIdeas,showArguments, isHorizontal);

            if (decisionMaking) {
                DecisionMakerPopup popup = new DecisionMakerPopup(graph,graphComp);
                graphComp.getGraphControl().addMouseListener(popup); 
            }
            
            return graphComp;
        } catch (Exception ex) {
            Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public JScrollPane generateSubGraph(Idea idea, boolean showOnlyDecidedIdeas, boolean showArguments, boolean isHorizontal) {
        
        try{
            graphComp = mountSubGraph(idea, showOnlyDecidedIdeas, showArguments, isHorizontal);
            
            return graphComp;
        } catch (Exception ex) {
            Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static JScrollPane generateGraphLegend() {
        mxGraph glegend = new mxGraph();

        glegend.setCellsEditable(false);
        glegend.setCellsCloneable(false);
        glegend.setCellsDeletable(false);
        glegend.setCellsDisconnectable(false);
        glegend.setCellsBendable(false);
        glegend.setAutoSizeCells(true);
        glegend.setCellsMovable(false);
        glegend.setCellsSelectable(false);
        applyStylesheet(glegend);
        
        mxCell parent = (mxCell) glegend.getDefaultParent();

        glegend.getModel().beginUpdate();              

        try
        {
            Object a,b;
            
            //suggests
            a = glegend.insertVertex(parent, null, QUESTION_LABEL, 10, 5, 90, 40,QUESTION_STYLE);
            b = glegend.insertVertex(parent, null, IDEA_LABEL, 290, 5, 90, 40,IDEA_STYLE);
            glegend.insertEdge(parent, null, SUGGESTS_LABEL , a, b, SUGGESTS_STYLE);
            
            //is addressed by
            a = glegend.insertVertex(parent, null, IDEA_LABEL, 10, 55, 90, 40,IDEA_STYLE);
            b = glegend.insertVertex(parent, null, QUESTION_LABEL, 290, 55, 90, 40,QUESTION_STYLE);
            glegend.insertEdge(parent, null, IS_ADDRESSED_BY_LABEL , a, b, NEUTRAL_IS_ADDRESSED_BY_STYLE);
            
            //accepted is addressed by
            a = glegend.insertVertex(parent, null, IDEA_LABEL, 10, 105, 90, 40,IDEA_STYLE);
            b = glegend.insertVertex(parent, null, QUESTION_LABEL, 290, 105, 90, 40,QUESTION_STYLE);
            glegend.insertEdge(parent, null, IS_ADDRESSED_BY_LABEL+" (Accepted)" , a, b, ACCEPTED_IS_ADDRESSED_BY_STYLE);
            
            //rejected is addressed by
            a = glegend.insertVertex(parent, null, IDEA_LABEL, 10, 155, 90, 40,IDEA_STYLE);
            b = glegend.insertVertex(parent, null, QUESTION_LABEL, 290, 155, 90, 40,QUESTION_STYLE);
            glegend.insertEdge(parent, null, IS_ADDRESSED_BY_LABEL+" (Rejected)" , a, b, REJECTED_IS_ADDRESSED_BY_STYLE);
            
            //in favor of
            a = glegend.insertVertex(parent, null, ARGUMENT_LABEL, 10, 205, 110, 40,ARGUMENT_STYLE);
            b = glegend.insertVertex(parent, null, IDEA_LABEL, 290, 205, 90, 40,IDEA_STYLE);
            glegend.insertEdge(parent, null, IN_FAVOR_OF_LABEL , a, b, IN_FAVOR_OF_STYLE);
            
            //objects to
            a = glegend.insertVertex(parent, null, ARGUMENT_LABEL, 10, 255, 110, 40,ARGUMENT_STYLE);
            b = glegend.insertVertex(parent, null, IDEA_LABEL, 290, 255, 90, 40,IDEA_STYLE);
            glegend.insertEdge(parent, null, OBJECTS_TO_LABEL , a, b, OBJECTS_TO_STYLE);
            
        }
        finally
        {
            glegend.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(glegend);
        graphComponent.setConnectable(false);
        graphComponent.setDragEnabled(false);

        return graphComponent;
    }
    
    public JScrollPane getGraph() {
        return graphComp;
    }  
        
    public mxCell mountQuestionView(mxCell parent, Question question, int x, int y, boolean showOnlyDecidedIdeas, boolean showOnlyIdeas, boolean showArguments) {
        
        mxCell desc = null;
        
        int xGap;
        if(question.getId().equals(Question.ROOT_QUESTION_ID)) {
            xGap = X_START_GAP;
        } else { 
            xGap = X_GAP;
            desc = parent;
        }
        
        mxCell node = (mxCell) graph.insertVertex(parent, question.getId(), question.getHasText(), x, y, 90,
					40,QUESTION_STYLE);
        graph.updateCellSize(node);

        Collection<Idea> addressedBy = question.getIsAddressedBy();
        int ideasNum = addressedBy.size();
        int count = Math.round(x-((ideasNum-1)*xGap)/2);
        for (Idea idea : addressedBy){ 
                Decision d = KuabaHelper.getDecision(question, idea);
                String txt = question.getHasText(); 

                //termina a recursão caso seja pra mostrar apenas ideias com decisoes e não houver decisão
                    if(txt.contains(Question.DOMAIN_QUESTION_TEXT_PREFIX) && showOnlyDecidedIdeas && d==null)
                    return node;

                if (question.getId().equals(Question.ROOT_QUESTION_ID)) {
                    desc = (mxCell) graph.insertVertex(parent, null, idea.getHasText(), count, y+Y_GAP, 80, 30,TREE_BOX_STYLE+";noLabel=1");
                }

                mxCell ideaNS;

                if (showOnlyIdeas)
                    ideaNS = mountIdeaOnlyView(desc, idea, count, y+Y_GAP, showArguments);
                else {
                    if (stageMap.get(question) >= stageMap.get(idea)) {
                        pendentLinks.put(question, idea);
                        return node;
                    } else
                        ideaNS = mountIdeaView(desc, idea, count, y+Y_GAP, showOnlyDecidedIdeas, showArguments);
                }

                count += xGap;

                String style = NEUTRAL_IS_ADDRESSED_BY_STYLE, label = "";

                if(d != null)
                    if (d.getIsAccepted()) {
                        style = ACCEPTED_IS_ADDRESSED_BY_STYLE;
                        label = ACCEPTED_IS_ADDRESSED_BY_LABEL;
                    } else {
                        style = REJECTED_IS_ADDRESSED_BY_STYLE;
                        label = REJECTED_IS_ADDRESSED_BY_LABEL;
                    }

                mxCell l = (mxCell) graph.insertEdge(parent, question.getId() + IS_ADDRESSED_BY_ID_COMPONENT +idea.getId(), label , node, ideaNS, style);
//                    mxCell l = (mxCell) graph.insertEdge(parent, question.getId() + IS_ADDRESSED_BY_ID_COMPONENT +idea.getId(), IS_ADDRESSED_BY_LABEL , node, ideaNS, style);

                if(decisions.containsKey(l.getId())) {
                    boolean dec = decisions.get(l.getId());

                    if(dec) {
                        l.setStyle(ACCEPTED_IS_ADDRESSED_BY_STYLE);
                        l.setValue(ACCEPTED_IS_ADDRESSED_BY_LABEL);
                    } else {
                        l.setStyle(REJECTED_IS_ADDRESSED_BY_STYLE);
                        l.setValue(REJECTED_IS_ADDRESSED_BY_LABEL);
                    }
                }

        }		

        return node;
	
    }
    
    private mxCell mountIdeaView(mxCell parent, Idea idea, int x, int y, boolean showOnlyDecidedIdeas, boolean showArguments) {
        mxCell node = (mxCell) ((mxGraphModel)graph.getModel()).getCell(idea.getId());
        if(node == null) {
            node = (mxCell) graph.insertVertex(parent, idea.getId(), idea.getHasText(), x, y, 80,
					30,IDEA_STYLE);
            graph.updateCellSize(node);
        }
//        else return node;
        else {
            node.setParent(parent);
            node.setGeometry(new mxGeometry(x, y, 80, 30));
        }

        if(idea.hasHasArgument() && showArguments){
            arguments.add(idea);
        }

        Collection<Question> suggests = idea.getSuggests();
        int questionsNum = suggests.size();
        int count = Math.round(x-((questionsNum-1) * X_GAP)/2);
        for (Question question : suggests) {
            
            mxCell l = (mxCell) graph.insertEdge(parent, idea.getId() + SUGGESTS_ID_COMPONENT + question.getId(), "" , node, mountQuestionView(parent,question, count, y+ Y_GAP, showOnlyDecidedIdeas,false, showArguments), SUGGESTS_STYLE);
//            mxCell l = (mxCell) graph.insertEdge(parent, idea.getId() + SUGGESTS_ID_COMPONENT + question.getId(), SUGGESTS_LABEL , node, mountQuestionView(parent,question, count, y+ Y_GAP, showOnlyDecidedIdeas,false, showArguments), SUGGESTS_STYLE);
            count +=X_GAP;
        }

        return node;
    }
    
    private mxCell mountIdeaOnlyView(mxCell parent, Idea idea, int x, int y, boolean showArguments) {
        
        mxCell node = (mxCell) ((mxGraphModel)graph.getModel()).getCell(idea.getId());
        if(node == null) {
            node = (mxCell) graph.insertVertex(parent, idea.getId(), idea.getHasText(), x, y, 80,
					30,IDEA_STYLE);
            graph.updateCellSize(node);
        }
        else return node;

        if(idea.hasHasArgument() && showArguments){
            arguments.add(idea);       
        }

        Collection<Question> suggests = idea.getSuggests();
        int count = -(suggests.size() * X_GAP);
        for (Question question : suggests) {
                count +=X_GAP;
                Collection<Idea> isAddressedBy = question.getIsAddressedBy();
                
                for (Idea addressedIdea : isAddressedBy) {
                    Decision d = KuabaHelper.getDecision(question, addressedIdea);
                    
                    String style = CONDENSED_IS_ADDRESSED_BY_STYLE, label = "";

                    if(d != null)
                        if (d.getIsAccepted()) {
                            style = ACCEPTED_IS_ADDRESSED_BY_STYLE;
                            label = ACCEPTED_IS_ADDRESSED_BY_LABEL;
                        } else {
                            style = REJECTED_IS_ADDRESSED_BY_STYLE;
                            label = REJECTED_IS_ADDRESSED_BY_LABEL;
                        }
                    
                    mxCell l;
                    if (stageMap.get(question) >= stageMap.get(addressedIdea)) {
                            mxCell ideaCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(addressedIdea.getId());
                            if(ideaCell == null) {
                                pendentLinks.put(idea, addressedIdea);
                                return node;
                            } else
                                l = (mxCell) graph.insertEdge(parent, question.getId() + IS_ADDRESSED_BY_ID_COMPONENT +addressedIdea.getId(), label , node, ideaCell, style);
                    } else
                        l = (mxCell) graph.insertEdge(parent, question.getId() + IS_ADDRESSED_BY_ID_COMPONENT +addressedIdea.getId(), label , node, mountIdeaOnlyView(parent, addressedIdea, x+count, y+ Y_GAP, showArguments), style);
                    
                    //preserva decisoes
                    if(decisions.containsKey(l.getId())) {
                        boolean dec = decisions.get(l.getId());

                        if(dec) {
                            l.setStyle(ACCEPTED_IS_ADDRESSED_BY_STYLE);
                            l.setValue(ACCEPTED_IS_ADDRESSED_BY_LABEL);
                        } else {
                            l.setStyle(REJECTED_IS_ADDRESSED_BY_STYLE);
                            l.setValue(REJECTED_IS_ADDRESSED_BY_LABEL);
                        }
                    }
                    
                    count+=X_GAP;
                }
        }

        return node;
    }
    
    private void mountPendentLinks(boolean showOnlyIdeas, boolean isHorizontal) {
        
        Set<ReasoningElement> reSet = pendentLinks.keySet();
        
        for (ReasoningElement re : reSet) {
            Idea idea=null,addressedIdea;
            Question question;
            if (showOnlyIdeas) {
                idea = (Idea) re;
                addressedIdea = pendentLinks.get(idea);
                question = idea.listSuggests().next();
            } else {
                question = (Question) re;
                addressedIdea = pendentLinks.get(question);
            }
            Decision d = KuabaHelper.getDecision(question, addressedIdea);
            
            mxCell addressedIdeaCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(addressedIdea.getId());
            mxCell sourceCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(question.getId());
            
            if(showOnlyIdeas) 
                sourceCell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(idea.getId());            
            
            String style,label = "";
            
            if(showOnlyIdeas) {
                style = CONDENSED_IS_ADDRESSED_BY_STYLE;
//                label = "";
            } else {
                style = NEUTRAL_IS_ADDRESSED_BY_STYLE;
//                label = IS_ADDRESSED_BY_LABEL;
            }
            
            if(d != null)
                if (d.getIsAccepted()) {
                    style = ACCEPTED_IS_ADDRESSED_BY_STYLE;
                    label = ACCEPTED_IS_ADDRESSED_BY_LABEL;
                } else {
                    style = REJECTED_IS_ADDRESSED_BY_STYLE;
                    label = REJECTED_IS_ADDRESSED_BY_LABEL;
                }
     
            mxCell l = (mxCell) graph.insertEdge(sourceCell.getParent(), question.getId() + IS_ADDRESSED_BY_ID_COMPONENT +addressedIdea.getId(), label, sourceCell, addressedIdeaCell, style);
            
            setPendentLinkControlPoints(l, isHorizontal);
            
            if(decisions.containsKey(l.getId())) {
                boolean dec = decisions.get(l.getId());

                if(dec) {
                    l.setStyle(ACCEPTED_IS_ADDRESSED_BY_STYLE);
                    l.setValue(ACCEPTED_IS_ADDRESSED_BY_LABEL);
                } else {
                    l.setStyle(REJECTED_IS_ADDRESSED_BY_STYLE);
                    l.setValue(REJECTED_IS_ADDRESSED_BY_LABEL);
                }
            }
        }
    }
    
    //identify the pendent links control points, and do the edge routing
    protected void setPendentLinkControlPoints(mxCell edge, boolean isHorizontal) {
        if (!edge.isEdge()) {
            return;
        }
        
        mxICell src = edge.getSource();
        mxICell tgt = edge.getTarget();
        
        double ctrlX, ctrlY, ctrlX2, ctrlY2;
        List<mxPoint> pl = new LinkedList<mxPoint>();
        
        if (isHorizontal) {

            ctrlY = graph.getView().getState(src).getCenterY();
            ctrlY2 = graph.getView().getState(tgt).getCenterY();

            ctrlX = CONTROL_POINTS_OFFSET + longestTreeEndPos; 
            
            while(collisionControlSet.contains(ctrlX)) {
                ctrlX+=CONTROL_POINTS_OFFSET/2;
            }
            
            ctrlX2 = ctrlX;
            collisionControlSet.add(ctrlX);
            
            //avoiding edge overlap on the connection ports
            if (ctrlY > ctrlY2) {
                ctrlY-=CONTROL_POINTS_OFFSET*2;
                ctrlY2+=CONTROL_POINTS_OFFSET*2;
            } else {
                ctrlY+=CONTROL_POINTS_OFFSET*2;
                ctrlY2-=CONTROL_POINTS_OFFSET*2;
            }
        } 
        else {
            
            ctrlX = graph.getView().getState(src).getCenterX();
            ctrlX2 = graph.getView().getState(tgt).getCenterX();
            
            ctrlY = CONTROL_POINTS_OFFSET + longestTreeEndPos;
            
            while(collisionControlSet.contains(ctrlY)) {
                ctrlY+=CONTROL_POINTS_OFFSET/2;
            }
            
            ctrlY2 = ctrlY;
            collisionControlSet.add(ctrlY);

            //avoiding edge overlap on the connection ports
            if (ctrlX > ctrlX2) {
                ctrlX-=CONTROL_POINTS_OFFSET*2;
                ctrlX2+=CONTROL_POINTS_OFFSET*2;
            } else {
                ctrlX+=CONTROL_POINTS_OFFSET*2;
                ctrlX2-=CONTROL_POINTS_OFFSET*2;
            }
        }
        
        pl.add(new mxPoint(ctrlX, ctrlY));
        pl.add(new mxPoint(ctrlX2, ctrlY2));
        edge.getGeometry().setPoints(pl);
    }
    
    private void mountArguments() {
        for (Idea idea : arguments) {
            mxCell node = (mxCell) ((mxGraphModel)graph.getModel()).getCell(idea.getId());
            

            double x = graph.getView().getState(node).getX();
            double y = graph.getView().getState(node).getY();
            
            if (!idea.getHasText().equals("Class"))
                x+=90;
            
            Collection<Argument> argumentCollection = idea.getHasArgument();
            for (Argument argument : argumentCollection) {
                
                    mxCell argumentCell = (mxCell) graph.insertVertex(graph.getDefaultParent(), argument.getId(), argument.getHasText(), x, y+60, 150,
                                    150,ARGUMENT_STYLE);

                    graph.updateCellSize(argumentCell);

                    double width = argumentCell.getGeometry().getWidth();
                    double height = argumentCell.getGeometry().getHeight();
                    double sqr = Math.sqrt(width*height);

                    argumentCell.getGeometry().setWidth(sqr);
                    argumentCell.getGeometry().setHeight(sqr);
                        
                    mxCell l;

                    if(argument.getInFavorOf().contains(idea)){ 
//                        l = (mxCell) graph.insertEdge(graph.getDefaultParent(), idea.getId()+ARGUMENT_ID_COMPONENT+argument.getId(), "" , argumentCell, node, IN_FAVOR_OF_STYLE);
                        l = (mxCell) graph.insertEdge(graph.getDefaultParent(), idea.getId()+ARGUMENT_ID_COMPONENT+argument.getId(), IN_FAVOR_OF_LABEL , argumentCell, node, IN_FAVOR_OF_STYLE);
                    }
                    else{
//                        l = (mxCell) graph.insertEdge(graph.getDefaultParent(), idea.getId()+ARGUMENT_ID_COMPONENT+argument.getId(), "" , argumentCell, node, OBJECTS_TO_STYLE);
                        l = (mxCell) graph.insertEdge(graph.getDefaultParent(), idea.getId()+ARGUMENT_ID_COMPONENT+argument.getId(), OBJECTS_TO_LABEL , argumentCell, node, OBJECTS_TO_STYLE);
                    }
                    y+=height+15;
            }
        }
    }
    
    private void refreshDecisions() {
        Collection cellColl = ((mxGraphModel)graph.getModel()).getCells().values();
        decisions.clear();
        
        for (Object edg : cellColl) {
            mxCell edge = (mxCell) edg;
            if (!edge.isEdge()) continue;
            
            if (!edge.getId().contains(IS_ADDRESSED_BY_ID_COMPONENT)) continue;
            boolean isAccpeted;
            
            if(edge.getStyle().contains(ACCEPTED_IS_ADDRESSED_BY_STYLE))
                isAccpeted = true;
            else if(edge.getStyle().contains(REJECTED_IS_ADDRESSED_BY_STYLE))
                isAccpeted = false;
            else continue;
            
            decisions.put(edge.getId(), isAccpeted);
        }
        
    }
    
    public void applyDecisions() {
        Collection cellColl = ((mxGraphModel)graph.getModel()).getCells().values();
        
        for (Object edg : cellColl) {
            mxCell edge = (mxCell) edg;
            if (!edge.isEdge()) continue;

            if (!edge.getId().contains(IS_ADDRESSED_BY_ID_COMPONENT)) continue;
            boolean isAccpeted;
            
            if(edge.getStyle().contains(ACCEPTED_IS_ADDRESSED_BY_STYLE))
                isAccpeted = true;
            else if(edge.getStyle().contains(REJECTED_IS_ADDRESSED_BY_STYLE))
                isAccpeted = false;
            else continue;
                
            String[] ids = edge.getId().split(IS_ADDRESSED_BY_ID_COMPONENT);
            
            String question = ids[0];
            String idea = ids[1];
            
            Decision d = source.getModelFactory().createDecision(UUID.randomUUID().toString());
            
            source.getQuestion(question).addHasDecision(d);
            d.setIsAccepted(isAccpeted);
            d.setConcludes(source.getIdea(idea));
            source.getIdea(idea).addIsConcludedBy(d);
//            d.setIsMadeBy(author);
        }
    }
    
    public static void main(String[] args) {
        
        boolean isHorizontal = false;
        
        KuabaRepository repo = TemplateGenerator.generate(OwlApiFileGateway.getInstance(), null);
        KuabaGraph kg = new KuabaGraph(repo);
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.add(kg.generateGraphLegend());
        f.add(kg.generateFullGraph(true, false, true, true, isHorizontal));
//        f.add(kg.generateIdeaOnlyGraph(true, true, true));
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
    
    private static void applyStylesheet(mxGraph graph) {
        mxStylesheet stylesheet = graph.getStylesheet();
        HashMap<String, Object> style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_FILLCOLOR, "#A4E4EB");
//        style.put(mxConstants.STYLE_WHITE_SPACE, "wrap");
//        style.put(mxConstants.STYLE_OVERFLOW, "fill");
        style.put(mxConstants.STYLE_AUTOSIZE, 1);
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        style.put(mxConstants.STYLE_SPACING, "6");
//        style.put(mxConstants.STYLE_PORT_CONSTRAINT,mxConstants.DIRECTION_EAST);
        stylesheet.putCellStyle(QUESTION_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_ROUNDED, true);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
        style.put(mxConstants.STYLE_WHITE_SPACE, "wrap");
//        style.put(mxConstants.STYLE_OVERFLOW, "fill");
//        style.put(mxConstants.STYLE_AUTOSIZE, 1);
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        style.put(mxConstants.STYLE_SPACING, "12"); 
        style.put(mxConstants.STYLE_DASHED, true); 
        style.put(mxConstants.STYLE_STROKEWIDTH, 2);
        stylesheet.putCellStyle(ARGUMENT_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_FILLCOLOR, "#EAF084");
//        style.put(mxConstants.STYLE_WHITE_SPACE, "wrap");
//        style.put(mxConstants.STYLE_OVERFLOW, "fill");
        style.put(mxConstants.STYLE_AUTOSIZE, 1);
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        style.put(mxConstants.STYLE_SPACING, "10"); 
        stylesheet.putCellStyle(IDEA_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        style.put(mxConstants.STYLE_STROKEWIDTH, 3);
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        style.put(mxConstants.STYLE_DASHED, true); 
        style.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_BOTTOM);
        stylesheet.putCellStyle(SUGGESTS_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_STROKECOLOR, "green");
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_BOTTOM);
//        style.put(mxConstants.STYLE_EDGE,mxConstants.EDGESTYLE_TOPTOBOTTOM);
        stylesheet.putCellStyle(ACCEPTED_IS_ADDRESSED_BY_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_STROKECOLOR, "red");
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_BOTTOM);
        stylesheet.putCellStyle(REJECTED_IS_ADDRESSED_BY_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_STROKECOLOR, "#DBD800");
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_BOTTOM);
//        style.put(mxConstants.STYLE_EDGE,mxConstants.EDGESTYLE_ORTHOGONAL);
        stylesheet.putCellStyle(NEUTRAL_IS_ADDRESSED_BY_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_STROKECOLOR, "#00EEFF");
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        stylesheet.putCellStyle(CONDENSED_IS_ADDRESSED_BY_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_STROKECOLOR, "#FF8CB6");
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        style.put(mxConstants.STYLE_DASHED, true); 
        style.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_BOTTOM);
        stylesheet.putCellStyle(OBJECTS_TO_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_STROKECOLOR, "blue");
        style.put(mxConstants.STYLE_FONTSIZE, 13);
        style.put(mxConstants.STYLE_DASHED, true); 
        style.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_BOTTOM); 
        stylesheet.putCellStyle(IN_FAVOR_OF_STYLE, style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_SPACING, "12"); 
        style.put(mxConstants.STYLE_FILLCOLOR, "#EEEEEE");
        style.put(mxConstants.STYLE_FONTSIZE, 13);
//        style.put(mxConstants.STYLE_, 13);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_TOP); 
        stylesheet.putCellStyle(TREE_BOX_STYLE, style);
    }
    
    private double getLongestTreeEndPos(mxCell parent, boolean isHorizontal) {
        double resp = 0;
        
        for (int x = 0; x<parent.getChildCount();x++) {
            mxICell child = parent.getChildAt(x);
            if (child.getStyle().contains(TREE_BOX_STYLE)) {
                double v;
                if (isHorizontal) {
                    v = graph.getView().getState(child).getX() + child.getGeometry().getWidth();
                }
                else {
                    v = graph.getView().getState(child).getY() + child.getGeometry().getHeight();
                }
                if (v > resp) resp = v;
            }
        }
        
        return resp;
    }
    
    private mxGraphComponent mountFullGraph(boolean showOnlyAcceptedIdeas, boolean showOnlyIdeas, boolean showArguments, boolean isHorizontal) {
        return mount(null, showOnlyAcceptedIdeas, showOnlyIdeas, showArguments, isHorizontal);
    }
    
    private mxGraphComponent mountSubGraph(Idea subGraphParent, boolean showOnlyAcceptedIdeas, boolean showArguments, boolean isHorizontal) {
        return mount(subGraphParent, showOnlyAcceptedIdeas, false, showArguments, isHorizontal);
    }
    
    private mxGraphComponent mount(Idea subGraphParent ,boolean showOnlyAcceptedIdeas, boolean showOnlyIdeas, boolean showArguments, boolean isHorizontal) {
        
        graph = new mxGraph();
        graph.setCellsEditable(false);
        graph.setCellsCloneable(false);
        graph.setCellsDeletable(false);
        graph.setCellsDisconnectable(false);
        graph.setCellsBendable(false);
        graph.setAutoSizeCells(true);
//        graph.setResetEdgesOnMove(true);
        graph.addListener(null, new KuabaGraphController(this, isHorizontal));
        applyStylesheet(graph);
        
        mxCell parent = (mxCell) graph.getDefaultParent();

        mxCompactTreeLayout layout = new mxCompactTreeLayout(graph, isHorizontal);
//        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
//        mxParallelEdgeLayout layout = new mxParallelEdgeLayout(graph);
        layout.setNodeDistance(80);
        layout.setLevelDistance(25);
        layout.setResizeParent(true);
        layout.setEdgeRouting(false);
        
        pendentLinks.clear();
        arguments.clear();
        collisionControlSet.clear();
        graph.getModel().beginUpdate();

        Question root = source.getQuestion(Question.ROOT_QUESTION_ID);
        int domainIdeasNum = root.getIsAddressedBy().size();
                

        try
        {
            if (subGraphParent == null)
                mountQuestionView(parent, root, domainIdeasNum*110, 0, showOnlyAcceptedIdeas, showOnlyIdeas, showArguments);
            else 
                mountIdeaView(parent, subGraphParent, 300, 0, showOnlyAcceptedIdeas, showArguments);
            
            layout.execute(parent);
            
//            mountPendentLinks(showOnlyIdeas);
//            mountArguments();
            
//            mxParallelEdgeLayout layout2 = new mxParallelEdgeLayout(graph);
//            layout2.execute(graph.getDefaultParent());

        }
        finally
        {
                graph.getModel().endUpdate();
        }
        
        //another cycle of update because it's impossible to find cells' absolute positions during their insertion cycle
        graph.getModel().beginUpdate();
        try
        {
            longestTreeEndPos = getLongestTreeEndPos(parent, isHorizontal); //used when routing the pendent links
            mountPendentLinks(showOnlyIdeas, isHorizontal);
            mountArguments();
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);
        graphComponent.setDragEnabled(false);

        return graphComponent;
    }
   
}