package br.ucam.kuabaSubsystem.repositories;

import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import br.ucam.kuabaSubsystem.kuabaModel.Artifact;
import br.ucam.kuabaSubsystem.kuabaModel.AtomicArtifact;
import br.ucam.kuabaSubsystem.kuabaModel.CompositeArtifact;
import br.ucam.kuabaSubsystem.kuabaModel.ExpectedDuration;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;
import br.ucam.kuabaSubsystem.kuabaModel.Method;
import br.ucam.kuabaSubsystem.kuabaModel.Person;
import br.ucam.kuabaSubsystem.kuabaModel.RelatedType;
import br.ucam.kuabaSubsystem.kuabaModel.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;

import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaElement;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaEventPump;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaListener;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;
//import edu.stanford.smi.protegex.owl.model.OWLModel;
//import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
//import edu.stanford.smi.protegex.owl.model.RDFIndividual;
//import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

public class OwlRepository implements KuabaListener, KuabaRepository {
	
//	private OWLModel model;
	private String url;
	private KuabaModelFactory factory;
	private HashMap<String, List<Idea>> ideaCache = 
		new HashMap<String, List<Idea>>();
	
	public OwlRepository() {
		super();		
	}
	
//	public OwlRepository(OWLModel model) {
//		this();
//		this.model = model;		
//	}

//	@Override
//	public Object getModel() {		
//		return this.model;
//	}

//	@Override
	public boolean save() {
		return false;
	}

//	@Override
//	public KuabaModelFactory getModelFactory() {
//		if(this.factory == null)
//			this.factory = new MyFactory(this.model); 
//		return this.factory;
//	}

	@Override
	public String getUrl() {
		return this.url;
	}

//	@Override
	public void setUrl(String url) {
		
		this.url = url;
	}
	
