package br.ucam.kuabaSubsystem.util;

import java.io.File;
import java.io.IOException;

public class Sequence {

	private String name;
	
	private long currentVal;
	
	private File holder;
	
	private int fileLine;
	
	private int step;
	
	public Sequence(File holder, String name){
		this.name = name;
		this.holder = holder;
	}
	
	public Sequence(String name, long currentVal, File holder, int fileLine,
			int step) {
		super();
		this.name = name;
		this.currentVal = currentVal;
		this.holder = holder;
		this.fileLine = fileLine;
		this.step = step;
	}
	
	public long currentVal() {
		return this.currentVal;
	}
	
	public long nextVal(){
		try {
			this.currentVal = Long.parseLong(
					FileUtil.getLineContent(this.holder, this.fileLine));
			this.currentVal += this.step ;
			FileUtil.changeFile(this.holder, this.fileLine, this.currentVal + "");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.currentVal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCurrentVal() {
		return currentVal;
	}

	public void setCurrentVal(long currentVal) {
		this.currentVal = currentVal;
		try {
			FileUtil.changeFile(this.holder, this.fileLine, this.currentVal + "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public File getHolder() {
		return holder;
	}

	public void setHolder(File holder) {
		this.holder = holder;
	}

	public int getFileLine() {
		return fileLine;
	}

	public void setFileLine(int fileLine) {
		this.fileLine = fileLine;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	
}
