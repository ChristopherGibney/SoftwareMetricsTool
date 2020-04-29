package softwaremetricstests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import softwaremetrics.CouplingBetweenObjectClasses;
import softwaremetrics.DataAbstractionCoupling;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;

public class DataAbstractionCoupling_Test {
	
	@Test
	public void testDACNormalClasses() {
		File testFileA = new File(getClass().getClassLoader().getResource("metricstestresources/DataAbstractionCoupling_TestResourceA.java").getFile());
		File testFileB = new File(getClass().getClassLoader().getResource("metricstestresources/DataAbstractionCoupling_TestResourceB.java").getFile());
		File testFileC = new File(getClass().getClassLoader().getResource("metricstestresources/DataAbstractionCoupling_TestResourceC.java").getFile());
		File testEmptyFile = new File(getClass().getClassLoader().getResource("metricstestresources/EmptyClass.java").getFile());
		ArrayList<InnerClassOfFile> dacClasses = new ArrayList<>();
		
		try {
			dacClasses.addAll(ExtractClassesFromFile.extract(testFileA));
			dacClasses.addAll(ExtractClassesFromFile.extract(testFileB));
			dacClasses.addAll(ExtractClassesFromFile.extract(testFileC));
			dacClasses.addAll(ExtractClassesFromFile.extract(testEmptyFile));
			
			assertEquals(dacClasses.size(), 4);
			ArrayList<File> parentFiles = new ArrayList<>();
			parentFiles.add(testFileA.getParentFile());
			
			double dacResultA = 0, dacResultB = 0, dacResultC = -1, emptyClassResult = -1.0;
			for (InnerClassOfFile currentClass : dacClasses) {
				if (currentClass.getClassName().equals("DataAbstractionCoupling_TestResourceA")) {
					dacResultA = DataAbstractionCoupling.run(currentClass, dacClasses, parentFiles);
				}
				else if (currentClass.getClassName().equals("DataAbstractionCoupling_TestResourceB")) {
					dacResultB = DataAbstractionCoupling.run(currentClass, dacClasses, parentFiles);
				}
				else if (currentClass.getClassName().equals("DataAbstractionCoupling_TestResourceC")) {
					dacResultC = DataAbstractionCoupling.run(currentClass, dacClasses, parentFiles);
				}
				else if (currentClass.getClassName().equals("EmptyClass")) {
					emptyClassResult = DataAbstractionCoupling.run(currentClass, dacClasses, parentFiles);
				}
			}
			assertEquals(4.0, dacResultA, 0.0);
			assertEquals(3.0, dacResultB, 0.0);
			assertEquals(0.0, dacResultC, 0.0);
			assertEquals(0.0, emptyClassResult, 0.0);
			
		} catch (FileNotFoundException e) {
			System.out.println("Error loading classes in DAC test");
		}
	}
}
