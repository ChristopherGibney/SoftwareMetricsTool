package softwaremetricshelperclasses;

import java.util.ArrayList;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class ExtractClassAttributesAccessedByMethod {

	public static ArrayList<VariableDeclarator> extract(ArrayList<VariableDeclarator> duplicateClassVars, MethodDeclaration method, InnerClassOfFile innerClass) {
		ArrayList<VariableDeclarator> classAttributesAccessed = new ArrayList<>();
		for (VariableDeclarator classVar : innerClass.getClassVariables()) {
			if (duplicateClassVars.contains(classVar)) {
				if (method.toString().contains("this."+classVar.toString()) ||
						method.toString().contains(innerClass.getClassName()+"."+classVar.toString())) {
					classAttributesAccessed.add(classVar);
				}
			}
			else if (method.toString().contains(classVar.toString())) {
				classAttributesAccessed.add(classVar);
			}
		}
		return classAttributesAccessed;
	}
}
