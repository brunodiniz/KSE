package br.ucam.kuabaSubsystem.kuabaModel;

import java.io.File;
import java.util.Collection;

import br.ucam.kuabaSubsystem.repositories.KuabaRepository;




public interface KuabaModelFactoryOld {
	
	/**-------------Operations related to {@link Idea} classes------------------*/
	
	/**
	 * This method creates a new Idea object. Attempts that the id must be unique
	 * in the current individuals file.
	 *  
	 * @param id: the ID to the new created Idea object.
	 * @return a new Idea object that contains the ID passed in the id parameter. 
	 */
	public Idea createIdea(String id);
	
	/**
	 * This method find an Idea object by your ID.
	 * @param id: The ID of the Idea object to be found.
	 * @return null if the Idea object with the id was not found.
	 * Or.
	 * @return the Idea object that has the ID passed on id parameter 	 
	 * */
	public Idea getIdea(String id);
	
	/**
	 * 
	 * @return all Idea objects in the current individuals file being used. 
	 */
	public Collection<Idea> getAllIdeaInstances();
	
	/**-------------Operations relates to {@link Question} classes-----------*/
	
	/**
	 * This method creates a new Question object with the id. Attempts that the id must be unique
	 * in the current individuals file.
	 *  
	 * @param id: the ID to the new created Idea object.	 * 
	 * @return a new Question object that contains the ID passed in the id parameter. 
	 */
	public Question createQuestion(String id);
	
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
	public Collection<Question> getAllQuestionInstances();
	
	/**-------------Operations related to {@link Argument} classes----------- */
	
	/** Creates a new Argument object.
	 * @param id: the ID of the Argument
	 * @return the new Argument with the ID specified on id parameter. 
	 */
	public Argument createArgument(String id);
	
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
	public Collection<Argument> getAllArgumentInstances();
	
	/**------------Operations related to {@link Decision} classes-------------*/	
	/**
	 * Creates an Decision object.
	 * @param id: the id of the Decision.
	 * @return the new Decision object.
	 */
	public Decision createDecision(String id);
	
	/**
	 * Finds a Decision object by id.
	 * @param id the id of the Decision.
	 * @return the Decision object.
	 */
	public Decision getDecision(String id);
	
	/**
	 * 
	 * @return a collection containing all Decision instances.
	 */
	public Collection<Decision> getAllDecisionInstances();	
	
	
	/**----------Operations related to {@link Role} classes--------------------*/
	/**
	 * Creates a new Role object with the specified id.
	 * @param id: the id of the Role object.
	 * @return the new Role object with the id. 
	 */
	public Role createRole(String id);
	
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
	 * @return a collection containing all Role instances. 
	 */
	public Collection<Role> getAllRoleInstances();
	
	/**---------Operations related to {@link Artifact} classes---------------*/
	/**
	 * Creates a new Artifact with the id passed on parameter.
	 * @param id: the id of the new Artifact object.
	 * @return the Artifact object.
	 */
	public Artifact createArtifact(String id);
	
	/**
	 * Finds an Artifact by id. 
	 * @param id: the id of the Artifact to be found. 
	 * @return the Artifact object.
	 */
	public Artifact getArtifact(String id);
	
	/**
	 * 
	 * @return a collection that contains all Artifacts instances.
	 */
	public Collection<Artifact> getAllArtifactInstances();
	
	/**------------Operations related to {@link AtomicArtifact} classes-------*/
	/**
	 * Creates an AtomicArtifact object.
	 * @param id: the id of the AtomicArtifact.
	 * @return the new AtomicArtifact object.
	 */
	public AtomicArtifact createAtomicArtifact(String id);
	
	/**
	 * Finds an AtomicArtifact object by id.
	 * @param id the id of the AtomicArtifact.
	 * @return the AtomicArtifact object.
	 */
	public AtomicArtifact getAtomicArtifact(String id);
	
	/**
	 * 
	 * @return a collection containing all AtomicArtifact instances.
	 */
	public Collection<AtomicArtifact> getAllAtomicArtifactInstances();
	
	/**---------Operations related to {@link CompositeArtifact} classes-------*/	
	/**
	 * Creates an CompositeArtifact object.
	 * @param id: the id of the CompositeArtifact.
	 * @return the new CompositeArtifact object.
	 */
	public CompositeArtifact createCompositeArtifact(String id);
	
