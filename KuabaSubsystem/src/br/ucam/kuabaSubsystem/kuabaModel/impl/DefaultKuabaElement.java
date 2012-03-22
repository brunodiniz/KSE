package br.ucam.kuabaSubsystem.kuabaModel.impl;

import java.awt.Dimension;



import br.ucam.kuabaSubsystem.kuabaModel.KuabaElement;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.util.Sequence;

import java.util.Collection;

//import edu.stanford.smi.protege.model.DefaultInstance;

public class DefaultKuabaElement implements KuabaElement {
	
//	private boolean canCopy = true;
	public static Sequence ID_GENERATOR = null;
        
        private KuabaRepository repo;
        private String id;
	
//        public DefaultKuabaElement() {
//		super();	
//	}
        
        public DefaultKuabaElement(String id, KuabaRepository repo) {
		this.id = id;
                this.repo = repo;
	}                    
        
	public static void setIdGenerator(Sequence idGenerator){
		ID_GENERATOR = idGenerator;
//		DefaultInstance.setIdGenerator(new ProtegeIdGeneratorAdapter(idGenerator));
	} 
	
	@Override
	public String getId() {
                return id;	
	}
        
        @Override
	public void setId(String id) {
                this.repo.rename(this.id, id);
		this.id = id;
	}
	
	@Override
	public String getImage() {		
		return "";
	}
	public Dimension getImageSize(){
		return new Dimension(10, 10);
	}	

	public void setCanCopy(boolean canCopy) {
//            this.canCopy = canCopy;
		if(canCopy){
			this.removeComment("false");
		}
		else{			
			this.addComment("false");
		}		
	}
	
	protected boolean canCopy() {
//		if(this.getComments().contains("false"))
//			return false;	
		return true;
	}
	
        
        protected boolean hasProperty(String propertyName) {
            return this.repo.hasProperty(propertyName, getId());
        }
        
        protected Collection<String> getDataPropertyValues(String propertyName) {
            return this.repo.getDataPropertyValues(propertyName, getId());
        }
        
        protected void addDataPropertyValue(String propertyName, String value) {
            this.repo.addDataPropertyValue(propertyName, getId(), value);
        }
        
        protected void removeDataPropertyValue(String propertyName, String value) {
            this.repo.removeDataPropertyValue(propertyName, getId(), value);
        }
        
        protected void setDataPropertyValues(String propertyName, Collection<String> values) {
            this.repo.setDataPropertyValues(propertyName, getId(), values);
        }
        
        
        protected Collection getObjectPropertyValues(String propertyName) {
            return this.repo.getObjectPropertyValues(propertyName, getId());
        }
        
        protected void addObjectPropertyValue(String propertyName, KuabaElement value) {
            this.repo.addObjectPropertyValue(propertyName, getId(), value);
        }
        
        protected void removeObjectPropertyValue(String propertyName, KuabaElement value) {
            this.repo.removeObjectPropertyValue(propertyName, getId(), value);
        }
        
        protected void setObjectPropertyValues(String propertyName, Collection values) {
            this.repo.setObjectPropertyValues(propertyName, getId(), values);
        }
         
        private Collection<String> getComments() {
            return this.repo.getComments(getId());
        }
        
        private void removeComment(String string) {
            this.repo.removeComment(string, getId());
        }

        private void addComment(String string) {
            this.repo.addComment(string, getId());
        }
        
        @Override
        public boolean equals(Object object) {
            if (object instanceof KuabaElement) {
//                KuabaElement e = (KuabaElement) object;
//                if(this.repo.equals(e.getRepository()) && this.id.equals(e.getId())) return true;
                return (object.hashCode() == this.hashCode());
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 41 * hash + (this.repo != null ? this.repo.hashCode() : 0);
            hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
            return hash;
        }

        public KuabaRepository getRepository() {
            return this.repo;
        }

        public void remove() {
            this.repo.removeIndividual(getId());
        }

}
