/* File:      FloraSession.java
**
** Author(s): Michael Kifer
**
** Contact:   flora-users@lists.sourceforge.net
** 
** Copyright (C) The Research Foundation of SUNY, 2006
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
import net.sourceforge.flora.javaAPI.util.FlrException;


/**
   Constants used in other places
*/
public class FloraConstants
{
    public final static boolean debug = false;

    public final static String ISA_SYMBOL       = ":";
    public final static String SUBCLASS_SYMBOL  = "::";
	
    public final static String INHERIT_DATA_ARROW    = "*->";
    public final static String NONINHERIT_DATA_ARROW = "->";
	
    public final static String INHERIT_SIGNATURE_ARROW    = "*=>";
    public final static String NONINHERIT_SIGNATURE_ARROW = "=>";

    public final static String PROCEDURAL_METHOD_SYMBOL = "%";
    public final static String INHERITABLE_METHOD_SYMBOL = "*";
    public final static String NONINHERITABLE_METHOD_SYMBOL = "";

    public final static String AT_MODULE_SYMBOL = "@";

    public final static boolean DATA       = true;
    public final static boolean SIGNATURE  = false;

    public final static int VALUE      = 1;
    public final static int BOOLEAN    = 2;
    public final static int PROCEDURAL = 3;

    public final static boolean INHERITABLE = true;
    public final static boolean NONINHERITABLE = false;

    public final static String WRAP_HILOG = "flapply";


    public static String printableMethodType(int type) {
	if (type == VALUE) return "value";
	else if (type == BOOLEAN) return "boolean";
	else if (type == PROCEDURAL) return "procedural";
	else throw new FlrException("j2flora2: Invalid method type, " + type);
    }

    public static void checkMethodType(int type) {
	if (type != VALUE &&
	    type != BOOLEAN &&
	    type != PROCEDURAL)
	    throw new FlrException("j2flora2: Invalid method type, " + type);
    }

    public static String printableInheritability(boolean inherit) {
	if (inherit) return "inheritable";
	else return "noninheritable";
    }

    public static String printableAtomType(boolean atomtype) {
	if (atomtype==DATA) return "data";
	else return "signature";
    }
}
