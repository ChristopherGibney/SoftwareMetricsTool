package softwaremetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import softwaremetricshelperclasses.InnerClassOfFile;

import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class DataAbstractionCoupling {
	public static double run(InnerClassOfFile currentClass, ArrayList<InnerClassOfFile> allClasses, ArrayList<File> parentFiles) throws FileNotFoundException {
		double dataAbstractionCouplingCount = 0;
		for (FieldAccessExpr fieldRef : currentClass.getClassOrInterfaceDeclaration().findAll(FieldAccessExpr.class)) {
			for (int fileCounter = 0; fileCounter < parentFiles.size(); fileCounter++) {
				TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(parentFiles.get(fileCounter)));
				JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver);
				//System.out.println("Field " +parentFile.getName() + " " + currentClass.getClassName() + " " + fieldRef.toString());
				
				try {
					if (javaParserFacade.solve(fieldRef).isSolved()) {
						fileCounter = parentFiles.size();
						ResolvedType resolvedFieldType = javaParserFacade.solve(fieldRef).getCorrespondingDeclaration().asField().getType();
						ResolvedFieldDeclaration resolvedField = javaParserFacade.solve(fieldRef).getCorrespondingDeclaration().asField();
						
						for (int classCounter = 0; classCounter < allClasses.size(); classCounter++) {
							if (allClasses.get(classCounter).getClassName().equals(resolvedFieldType.getClass().getName()) && allClasses.get(classCounter) != currentClass) {
								if (allClasses.get(classCounter).containsVariable(resolvedField.getName().toString())) {
									System.out.println("Field " +currentClass.getClassName() + " " + allClasses.get(classCounter).getClassName() + " " + javaParserFacade.solve(fieldRef).getCorrespondingDeclaration().asField().getName());
									dataAbstractionCouplingCount++;
									classCounter = allClasses.size();
								}
							}
						}
					}
				} catch (Exception e) {}
				JavaParserFacade.clearInstances();
			}
		}
		return dataAbstractionCouplingCount;
	}
}
