package softwaremetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import cohesionhelperclasses.ExtractClassesFromFile;
import cohesionhelperclasses.ExtractDuplicateClassAttributesOfMethod;
import cohesionhelperclasses.InnerClassOfFile;
import cohesionhelperclasses.ExtractClassAttributesAccessedByMethod;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class ClassCohesion {

	public static ArrayList<Entry<InnerClassOfFile, Double>> run(File f) throws FileNotFoundException {
		
		CompilationUnit cu = StaticJavaParser.parse(f);
		ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(cu);
		ArrayList<Entry<InnerClassOfFile, Double>> classCohesionOfAllInnerClasses = new ArrayList<>();
		
		for (InnerClassOfFile innerClass : classes) {
			ArrayList<Entry<MethodDeclaration, MethodDeclaration>> pairs = new ArrayList<>();
			double sumMethodSimilarities = 0;
			double numMethods = innerClass.getClassMethods().size();
			
			ArrayList<MethodDeclaration> classMethods = innerClass.getClassMethods();
			for (int i = 0; i < classMethods.size(); i++) {
				for (int j = i + 1; j < classMethods.size(); j++) {
					double methodPairIntersection = 0, methodPairUnion = 0;
					Entry<MethodDeclaration, MethodDeclaration> pair = new SimpleEntry<>(classMethods.get(i), classMethods.get(j));
					pairs.add(pair);
					MethodDeclaration method1 = classMethods.get(i);
					MethodDeclaration method2 = classMethods.get(j);
					
					ArrayList<VariableDeclarator> method1DuplicateClassVars = 
							ExtractDuplicateClassAttributesOfMethod.extract(method1, innerClass);
					ArrayList<VariableDeclarator> method2DuplicateClassVars = 
							ExtractDuplicateClassAttributesOfMethod.extract(method2, innerClass);
					ArrayList<VariableDeclarator> method1ClassAttributes = 
							ExtractClassAttributesAccessedByMethod.extract(method1DuplicateClassVars, method1, innerClass);
					ArrayList<VariableDeclarator> method2ClassAttributes = 
							ExtractClassAttributesAccessedByMethod.extract(method2DuplicateClassVars, method2, innerClass);

					for (VariableDeclarator method2attribute : method2ClassAttributes) {
						if (method1ClassAttributes.contains(method2attribute))
							methodPairIntersection += 1;
					}
					
					methodPairUnion = method1ClassAttributes.size() + method2ClassAttributes.size() - methodPairIntersection;
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
