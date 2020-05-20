package analysis;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

public class UserInput {
	
	public boolean localDir = false, remoteGitRepo = false, localGitRepo = false;
	public String repoLink = "", directoriesPath;
	public File rootFile;

	public UserInput() throws IOException, InvalidRemoteException, GitAPIException {
		
		Scanner userInput = new Scanner(new InputStreamReader(System.in));
		
		System.out.println("Please indicate which input type you wish to run "
				+ "metrics analysis on."	);
		System.out.println("Enter 1 for local directory/project, 2 for local GitHub"
				+ " repository or 3 for remote GitHub repository: ");
		int fileType = userInput.nextInt();
		//must call next line as nextInt does not consume \n
		userInput.nextLine();
		
		if (fileType == 1) {
			localDir = true;
			System.out.println("\nEnter full path to local directory/project in the format: \n"
					+ "C://Users//Chris//Desktop//LocalProject ");
			String localPath = userInput.nextLine();
			rootFile = new File(localPath);
			System.out.println("\nEnter path to folder where two results files can be created "
					+ "for example:\nC:\\\\Users\\\\Chris\\\\Desktop will create two results text files on Desktop.");
			directoriesPath = userInput.nextLine();
		}
		else if (fileType == 2) {
			localGitRepo = true;
			System.out.println("\nEnter path to folder containing repo in the format:\n"
					+ "C:\\\\Users\\\\Chris\\\\Desktop\\\\MyRepo");
			String repoPath = userInput.nextLine();
			rootFile = new File(repoPath);
			System.out.println("\nEnter path to folder where necessary temp directories can be created "
					+ "for example: \n"
					+ "C:\\\\Users\\\\Chris\\\\Desktop: ");
			directoriesPath = userInput.nextLine();
		}
		else if (fileType == 3) {
			remoteGitRepo = true;
			System.out.println("\nEnter link to remote GitHub repo for example: ");
			System.out.println("https://github.com/ChristopherGibney/SoftwareMetricsTool");
			repoLink = userInput.nextLine();
			System.out.println("\nEnter path to folder where necessary temp directories can be created "
					+ "for example: \n"
					+ "C:\\\\Users\\\\Chris\\\\Desktop ");
			directoriesPath = userInput.nextLine();
			String cloneRepoLocation = directoriesPath + "\\SoftwareMetricsToolRepo";
			rootFile = new File(cloneRepoLocation);
		}
		
		userInput.close();
	}
}
