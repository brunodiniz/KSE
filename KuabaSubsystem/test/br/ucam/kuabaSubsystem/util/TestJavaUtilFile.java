package br.ucam.kuabaSubsystem.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import junit.framework.TestCase;

public class TestJavaUtilFile extends TestCase {

	public void test_list_names_of_a_directory(){
		File f = new File("test/br/ucam/kuabaSubsystem/fixtures/");
		File[] files = f.listFiles(
		new FileFilter(){
			public boolean accept(File f){
				if (f.isDirectory())
					return false;
				return true;
			}
		});
		assertEquals(true, files.length != 0);		
		Arrays.sort(files);
		assertEquals("activities.xml", files[0].getName());
		assertEquals("arguments.xml", files[1].getName());
		assertEquals("artifacts.xml", files[2].getName());		
		
	}
}
