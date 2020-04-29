
public class LCOM5_TestResource {
	
	public int publicAttributeInt1, publicAttributeInt2;
	private String privateAttributeString1;
	private static int privateStaticAttribute1;

	public testLCOM5() {
		int constructorInt1, constructorInt2;
	}
	
	public void method1() {
		double publicMethod1double, publicAttributeInt1;
		publicAttributeInt1 = 0;
		privateAttributeString1 = "test";
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
		LCOM5_TestResource.privateStaticAttribute1 = 0;
	}
}
