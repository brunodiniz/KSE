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

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.argouml.ui.UndoableAction;

import com.compendium.core.ICoreConstants;
import com.compendium.core.datamodel.Link;
import com.compendium.core.datamodel.ModelSessionException;
import com.compendium.core.datamodel.NodeSummary;
import com.compendium.core.datamodel.PCSession;
import com.compendium.core.datamodel.View;
import com.compendium.ui.UIMapViewFrame;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.graph.KuabaGraph;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import org.argouml.module.kuabaModule.ui.DRViewPanel;

public class ActionShowCompleteDesignRationale extends UndoableAction {
    
    public ActionShowCompleteDesignRationale(String modelName) {
        super(modelName);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showMap(KuabaSubsystem.facade.getRootQuestion());      
    } 
    
    public void showMap(Question rootQuestion){
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
//
//        try {
//                rootQuestion.getView(v, 1000, 0);
//                v.setType(ICoreConstants.MAPVIEW, "Thiago");
//        } catch (Exception e) {
//                // TODO Auto-generated catch block
//                JOptionPane.showMessageDialog(null, e.getStackTrace());
//        }
        DRViewPanel drView = new DRViewPanel();
//        UIMapViewFrame map = null;              
//        try{
//                map = new UIMapViewFrame(v);
//                map.setClosable(false);
//                map.setResizable(false);
                JFrame frame = new JFrame();
                frame.setSize(700,590);
                frame.setLocationRelativeTo(null);
                frame.add(drView);
                drView.showFullDRView(rootQuestion.getRepository());
                frame.setVisible(true);
//                map.setVisible(true);
//        }catch (Exception e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, e.getMessage());
//        }       
        
    }    

}
