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

package org.argouml.util;

import java.util.ArrayList;
import java.util.List;

import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import javax.jmi.reflect.RefObject;
import javax.swing.Action;

import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.ui.ActionFindDesignRationale;

import br.ucam.kuabaSubsystem.util.MofHelper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.ActionShowCompleteDesignRationale;

public class PopUpHelper {
    private static List<RefObject> visitedRefObjects = new ArrayList<RefObject>();
    
    public static ArgoJMenu buildDesignRationalePopUp(RefObject source){
        visitedRefObjects.clear();
        ArgoJMenu result =  build(source);
        Action showFullDRModel = new ActionShowCompleteDesignRationale(Model.getFacade().getName(Model.getFacade().getRootElements().iterator().next()));
        result.add(showFullDRModel);
        return result;
    }
    
    public static ArgoJMenu build(RefObject source){
        
        visitedRefObjects.add(source);
        String name = "";        
        if(source.refGetValue("name") != null)
            name = (String)source.refGetValue("name");        
        ArgoJMenu designRationaleMenu = 
            new ArgoJMenu(name);            
        Action ownerDesignRationale = new ActionFindDesignRationale(source);        
        designRationaleMenu.add(ownerDesignRationale);        
        MofClass ownerMofClass = (MofClass)source.refMetaObject();        
        List<Reference> references = MofHelper.referencesOfClass(
                ownerMofClass, true);        
        for (Reference reference : references) {                        
            if(source.refGetValue(reference.getName()) 
                    instanceof List){                
                List<RefObject> refObjects = 
                    (List<RefObject>)source.refGetValue(
                            reference.getName());
                for (RefObject refValue : refObjects) {
                    Action action = new ActionFindDesignRationale(refValue);
                    ArgoJMenu menu = null;
                    if(!visitedRefObjects.contains(refValue)){                        
                        menu = build(refValue);                        
                        designRationaleMenu.add(menu);
                    }
                    //else
                        //designRationaleMenu.add(action);         
                }               
            }                
//            if(source.refGetValue(reference.getName())
//                    instanceof RefObject){
//                RefObject refValue = (RefObject)source.refGetValue(
//                        reference.getName()); 
//                Action action = new ActionFindDesignRationale(refValue);
//                ArgoJMenu menu = null;
//                if(!visitedRefObjects.contains(refValue)){                    
//                    menu = build(refValue);                            
//                    designRationaleMenu.add(menu);
//                }
//                //else
//                  //  designRationaleMenu.add(action);                
//            }
        }

        return designRationaleMenu;
        
    }
    

}
