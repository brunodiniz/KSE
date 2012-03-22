package br.ucam.kuabaSubsystem.mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.jmi.model.AliasType;
import javax.jmi.model.Attribute;
import javax.jmi.model.Classifier;
import javax.jmi.model.EnumerationType;
import javax.jmi.model.ModelElement;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.MultiplicityType;
import javax.jmi.model.NameNotFoundException;
import javax.jmi.model.NameNotResolvedException;
import javax.jmi.model.Namespace;
import javax.jmi.model.Reference;
import javax.jmi.model.VisibilityKind;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefException;
import javax.jmi.reflect.RefFeatured;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import static org.easymock.EasyMock.*;
public class NiceMockCore implements MofPackage {
	
	private List<ModelElement> contents;
	private HashMap<String, ModelElement> mockRegistry;
	private List<ModelElement> modelElementContents;
	private List<ModelElement> associationContents;
	private List<ModelElement> classContents;
	private List<ModelElement> classSupertypes;
	private List<String> visibilityLabels;
	private AliasType mockAliasType;
	
	public NiceMockCore(){	
		
		this.associationContents = new ArrayList<ModelElement>();
		this.classContents = new ArrayList<ModelElement>();
		this.classSupertypes = new ArrayList<ModelElement>();
		this.visibilityLabels = new ArrayList<String>();
		this.contents = new ArrayList<ModelElement>();
		
		this.mockRegistry = new HashMap<String, ModelElement>();
		this.mockAliasType = createNiceMock(AliasType.class);
		
		/*
		 * Creating a ModelElement mock and your fake Attributes for test
		 * purpose and preparing the getContents call to return a list of
		 * Attributes.0 
		 */
		MofClass modelElement = createNiceMock(MofClass.class);
			//Creating name Attribute.
			Attribute name = createNiceMock(Attribute.class);
			expect(name.getName()).andReturn("name").anyTimes();
			expect(name.getType()).andReturn(this.mockAliasType).anyTimes();
			
			//Creating visibility Attribute.
			MultiplicityType visibilityMultiplicity = createNiceMock(MultiplicityType.class);
			Attribute visibility = createNiceMock(Attribute.class);
			expect(visibility.getName()).andReturn("visibility").anyTimes();	
			expect(visibility.getMultiplicity()).andReturn(visibilityMultiplicity);
			expect(visibilityMultiplicity.getUpper()).andReturn(2).anyTimes();
			EnumerationType visibilityKind = createNiceMock(EnumerationType.class);			
			String[] labels = new String[]{"vk_public", "vk_protected",
									 "vk_private", "vk_package"};
			expect(visibility.getType()).andReturn(visibilityKind).anyTimes();
		this.visibilityLabels = new ArrayList<String>(Arrays.asList(labels));
		ModelElement[] contents = new ModelElement[]{name, visibility}; 
		this.modelElementContents = new ArrayList
							<ModelElement>(Arrays.asList(contents));
		expect(modelElement.getName()).andReturn("ModelElement").anyTimes();
		expect(modelElement.getContents()).
							andReturn(this.modelElementContents).anyTimes();
		expect(visibilityKind.getLabels()).
							andReturn(this.visibilityLabels).anyTimes();
		expect(modelElement.getSupertypes()).andReturn(new ArrayList()).anyTimes();
		expect(modelElement.isAbstract()).andReturn(true).anyTimes();
		
		//creating UML Class Mock
		MofClass umlClass = createNiceMock(MofClass.class);
		expect(umlClass.isAbstract()).andReturn(false).anyTimes();			
			
			//creating isAcitve Attribute.
			MultiplicityType isActiveMultiplicity = createNiceMock(MultiplicityType.class);
			Attribute isActive = createNiceMock(Attribute.class);
			expect(isActive.getName()).andReturn("isActive").anyTimes();			
			expect(isActive.getMultiplicity()).andReturn(isActiveMultiplicity);
			expect(isActiveMultiplicity.getUpper()).andReturn(2).anyTimes();
			expect(isActive.getType()).andReturn(this.mockAliasType).anyTimes();
			this.classContents = new ArrayList<ModelElement>();
			
		//Setting the superclass: modelElement
		this.classSupertypes = new ArrayList<ModelElement>();
		this.classSupertypes.add(modelElement);		
		expect(umlClass.getSupertypes()).andReturn(this.classSupertypes).anyTimes();
		expect(umlClass.allSupertypes()).andReturn(this.classSupertypes).anyTimes();
		
		//setting the expected return for getContents of umlClass.
		this.classContents.add(isActive);
		expect(umlClass.getContents()).andReturn(this.classContents).
		anyTimes();
		expect(umlClass.getName()).andReturn("Class").anyTimes();
		
		/*
		 * Creating the UML Association Mock
		 */
		MofClass association = createNiceMock(MofClass.class);
		expect(association.isAbstract()).andReturn(false).anyTimes();
			//creating a mock Reference called connection.
			Reference connection = createNiceMock(Reference.class);
			expect(connection.getName()).andReturn("connection").anyTimes();
			this.associationContents = new ArrayList<ModelElement>();
			this.associationContents.add(connection);
		//Setting the expected return for getContents of the association
		expect(association.getContents()).andReturn(this.associationContents).anyTimes();
		//Setting the association superclass: modelElement
		expect(association.allSupertypes()).andReturn(this.classSupertypes).anyTimes();
		expect(association.getName()).andReturn("Association").anyTimes();
		
		this.mockRegistry.put("ModelElement", modelElement);
		this.mockRegistry.put("Class", umlClass);
		this.mockRegistry.put("Association", association);
		this.contents.add(modelElement);
		this.contents.add(umlClass);
		this.contents.add(association);
		
		replay(this.mockAliasType);
		replay(modelElement);
		replay(name);
		replay(visibility);
		replay(visibilityMultiplicity);
		replay(visibilityKind);
		replay(umlClass);
		replay(isActive);
		replay(isActiveMultiplicity);
		replay(association);
		replay(connection);				
		
	}
	@Override
	public List allSupertypes() {	
		return null;
	}

