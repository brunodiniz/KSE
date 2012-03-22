package br.ucam.kuabaSubsystem.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.hp.hpl.jena.util.FileUtils;

import br.ucam.kuabaSubsystem.core.ConfigurationManager;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.util.FileUtil;
//import edu.stanford.smi.protegex.owl.ProtegeOWL;
//import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
//import edu.stanford.smi.protegex.owl.repository.impl.LocalFolderRepository;

public class OwlFileGateway extends AbstractRepositoryGateway{

	private String localRepositoryName = "kuabaOntology";
	private File kuabaOntologyRepository = new File(this.localRepositoryName); 
//	@Override
//	public boolean save(KuabaRepository kr) {
//		try {
//			((JenaOWLModel)kr.getModel()).save(new FileOutputStream(new File(kr.getUrl().toString())), FileUtils.langXMLAbbrev, new ArrayList());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return true;
//	}
//	
//
//	public OwlFileGateway() {
//		super();
//	}
//
//
//	@Override
//	protected KuabaRepository createRepository(String url) {
//		//Creating a local repository with the directory of the file "KuabaOntology.owl".
//		//The local repositories are used to resolve the URI references on the imports clause.		
//		LocalFolderRepository lr = new LocalFolderRepository(this.kuabaOntologyRepository, true);		
//		File individualsHolder = new File(url);	
//		//Creating an empty JenaOwlModel.
//		 JenaOWLModel model = ProtegeOWL.createJenaOWLModel();
//	     
//		 //Adding the local repository created above to this JenaOWLModel.
//		 model.getRepositoryManager().addProjectRepository(lr);
//		 
//		 //Loading the model from the file "br.ucam.kuabaSubsystem.testBase.xml" that imports "KuabaOntology.owl".
//		 try {
//			model.load(new FileInputStream(individualsHolder), "");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		OwlRepository repository = new  OwlRepository(model);
//		repository.setUrl(url);
//		return repository;
//	}

	@Override
	protected KuabaRepository newRepository(String url) {	
				
		try {
			File modelFile = new File(url);
			File header = new File(this.localRepositoryName + "/header.txt");
			File eof = new File(this.localRepositoryName + "/EOF.txt");
			FileUtil.copyFile(header, modelFile);
			FileUtil.copyFile(eof, modelFile);			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return createRepository(url);
	}

    public boolean save(KuabaRepository kr, File destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public KuabaRepository createNewRepository() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public KuabaRepository createNewRepository(File destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public KuabaRepository createNewRepository(String url, File destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean save(KuabaRepository kr, File destination, String newBaseUrl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected KuabaRepository createRepository(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean save(KuabaRepository kr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	

}
