package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
				Map<String, ArrayList<InnerClassOfFile>> packages = new HashMap<>();
				ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(repoDirectory);
				//ArrayList<File> currentRepoJavaFiles = extractJavaFiles.getJavaFiles();
				//repo.addJavaFiles(currentRepoJavaFiles);
				for (File f : extractJavaFiles.getJavaFiles()) {
					String fileName = f.getName().substring(0, f.getName().indexOf(".")) + "/";
					String packageName = f.getParentFile().getName() + "/";
					ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(f);
					allClasses.addAll(classes);
					if (packages.containsKey(f.getParentFile().toString())) {
						packages.get(f.getParentFile().toString()).addAll(classes);
					}
					else {
						String currentPackageName = f.getParentFile().toString();
						packages.put(currentPackageName, classes);
					}
					for (InnerClassOfFile innerClass : classes) {
						innerClass.addFileName(fileName);
						innerClass.addPackageName(packageName);
						innerClass.addBranchName(branchName);
					}	
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
					String fileClassName = currentClass.getBranchName() + currentClass.getPackageName() + 
							currentClass.getFileName() + currentClass.getClassName();
					
					repoOnBranch.addResult(fileClassName, "SensitiveClassCohesion", SensitiveClassCohesion.run(currentClass));
					repoOnBranch.addResult(fileClassName, "LCOM5", LackOfCohesionOfMethodsFive.run(currentClass));
					repoOnBranch.addResult(fileClassName, "ClassCohesion", ClassCohesion.run(currentClass));
					repoOnBranch.addResult(fileClassName, "DataAbstractionCoupling", DataAbstractionCoupling.run(currentClass, allClasses, extractJavaFiles.getParentFiles()));
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
