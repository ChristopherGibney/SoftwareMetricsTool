package javaparser;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class ParseRemoteGitRepo {
	
	public ParseRemoteGitRepo(File repoFile, String repoLink) throws InvalidRemoteException, GitAPIException {
	
	
		Git git = Git.cloneRepository()
				.setURI(repoLink)
				.setDirectory(repoFile)
				.setCloneAllBranches(true)
				.call();
		//"https://github.com/ChristopherGibney/SoftwareMetricsTool"
	}
}
