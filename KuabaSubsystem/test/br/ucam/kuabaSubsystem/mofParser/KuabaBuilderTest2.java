package br.ucam.kuabaSubsystem.mofParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.util.FileUtils;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.util.Sequence;
import br.ucam.kuabaSubsystem.util.SequenceGenerator;
import br.ucam.kuabaSubsystem.util.SequenceGeneratorImpl;

public class KuabaBuilderTest2 extends AbstractKuabaTestCase {
	private KuabaBuilder builder;
	FormalModel uml;
	protected void setUp() throws Exception {
		super.setUp();
		 uml = this.factory.getKuabaRepository().getFormalModel("UML");
		SequenceGenerator generator = new SequenceGeneratorImpl(new File("sequences/sequences.txt"));
		
		Sequence seq = null;
		if(generator.loadSequence("TestSequence") == null)
			seq = generator.createNewSequence("TestSequence", 0, 1);
		else
			seq = generator.loadSequence("TestSequence");
		
		
		this.builder = new KuabaBuilder(this.factory, uml,seq );
	}
	
	public void testCreateQuestion(){
		
		this.builder.createRootQuestion("teste");
		List<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
	}

}
