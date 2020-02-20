package softwaremetrics;

import com.github.javaparser.ast.CompilationUnit;

public class LinesOfCode {
	
	public static int getLinesOfCode(CompilationUnit cu) {
		return cu.getRange().get().getLineCount();	
	}
}
