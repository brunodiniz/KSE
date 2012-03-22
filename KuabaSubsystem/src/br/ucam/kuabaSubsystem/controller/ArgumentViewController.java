/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ucam.kuabaSubsystem.controller;

import br.ucam.kuabaSubsystem.gui.ArgumentView;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Thiago
 */
public class ArgumentViewController extends ArgumentController {

	public ArgumentViewController(Idea idea, Question consideredQuestion) {
		super(new Idea[]{idea}, null, consideredQuestion);
	}
	public ArgumentViewController(Idea idea) {
		super(new Idea[]{idea}, null, null);
	}
	
	@Override
	public JDialog getView() {
		ArgumentView view = new ArgumentView(this);
		view.setTitle("Argument View");
		view.setIdeaText(this.inFavoridea[0].getHasText());
		return view;
	}

}
