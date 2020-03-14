package parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

public class ParseDirectory {
	
	public static List<CompilationUnit> parse(File rootDir) {
		
		List<CompilationUnit> cuList = null;
		
		CombinedTypeSolver typeSolver = new CombinedTypeSolver(
				new ReflectionTypeSolver(),
				new JavaParserTypeSolver(rootDir));
		
		ParserConfiguration parserConfiguration =
				new ParserConfiguration()
				.setSymbolResolver(new JavaSymbolSolver(typeSolver));
		
		SourceRoot sr = new SourceRoot(rootDir.toPath());
		
		sr.setParserConfiguration(parserConfiguration);
		
		try {
			sr.tryToParse("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cuList = sr.getCompilationUnits();
		
		return cuList;
        
	}
	
}
