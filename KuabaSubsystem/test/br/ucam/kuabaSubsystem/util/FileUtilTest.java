package br.ucam.kuabaSubsystem.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.ucam.kuabaSubsystem.util.FileUtil;


public class FileUtilTest {
	File testFile;
	@Before
	public void setUp() throws Exception {
		
		File fixture = new File("test/br/ucam/kuabaSubsystem/fixtures/fileUtil/fileUtil.txt");
		this.testFile = new File("test/br/ucam/kuabaSubsystem/testBase/fileOperationsTestBase.txt");
		new FileOutputStream(this.testFile).close();
		
		FileUtil.copyFile(fixture, this.testFile);
	}
	
	@After
	public void tearDown() throws Exception {
		
		
	}
	@Test
	public void testChangeFile() throws IOException {
		FileUtil.changeFile(this.testFile, 6, "mudei3");
	}

	@Test
	public void testSeekLine() throws Exception {
		
		assertEquals(1, FileUtil.seekLine(this.testFile, "linha1"));
		assertEquals(2, FileUtil.seekLine(this.testFile, "linha2"));
		assertEquals(3, FileUtil.seekLine(this.testFile, "linha3"));
		assertEquals(4, FileUtil.seekLine(this.testFile, "linha4"));
		assertEquals(5, FileUtil.seekLine(this.testFile, "linha5"));
		
		assertEquals(-1, FileUtil.seekLine(this.testFile, "linha"));
	}
	@Test
	public void testGetLineContent() throws Exception {
		
		assertEquals("linha1", FileUtil.getLineContent(this.testFile, 1));
		assertEquals("linha2", FileUtil.getLineContent(this.testFile, 2));
		assertEquals("linha3", FileUtil.getLineContent(this.testFile, 3));
		assertEquals(null, FileUtil.getLineContent(this.testFile, 7));
	}
	
	@Test
	public void testAddContent() throws Exception {
		FileUtil.addContent(this.testFile, "new content");
		assertEquals(6, FileUtil.seekLine(this.testFile, "new content"));
		
		FileUtil.addContent(this.testFile, "new content2");
		assertEquals(7, FileUtil.seekLine(this.testFile, "new content2"));
		
		FileUtil.addContent(this.testFile, "new content3");
		assertEquals(8, FileUtil.seekLine(this.testFile, "new content3"));
	}
}
