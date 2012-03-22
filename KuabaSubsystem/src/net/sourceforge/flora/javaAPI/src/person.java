package net.sourceforge.flora.javaAPI.src;

/* File:      javaAPI.flh
**
** Author(s): Michael Kifer
**
** Template included with every high-level Java object in the FLORA-2 Java API.
**
*/


import java.util.*;
import com.declarativa.interprolog.TermModel;
import net.sourceforge.flora.javaAPI.util.*;
import net.sourceforge.flora.javaAPI.src.*;
public class person extends FloraConstants {

    static TermModel floraClassName = new TermModel("person");
    FloraObject sourceFloraObject;
    String moduleName;

    public person(FloraObject sourceFloraObject,String moduleName) {
	this.sourceFloraObject = sourceFloraObject;
	if (sourceFloraObject == null)
	    throw new FlrException("Cannot create Java class " + floraClassName
				   + ". Null FloraObject passed to "
				   + floraClassName
				   + "(sourceFloraObject,moduleName)");
	    this.moduleName = moduleName;
    }

    public person(String floraOID,String moduleName,FloraSession session) {
	this.sourceFloraObject = new FloraObject(floraOID,session);
	this.moduleName = moduleName;
    }

    public String toString() {
        return sourceFloraObject.toString();
    }


    // Sub/Superclass methods
    public Iterator getDirectInstances() {
	return sourceFloraObject.getDirectInstances(moduleName);
    }

    public Iterator getInstances() {
	return sourceFloraObject.getInstances(moduleName);
    }

    public Iterator getDirectSubClasses() {
	return sourceFloraObject.getDirectSubClasses(moduleName);
    }

    public Iterator getSubClasses() {
	return sourceFloraObject.getSubClasses(moduleName);
    }

    public Iterator getDirectSuperClasses() {
	return sourceFloraObject.getDirectSuperClasses(moduleName);
    }

    public Iterator getSuperClasses() {
	return sourceFloraObject.getSuperClasses(moduleName);
    }

