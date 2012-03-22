package br.ucam.kuabaSubsystem.kuabaModel;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.impl.DefaultIdea;
import br.ucam.kuabaSubsystem.kuabaModel.impl.DefaultKuabaElement;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.repositories.OwlApiKuabaRepository;
import br.ucam.kuabaSubsystem.util.Sequence;
import br.ucam.kuabaSubsystem.util.SequenceGenerator;
import br.ucam.kuabaSubsystem.util.SequenceGeneratorImpl;
import br.ucam.kuabaSubsystem.graph.util.KuabaGraphUtil;

import com.hp.hpl.jena.util.FileUtils;
import org.semanticweb.owlapi.model.OWLOntology;

public class DeepCopyTest extends AbstractKuabaTestCase{
	/**
	 * A factory for the kuaba subtree template model
	 */
	private KuabaModelFactory kuabaSubtreeTemplateFactory;
	
	/**
	 * A kuaba subtree template model
	 */
//	private JenaOWLModel kuabaSubtreeTemplateModel;
	
	private Idea templateGenreDomainIdea;
	private Idea templateCDDomainIdea;
	private Idea templateClassDesignIdea;	
	private Idea templateAttributeDesignIdea;
	
	private Question templateWhatElementsQuestion;
	private Question templateHowModelGenreQuestion;
	private Question templateHowModelCDQuestion;	
	private Question templateOwnerQuestion;
	private Map<ReasoningElement, Integer> stageMap;
	
	
	
	
	public void setUp() throws Exception {
		super.setUp();
//		File kuabaOntologyFolder = new File("kuabaOntology/");
//		LocalFolderRepository kuabaOntologyLocalRepository = new LocalFolderRepository(kuabaOntologyFolder, true);		
//		File kuabaSubtreeTemplateFile = new File("templates/formalModelsTemplate.xml");		
//		
//		Creating an empty JenaOwlModel.
//		 kuabaSubtreeTemplateModel = ProtegeOWL.createJenaOWLModel();
//	     
//		 Adding the local repository created above to this JenaOWLModel.
//		 kuabaSubtreeTemplateModel.getRepositoryManager().addProjectRepository(kuabaOntologyLocalRepository);
//		 
//		 Loading the model from the file "br.ucam.kuabaSubsystem.testBase.xml" that imports "KuabaOntology.owl".
//		 try {
//			kuabaSubtreeTemplateModel.load(new FileInputStream(kuabaSubtreeTemplateFile), "");
//		} catch (FileNotFoundException e) {
//
//			e.printStackTrace();
//			fail("Kuaba subtree template not found!");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			fail("Some weird thing happened!");
//		}
                
                KuabaRepository repository = gate.load("templates/formalModelsTemplate.xml");
		
		SequenceGenerator generator = new SequenceGeneratorImpl(new File("sequences/sequences.txt"));
		
		Sequence seq = null;
		if(generator.loadSequence("TestSequence") == null)
			seq = generator.createNewSequence("TestSequence", 0, 1);
		else
			seq = generator.loadSequence("TestSequence");
		seq.setCurrentVal(0);
		DefaultKuabaElement.setIdGenerator(seq);		
		this.kuabaSubtreeTemplateFactory = repository.getModelFactory();
		
		templateWhatElementsQuestion = this.kuabaSubtreeTemplateFactory.createQuestion("whatElements");
		templateWhatElementsQuestion.setHasText("What Elements?");
		
		templateGenreDomainIdea = this.kuabaSubtreeTemplateFactory.createIdea("genre");
		templateGenreDomainIdea.setHasText("Genre");
		
		templateCDDomainIdea = this.kuabaSubtreeTemplateFactory.createIdea("cd");
		templateCDDomainIdea.setHasText("CD");
		
		templateHowModelGenreQuestion = this.kuabaSubtreeTemplateFactory.createQuestion("howModelGenre");
		templateHowModelGenreQuestion.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"genre?");
		
