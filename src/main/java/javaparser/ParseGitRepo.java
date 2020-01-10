package javaparser;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class ParseGitRepo {
	
	public ParseGitRepo(File repoFile) throws InvalidRemoteException, GitAPIException {
	
		//C:\\Users\\Chris\\Desktop\\TestRepo
	
		Git git = Git.cloneRepository()
				.setURI("https://github.com/ChristopherGibney/SoftwareMEtricsTool")
				.setDirectory(repoFile)
				.setCloneAllBranches(true)
				.call();
		
	
		
		
	}
}
