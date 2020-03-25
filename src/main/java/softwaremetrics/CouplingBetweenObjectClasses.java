package softwaremetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import softwaremetricshelperclasses.ExtractClassesFromFile;
import softwaremetricshelperclasses.InnerClassOfFile;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class CouplingBetweenObjectClasses {
	
	public static void run(File f) throws FileNotFoundException {
		TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(f));
		JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver);
		CompilationUnit cu = StaticJavaParser.parse(f);
		
		ArrayList<InnerClassOfFile> classes = ExtractClassesFromFile.extract(cu);
		
		for (InnerClassOfFile innerClass : classes) {
			for (MethodCallExpr methodRef : innerClass.getClassOrInterfaceDeclaration().findAll(MethodCallExpr.class)) {
				if (javaParserFacade.solve(methodRef).isSolved()) {
					//System.out.println(methodRef.toString());
				}
			}
		}
	}
}
