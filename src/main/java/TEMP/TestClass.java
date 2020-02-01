package TEMP;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestClass {


	@Test
	public void myTestMethod() {
		// test code here

		System.out.println("merge boss..");

	}

	@BeforeClass
	public static void beforeClass() {

		System.out.println("Before class");
	}

	@Before
	public void before() {

		System.out.println("Before each test");
	}

	@Test
	public void test01() {

		System.out.println("Test 1");
	}
}
