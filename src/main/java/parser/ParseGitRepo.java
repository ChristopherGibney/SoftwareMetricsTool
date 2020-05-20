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
		git.getRepository().close();
	}
	
	public static void clearTempDirs(String dirsPath, boolean remote) {
		File dirForCopiedRepos = new File(dirsPath + "//SoftwareMetricsToolRepoVersions");
		dirForCopiedRepos.mkdirs();
		try {
			FileUtils.deleteDirectory(dirForCopiedRepos);
		} catch (IOException e) {
			System.out.println("Error when deleting temp directory " + dirsPath + "\\\\SoftwareMetricsToolRepoVersions");
		}
		//only want to delete repo local file if it is a remote cloned to local file created by the tool
		if (remote) {
			File dirForClonedRepo = new File(dirsPath + "\\SoftwareMetricsToolRepo");
			dirForClonedRepo.mkdirs();
			try {
				FileUtils.forceDelete(dirForClonedRepo);
			} catch (IOException e) {
				System.out.println("Error when deleting temp directory " + dirsPath + "\\\\SoftwareMetricsToolRepo");
			}
		}
	}
	
	public ArrayList<GitJavaFile> getFilesWithCommits() {
		return filesWithCommits;
	}
	public ArrayList<RepoAllVersionsOnBranch> getRepoAllVersionsAllBranches() {
		return repoAllVersionsAllBranches;
	}
}
