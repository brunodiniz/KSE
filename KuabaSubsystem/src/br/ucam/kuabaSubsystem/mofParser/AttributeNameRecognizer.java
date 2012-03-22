package br.ucam.kuabaSubsystem.mofParser;

import java.util.List;

import javax.jmi.model.Attribute;
import javax.jmi.model.MofClass;

import br.ucam.kuabaSubsystem.util.MofHelper;

public class AttributeNameRecognizer implements BaseClassRecognizer {

	@Override
	public MofClass findBaseClass(List<MofClass> modelElements) {
		for (MofClass mofClass : modelElements) {
			for (Attribute attr : MofHelper.attributesOfClass(mofClass, false)) {
				if(attr.getName().equals("name"));
				return mofClass;
			}
		}
		return null;
	}

}
