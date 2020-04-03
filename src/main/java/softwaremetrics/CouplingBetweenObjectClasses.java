package softwaremetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class CouplingBetweenObjectClasses {
	
	public static void run(File localDir, ArrayList<InnerClassOfFile> allClasses, ArrayList<File> parentFiles) throws FileNotFoundException {
		for (InnerClassOfFile innerClass : allClasses) {
			CouplingBetweenObjectClasses.coupledFromCurrentClass(localDir, innerClass, allClasses, parentFiles);
		}
		for (InnerClassOfFile currentClass : allClasses) {
			for (InnerClassOfFile objectClass : allClasses) {
				if (!currentClass.getCoupledObjectClasses().contains(objectClass)) {
					if (objectClass.getCoupledObjectClasses().contains(currentClass)) {
						currentClass.addCoupledObjectClass(objectClass);
					}
				}
			}
			currentClass.addTotalNumberCoupledClasses(currentClass.getCoupledObjectClasses().size());
		}
	}
	
	private static void coupledFromCurrentClass(File fileDir, InnerClassOfFile currentClass, ArrayList<InnerClassOfFile> allClasses, ArrayList<File> parentFiles) throws FileNotFoundException {
		//TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(fileDir));
		//JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver);
		
		for (MethodCallExpr methodRef : currentClass.getClassOrInterfaceDeclaration().findAll(MethodCallExpr.class)) {
			for (File parentFile : parentFiles) {
				TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(parentFile));
				JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver);
				
				try {
					if (javaParserFacade.solve(methodRef).isSolved()) {
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
			for (File parentFile : parentFiles) {
				TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(parentFile));
				JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver);
				
				try {
					if (javaParserFacade.solve(fieldRef).isSolved()) {
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
