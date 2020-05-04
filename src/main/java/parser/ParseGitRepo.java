package parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

public class ParseGitRepo {
	
	private ArrayList<GitJavaFile> filesWithCommits = new ArrayList<GitJavaFile>();
	private ArrayList<RepoAllVersionsOnBranch> repoAllVersionsAllBranches = new ArrayList<RepoAllVersionsOnBranch>();
	
	public ParseGitRepo(File repoFile, Git git, String directoriesPath, boolean remote) throws InvalidRemoteException, GitAPIException, IOException {
		File dirForCopiedRepos = new File(directoriesPath + "//SoftwareMetricsToolRepoVersions");
		if (!dirForCopiedRepos.mkdirs()) {
			System.out.println("Please delete SoftwareMetricsToolRepoVersions directory");
		}
		ArrayList<Ref> branches = new ArrayList<>();
		if (remote) {
			branches.addAll(git.branchList().setListMode(ListMode.REMOTE).call());
		}
		else {
			branches.addAll(git.branchList().call());
		}
		for (Ref branch : branches) {
			System.out.println(branch.getName());
		}
		for (Ref branch : branches) {
			System.out.println(branch.getName());
			int repoCounter = 0;
			Iterable<RevCommit> repoAllCommits = git.log().add(git.getRepository().resolve(branch.getName())).call();
			RepoAllVersionsOnBranch currentBranchAllVersions = new RepoAllVersionsOnBranch(git.getRepository(), branch, repoAllCommits);
			
			for (Iterator<RevCommit> i = repoAllCommits.iterator(); i.hasNext(); ) {
				RevCommit commit = i.next();
				git.checkout().setName(commit.getName()).call();
				String repoName = directoriesPath + "//SoftwareMetricsToolRepoVersions//Repo" + currentBranchAllVersions.getBranchSimpleName() + String.valueOf(repoCounter);
				File oldRepoVersion = new File(repoName);
				if (!oldRepoVersion.mkdirs()) {
					System.out.println("Please delete SoftwareMetricsToolRepoVersions directory");
				}
				FileUtils.copyDirectory(repoFile, oldRepoVersion);
				currentBranchAllVersions.addVersionOfRepo(oldRepoVersion);
				repoCounter++;
			}
			repoAllVersionsAllBranches.add(currentBranchAllVersions);
		}
		
	}
	
	//returns commits for a file as an array list, order is most recent->least recent
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
