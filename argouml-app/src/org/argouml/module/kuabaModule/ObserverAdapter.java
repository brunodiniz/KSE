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

package org.argouml.module.kuabaModule;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefException;
import javax.jmi.reflect.RefFeatured;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import org.argouml.model.Model;

public class ObserverAdapter implements PropertyChangeListener {
    private PropertyChangeListener adaptee;
    
    public ObserverAdapter(PropertyChangeListener adaptee) {
        super();
        this.adaptee = adaptee;
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        
        arg0.setPropagationId(Model.getFacade().getUUID(arg0.getSource())
                .split(":")[3]);
        System.out.println(Model.getFacade().getUUID(arg0.getSource())
                .split(":")[3]);
      /*  Object newvalue = null;
        if (arg0.getNewValue() instanceof RefObject)
            newvalue = new UUIDRefObject((RefObject)arg0.getNewValue());
        else
            newvalue = arg0.getNewValue();
        
        Object oldValue = null;
        if(arg0.getOldValue() instanceof RefObject)
            oldValue = new UUIDRefObject((RefObject)arg0.getOldValue());
        else
            oldValue = arg0.getOldValue();
        
        Object source = null;
        if(arg0.getSource() instanceof RefObject)
            source = new UUIDRefObject((RefObject)arg0.getSource());
        else
            source = arg0.getOldValue();
        
            
        
        PropertyChangeEvent evt = new PropertyChangeEvent(
                source, arg0.getPropertyName(), oldValue,newvalue);*/
        this.adaptee.propertyChange(arg0);
        

    }
    
    @Override
    public boolean equals(Object obj) {

        return this.adaptee.equals(obj);
    }

    @Override
    public int hashCode() {
        // TODO: Auto-generated method stub
        return this.adaptee.hashCode();
    }

    public class UUIDRefObject implements RefObject{
        private RefObject object;
        
        @Override
        public boolean equals(Object obj) {
            
            return this.object.equals(obj);
        }


        @Override
        public int hashCode() {
            // TODO: Auto-generated method stub
            return this.object.hashCode();
        }

        
        public UUIDRefObject(RefObject object) {
            super();
            this.object = object;
        }


        public RefClass refClass() {
            
            return this.object.refClass();
        }

        
        public void refDelete() {
            this.object.refDelete();
            
        }

        
        public RefFeatured refImmediateComposite() {

            return this.object.refImmediateComposite();
        }

        
        public boolean refIsInstanceOf(RefObject arg0, boolean arg1) {
            
            return this.object.refIsInstanceOf(arg0, arg1);
        }

        
        public RefFeatured refOutermostComposite() {
            
            return this.object.refOutermostComposite();
        }

        
        public Object refGetValue(RefObject arg0) {

            return this.object.refGetValue(arg0);
        }

        
        public Object refGetValue(String arg0) {

            return this.object.refGetValue(arg0);
        }

        
        public Object refInvokeOperation(RefObject arg0, List arg1)
            throws RefException {

            return this.object.refInvokeOperation(arg0, arg1);
        }

        
        public Object refInvokeOperation(String arg0, List arg1)
            throws RefException {

            return this.object.refInvokeOperation(arg0, arg1);
        }

        
        public void refSetValue(RefObject arg0, Object arg1) {
            this.object.refSetValue(arg0, arg1);
            
        }

        
        public void refSetValue(String arg0, Object arg1) {
            this.object.refSetValue(arg0, arg1);
            
        }

        
        public RefPackage refImmediatePackage() {
            
            return this.object.refImmediatePackage();
        }

        
        public RefObject refMetaObject() {

            return this.object.refMetaObject();
        }

        
        public String refMofId() {
         
            return Model.getFacade().getUUID(this.object).split(":")[3];
        }

        
        public RefPackage refOutermostPackage() {

            return this.object.refOutermostPackage();
        }

        
        public Collection refVerifyConstraints(boolean arg0) {

            return this.refVerifyConstraints(arg0);
        }
        
    }

}
