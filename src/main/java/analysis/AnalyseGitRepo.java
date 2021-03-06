package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import parser.ExtractClassesAndPackages;
import parser.ExtractJavaFiles;
import parser.ParseGitRepo;
import parser.RepoAllVersionsOnBranch;
import results.ApplicationLevelResults;
import results.HandleGitResults;
import softwaremetrics.AfferentCoupling;
import softwaremetrics.ApplicationLevelMetric;
import softwaremetrics.PackageCohesion;
import softwaremetrics.ClassCohesion;
import softwaremetrics.CouplingBetweenObjectClasses;
import softwaremetrics.DataAbstractionCoupling;
import softwaremetrics.EfferentCoupling;
import softwaremetrics.LackOfCohesionOfMethodsFive;
import softwaremetrics.SensitiveClassCohesion;
import softwaremetricshelperclasses.ExtractClassesCoupledFromCurrentClass;
import softwaremetricshelperclasses.ExtractDependantClasses;
import softwaremetricshelperclasses.InnerClassOfFile;

public class AnalyseGitRepo {

	public static void analyseGitRepo(File repoFile, Git git, String directoriesPath, boolean remote) throws IOException, InvalidRemoteException, GitAPIException {
		ParseGitRepo gitRepo = new ParseGitRepo(repoFile, git, directoriesPath, remote);
		ArrayList<RepoAllVersionsOnBranch> repoAllVersionsAllBranches = gitRepo.getRepoAllVersionsAllBranches();
		String resultsDirPath = directoriesPath + "//SoftwareMetricsToolResults";
		
		//for each  branch
		for (RepoAllVersionsOnBranch branch : repoAllVersionsAllBranches) {
			//combination of all classes on this branch over the evolution of the branch
			ApplicationLevelResults applicationLevelResults = new ApplicationLevelResults();
			String branchName = branch.getBranchSimpleName()+"/";
			//used to track results
			int repoVersion = branch.getAllRepoVersionsOnBranch().size()-1;
			System.out.println("\n");
			
			//for each version of the repo on the branch
			for (File repoVersionDirectory : branch.getAllRepoVersionsOnBranch()) {
				List<Double> LCOM5All = new ArrayList<>(), classCohesionAll= new ArrayList<>(), sensitiveClassCohesionAll = new ArrayList<>(), 
						DACAll = new ArrayList<>(), CBOAll = new ArrayList<>(), afferentAll = new ArrayList<>(), efferentAll = new ArrayList<>(),
						packageCohesionAll = new ArrayList<>();
				System.out.println("Analysing: " + repoVersionDirectory.getAbsolutePath());
				ExtractJavaFiles extractJavaFiles = new ExtractJavaFiles(repoVersionDirectory);
				ExtractClassesAndPackages classesAndPackages = new ExtractClassesAndPackages(extractJavaFiles.getJavaFiles(), branchName);
				
				for (InnerClassOfFile currentClass : classesAndPackages.getAllClasses()) {
					ExtractClassesCoupledFromCurrentClass.extract(currentClass, classesAndPackages.getAllClasses(), extractJavaFiles.getParentFiles());
				}
				ExtractDependantClasses.extract(classesAndPackages.getAllClasses());
				for (InnerClassOfFile currentClass : classesAndPackages.getAllClasses()) {
					String fileClassName = currentClass.getBranchName() + currentClass.getPackageName() + 
							currentClass.getFileName() + currentClass.getClassName();
					double classWeighting = currentClass.getClassMethods().size()+currentClass.getClassVariables().size();
					
					Double LCOM5Result = LackOfCohesionOfMethodsFive.run(currentClass);
					if (!LCOM5Result.isNaN() && !LCOM5Result.isInfinite()) {
						LCOM5All.add(LCOM5Result*classWeighting);
					}
					branch.addResult(fileClassName, "LackOfCohesionOfMethodsFive", LCOM5Result, repoVersion);
					
					Double classCohesionResult = ClassCohesion.run(currentClass);
					if (!classCohesionResult.isNaN() && !classCohesionResult.isInfinite()) {
						classCohesionAll.add(classCohesionResult*classWeighting);
					}
					branch.addResult(fileClassName, "ClassCohesion", classCohesionResult.doubleValue(), repoVersion);
					
					Double sensitiveClassCohesionResult = SensitiveClassCohesion.run(currentClass);
					if (!sensitiveClassCohesionResult.isNaN() && !sensitiveClassCohesionResult.isInfinite()) {
						sensitiveClassCohesionAll.add(sensitiveClassCohesionResult*classWeighting);
					}
					branch.addResult(fileClassName, "SensitiveClassCohesion", sensitiveClassCohesionResult, repoVersion);
					
					Double DACResult = DataAbstractionCoupling.run(currentClass, classesAndPackages.getAllClasses());
					DACAll.add(DACResult*classWeighting);
					branch.addResult(fileClassName, "DataAbstractionCoupling", DACResult, repoVersion);
					
					Double CBOResult = CouplingBetweenObjectClasses.run(currentClass);
					CBOAll.add(CBOResult*classWeighting);
					branch.addResult(fileClassName, "CouplingBetweenObjectClasses", CBOResult, repoVersion);
				}
				for (String packageKey : classesAndPackages.getAllPackages().keySet()) {
					String branchPackageName = branchName + packageKey;
					double packageWeighting = classesAndPackages.getAllPackages().get(packageKey).size();
					
					Double afferentResult = AfferentCoupling.run(classesAndPackages.getAllPackages().get(packageKey));
					afferentAll.add(afferentResult*packageWeighting);
					branch.addResult(branchPackageName, "AfferentCoupling", afferentResult, repoVersion);
					
					Double efferentResult = EfferentCoupling.run(classesAndPackages.getAllPackages().get(packageKey));
					efferentAll.add(efferentResult*packageWeighting);
					branch.addResult(branchPackageName, "EfferentCoupling", efferentResult, repoVersion);
					
					Double packageCohesionResult = PackageCohesion.run(classesAndPackages.getAllPackages().get(packageKey));
					packageCohesionAll.add(packageCohesionResult*packageWeighting);
					branch.addResult(branchPackageName, "PackageCohesion", packageCohesionResult, repoVersion);
					
				}
				applicationLevelResults.addLCOM5Results(LCOM5All);
				applicationLevelResults.addClassCohesionResults(classCohesionAll);
				applicationLevelResults.addSensitiveClassCohesionResults(sensitiveClassCohesionAll);
				applicationLevelResults.addCBOResults(CBOAll);
				applicationLevelResults.addDACResults(DACAll);
				applicationLevelResults.addAfferentResults(afferentAll);
				applicationLevelResults.addEfferentResults(efferentAll);
				applicationLevelResults.addPackageCohesionResults(packageCohesionAll);
				
				classesAndPackages.getAllClasses().clear();
				repoVersion--;
			}
			ApplicationLevelMetric.run(applicationLevelResults);
			HandleGitResults.gitRepoResults(branch, applicationLevelResults, resultsDirPath);
		}
		ParseGitRepo.clearTempDirs(directoriesPath, remote);
	}
}
