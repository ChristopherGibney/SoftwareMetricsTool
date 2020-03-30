package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class RepoAllVersionsOnBranch {
	private Repository repo;
	private Ref branch;
	private String branchSimpleName;
	//commits are always in order of most recent first
	private Iterable<RevCommit> commits = new ArrayList<RevCommit>();
	private ArrayList<File> repoAllVersions = new ArrayList<File>();
	private ArrayList<ArrayList<File>> javaFiles = new ArrayList<ArrayList<File>>();
	private Map<String, Map<String, List<Double>>> allClassResults = new HashMap<>();
	
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
	public void addResult(String fileClassName, String metric, Double result) {
		if (allClassResults.containsKey(fileClassName) && !result.isNaN()) {
			if (allClassResults.get(fileClassName).containsKey(metric)) {
				allClassResults.get(fileClassName).get(metric).add(result);
			}
			else {
				List<Double> results = new ArrayList<>();
				results.add(result);
				allClassResults.get(fileClassName).put(metric, results);
			}
		}
		else if (!result.isNaN()) {
			List<Double> results = new ArrayList<>();
			results.add(result);
			Map<String, List<Double>> metricResults = new HashMap<>();
			metricResults.put(metric, results);
			allClassResults.put(fileClassName, metricResults);
		}	
	}
	public Map<String, Map<String, List<Double>>> getResults() {
		return allClassResults;
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
