package br.ucam.kuabaSubsystem.kuabaModel;

import java.util.ArrayList;
import java.util.Collection;


import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.ExpectedDuration;

//import com.hp.hpl.jena.util.FileUtils;

public class ExpectedDurationTest extends AbstractKuabaTestCase {

	public void test_set_and_get_has_amount(){
		
		ExpectedDuration testDuration = this.factory.createExpectedDuration("one week");
		
		testDuration.setHasAmount(7);
		
		assertTrue(testDuration.hasHasAmount());
		
//		ArrayList<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
//		assertTrue(errors.isEmpty());
		
		ExpectedDuration retrievedTestDuration = this.repo.getExpectedDuration("one_week");
		
		assertEquals(7, retrievedTestDuration.getHasAmount());
	}
	
	public void test_add_unit_time(){
		
		ExpectedDuration testDuration = this.factory.createExpectedDuration("one week");
		
		testDuration.addHasUnitTime("Days");
		
		assertFalse(testDuration.getHasUnitTime().isEmpty());
		assertTrue(testDuration.hasHasUnitTime());
		
		assertTrue(testDuration.getHasUnitTime().contains("Days"));
		
		testDuration.addHasUnitTime("Week");
		assertEquals(2, testDuration.getHasUnitTime().size());
		assertTrue(testDuration.getHasUnitTime().contains("Week"));
		
//		ArrayList<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
//		assertTrue(errors.isEmpty());
	}
	
	public void test_add_repeated_unit_times(){
		ExpectedDuration month = this.repo.getExpectedDuration("one_month");
		month.addHasUnitTime("day");
		month.addHasUnitTime("day");
		month.addHasUnitTime("day");
		
		Collection<String> unitTimes = month.getHasUnitTime();
		assertTrue(month.hasHasUnitTime());
		
		assertEquals(1, unitTimes.size());
		assertTrue(unitTimes.contains("day"));
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		
	}
}
