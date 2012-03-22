/* File:      PrologFlora.java
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
*/


package net.sourceforge.flora.javaAPI.src;

import java.io.File;
import java.util.Vector;

import net.sourceforge.flora.javaAPI.util.FlrException;

import com.declarativa.interprolog.PrologEngine;
import com.declarativa.interprolog.TermModel;
import com.declarativa.interprolog.XSBSubprocessEngine;
import com.declarativa.interprolog.util.IPException;
import com.xsb.interprolog.NativeEngine;

/** This class is used to call FLORA-2 commands 
    at a low level from JAVA using Interprolog libraries */
public class PrologFlora extends FloraConstants
{
    public static String sFloraRootDir = null;
    PrologEngine engine;
    boolean isNative = false;
    String commands[];

    
    /* Function for setting Initialization commands */
    void initCommandStrings(String FloraRootDir)
    {
	commands = (new String[] {
		"(import bootstrap_flora/0 from flora2)",
		String.valueOf(String.valueOf((new StringBuffer("asserta(library_directory('")).append(FloraRootDir).append("'))"))),
		"consult(flora2)",
		"bootstrap_flora",
		"consult(flrimportedcalls)",
		"import ('_load')/1, ('_add')/1 from flora2",
		"import flora_query/4 from flora2",
		"import flora_decode_oid_as_atom/2 from flrdecode"
	    });
    }
    

    /* Function to execute the initialization commands */
    void executeInitCommands()
    {
        if(commands == null)
            throw new FlrException("j2flora2: System bug, please report");

	for(int i = 0; i < commands.length; i++) {
	    boolean cmdsuccess = simplePrologCommand(commands[i]);
	    if(!cmdsuccess)
		throw new FlrException("j2flora2: FLORA-2 startup failed");
	}
    }


    /* Function to use _load to load FLORA-2 file into moduleName
    ** fileName   : name of the file to load
    ** moduleName : name of FLORA module in which to load
    */
    public boolean loadFile(String fileName,String moduleName)
    {
    boolean cmdsuccess = false; 
	String cmd = "'_load'('"+fileName + "'>>" + moduleName+")";
	try {
		cmdsuccess = engine.command(cmd);
	}
	catch(IPException ipe) {
	    ipe.printStackTrace();
	    throw new FlrException("j2flora2: Command "+ cmd + " failed");
	}
        return cmdsuccess;
    }


    /* Function to use _add to add FLORA-2 file to moduleName
    ** fileName   : name of the file to load
    ** moduleName : name of FLORA module in which to load
    */
    public boolean addFile(String fileName,String moduleName)
    {
    boolean cmdsuccess = false;
	String cmd = "'_add'('"+fileName + "'>>" + moduleName+")";
	try {
		cmdsuccess = engine.command(cmd);
	}
	catch(IPException ipe) {
	    ipe.printStackTrace();
	    throw new FlrException("j2flora2: Command "+ cmd + " failed");
	}
        return cmdsuccess;
    }
    
    

