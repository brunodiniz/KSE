package br.ucam.kuabaSubsystem.kuabaModel;

import br.ucam.kuabaSubsystem.repositories.KuabaRepository;


public interface KuabaModelFactory {
	
	/**-------------Operations related to {@link Idea} classes------------------*/
	
	/**
	 * This method creates a new Idea object. Attempts that the id must be unique
	 * in the current individuals file.
	 *  
	 * @param id: the ID to the new created Idea object.
	 * @return a new Idea object that contains the ID passed on the id parameter. 
	 */
	public Idea createIdea(String id);
	
        
        
	/**
	 * This method creates a new Solution object. Attempts that the id must be unique
	 * in the current individuals file.
	 *  
	 * @param id: the ID to the new created Solution object.
	 * @return a new Solution object that contains the ID passed on the id parameter. 
	 */
	public Solution createSolution(String id);
	
	/**-------------Operations related to {@link Question} classes-----------*/
	
	/**
	 * This method creates a new Question object with the id. Attempts that the id must be unique
	 * in the current individuals file.
	 *  
	 * @param id: the ID to the new created Idea object.	  
	 * @return a new Question object that contains the ID passed on the id parameter. 
	 */
	public Question createQuestion(String id);
	
	
	/**-------------Operations related to {@link Argument} classes----------- */
	
	/** Creates a new Argument object.
	 * @param id: the ID of the Argument
	 * @return the new Argument with the ID specified on id parameter. 
	 */
	public Argument createArgument(String id);
	
	
	
	
	/**------------Operations related to {@link Decision} classes-------------*/	
	/**
	 * Creates an Decision object.
	 * @param id: the id of the Decision.
	 * @return the new Decision object.
	 */
	public Decision createDecision(String id);
	
	
	
	/**----------Operations related to {@link Role} classes--------------------*/
	/**
	 * Creates a new Role object with the specified id.
	 * @param id: the id of the Role object.
	 * @return the new Role object with the id. 
	 */
	public Role createRole(String id);
        
	
	/**------------Operations related to {@link AtomicArtifact} classes-------*/
	/**
	 * Creates an AtomicArtifact object.
	 * @param id: the id of the AtomicArtifact.
	 * @return the new AtomicArtifact object.
	 */
	public AtomicArtifact createAtomicArtifact(String id);
	
	
	
	/**---------Operations related to {@link CompositeArtifact} classes-------*/	
	/**
	 * Creates an CompositeArtifact object.
	 * @param id: the id of the CompositeArtifact.
	 * @return the new CompositeArtifact object.
	 */
	public CompositeArtifact createCompositeArtifact(String id);
	
	
	
	/**----------- Operations related to {@link ExpectedDuration} classes------*/	
	/**
	 * Creates an ExpectedDuration object.
	 * @param id: the id of the ExpectedDuration.
	 * @return the new ExpectedDuration object.
	 */
	public ExpectedDuration createExpectedDuration(String id);
	
	
	
	/**----- Operations related to {@link RelatedType} classes-----------------*/	
	/**
	 * Creates a RelatedType object.
	 * @param id: the id of the RelatedType.
	 * @return the new RelatedType object.
	 */
	public RelatedType createRelatedType(String id);
	
	
	
	/**-------------Operations related to {@link FormalModel} classes---------*/	
	/**
	 * Creates a FormalModel object.
	 * @param id: the id of the FormalModel.
	 * @return the new FormalModel object.
	 */
	public FormalModel createFormalModel(String id);
	
	
	
	/** ----------Operations related to {@link Justification} classes---------*/	
	/**
	 * Creates a Justification object.
	 * @param id: the id of the Justification.
	 * @return the new Justification object.
	 */
	public Justification createJustification(String id);
	
	
	
	/**------- Operations related to {@link Person} classes-------------------*/	
	/**
	 * Creates an Person object.
	 * @param id: the id of the Person.
	 * @return the new Person object.
	 */
	public Person createPerson(String id);
	
	
	
	/**----------Operations related to {@link Activity} classes----------------*/	
	/**
	 * Creates an Activity object.
	 * @param id: the id of the Activity.
	 * @return the new Activity object.
	 */
	public Activity createActivity(String id);
	
	
	
	/**---------- Operations related to {@link Method} classes---------------------*/	
	/**
	 * Creates an Method object.
	 * @param id: the id of the Method.
	 * @return the new Method object.
	 */
	public Method createMethod(String id);
	
	public KuabaRepository getKuabaRepository();
	
	
}
