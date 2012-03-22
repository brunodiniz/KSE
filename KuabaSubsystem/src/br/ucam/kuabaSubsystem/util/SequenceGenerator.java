package br.ucam.kuabaSubsystem.util;

import java.io.IOException;

import br.ucam.kuabaSubsystem.kuabaExceptions.SequenceNameAlreadyExistsException;


/**
 * 
 * @author Thiago
 * 
 * This interface offer operations to load, create and manipulate sequences.
 * A sequence must have a name and must be persisted in a file. One file
 * may contain one or more sequences.
 */
public interface SequenceGenerator {
	
	public void openSequencesFile(String filePath);
	
	public Sequence createNewSequence(String sequenceName, long init, int step)
	throws IOException;
	
	public Sequence loadSequence(String sequenceName) throws IOException;
	
}
