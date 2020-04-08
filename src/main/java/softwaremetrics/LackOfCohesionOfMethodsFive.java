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

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class LackOfCohesionOfMethodsFive {
	
	public static double run(InnerClassOfFile currentClass) throws FileNotFoundException {
		double numMethods = currentClass.getClassMethods().size();
		double numAttributes = currentClass.getClassVariables().size();
		double sumAttributesAccessed = 0;
			
		for (MethodDeclaration method : currentClass.getClassMethods()) {
			ArrayList<VariableDeclarator> duplicateMethodClassVars = 
					ExtractDuplicateClassAttributesOfMethod.extract(method, currentClass);
				
			ArrayList<VariableDeclarator> classAttributesAccessed = 
					ExtractClassAttributesAccessedByMethod.extract(duplicateMethodClassVars, method, currentClass);
				
			sumAttributesAccessed += classAttributesAccessed.size();
		}
			
		double lcomClassResult = (numMethods - (sumAttributesAccessed/numAttributes)) / (numMethods - 1.0); 
		
		return lcomClassResult;
	}
}

