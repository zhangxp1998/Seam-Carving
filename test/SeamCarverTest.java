import java.util.Arrays;

import org.junit.Test;

import edu.princeton.cs.algs4.Picture;

public class SeamCarverTest
{
	@Test
	public void testFindHorizontalSeam()
	{
		Picture small = new Picture("/home/kelvin/Downloads/seamCarving/5x6.png");
		SeamCarver sc = new SeamCarver(small);
		int[] hseam = sc.findHorizontalSeam();
		System.out.println(Arrays.toString(hseam));
	}
}
