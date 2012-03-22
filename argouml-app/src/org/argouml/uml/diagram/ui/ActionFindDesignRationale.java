// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.jmi.reflect.RefObject;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.MDC;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;

import com.compendium.core.ICoreConstants;
import com.compendium.core.datamodel.Link;
import com.compendium.core.datamodel.ModelSessionException;
import com.compendium.core.datamodel.NodeSummary;
import com.compendium.core.datamodel.PCSession;
import com.compendium.core.datamodel.View;
import com.compendium.ui.UIMapViewFrame;

import br.ucam.kuabaSubsystem.controller.ArgumentViewController;
import br.ucam.kuabaSubsystem.controller.JustificationViewController;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.graph.KuabaGraph;
import br.ucam.kuabaSubsystem.gui.JustificationView;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;

public class ActionFindDesignRationale extends UndoableAction {
    private RefObject value;
    private String valueType;    
    public ActionFindDesignRationale(RefObject value) {
        super((String)value.refGetValue("name"));
        this.value = value;
        this.valueType = (String)this.value.
        refMetaObject().refGetValue("name");        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        KuabaRepository modelRepository = KuabaSubsystem.facade.modelRepository();   
        
        List<Idea> elementDomainIdeas = 
            modelRepository.getDomainIdeasWhereIdLike("_"+
                    Model.getFacade().getUUID(value).split(":")[3]);
        Idea domainIdea = null;
        String elementName = "";
        
        if(!(this.value.refGetValue("name") == null))        
            elementName = (String)this.value.refGetValue("name");
        
        for (Idea idea : elementDomainIdeas)
            if(idea.getHasText().equals(elementName))
                domainIdea = idea;
        showMap(domainIdea);
//        
//        if(domainIdea != null){
//            Idea designIdea = 
//                modelRepository.findFirstIdeaByText(
//                    (Question)domainIdea.listSuggests().next(), 
//                    this.valueType);
//            ArgumentViewController controller = 
//                new ArgumentViewController(designIdea);
//            controller.render();
//        }        
    } 
    
    public void showMap(Idea domainIdea){
//        com.compendium.core.datamodel.Model m = new com.compendium.core.datamodel.Model("Test");
//        try {
//                m.initialize();
//        } catch (UnknownHostException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//        } catch (SQLException e) {
//            // TODO: Auto-generated catch block
//        }
//        PCSession session = m.getSession();             
//
//        View v = new View("01");
//        v.initialize(session, m);
//        try {
//                domainIdea.getView(v, 1000, 0);
//                v.setType(ICoreConstants.MAPVIEW, "Thiago");
//        } catch (Exception e) {
//                // TODO Auto-generated catch block
//                JOptionPane.showMessageDialog(null, e.getStackTrace());
//        }
        KuabaGraph kg = new KuabaGraph(domainIdea.getRepository());
//        UIMapViewFrame map = null;              
//        try{
//                map = new UIMapViewFrame(v);
//                map.setClosable(false);
//                map.setResizable(false);
                JFrame frame = new JFrame();
                frame.setSize(700,590);
                frame.setLocationRelativeTo(null);
                frame.add(kg.generateSubGraph(domainIdea, true, true));
                frame.setVisible(true);
//                map.setVisible(true);
//        }catch (Exception e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, e.getMessage());
//        }       
        
    }    

}
