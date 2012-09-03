/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.graph;

import br.ucam.kuabaSubsystem.graph.util.KuabaGraphUtil;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaElement;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import com.compendium.core.ICoreConstants;
import com.compendium.core.datamodel.Link;
import com.compendium.core.datamodel.Model;
import com.compendium.core.datamodel.ModelSessionException;
import com.compendium.core.datamodel.NodeSummary;
import com.compendium.core.datamodel.PCSession;
import com.compendium.core.datamodel.View;
import com.compendium.core.datamodel.services.LinkService;
import com.compendium.core.datamodel.services.ServiceManager;
import com.compendium.ui.UILink;
import com.compendium.ui.UIMapViewFrame;
import com.compendium.ui.UINode;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Bruno
 */
public class KuabaGraph {
    
    //labels
    protected static final String IS_ADDRESSED_BY_LABEL = "Is Addressed By";
    protected static final String SUGGESTS_LABEL = "Suggests";
    protected static final String OBJECTS_TO_LABEL = "Against";
    protected static final String IN_FAVOR_OF_LABEL = "For";
    
    //ID components
    protected static final String IS_ADDRESSED_BY_ID_COMPONENT = ":is_addressed_by:";
    protected static final String SUGGESTS_ID_COMPONENT = ":suggests:";
    protected static final String ARGUMENT_ID_COMPONENT = ":has_argument:";
    
    //GAP for tree mount
    private static final int Y_GAP = 180;
    private static final int X_START_GAP = 200;
    private static final int X_GAP = 100;
    
    //
    private KuabaRepository source;
    private Model model;
    private PCSession session = null;
    private View view;
    private String author = "";     //not used yet
    private Map<String, Boolean> decisions = new HashMap<String, Boolean>();
    private boolean showOnlyIdeas;
    
    private UIMapViewFrame graph = null;

//    public KuabaGraph(ReasoningElement sourceRE) {
//        this(sourceRE.getRepository());
//    }
    
    public KuabaGraph(KuabaRepository source) {
        this.source = source;
    }
    
