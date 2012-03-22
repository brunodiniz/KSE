// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2009 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.Color;
import java.util.List;

import javax.jmi.reflect.RefObject;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.swingext.UpArrowIcon;
import org.tigris.gef.presentation.Fig;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Vertical;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.ArgumentViewController;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.gui.ArgumentView;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.MofHelper;

public class TabDesignRationale extends PropPanel {
    private JPanel lastPanel;
    @Override
    public void setTarget(Object t) {        
        super.setTarget(t);
        if(this.getTarget() != null){
            if(this.lastPanel != null)
                this.remove(this.lastPanel);
            RefObject target = (RefObject)this.getTarget();
            String xmiId = KuabaSubsystem.resolver.resolveXmiId(target);
            
            String name = "";
            if(target.refGetValue("name") != null)
                name = (String)target.refGetValue("name");
            Idea idea = KuabaHelper.getDomainIdea(
                    KuabaSubsystem.facade.modelRepository(), xmiId, name);
            if (idea == null)
                return;            
            Idea designIdea = KuabaHelper.getAcceptedDesignIdea(idea, 
                    (String)target.refMetaObject().refGetValue("name"));
            ArgumentViewController controller = new ArgumentViewController(designIdea);
            ArgumentView view = new ArgumentView(controller);
            view.setIdeaText(designIdea.getHasText());
            JPanel panel = view.getArgumentViewPanel();
            panel.remove(view);
            view.setVisible(false);
            view.dispose();
            this.add(panel);
            this.lastPanel = panel;
            
        }
    }

    private static String orientation = Configuration.getString(Configuration
            .makeKey("layout", "tabdocumentation"));

    /**
     * Construct new documentation tab
     */
    public TabDesignRationale() {
        super("Design Rationale", (ImageIcon) null);        
    }
    
    private void disableTextArea(final JTextArea textArea) {
        textArea.setRows(2);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEnabled(false);
        textArea.setDisabledTextColor(textArea.getForeground());
        // Only change the background colour if it is supplied by the LAF.
        // Otherwise leave look and feel to handle this itself.
        final Color inactiveColor =
            UIManager.getColor("TextField.inactiveBackground");
        if (inactiveColor != null) {
            // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4919687
            textArea.setBackground(new Color(inactiveColor.getRGB()));
        }
    }

    /**
     * Checks if the tab should be enabled. Returns true if the target
     * returned by getTarget is a modelelement or if that target shows up as Fig
     * on the active diagram and has a modelelement as owner.
     *
     * @return true if this tab should be enabled, otherwise false.
     */
    public boolean shouldBeEnabled() {
        Object target = getTarget();
        return shouldBeEnabled(target);
    }

    /*
     * @see org.argouml.uml.ui.PropPanel#shouldBeEnabled(java.lang.Object)
     */
    @Override
    public boolean shouldBeEnabled(Object target) {
        target = (target instanceof Fig) ? ((Fig) target).getOwner() : target;
        return Model.getFacade().isAModelElement(target);
    }


}
