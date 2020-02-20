package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javaparser.GitJavaFile;
import javaparser.ParseDirectory;
import javaparser.ParseRemoteGitRepo;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import softwaremetrics.LinesOfCode;

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
			analyseRemoteGitRepo(userInput.rootFile, userInput.repoLink);
		}
	}
	
	private static void analyseLocalDirectory(File localFile) {
		List<CompilationUnit> cuList = ParseDirectory.parse(localFile);
		
		for (CompilationUnit cu : cuList) {
			int linesOfCode = LinesOfCode.getLinesOfCode(cu);
			
			System.out.println(cu.getParentNode().toString()+ ": " + linesOfCode + " Lines of code.");
		}
	}
	
	private static void analyseRemoteGitRepo(File cloneRepo, String repoLink) throws IOException, InvalidRemoteException, GitAPIException {
		ParseRemoteGitRepo remoteGitRepo = new ParseRemoteGitRepo(cloneRepo, repoLink);
		ArrayList<GitJavaFile> filesWithCommits = remoteGitRepo.getFilesWithCommits();
		//ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(cloneRepo);
		
		//String repoJavaFilesStrings[] = extractJavaFiles.returnJavaClassesStrings();
		//ArrayList<File> repoJavaFiles = extractJavaFiles.returnJavaFiles();
		
		//for (int i = 0; i < repoJavaFiles.length; i++) {
			//if (repoJavaFiles[i] != null) {
				//File oldVersions[] = remoteGitRepo.returnAllVersions(repoJavaFiles[i]);
			//}
		//}
	}
}
