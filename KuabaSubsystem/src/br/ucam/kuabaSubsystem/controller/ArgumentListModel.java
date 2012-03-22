/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ucam.kuabaSubsystem.controller;

import java.util.List;
import javax.swing.DefaultListModel;

import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import java.util.HashMap;

/**
 *
 * @author pf_robo
 */
public class ArgumentListModel extends DefaultListModel{
    private HashMap<String, Argument> argumentHash = 
    	new HashMap<String, Argument>();

    public ArgumentListModel(List<Argument> arguments) {
        super();
        
        for (Argument argument : arguments) {
            this.argumentHash.put(argument.getHasText(), argument);
            if(!this.contains(argument.getHasText()))
                this.addElement(argument.getHasText());
            
        }        
    }
    
    public Argument getArgument(String text){
        return this.argumentHash.get(text);
    }
    
            
    
}
