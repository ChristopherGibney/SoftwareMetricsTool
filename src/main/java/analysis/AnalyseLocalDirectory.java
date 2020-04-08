package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import parser.ExtractJavaFiles;
import results.ClassResultsMap;
import softwaremetrics.ClassCohesion;
import softwaremetrics.CouplingBetweenObjectClasses;
import softwaremetrics.DataAbstractionCoupling;
import softwaremetrics.LackOfCohesionOfMethodsFive;
import softwaremetrics.SensitiveClassCohesion;
import softwaremetricshelperclasses.ExtractClassesCoupledFromCurrentClass;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.ExtractDependantClasses;
import softwaremetricshelperclasses.InnerClassOfFile;

public class AnalyseLocalDirectory {

	public static void analyseLocalDirectory(File localDir, String resultsFilePath) throws IOException {
		ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(localDir);
		ArrayList<File> localJavaFiles = extractJavaFiles.getJavaFiles();
		ClassResultsMap classResultsMap = new ClassResultsMap();
		
		ArrayList<InnerClassOfFile> allClasses = new ArrayList<>();
		
		for (File f : localJavaFiles) {
			String fileName = f.getName().substring(0, f.getName().indexOf(".")) + "/";
			String packageName = f.getParentFile().getName() + "/";
			ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(f);
			allClasses.addAll(classes);
			for (InnerClassOfFile innerClass : classes) {
				innerClass.addFileName(fileName);
				innerClass.addPackageName(packageName);
			}
			
		}
		for (InnerClassOfFile currentClass : allClasses) {
			ExtractClassesCoupledFromCurrentClass.extract(localDir, currentClass, allClasses, extractJavaFiles.getParentFiles());
		}
		ExtractDependantClasses.extract(allClasses);
		ArrayList<Entry<InnerClassOfFile, Double>> cboResults = CouplingBetweenObjectClasses.run(allClasses);
		for (Entry<InnerClassOfFile, Double> cboResult : cboResults) {
			String fileClassName = cboResult.getKey().getPackageName() + cboResult.getKey().getFileName() + cboResult.getKey().getClassName();
			classResultsMap.addResult(fileClassName, "CouplingBetweenObjectClasses", cboResult.getValue());
		}
		for (InnerClassOfFile currentClass : allClasses) {
			String fileClassName = currentClass.getPackageName() + currentClass.getFileName() + currentClass.getClassName();
			
			classResultsMap.addResult(fileClassName, "LCOM5", LackOfCohesionOfMethodsFive.run(currentClass));
			classResultsMap.addResult(fileClassName, "ClassCohesion", ClassCohesion.run(currentClass));
			classResultsMap.addResult(fileClassName, "SensitiveClassCohesion", SensitiveClassCohesion.run(currentClass));
			classResultsMap.addResult(fileClassName, "DataAbstractionCoupling", DataAbstractionCoupling.run(currentClass, allClasses, extractJavaFiles.getParentFiles()));
		}
		
		Map<String, Map<String, List<Double>>> classResults = classResultsMap.getResults();
		for (String classKey : classResults.keySet()) {
			Map<String, List<Double>> metricResults = classResults.get(classKey);
			for (String metricKey : metricResults.keySet()) {
				System.out.println(classKey + " " + metricKey + metricResults.get(metricKey).toString()); 
			}
		}
	}

}
