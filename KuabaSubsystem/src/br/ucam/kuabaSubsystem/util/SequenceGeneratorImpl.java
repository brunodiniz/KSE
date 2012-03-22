package br.ucam.kuabaSubsystem.util;

import java.io.File;
import java.io.IOException;

import br.ucam.kuabaSubsystem.kuabaExceptions.SequenceNameAlreadyExistsException;
import br.ucam.kuabaSubsystem.kuabaExceptions.SequencesFileNotFoundException;



public class SequenceGeneratorImpl implements SequenceGenerator {

	private File sequencesFile;
	
	
	public SequenceGeneratorImpl(File sequencesFile) {
		super();
		this.sequencesFile = sequencesFile;
	}

	@Override
	public Sequence createNewSequence(String sequenceName, long init, int step)
	throws IOException{
		
		if(FileUtil.seekLine(this.sequencesFile, sequenceName) >= 0)
			throw new SequenceNameAlreadyExistsException("A sequence with name '"+
					sequenceName +"' already exists!");
		FileUtil.addContent(this.sequencesFile, sequenceName);
		FileUtil.addContent(this.sequencesFile, step + "");
		FileUtil.addContent(this.sequencesFile, init + "");
		
		int line = FileUtil.seekLine(this.sequencesFile, sequenceName);
		line = line + 2;
		Sequence seq = new Sequence(sequenceName, init, this.sequencesFile,line, step);
		return seq;
	}

	@Override
	public Sequence loadSequence(String sequenceName) throws IOException {
		
		int line = FileUtil.seekLine(this.sequencesFile, sequenceName);
		
		if(line < 0)
			return null;
		
		int step = Integer.parseInt(FileUtil.getLineContent(this.sequencesFile, line + 1));
		int currVal = Integer.parseInt(FileUtil.getLineContent(this.sequencesFile, line + 2));
		
		Sequence seq = new Sequence(sequenceName, currVal, this.sequencesFile, line + 2, step);
		return seq;
	}

	@Override
	public void openSequencesFile(String filePath) {
		File sequencesFile = new File(filePath);
		if(!sequencesFile.exists())
			throw new SequencesFileNotFoundException("The sequences file '" +filePath+ "' does not exists");
		this.sequencesFile = sequencesFile;
	}

}
