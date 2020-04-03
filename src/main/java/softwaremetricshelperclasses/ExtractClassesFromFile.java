package softwaremetricshelperclasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class ExtractClassesFromFile {

	public static ArrayList<InnerClassOfFile> extract(File f) throws FileNotFoundException {
		CompilationUnit cu = StaticJavaParser.parse(f);
		ArrayList<InnerClassOfFile> classes = new ArrayList<>();
		
		for (Node child : cu.getChildNodes()) {
			if (child instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration classOrInterface = (ClassOrInterfaceDeclaration) child;
				InnerClassOfFile newClassOrInterface = new InnerClassOfFile(classOrInterface);
				for (Node n : child.getChildNodes()) {
					if (n instanceof FieldDeclaration) {
						FieldDeclaration field = (FieldDeclaration) n;
						NodeList<VariableDeclarator> variables = field.getVariables();
						for (VariableDeclarator v : variables) {
							newClassOrInterface.addVariable(v);
						}
						newClassOrInterface.addField((FieldDeclaration) n);
					}
					else if (n instanceof MethodDeclaration) {
						newClassOrInterface.addMethod((MethodDeclaration) n);
					}
				}
				classes.add(newClassOrInterface);
			}
		}
		return classes;
	}
}
