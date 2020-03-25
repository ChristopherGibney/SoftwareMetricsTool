package softwaremetricshelperclasses;

import java.util.ArrayList;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class MethodPairIntersectionAndUnion {
	
	private static double methodPairIntersection, methodPairUnion, minMethodAttributes;

	public static void run(MethodDeclaration method1, MethodDeclaration method2, InnerClassOfFile innerClass) {
		methodPairIntersection = 0;
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
		minMethodAttributes = (method1ClassAttributes.size() < method2ClassAttributes.size()) ? method1ClassAttributes.size() : method2ClassAttributes.size();
	}
	public static double getMethodPairIntersection() {
		return methodPairIntersection;
	}
	public static double getMethodPairUnion() {
		return methodPairUnion;
	}
	public static double getMinMethodAttributes() {
		return minMethodAttributes;
	}
}
