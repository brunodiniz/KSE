package br.ucam.kuabaSubsystem.mofParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.xmi.XmiReader;

import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

public class MofReader implements MetamodelReader {
	private XmiReader reader;
	private ModelPackage mofExtent;
	public MofReader() {
		super();
		MDRepository repository = MDRManager.getDefault().getDefaultRepository();
		this.mofExtent = (ModelPackage) repository.getExtent("MOF Extent");
		if (mofExtent == null) {

            try {
                this.mofExtent = 
                    (ModelPackage) repository.createExtent("MOF Extent");
            } catch (CreationFailedException e) {
                e.printStackTrace();
            }
		}
		reader = XMIReaderFactory.getDefault().createXMIReader();

	}

	@Override
	public List read(String resource) throws IOException, MalformedXMIException {
		
		return new ArrayList<MofPackage>(this.reader.read(resource, this.mofExtent));
	}

}
