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
import softwaremetrics.ClassCohesion;
import softwaremetrics.LackOfCohesionOfMethodsFive;
import softwaremetrics.LinesOfCode;
import softwaremetrics.SensitiveClassCohesion;
import softwaremetricshelperclasses.InnerClassOfFile;

import com.github.javaparser.ast.CompilationUnit;

public class AnalysisRunner {

	public static void main(String[] args) throws IOException, InvalidRemoteException, GitAPIException {
		UserInput userInput = new UserInput();
		
		if (userInput.localDir) {
			analyseLocalDirectory(userInput.rootFile);
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
	
	private static void analyseLocalDirectory(File localFile) throws IOException {
		ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(localFile);
		ArrayList<File> localJavaFiles = extractJavaFiles.getJavaFiles();
		
		for (File f: localJavaFiles) {
			System.out.println("java file: " + f.getAbsolutePath());
		}
		List<CompilationUnit> cuList = ParseDirectory.parse(localFile);
		
		for (CompilationUnit cu : cuList) {
			int linesOfCode = LinesOfCode.getLinesOfCode(cu);
			
			//System.out.println(cu.getParentNode().toString()+ ": " + linesOfCode + " Lines of code.");
		}
		
		for (File f : localJavaFiles) {
			ArrayList<Entry<InnerClassOfFile, Double>> lcom5Result = LackOfCohesionOfMethodsFive.run(f);
			ArrayList<Entry<InnerClassOfFile, Double>> classCohesionResult = ClassCohesion.run(f);
			ArrayList<Entry<InnerClassOfFile, Double>> sensitiveClassCohesionResult = SensitiveClassCohesion.run(f);
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
				ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(repoDirectory);
				//ArrayList<File> currentRepoJavaFiles = extractJavaFiles.getJavaFiles();
				//repo.addJavaFiles(currentRepoJavaFiles);
				for (File f : extractJavaFiles.getJavaFiles()) {
					String fileName = f.getName().substring(0, f.getName().indexOf(".")) + "/";
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
				}
			}
			Map<String, Map<String, List<Double>>> classResults = repoOnBranch.getResults();
			for (String classKey : classResults.keySet()) {
				Map<String, List<Double>> metricResults = classResults.get(classKey);
				for (String metricKey : metricResults.keySet()) {
					System.out.println(classKey + " " + metricKey + metricResults.get(metricKey).toString()); 
				}
			}
		}
	}
	
}
