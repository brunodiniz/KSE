package br.ucam.kuabaSubsystem.core;

import java.util.Properties;

public class PropertiesConfigurator implements ConfigurationManager {
	private Properties properties = new Properties();
	
	public void addProperty(String propertyName, String value){
		properties.setProperty(propertyName, value);
	}	
	@Override
	public Properties getConfigurationProperties() {
		
		return properties;
	}
	
	public void put(String key, Object value){
		this.properties.put(key, value);
	}
	

}
