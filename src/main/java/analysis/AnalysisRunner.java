package analysis;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import softwaremetrics.LinesOfCode;
import javaparser.ParseDirectory;
import javaparser.ExtractJavaFiles;
import javaparser.ParseRemoteGitRepo;

import com.github.javaparser.ast.CompilationUnit;


public class AnalysisRunner {

	public static void main(String[] args) throws IOException {
		
		Scanner userInput = new Scanner(new InputStreamReader(System.in));
		
		System.out.println("Please indicate which file type you wish to run "
							+ "metrics on."	);
		System.out.println("Enter 1 for local directory/project, 2 for local GitHub"
							+ "repository or 3 for remote GitHub repository: ");
		int fileType = userInput.nextInt();
		//must call next line as nextInt does not consume \n
		userInput.nextLine();
		
		
		if (fileType == 1) {
			System.out.println("Enter full path to local directory/project: ");
			String localPath = userInput.nextLine();
			File localFile = new File(localPath);
			analyseLocalDirectory(localFile);
		}
		
		
		if (fileType == 3) {
			System.out.println("Enter link to remote GitHub repo: ");
			String repoLink = userInput.nextLine();
			System.out.println("Enter path to empty folder Remote repository can "
								+ "be cloned into in the form \n"
								+ "C:\\\\Users\\\\Chris\\\\Desktop\\\\TestRepo: ");
			String cloneRepoLocation = userInput.nextLine();
			File cloneRepoToFile = new File(cloneRepoLocation);
			analyseRemoteGitRepo(cloneRepoToFile, repoLink);
		}
		//File root = new File("C:\\Users\\Chris\\Downloads\\eclipse-java-kepler-SR1-win32-x86_64\\SoftwareMetricsTool\\src\\main\\java");
		
		
		//File repoFile = new File("C:\\Users\\Chris\\Desktop\\TestRepo");
		
	}
	
	private static void analyseLocalDirectory(File localFile) {
		List<CompilationUnit> cuList = ParseDirectory.parse(localFile);
		
		for (CompilationUnit cu : cuList) {
			int linesOfCode = LinesOfCode.getLinesOfCode(cu);
			
			System.out.println(cu.getParentNode().toString()+ ": " + linesOfCode + " Lines of code.");
		}
	}
	
	private static void analyseRemoteGitRepo(File cloneRepo, String repoLink) throws IOException {
		try {
			ParseRemoteGitRepo remoteGitRepo = new ParseRemoteGitRepo(cloneRepo, repoLink);
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		
		ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(cloneRepo);
		String repoJavaFilesStrings[] = extractJavaFiles.returnJavaClassesStrings();
		File repoJavaFiles[] = extractJavaFiles.returnJavaFiles();
		
		for (int i = 0; i < repoJavaFiles.length; i++) {
			if (repoJavaFiles[i] != null)
				System.out.println(repoJavaFilesStrings[i] + " //// " + repoJavaFiles[i].getAbsolutePath());
		}
	}
}
