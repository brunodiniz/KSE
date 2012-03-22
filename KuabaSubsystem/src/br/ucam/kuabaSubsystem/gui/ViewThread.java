package br.ucam.kuabaSubsystem.gui;

import br.ucam.kuabaSubsystem.controller.ViewRenderer;
import javax.swing.JDialog;

public class ViewThread extends Thread {
	private JDialog view;
	
	public ViewThread(JDialog view) {
		this.view = view;		
	}

	@Override
	public void run() {	
		this.view.setVisible(true);        
	}
}
