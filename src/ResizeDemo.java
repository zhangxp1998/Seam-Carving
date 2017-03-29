
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

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

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
//		int removeColumns = Integer.parseInt(args[1]);
//		int removeRows = Integer.parseInt(args[2]);

		StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
		System.out.println("Press enter to start carving...");
		Scanner in = new Scanner(System.in);
		in.nextLine();
		in.close();

		System.out.println("Start carving...");
//		SeamCarver sc = new DPSeamCarver(ImageIO.read(new File(args[0])));

//		Stopwatch sw = new Stopwatch();

		/*
		 * Will_start
		 */
		

		Image img = ImageIO.read(new File(args[0]));
		EventQueue.invokeLater(new Runnable()
        {
            public void run(){
                ImageFrame frame = new ImageFrame(inputImg.width(),inputImg.height());
                ImageComponent ic = new ImageComponent(img);
                ic.addComponentListener(new ComponentAdapter() {
                	@Override
                    public void componentResized(ComponentEvent e) {
                        System.out.println(e);
                        ImageComponent comp = (ImageComponent) e.getSource();
                        Image curImg = inputImg.getBufferedImage();
                        SeamCarver sc = new DPSeamCarver((BufferedImage) curImg);
                        
                        int rRow = inputImg.height() - comp.getHeight();
                        int rCol = inputImg.width() - comp.getWidth();
                        
                        if (rRow>0) for (int i = 0; i < rRow; i++)
                		{
                			int[] horizontalSeam = sc.findHorizontalSeam();
                			sc.removeHorizontalSeam(horizontalSeam);
                			System.out.println(i + ": " + Runtime.getRuntime().totalMemory() / 100_0000.0F);
                		}

                		if (rCol>0) for (int i = 0; i < rCol; i++)
                		{
                			int[] verticalSeam = sc.findVerticalSeam();
                			sc.removeVerticalSeam(verticalSeam);
                			System.out.println(i + ": " + Runtime.getRuntime().totalMemory() / 100_0000.0F);
                		}
                		comp.setImage(sc.picture().getBufferedImage());
                		comp.repaint();
                    }
				});
                frame.add(ic);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        }
        );
		
		/*
		 * Will_end
		 */
		
//		for (int i = 0; i < removeRows; i++)
//		{
//			int[] horizontalSeam = sc.findHorizontalSeam();
//			sc.removeHorizontalSeam(horizontalSeam);
//			System.out.println(i + ": " + Runtime.getRuntime().totalMemory() / 100_0000.0F);
//		}
//
//		for (int i = 0; i < removeColumns; i++)
//		{
//			int[] verticalSeam = sc.findVerticalSeam();
//			sc.removeVerticalSeam(verticalSeam);
//			System.out.println(i + ": " + Runtime.getRuntime().totalMemory() / 100_0000.0F);
//		}
//		System.out.println("Carving Done!");
//
//		Picture outputImg = sc.picture();
//
//		StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());
//
//		StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
//		inputImg.show();
//		outputImg.show();
//		outputImg.save("./output1.jpg");
	}
}

class ImageFrame extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6626001906329483589L;
	public ImageFrame(int w, int h){
        setTitle("ImageTest");
        setSize(w, h);
    }

	@Override
	public void paintComponents(Graphics g)
	{
		super.paintComponents(g);
		
	}
}


class ImageComponent extends JComponent{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Image image;
    public ImageComponent(Image img){
    	image = img;
    }
    public void paintComponent (Graphics g){
        if(image == null) return;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        g.drawImage(image, 0, 0, this);

        }
    public Image getImage(){ return image; }
    public void setImage(Image i) {
    	this.image = i;
    	this.revalidate();
    }
}