		templateHowModelCDQuestion = this.kuabaSubtreeTemplateFactory.createQuestion("howModelCd");
		templateHowModelCDQuestion.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"CD?");
		
		templateClassDesignIdea = this.kuabaSubtreeTemplateFactory.createIdea("umlClass");
		templateClassDesignIdea.setHasText("Class");
		
		templateAttributeDesignIdea = this.kuabaSubtreeTemplateFactory.createIdea("attribute");
		templateAttributeDesignIdea.setHasText("Attribute");
		
		templateOwnerQuestion = this.kuabaSubtreeTemplateFactory.createQuestion("owner");
		templateOwnerQuestion.setHasText("Owner?");
                
		stageMap = KuabaGraphUtil.getReasoningElementStageMap(this.templateWhatElementsQuestion);
				
	}
	
	/**
	 * tests the {@link Question#deepCopy(br.ucam.kuabaSubsystem.repositories.KuabaRepository)}
	 * 
	 * Tests if the deep copy procedure stops at the element with canCopy attribute as false.
	 *  
	 * In the case that the designer changes a name of an design element, 
	 * a new domain idea takes place with the same relationships that the old
	 * domain idea had. Thus, we can clone the subtree of the old domain idea 
	 * preserving the decisions made and Its structure.
	 * 
	 * The graph structure used in this test is shown below: 
	 * 
	 *      what Elements?
	 *      /           \ 
	 *     /             \   
	 *   genre            CD	    
	 *     |               |
	 *     |               |   
	 *  How Model Genre?  How Model CD?
	 *     |                |
	 *     |                | 
	 * Attribute         Class
	 *     \               /
	 *      \             /
	 *       \           /
	 *        \         /
	 *         \       /  
	 *           Owner?  
	 */	
	public void testIfCanCopyIsRespected(){		
            
		templateWhatElementsQuestion.addIsAddressedBy(templateGenreDomainIdea);
		templateWhatElementsQuestion.addIsAddressedBy(templateCDDomainIdea);
		
		templateCDDomainIdea.addSuggests(templateHowModelCDQuestion);
		templateGenreDomainIdea.addSuggests(templateHowModelGenreQuestion);
		
		templateHowModelCDQuestion.addIsAddressedBy(templateClassDesignIdea);		
		templateHowModelGenreQuestion.addIsAddressedBy(templateAttributeDesignIdea);
		
		templateAttributeDesignIdea.addSuggests(templateOwnerQuestion);
		
		templateOwnerQuestion.addIsAddressedBy(templateClassDesignIdea);
		
		templateClassDesignIdea.setCanCopy(false);  
		templateWhatElementsQuestion.setCanCopy(false);
		templateGenreDomainIdea.setCanCopy(false);
                
                stageMap = KuabaGraphUtil.getReasoningElementStageMap(this.templateWhatElementsQuestion);
                
		Question howModelGenreClone = templateHowModelGenreQuestion.deepCopy(stageMap, this.repo);
                
//                System.out.println(((OWLOntology)howModelGenreClone.getRepository().getModel()).getOntologyID().getOntologyIRI());
//                System.out.println(((OWLOntology)templateHowModelGenreQuestion.getRepository().getModel()).getOntologyID().getOntologyIRI());
                
		assertNotNull(howModelGenreClone);		
		howModelGenreClone.addIsSuggestedBy(templateGenreDomainIdea);
		Idea genreDomainIdea =(Idea) howModelGenreClone.listIsSuggestedBy().next();

		assertEquals(this.templateGenreDomainIdea, genreDomainIdea);
		assertEquals(this.templateWhatElementsQuestion, genreDomainIdea.listAddress().next());
		
		Iterator<Idea> howModelGenreCloneAdressedIdeaIterator =
			howModelGenreClone.getIsAddressedBy().iterator();
		
		Idea attributeDesignIdeaClone = howModelGenreCloneAdressedIdeaIterator.next();
		assertNotNull(attributeDesignIdeaClone);
                
//                System.out.println("atrib? "+ attributeDesignIdeaClone.getHasText());
                
//                System.out.println(((OWLOntology)attributeDesignIdeaClone.getRepository().getModel()).getOntologyID().getOntologyIRI());
//                System.out.println(((OWLOntology)templateAttributeDesignIdea.getRepository().getModel()).getOntologyID().getOntologyIRI());
		
		Iterator<Question> suggestedAttributeQuestionIterator = 
			attributeDesignIdeaClone.listSuggests();		
		assert suggestedAttributeQuestionIterator.hasNext();
		Question ownerCloneQuestion = suggestedAttributeQuestionIterator.next();
		
//                System.out.println("own? "+ownerCloneQuestion.getHasText());
		
		Iterator<Idea> addressedOwnerIdeaIterator = 
			ownerCloneQuestion.listIsAddressedBy();		
		assert addressedOwnerIdeaIterator.hasNext();		
		Idea classCloneIdea = addressedOwnerIdeaIterator.next();
                
//                System.out.println("class? "+classCloneIdea.getHasText());
                
                
//                System.out.println(((OWLOntology)classCloneIdea.getRepository().getModel()).getOntologyID().getOntologyIRI());
//                System.out.println(((OWLOntology)templateClassDesignIdea.getRepository().getModel()).getOntologyID().getOntologyIRI());
		
		//testing if the deep copy procedure stopped at the class design idea
		
                Question HowModelCDQuestion = classCloneIdea.listAddress().next();
                if (!HowModelCDQuestion.equals(this.templateHowModelCDQuestion))
                    for (Question q : classCloneIdea.getAddress()) {
//                        System.out.println(q.getRepository().getUrl()+"#"+q.getId());
                        if (q.equals(this.templateHowModelCDQuestion)) HowModelCDQuestion = q;
                    }
                
//                System.out.println(HowModelCDQuestion.getRepository().getUrl()+"#"+HowModelCDQuestion.getId());
//                System.out.println(this.templateHowModelCDQuestion.getRepository().getUrl()+"#"+this.templateHowModelCDQuestion.getId());
                
		assertEquals(this.templateHowModelCDQuestion, HowModelCDQuestion);
//                assertTrue(classCloneIdea.getAddress().contains(this.templateHowModelCDQuestion));
		
		Idea cdDomainIdea = (Idea) HowModelCDQuestion.listIsSuggestedBy().next();		
		assertEquals(this.templateCDDomainIdea, cdDomainIdea);
		
		assertEquals(this.templateWhatElementsQuestion, cdDomainIdea.listAddress().next());
		
	}
	
	/**
	 * tests the {@link Question#deepCopy(br.ucam.kuabaSubsystem.repositories.KuabaRepository)}
	 */
	public void testDeepCopy(){		
		
		templateWhatElementsQuestion.addIsAddressedBy(templateGenreDomainIdea);
		templateWhatElementsQuestion.addIsAddressedBy(templateCDDomainIdea);
		
		templateCDDomainIdea.addSuggests(templateHowModelCDQuestion);
		templateGenreDomainIdea.addSuggests(templateHowModelGenreQuestion);
		
		templateHowModelCDQuestion.addIsAddressedBy(templateClassDesignIdea);		
		templateHowModelGenreQuestion.addIsAddressedBy(templateAttributeDesignIdea);
		
		templateAttributeDesignIdea.addSuggests(templateOwnerQuestion);
		
		templateOwnerQuestion.addIsAddressedBy(templateClassDesignIdea);
                
                stageMap = KuabaGraphUtil.getReasoningElementStageMap(this.templateWhatElementsQuestion);
		
		Question howModelGenreClone = templateHowModelGenreQuestion.deepCopy(
				stageMap, this.repo);
		howModelGenreClone.addIsSuggestedBy(templateGenreDomainIdea);
		assertNotNull(howModelGenreClone);
		assertEquals(Question.DOMAIN_QUESTION_TEXT_PREFIX+"genre?", howModelGenreClone.getHasText());
//		assertEquals("_1", howModelGenreClone.getId());
		

		Idea genreDomainIdea =(Idea)howModelGenreClone.listIsSuggestedBy().next();
		System.out.println(genreDomainIdea.getHasText());
		assertEquals("Genre", genreDomainIdea.getHasText());
//		assertEquals("_8", genreDomainIdea.getId());
		
		
		//testing if the subtree was really cloned
		Iterator<Idea> howModelGenreCloneAdressedIdeaIterator = 
			howModelGenreClone.getIsAddressedBy().iterator();
		
		Idea attributeDesignIdeaClone = howModelGenreCloneAdressedIdeaIterator.next();
		assertNotNull(attributeDesignIdeaClone);
		assertEquals("Attribute", attributeDesignIdeaClone.getHasText());		
//		assertEquals("_2", attributeDesignIdeaClone.getId());		
		
		Iterator<Question> suggestedAttributeQuestionIterator = 
			attributeDesignIdeaClone.listSuggests();		
		assert suggestedAttributeQuestionIterator.hasNext();
		Question ownerCloneQuestion = suggestedAttributeQuestionIterator.next();
		assertNotNull(ownerCloneQuestion);
		assertEquals("Owner?", ownerCloneQuestion.getHasText());		
//		assertEquals("_3", ownerCloneQuestion.getId());
		
		
		gate.save(this.repo);
		
	}
//	/**
//	 * tests the {@link Question#deepCopy(br.ucam.kuabaSubsystem.repositories.KuabaRepository)}
//	 */
//	public void testDeepCopy2(){
//		Question howModel = this.kuabaSubtreeTemplateFactory.createQuestion("howModelQuestion");
//		Question howModelCopy = howModel.deepCopy(new OwlApiKuabaRepository(this.model));
//		assertEquals("_1", howModelCopy.getId());
//	}	
}
