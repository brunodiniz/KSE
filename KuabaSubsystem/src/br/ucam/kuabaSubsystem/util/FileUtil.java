package br.ucam.kuabaSubsystem.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.zip.CRC32;


public class FileUtil {
	
	private static int bufferSize = 4 * 1024;
	
	public static void copyFile(File srcFile, File destFile)
	throws IOException {
		
		InputStream in = new FileInputStream(srcFile);
		OutputStream out = new FileOutputStream(destFile, true);
	
		byte[] buffer = new byte[bufferSize];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) >= 0) {		
			out.write(buffer, 0, bytesRead);
		}
		out.close();
		in.close();	
	}
	
	public static void clearFile(File fileToClear) throws IOException{
		FileOutputStream out = new FileOutputStream(fileToClear);
		out.close();
	}
	
	public static void changeFile(File src, int lineNumber, String content) throws IOException{		
		
		FileReader reader = new FileReader(src);
		
		BufferedReader buffReader =  new BufferedReader(reader);
		String line;
		String fileContent = "";
		
		int count = 0;
		while ((line = buffReader.readLine())!= null) {			
			count++;
			if(count == lineNumber){
				line = content;
			}
			fileContent += line+"\n"; 
		}		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(src));
		writer.write(fileContent);
		
		writer.flush();
		writer.close();
	}
	
	public static int seekLine(File src, String lineContent) throws IOException{
		
		FileReader reader = new FileReader(src);
		
		BufferedReader buffReader =  new BufferedReader(reader);
		String line;		
		int count = 0;
		while ((line = buffReader.readLine())!= null) {			
			count++;
			if(line.equals(lineContent)){
				return count;
			}			 
		}
		buffReader.close();
		return -1;
	}
	
	public static String getLineContent(File src, int lineNumber) throws IOException{
		
		FileReader reader = new FileReader(src);		
		BufferedReader buffReader =  new BufferedReader(reader);
		String line;		
		int count = 1;		
		while (((line = buffReader.readLine())!= null) && (count < lineNumber))			
			count++;			
		
		buffReader.close();
		return line;
	}
	
	public static void addContent(File fileToAppend, String content) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileToAppend, true));
		writer.newLine();
		writer.append(content);
		writer.flush();
		writer.close();
	}
}
