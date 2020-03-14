package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ExtractJavaFiles {
	private static String classStrings[] = new String[200];
	private static ArrayList<File> javaFiles = new ArrayList<File>();
	
	public ExtractJavaFiles(File root) throws IOException {
		File[] files = root.listFiles();
		getClassFiles(files);
	}
	
	private static void getClassFiles(File[] files) {
		for (File f : files ) {
			 String fileName = f.getAbsolutePath();
			 
			 if (fileName.endsWith(".java")) {
				String shortenedName = (f.getName().substring(0, f.getName().indexOf(".")));
				boolean expandFileArray = true;
				javaFiles.add(f);
				
				for (int i = 0; i < classStrings.length; i++) {
					if (classStrings[i] == null) {
						//javaFiles.add(f);
						classStrings[i] = shortenedName;
						i = classStrings.length + 1;
						expandFileArray = false;
					}
				}
				
				if (expandFileArray) {
					String newClassStrings[] = new String[classStrings.length * 2];
					//File newJavaFiles[] = new File[javaFiles.length * 2];
					
					for (int j = 0; j < classStrings.length - 1; j++) {
						//newJavaFiles[j] = javaFiles[j];
						newClassStrings[j] = classStrings[j];
					}
					classStrings = newClassStrings;
					//javaFiles = newJavaFiles;
				}
			 }
			 
			 else {
				File exploreFurther = new File(fileName);
				if (exploreFurther.listFiles() != null) {
					getClassFiles(exploreFurther.listFiles());
				}
			}
		}
	}

	public String[] returnJavaClassesStrings() {
		return classStrings;
	}
	
	public ArrayList<File> returnJavaFiles() {
		return javaFiles;
	}
}
