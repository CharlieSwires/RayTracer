import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Outermost public class 
 * @author Charlie Swires
 *
 */
public class CandleLight {

	/**
	 * Class attributes default visibility
	 */
	JButton render, quit;
	WindAdapter windListener;
	DisplayCanvas dc;
	static int w; //static means part of the class not the instance
	static int h;
	static int d;
	double xarr[]; //initial position of the ball
	double yarr[];
	double zarr[];
	double dxarr[];  // initial delta or speed
	double dyarr[];
	double dzarr[];
	static int count;
	JFrame jfrm; // main frame
	MyThread t = null; // thread
	long index = 0;

	/**
	 * Constructor for setting up the JFrame
	 * @param title
	 */
	public CandleLight(String title){
		jfrm = new JFrame(title);
		jfrm.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		ButtonListener listener = new ButtonListener();
		windListener = new WindAdapter();
		jfrm.addWindowListener((WindowListener)windListener);          
		render = new JButton("Render");
		render.addActionListener(listener);
		buttonPanel.add(render);
		quit = new JButton("Quit");
		quit.addActionListener(listener);
		buttonPanel.add(quit);
		jfrm.add(buttonPanel, BorderLayout.SOUTH);
		JPanel dimensionsPanel = new JPanel();
		dimensionsPanel.setLayout(new FlowLayout());
		jfrm.add(dimensionsPanel, BorderLayout.NORTH);
		dc = new DisplayCanvas(
				);
		jfrm.add(dc, BorderLayout.CENTER);
		jfrm.setSize(w+10,h+40);
		jfrm.setVisible(true);
	}
	/**
	 * Thread for triggering the repaint and the calculations
	 * @author Charlie Swires
	 *
	 */
	class MyThread extends Thread{
		@Override
		public void run(){
			while (true){
				for (int i=0; i< count;i++){
					double x = xarr[i];
					double y = yarr[i];
					double z = zarr[i];
					double dx = dxarr[i];
					double dy = dyarr[i];
					double dz = dzarr[i];
					//System.out.println("x = "+x);
					x += dx;//Laws of motion
					y += dy;
					z += dz;
					//Collisions with walls
					double scale = 0.5;
					if (index%1 == 0){
					dx = 2.0*Math.random()*scale - 1.0*scale;
					dy = 2.0*Math.random()*scale - 1.0*scale;
					dz = 2.0*Math.random()*scale - 1.0*scale;
					}
					if (x > w){
						x = w;
						dx = -1.0*scale*Math.random()*Math.signum(dx);
					}
					if (y > h){
						y = h;
						dy = -1.0*scale*Math.random()*Math.signum(dy);
					}
					if (z > d){
						z = d;
						dz = -1.0*scale*Math.random()*Math.signum(dz);
					}
					if (x < 0.0){
						x = 0.0;
						dx = -1.0*scale*Math.random()*Math.signum(dx);
					}
					if (y < 0.0){
						y = 0.0;
						dy = -1.0*scale*Math.random()*Math.signum(dy);
					}
					if (z < 0.0){
						z = 0.0;
						dz = -1.0*scale*Math.random()*Math.signum(dz);
					}
					xarr[i]=x;
					yarr[i]=y;
					zarr[i]=z;
					dxarr[i]=dx;
					dyarr[i]=dy;
					dzarr[i]=dz;
				}
				dc.repaint();//trigger paint
				try {
					Thread.sleep(50);//50ms frame length + 
					//calculations and drawing time
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Listener for the botton presses render and quit
	 * @author Charlie Swires3
	 *
	 */
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if (e.getSource() == render){
				jfrm.remove(dc); // setup a new DisplayCanvas
				dc = new DisplayCanvas();
				jfrm.add(dc, BorderLayout.CENTER);
				jfrm.setVisible(true);
				if (t == null){ //Only once initialise and set off thread
					t = new MyThread();
					t.start();
				}
			}
			if (e.getSource() == quit){
				System.exit(0);
			}
		}
	}

	/**
	 * For closing window using the X
	 * @author Charlie Swires
	 *
	 */
	class WindAdapter extends WindowAdapter{

		public void windowClosing(WindowEvent e){  
			System.exit(0);
		}

	}

	/**
	 * Canvas has the paint method that gets called when repaint is tickled
	 * @author Charlie Swires
	 *
	 */
	class DisplayCanvas extends Canvas { 
		private static final long serialVersionUID = 1L;

		/**
		 * No parameter constructor
		 */
		public DisplayCanvas(){
			xarr=new double[count];
			dxarr=new double[count];
			yarr=new double[count];
			dyarr=new double[count];
			zarr=new double[count];
			dzarr=new double[count];

			for(int i=0;i<count;i++){
				xarr[i] = w*Math.random();// reinitialise
				yarr[i] = h*Math.random();
				zarr[i] = d*Math.random();
				dxarr[i] = 0.0;
				dyarr[i] = 0.0;
				dzarr[i] = 0.0;
			}
		}
		/**
		 * Overridden paint method that has the magic g
		 * @Override
		 */
		public void paint(Graphics g) { 
			Color c = Color.BLACK;
			g.setColor(c);
			g.fillRect(0, 0, w, h); // clear the frame
			c = Color.RED;
			g.setColor(c); // draw the circle
			for(int i=0;i<count;i++)
				g.fillArc(Math.round((float)xarr[i]), Math.round((float)yarr[i]),Math.round((float)zarr[i]), Math.round((float)zarr[i]), -180,+360);
			String args[] = {"charlieAndZhaida.jpg","1500","1000"};
			try {
				generateScene(args, index);
				index++;
				if (index>359)System.exit(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private void generateScene(String args[], long index) throws IOException {
 
				RayTracerMain rtm = new RayTracerMain();

				int i = 0;
				//int x = 0;
				//int y = 0;
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
				Date date;
				double scale = 1e-1;
				
				i = (int)index;
				{
					date = new Date();
					System.out.println("i ="+i+" date time ="+date);
					//generate scene2.inf
					//###########################################Red#########################################

					scenefp = new File("scene2red.inf");
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

					pw.println("re ;");
					pw.println("re t;");

					pw.println("ls "+xarr[0]+
							", "+yarr[0]+","+zarr[0]+", "+20e9*(zarr[0]+40)+";");
					pw.println("vp "+(-1000)+
							", "+0+
							",150,  "+0+",0, -150, 1,0;");
					/* refractive index */
					/* absorbance of material +ve normal side */
					/* absorbance of material -ve normal side */
					/* percentage perfect mirror */
					/* Phong smoothness value for a surface */
					/* Diffuse reflectance of the material */

					pw.println("mp 0,1,1,0.0,100,0.32;");
					pw.println("cyl "+(20)+",  "+(20)+",+20,0, "+(0.0)+","+0+","+0+", "+0+",0.0,"+1+", "+(-300)+
							", "+0+",0.0,"+1+", "+(-40)+";");
					pw.println("mp 0,1,1,5.0,100,0.001;");
					pw.println("disk "+(1000)+", "+(0)+", "+(0)+","+(0)+","+(-300)+","
							+""+1+","+0+","+0+","
							+""+0+","+1+","+0+","
							+""+0+","+0+","+1+";");
					pw.println("ttx "+(50)+","+(-210-50-200)+","+(-300)+","                   
							+ "0,"+(-200)+",0,"                                                                           
							+ "0,0,"+(200)+","                                                                           
							+ "R,zhaida.jpg,specular.jpg;");                                                      
					pw.println("mp 0,1,1,10.0,1000,0.02;");                                                           
					pw.println("tr "                                                                                  
							+(50)+","+(-210-50-200)+","+(-100)+","                                 
							+(50)+","+(-50)+","+(-100)+","                                     
							+(50)+","+(-50)+","+(-300)+";");                                
					pw.println("tr "                                                                                  
							+(50)+","+(-50)+","+(-300)+","                                  
							+(50)+","+(-210-50-200)+","+(-300)+","                              
							+(50)+","+(-210-50-200)+","+-100+";");                               
					pw.println("clt;");                                                                               
					pw.println("ttx "+(50)+","+(+210+50+200)+","+(-300)+","                   
							+ "0,0,"+(200)+","                                                                           
							+ "0,"+(+100)+",0,"                                                                           
							+ "R,charlie.jpg,specular.jpg;");                                                      
					pw.println("mp 0,1,1,10.0,1000,0.02;");                                                           
					pw.println("tr "                                                                                  
							+(50)+","+(+210+50+200)+","+(-100)+","                                 
							+(50)+","+(+50)+","+(-100)+","                                     
							+(50)+","+(+50)+","+(-300)+";");                                
					pw.println("tr "                                                                                  
							+(50)+","+(+50)+","+(-300)+","                                  
							+(50)+","+(+210+50+200)+","+(-300)+","                              
							+(50)+","+(+210+50+200)+","+-100+";");                               
					pw.println("clt;");                                                                               
					pw.println("mp 0,1,1,0,0,0.0;");
	
					pw.println("tex 0,0,0,1,0,0, 0,1,0, 0,0,1,R, sky.jpg, sky.jpg;");
					pw.println("sky;");
					pw.println("clear;");

					pw.close();
					//###########################################Green#########################################
					scenefp = new File("scene2green.inf");
					pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

					pw.println("re test scene showing two spheres above a square plain made from two;");
					pw.println("re triangles of different shade;");


					pw.println("ls "+xarr[0]+
							", "+yarr[0]+","+zarr[0]+", "+20e9*(zarr[0]+40)+";");
					pw.println("vp "+(-1000)+
							", "+0+
							",150,  "+0+",0, -150, 1,0;");
					/* refractive index */
					/* absorbance of material +ve normal side */
					/* absorbance of material -ve normal side */
					/* percentage perfect mirror */
					/* Phong smoothness value for a surface */
					/* Diffuse reflectance of the material */

					pw.println("mp 0,1,1,0.0,100,0.32;");
					pw.println("cyl "+(20)+",  "+(20)+",+20,0, "+(0.0)+","+0+","+0+", "+0+",0.0,"+1+", "+(-300)+
							", "+0+",0.0,"+1+", "+(-40)+";");
					pw.println("mp 0,1,1,5.0,100,0.001;");
					pw.println("disk "+(1000)+", "+(0)+", "+(0)+","+(0)+","+(-300)+","
							+""+1+","+0+","+0+","
							+""+0+","+1+","+0+","
							+""+0+","+0+","+1+";");
					pw.println("ttx "+(50)+","+(-210-50-200)+","+(-300)+","                   
							+ "0,"+(-200)+",0,"                                                                           
							+ "0,0,"+(200)+","                                                                           
							+ "G,zhaida.jpg,specular.jpg;");                                                      
					pw.println("mp 0,1,1,10.0,1000,0.02;");                                                           
					pw.println("tr "                                                                                  
							+(50)+","+(-210-50-200)+","+(-100)+","                                 
							+(50)+","+(-50)+","+(-100)+","                                     
							+(50)+","+(-50)+","+(-300)+";");                                
					pw.println("tr "                                                                                  
							+(50)+","+(-50)+","+(-300)+","                                  
							+(50)+","+(-210-50-200)+","+(-300)+","                              
							+(50)+","+(-210-50-200)+","+-100+";");                               
					pw.println("clt;");                                                                               
					pw.println("ttx "+(50)+","+(+210+50+200)+","+(-300)+","                   
							+ "0,0,"+(200)+","                                                                           
							+ "0,"+(+100)+",0,"                                                                           
							+ "G,charlie.jpg,specular.jpg;");                                                      
					pw.println("mp 0,1,1,10.0,1000,0.02;");                                                           
					pw.println("tr "                                                                                  
							+(50)+","+(+210+50+200)+","+(-100)+","                                 
							+(50)+","+(+50)+","+(-100)+","                                     
							+(50)+","+(+50)+","+(-300)+";");                                
					pw.println("tr "                                                                                  
							+(50)+","+(+50)+","+(-300)+","                                  
							+(50)+","+(+210+50+200)+","+(-300)+","                              
							+(50)+","+(+210+50+200)+","+-100+";");                               
					pw.println("clt;");                                                                               
					pw.println("mp 0,1,1,0,0,0.0;");
	
					pw.println("tex 0,0,0,1,0,0, 0,1,0, 0,0,1,G, sky.jpg, sky.jpg;");
					pw.println("sky;");
					pw.println("clear;");

					pw.close();
					//##############################################Blue#################################################
					scenefp = new File("scene2blue.inf");
					pw = new PrintWriter(new BufferedWriter(new FileWriter(scenefp)));

					pw.println("re test scene showing two spheres above a square plain made from two;");
					pw.println("re triangles of different shade;");


					pw.println("ls "+xarr[0]+
							", "+yarr[0]+","+zarr[0]+", "+20e9*(zarr[0]+40)+";");
					pw.println("vp "+(-1000)+
							", "+0+
							",150,  "+0+",0, -150, 1,0;");
					/* refractive index */
					/* absorbance of material +ve normal side */
					/* absorbance of material -ve normal side */
					/* percentage perfect mirror */
					/* Phong smoothness value for a surface */
					/* Diffuse reflectance of the material */

					pw.println("mp 0,1,1,0.0,100,0.32;");
					pw.println("cyl "+(20)+",  "+(20)+",+20,0, "+(0.0)+","+0+","+0+", "+0+",0.0,"+1+", "+(-300)+
							", "+0+",0.0,"+1+", "+(-40)+";");
					pw.println("mp 0,1,1,5.0,100,0.001;");
					pw.println("disk "+(1000)+", "+(0)+", "+(0)+","+(0)+","+(-300)+","
							+""+1+","+0+","+0+","
							+""+0+","+1+","+0+","
							+""+0+","+0+","+1+";");
					pw.println("ttx "+(50)+","+(-210-50-200)+","+(+300)+","                   
							+ "0,"+(-200)+",0,"                                                                           
							+ "0,0,"+(200)+","                                                                           
							+ "B,zhaida.jpg,specular.jpg;");                                                      
					pw.println("mp 0,1,1,10.0,1000,0.02;");                                                           
					pw.println("tr "                                                                                  
							+(50)+","+(-210-50-200)+","+(-100)+","                                 
							+(50)+","+(-50)+","+(-100)+","                                     
							+(50)+","+(-50)+","+(-300)+";");                                
					pw.println("tr "                                                                                  
							+(50)+","+(-50)+","+(-300)+","                                  
							+(50)+","+(-210-50-200)+","+(-300)+","                              
							+(50)+","+(-210-50-200)+","+-100+";");                               
					pw.println("clt;");
					pw.println("ttx "+(50)+","+(+210+50+200)+","+(-300)+","                   
							+ "0,0,"+(200)+","                                                                           
							+ "0,"+(+100)+",0,"                                                                           
							+ "B,charlie.jpg,specular.jpg;");                                                      
					pw.println("mp 0,1,1,10.0,1000,0.02;");                                                           
					pw.println("tr "                                                                                  
							+(50)+","+(+210+50+200)+","+(-100)+","                                 
							+(50)+","+(+50)+","+(-100)+","                                     
							+(50)+","+(+50)+","+(-300)+";");                                
					pw.println("tr "                                                                                  
							+(50)+","+(+50)+","+(-300)+","                                  
							+(50)+","+(+210+50+200)+","+(-300)+","                              
							+(50)+","+(+210+50+200)+","+-100+";");                               
					pw.println("clt;");                                                                               

					pw.println("mp 0,1,1,0,0,0.0;");
	
					pw.println("tex 0,0,0,1,0,0, 0,1,0, 0,0,1,B, sky.jpg, sky.jpg;");
					pw.println("sky;");
					pw.println("clear;");




					pw.close();

					//call raytrace
					String s =  null;

					String argr[]={"scene2red.inf", "scene2red.out", ""+w, ""+h, "10"};
					double or[][] = new double [w][h];
					or = rtm.raytracer(argr,or,false);

					String argg[]={"scene2green.inf", "scene2green.out", ""+w, ""+h, "10"};
					double og[][] = new double [w][h];
					og = rtm.raytracer(argg,og,false);

					String argb[]={"scene2blue.inf", "scene2blue.out", ""+w, ""+h, "10"};
					double ob[][] = new double [w][h];
					ob = rtm.raytracer(argb,ob,false);

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

	/**
	 * Main for kicking everything off and taking the size from the command line
	 * @param args
	 */
	public static void main(String args[]){
		try {
			w = Integer.parseInt(args[0]);
			h = Integer.parseInt(args[1]);
			d = Integer.parseInt(args[2]);
			count = Integer.parseInt(args[3]);
			new CandleLight("Bouncing Ball");
		}
		catch(Exception e){
			System.out.println("java -classpath . BouncingBall 1000 600 600 10");
			e.printStackTrace();
		}
	}
}
