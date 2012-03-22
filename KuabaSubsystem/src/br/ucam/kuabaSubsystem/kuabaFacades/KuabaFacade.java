package br.ucam.kuabaSubsystem.kuabaFacades;

import br.ucam.kuabaSubsystem.repositories.RepositoryLoadException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmi.reflect.RefObject;
import javax.jmi.xmi.MalformedXMIException;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.core.FormalModels;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaExceptions.NoPersonLoggedExpetion;
import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.ExpectedDuration;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaEventPump;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaListener;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Method;
import br.ucam.kuabaSubsystem.kuabaModel.Person;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.kuabaModel.Role;
import br.ucam.kuabaSubsystem.kuabaModel.impl.DefaultKuabaElement;
import br.ucam.kuabaSubsystem.mofParser.AttributeNameRecognizer;
import br.ucam.kuabaSubsystem.mofParser.Director;
import br.ucam.kuabaSubsystem.mofParser.KuabaBuilder;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.Sequence;
import br.ucam.kuabaSubsystem.util.SequenceGenerator;
import br.ucam.kuabaSubsystem.util.SequenceGeneratorImpl;
import br.ucam.kuabaSubsystem.graph.util.KuabaGraphUtil;
import java.util.UUID;
import javax.swing.JOptionPane;


/**
 * 
 * @author Thiago
 * 
 *This class provides all methods necessary to manipulate the br.ucam.KuabaSubsystem
 *framework. It represents the front-end to the other systems that wish to use the
 *KuabaSubsystem framework.  
 * 
 * All contracts for external systems must be specified here.
 */
public class KuabaFacade {	
	
	private KuabaModelFactory modelFactory;
	private KuabaRepository modelRepository;
	private KuabaRepository templatesRepository;
	private KuabaSession session;
	private Map<String, Decision> sessionMap = new HashMap<String, Decision>();
	private Sequence mainSequence;
	
	private Person loggedPerson;
	
	private List<String> errors = new ArrayList<String>();	
	
