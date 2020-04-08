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
			
			ArrayList<Entry<InnerClassOfFile, Double>> lcom5Result = LackOfCohesionOfMethodsFive.run(f);
			ArrayList<Entry<InnerClassOfFile, Double>> classCohesionResult = ClassCohesion.run(f);
			ArrayList<Entry<InnerClassOfFile, Double>> sensitiveClassCohesionResult = SensitiveClassCohesion.run(f);
			for (Entry<InnerClassOfFile, Double> lcom5 : lcom5Result) {
				String fileClassName = packageName + fileName + lcom5.getKey().getClassName();
				classResultsMap.addResult(fileClassName, "LCOM5", lcom5.getValue());
			}
			for (Entry<InnerClassOfFile, Double> classCohesion : classCohesionResult) {
				String fileClassName = packageName + fileName + classCohesion.getKey().getClassName();
				classResultsMap.addResult(fileClassName, "ClassCohesion", classCohesion.getValue());
			}
			for (Entry<InnerClassOfFile, Double> sensitiveClassCohesion : sensitiveClassCohesionResult) {
				String fileClassName = packageName + fileName + sensitiveClassCohesion.getKey().getClassName();
				classResultsMap.addResult(fileClassName, "SensitiveClassCohesion", sensitiveClassCohesion.getValue());
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
			double dataAbstractionCouplingResult = DataAbstractionCoupling.run(currentClass, allClasses, extractJavaFiles.getParentFiles());
			String fileClassName = currentClass.getPackageName() + currentClass.getFileName() + currentClass.getClassName();
			classResultsMap.addResult(fileClassName, "DataAbstractionCoupling", dataAbstractionCouplingResult);
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
