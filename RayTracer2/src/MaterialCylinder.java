import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MaterialCylinder {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double origin[] = new double[3];
	public double maxz, minz;
	public double xv[] = new double[3];
	public double yv[] = new double[3];
	public double zv[] = new double[3];
	public char colourChannel;
	public String file = null;
	public String file2 = null;
	private BufferedImage img;
	private BufferedImage img2;
	private static final double radToDeg = 180.0/Math.PI;
	private double uxInv[] = new double [3];
	private double uyInv[] = new double [3];
	private double uzInv[] = new double [3];
	private double ux[] = new double [3];
	private double uy[] = new double [3];
	private double uz[] = new double [3];
	private double iuInv[][] = new double[3][3];
	private double iu[][] = new double[3][3];
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
	MaterialCylinder(
			double x0, double y0, double z0,
			double maxz, double minz,
			double alpha, double beta, double gamma,
			String colourChannel, String file, String fileShiny){
		origin[0]=x0;
		origin[1]=y0;
		origin[2]=z0;
		this.maxz = maxz;
		this.minz = minz;

		double sina = Math.sin(alpha); double cosa = Math.cos(alpha);
		double sinb = Math.sin(beta); double cosb = Math.cos(beta);
		double sinc = Math.sin(gamma); double cosc = Math.cos(gamma);
		double Rx[][] = {{1.0,0.0,0.0},{0.0,cosa,-sina},{0.0,sina,cosa}};
		double Ry[][] = {{cosb,0.0,sinb},{0.0,1.0,0.0},{-sinb,0.0,cosb}};
		double Rz[][] = {{cosc,-sinc,0.0},{sinc, cosc,0.0},{0.0,0.0,1.0}};
		double temp[][] = new double[3][3];
		VectorAlgebra.matrix3x3Multiply(Rz, Ry, temp);
		VectorAlgebra.matrix3x3Multiply(temp,Rx,iu);
		double [] txv = {1.0,0.0,0.0};
//		VectorAlgebra.matrixMultiplyVect(iu, txv, ux);
		VectorAlgebra.copyv(ux, txv);
		double [] tyv = {0.0,1.0,0.0};
//		VectorAlgebra.matrixMultiplyVect(iu, tyv, uy);
		VectorAlgebra.copyv(uy, tyv);
		double [] tzv = {0.0,0.0,1.0};
//		VectorAlgebra.matrixMultiplyVect(iu, tzv, uz);
		VectorAlgebra.copyv(uz, tzv);
		double tempv[] = new double[3];
		sina = Math.sin(-alpha); cosa = Math.cos(-alpha);
		sinb = Math.sin(-beta); cosb = Math.cos(-beta);
		sinc = Math.sin(-gamma); cosc = Math.cos(-gamma);
		double RxInv[][] = {{1.0,0.0,0.0},{0.0,cosa,-sina},{0.0,sina,cosa}};
		double RyInv[][] = {{cosb,0.0,sinb},{0.0,1.0,0.0},{-sinb,0.0,cosb}};
		double RzInv[][] = {{cosc,-sinc,0.0},{sinc, cosc,0.0},{0.0,0.0,1.0}};
		VectorAlgebra.matrix3x3Multiply(RxInv, RyInv, temp);
		VectorAlgebra.matrix3x3Multiply(temp,RzInv,iuInv);
		VectorAlgebra.matrixMultiplyVect(iuInv, txv, uxInv);
		VectorAlgebra.matrixMultiplyVect(iuInv, tyv, uyInv);
		VectorAlgebra.matrixMultiplyVect(iuInv, tzv, uzInv);
		xv = uxInv;
		yv = uyInv;
		zv = uzInv;
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
//		double r = Math.sqrt(x*x+y*y);
		double a = Math.atan2(y,x)*radToDeg;
		double b = (-z + minz) / (maxz-minz);
		int x1 = (int)(img.getWidth()*(a+180.0)/360.0);
		int zint = (int)(img.getHeight()*(b));
		int y1;
		if (zint < 0)
			y1 = ((int)(img.getHeight() - ((- zint) % img.getHeight())));
		else
			y1 = ((int) zint % img.getHeight());
		while (x1<0){
			x1+=img.getWidth();
		}
		while (x1>(img.getWidth()-1)){
			x1-=img.getWidth();
		}
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
//		double r = Math.sqrt(x*x+y*y);
		double a = Math.atan2(y,x)*radToDeg;
		double b = (-z + minz) / (maxz-minz);
		int x1 = (int)(img2.getWidth()*(a+180.0)/360.0);
		int zint = (int)(img2.getHeight()*(b));
		int y1;
		if (zint < 0)
			y1 = ((int)(img2.getHeight() - ((- zint) % img2.getHeight())));
		else
			y1 = ((int) zint % img2.getHeight());
		while (x1<0){
			x1+=img2.getWidth();
		}
		while (x1>(img2.getWidth()-1)){
			x1-=img2.getWidth();
		}
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
