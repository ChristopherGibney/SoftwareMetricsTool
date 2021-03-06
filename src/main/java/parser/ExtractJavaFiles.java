package parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExtractJavaFiles {
	private ArrayList<String> fileNames = new ArrayList<String>();
	private ArrayList<String> fileAbsolutePath = new ArrayList<String>();
	private ArrayList<File> javaFiles = new ArrayList<File>();
	private ArrayList<File> parentFiles = new ArrayList<File>();
	
	public ExtractJavaFiles(File root) throws IOException {
		File[] files = root.listFiles();
		this.getClassFiles(files);
	}
	
	private void getClassFiles(File[] files) {
		for (File f : files ) {
			String fileName = f.getAbsolutePath();
			 
			if (fileName.endsWith(".java")) {
				String shortenedName = (f.getName().substring(0, f.getName().indexOf(".")));
				javaFiles.add(f);
				if (!parentFiles.contains(f.getParentFile())) {
					//System.out.println(f.getParentFile().getName());
					parentFiles.add(f.getParentFile());
				}
				if (!parentFiles.contains(f.getParentFile().getParentFile())) {
					parentFiles.add(f.getParentFile().getParentFile());
				}
				fileNames.add(shortenedName);
				fileAbsolutePath.add(f.getAbsolutePath());
			}
			else {
				File exploreFurther = new File(fileName);
				if (exploreFurther.listFiles() != null) {
					getClassFiles(exploreFurther.listFiles());
				}
			}
		}
	}
	public ArrayList<String> getJavaClassesStrings() {
		return fileNames;
	}
	public ArrayList<File> getJavaFiles() {
		return javaFiles;
	}
	public ArrayList<File> getParentFiles() {
		return parentFiles;
	}
}
