package parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

public class ParseGitRepo {
	
	private ArrayList<GitJavaFile> filesWithCommits = new ArrayList<GitJavaFile>();
	private ArrayList<RepoAllVersionsOnBranch> repoAllVersionsAllBranches = new ArrayList<RepoAllVersionsOnBranch>();
	
	public ParseGitRepo(File repoFile, Git git, String directoriesPath) throws InvalidRemoteException, GitAPIException, IOException {
		/*ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(repoFile);
		ArrayList<File> repoJavaFiles = extractJavaFiles.getJavaFiles();
		
		File dirForCopiedFiles = new File(directoriesPath + "//SoftwareMetricsToolFileVersions");
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
			//System.out.println("**************** " + f.getAbsolutePath().replace("\\", "/"));
			
			for (RevCommit c : commits) {
				//ExtractJavaFiles ejf = new ExtractJavaFiles(repo.getWorkTree());
				git.checkout().setName(c.getName()).call();
				Repository currentRepo = git.checkout().setName(c.getName()).getRepository();
				
				File checkedOutFile = new File(currentRepo.getWorkTree(), filePath);
				String newFilePath = directoriesPath + "//SoftwareMetricsToolFileVersions//"
						+ f.getName().substring(0, f.getName().indexOf("."))
											+ String.valueOf(fileCounter) + ".java";
				fileCounter++;
				File destination = new File(newFilePath);
				destination.createNewFile();
				Files.copy(checkedOutFile, destination);
				fwc.addVersionOfFile(destination);
				//System.out.println(f.getAbsolutePath() + "----" + c.getFullMessage() + c.getCommitTime());
			}
		}*/
		
		for (Ref branch : git.branchList().call()) {
			int repoCounter = 0;
			File dirForCopiedRepos = new File(directoriesPath + "//SoftwareMetricsToolRepoVersions");
			dirForCopiedRepos.mkdir();
			Iterable<RevCommit> repoAllCommits = git.log().add(git.getRepository().resolve(branch.getName())).call();
			RepoAllVersionsOnBranch currentBranchAllVersions = new RepoAllVersionsOnBranch(git.getRepository(), branch, repoAllCommits);
			
			for (Iterator<RevCommit> i = repoAllCommits.iterator(); i.hasNext(); ) {
				RevCommit commit = i.next();
				git.checkout().setName(commit.getName()).call();
				String repoName = directoriesPath + "//SoftwareMetricsToolRepoVersions//Repo" + currentBranchAllVersions.getBranchSimpleName() + String.valueOf(repoCounter);
				File oldRepoVersion = new File(repoName);
				oldRepoVersion.mkdir();
				FileUtils.copyDirectory(repoFile, oldRepoVersion);
				currentBranchAllVersions.addVersionOfRepo(oldRepoVersion);
				repoCounter++;
			}
			repoAllVersionsAllBranches.add(currentBranchAllVersions);
		}
		
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
	public ArrayList<RepoAllVersionsOnBranch> getRepoAllVersionsAllBranches() {
		return repoAllVersionsAllBranches;
	}
}
