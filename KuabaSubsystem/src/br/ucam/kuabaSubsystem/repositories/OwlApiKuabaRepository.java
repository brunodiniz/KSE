/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.repositories;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Activity;
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
import br.ucam.kuabaSubsystem.kuabaModel.KuabaListener;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Method;
import br.ucam.kuabaSubsystem.kuabaModel.Person;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.RelatedType;
import br.ucam.kuabaSubsystem.kuabaModel.Role;
import br.ucam.kuabaSubsystem.kuabaModel.impl.OwlApiKuabaModelFactory;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

/**
 *
 * @author Bruno
 */
public class OwlApiKuabaRepository implements KuabaListener, KuabaRepository {
    
    public static final String ONTOLOGY_URL = KuabaSubsystem.ONTOLOGY_URL;
    
    private OWLDataFactory factory;
    private KuabaModelFactory kuabaFactory;
    private OWLOntology model;
    private String instanceUrl;  
    private HashMap<String, List<Idea>> ideaCache = new HashMap<String, List<Idea>>();
    
    // transforma o indId em IRI completa, ou deixa como está se já for completa
    private String getCompleteURI(String id, boolean ontology) {
        String uri = id;
        String base;
        if(ontology) base = ONTOLOGY_URL;
        else base = this.getUrl();
        if(!id.contains("#")) uri = base+"#"+id;
        return uri;
    }


//    
//    //pega valor da dataproperty para um determinado indivíduo
//    public LinkedList<String> getIndividualObjectPropertyValue(String individualName, String propertyName) {
//        String individualIRI = getCompleteIRI(individualName);
//        String propertyIRI = getCompleteIRI(propertyName);
//        return getIndividualObjectPropertyValue(IRI.create(individualIRI),IRI.create(propertyIRI));
//    }
//    
//    public LinkedList<String> getIndividualObjectPropertyValue(IRI individualIRI, IRI propertyIRI) {
//        return getIndividualObjectPropertyValue(factory.getOWLNamedIndividual(individualIRI),factory.getOWLObjectProperty(propertyIRI));
//    }
//    
//    public LinkedList<String> getIndividualObjectPropertyValue(OWLNamedIndividual individual, OWLObjectProperty property) {
//        Set<OWLIndividual> inversesSet = individual.getObjectPropertyValues(property, owlInstance);
//        LinkedList<String> resp = new LinkedList<String>();
//        for (OWLIndividual i : inversesSet) {                       
//            resp.add(i.asOWLNamedIndividual().getIRI().toString());
//        }
//        return resp;
//    }
//    
//    
//    public boolean containsIndividual(String individualName) {
//        String individualIRI = getCompleteIRI(individualName);
//        return owlInstance.containsIndividualInSignature(IRI.create(individualIRI));
//    }
//    
    


	public OwlApiKuabaRepository(OWLOntology model, OWLDataFactory factory) {
		this.model = model;
                this.factory = factory;
                instanceUrl = model.getOntologyID().getOntologyIRI().toString();
	}

	@Override
	public Object getModel() {		
		return this.model;
	}

//	@Override
//	public boolean save() {
//		return false;
//	}

	@Override
	public KuabaModelFactory getModelFactory() {
            
		if(this.kuabaFactory == null)
			this.kuabaFactory = new OwlApiKuabaModelFactory(this); 
		return this.kuabaFactory;
	}

	@Override
	public String getUrl() {
		return instanceUrl;         
	}

//	@Override
//	public void setUrl(String url) {		
//                throw new UnsupportedOperationException("Not supported yet.");
//	}
        
    private Class getIndividualClass(String individualId/*, Class cls*/) throws ClassNotFoundException {

//        OWLClass argumentClass = factory.getOWLClass(IRI.create(ONTOLOGY_URL +"#"+ cls.getSimpleName()));       
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(instanceUrl +"#"+ individualId));
        
        OWLClass cl = null;
        Set<OWLClassExpression> set = individual.getTypes((OWLOntology)this.getModel());
        
//        //Valida se o indivíduo realmente pertence a classe fornecida
//        if (!set.contains(argumentClass)) return null;
        
