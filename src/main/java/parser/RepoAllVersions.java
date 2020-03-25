package parser;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class RepoAllVersions {
	private Repository repo;
	private Ref branch;
	//commits are always in order of most recent first
	private Iterable<RevCommit> commits = new ArrayList<RevCommit>();
	private ArrayList<File> repoAllVersions = new ArrayList<File>();
	private ArrayList<ArrayList<File>> javaFiles = new ArrayList<ArrayList<File>>();
	
	public RepoAllVersions(Repository r, Ref branch, Iterable<RevCommit> commits) {
		this.repo = r;
		this.branch = branch;
		this.commits = commits;
	}
	public void addVersionOfRepo(File f) {
		this.repoAllVersions.add(f);
	}
	public void addJavaFiles(ArrayList<File> files){
		javaFiles.add(files);
	}
	public ArrayList<File> getAllRepoVersions() {
		return this.repoAllVersions;
	}
	public Iterable<RevCommit> getCommits() {
		return this.commits;
	}
	public Repository getRepo() {
		return this.repo;
	}
}
