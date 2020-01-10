package softwaremetrics;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import javaparser.ParseDirectory;
import javaparser.ParseGitRepo;

import com.github.javaparser.ast.CompilationUnit;


public class AnalysisRunner {

	public static void main(String[] args) {
		File root = new File("C:\\Users\\Chris\\Downloads\\eclipse-java-kepler-SR1-win32-x86_64\\SoftwareMetricsTool\\src\\main\\java");

		List<CompilationUnit> cuList = ParseDirectory.parse(root);
		
		for (CompilationUnit cu : cuList) {
			int linesOfCode = LinesOfCode.getLinesOfCode(cu);
			
			System.out.println(cu.getParentNode().toString()+ ": " + linesOfCode + " Lines of code.");
		}
		
		
		File repoFile = new File("C:\\Users\\Chris\\Desktop\\TestRepo");
		try {
			ParseGitRepo remoteGitRepo = new ParseGitRepo(repoFile);
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}
	

}
