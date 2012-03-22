package br.ucam.kuabaSubsystem.abstractTestCase;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.net.URI;



import br.ucam.kuabaSubsystem.kuabaModel.*;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;
import br.ucam.kuabaSubsystem.kuabaModel.impl.OwlApiKuabaModelFactory;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.repositories.OwlApiFileGateway;
import br.ucam.kuabaSubsystem.repositories.OwlApiKuabaRepository;
import br.ucam.kuabaSubsystem.repositories.OwlFileGateway;
import br.ucam.kuabaSubsystem.util.FileUtil;


import junit.framework.TestCase;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class AbstractOwlApiKuabaTestCase extends TestCase {
    
//	public static String TEST_BASE_PACKAGE = "test/br/ucam/kuabaSubsystem/testBase/" ;
        public static final String ROOT = "/Users/Bruno/Documents/NetBeansProjects/Kuaba/KuabaSubystem/";
        public static final String TEST_BASE_PACKAGE = ROOT+"test/br/ucam/kuabaSubsystem/testBase/";
//        public static final String ONTOLOGY_URI = "http://www.tecweb.inf.puc-rio.br/DesignRationale/KuabaOntology.owl";
        public static final String ONTOLOGY_URI = "http://www.progen.kilu.de/DesignRationale/KuabaOntology.owl";
	/**
	 * factory: the factory object to instantiate the Kuaba model classes.  
	 */
	protected KuabaModelFactory factory;
	
	/**
	 * lr: the Local Repository that holds the KuabaOntology.owl. This attribute
	 * indicates the local path of the Kuaba Ontology file that is necessary to
	 * resolve import clauses on the header.txt file.
	 */
//	protected LocalFolderRepository lr;
	
	/**
	 * br.ucam.kuabaSubsystem.testBase: this is the file that hold all the owl individuals
	 * specified on the files on the "Test/br.ucam.kuabaSubsystem.fixtures" path.
	 * This File represents the test base. It is cleared and filled before the 
	 * execution of any test method. This way, the execution of a test do not
	 * affect another test execution. 
	 */
	protected File testKuabaKnowlegeBase;
	
	/**
	 * br.ucam.kuabaSubsystem.fixtures: this attribute is the array of all br.ucam.kuabaSubsystem.fixtures files contained in
	 * the "Text/br.ucam.kuabaSubsystem.fixtures/" directory.  
	 */
	protected File[] fixtures;
	
        protected KuabaRepository repo;
	
	/**
	 * the setUp() method is executed before any test method execution.
	 */
	@Override
	protected void setUp() throws Exception {
		
		//Getting an array of all br.ucam.kuabaSubsystem.fixtures files		
		File fixturesDirectory = new File(ROOT+"test/br/ucam/kuabaSubsystem/fixtures/");
		//Filtering only files. Directories must be ignored.
		this.fixtures = fixturesDirectory.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory())
					return false;
				return true;
			}			
		});
		
		//Referencing the file "br.ucam.kuabaSubsystem.testBase.xml" that will hold all individuals contained in
		//br.ucam.kuabaSubsystem.fixtures files located at "Test/br.ucam.kuabaSubsystem.fixtures/" directory.
		 testKuabaKnowlegeBase = new File(TEST_BASE_PACKAGE+"testOwlApiKuabaKnowlegeBase.xml");
		 
		 //Clearing the "br.ucam.kuabaSubsystem.testBase.xml" file
//		 FileUtil.clearFile(testKuabaKnowlegeBase);		
//		 
//		//Coping the headers and br.ucam.kuabaSubsystem.fixtures files content to "br.ucam.kuabaSubsystem.testBase.xml".		 
//		 FileUtil.copyFile(new File(ROOT+"test/br/ucam/kuabaSubsystem/fixtures/headers/header.txt"), this.testKuabaKnowlegeBase);
//		 for (File fixture : this.fixtures) {
//			FileUtil.copyFile(fixture, this.testKuabaKnowlegeBase);
//		 }
//		 FileUtil.copyFile(new File(ROOT+"test/br/ucam/kuabaSubsystem/fixtures/headers/EOF.txt"), this.testKuabaKnowlegeBase);
//		 
//		 //creating Local Repository
//		 File kuabaRepository = new File("kuabaOntology/");
//		 System.out.println("Path: " + kuabaRepository.getAbsolutePath());
//		 assert kuabaRepository.exists();
//		 
//		 //Creating a local repository with the directory of the file "KuabaOntology.owl".
//		 //The local repositories are used to resolve the URI references on the imports clause.
//		 this.lr = new LocalFolderRepository(kuabaRepository, true);
//		 
//		 System.out.println("lista vazia? " + lr.getOntologies().isEmpty());
//		 System.out.println("Contêm KuabaOntology " + lr.contains(URI.create(ONTOLOGY_URI)));
		 
		 OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//                 model = manager.loadOntologyFromOntologyDocument(testKuabaKnowlegeBase);
                 this.repo = OwlApiFileGateway.getInstance().load(TEST_BASE_PACKAGE+"testOwlApiKuabaKnowlegeBase.xml");
		 this.factory = repo.getModelFactory();		 
	}
	
	/**
	 * Executed after a test method execution.
	 */
	@Override
	protected void tearDown() throws Exception {
	
			
			
	}
	public void test(){
		
	}
	

}
