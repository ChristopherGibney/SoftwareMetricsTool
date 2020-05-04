package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import parser.ExtractClassesAndPackages;
import parser.ExtractJavaFiles;
import results.ApplicationLevelResults;
import results.ClassResultsMap;
import results.HandleGitResults;
import results.HandleLocalResults;
import softwaremetrics.AfferentCoupling;
import softwaremetrics.ApplicationLevelMetric;
import softwaremetrics.ClassCohesion;
import softwaremetrics.CouplingBetweenObjectClasses;
import softwaremetrics.DataAbstractionCoupling;
import softwaremetrics.EfferentCoupling;
import softwaremetrics.LackOfCohesionOfMethodsFive;
import softwaremetrics.PackageCohesion;
import softwaremetrics.SensitiveClassCohesion;
import softwaremetricshelperclasses.ExtractClassesCoupledFromCurrentClass;
import softwaremetricshelperclasses.ExtractDependantClasses;
import softwaremetricshelperclasses.InnerClassOfFile;

public class AnalyseLocalDirectory {

	public static void analyseLocalDirectory(File localDir, String resultsFilePath) throws IOException {
		ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(localDir);
		ClassResultsMap classResultsMap = new ClassResultsMap();
		ExtractClassesAndPackages classesAndPackages = new ExtractClassesAndPackages(extractJavaFiles.getJavaFiles(), "");
		ApplicationLevelResults applicationLevelResults = new ApplicationLevelResults();
		
		for (InnerClassOfFile currentClass : classesAndPackages.getAllClasses()) {
			ExtractClassesCoupledFromCurrentClass.extract(currentClass, classesAndPackages.getAllClasses(), extractJavaFiles.getParentFiles());
		}
		ExtractDependantClasses.extract(classesAndPackages.getAllClasses());
		List<Double> LCOM5All = new ArrayList<>(), classCohesionAll= new ArrayList<>(), sensitiveClassCohesionAll = new ArrayList<>(), 
				DACAll = new ArrayList<>(), CBOAll = new ArrayList<>(), afferentAll = new ArrayList<>(), efferentAll = new ArrayList<>(),
				packageCohesionAll = new ArrayList<>();
		
		for (InnerClassOfFile currentClass : classesAndPackages.getAllClasses()) {
			String fileClassName = currentClass.getPackageName() + currentClass.getFileName() + currentClass.getClassName();
			double classWeighting = currentClass.getClassMethods().size()+currentClass.getClassVariables().size();
			
			Double lcom5Result = LackOfCohesionOfMethodsFive.run(currentClass);
			classResultsMap.addResult(fileClassName, "LCOM5", lcom5Result, 0);
			if (!lcom5Result.isNaN() && !lcom5Result.isInfinite()) {
				LCOM5All.add(lcom5Result*classWeighting);
			}
			
			Double classCohesionResult = ClassCohesion.run(currentClass);
			classResultsMap.addResult(fileClassName, "ClassCohesion", classCohesionResult, 0);
			if (!classCohesionResult.isNaN() && !classCohesionResult.isInfinite()) {
				classCohesionAll.add(classCohesionResult*classWeighting);
			}
			
			Double sensitiveClassCohesionResult = SensitiveClassCohesion.run(currentClass);
			classResultsMap.addResult(fileClassName, "SensitiveClassCohesion", sensitiveClassCohesionResult, 0);
			if (!sensitiveClassCohesionResult.isNaN() && !sensitiveClassCohesionResult.isInfinite()) {
				sensitiveClassCohesionAll.add(sensitiveClassCohesionResult*classWeighting);
			}
			
			Double cboResult = CouplingBetweenObjectClasses.run(currentClass);
			classResultsMap.addResult(fileClassName, "CouplingBetweenObjectClasses", cboResult, 0);
			CBOAll.add(cboResult*classWeighting);
			
			Double dacResult = DataAbstractionCoupling.run(currentClass, classesAndPackages.getAllClasses(), extractJavaFiles.getParentFiles());
			classResultsMap.addResult(fileClassName, "DataAbstractionCoupling", dacResult, 0);
			DACAll.add(dacResult*classWeighting);
			
			
		}
		for (String packageKey : classesAndPackages.getAllPackages().keySet()) {
			String branchPackageName = packageKey;
			double packageWeighting = classesAndPackages.getAllPackages().get(packageKey).size();
			
			Double afferentResult = AfferentCoupling.run(classesAndPackages.getAllPackages().get(packageKey));
			classResultsMap.addResult(branchPackageName, "AfferentCoupling", afferentResult, 0);
			afferentAll.add(afferentResult);
			
			Double efferentResult = EfferentCoupling.run(classesAndPackages.getAllPackages().get(packageKey));
			classResultsMap.addResult(packageKey, "EfferentCoupling", efferentResult, 0);
			efferentAll.add(efferentResult);
			
			Double packageCohesionResult = PackageCohesion.run(classesAndPackages.getAllPackages().get(packageKey));
			classResultsMap.addResult(packageKey, "PackageCohesion", packageCohesionResult, 0);
			packageCohesionAll.add(packageCohesionResult);
			
		}
		
		classesAndPackages.getAllClasses().clear();
		
		applicationLevelResults.addLCOM5Results(LCOM5All);
		applicationLevelResults.addClassCohesionResults(classCohesionAll);
		applicationLevelResults.addSensitiveClassCohesionResults(sensitiveClassCohesionAll);
		applicationLevelResults.addCBOResults(CBOAll);
		applicationLevelResults.addDACResults(DACAll);
		applicationLevelResults.addAfferentResults(afferentAll);
		applicationLevelResults.addEfferentResults(efferentAll);
		applicationLevelResults.addPackageCohesionResults(packageCohesionAll);
		
		ApplicationLevelMetric.run(applicationLevelResults);
		HandleLocalResults.localDirResults(classResultsMap, applicationLevelResults, resultsFilePath);
		
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