	public<T> List<T> findAllKuabaElement(Class<T> _class){
		List<T> result = new ArrayList<T>();		
//		OWLNamedClass argumentClass = this.model.getOWLNamedClass("k:" + _class.getSimpleName());
//		Collection<RDFIndividual> instances = argumentClass.getInstances(true);		
//		for (RDFIndividual individual : instances) {
//			result.add((T)individual.as(_class));
//		}		
		return result;
	}
	
//	@Override
//	public List<Question> findAllQuestions() {		
//		return findAllKuabaElement(Question.class);
//	}
//
//	@Override
//	public List<Question> findQuestions(FormalModel formalModel) {
//		List<Question> allQuestions = findAllQuestions();
//		List<Question> result = new ArrayList<Question>();
//		for (Question question : allQuestions) {
//			if(formalModel.equals(question.getIsDefinedBy()))
//				result.add(question);
//		}
//		return result;
//	}
//
//	@Override
//	public List<Question> findQuestionByText(String text) {
//		
//		List<Question> allQuestions = findAllQuestions();
//		List<Question> result = new ArrayList<Question>();
//		for (Question question : allQuestions) {
//			if(text.equals(question.getHasText()))
//				result.add(question);
//		}
//		return result;
//		
//	}
//
//	@Override
//	public List<Idea> findAcceptedIdeas(Question question) {
//		List<Idea> acceptedIdeas = new ArrayList<Idea>();
//		Collection<Decision> decisions = question.getHasDecision();
//		for (Decision decision : decisions) {
//			if(decision.getIsAccepted())
//				acceptedIdeas.add(decision.getConcludes());
//		}
//		return acceptedIdeas;
//	}
//
//	@Override
//	public Question findFirstQuestionByText(String text) {
//		
//		List<Question> allQuestions = findAllQuestions();		
//		for (Question question : allQuestions) {
//			if(text.equals(question.getHasText()))
//				return question;
//		}
//		return null;
//	}
//
//	@Override
//	public Idea findFirstIdeaByText(Question addressedQuestion, String ideaText) {
//		Collection<Idea> ideas = addressedQuestion.getIsAddressedBy();
//		for (Idea idea : ideas) {
//			if(ideaText.equals(idea.getHasText()))
//				return idea;
//		}
//		return null;
//	}
//
//	@Override
//	public Question findFirstQuestionByText(Idea addressIdea,
//			String questionText) {
//		Collection<Question> questions = addressIdea.getSuggests();
//		for (Question question : questions) {
//			if(questionText.equals(question.getHasText()))
//				return question;
//		}
//		return null;
//	}
//
//	@Override
//	public List<Idea> findIdeaByText(String ideaText) {
//		
//		List<Idea> allIdeas = findAllIdeas();
//		List<Idea> result = new ArrayList<Idea>();
//		for (Idea idea : allIdeas) {
//			if(ideaText.equals(idea.getHasText()))
//				result.add(idea);
//		}
//		return result;
//	}
//
//	@Override
//	public List<Idea> findAllIdeas() {		
//		return this.findAllKuabaElement(Idea.class);
//		
//	}
//
//	@Override
//	public List<Idea> findDesignedDomainIdeas(String domainIdeaText,
//			String designIdeaText) {
//		List<Idea> result = new ArrayList<Idea>();
//		List<Idea> domainIdeas = this.findIdeaByText(domainIdeaText);
//		
//		for (Idea idea : domainIdeas) {
//			Question howModel = (Question)idea.getSuggests().iterator().next();			
//			Collection<Idea> ideas = this.findAcceptedIdeas(howModel);
//			for (Idea addressedIdea : ideas) {
//				if(designIdeaText.equals(addressedIdea.getHasText()))
//					result.add(idea);
//			}
//		}
//		return result;
//		
//	}

//	@Override
//	public void dispose() {
//		this.model.dispose();
//		
//	}
//	public List<Idea> findDomainIdeas(){
//		List<Idea> allIdeas = this.findAllIdeas();
//		List<Idea> result = new ArrayList<Idea>();		
//		for (Idea idea : allIdeas){
//			if (idea.getIsDefinedBy() == null){
//				if(idea.getId().contains("_")){
//					String elementId = "_" + idea.getId().split("_")[1];
//					List<Idea> ideaList = this.ideaCache.get(elementId);
//					if (ideaList == null){
//						ideaList = new ArrayList<Idea>();
//						this.ideaCache.put(elementId, ideaList);
//					}
//					ideaList.add(idea);
//					result.add(idea);
//				}
//			}
//		}
//		return result;
//		
//	}
//
//	@Override
//	public List<Idea> findDomainIdeasWhereIdLike(String idPeace) {
//		if(!idPeace.contains("_"))
//			idPeace = "_" + idPeace;		
//		List<Idea> allIdeas = this.ideaCache.get(idPeace);
//		if (allIdeas != null) {		
//			if(this.ideaCache.isEmpty())
//				allIdeas = this.findDomainIdeas();
//				
//			List<Idea> result = new ArrayList<Idea>();
//			for (Idea idea : allIdeas){			
//				if (idea.getId().contains(idPeace))
//					result.add(idea);
//			}
//			
//			return result;
//		}
//		return new ArrayList<Idea>();
//	}
//
//	@Override
//	public Decision findDecision(Question question, Idea idea) {
//		
//		Collection<Decision> ideaDecisionList = idea.getIsConcludedBy();
//		for (Decision decision : ideaDecisionList) {
//			if(question.getHasDecision().contains(decision))
//				return decision;
//		}
//		return null;
//	}
//
//	@Override
//	public List<Argument> findAllArguments() {		
//		return this.findAllKuabaElement(Argument.class);	
//	}
//
//	@Override
//	public List<Idea> findRejectedIdeas(Question question) {
//		List<Idea> acceptedIdeas = new ArrayList<Idea>();
//		Collection<Decision> decisions = question.getHasDecision();
//		for (Decision decision : decisions)
//			if(!decision.getIsAccepted())
//				acceptedIdeas.add(decision.getConcludes());
//		
//		return acceptedIdeas;
//	}
//
//	@Override
//	public Decision findMostRecentDecision(Question question) {
//		Collection<Decision> decisions = question.getHasDecision();
//		Decision mostRecent = null;
//		if(!decisions.isEmpty()){
//			mostRecent = decisions.iterator().next();
//			for (Decision decision : decisions) 
//				if(decision.getHasDate().after(mostRecent.getHasDate()))
//					mostRecent = decision;
//			
//		}			
//		return mostRecent;
//	}
//
//	@Override
//	public List<Decision> findAllDecisions() {	
//		return this.findAllKuabaElement(Decision.class);
//	}
//
//	@Override
	public void newElementCreated(KuabaElement kuabaElement) {
//		if(kuabaElement.getId().split("_").length < 2)
//			return;
//		if(this.ideaCache.isEmpty())
//			this.findDomainIdeas();		
//		String elementId = "_"+kuabaElement.getId().split("_")[1];
//		Idea idea = (Idea)kuabaElement;
//		List<Idea> ideaList = this.ideaCache.get(elementId);
//		if(ideaList == null){
//			ideaList = new ArrayList<Idea>();
//			this.ideaCache.put(elementId, ideaList);
//		}
//		ideaList.add(idea);
//		
//		
            throw new UnsupportedOperationException("Not supported yet.");
	}

