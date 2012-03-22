package br.ucam.kuabaSubsystem.controller;

import br.ucam.kuabaSubsystem.repositories.RepositoryLoadException;
import java.io.File;
import java.io.IOException;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.util.Sequence;
import br.ucam.kuabaSubsystem.util.SequenceGenerator;
import br.ucam.kuabaSubsystem.util.SequenceGeneratorImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractController {
	protected KuabaRepository modelRepository;
	protected KuabaModelFactory modelFactory;
	protected Sequence modelSequence;
	
	public AbstractController() {
		super();
		String modelUrl = KuabaSubsystem.getProperty(
				KuabaSubsystem.REPOSITORY_PATH);
                
                try {
                    this.modelRepository = KuabaSubsystem.gateway.load(modelUrl);
                } catch (RepositoryLoadException ex) {
                    Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
		this.modelFactory = this.modelRepository.getModelFactory();
		
		String fileSequencesPath = KuabaSubsystem.SEQUENCES_FILE_PATH;
		try {
			this.loadSequence(new File(fileSequencesPath));
		} catch (IOException e1) {		
			e1.printStackTrace();
			System.exit(0);
		}
	}
	
	private void loadSequence(File sequencesFile) throws IOException{		
		
		SequenceGenerator generator = new SequenceGeneratorImpl(sequencesFile);
		String modelSequenceName = KuabaSubsystem.MODEL_SEQUENCE_NAME;
		Sequence seq = null;
		if(generator.loadSequence(modelSequenceName) == null)
			seq = generator.createNewSequence(modelSequenceName, 0, 1);
		else
			seq = generator.loadSequence(modelSequenceName);
		this.modelSequence = seq;
	}
	
	
	

}
