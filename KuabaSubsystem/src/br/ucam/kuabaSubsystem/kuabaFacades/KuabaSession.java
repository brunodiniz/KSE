package br.ucam.kuabaSubsystem.kuabaFacades;

import java.util.HashMap;
import java.util.Map;

import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;

public class KuabaSession {
	
	private Map<String, Idea> mofIdIdeaIdMap = new HashMap<String, Idea>();
	private SessionUnitOfWork unitOfWork = new SessionUnitOfWork();	
        
        
        
        
	public void putIdea(String elementId, Idea idea){
		this.mofIdIdeaIdMap.put(elementId, idea);
	}
	public Idea getIdea(String elementId){
		
		return this.mofIdIdeaIdMap.get(elementId);
	}
        
        public boolean hasIdea(String elementId){
            return this.mofIdIdeaIdMap.containsKey(elementId);
        }
	
	public Idea removeEntry(String elementId){
		return this.mofIdIdeaIdMap.remove(elementId);
	}
	public void persistSession() {
		
	}
	public SessionUnitOfWork unitOfWork() {
		return this.unitOfWork;
	}

}
