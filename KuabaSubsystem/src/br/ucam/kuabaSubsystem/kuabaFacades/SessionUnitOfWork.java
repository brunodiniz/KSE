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
        
        
        //Esse map contem o par Idea de design - XMIiD
        private final Map<Idea,String> associationEnd = new HashMap<Idea,String>(); 
        
        public boolean addAssociationEnd(Idea idea,String xmiId){
            if(!(idea==null&&xmiId==null)){
                this.associationEnd.put(idea, xmiId);
                return true;
            }
            return false;
        }
        
        public void removeAssociationEnd(Idea idea){
                this.associationEnd.remove(idea);
        }
        
        public Idea getAssociationEndIdea(String xmiId){
            if(this.associationEnd.containsValue(xmiId)){
                for(Idea i : this.associationEnd.keySet())
                    if(this.associationEnd.get(i).equals(xmiId))
                        return i;
            }
            return null;
        }
        
        
        
	public void addConsideredIdea(Idea consideredIdea){
		System.out.println("Texto:"+ consideredIdea.getHasText());
		System.out.println("ID"+ consideredIdea.getId());
		System.out.println();
		System.out.println(consideredIdeasMap);
                if(this.rejectedIdeasMap.containsKey(consideredIdea.getId()))
			this.rejectedIdeasMap.remove(consideredIdea.getId());
		this.consideredIdeasMap.put(consideredIdea.getId(), consideredIdea);
		
	}
	
	public void addRejectedIdea(Idea rejectedIdea){
		
		if(!this.consideredIdeasMap.containsKey(rejectedIdea.getId()))
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
        
        public boolean hasConsideredIdeas(String id){
            return this.consideredIdeasMap.containsKey(id);
        }
        public boolean hasRejectedIdeas(String id){
            return this.rejectedIdeasMap.containsKey(id);
        }
        
        //----------------------------- Estas funcoes devem ser usadas com cuidado
        
	//Remove uma ideia rejeitada da lista
	public void removeRejectedIdea(String ideaId)
        {
            if(this.rejectedIdeasMap.containsKey(ideaId))
                this.rejectedIdeasMap.remove(ideaId);
        }
        //Adiciona uma ideia rejeitada da lista
	public void removeAcceptedIdea(String elementId)
        {
            if(this.consideredIdeasMap.containsKey(elementId))
                this.consideredIdeasMap.remove(elementId);
        }

    public Idea getRejectedIdea(String elementId) {
        return this.rejectedIdeasMap.get(elementId);
    }

    public boolean containsRejectedIdeaName(String Name) {
        for (Idea d: this.getRejectedIdeas())
            if(d.getHasText().equals(Name))
                   return true;
        return false;
    }
    
    public boolean containsConsideredIdeaName(String Name) {
        for (Idea d: this.getConsideredIdeas())
            if(d.getHasText().equals(Name))
                   return true;
        return false;
    }
    
    public Idea getConsideredIdeaByName(String Name)
    {
        for (Idea d: this.getConsideredIdeas())
            if(d.getHasText().equals(Name))
                return d;
        return null;
    }
    
    public Idea getRejectedIdeaByName(String Name)
    {
        for (Idea d: this.getRejectedIdeas())
            if(d.getHasText().equals(Name))
                return d;
        return null;
    }
}