	/**
	 * Finds an CompositeArtifact object by id.
	 * @param id the id of the CompositeArtifact.
	 * @return the CompositeArtifact object.
	 */
	public CompositeArtifact getCompositeArtifact(String id);
	
	/**
	 * 
	 * @return a collection containing all CompositeArtifact instances.
	 */
	public Collection<CompositeArtifact> getAllCompositeArtifactInstances();
	
	/**----------- Operations related to {@link ExpectedDuration} classes------*/	
	/**
	 * Creates an ExpectedDuration object.
	 * @param id: the id of the ExpectedDuration.
	 * @return the new ExpectedDuration object.
	 */
	public ExpectedDuration createExpectedDuration(String id);
	
	/**
	 * Finds an ExpectedDuration object by id.
	 * @param id the id of the ExpectedDuration.
	 * @return the ExpectedDuration object.
	 */
	public ExpectedDuration getExpectedDuration(String id);
	
	/**
	 * 
	 * @return a collection containing all ExpectedDuration instances.
	 */
	public Collection<ExpectedDuration> getAllExpectedDurationInstances();
	
	/**----- Operations related to {@link RelatedType} classes-----------------*/	
	/**
	 * Creates a RelatedType object.
	 * @param id: the id of the RelatedType.
	 * @return the new RelatedType object.
	 */
	public RelatedType createRelatedType(String id);
	
	/**
	 * Finds a RelatedType object by id.
	 * @param id the id of the RelatedType.
	 * @return the RelatedType object.
	 */
	public RelatedType getRelatedType(String id);
	
	/**
	 * 
	 * @return a collection containing all RelatedType instances.
	 */
	public Collection<RelatedType> getAllRelatedTypeInstances();
	
	/**-------------Operations related to {@link FormalModel} classes---------*/	
	/**
	 * Creates a FormalModel object.
	 * @param id: the id of the FormalModel.
	 * @return the new FormalModel object.
	 */
	public FormalModel createFormalModel(String id);
	
	/**
	 * Finds a FormalModel object by id.
	 * @param id the id of the FormalModel.
	 * @return the FormalModel object.
	 */
	public FormalModel getFormalModel(String id);
	
	/**
	 * 
	 * @return a collection containing all FormalModel instances.
	 */
	public Collection<FormalModel> getAllFormalModelInstances();
	
	/** ----------Operations related to {@link Justification} classes---------*/	
	/**
	 * Creates a Justification object.
	 * @param id: the id of the Justification.
	 * @return the new Justification object.
	 */
	public Justification createJustification(String id);
	
	/**
	 * Finds a Justification object by id.
	 * @param id the id of the Justification.
	 * @return the Justification object.
	 */
	public Justification getJustification(String id);
	
	/**
	 * 
	 * @return a collection containing all Justification instances.
	 */
	public Collection<Justification> getAllJustificationInstances();
	
	/**------- Operations related to {@link Person} classes-------------------*/	
	/**
	 * Creates an Person object.
	 * @param id: the id of the Person.
	 * @return the new Person object.
	 */
	public Person createPerson(String id);
	
	/**
	 * Finds an Person object by id.
	 * @param id the id of the Person.
	 * @return the Person object.
	 */
	public Person getPerson(String id);
	
	/**
	 * 
	 * @return a collection containing all Person instances.
	 */
	public Collection<Person> getAllPersonInstances();
	
	/**----------Operations related to {@link Activity} classes----------------*/	
	/**
	 * Creates an Activity object.
	 * @param id: the id of the Activity.
	 * @return the new Activity object.
	 */
	public Activity createActivity(String id);
	
	/**
	 * Finds an Activity object by id.
	 * @param id the id of the Activity.
	 * @return the Activity object.
	 */
	public Activity getActivity(String id);
	
	/**
	 * 
	 * @return a collection containing all Activity instances.
	 */
	public Collection<Activity> getAllActivityInstances();
	
	/**---------- Operations related to {@link Method} classes---------------------*/	
	/**
	 * Creates an Method object.
	 * @param id: the id of the Method.
	 * @return the new Method object.
	 */
	public Method createMethod(String id);
	
	/**
	 * Finds an Method object by id.
	 * @param id the id of the Method.
	 * @return the Method object.
	 */
	public Method getMethod(String id);
	
	/**
	 * 
	 * @return a collection containing all Method instances.
	 */
	public Collection<Method> getAllMethodInstances();
	
	
}
