package softwaremetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import softwaremetricshelperclasses.InnerClassOfFile;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class DataAbstractionCoupling {
	public static double run(InnerClassOfFile currentClass, ArrayList<InnerClassOfFile> allClasses, ArrayList<File> parentFiles) throws FileNotFoundException {
		double dataAbstractionCouplingCount = 0;
		
		for (VariableDeclarator var : currentClass.getClassVariables()) {
			List<ClassOrInterfaceType> listVarTypes = var.getType().findAll(ClassOrInterfaceType.class);
			for (InnerClassOfFile innerClass : allClasses) {
				if (!innerClass.equals(currentClass)) {
					for (ClassOrInterfaceType varType : listVarTypes) {
						if (innerClass.getClassName().equals(varType.asString())) {
							dataAbstractionCouplingCount++;
						}
					}
				}
			}
		}
		return dataAbstractionCouplingCount;
	}
}
