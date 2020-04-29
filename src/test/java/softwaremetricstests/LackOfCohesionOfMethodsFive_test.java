package softwaremetricstests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import softwaremetrics.LackOfCohesionOfMethodsFive;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;

public class LackOfCohesionOfMethodsFive_test {

	@Test
	public void testLCOM5NormalClass() {
		File testFile = new File(getClass().getClassLoader().getResource("metricstestresources/LCOM5_TestResource.java").getFile());
		try {
			ArrayList<InnerClassOfFile> testFileClasses = ExtractClassesFromFile.extract(testFile);
			assertEquals(testFileClasses.size(), 1);
			double lcom5Result = LackOfCohesionOfMethodsFive.run(testFileClasses.get(0));
			assertEquals(0.875, lcom5Result, 0.0);
		} catch (FileNotFoundException e) {
			System.out.println("Error loading LCOM5 test resource.");
		}
	}
	
	@Test
	public void testLCOM5EmptyClass() {
		File testFile = new File(getClass().getClassLoader().getResource("metricstestresources/EmptyClass.java").getFile());
		try {
			ArrayList<InnerClassOfFile> testFileClasses = ExtractClassesFromFile.extract(testFile);
			assertEquals(testFileClasses.size(), 1);
			Double lcom5Result = LackOfCohesionOfMethodsFive.run(testFileClasses.get(0));
			assertTrue(lcom5Result.isNaN());
		} catch (FileNotFoundException e) {
			System.out.println("Error loading Empty Class resource.");
		}
	}
}
