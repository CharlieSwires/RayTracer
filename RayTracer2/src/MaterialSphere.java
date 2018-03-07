import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class MaterialSphere {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double origin[] = new double[3];
	public double xv[] = new double[3];
	public double yv[] = new double[3];
	public double zv[] = new double[3];
	public char colourChannel;
	public String file = null;
	public String file2 = null;
	private BufferedImage img;
	private BufferedImage img2;
	private static final double radToDeg = 180.0/Math.PI;

	static BufferedImage[] images= new BufferedImage[20];
	static String filenames[] = new String[20];
	
	
	static BufferedImage imageior (String filename){
		int i = 0;
		for (;i < 20;i++){
			if (filenames[i] != null && !filenames[i].isEmpty() && filename.equals(filenames[i])){
				break;
			}
			else if (filenames[i] == null ||filenames[i].isEmpty())
			{
				try {
					images[i] = ImageIO.read(new File(filename));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				filenames[i]=filename;
				break;
			}
		}
		return images[i];

	}
	MaterialSphere(
			double x0, double y0, double z0,
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			String colourChannel, String file, String fileShiny){
		origin[0]=x0;
		origin[1]=y0;
		origin[2]=z0;
		xv[0]=x1;
		xv[1]=y1;
		xv[2]=z1;
		yv[0]=x2;
		yv[1]=y2;
		yv[2]=z2;
		zv[0]=x3;
		zv[1]=y3;
		zv[2]=z3;
		this.colourChannel = colourChannel.charAt(0);
		this.file = file;
		this.file2 = fileShiny;
			img = imageior(file);
			img2 = imageior(fileShiny);

	}

	public double rdiff(double normal[]) {
		double x = VectorAlgebra.dotv(normal, xv);
		double y = VectorAlgebra.dotv(normal, yv);
		double z = VectorAlgebra.dotv(normal, zv);
		double r = Math.sqrt(x*x+y*y+z*z);
		double a = Math.atan2(y,x)*radToDeg;
		double b = Math.acos(z/r)*radToDeg;
		int x1 = (int)(img.getWidth()*(a+180.0)/360.0);
		int y1 = (int)(img.getHeight()*(b)/180.0);
		if (x1<0)
			x1=0;
		if (x1>(img.getWidth()-1))
			x1=(img.getWidth()-1);
		if (y1<0)
			y1=0;
		if (y1>(img.getHeight()-1))
			y1=(img.getHeight()-1);	
		int colour = img.getRGB(x1, y1);
		Color c = new Color(colour);
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		switch(this.colourChannel){
		case 'R':
			return (double)red/255.0;
		case 'G':
			return (double)green/255.0;
		case 'B':
			return (double)blue/255.0;
		default: 
			return 0.0;
		}
	}
	public double pcntshiny(double normal[]) {
		double x = VectorAlgebra.dotv(normal, xv);
		double y = VectorAlgebra.dotv(normal, yv);
		double z = VectorAlgebra.dotv(normal, zv);
		double r = Math.sqrt(x*x+y*y+z*z);
		double a = Math.atan2(y,x)*radToDeg;
		double b = Math.acos(z/r)*radToDeg;
		int x1 = (int)(img2.getWidth()*(a+180.0)/360.0);
		int y1 = (int)(img2.getHeight()*(b)/180.0);
		if (x1<0)
			x1=0;
		if (x1>(img2.getWidth()-1))
			x1=(img2.getWidth()-1);
		if (y1<0)
			y1=0;
		if (y1>(img2.getHeight()-1))
			y1=(img2.getHeight()-1);	
		int colour = img2.getRGB(x1, y1);
		Color c = new Color(colour);
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		double intensity = (double)(red+green+blue);
		double pcentMir =(intensity>150)? 0.0: 100.0;
		if (pcentMir > 100.0){
			pcentMir=100.0;
		}else if(pcentMir< 0){
			pcentMir=0.0;
		}
		return pcentMir;

	}
}
