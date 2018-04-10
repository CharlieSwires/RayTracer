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


public class RayTracerUI {

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

		for (i = 0; i < 360; i++){
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

			pw.println("re test scene showing two spheres above a square plain made from two;");
			pw.println("re triangles of different shade;");

			pw.println("ls "+(-149.6e6)+
					", "+(0.0)+",0, 10e21;");
			pw.println("vp "+
					(-(12756.0/2.0+30000)-(20000*scale) * Math.sin((Math.PI *((double)i/20.0+90))/180.0))+
					", "+(-(20000*scale) * Math.cos((Math.PI *((double)i/20.0+90))/180.0))+
					",0,  "+(-(12756.0/2.0+30000))+",0, 0, 4,0;");
			/* refractive index */
			/* absorbance of material +ve normal side */
			/* absorbance of material -ve normal side */
			/* percentage perfect mirror */
			/* Phong smoothness value for a surface */
			/* Diffuse reflectance of the material */
			bounding(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,pw);
			double xx = (-12756.0/2.0-30000)-660.0*scale;
			double zz = -660.0*scale;
			double xxw =  (-12756.0/2.0-30000)+660.0*scale;
			double zzw = +660.0*scale;
			double min = (-210-50-200)*1*scale;
			double max = (+210+50+200)*1*scale;
			bounding ( xx,  min,  zz,
					xxw,  min,  zz,
					xxw,  max,  zz, 
					xx,  max,  zz, 
					xx,  min,  zzw, 
					pw);

			pw.println("re satelite;");
			pw.println("ttx "+(-(12756.0/2.0+30000))+","+(-210-50-200)*1*scale+","+(-400*scale)+","                   
					+ "0,"+(200*scale)+",0,"                                                                           
					+ "0,0,"+(800*scale)+","                                                                           
					+ "R,SolarPanel.jpg,specular.jpg;");                                                      
			pw.println("mp 0,1,1,10.0,1000,0.04;");                                                           
			pw.println("tr "                                                                                  
					+(-12756.0/2.0-30000)+","+(-210-50-200)*1*scale+","+400*scale+","                                 
					+(-12756.0/2.0-30000)+","+(-210-50)*1*scale+","+400*scale+","                                     
					+(-12756.0/2.0-30000)+","+(-210-50)*1*scale+","+(-400*scale)+";");                                
			pw.println("tr "                                                                                  
					+(-12756.0/2.0-30000)+","+(-210-50)*1*scale+","+(-400*scale)+","                                  
					+(-12756.0/2.0-30000)+","+(-210-50-200)*1*scale+","+(-400*scale)+","                              
					+(-12756.0/2.0-30000)+","+(-210-50-200)*1*scale+","+400*scale+";");                               
			pw.println("clt;");                                                                               
			pw.println("ttx "+(-(12756.0/2.0+30000))+","+(+210+50)*1*scale+","+(-400*scale)+","                       
					+ "0,"+(200*scale)+",0,"                                                         			      
					+ "0,0,"+(800*scale)+","                                                         			      
					+ "R,SolarPanel.jpg,specular.jpg;");                                     			      
			pw.println("tr "                                                                                  
					+(-12756.0/2.0-30000)+","+(+210+50)*1*scale+","+400*scale+","                                     
					+(-12756.0/2.0-30000)+","+(+210+50+200)*1*scale+","+400*scale+","                                 
					+(-12756.0/2.0-30000)+","+(+210+50+200)*1*scale+","+(-400*scale)+";");                            
			pw.println("tr "                                                                                  
					+(-12756.0/2.0-30000)+","+(+210+50+200)*1*scale+","+(-400*scale)+","                              
					+(-12756.0/2.0-30000)+","+(+210+50)*1*scale+","+(-400*scale)+","                                  
					+(-12756.0/2.0-30000)+","+(+210+50)*1*scale+","+400*scale+";");                                   
			pw.println("clt;"); 
			//back
			pw.println("ttx "+(-(12756.0/2.0+30000-2*scale))+","+(-210-50)*1*scale+","+(-400*scale)+","
					+ "0,-"+(200*scale)+",0,"
					+ "0,0,"+(800*scale)+","
					+ "R,SolarPanelBack.jpg,SolarPanelBackSpecular.jpg;");
			pw.println("mp 0,1,1,90.0,100,0.04;");                                                                        
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50-200)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50)*1*scale+","+400*scale+";");
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50-200)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50-200)*1*scale+","+(-400*scale)+";");
			pw.println("clt;"); 
			pw.println("ttx "+(-(12756.0/2.0+30000-2*scale))+","+(+210+50+200)*1*scale+","+(-400*scale)+"," 
					+ "0,-"+(200*scale)+",0,"                                                         			
					+ "0,0,"+(800*scale)+","                                                         			
					+ "R,SolarPanelBack.jpg,SolarPanelBackSpecular.jpg;");                                     			
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50+200)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50+200)*1*scale+","+400*scale+";");
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50+200)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50)*1*scale+","+(400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50)*1*scale+","+(-400*scale)+";");
			pw.println("clt;");
			pw.println("mp 0,1,1,90.0,100,0.32;");
			pw.println("tcx "+(-(12756.0/2.0+30000))+",0,0,"+(660.0*scale)+","+(515.0*scale-660.0*scale)+","+(0.0)+","+(Math.PI * i /180.0)+","+(Math.PI * -120 /180.0)+" ,R, NarrowTube.jpg, specular.jpg;");
			pw.println("cyl "+(151.0*scale)+",  "+(-(12756.0/2.0+30000))+",0,0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+
					", "+0+",0.0,"+1+", "+(660.0*scale)+";");
			pw.println("clc;");
			pw.println("tcx "+(-(12756.0/2.0+30000))+",0,0, "+(+515.0*scale-660.0*scale)+","+(-660.0*scale)+","+(0.0)+","+(Math.PI * i /180.0)+","+(Math.PI * 90 /180.0)+", R, BottomTube.jpg, specular.jpg;");
			pw.println("cyl "+(210.0*scale)+",  "+(-(12756.0/2.0+30000))+",0, 0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(-660.0*scale)+
					","+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+";");
			pw.println("clc;");

			double posnAftShroudCorner[]={-210*scale,-210*scale,-660.0*scale};
			double vectYAftShroud[]= {0,210*scale*2,0};
			double vectXAftShroud[]= {210*scale*2,0,0};
			double aftSC[]= new double[3];
			VectorAlgebra.matrixMultiplyVect(iu, posnAftShroudCorner, aftSC);
			double yVec[]= new double[3];
			double xVec[]= new double[3];
			VectorAlgebra.matrixMultiplyVect(iu, vectYAftShroud, yVec);
			VectorAlgebra.matrixMultiplyVect(iu, vectXAftShroud, xVec);
			double posnMidShroudCorner[]={-210*scale,-210*scale,515.0*scale-660.0*scale};
			double midSC[]= new double[3];
			VectorAlgebra.matrixMultiplyVect(iu2, posnMidShroudCorner, midSC);
			double posnAftShroudDisk[] = {0,0,-660*scale};
			double posnMidShroudDisk[] = {0,0,515.0*scale-660*scale};
			double xDir[]={1,0,0};
			double yDir[]={0,1,0};
			double zDir[]={0,0,1};
			double aftSD[] = new double[3];
			double midSD[] = new double[3];
			double rotUx[] = new double[3];double rotUy[] = new double[3];double rotUz[] = new double[3];
			VectorAlgebra.matrixMultiplyVect(iu,posnAftShroudDisk,aftSD);
			VectorAlgebra.matrixMultiplyVect(iu2,posnMidShroudDisk,midSD);
			VectorAlgebra.matrixMultiplyVect(iu,xDir,rotUx);
			VectorAlgebra.matrixMultiplyVect(iu,yDir,rotUy);
			VectorAlgebra.matrixMultiplyVect(iu,zDir,rotUz);
			double rotUx2[] = new double[3];double rotUy2[] = new double[3];double rotUz2[] = new double[3];
			double yVec2[]= new double[3];
			double xVec2[]= new double[3];
			VectorAlgebra.matrixMultiplyVect(iu2,xDir,rotUx2);
			VectorAlgebra.matrixMultiplyVect(iu2,yDir,rotUy2);
			VectorAlgebra.matrixMultiplyVect(iu2,zDir,rotUz2);
			VectorAlgebra.matrixMultiplyVect(iu2, vectYAftShroud, yVec2);
			VectorAlgebra.matrixMultiplyVect(iu2, vectXAftShroud, xVec2);

			pw.println("ttx "+(-(12756.0/2.0+30000)+aftSC[0])+","+(aftSC[1])+","+(aftSC[2])+","                   
					+ ""+(xVec[0])+","+(xVec[1])+","+(xVec[2])+","                                                                           
					+ ""+(yVec[0])+","+(yVec[1])+","+(yVec[2])+","                                                                           
					+ "R,AftShroud.jpg,specular.jpg;");
			pw.println("disk "+(210.0*scale)+", 0.0,"+(-(12756.0/2.0+30000)+aftSD[0])+","+(aftSD[1])+","+(aftSD[2])+","
					+""+rotUx[0]+","+rotUx[1]+","+rotUx[2]+","
					+""+rotUy[0]+","+rotUy[1]+","+rotUy[2]+","
					+""+rotUz[0]+","+rotUz[1]+","+rotUz[2]+";");
			pw.println("clt;");                                                                               
			pw.println("ttx "+(-(12756.0/2.0+30000)+midSC[0])+","+(midSC[1])+","+(midSC[2])+","                   
					+ ""+(xVec2[0])+","+(xVec2[1])+","+(xVec2[2])+","                                                                           
					+ ""+(yVec2[0])+","+(yVec2[1])+","+(yVec2[2])+","                                                                           
					+ "R,MidShroud.jpg,specular.jpg;");
			pw.println("disk "+(210.0*scale)+", "+(151.0*scale)+", "+(-(12756.0/2.0+30000)+midSD[0])+","+(midSD[1])+","+(midSD[2])+","
					+""+rotUx2[0]+","+rotUx2[1]+","+rotUx2[2]+","
					+""+rotUy2[0]+","+rotUy2[1]+","+rotUy2[2]+","
					+""+rotUz2[0]+","+rotUz2[1]+","+rotUz2[2]+";");
			pw.println("clt;");  

			pw.println("mp 0,1,1,5.0,100,0.01;");
			pw.println("cyl "+(150.0*scale)+",  "+(-(12756.0/2.0+30000))+",0,0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+
					", "+0+",0.0,"+1+", "+(660.0*scale)+";");
			pw.println("cyl "+(209.0*scale)+",  "+(-(12756.0/2.0+30000))+",0, 0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(-660.0*scale)+
					","+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+";");
			pw.println("end;");

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

			double alpha= Math.asin(((12756.0/2.0)+(3475.0/2.0)*2.0)/0.384e6)+Math.asin((12756.0/2.0)/((12756.0/2.0)+30000));       
			xx = 0.384e6*Math.cos(alpha)-(3475.0/2.0);
			zz = -(3475.0/2.0);
			xxw = 0.384e6*Math.cos(alpha)+(3475.0/2.0);
			zzw = +(3475.0/2.0);
			min = (-0.384e6*Math.sin(alpha))-(3475.0/2.0);
			max = (-0.384e6*Math.sin(alpha))+(3475.0/2.0);
			bounding ( xx,  min,  zz,
					xxw,  min,  zz,
					xxw,  max,  zz, 
					xx,  max,  zz, 
					xx,  min,  zzw, 
					pw);
			pw.println("re moon;");
			pw.println("tex "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,-1,0,0, 0,-1,0, 0,0,1,R, moon.jpg, diffuse.jpg;");
			pw.println("mp 0,1,1,0,0,0.04;");
			pw.println("sp "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,"+(3475.0/2.0)+";");
			pw.println("clear;");
			pw.println("end;");
			pw.println("end;");

			pw.close();
			//###########################################Green#########################################
			scenefp = new File("scene2green.inf");
			pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

			pw.println("re test scene showing two spheres above a square plain made from two;");
			pw.println("re triangles of different shade;");

			pw.println("ls "+(-149.6e6)+
					", "+(0.0)+",0, 10e21;");
			pw.println("vp "+
					(-(12756.0/2.0+30000)-(20000*scale) * Math.sin((Math.PI *((double)i/20.0+90))/180.0))+
					", "+(-(20000*scale) * Math.cos((Math.PI *((double)i/20.0+90))/180.0))+
					",0,  "+(-(12756.0/2.0+30000))+",0, 0, 4,0;");
			/* refractive index */
			/* absorbance of material +ve normal side */
			/* absorbance of material -ve normal side */
			/* percentage perfect mirror */
			/* Phong smoothness value for a surface */
			/* Diffuse reflectance of the material */
			bounding(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,pw);
			xx = (-12756.0/2.0-30000)-800.0*scale;
			zz = -800.0*scale;
			xxw =  (-12756.0/2.0-30000)+800.0*scale;
			zzw = +800.0*scale;
			min = (-210-50-200)*1*scale;
			max = (+210+50+200)*1*scale;
			bounding ( xx,  min,  zz,
					xxw,  min,  zz,
					xxw,  max,  zz, 
					xx,  max,  zz, 
					xx,  min,  zzw, 
					pw);
			pw.println("re satelite;");
			pw.println("ttx "+(-(12756.0/2.0+30000))+","+(-210-50-200)*1*scale+","+(-400*scale)+","                   
					+ "0,"+(200*scale)+",0,"                                                                           
					+ "0,0,"+(800*scale)+","                                                                           
					+ "G,SolarPanel.jpg,specular.jpg;");                                                      
			pw.println("mp 0,1,1,10.0,1000,0.04;");                                                           
			pw.println("tr "                                                                                  
					+(-12756.0/2.0-30000)+","+(-210-50-200)*1*scale+","+400*scale+","                                 
					+(-12756.0/2.0-30000)+","+(-210-50)*1*scale+","+400*scale+","                                     
					+(-12756.0/2.0-30000)+","+(-210-50)*1*scale+","+(-400*scale)+";");                                
			pw.println("tr "                                                                                  
					+(-12756.0/2.0-30000)+","+(-210-50)*1*scale+","+(-400*scale)+","                                  
					+(-12756.0/2.0-30000)+","+(-210-50-200)*1*scale+","+(-400*scale)+","                              
					+(-12756.0/2.0-30000)+","+(-210-50-200)*1*scale+","+400*scale+";");                               
			pw.println("clt;");                                                                               
			pw.println("ttx "+(-(12756.0/2.0+30000))+","+(+210+50)*1*scale+","+(-400*scale)+","                       
					+ "0,"+(200*scale)+",0,"                                                         			      
					+ "0,0,"+(800*scale)+","                                                         			      
					+ "G,SolarPanel.jpg,specular.jpg;");                                     			      
			pw.println("tr "                                                                                  
					+(-12756.0/2.0-30000)+","+(+210+50)*1*scale+","+400*scale+","                                     
					+(-12756.0/2.0-30000)+","+(+210+50+200)*1*scale+","+400*scale+","                                 
					+(-12756.0/2.0-30000)+","+(+210+50+200)*1*scale+","+(-400*scale)+";");                            
			pw.println("tr "                                                                                  
					+(-12756.0/2.0-30000)+","+(+210+50+200)*1*scale+","+(-400*scale)+","                              
					+(-12756.0/2.0-30000)+","+(+210+50)*1*scale+","+(-400*scale)+","                                  
					+(-12756.0/2.0-30000)+","+(+210+50)*1*scale+","+400*scale+";");                                   
			pw.println("clt;"); 
			//back
			pw.println("ttx "+(-(12756.0/2.0+30000-2*scale))+","+(-210-50)*1*scale+","+(-400*scale)+","
					+ "0,-"+(200*scale)+",0,"
					+ "0,0,"+(800*scale)+","
					+ "G,SolarPanelBack.jpg,SolarPanelBackSpecular.jpg;");
			pw.println("mp 0,1,1,90.0,100,0.04;");                                                                        
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50-200)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50)*1*scale+","+400*scale+";");
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50-200)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50-200)*1*scale+","+(-400*scale)+";");
			pw.println("clt;"); 
			pw.println("ttx "+(-(12756.0/2.0+30000-2*scale))+","+(+210+50+200)*1*scale+","+(-400*scale)+"," 
					+ "0,-"+(200*scale)+",0,"                                                         			
					+ "0,0,"+(800*scale)+","                                                         			
					+ "G,SolarPanelBack.jpg,SolarPanelBackSpecular.jpg;");                                     			
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50+200)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50+200)*1*scale+","+400*scale+";");
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50+200)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50)*1*scale+","+(400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50)*1*scale+","+(-400*scale)+";");
			pw.println("clt;");
			pw.println("mp 0,1,1,90.0,100,0.32;");
			pw.println("tcx "+(-(12756.0/2.0+30000))+",0,0,"+(660.0*scale)+","+(515.0*scale-660.0*scale)+","+(0.0)+","+(Math.PI * i /180.0)+","+(Math.PI * -120 /180.0)+" ,G, NarrowTube.jpg, specular.jpg;");
			pw.println("cyl "+(151.0*scale)+",  "+(-(12756.0/2.0+30000))+",0,0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+
					", "+0+",0.0,"+1+", "+(660.0*scale)+";");
			pw.println("clc;");
			pw.println("tcx "+(-(12756.0/2.0+30000))+",0,0, "+(+515.0*scale-660.0*scale)+","+(-660.0*scale)+","+(0.0)+","+(Math.PI * i /180.0)+","+(Math.PI * 90 /180.0)+", G, BottomTube.jpg, specular.jpg;");
			pw.println("cyl "+(210.0*scale)+",  "+(-(12756.0/2.0+30000))+",0, 0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(-660.0*scale)+
					","+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+";");
			pw.println("clc;");

			pw.println("ttx "+(-(12756.0/2.0+30000)+aftSC[0])+","+(aftSC[1])+","+(aftSC[2])+","                   
					+ ""+(xVec[0])+","+(xVec[1])+","+(xVec[2])+","                                                                           
					+ ""+(yVec[0])+","+(yVec[1])+","+(yVec[2])+","                                                                           
					+ "G,AftShroud.jpg,specular.jpg;");
			pw.println("disk "+(210.0*scale)+", 0.0,"+(-(12756.0/2.0+30000)+aftSD[0])+","+(aftSD[1])+","+(aftSD[2])+","
					+""+rotUx[0]+","+rotUx[1]+","+rotUx[2]+","
					+""+rotUy[0]+","+rotUy[1]+","+rotUy[2]+","
					+""+rotUz[0]+","+rotUz[1]+","+rotUz[2]+";");
			pw.println("clt;");                                                                               
			pw.println("ttx "+(-(12756.0/2.0+30000)+midSC[0])+","+(midSC[1])+","+(midSC[2])+","                   
					+ ""+(xVec2[0])+","+(xVec2[1])+","+(xVec2[2])+","                                                                           
					+ ""+(yVec2[0])+","+(yVec2[1])+","+(yVec2[2])+","                                                                           
					+ "G,MidShroud.jpg,specular.jpg;");
			pw.println("disk "+(210.0*scale)+", "+(151.0*scale)+", "+(-(12756.0/2.0+30000)+midSD[0])+","+(midSD[1])+","+(midSD[2])+","
					+""+rotUx2[0]+","+rotUx2[1]+","+rotUx2[2]+","
					+""+rotUy2[0]+","+rotUy2[1]+","+rotUy2[2]+","
					+""+rotUz2[0]+","+rotUz2[1]+","+rotUz2[2]+";");
			pw.println("clt;");  

			pw.println("mp 0,1,1,5.0,100,0.01;");
			pw.println("cyl "+(150.0*scale)+",  "+(-(12756.0/2.0+30000))+",0,0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+
					", "+0+",0.0,"+1+", "+(660.0*scale)+";");
			pw.println("cyl "+(209.0*scale)+",  "+(-(12756.0/2.0+30000))+",0, 0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(-660.0*scale)+
					","+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+";");
			pw.println("end;");

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

			xx = 0.384e6*Math.cos(alpha)-(3475.0/2.0);
			zz = -(3475.0/2.0);
			xxw = 0.384e6*Math.cos(alpha)+(3475.0/2.0);
			zzw = +(3475.0/2.0);
			min = (-0.384e6*Math.sin(alpha))-(3475.0/2.0);
			max = (-0.384e6*Math.sin(alpha))+(3475.0/2.0);
			bounding ( xx,  min,  zz,
					xxw,  min,  zz,
					xxw,  max,  zz, 
					xx,  max,  zz, 
					xx,  min,  zzw, 
					pw);

			pw.println("re moon;");
			pw.println("tex "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,-1,0,0, 0,-1,0, 0,0,1,G, moon.jpg, diffuse.jpg;");
			pw.println("mp 0,1,1,0,0,0.04;");
			pw.println("sp "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,"+(3475.0/2.0)+";");
			pw.println("clear;");
			pw.println("end;");
			pw.println("end;");

			pw.close();
			//##############################################Blue#################################################
			scenefp = new File("scene2blue.inf");
			pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

			pw.println("re test scene showing two spheres above a square plain made from two;");
			pw.println("re triangles of different shade;");

			pw.println("ls "+(-149.6e6)+
					", "+(0.0)+",0, 10e21;");
			pw.println("vp "+
					(-(12756.0/2.0+30000)-(20000*scale) * Math.sin((Math.PI *((double)i/20.0+90))/180.0))+
					", "+(-(20000*scale) * Math.cos((Math.PI *((double)i/20.0+90))/180.0))+
					",0,  "+(-(12756.0/2.0+30000))+",0, 0, 4,0;");
			/* refractive index */
			/* absorbance of material +ve normal side */
			/* absorbance of material -ve normal side */
			/* percentage perfect mirror */
			/* Phong smoothness value for a surface */
			/* Diffuse reflectance of the material */
			bounding(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,pw);
			xx = (-12756.0/2.0-30000)-800.0*scale;
			zz = -800.0*scale;
			xxw =  (-12756.0/2.0-30000)+800.0*scale;
			zzw = +800.0*scale;
			min = (-210-50-200)*1*scale;
			max = (+210+50+200)*1*scale;
			bounding ( xx,  min,  zz,
					xxw,  min,  zz,
					xxw,  max,  zz, 
					xx,  max,  zz, 
					xx,  min,  zzw, 
					pw);
			pw.println("re satelite;");
			pw.println("ttx "+(-(12756.0/2.0+30000))+","+(-210-50-200)*1*scale+","+(-400*scale)+","
					+ "0,"+(200*scale)+",0,"
					+ "0,0,"+(800*scale)+","
					+ "B,SolarPanel.jpg,specular.jpg;");
			pw.println("mp 0,1,1,10.0,1000,0.04;");                                                                        
			pw.println("tr "
					+(-12756.0/2.0-30000)+","+(-210-50-200)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000)+","+(-210-50)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000)+","+(-210-50)*1*scale+","+(-400*scale)+";");
			pw.println("tr "
					+(-12756.0/2.0-30000)+","+(-210-50)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000)+","+(-210-50-200)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000)+","+(-210-50-200)*1*scale+","+400*scale+";");
			pw.println("clt;"); 
			pw.println("ttx "+(-(12756.0/2.0+30000))+","+(+210+50)*1*scale+","+(-400*scale)+"," 
					+ "0,"+(200*scale)+",0,"                                                         			
					+ "0,0,"+(800*scale)+","                                                         			
					+ "B,SolarPanel.jpg,specular.jpg;");                                     			
			pw.println("tr "
					+(-12756.0/2.0-30000)+","+(+210+50)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000)+","+(+210+50+200)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000)+","+(+210+50+200)*1*scale+","+(-400*scale)+";");
			pw.println("tr "
					+(-12756.0/2.0-30000)+","+(+210+50+200)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000)+","+(+210+50)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000)+","+(+210+50)*1*scale+","+(400*scale)+";");
			pw.println("clt;"); 
			//back
			pw.println("ttx "+(-(12756.0/2.0+30000-2*scale))+","+(-210-50)*1*scale+","+(-400*scale)+","
					+ "0,-"+(200*scale)+",0,"
					+ "0,0,"+(800*scale)+","
					+ "B,SolarPanelBack.jpg,SolarPanelBackSpecular.jpg;");
			pw.println("mp 0,1,1,90.0,100,0.04;");                                                                        
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50-200)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50)*1*scale+","+400*scale+";");
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50-200)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000+2*scale)+","+(-210-50-200)*1*scale+","+(-400*scale)+";");
			pw.println("clt;"); 
			pw.println("ttx "+(-(12756.0/2.0+30000-2*scale))+","+(+210+50+200)*1*scale+","+(-400*scale)+"," 
					+ "0,-"+(200*scale)+",0,"                                                         			
					+ "0,0,"+(800*scale)+","                                                         			
					+ "B,SolarPanelBack.jpg,SolarPanelBackSpecular.jpg;");                                     			
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50)*1*scale+","+400*scale+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50+200)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50+200)*1*scale+","+400*scale+";");
			pw.println("tr "
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50+200)*1*scale+","+(-400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50)*1*scale+","+(400*scale)+","
					+(-12756.0/2.0-30000+2*scale)+","+(+210+50)*1*scale+","+(-400*scale)+";");
			pw.println("clt;");
			pw.println("mp 0,1,1,90.0,100,0.32;");
			pw.println("tcx "+(-(12756.0/2.0+30000))+",0,0,"+(660.0*scale)+","+(515.0*scale-660.0*scale)+","+(0.0)+","+(Math.PI * i /180.0)+","+(Math.PI * -120 /180.0)+" ,B, NarrowTube.jpg, specular.jpg;");
			pw.println("cyl "+(151.0*scale)+",  "+(-(12756.0/2.0+30000))+",0,0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+
					", "+0+",0.0,"+1+", "+(660.0*scale)+";");
			pw.println("clc;");
			pw.println("tcx "+(-(12756.0/2.0+30000))+",0,0, "+(+515.0*scale-660.0*scale)+","+(-660.0*scale)+","+(0.0)+","+(Math.PI * i /180.0)+","+(Math.PI * 90 /180.0)+", B, BottomTube.jpg, specular.jpg;");
			pw.println("cyl "+(210.0*scale)+",  "+(-(12756.0/2.0+30000))+",0, 0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(-660.0*scale)+
					","+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+";");
			pw.println("clc;");

			pw.println("ttx "+(-(12756.0/2.0+30000)+aftSC[0])+","+(aftSC[1])+","+(aftSC[2])+","                   
					+ ""+(xVec[0])+","+(xVec[1])+","+(xVec[2])+","                                                                           
					+ ""+(yVec[0])+","+(yVec[1])+","+(yVec[2])+","                                                                           
					+ "B,AftShroud.jpg,specular.jpg;");
			pw.println("disk "+(210.0*scale)+", 0.0,"+(-(12756.0/2.0+30000)+aftSD[0])+","+(aftSD[1])+","+(aftSD[2])+","
					+""+rotUx[0]+","+rotUx[1]+","+rotUx[2]+","
					+""+rotUy[0]+","+rotUy[1]+","+rotUy[2]+","
					+""+rotUz[0]+","+rotUz[1]+","+rotUz[2]+";");
			pw.println("clt;");                                                                               
			pw.println("ttx "+(-(12756.0/2.0+30000)+midSC[0])+","+(midSC[1])+","+(midSC[2])+","                   
					+ ""+(xVec2[0])+","+(xVec2[1])+","+(xVec2[2])+","                                                                           
					+ ""+(yVec2[0])+","+(yVec2[1])+","+(yVec2[2])+","                                                                           
					+ "B,MidShroud.jpg,specular.jpg;");
			pw.println("disk "+(210.0*scale)+", "+(151.0*scale)+", "+(-(12756.0/2.0+30000)+midSD[0])+","+(midSD[1])+","+(midSD[2])+","
					+""+rotUx2[0]+","+rotUx2[1]+","+rotUx2[2]+","
					+""+rotUy2[0]+","+rotUy2[1]+","+rotUy2[2]+","
					+""+rotUz2[0]+","+rotUz2[1]+","+rotUz2[2]+";");
			pw.println("clt;");  

			pw.println("mp 0,1,1,5.0,100,0.01;");
			pw.println("cyl "+(150.0*scale)+",  "+(-(12756.0/2.0+30000))+",0,0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+
					", "+0+",0.0,"+1+", "+(660.0*scale)+";");
			pw.println("cyl "+(209.0*scale)+",  "+(-(12756.0/2.0+30000))+",0, 0, "+(0.0)+","+(Math.PI * i /180.0)+","+0+", "+0+",0.0,"+1+", "+(-660.0*scale)+
					","+0+",0.0,"+1+", "+(515.0*scale-660.0*scale)+";");
			pw.println("end;");

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

			xx = 0.384e6*Math.cos(alpha)-(3475.0/2.0);
			zz = -(3475.0/2.0);
			xxw = 0.384e6*Math.cos(alpha)+(3475.0/2.0);
			zzw = +(3475.0/2.0);
			min = (-0.384e6*Math.sin(alpha))-(3475.0/2.0);
			max = (-0.384e6*Math.sin(alpha))+(3475.0/2.0);
			bounding ( xx,  min,  zz,
					xxw,  min,  zz,
					xxw,  max,  zz, 
					xx,  max,  zz, 
					xx,  min,  zzw, 
					pw);

			pw.println("re moon;");
			pw.println("tex "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,-1,0,0, 0,-1,0, 0,0,1,B, moon.jpg, diffuse.jpg;");
			pw.println("mp 0,1,1,0,0,0.04;");
			pw.println("sp "+0.384e6*Math.cos(alpha)+","+(-0.384e6*Math.sin(alpha))+",0,"+(3475.0/2.0)+";");
			pw.println("clear;");
			pw.println("end;");
			pw.println("end;");

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