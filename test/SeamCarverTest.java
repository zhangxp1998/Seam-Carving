import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import edu.princeton.cs.algs4.Picture;

public class SeamCarverTest
{
	@Test
	public void testFindHorizontalSeam5x6()
	{
		Picture small = new Picture("/home/kelvin/Downloads/seamCarving/5x6.png");
		SeamCarver sc = new SeamCarver(small);
		int[] hseam = sc.findHorizontalSeam();
		int[] answer = { 2, 3, 2, 3, 2 };
		assertTrue(Arrays.equals(answer, hseam));
	}
	
	@Test
	public void testFindHorizontalSeamBig()
	{
		Picture small = new Picture("/home/kelvin/Downloads/seamCarving/HJocean.png");
		SeamCarver sc = new SeamCarver(small);
		int[] hseam = sc.findHorizontalSeam();
		System.out.println(hseam);
	}
}
