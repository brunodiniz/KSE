package br.ucam.kuabaSubsystem.repositories;

import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import java.util.List;

import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Artifact;
import br.ucam.kuabaSubsystem.kuabaModel.AtomicArtifact;
import br.ucam.kuabaSubsystem.kuabaModel.CompositeArtifact;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.ExpectedDuration;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaElement;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Method;
import br.ucam.kuabaSubsystem.kuabaModel.Person;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.RelatedType;
import br.ucam.kuabaSubsystem.kuabaModel.Role;
import java.util.Collection;

public interface KuabaRepository {
	public Object getModel();
	public String getUrl();
//	public void setUrl(String url);
	public KuabaModelFactory getModelFactory();
//	public boolean save();
        
        /**-------------Operations related to {@link Question} classes-----------*/
        
	/**
	 * Gets a Question object by id. 
	 * @param id: the id of the Question to be found.
	 * @return null if no Question was found.
	 * @return The Question that has the id.
	 */
	public Question getQuestion(String id);
        
        /**
	 * 
	 * @return all Question instances.
	 */
	public List<Question> getAllQuestions();
	public List<Question> getQuestions(FormalModel formalModel);
	public List<Question> getQuestionByText(String text);	
	public Question getFirstQuestionByText(String text);	
	public Question getFirstQuestionByText(Idea addressIdea, String questionText);
	
        
        /**-------------Operations related to {@link Idea} classes------------------*/
        
        /**
	 * This method find an Idea object by your id.
	 * @param id: The id of the Idea object to be found.
	 * @return null if the Idea object with the id was not found.
	 * Or.
	 * @return the Idea object that has the id passed on id parameter 	 
	 * */
	public Idea getIdea(String id);
        
	public Idea getFirstIdeaByText(Question addressedQuestion, String ideaText);    
	public List<Idea> getIdeaByText(String ideaText);
	public List<Idea> getDesignedDomainIdeas(String domainIdeaText, String designIdeaText);
        
        /**
	 * 
	 * @return all Idea objects in the current individuals file being used. 
	 */
	public List<Idea> getAllIdeas();
	public List<Idea> getAcceptedIdeas(Question question);
	
        public List<Idea> getDomainIdeas();
        List<Idea> getDomainIdeasWhereIdLike(String idPeace);
	public List<Idea> getRejectedIdeas(Question question);
        
        
	/**-------------Operations related to {@link Argument} classes----------- */
        
        /**
	 * Find an Argument by id
	 * @param id: the id of the Argument to be found.
	 * @return null if the Argument with the specified id does not exists.
	 * @return the Argument object that has the specified id.
	 */
	public Argument getArgument(String id);
        
	/**
	 * 
	 * @return all Argument instances.
	 */
	public List<Argument> getAllArguments();
        
        
        
        /**------------Operations related to {@link Decision} classes-------------*/	
        
        /**
	 * Finds a Decision object by id.
	 * @param id the id of the Decision.
	 * @return the Decision object.
	 */
	public Decision getDecision(String id);
                
        /**
	 * 
	 * @return a list containing all Decision instances.
	 */
	public List<Decision> getAllDecisions();
        
        public Decision getDecision(Question question, Idea idea); 
	public Decision getMostRecentDecision(Question question);
	
        
        /**----------Operations related to {@link Role} classes--------------------*/
        /**
	 * Gets a Role object that has the specified id.
	 * 
	 * @param id: the id of the Role to be found
	 * @return the Role object that has the specified id.
	 * @return null if the Role Object with the id does not exists.
	 */
	public Role getRole(String id);
	
	/**
	 * 
	 * @return a list containing all Role instances. 
	 */
	public List<Role> getAllRoles();
        
        
        /**------------Operations related to {@link Artifact} classes-------*/
        /**
	 * Finds an Artifact object by id.
	 * @param id the id of the Artifact.
	 * @return the Artifact object.
	 */
	public Artifact getArtifact(String id);
	
	/**
	 * 
	 * @return a list containing all Artifact instances.
	 */
	public List<Artifact> getAllArtifacts();
           
        
        /**------------Operations related to {@link AtomicArtifact} classes-------*/
        /**
	 * Finds an AtomicArtifact object by id.
	 * @param id the id of the AtomicArtifact.
	 * @return the AtomicArtifact object.
	 */
	public AtomicArtifact getAtomicArtifact(String id);
	
