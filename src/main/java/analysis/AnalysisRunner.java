package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import parser.ExtractJavaFiles;
import parser.GitJavaFile;
import parser.ParseDirectory;
import parser.ParseRemoteGitRepo;
import parser.RepoAllVersions;
import softwaremetrics.ClassCohesion;
import softwaremetrics.LackOfCohesionOfMethodsFive;
import softwaremetrics.LinesOfCode;
import softwaremetricshelperclasses.InnerClassOfFile;

import com.github.javaparser.ast.CompilationUnit;

public class AnalysisRunner {

	public static void main(String[] args) throws IOException, InvalidRemoteException, GitAPIException {
		UserInput userInput = new UserInput();
		
		if (userInput.localDir) {
			analyseLocalDirectory(userInput.rootFile);
		}
		if (userInput.localGitRepo) {
			
		}
		if (userInput.remoteGitRepo) {
			analyseRemoteGitRepo(userInput.rootFile, userInput.repoLink, userInput.directoriesPath);
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
		}
	}
	
	private static void analyseRemoteGitRepo(File cloneRepo, String repoLink, String directoriesPath) throws IOException, InvalidRemoteException, GitAPIException {
		ParseRemoteGitRepo remoteGitRepo = new ParseRemoteGitRepo(cloneRepo, repoLink, directoriesPath);
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
				}
			}
		}
	}
	
}