    private void initializeGraph() {
        
        model = new Model(source.getUrl());
        
        try {
            model.initialize();
        } catch (UnknownHostException ex) {
            Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        session = new PCSession(UUID.randomUUID().toString(), source.getUrl(), "USER ID");
        model.setSession(session);
        LinkService ls = new LinkService();
        model.setLinkService(ls);
        
        view = new View("primaryView");        
        view.initialize(session, model);
        
        try {
            view.setType(ICoreConstants.MAPVIEW, author);
        } catch (Exception ex) {
            Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JInternalFrame generateFullGraph(boolean decisionMaking, boolean showOnlyAcceptedIdeas, boolean preserveDecisions, boolean showArguments) {   
        return generateGraph(decisionMaking, showOnlyAcceptedIdeas, false, preserveDecisions, showArguments);
    }
    
    public JInternalFrame generateIdeaOnlyGraph(boolean decisionMaking, boolean preserveDecisions, boolean showArguments) {   
        return generateGraph(decisionMaking, false, true, preserveDecisions, showArguments);
    }
    
    private JInternalFrame generateGraph(boolean decisionMaking, boolean showOnlyAcceptedIdeas, boolean showOnlyIdeas, boolean preserveDecisions, boolean showArguments) {       		
        this.showOnlyIdeas = showOnlyIdeas;
        
        if(preserveDecisions && graph != null)
            refreshDecisions(); 
        else
            decisions.clear();
        
        initializeGraph();
        
        try{
            Question root = source.getQuestion(Question.ROOT_QUESTION_ID);
            int domainIdeasNum = root.getIsAddressedBy().size();
            mountQuestionView(root, domainIdeasNum*110, 0, showOnlyAcceptedIdeas, showOnlyIdeas, showArguments);
            
            graph = new UIMapViewFrame(view);
            graph.setTitle(source.getUrl());

//            if (decisionMaking) graph.getViewPane().addMouseListener(new DecisionMakerPopup(graph));
            if (decisionMaking) {
                DecisionMakerPopup popup = new DecisionMakerPopup(graph);
                graph.getViewPane().addMouseListener(popup);
                
                Vector<NodeSummary> vect = view.getMemberNodes();
                
                for (int x = 0; x<vect.size(); x++) {
                    UINode uinode = graph.getViewPane().getViewPaneUI().getUINode(vect.get(x).getId()); 
                    if(uinode.getType() == ICoreConstants.POSITION)
                        uinode.addMouseListener(popup);
                } 
                
            }
            
            graph.setClosable(false);
            graph.setResizable(false);
            graph.setVisible(true);
            
            return graph;
        } catch (Exception ex) {
            Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public JInternalFrame generateSubGraph(Idea idea, boolean showOnlyAcceptedIdeas, boolean showArguments) {
        initializeGraph();
        
        try{
            mountIdeaView(idea, 300, 0, showOnlyAcceptedIdeas, showArguments);
            
            graph = new UIMapViewFrame(view);
            graph.setTitle(idea.getHasText());

//            if (decisionMaking) graph.getViewPane().addMouseListener(new DecisionMakerPopup());
            
            graph.setClosable(false);
            graph.setResizable(false);
            graph.setVisible(true);
            
            return graph;
        } catch (Exception ex) {
            Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public JInternalFrame getGraph() {
        return graph;
    }  
    
    private NodeSummary mountQuestionView(Question question, int x, int y, boolean showOnlyAcceptedIdeas, boolean showOnlyIdeas, boolean showArguments) {
        NodeSummary node = new NodeSummary();

        try {
                node.setId(question.getId());
                node.setCreationDate(new Date(), author);
                node.setType(ICoreConstants.ISSUE, author);
                node.setLabel(question.getHasText(), author);
                node.initialize(session, model);
                view.addNodeToView(node, x, y);

        } catch (Exception ex) {
                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int xGap;
        if(question.getId().equals(Question.ROOT_QUESTION_ID))
            xGap = X_START_GAP;
        else
            xGap = X_GAP;

        Collection<Idea> addressedBy = question.getIsAddressedBy();
        int ideasNum = addressedBy.size();
        int count = Math.round(x-((ideasNum-1)*xGap)/2);
        for (Idea idea : addressedBy){ 
                Decision d = KuabaHelper.getDecision(question, idea);
                String txt = question.getHasText(); 
                if(txt.contains(Question.DOMAIN_QUESTION_TEXT_PREFIX) && showOnlyAcceptedIdeas){
                        if(d != null){
                                
                                NodeSummary ideaNS = new NodeSummary();
                                
                                ideaNS = mountIdeaView(idea, count, y+Y_GAP, showOnlyAcceptedIdeas, showArguments);
                                count += xGap;
                                
                                if(d.getIsAccepted())
                                        try {
                                                ideaNS.setType(ICoreConstants.DECISION, author);
                                        } catch (Exception ex) {
                                                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                
                                Link l = new Link(ICoreConstants.EXPANDS_ON_LINK,
                                                node, ideaNS, IS_ADDRESSED_BY_LABEL, 
                                                ICoreConstants.ARROW_TO);
                                l.setId(question.getId() + IS_ADDRESSED_BY_ID_COMPONENT +idea.getId());
                                l.initialize(session, model);
                                
                                //preserva decisoes
                                if(decisions.containsKey(l.getId())) {
                                    try {
                                        boolean dec = decisions.get(l.getId());

                                        if(dec)
                                            l.setType(ICoreConstants.SUPPORTS_LINK);
                                        else
                                            l.setType(ICoreConstants.OBJECTS_TO_LINK);
                                    } catch (Exception ex) {
                                                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                
                                try {
                                        view.addLinkToView(l);				
                                        view.addNodeToView(node, x, y);
                                } catch (Exception ex) {
                                        Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }
                }			
                else{
                        
                        NodeSummary ideaNS = new NodeSummary();
                        if (showOnlyIdeas)
                            ideaNS = mountIdeaOnlyView(idea, count, y+Y_GAP, showArguments);
                        else
                            ideaNS = mountIdeaView(idea, count, y+Y_GAP, showOnlyAcceptedIdeas, showArguments);
                        count += xGap;
                        if(d != null){					
                                if(d.getIsAccepted())
                                        try {
                                                ideaNS.setType(ICoreConstants.DECISION, author);
                                        } catch (Exception ex) {
                                                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                                        }					
                        }
                        Link l = new Link(ICoreConstants.EXPANDS_ON_LINK,
                                        node, ideaNS, IS_ADDRESSED_BY_LABEL, 
                                        ICoreConstants.ARROW_TO);

                        l.setId(question.getId() + IS_ADDRESSED_BY_ID_COMPONENT +idea.getId());
                        l.initialize(session, model);
                        
                        //preserva decisoes
                        if(decisions.containsKey(l.getId())) {
                            try {
                                boolean dec = decisions.get(l.getId());

                                if(dec)
                                    l.setType(ICoreConstants.SUPPORTS_LINK);
                                else
                                    l.setType(ICoreConstants.OBJECTS_TO_LINK);
                            } catch (Exception ex) {
                                        Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                        try {
                                view.addLinkToView(l);				
                                view.addNodeToView(node, x, y);
                        } catch (Exception ex) {
                                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }

        }		

        return node;
	
    }
    
    private NodeSummary mountIdeaView(Idea idea, int x, int y, boolean showOnlyAcceptedIdeas, boolean showArguments) {
        NodeSummary node = new NodeSummary();

        try {
                node.setId(idea.getId());
                node.setCreationDate(new Date(), author);			
                node.setType(ICoreConstants.POSITION, author);
                node.setLabel(idea.getHasText(), author);
                node.initialize(session, model);
                view.addNodeToView(node, x, y);
        } catch (Exception ex) {
                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(idea.hasHasArgument() && showArguments){
                Collection<Argument> argumentCollection = idea.getHasArgument();
                for (Argument argument : argumentCollection) {
                        NodeSummary argumentNS = new NodeSummary();
                        try {
                                argumentNS.setId(argument.getId());
                                argumentNS.setLabel(argument.getHasText(), author);
                                argumentNS.setCreationDate(new Date(), author);

                                view.addNodeToView(argumentNS, x + 50, y + 50);
                                String typeLink = "";
                                Link l;
                                if(argument.getInFavorOf().contains(idea)){
                                        typeLink = ICoreConstants.SPECIALIZES_LINK;
                                        argumentNS.setType(ICoreConstants.PRO, author);
                                        l = new Link(typeLink,
                                                argumentNS, node, IN_FAVOR_OF_LABEL, 
                                                ICoreConstants.ARROW_TO);
                                }
                                else{
                                        typeLink = ICoreConstants.CHALLENGES_LINK;
                                        argumentNS.setType(ICoreConstants.CON, author);
                                        l = new Link(typeLink,
                                                argumentNS, node, OBJECTS_TO_LABEL, 
                                                ICoreConstants.ARROW_TO);
                                }
                                
                                l.setId(idea.getId()+ARGUMENT_ID_COMPONENT+argument.getId());
                                l.initialize(session, model);
                                view.addLinkToView(l);
                        } catch (Exception ex) {
                                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
        }

        Collection<Question> suggests = idea.getSuggests();
        int questionsNum = suggests.size();
        int count = Math.round(x-((questionsNum-1) * X_GAP)/2);
        for (Question question : suggests) {
                Link l = new Link(ICoreConstants.RELATED_TO_LINK,
                                node, mountQuestionView(question, count, y+ Y_GAP, showOnlyAcceptedIdeas,false, showArguments), SUGGESTS_LABEL, 
                                ICoreConstants.ARROW_TO);
                l.setId(idea.getId() + SUGGESTS_ID_COMPONENT + question.getId());
                l.initialize(session, model);
                try {
                        view.addLinkToView(l);

                } catch (Exception ex) {
                        Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                }
                count +=X_GAP;
        }

        return node;
    }
    
    private NodeSummary mountIdeaOnlyView(Idea idea, int x, int y, boolean showArguments) {
        
        NodeSummary node = new NodeSummary();

        try {
                node.setId(idea.getId());
                node.setCreationDate(new Date(), author);			
                node.setType(ICoreConstants.POSITION, author);
                node.setLabel(idea.getHasText(), author);
                node.initialize(session, model);
                view.addNodeToView(node, x, y);
        } catch (Exception ex) {
                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(idea.hasHasArgument() && showArguments){
                Collection<Argument> argumentCollection = idea.getHasArgument();
                for (Argument argument : argumentCollection) {
                        NodeSummary argumentNS = new NodeSummary();
                        try {
                                argumentNS.setId(argument.getId());
                                argumentNS.setLabel(argument.getHasText(), author);
                                argumentNS.setCreationDate(new Date(), author);

                                view.addNodeToView(argumentNS, x + 50, y + 50);
                                String typeLink = "";
                                Link l;
                                if(argument.getInFavorOf().contains(idea)){
                                        typeLink = ICoreConstants.SPECIALIZES_LINK;
                                        argumentNS.setType(ICoreConstants.PRO, author);
                                        l = new Link(typeLink,
                                                argumentNS, node, IN_FAVOR_OF_LABEL, 
                                                ICoreConstants.ARROW_TO);
                                }
                                else{
                                        typeLink = ICoreConstants.CHALLENGES_LINK;
                                        argumentNS.setType(ICoreConstants.CON, author);
                                        l = new Link(typeLink,
                                                argumentNS, node, OBJECTS_TO_LABEL, 
                                                ICoreConstants.ARROW_TO);
                                }
                                
                                l.setId(idea.getId()+ARGUMENT_ID_COMPONENT+argument.getId());
                                l.initialize(session, model);
                                view.addLinkToView(l);
                        } catch (Exception ex) {
                                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
        }

        Collection<Question> suggests = idea.getSuggests();
        int count = -(suggests.size() * X_GAP);
        for (Question question : suggests) {
                count +=X_GAP;
                Collection<Idea> isAddressedBy = question.getIsAddressedBy();
                
                for (Idea addressedIdea : isAddressedBy) {
                    Link l = new Link(ICoreConstants.ABOUT_LINK,
                                    node, mountIdeaOnlyView(addressedIdea, x+count, y+ Y_GAP, showArguments), "", 
                                    ICoreConstants.ARROW_TO);
                    l.setId(question.getId() + IS_ADDRESSED_BY_ID_COMPONENT +addressedIdea.getId());
                    l.initialize(session, model);
                    
                    //preserva decisoes
                    if(decisions.containsKey(l.getId())) {
                        try {
                            boolean dec = decisions.get(l.getId());

                            if(dec)
                                l.setType(ICoreConstants.SUPPORTS_LINK);
                            else
                                l.setType(ICoreConstants.OBJECTS_TO_LINK);
                        } catch (Exception ex) {
                                    Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    try {
                            view.addLinkToView(l);

                    } catch (Exception ex) {
                            Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    count+=X_GAP;
                }
        }

        return node;
    }
    /*
    private void organizeView(int startX, int startY) {
        Question rootQuestion = source.getQuestion(Question.ROOT_QUESTION_ID);
        Map<ReasoningElement, Integer> stageMap = KuabaGraphUtil.getReasoningElementStageMap(rootQuestion);
        
        Queue<ReasoningElement> q = new LinkedList<ReasoningElement>();
        q.add(rootQuestion);
        ReasoningElement currentFather = null;
        int cont = 0;
        HashMap<ReasoningElement, Integer> childNumMap = new HashMap<ReasoningElement, Integer>();
        HashMap<ReasoningElement, ReasoningElement> fatherMap = new HashMap<ReasoningElement, ReasoningElement>();
        childNumMap.put(rootQuestion, 1);
        
        while (!q.isEmpty()) {
            ReasoningElement re = q.poll();
            int x;
            if(currentFather != null) {
                if(!currentFather.equals(fatherMap.get(re))) {
                    cont = 0;
                    currentFather = fatherMap.get(re);
                }
                else
                    cont++;

                x=Math.round(view.getNodePosition(fatherMap.get(re).getId()).getXPos() -((childNumMap.get(fatherMap.get(re)) -1) * X_GAP)/2)+(cont*X_GAP); 
            } else {
                currentFather = re;
                x=startX;
            }
            
            int y=startY+((stageMap.get(re) -1)*Y_GAP);
            
            try {
                view.setNodePosition(re.getId(), new Point(x, y));
            } catch (NoSuchElementException ex) {
                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ModelSessionException ex) {
                Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (re instanceof Question) {
                Question question = (Question) re;
                int elemNum = 0;
                
                for (Idea idea : question.getIsAddressedBy()){
                    if (stageMap.get(idea) > stageMap.get(question)) {
                        q.add(idea);
                        fatherMap.put(idea, re);
                        elemNum++;
                        childNumMap.put(idea, elemNum);
                    }
                }
                
                childNumMap.put(re, elemNum);
            }
            else if (re instanceof Idea) {
                Idea idea = (Idea) re;
                int elemNum = 0;
                
                for (Question question : idea.getSuggests()){
                    if (stageMap.get(question) > stageMap.get(idea)) {
                        q.add(question);
                        fatherMap.put(question, re);
                        elemNum++;
                        childNumMap.put(question, elemNum);
                    }
                }
                
                childNumMap.put(re, elemNum);
            }
        }
    }*/
    
    private void refreshDecisions() {
        Enumeration<Link> linkEnum = view.getLinks();
        decisions.clear();
        
        while (linkEnum.hasMoreElements()) {
            Link l = linkEnum.nextElement();
            
            if (!l.getId().contains(IS_ADDRESSED_BY_ID_COMPONENT)) continue;
            boolean isAccpeted;
            
            if(l.getType().equals(ICoreConstants.SUPPORTS_LINK))
                isAccpeted = true;
            else if(l.getType().equals(ICoreConstants.OBJECTS_TO_LINK))
                isAccpeted = false;
            else continue;
            
            decisions.put(l.getId(), isAccpeted);
        }
        
    }
    
    public void applyDecisions() {
        Enumeration<Link> linkEnum = view.getLinks();
        
        while (linkEnum.hasMoreElements()) {
            Link l = linkEnum.nextElement();
            if (!l.getId().contains(IS_ADDRESSED_BY_ID_COMPONENT)) continue;
            boolean isAccpeted;
            
            if(l.getType().equals(ICoreConstants.SUPPORTS_LINK))
                isAccpeted = true;
            else if(l.getType().equals(ICoreConstants.OBJECTS_TO_LINK))
                isAccpeted = false;
            else continue;
                
            String[] ids = l.getId().split(IS_ADDRESSED_BY_ID_COMPONENT);
            
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
    


    //generic function to change the type of the "is addressed by" links that are associated to the selected nodes, the type must be specified on ICoreConstants
    private void changeSelectedNodes(String linkType) {
            Enumeration nodeEnum = graph.getViewPane().getSelectedNodes();

            while (nodeEnum.hasMoreElements()) {
                NodeSummary n = ((UINode) nodeEnum.nextElement()).getNode();

                //considering only Ideas
                if (n.getType()!=ICoreConstants.POSITION) continue;

                Vector<Link> linksForNode = graph.getView().getLinksForNode(n.getId());
                
                for(int x = 0; x<linksForNode.size(); x++) {
                    Link l = linksForNode.get(x);

                    if (l.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) try {
                        if (linkType.equals(ICoreConstants.EXPANDS_ON_LINK))
                            if (showOnlyIdeas && l.getLabel().equals(""))
                                l.setType(ICoreConstants.ABOUT_LINK);
                            else
                                l.setType(ICoreConstants.EXPANDS_ON_LINK);
                        else
                            l.setType(linkType);
                    } catch (Exception ex) {
                        Logger.getLogger(PopupAcceptSelectionAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
    }
    
    //generic function to change the type of the selected "is addressed by" links, the type must be specified on ICoreConstants
    private void changeSelectedLinks(String linkType) {
            Enumeration linkEnum = graph.getViewPane().getSelectedLinks();

            while (linkEnum.hasMoreElements()) {
                Link l = ((UILink) linkEnum.nextElement()).getLink();
                
                if (l.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) try {
                    if (linkType.equals(ICoreConstants.EXPANDS_ON_LINK))
                        if (showOnlyIdeas && l.getLabel().equals(""))
                            l.setType(ICoreConstants.ABOUT_LINK);
                        else
                            l.setType(ICoreConstants.EXPANDS_ON_LINK);
                    else
                        l.setType(linkType);
                } catch (Exception ex) {
                    Logger.getLogger(PopupAcceptSelectionAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    

    //popup classes

    class PopupAcceptSelectionAction extends AbstractAction {

        private UIMapViewFrame map;

        public PopupAcceptSelectionAction(UIMapViewFrame map) {
            super("Accept Selection");
            this.map = map;
        }

        private void acceptIdea(NodeSummary idea) {
            Vector<Link> linksForNode = map.getView().getLinksForNode(idea.getId());

                for(int x = 0; x<linksForNode.size(); x++) {
                    Link l = linksForNode.get(x);

                    if (l.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) try {
                        if (l.getFrom().getLabel().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX) || l.getFrom().getId().equals(Question.ROOT_QUESTION_ID)) {
                            l.setType(ICoreConstants.SUPPORTS_LINK);
                            acceptQuestion(l.getFrom());
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(PopupAcceptSelectionAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        }

        private void acceptQuestion(NodeSummary question) {
            if(question.getLabel().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)) {

                Vector<Link> questionLinks = map.getView().getLinksForNode(question.getId());
                for(int y = 0; y<questionLinks.size(); y++) {
                    Link lnk = questionLinks.get(y);

                    if (lnk.getLabel().equals(KuabaGraph.SUGGESTS_LABEL) && question.getLabel().contains(lnk.getFrom().getLabel()))
                        acceptIdea(lnk.getFrom());
                }
            }
        }

        public void actionPerformed(ActionEvent e) {
            //old propagation implementation
            if(map.getViewPane().getNumberOfSelectedLinks() == 0) {
                Enumeration nodeEnum = map.getViewPane().getSelectedNodes();

                while (nodeEnum.hasMoreElements()) {
                    NodeSummary n = ((UINode) nodeEnum.nextElement()).getNode();

                    //considering only Ideas
                    if (n.getType()!=ICoreConstants.POSITION) continue;
                    acceptIdea(n);

                }
            }
            else {
            
            //implementation without propagation - based on link selection
//            changeSelectedNodes(ICoreConstants.SUPPORTS_LINK);
            changeSelectedLinks(ICoreConstants.SUPPORTS_LINK);
            }

            map.getViewPane().setSelectedLink(null, ICoreConstants.DESELECTALL);
            map.getViewPane().setSelectedNode(null, ICoreConstants.DESELECTALL);
        }

    }

    class PopupRejectSelectionAction extends AbstractAction {

        private UIMapViewFrame map;

        public PopupRejectSelectionAction(UIMapViewFrame map) {
            super("Reject Selection");
            this.map = map;    
        }

        private void rejectIdea(NodeSummary idea) {
            Vector<Link> linksForNode = map.getView().getLinksForNode(idea.getId());

                for(int x = 0; x<linksForNode.size(); x++) {
                    Link l = linksForNode.get(x);
                    
                    if (l.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) try {
//                        if (l.getFrom().getId().equals(Question.ROOT_QUESTION_ID) || idea.getLabel().equals("Association") || idea.getLabel().equals("AssociationEnd")) {
                            l.setType(ICoreConstants.OBJECTS_TO_LINK);
                                for(int y = 0; y<linksForNode.size(); y++) {
                                    Link l2 = linksForNode.get(y);

                                    if (l2.getId().contains(KuabaGraph.SUGGESTS_ID_COMPONENT)) 
                                        rejectQuestion(l2.getTo());
                                }
//                        }

                    } catch (Exception ex) {
                        Logger.getLogger(PopupAcceptSelectionAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        }

        private void rejectQuestion(NodeSummary question) {
//            if(question.getLabel().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)) {

                Vector<Link> questionLinks = map.getView().getLinksForNode(question.getId());
                for(int y = 0; y<questionLinks.size(); y++) {
                    Link lnk = questionLinks.get(y);

                    if (lnk.getId().contains(KuabaGraph.IS_ADDRESSED_BY_ID_COMPONENT)) try {
                        lnk.setType(ICoreConstants.OBJECTS_TO_LINK);
                        if(!question.getLabel().equals("participant?")) rejectIdea(lnk.getTo()); //avoid to reject the participants when rejecting an association
                    } catch (Exception ex) {
                        Logger.getLogger(KuabaGraph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
//            }
        }

        public void actionPerformed(ActionEvent e) {

            if(map.getViewPane().getNumberOfSelectedLinks() == 0) {
                Enumeration nodeEnum = map.getViewPane().getSelectedNodes();

                while (nodeEnum.hasMoreElements()) {
                    NodeSummary n = ((UINode) nodeEnum.nextElement()).getNode();

                    //considering only Ideas
                    if (n.getType()!=ICoreConstants.POSITION) continue;
                    rejectIdea(n);

                }
            }
            else {
            
            //implementation without propagation - based on link selection
//            changeSelectedNodes(ICoreConstants.OBJECTS_TO_LINK);
            changeSelectedLinks(ICoreConstants.OBJECTS_TO_LINK);
            }

            map.getViewPane().setSelectedLink(null, ICoreConstants.DESELECTALL);
            map.getViewPane().setSelectedNode(null, ICoreConstants.DESELECTALL);
        }
    }

    class PopupClearDecisionsAction extends AbstractAction {

        private UIMapViewFrame map;

        public PopupClearDecisionsAction(UIMapViewFrame map) {
            super("Clear Decisions");
            this.map = map;
        }


        public void actionPerformed(ActionEvent e) {

            //if no links selected, clear decisions around the selected node
            //if links are selected, clear the decision of the selected links
            if(map.getViewPane().getNumberOfSelectedLinks() == 0)
                changeSelectedNodes(ICoreConstants.EXPANDS_ON_LINK);
            else
                changeSelectedLinks(ICoreConstants.EXPANDS_ON_LINK);

            map.getViewPane().setSelectedLink(null, ICoreConstants.DESELECTALL);
            map.getViewPane().setSelectedNode(null, ICoreConstants.DESELECTALL);
        }

    }

    class DecisionMakerPopup extends MouseAdapter {

        private JPopupMenu popup;
        private UIMapViewFrame map;

        public DecisionMakerPopup(UIMapViewFrame map) {
            super();

            this.map=map;

            popup = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem("Accept Selection");
            menuItem.setAction(new PopupAcceptSelectionAction(map));
            popup.add(menuItem);
            menuItem = new JMenuItem("Reject Selection");
            menuItem.setAction(new PopupRejectSelectionAction(map));
            popup.add(menuItem);
            menuItem = new JMenuItem("Clear Decisions");
            menuItem.setAction(new PopupClearDecisionsAction(map));
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
//            if (e.isPopupTrigger() && map.getViewPane().getNumberOfSelectedNodes() > 0) {
//            if (e.isPopupTrigger() && map.getViewPane().getNumberOfSelectedLinks() > 0) {
            if (e.isPopupTrigger() && (map.getViewPane().getNumberOfSelectedLinks() > 0 || map.getViewPane().getNumberOfSelectedNodes() > 0)) {
                popup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
    }
}