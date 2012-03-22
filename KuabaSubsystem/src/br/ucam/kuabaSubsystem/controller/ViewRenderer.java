/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ucam.kuabaSubsystem.controller;

import br.ucam.kuabaSubsystem.gui.ViewThread;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JDialog;



/**
 *
 * @author Thiago
 */
public class ViewRenderer {
    private static Queue<JDialog> dialogQueue  = new LinkedList<JDialog>();
    private static boolean blocked;
    //private static ViewRenderer instance = new ViewRenderer();
    
   // public static ViewRenderer getInstance(){
    //    return instance;
   // }
    public static void addDialogToRender(JDialog dialog){
        if(blocked)
            dialogQueue.add(dialog);
        else
            new ViewThread(dialog).start();
    }
    
    public static void block(){
        blocked = true;
    }
    public static void unblock(){
    	
        blocked = false;
        if(!dialogQueue.isEmpty()){
            JDialog dialogToRender = dialogQueue.remove();
            new ViewThread(dialogToRender).start();
        }
            
    }
}
