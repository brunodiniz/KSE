package br.ucam.kuabaSubsystem.core;

import java.io.File;
import java.util.Properties;

import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.api.xmi.XMIReferenceResolver;

import br.ucam.kuabaSubsystem.kuabaFacades.DefaultXmiIdResolver;
import br.ucam.kuabaSubsystem.kuabaFacades.KuabaFacade;
import br.ucam.kuabaSubsystem.kuabaFacades.KuabaSession;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.KuabaEventPump;
import br.ucam.kuabaSubsystem.observers.uml.core.ModelObserver;
import br.ucam.kuabaSubsystem.repositories.OwlApiFileGateway;
import br.ucam.kuabaSubsystem.repositories.RepositoryGateway;
import br.ucam.kuabaSubsystem.kuabaFacades.XMIIdResolver;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;


public class KuabaSubsystem {
        public static final String ONTOLOGY_URL = "http://www.progen.kilu.de/DesignRationale/KuabaOntology.owl";
    
	public static final String WORKSPACE = "/Users/Bruno/Documents/NetBeansProjects/Kuaba/";
	public static final String PARSER_PACK = "br.ucam.kuabaSubsystem.mofParser";
	
	public static final String METAMODEL_PATH = "mof/01-02-15_Diff.xml";
	
	public static final String TEMPLATES_REPOSITORY_PATH = "templates/" +
			"formalModelsTemplate.xml";
	
	public static final String REPOSITORY_PATH = "test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml";
	public static final String ID_RESOLVER = "IdResolver";
	
	public static final String SEQUENCES_FILE_PATH = "sequences/sequences.txt";
	
	public static final String MODEL_SEQUENCE_NAME = "mainSequence";
	private static KuabaSession session;	 
	private static Properties properties;
	public static XMIIdResolver resolver;	
	public static KuabaFacade facade;
	public static KuabaModelFactory factory;	
	public static RepositoryGateway gateway;
	public static KuabaSubsystem instance = new KuabaSubsystem();
	public static KuabaEventPump eventPump;	
//        private static KuabaRepository currentRepo;
	
	private KuabaSubsystem() {
		// TODO Auto-generated constructor stub
	}
	public static KuabaSession getSession(){
		return session;
	}
	
	public static void init(ConfigurationManager configurator, KuabaEventPump eventPump){
                OwlApiFileGateway.getInstance().disposeAll();
		properties = configurator.getConfigurationProperties();	
		gateway = OwlApiFileGateway.getInstance();
		File modelRepositoryFile = new File(properties.getProperty(KuabaSubsystem.REPOSITORY_PATH));
		if(!modelRepositoryFile.exists() || properties.getProperty(KuabaSubsystem.REPOSITORY_PATH).equals("designRationale/temp.xml"))
			gateway.createNewRepository(modelRepositoryFile);
		
		session = new KuabaSession();
		facade = new KuabaFacade();
		if(!configurator.getConfigurationProperties().containsKey(
				ID_RESOLVER))
			resolver = new DefaultXmiIdResolver();
		else
			resolver = (XMIIdResolver)configurator.getConfigurationProperties(
					).get(ID_RESOLVER);		
		KuabaSubsystem.eventPump = eventPump;
		//KuabaSubsystem.eventPump.addModelObserver(new EventFilter(new ModelObserver()));
		facade.formalModelAdded(FormalModels.UML_CLASS_DIAGRAM);
                
                
                
                //gambiarra pra fazer refresh no cache de ideias
                facade.modelRepository().getDomainIdeas();
	}
        
	public static String getProperty(String propertyType){
		return properties.getProperty(propertyType);
		
	}
	public void configure(ConfigurationManager configurator){
		
		
	}

	public KuabaFacade getFacade() {
		return facade;
	}
	

	public KuabaModelFactory getFactory() {
		return factory;
	}	

	

	public static KuabaSubsystem getInstance() {
		return instance;
	}
        
        public static boolean saveDRSession(File argoProjectFile) {
//            File drFile = new File(argoProjectFile.getAbsolutePath()+".owl");
            File drFile = new File("designRationale/"+argoProjectFile.getName()+".xml");
            return facade.saveSession(facade.modelRepository(), drFile);
//            return gateway.save(facade.modelRepository(), drFile);
        }

}
