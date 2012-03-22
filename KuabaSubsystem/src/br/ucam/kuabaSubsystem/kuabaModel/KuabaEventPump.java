package br.ucam.kuabaSubsystem.kuabaModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KuabaEventPump {
	private static KuabaEventPump instance = new KuabaEventPump();
	private HashMap<Class, List<KuabaListener>> listenerHash = 
		new HashMap<Class, List<KuabaListener>>();
	private KuabaEventPump() {
		super();
	}
	
	public static KuabaEventPump getInstance(){
		return instance;
	}
	
	public void notifyListerners(Class elementInterface, KuabaElement element){
		List<KuabaListener> listenerList = this.listenerHash.get(
				elementInterface);
		if(listenerList != null)
			for (KuabaListener kuabaListener : listenerList)
				kuabaListener.newElementCreated(element);			
	}
	public void reset(){
		listenerHash = 
			new HashMap<Class, List<KuabaListener>>();
	}
	public void addListener(Class elemInterfaceName, KuabaListener listener){
		List<KuabaListener> listenerList = this.listenerHash.get(
				elemInterfaceName); 
		if(listenerList == null){
			listenerList = new ArrayList<KuabaListener>();
			this.listenerHash.put(elemInterfaceName, listenerList);
		}
		if(!listenerList.contains(listener))
			listenerList.add(listener);
		
	}
	
	

}