    // Java proxy methods for FLORA-2 methods
    public boolean getBDI_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getboolean(moduleName,"married",INHERITABLE,DATA,pars);
    }

    public Iterator getBDIall_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getbooleanAll(moduleName,"married",INHERITABLE,DATA,pars);
    }

    public boolean setBDI_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setboolean(moduleName,"married",INHERITABLE,DATA,pars);
    }

    public boolean deleteBDI_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deleteboolean(moduleName,"married",INHERITABLE,DATA,pars);
    }

    public boolean getBDN_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getboolean(moduleName,"married",NONINHERITABLE,DATA,pars);
    }

    public Iterator getBDNall_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getbooleanAll(moduleName,"married",NONINHERITABLE,DATA,pars);
    }

    public boolean setBDN_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setboolean(moduleName,"married",NONINHERITABLE,DATA,pars);
    }

    public boolean deleteBDN_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deleteboolean(moduleName,"married",NONINHERITABLE,DATA,pars);
    }

    public boolean getBSI_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getboolean(moduleName,"married",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getBSIall_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getbooleanAll(moduleName,"married",INHERITABLE,SIGNATURE,pars);
    }

    public boolean setBSI_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setboolean(moduleName,"married",INHERITABLE,SIGNATURE,pars);
    }

    public boolean deleteBSI_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deleteboolean(moduleName,"married",INHERITABLE,SIGNATURE,pars);
    }

    public boolean getBSN_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getboolean(moduleName,"married",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getBSNall_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getbooleanAll(moduleName,"married",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean setBSN_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setboolean(moduleName,"married",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean deleteBSN_married(Object year)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deleteboolean(moduleName,"married",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean getPDN_testmethod()
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getprocedural(moduleName,"testmethod",NONINHERITABLE,DATA,pars);
    }

    public Iterator getallPDN_testmethod()
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getproceduralAll(moduleName,"testmethod",NONINHERITABLE,DATA,pars);
    }

    public boolean setPDN_testmethod()
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setprocedural(moduleName,"testmethod",NONINHERITABLE,DATA,pars);
    }

    public boolean deletePDN_testmethod()
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deleteprocedural(moduleName,"testmethod",NONINHERITABLE,DATA,pars);
    }

    public boolean getPSN_testmethod()
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getprocedural(moduleName,"testmethod",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallPSN_testmethod()
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getproceduralAll(moduleName,"testmethod",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean setPSN_testmethod()
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setprocedural(moduleName,"testmethod",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean deletePSN_testmethod()
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deleteprocedural(moduleName,"testmethod",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVDI_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"age",INHERITABLE,DATA,pars);
    }

    public Iterator getallVDI_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"age",INHERITABLE,DATA,pars);
    }

    public boolean setVDI_age(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"age",INHERITABLE,DATA,pars,value);
    }

    public boolean setVDI_age(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"age",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_age(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_age(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",INHERITABLE,DATA,pars);
    }

    public Iterator getVDI_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"believes_in",INHERITABLE,DATA,pars);
    }

    public Iterator getallVDI_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"believes_in",INHERITABLE,DATA,pars);
    }

    public boolean setVDI_believes_in(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"believes_in",INHERITABLE,DATA,pars,value);
    }

    public boolean setVDI_believes_in(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"believes_in",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_believes_in(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_believes_in(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",INHERITABLE,DATA,pars);
    }

    public Iterator getVDI_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"hobbies",INHERITABLE,DATA,pars);
    }

    public Iterator getallVDI_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"hobbies",INHERITABLE,DATA,pars);
    }

    public boolean setVDI_hobbies(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"hobbies",INHERITABLE,DATA,pars,value);
    }

    public boolean setVDI_hobbies(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"hobbies",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_hobbies(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_hobbies(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",INHERITABLE,DATA,pars);
    }

    public Iterator getVDI_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"kids",INHERITABLE,DATA,pars);
    }

    public Iterator getallVDI_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"kids",INHERITABLE,DATA,pars);
    }

    public boolean setVDI_kids(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"kids",INHERITABLE,DATA,pars,value);
    }

    public boolean setVDI_kids(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"kids",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_kids(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_kids(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",INHERITABLE,DATA,pars);
    }

    public Iterator getVDI_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getvalue(moduleName,"salary",INHERITABLE,DATA,pars);
    }

    public Iterator getallVDI_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getvalueAll(moduleName,"salary",INHERITABLE,DATA,pars);
    }

    public boolean setVDI_salary(Object year,Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setvalue(moduleName,"salary",INHERITABLE,DATA,pars,value);
    }

    public boolean setVDI_salary(Object year,Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setvalue(moduleName,"salary",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_salary(Object year,Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_salary(Object year,Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",INHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDI_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",INHERITABLE,DATA,pars);
    }

    public Iterator getVDN_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"age",NONINHERITABLE,DATA,pars);
    }

    public Iterator getallVDN_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"age",NONINHERITABLE,DATA,pars);
    }

    public boolean setVDN_age(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"age",NONINHERITABLE,DATA,pars,value);
    }

    public boolean setVDN_age(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"age",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_age(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_age(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",NONINHERITABLE,DATA,pars);
    }

    public Iterator getVDN_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"believes_in",NONINHERITABLE,DATA,pars);
    }

    public Iterator getallVDN_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"believes_in",NONINHERITABLE,DATA,pars);
    }

    public boolean setVDN_believes_in(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"believes_in",NONINHERITABLE,DATA,pars,value);
    }

    public boolean setVDN_believes_in(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"believes_in",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_believes_in(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_believes_in(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",NONINHERITABLE,DATA,pars);
    }

    public Iterator getVDN_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"hobbies",NONINHERITABLE,DATA,pars);
    }

    public Iterator getallVDN_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"hobbies",NONINHERITABLE,DATA,pars);
    }

    public boolean setVDN_hobbies(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"hobbies",NONINHERITABLE,DATA,pars,value);
    }

    public boolean setVDN_hobbies(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"hobbies",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_hobbies(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_hobbies(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",NONINHERITABLE,DATA,pars);
    }

    public Iterator getVDN_instances()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"instances",NONINHERITABLE,DATA,pars);
    }

    public Iterator getallVDN_instances()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"instances",NONINHERITABLE,DATA,pars);
    }

    public boolean setVDN_instances(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"instances",NONINHERITABLE,DATA,pars,value);
    }

    public boolean setVDN_instances(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"instances",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_instances(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"instances",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_instances(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"instances",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_instances()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"instances",NONINHERITABLE,DATA,pars);
    }

    public Iterator getVDN_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"kids",NONINHERITABLE,DATA,pars);
    }

    public Iterator getallVDN_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"kids",NONINHERITABLE,DATA,pars);
    }

    public boolean setVDN_kids(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"kids",NONINHERITABLE,DATA,pars,value);
    }

    public boolean setVDN_kids(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"kids",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_kids(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_kids(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",NONINHERITABLE,DATA,pars);
    }

    public Iterator getVDN_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getvalue(moduleName,"salary",NONINHERITABLE,DATA,pars);
    }

    public Iterator getallVDN_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getvalueAll(moduleName,"salary",NONINHERITABLE,DATA,pars);
    }

    public boolean setVDN_salary(Object year,Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setvalue(moduleName,"salary",NONINHERITABLE,DATA,pars,value);
    }

    public boolean setVDN_salary(Object year,Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setvalue(moduleName,"salary",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_salary(Object year,Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_salary(Object year,Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",NONINHERITABLE,DATA,pars,value);
    }

    public boolean deleteVDN_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",NONINHERITABLE,DATA,pars);
    }

    public Iterator getVSI_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"age",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSI_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"age",INHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSI_age(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"age",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSI_age(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"age",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_age(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_age(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSI_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"believes_in",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSI_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"believes_in",INHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSI_believes_in(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"believes_in",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSI_believes_in(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"believes_in",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_believes_in(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_believes_in(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSI_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"hobbies",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSI_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"hobbies",INHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSI_hobbies(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"hobbies",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSI_hobbies(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"hobbies",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_hobbies(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_hobbies(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSI_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"kids",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSI_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"kids",INHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSI_kids(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"kids",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSI_kids(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"kids",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_kids(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_kids(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSI_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getvalue(moduleName,"salary",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSI_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getvalueAll(moduleName,"salary",INHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSI_salary(Object year,Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setvalue(moduleName,"salary",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSI_salary(Object year,Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setvalue(moduleName,"salary",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_salary(Object year,Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_salary(Object year,Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",INHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSI_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",INHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSN_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"age",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSN_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"age",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSN_age(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"age",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSN_age(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"age",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_age(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_age(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_age()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"age",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSN_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"believes_in",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSN_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"believes_in",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSN_believes_in(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"believes_in",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSN_believes_in(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"believes_in",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_believes_in(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_believes_in(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_believes_in()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"believes_in",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSN_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"hobbies",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSN_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"hobbies",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSN_hobbies(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"hobbies",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSN_hobbies(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"hobbies",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_hobbies(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_hobbies(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_hobbies()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"hobbies",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSN_instances()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"instances",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSN_instances()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"instances",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSN_instances(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"instances",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSN_instances(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"instances",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_instances(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"instances",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_instances(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"instances",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_instances()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"instances",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSN_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalue(moduleName,"kids",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSN_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.getvalueAll(moduleName,"kids",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSN_kids(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"kids",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSN_kids(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.setvalue(moduleName,"kids",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_kids(Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_kids(Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_kids()    {
      Vector<Object> pars = new Vector<Object>();
      return sourceFloraObject.deletevalue(moduleName,"kids",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getVSN_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getvalue(moduleName,"salary",NONINHERITABLE,SIGNATURE,pars);
    }

    public Iterator getallVSN_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.getvalueAll(moduleName,"salary",NONINHERITABLE,SIGNATURE,pars);
    }

    public boolean setVSN_salary(Object year,Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setvalue(moduleName,"salary",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean setVSN_salary(Object year,Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.setvalue(moduleName,"salary",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_salary(Object year,Vector<Object> value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_salary(Object year,Object value)
    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",NONINHERITABLE,SIGNATURE,pars,value);
    }

    public boolean deleteVSN_salary(Object year)    {
      Vector<Object> pars = new Vector<Object>();
      pars.add(year);
      return sourceFloraObject.deletevalue(moduleName,"salary",NONINHERITABLE,SIGNATURE,pars);
    }

}
