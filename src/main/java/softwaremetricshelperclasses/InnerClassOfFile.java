package softwaremetricshelperclasses;

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
	private String className = "", fileName = "", branchName = "", packageName = "";
	private ArrayList<InnerClassOfFile> coupledObjectClasses = new ArrayList<>(), dependantClasses = new ArrayList<>();
	
	public InnerClassOfFile(ClassOrInterfaceDeclaration c) {
		classOrInterface = c;
		className = classOrInterface.getName().asString();
	}
	public boolean containsVariable(String variableString) {
		for (VariableDeclarator variable : classVariables) {
			if (variable.getName().toString().equals(variableString)) {
				return true;
			}
		}
		return false;
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
	public void addDependantClass(InnerClassOfFile dependantClass) {
		dependantClasses.add(dependantClass);
	}
	public void addCoupledObjectClass(InnerClassOfFile coupledClass) {
		coupledObjectClasses.add(coupledClass);
	}
	public void addFileName(String nameOfFile) {
		fileName = nameOfFile;
	}
	public void addPackageName(String nameOfPackage) {
		packageName = nameOfPackage;
	}
	public void addBranchName (String nameOfBranch) {
		branchName = nameOfBranch;
	}
	public ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration() {
		return classOrInterface;
	}
	public ArrayList<InnerClassOfFile> getCoupledObjectClasses() {
		return coupledObjectClasses;
	}
	public ArrayList<InnerClassOfFile> getDependantClasses() {
		return dependantClasses;
	}
	public String getClassName() {
		return className;
	}
	public String getFileName() {
		return fileName;
	}
	public String getPackageName() {
		return packageName;
	}
	public String getBranchName() {
		return branchName;
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
