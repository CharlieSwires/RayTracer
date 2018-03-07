import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class CrossHair {

	public static void main (String args[]) throws IOException{

		int w=1024;
		int h=1024;
		BufferedImage img = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		for (int y1 = 0 ; y1 < h; y1++){
			for (int x1 = 0; x1 < w; x1++){
				int col = 0x00000000;
				img.setRGB(x1, y1, col);
			}
		}
		Graphics g = img.getGraphics();
		g.setColor(Color.GREEN);
		g.drawLine(w/2, 0, w/2, h);

		for (int y1 = -8 ; y1 <= 8; y1++){
			int x1 = w/2;
			int col = 0xff00ff00;
			int y = (y1+8)*h/(8+8);
			g.setColor(Color.GREEN);
			g.drawLine(x1-20-10*(1-Math.abs(y1)%2)-((y1==0)?10:0), y,
					x1+20+10*(1-Math.abs(y1)%2)+((y1==0)?10:0), y);
			g.drawString(""+y1, x1-40-10*(1-Math.abs(y1)%2)-((y1==0)?10:0), y);
			
		}
		String[] binaryFileName = args[0].split("[.]");
		File f = new File(binaryFileName[0]+"."+binaryFileName[1]);
		ImageIO.write(img, binaryFileName[1].toUpperCase(), f);
	
	}
}