	/**
	 * 
	 * @return a list containing all AtomicArtifact instances.
	 */
	public List<AtomicArtifact> getAllAtomicArtifacts();
        
        
        /**---------Operations related to {@link CompositeArtifact} classes-------*/
        /**
	 * Finds an CompositeArtifact object by id.
	 * @param id the id of the CompositeArtifact.
	 * @return the CompositeArtifact object.
	 */
	public CompositeArtifact getCompositeArtifact(String id);
	
	/**
	 * 
	 * @return a list containing all CompositeArtifact instances.
	 */
	public List<CompositeArtifact> getAllCompositeArtifacts();
        
        
        /**----------- Operations related to {@link ExpectedDuration} classes------*/
        /**
	 * Finds an ExpectedDuration object by id.
	 * @param id the id of the ExpectedDuration.
	 * @return the ExpectedDuration object.
	 */
	public ExpectedDuration getExpectedDuration(String id);
	
	/**
	 * 
	 * @return a list containing all ExpectedDuration instances.
	 */
	public List<ExpectedDuration> getAllExpectedDurations();
        
        
        /**----- Operations related to {@link RelatedType} classes-----------------*/	
        /**
	 * Finds a RelatedType object by id.
	 * @param id the id of the RelatedType.
	 * @return the RelatedType object.
	 */
	public RelatedType getRelatedType(String id);
	
	/**
	 * 
	 * @return a list containing all RelatedType instances.
	 */
	public List<RelatedType> getAllRelatedTypes();
        
        
        /**-------------Operations related to {@link FormalModel} classes---------*/	
        /**
	 * Finds a FormalModel object by id.
	 * @param id the id of the FormalModel.
	 * @return the FormalModel object.
	 */
	public FormalModel getFormalModel(String id);
	
	/**
	 * 
	 * @return a list containing all FormalModel instances.
	 */
	public List<FormalModel> getAllFormalModels();
        
        
        /** ----------Operations related to {@link Justification} classes---------*/
        /**
	 * Finds a Justification object by id.
	 * @param id the id of the Justification.
	 * @return the Justification object.
	 */
	public Justification getJustification(String id);
	
	/**
	 * 
	 * @return a list containing all Justification instances.
	 */
	public List<Justification> getAllJustifications();
        
        
        /**------- Operations related to {@link Person} classes-------------------*/
        /**
	 * Finds an Person object by id.
	 * @param id the id of the Person.
	 * @return the Person object.
	 */
	public Person getPerson(String id);
	
	/**
	 * 
	 * @return a list containing all Person instances.
	 */
	public List<Person> getAllPeople();
        
        
        /**----------Operations related to {@link Activity} classes----------------*/
        /**
	 * Finds an Activity object by id.
	 * @param id the id of the Activity.
	 * @return the Activity object.
	 */
	public Activity getActivity(String id);
	
	/**
	 * 
	 * @return a list containing all Activity instances.
	 */
	public List<Activity> getAllActivities();
        
        
        /**---------- Operations related to {@link Method} classes---------------------*/
        /**
	 * Finds an Method object by id.
	 * @param id the id of the Method.
	 * @return the Method object.
	 */
	public Method getMethod(String id);
	
	/**
	 * 
	 * @return a list containing all Method instances.
	 */
	public List<Method> getAllMethodInstances();
        
        
        /**----------Misc Operations---------------------*/
        
        
        
        public Collection<String> getDataPropertyValues(String propertyName, String individualId);
        public void addDataPropertyValue(String propertyName, String individualId, String value);
        public void setDataPropertyValues(String propertyName, String individualId, Collection<String> value);
        public void removeDataPropertyValue(String propertyName, String individualId, String value);
        
        public Collection getObjectPropertyValues(String propertyName, String individualId);
        public void addObjectPropertyValue(String propertyName, String individualId, KuabaElement value);
        public void setObjectPropertyValues(String propertyName, String individualId, Collection values);
        public void removeObjectPropertyValue(String propertyName, String individualId, KuabaElement value);
        
        public boolean hasProperty(String propertyName, String individualId);
        
        public Collection<String> getComments(String individualId);
        public void addComment(String string, String individualId);
        public void removeComment(String string, String individualId);
        
        public void rename(String oldID, String newID);
        
        public void removeIndividual(String individualId);
        
        public void dispose();
        
        /**
	 * @return a copy of the current repository with a random base URL.
	 */
        public KuabaRepository copy();
        
        /**
	 * Returns a copy of the current repository with the specified base URL.
	 * @param the new base URL.
	 * @return a copy of the current repository with the new base URL.
	 */
        public KuabaRepository copy(String url);
}
