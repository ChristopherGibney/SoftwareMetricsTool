package softwaremetrics;

import com.github.javaparser.ast.CompilationUnit;

/*Not used in most recent version of tool, was used in initial development setting up tool*/
public class LinesOfCode {
	
	public static int getLinesOfCode(CompilationUnit cu) {
		return cu.getRange().get().getLineCount();	
	}
}
