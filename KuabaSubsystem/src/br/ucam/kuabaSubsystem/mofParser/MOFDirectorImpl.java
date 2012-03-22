package br.ucam.kuabaSubsystem.mofParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jmi.model.Attribute;
import javax.jmi.model.EnumerationType;
import javax.jmi.model.ModelElement;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.MultiplicityType;
import javax.jmi.model.Reference;
import javax.jmi.model.StructuralFeature;
import javax.jmi.xmi.MalformedXMIException;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.util.MofHelper;


@SuppressWarnings("unchecked")
public class MOFDirectorImpl implements Director {
	
	private ParserBuilder builder;	
	private Iterator mofIterator;
	private List<MofPackage> mofPackages;	
	private BaseClassRecognizer strategy;
	private MofClass baseClass;
	
	public MOFDirectorImpl(BaseClassRecognizer strategy,
			List mofPackages) {
		
		this.mofPackages = mofPackages;
		this.strategy = strategy;
		this.baseClass = strategy.findBaseClass(mofPackages);
	}
	
	@Override
	public void construct() {
		
		this.builder.createRootQuestion("What Elements");		
		this.builder.createIdea("Domain Idea");
		this.builder.push();
		this.builder.createQuestion("How Model", Question.ORTYPE);
		this.builder.push();
		
		int totalEnumerationValues = 0;		
		int currentEnumerationValues = 0;		 
		
		while (mofIterator.hasNext()) {
			
			Object modelElement = (Object) mofIterator.next();
			
			if (modelElement instanceof MofClass) {				
							
				MofClass metaModelClass = (MofClass) modelElement;
			
				/*
				 * Only concrete classes derived from "this.baseClass" 
				 * interest here.
				 */
				List<ModelElement> supertypes = metaModelClass.allSupertypes();
				if((metaModelClass.isAbstract()) ||
						(!supertypes.contains(this.baseClass))){					
					continue;					
				}				
				/*
				 * If the builder has an Idea in the top of your stack
				 * it must be popped to put the "How Model" question on
				 * the top.
				 */
				if (this.builder.top() instanceof Idea) {
					this.builder.pop();					
				}	
				this.builder.createIdea(metaModelClass.getName());				
				this.builder.push();				
			}
			else if (modelElement instanceof Attribute) {
				
				Attribute metaModelAttribute = (Attribute) modelElement;
			
				if(metaModelAttribute.getName().equals("name"))
					continue;
				String questionType = this.selectQuestionType(metaModelAttribute);
				this.builder.createQuestion(metaModelAttribute.getName(),
						questionType);
				
				if (metaModelAttribute.getType() instanceof EnumerationType) {
					EnumerationType enumType = (EnumerationType) metaModelAttribute.getType();
					totalEnumerationValues = enumType.getLabels().size();
					this.builder.push();
				}				
			}
			else if (modelElement instanceof Reference) {
				Reference metaReference = (Reference) modelElement;
			
				this.builder.createQuestion(metaReference.getName(), 
						Question.ORTYPE);				
			} 
			else if (modelElement instanceof String) {
				currentEnumerationValues++;
				String defaultValue = (String) modelElement;
				this.builder.createIdea(defaultValue);
				
				if(currentEnumerationValues == totalEnumerationValues){
					this.builder.pop();
				}
			}			
		}		
	}

	private int getMembersCount(Iterator iterator) {
		int count = 0;
		
		while (iterator.hasNext()) {
			Object member = (Object) iterator.next();
			if ((member instanceof Attribute) || (member instanceof Reference)) {
				count++;				
			}			
		}		
		return count;
	}

	
	public void init(ParserBuilder builder, Iterator mofIterator) {
		this.builder = builder;
		this.mofIterator = mofIterator;
		
	}
	
	public String selectQuestionType(StructuralFeature feature){
		MultiplicityType m = feature.getMultiplicity();
		if((m.getUpper() < 0) || (m.getUpper() > 1 ))
			return 	Question.ORTYPE;
		else
			return 	Question.XORTYPE;
		
		
	}

	protected Iterator createIterator(){
		return null;
	}

	@Override
	public void init(ParserBuilder builder, 
			BaseClassRecognizer baseClassFinder) {
		this.builder = builder;
		this.baseClass = baseClassFinder.findBaseClass(new ArrayList<MofClass>());
		for (MofPackage pack : this.mofPackages) {
			if(pack.getName().equals("Core"))
				this.mofIterator = MofHelper.classesOfPackage(pack).listIterator();
		}
		
	}

	@Override
	public void init(ParserBuilder builder,
			BaseClassRecognizer baseClassFinder, URL resource)
			throws IOException, MalformedXMIException {
		// TODO Auto-generated method stub
		
	}
	
}
