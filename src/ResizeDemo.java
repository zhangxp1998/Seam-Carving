
/******************************************************************************
 *  Compilation:  javac ResizeDemo.java
 *  Execution:    java ResizeDemo input.png columnsToRemove rowsToRemove
 *  Dependencies: SeamCarver.java SCUtility.java
 *                
 *
 *  Read image from file specified as command line argument. Use SeamCarver
 *  to remove number of rows and columns specified as command line arguments.
 *  Show the images and print time elapsed to screen.
 *
 ******************************************************************************/

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class ResizeDemo
{
	public static void main(String[] args) throws IOException
	{
		if (args.length != 3)
		{
			StdOut.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
			return;
		}

		Picture inputImg = new Picture(args[0]);
		int removeColumns = Integer.parseInt(args[1]);
		int removeRows = Integer.parseInt(args[2]);

		StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
		System.out.println("Press enter to start carving...");
		Scanner in = new Scanner(System.in);
		in.nextLine();
		in.close();

		System.out.println("Start carving...");
		SeamCarver sc = new DPSeamCarving(ImageIO.read(new File(args[0])));

		Stopwatch sw = new Stopwatch();

		for (int i = 0; i < removeRows; i++)
		{
			int[] horizontalSeam = sc.findHorizontalSeam();
			sc.removeHorizontalSeam(horizontalSeam);
		}

		for (int i = 0; i < removeColumns; i++)
		{
			int[] verticalSeam = sc.findVerticalSeam();
			sc.removeVerticalSeam(verticalSeam);
		}
		System.out.println("Carving Done!");
		Picture outputImg = sc.picture();

		StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());

		StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
		inputImg.show();
		outputImg.show();
	}
}
