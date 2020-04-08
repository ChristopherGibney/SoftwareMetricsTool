package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import parser.ExtractJavaFiles;
import parser.ParseGitRepo;
import parser.RepoAllVersionsOnBranch;
import softwaremetrics.ClassCohesion;
import softwaremetrics.CouplingBetweenObjectClasses;
import softwaremetrics.DataAbstractionCoupling;
import softwaremetrics.LackOfCohesionOfMethodsFive;
import softwaremetrics.SensitiveClassCohesion;
import softwaremetricshelperclasses.ExtractClassesCoupledFromCurrentClass;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.ExtractDependantClasses;
import softwaremetricshelperclasses.InnerClassOfFile;

public class AnalyseGitRepo {

	public static void analyseGitRepo(File repoFile, Git git, String directoriesPath) throws IOException, InvalidRemoteException, GitAPIException {
		ParseGitRepo gitRepo = new ParseGitRepo(repoFile, git, directoriesPath);
		//ArrayList<GitJavaFile> filesWithCommits = gitRepo.getFilesWithCommits();
		ArrayList<RepoAllVersionsOnBranch> repoAllVersionsAllBranches = gitRepo.getRepoAllVersionsAllBranches();
		
		//for each repo version in a branch
		for (RepoAllVersionsOnBranch repoOnBranch : repoAllVersionsAllBranches) {
			String branchName = repoOnBranch.getBranchSimpleName()+"/";
			//for each version of the repo in the branch
			for (File repoDirectory : repoOnBranch.getAllRepoVersionsOnBranch()) {
				System.out.println(repoDirectory.getAbsolutePath());
				ArrayList<InnerClassOfFile> allClasses = new ArrayList<>();
				ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(repoDirectory);
				//ArrayList<File> currentRepoJavaFiles = extractJavaFiles.getJavaFiles();
				//repo.addJavaFiles(currentRepoJavaFiles);
				for (File f : extractJavaFiles.getJavaFiles()) {
					String fileName = f.getName().substring(0, f.getName().indexOf(".")) + "/";
					String packageName = f.getParentFile().getName() + "/";
					ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(f);
					allClasses.addAll(classes);
					for (InnerClassOfFile innerClass : classes) {
						innerClass.addFileName(fileName);
						innerClass.addPackageName(packageName);
						innerClass.addBranchName(branchName);
					}
					ArrayList<Entry<InnerClassOfFile, Double>> lcom5Result = LackOfCohesionOfMethodsFive.run(f);
					ArrayList<Entry<InnerClassOfFile, Double>> classCohesionResult = ClassCohesion.run(f);
					ArrayList<Entry<InnerClassOfFile, Double>> sensitiveClassCohesionResult = SensitiveClassCohesion.run(f);
					
					for (Entry<InnerClassOfFile, Double> lcom5 : lcom5Result) {
						String fileClassName = branchName + packageName + fileName + lcom5.getKey().getClassName();
						repoOnBranch.addResult(fileClassName, "LCOM5", lcom5.getValue());
					}
					for (Entry<InnerClassOfFile, Double> classCohesion : classCohesionResult) {
						String fileClassName = branchName + packageName + fileName + classCohesion.getKey().getClassName();
						repoOnBranch.addResult(fileClassName, "ClassCohesion", classCohesion.getValue());
					}
					for (Entry<InnerClassOfFile, Double> sensitiveClassCohesion : sensitiveClassCohesionResult) {
						String fileClassName = branchName + packageName + fileName + sensitiveClassCohesion.getKey().getClassName();
						repoOnBranch.addResult(fileClassName, "SensitiveClassCohesion", sensitiveClassCohesion.getValue());
					}
					lcom5Result.clear();
					classCohesionResult.clear();
					sensitiveClassCohesionResult.clear();
				}
				for (InnerClassOfFile currentClass : allClasses) {
					ExtractClassesCoupledFromCurrentClass.extract(repoDirectory, currentClass, allClasses, extractJavaFiles.getParentFiles());
				}
				ExtractDependantClasses.extract(allClasses);
				ArrayList<Entry<InnerClassOfFile, Double>> cboResults = CouplingBetweenObjectClasses.run(allClasses);
				for (Entry<InnerClassOfFile, Double> cboResult : cboResults) {
					String fileClassName = branchName + cboResult.getKey().getPackageName() + cboResult.getKey().getFileName() + cboResult.getKey().getClassName();
					repoOnBranch.addResult(fileClassName, "CouplingBetweenObjectClasses", cboResult.getValue());
				}
				for (InnerClassOfFile currentClass : allClasses) {
					double dataAbstractionCouplingResult = DataAbstractionCoupling.run(currentClass, allClasses, extractJavaFiles.getParentFiles());
					String fileClassName = branchName + currentClass.getPackageName() + currentClass.getFileName() + currentClass.getClassName();
					repoOnBranch.addResult(fileClassName, "DataAbstractionCoupling", dataAbstractionCouplingResult);
				}
				allClasses.clear();
				
			}
			for (String classKey : repoOnBranch.getResults().keySet()) {
				for (String metricKey : repoOnBranch.getResults().get(classKey).keySet()) {
					if (metricKey.equals("CouplingBetweenObjectClasses")) {
						System.out.println(classKey + " " + metricKey + repoOnBranch.getResults().get(classKey).get(metricKey).toString()); 
					}
				}
			}
		}
	}

}
