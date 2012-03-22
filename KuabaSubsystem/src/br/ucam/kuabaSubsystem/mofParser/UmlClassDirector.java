package br.ucam.kuabaSubsystem.mofParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.xmi.MalformedXMIException;

import br.ucam.kuabaSubsystem.util.MofHelper;

public class UmlClassDirector extends MofDirector {

	
	public UmlClassDirector(MofPackage pack){
		super(pack);
	}

	public UmlClassDirector() {
		super();

	}

	@Override
	protected List<MofClass> getClassList(List<MofPackage> packages) {
		for (MofPackage mofPackage : packages) {
			if (mofPackage.getName().equals("Core")) {
				return MofHelper.classesOfPackage(mofPackage);
			}
		}
		return new ArrayList<MofClass>();
	}	

}
