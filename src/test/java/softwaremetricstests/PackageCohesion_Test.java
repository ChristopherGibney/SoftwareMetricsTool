package softwaremetricstests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import softwaremetrics.CouplingBetweenObjectClasses;
import softwaremetrics.PackageCohesion;
import softwaremetricshelperclasses.ExtractClassesCoupledFromCurrentClass;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.ExtractDependantClasses;
import softwaremetricshelperclasses.InnerClassOfFile;

public class PackageCohesion_Test {
	
	@Test
	public void testPackageCohesion() {
		File testFileA = new File(getClass().getClassLoader().getResource("metricstestresources/CouplingBetweenObjectClasses_TestResourceA.java").getFile());
		File testFileB = new File(getClass().getClassLoader().getResource("metricstestresources/CouplingBetweenObjectClasses_TestResourceB.java").getFile());
		File testFileC = new File(getClass().getClassLoader().getResource("metricstestresources/CouplingBetweenObjectClasses_TestResourceC.java").getFile());
		File testEmptyFile = new File(getClass().getClassLoader().getResource("metricstestresources/EmptyClass.java").getFile());
		ArrayList<InnerClassOfFile> samplePackageClasses = new ArrayList<>();
	
		try {
			samplePackageClasses.addAll(ExtractClassesFromFile.extract(testFileA));
			samplePackageClasses.addAll(ExtractClassesFromFile.extract(testFileB));
			samplePackageClasses.addAll(ExtractClassesFromFile.extract(testFileC));
			samplePackageClasses.addAll(ExtractClassesFromFile.extract(testEmptyFile));
			
			assertEquals(samplePackageClasses.size(), 4);
			ArrayList<File> parentFiles = new ArrayList<>();
			parentFiles.add(testFileA.getParentFile());
			
			for (InnerClassOfFile currentClass : samplePackageClasses) {
				ExtractClassesCoupledFromCurrentClass.extract(currentClass, samplePackageClasses, parentFiles);
			}
			ExtractDependantClasses.extract(samplePackageClasses);
				
			Double packageCohesionValue = PackageCohesion.run(samplePackageClasses);
			
			//Of the sample package classes provided, A is coupled to B and C and B is coupled to A
			//therefore result should be 3/4*(4-1) = 0.25
			assertEquals(0.25, packageCohesionValue, 0.0);
		} catch (FileNotFoundException e) {
				System.out.println("Error loading classes in CBO test");
		}
	}
	
	@Test
	public void testPackageCohesionInterface() {
		File testFileA = new File(getClass().getClassLoader().getResource("metricstestresources/PackageCohesion_TestResourceA.java").getFile());
		File testFileB = new File(getClass().getClassLoader().getResource("metricstestresources/PackageCohesion_TestResourceB.java").getFile());
		File testFileC = new File(getClass().getClassLoader().getResource("metricstestresources/PackageCohesion_TestResourceC.java").getFile());
		ArrayList<InnerClassOfFile> samplePackageClasses = new ArrayList<>();
	
		try {
			samplePackageClasses.addAll(ExtractClassesFromFile.extract(testFileA));
			samplePackageClasses.addAll(ExtractClassesFromFile.extract(testFileB));
			samplePackageClasses.addAll(ExtractClassesFromFile.extract(testFileC));
			
			assertEquals(samplePackageClasses.size(), 3);
			ArrayList<File> parentFiles = new ArrayList<>();
			parentFiles.add(testFileA.getParentFile());
			
			for (InnerClassOfFile currentClass : samplePackageClasses) {
				ExtractClassesCoupledFromCurrentClass.extract(currentClass, samplePackageClasses, parentFiles);
			}
			ExtractDependantClasses.extract(samplePackageClasses);
				
			Double packageCohesionValue = PackageCohesion.run(samplePackageClasses);
			
			//Of the sample package classes provided, C is interface of A and B
			//therefore result should be 2/3*(3-1) = 0.33
			assertEquals(0.33, packageCohesionValue, 0.01);
		} catch (FileNotFoundException e) {
				System.out.println("Error loading classes in CBO test");
		}
	}
}	
