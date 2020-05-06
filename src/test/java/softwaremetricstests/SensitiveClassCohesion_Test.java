package softwaremetricstests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import softwaremetrics.SensitiveClassCohesion;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;

public class SensitiveClassCohesion_Test {
	
	@Test
	public void testClassCohesionNormalClass() {
		File testFile = new File(getClass().getClassLoader().getResource("metricstestresources/SensitiveClassCohesion_TestResource.java").getFile());
		try {
			ArrayList<InnerClassOfFile> testFileClasses = ExtractClassesFromFile.extract(testFile);
			assertEquals(testFileClasses.size(), 1);
			double sensitiveClassCohesionResult = SensitiveClassCohesion.run(testFileClasses.get(0));
			System.out.println(sensitiveClassCohesionResult);
			assertEquals(sensitiveClassCohesionResult, 0.333, 0.001);
		} catch (FileNotFoundException e) {
			System.out.println("Error loading LCOM5 test resource.");
		}
	}
}
