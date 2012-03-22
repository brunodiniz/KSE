/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.util;

import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Artifact;
import br.ucam.kuabaSubsystem.kuabaModel.CompositeArtifact;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.ExpectedDuration;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Method;
import br.ucam.kuabaSubsystem.kuabaModel.Person;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.RelatedType;
import br.ucam.kuabaSubsystem.kuabaModel.Role;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.repositories.RepositoryGateway;
import br.ucam.kuabaSubsystem.repositories.RepositoryLoadException;
import java.io.File;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class TemplateGenerator {
    
    
    public static KuabaRepository generate (RepositoryGateway gate, File destination) {
        KuabaRepository repo = gate.createNewRepository("http://www.tecweb.inf.puc-rio.br/DesignRationale/KuabaOntologyGeneratedTemplate.owl");
        
        if (repo == null) try {
            return gate.load(destination.getPath());
        } catch (RepositoryLoadException ex) {
            Logger.getLogger(TemplateGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        KuabaModelFactory factory = repo.getModelFactory();
        Activity diagram_domain_model = factory.createActivity("diagram_domain_model");
        Activity design_domain_model = factory.createActivity("design_domain_model");
        Argument model_athlete_as_class = factory.createArgument("model_athlete_as_class");
        Artifact cd_class = factory.createAtomicArtifact("cd_class");
        CompositeArtifact use_case_diagram = factory.createCompositeArtifact("use_case_diagram");
        CompositeArtifact class_diagram = factory.createCompositeArtifact("class_diagram");
        Decision model_cd_as_class = factory.createDecision("model_cd_as_class");
        Decision model_cd_as_attribute = factory.createDecision("model_cd_as_attribute");
        Decision person_is_a_model_element = factory.createDecision("person_is_a_model_element");
        Decision model_person_as_class = factory.createDecision("model_person_as_class");
        ExpectedDuration onemonth = factory.createExpectedDuration("one_month");
        ExpectedDuration twoweeks = factory.createExpectedDuration("two_weeks");
        FormalModel uml = factory.createFormalModel("UML");
        Idea clazz = factory.createIdea("class");
        Idea athlete = factory.createIdea("athlete");
        Idea attribute = factory.createIdea("attribute");
        Idea person = factory.createIdea("person");
        Idea email = factory.createIdea("email");
        Idea dog = factory.createIdea("dog");
        Method oo = factory.createMethod("OO");
        Method up = factory.createMethod("UP");
        Person pedro = factory.createPerson("pedro");
        Person manoel = factory.createPerson("manoel");
        Question how_model_athlete = factory.createQuestion("how_model_athlete");
        Question how_model_person = factory.createQuestion("how_model_person");
        Question what_elements = factory.createQuestion(Question.ROOT_QUESTION_ID); 
        Question how_model_email = factory.createQuestion("how_model_email");
        Question email_owner = factory.createQuestion("email_owner");
        Question how_model_cd = factory.createQuestion("how_model_cd");
        RelatedType include = factory.createRelatedType("include");
        Role dev = factory.createRole("developer");       
        
        //activities
        diagram_domain_model.setHasDescription("this Activity focus on domain model design");
        diagram_domain_model.setHasFinishDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));
        diagram_domain_model.setHasStartDate(new GregorianCalendar(2008, 6, 14, 8, 30, 0));
        diagram_domain_model.setHasName("Domain model design");
        
        diagram_domain_model.addIsExecutedBy(pedro);
        diagram_domain_model.setHasExpectedDuration(twoweeks);
        diagram_domain_model.addRequires(dev);
        diagram_domain_model.addInvolves(clazz);
        diagram_domain_model.addInvolves(athlete);
        diagram_domain_model.addInvolves(how_model_athlete);
        diagram_domain_model.addInvolves(what_elements);
        diagram_domain_model.addInvolves(model_athlete_as_class);
        
        design_domain_model.setHasDescription("designing domain model");
        design_domain_model.setHasFinishDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));
        design_domain_model.setHasStartDate(new GregorianCalendar(2008, 6, 14, 8, 30, 0));
        design_domain_model.setHasName("Design Domain model");
        
        //arguments
        model_athlete_as_class.setHasText("An athlete element involves multiple fields");
        model_athlete_as_class.setHasCreationDate(new GregorianCalendar(2008, 4, 8, 16, 50, 3));
        
        model_athlete_as_class.setConsiders(what_elements);
        model_athlete_as_class.addInFavorOf(athlete);
        model_athlete_as_class.addObjectsTo(attribute);
        
        //artifacts
        cd_class.setHasDescription("A CD class");
        cd_class.setHasName("CD class");
        cd_class.setHasUrl("http://kuaba-project/googlecode/svn");
        cd_class.setHasCreationDate(new GregorianCalendar(2008, 6, 21, 20, 22, 0));
        
        cd_class.addIsCreatedBy(pedro);
        cd_class.addIsDescribedBy(uml);
        
        //composite artifacts
        use_case_diagram.addCompositionOf(cd_class);
        
        class_diagram.addCompositionOf(cd_class);
        
        //decisions
        model_cd_as_class.setIsAccepted(false);
        model_cd_as_class.setHasDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));
        
        model_cd_as_attribute.setIsAccepted(true);
        model_cd_as_attribute.setHasDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));
        model_cd_as_attribute.setConcludes(attribute);
        
        person_is_a_model_element.setIsAccepted(true);
        person_is_a_model_element.setHasDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));
        person_is_a_model_element.setConcludes(person);
        
        model_person_as_class.setIsAccepted(true);
        model_person_as_class.setHasDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));
        model_person_as_class.setConcludes(clazz);
        
        //expected durations
        onemonth.setHasAmount(30);
        onemonth.addHasUnitTime(ExpectedDuration.DAY);
        
        twoweeks.setHasAmount(2);
        twoweeks.addHasUnitTime(ExpectedDuration.WEEK);
        
        //formal models
        uml.setHasName("Unified Modeling Language");
        uml.addHasLocalization("www.omg.org");
        
        //ideas
        clazz.setHasText("Class");
        clazz.setHasCreationDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));
        clazz.addIsConcludedBy(model_person_as_class);
        clazz.addAddress(how_model_person);
        clazz.setIsDefinedBy(uml);
        
        person.setHasText("Person");
        person.setHasCreationDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));       
        person.addSuggests(how_model_person);
        person.addAddress(what_elements);
        
        attribute.setHasText("Attribute");
        attribute.setHasCreationDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));       
        attribute.addSuggests(email_owner);
        attribute.addAddress(how_model_email);
        attribute.setIsDefinedBy(uml);
        
        email.setHasText("email");
        email.setHasCreationDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));       
        email.addSuggests(how_model_email);
        email.addAddress(what_elements);
        
        athlete.setHasText("Athlete");
        athlete.setHasCreationDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));
        
        dog.setHasText("Dog");
        dog.setHasCreationDate(new GregorianCalendar(2008, 6, 21, 20, 22, 3));
        
        //Methods
        oo.setHasName("Object Oriented");
        
        up.setHasName("Unified Process");
        
        //persons
        pedro.setHasName("pedro");
        pedro.addHasAddress("Campos");
        pedro.addHasAddress("Amaro Silveira");
        pedro.addHasAddress("Rua Domingos Andretti n° 35");       
        pedro.addHasTelephone("2227225121");
        pedro.addHasTelephone("2227235334");
        pedro.addHasTelephone("2227350817");
        pedro.addHasEmail("thiagonunes@gmail.com");
        pedro.addHasEmail("slash_thiago@yahoo.com.br");
        pedro.addHasEmail("thiagorinu@gmail.com");
        pedro.addExecutes(diagram_domain_model);
        
        manoel.setHasName("manoel");
        manoel.addHasAddress("Campos");
        manoel.addHasAddress("Amaro Silveira");
        manoel.addHasAddress("Rua Domingos Andretti n° 35");       
        manoel.addHasTelephone("2227225121");
        manoel.addHasTelephone("2227235334");
        manoel.addHasTelephone("2227350817");
        manoel.addHasEmail("thiagonunes@gmail.com");
        manoel.addHasEmail("slash_thiago@yahoo.com.br");
        manoel.addHasEmail("thiagorinu@gmail.com");
        
        //questions
        how_model_cd.setHasType(Question.XORTYPE);
        how_model_cd.setIsDefinedBy(uml);
        
        what_elements.setHasType(Question.ORTYPE);
        what_elements.setHasText("What are the Model Elements?");
        what_elements.setIsDefinedBy(uml);
        what_elements.addIsAddressedBy(person);
        what_elements.addIsAddressedBy(email);
        what_elements.addHasDecision(person_is_a_model_element);
        
        how_model_athlete.setHasType(Question.XORTYPE);
        how_model_athlete.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"athlete");
        
        how_model_person.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"Person?");
        how_model_person.addIsSuggestedBy(person);
        how_model_person.addIsAddressedBy(clazz);
        how_model_person.addHasDecision(model_person_as_class);
        
        how_model_email.setHasText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"email?");
        how_model_email.addIsAddressedBy(attribute);
        how_model_email.addIsSuggestedBy(email);
        
        email_owner.setHasText("Owner?");
        email_owner.addIsAddressedBy(clazz);
        email_owner.addIsSuggestedBy(attribute);
        
        //related types
        include.setHasRelationType("include");
        
        //roles
        dev.setHasName("developer");
        
        //saving...
        if (destination!=null) gate.save(repo, destination);
        
        return repo;
    }
    
    public static void generateRootQuestion(KuabaRepository repo) {
        FormalModel uml = repo.getModelFactory().createFormalModel(FormalModel.UML_FORMAL_MODEL_ID);
        uml.setHasName("Unified Modeling Language");
        Question what_elements = repo.getModelFactory().createQuestion(Question.ROOT_QUESTION_ID);
        what_elements.setHasType(Question.ORTYPE);
        what_elements.setHasText("What are the Model Elements?");
        what_elements.setIsDefinedBy(uml);
    }
}
