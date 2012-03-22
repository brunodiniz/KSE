package br.ucam.kuabaSubsystem.kuabaModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.*;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;
import br.ucam.kuabaSubsystem.util.FileUtil;

//import com.hp.hpl.jena.util.FileUtils;


//import edu.stanford.smi.protegex.owl.ProtegeOWL;
//import edu.stanford.smi.protegex.owl.jena.Jena;
//import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
//import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
//import edu.stanford.smi.protegex.owl.model.RDFProperty;
//import edu.stanford.smi.protegex.owl.repository.impl.LocalFolderRepository;

import junit.framework.TestCase;

public class PersonTest extends AbstractKuabaTestCase{
	
	private Person thiago;
	
	@Override
	protected void setUp() throws Exception {		
		super.setUp();
		this.thiago = this.factory.createPerson("thiago");
	}	
	
	public void test_add_activity(){
		Activity ac = this.repo.getActivity("diagram_domain_model");
		Person pedro = this.repo.getPerson("pedro");
		//pedro.addExecutes(ac);
		ac.addIsExecutedBy(pedro);
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		//Jena.saveOntModel(model, this.testKuabaKnowlegeBase, model.getOntModel(), "saved");
	}	
	
	public void test_get_activity_domain_model_design(){
		Person pedro = this.repo.getPerson("pedro");
		
		assertFalse(pedro.getExecutes() == null);
		assertFalse(pedro.getExecutes().isEmpty());
		
		Activity dd = (Activity)pedro.getExecutes().iterator().next();
		
		assertEquals("Domain model design", dd.getHasName());
	}
	
	public void test_addHasAddress() throws FileNotFoundException, Exception {
		
		this.thiago.addHasAddress("Rua Domingos Andretti n� 35");
		this.thiago.addHasAddress("Pq california");
		this.thiago.addHasAddress("Brasil");
		
		Collection<String> address = this.thiago.getHasAddress();
		
		assertTrue(this.thiago.hasHasAddress());
		assertEquals(3, address.size());
		assertTrue(address.contains("Rua Domingos Andretti n� 35"));
		assertTrue(address.contains("Pq california"));
		assertTrue(address.contains("Brasil"));
		
		//Saving the Ontology Model on file instances.xml.
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());		 
//		 
//		 //Loading the model again to guarantee that the addresses was really saved.		 		 
//		 JenaOWLModel otherModel = ProtegeOWL.createJenaOWLModel();
//		 otherModel.getRepositoryManager().addProjectRepository(this.lr);
//		 otherModel.load(new FileInputStream(testKuabaKnowlegeBase), "oi");
		 
		 //Creating other Factory with the otherModel.
//		 MyFactory otherFactory = new MyFactory(otherModel);
		 
		 //Loading the Person "thiago" again
		 Person loadedPerson = repo.getPerson("thiago");
		 
		 //Loading the address from the Person "thiago"
		 Collection<String> loadedAddress = this.thiago.getHasAddress();
		 
