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
import br.ucam.kuabaSubsystem.controller.InFavorJustificationController;
import br.ucam.kuabaSubsystem.controller.JustificationController;
import br.ucam.kuabaSubsystem.controller.ObjectsToArgumentController;
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
import br.ucam.kuabaSubsystem.kuabaModel.Solution;
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
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import static br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver.observersPack;
import br.ucam.kuabaSubsystem.util.MofHelper;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.UUID;
import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import javax.swing.JFrame;
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
	private Sequence mainSequence;
        
        static List<String> solutionedIdeas = new ArrayList<String>();
        static List<String> actualSolutionIdeas = new ArrayList<String>();
        static List<String> refusedSolutionedIdeas = new ArrayList<String>();
        
        static List<Question> solQuestion;
        
	
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
	public Decision referenceChanged(RefObject source, RefObject target,String propertyName){
            
            Idea sourceDomainIdea= null;
            Idea targetDomainIdea = null;
            
            
            
            for(Idea d: KuabaSubsystem.facade.modelRepository().getDomainIdeas()){
                String txt1 = d.getId().split("_")[1];
                String txt2 = source.refMofId().split(":")[3];
                String txt = source.refMofId();
                if(txt1.equals(txt2))
                    sourceDomainIdea = d;
            }
            for(Idea d: KuabaSubsystem.facade.modelRepository().getDomainIdeas()){
                String txt1 = d.getId().split("_")[1];
                String txt2 = source.refMofId().split(":")[3];
                String txt = source.refMofId();
                if(txt1.equals(txt2))
                    targetDomainIdea = d;
            }
            
            Idea sourceDesignIdea = KuabaHelper.getAcceptedDesignIdea(sourceDomainIdea);
            
            
            //Idea sourceDesignIdea = KuabaHelper.getAcceptedDesignIdea(sourceDomainIdea, (String)source.refMetaObject().refGetValue("name"));
            
            Question sourceQuestion = (Question)KuabaHelper.getReasoningElementInTree(sourceDesignIdea, propertyName+"?");
            if(sourceQuestion == null){
		sourceQuestion = modelRepository().getModelFactory().createQuestion(this.getNextId());
		sourceQuestion.setHasText(propertyName+"?");
		sourceQuestion.addIsSuggestedBy(sourceDesignIdea);
                        
            }
		
            String targetXmiId = KuabaSubsystem.resolver.resolveXmiId(target);
		//Idea targetDomainIdea = KuabaHelper.getDomainIdea(this.modelRepository(), targetXmiId, this.getNamePropertyValue(
	//					target));
            Question howModel = (Question)targetDomainIdea.listSuggests().next();
            Idea targetDesignIdea = KuabaHelper.getIdeaByText(howModel.getIsAddressedBy(), (String)target.refMetaObject().refGetValue("name"));
                
            if(sourceQuestion != null) 
                sourceQuestion.addIsAddressedBy(targetDesignIdea);
                
		return KuabaSubsystem.facade.makeDecision(sourceQuestion,targetDesignIdea, true);
		
	}
        //esta versao foi usada exclusivamente pro attributeObserver
        
        public Decision referenceChanged(Idea sourceDesignIdea, RefObject target,String propertyName){
            
		//String xmiId = KuabaSubsystem.resolver.resolveXmiId(source);	
		//String sourceName = this.getNamePropertyValue(source);		
		//Idea sourceDomainIdea = KuabaHelper.getDomainIdea(this.modelRepository(), xmiId, sourceName);
		//Idea sourceDesignIdea = KuabaHelper.getAcceptedDesignIdea(sourceDomainIdea, (String)source.refMetaObject().refGetValue("name"));
		Question sourceQuestion = (Question)KuabaHelper.getReasoningElementInTree(sourceDesignIdea, propertyName+"?");
		if(sourceQuestion == null){
			sourceQuestion = modelRepository().getModelFactory().createQuestion(this.getNextId());
			sourceQuestion.setHasText(propertyName+"?");
			sourceQuestion.addIsSuggestedBy(sourceDesignIdea);
                        
		}
		
		String targetXmiId = KuabaSubsystem.resolver.resolveXmiId(target);
		Idea targetDomainIdea = KuabaHelper.getDomainIdea(this.modelRepository(), targetXmiId, this.getNamePropertyValue(
						target));
		Question howModel = (Question)targetDomainIdea.listSuggests().next();
		Idea targetDesignIdea = KuabaHelper.getIdeaByText(howModel.getIsAddressedBy(), (String)target.refMetaObject().refGetValue("name"));
                
		if(sourceQuestion != null) 
                    sourceQuestion.addIsAddressedBy(targetDesignIdea);
                
		return KuabaSubsystem.facade.makeDecision(sourceQuestion,targetDesignIdea, true);
		
	}
        
        
	public void renderInFavorArgumentView(Question question, Idea[] idea, String text){
            ArgumentController inFavorController = new InFavorArgumentController(idea, null, text, question);
            inFavorController.render();
	}
	public void renderInFavorArgumentView(
			HashMap<Idea, Question> ideaQuestionMap, String text){
		ArgumentController inFavorController = new InFavorArgumentController(
				ideaQuestionMap, null, text);
			inFavorController.render();
	}
	
	public void renderInFavorArgumentView(Question question, Idea idea, String text){
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
	
        //Toma uma decisao para um par Questão-Ideia
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
		String questionText = Question.DOMAIN_QUESTION_TEXT_PREFIX + domainIdea.getHasText() + "?";		
		Question howModel = modelRepository().getFirstQuestionByText(domainIdea, questionText);
		List<Idea> acceptedDesignIdeas = modelRepository().getRejectedIdeas(howModel);
                for (Idea idea : acceptedDesignIdeas)
                {
                    if(idea.hasIsConcludedBy())
                        for(Decision d : idea.getIsConcludedBy())
                            d.setIsAccepted(true);
                    else
                        this.makeDecision(howModel, idea, true);
                }
                
                
		//for (Idea idea : acceptedDesignIdeas)	// não entendi
		//	this.makeDecision(howModel, idea, true);			
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
	
        //Rejeita esta ideia de dominio
	public void domainIdeaDesconsidered(Idea rejectedDomainIdea){
                if(this.session.unitOfWork().hasConsideredIdeas(rejectedDomainIdea.getId()))
                    this.session.unitOfWork().removeAcceptedIdea(rejectedDomainIdea.getId());
                
		this.session.unitOfWork().addRejectedIdea(rejectedDomainIdea);
		Question howModel = (Question)rejectedDomainIdea.listSuggests().next();
                
                List<Idea> acceptedDesignIdeas = modelRepository().getAcceptedIdeas(howModel);
                for (Idea idea : acceptedDesignIdeas)
                {
                    if(idea.hasIsConcludedBy())
                        for(Decision d : idea.getIsConcludedBy())
                            d.setIsAccepted(false);
                    
                    else
                        this.makeDecision(howModel, idea, false);
                }				
		
	}
	
        //Procura a ideia de dominio pelo ID e texto e caso encontre, chama a funcao acima para rejeitar
	public Idea domainIdeaRejected(String elementMofId, String text){		
		Idea rejectedDomainIdea = KuabaHelper.getDomainIdea(
				modelRepository(), elementMofId, text);
		if(rejectedDomainIdea != null){		
			this.domainIdeaDesconsidered(rejectedDomainIdea);			
			return rejectedDomainIdea;
		}
		return null;
	}
	
        //Adiciona a nova ideia de dominio como aceita e recusa a ideia antiga.
	public Idea domainIdeaRejectedAndAdded(Idea rejectedDomainIdea,
			String domainIdeaText, String designIdeaText, String elementId ){
		Idea existentDomainIdea = KuabaHelper.getDomainIdea(
				modelRepository(), elementId, domainIdeaText);
		Idea domainIdea = null;
		if(existentDomainIdea != null){
			this.domainIdeaConsidered(existentDomainIdea);
			return existentDomainIdea;			
		
		}
                else
                {
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
        
        //Cria uma questao sugerida por um elemento de raciocinio e uma ideia que endereca esta questao
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
        //Copia a questao sugerida passada como parametro para a ideia de design passada como parametro
        public Question clonePrototypeQuestion(Question prototypeQuestion, Idea designIdea){
            
            if(designIdea==null)
            {
                designIdea = modelRepository().getModelFactory().createIdea(UUID.randomUUID().toString());
                designIdea.setIsDefinedBy(modelRepository().getFormalModel(FormalModel.UML_FORMAL_MODEL_ID));
            }
                
            Question suggested = modelRepository().getModelFactory().createQuestion(UUID.randomUUID().toString());
            designIdea.addSuggests(suggested);    
                
            return suggested;
	}
        
        public Idea clonePrototypeIdea(Question addressed,String designIdeaText)
        {
            Idea newIdea = modelRepository().getModelFactory().createIdea(UUID.randomUUID().toString());
            newIdea.addAddress(addressed);
            newIdea.setHasText(designIdeaText);
            newIdea.setIsDefinedBy(modelRepository().getFormalModel(FormalModel.UML_FORMAL_MODEL_ID));
            return newIdea;
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
	
        //Esta funcao cria a subarvore da idiea de dominio em questao
        
        //Continuar o tratamento, verificar na funcao referenceModified os requisitos para criar a associacao na ideia de design. 
        //QUIÇÁ NEM PRECISO PERCORRER A SUBARVORE, só criar a ideai de design nova xD
	public Idea makeDomainIdeaSubTree(String domainIdeaText,String designIdeaText, String elementId, Question prototypeHowModel){
		Idea domainIdea = null;
                
                //para volta a cópia completa descomente esta linha e comente a próxima
//                Question cloneHowModel = this.clonePrototypeQuestion(prototypeHowModel);
		Question cloneHowModel = this.clonePrototypeQuestion(prototypeHowModel, designIdeaText);
                
                
		domainIdea = this.newDomainIdea(cloneHowModel, elementId, domainIdeaText);
		Question howModel = (Question)domainIdea.listSuggests().next();

		Idea designIdea = KuabaHelper.getIdeaByText(howModel.getIsAddressedBy(), designIdeaText);
                        
		if(designIdea != null)			
			this.makeDecision(cloneHowModel, designIdea, true);		
		this.domainIdeaConsidered(domainIdea);		
		return domainIdea;
	}
	
        //Esta funcao retorna a ideia de dominio adicionada. Caso ja exista, diz que esta ideia de dominio e considerada
	public Idea domainIdeaAdded(String domainIdeaText,String designIdeaText, String elementId){
		//Idea existentDomainIdea = KuabaHelper.getDomainIdea(modelRepository(), elementId, domainIdeaText);	
		Idea existentDomainIdea = KuabaHelper.getDomainIdea(domainIdeaText);
                
                if(existentDomainIdea != null){
			this.domainIdeaConsidered(existentDomainIdea);
			return existentDomainIdea;
		}
                
                //Caso nao exista ideia de dominio, cria uma 
		else{
			FormalModel formalModel = this.formalModelAdded(FormalModels.UML_CLASS_DIAGRAM);		
			Question prototypeHowModel = this.getPrototypeHowModelQuestion(formalModel);		
			return makeDomainIdeaSubTree(domainIdeaText, designIdeaText,elementId, prototypeHowModel);
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
            
               
                //Crio o elemento solucao no meu modelo
              
                List<Solution> solutioned = this.createSolutions();
                
                List<Solution> toRemove = new ArrayList<Solution>();
                for(Solution s : this.modelRepository.getAllSolutionInstances())
                {
                    if(!solutioned.contains(s))
                    {
                        for(Solution s2 : solutioned){
                            if(s.getIncludes().containsAll(s2.getIncludes())&&s.getIncludes().size()==s2.getIncludes().size())
                            {
                                toRemove.add(s2);
                                break;
                            }
                        }
                    }
                }
                for(Solution s : toRemove){
                    solutioned.remove(s);
                    this.modelRepository.removeIndividual(s.getId());
                }
                
                
                
                String str="";
                int cont=1;
                for(Solution s : solutioned){
                    String res = (s.getAccepted()?"aceita ":"recusada ") ;
                    str+="Solução "+ res +Integer.toString(cont)+"\nIdeias: ";
                    for(Idea idea : s.getIncludes()){
                        str+=idea.getHasText()+", ";
                    }
                    str+="\n";
                    cont++;
                }   
                
                //JOptionPane.showConfirmDialog(new JFrame(), str);
                
                for(Solution s: solutioned){
                    String question;
                    question=null;
                    question=this.getSolutionQuestion(s);
                    
                    Justification just = this.modelFactory.createJustification(this.getNextId());
                    s.addContains(just);
                    //s.setQuestion(question);
                    String justificativa = JOptionPane.showInputDialog(new JFrame(), question, "Justificativa", JOptionPane.QUESTION_MESSAGE) ; 
                    if(!(justificativa==null||justificativa.equals("")))
                        s.getContains().iterator().next().setHasText(justificativa);

                }
                
		return KuabaSubsystem.gateway.save(kr, destination);		
	}
        
        public String getSolutionQuestion(Solution sol){
            Idea owner,assoc;
            owner=null;
            assoc=null;
            boolean isOwner=false;
            boolean isAssoc=false;
            for(Idea idea : sol.getIncludes()){
                if(idea.getHasText().equals("Association")){
                    isAssoc=true;
                    break;
                }
                else if(idea.getHasText().equals("Attribute")||idea.getHasText().equals("Operation")){
                    isOwner=true;
                    break;
                }
            }
            
            if(isOwner)
            {
                List<String> atributos = new ArrayList<String>();
                List<String> operacoes = new ArrayList<String>();
                
                for(Idea idea : sol.getIncludes()){
                    if(idea.getHasText().equals("Class"))
                        owner=KuabaHelper.getDomainIdea(idea);
                    if(idea.getHasText().equals("Attribute")){
                        atributos.add(KuabaHelper.getDomainIdea(idea).getHasText());
                    }
                    if(idea.getHasText().equals("Operation")){
                        operacoes.add(KuabaHelper.getDomainIdea(idea).getHasText());
                    }
                }
                String txt = sol.getAccepted()? " decidiu " : " desistiu de ";
                String question ="Por quê você" +txt+ "modelar "+ owner.getHasText()+" com";
                if(atributos.size()==1)
                    question += " o atributo " + atributos.get(0);
                else if(atributos.size()>1){
                    question+=" os atributos " + atributos.get(0);
                    int i=1;
                    while(i<atributos.size()-1){
                        question+=", "+atributos.get(i);
                        i++;
                    }
                    question+=" e "+atributos.get(atributos.size()-1);
                }    
                if(atributos.size()>1&&operacoes.size()>1)
                    question += " e ";
                if(operacoes.size()==1){
                    question+= " a operacao "+operacoes.get(0);
                }
                else if(operacoes.size()>1){
                    question+=" as operacoes " + operacoes.get(0);
                    int i=1;
                    while(i<operacoes.size()-1)
                        question+=", "+operacoes.get(i);
                    question+=" e "+operacoes.get(operacoes.size()-1);
                }
                question+="?";
                return question;
            }
            else if(isAssoc)
            {
                for(Idea idea : sol.getIncludes()){
                    if(idea.getHasText().equals("Association")){
                        assoc=KuabaHelper.getDomainIdea(idea);
                        break;
                    }
                }
                
                String txt = sol.getAccepted()? " decidiu " : " desistiu de ";
                
                String question = "Por quê você" +txt+ "modelar a associação "+ assoc.getHasText() +" entre as classes ";
                List<String> classes  = new ArrayList<String>();
                for(Idea idea : sol.getIncludes())
                    if(idea.getHasText().equals("Class"))
                        classes.add(KuabaHelper.getDomainIdea(idea).getHasText());
                question += classes.get(0) + " e " + classes.get(1) + "?";
                return question;
            }
            else if (!isAssoc&&!isOwner){
                for(Idea idea : sol.getIncludes()){
                    if(idea.getHasText().equals("Class"))
                        owner=KuabaHelper.getDomainIdea(idea);
                }
                String txt = sol.getAccepted()? " decidiu " : " desistiu de ";
                String question ="Por quê você"+txt+ "modelar "+ owner.getHasText()+"?";
                return question;
            }
            return null;
        }
        
        //TODO COLOCAR COMO CRITERIO DE PARADA SE JA EXISTE SOLUCAO COM ESTAS MESMAS IDEIAS
        
        public List<Solution> createSolutions(){
            
            List<Idea> designIdeas = new ArrayList<Idea>();
            List<Idea> solutionedIdea = new ArrayList<Idea>();
            
            List<Solution> solutions = new ArrayList<Solution>();
            //Algortimo da Gabriela
            //1.
            for(Idea idea : this.modelRepository.getQuestion(Question.ROOT_QUESTION_ID).getIsAddressedBy()){
                for(Idea design : idea.getSuggests().iterator().next().getIsAddressedBy()){
                    designIdeas.add(design);
                }
            }
            
            //2
            for (Idea designIdea : designIdeas)
            {
                if(!solutionedIdea.contains(designIdea))
                {
                    //Se sugere outras questões
                    if(designIdea.hasSuggests())
                    {
                        //a
                        int cont=0;
                        //Pego as ideias de design que respondem a questao sugerida
                        for(Idea addrSugg : designIdea.getSuggests().iterator().next().getIsAddressedBy()){
                            //Se a ideia de design sugere mais ideias
                            if(addrSugg.hasSuggests()){
                                cont++;
                            }
                        }
                        //b
                        //Se mais de uma ideia responde a questao automaticamente sugerida (na UML == connection) ja sei o que fazer
                        if(cont>=2)
                        {
                         
                            //VERIFICA SE A IDEIA É ACEITA OU REJEITADA
                            boolean isAccepted = false;
                            for(Question q : designIdea.getAddress()){
                                if(q.getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){
                                    for(Decision dec : q.getHasDecision()){
                                        if(dec.getConcludes().equals(designIdea)&& dec.getIsAccepted()){
                                            isAccepted=true;
                                            break;
                                        }
                                    }
                                }
                            }
                            
                            if(isAccepted)
                            {
                            
                                if(!solutionedIdea.contains(designIdea) )
                                {
                                    Solution A = this.modelFactory.createSolution(this.getNextId()); //i.
                                
                                    KuabaFacade.solQuestion = new ArrayList<Question>();
                                
                                    A = this.varrerSubArvoreAceita(designIdea, A);  //iii.

                                    for(Idea solIdea : A.getIncludes()){ //iv.
                                        for(Question addressed : solIdea.getAddress()){ //1.
                                            if(addressed.getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){
                                                KuabaFacade.solQuestion.add(addressed);
                                                Idea domain = (Idea)addressed.getIsSuggestedBy().iterator().next();
                                                A.addIncludes(domain);  
                                            }
                                        }
                                    }
                                
                                
                                    for(Idea solIdea : A.getIncludes()){ //iv 2.
                                        if(designIdeas.contains(solIdea)){
                                            int cont2=0;
                                            for(Question q : solIdea.getAddress()){
                                                if(KuabaFacade.solQuestion.contains(q)){
                                                    cont2++;
                                                }
                                            }
                                            if(cont2==solIdea.getAddress().size()){
                                                solutionedIdea.add(solIdea);
                                            }
                                        }
                                    }
                                    A.setAccepted(true);
                                    solutions.add(A);
                                }
                            }
                            
                            else
                            {
                            
                            //V.
                                if(designIdea.hasSuggests())
                                {
                                    int cont3 =0;
                                    Question suggested = designIdea.getSuggests().iterator().next();
                                    for(Decision dec: suggested.getHasDecision())
                                    {
                                        if(!dec.getIsAccepted())
                                            cont3++;
                                    }
                                    //NAO PASSAR AQUI QUANDO A IDEIA DE DESIGN FOR ACEITA
                                        if(cont3==suggested.getIsAddressedBy().size())
                                        //if(!dec.getIsAccepted())
                                        {
                                            Solution A = this.modelFactory.createSolution(this.getNextId());
                                            KuabaFacade.solQuestion = new ArrayList<Question>();
                                            A = this.varrerSubArvoreRejeitada(designIdea,A);    //4.
                                        
                                            for(Idea solIdea : A.getIncludes()){ //5.
                                                for(Question addressed : solIdea.getAddress()){
                                                    if(addressed.getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){ 
                                                        KuabaFacade.solQuestion.add(addressed);
                                                        Idea domain = (Idea)addressed.getIsSuggestedBy().iterator().next();
                                                        A.addIncludes(domain);  
                                                    }   
                                                }
                                            }
                                        
                                            for(Idea solIdea : A.getIncludes()){ //iv 2.
                                                if(designIdeas.contains(solIdea)){
                                                    int cont2=0;
                                                    for(Question q : solIdea.getAddress()){
                                                        if(KuabaFacade.solQuestion.contains(q)){
                                                            cont2++;
                                                        }
                                                    }
                                                    if(cont2==solIdea.getAddress().size()){
                                                        solutionedIdea.add(solIdea);   
                                                    }
                                                }
                                            }    
                                            A.setAccepted(false);
                                            solutions.add(A);    
                                        }
                                }    
                            }
                        }
                    }
                }
                
                if(!solutionedIdea.contains(designIdea))
                {
                    //c.
                    Idea domain = KuabaHelper.getDomainIdea(designIdea);
                    Question root = this.modelRepository.getQuestion(Question.ROOT_QUESTION_ID);
                    for(Decision dec : root.getHasDecision()){
                        if(!dec.getIsAccepted()&&dec.getConcludes().equals(domain)){
                            Solution A = this.modelFactory.createSolution(this.getNextId());
                            
                            KuabaFacade.solQuestion = new ArrayList<Question>();
                            A = this.varrerSubArvoreRejeitada(designIdea, A);
                            
                            
                            for(Idea solIdea : A.getIncludes()){ //iv.
                                for(Question addressed : solIdea.getAddress()){ //1.
                                    if(addressed.getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){ 
                                        KuabaFacade.solQuestion.add(addressed);
                                        Idea domain2 = (Idea)addressed.getIsSuggestedBy().iterator().next();
                                        A.addIncludes(domain2);  
                                    }
                                }
                            }
                            
                            for(Idea solIdea : A.getIncludes()){ //iv 2.
                                if(designIdeas.contains(solIdea)){
                                    int cont2=0;
                                    for(Question q : solIdea.getAddress()){
                                        if(KuabaFacade.solQuestion.contains(q)){
                                            cont2++;
                                        }
                                    }
                                    if(cont2==solIdea.getAddress().size()){
                                        solutionedIdea.add(solIdea);
                                    }
                                }
                            }
                            
                            A.setAccepted(false);
                            solutions.add(A);                       
                        }
                    }
                }
                
                if(!solutionedIdea.contains(designIdea))
                {
                    //d.
                    //Listo todas as questoes respondidas como keys do hashMap, onde cada key contem uma lista com as questoes iguais
                    Map<String,List<Question>> ideas = new HashMap<String,List<Question>>();
                    
                    for(Question addressed : designIdea.getAddress()){
                        if(!addressed.getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){
                            for(Decision dec : addressed.getHasDecision()){
                                if(dec.getConcludes().equals(designIdea)&&dec.getIsAccepted()){
                                    
                                    
                                    if(ideas.keySet().contains(addressed.getHasText())){
                                        List<Question> aux = ideas.get(addressed.getHasText());
                                        aux.add(addressed);
                                        ideas.put(dec.getConcludes().getHasText(), aux);
                                    }
                                    else{
                                        List<Question> aux = new ArrayList<Question>();
                                        aux.add(addressed);
                                        ideas.put(addressed.getHasText(), aux);
                                    }
                                    
                                }  
                            }
                        }
                    }
                    //Aqui comeca o real processamento
                    for(String str : ideas.keySet()){
                        List<Question> aux = ideas.get(str);
                        if(aux.size()>1)
                        {
                            //Evita que execute 2 vezes este codigo para um conjunto que ja foi solucionado
                            boolean isContained=false;
                            for(Question q : aux){
                                if(solutionedIdea.contains((Idea)q.getIsSuggestedBy().iterator().next()))
                                    isContained=true;
                            }
                            if(isContained)
                                break;
                            
                            
                            if(((Idea)aux.get(0).getIsSuggestedBy().iterator().next()).getAddress().iterator().next().getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){
                            
                            KuabaFacade.solQuestion = new ArrayList<Question>();
                            
                            Solution A = this.modelFactory.createSolution(this.getNextId()); //i.
                            A.addIncludes(designIdea);                                       //ii.
                            for(Question q : aux)
                                A.addIncludes((Idea)q.getIsSuggestedBy().iterator().next()); //iii.
                            for(Idea idea : A.getIncludes()){   //iv.
                                A = this.varrerSubArvoreAceita(idea, A);
                            }
                            
                            for(Idea solIdea : A.getIncludes()){ //v.
                                for(Question addressed : solIdea.getAddress()){ //1.
                                    if(addressed.getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){ 
                                        KuabaFacade.solQuestion.add(addressed);
                                        Idea domain = (Idea)addressed.getIsSuggestedBy().iterator().next();
                                        A.addIncludes(domain);  
                                    }
                                }
                            }
                            
                            for(Idea solIdea : A.getIncludes()){ //iv 2.
                                if(designIdeas.contains(solIdea)){
                                    int cont2=0;
                                    for(Question q : solIdea.getAddress()){
                                        if(KuabaFacade.solQuestion.contains(q)){
                                            cont2++;
                                        }
                                    }
                                    if(cont2==solIdea.getAddress().size()){
                                        solutionedIdea.add(solIdea);
                                    }
                                }
                            }
                            A.setAccepted(true);
                            solutions.add(A);                        
                        }
                        }
                    }
                }
            }
            
            //3.
            for (Idea designIdea : designIdeas)
            {
                if(!solutionedIdea.contains(designIdea)){   
                    
                    Map<String,List<Question>> questions = new HashMap<String,List<Question>>();
                    for(Question addressed : designIdea.getAddress()){
                        if(!addressed.getHasText().equals(Question.DOMAIN_QUESTION_TEXT_PREFIX)){
                            for(Decision dec : addressed.getHasDecision()){
                                if(dec.getIsAccepted()&&dec.getConcludes().equals(designIdea)){
                                    if(questions.keySet().contains(addressed.getHasText())){
                                        List<Question> aux = questions.get(addressed.getHasText());
                                        aux.add(addressed);
                                        questions.put(addressed.getHasText(), aux);
                                    }
                                    else
                                    {
                                        List<Question> aux = new ArrayList<Question>();
                                        aux.add(addressed);
                                        questions.put(addressed.getHasText(), aux);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    
                    for(String str : questions.keySet()){
                        if(questions.get(str).size()==1)
                        {
                            List<Question> aux = questions.get(str);
                            if(((Idea)aux.get(0).getIsSuggestedBy().iterator().next()).getAddress().iterator().next().getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){
                            
                            KuabaFacade.solQuestion = new ArrayList<Question>();
                            
                            Solution A = this.modelFactory.createSolution(this.getNextId());
                            A.addIncludes(designIdea);
                            A.addIncludes((Idea)questions.get(str).get(0).getIsSuggestedBy().iterator().next());
                            for(Idea i : A.getIncludes()){
                                A = this.varrerSubArvoreAceita(i, A);
                            }
                            
                            for(Idea solIdea : A.getIncludes()){ //vi.
                                for(Question addressed : solIdea.getAddress()){ //1.
                                    if(addressed.getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){ 
                                        KuabaFacade.solQuestion.add(addressed);
                                        Idea domain = (Idea)addressed.getIsSuggestedBy().iterator().next();
                                        A.addIncludes(domain);  
                                    }
                                }
                            }
                            
                            for(Idea solIdea : A.getIncludes()){ //iv 2.
                                if(designIdeas.contains(solIdea)){
                                    int cont2=0;
                                    for(Question q : solIdea.getAddress()){
                                        if(KuabaFacade.solQuestion.contains(q)){
                                            cont2++;
                                        }
                                    }
                                    if(cont2==solIdea.getAddress().size()){
                                        solutionedIdea.add(solIdea);
                                    }
                                }
                            }
                            
                            A.setAccepted(true);
                            solutions.add(A);
                        }
                        }
                    }
                }
            }
            
            //4.
            for(Idea designIdea : designIdeas){
                if(!solutionedIdea.contains(designIdea)){
                    KuabaFacade.solQuestion = new ArrayList<Question>();
                    
                    Solution A = this.modelFactory.createSolution(this.getNextId());
                    A = this.varrerSubArvoreAceita(designIdea, A);
                    
                    for(Idea solIdea : A.getIncludes()){ //iv.
                        for(Question addressed : solIdea.getAddress()){ //1.
                            if(addressed.getHasText().contains(Question.DOMAIN_QUESTION_TEXT_PREFIX)){ 
                                KuabaFacade.solQuestion.add(addressed);
                                Idea domain = (Idea)addressed.getIsSuggestedBy().
                                        iterator().next();
                                A.addIncludes(domain);  
                            }
                        }
                    }
                    
                    for(Idea solIdea : A.getIncludes()){ //iv 2.
                        if(designIdeas.contains(solIdea)){
                            int cont2=0;
                            for(Question q : solIdea.getAddress()){
                                if(KuabaFacade.solQuestion.contains(q)){
                                    cont2++;
                                }
                            }
                            if(cont2==solIdea.getAddress().size()){
                                solutionedIdea.add(solIdea);
                            }
                        }
                    }
                    
                    A.setAccepted(true);
                    solutions.add(A);
                }
            }
            
            
            return solutions;
        }
        
        
        public Solution varrerSubArvoreRejeitada(Idea X, Solution A){
            if(!A.hasIncludes()||!A.getIncludes().contains(X))
                A.addIncludes(X);
            
            if(X.hasSuggests()){
                Question suggested = X.getSuggests().iterator().next();
                for(Decision dec : suggested.getHasDecision()){
                    if(!dec.getIsAccepted()){
                        KuabaFacade.solQuestion.add(X.getSuggests().iterator().next());
                        A = this.varrerSubArvoreRejeitada(dec.getConcludes(), A);
                    }
                        
                }
            }
            return A;
        }
        
        //Funcao varrer subarvore Aceita
        public Solution varrerSubArvoreAceita(Idea X, Solution A){
            A.addIncludes(X);
            if(X.hasSuggests()){
                for(Decision dec: X.getSuggests().iterator().next().getHasDecision()){
                    if(dec.getIsAccepted()){
                        KuabaFacade.solQuestion.add(X.getSuggests().iterator().next());
                        A = varrerSubArvoreAceita(dec.getConcludes(),A);
                    }
                }
            }
            return A;
        }
        
        
        public int getMaxNumAddressedDesign(Idea designIdea){
            if(designIdea.getSuggests().isEmpty())
                return 0;
            Question Q = designIdea.getSuggests().iterator().next();
            Collection<Idea> list = Q.getIsAddressedBy();
            int cont;
            int max=0;
            if(list.size()>1){
                cont=0;
                for(Idea i : list){
                    for(Idea j : list){
                        if(!i.equals(j)&&i.getHasText().equals(j.getHasText()))
                            cont++;
                    }
                }
                if(cont>max)
                    max=cont;
                cont=0;
            }
            return max;
        }
        
        
        //Recuso as solucoes
        
        
        //Esta funcao retorna o numero maximo de ideias de design aceitas por nivel da arvore
        public int getMaxNumAcceptedSuggestedDecisions(Idea idea){
            
            int cont=0;
            Idea temp= null;
            if(!idea.getSuggests().isEmpty()){
                for(Decision dec : idea.getSuggests().iterator().next().getHasDecision()){
                    if(dec.getIsAccepted()){
                       cont++;
                       temp=dec.getConcludes();
                    }
                }
            }
            if(cont>=2)
                return cont;
            if(temp!=null)
                return getMaxNumAcceptedSuggestedDecisions(temp);
            else
                return 0;
        }
        
        //Esta funcao retorna a lista de ideias de design aceitas quando mais de uma e aceita em uma mesma altura
        //da arvore de dr
        public List<Idea> getAcceptedSuggestedDecisions(Idea idea,int num){
            
            Idea temp= null;
            List<Idea> list = new ArrayList<Idea>();
            
            if(!idea.getSuggests().isEmpty()){
                for(Decision dec : idea.getSuggests().iterator().next().getHasDecision()){
                    if(dec.getIsAccepted()){
                        list.add(dec.getConcludes());
                        temp = dec.getConcludes();
                    }
                }
            }
            else
                return null;
            
            if(list.size()>=2)
                return list;
            if(list.isEmpty())
                return null;
            else
                return this.getAcceptedSuggestedDecisions(temp, num);
        }
        
        
        //Adiciona as ideias de dominio na lista de ideias que vao na solucao
        public void getSolutionIdeas(Idea designIdea)
        {
            Idea domain = KuabaHelper.getDomainIdea(designIdea);
            if(domain==null)
                domain = KuabaHelper.upperDomainIdea(designIdea);
                
            //Verifico se ela ja nao esta na lista definitiva ou na lista atual
            if(!KuabaFacade.solutionedIdeas.contains(domain.getHasText())&&!KuabaFacade.actualSolutionIdeas.contains(domain.getHasText())){
                
                
                
                //Tratamento especial para casos como ssociacao
                int num = this.getMaxNumAcceptedSuggestedDecisions(domain); 
                if(num>=2){
//    if(isMultiLevel){
                    if(!KuabaFacade.actualSolutionIdeas.contains(domain.getHasText()))
                        KuabaFacade.actualSolutionIdeas.add(domain.getHasText());

                    List<Idea> multiAccepted = this.getAcceptedSuggestedDecisions(domain,num);
                    
                    for(Idea i : multiAccepted){
                        Question Q = i.getSuggests().iterator().next();
                        for(Decision dec: Q.getHasDecision())
                            if(dec.getIsAccepted())
                                this.getSolutionIdeas(dec.getConcludes());
                    }
                    
                    
                    /*Idea acceptedDesign = KuabaHelper.getAcceptedDesignIdea(domain);
        
                                        
                    Question Q = acceptedDesign.getSuggests().iterator().next();
                    for(Decision dec : Q.getHasDecision()){
                        if(dec.getIsAccepted()){
                            Question Q2 = dec.getConcludes().getSuggests().iterator().next();
                            for(Decision dec2 : Q2.getHasDecision())
                                if(dec2.getIsAccepted())
                                    this.getSolutionIdeas(dec2.getConcludes());
                        }
                    }*/
                }
                
                //Se a ideia de design sugerir outras questoes
                else if(designIdea.hasSuggests())
                {
                    if(!KuabaFacade.actualSolutionIdeas.contains(domain.getHasText()))            
                        KuabaFacade.actualSolutionIdeas.add(domain.getHasText());
                    Question suggested = designIdea.getSuggests().iterator().next();
                        //Senao, apenas  verifico pras ideias 
                        for(Decision d : suggested.getHasDecision())
                            if(d.getIsAccepted())
                                this.getSolutionIdeas(d.getConcludes());
                
                    
                }
                else
                {
                    if(!KuabaFacade.actualSolutionIdeas.contains(domain.getHasText()))
                        KuabaFacade.actualSolutionIdeas.add(domain.getHasText());
                    String txt = domain.getHasText();
                    
                    for(Question q: designIdea.getAddress())
                    {
                        if(!q.getHasText().equals("How to Model "+domain.getHasText()+"?"))
                        {
                            for(Decision d : q.getHasDecision())
                                if(d.getIsAccepted()&&!KuabaFacade.solutionedIdeas.contains(d.getConcludes().getHasText()))
                                    this.getSolutionIdeas((Idea)q.getIsSuggestedBy().iterator().next());
                        }
                    }
                }
            }
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

        //Calcular as decisoes para as sub arvore da ideia de dominio
        public void domainIdeaSubTreeDecision(Idea ideia,boolean isAccepted)
        {
            for(Question q: ideia.getSuggests())
            {
                for(Idea d : q.getIsAddressedBy())
                {
                    if(!d.hasIsConcludedBy())
                        this.makeDecision(q, d, isAccepted);
                    else
                    {
                        for(Decision dec : d.getIsConcludedBy())
                            dec.setIsAccepted(isAccepted);
                    }
                    domainIdeaSubTreeDecision(d,isAccepted);
                }
            }
        }
        
        public Question copyQuestion(Question question)
        {
            Question newQuestion = this.modelFactory.createQuestion(this.getNextId());
            newQuestion.setHasText(question.getHasText());
            return newQuestion;
        }
        
        public Idea copyIdea(Idea idea)
        {
            Idea newIdea = this.modelFactory.createIdea(this.getNextId());
            newIdea.setHasText(idea.getHasText());
            return newIdea;
        }
        
        public Idea copyIdea(Idea originalIdea,Question addQuestion,boolean decision)
        {
            Idea newIdea = this.modelFactory.createIdea(this.getNextId());
            newIdea.setHasText(originalIdea.getHasText());
            //Adiciona à lista de ideias enderecadas da questao
            addQuestion.addIsAddressedBy(newIdea);
            this.makeDecision(addQuestion, newIdea, decision);
            if(decision)
                this.session.unitOfWork().addConsideredIdea(newIdea);
            else
                this.session.unitOfWork().addRejectedIdea(newIdea);
            
            return newIdea;
        }
        
        
        
          //Copia as questoes associadas a uma ideia e depois copia as ideias associadas
        public void copyQuestions(Idea rejIdea,Idea accIdea,int limit){
            
            if(limit>0){
                System.out.println("Question: "+Integer.toString(limit));
                //Se a ideia de design aceita nao sugere nenhuma questao, copia a da ideia rejeitada
                if(!accIdea.hasSuggests())
                {
                    //Map<Question,Question> addedReject = new HashMap<Question,Question>();
                    //Para cada questao sugerida pela ideia rejeitada
                    limit--;
                    for(Question rejQ : rejIdea.getSuggests())
                    {
                        //Copia a questao sugerida e em seguida copia as ideias
                        Question newQuestion = KuabaSubsystem.facade.copyQuestion(rejQ);
                        //Adiciona à lista de ideias enderecadas da questao
                        accIdea.addSuggests(newQuestion);
                        //Inserir em profundidade
                        this.copyIdeas(rejQ,newQuestion,limit);
                    }
                }
                //Se a ideia de design aceita sugere alguma questao...
                else{
                    List<String> accSuggests = new ArrayList<String>();
                    //leio todas as questoes sugeridas
                    for(Question accQ : accIdea.getSuggests())
                        accSuggests.add(accQ.getHasText());
                    
                    limit--;
                    //Se alguma delas for igual a uma ideia rejeitada
                    for(Question rejQ : rejIdea.getSuggests())
                    {
                        //Nao copio a questao, apenas vou copiar as ideias dentro delas
                        if(accSuggests.contains(rejQ.getHasText()))
                        {
                            for(Question accQ : accIdea.getSuggests())
                                if(accQ.getHasText().equals(rejQ.getHasText()))
                                {
                                    
                                    this.copyIdeas(rejQ,accQ,limit);
                                    break;
                                }
                            //break;
                        }
                        else{
                            Question newQuestion = KuabaSubsystem.facade.copyQuestion(rejQ);
                            //Adiciona à lista de ideias enderecadas da questao
                            accIdea.addSuggests(newQuestion);
                            limit--;
                            this.copyIdeas(rejQ, newQuestion, limit);
                        }
                    }
                }
            
            }
        }
        
        //Copia as questoes associadas a uma ideia e depois copia as ideias associadas
        public void copyIdeas(Question rejQuestion,Question accQuestion,int limit)
        {
            //Se estou no ultimo nivel, entao devo copiar as associacoes das questoes, e nao as ideias em si
            if(limit==0)
            {
                //Se a questao possui decisoes que devem ser rejeitadas
                if(rejQuestion.hasHasDecision()){
                    //rejeito cada uma das decisoes
                    for(Decision dec : rejQuestion.getHasDecision()){
                        dec.setIsAccepted(false);
                    }
                }
                //se nao possui decisoes, crio as decisoes e configuro como falsas
                else{
                    for(Idea idea : rejQuestion.getIsAddressedBy()){
                        KuabaSubsystem.facade.makeDecision(rejQuestion, idea, false);
                    }
                }
                
                //Se as questoes das ideias aceitas tem decisoes, configuro cada uma como aceita
                boolean isAddressed = false;
                Idea addIdea = null;
                for(Idea rejIdea : rejQuestion.getIsAddressedBy()){
                   for(Idea accIdea : accQuestion.getIsAddressedBy())
                   {
                       if(accIdea.getId().equals(rejIdea.getId())){
                           isAddressed=true;
                           break;
                       }
                   }
                   if(!isAddressed)
                       accQuestion.addIsAddressedBy(rejIdea);
                   isAddressed=false;
                }
                boolean isDecided = false;
                for(Idea idea : accQuestion.getIsAddressedBy()){
                    for(Decision dec : accQuestion.getHasDecision()){
                        if(dec.getConcludes().getId().equals(idea.getId())){
                            dec.setIsAccepted(true);
                            isDecided = true;
                        }
                    }
                    if(!isDecided)
                        KuabaSubsystem.facade.makeDecision(accQuestion, idea, true);
                }
                
                
            }
            
            else if(limit>0){
                System.out.println("Idea: "+Integer.toString(limit));
           
                //Se a ideia de design aceita nao sugere nenhuma questao, copia a da ideia rejeitada
                if(!accQuestion.hasIsAddressedBy())
                {
                    limit--;
                    //Para cada questao sugerida pela ideia rejeitada
                    for(Idea rejIdea : rejQuestion.getIsAddressedBy())
                    {
                        //recusa as ideia para rejQuestion
                        for(Decision dec : rejIdea.getIsConcludedBy())
                            dec.setIsAccepted(false);
                        //Copia a questao sugerida e em seguida copia as ideias
                        Idea newIdea = KuabaSubsystem.facade.copyIdea(rejIdea,accQuestion,true);    
                        //Aceita a ideia para as accQuestions
                        KuabaSubsystem.facade.makeDecision(accQuestion, newIdea, true);
                        this.copyQuestions(rejIdea,newIdea,limit);
                        
                    }
                }
                //Se a ideia de design aceita sugere alguma questao...
                else{
                    Map<String,String> accAddressed = new HashMap<String,String>();
                    //leio todas as questoes sugeridas
                    for(Idea accIdea : accQuestion.getIsAddressedBy())
                        accAddressed.put(accIdea.getId(),accIdea.getHasText());
                    
                    limit--;
                    //Se alguma delas for igual a uma ideia rejeitada
                    
                    //Recuso todas as ideias rejeitadas
                    for(Idea rejIdea : rejQuestion.getIsAddressedBy())
                    {
                        //recusa as ideia para rejQuestion
                        for(Decision dec : rejIdea.getIsConcludedBy())
                            dec.setIsAccepted(false);
                    } 
                    //Aceito todas as ideias sugeridas
                    for(Idea accIdea : accQuestion.getIsAddressedBy())    
                    {
                        if(!accIdea.hasIsConcludedBy())
                            KuabaSubsystem.facade.makeDecision(accQuestion, accIdea, true);
                        else
                            for(Decision dec : accIdea.getIsConcludedBy())
                                dec.setIsAccepted(true);
                    }
                 
                    boolean isEqual = false;
                    //Para cada ideia rejeitada e aceita que nao tenha sido copiada...
                    for(Idea rejIdea : rejQuestion.getIsAddressedBy())
                    {
                        
                        for(Idea accIdea : accQuestion.getIsAddressedBy())
                        {
                            if(accIdea.getHasText().equals(rejIdea.getHasText())){
                                isEqual=true;
                                this.copyQuestions(rejIdea, accIdea, limit);
                                break;
                            }
                            
                        }
                        if(!isEqual)
                        {
                            Idea newIdea = KuabaSubsystem.facade.copyIdea(rejIdea,accQuestion,true);
                            this.copyQuestions(rejIdea, newIdea, limit);
                        }
                        isEqual=false;
                    }
                    
                }
            }
        }
        
        //Retorna uma ideia de design cujo nome foi passado como parametro e pertencente a ideia de dominio passada
        public Idea getDesignIdea(Idea domainIdea, String designIdeaText)
        {
            for(Question questions : domainIdea.getSuggests()){
                for(Idea ideas : questions.getIsAddressedBy()){
                    if(designIdeaText.equals(ideas.getHasText()))
                        return ideas;
                }
            }
            return null;
            
        }
        
        public void createObservers(RefObject accValue,String propertyName){
            PropertyChangeEvent nameEvt = new PropertyChangeEvent(accValue, "name", "", accValue.refGetValue("name"));			
            PropertyChangeListener listener = KuabaSubsystem.eventPump.getObserver(accValue);
            if(listener == null){
                listener = this.createListener(accValue);
                if (listener == null)
                    return;
                KuabaSubsystem.eventPump.addModelElementObserver(listener, accValue);
            }
            listener.propertyChange(nameEvt);
            Reference ref = MofHelper.getReference(propertyName, (MofClass)accValue.refMetaObject());
            if(!(ref == null)){
                String otherSide = MofHelper.getExposedEndName(ref);
                try{
                    PropertyChangeEvent referenceEvt = new PropertyChangeEvent(accValue, otherSide, null, accValue.refGetValue(otherSide));
                    listener.propertyChange(referenceEvt);
                }catch (Exception e) {
                // TODO: handle exception
                }
            }			
        
        }
        
        protected PropertyChangeListener createListener(RefObject subject){
		String metaClassName =
			(String)subject.refMetaObject().refGetValue("name");
		ModelElementObserver subjectListener = null;
		String observerName = ModelElementObserver.observersPack + "." + metaClassName + "Observer";
			try {
				subjectListener = 
					(ModelElementObserver)Class.forName(observerName).newInstance();
			} catch (ClassNotFoundException e) {				
				System.err.println("Observer " + observerName + " not found!");
			} catch (InstantiationException e) {				
				e.printStackTrace();
			} catch (IllegalAccessException e) {				
				e.printStackTrace();
			}
		if(subjectListener != null)	
			return subjectListener.applyFilter(subject);
		else
			return null;
		
	}
        
        
        //Esta funcao rejeita a sub-arvore de uma ideia de design do tipo atributo/metodo
        //e aceita a ideia de design de outra sub-arvore do tipo atributo/metodo
        public void rejectAndAcceptOwnership(Idea rejectedIdea,Idea acceptedIdea){
            //Primeiro, recuso a ideia de design
            rejectedIdea.getIsConcludedBy().iterator().next().setIsAccepted(false);
            acceptedIdea.getIsConcludedBy().iterator().next().setIsAccepted(true);       
            //Depois verifico, para a ideia de design que foi recusada, qual e a classe que e "owner"
            Idea owner = null;
            for(Question q: rejectedIdea.getSuggests()){
                for(Decision d : q.getHasDecision()){
                    //Quando encontrar a classe, recuso a ideia de design e seguro esta classe owner
                    if(d.getIsAccepted()){
                        owner = d.getConcludes();
                        d.setIsAccepted(false);
                    }
                }
            }
            
            if(owner == null)
                return;
            
            for(Question q: acceptedIdea.getSuggests())
                for(Decision d : q.getHasDecision())
                        if(d.getConcludes().equals(owner))
                            d.setIsAccepted(true);
            
            //Faz a pergunta para a classe rejeitada
            ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{rejectedIdea});
            controller.render();
            //Faz a pergunta para a classe aceita
            String text = "Why make " + KuabaHelper.getDomainIdea(owner).getHasText() + " owner of " + KuabaHelper.getDomainIdea(acceptedIdea).getHasText() + "?";
            controller = new InFavorArgumentController(new Idea[]{acceptedIdea},null,text,acceptedIdea.getSuggests().iterator().next());
            controller.render();
            
        }
        //Se retornar true, a ideia de design ja existia
        //Se retornar false, a ideia de design nao existia
        public boolean reAcceptDomainIdea(Idea idea,String designIdeaText){
            boolean isAccepted=false;
            for(Idea d :KuabaSubsystem.getSession().unitOfWork().getRejectedIdeas())
                if(d.equals(idea))
                    isAccepted=true;
            
            //Se deve aceitar a ideia de dominio novamente
            if(isAccepted){
                KuabaSubsystem.getSession().unitOfWork().removeRejectedIdea(idea.getId());
                KuabaSubsystem.getSession().unitOfWork().addConsideredIdea(idea);
                //Busco se existe ideia de design para a ideia de dominio passada
                for(Question q : idea.getSuggests()){
                   for(Idea d : q.getIsAddressedBy()){
                       //Caso encontre, chamo a funcao que vai aceitar as ideias derivadas
                       if(d.getHasText().equals(designIdeaText)){
                           for(Decision dec: d.getIsConcludedBy())
                               dec.setIsAccepted(true);
                           domainIdeaSubTreeDecision(d,true,1);
                           return true;
                        }
                   }
                }
                //Se chegou aqui, entao nao encontrou a ideia de design correspondente, neste caso
                //Devo criar uma nova e refazer a subarvore
                //Necessario nao chamar outra funcao porque as outras so criam par ideia de dominio- ideia de design
                this.createIdea(idea.getSuggests().iterator().next(),"Attribute","owner?");
                
            }
            return false;
        }
        
        public Idea createIdea(Question addressedQuestion,String designText,String questionText){
            
            Idea idea = this.modelFactory.createIdea(this.getNextId());
            idea.setHasText(designText);
            idea.addAddress(addressedQuestion);
            Question q = this.modelFactory.createQuestion(this.getNextId());
            q.setHasText(questionText);
            idea.addSuggests(q);
            return idea;
        }
        
        //Calcular as decisoes para as sub arvore da ideia de dominio
        public void domainIdeaSubTreeDecision(Idea ideia,boolean isAccepted,int limit){
            if(limit>0){
                for(Question q: ideia.getSuggests())
                {
                    for(Idea d : q.getIsAddressedBy())
                    {
                        if(!d.hasIsConcludedBy())
                            this.makeDecision(q, d, isAccepted);
                        else
                        {
                            for(Decision dec : d.getIsConcludedBy())
                                dec.setIsAccepted(isAccepted);
                        }
                        domainIdeaSubTreeDecision(d,isAccepted);
                    }
                }
            
            }
        }
        
        //Calcular as decisoes para as sub arvore da ideia de dominio
        //Quando se chegar no limite, significa que fechou um ciclo, porque possui uma relacionamento entre as ideias de dominio
        public void domainIdeaSubTreeCycleDecision(Idea ideia,boolean isAccepted,int limit){
            
            if(limit==0){
                for(Question q :ideia.getSuggests()){
                    for(Decision d : q.getHasDecision()){
                        if(d.getIsAccepted()){
                            System.out.println("ideia de dominio: "+ ideia.getHasText());
                            
                            
                            String texto = ideia.getHasText().equals("Generalization")? " generalization of " : " owner of ";
                            
                            String text = "Why not make " + KuabaHelper.getDomainIdea(d.getConcludes()).getHasText() + texto/*" owner of "*/+ KuabaHelper.getDomainIdea(ideia).getHasText() +"?";
                            ArgumentController controller = new ObjectsToArgumentController(null,new Idea[]{ideia},text,q);
                            controller.render();
                            d.setIsAccepted(isAccepted);
                        }
                    }
                }
            }
            if(limit>0){
                limit--;
                for(Question q: ideia.getSuggests())
                {
                    for(Idea d : q.getIsAddressedBy())
                    {
                        if(!d.hasIsConcludedBy())
                            this.makeDecision(q, d, isAccepted);
                        else
                        {
                            for(Decision dec : d.getIsConcludedBy())
                                dec.setIsAccepted(isAccepted);
                        }
                            domainIdeaSubTreeCycleDecision(d,isAccepted,limit);
                    }   
                }
            }
        }
        
        //Retorna a questao levantada por uma ideia de design, se nao  tiver nenhuma, cria uma com o texto passado
        public Question obtainQuestion(Idea designIdea, String text){
            
            for(Question q: designIdea.getSuggests()){
                if(q.getHasText().equals(text))
                    return q;
            }
            //Se chegou ate aqui, nao havia a questao sugerida, entao cria uma nova
            Question question = this.modelFactory.createQuestion(this.getNextId());
            question.setHasText(text);
            designIdea.addSuggests(question);
            return question;
        }
        

        public Idea createIdea(Question addressedBy,String text){
            
            Idea idea = this.modelFactory.createIdea(this.getNextId());
            idea.setHasText(text);
            idea.addAddress(addressedBy);
            addressedBy.addIsAddressedBy(idea);
            return idea;
        }
        
        
        public boolean hasAllAssociationEnds(Idea designIdea){
            
            if(designIdea.getSuggests() == null)
                return false;
            
            if(designIdea.getSuggests().isEmpty())
                return false;
            Question suggested = designIdea.getSuggests().iterator().next();
            if(suggested.getIsAddressedBy() == null)
                return false;
            if(suggested.getIsAddressedBy().isEmpty())
                return false;
            if(suggested.getIsAddressedBy().size()==1)
                return false;
            return true;
        }

    public Idea createDesignIdea(Question questao, String designIdeaName) {
        
         Idea newIdea = this.modelFactory.createIdea(this.getNextId());
         newIdea.setHasText(designIdeaName);
         questao.addIsAddressedBy(newIdea);
         this.makeDecision(questao, newIdea, true);
         return newIdea;
    }
        
    
        
}
