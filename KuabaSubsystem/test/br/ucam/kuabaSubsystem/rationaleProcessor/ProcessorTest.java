/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.rationaleProcessor;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.abstractTestCase.FunctionalTestCase;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.graph.KuabaGraph;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.repositories.OwlApiFileGateway;
import br.ucam.kuabaSubsystem.repositories.RepositoryLoadException;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import com.compendium.core.ICoreConstants;
import com.compendium.core.datamodel.Link;
import com.compendium.core.datamodel.Model;
import com.compendium.core.datamodel.ModelSessionException;
import com.compendium.core.datamodel.NodeSummary;
import com.compendium.core.datamodel.PCSession;
import com.compendium.core.datamodel.View;
import com.compendium.ui.UIMapViewFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author Bruno
 */
public class ProcessorTest extends FunctionalTestCase implements ActionListener, MouseListener{
    
    private KuabaRepository repo2, repo3;
    private KuabaProcessor kp = new KuabaProcessor();
    private EquivalenceRuleManager erm;
    private JPopupMenu popup;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        KuabaSubsystem.gateway = gate;
        
        repo2 = gate.createNewRepository();
        repo3 = gate.createNewRepository();
        
        KuabaModelFactory factory2 = repo2.getModelFactory();

        FormalModel uml = factory2.createFormalModel("UML");
        Idea clazz = factory2.createIdea("class");
        Idea athlete = factory2.createIdea("athlete");
        Idea attribute = factory2.createIdea("attribute");
        Idea person = factory2.createIdea("person");
        Idea email = factory2.createIdea("email");
        Idea cat = factory2.createIdea("cat");
        Question how_model_athlete = factory2.createQuestion("how_model_athlete");
        Question how_model_person = factory2.createQuestion("how_model_person");
        Question what_elements = factory2.createQuestion(Question.ROOT_QUESTION_ID); 
        Question how_model_email = factory2.createQuestion("how_model_email");
        Question email_owner = factory2.createQuestion("email_owner");
//        Question how_model_cd = factory2.createQuestion("how_model_cd");      
      
        //formal models
        uml.setHasName("Unified Modeling Language");
        uml.addHasLocalization("www.omg.org");
        
        //ideas
        clazz.setHasText("Class");
        clazz.addAddress(how_model_person);
        clazz.setIsDefinedBy(uml);
        
        person.setHasText("Person");      
        person.addSuggests(how_model_person);
        person.addAddress(what_elements);
        
        attribute.setHasText("Attribute");     
        attribute.addSuggests(email_owner);
        attribute.addAddress(how_model_email);
        attribute.setIsDefinedBy(uml);
        
        email.setHasText("email");       
        email.addSuggests(how_model_email);
        email.addAddress(what_elements);
        
        athlete.setHasText("Athlete");

        cat.setHasText("Cat");
        
        
        //questions
//        how_model_cd.setHasType(Question.XORTYPE);
//        how_model_cd.setIsDefinedBy(uml);
        
        what_elements.setHasType(Question.ORTYPE);
        what_elements.setHasText("What are the Model Elements?");
        what_elements.setIsDefinedBy(uml);
        what_elements.addIsAddressedBy(person);
        what_elements.addIsAddressedBy(email);
        what_elements.addIsAddressedBy(cat);
       
