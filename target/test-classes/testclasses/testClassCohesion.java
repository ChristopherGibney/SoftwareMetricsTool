
public class testClassCohesion {
	public int publicAttributeInt1, publicAttributeInt2;
	private String privateAttributeString1;
	private static int privateStaticAttribute1;
	
	public void method1() {
		double publicMethod1double, publicAttributeInt1;
		publicAttributeInt1 = 0;
		privateAttributeString1 = "test";
		testClassCohesion.privateStaticAttribute1 = 0;
	}
	
	private void method2() {
		double privateMethod2double, publicAttributeInt2;
		this.publicAttributeInt2 = 1;
		publicAttributeInt1 = 0;
		privateAttributeString1 = "test";
	}
	
	private static void method3() {
		int privateStaticAttribute1;
		privateStaticAttribute1 = 0;
		testClassCohesion.privateStaticAttribute1 = 0;
	}
}
