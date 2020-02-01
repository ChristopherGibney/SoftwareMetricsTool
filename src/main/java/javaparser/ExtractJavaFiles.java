package javaparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ExtractJavaFiles {
	
	private String classStrings[] = new String[20];
	
	public ExtractJavaFiles(File root) throws IOException {
		
		//System.out.println(root.listFiles().toString());
		File[] files = root.listFiles();
		
		//String classStrings[] = new String[20];
		classStrings = getClassFiles(files, classStrings);
		
		for (int i = 0; i < classStrings.length; i++) {
			if (classStrings[i] != null)
				System.out.println(classStrings[i]);
		}
		
	}
	
	private static String[] getClassFiles(File[] files, String classStrings[]) {
		
		for (File f : files ) {
			 String fileName = f.getAbsolutePath();
			 
			 if (fileName.endsWith(".java")) {
				String test = null;
				try {
					test = (f.getName().substring(0, f.getName().indexOf("."))) + "\n";
					test += createFileString(fileName);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				int i = 0;
				
				for (i = 0; i < classStrings.length; i++) {
					
					if (classStrings[i] == null) {
						classStrings[i] = test;
						i = classStrings.length + 1;
					}
				}
				
				//if classStrings is full, i will equal length of classStrings from for loop above
				if (i == classStrings.length) {
					String newClassStrings[] = new String[classStrings.length * 2];
					for (int j = 0; j < classStrings.length - 1; j++) {
						newClassStrings[j] = classStrings[j];
					}
					classStrings = newClassStrings;
				}
				 
			 }
			 //if classes not found yet go into folder and add more files to be looked at
			 else {
				 File exploreFurther = new File(fileName);
				 if (exploreFurther.listFiles() != null) {
					 getClassFiles(exploreFurther.listFiles(), classStrings);
				 }
			 }
		 }
			
		
		return classStrings;
		
	}

	private static String createFileString(String filePath) throws IOException {
		
		StringBuilder fileAsString = new StringBuilder(1000);
		BufferedReader r = new BufferedReader(new FileReader(filePath));

		char[] chars = new char[2000];
		String fileSegment = null;
		int charsToRead = 0;
	
		while ((charsToRead = r.read(chars)) != -1) {
			fileSegment = new String(chars, 0, charsToRead);
			fileAsString.append(fileSegment);
		}

		r.close();

		return  fileAsString.toString();	
	}
	
	public String[] returnJavaClassesStrings() {
		return classStrings;
	}

}