        //Obtem a classe folha (sem mais subclasses) do indivíduo
        for (OWLClassExpression ce : set) {
            cl = ce.asOWLClass();
            if (cl.getSubClasses(OwlApiFileGateway.getInstance().getKuabaOntology()).isEmpty()) break;
        }
//        if (cl == null) return null;
 
        return Class.forName("br.ucam.kuabaSubsystem.kuabaModel.impl.Default"+cl.getIRI().getFragment());
    }        
        
    
    /**
	 * Gets all elements of a given class.
	 * @param The class which elements will be taken.
	 * @return the list of all instances of that class.
     */
    private <T> List<T> getAllKuabaElements(Class cls) {
            
//            System.out.println(ONTOLOGY_URL +"#"+ cls.getSimpleName());
            
            
            OWLClass argumentClass = factory.getOWLClass(IRI.create(ONTOLOGY_URL +"#"+ cls.getSimpleName()));
//            OWLClass argumentClass = factory.getOWLClass(cls.getSimpleName(), new DefaultPrefixManager(ONTOLOGY_URL+"#"));
//            OWLClass argumentClass = factory.getOWLClass(IRI.create("k:"+ cls.getSimpleName()));

            Set<OWLIndividual> individuals = argumentClass.getIndividuals(model); 
            
            List<T> result = new ArrayList<T>();
            
            for (OWLIndividual i : individuals) {
                try {      
//                    Class cl = Class.forName("br.ucam.kuabaSubsystem.kuabaModel.impl.Default"+cls.getSimpleName());
                    String indId = i.asOWLNamedIndividual().getIRI().getFragment();
                    
                    Class cl = getIndividualClass(indId);

                    Class partypes[] = new Class[2];  
                    partypes[0] = String.class;  
                    partypes[1] = KuabaRepository.class;  

                    Constructor ct = cl.getConstructor(partypes);  

                    Object arglist[] = new Object[2];  
                    arglist[0] = indId;  
                    arglist[1] = this;  

                    Object retobj = ct.newInstance(arglist);

                    result.add((T) retobj);
                
                } catch (Exception e) {
                    System.err.println("ERRO em getAllKuabaElements -- " + e.toString());
                }     
            }
            
            return result;
    }   
    
          
    private <T> T getKuabaElement(String id, Class<T> cls) {  
        if (validateIndividual(id,cls) && KuabaElement.class.isAssignableFrom(cls)) {
            try {             
//                Class cl = Class.forName("br.ucam.kuabaSubsystem.kuabaModel.impl.Default"+cls.getSimpleName()); 
                Class cl = getIndividualClass(id);
  
                Class partypes[] = new Class[2];  
                partypes[0] = String.class;  
                partypes[1] = KuabaRepository.class;  

                Constructor ct = cl.getConstructor(partypes);  

                Object arglist[] = new Object[2];  
                arglist[0] = id;  
                arglist[1] = this;  

                return (T) ct.newInstance(arglist);

                
            } catch (Exception e) {
                System.err.println("ERRO em getKuabaElement -- "+e.toString());
            }
        }
        
        return null;
    }    
     
    private boolean validateIndividual(String individualId, Class cls) {
        OWLClass argumentClass = factory.getOWLClass(IRI.create(ONTOLOGY_URL +"#"+ cls.getSimpleName()));       
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(instanceUrl +"#"+ individualId));

        Set<OWLClassExpression>  s = individual.getTypes(model);
        return s.contains(argumentClass);
    }
    

    public Question getQuestion(String id) {
        return getKuabaElement(id, Question.class);
    }

    public List<Question> getAllQuestions() {
        return getAllKuabaElements(Question.class);
    }

    public List<Question> getQuestions(FormalModel formalModel) {
        List<Question> allQuestions = getAllQuestions();
        List<Question> result = new ArrayList<Question>();
        for (Question question : allQuestions) {
                if(formalModel.equals(question.getIsDefinedBy()))
                        result.add(question);
        }
        return result;
    }

    public List<Question> getQuestionByText(String text) {
        List<Question> allQuestions = getAllQuestions();
        List<Question> result = new ArrayList<Question>();
        for (Question question : allQuestions) {
                if(text.equals(question.getHasText()))
                        result.add(question);
        }
        return result;
    }

    public Question getFirstQuestionByText(String text) {
        List<Question> allQuestions = getAllQuestions();		
        for (Question question : allQuestions) {
                if(text.equals(question.getHasText()))
                        return question;
        }
        return null;
    }

    public Question getFirstQuestionByText(Idea addressIdea, String questionText) {
        Collection<Question> questions = addressIdea.getSuggests();
        for (Question question : questions) {
                if(questionText.equals(question.getHasText()))
                        return question;
        }
        return null;
    }

    public Idea getIdea(String id) {
        return getKuabaElement(id, Idea.class);
    }

    public Idea getFirstIdeaByText(Question addressedQuestion, String ideaText) {
        Collection<Idea> ideas = addressedQuestion.getIsAddressedBy();
        for (Idea idea : ideas) {
                if(ideaText.equals(idea.getHasText()))
                        return idea;
        }
        return null;
    }

    public List<Idea> getIdeaByText(String ideaText) {
        List<Idea> allIdeas = getAllIdeas();
        List<Idea> result = new ArrayList<Idea>();
        for (Idea idea : allIdeas) {
                if(ideaText.equals(idea.getHasText()))
                        result.add(idea);
        }
        return result;
    }

    public List<Idea> getDesignedDomainIdeas(String domainIdeaText, String designIdeaText) {
        List<Idea> result = new ArrayList<Idea>();
        List<Idea> domainIdeas = this.getIdeaByText(domainIdeaText);

        for (Idea idea : domainIdeas) {
                Question howModel = (Question)idea.getSuggests().iterator().next();			
                Collection<Idea> ideas = this.getAcceptedIdeas(howModel);
                for (Idea addressedIdea : ideas) {
                        if(designIdeaText.equals(addressedIdea.getHasText()))
                                result.add(idea);
                }
        }
        return result;
    }

    public List<Idea> getAllIdeas() {
        return getAllKuabaElements(Idea.class);
    }

    public List<Idea> getAcceptedIdeas(Question question) {
        List<Idea> acceptedIdeas = new ArrayList<Idea>();
        Collection<Decision> decisions = question.getHasDecision();
        for (Decision decision : decisions) {
                if(decision.getIsAccepted())
                        acceptedIdeas.add(decision.getConcludes());
        }
        return acceptedIdeas;
    }
    
    public List<Idea> getDomainIdeas(){
        List<Idea> allIdeas = this.getAllIdeas();
        List<Idea> result = new ArrayList<Idea>();		
        for (Idea idea : allIdeas){
                if (idea.getIsDefinedBy() == null){
                        if(idea.getId().contains("_")){
                                String elementId = "_" + idea.getId().split("_")[1];
                                List<Idea> ideaList = this.ideaCache.get(elementId);
                                if (ideaList == null){
                                        ideaList = new ArrayList<Idea>();
                                        this.ideaCache.put(elementId, ideaList);
                                }
                                ideaList.add(idea);
                                result.add(idea);
                        }
                }
        }
        return result;

    }

    public List<Idea> getDomainIdeasWhereIdLike(String idPeace) {
        if(!idPeace.contains("_"))
                idPeace = "_" + idPeace;		
        List<Idea> allIdeas = this.ideaCache.get(idPeace);
        if (allIdeas != null) {		
                if(this.ideaCache.isEmpty())
                        allIdeas = this.getDomainIdeas();

                List<Idea> result = new ArrayList<Idea>();
                for (Idea idea : allIdeas){			
                        if (idea.getId().contains(idPeace))
                                result.add(idea);
                }

                return result;
        }
        return new ArrayList<Idea>();
    }

    public List<Idea> getRejectedIdeas(Question question) {
        List<Idea> acceptedIdeas = new ArrayList<Idea>();
        Collection<Decision> decisions = question.getHasDecision();
        for (Decision decision : decisions)
                if(!decision.getIsAccepted())
                        acceptedIdeas.add(decision.getConcludes());

        return acceptedIdeas;
    }

    public Argument getArgument(String id) {
        return getKuabaElement(id, Argument.class);
    }

    public List<Argument> getAllArguments() {
        return getAllKuabaElements(Argument.class);
    }

    public Decision getDecision(String id) {
        return getKuabaElement(id, Decision.class);
    }

    public List<Decision> getAllDecisions() {
        return getAllKuabaElements(Decision.class);
    }

    public Decision getDecision(Question question, Idea idea) {
        Collection<Decision> ideaDecisionList = idea.getIsConcludedBy();
        for (Decision decision : ideaDecisionList) {
                if(question.getHasDecision().contains(decision))
                        return decision;
        }
        return null;
    }

    public Decision getMostRecentDecision(Question question) {
        Collection<Decision> decisions = question.getHasDecision();
        Decision mostRecent = null;
        if(!decisions.isEmpty()){
                mostRecent = decisions.iterator().next();
                for (Decision decision : decisions) 
                        if(decision.getHasDate().after(mostRecent.getHasDate()))
                                mostRecent = decision;

        }			
        return mostRecent;
    }

    public Role getRole(String id) {
        return getKuabaElement(id, Role.class);
    }

    public List<Role> getAllRoles() {
        return getAllKuabaElements(Role.class);
    }

    public Artifact getArtifact(String id) {
        return getKuabaElement(id, Artifact.class);
    }

    public List<Artifact> getAllArtifacts() {
        return getAllKuabaElements(Artifact.class);
    }

    public AtomicArtifact getAtomicArtifact(String id) {
        return getKuabaElement(id, AtomicArtifact.class);
    }

    public List<AtomicArtifact> getAllAtomicArtifacts() {
        return getAllKuabaElements(AtomicArtifact.class);
    }

    public CompositeArtifact getCompositeArtifact(String id) {
        return getKuabaElement(id, CompositeArtifact.class);
    }

    public List<CompositeArtifact> getAllCompositeArtifacts() {
        return getAllKuabaElements(CompositeArtifact.class);
    }

    public ExpectedDuration getExpectedDuration(String id) {
        return getKuabaElement(id, ExpectedDuration.class);
    }

    public List<ExpectedDuration> getAllExpectedDurations() {
        return getAllKuabaElements(ExpectedDuration.class);
    }

    public RelatedType getRelatedType(String id) {
        return getKuabaElement(id, RelatedType.class);
    }

    public List<RelatedType> getAllRelatedTypes() {
        return getAllKuabaElements(RelatedType.class);
    }

    public FormalModel getFormalModel(String id) {
        return getKuabaElement(id, FormalModel.class);
    }

    public List<FormalModel> getAllFormalModels() {
       return getAllKuabaElements(FormalModel.class);
    }

    public Justification getJustification(String id) {
        return getKuabaElement(id, Justification.class);
    }

    public List<Justification> getAllJustifications() {
        return getAllKuabaElements(Justification.class);
    }

    public Person getPerson(String id) {
        return getKuabaElement(id, Person.class);
    }

    public List<Person> getAllPeople() {
        return getAllKuabaElements(Person.class);
    }

    public Activity getActivity(String id) {
        return getKuabaElement(id, Activity.class);
    }

    public List<Activity> getAllActivities() {
        return getAllKuabaElements(Activity.class);
    }

    public Method getMethod(String id) {
        return getKuabaElement(id, Method.class);
    }

    public List<Method> getAllMethodInstances() {
        return getAllKuabaElements(Method.class);
    }

    public Collection<String> getDataPropertyValues(String propertyName, String individualId) {
        Collection<String> result = new ArrayList<String>();
        
        OWLDataProperty dp = factory.getOWLDataProperty(IRI.create(getCompleteURI(propertyName, true)));
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        Set<OWLLiteral> s = i.getDataPropertyValues(dp, model);
        for (OWLLiteral l : s) result.add(l.getLiteral());
        
        return result;
    }

    public void addDataPropertyValue(String propertyName, String individualId, String value) {
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        OWLDataProperty dp = factory.getOWLDataProperty(IRI.create(getCompleteURI(propertyName, true)));
        OWLDataPropertyAssertionAxiom a = factory.getOWLDataPropertyAssertionAxiom(dp, i, value);
        model.getOWLOntologyManager().addAxiom(model, a);
    }

    //apaga as dataProperty existentes e adiciona o conjunto value
    public void setDataPropertyValues(String propertyName, String individualId, Collection<String> value) {
            
        HashSet<OWLDataPropertyAssertionAxiom> hset = new HashSet<OWLDataPropertyAssertionAxiom>();
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        OWLDataProperty dp = factory.getOWLDataProperty(IRI.create(getCompleteURI(propertyName, true)));
        
        Set<OWLLiteral> s = i.getDataPropertyValues(dp, model);
        
        for (OWLLiteral l : s) {
            OWLDataPropertyAssertionAxiom a = factory.getOWLDataPropertyAssertionAxiom(dp, i, l);
            hset.add(a);
        }
        
        model.getOWLOntologyManager().removeAxioms(model, hset);
        hset.clear();
        
        for (String v : value) {
            OWLDataPropertyAssertionAxiom a = factory.getOWLDataPropertyAssertionAxiom(dp, i, v);
            hset.add(a);
        }
        
        model.getOWLOntologyManager().addAxioms(model, hset);
        
    }

    public void removeDataPropertyValue(String propertyName, String individualId, String value) {
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        OWLDataProperty dp = factory.getOWLDataProperty(IRI.create(getCompleteURI(propertyName, true)));
        OWLDataPropertyAssertionAxiom a = factory.getOWLDataPropertyAssertionAxiom(dp, i, value);
        model.getOWLOntologyManager().removeAxiom(model, a);
    }

    public Collection getObjectPropertyValues(String propertyName, String individualId) {
//        throw new UnsupportedOperationException("Not supported yet.");  
        Collection result = new ArrayList();
        
        OWLObjectProperty op = factory.getOWLObjectProperty(IRI.create(getCompleteURI(propertyName, true)));
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        Set<OWLIndividual> s = i.getObjectPropertyValues(op, model);

        for (OWLIndividual ind : s) {          
            try {
                //identifying (and loading, if it isn't loaded yet) the repository which the individual resides
                KuabaRepository repository = OwlApiFileGateway.getInstance().getSourceRepository(ind.asOWLNamedIndividual().getIRI());
      
                //identifying the class which the individual is asserted
                OWLClass cl = null;
                for (OWLClassExpression ce : ind.getTypes((OWLOntology)repository.getModel())) {
                    cl = ce.asOWLClass();
                    if (cl.getSubClasses(OwlApiFileGateway.getInstance().getKuabaOntology()).isEmpty()) break;
                }
                if (cl == null) throw new Exception("Class not found");

                Class cls = Class.forName("br.ucam.kuabaSubsystem.kuabaModel.impl.Default"+cl.getIRI().getFragment()); 
                
                Class partypes[] = new Class[2];  
                partypes[0] = String.class;  
                partypes[1] = KuabaRepository.class;  

                Constructor ct = cls.getConstructor(partypes);  

                Object arglist[] = new Object[2];  
                arglist[0] = ind.asOWLNamedIndividual().getIRI().getFragment();  
                arglist[1] = repository;  
                
                Object retobj = ct.newInstance(arglist);

                result.add(retobj);
            } catch (Exception e) {
                System.err.println("ERRO em getObjectPropertyValues -- "+e.toString()+" -- "+e.getMessage() +" -- Property = "+propertyName);
//                e.printStackTrace();
            }
        }
        return result;
    }

    public void addObjectPropertyValue(String propertyName, String individualId, KuabaElement value) {
        //identificando a object property e os individuos
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        OWLNamedIndividual iValue = factory.getOWLNamedIndividual(IRI.create(value.getRepository().getUrl()+"#"+value.getId()));
        OWLObjectProperty op = factory.getOWLObjectProperty(IRI.create(getCompleteURI(propertyName, true)));
        
        //verificando se há properties inversas e cria as assertions dessas object properties inversas
        Set<OWLObjectPropertyExpression> s = op.getInverses(OwlApiFileGateway.getInstance().getKuabaOntology());
        for (OWLObjectPropertyExpression iope : s) {
            OWLObjectProperty iop = iope.getNamedProperty();
            if (!iValue.getObjectPropertyValues(iope, model).contains(i)) {
                OWLObjectPropertyAssertionAxiom a = factory.getOWLObjectPropertyAssertionAxiom(iop, iValue, i);
                model.getOWLOntologyManager().addAxiom(model, a);
            }
        }
        
        //criando e adicionando o axioma assertion na ontologia
        OWLObjectPropertyAssertionAxiom a = factory.getOWLObjectPropertyAssertionAxiom(op, i, iValue);
        model.getOWLOntologyManager().addAxiom(model, a);
    }

    public void setObjectPropertyValues(String propertyName, String individualId, Collection values) {
        HashSet<OWLObjectPropertyAssertionAxiom> hset = new HashSet<OWLObjectPropertyAssertionAxiom>();
        
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        OWLObjectProperty op = factory.getOWLObjectProperty(IRI.create(getCompleteURI(propertyName, true)));        
        
        //set de properties que são inversas a property fornecida
        Set<OWLObjectPropertyExpression> inversesSet = op.getInverses(OwlApiFileGateway.getInstance().getKuabaOntology());
        
        //identificando object properties presentes
        Set<OWLIndividual> s = i.getObjectPropertyValues(op, model);
        for (OWLIndividual ind : s) {            
            OWLObjectPropertyAssertionAxiom a = factory.getOWLObjectPropertyAssertionAxiom(op, i, ind);
            hset.add(a);
        }
        
//        //identificando object properties inversas presentes
//        for (OWLObjectPropertyExpression iope : inversesSet) {
//            OWLObjectProperty iop = iope.getNamedProperty();
//            for (OWLIndividual ind : s) {           
//                if (ind.getObjectPropertyValues(iope, model).contains(i)) {
//                    OWLObjectPropertyAssertionAxiom a = factory.getOWLObjectPropertyAssertionAxiom(iop, ind, i);
//                    hset.add(a);
//                }
//            }
//        }
        
        //comando para remoção das object properties identificadas
        model.getOWLOntologyManager().removeAxioms(model, hset);
        hset.clear();
        
        //criando axiomas para incluir as novas properties
        for (Object v : values) {           
            KuabaElement value = (KuabaElement) v;
            OWLNamedIndividual iValue = factory.getOWLNamedIndividual(IRI.create(value.getRepository().getUrl()+"#"+value.getId()));
            
            //cria as assertions das object properties inversas, se houver
            for (OWLObjectPropertyExpression iope : inversesSet) {
                OWLObjectProperty iop = iope.getNamedProperty();
                if (!iValue.getObjectPropertyValues(iope, model).contains(i)) {
                    OWLObjectPropertyAssertionAxiom a = factory.getOWLObjectPropertyAssertionAxiom(iop, iValue, i);
                    hset.add(a);
                }
            }
            
            //cria o assertion axiom da object property
            OWLObjectPropertyAssertionAxiom b = factory.getOWLObjectPropertyAssertionAxiom(op, i, iValue);
            hset.add(b);
        }
        
        //incluindo
        model.getOWLOntologyManager().addAxioms(model, hset);
    }

    public void removeObjectPropertyValue(String propertyName, String individualId, KuabaElement value) {
        //identificando a object property e os individuos
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        OWLNamedIndividual iValue = factory.getOWLNamedIndividual(IRI.create(value.getRepository().getUrl()+"#"+value.getId()));
        OWLObjectProperty op = factory.getOWLObjectProperty(IRI.create(getCompleteURI(propertyName, true)));
        
//        //verificando se há properties inversas e remove as assertions dessas object properties inversas
//        Set<OWLObjectPropertyExpression> s = op.getInverses(OwlApiFileGateway.getInstance().getKuabaOntology());
//        for (OWLObjectPropertyExpression iope : s) {
//            OWLObjectProperty iop = iope.getNamedProperty();
//            if (iValue.getObjectPropertyValues(iope, model).contains(i)) {
//                OWLObjectPropertyAssertionAxiom a = factory.getOWLObjectPropertyAssertionAxiom(iop, iValue, i);
//                model.getOWLOntologyManager().removeAxiom(model, a);
//            }
//        }
        
        //criando e removendo o axioma assertion da ontologia
        OWLObjectPropertyAssertionAxiom a = factory.getOWLObjectPropertyAssertionAxiom(op, i, iValue);
        model.getOWLOntologyManager().removeAxiom(model, a);
    }

    public boolean hasProperty(String propertyName, String individualId) {
        
        if (!getDataPropertyValues(propertyName, individualId).isEmpty()) return true;
        if (!getObjectPropertyValues(propertyName, individualId).isEmpty()) return true;
        
        return false;
    }

    public Collection<String> getComments(String individualId) {
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        OWLAnnotationProperty ap = factory.getOWLAnnotationProperty(IRI.create(getCompleteURI("comment", true)));
        
        Set<OWLAnnotation> set = i.getAnnotations(model, ap);
        List<String> result = new ArrayList<String>();
        
        for (OWLAnnotation anot : set) {
            OWLLiteral lit = (OWLLiteral) anot.getValue();
            result.add(lit.getLiteral());
        }
        return result;
    }
    
    public void addComment(String string, String individualId) {
        OWLAnnotationProperty ap = factory.getOWLAnnotationProperty(IRI.create(getCompleteURI("comment", true)));
        OWLLiteral lit = factory.getOWLLiteral(string);
        OWLAnnotationAssertionAxiom ax = factory.getOWLAnnotationAssertionAxiom(ap, IRI.create(getCompleteURI(individualId, false)), lit);
        model.getOWLOntologyManager().addAxiom(model, ax);
    }

    public void removeComment(String string, String individualId) {
        OWLAnnotationProperty ap = factory.getOWLAnnotationProperty(IRI.create(getCompleteURI("comment", true)));
        OWLLiteral lit = factory.getOWLLiteral(string);
        OWLAnnotationAssertionAxiom ax = factory.getOWLAnnotationAssertionAxiom(ap, IRI.create(getCompleteURI(individualId, false)), lit);
        model.getOWLOntologyManager().removeAxiom(model, ax);
    }
    
    public void rename(String oldID, String newID) {
        OWLEntityRenamer renamer = new OWLEntityRenamer(model.getOWLOntologyManager(), model.getImportsClosure());
        model.getOWLOntologyManager().applyChanges(renamer.changeIRI(IRI.create(getCompleteURI(oldID, false)), IRI.create(getCompleteURI(newID, false))));
    }

    public void dispose() {
        OwlApiFileGateway.getInstance().dispose(this);
    }
 
    public void newElementCreated(KuabaElement kuabaElement) {
        if(kuabaElement.getId().split("_").length < 2) return;
        
        if(this.ideaCache.isEmpty())
            this.getDomainIdeas();	
        
        String elementId = "_"+kuabaElement.getId().split("_")[1];
        Idea idea = (Idea)kuabaElement;
        List<Idea> ideaList = this.ideaCache.get(elementId);
        if(ideaList == null){
            ideaList = new ArrayList<Idea>();
            this.ideaCache.put(elementId, ideaList);
        }
        ideaList.add(idea);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OwlApiKuabaRepository) {
            Object objModel = ((OwlApiKuabaRepository) obj).getModel();
            if (this.model.equals(objModel)) return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return getUrl();
    }

    public KuabaRepository copy() {
        return OwlApiFileGateway.getInstance().copy(this);
    }

    public KuabaRepository copy(String url) {
        return OwlApiFileGateway.getInstance().copy(this, url);
    }

    public void removeIndividual(String individualId) {
        OWLEntityRemover remover = new OWLEntityRemover(model.getOWLOntologyManager(), Collections.singleton(model));
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(getCompleteURI(individualId, false)));
        i.accept(remover);
        model.getOWLOntologyManager().applyChanges(remover.getChanges());
        
        //gambiarra pra fazer refresh no cache de ideias
        this.ideaCache.clear();
        getDomainIdeas();
    }
    
}
