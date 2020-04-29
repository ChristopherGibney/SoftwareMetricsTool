package softwaremetricstests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import softwaremetrics.ClassCohesion;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;

public class ClassCohesion_Test {

	@Test
	public void testClassCohesionNormalClass() {
		File testFile = new File(getClass().getClassLoader().getResource("metricstestresources/ClassCohesion_TestResource.java").getFile());
		try {
			ArrayList<InnerClassOfFile> testFileClasses = ExtractClassesFromFile.extract(testFile);
			assertEquals(testFileClasses.size(), 1);
			double classCohesionResult = ClassCohesion.run(testFileClasses.get(0));
			assertEquals(0.25, classCohesionResult, 0.001);
		} catch (FileNotFoundException e) {
			System.out.println("Error loading LCOM5 test resource.");
		}
	}
}
