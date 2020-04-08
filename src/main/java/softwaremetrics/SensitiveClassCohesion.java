package softwaremetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;
import softwaremetricshelperclasses.MethodPairIntersectionAndUnion;

import com.github.javaparser.ast.body.MethodDeclaration;

public class SensitiveClassCohesion {
	
	public static double run(InnerClassOfFile currentClass) throws FileNotFoundException {
		double sumSensitiveMethodSimilarities = 0;
		double numAttributes = currentClass.getClassVariables().size();
		double numMethods = currentClass.getClassMethods().size();
			
		ArrayList<MethodDeclaration> classMethods = currentClass.getClassMethods();
		for (int i = 0; i < classMethods.size(); i++) {
			for (int j = i + 1; j < classMethods.size(); j++) {
				MethodPairIntersectionAndUnion.run(classMethods.get(i), classMethods.get(j), currentClass);
				double methodPairIntersection = MethodPairIntersectionAndUnion.getMethodPairIntersection();
				double methodPairUnion = MethodPairIntersectionAndUnion.getMethodPairUnion();
				double minMethodAttributes = MethodPairIntersectionAndUnion.getMinMethodAttributes();
				sumSensitiveMethodSimilarities += ((methodPairIntersection / minMethodAttributes)*(methodPairUnion/numAttributes));
			}
		}
			
		double sensitiveClassCohesionResult = (2 * (sumSensitiveMethodSimilarities)) / (numMethods * (numMethods - 1));
		
		return sensitiveClassCohesionResult;
	}
}