    /* Call the flora_query/4 predicate of FLORA-2
    ** Binds FLORA-2 variables to the returned values
    ** and returns an array of answers. Each answer is an Interprolog
    ** term model from which variable bindings can be obtained.
    ** (See FindAllMatches and ExecuteQuery/1 for how to do this.)
    **
    ** cmd : Flora query to be executed 
    ** vars : Variables in the Flora query that need to be bound
    */
    public Object[] FloraCommand(String cmd,Vector<String> vars)
    {
        StringBuffer sb = new StringBuffer();
	String varsString = "";
	for (int i=0; i<vars.size(); i++) {
	    String floravar = vars.elementAt(i);
	    if (!floravar.startsWith("?"))
		throw new FlrException("j2flora2: Illegal variable name "
				       + floravar
				       + ". Variables passed to ExecuteQuery "
				       + "must be FLORA-2 variables and "
				       + "start with a `?'");
	    if (i>0) varsString += ",";
	    varsString += "'" + vars.elementAt(i) + "'=__Var" + i;
	}
	varsString = "[" + varsString + "]";

	String listString = "L_rnd="+varsString + ",";
	String queryString = "S_rnd='"+cmd + "',";
	String floraQueryString =
	    "findall(TM_rnd,(flora_query(S_rnd,L_rnd,_St,_Ex),buildTermModel(L_rnd,TM_rnd)),BL_rnd),ipObjectSpec('ArrayOfObject',BL_rnd,LM)";
   
	sb.append(queryString);
	sb.append(listString);
	sb.append(floraQueryString);
	
	if (debug)
	    System.out.println("FloraCommand: " + sb);

	try {
	    Object solutions[] =
		(Object[])engine.deterministicGoal(sb.toString(), "[LM]")[0];
	    return solutions;
	}
	catch(Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }


    /* A simpler way to call FLORA commands that are not queries
    **
    ** cmd : Command to be executed
    */
    public boolean simpleFloraCommand(String cmd)
    {
	String queryString = "S_rnd='"+cmd + "',";
	String listString = "L_rnd=[],";

        StringBuffer sb = new StringBuffer();
	sb.append(queryString);
	sb.append(listString);
        sb.append("flora_query(S_rnd,L_rnd,_St,_Ex)");

	if (debug)
	    System.out.println("simpleFloraCommand: " + sb);

	try {
	    return engine.deterministicGoal(sb.toString());
	}
	catch(IPException ipe) {
	    ipe.printStackTrace();
	    throw new FlrException("j2flora2: Command " + cmd + " failed");
	}
    }


    public boolean simplePrologCommand(String cmd)
    {
        try {
	    return engine.command(cmd);
	}
        catch(IPException ipe) {
	    ipe.printStackTrace();
	    throw ipe;
	}
    }
    

    /* Query the term model structure 
    **
    ** tm : TermModel to be queried 
    ** name : binding variable name to be queried 
    */
    public static TermModel findValue(TermModel tm, String name)
    {
	if (debug)
	    System.out.println("in findValue, Term model: " + tm);

        for( ; tm.isList(); tm = (TermModel)tm.getChild(1)) {
	    TermModel item = (TermModel)tm.getChild(0);
	    
	    if (debug && name != null)
		System.out.println("name in findValue: " + name);

	    if(name == null
	       || (item.getChild(0).toString().compareTo(name) == 0)) {
		TermModel val = (TermModel)item.getChild(1);
		return val;
	    }
	}
	return new TermModel();
    }


    /* Initialise the PrologEngine */
    void initEngine()
    {
	String PrologRootDir = System.getProperty("PROLOGDIR");
	String CmdFloraRootDir = System.getProperty("FLORADIR");
	String PrologBinDir = PrologRootDir + File.separator + "xsb";

        if(PrologRootDir == null || PrologRootDir.trim().length() == 0)
            throw new FlrException("j2flora2: Must define PROLOGDIR property");
        
        String FloraRootDir;	
	if (CmdFloraRootDir == null || CmdFloraRootDir.trim().length() == 0) {
	    throw new FlrException("j2flora2: Must define FLORADIR property");
	    }
	else
	    FloraRootDir = CmdFloraRootDir;

	String engineType;

	engineType = System.getProperty("ENGINE"); 
	engineType = engineType.toUpperCase();
	if (engineType.equals("NATIVE")) {
	    try {
		engine = new NativeEngine(PrologRootDir);
		isNative = true;
	    }
	    catch(Throwable e) {
		throw new FlrException("j2flora2: InterProlog failed to start its Native Engine");
	    }
	} else {
	    try {
		engine = new XSBSubprocessEngine(PrologBinDir);
	    }
	    catch(Exception e2) {
		throw new FlrException("j2flora2: InterProlog failed start its SubProcess Engine");
	    }
	}
	initCommandStrings(FloraRootDir);
	executeInitCommands();
    }


    /* Constructor function */
    public PrologFlora()
    {
        commands = null;
	initEngine();
	return;
    }
    

    /* Shut down the Prolog Engine */
    public void close()
    {
	// don't exit: let the Java program continue after the shutdown
	if(isNative) {
	    // shutdown isn't implemented in Native engine, but this is harmless
	    engine.shutdown();
	} else {
	    engine.shutdown();
	}
    }
}
