package javaparser;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jgit.revwalk.RevCommit;

public class GitJavaFile {
	private File repoFile;
	//commits are always in order of most recent first
	private ArrayList<RevCommit> commits = new ArrayList<RevCommit>();
	private ArrayList<File> fileAllVersions = new ArrayList<File>();
	
	public GitJavaFile(File f, ArrayList<RevCommit> commits) {
		this.repoFile = f;
		this.commits = commits;
	}
	
	public void addVersionOfFile(File f) {
		this.fileAllVersions.add(f);
	}
	
	public ArrayList<RevCommit> getCommits() {
		return this.commits;
	}
	
	public File getRepoFile() {
		return this.repoFile;
	}
}
