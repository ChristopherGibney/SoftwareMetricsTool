
public class DataAbstractionCoupling_TestResourceC {
	int i, j, k;
	String s, l, m;
	double x, y, z;
	
	public void method1() {
		DataAbstractionCoupling_TestResourceA dacDoesNotCount = new DataAbstractionCoupling_TestResourceA();
		DataAbstractionCoupling_TestResourceB dacDoesNotCount2 = new DataAbstractionCoupling_TestResourceB();
	}
	public void method2() {
		ArrayList<DataAbstractionCoupling_TestResourceA> dacDoesNotCount = new ArrayList<>();
		Map<DataAbstractionCoupling_TestResourceB, String> dacDoesNotCount2 = new HashMap<>();
	}
}
