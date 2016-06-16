/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.kuabaModel.impl;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.AtomicArtifact;
import br.ucam.kuabaSubsystem.kuabaModel.CompositeArtifact;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.ExpectedDuration;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaElement;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaEventPump;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Method;
import br.ucam.kuabaSubsystem.kuabaModel.Person;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.kuabaModel.RelatedType;
import br.ucam.kuabaSubsystem.kuabaModel.Role;
import br.ucam.kuabaSubsystem.kuabaModel.Solution;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.repositories.OwlApiFileGateway;
import java.lang.reflect.Constructor;
import java.util.GregorianCalendar;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 * @author Bruno
 */
public class OwlApiKuabaModelFactory implements KuabaModelFactory {
    
    private OWLDataFactory factory;
    private OWLOntology model;
    private OWLOntologyManager manager;
    private KuabaRepository repo;
    
    private static final String ONTOLOGY_URL = KuabaSubsystem.ONTOLOGY_URL;

    public OwlApiKuabaModelFactory(KuabaRepository repo) {
        this.repo = repo;
        model = (OWLOntology) repo.getModel();
        manager = model.getOWLOntologyManager();
        factory = manager.getOWLDataFactory();
    }
    

    public Idea createIdea(String id) {
        return createElement(id, Idea.class);
    }

    public Question createQuestion(String id) {
        return createElement(id, Question.class);
    }

    public Argument createArgument(String id) {
        return createElement(id, Argument.class);
    }

    public Decision createDecision(String id) {
        return createElement(id, Decision.class);
    }

    public Role createRole(String id) {
        return createElement(id, Role.class);
    }

    public AtomicArtifact createAtomicArtifact(String id) {
        return createElement(id, AtomicArtifact.class);
    }

    public CompositeArtifact createCompositeArtifact(String id) {
        return createElement(id, CompositeArtifact.class);
    }

    public ExpectedDuration createExpectedDuration(String id) {
        return createElement(id, ExpectedDuration.class);
    }

    public RelatedType createRelatedType(String id) {
        return createElement(id, RelatedType.class);
    }

    public FormalModel createFormalModel(String id) {
        return createElement(id, FormalModel.class);
    }

    public Justification createJustification(String id) {
        return createElement(id, Justification.class);
    }

    public Person createPerson(String id) {
        return createElement(id, Person.class);
    }

    public Activity createActivity(String id) {
        return createElement(id, Activity.class);
    }

    public Method createMethod(String id) {
        return createElement(id, Method.class);
    }
    
    public Solution createSolution(String id){
        return createElement(id,Solution.class);
    }
    
    private <T> T createElement(String individualId, Class<T> cls) {
        String indID = individualId.replaceAll("\\s|\\?", "_");
        OWLNamedIndividual i = factory.getOWLNamedIndividual(IRI.create(model.getOntologyID().getOntologyIRI().toString()+"#"+indID));
        OWLClass c = factory.getOWLClass(IRI.create(ONTOLOGY_URL+"#"+cls.getSimpleName()));
        OWLClassAssertionAxiom a = factory.getOWLClassAssertionAxiom(c, i);
        manager.addAxiom(model, a);
        
        //Asserting superclasses
        for (OWLClassExpression ce : c.getSuperClasses(OwlApiFileGateway.getInstance().getKuabaOntology())) {
            try {
                OWLClass cl = ce.asOWLClass();
//                System.out.println(cl.getIRI());
                OWLClassAssertionAxiom ax = factory.getOWLClassAssertionAxiom(cl, i);
                manager.addAxiom(model, ax);
            } catch (Exception e) {
                continue;
            }            
        }
        
        try {
                       
            Class cl = Class.forName("br.ucam.kuabaSubsystem.kuabaModel.impl.Default"+cls.getSimpleName()); 

            Class partypes[] = new Class[2];  
            partypes[0] = String.class;  
            partypes[1] = KuabaRepository.class;  

            Constructor ct = cl.getConstructor(partypes);  

            Object arglist[] = new Object[2];  
            arglist[0] = indID;  
            arglist[1] = repo;  

            Object retobj = ct.newInstance(arglist);

            if (ReasoningElement.class.isAssignableFrom(cls)) {
                ((ReasoningElement) retobj).setHasCreationDate(new GregorianCalendar());
            }
            
            KuabaEventPump.getInstance().notifyListerners(cls, (KuabaElement) retobj);
            return (T) retobj;
        } catch (Exception e) {
            System.err.println("ERRO em createElement -- "+e);
        }
        
        return null;
    }

    public KuabaRepository getKuabaRepository() {
        return this.repo;
    }
    
    
}
