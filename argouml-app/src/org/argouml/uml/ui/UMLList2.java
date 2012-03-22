// $Id: UMLList2.java 15954 2008-11-02 09:27:35Z mvw $
// Copyright (c) 1996-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.uml.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.jmi.model.MofClass;
import javax.jmi.reflect.RefObject;
import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;
import org.argouml.uml.diagram.ui.ActionRenderArgumentView;
import org.argouml.util.PopUpHelper;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.MofHelper;

/**
 * This class is derived from a Swing JList, and adds:<p>
 *
 * Mouselistener for the implementation of a popup menu.
 * The popup menu itself is to be created by the model.<p>
 *
 * TargettableModelView: Which determines that the model of this list
 * listens to target changes, i.e. implements the TargetListener interface.
 *
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLList2
    extends JList
    implements TargettableModelView, MouseListener {

    private static final Logger LOG = Logger.getLogger(UMLList2.class);
    /**
     * Constructor for UMLList2. Used by subclasses that want to add their own
     * renderer to the list.
     * @param dataModel the data model
     * @param renderer the renderer
     */
    protected UMLList2(ListModel dataModel, ListCellRenderer renderer) {
        super(dataModel);
        setDoubleBuffered(true);
        if (renderer != null) {
            setCellRenderer(renderer);
        }
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        addMouseListener(this);
    }

    /**
     * Getter for the target. First approach to get rid of the container.
     * @return Object
     */
    public Object getTarget() {
        return ((UMLModelElementListModel2) getModel()).getTarget();
    }

    /*
     * @see TargettableModelView#getTargettableModel()
     */
    public TargetListener getTargettableModel() {
        return (TargetListener) getModel();
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        showPopup(e);
    }
    
    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        if (hasPopup()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }
    
    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        if (hasPopup()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        showPopup(e);
    }
    
    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        showPopup(e);
    }

    private final void showPopup(MouseEvent event) {
        if (event.isPopupTrigger()
                && !Model.getModelManagementHelper().isReadOnly(getTarget())) {
            Point point = event.getPoint();
            int index = locationToIndex(point);
            JPopupMenu popup = new JPopupMenu();            
            ListModel lm = getModel();
            if(index >= 0)
                popup.add(buildDesignRationalePopup(
                        (RefObject)lm.getElementAt(index),
                        (RefObject)this.getTarget()));
            if (lm instanceof UMLModelElementListModel2) {
                if (((UMLModelElementListModel2) lm).buildPopup(popup, index)) {
                    LOG.debug("Showing popup");
                    popup.show(this, point.x, point.y);
                }
                else
                    popup.show(this, point.x, point.y);
            }
        }
    }
    
    private Action buildDesignRationalePopup(RefObject source, RefObject target){
        //ArgoJMenu designRationaleMenu = new ArgoJMenu("Design Rationale");        
        RefObject targetMetaObject = target.refMetaObject();
        
        if(targetMetaObject.refGetValue("name").equals("Generalization")
                ||(targetMetaObject.refGetValue("name").equals("Abstraction"))
                ||(targetMetaObject.refGetValue("name").equals("Usage"))){
            RefObject swap;
            swap = target;
            target = source;
            source = swap;
        }
        
        String relationship = MofHelper.discoverRelationship(source, target);
        Idea sourceDomainIdea = this.getDomainIdea(source);
        Idea sourceDesignIdea = KuabaHelper.getAcceptedDesignIdea(sourceDomainIdea,
                (String)source.refMetaObject().refGetValue("name"));      
        Question relationshipQuestion = (Question)KuabaHelper.getReasoningElementInTree(
                sourceDesignIdea, relationship + "?");
        Idea targetDomainIdea  = this.getDomainIdea(target);
        Idea designIdea = KuabaHelper.getAcceptedDesignIdea(targetDomainIdea,
                (String)target.refMetaObject().refGetValue("name"));      
        //designRationaleMenu.add(new ActionRenderArgumentView(designIdea, relationshipQuestion));        
        return new ActionRenderArgumentView(designIdea, relationshipQuestion);
        
    }
    
    private Idea getDomainIdea(RefObject refObject){
        KuabaRepository repository = KuabaSubsystem.facade.modelRepository();
        String xmiId = KuabaSubsystem.resolver.resolveXmiId(refObject);
        Idea domainIdea = KuabaHelper.getDomainIdea(repository, xmiId, 
                (String)refObject.refGetValue("name"));
        return domainIdea;
    }
    protected boolean hasPopup() {
        if (getModel() instanceof UMLModelElementListModel2) {
            return ((UMLModelElementListModel2) getModel()).hasPopup();
        }
        return false;
    }
}
