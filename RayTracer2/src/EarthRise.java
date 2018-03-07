import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Date;

import javax.imageio.ImageIO;


public class EarthRise {

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

		i = 30;
		{
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
			alpha1 = Math.PI * 90.0 /180.0;
			beta = Math.PI * 30.0 /180.0;
			gamma = Math.PI * (90.0) /180.0;;
			sina = Math.sin(alpha1); cosa = Math.cos(alpha1);
			sinb = Math.sin(beta); cosb = Math.cos(beta);
			sinc = Math.sin(gamma); cosc = Math.cos(gamma);
			double Rx2[][] = {{1.0,0.0,0.0},{0.0,cosa,-sina},{0.0,sina,cosa}};
			double Ry2[][] = {{cosb,0.0,sinb},{0.0,1.0,0.0},{-sinb,0.0,cosb}};
			double Rz2[][] = {{cosc,-sinc,0.0},{sinc, cosc,0.0},{0.0,0.0,1.0}};
			double temp2[][] = new double[3][3];
			VectorAlgebra.matrix3x3Multiply(Rx2, Ry2, temp2);
			VectorAlgebra.matrix3x3Multiply(temp2,Rz2,iu2);
			double alpha= 160 * Math.PI /180.0;       
			double beta2 = alpha + Math.asin(-150.0/0.384e6);
			double trans[] = {0.382e6*Math.cos(beta2),-0.382e6*Math.sin(beta2),0};
			//generate scene2.inf
			//###########################################Red#########################################

			scenefp = new File("scene2red.inf");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));
			PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter("temp.out")));

			pw.println("re test scene showing two spheres above a square plain made from two;");
			pw.println("re triangles of different shade;");

			pw.println("ls "+(-149.6e6)+
					", "+(0.0)+",0, 10e21;");
			pw.println("vp "+(2000.0+0.384e6)*Math.cos(alpha)+","+(-(0.384e6+2000.0)*Math.sin(alpha))+",0,"+
					"  "+0+",0, 0, 4,-90;");
			/* refractive index */
			/* absorbance of material +ve normal side */
			/* absorbance of material -ve normal side */
			/* percentage perfect mirror */
			/* Phong smoothness value for a surface */
			/* Diffuse reflectance of the material */
			bounding ( 0.0,  0.0,  0.0,
					0.0,  0.0,  0.0,
					0.0,  0.0, 0.0, 
					0.0,  0.0,  0.0, 
					0.0,  0.0,  0.0, 
				     pw);

			double xx = -(12756.0/2.0+10.0);
			double zz = -(12756.0/2.0+10.0);
			double xxw = (12756.0/2.0+10.0);
			double zzw = (12756.0/2.0+10.0);
			double min = -(12756.0/2.0+10.0);
			double max = (12756.0/2.0+10.0);
			bounding ( xx,  min,  zz,
				      xxw,  min,  zz,
				      xxw,  max,  zz, 
				      xx,  max,  zz, 
				      xx,  min,  zzw, 
				     pw);


			pw.println("re earth;");
			pw.println("tex 0,0,0, "+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,R, world.jpg, world.jpg;");
			pw.println("mp 1.4,1,1,100,50,0.04;");
			pw.println("sp 0,0,0,"+(12756.0/2.0)+";");
			pw.println("clear;");
			pw.println("tex 0,0,0, "+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,R, Earth-Clouds2700.jpg, Earth-Clouds2700.jpg;");
			pw.println("mp 1.0,1,1,100,50,0.04;");
			pw.println("sp 0,0,0,"+(12756.0/2.0+1.0)+";");
			pw.println("clear;");
			pw.println("mp 1.000277,1,0.9999992,100,50,0.05;");
			pw.println("sp 0,0,0,"+(12756.0/2.0+10.0)+";");

			pw.println("mp 0,1,1,0,0,0.0;");
			pw.println("sp 0,0,0,"+(12756.0/2.0-1.0)+";");
			pw.println("end;");

			pw.println("tex 0,0,0,1,0,0, 0,1,0, 0,0,1,R, sky.jpg, sky.jpg;");
			pw.println("sky;");
			pw.println("clear;");

//			//			pw.println("re moon;");
//			//			pw.println("tex "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,-1,0,0, 0,-1,0, 0,0,1,R, moon.jpg, diffuse.jpg;");
			pw.println("mp 0,1,1,0,0,0.04;");
//			//			pw.println("sp "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,"+(3475.0/2.0)+";");
//			//			pw.println("clear;");
			String line;
			InputStream fis = new FileInputStream("fred.out");
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			{
				double aa[] = new double[3];
				aa[0] = 1000.0;aa[1] = 1000.0;aa[2] = 0.0;
				double bb[] = new double[3];
				bb[0] = 2000.0;bb[1] =0.0; bb[2] =0.0;
				double cc[] = new double[3];
				cc[0] = 0.0; cc[1] =-2000.0; cc[2] =0.0;
				double dd[] = new double[3];
				double ee[] = new double[3];
				double ff[] = new double[3];
				VectorAlgebra.matrixMultiplyVect(iu2, aa, dd);
				VectorAlgebra.matrixMultiplyVect(iu2, bb, ee);
				VectorAlgebra.matrixMultiplyVect(iu2, cc, ff);
				VectorAlgebra.addv(dd, trans, aa);
				
				pw.println("ttx "+aa[0]+","+aa[1]+","+aa[2]+","                   
					+ ee[0]+","+ee[1]+","+ee[2]+","                                                                           
					+ ff[0]+","+ff[1]+","+ff[2]+","                                                                           
					+ "R,land.jpg,diffuse.jpg;");
			}
			while ((line = br.readLine()) != null) {
				if (line.startsWith("tr")){
					double aa[] = new double[3];
					double bb[] = new double[3];
					double cc[] = new double[3];
					double dd[] = new double[3];
					double ee[] = new double[3];
					double ff[] = new double[3];
				String arr[] = line.replaceAll("[tr ]","").split("[,]");
				for (int idx = 0; idx < 3; idx++){
					aa[idx] = Double.parseDouble(arr[idx ].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					bb[idx] = Double.parseDouble(arr[idx +3].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					cc[idx] = Double.parseDouble(arr[idx +6].trim().replaceAll("[;]",""));
				}
				
				VectorAlgebra.matrixMultiplyVect(iu2, aa, dd);
				VectorAlgebra.matrixMultiplyVect(iu2, bb, ee);
				VectorAlgebra.matrixMultiplyVect(iu2, cc, ff);
				VectorAlgebra.addv(dd, trans, aa);
				VectorAlgebra.addv(ee, trans, bb);
				VectorAlgebra.addv(ff, trans, cc);
				bufferedWrite(pw,"tr "+aa[0]+", "+aa[1]+", "+aa[2]+", "
						+bb[0]+", "+bb[1]+", "+bb[2]+", "
						+cc[0]+", "+cc[1]+", "+cc[2]+";");
				}else if (line.startsWith("bounding")){
					double aa[] = new double[3];
					double bb[] = new double[3];
					double cc[] = new double[3];
					double dd[] = new double[3];
					double ee[] = new double[3];
					double ff[] = new double[3];
					double gg[] = new double[3];
					double hh[] = new double[3];
					double ii[] = new double[3];
					double jj[] = new double[3];
				String arr[] = line.replaceAll("[bounding ]","").split("[,]");
				for (int idx = 0; idx < 3; idx++){
					aa[idx] = Double.parseDouble(arr[idx ].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					bb[idx] = Double.parseDouble(arr[idx +3].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					cc[idx] = Double.parseDouble(arr[idx +6].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					dd[idx] = Double.parseDouble(arr[idx +6].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					ee[idx] = Double.parseDouble(arr[idx +6].trim().replaceAll("[;]",""));
				}
				
				VectorAlgebra.matrixMultiplyVect(iu2, aa, ff);
				VectorAlgebra.matrixMultiplyVect(iu2, bb, gg);
				VectorAlgebra.matrixMultiplyVect(iu2, cc, hh);
				VectorAlgebra.matrixMultiplyVect(iu2, dd, ii);
				VectorAlgebra.matrixMultiplyVect(iu2, ee, jj);
				VectorAlgebra.addv(ff, trans, aa);
				VectorAlgebra.addv(gg, trans, bb);
				VectorAlgebra.addv(hh, trans, cc);
				VectorAlgebra.addv(ii, trans, dd);
				VectorAlgebra.addv(jj, trans, ee);
				bufferedWrite(pw,"bounding "+aa[0]+", "+aa[1]+", "+aa[2]+", "
						+bb[0]+", "+bb[1]+", "+bb[2]+", "
						+cc[0]+", "+cc[1]+", "+cc[2]+", "+
						+dd[0]+", "+dd[1]+", "+dd[2]+", "+
						+ee[0]+", "+ee[1]+", "+ee[2]+
						";");
					
				}else if (line.startsWith("end")){
					bufferedWrite(pw,"end;");
	
				}

			}
//			pw.println("mp 1.3,1,1,100,50,0.04;");

			bufferedWrite(pw,"flush");
			pw.println("clt;");                                                                               
			pw.println("end;");
			br.close();
			isr.close();
			fis.close();
			fis = new FileInputStream("fred.out");
			isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			br = new BufferedReader(isr);
			pw.close();
			//###########################################Green#########################################
			scenefp = new File("scene2green.inf");
			pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

			pw.println("re test scene showing two spheres above a square plain made from two;");
			pw.println("re triangles of different shade;");

			pw.println("ls "+(-149.6e6)+
					", "+(0.0)+",0, 10e21;");
			pw.println("vp "+(2000.0+0.384e6)*Math.cos(alpha)+","+(-(0.384e6+2000.0)*Math.sin(alpha))+",0,"+
					"  "+0+",0, 0, 4,-90;");
			bounding ( 0.0,  0.0,  0.0,
					0.0,  0.0,  0.0,
					0.0,  0.0, 0.0, 
					0.0,  0.0,  0.0, 
					0.0,  0.0,  0.0, 
				     pw);

			xx = -(12756.0/2.0+10.0);
			zz = -(12756.0/2.0+10.0);
			xxw = (12756.0/2.0+10.0);
			zzw = (12756.0/2.0+10.0);
			min = -(12756.0/2.0+10.0);
			max = (12756.0/2.0+10.0);

			bounding ( xx,  min,  zz,
				      xxw,  min,  zz,
				      xxw,  max,  zz, 
				      xx,  max,  zz, 
				      xx,  min,  zzw, 
				     pw);



			pw.println("re earth;");
			pw.println("tex 0,0,0, "+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,G, world.jpg, world.jpg;");
			pw.println("mp 1.4,1,1,100,50,0.04;");
			pw.println("sp 0,0,0,"+(12756.0/2.0)+";");
			pw.println("clear;");
			pw.println("tex 0,0,0,"+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,G, Earth-Clouds2700.jpg, Earth-Clouds2700.jpg;");
			pw.println("mp 1.0,1,1,100,50,0.04;");
			pw.println("sp 0,0,0,"+(12756.0/2.0+1.0)+";");
			pw.println("clear;");
			pw.println("mp 1.000277,1,0.9999992,100,50,0.05;");
			pw.println("sp 0,0,0,"+(12756.0/2.0+10.0)+";");

			pw.println("mp 0,1,1,0,0,0.0;");
			pw.println("sp 0,0,0,"+(12756.0/2.0-1.0)+";");
			pw.println("end;");

			pw.println("tex 0,0,0,1,0,0, 0,1,0, 0,0,1,G, sky.jpg, sky.jpg;");
			pw.println("sky;");
			pw.println("clear;");

//			//			pw.println("re moon;");
//			//			pw.println("tex "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,-1,0,0, 0,-1,0, 0,0,1,G, moon.jpg, diffuse.jpg;");
			pw.println("mp 0,1,1,0,0,0.04;");
//			//			pw.println("sp "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,"+(3475.0/2.0)+";");
//			//			pw.println("clear;");
			{
				double aa[] = new double[3];
				aa[0] = 1000.0;aa[1] = 1000.0;aa[2] = 0.0;
				double bb[] = new double[3];
				bb[0] = 2000.0;bb[1] =0.0; bb[2] =0.0;
				double cc[] = new double[3];
				cc[0] = 0.0; cc[1] =-2000.0; cc[2] =0.0;
				double dd[] = new double[3];
				double ee[] = new double[3];
				double ff[] = new double[3];
				VectorAlgebra.matrixMultiplyVect(iu2, aa, dd);
				VectorAlgebra.matrixMultiplyVect(iu2, bb, ee);
				VectorAlgebra.matrixMultiplyVect(iu2, cc, ff);
				VectorAlgebra.addv(dd, trans, aa);
				
				pw.println("ttx "+aa[0]+","+aa[1]+","+aa[2]+","                   
					+ ee[0]+","+ee[1]+","+ee[2]+","                                                                           
					+ ff[0]+","+ff[1]+","+ff[2]+","                                                                           
					+ "G,land.jpg,diffuse.jpg;");
			}
			while ((line = br.readLine()) != null) {
				if (line.startsWith("tr")){
					double aa[] = new double[3];
					double bb[] = new double[3];
					double cc[] = new double[3];
					double dd[] = new double[3];
					double ee[] = new double[3];
					double ff[] = new double[3];
				String arr[] = line.replaceAll("[tr ]","").split("[,]");
				for (int idx = 0; idx < 3; idx++){
					aa[idx] = Double.parseDouble(arr[idx ].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					bb[idx] = Double.parseDouble(arr[idx +3].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					cc[idx] = Double.parseDouble(arr[idx +6].trim().replaceAll("[;]",""));
				}
				
				VectorAlgebra.matrixMultiplyVect(iu2, aa, dd);
				VectorAlgebra.matrixMultiplyVect(iu2, bb, ee);
				VectorAlgebra.matrixMultiplyVect(iu2, cc, ff);
				VectorAlgebra.addv(dd, trans, aa);
				VectorAlgebra.addv(ee, trans, bb);
				VectorAlgebra.addv(ff, trans, cc);
				bufferedWrite(pw,"tr "+aa[0]+", "+aa[1]+", "+aa[2]+", "
						+bb[0]+", "+bb[1]+", "+bb[2]+", "
						+cc[0]+", "+cc[1]+", "+cc[2]+";");
				}else if (line.startsWith("bounding")){
					double aa[] = new double[3];
					double bb[] = new double[3];
					double cc[] = new double[3];
					double dd[] = new double[3];
					double ee[] = new double[3];
					double ff[] = new double[3];
					double gg[] = new double[3];
					double hh[] = new double[3];
					double ii[] = new double[3];
					double jj[] = new double[3];
				String arr[] = line.replaceAll("[bounding ]","").split("[,]");
				for (int idx = 0; idx < 3; idx++){
					aa[idx] = Double.parseDouble(arr[idx ].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					bb[idx] = Double.parseDouble(arr[idx +3].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					cc[idx] = Double.parseDouble(arr[idx +6].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					dd[idx] = Double.parseDouble(arr[idx +6].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					ee[idx] = Double.parseDouble(arr[idx +6].trim().replaceAll("[;]",""));
				}
				
				VectorAlgebra.matrixMultiplyVect(iu2, aa, ff);
				VectorAlgebra.matrixMultiplyVect(iu2, bb, gg);
				VectorAlgebra.matrixMultiplyVect(iu2, cc, hh);
				VectorAlgebra.matrixMultiplyVect(iu2, dd, ii);
				VectorAlgebra.matrixMultiplyVect(iu2, ee, jj);
				VectorAlgebra.addv(ff, trans, aa);
				VectorAlgebra.addv(gg, trans, bb);
				VectorAlgebra.addv(hh, trans, cc);
				VectorAlgebra.addv(ii, trans, dd);
				VectorAlgebra.addv(jj, trans, ee);
				bufferedWrite(pw,"bounding "+aa[0]+", "+aa[1]+", "+aa[2]+", "
						+bb[0]+", "+bb[1]+", "+bb[2]+", "
						+cc[0]+", "+cc[1]+", "+cc[2]+", "+
						+dd[0]+", "+dd[1]+", "+dd[2]+", "+
						+ee[0]+", "+ee[1]+", "+ee[2]+
						";");
					
				}else if (line.startsWith("end")){
					bufferedWrite(pw,"end;");
	
				}

			}
//			pw.println("mp 1.3,1,1,100,50,0.04;");

			bufferedWrite(pw,"flush");
			pw.println("clt;");                                                                               
			pw.println("end;");
			br.close();
			isr.close();
			fis.close();
			fis = new FileInputStream("fred.out");
			isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			br = new BufferedReader(isr);

			pw.close();
			//##############################################Blue#################################################
			scenefp = new File("scene2blue.inf");
			pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

			pw.println("re test scene showing two spheres above a square plain made from two;");
			pw.println("re triangles of different shade;");

			pw.println("ls "+(-149.6e6)+
					", "+(0.0)+",0, 10e21;");
			pw.println("vp "+(2000.0+0.384e6)*Math.cos(alpha)+","+(-(0.384e6+2000.0)*Math.sin(alpha))+",0,"+
					"  "+0+",0, 0, 4,-90;");
			bounding ( 0.0,  0.0,  0.0,
					0.0,  0.0,  0.0,
					0.0,  0.0, 0.0, 
					0.0,  0.0,  0.0, 
					0.0,  0.0,  0.0, 
				     pw);

			xx = -(12756.0/2.0+10.0);
			zz = -(12756.0/2.0+10.0);
			xxw = (12756.0/2.0+10.0);
			zzw = (12756.0/2.0+10.0);
			min = -(12756.0/2.0+10.0);
			max = (12756.0/2.0+10.0);

			bounding ( xx,  min,  zz,
				      xxw,  min,  zz,
				      xxw,  max,  zz, 
				      xx,  max,  zz, 
				      xx,  min,  zzw, 
				     pw);


			pw.println("re earth;");
			pw.println("tex 0,0,0,"+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,B, world.jpg, world.jpg;");
			pw.println("mp 1.4,1,1,100,50,0.04;");
			pw.println("sp 0,0,0,"+(12756.0/2.0)+";");
			pw.println("clear;");
			pw.println("tex 0,0,0,"+Math.cos((Math.PI *(double)i*1.0)/180.0)+","+Math.sin((Math.PI *(double)i*1.0)/180.0)+",0, "+(-Math.sin((Math.PI *(double)i*1.0)/180.0))+","+Math.cos((Math.PI *(double)i*1.0)/180.0)+",0, 0,0,1,B, Earth-Clouds2700.jpg, Earth-Clouds2700.jpg;");
			pw.println("mp 1.0,1,1,100,50,0.04;");
			pw.println("sp 0,0,0,"+(12756.0/2.0+1.0)+";");
			pw.println("clear;");
			pw.println("mp 1.000277,1,1.0,100,50,0.05;");
			pw.println("sp 0,0,0,"+(12756.0/2.0+10.0)+";");

			pw.println("mp 0,1,1,0,0,0.008;");
			pw.println("sp 0,0,0,"+(12756.0/2.0-1.0)+";");
			pw.println("end;");
			
			pw.println("tex 0,0,0,1,0,0, 0,1,0, 0,0,1,B, sky.jpg, sky.jpg;");
			pw.println("sky;");
			pw.println("clear;");


//			//			pw.println("re moon;");
//			//			pw.println("tex "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,-1,0,0, 0,-1,0, 0,0,1,B, moon.jpg, diffuse.jpg;");
			pw.println("mp 0,1,1,0,0,0.04;");
//			//			pw.println("sp "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,"+(3475.0/2.0)+";");
//			//			pw.println("clear;");
			{
				double aa[] = new double[3];
				aa[0] = 1000.0;aa[1] = 1000.0;aa[2] = 0.0;
				double bb[] = new double[3];
				bb[0] = 2000.0;bb[1] =0.0; bb[2] =0.0;
				double cc[] = new double[3];
				cc[0] = 0.0; cc[1] =-2000.0; cc[2] =0.0;
				double dd[] = new double[3];
				double ee[] = new double[3];
				double ff[] = new double[3];
				VectorAlgebra.matrixMultiplyVect(iu2, aa, dd);
				VectorAlgebra.matrixMultiplyVect(iu2, bb, ee);
				VectorAlgebra.matrixMultiplyVect(iu2, cc, ff);
				VectorAlgebra.addv(dd, trans, aa);
				
				pw.println("ttx "+aa[0]+","+aa[1]+","+aa[2]+","                   
					+ ee[0]+","+ee[1]+","+ee[2]+","                                                                           
					+ ff[0]+","+ff[1]+","+ff[2]+","                                                                           
					+ "B,land.jpg,diffuse.jpg;");
			}
			while ((line = br.readLine()) != null) {
				if (line.startsWith("tr")){
					double aa[] = new double[3];
					double bb[] = new double[3];
					double cc[] = new double[3];
					double dd[] = new double[3];
					double ee[] = new double[3];
					double ff[] = new double[3];
				String arr[] = line.replaceAll("[tr ]","").split("[,]");
				for (int idx = 0; idx < 3; idx++){
					aa[idx] = Double.parseDouble(arr[idx ].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					bb[idx] = Double.parseDouble(arr[idx +3].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					cc[idx] = Double.parseDouble(arr[idx +6].trim().replaceAll("[;]",""));
				}
				
				VectorAlgebra.matrixMultiplyVect(iu2, aa, dd);
				VectorAlgebra.matrixMultiplyVect(iu2, bb, ee);
				VectorAlgebra.matrixMultiplyVect(iu2, cc, ff);
				VectorAlgebra.addv(dd, trans, aa);
				VectorAlgebra.addv(ee, trans, bb);
				VectorAlgebra.addv(ff, trans, cc);
				bufferedWrite(pw,"tr "+aa[0]+", "+aa[1]+", "+aa[2]+", "
						+bb[0]+", "+bb[1]+", "+bb[2]+", "
						+cc[0]+", "+cc[1]+", "+cc[2]+";");
				}else if (line.startsWith("bounding")){
					double aa[] = new double[3];
					double bb[] = new double[3];
					double cc[] = new double[3];
					double dd[] = new double[3];
					double ee[] = new double[3];
					double ff[] = new double[3];
					double gg[] = new double[3];
					double hh[] = new double[3];
					double ii[] = new double[3];
					double jj[] = new double[3];
				String arr[] = line.replaceAll("[bounding ]","").split("[,]");
				for (int idx = 0; idx < 3; idx++){
					aa[idx] = Double.parseDouble(arr[idx ].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					bb[idx] = Double.parseDouble(arr[idx +3].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					cc[idx] = Double.parseDouble(arr[idx +6].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					dd[idx] = Double.parseDouble(arr[idx +6].trim());
				}
				for (int idx = 0; idx < 3; idx++){
					ee[idx] = Double.parseDouble(arr[idx +6].trim().replaceAll("[;]",""));
				}
				
				VectorAlgebra.matrixMultiplyVect(iu2, aa, ff);
				VectorAlgebra.matrixMultiplyVect(iu2, bb, gg);
				VectorAlgebra.matrixMultiplyVect(iu2, cc, hh);
				VectorAlgebra.matrixMultiplyVect(iu2, dd, ii);
				VectorAlgebra.matrixMultiplyVect(iu2, ee, jj);
				VectorAlgebra.addv(ff, trans, aa);
				VectorAlgebra.addv(gg, trans, bb);
				VectorAlgebra.addv(hh, trans, cc);
				VectorAlgebra.addv(ii, trans, dd);
				VectorAlgebra.addv(jj, trans, ee);
				bufferedWrite(pw,"bounding "+aa[0]+", "+aa[1]+", "+aa[2]+", "
						+bb[0]+", "+bb[1]+", "+bb[2]+", "
						+cc[0]+", "+cc[1]+", "+cc[2]+", "+
						+dd[0]+", "+dd[1]+", "+dd[2]+", "+
						+ee[0]+", "+ee[1]+", "+ee[2]+
						";");
					
				}else if (line.startsWith("end")){
					bufferedWrite(pw,"end;");
	
				}

			}
//			pw.println("mp 1.3,1,1,100,50,0.04;");
			bufferedWrite(pw,"flush");
			pw.println("clt;");                                                                               
			pw.println("end;");
			
			br.close();
			isr.close();
			fis.close();

			pw.close();

			//call raytrace
			String s =  null;

			String argr[]={"scene2red.inf", "scene2red.out", ""+w, ""+h, "5"};
			double or[][] = new double [w][h];
			or = rtm.raytracer(argr,or,wireOnly);

			String argg[]={"scene2green.inf", "scene2green.out", ""+w, ""+h, "5"};
			double og[][] = new double [w][h];
			og = rtm.raytracer(argg,og,wireOnly);

			String argb[]={"scene2blue.inf", "scene2blue.out", ""+w, ""+h, "5"};
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
	private static final int MAX_BUF_LENGTH = 3+2;
	private static String[] stringCircularBuf = new String[MAX_BUF_LENGTH];
	private static int stringIndex = 0;
	private static void bufferedWrite(PrintWriter pw, String string) {
		if(string.equals("flush")){
			for (int index = 0; index<MAX_BUF_LENGTH;index++){
				stringCircularBuf[stringIndex]= "";
				int threeAway = stringIndex + 1;
				if (threeAway >= MAX_BUF_LENGTH ) threeAway = 0;
				pw.println(stringCircularBuf[threeAway]!= null?stringCircularBuf[threeAway]:"");
				stringIndex++;
				if (stringIndex >=MAX_BUF_LENGTH)stringIndex = 0;
			}
		}else{
			stringCircularBuf[stringIndex]= string;
			int threeAway = stringIndex + 1;
			if (threeAway >= MAX_BUF_LENGTH) threeAway = 0;
			pw.println(stringCircularBuf[threeAway]!= null?stringCircularBuf[threeAway]:"");
			stringIndex++;
			if (stringIndex >= MAX_BUF_LENGTH)stringIndex = 0;
		}

	}
	static void bounding (double x1, double y1, double z1,
		     double x2, double y2, double z2,
		     double x3, double y3, double z3, 
		     double x4, double y4, double z4, 
		     double x5, double y5, double z5, 
		     PrintWriter out)
	{
		double scale = 1.0;
		out.println("bounding "+ x1 * scale + ", " +y1 * scale + ", " + z1 * scale  + ", " +
				+ x2 * scale + ", " +y2 * scale + ", " + z2 * scale  + ", " +
				+ x3 * scale + ", " +y3 * scale + ", " + z3 * scale  + ", " +
				+ x4 * scale + ", " +y4 * scale + ", " + z4 * scale  + ", " +
				+ x5 * scale + ", " +y5 * scale + ", " + z5 * scale  +
		";");
	}

}