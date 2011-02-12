package de.unisb.cs.st.javalanche.mutation.runtime.testDriver.junit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.unisb.cs.st.javalanche.mutation.properties.MutationProperties;
import de.unisb.cs.st.javalanche.mutation.runtime.testDriver.MutationTestRunnable;
import de.unisb.cs.st.javalanche.mutation.runtime.testDriver.SingleTestResult;
import de.unisb.cs.st.javalanche.mutation.runtime.testDriver.SingleTestResult.TestOutcome;
import de.unisb.cs.st.javalanche.mutation.runtime.testDriver.junit.data.AllTestsJunit3;

public class Junit4MutationTestDriverTest {

	private Junit4MutationTestDriver driver;

	@Before
	public void setUp() {
		MutationProperties.TEST_SUITE = AllTestsJunit3.class.getName();
		driver = new Junit4MutationTestDriver();

	}

	@Test
	public void testSize() {
		List<String> allTests = driver.getAllTests();
		assertThat(allTests.size(), is(3));
	}

	@Test
	public void testPassingTest() {
		runTestHelper("testPass", TestOutcome.PASS);
	}

	@Test
	public void testFailingTest() {
		runTestHelper("testFailure", TestOutcome.FAIL);
	}

	@Test
	public void testErrorTest() {
		runTestHelper("testError", TestOutcome.ERROR);
	}

	@Test
	public void testErrorMessage() {
		SingleTestResult result2 = runTestHelper("testError", TestOutcome.ERROR);
		String message = result2.getTestMessage().getMessage();
		assertThat(message, containsString(SimpleTest.ERROR_MESSAGE));
		String trace1 = "testclasses.SimpleTest.testError";
		assertThat(message, not(containsString(trace1)));
	}

	@Test
	public void testErrorMessageWithTrace() {
		MutationProperties.IGNORE_EXCEPTION_TRACES = false;
		SingleTestResult result2 = runTestHelper("testError", TestOutcome.ERROR);
		String message = result2.getTestMessage().getMessage();
		assertThat(message, containsString(SimpleTest.ERROR_MESSAGE));
		String trace1 = "testclasses.SimpleTest.testError";
		String trace2 = "de.unisb.cs.st.javalanche.mutation.runtime.testDriver.junit.Junit4MutationTestDriverTest.testErrorMessageWithTrace";
		assertThat(message, containsString(trace1));
		assertThat(message, containsString(trace2));
	}

	private SingleTestResult runTestHelper(String testName, TestOutcome expected) {
		List<String> allTests = driver.getAllTests();
		String test = getElement(allTests, testName);
		MutationTestRunnable runnable = driver.getTestRunnable(test);
		runnable.run();
		SingleTestResult result = runnable.getResult();
		TestOutcome outcome = result.getOutcome();
		assertThat(outcome, is(expected));
		return result;
	}

	private String getElement(List<String> allTests, String testName) {
		for (String string : allTests) {
			if (string.contains(testName)) {
				return string;
			}
		}
		return null;
	}
}
