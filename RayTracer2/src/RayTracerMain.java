import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.tools.ant.DirectoryScanner;


public class RayTracerMain{
	int noThreads = 8;
    private static final double NEARZERO = 0.0001;
	private static final boolean DEBUG_LINES = true;
	public static final boolean TRANSPARENCIES_HAVE_SHADOWS = false;
	public static int PIXELSH;                 /* number of horizontal pixels */
	public static int PIXELSV;                 /* number of vertical pixels */
	public static int minx;                    /* min value of x co-ordinate */
	public static int maxx;                    /* max value of x co-ordinate */
	public static int miny;                    /* min value of y co-ordinate */
	public static int maxy;                    /* max value of y co-ordinate */
	public static int depth;                   /* entered depth of raytrace */
	public static int done_once;               /* flag which records if raygen has been*/
	/* executed */
	public static double z;                     /*z component of initial direction vector*/
	public static File fpi;                   /* pointer to input file */
	public static File fpo;                   /* pointer to output file */
	/* the first elements in the enviroment */
	/* (lights, triangles, spheres etc).    */
	public static Enviro inptrs[];
	public static double output[][];
	Raynode rootray[];     /* pointer to the root of the ray tree */
	private JFrame jf = null;

	public static void main(String aRayGenerators[]) throws IOException{
		System.out.println("\nEnter the number of pixels horizontally ?");
		PIXELSH = Integer.parseInt(aRayGenerators[2]);
		System.out.println("\nEnter the number of pixels vertically ?");
		PIXELSV = Integer.parseInt(aRayGenerators[3]);
		boolean wireOnly = (aRayGenerators.length > 5 && aRayGenerators[5].equals("wire"))?true:false;

		output= new double[PIXELSH][PIXELSV];
		RayTracerMain rtm = new RayTracerMain();
		output = rtm.raytracer(aRayGenerators,output, wireOnly);
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(aRayGenerators[1])));
		for (int y=0;y<PIXELSV;y++){       /* for all vertical pixels */
			for (int x=0;x < PIXELSH;x++){  /* for all horizontal pixels */
				pw.println(output[x][y]);
			}
		}
		pw.close();
	}
	/******************************************************************************/
	/*                                                                            */
	/* TITLE :  Testbed Raytracer (final year project)                            */
	/* CREATOR: C.E.L. Swires                                                     */
	/* DATE:    17/3/1990                                                         */
	/*                                                                            */
	/* FILE: m.c                                                                  */
	/*                                                                            */
	/* PROCEDURE : main()                                                         */
	/*                                                                            */
	/* DESCRIPTION : This procedure opens the file containing the scene to be     */
	/* raytraced, opens the output file for the raster, allows user to enter      */
	/* raster size (ie number of pixels horizontally and number vertically) and   */
	/* the maximum ray tree depth. The scene is read in by env_read. Finally      */
	/* raygen and rtree are then called until the raster is completed.            */
	/*                                                                            */
	/* FUNCTIONS CALLED : env_read, raygen, rtree,                                */
	/*                                                                            */
	/* RELATED DOCUMENTS :                                                        */
	/*                                                                            */
	/**
	 * @param b 
	 * @throws IOException ****************************************************************************/

	public double[][] raytracer(String aRayGeneratorv[], double output[][], boolean DISPLAY_WIRE_FRAME_ONLY) throws IOException
	/* number of command line aRayGeneratoruments */
	/* text pointers to command line aRayGeneratoruments */

	{

		if (aRayGeneratorv.length == 0) {
			System.out.println("Input and output file names were not found on the command line \n");
			System.exit (1);
		}

		if (aRayGeneratorv.length == 1) {
			System.out.println("Output file name was not specified on the command line \n");
			System.exit(1);
		}

		if (aRayGeneratorv.length > 6) {
			System.out.println("I require only two file names on the command line, input followed by output file \n");
			System.exit(1);
		}
		inptrs = new Enviro[noThreads];        /* structure to contain pointers to all */
		rootray = new Raynode[noThreads];
		/* read input file */
		/* enter raster size and max tree depth */

		//System.out.println("\nEnter the number of pixels horizontally ?");
		PIXELSH = Integer.parseInt(aRayGeneratorv[2]);
		//System.out.println("\nEnter the number of pixels vertically ?");
		PIXELSV = Integer.parseInt(aRayGeneratorv[3]);
		//System.out.println("\nEnter the maximum depth of ray tree ?");
		depth = Integer.parseInt(aRayGeneratorv[4]);

		
		for (int i = 0;i<noThreads;i++){
			inptrs[i]= new Enviro();
			rootray[i] = new Raynode();
			inptrs[i].ti = null;       /* initialise all enviroment pointers */
			inptrs[i].si = null;
			inptrs[i].ci = null;
			inptrs[i].di = null;
			inptrs[i].bpi= null;
			inptrs[i].li = null;
			inptrs[i].vi = null;
			
			fpi = new File(aRayGeneratorv[0]);
			SceneReader.env_read(fpi,inptrs[i]);
			System.out.println("read "+i);
			if(i==0){
				if (jf == null){
					jf  = displayWireFrame(inptrs[0]);
				}else{
					jf.dispose();
					jf  = displayWireFrame(inptrs[0]);	
				}
				if (DISPLAY_WIRE_FRAME_ONLY)
				return output;
			}
		}
		/* work out max and min values for the x and y component of the */
		/* initial direction vector */

		minx=1-PIXELSH/2-PIXELSH%2;
		maxx=PIXELSH/2;
		miny=1-PIXELSV/2-PIXELSV%2;
		maxy=PIXELSV/2;

		done_once = -1;         /* raygen has not been executed */
		/* used to prvent recalculation of rotations*/

		z = inptrs[0].vi.mag * PIXELSH;   /* z value for initial ray direction */
		this.output= output;
		NewThread nt[]= new NewThread[noThreads];
		for(int thread = 0; thread < noThreads; thread++){
			nt[thread] = new NewThread(thread, noThreads);
		}
		for(int thread = 0; thread < noThreads; thread++){
			try {
				nt[thread].t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return output;

	}

	public class NewThread implements Runnable {
		int offset;
		int noThreads;
		Thread t;
		
		NewThread(int offset, int noThreads){
			this.offset = offset;
			this.noThreads = noThreads;
			t= new Thread(this, ""+offset);
			t.start();
		}
		@Override
		public void run() {
			divideWork(offset,noThreads,inptrs, output,miny, maxy, minx, maxx, depth,rootray);
		}
		
	}
	private void divideWork(int offset,int noThreads,Enviro inptrs[], double[][] output,int miny, int maxy, int minx, int maxx, int depth,Raynode rootray[]){
		for (int y=miny+offset;y<=maxy;y+=noThreads){       /* for all vertical pixels */
			doLine(inptrs[offset], output,minx,maxx, depth, y, rootray[offset]);
		}
	}	
	private void doLine(Enviro inptrs_single, double[][] output,int minx,int maxx, int depth,int y, Raynode rootray){
		if (DEBUG_LINES)System.out.print("y start ="+y+", ");
		for (int x=minx;x<=maxx;x++){  /* for all horizontal pixels */
			double b1,b2,b3,b4;
			/* work out ray according to the view point parameters*/
			rootray=RayGenerator.raygen((double)x-0.25,(double)y-0.25,z,inptrs_single.vi,done_once);

			done_once = 1; /* raygen executed once */

			rootray.depth=depth;
			RayTree.depth =depth;

			/* calculate brightness level for a pixel and file it*/

			b1 = RayTree.rtree(rootray,inptrs_single);
			/* work out ray according to the view point parameters*/

			rootray=RayGenerator.raygen((double)x+0.25,(double)y-0.25,z,inptrs_single.vi,done_once);

			done_once = 1; /* raygen executed once */

			rootray.depth=depth;
			RayTree.depth =depth;

			/* calculate brightness level for a pixel and file it*/

			b2 = RayTree.rtree(rootray,inptrs_single);
			/* work out ray according to the view point parameters*/

			rootray=RayGenerator.raygen((double)x+0.25,(double)y+0.25,z,inptrs_single.vi,done_once);

			done_once = 1; /* raygen executed once */

			rootray.depth=depth;
			RayTree.depth =depth;

			/* calculate brightness level for a pixel and file it*/

			b3 = RayTree.rtree(rootray,inptrs_single);
			/* work out ray according to the view point parameters*/

			rootray=RayGenerator.raygen((double)x-0.25,(double)y+0.25,z,inptrs_single.vi,done_once);

			done_once = 1; /* raygen executed once */

			rootray.depth=depth;
			RayTree.depth =depth;

			/* calculate brightness level for a pixel and file it*/

			b4 =RayTree.rtree(rootray,inptrs_single);
			output[x-minx][y-miny]=((b1+b2+b3+b4)/4.0);

		}
		
//		System.out.print("y end ="+y+", ");
		if (y%8==0 && DEBUG_LINES){
			System.out.println();
			System.out.print("time="+new Date()+":");
		}
	}
	
	JFrame jfrm;
	private DisplayCanvas ro;
	public JFrame displayWireFrame(Enviro env){
		jfrm = new JFrame("Ray Tracer");
		//contentPane = getContentPane();
		//contentPane.
		jfrm.setLayout(new BorderLayout());
		ro = new DisplayCanvas(env);
		jfrm.add(ro, BorderLayout.CENTER);
		jfrm.setSize(PIXELSH,PIXELSV);
		jfrm.setVisible(true);
		return jfrm;
	}

	class DisplayCanvas extends Canvas { 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		double mleft, mtop, mright, mbottom;
		Enviro environment;
		private boolean doneOnce = false;
		private double sin1;
		private double sin2;
		private double sin3;
		private double cos1;
		private double cos2;
		private double cos3;
		
		public DisplayCanvas(Enviro env){
			environment = env;
		}
		public void paint(Graphics g) { 
			View vi = environment.vi;
			Triangle tr = environment.ti;
			while(tr != null){
				draw(g,tr,vi);
				tr =tr.next;
			}/* end of while loop, for triangle list */

			Sphere sp = environment.si;
			while(sp != null){
				draw(g,sp,vi);
				sp=sp.next;
			}/* end of sphere while loop */

			Cylinder cy = environment.ci;
			while(cy != null){
				draw(g,cy,vi);
				cy =cy.next;
			}/* end of sphere while loop */

			Disk dc = environment.di;
			while(dc != null){
				draw(g,dc,vi);
				dc =dc.next;
			}/* end of while loop, for dciangle list */

			Lights li = environment.li;
			while(li != null){
				draw(g,li,vi);
				li=li.next;
			}/* end of while loop, for dciangle list */


		}
		private void draw(Graphics g, Triangle tr, View vi) {
			g.setColor(Color.BLACK);
			double centre[]={PIXELSH/2.0,PIXELSV/2.0,0.0};
			double tr1[] = transform3Dto2Dcoord(tr.co1,vi);
			double tr2[] = transform3Dto2Dcoord(tr.co2,vi);
			double tr3[] = transform3Dto2Dcoord(tr.co3,vi);
			VectorAlgebra.addv(centre, tr1, tr1);
			VectorAlgebra.addv(centre, tr2, tr2);
			VectorAlgebra.addv(centre, tr3, tr3);
			if (tr1[2] > 0.0 && tr2[2] > 0.0 && tr3[2] > 0.0){
				int xPoints[]={(int)Math.round(tr1[0]),(int)Math.round(tr2[0]),(int)Math.round(tr3[0])};
				int yPoints[]={(int)Math.round(tr1[1]),(int)Math.round(tr2[1]),(int)Math.round(tr3[1])};
				g.drawPolygon(xPoints, yPoints, 3);
	
			}
		}
		private void draw(Graphics g, Sphere sp, View vi) {
			g.setColor(Color.BLACK);
			double centre[]={PIXELSH/2.0,PIXELSV/2.0,0.0};
			double sp1[] = transform3Dto2Dcoord(sp.r_cnt,vi);
			double r = transform3DandRadiustoRadius(sp.r_cnt,sp.r_cnt[3],vi);
			VectorAlgebra.addv(centre, sp1, sp1);
			if (r > 0.0){
				g.drawArc((int)Math.round(sp1[0])-(int)Math.round(r), 
						(int)Math.round(sp1[1])-(int)Math.round(r), 
						(int)Math.round(2*r),(int)Math.round(2*r), 0,360);
	
			}
		}
		private void draw(Graphics g, Cylinder cy, View vi) {
			g.setColor(Color.BLACK);
			double centre[]={PIXELSH/2.0,PIXELSV/2.0,0.0};
			for (int i = 0; i < 360; i+=20){
				Raynode ray = new Raynode();
				double alpha = Math.PI * i /180.0;
				double sinvec[]=new double[3];
				double cosvec[]=new double[3];
				VectorAlgebra.mulv(Math.sin(alpha)*cy.radius, cy.uxInv, sinvec);
				VectorAlgebra.mulv(Math.cos(alpha)*cy.radius, cy.uyInv, cosvec);
				double result[] = new double[3];
				VectorAlgebra.addv(sinvec, cosvec, result);
				VectorAlgebra.addv(result, cy.position, result);
				VectorAlgebra.copyv(ray.ri, result);
				VectorAlgebra.copyv(ray.rd, cy.uzInv);
				double r0[]={0.0,0.0,0.0};
				double beta = VectorAlgebra.rpcol(r0, ray.rd, cy.plane1);
				double gamma = VectorAlgebra.rpcol(r0, ray.rd, cy.plane2);
				double betaPlPos[] = new double[3];
				double gammaPlPos[] = new double[3];
				VectorAlgebra.addv(ray.ri,VectorAlgebra.mulv(beta, ray.rd, betaPlPos),betaPlPos);
				VectorAlgebra.addv(ray.ri,VectorAlgebra.mulv(gamma, ray.rd, gammaPlPos),gammaPlPos);
				double tr1[] = transform3Dto2Dcoord(betaPlPos,vi);
				double tr2[] = transform3Dto2Dcoord(gammaPlPos,vi);
				alpha = Math.PI * (i+20) /180.0;
				VectorAlgebra.mulv(Math.sin(alpha)*cy.radius, cy.uxInv, sinvec);
				VectorAlgebra.mulv(Math.cos(alpha)*cy.radius, cy.uyInv, cosvec);
				VectorAlgebra.addv(sinvec, cosvec, result);
				VectorAlgebra.addv(result, cy.position, result);
				VectorAlgebra.copyv(ray.ri, result);
				VectorAlgebra.copyv(ray.rd, cy.uzInv);
				beta = VectorAlgebra.rpcol(r0, ray.rd, cy.plane1);
				gamma = VectorAlgebra.rpcol(r0, ray.rd, cy.plane2);
				VectorAlgebra.addv(ray.ri,VectorAlgebra.mulv(beta, ray.rd, betaPlPos),betaPlPos);
				VectorAlgebra.addv(ray.ri,VectorAlgebra.mulv(gamma, ray.rd, gammaPlPos),gammaPlPos);
				double tr4[] = transform3Dto2Dcoord(betaPlPos,vi);
				double tr3[] = transform3Dto2Dcoord(gammaPlPos,vi);
				if (tr1[2] > 0.0 && tr2[2] > 0.0&&tr3[2] > 0.0 && tr3[2] > 0.0){
					VectorAlgebra.addv(centre, tr1, tr1);
					VectorAlgebra.addv(centre, tr2, tr2);
					VectorAlgebra.addv(centre, tr3, tr3);
					VectorAlgebra.addv(centre, tr4, tr4);
					int xPoints[]={(int)Math.round(tr1[0]),(int)Math.round(tr2[0]),(int)Math.round(tr3[0]),(int)Math.round(tr4[0])};
					int yPoints[]={(int)Math.round(tr1[1]),(int)Math.round(tr2[1]),(int)Math.round(tr3[1]),(int)Math.round(tr4[1])};
					g.drawPolygon(xPoints, yPoints, 4);
				}	
			}
		}
		private void draw(Graphics g, Disk dc, View vi) {
			int xPoints[]=new int[360];
			int yPoints[]=new int[360];
			int xPointsi[]=new int[360];
			int yPointsi[]=new int[360];
			double centre[]={PIXELSH/2.0,PIXELSV/2.0,0.0};
			boolean behind = false;
			for (int i = 0; i < 360; i++){
				double alpha = Math.PI * i /180.0;
				double sinvec[]=new double[3];
				double cosvec[]=new double[3];
				VectorAlgebra.mulv(Math.sin(alpha)*dc.outerRadius, dc.ux, sinvec);
				VectorAlgebra.mulv(Math.cos(alpha)*dc.outerRadius, dc.uy, cosvec);
				double sinveci[]=new double[3];
				double cosveci[]=new double[3];
				VectorAlgebra.mulv(Math.sin(alpha)*dc.innerRadius, dc.ux, sinveci);
				VectorAlgebra.mulv(Math.cos(alpha)*dc.innerRadius, dc.uy, cosveci);
				double result[] = new double[3];
				double resulti[] = new double[3];
				VectorAlgebra.addv(sinvec, cosvec, result);
				VectorAlgebra.addv(sinveci, cosveci, resulti);
				VectorAlgebra.addv(result, dc.position, result);
				VectorAlgebra.addv(resulti, dc.position, resulti);			
				double tr1[] = transform3Dto2Dcoord(result,vi);
				double tr2[] = transform3Dto2Dcoord(resulti,vi);
				if (tr1[2] < 0.0 || tr2[2] < 0.0){
					behind = true; break;
				}
				VectorAlgebra.addv(centre, tr1, tr1);
				VectorAlgebra.addv(centre, tr2, tr2);
				xPoints[i]=(int)Math.round(tr1[0]);
				yPoints[i]=(int)Math.round(tr1[1]);
				xPointsi[i]=(int)Math.round(tr2[0]);
				yPointsi[i]=(int)Math.round(tr2[1]);
				
			}
			if (!behind){
				g.drawPolygon(xPoints, yPoints, 360);
				g.drawPolygon(xPointsi, yPointsi, 360);				
			}
			
		}
		private void draw(Graphics g, Lights li, View vi) {
			g.setColor(Color.BLACK);
			double centre[]={PIXELSH/2.0,PIXELSV/2.0,0.0};
			double tr1[] = transform3Dto2Dcoord(li.co,vi);
			VectorAlgebra.addv(centre, tr1, tr1);
			if (tr1[2] > 0.0){
				g.drawString("Li", (int)Math.round(tr1[0]), (int)Math.round(tr1[1]));
	
			}
		}
		private double[] transform3Dto2Dcoord(double coord[], View vi){
	        Raynode ray;            /* pointer to current light ray */
	        double tmod;                     /* temp store for modulus */
	        double tv1[]=new double[3];                   /* temporary vector */
	        double tv2[]=new double[3];                   /* temporary vector */
	        double tv3[]=new double[3];                   /* temporary vector */

	        ray = new Raynode();               /* make a ray with starting position */
	        VectorAlgebra.copyv(ray.ri,vi.co1);      /* from view point */

	        VectorAlgebra.subv(vi.co2,vi.co1,tv1);/* place in tv1 and tv2 the direction*/
	        VectorAlgebra.copyv(tv2,tv1);                 /* of view */

	        ray.rd[0]=coord[0]-ray.ri[0];                   /* set ray->rd to vector from main */
	        ray.rd[1]=coord[1]-ray.ri[1];
	        ray.rd[2]=coord[2]-ray.ri[2];

	        if (!doneOnce){             /* this works out the roll values */
	                sin3 = Math.sin(vi.rot); /* if rot=pi radians the output  */
	                cos3 = Math.cos(vi.rot); /* picture would be inverted */
	        }


	        if (!doneOnce){
	                tmod = VectorAlgebra.modv(tv1);
	                if (tmod < NEARZERO){   /* if modulus of tv1 is zero do not*/
	                        sin1=0.0;         /* rotate */
	                        cos1=1.0;
	                }
	                else{
	                        cos1=tv1[2]/tmod;       /* cos and sin of angle */
	                        sin1=Math.sqrt(1-cos1*cos1); /* between unit z vector and */
	                                                /* tv1 */
	                }

	                tv1[0]=tv1[1]=0.0;              /* carry out rotation on tv1 */
	                tv1[2]=1.0;
	                VectorAlgebra.rotx(sin1,cos1,tv1,tv1);
	        }

	        if (!doneOnce){
	                tv2[2]=0.0;tv1[2]=0.0;          /* make new ref vec tv3 */
	                VectorAlgebra.rotz(1.0,0.0,tv1,tv3);

	                tmod = VectorAlgebra.modv(tv2)*VectorAlgebra.modv(tv1);

	                if (tmod < NEARZERO){
	                        sin2=0.0;
	                        cos2=1.0;
	                }

	                else {
	                        cos2=(VectorAlgebra.dotv(tv1,tv2))/tmod;/* calc sin and cos from */
	                        sin2=(VectorAlgebra.dotv(tv2,tv3))/tmod;/* tv1,tv2 and tv3 */
	                }
	        }
	        if (VectorAlgebra.modv(ray.rd)>0){
	        	VectorAlgebra.normv(ray.rd);
	        }
	        doneOnce=true;
	        VectorAlgebra.rotz(-sin2,cos2,ray.rd,ray.rd);        /* perform final rotation */
	        VectorAlgebra.rotx(-sin1,cos1,ray.rd,ray.rd);        /* rotate ray direction vec */
	        VectorAlgebra.rotz(-sin3,cos3,ray.rd,ray.rd);
			
			//pitch
			//yaw
			//roll
			//perspective
			VectorAlgebra.mulv(vi.mag*PIXELSH/ray.rd[2], ray.rd, ray.rd);
			return ray.rd;
		}
		private double transform3DandRadiustoRadius(double coord[], double radius, View vi){
	        Raynode ray;            /* pointer to current light ray */
	        double tmod;                     /* temp store for modulus */
	        double tv1[]=new double[3];                   /* temporary vector */
	        double tv2[]=new double[3];                   /* temporary vector */
	        double tv3[]=new double[3];                   /* temporary vector */

	        ray = new Raynode();               /* make a ray with starting position */
	        VectorAlgebra.copyv(ray.ri,vi.co1);      /* from view point */

	        VectorAlgebra.subv(vi.co2,vi.co1,tv1);/* place in tv1 and tv2 the direction*/
	        VectorAlgebra.copyv(tv2,tv1);                 /* of view */

	        ray.rd[0]=coord[0]-ray.ri[0];                   /* set ray->rd to vector from main */
	        ray.rd[1]=coord[1]-ray.ri[1];
	        ray.rd[2]=coord[2]-ray.ri[2];

	        if (!doneOnce){             /* this works out the roll values */
	                sin3 = Math.sin(vi.rot); /* if rot=pi radians the output  */
	                cos3 = Math.cos(vi.rot); /* picture would be inverted */
	        }


	        if (!doneOnce){
	                tmod = VectorAlgebra.modv(tv1);
	                if (tmod < NEARZERO){   /* if modulus of tv1 is zero do not*/
	                        sin1=0.0;         /* rotate */
	                        cos1=1.0;
	                }
	                else{
	                        cos1=tv1[2]/tmod;       /* cos and sin of angle */
	                        sin1=Math.sqrt(1-cos1*cos1); /* between unit z vector and */
	                                                /* tv1 */
	                }

	                tv1[0]=tv1[1]=0.0;              /* carry out rotation on tv1 */
	                tv1[2]=1.0;
	                VectorAlgebra.rotx(sin1,cos1,tv1,tv1);
	        }

	        if (!doneOnce){
	                tv2[2]=0.0;tv1[2]=0.0;          /* make new ref vec tv3 */
	                VectorAlgebra.rotz(1.0,0.0,tv1,tv3);

	                tmod = VectorAlgebra.modv(tv2)*VectorAlgebra.modv(tv1);

	                if (tmod < NEARZERO){
	                        sin2=0.0;
	                        cos2=1.0;
	                }

	                else {
	                        cos2=(VectorAlgebra.dotv(tv1,tv2))/tmod;/* calc sin and cos from */
	                        sin2=(VectorAlgebra.dotv(tv2,tv3))/tmod;/* tv1,tv2 and tv3 */
	                }
	        }
	        doneOnce=true;
	        VectorAlgebra.rotz(-sin2,cos2,ray.rd,ray.rd);        /* perform final rotation */
	        VectorAlgebra.rotx(-sin1,cos1,ray.rd,ray.rd);        /* rotate ray direction vec */
	        VectorAlgebra.rotz(-sin3,cos3,ray.rd,ray.rd);
			
			//pitch
			//yaw
			//roll
			//perspective
			return radius*vi.mag*PIXELSH/ray.rd[2];			
		}
	}

}
