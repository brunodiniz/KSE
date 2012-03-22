package br.ucam.kuabaSubsystem.mofParser;

import br.ucam.kuabaSubsystem.kuabaModel.KuabaElement;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
/**
 * 
 * @author Thiago
 * This interface allows a client to construct a complex Kuaba Tree of Questions
 * and Ideas. On of the purpose of this interface is to encapsulate the Kuaba
 * elements creation from the clients.
 * 
 * This interface could be used, for example, to construct a Kuaba Tree
 * following a predetermined sequence of passes.
 * 
 *  See the Builder Gof pattern to understand how this class could be used 
 *  with a Director class.
 */
public interface ParserBuilder {
	
	public void createRootQuestion(String questionText);
	public void createIdea(String ideaText);
	public void createQuestion(String questionText, String questionType);	
	public void clearStack();
	public void push();
	public KuabaElement pop();
	public KuabaElement	top();
	
	public Question getResult();
	
	
}
