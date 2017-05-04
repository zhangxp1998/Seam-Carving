
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
	private SeamCarver sc;
	private ImageComponent ic;

	public ImageFrame(BufferedImage img)
	{
		sc = new DPSeamCarver(img);
		setTitle("ImageTest");
		ic = new ImageComponent(img, sc);

		add(ic);
		pack();
		ic.addComponentListener(new ComponentAdapter()
		{

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
					ic.sc = ori;
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
					ic.sc = ori;
				}
				// Set the new Image and repaint
				comp.setImage(sc.getBufferedImage());
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
					FileDialog fd = new FileDialog(ImageFrame.this, "Choose Image", FileDialog.LOAD);
					fd.setVisible(true);
					String absPath = fd.getDirectory() + fd.getFile();
					System.out.println(absPath);
					try
					{
						sc = new DPSeamCarver(ImageIO.read(new File(absPath)));
						ic.setImage(sc.getBufferedImage());
						ic.repaint();
						ImageFrame.this.pack();
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_R)
				{
					int n = 0;
					while (sc.hasUnwantedPixels())
					{
						int[] verticalSeam = sc.findVerticalSeam();
						sc.removeVerticalSeam(verticalSeam);
						n++;
					}

					SeamCarver ori = new DPSeamCarver(sc);
					for (int i = 0; i < n; i++)
					{
						// avoid finding the same seam
						int[] verticalSeam = sc.findVerticalSeam();
						sc.removeVerticalSeam(verticalSeam);
						ori.insertVerticalSeam(verticalSeam);
					}
					sc = ori;
					ic.sc = ori;

					ic.setImage(sc.getBufferedImage());
					ImageFrame.this.pack();
					ic.repaint();
				}
				System.out.println(e.getKeyChar() + " : " + e.getKeyCode());
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
	public SeamCarver sc;
	private final int R = 20;

	public ImageComponent(Image img, SeamCarver sc)
	{
		this.sc = sc;
		this.setSize(img.getWidth(null), img.getHeight(null));
		this.setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				Graphics g = ImageComponent.this.getGraphics();
				g.setColor(new Color(255, 0, 0, 90));
				g.fillOval(e.getX() - R / 2, e.getY() - R / 2, R, R);
				ImageComponent.this.sc.reduceWeight(e.getX(), e.getY(), R);
			}

		});
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				System.out.println("ImageComponent.ImageComponent(...).new MouseAdapter() {...}.mouseClicked()");
				Graphics g = ImageComponent.this.getGraphics();
				g.setColor(new Color(255, 0, 0, 127));
				g.fillOval(e.getX(), e.getY(), 20, 20);
				ImageComponent.this.sc.reduceWeight(e.getX(), e.getY(), R);
			}
		});
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
		this.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
	}
}