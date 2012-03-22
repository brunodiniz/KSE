package br.ucam.kuabaSubsystem.mofParser;

import java.io.IOException;
import java.util.List;

import javax.jmi.xmi.MalformedXMIException;

public interface MetamodelReader {
	
	public List read(String resource) throws IOException, MalformedXMIException;

}
