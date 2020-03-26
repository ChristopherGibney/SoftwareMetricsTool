package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import parser.ExtractJavaFiles;
import parser.GitJavaFile;
import parser.ParseDirectory;
import parser.ParseGitRepo;
import parser.RepoAllVersions;
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
		ArrayList<File> localJavaFiles = extractJavaFiles.returnJavaFiles();
		
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
		ParseGitRepo remoteGitRepo = new ParseGitRepo(repoFile, git, directoriesPath);
		ArrayList<GitJavaFile> filesWithCommits = remoteGitRepo.getFilesWithCommits();
		ArrayList<RepoAllVersions> repoAllVersions = remoteGitRepo.getRepoAllVersions();
		
		//for each repo in a branch
		for (RepoAllVersions repo : repoAllVersions) {
			//for each version of the repo in the branch
			for (File repoDirectory : repo.getAllRepoVersions()) {
				ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(repoDirectory);
				ArrayList<File> currentRepoJavaFiles = extractJavaFiles.returnJavaFiles();
				repo.addJavaFiles(currentRepoJavaFiles);
				for (File f : currentRepoJavaFiles) {
					ArrayList<Entry<InnerClassOfFile, Double>> lcom5Result = LackOfCohesionOfMethodsFive.run(f);
					ArrayList<Entry<InnerClassOfFile, Double>> classCohesionResult = ClassCohesion.run(f);
					ArrayList<Entry<InnerClassOfFile, Double>> sensitiveClassCohesionResult = SensitiveClassCohesion.run(f);
				}
			}
		}
	}
	
}