        how_model_athlete.setHasType(Question.XORTYPE);
        how_model_athlete.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"athlete");
        
        how_model_person.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"Person?");
        how_model_person.addIsSuggestedBy(person);
        how_model_person.addIsAddressedBy(clazz);

        
        how_model_email.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"email?");
        how_model_email.addIsAddressedBy(attribute);
        how_model_email.addIsSuggestedBy(email);
        
        email_owner.setHasText("Owner?");
        email_owner.addIsAddressedBy(clazz);
        email_owner.addIsSuggestedBy(attribute);
        
        
        
        
        
        KuabaModelFactory factory3 = repo3.getModelFactory();

        FormalModel uml3 = factory3.createFormalModel("UML");
        Idea clazz3 = factory3.createIdea("class");
        Idea attribute3 = factory3.createIdea("attribute");
        Idea music = factory3.createIdea("music");
        Idea email3 = factory3.createIdea("email");
        Idea cd = factory3.createIdea("cd");
        Question how_model_music = factory3.createQuestion("how_model_music");
        Question how_model_cd = factory3.createQuestion("how_model_cd");
        Question what_elements3 = factory3.createQuestion(Question.ROOT_QUESTION_ID); 
        Question how_model_email3 = factory3.createQuestion("how_model_email");
        Question email_owner3 = factory3.createQuestion("email_owner"); 
        Argument email_arg = factory3.createArgument("email_arg");
        
        email_arg.setHasText("E-mail é importante!");
        email_arg.addInFavorOf(email3);
        email_arg.setConsiders(what_elements3);
        
        //ideas
        clazz3.setHasText("Class");
        clazz3.addAddress(how_model_cd);
        clazz3.setIsDefinedBy(uml3);
        
        attribute3.setHasText("Attribute");     
        attribute3.addSuggests(email_owner3);
        attribute3.addAddress(how_model_email3);
        attribute3.setIsDefinedBy(uml3);
        
        email3.setHasText("email");       
        email3.addSuggests(how_model_email3);
        email3.addAddress(what_elements3);
        email3.addHasArgument(email_arg);
        
        cd.setHasText("CD");
        cd.addSuggests(how_model_cd);
        cd.addAddress(what_elements3);

        music.setHasText("Music");
        music.addSuggests(how_model_music);
        music.addAddress(what_elements3);
        
        
        //questions
        how_model_cd.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"CD?");
        how_model_cd.setHasType(Question.XORTYPE);
        how_model_cd.setIsDefinedBy(uml3);
        
        what_elements3.setHasType(Question.ORTYPE);
        what_elements3.setHasText("What are the Model Elements?");
        what_elements3.setIsDefinedBy(uml3);
//        what_elements3.addIsAddressedBy(person);
//        what_elements3.addIsAddressedBy(email3);
//        what_elements3.addIsAddressedBy(cat);
       
        
        how_model_music.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"Music?");
        how_model_music.addIsAddressedBy(attribute3);

        
        how_model_email3.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"email?");
        how_model_email3.addIsAddressedBy(attribute3);
        how_model_email3.addIsSuggestedBy(email3);
        
        email_owner3.setHasText("Owner?");
