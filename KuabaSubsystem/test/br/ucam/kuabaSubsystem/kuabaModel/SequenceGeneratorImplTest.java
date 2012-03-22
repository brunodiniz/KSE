package br.ucam.kuabaSubsystem.kuabaModel;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import org.junit.Before;
import org.junit.Test;

import br.ucam.kuabaSubsystem.kuabaExceptions.SequenceNameAlreadyExistsException;
import br.ucam.kuabaSubsystem.kuabaExceptions.SequencesFileNotFoundException;
import br.ucam.kuabaSubsystem.util.FileUtil;
import br.ucam.kuabaSubsystem.util.Sequence;
import br.ucam.kuabaSubsystem.util.SequenceGenerator;
import br.ucam.kuabaSubsystem.util.SequenceGeneratorImpl;


public class SequenceGeneratorImplTest {
	
	private File testFile;
	private SequenceGenerator sq;
	
	@Before
	public void setUp() throws Exception {
		File fixture = new File("test/br/ucam/kuabaSubsystem/fixtures/fileUtil/sequences");
		this.testFile = new File("test/br/ucam/kuabaSubsystem/testBase/fileOperationsTestBase.txt");
		new FileOutputStream(this.testFile).close();
		
		FileUtil.copyFile(fixture, this.testFile);
		this.sq = new SequenceGeneratorImpl(this.testFile);
	}

	@Test
	public void testCreateNewSequence() throws IOException, SequenceNameAlreadyExistsException {		
		assertEquals(-1, FileUtil.seekLine(this.testFile, "newSequence"));
		Sequence newSequence = sq.createNewSequence("newSequence", 0, 1);
		
		assertEquals(10,FileUtil.seekLine(this.testFile, "newSequence"));
		assertEquals("1", FileUtil.getLineContent(this.testFile, 11));
		assertEquals("0", FileUtil.getLineContent(this.testFile, 12));
		
		assertEquals("newSequence", newSequence.getName());
		assertEquals(1, newSequence.getStep());
		assertEquals(0, newSequence.getCurrentVal());
	}

	@Test
	public void testCreateNewSequence2() throws IOException, SequenceNameAlreadyExistsException {
		
		assertEquals(-1, FileUtil.seekLine(this.testFile, "newSequence2"));
		
		Sequence newSequence = sq.createNewSequence("newSequence2", 4, 2);
		
		assertEquals(10,FileUtil.seekLine(this.testFile, "newSequence2"));
		assertEquals("2", FileUtil.getLineContent(this.testFile, 11));
		assertEquals("4", FileUtil.getLineContent(this.testFile, 12));
		
		assertEquals("newSequence2", newSequence.getName());
		assertEquals(2, newSequence.getStep());
		assertEquals(4, newSequence.getCurrentVal());
	}
	
	@Test
	public void testCreateAnExistingSequence() throws IOException {
		
		assertEquals(1, FileUtil.seekLine(this.testFile, "sequence1"));
		Sequence newSequence = null;
		try{
			newSequence =  sq.createNewSequence("sequence1", 4, 2);
			fail("An Exception must be thrown");
		}catch(SequenceNameAlreadyExistsException e){
			assertEquals("A sequence with name 'sequence1' already" +
					" exists!", e.getMessage());
		}
		
		assertEquals(1,FileUtil.seekLine(this.testFile, "sequence1"));
		assertEquals("1", FileUtil.getLineContent(this.testFile, 2));
		assertEquals("0", FileUtil.getLineContent(this.testFile, 3));
		assertNull(newSequence);
		
	}
	
	@Test
	public void testLoadSequence() throws IOException {
		
		Sequence sequence1 = this.sq.loadSequence("sequence1");
		assertEquals(1, sequence1.getStep());
		assertEquals(0, sequence1.getCurrentVal());
		
		Sequence sequence2 = this.sq.loadSequence("sequence2");
		assertEquals(2, sequence2.getStep());
		assertEquals(1, sequence2.getCurrentVal());
		
		Sequence sequence3 = this.sq.loadSequence("sequence3");
		assertEquals(1, sequence3.getStep());
		assertEquals(4, sequence3.getCurrentVal());
		
		//sequence8 does not exists, null must be returned in this case
		Sequence sequence8 = this.sq.loadSequence("sequence8");
		assertNull(sequence8);
	}

	@Test
	public void testOpenSequencesFile() throws IOException {
		
		this.sq.openSequencesFile("test/br/ucam/kuabaSubsystem/testBase/OtherSequenceFile");
		Sequence otherSequence1 = this.sq.loadSequence("otherSequence1");
		assertNotNull(otherSequence1);		
	}
	
	@Test
	public void testOpenInexistentSequencesFile() throws IOException {
	
		try{
			this.sq.openSequencesFile("test/br/ucam/kuabaSubsystem/testBase/asdfadfadf");
			fail("A SequencesFileNotFound must be thrown");
		}catch(SequencesFileNotFoundException e){
			assertEquals("The sequences file 'test/br/ucam/kuabaSubsystem/testBase/asdfadfadf' does not exists", e.getMessage());
		}
	}

}
