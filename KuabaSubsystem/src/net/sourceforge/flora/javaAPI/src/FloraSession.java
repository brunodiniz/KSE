/* File:      FloraSession.java
**
** Author(s): Aditi Pandit
**
** Contact:   flora-users@lists.sourceforge.net
** 
** Copyright (C) The Research Foundation of SUNY, 2005, 2006
** 
** FLORA-2 is free software; you can redistribute it and/or modify it under the
** terms of the GNU Library General Public License as published by the Free
** Software Foundation; either version 2 of the License, or (at your option)
** any later version.
** 
** FLORA-2 is distributed in the hope that it will be useful, but WITHOUT ANY
** WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
** FOR A PARTICULAR PURPOSE.  See the GNU Library General Public License for
** more details.
** 
** 
** You should have received a copy of the GNU Library General Public License
** along with FLORA-2; if not, write to the Free Software Foundation,
** Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
**
**
** 
*/

package net.sourceforge.flora.javaAPI.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.flora.javaAPI.util.FlrException;

import com.declarativa.interprolog.PrologEngine;
import com.declarativa.interprolog.TermModel;

/** This class is a higher level wrapper to call the 
   lower level functions of the PrologFlora class
*/
public class FloraSession extends FloraConstants
{
    PrologFlora flora;

    /* Constructor function. */
    public FloraSession()
    {
	flora = new PrologFlora();	
	if(debug)
	    System.out.println("FLORA-2 session started");
    }
	

    /* Execute a command at the FLORA-2 session
    ** True, if the command succeeds
    **
    ** command : the command to be executed
    */
    public boolean ExecuteCommand(String command)
    {
	boolean result = false;
	try {
	    result = flora.simpleFloraCommand(command);
	    if (debug)
		System.out.println("ExecuteCommand: "+command);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    throw new FlrException("j2flora2: Command " + command + " failed");
	}
	return result;
    }


    /* Execute a command at the FLORA-2 session 
    ** The answer is a resultset that can be queried
    **
    ** query : Flora query to be executed 
    ** vars : Vector of variables to be bound
    */
    public Iterator<HashMap<String, FloraObject>> FindAllMatches(String query,Vector<String> vars)
    {
	Vector<HashMap<String,FloraObject>> retBindings = new Vector<HashMap<String,FloraObject>>();
	Object[] bindings = null;
		
	try {
	    bindings = flora.FloraCommand(query,vars);
	 
	    for (int i=0; i<bindings.length; i++) {
				
		TermModel tm = (TermModel)bindings[i];
		HashMap<String,FloraObject> currBinding = new HashMap<String,FloraObject>();
		for (int j=0; j<vars.size(); j++) {
		    String varValue = vars.elementAt(j);

		    if (debug) {
			System.out.println("FindAllMatches, term model: " + tm);
			System.out.println("FindAllMatches, varValue="+varValue);
		    }

		    TermModel objName = PrologFlora.findValue(tm,varValue);
		    FloraObject obj = new FloraObject(objName,this);
		    currBinding.put(varValue,obj);		
		}		
		retBindings.add(currBinding);
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	    System.err.println();
	    throw new FlrException("j2flora2: Error in query "+query);
	}

	Iterator<HashMap<String, FloraObject>> iter = retBindings.iterator();
	return iter;
    }

    
    public void close()
    {
	flora.close();
    }

    
    /* Load a FLORA-2 file into a module
    ** fileName : file to be loaded 
    ** moduleName : module name to load the file into
    */
    public boolean loadFile(String fileName,String moduleName)
    {
    return flora.loadFile(fileName,moduleName);
    }


    /* Add a FLORA-2 file to an existing module
    ** fileName : file to be loaded 
    ** moduleName : module name to load the file into
    */
    public boolean addFile(String fileName,String moduleName)
    {
	return flora.addFile(fileName,moduleName);
    }
    
    
    /* Execute a query with variables
    **
    ** query : query to be executed
    ** vars : variables in the query
    */
    public Iterator<HashMap<String, FloraObject>> ExecuteQuery(String query,Vector<String> vars)
    {
	return FindAllMatches(query,vars);
    }

    
    /*
    ** Like ExecuteQuery/2 above, but is used only when a query has
    ** just one variable. This provides a simplified interface, since
    ** no variables need to be passed into ExecuteQuery/1 and the
    ** output is just an iterator, which contains just a list of
    ** bindings for that single variable.
    **
    ** query : query to be executed
    */
    public Iterator<FloraObject> ExecuteQuery(String query)
    {
	Vector<FloraObject> retBindings = new Vector<FloraObject>();
	Object[] bindings = null;
		
	Vector<String> vars = new Vector<String>();
	vars.add("?");
	try {
	    bindings = flora.FloraCommand(query,vars);
			
	    for (int i=0; i<bindings.length; i++) {
		TermModel tm = (TermModel)bindings[i];
		TermModel objName = PrologFlora.findValue(tm,null);
		if (debug) {
		    System.out.println("ExecuteQuery/1, term model: " + tm);
		    System.out.println("ExecuteQuery/1, objName="+objName);
		}
		FloraObject obj = new FloraObject(objName,this);
		retBindings.add(obj);
	    }
	}
	catch(Exception e) {
	    e.printStackTrace();
	    throw new FlrException("j2flora2: Error in query "+query);
	}
	Iterator<FloraObject> iter = retBindings.iterator();
	return iter;
    }	
    
    public PrologEngine getEngine()
    {
    	return flora.engine;
    }
}
