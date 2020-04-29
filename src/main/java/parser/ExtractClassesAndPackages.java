package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;

public class ExtractClassesAndPackages {
	private ArrayList<InnerClassOfFile> allClasses = new ArrayList<>();
	Map<String, ArrayList<InnerClassOfFile>> packages = new HashMap<>();
	
	public ExtractClassesAndPackages(ArrayList<File> allFiles, String branchName) throws FileNotFoundException {
		for (File f : allFiles) {
			String fileName = f.getName().substring(0, f.getName().indexOf(".")) + "/";
			String packageName = f.getParentFile().getName() + "/";
			ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(f);
			allClasses.addAll(classes);
			for (InnerClassOfFile innerClass : classes) {
				innerClass.addFileName(fileName);
				innerClass.addPackageName(packageName);
				innerClass.addBranchName(branchName);
			}
			if (packages.containsKey(f.getParentFile().getName())) {
				packages.get(f.getParentFile().getName()).addAll(classes);
			}
			else {
				packages.put(f.getParentFile().getName(), classes);
			}	
		}
	}
	
	public ArrayList<InnerClassOfFile> getAllClasses() {
		return allClasses;
	}
	public Map<String, ArrayList<InnerClassOfFile>> getAllPackages() {
		return packages;
	}
}
