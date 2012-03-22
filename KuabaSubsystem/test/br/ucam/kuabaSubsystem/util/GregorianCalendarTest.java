package br.ucam.kuabaSubsystem.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;

import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;

public class GregorianCalendarTest extends AbstractKuabaTestCase {
		
	public void test_parse_gregorianCalendar_RDFLiteral_vice_versa(){
		
		
		OWLLiteral literal = ((OWLOntology) this.repo.getModel()).getOWLOntologyManager().getOWLDataFactory().getOWLLiteral("2008-06-21T20:22:03", "xsd:dateTime");
		assertEquals("2008-06-21T20:22:03", literal.getLiteral());
		GregorianCalendar calendar = new GregorianCalendar();
		Date date = null;
		SimpleDateFormat xmlFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		try {
			date = xmlFormat.parse(literal.getLiteral());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		calendar.setTime(date);
		assertEquals(2008, calendar.get(GregorianCalendar.YEAR));
		assertEquals(05, calendar.get(GregorianCalendar.MONTH));
		assertEquals(21, calendar.get(GregorianCalendar.DAY_OF_MONTH));
		
		assertEquals(20, calendar.get(GregorianCalendar.HOUR_OF_DAY));
		assertEquals(22, calendar.get(GregorianCalendar.MINUTE));
		assertEquals(03, calendar.get(GregorianCalendar.SECOND));
		
		assertEquals("2008-06-21T20:22:03", xmlFormat.format(calendar.getTime()));
	}
}
