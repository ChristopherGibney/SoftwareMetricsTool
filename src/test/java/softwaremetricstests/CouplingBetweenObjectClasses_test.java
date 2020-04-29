package softwaremetricstests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import softwaremetrics.CouplingBetweenObjectClasses;
import softwaremetricshelperclasses.ExtractClassesCoupledFromCurrentClass;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.ExtractDependantClasses;
import softwaremetricshelperclasses.InnerClassOfFile;

public class CouplingBetweenObjectClasses_test {

	@Test
	public void testCBONormalClasses() {
		File testFileA = new File(getClass().getClassLoader().getResource("metricstestresources/CouplingBetweenObjectClasses_TestResourceA.java").getFile());
		File testFileB = new File(getClass().getClassLoader().getResource("metricstestresources/CouplingBetweenObjectClasses_TestResourceB.java").getFile());
		File testFileC = new File(getClass().getClassLoader().getResource("metricstestresources/CouplingBetweenObjectClasses_TestResourceC.java").getFile());
		File testEmptyFile = new File(getClass().getClassLoader().getResource("metricstestresources/EmptyClass.java").getFile());
		ArrayList<InnerClassOfFile> cboClasses = new ArrayList<>();
		
		try {
			cboClasses.addAll(ExtractClassesFromFile.extract(testFileA));
			cboClasses.addAll(ExtractClassesFromFile.extract(testFileB));
			cboClasses.addAll(ExtractClassesFromFile.extract(testFileC));
			cboClasses.addAll(ExtractClassesFromFile.extract(testEmptyFile));
			
			assertEquals(cboClasses.size(), 4);
			ArrayList<File> parentFiles = new ArrayList<>();
			parentFiles.add(testFileA.getParentFile());
			
			for (InnerClassOfFile currentClass : cboClasses) {
				ExtractClassesCoupledFromCurrentClass.extract(currentClass, cboClasses, parentFiles);
			}
			ExtractDependantClasses.extract(cboClasses);
			
			double cboResultA = 0, cboResultB = 0, cboResultC = 0, emptyClassResult = -1.0;
			for (InnerClassOfFile currentClass : cboClasses) {
				if (currentClass.getClassName().equals("CouplingBetweenObjectClasses_TestResourceA")) {
					cboResultA = CouplingBetweenObjectClasses.run(currentClass);
				}
				else if (currentClass.getClassName().equals("CouplingBetweenObjectClasses_TestResourceB")) {
					cboResultB = CouplingBetweenObjectClasses.run(currentClass);
				}
				else if (currentClass.getClassName().equals("CouplingBetweenObjectClasses_TestResourceC")) {
					cboResultC = CouplingBetweenObjectClasses.run(currentClass);
				}
				else if (currentClass.getClassName().equals("EmptyClass")) {
					emptyClassResult = CouplingBetweenObjectClasses.run(currentClass);
				}
			}
			assertEquals(2.0, cboResultA, 0.0);
			assertEquals(1.0, cboResultB, 0.0);
			assertEquals(1.0, cboResultC, 0.0);
			assertEquals(0.0, emptyClassResult, 0.0);
		} catch (FileNotFoundException e) {
			System.out.println("Error loading classes in CBO test");
		}
	}
}
