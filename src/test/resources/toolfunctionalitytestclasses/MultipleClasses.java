
public class MultipleClasses {
	
	private int attribute1, attribute2;

	public void method1() {
		attribute1 = attribute2;
	}
	
	public void method2() {
		attribute2 = attribute1;
	}
}

class SecondClass {
	private int secondClassAttribute1, secondClassAttribute2;
	
	public void secondClassMethod1() {
		secondClassAttribute1 = secondClassAttribute2;
	}
	
	public void secondClassMethod2() {
		secondClassAttribute1 = secondClassAttribute2;
	}
}
