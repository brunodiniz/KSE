package br.ucam.kuabaSubsystem.kuabaExceptions;
/**
 * This Exception is thrown every time that a client tries to create an sequence
 * and this sequence already exists in the current sequences file.
 * @author Thiago
 *
 */
public class SequenceNameAlreadyExistsException extends RuntimeException {

	public SequenceNameAlreadyExistsException(String arg0) {
		super(arg0);	
	}

}