		 //testing again
		 assertTrue(loadedPerson.hasHasAddress());
		 assertEquals(3, address.size());
		 assertTrue(loadedAddress.contains("Rua Domingos Andretti n� 35"));
		 assertTrue(loadedAddress.contains("Pq california"));
		 assertTrue(loadedAddress.contains("Brasil"));		 
//		 otherModel.dispose();
	}
	
	public void test_add_repeated_address(){
		this.thiago.addHasAddress("Rua Domingos Andretti n� 35");
		this.thiago.addHasAddress("Rua Domingos Andretti n� 35");
		this.thiago.addHasAddress("Rua Domingos Andretti n� 35");
		
		Collection<String> address = this.thiago.getHasAddress();
		assertTrue(this.thiago.hasHasAddress());
		assertEquals(1, address.size());
		assertTrue(address.contains("Rua Domingos Andretti n� 35"));
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		
	}
	
	public void test_listAddress(){
		
		Person pedro = this.repo.getPerson("pedro");
		assertEquals("pedro", pedro.getHasName());		
		
		Iterator<String> addressIterator = pedro.listHasAddress();
		
		assertFalse(addressIterator == null);
		assertEquals("Campos", addressIterator.next());
		assertEquals("Amaro Silveira", addressIterator.next());
		assertEquals("Rua Domingos Andretti n° 35", addressIterator.next());
		
		//The addressIterator have only 3 elements, so, a call for a next element
		//must throw a NoSuchElementException.
		
		try {
			addressIterator.next();
			assertTrue(false);
		} catch (NoSuchElementException e) {			
			assertTrue(true);
		}
	}
	
	public void test_removeHasAddress(){
		Person pedro = this.repo.getPerson("pedro");
		
		Collection<String> address = pedro.getHasAddress();
		
		assertTrue(pedro.hasHasAddress());
		assertEquals(3, address.size());
		assertTrue(address.contains("Campos"));
		assertTrue(address.contains("Amaro Silveira"));
		assertTrue(address.contains("Rua Domingos Andretti n° 35"));
		
		pedro.removeHasAddress("Campos");
		address = pedro.getHasAddress();
		assertEquals(2, address.size());
		assertTrue(!address.contains("Campos"));
		assertTrue(address.contains("Amaro Silveira"));
		assertTrue(address.contains("Rua Domingos Andretti n° 35"));
	
		pedro.removeHasAddress("Campos");
	}
	
	public void test_setHasAddress(){
		
		Person pedro = this.repo.getPerson("pedro");
		
		List<String> newAddress = new ArrayList<String>();
		newAddress.add("California");
		newAddress.add("USA");
		
		pedro.setHasAddress(newAddress);
		
		Collection<String> addresses = pedro.getHasAddress();
		assertTrue(addresses.contains("USA"));
		assertTrue(addresses.contains("California"));		
		
	}
	
	public void test_setHasName(){
		
		Person pedro = this.repo.getPerson("pedro");
		pedro.setHasName("thiago");
		
		assertEquals("thiago", pedro.getHasName());
		
	}	
	
	/**public void test_setting_name_more_than_80_chars(){
		
		Person iva = this.factory.createPerson("");
		assertTrue(iva.saveOnModel());
		iva.setName("asdkfjasldkfjhaslkdfhaslkdfhalskdfhalsdkfhalskdjfhalksdhflakd" +
				"asdfalksdjfaklsdfhalksdhfalksdhfalksdfhalskdfhalksdfhaskdfjhadksfh"+
				 "asdfaslkdfjasldkjfal�sdkjfa�sldkjfa�sldkjf�alsdjfa�dlsfja�ldfja�ls");
		assertFalse(iva.saveOnModel());
		
		List<Error> nameErrors = iva.getErrors("nome");
		
		assertEquals("The name field must be fewer than 80 chars", nameErrors.get(0));
	}**/
	
	/**public void test_create_person_without_name(){
		
		Person iva = this.factory.createPerson("");
		assertFalse(iva.saveOnModel());
		
		List<Error> nameErrors = iva.getErrors("name");
		
		assertEquals("The name field can't be empty or whitespaces", nameErrors.get(0));
	}**/
	
	public void test_addHasTelephone() throws FileNotFoundException, Exception {
		
		this.thiago.addHasTelephone("2227350817");
		this.thiago.addHasTelephone("2227235334");
		this.thiago.addHasTelephone("2227225121");
		
		Collection<String> tels = this.thiago.getHasTelephone();
		
		assertTrue(this.thiago.hasHasTelephone());
		assertEquals(3, tels.size());
		assertTrue(tels.contains("2227350817"));
		assertTrue(tels.contains("2227235334"));
		assertTrue(tels.contains("2227225121"));
		
//		//Saving the Ontology Model on file instances.xml.
//		 this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
//		 
//		 
//		 //Loading the model again to guarantee that the addresses was really saved.		 		 
//		 JenaOWLModel otherModel = ProtegeOWL.createJenaOWLModel();
//		 otherModel.getRepositoryManager().addProjectRepository(this.lr);
//		 otherModel.load(new FileInputStream(testKuabaKnowlegeBase), "oi");
		 
		 //Creating other Factory with the otherModel.
//		 MyFactory otherFactory = new MyFactory(otherModel);
		 
		 //Loading the Person "thiago" again
		 Person loadedPerson = repo.getPerson("thiago");
		 
		 //Loading the address from the Person "thiago"
		 Collection<String> loadedTels = this.thiago.getHasTelephone();
		 
		 //testing again
		 assertTrue(loadedPerson.hasHasTelephone());
		 assertEquals(3, tels.size());
		 assertTrue(loadedTels.contains("2227350817"));
		 assertTrue(loadedTels.contains("2227235334"));
		 assertTrue(loadedTels.contains("2227225121"));		 
		
	}
	
	/**public void test_add_telephone_without_code(){
		
		Person iva = this.factory.createPerson("Iva");		
		//Telephone without code
		iva.addHasTelephone("27350817");
		
		assertFalse(iva.saveOnModel());
		
		List<Error> nameErrors = iva.getErrors("telephone");
		
		assertEquals("Code missing! Please enter with telephone code.", nameErrors.get(0));		
	}
	
	public void test_add_telephone_with_wrong_format(){
		
		Person iva = this.factory.createPerson("Iva");		
		//Telephone without code
		iva.addHasTelephone("350817");
		
		assertFalse(iva.saveOnModel());
		
		List<Error> nameErrors = iva.getErrors("telephone");
		
		assertEquals("Wrong format of telephone.", nameErrors.get(0));		
		
	}**/
	
	public void test_add_repeated_Telephones(){
		this.thiago.addHasTelephone("2227350817");
		this.thiago.addHasTelephone("2227350817");
		this.thiago.addHasTelephone("2227350817");
		
		Collection<String> tels = this.thiago.getHasTelephone();
		assertTrue(this.thiago.hasHasTelephone());
		assertEquals(1, tels.size());
		assertTrue(tels.contains("2227350817"));
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		
	}
	
	public void test_listTelephone(){
		
		Person pedro = this.repo.getPerson("pedro");
		assertEquals("pedro", pedro.getHasName());		
		
		Iterator<String> telIterator = pedro.listHasTelephone();
		
		assertFalse(telIterator == null);
		assertEquals("2227225121", telIterator.next());
		assertEquals("2227235334", telIterator.next());
		assertEquals("2227350817", telIterator.next());		
		
		//The telIterator have only 3 elements, so, a call for a next element
		//must throw a NoSuchElementException.
		try {
			telIterator.next();
			assertTrue(false);
		} catch (NoSuchElementException e) {			
			assertTrue(true);
		}
	}
	
	public void test_removeHasTelephone(){
		Person pedro = this.repo.getPerson("pedro");
		
		Collection<String> tels = pedro.getHasTelephone();
		
		assertTrue(pedro.hasHasTelephone());
		assertEquals(3, tels.size());
		assertTrue(tels.contains("2227350817"));
		assertTrue(tels.contains("2227235334"));
		assertTrue(tels.contains("2227225121"));
		
		pedro.removeHasTelephone("2227350817");
		tels = pedro.getHasTelephone();
		assertEquals(2, tels.size());
		assertTrue(!tels.contains("2227350817"));
		assertTrue(tels.contains("2227235334"));
		assertTrue(tels.contains("2227225121"));
	
		pedro.removeHasTelephone("2227350817");
	}
	
	public void test_setHasTelephone(){
		
		Person pedro = this.repo.getPerson("pedro");
		
		List<String> newTels = new ArrayList<String>();
		newTels.add("2227235334");
		newTels.add("2227343132");
		
		pedro.setHasTelephone(newTels);
		
		Collection<String> tels = pedro.getHasTelephone();
		assertTrue(tels.contains("2227235334"));
		assertTrue(tels.contains("2227343132"));		
		
	}
	
	/**public void test_email_with_wrong_format(){
		this.thiago.addHas_Email("qualquercoisa");
		assertFalse(this.thiago.saveOnModel());
		
		List<Error> emailErrors = this.thiago.getErrors("email");
		
		assertEquals("Formato inv�lido de email.", emailErrors.get(0));
		
		this.thiago.removeHas_Email("qualquercoisa");
		this.thiago.addHas_Email("thiagorinu@gmail.com");
		
		assertTrue(this.thiago.saveOnModel);
	}
	
	public void test_email_without_arroba(){
		this.thiago.addHas_Email("qualquercoisa.gmail.com");
		assertFalse(this.thiago.saveOnModel());
		
		List<Error> emailErrors = this.thiago.getErrors("email");
		
		assertEquals("Formato inv�lido de email.", emailErrors.get(0));
		
		this.thiago.removeHas_Email("qualquercoisa.gmail.com");
		this.thiago.addHas_Email("thiagorinu@gmail.com");
		
		assertTrue(this.thiago.saveOnModel);
	}
	
	public void test_email_without_four_points(){
		this.thiago.addHas_Email("qualquercoisa@gmail.com.br.edu.ucam.ola");
		assertFalse(this.thiago.saveOnModel());
		
		List<Error> emailErrors = this.thiago.getErrors("email");
		
		assertEquals("Formato inv�lido de email.", emailErrors.get(0));
		
		this.thiago.removeHas_Email("qualquercoisa@gmail.com.br.edu.ucam.ola");
		this.thiago.addHas_Email("thiagorinu@gmail.com");
		
		assertTrue(this.thiago.saveOnModel);
	}**/
	
	public void test_addHasE_mail() throws FileNotFoundException, Exception {
		
		this.thiago.addHasEmail("thiagorinu@gmail.com");
		this.thiago.addHasEmail("slash_thiago@yahoo.com.br");
		this.thiago.addHasEmail("thiagonunes@gmail.com");
		
		Collection<String> emails = this.thiago.getHasEmail();
		
		assertTrue(this.thiago.hasHasEmail());
		assertEquals(3, emails.size());
		assertTrue(emails.contains("thiagorinu@gmail.com"));
		assertTrue(emails.contains("slash_thiago@yahoo.com.br"));
		assertTrue(emails.contains("thiagonunes@gmail.com"));
		
		//Saving the Ontology Model on file instances.xml.
//		 this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
//		 
//		 
//		 //Loading the model again to guarantee that the addresses was really saved.		 		 
//		 JenaOWLModel otherModel = ProtegeOWL.createJenaOWLModel();
//		 otherModel.getRepositoryManager().addProjectRepository(this.lr);
//		 otherModel.load(new FileInputStream(testKuabaKnowlegeBase), "oi");
		 
		 //Creating other Factory with the otherModel.
//		 MyFactory otherFactory = new MyFactory(otherModel);
		 
		 //Loading the Person "thiago" again
//		 Person loadedPerson = otherFactory.getPerson("thiago");
                Person loadedPerson = repo.getPerson("thiago");
		 
		 //Loading the address from the Person "thiago"
		 Collection<String> loadedEmails = this.thiago.getHasEmail();
		 
		 //testing again
		 assertTrue(loadedPerson.hasHasEmail());
		 assertEquals(3, loadedEmails.size());
		 assertTrue(loadedEmails.contains("thiagorinu@gmail.com"));
		 assertTrue(loadedEmails.contains("slash_thiago@yahoo.com.br"));
		 assertTrue(loadedEmails.contains("thiagonunes@gmail.com"));		 
		
	}
	
	public void test_add_repeated_emails(){
		
		this.thiago.addHasEmail("thiagorinu@gmail.com");
		this.thiago.addHasEmail("thiagorinu@gmail.com");
		this.thiago.addHasEmail("thiagorinu@gmail.com");
		
		Collection<String> emails = this.thiago.getHasEmail();
		assertTrue(this.thiago.hasHasEmail());
		assertEquals(1, emails.size());
		assertTrue(emails.contains("thiagorinu@gmail.com"));
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		
	}
	
	public void test_listHas_email(){
		
		Person pedro = this.repo.getPerson("pedro");
		assertEquals("pedro", pedro.getHasName());		
		
		Collection<String> emails = pedro.getHasEmail();
		
		assertTrue(emails.size()==3);
		assertTrue(emails.contains("thiagonunes@gmail.com"));
		assertTrue(emails.contains("slash_thiago@yahoo.com.br"));		
		assertTrue(emails.contains("thiagorinu@gmail.com"));
		
		//The telIterator have only 3 elements, so, a call for a next element
		//must throw a NoSuchElementException.
//		try {
//			emails.next();
//			assertTrue(false);
//		} catch (NoSuchElementException e) {			
//			assertTrue(true);
//		}
	}
	
	public void test_removeHas_Email(){
		Person pedro = this.repo.getPerson("pedro");
		
		Collection<String> emails = pedro.getHasEmail();
		
		assertTrue(pedro.hasHasEmail());
		assertEquals(3, emails.size());		
		
		assertTrue(emails.contains("thiagorinu@gmail.com"));
		assertTrue(emails.contains("slash_thiago@yahoo.com.br"));
		assertTrue(emails.contains("thiagonunes@gmail.com"));
		
		pedro.removeHasEmail("thiagorinu@gmail.com");
		emails = pedro.getHasEmail();
		assertEquals(2, emails.size());
		assertTrue(!emails.contains("thiagorinu@gmail.com"));
		assertTrue(emails.contains("slash_thiago@yahoo.com.br"));
		assertTrue(emails.contains("thiagonunes@gmail.com"));
	
		pedro.removeHasEmail("thiagorinu@gmail.com");
	}
	
	public void test_setHas_Email(){
		
		Person pedro = this.repo.getPerson("pedro");
		
		List<String> newEmails = new ArrayList<String>();
		newEmails.add("email4@gmail.com");
		newEmails.add("email5@gmail.com");
		
		pedro.setHasEmail(newEmails);
		
		Collection<String> emails = pedro.getHasEmail();
		assertTrue(emails.contains("email4@gmail.com"));
		assertTrue(emails.contains("email5@gmail.com"));		
		
	}


	
}
