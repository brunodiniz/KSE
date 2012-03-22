package br.ucam.kuabaSubsystem.util;

import java.util.Comparator;

import br.ucam.kuabaSubsystem.kuabaModel.Decision;

public class DecisionByDateComparator implements Comparator<Decision> {
	

	@Override
	public int compare(Decision o1, Decision o2) {

		return o1.getHasDate().compareTo(o2.getHasDate());
	}

}
