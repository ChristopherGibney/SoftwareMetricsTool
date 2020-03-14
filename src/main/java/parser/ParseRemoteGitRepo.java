package parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import com.google.common.io.Files;

public class ParseRemoteGitRepo {
	
	private ArrayList<GitJavaFile> filesWithCommits = new ArrayList<GitJavaFile>();
	
	public ParseRemoteGitRepo(File repoFile, String repoLink) throws InvalidRemoteException, GitAPIException, IOException {
	
		Git git = Git.cloneRepository()
				.setURI(repoLink)
				.setDirectory(repoFile)
				.setCloneAllBranches(true)
				.call();
		Repository repo = new FileRepository(repoFile);
		RevWalk revWalk = new RevWalk(repo);
		List<Ref> allBranches = git.branchList().call();
		
		ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(repoFile);
		ArrayList<File> repoJavaFiles = extractJavaFiles.returnJavaFiles();
		
		File dirForCopiedFiles = new File("C://Users//Chris//Desktop//TestRepo_1_files");
		dirForCopiedFiles.mkdir();
		
		for (File f : repoJavaFiles) {
			ArrayList<RevCommit> commits = allCommitsForFile(f, git, repoFile.getName());
			filesWithCommits.add(new GitJavaFile(f, commits));
		}
		
		for (GitJavaFile fwc : filesWithCommits) {
			File f = fwc.getRepoFile();
			int fileCounter = 0;
			String filePath = getRepoRelativePath(f, repoFile.getName());
			ArrayList<RevCommit> commits = fwc.getCommits();
			System.out.println("**************** " + f.getAbsolutePath().replace("\\", "/"));
			
			for (RevCommit c : commits) {
				//ExtractJavaFiles ejf = new ExtractJavaFiles(repo.getWorkTree());
				git.checkout().setName(c.getName()).call();
				Repository currentRepo = git.checkout().setName(c.getName()).getRepository();
				
				File checkedOutFile = new File(currentRepo.getWorkTree(), filePath);
				String newFilePath = "C://Users//Chris//Desktop//TestRepo_1_files//"
						+ f.getName().substring(0, f.getName().indexOf("."))
											+ String.valueOf(fileCounter) + ".java";
				fileCounter++;
				File destination = new File(newFilePath);
				destination.createNewFile();
				Files.copy(checkedOutFile, destination);
				fwc.addVersionOfFile(destination);
				//System.out.println(f.getAbsolutePath() + "----" + c.getFullMessage() + c.getCommitTime());
			}
		}
		
		//for (Ref branch : allBranches) {
			//Iterable<RevCommit> commits = git.log().all().call();
			
			//for (RevCommit commit : commits) {
				//RevTree commitTreeId = commit.getTree();
				
				//try (TreeWalk treeWalk = new TreeWalk(commitTreeId)) {
					//treeWalk.reset
				//}
			//}
		//}
		//"https://github.com/ChristopherGibney/SoftwareMetricsTool"
	}
	
	//returns commits for a file as an arraylist, order is most recent->least recent
	private static ArrayList<RevCommit> allCommitsForFile(File file, Git git, String repoName) throws NoHeadException, GitAPIException {
		ArrayList<RevCommit> commits = new ArrayList<RevCommit>();
		String path = getRepoRelativePath(file, repoName);
		Iterable<RevCommit> commitLog = git.log().addPath(path).call();
		
		for (RevCommit commit : commitLog) {
			commits.add(commit);
		}
		return commits;
	}
	
	//need repo relative path for file to access logs
	private static String getRepoRelativePath(File f, String repoName) {
		String path = f.getAbsolutePath().replace("\\", "/");
		int index1 = path.indexOf(repoName) + repoName.length() + 1;
		int index2 = path.length();
		path = path.substring(index1, index2);
		return path;
	}
	
	public ArrayList<GitJavaFile> getFilesWithCommits() {
		return filesWithCommits;
	}
}
