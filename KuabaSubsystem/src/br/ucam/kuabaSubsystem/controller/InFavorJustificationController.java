/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.controller;

import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Solution;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Kildare
 */
public class InFavorJustificationController extends JustificationController {

    private Solution sol;
    
    public InFavorJustificationController(Solution sol){
        this.sol=sol;
    }
    
    @Override
    public List<Argument> findInFavorArguments() {
        List<Argument> arg = new ArrayList<Argument>();
        for(Argument a : this.justification.getIsDerivedOf())
            if(a.hasInFavorOf())
                arg.add(a);
        if(arg.size()>0)
            return arg;
        return null;
    }

    @Override
    public List<Argument> findObjectsToArgument() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JDialog render() {
        
        JDialog justificationFrame = this.getView();    	
    	ViewRenderer.addDialogToRender(justificationFrame);
    	return justificationFrame;
    }
    
    public JDialog getView(){
        
          // jv = new InFavorJustificationView(this);
          
      //    return jv;
      return null;
    }
    
    
    
}
