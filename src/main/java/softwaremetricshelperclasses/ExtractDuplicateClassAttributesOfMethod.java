package softwaremetricshelperclasses;

import java.util.ArrayList;
import java.util.Objects;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class ExtractDuplicateClassAttributesOfMethod {

	public static ArrayList<VariableDeclarator> extract(MethodDeclaration method, InnerClassOfFile innerClass) {
		ArrayList<VariableDeclarator> duplicateMethodClassVars = new ArrayList<>();
		for (VariableDeclarator methodVariable : method.findAll(VariableDeclarator.class)) {
			for (VariableDeclarator classVar : innerClass.getClassVariables()) {
				if (Objects.equals(methodVariable.toString(), classVar.toString())) {
					duplicateMethodClassVars.add(classVar);
				}	
			}
		}
		return duplicateMethodClassVars;
	}
}
