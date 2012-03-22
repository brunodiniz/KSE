package br.ucam.kuabaSubsystem.kuabaFacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



import br.ucam.kuabaSubsystem.abstractTestCase.FunctionalTestCase;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaExceptions.NoPersonLoggedExpetion;
import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.ExpectedDuration;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Method;
import br.ucam.kuabaSubsystem.kuabaModel.Person;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.Role;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import java.util.List;




public class kuabaFacadeTest extends FunctionalTestCase{
	//private KuabaFacade kuabaFacade;
	private Person manoel;
	//private KuabaRepository modelRepository;
	//private KuabamodelRepository templatesmodelRepository;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
//		this.modelRepository = new OwlmodelRepository(this.factory.getOwlModel());
//		this.modelRepository.setUrl(this.testKuabaKnowlegeBase.toString());
//		
//		PropertiesConfigurator manager = new PropertiesConfigurator();
//		manager.addProperty(KuabaSubsystem.modelRepository_PATH, this.testKuabaKnowlegeBase.toString());
//		manager.addProperty(KuabaSubsystem.TEMPLATES_modelRepository_PATH, TEST_BASE_PACKAGE + "templatesTestBase.xml");
//		
//		
		this.manoel = this.repo.getPerson("manoel");		
//		
//		
//		KuabaEventPump mockPump = EasyMock.createNiceMock(KuabaEventPump.class);
//		EasyMock.replay(mockPump);
//		KuabaSubsystem.init(manager, mockPump );
//		this.kuabaFacade = new KuabaFacade();
//		this.kuabaFacade.setKuabaFactory(this.factory);
//		this.templatesmodelRepository = KuabaSubsystem.gateway.load(TEST_BASE_PACKAGE + "templatesTestBase.xml", "", "");
		
	}

	
	public void test_representDesignData() throws NoPersonLoggedExpetion{
		
		this.facade.setLoggedPerson(this.manoel);
		
		Method up = this.repo.getMethod("UP");
		Activity activity = this.repo.getActivity("design_domain_model");
		Set<String> roles = new HashSet<String>();
		roles.add("developer");
		
		this.facade.representDesignData(up, activity, 3, ExpectedDuration.WEEK, roles);
		
		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		//Testing if the design_domain_model Activity was attached to UP
		//Method.
		up = this.repo.getMethod("UP");
		assertTrue(up.hasAggregates());
		Iterator<Activity> it = up.listAggregates();
		assertEquals("design_domain_model", it.next().getId());
		
		Person manoel = this.repo.getPerson("manoel");
		
		//Testing if the design_domain_model Activity was attached to manoel
		//Person.
		assertTrue(manoel.hasExecutes());		 
		Activity designDomainModel = manoel.listExecutes().next();  
		assertEquals("design_domain_model", designDomainModel.getId());
		
		//Testing if manoel Person was attached to the design_domain_model
		//Activity.
		assertTrue(designDomainModel.hasIsExecutedBy());
		Iterator<Person> isExecutedBy = designDomainModel.listIsExecutedBy();   
		assertEquals("manoel", isExecutedBy.next().getId());
		
		//Testing if the developer Role was attached to manoel Person
		assertTrue(manoel.hasPerforms());
		Role developer = manoel.listPerforms().next();
		assertNotNull(developer);
		assertEquals("developer", developer.getId());
		
		//Testing if the manoel Person was attached to developer Role.  
		assertTrue(designDomainModel.hasRequires());
		Iterator<Role> rolesIt = designDomainModel.listRequires();
		assertEquals("developer", rolesIt.next().getId());
		
		//Testing if the ExpectedDuration object was created correctly and attached
		//to design_domain_model Activity.
		assertTrue(designDomainModel.hasHasExpectedDuration());
		ExpectedDuration threeWeeks =  designDomainModel.getHasExpectedDuration();
		assertEquals("3_week", threeWeeks.getId());
		assertEquals(3, threeWeeks.getHasAmount());
		assertEquals(ExpectedDuration.WEEK, threeWeeks.listHasUnitTime().next());
	}
	
	public void test_representDesignData_NoPersonLoggedEexception(){
		
		Method up = this.repo.getMethod("UP");
		Activity activity = this.repo.getActivity("design_domain_model");
		Set<String> roles = new HashSet<String>();
		roles.add("developer");		
		
		try{
			this.facade.representDesignData(up, activity, 3, ExpectedDuration.WEEK, roles);
			fail();
		}catch (NoPersonLoggedExpetion e) {
			assertEquals("No person logged in this session, please login!", e.getMessage());
		}
	}
	
	public void testDomainIdeaAdded(){
		Idea domainIdea = KuabaSubsystem.facade.domainIdeaAdded("Person", "Class", "_0123");
		
                
//                System.out.println("MAIN = "+domainIdea.getId()+" - "+domainIdea.getHasText()+"\n");
//                
//                System.out.println(domainIdea.getIsInvolved());
                
//                for(Idea i : this.repo.getAllIdeas()) System.out.println(i.getId());
                
                
//		assertNotNull(this.repo.getActivity(domainIdea.getId()));
		Collection<Question> questions = domainIdea.getAddress();
		assertNotNull(questions);
		assertEquals(1, questions.size());
		assertEquals("What Elements?", questions.iterator().next().getHasText());
		
		Iterator<Question> suggestedQuestions = domainIdea.getSuggests().iterator();
		assertTrue(suggestedQuestions.hasNext());
		
		Question howModelPerson = suggestedQuestions.next();
		assertEquals(Question.DOMAIN_QUESTION_TEXT_PREFIX+"Person?", howModelPerson.getHasText());
		
//		Iterator<Idea> howModelPersonAddressedIdeas = howModelPerson.getIsAddressedBy().iterator();
                List<Idea> lista = KuabaHelper.getAcceptedAddressedIdeas(howModelPerson);
                Iterator<Idea> howModelPersonAddressedIdeas = lista.iterator();
//                System.out.println("TAM LISTA = "+lista.size());
//                for(Idea i : lista) System.out.println(i.getHasText());
                
		Idea umlClass = howModelPersonAddressedIdeas.next();
		assertNotNull(umlClass);
		assertEquals("Class", umlClass.getHasText());
		
//		Idea attribute = howModelPersonAddressedIdeas.next();
//		assertEquals("Attribute", attribute.getHasText());
		
		Argument testArgument = this.factory.createArgument("t");
		testArgument.setHasText("testArgument");		
		Idea otherIdea = this.factory.createIdea("_test");
		otherIdea.setHasText("testIdea");
		otherIdea.addHasArgument(testArgument);
		testArgument.addInFavorOf(otherIdea);
		howModelPerson.addIsAddressedBy(otherIdea);		
		Idea otherDomainIdea = KuabaSubsystem.facade.domainIdeaAdded("People", "class", "_0123");
		Idea testIdea = 
			(Idea)KuabaHelper.getReasoningElementInTree(
					otherDomainIdea, "testIdea"); 
		assertTrue(testIdea == null);
		
		assertEquals(2, this.modelRepository.getAllArguments().size());		
	}
	
	
	public void test_saveSession() throws NoPersonLoggedExpetion{			
			KuabaSubsystem.facade.setLoggedPerson(this.manoel);
			
			Method up = this.repo.getMethod("UP");
			Activity activity = this.repo.getActivity("design_domain_model");
			Set<String> roles = new HashSet<String>();
			roles.add("developer");
			
			KuabaSubsystem.facade.representDesignData(up, activity, 3, ExpectedDuration.WEEK, roles);		
			assertTrue(KuabaSubsystem.facade.saveSession(this.modelRepository,null));
			
			//Testing if the design_domain_model Activity was attached to UP
			//Method.
			up = this.repo.getMethod("UP");
			assertTrue(up.hasAggregates());
			Iterator<Activity> it = up.listAggregates();
			assertEquals("design_domain_model", it.next().getId());
			
			Person manoel = this.repo.getPerson("manoel");
			
			//Testing if the design_domain_model Activity was attached to manoel
			//Person.
			assertTrue(manoel.hasExecutes());		 
			Activity designDomainModel = manoel.listExecutes().next();  
			assertEquals("design_domain_model", designDomainModel.getId());
                        assertEquals(activity, designDomainModel);
			
			//Testing if manoel Person was attached to the design_domain_model
			//Activity.
			assertTrue(designDomainModel.hasIsExecutedBy());
			Iterator<Person> isExecutedBy = designDomainModel.listIsExecutedBy();   
			assertEquals("manoel", isExecutedBy.next().getId());
			
			//Testing if the developer Role was attached to manoel Person
			assertTrue(manoel.hasPerforms());
			Role developer = manoel.listPerforms().next();
			assertNotNull(developer);
			assertEquals("developer", developer.getId());
			
			//Testing if the manoel Person was attached to developer Role.  
			assertTrue(designDomainModel.hasRequires());
			Iterator<Role> rolesIt = designDomainModel.listRequires();
			assertEquals("developer", rolesIt.next().getId());
			
			//Testing if the ExpectedDuration object was created correctly and attached
			//to design_domain_model Activity.
                        
			assertTrue(designDomainModel.hasHasExpectedDuration());
			ExpectedDuration threeWeeks =  designDomainModel.getHasExpectedDuration();
			assertEquals("3_week", threeWeeks.getId());
			assertEquals(3, threeWeeks.getHasAmount());
	}
	
}
