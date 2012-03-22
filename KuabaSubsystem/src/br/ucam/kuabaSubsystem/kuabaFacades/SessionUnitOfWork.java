package br.ucam.kuabaSubsystem.kuabaFacades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;

public class SessionUnitOfWork {
	private Map<String, Idea> consideredIdeasMap = new HashMap<String, Idea>();
	private Map<String, Idea> rejectedIdeasMap = new HashMap<String, Idea>();
	
	public void addConsideredIdea(Idea consideredIdea){
		System.out.println("Texto:"+ consideredIdea.getHasText());
		System.out.println("ID"+ consideredIdea.getId());
		System.out.println();
		if(this.rejectedIdeasMap.containsKey(consideredIdea.getId()))
			this.rejectedIdeasMap.remove(consideredIdea.getId());
		this.consideredIdeasMap.put(consideredIdea.getId(), consideredIdea);
		
	}
	
	public void addRejectedIdea(Idea rejectedIdea){
		
		if(this.consideredIdeasMap.containsKey(rejectedIdea.getId()))
			this.consideredIdeasMap.remove(rejectedIdea.getId());
		
		this.rejectedIdeasMap.put(rejectedIdea.getId(), rejectedIdea);		
	}

	public Idea getConsideredIdea(String id) {		
		return this.consideredIdeasMap.get(id);
	}

	public List<Idea> getRejectedIdeas() {
		
		return new ArrayList<Idea>(this.rejectedIdeasMap.values());
	}

	public List<Idea> getConsideredIdeas() {
		// TODO Auto-generated method stub
		return new ArrayList<Idea>(this.consideredIdeasMap.values());
	}
	
	
	
	
}
