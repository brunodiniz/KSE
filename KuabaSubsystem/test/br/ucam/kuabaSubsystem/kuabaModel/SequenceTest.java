package br.ucam.kuabaSubsystem.kuabaModel;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import br.ucam.kuabaSubsystem.util.FileUtil;
import br.ucam.kuabaSubsystem.util.SequenceGenerator;
import br.ucam.kuabaSubsystem.util.SequenceGeneratorImpl;


public class SequenceTest {
	
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
	public void testCurrentVal() throws IOException {
		br.ucam.kuabaSubsystem.util.Sequence sequence1 = this.sq.loadSequence("sequence1");
		assertEquals(0, sequence1.currentVal());
	}

	@Test
	public void testNextVal() throws IOException {
		br.ucam.kuabaSubsystem.util.Sequence sequence1 = this.sq.loadSequence("sequence1");
		assertEquals(0, sequence1.currentVal());
		assertEquals("0", FileUtil.getLineContent(this.testFile, 3));
		
		assertEquals(1, sequence1.nextVal());
		
		assertEquals(1, sequence1.currentVal());
		assertEquals("1", FileUtil.getLineContent(this.testFile, 3));
		
		assertEquals(2, sequence1.nextVal());
		
		assertEquals(2, sequence1.currentVal());
		assertEquals("2", FileUtil.getLineContent(this.testFile, 3));
		
		assertEquals(3, sequence1.nextVal());
		
		assertEquals(3, sequence1.currentVal());
		assertEquals("3", FileUtil.getLineContent(this.testFile, 3));
	}
	
	@Test
	public void testNextValStep2() throws IOException {
		br.ucam.kuabaSubsystem.util.Sequence sequence2 = this.sq.loadSequence("sequence2");
		assertEquals(1, sequence2.currentVal());
		assertEquals("1", FileUtil.getLineContent(this.testFile, 6));
		
		assertEquals(3, sequence2.nextVal());
		
		assertEquals(3, sequence2.currentVal());
		assertEquals("3", FileUtil.getLineContent(this.testFile, 6));
		
		assertEquals(5, sequence2.nextVal());
		
		assertEquals(5, sequence2.currentVal());
		assertEquals("5", FileUtil.getLineContent(this.testFile, 6));
		
		assertEquals(7, sequence2.nextVal());
		
		assertEquals(7, sequence2.currentVal());
		assertEquals("7", FileUtil.getLineContent(this.testFile, 6));
	}
}
