package softwaremetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;
import softwaremetricshelperclasses.MethodPairIntersectionAndUnion;

import com.github.javaparser.ast.body.MethodDeclaration;

public class ClassCohesion {

	public static ArrayList<Entry<InnerClassOfFile, Double>> run(File f) throws FileNotFoundException {
		ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(f);
		ArrayList<Entry<InnerClassOfFile, Double>> classCohesionOfAllInnerClasses = new ArrayList<>();
		
		for (InnerClassOfFile innerClass : classes) {
			double sumMethodSimilarities = 0;
			double numMethods = innerClass.getClassMethods().size();
			ArrayList<MethodDeclaration> classMethods = innerClass.getClassMethods();
			for (int i = 0; i < classMethods.size(); i++) {
				for (int j = i + 1; j < classMethods.size(); j++) {
					MethodPairIntersectionAndUnion.run(classMethods.get(i), classMethods.get(j), innerClass);
					double methodPairIntersection = MethodPairIntersectionAndUnion.getMethodPairIntersection();
					double methodPairUnion = MethodPairIntersectionAndUnion.getMethodPairUnion();
					sumMethodSimilarities += (methodPairIntersection / methodPairUnion);
				}
			}
			double classCohesionResult = (2 * (sumMethodSimilarities)) / (numMethods * (numMethods - 1)); 
			Entry<InnerClassOfFile, Double> classCohesion = new SimpleEntry<>(innerClass, classCohesionResult);
			classCohesionOfAllInnerClasses.add(classCohesion);
		}
		return classCohesionOfAllInnerClasses;
	}
}
