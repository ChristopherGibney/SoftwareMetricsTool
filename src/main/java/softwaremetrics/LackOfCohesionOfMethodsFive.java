package softwaremetrics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import softwaremetricshelperclasses.ExtractClassAttributesAccessedByMethod;
import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.ExtractDuplicateClassAttributesOfMethod;
import softwaremetricshelperclasses.InnerClassOfFile;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class LackOfCohesionOfMethodsFive {
	
	public static ArrayList<Entry<InnerClassOfFile, Double>> run(File f) throws FileNotFoundException {
		CompilationUnit cu = StaticJavaParser.parse(f);
		ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(cu);
		ArrayList<Entry<InnerClassOfFile, Double>> lcomOfAllInnerClasses = new ArrayList<>();
		
		for (InnerClassOfFile innerClass : classes) {
			double numMethods = innerClass.getClassMethods().size();
			double numAttributes = innerClass.getClassVariables().size();
			double sumAttributesAccessed = 0;
			
			for (MethodDeclaration method : innerClass.getClassMethods()) {
				ArrayList<VariableDeclarator> duplicateMethodClassVars = 
						ExtractDuplicateClassAttributesOfMethod.extract(method, innerClass);
				
				ArrayList<VariableDeclarator> classAttributesAccessed = 
						ExtractClassAttributesAccessedByMethod.extract(duplicateMethodClassVars, method, innerClass);
				
				sumAttributesAccessed += classAttributesAccessed.size();
			}
			
			
			double lcomClassResult = (numMethods - (sumAttributesAccessed/numAttributes)) / (numMethods - 1.0);
			Entry<InnerClassOfFile, Double> lcomClass = new SimpleEntry<>(innerClass, lcomClassResult);
			lcomOfAllInnerClasses.add(lcomClass);
		}
		
		return lcomOfAllInnerClasses;
	}
}

