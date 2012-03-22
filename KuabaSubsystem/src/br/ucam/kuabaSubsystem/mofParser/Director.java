package br.ucam.kuabaSubsystem.mofParser;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.jmi.xmi.MalformedXMIException;

@SuppressWarnings("unchecked")
public interface Director {
	
	/**
	 * This Method is used to initialize the director to build the Kuaba tree
	 * following the metamodel.
	 * 
	 * @param builder: the builder to construct the Kuaba Tree.
	 * @param mofIterator: The iterator that contains all of the
	 * MOF based metamodel elements.  
	 */
	public void init(ParserBuilder builder, BaseClassRecognizer baseClassFinder);
	
	public void init(ParserBuilder builder, BaseClassRecognizer baseClassFinder, URL resource) throws IOException, MalformedXMIException;
	
	/**
	 * This method should be called after {@link #init(ParserBuilder, Iterator)}
	 * of the director.
	 * 
	 * The execution of this method should iterate over the metamodel elements
	 * contained in mofIterator and make a predetermined sequence of calls on
	 * the builder to construct the Kuaba Tree correctly according with the metamodel.
	 * 
	 * The builder operate with a internal stack that register the created
	 * Kuaba elements. Is Director responsibility operate this stack to construct
	 * the correct Kuaba representation of the metamodel.
	 * 
	 * @see {@link ParserBuilder} contract to understand  how builder works.
	 */
	public void construct();
}
