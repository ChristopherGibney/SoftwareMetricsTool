package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import parser.ExtractJavaFiles;
import parser.GitJavaFile;
import parser.ParseDirectory;
import parser.ParseGitRepo;
import parser.RepoAllVersionsOnBranch;
import results.ClassResultsMap;
import softwaremetrics.ClassCohesion;
import softwaremetrics.CouplingBetweenObjectClasses;
import softwaremetrics.LackOfCohesionOfMethodsFive;
import softwaremetrics.LinesOfCode;
import softwaremetrics.SensitiveClassCohesion;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class AnalysisRunner {

	public static void main(String[] args) throws IOException, InvalidRemoteException, GitAPIException {
		UserInput userInput = new UserInput();
		
		if (userInput.localDir) {
			analyseLocalDirectory(userInput.rootFile, userInput.directoriesPath);
		}
		if (userInput.localGitRepo) {
			Git git = Git.open(userInput.rootFile);
			analyseGitRepo(userInput.rootFile, git, userInput.directoriesPath);
		}
		if (userInput.remoteGitRepo) {
			Git git = Git.cloneRepository()
					.setURI(userInput.repoLink)
					.setDirectory(userInput.rootFile)
					.setCloneAllBranches(true)
					.call();
			analyseGitRepo(userInput.rootFile, git, userInput.directoriesPath);
		}
	}
	
	private static void analyseLocalDirectory(File localDir, String resultsFilePath) throws IOException {
		ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(localDir);
		ArrayList<File> localJavaFiles = extractJavaFiles.getJavaFiles();
		ClassResultsMap classResultsMap = new ClassResultsMap();
		
		ArrayList<InnerClassOfFile> allClasses = new ArrayList<>();
		
		for (File f : localJavaFiles) {
			String fileName = f.getName().substring(0, f.getName().indexOf(".")) + "/";
			ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(f);
			allClasses.addAll(classes);
			for (InnerClassOfFile innerClass : classes) {
				innerClass.addFileName(fileName);
			}
			
			ArrayList<Entry<InnerClassOfFile, Double>> lcom5Result = LackOfCohesionOfMethodsFive.run(f);
			ArrayList<Entry<InnerClassOfFile, Double>> classCohesionResult = ClassCohesion.run(f);
			ArrayList<Entry<InnerClassOfFile, Double>> sensitiveClassCohesionResult = SensitiveClassCohesion.run(f);
			for (Entry<InnerClassOfFile, Double> lcom5 : lcom5Result) {
				String fileClassName = fileName + lcom5.getKey().getClassName();
				classResultsMap.addResult(fileClassName, "LCOM5", lcom5.getValue());
			}
			for (Entry<InnerClassOfFile, Double> classCohesion : classCohesionResult) {
				String fileClassName = fileName + classCohesion.getKey().getClassName();
				classResultsMap.addResult(fileClassName, "ClassCohesion", classCohesion.getValue());
			}
			for (Entry<InnerClassOfFile, Double> sensitiveClassCohesion : sensitiveClassCohesionResult) {
				String fileClassName = fileName + sensitiveClassCohesion.getKey().getClassName();
				classResultsMap.addResult(fileClassName, "SensitiveClassCohesion", sensitiveClassCohesion.getValue());
			}
		}
		CouplingBetweenObjectClasses.run(localDir, allClasses, extractJavaFiles.getParentFiles());
		for (InnerClassOfFile innerClass : allClasses) {
			String fileClassName = innerClass.getFileName() + innerClass.getClassName();
			System.out.println(localDir.toString());
			classResultsMap.addResult(fileClassName, "CouplingBetweenObjectClasses", (double) innerClass.getCoupledObjectClasses().size());
		}
		
		Map<String, Map<String, List<Double>>> classResults = classResultsMap.getResults();
		for (String classKey : classResults.keySet()) {
			Map<String, List<Double>> metricResults = classResults.get(classKey);
			for (String metricKey : metricResults.keySet()) {
				System.out.println(classKey + " " + metricKey + metricResults.get(metricKey).toString()); 
			}
		}
	}
	
	private static void analyseGitRepo(File repoFile, Git git, String directoriesPath) throws IOException, InvalidRemoteException, GitAPIException {
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
					ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(f);
					allClasses.addAll(classes);
					for (InnerClassOfFile innerClass : classes) {
						innerClass.addFileName(fileName);
					}
					ArrayList<Entry<InnerClassOfFile, Double>> lcom5Result = LackOfCohesionOfMethodsFive.run(f);
					ArrayList<Entry<InnerClassOfFile, Double>> classCohesionResult = ClassCohesion.run(f);
					ArrayList<Entry<InnerClassOfFile, Double>> sensitiveClassCohesionResult = SensitiveClassCohesion.run(f);
					
					for (Entry<InnerClassOfFile, Double> lcom5 : lcom5Result) {
						String fileClassName = branchName + fileName + lcom5.getKey().getClassName();
						repoOnBranch.addResult(fileClassName, "LCOM5", lcom5.getValue());
					}
					for (Entry<InnerClassOfFile, Double> classCohesion : classCohesionResult) {
						String fileClassName = branchName + fileName + classCohesion.getKey().getClassName();
						repoOnBranch.addResult(fileClassName, "ClassCohesion", classCohesion.getValue());
					}
					for (Entry<InnerClassOfFile, Double> sensitiveClassCohesion : sensitiveClassCohesionResult) {
						String fileClassName = branchName + fileName + sensitiveClassCohesion.getKey().getClassName();
						repoOnBranch.addResult(fileClassName, "SensitiveClassCohesion", sensitiveClassCohesion.getValue());
					}
					lcom5Result.clear();
					classCohesionResult.clear();
					sensitiveClassCohesionResult.clear();
				}
				CouplingBetweenObjectClasses.run(repoDirectory, allClasses, extractJavaFiles.getParentFiles());
				for (InnerClassOfFile innerClass : allClasses) {
					String fileClassName = branchName + innerClass.getFileName() + innerClass.getClassName();
					repoOnBranch.addResult(fileClassName, "CouplingBetweenObjectClasses", (double) innerClass.getTotalCoupledObjectClasses());
				}
				allClasses.clear();
			}
			for (String classKey : repoOnBranch.getResults().keySet()) {
				for (String metricKey : repoOnBranch.getResults().get(classKey).keySet()) {
					if (metricKey.equals("CouplingBetweenObjectClasses")) {
						//System.out.println(classKey + " " + metricKey + repoOnBranch.getResults().get(classKey).get(metricKey).toString()); 
					}
				}
			}
		}
	}
	
}
