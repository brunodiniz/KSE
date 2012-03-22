
/* File:      FloraMethod.java
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
 
import java.util.Vector;

/* Class to encapsulate a Flora method */
public class FloraMethod extends FloraConstants
{
	
    public FloraObject methodName;
    public Vector<FloraObject> parameters; // of FloraObject
    public FloraObject returnType;
    public boolean inheritable;
    public int type; // DATA, BOOLEAN, PROCEDURAL
	
    public FloraMethod(FloraObject methodName,
		       Vector<FloraObject> params, FloraObject returnType,
		       boolean inheritable, int type)
    {
	this.methodName = methodName;
	this.parameters = params;
	this.returnType = returnType;
	this.inheritable = inheritable;
	this.type = type;
	return;
    }

    public String toString()
    {
	return methodName.toString();
    }
	
    // mainly for debugging
    public String methodDetails()
    {
	return
	    "Method name:   " + methodName.toString()
	    + "\n    parameters:     " + parameters.toString()
	    + "\n    return type:    " + (returnType==null?
					  "*none*" : returnType.toString())
	    + "\n    inheritability: " + printableInheritability(inheritable)
	    + "\n    method type:    " + printableMethodType(type);
    }

}
