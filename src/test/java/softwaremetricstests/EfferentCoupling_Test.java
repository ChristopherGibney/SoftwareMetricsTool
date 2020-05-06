package softwaremetricstests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import softwaremetrics.EfferentCoupling;
import softwaremetricshelperclasses.ExtractClassesCoupledFromCurrentClass;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.ExtractDependantClasses;
import softwaremetricshelperclasses.InnerClassOfFile;

public class EfferentCoupling_Test {
	@Test
	public void testAfferentCoupling() {
		File testFileA = new File(getClass().getClassLoader().getResource("metricstestresources/PackageCoupling_TestResourceA.java").getFile());
		File testFileB = new File(getClass().getClassLoader().getResource("metricstestresources/PackageCoupling_TestResourceB.java").getFile());
		File testFileC = new File(getClass().getClassLoader().getResource("metricstestresources/PackageCoupling_TestResourceC.java").getFile());
		File testEmptyFile = new File(getClass().getClassLoader().getResource("metricstestresources/EmptyClass.java").getFile());
		File testFileD = new File(getClass().getClassLoader().getResource("metricstestresourcessecondpackage/PackageCoupling_TestResourceD.java").getFile());
		File testFileE = new File(getClass().getClassLoader().getResource("metricstestresourcessecondpackage/PackageCoupling_TestResourceE.java").getFile());
		File testFileF = new File(getClass().getClassLoader().getResource("metricstestresourcessecondpackage/PackageCoupling_TestResourceF.java").getFile());
		
		ArrayList<InnerClassOfFile> packageClasses1 = new ArrayList<>();
		ArrayList<InnerClassOfFile> packageClasses2 = new ArrayList<>();
		
		try {
			packageClasses1.addAll(ExtractClassesFromFile.extract(testFileA));
			packageClasses1.addAll(ExtractClassesFromFile.extract(testFileB));
			packageClasses1.addAll(ExtractClassesFromFile.extract(testFileC));
			packageClasses1.addAll(ExtractClassesFromFile.extract(testEmptyFile));
			
			assertEquals(packageClasses1.size(), 4);
			
			packageClasses2.addAll(ExtractClassesFromFile.extract(testFileD));
			packageClasses2.addAll(ExtractClassesFromFile.extract(testFileE));
			packageClasses2.addAll(ExtractClassesFromFile.extract(testFileF));
			
			ArrayList<File> parentFiles = new ArrayList<>();
			parentFiles.add(testFileA.getParentFile());
			parentFiles.add(testFileD.getParentFile());
			
			ArrayList<InnerClassOfFile> allClasses = new ArrayList<>();
			allClasses.addAll(packageClasses1);
			allClasses.addAll(packageClasses2);
			
			for (InnerClassOfFile currentClass : packageClasses1) {
				currentClass.addPackageName(testFileA.getParentFile().getName());
			}
			for (InnerClassOfFile currentClass : packageClasses2) {
				currentClass.addPackageName(testFileD.getParentFile().getName());
			}
			
			for (InnerClassOfFile currentClass : allClasses) {
				ExtractClassesCoupledFromCurrentClass.extract(currentClass, allClasses, parentFiles);
			}
			ExtractDependantClasses.extract(allClasses);
			
			Double efferentValue = EfferentCoupling.run(packageClasses2);
			
			//Of the sample package classes provided, A is coupled to B and C and B is coupled to A
			//therefore result should be 3/4*(4-1) = 0.25
			assertEquals(3, efferentValue, 0.0);
		} catch (FileNotFoundException e) {
				System.out.println("Error loading classes in Efferent Coupling test");
		}
	}
}
