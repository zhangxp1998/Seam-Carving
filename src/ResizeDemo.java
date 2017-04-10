
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class ResizeDemo
{
	public static void main(String[] args) throws IOException
	{
		if (args.length != 3)
		{
			System.out.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
			return;
		}

		BufferedImage inputImg = ImageIO.read(new File(args[0]));
		// int removeColumns = Integer.parseInt(args[1]);
		// int removeRows = Integer.parseInt(args[2]);

		System.out.printf("image is %d columns by %d rows\n", inputImg.getWidth(), inputImg.getHeight());
		System.out.println("Press enter to start carving...");
		// Scanner in = new Scanner(System.in);
		// in.nextLine();
		// in.close();

		System.out.println("Start carving...");
		// SeamCarver sc = new DPSeamCarver(ImageIO.read(new File(args[0])));

		// Stopwatch sw = new Stopwatch();

		/*
		 * Will_start
		 */

		ImageFrame frame = new ImageFrame(inputImg);

		frame.setVisible(true);

		/*
		 * Will_end
		 */

		// for (int i = 0; i < removeRows; i++)
		// {
		// int[] horizontalSeam = sc.findHorizontalSeam();
		// sc.removeHorizontalSeam(horizontalSeam);
		// System.out.println(i + ": " + Runtime.getRuntime().totalMemory() /
		// 100_0000.0F);
		// }
		//
		// for (int i = 0; i < removeColumns; i++)
		// {
		// int[] verticalSeam = sc.findVerticalSeam();
		// sc.removeVerticalSeam(verticalSeam);
		// System.out.println(i + ": " + Runtime.getRuntime().totalMemory() /
		// 100_0000.0F);
		// }
		// System.out.println("Carving Done!");
		//
		// Picture outputImg = sc.picture();
		//
		// StdOut.printf("new image size is %d columns by %d rows\n",
		// sc.width(), sc.height());
		//
		// StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
		// inputImg.show();
		// outputImg.show();
		// outputImg.save("./output1.jpg");
	}
}

class ImageFrame extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6626001906329483589L;

	public ImageFrame(BufferedImage img)
	{
		setTitle("ImageTest");
		ImageComponent ic = new ImageComponent(img);
		add(ic);
		pack();
		ic.addComponentListener(new ComponentAdapter()
		{
			SeamCarver sc = new DPSeamCarver(img);

			@Override
			public void componentResized(ComponentEvent e)
			{
				// System.out.println(e);
				// Get the source Component
				ImageComponent comp = (ImageComponent) e.getSource();
				// New SeamCarving Instance
				// Get the rows and Cols difference between the origin
				// and current
				System.out.println((comp.getHeight() - sc.height) + " : " + (comp.getWidth() - sc.width));
				while (sc.height() > comp.getHeight())
				{
					int[] horizontalSeam = sc.findHorizontalSeam();
					sc.removeHorizontalSeam(horizontalSeam);
				}

				if (sc.height() < comp.getHeight())
				{
					SeamCarver ori = new DPSeamCarver(sc);
					while (ori.height() < comp.getHeight())
					{
						int[] horizontalSeam = sc.findHorizontalSeam();
						sc.removeHorizontalSeam(horizontalSeam);
						ori.insertHorizontalSeam(horizontalSeam);
					}
					sc = ori;
				}

				while (sc.width() > comp.getWidth())
				{
					int[] verticalSeam = sc.findVerticalSeam();
					sc.removeVerticalSeam(verticalSeam);
				}

				if (sc.width() < comp.getWidth())
				{
					SeamCarver ori = new DPSeamCarver(sc);
					while (ori.width() < comp.getWidth())
					{
						// avoid finding the same seam
						int[] verticalSeam = sc.findVerticalSeam();
						sc.removeVerticalSeam(verticalSeam);
						ori.insertVerticalSeam(verticalSeam);
					}
					sc = ori;
				}
				// Set the new Image and repaint
				comp.setImage(sc.picture().getBufferedImage());
				comp.repaint();
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyPressed(KeyEvent e)
			{
				if ((e.getKeyCode() == KeyEvent.VK_O) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
				{
					System.out.println("woot!");
				}
			}

		});
	}
}

class ImageComponent extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image;

	public ImageComponent(Image img)
	{
		this.setSize(img.getWidth(null), img.getHeight(null));
		this.setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		image = img;
	}

	public void paintComponent(Graphics g)
	{
		if (image == null)
			return;

		g.drawImage(image, 0, 0, this);
	}

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image i)
	{
		this.image = i;
		this.revalidate();
	}
}