	@Override
	public List findElementsByTypeExtended(MofClass arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getSupertypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisibilityKind getVisibility() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAbstract() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ModelElement lookupElementExtended(String arg0)
			throws NameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAbstract(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLeaf(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRoot(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisibility(VisibilityKind arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public List findElementsByType(MofClass arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getContents() {
		
		return this.contents;
	}

	@Override
	public ModelElement lookupElement(String elementName) throws NameNotFoundException {
		ModelElement returnElement = this.mockRegistry.get(elementName);
		if(returnElement == null)
			throw new NameNotFoundException("element" + elementName + "does" +
					"not exists");
		return returnElement;
	}

	@Override
	public boolean nameIsValid(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ModelElement resolveQualifiedName(List arg0)
			throws NameNotResolvedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection findRequiredElements(Collection arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAnnotation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getConstraints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Namespace getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		
		return "Core";
	}

	@Override
	public List getQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getRequiredElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFrozen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequiredBecause(ModelElement arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVisible(ModelElement arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAnnotation(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContainer(Namespace arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setName(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public RefClass refClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refDelete() {
		// TODO Auto-generated method stub

	}

	@Override
	public RefFeatured refImmediateComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean refIsInstanceOf(RefObject arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RefFeatured refOutermostComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object refGetValue(RefObject arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object refGetValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object refInvokeOperation(RefObject arg0, List arg1)
			throws RefException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object refInvokeOperation(String arg0, List arg1)
			throws RefException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refSetValue(RefObject arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refSetValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public RefPackage refImmediatePackage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefObject refMetaObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String refMofId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefPackage refOutermostPackage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection refVerifyConstraints(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
