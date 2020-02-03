package javaparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ExtractJavaFiles {
	
	//these arrays will always be of the same size
	private static String classStrings[] = new String[20];
	private static File javaFiles[] = new File[20];
	
	public ExtractJavaFiles(File root) throws IOException {
		
		//System.out.println(root.listFiles().toString());
		File[] files = root.listFiles();
		
		//String classStrings[] = new String[20];
		getClassFiles(files);
		
		
	}
	
	private static void getClassFiles(File[] files) {
		
		for (File f : files ) {
			 String fileName = f.getAbsolutePath();
			 
			 if (fileName.endsWith(".java")) {
				String shortenedName = null;
				
				shortenedName = (f.getName().substring(0, f.getName().indexOf(".")));
				
				int i = 0;
				
				for (i = 0; i < classStrings.length; i++) {
					
					if (classStrings[i] == null) {
						javaFiles[i] = f;
						classStrings[i] = shortenedName;
						i = classStrings.length + 1;
					}
				}
				
				//if classStrings is full, i will equal length of classStrings from for loop above
				if (i == classStrings.length) {
					
					String newClassStrings[] = new String[classStrings.length * 2];
					File newJavaFiles[] = new File[javaFiles.length * 2];
					
					for (int j = 0; j < classStrings.length - 1; j++) {
						newJavaFiles[j] = javaFiles[j];
						newClassStrings[j] = classStrings[j];
					}
					classStrings = newClassStrings;
					javaFiles = newJavaFiles;
				}
				 
			 }
			 
			 //if classes not found yet go into folder and add more files to be looked at
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
	
	public File[] returnJavaFiles() {
		return javaFiles;
	}

}
