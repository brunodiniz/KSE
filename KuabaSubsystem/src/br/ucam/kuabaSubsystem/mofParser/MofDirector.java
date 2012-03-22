package br.ucam.kuabaSubsystem.mofParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.jmi.model.Attribute;
import javax.jmi.model.EnumerationType;
import javax.jmi.model.ModelElement;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.MultiplicityType;
import javax.jmi.model.Reference;
import javax.jmi.model.StructuralFeature;
import javax.jmi.xmi.MalformedXMIException;

import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.util.MofHelper;
@SuppressWarnings("unchecked")
public abstract class MofDirector implements Director {
	
	protected MetamodelReader reader;
	protected ParserBuilder builder;	
	protected List<MofClass> metamodelClasses;
	protected Iterator mofIterator;
	protected List<MofPackage> mofPackages;	
	protected BaseClassRecognizer strategy;
	protected MofClass baseClass;	
		
	public MofDirector() {
		super();
		
	}

	public MofDirector(MofPackage pack) {
		super();
		this.mofPackages = new ArrayList<MofPackage>();
		this.mofPackages.add(pack);		
	}

	protected MetamodelReader createMetamodelReader(){
		return new MofReader();
	}

	protected abstract List<MofClass> getClassList(List<MofPackage> packages);	
	
	@Override
	public void construct() {		
		this.builder.createRootQuestion("How Model?");		
		while (mofIterator.hasNext()) {			
			MofClass mofClass = (MofClass) mofIterator.next();
			this.builder.createIdea(mofClass.getName());
			this.builder.push();
			
			for (Reference ref : MofHelper.referencesOfClass(mofClass, true)) {
				if(!(ref.getContainer().getContainer().equals(ref.getType().getContainer())))					
					if(ref.getType().isAbstract()){
						List<MofClass> l = MofHelper.getConcreteSubClasses(ref.getType());
						for (MofClass c : l) 
							insertElement(c);
					}
					else
						insertElement(ref.getType());					
				this.builder.createQuestion(ref.getName(), 
						selectQuestionType(ref));				
			}
			
			for (Attribute attr : MofHelper.attributesOfClass(mofClass, true)) {
				if(!attr.getName().equals("name")){
					String questionType = this.selectQuestionType(attr);
					this.builder.createQuestion(attr.getName(),
							questionType);
					if(MofHelper.isEnumerationType(attr)){
						this.builder.push();
						List<String> labels = ((EnumerationType)attr.getType()).getLabels();
						for (String label : labels)
							this.builder.createIdea(label);						
						this.builder.pop();
					}
				}
			}
			this.builder.pop();
		}			
	}
	
	
	
	private void insertElement(ModelElement mofClass){
		
		if(((ListIterator)this.mofIterator).hasNext()){
			this.mofIterator.next();
		    ((ListIterator)this.mofIterator).add(mofClass);
		    ((ListIterator)this.mofIterator).previous();
		}else{
			 Object previous = ((ListIterator)this.mofIterator).previous();
			((ListIterator)this.mofIterator).set(mofClass);
			((ListIterator)this.mofIterator).add(previous);			
		}
	}
	
	public String selectQuestionType(StructuralFeature feature){
		MultiplicityType m = feature.getMultiplicity();
		if((m.getUpper() < 0) || (m.getUpper() > 1 ))
			return 	Question.ORTYPE;
		else
			return 	Question.XORTYPE;		
	}

	public void init(ParserBuilder builder,	BaseClassRecognizer baseClassFinder) {
		
		this.metamodelClasses = this.getClassList(this.mofPackages);		
		this.baseClass = baseClassFinder.findBaseClass(this.metamodelClasses);
		this.mofIterator = MofHelper.selectConcreteClasses(this.metamodelClasses).listIterator();
		this.builder = builder;
		
	}

	@Override
	public void init(ParserBuilder builder,
			BaseClassRecognizer baseClassFinder, URL resource) throws IOException, MalformedXMIException {
		this.reader = createMetamodelReader();
                
	    this.mofPackages = new ArrayList<MofPackage>(reader.read(resource.toString()));
	    this.init(builder, baseClassFinder);
		
	}
}
