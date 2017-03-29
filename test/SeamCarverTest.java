import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

public class SeamCarverTest
{
	private String home;

	@Before
	public void setup()
	{
		home = System.getProperty("user.home");
	}

	private void testSeamFinding(String filename) throws FileNotFoundException
	{
		String solution = filename.replaceAll(".png", ".printseams.txt");
		File solutionFile = new File(solution);
		assertTrue(String.format("Solution file for %s not found!", filename), solutionFile.exists());

		Scanner in = new Scanner(solutionFile);
		int[] hanswer = null;
		int[] vanswer = null;

		while (in.hasNext())
		{
			String str = in.nextLine();
			if (str.startsWith("Horizontal seam: "))
			{
				str = str.substring("Horizontal seam: ".length() + 2, str.length() - 1);
				String[] vals = str.split(" ");
				hanswer = new int[vals.length];

				for (int i = 0; i < hanswer.length; i++)
					hanswer[i] = Integer.parseInt(vals[i]);

				if (vanswer != null)
					break;

			} else if (str.startsWith("Vertical seam: "))
			{
				str = str.substring("Vertical seam: ".length() + 2, str.length() - 1);
				String[] vals = str.split(" ");
				vanswer = new int[vals.length];

				for (int i = 0; i < vanswer.length; i++)
					vanswer[i] = Integer.parseInt(vals[i]);

				if (hanswer != null)
					break;
			}
		}

		assert hanswer != null;
		assert vanswer != null;

		// System.out.println(Arrays.toString(hanswer));
		// System.out.println(Arrays.toString(vanswer));

		in.close();

		SeamCarver sc;
		try
		{
			sc = new DPSeamCarving(ImageIO.read(new File(filename)));
			int[] hseam = sc.findHorizontalSeam();
			assertTrue(Arrays.equals(hanswer, hseam));

			int[] vseam = sc.findVerticalSeam();
			assertTrue(Arrays.equals(vanswer, vseam));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testFindSeam5x6() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/5x6.png");
	}

	@Test
	public void testFindSeam3x4() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/3x4.png");
	}

	@Test
	public void testFindSeam3x7() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/3x7.png");
	}

	@Test
	public void testFindSeam4x6() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/4x6.png");
	}

	@Test
	public void testFindSeam6x5() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/6x5.png");
	}

	@Test
	public void testFindSeam7x3() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/7x3.png");
	}

	@Test
	public void testFindSeam7x10() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/7x10.png");
	}

	@Test
	public void testFindSeam10x10() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/10x10.png");
	}

	@Test
	public void testFindSeam10x12() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/10x12.png");
	}

	@Test
	public void testFindSeam12x10() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/12x10.png");
	}

	@Test
	public void testFindSeamdiagonals() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/diagonals.png");
	}

	@Test
	public void testFindSeamstripes() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/stripes.png");
	}

	@Test
	public void testFindSeamBigPicture() throws FileNotFoundException
	{
		testSeamFinding(home + "/Downloads/seamCarving/chameleon.png");
	}
}