	public KuabaFacade() {
		super();		
		String fileSequencesPath = KuabaSubsystem.SEQUENCES_FILE_PATH;
		try {
			this.getSequence(new File(fileSequencesPath));
		} catch (IOException e1) {		
			e1.printStackTrace();
			System.exit(0);
		}
		this.session = KuabaSubsystem.getSession();
		String modelUrl = KuabaSubsystem.getProperty(
				KuabaSubsystem.REPOSITORY_PATH);
                
                try {
                    this.modelRepository = KuabaSubsystem.gateway.load(modelUrl);
                } catch (RepositoryLoadException ex) {
                    JOptionPane.showMessageDialog(null, "Fatal Error\n"+ex.getMessage());
                    Logger.getLogger(KuabaFacade.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(0);
                }
                
		KuabaEventPump.getInstance().addListener(Idea.class, 
				(KuabaListener)this.modelRepository());
		
		String templateUrl = KuabaSubsystem.getProperty(
				KuabaSubsystem.TEMPLATES_REPOSITORY_PATH);
                
            try {
                this.templatesRepository = KuabaSubsystem.gateway.load(templateUrl);
            } catch (RepositoryLoadException ex) {
                Logger.getLogger(KuabaFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
            
		this.modelFactory = modelRepository().getModelFactory();
	}
	public void loadIdeaOnSession(String elementId, String name){
		Idea domainIdea = KuabaHelper.getDomainIdea(this.modelRepository(),
				elementId, name);
		if(!(domainIdea == null))
			this.session.putIdea(elementId, domainIdea);
	}
	public void elementRemoved(RefObject element){
		String xmiId = KuabaSubsystem.resolver.resolveXmiId(element);
		Idea domainIdea = KuabaHelper.getDomainIdea(modelRepository(), xmiId, 
				(String)element.refGetValue("name"));
		String designIdeaText = (String)element.refMetaObject(
				).refGetValue("name");
		Idea designIdea = KuabaHelper.getAcceptedDesignIdea(domainIdea, 
				designIdeaText);
		
	}
	public Decision referenceChanged(RefObject source, RefObject target,
			String propertyName){
            
		String xmiId = KuabaSubsystem.resolver.resolveXmiId(source);	
		String sourceName = this.getNamePropertyValue(source);		
		Idea sourceDomainIdea = KuabaHelper.getDomainIdea(
				this.modelRepository(), xmiId, sourceName);
		Idea sourceDesignIdea = KuabaHelper.getAcceptedDesignIdea(
				sourceDomainIdea, (String)source.refMetaObject(
						).refGetValue("name"));
		Question sourceQuestion = 
			(Question)KuabaHelper.getReasoningElementInTree(
					sourceDesignIdea, propertyName+"?");
		if(sourceQuestion == null){
			sourceQuestion = modelRepository().getModelFactory().createQuestion(this.getNextId());
			sourceQuestion.setHasText(propertyName+"?");
			sourceQuestion.addIsSuggestedBy(sourceDesignIdea);
                        
		}
		
		String targetXmiId = KuabaSubsystem.resolver.resolveXmiId(target);
		Idea targetDomainIdea = KuabaHelper.getDomainIdea(
				this.modelRepository(), targetXmiId, this.getNamePropertyValue(
						target));
		Question howModel = (Question)targetDomainIdea.listSuggests().next();
		Idea targetDesignIdea = KuabaHelper.getIdeaByText(
				howModel.getIsAddressedBy(), (String)target.refMetaObject(
						).refGetValue("name"));
                
		if(sourceQuestion != null) sourceQuestion.addIsAddressedBy(targetDesignIdea);
                
		return KuabaSubsystem.facade.makeDecision(sourceQuestion,
				targetDesignIdea, true);
		
	}
	public void renderInFavorArgumentView(Question question, Idea[] idea, 
			String text){
		ArgumentController inFavorController = new InFavorArgumentController(
				idea, null, text, question);
			inFavorController.render();
	}
	public void renderInFavorArgumentView(
			HashMap<Idea, Question> ideaQuestionMap, String text){
		ArgumentController inFavorController = new InFavorArgumentController(
				ideaQuestionMap, null, text);
			inFavorController.render();
	}
	
	public void renderInFavorArgumentView(Question question, Idea idea, 
			String text){
		this.renderInFavorArgumentView(question, new Idea[]{idea}, text);
	}
	
	public boolean existsDomainIdea(RefObject designElement){
		Collection<Idea> domainIdeas = 
			modelRepository().getDomainIdeasWhereIdLike(
				KuabaSubsystem.resolver.resolveXmiId(designElement));
		return !domainIdeas.isEmpty();
	}
	public String getNamePropertyValue(RefObject refObject){
		String newName = "";
		if(refObject.refGetValue("name") != null)
			newName = (String)refObject.refGetValue("name");
		return newName;
	}
	public Idea getIdeaOnSession(String elementId){
		return this.session.getIdea(elementId);
	}
	public void addIdeaOnSession(Idea idea){
		String elementId = idea.getId().split("_")[1];
		this.session.putIdea(elementId, idea);
	}
	public void setKuabaModelFactory(KuabaModelFactory factory){
		this.modelFactory = factory;
	}

	public KuabaModelFactory getKuabaFactory() {
		return modelFactory;
	}

	public void setKuabaFactory(KuabaModelFactory kuabaFactory) {
		this.modelFactory = kuabaFactory;
	}

	public Person getLoggedPerson() {
		return loggedPerson;
	}

	public void setLoggedPerson(Person loggedPerson) {
		this.loggedPerson = loggedPerson;
	}	
	
	private Sequence getSequence(File sequencesFile) throws IOException{
		SequenceGenerator generator = new SequenceGeneratorImpl(sequencesFile);
		String modelSequenceName = KuabaSubsystem.MODEL_SEQUENCE_NAME;
		Sequence seq = null;
		if(generator.loadSequence(modelSequenceName) == null)
			seq = generator.createNewSequence(modelSequenceName, 0, 1);
		else
			seq = generator.loadSequence(modelSequenceName);
		this.mainSequence = seq;
		DefaultKuabaElement.setIdGenerator(this.mainSequence);
		return seq;
	}	
	
	private Question getPrototypeHowModelQuestion(FormalModel formalModel){		
		Question q = this.templatesRepository.getQuestion(
		"How_Model_"); 
		return q;
	}
	private Question buildKuabaFromMetamodel(String directorClassName, FormalModel formalModel,
			KuabaModelFactory factory){
		Class directorClass = null;
		try {
			directorClass = Class.forName(directorClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Director director = null;
		try {
			director = (Director) directorClass.newInstance();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}
		KuabaBuilder builder = new KuabaBuilder(factory, formalModel,
				mainSequence);
		
		File metamodelFile = new File(KuabaSubsystem.METAMODEL_PATH);		
        
		try {
			director.init(builder, new AttributeNameRecognizer(), metamodelFile.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MalformedXMIException e) {
			e.printStackTrace();
		}
		director.construct();
		return builder.getResult();
	}
	
	public FormalModel formalModelAdded(String formalModelName){		
		
		KuabaModelFactory factory = templatesRepository.getModelFactory(); 
		FormalModel formalModel = templatesRepository.getFormalModel(formalModelName);		
		if (formalModel == null) {
			formalModel = factory.createFormalModel(formalModelName);
			String directorClassName = KuabaSubsystem.PARSER_PACK + ".UmlClassDirector";
			this.buildKuabaFromMetamodel(directorClassName, formalModel, factory);
			KuabaSubsystem.gateway.save(templatesRepository);
		}		
		
		return formalModel;
	}
	
	
	private String getNextId(){				
		return "_" + this.mainSequence.nextVal(); 
	}
	
	public Decision makeDecision(Question question, Idea idea, boolean isAccepted){
		
		Decision decision = null;		
		System.out.println("creating decision");
		decision = this.modelFactory.createDecision(this.getNextId());		
		decision.setIsAccepted(isAccepted);
		decision.setHasDate(new GregorianCalendar());
		decision.setConcludes(idea);
		idea.addIsConcludedBy(decision);
		question.addHasDecision(decision);
				//this.sessionMap.put(sessionMapKey, decision);
		
				
		return decision;
	}
	
	public Idea designIdeaAccepted(String howModelText, String howModelDesignIdeaText, String addressedQuestionText,
			String designIdeaText){
		
		Question howModel = modelRepository().getFirstQuestionByText(howModelText);
		Idea howModelAnswer = modelRepository().getFirstIdeaByText(howModel, howModelDesignIdeaText);
		Question addressedQuestion = modelRepository().getFirstQuestionByText(howModelAnswer, addressedQuestionText+"?");
		Idea acceptedDesignIdea = modelRepository().getFirstIdeaByText(addressedQuestion, designIdeaText);
		if (acceptedDesignIdea == null) {
			acceptedDesignIdea = this.modelFactory.createIdea(this.getNextId());
			addressedQuestion.addIsAddressedBy(acceptedDesignIdea);
			acceptedDesignIdea.setHasText(designIdeaText);
		}		
		String type = addressedQuestion.getHasType();		
		if(type.equals(Question.XORTYPE)){
			Collection<Decision> decisions = addressedQuestion.getHasDecision();
			for (Decision decision : decisions) {
				decision.setIsAccepted(false);
			}
		}
		this.makeDecision(addressedQuestion, acceptedDesignIdea, true);		
		return acceptedDesignIdea;		
	}
	
	private Question getHowModelQuestion(String domainIdeaText){
		return modelRepository().getFirstQuestionByText(
				Question.DOMAIN_QUESTION_TEXT_PREFIX+ domainIdeaText + "?");		
	}
	
	public void domainIdeaConsidered(Idea domainIdea){
		this.session.unitOfWork().addConsideredIdea(domainIdea);
		this.addIdeaOnSession(domainIdea);
		String questionText = 
			Question.DOMAIN_QUESTION_TEXT_PREFIX + domainIdea.getHasText() + "?";		
		Question howModel = 
			modelRepository().getFirstQuestionByText(
					domainIdea, questionText);
		List<Idea> acceptedDesignIdeas = 
			modelRepository().getRejectedIdeas(howModel);
		
		for (Idea idea : acceptedDesignIdeas)	// não entendi
			this.makeDecision(howModel, idea, true);			
	}
	
	public Idea findDomainIdea(String domainIdeaText, String designIdeaText){
		List<Idea> domainIdeas = modelRepository().getIdeaByText(domainIdeaText);
		for (Idea idea : domainIdeas) {
			Question howModel = (Question)idea.getSuggests().iterator().next();
			Collection<Idea> ideas = modelRepository().getAcceptedIdeas(howModel);
			for (Idea addressedIdea : ideas)
				if(designIdeaText.equals(addressedIdea.getHasText()))
					return idea;			
		}
		return null;
	}
	
	public void domainIdeaDesconsidered(Idea rejectedDomainIdea){
		this.session.unitOfWork().addRejectedIdea(rejectedDomainIdea);
		Question howModel = 
			(Question)rejectedDomainIdea.listSuggests().next();				
		List<Idea> acceptedDesignIdeas = 
			modelRepository().getAcceptedIdeas(howModel);			
		for (Idea idea : acceptedDesignIdeas)				
			this.makeDecision(howModel, idea, false);
	}
	
	public Idea domainIdeaRejected(String elementMofId, String text){		
		Idea rejectedDomainIdea = KuabaHelper.getDomainIdea(
				modelRepository(), elementMofId, text);
		if(rejectedDomainIdea != null){		
			this.domainIdeaDesconsidered(rejectedDomainIdea);			
			return rejectedDomainIdea;
		}
		return null;
	}
	
	public Idea domainIdeaRejectedAndAdded(Idea rejectedDomainIdea,
			String domainIdeaText, String designIdeaText, String elementId ){
		Idea existentDomainIdea = KuabaHelper.getDomainIdea(
				modelRepository(), elementId, domainIdeaText);
		Idea domainIdea = null;
		if(existentDomainIdea != null){
			this.domainIdeaConsidered(existentDomainIdea);
			return existentDomainIdea;			
		
		}else{
			Question prototypeHowModel = 
				(Question)rejectedDomainIdea.listSuggests().next();
			prototypeHowModel.removeIsSuggestedBy(rejectedDomainIdea);			
			domainIdea = this.makeDomainIdeaSubTree(
					domainIdeaText, designIdeaText, elementId, prototypeHowModel);
			prototypeHowModel.addIsSuggestedBy(rejectedDomainIdea);			
		}
		return domainIdea;		
	}
	
	public Question clonePrototypeQuestion(Question prototypeQuestion){
		Map<ReasoningElement, Integer> stageMap = KuabaGraphUtil.getReasoningElementStageMap(prototypeQuestion);

		return prototypeQuestion.deepCopy(stageMap, modelRepository());  
	}
        
        public Question clonePrototypeQuestion(Question prototypeQuestion, String designIdeaText){
                
                Question resp = modelRepository().getModelFactory().createQuestion(UUID.randomUUID().toString());
                Idea designIdea = modelRepository().getModelFactory().createIdea(UUID.randomUUID().toString());
                designIdea.setHasText(designIdeaText);
                designIdea.addAddress(resp);
                designIdea.setIsDefinedBy(modelRepository().getFormalModel(FormalModel.UML_FORMAL_MODEL_ID));
                
//                if(designIdeaText.equals("AssociationEnd"))
//                    try {
//            throw new Exception();
//        } catch (Exception ex) {
//            Logger.getLogger(KuabaFacade.class.getName()).log(Level.SEVERE, null, ex);
//        }
                
                return resp;
	}
	
	private Idea newDomainIdea(Question cloneHowModel, String elementId, 
			String domainIdeaText){	
		
		cloneHowModel.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX + domainIdeaText + "?");
		Idea domainIdea = modelRepository.getModelFactory().createIdea(
				elementId + "_"+ mainSequence.nextVal());
		domainIdea.setHasText(domainIdeaText);		
		domainIdea.addAddress(getRootQuestion());
		domainIdea.addSuggests(cloneHowModel);		
		return domainIdea;
	}
	
	public Question getRootQuestion(){
		Question rootQuestion = this.modelRepository().getQuestion(Question.ROOT_QUESTION_ID);
//		Question whatElements = null;
		if(rootQuestion == null){			
			rootQuestion = modelRepository.getModelFactory().createQuestion(Question.ROOT_QUESTION_ID);
			rootQuestion.setHasText("What Elements?");
		}	
		return rootQuestion;
	}
	
	public Idea makeDomainIdeaSubTree(String domainIdeaText,
			String designIdeaText, String elementId, Question prototypeHowModel){
		Idea domainIdea = null;
                
                //para volta a cópia completa descomente esta linha e comente a próxima
//                Question cloneHowModel = this.clonePrototypeQuestion(prototypeHowModel);
		Question cloneHowModel = this.clonePrototypeQuestion(prototypeHowModel, designIdeaText);
                
                
		domainIdea = this.newDomainIdea(cloneHowModel, elementId, domainIdeaText);
		Question howModel = (Question)domainIdea.listSuggests().next();

		Idea designIdea = 
			KuabaHelper.getIdeaByText(howModel.getIsAddressedBy(), designIdeaText);
                
		if(designIdea != null)			
			this.makeDecision(cloneHowModel, designIdea, true);		
		this.domainIdeaConsidered(domainIdea);		
		return domainIdea;
	}
	
	public Idea domainIdeaAdded(String domainIdeaText,		
		String designIdeaText, String elementId){
		Idea existentDomainIdea = KuabaHelper.getDomainIdea(
				modelRepository(), elementId, domainIdeaText);	
		if(existentDomainIdea != null){
			this.domainIdeaConsidered(existentDomainIdea);
			return existentDomainIdea;
		}
		else{
			FormalModel formalModel = this.formalModelAdded(FormalModels.UML_CLASS_DIAGRAM);		
			Question prototypeHowModel = this.getPrototypeHowModelQuestion(formalModel);		
			return makeDomainIdeaSubTree(domainIdeaText, designIdeaText,
					elementId, prototypeHowModel);
		}
		
	}
	
	public void representDesignData(Method method, Activity activity, int expectedAmount, 
			String durationUnitTime, Set<String> roles) throws NoPersonLoggedExpetion{		
		if (this.loggedPerson == null)
			throw new NoPersonLoggedExpetion("No person logged in this session, please login!");		
		method.addAggregates(activity);		
		String expectedDurationID = expectedAmount + "_" + durationUnitTime.trim();
		ExpectedDuration expectedDuration = null;
		
		if(this.modelFactory.getKuabaRepository().getExpectedDuration(expectedDurationID) == null) {
			expectedDuration = this.modelFactory.createExpectedDuration(expectedDurationID);
                }else {
			expectedDuration = this.modelFactory.getKuabaRepository().getExpectedDuration(expectedDurationID);
                }
		               
		expectedDuration.setHasAmount(expectedAmount);
		expectedDuration.addHasUnitTime(durationUnitTime);
                
		activity.setHasExpectedDuration(expectedDuration);
		this.loggedPerson.addExecutes(activity);
		
		Iterator<String> rolesIterator = roles.iterator();
		Role role = null;
		while (rolesIterator.hasNext()) {
			
			String roleName = rolesIterator.next().trim();
			if(this.modelFactory.getKuabaRepository().getRole(roleName) == null)		
				role = this.modelFactory.createRole(roleName);			
			else{
				role = this.modelFactory.getKuabaRepository().getRole(roleName);
			}			
			role.setHasName(roleName);
			activity.addRequires(role);
			this.loggedPerson.addPerforms(role);
		}		
	}
	
	public boolean saveSession(KuabaRepository kr, File destination){
//		System.out.println("model em saveSession? " + ((OWLModel)modelRepository().getModel()).getName());
		Question whatElements = modelRepository().getQuestion(Question.ROOT_QUESTION_ID);
                
		List<Idea> consideredIdeas = this.session.unitOfWork().getConsideredIdeas();
		for (Idea idea : consideredIdeas) 
			this.makeDecision(whatElements, idea, true);		
		List<Idea> rejectedIdeas = this.session.unitOfWork().getRejectedIdeas();
		for (Idea idea2 : rejectedIdeas) 
			this.makeDecision(whatElements, idea2, false);				
		return KuabaSubsystem.gateway.save(kr, destination);		
	}
	
	public boolean saveSession() {
		return KuabaSubsystem.gateway.save(modelRepository());
	}
	public Sequence getMainSequence() {
		return mainSequence;
	}
	public KuabaRepository modelRepository() {
            try {
                return KuabaSubsystem.gateway.load(KuabaSubsystem.getProperty(KuabaSubsystem.REPOSITORY_PATH));
            } catch (RepositoryLoadException ex) {
                Logger.getLogger(KuabaFacade.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
	}
	
}
