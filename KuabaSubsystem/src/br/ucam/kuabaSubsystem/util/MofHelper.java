package br.ucam.kuabaSubsystem.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.jmi.model.Association;
import javax.jmi.model.AssociationEnd;
import javax.jmi.model.Attribute;
import javax.jmi.model.Classifier;
import javax.jmi.model.Constant;
import javax.jmi.model.DataType;
import javax.jmi.model.EnumerationType;
import javax.jmi.model.GeneralizableElement;
import javax.jmi.model.Import;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofException;
import javax.jmi.model.MofPackage;
import javax.jmi.model.Namespace;
import javax.jmi.model.Operation;
import javax.jmi.model.Parameter;
import javax.jmi.model.PrimitiveType;
import javax.jmi.model.Reference;
import javax.jmi.model.ScopeKind;
import javax.jmi.model.ScopeKindEnum;
import javax.jmi.model.StructuralFeature;
import javax.jmi.model.StructureField;
import javax.jmi.model.StructureType;
import javax.jmi.model.TypedElement;
import javax.jmi.reflect.RefFeatured;
import javax.jmi.reflect.RefObject;



@SuppressWarnings("unchecked")
public class MofHelper {
	public static HashMap<MofClass, List<MofClass>> subClassesMap = new HashMap<MofClass, List<MofClass>>();
	
	
	public static List<MofClass> getSubClasses(Classifier _class){
		if(subClassesMap.size() == 0){
			searchAllSubClasses(((MofPackage) _class.getContainer()).refClass().refAllOfType().iterator());
		}
		if(subClassesMap.containsKey(_class)){
			List<MofClass> subClasses = subClassesMap.get(_class);
			return subClasses;
		}		
		return new ArrayList<MofClass>();
	}	
	public static String discoverRelationship(RefObject source,
			RefObject target){
		List<Reference> referenceList = referencesOfClass(
				(MofClass)source.refMetaObject(), true);
		for (Reference reference : referenceList)
			if(source.refGetValue(reference) instanceof Collection){
				Collection<RefObject> referenceValues = 
					(Collection<RefObject>) source.refGetValue(reference);
				if(referenceValues.contains(target))
					return reference.getName();
			}
			else
				if((source.refGetValue(reference) != null)&& 
						(source.refGetValue(reference).equals(target)))
					return reference.getName();
		
		return null;
	}
	
	public static String getQualifiedName(RefObject object){		
		RefFeatured outermost = object.refOutermostComposite();
		RefObject current = (RefObject)object.refImmediateComposite();
		String qualifiedName = current.refGetValue("name") + ":";
		while(current != outermost){
			current = (RefObject)current.refImmediateComposite();
			qualifiedName += current.refGetValue("name") + ":";
		}
		qualifiedName += object.refGetValue("name");
		return qualifiedName;
	}
	public static List<MofClass> getConcreteSubClasses(Classifier _class){
		List<MofClass> subclasses = getSubClasses(_class);
		List<MofClass> concreteSubclasses = new ArrayList<MofClass>();
		Iterator<MofClass> it = subclasses.iterator();
		while (it.hasNext()) {
			MofClass mofClass = (MofClass) it.next();
			if (!mofClass.isAbstract()) {
				concreteSubclasses.add(mofClass);
				
			}
		}
		return concreteSubclasses;
	}
	
	public static void searchAllSubClasses(Iterator<MofPackage> packs){
		while (packs.hasNext()) {
			MofPackage mofPackage = (MofPackage) packs.next();
			MofClass[] classes = classesOfPackage(mofPackage).toArray(new MofClass[0]);
			for (MofClass mofClass : classes) {
				List<Classifier> superClasses = mofClass.allSupertypes();
				Iterator<Classifier> itSup = superClasses.iterator();
				while (itSup.hasNext()) {
					Classifier superClass = (Classifier) itSup.next();
					addInMap((MofClass)superClass, mofClass);					
				}
			}			
		}
	}
	
	private static void addInMap(MofClass superClass, MofClass subClass){
		if(subClassesMap.containsKey(superClass)){
			List<MofClass> subClasses = subClassesMap.get(superClass);
			if(!subClasses.contains(subClass));
				subClasses.add(subClass);
		}
		else {
			List<MofClass> subclasses = new ArrayList<MofClass>();
			subclasses.add(subClass);
			subClassesMap.put(superClass, subclasses);
		}
			
	}
	
	 public static MofPackage[] packagesOfPackage(MofPackage p){
		 List l = filterContentsByClass(p, MofPackage.class ,false);
		 return (MofPackage[]) l.toArray(new MofPackage[0]); 
	 }
	
	 public static List<MofClass> classesOfPackage(MofPackage p) {
         List l = filterContentsByClass(p, MofClass.class ,false);
         return l; 
     }
		      
	 public static Association[] associationsOfPackage(MofPackage p) {
         List l = filterContentsByClass(p, Association.class ,false);
         return (Association[]) l.toArray(new Association[0]); 
     }		      
     
     public static Import[] importsOfPackage(MofPackage p) {
         List l = filterContentsByClass(p, Import.class ,false);
         return (Import[]) l.toArray(new Import[0]); 
     }
         
     public static DataType[] datatypesOf(Namespace ns, boolean considerSupertypes) {
         List l = filterContentsByClass(ns, DataType.class ,considerSupertypes);
         return (DataType[]) l.toArray(new DataType[0]); 
     }
     
