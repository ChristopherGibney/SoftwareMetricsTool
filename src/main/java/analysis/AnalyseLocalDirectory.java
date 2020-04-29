package analysis;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import parser.ExtractClassesAndPackages;
import parser.ExtractJavaFiles;
import results.ClassResultsMap;
import softwaremetrics.AfferentCoupling;
import softwaremetrics.ClassCohesion;
import softwaremetrics.CouplingBetweenObjectClasses;
import softwaremetrics.DataAbstractionCoupling;
import softwaremetrics.EfferentCoupling;
import softwaremetrics.LackOfCohesionOfMethodsFive;
import softwaremetrics.SensitiveClassCohesion;
import softwaremetricshelperclasses.ExtractClassesCoupledFromCurrentClass;
import softwaremetricshelperclasses.ExtractDependantClasses;
import softwaremetricshelperclasses.InnerClassOfFile;

public class AnalyseLocalDirectory {

	public static void analyseLocalDirectory(File localDir, String resultsFilePath) throws IOException {
		ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(localDir);
		ClassResultsMap classResultsMap = new ClassResultsMap();
		ExtractClassesAndPackages classesAndPackages = new ExtractClassesAndPackages(extractJavaFiles.getJavaFiles(), "");
		
		for (InnerClassOfFile currentClass : classesAndPackages.getAllClasses()) {
			ExtractClassesCoupledFromCurrentClass.extract(currentClass, classesAndPackages.getAllClasses(), extractJavaFiles.getParentFiles());
		}
		
		ExtractDependantClasses.extract(classesAndPackages.getAllClasses());
		
		for (InnerClassOfFile currentClass : classesAndPackages.getAllClasses()) {
			String fileClassName = currentClass.getPackageName() + currentClass.getFileName() + currentClass.getClassName();
			
			classResultsMap.addResult(fileClassName, "LCOM5", LackOfCohesionOfMethodsFive.run(currentClass), 0);
			classResultsMap.addResult(fileClassName, "ClassCohesion", ClassCohesion.run(currentClass), 0);
			classResultsMap.addResult(fileClassName, "SensitiveClassCohesion", SensitiveClassCohesion.run(currentClass), 0);
			classResultsMap.addResult(fileClassName, "CouplingBetweenObjectClasses", CouplingBetweenObjectClasses.run(currentClass), 0);
			classResultsMap.addResult(fileClassName, "DataAbstractionCoupling", DataAbstractionCoupling.run(currentClass, classesAndPackages.getAllClasses(), extractJavaFiles.getParentFiles()), 0);
		}
		for (String packageKey : classesAndPackages.getAllPackages().keySet()) {
			String branchPackageName = packageKey;
			
			classResultsMap.addResult(branchPackageName, "AfferentCoupling", AfferentCoupling.run(classesAndPackages.getAllPackages().get(packageKey)), 0);
			classResultsMap.addResult(packageKey, "EfferentCoupling", EfferentCoupling.run(classesAndPackages.getAllPackages().get(packageKey)), 0);
		}
		
		classesAndPackages.getAllClasses().clear();
		
		Map<String, Map<String, List<Entry<Integer, Double>>>> classResults = classResultsMap.getResults();
		for (String classKey : classResults.keySet()) {
			Map<String, List<Entry<Integer, Double>>> metricResults = classResults.get(classKey);
			for (String metricKey : metricResults.keySet()) {
				//System.out.println(classKey + " " + metricKey + metricResults.get(metricKey).toString()); 
			}
			//System.out.println("\n");
		}
	}

}
