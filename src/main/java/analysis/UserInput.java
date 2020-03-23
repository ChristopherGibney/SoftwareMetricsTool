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
		
		System.out.println("Please indicate which file type you wish to run "
							+ "metrics on."	);
		System.out.println("Enter 1 for local directory/project, 2 for local GitHub"
							+ " repository or 3 for remote GitHub repository: ");
		int fileType = userInput.nextInt();
		//must call next line as nextInt does not consume \n
		userInput.nextLine();
		
		if (fileType == 1) {
			localDir = true;
			System.out.println("Enter full path to local directory/project in the format: \n"
								+ "C://Users//Chris//Desktop//LocalFile: ");
			String localPath = userInput.nextLine();
			rootFile = new File(localPath);
		}
		
		if (fileType == 3) {
			remoteGitRepo = true;
			System.out.println("Enter link to remote GitHub repo: ");
			repoLink = userInput.nextLine();
			//repoLink = "https://github.com/ChristopherGibney/SoftwareMetricsTool";
			System.out.println("Enter path to folder where necessary temp directories can be created "
								+ "for example: \n"
								+ "C:\\\\Users\\\\Chris\\\\Desktop: ");
			directoriesPath = userInput.nextLine();
			String cloneRepoLocation = directoriesPath + "\\SoftwareMetricsToolRepo";
			//String cloneRepoLocation = "C:\\Users\\Chris\\Desktop\\TestRepo_1";
			rootFile = new File(cloneRepoLocation);
		}
		
		userInput.close();
		//File root = new File("C:\\Users\\Chris\\Downloads\\eclipse-java-kepler-SR1-win32-x86_64\\SoftwareMetricsTool\\src\\main\\java");
		//File repoFile = new File("C:\\Users\\Chris\\Desktop\\TestRepo");
	}
}
