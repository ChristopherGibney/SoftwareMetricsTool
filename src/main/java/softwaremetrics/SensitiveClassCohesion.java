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
	
	public static ArrayList<Entry<InnerClassOfFile, Double>> run(File f) throws FileNotFoundException {
		ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(f);
		ArrayList<Entry<InnerClassOfFile, Double>> sensitiveClassCohesionOfAllInnerClasses = new ArrayList<>();
		
		for (InnerClassOfFile innerClass : classes) {
			double sumSensitiveMethodSimilarities = 0;
			double numAttributes = innerClass.getClassVariables().size();
			double numMethods = innerClass.getClassMethods().size();
			
			ArrayList<MethodDeclaration> classMethods = innerClass.getClassMethods();
			for (int i = 0; i < classMethods.size(); i++) {
				for (int j = i + 1; j < classMethods.size(); j++) {
					MethodPairIntersectionAndUnion.run(classMethods.get(i), classMethods.get(j), innerClass);
					double methodPairIntersection = MethodPairIntersectionAndUnion.getMethodPairIntersection();
					double methodPairUnion = MethodPairIntersectionAndUnion.getMethodPairUnion();
					double minMethodAttributes = MethodPairIntersectionAndUnion.getMinMethodAttributes();
					sumSensitiveMethodSimilarities += ((methodPairIntersection / minMethodAttributes)*(methodPairUnion/numAttributes));
				}
			}
			
			double sensitiveClassCohesionResult = (2 * (sumSensitiveMethodSimilarities)) / (numMethods * (numMethods - 1));
			Entry<InnerClassOfFile, Double> sensitiveClassCohesion = new SimpleEntry<>(innerClass, sensitiveClassCohesionResult);
			sensitiveClassCohesionOfAllInnerClasses.add(sensitiveClassCohesion);
		}
		return sensitiveClassCohesionOfAllInnerClasses;
	}
}
