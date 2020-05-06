package parser;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import results.ClassResultsMap;

public class RepoAllVersionsOnBranch {
	private Repository repo;
	private Ref branch;
	private String branchSimpleName;
	//commits are always in order of most recent first
	private Iterable<RevCommit> commits = new ArrayList<RevCommit>();
	private ArrayList<File> repoAllVersions = new ArrayList<File>();
	private ArrayList<ArrayList<File>> javaFiles = new ArrayList<ArrayList<File>>();
	private ClassResultsMap classResultsMap = new ClassResultsMap();
	
	public RepoAllVersionsOnBranch(Repository r, Ref branch, Iterable<RevCommit> commits) {
		this.repo = r;
		this.branch = branch;
		String branchArrayName[] = branch.getName().split("/");
		this.branchSimpleName = branchArrayName[branchArrayName.length-1];
		this.commits = commits;
	}
	public void addVersionOfRepo(File f) {
		this.repoAllVersions.add(f);
	}
	public void addJavaFiles(ArrayList<File> files){
		javaFiles.add(files);
	}
	public void addResult(String fileClassName, String metric, Double result, Integer repoVersion) {
		classResultsMap.addResult(fileClassName, metric, result, repoVersion);
	}
	public ClassResultsMap getResults() {
		return classResultsMap;
	}
	public ArrayList<File> getAllRepoVersionsOnBranch() {
		return this.repoAllVersions;
	}
	public Iterable<RevCommit> getCommits() {
		return this.commits;
	}
	public Repository getRepo() {
		return this.repo;
	}
	public Ref getBranch() {
		return branch;
	}
	public String getBranchSimpleName() {
		return branchSimpleName;
	}
}
