package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.observers.AbstractObserver;

public class CommentObserver extends AbstractObserver {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		RefObject attr = (RefObject)evt.getSource();
		System.out.println("O comentario "+attr.refGetValue("name")+ " foi modificada");
		System.out.println("Nome da propriedade: "+evt.getPropertyName());
		System.out.println("Novo valor: "+evt.getNewValue());
		
		System.out.println("--------------------------CommentObs--------------------");

	}

}
