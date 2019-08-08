package jUnitTesting1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test1 {
	/**
	 * JUnit test to check that a rating above 5 is Positive, 5 or below is Negative
	 */
	@Test
	void test() {
		JUnitTesting1 test = new JUnitTesting1();
		
		for(int i = 1; i < 11; i++) {
			String sTest = test.getReviewDecision(i);
			if(i > 5)
				assertEquals("Positive", sTest);
			else
				assertEquals("Negative", sTest);
		}	
	}
}
