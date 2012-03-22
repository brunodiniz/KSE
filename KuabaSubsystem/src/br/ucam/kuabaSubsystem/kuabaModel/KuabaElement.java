package br.ucam.kuabaSubsystem.kuabaModel;

import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import java.awt.Dimension;



public interface KuabaElement {
	
	public String getImage();
	public Dimension getImageSize();
	public void setId(String id);	
	public String getId();
	public void setCanCopy(boolean canCopy);
        public KuabaRepository getRepository();
        public void remove();
}
