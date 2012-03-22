package br.ucam.kuabaSubsystem.controller;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import br.ucam.kuabaSubsystem.kuabaModel.Argument;

public class ArgumentTableModel extends DefaultTableModel {
	private List<Argument> arguments;

	public void insertArguments(List<Argument> arguments){
		this.arguments = arguments;
		for (Argument argument : arguments)
			this.addRow(new String[]{argument.getHasText()});		
	}
	
	public List<Argument> getArguments(){
		return this.arguments;
	}
	
	

}
