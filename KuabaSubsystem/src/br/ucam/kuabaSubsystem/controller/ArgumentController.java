/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ucam.kuabaSubsystem.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.ListModel;

import br.ucam.kuabaSubsystem.gui.ViewThread;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.util.KuabaHelper;


/**
 *
 * @author pf_robo
 */
public abstract class ArgumentController extends AbstractController {
    private ArgumentListModel argumentsListModel;
    
    private ArgumentTableModel inFavorTableModel = new ArgumentTableModel();
    private ArgumentTableModel objectsToTableModel = new ArgumentTableModel();
    
    protected Argument argument;
    
    protected Idea[] inFavoridea;
    protected Question consideredQuestion;
    protected Idea[] objectsToIdea;
    protected HashMap<Idea, Question> inFavorIdeaHash = null;
    protected HashMap<Idea, Question> againstIdeaHash = null;
    
    public ArgumentController(Idea[] inFavoridea,
			Idea objectsToIdea[], Question consideredQuestion) {				
		this(inFavoridea, objectsToIdea);
		this.consideredQuestion = consideredQuestion;
        
	}
    public ArgumentController(HashMap<Idea, Question>inFavorIdeaHash,
    		HashMap<Idea, Question>againstIdeaHash){
    	this.againstIdeaHash = againstIdeaHash;
    	this.inFavorIdeaHash = inFavorIdeaHash;
    	
    }
    public ArgumentController(Idea[] inFavoridea, Idea[] objectsToIdea){
    	this.inFavoridea = inFavoridea;
		this.objectsToIdea = objectsToIdea;		
		this.inFavorTableModel.setColumnIdentifiers(
        		new String[]{"In Favor Arguments"});
        this.objectsToTableModel.setColumnIdentifiers(
        		new String[]{"Objects To Arguments"});
    }

    
    public Question getConsideredQuestion() {
		return consideredQuestion;
	}

	public void clearTableModels(){
    	this.inFavorTableModel = new ArgumentTableModel();
    	this.objectsToTableModel = new ArgumentTableModel();
    	this.inFavorTableModel.setColumnIdentifiers(
        		new String[]{"In Favor Arguments"});
        this.objectsToTableModel.setColumnIdentifiers(
        		new String[]{"Objects To Arguments"});
    }

	public Argument getArgument() {
		return argument;
	}
	protected Argument createNewArgument(String text){
		String id = "_"+ this.modelSequence.nextVal();
        Argument argument = this.modelFactory.createArgument(id);
        argument.setHasText(text);
        return argument; 
	}
	public void newArgument(String text){
		Argument argument = null;
		argument = this.argumentsListModel.getArgument(text);
		if(argument != null){
			this.argument = argument;
		}
		else{			
	    	this.argument = createNewArgument(text);
		}
		if(this.inFavoridea != null){
			for (Idea inFavorIdea : this.inFavoridea) {
				inFavorIdea.addHasArgument(this.argument);
	        	this.argument.addInFavorOf(inFavorIdea);
			}
			
		}
		if(this.objectsToIdea != null){
			for (Idea againstIdea : this.objectsToIdea) {
				againstIdea.addHasArgument(this.argument);
				this.argument.addObjectsTo(againstIdea);
			}			
		}
		if(this.againstIdeaHash != null){
			Collection<Idea> keys = this.againstIdeaHash.keySet();
			for (Idea againstIdea : keys) {
				againstIdea.addHasArgument(this.argument);
				this.argument.addObjectsTo(againstIdea);
				if(this.againstIdeaHash.get(againstIdea) != null)
					this.argument.setConsiders(this.againstIdeaHash.get(
							againstIdea));
			}
		}
		if(this.inFavorIdeaHash != null){
			Collection<Idea> keys = this.inFavorIdeaHash.keySet();
			for (Idea inFavorIdea : keys) {
				inFavorIdea.addHasArgument(this.argument);
				this.argument.addInFavorOf(inFavorIdea);
				if(this.inFavorIdeaHash.get(inFavorIdea) != null)
						if(this.argument.getConsiders() == null)							
							this.argument.setConsiders(
									this.inFavorIdeaHash.get(inFavorIdea));
						else{
							Argument arg = 
								createNewArgument(this.argument.getHasText());
							arg.addInFavorOf(inFavorIdea);
							inFavorIdea.addHasArgument(arg);
							arg.setConsiders(this.inFavorIdeaHash.get(
									inFavorIdea));
						}
							
			}
		}
		if(this.consideredQuestion != null)
			this.argument.setConsiders(this.consideredQuestion);
    }    
    public String getInFavorItem(int index){
        return (String) this.argumentsListModel.getElementAt(index);
    }
    public String getItem(int index){
        return (String) this.argumentsListModel.getElementAt(index);
    }
    public ListModel allArguments(){
        this.argumentsListModel = new ArgumentListModel(
        		this.modelRepository.getAllArguments());
        
        return this.argumentsListModel;
    }    
    public String getInFavorElement(int row, int column){
        String element = (String)this.inFavorTableModel.getValueAt(
        		row, column);
        return element;
    }
    public String getObjectsToElement(int row, int column){
        String element = (String)this.objectsToTableModel.getValueAt(
                row, column);
        return element;
    }

