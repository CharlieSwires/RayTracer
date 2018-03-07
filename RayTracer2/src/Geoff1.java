import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;

import javax.imageio.ImageIO;


public class Geoff1 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		RayTracerMain rtm = new RayTracerMain();

		int i = 0;
		int x = 0;
		int y = 0;
		double intensity = 0.0;
		double newIntensity = 0.0;
		byte b = 0;
		File scenefp = null;
		File pixelsfp = null;
		File rawfp = null;
		String binaryFileName[]=args[0].split("[.]");
		int w =1000;
		int h = 600;
		w = Integer.parseInt(args[1]);
		h = Integer.parseInt(args[2]);
		boolean wireOnly = (args.length > 3 && args[3].equals("wire"))?true:false;
		Date date;
		double scale = 1e-1;
		
		for(i=0;i <360;i++){
			date = new Date();
			System.out.println("i ="+i+" date time ="+date);
			double sinth = Math.sin(Math.PI * i /180.0);
			double costh = Math.cos(Math.PI * i /180.0);
			double alpha1 = 0.0;
			double beta = Math.PI * -i /180.0;
			double gamma = 0.0;
			double iu[][]= new double[3][3];
			double iu2[][]= new double[3][3];
			double sina = Math.sin(alpha1); double cosa = Math.cos(alpha1);
			double sinb = Math.sin(beta); double cosb = Math.cos(beta);
			double sinc = Math.sin(gamma); double cosc = Math.cos(gamma);
			double Rx[][] = {{1.0,0.0,0.0},{0.0,cosa,-sina},{0.0,sina,cosa}};
			double Ry[][] = {{cosb,0.0,sinb},{0.0,1.0,0.0},{-sinb,0.0,cosb}};
			double Rz[][] = {{cosc,-sinc,0.0},{sinc, cosc,0.0},{0.0,0.0,1.0}};
			double temp[][] = new double[3][3];
			VectorAlgebra.matrix3x3Multiply(Rx, Ry, temp);
			VectorAlgebra.matrix3x3Multiply(temp,Rz,iu);
			alpha1 = 0.0;
			beta = Math.PI * -i /180.0;
			gamma = Math.PI * (-70.0) /180.0;;
			sina = Math.sin(alpha1); cosa = Math.cos(alpha1);
			sinb = Math.sin(beta); cosb = Math.cos(beta);
			sinc = Math.sin(gamma); cosc = Math.cos(gamma);
			double Rx2[][] = {{1.0,0.0,0.0},{0.0,cosa,-sina},{0.0,sina,cosa}};
			double Ry2[][] = {{cosb,0.0,sinb},{0.0,1.0,0.0},{-sinb,0.0,cosb}};
			double Rz2[][] = {{cosc,-sinc,0.0},{sinc, cosc,0.0},{0.0,0.0,1.0}};
			double temp2[][] = new double[3][3];
			VectorAlgebra.matrix3x3Multiply(Rx2, Ry2, temp2);
			VectorAlgebra.matrix3x3Multiply(temp2,Rz2,iu2);
			//generate scene2.inf
			//###########################################Red#########################################

			scenefp = new File("scene2red.inf");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

			pw.println("re ;");
			pw.println("re t;");

			pw.println("ls "+(1000.0)+", "+(1000.0)+","+1000.0+", "+40e10+";");
			pw.println("vp "+(-1000)+
					", "+0+
					",150,  "+0+",0, -150, 1,0;");
			/* refractive index */
			/* absorbance of material +ve normal side */
			/* absorbance of material -ve normal side */
			/* percentage perfect mirror */
			/* Phong smoothness value for a surface */
			/* Diffuse reflectance of the material */
			pw.println("re earth;");
			pw.println("tex 400,-400,-200,"+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,R, world.jpg, world.jpg;");
			pw.println("mp 1.4,1,1,100,50,0.4;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0)*200.0/12756.0+";");
			pw.println("clear;");
			pw.println("tex 400,-400,-200,"+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,R, Earth-Clouds2700.jpg, Earth-Clouds2700.jpg;");
			pw.println("mp 1.0,1,1,100,50,0.4;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0+1.0)*200.0/12756.0+";");
			pw.println("clear;");
			pw.println("mp 1.000277,1,1.0,100,50,0.05;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0+10.0)*200.0/12756.0+";");
			pw.println("mp 0,1,1,0,0,0.0;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0-1.0)*200.0/12756.0+";");

			pw.println("mp 1.50917,1,1,100,100,0.32;");
			pw.println("sp "+0.0+",  "+0.0+", "+(-400+360-i) +", "+100.0+";");
			pw.println("mp 0,1,1,20.0,100,0.4;");
			pw.println("ttx "+(-1000.0)+","+(-1000.0)+","+(0.0)+","                   
					+ "0,2000.0,"+(0.0)+","                                                                           
					+ "2000.0,"+(0.0)+",0,"                                                                           
					+ "R,GeoffsFloor.jpg,specular.jpg;");                                                      
			pw.println("disk "+(1000)+", "+(0)+", "+(0)+","+(0)+","+(-300)+","
					+""+1+","+0+","+0+","
					+""+0+","+1+","+0+","
					+""+0+","+0+","+1+";");
			pw.println("clt;");                                                                               
			pw.println("mp 0,1,1,0,0,0.0;");

			pw.println("tex 400,-400,-200,1,0,0, 0,1,0, 0,0,1,R, sky.jpg, sky.jpg;");
			pw.println("sky;");
			pw.println("clear;");

			pw.close();
			//###########################################Green#########################################
			scenefp = new File("scene2green.inf");
			pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

			pw.println("re test scene showing two spheres above a square plain made from two;");
			pw.println("re triangles of different shade;");



			pw.println("ls "+(1000.0)+", "+(1000.0)+","+1000.0+", "+40e10+";");
			pw.println("vp "+(-1000)+
					", "+0+
					",150,  "+0+",0, -150, 1,0;");
			/* refractive index */
			/* absorbance of material +ve normal side */
			/* absorbance of material -ve normal side */
			/* percentage perfect mirror */
			/* Phong smoothness value for a surface */
			/* Diffuse reflectance of the material */

			
			pw.println("re earth;");
			pw.println("tex 400,-400,-200,"+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,G, world.jpg, world.jpg;");
			pw.println("mp 1.4,1,1,100,50,0.4;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0)*200.0/12756.0+";");
			pw.println("clear;");
			pw.println("tex 400,-400,-200,"+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,G, Earth-Clouds2700.jpg, Earth-Clouds2700.jpg;");
			pw.println("mp 1.0,1,1,100,50,0.4;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0+1.0)*200.0/12756.0+";");
			pw.println("clear;");
			pw.println("mp 1.000277,1,1.0,100,50,0.05;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0+10.0)*200.0/12756.0+";");
			pw.println("mp 0,1,1,0,0,0.0;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0-1.0)*200.0/12756.0+";");

			pw.println("mp 1.51534,1,1,100,100,0.32;");
			pw.println("sp "+0.0+",  "+0.0+", "+(-400+360-i) +", "+100.0+";");
			pw.println("mp 0,1,1,20.0,100,0.4;");
			pw.println("ttx "+(-1000.0)+","+(-1000.0)+","+(0.0)+","                   
					+ "0,2000.0,"+(0.0)+","                                                                           
					+ "2000.0,"+(0.0)+",0,"                                                                           
					+ "G,GeoffsFloor.jpg,specular.jpg;");                                                      
			pw.println("disk "+(1000)+", "+(0)+", "+(0)+","+(0)+","+(-300)+","
					+""+1+","+0+","+0+","
					+""+0+","+1+","+0+","
					+""+0+","+0+","+1+";");
			pw.println("clt;");                                                                               
			pw.println("mp 0,1,1,0,0,0.0;");

			pw.println("tex 400,-400,-200,1,0,0, 0,1,0, 0,0,1,G, sky.jpg, sky.jpg;");
			pw.println("sky;");
			pw.println("clear;");

			pw.close();
			//##############################################Blue#################################################
			scenefp = new File("scene2blue.inf");
			pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

			pw.println("re test scene showing two spheres above a square plain made from two;");
			pw.println("re triangles of different shade;");

			pw.println("ls "+(1000.0)+", "+(1000.0)+","+1000.0+", "+40e10+";");
			pw.println("vp "+(-1000)+
					", "+0+
					",150,  "+0+",0, -150, 1,0;");
			/* refractive index */
			/* absorbance of material +ve normal side */
			/* absorbance of material -ve normal side */
			/* percentage perfect mirror */
			/* Phong smoothness value for a surface */
			/* Diffuse reflectance of the material */
			pw.println("re earth;");
			pw.println("tex 400,-400,-200,"+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,B, world.jpg, world.jpg;");
			pw.println("mp 1.4,1,1,100,50,0.4;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0)*200.0/12756.0+";");
			pw.println("clear;");
			pw.println("tex 400,-400,-200,"+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,B, Earth-Clouds2700.jpg, Earth-Clouds2700.jpg;");
			pw.println("mp 1.0,1,1,100,50,0.4;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0+1.0)*200.0/12756.0+";");
			pw.println("clear;");
			pw.println("mp 1.000277,1,1.0,100,50,0.05;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0+10.0)*200.0/12756.0+";");
			pw.println("mp 0,1,1,0,0,0.08;");
			pw.println("sp 400,-400,-200,"+(12756.0/2.0-1.0)*200.0/12756.0+";");

			pw.println("mp 1.51690,1,1.005,100,100,0.32;");
			pw.println("sp "+0.0+",  "+0.0+", "+(-400+360-i) +", "+100.0+";");
			pw.println("mp 0,1,1,20.0,100,0.4;");
			pw.println("ttx "+(-1000.0)+","+(-1000.0)+","+(0.0)+","                   
					+ "0,2000.0,"+(0.0)+","                                                                           
					+ "2000.0,"+(0.0)+",0,"                                                                           
					+ "B,GeoffsFloor.jpg,specular.jpg;");                                                      
			pw.println("disk "+(1000)+", "+(0)+", "+(0)+","+(0)+","+(-300)+","
					+""+1+","+0+","+0+","
					+""+0+","+1+","+0+","
					+""+0+","+0+","+1+";");
			pw.println("clt;");                                                                               
			pw.println("mp 0,1,1,0,0,0.0;");

			pw.println("tex 400,-400,-200,1,0,0, 0,1,0, 0,0,1,B, sky.jpg, sky.jpg;");
			pw.println("sky;");
			pw.println("clear;");

			pw.close();

			//call raytrace
			String s =  null;

			String argr[]={"scene2red.inf", "scene2red.out", ""+w, ""+h, "10"};
			double or[][] = new double [w][h];
			or = rtm.raytracer(argr,or,wireOnly);

			String argg[]={"scene2green.inf", "scene2green.out", ""+w, ""+h, "10"};
			double og[][] = new double [w][h];
			og = rtm.raytracer(argg,og,wireOnly);

			String argb[]={"scene2blue.inf", "scene2blue.out", ""+w, ""+h, "10"};
			double ob[][] = new double [w][h];
			ob = rtm.raytracer(argb,ob,wireOnly);

			/*		

    	System.out.println("java RayTracerMain scene2blue.inf scene2blue.out "+w+" "+h+" 10");
		p = Runtime.getRuntime().exec("java RayTracerMain scene2blue.inf scene2blue.out "+w+" "+h+" 10");

        stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));
        // read the output from the command
        System.out.println("Here is the standard output of the command:");
        while ((s = stdInput.readLine()) != null) {
            System.out.print(s+", ");
        }
			 */
			//atenuate scene2.out and convert to byte file


			BufferedImage img = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			for (int y1 = 0 ; y1 < h; y1++){
				for (int x1 = 0; x1 < w; x1++){
					double intensityRed = or[x1][y1];
					newIntensity = intensityRed/75.0;
					int red;
					if (newIntensity > 255){
						red = 255;
					}else{
						red = (int)newIntensity;
					}
					double intensityGreen = og[x1][y1];
					newIntensity = intensityGreen/75.0;
					int green;
					if (newIntensity > 255){
						green = 255;
					}else{
						green = (int)newIntensity;
					}
					double intensityBlue = ob[x1][y1];
					newIntensity = intensityBlue/75.0;
					int blue;
					if (newIntensity > 255){
						blue= 255;
					}else{
						blue = (int)newIntensity;
					}
					int col = (red << 16) | (green << 8) | blue;
					img.setRGB(x1, y1, col);
				}
			}
			File f = new File(binaryFileName[0]+i+"."+binaryFileName[1]);
			ImageIO.write(img, binaryFileName[1].toUpperCase(), f);
			img = null;
			System.out.println("\nWritten "+binaryFileName[0]+i+"."+binaryFileName[1]+
					" time elapsed ="+ ((new Date()).getTime()-date.getTime())/1000.0);
		}

	}
}