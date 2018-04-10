import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SpriteSheet {

	public static void main (String args[]) throws IOException{
		int numberOfPictures = 0;
		if (args.length != 5){
			System.out.println("java SpriteSheet filenameRootWithoutNumbers outFilename widthPixels widthInSprites isSpriteWidth");
		}else{
			//ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();
			System.out.println("Pre-read of files");
			for (int i = 1;;i++){
				try {
					BufferedImage image= null;
					image = ImageIO.read(new File(args[0].split("[.]")[0]+padNumber(i)+"."+args[0].split("[.]")[1]));
					//list.add(image);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					numberOfPictures = i -1;
					break;
				}	
			}
			int w=0;
			int h=0;
			int rows;
			System.out.println("numberOfPictures:"+numberOfPictures);
			if (args[4].equals("false")){
				w = Integer.parseInt(args[2]);
				rows = numberOfPictures/Integer.parseInt(args[3]);
				int remainder = numberOfPictures%Integer.parseInt(args[3]);
				rows = remainder > 0? 1 + rows: rows;
				h = (int)((double)w /Integer.parseInt(args[3]))*rows; 
			}else{
				w = Integer.parseInt(args[2])*Integer.parseInt(args[3]);
				rows = numberOfPictures/Integer.parseInt(args[3]);
				int remainder = numberOfPictures%Integer.parseInt(args[3]);
				rows = remainder > 0? 1 + rows: rows;
				h = (int)(Integer.parseInt(args[2]))*rows; 
			}
			System.out.println("WxH:"+w+"x"+h+" ColumnsxRows:"+Integer.parseInt(args[3])+"x"+rows);

			BufferedImage img = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			for (int y = 0; y < h; y++){
				for (int x = 0; x < w;x++){
					img.setRGB(x, y, 0);
				}
			}
			System.out.println("Copying pictures to sprite sheet");
			int spriteWidth = w /Integer.parseInt(args[3]);
			for (int i = 0;i < numberOfPictures;i++){
				BufferedImage image= null;
				image = ImageIO.read(new File(args[0].split("[.]")[0]+padNumber(i+1)+"."+args[0].split("[.]")[1]));

				for (int y1 = 0 ; y1 < image.getHeight(); y1++){
					for (int x1 = 0; x1 < image.getWidth(); x1++){
						int col = image.getRGB(x1, y1);

						img.setRGB(
								(int)(spriteWidth * (i%Integer.parseInt(args[3]))+(spriteWidth*(double)x1)/image.getWidth()), 
								(int)(spriteWidth * (int)(i/Integer.parseInt(args[3]))+(spriteWidth*(double)y1)/image.getHeight()), 
								col);
					}
				}
				if (i%10 == 0)System.out.print("\n"+padNumber(i+1));
				System.out.print(".");
			}


			String[] binaryFileName = args[1].split("[.]");
			File f = new File(binaryFileName[0]+"."+binaryFileName[1]);
			ImageIO.write(img, binaryFileName[1].toUpperCase(), f);
			System.out.println("\n"+binaryFileName[0]+"."+binaryFileName[1]+" written");
		}
	}
	static String padNumber(int i){
		if (i < 10){
			return "000"+i;
		}else if (i < 100){
			return "00"+i;
		}else if (i < 1000){
			return "0"+i;
		}else {
			return ""+i;
		}
	}
}