    public ArgumentTableModel getInFavorTableModel() {
        return inFavorTableModel;
    }

    public void setInFavorTableModel(ArgumentTableModel inFavorTableModel) {
        this.inFavorTableModel = inFavorTableModel;
    }

    public ArgumentTableModel getObjectsToTableModel() {
        return objectsToTableModel;
    }

    public void setObjectsToTableModel(ArgumentTableModel objectsToTableModel) {
        this.objectsToTableModel = objectsToTableModel;
    }
    public void insertInFavorArguments(){  
        List<Argument> arguments = findInFavorArguments();        
        this.inFavorTableModel.insertArguments(arguments);
    }       
    
    public void insertObjectsToArguments(){
    	List<Argument> arguments = findObjectsToArgument();
        this.objectsToTableModel.insertArguments(arguments);
    } 
    
    public List<Argument> findInFavorArguments() {		
        List<Argument> inFavorOfList = new ArrayList<Argument>();
        Collection<Argument> arguments = new ArrayList<Argument>();
        for (Idea inFavorIdea : this.inFavoridea) {
        	arguments.addAll(KuabaHelper.filterConsidersArguments(
            		inFavorIdea.getHasArgument(), consideredQuestion));
		}
        	
        for (Argument argument : arguments)
        	if(argument.getInFavorOf().containsAll(Arrays.asList(this.inFavoridea)))                	
        		inFavorOfList.add(argument);
        return inFavorOfList;
	}	
	
    public List<Argument> findObjectsToArgument() {		
    	List<Argument> objectsToList = new ArrayList<Argument>();
        Collection<Argument> arguments = new ArrayList<Argument>();
        for (Idea inFavorIdea : this.inFavoridea) {
        	arguments.addAll(KuabaHelper.filterConsidersArguments(
            		inFavorIdea.getHasArgument(), consideredQuestion));
		}
        	
        for (Argument argument : arguments)
        	if(argument.getObjectsTo().containsAll(Arrays.asList(this.inFavoridea)))                	
        		objectsToList.add(argument);
        return objectsToList;
	}
    
    public JDialog render(){
    	JDialog argumentFrame = this.getView();    	
    	ViewRenderer.addDialogToRender(argumentFrame);
    	return argumentFrame;
    }
    protected abstract JDialog getView();

	public Idea[] getInFavoridea() {
		return inFavoridea;
	}

	public void setInFavoridea(Idea[] inFavoridea) {
		this.inFavoridea = inFavoridea;
	}

	public Idea[] getObjectsToIdea() {
		return objectsToIdea;
	}

	public void setObjectsToIdea(Idea[] objectsToIdea) {
		this.objectsToIdea = objectsToIdea;
	}	
	
}
