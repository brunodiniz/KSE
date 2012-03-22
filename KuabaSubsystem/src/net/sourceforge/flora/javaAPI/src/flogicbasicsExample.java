package net.sourceforge.flora.javaAPI.src;

/* File:      flogicbasicsExample.java
**
** Author(s): Aditi Pandit, Michael Kifer
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

/***********************************************************************
     This file contains examples of the uses of low-level API followed
     by the examples of the uses of high-level API.
***********************************************************************/

import net.sourceforge.flora.javaAPI.src.*;
import java.util.*;

public class flogicbasicsExample{
	
    public static void main(String[] args) {
    	System.setProperty("PROLOGDIR", "D:/Flora-2/xsb-3.1-win32/config/x86-pc-windows/bin");
    	System.setProperty("FLORADIR", "D:/Flora-2/flora2");
    	System.setProperty("ENGINE", "xsb");
    	System.setProperty("FLORA_FILE", "D:/Flora-2/flora2/java/API/examples/flogicbasicsExample/flogic_basics.flr");
    	FloraSession session = new FloraSession();
	System.out.println("FLORA-2 session started");	
		
	String fileName = System.getProperty("FLORA_FILE");
	
	
	if(fileName == null || fileName.trim().length() == 0) {
	    System.out.println("Invalid path to example file!");
	    System.exit(0);
	}
	session.loadFile(fileName,"example");
	
	/* Examples of uses of the low-level Java-FLORA-2 API */
	
	// Querying persons
	String command = "?X:person@example.";
	System.out.println("Query:"+command);
	Iterator personObjs = session.ExecuteQuery(command);
	/* Printing out persons  */
    	while (personObjs.hasNext()) {
	    Object personObj = personObjs.next();
	    System.out.println("Person Id: "+personObj);
	}

		
	command = "person[instances -> ?X]@example.";
	System.out.println("Query:"+command);
	personObjs = session.ExecuteQuery(command);
		
	/* Printing out persons  */
    	while (personObjs.hasNext()) {
	    Object personObj = personObjs.next();
	    System.out.println("Person Id: "+personObj);
	}

	/* Example of query with two variables */
	Vector<String> vars = new Vector<String>();
	vars.add("?X");
	vars.add("?Y");
		
	Iterator allmatches =
	    session.ExecuteQuery("?X[believes_in -> ?Y]@example.",vars);
	System.out.println("Query:?X[believes_in -> ?Y]@example.");

	HashMap firstmatch;
	while (allmatches.hasNext()) {	
	    firstmatch = (HashMap)allmatches.next();
	    FloraObject Xobj = (FloraObject)firstmatch.get("?X");
	    FloraObject Yobj = (FloraObject)firstmatch.get("?Y");	
	    System.out.println(Xobj+" believes in: "+Yobj);	
	}

	FloraObject personObj = new FloraObject("person",session);
	Iterator methIter = personObj.getMethods("example");
	while (methIter.hasNext()) {
	    System.out.println(((FloraMethod)methIter.next()).methodDetails());
	}

	// instances of the person class
	Iterator instanceIter = personObj.getInstances("example");
	System.out.println("Person instances:");
	while (instanceIter.hasNext())
	    System.out.println("    " + (FloraObject)instanceIter.next());


	instanceIter = personObj.getDirectInstances("example");
	System.out.println("Person direct instances:");
	while (instanceIter.hasNext())
	    System.out.println("    " + (FloraObject)instanceIter.next());

	Iterator subIter = personObj.getSubClasses("example");
	System.out.println("Person subclasses:");
	while (subIter.hasNext())
	    System.out.println("    " + (FloraObject)subIter.next());

	subIter = personObj.getDirectSubClasses("example");
	System.out.println("Person direct subclasses:");
	while (subIter.hasNext())
	    System.out.println("    " + (FloraObject)subIter.next());

	Iterator supIter = personObj.getSuperClasses("example");
	System.out.println("Person superclasses:");
	while (supIter.hasNext())
	    System.out.println("    " + (FloraObject)supIter.next());

	supIter = personObj.getDirectSuperClasses("example");
	System.out.println("Person direct superclasses:");
	while (supIter.hasNext())
	    System.out.println("    " + (FloraObject)supIter.next());

	/*****************************************************************
	 ******** Examples of uses of the high-level Java-FLORA-2 API
	 *****************************************************************/

	/* Printing out people's names and information about their kids
	   using the high-level API. Note that the high-level person-object 
	   is obtained here out of the low-level FloraObject personObj
	*/
	person currPerson = null;
    	while (personObjs.hasNext()) {
	    personObj = (FloraObject)(personObjs.next());
	    System.out.println("Person name:"+personObj);

	    currPerson =new person(personObj,"example");
	    Iterator kidsItr = currPerson.getVDN_kids();

	    while(kidsItr.hasNext()) {
		FloraObject kidObj = (FloraObject)(kidsItr.next());
		System.out.println("Person Name: " + personObj
				   + " has kid: " + kidObj);
		    
		person kidPerson = null;
		kidPerson = new person(kidObj,"example");
		
		Iterator hobbiesItr = kidPerson.getVDN_hobbies();

		while(hobbiesItr.hasNext()) {
		    FloraObject hobbyObj = null;
		    hobbyObj = (FloraObject)(hobbiesItr.next());
		    System.out.println("Kid:"+kidObj
				       + " has hobby: " + hobbyObj);
		}
	    }
	}


	FloraObject age;
	currPerson = new person("father(mary)", "example", session);
	Iterator maryfatherItr = currPerson.getVDN_age();
	age = (FloraObject)maryfatherItr.next();
	System.out.println("Mary's father is " + age + " years old");

	currPerson = new person("mary", "example", session);
	Iterator maryItr = currPerson.getVDN_age();
	age = (FloraObject)maryItr.next();
	System.out.println("Mary is " + age + " years old");

	// person instances using high-level interface
	person personClass = new person("person", "example", session);
	instanceIter = personClass.getInstances();
	System.out.println("Person instances using high-level API:");
	while (instanceIter.hasNext())
	    System.out.println("    " + (FloraObject)instanceIter.next());


	Iterator subclassIter = personClass.getSubClasses();
	System.out.println("Person subclasses using high-level API:");
	while (subclassIter.hasNext())
	    System.out.println("    " + (FloraObject)subclassIter.next());


	// Close session and good bye
	session.close();
	System.exit(0);
    }
}
