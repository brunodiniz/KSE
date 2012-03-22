package br.ucam.kuabaSubsystem.kuabaModel.visitors;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;


public interface KuabaVisitor {
	public void visit(Idea idea);
}
