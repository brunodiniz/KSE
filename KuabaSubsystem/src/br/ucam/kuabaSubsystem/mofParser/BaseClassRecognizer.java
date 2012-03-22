package br.ucam.kuabaSubsystem.mofParser;

import java.util.List;

import javax.jmi.model.ModelElement;
import javax.jmi.model.MofClass;

public interface BaseClassRecognizer {

	public MofClass findBaseClass(List<MofClass> modelElements);
}
