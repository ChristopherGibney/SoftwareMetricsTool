package cohesionhelperclasses;

import java.util.ArrayList;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class InnerClassOfFile {
	ClassOrInterfaceDeclaration classOrInterface = new ClassOrInterfaceDeclaration();
	private ArrayList<FieldDeclaration> classFields = new ArrayList<>();
	private ArrayList<VariableDeclarator> classVariables = new ArrayList<>();
	private ArrayList<MethodDeclaration> classMethods = new ArrayList<>();
	private String className = "";
	
	public InnerClassOfFile(ClassOrInterfaceDeclaration c) {
		classOrInterface = c;
		className = classOrInterface.getName().asString();
	}
	public void addField(FieldDeclaration fieldDeclaration) {
		classFields.add(fieldDeclaration);
	}
	public void addVariable(VariableDeclarator variable) {
		classVariables.add(variable);
	}
	public void addMethod(MethodDeclaration methodDeclaration) {
		classMethods.add(methodDeclaration);
	}
	public ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration() {
		return classOrInterface;
	}
	public String getClassName() {
		return className;
	}
	public ArrayList<FieldDeclaration> getClassFields() {
		return classFields;
	}
	public ArrayList<VariableDeclarator> getClassVariables() {
		return classVariables;
	}
	public ArrayList<MethodDeclaration> getClassMethods() {
		return classMethods;
	}
}
