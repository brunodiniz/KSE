package br.ucam.kuabaSubsystem.kuabaFacades;

import javax.jmi.reflect.RefObject;

public class DefaultXmiIdResolver implements XMIIdResolver {

	@Override
	public String resolveXmiId(RefObject refObject) {
		
		return refObject.refMofId();
	}

}