     public static AssociationEnd[] associationEndsOfAssociation(Association a) {
         List l = filterContentsByClass(a, AssociationEnd.class ,false);
         return (AssociationEnd[]) l.toArray(new AssociationEnd[0]); 
     }
 
     
     public static Constant[] constantsOfClass(MofClass c) {
         List l = filterContentsByClass(c, Constant.class ,false);
         return (Constant[]) l.toArray(new Constant[0]); 
     }
     
     public static Operation[] operationsOfClass(MofClass cl, ScopeKind sk, boolean considerSupertypes) { 
         List l = filterContentsByClass(cl, Operation.class ,considerSupertypes);
         List l2 = new Vector();
         Iterator it = l.iterator();
         while(it.hasNext()) {
           Operation op = (Operation)it.next(); 
           if(op.getScope().equals(sk)) {
              l2.add(op);
           }
         }
         return (Operation[]) l2.toArray(new Operation[0]); 
     }
     
     public static List<MofClass> selectConcreteClasses(List<MofClass> classList){
    	 List<MofClass> concreteClassList = new ArrayList<MofClass>();
    	 for (MofClass mofClass : classList) {
			if (!mofClass.isAbstract()) {
				concreteClassList.add(mofClass);
			}
		}
    	 return concreteClassList;
     }
     public static List<StructuralFeature> structuralFeaturesOfClass(
    		 MofClass c, boolean considerSupertypes){
    	 List<StructuralFeature> structuralFeatureList = new ArrayList<StructuralFeature>();
    	 structuralFeatureList.addAll(
    			 MofHelper.attributesOfClass(c, considerSupertypes));
    	 structuralFeatureList.addAll(
    			 MofHelper.referencesOfClass(c, considerSupertypes));
    	 return structuralFeatureList;
     }
     public static List<Reference> referencesOfClass(MofClass c, boolean considerSupertypes) {
         List l = filterContentsByClass(c, Reference.class , considerSupertypes);
         return l; 
     }
         
     public static List<Attribute> attributesOfClass(
             MofClass cl, boolean considerSupertypes) {
                 
         List l = filterContentsByClass(cl, Attribute.class ,considerSupertypes);
         List<Attribute> l2 = new ArrayList<Attribute>(l);
         
         return l2; 
     }
     
     public static List<Attribute> attributesOfClass(
             MofClass cl, ScopeKind sk, boolean considerSupertypes) {                
         
         List<Attribute> l = MofHelper.attributesOfClass(cl, considerSupertypes);
         List<Attribute> scopedAttributeList = new ArrayList<Attribute>();
         for (Attribute attribute : l)
			if (attribute.equals(sk)) 
				scopedAttributeList.add(attribute);
		
         return scopedAttributeList; 
     }
     
     public static StructureField[] structureFieldsOf(StructureType t) {
        List l = filterContentsByClass(t, StructureField.class ,false);
        return (StructureField[]) l.toArray(new StructureField[0]); 
    }     
     
     public static Parameter[] parametersOf(Operation op) {
         List l = filterContentsByClass(op, Parameter.class ,false);
         return (Parameter[]) l.toArray(new Parameter[0]); 
     }
     
     public static MofException[] exceptionsOf(Operation op) {
       List l = filterContentsByClass(op, MofException.class ,false);
       return (MofException[]) l.toArray(new MofException[0]);
     }
     
     
     
     public static List filterContentsByClass
             (Namespace ns, Class  c, boolean considerSupertypes) {
       List r = new ArrayList();
       if((ns instanceof GeneralizableElement) && considerSupertypes) {
            Iterator it = ((GeneralizableElement)ns).getSupertypes().iterator();
            while(it.hasNext()) {
              Namespace sup = (Namespace) it.next();
              Iterator it2 = filterContentsByClass(sup,c,true).iterator();
              while(it2.hasNext()) {
                 Object  o = it2.next();
                 if(!r.contains(o)) r.add(o);
              }
            }
       }
       Iterator it = ns.getContents().iterator();
       while(it.hasNext()) {
           Object  o = it.next();
           if(c.isInstance(o)) {
              if(!r.contains(o)) r.add(o);
           }
       }
       return r;
     }
     
     public static boolean isEnumerationType(TypedElement elem){
    	 if (elem.getType() instanceof EnumerationType)
    		 return true;
    	 return false;
     }
     public static boolean isSinglePrimitive(TypedElement elem) {
       if(!(elem.getType() instanceof PrimitiveType)) return false;
       if(elem instanceof StructuralFeature) {
         StructuralFeature f = (StructuralFeature)elem;
         if(f.getMultiplicity().getLower()!=1) return false;
         if(f.getMultiplicity().getUpper()!=1) return false; 
         return true;
       }
       if(elem instanceof Parameter) {
         Parameter p = (Parameter)elem;
         if(p.getMultiplicity().getLower()!=1) return false;
         if(p.getMultiplicity().getUpper()!=1) return false; 
         return true;
       }
       return true;
     }
     
     public static String getExposedEndName(Reference ref){
    	 AssociationEnd exposedEnd = ref.getExposedEnd();    	 
    	 return exposedEnd.getName();
     }
	public static Reference getReference(String name, MofClass _class){
		List<Reference> referenceList = referencesOfClass(_class, true);
		for (Reference reference : referenceList) {
			if(reference.getName().equals(name))
				return reference;
		}
		return null;
	}
		  
	
}
