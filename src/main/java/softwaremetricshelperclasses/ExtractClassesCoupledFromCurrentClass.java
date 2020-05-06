package softwaremetricshelperclasses;

import java.io.File;
import java.util.ArrayList;

import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class ExtractClassesCoupledFromCurrentClass {
	public static void extract(InnerClassOfFile currentClass, ArrayList<InnerClassOfFile> allClasses, ArrayList<File> parentFiles) {
		
		for (MethodCallExpr methodRef : currentClass.getClassOrInterfaceDeclaration().findAll(MethodCallExpr.class)) {
			for (int i = 0; i < parentFiles.size(); i++) {
				TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(parentFiles.get(i)));
				JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver);
				//System.out.println("Method " +parentFile.getName() + " " + currentClass.getClassName() + " " + methodRef.toString());
				try {
					if (javaParserFacade.solve(methodRef).isSolved()) {
						//end for loop when reference is solved
						i = parentFiles.size();
						ResolvedMethodDeclaration resolvedMethod = javaParserFacade.solve(methodRef).getCorrespondingDeclaration();
						//only interested in couplings with other classes from the application so method call is not counted
						//if it comes from a class not within the app or if it is a call to a method in the current class
						for (InnerClassOfFile innerClass : allClasses) {
							if (innerClass.getClassName().equals(resolvedMethod.getClassName()) && innerClass != currentClass) {
								if (innerClass.getClassMethods().contains(resolvedMethod.toAst().get())) {
									//System.out.println("Method "+currentClass.getClassName() + " " +innerClass.getClassName() + " " + resolvedMethod.toAst().get().toString());
									if (!currentClass.getCoupledObjectClasses().contains(innerClass)) {
										currentClass.addCoupledObjectClass(innerClass);
									}
								}
							}	
						}
					} 
				} catch (Exception e) {}
				JavaParserFacade.clearInstances();
			}
		}
		for (FieldAccessExpr fieldRef : currentClass.getClassOrInterfaceDeclaration().findAll(FieldAccessExpr.class)) {
			for (int i = 0; i < parentFiles.size(); i++) {
				TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(parentFiles.get(i)));
				JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver);
				//System.out.println("Field " +parentFile.getName() + " " + currentClass.getClassName() + " " + fieldRef.toString());
				
				try {
					if (javaParserFacade.solve(fieldRef).isSolved()) {
						//end for loop when reference is solved
						i = parentFiles.size();
						ResolvedClassDeclaration resolvedFieldClass = javaParserFacade.solve(fieldRef).getCorrespondingDeclaration().asField().declaringType().asClass();
						for (InnerClassOfFile innerClass : allClasses) {
							if (innerClass.getClassName().equals(resolvedFieldClass.getName()) && innerClass != currentClass) {
								if (innerClass.containsVariable(javaParserFacade.solve(fieldRef).getCorrespondingDeclaration().asField().getName().toString())) {
									//System.out.println("Field " +currentClass.getClassName() + " " + innerClass.getClassName() + " " + javaParserFacade.solve(fieldRef).getCorrespondingDeclaration().asField().getName());
									if (!currentClass.getCoupledObjectClasses().contains(innerClass)) {
										currentClass.addCoupledObjectClass(innerClass);
									}
								}
							}
						}
					}
				} catch (Exception e) {}
				JavaParserFacade.clearInstances();
			}
		}
	}
}