//        email_owner3.addIsAddressedBy(clazz3);
        email_owner3.addIsSuggestedBy(attribute3);
        
        HashSet<Idea> domainIdeas = new HashSet<Idea>();
        domainIdeas.addAll(repo.getDomainIdeas());
        domainIdeas.addAll(repo2.getDomainIdeas());
        domainIdeas.addAll(repo3.getDomainIdeas());
        
        erm = new EquivalenceRuleManager(domainIdeas);
        kp.setEquivalences(erm);
    }
    
    @Override
    protected void tearDown() throws Exception {		
        super.tearDown();
        erm.removeAllEquivalenceRules();
    }
    
    public void testEquivalency() {
        
        assertFalse(kp.compareIdeas(repo.getIdea("dog"), repo2.getIdea("cat")));
        assertTrue(kp.compareIdeas(repo.getIdea("email"), repo2.getIdea("email")));
        assertTrue(kp.compareIdeas(repo.getIdea("class"), repo2.getIdea("class")));
        assertFalse(kp.compareIdeas(repo.getIdea("class"), repo2.getIdea("attribute")));
        assertFalse(kp.compareIdeas(repo.getIdea("person"), repo2.getIdea("email")));
        assertFalse(kp.compareIdeas(repo.getIdea("person"), repo2.getIdea("class")));
        
        
        assertTrue(kp.compareQuestions(repo.getQuestion("how_model_person"), repo2.getQuestion("how_model_person")));
        assertTrue(kp.compareQuestions(repo.getQuestion("email_owner"), repo2.getQuestion("email_owner")));
        assertFalse(kp.compareQuestions(repo.getQuestion("how_model_person"), repo2.getQuestion("email_owner")));
        assertFalse(kp.compareQuestions(repo.getQuestion("how_model_person"), repo2.getQuestion("how_model_email")));
        
        
        TreeSet<String> set = new TreeSet<String>();
        set.add("person");
        set.add("email");
        
        try {
            erm.addEquivalenceRule(set);
        } catch (Exception ex) {
            Logger.getLogger(ProcessorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        assertFalse(kp.compareIdeas(repo.getIdea("person"), repo2.getIdea("email")));
//        assertFalse(kp.compareQuestions(repo.getQuestion("how_model_person"), repo2.getQuestion("how_model_email")));
//        
//        set = new TreeSet<String>();
//        set.add("email");
//        eqMap.put("person", set);
////        kp.setEquivalenceMap(eqMap);
        
        assertTrue(kp.compareIdeas(repo.getIdea("person"), repo2.getIdea("email")));
        assertTrue(kp.compareQuestions(repo.getQuestion("how_model_person"), repo2.getQuestion("how_model_email")));   
    }
   
    public void testUnion() {
        Set<KuabaRepository> set = new HashSet<KuabaRepository>();
        set.add(repo2);
        set.add(repo3);
        KuabaRepository result = kp.union(repo, set);
//        assertTrue(result.getAllIdeas().contains(repo.getIdea("email")));
        gate.save(result, new File("testUnion.xml"));
        
        
        
        Model m = new Model("Test");
        PCSession session = m.getSession();

        
        View v = new View("01");        
        v.initialize(session, m);
        
//        popup = new JPopupMenu();
//        JMenuItem menuItem = new JMenuItem("A popup menu item");
//        menuItem.addActionListener(this);
//        popup.add(menuItem);
//        menuItem = new JMenuItem("Another popup menu item");
//        menuItem.addActionListener(this);
//        popup.add(menuItem);

//        try {
//                result.getQuestion(Question.ROOT_QUESTION_ID).getView(v, 100, 0);
//                v.setType(ICoreConstants.MAPVIEW, "Thiago");
//        } catch (Exception e) {
//                // TODO Auto-generated catch block                       
//                JOptionPane.showMessageDialog(null, e.getStackTrace());
//        }
        
        KuabaGraph kg = new KuabaGraph(result);


                JFrame frame = new JFrame();
                frame.setSize(700,590);
                frame.setLocationRelativeTo(null);
                frame.add(kg.generateFullGraph(true, false, true, true, false));
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                map.setVisible(true);

    }
    
    public void testUnion2() {
        try {
            KuabaRepository repo1 = OwlApiFileGateway.getInstance().load("/Users/Bruno/Documents/NetBeansProjects/Kuaba/argouml-app/designRationale/tt.zargo.xml");
            KuabaRepository repo2 = OwlApiFileGateway.getInstance().load("/Users/Bruno/Documents/NetBeansProjects/Kuaba/argouml-app/designRationale/tt2.zargo.xml");
            
            TreeSet<String> set = new TreeSet<String>();
            set.add("musica");
            set.add("cd");

            
            kp = new KuabaProcessor();
            
            HashSet<Idea> domainIdeas = new HashSet<Idea>();
            domainIdeas.addAll(repo1.getDomainIdeas());
            domainIdeas.addAll(repo2.getDomainIdeas());

            erm = new EquivalenceRuleManager(domainIdeas);
            kp.setEquivalences(erm);
            
            erm.addEquivalenceRule(set);
            
            KuabaRepository result = kp.union(repo1, repo2);
            gate.save(result, new File("testUnion.xml"));
            
            KuabaGraph kg = new KuabaGraph(result);


                JFrame frame = new JFrame();
                frame.setSize(700,590);
                frame.setLocationRelativeTo(null);
                frame.add(kg.generateFullGraph(true, false, true, true, false));
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
        } catch (Exception ex) {
            Logger.getLogger(ProcessorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
       ProcessorTest test = new ProcessorTest();
        try {
//            test.setUp();
//            test.testUnion();
            test.testUnion2();
        } catch (Exception ex) {
            Logger.getLogger(ProcessorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    public void actionPerformed(ActionEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseClicked(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }

    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