    public Question getQuestion(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Question> getAllQuestions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Question> getQuestions(FormalModel formalModel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Question> getQuestionByText(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Question getFirstQuestionByText(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Question getFirstQuestionByText(Idea addressIdea, String questionText) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Idea getIdea(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Idea getFirstIdeaByText(Question addressedQuestion, String ideaText) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Idea> getIdeaByText(String ideaText) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Idea> getDesignedDomainIdeas(String domainIdeaText, String designIdeaText) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Idea> getAllIdeas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Idea> getAcceptedIdeas(Question question) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Idea> getDomainIdeas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Idea> getDomainIdeasWhereIdLike(String idPeace) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Idea> getRejectedIdeas(Question question) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Argument getArgument(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Argument> getAllArguments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Decision getDecision(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Decision> getAllDecisions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Decision getDecision(Question question, Idea idea) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Decision getMostRecentDecision(Question question) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Role getRole(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Role> getAllRoles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Artifact getArtifact(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Artifact> getAllArtifacts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AtomicArtifact getAtomicArtifact(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<AtomicArtifact> getAllAtomicArtifacts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CompositeArtifact getCompositeArtifact(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<CompositeArtifact> getAllCompositeArtifacts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ExpectedDuration getExpectedDuration(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ExpectedDuration> getAllExpectedDurations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RelatedType getRelatedType(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<RelatedType> getAllRelatedTypes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FormalModel getFormalModel(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<FormalModel> getAllFormalModels() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Justification getJustification(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Justification> getAllJustifications() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Person getPerson(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Person> getAllPeople() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Activity getActivity(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Activity> getAllActivities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Method getMethod(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Method> getAllMethodInstances() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<String> getDataPropertyValues(String propertyName, String individualId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addDataPropertyValue(String propertyName, String individualId, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDataPropertyValues(String propertyName, String individualId, Collection<String> value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeDataPropertyValue(String propertyName, String individualId, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection getObjectPropertyValues(String propertyName, String individualId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addObjectPropertyValue(String propertyName, String individualId, KuabaElement value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setObjectPropertyValues(String propertyName, String individualId, Collection values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeObjectPropertyValue(String propertyName, String individualId, KuabaElement value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasProperty(String propertyName, String individualId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rename(String oldID, String newID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addComment(String string, String individualId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeComment(String string, String individualId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<String> getComments(String individualId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public KuabaRepository copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public KuabaRepository copy(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getModel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public KuabaModelFactory getModelFactory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeIndividual(String individualId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
