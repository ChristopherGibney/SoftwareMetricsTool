package analysis;

import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import parser.GitJavaFile;
import parser.ParseDirectory;
import softwaremetrics.LinesOfCode;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class AnalysisRunner {

	public static void main(String[] args) throws IOException, InvalidRemoteException, GitAPIException {
		UserInput userInput = new UserInput();
		
		if (userInput.localDir) {
			AnalyseLocalDirectory.analyseLocalDirectory(userInput.rootFile, userInput.directoriesPath);
		}
		if (userInput.localGitRepo) {
			Git git = Git.open(userInput.rootFile);
			AnalyseGitRepo.analyseGitRepo(userInput.rootFile, git, userInput.directoriesPath);
		}
		if (userInput.remoteGitRepo) {
			Git git = Git.cloneRepository()
					.setURI(userInput.repoLink)
					.setDirectory(userInput.rootFile)
					.setCloneAllBranches(true)
					.call();
			AnalyseGitRepo.analyseGitRepo(userInput.rootFile, git, userInput.directoriesPath);
		}
	}
	
}
