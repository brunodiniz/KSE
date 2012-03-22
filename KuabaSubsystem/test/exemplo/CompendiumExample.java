package exemplo;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import br.ucam.kuabaSubsystem.abstractTestCase.FunctionalTestCase;
import br.ucam.kuabaSubsystem.graph.KuabaGraph;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import com.compendium.core.ICoreConstants;
import com.compendium.core.datamodel.Link;
import com.compendium.core.datamodel.Model;
import com.compendium.core.datamodel.ModelSessionException;
import com.compendium.core.datamodel.NodeSummary;
import com.compendium.core.datamodel.PCSession;
import com.compendium.core.datamodel.View;
import com.compendium.ui.UIMapViewFrame;

public class CompendiumExample extends FunctionalTestCase {

	
	public CompendiumExample() throws SQLException {
		super();
		try {
			this.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Question q =this.templatesRepository.getQuestion("How_Model_");
                System.out.println(q.getHasText());
		Idea idea = (Idea)q.listIsAddressedBy().next();
		this.facade.makeDecision(q, idea, true);
		Argument arg = this.factory.createArgument("01");
		arg.setHasText("Argument in favor of class");
		arg.addInFavorOf(idea);
		idea.addHasArgument(arg);
//		Model m = new Model("Test");
//		try {                   
//			m.initialize();
//		} catch (UnknownHostException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		PCSession session = m.getSession();		
//		NodeSummary n = new NodeSummary();
//		NodeSummary from = new NodeSummary();
//		
//		n.initialize(session, m);
//		from.initialize(session, m);
//		try {
//			n.setId("id1");
//			n.setType(ICoreConstants.ARGUMENT, "Thiago");
//			n.setLabel("testLabel", "Thiago");
//			n.setCreationDate(new Date(), "Thiago");
//			from.setCreationDate(new Date(), "Thiago");
//			from.setId("id2");
//			from.setType(ICoreConstants.ISSUE, "Thiago");
//			from.setLabel("asasdf", "Thiago");
//		} catch (ModelSessionException e) {
//			// TODO Auto-generated catch block
//			JOptionPane.showMessageDialog(null, e.getStackTrace());
//		}
//		View v = new View("01");
//		v.initialize(session, m);
//		Link l = new Link(ICoreConstants.OBJECTS_TO_LINK, from, n, "address", Link.ARROW_FROM);
//		l.setId("newId");
//		try {
//			q.getView(v, 1000, 0);
//			v.setType(ICoreConstants.MAPVIEW, "Thiago");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block                       
//			JOptionPane.showMessageDialog(null, e.getStackTrace());
//		}
//		UIMapViewFrame map = null;		
		try{
//			map = new UIMapViewFrame(v);	
                        KuabaGraph kg = new KuabaGraph(repo);
			JFrame frame = new JFrame();
			frame.setSize(600,600);
			frame.add(kg.generateFullGraph(false, true, false, true));
			frame.setVisible(true);
//			map.setVisible(true);
		}catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}	
        }

	public static void main(String[] args) throws SQLException {
		new CompendiumExample();
//            print();
	}
	
	public static void print() throws SQLException{
		
		//ProjectCompendium.APP.onFileOpen();
		Model m = new Model("Test");
		try {
			m.initialize();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PCSession session = m.getSession();		
		NodeSummary n = new NodeSummary();
		NodeSummary from = new NodeSummary();
		
		n.initialize(session, m);
		from.initialize(session, m);
		try {
			n.setId("id1");
			n.setType(ICoreConstants.ARGUMENT, "Thiago");
			n.setLabel("testLabel", "Thiago");
			n.setCreationDate(new Date(), "Thiago");
			from.setCreationDate(new Date(), "Thiago");
			from.setId("id2");
			from.setType(ICoreConstants.ISSUE, "Thiago");
			from.setLabel("asasdf", "Thiago");
		} catch (ModelSessionException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getStackTrace());
		}
		View v = new View("01");
		v.initialize(session, m);
		Link l = new Link(ICoreConstants.OBJECTS_TO_LINK, from, n, "address", Link.ARROW_FROM);
		l.setId("newId");
		try {
			v.addNodeToView(n, 10, 10);
			v.addNodeToView(from, 100, 100);
			boolean b = v.addLinkToView(l);
			v.setType(ICoreConstants.MAPVIEW, "Thiago");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getStackTrace());
		}
		UIMapViewFrame map = null;		
		try{
		map = new UIMapViewFrame(v);		
		JFrame frame = new JFrame();
		frame.setSize(200,200);
		frame.add((JInternalFrame)map);
		frame.setVisible(true);
		map.setVisible(true);
		}catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
	}
}
