import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class SceneReader {

	/******************************************************************************/
	/*                                                                            */
	/* TITLE :  Testbed Raytracer (final year project)                            */
	/* CREATOR: C.E.L. Swires                                                     */
	/* DATE:    2/4/1990                                                         */
	/*                                                                            */
	/* FILE : r.c                                                                 */
	/*                                                                            */
	/* PROCEDURE : env_read()                                                     */
	/*                                                                            */
	/* DESCRIPTION : This procedure reads the opened input file, a line at a time,*/
	/* converting the text information into an enviroment description. This       */
	/* description is in the form of: linked lists of light sources, spheres and  */
	/* triangles; a viewpoint structure; and a list of materials which is pointed */
	/* to by the triangle and sphere structures.                                  */
	/*                                                                            */
	/* FUNCTIONS CALLED : skipno(), matalloc(), litalloc(), trialloc(), spalloc(),*/
	/* crossv(), subv(), normv().                                                 */
	/*                                                                            */
	/* RELATED DOCUMENTS :                                                        */
	/*                                                                            */
	/******************************************************************************/

	/* maximum size of text line buffer, and number of commands */

	private static final int MAXBUF = 512;

	/* the array of strings which holds the commands used by the raytracer to */
	/* describe a scene */

	private static String command[]={
			"re",             /* remark, ignored by raytracer */
			"vp",             /* view point */
			"tr",             /* triangle */
			"sp",             /* sphere */
			"ls",             /* light source */
			"mp",              /* material */
			"tex",              /* material sphere */
			"clear",              /* material */
			"sky",              /* material */
			"ttx",              /* material */
			"clt",              /* material */
			"cyl",              /* cylinder */
			"tcx",              /* material cylinder */
			"clc",				/* clear material cylinder */
			"disk",				/* disk */
			"bounding",			/* bounding volume */
			"end"				/* end on bounding volume */
	};

	/**
	 * @throws IOException ****************************************************************************/
	public static void env_read(File fp,Enviro eptrs) throws IOException

	/* pointer to structure of initial pointers*/
	/* pointer to input file */

	{
		int i;                  /* character counter */
		int com;                /* command number, token */
		int cono;               /* co-ordinate counter */
		int c;                 /* single character from input file*/
		/* buffer to hold a line of text from input */
		/* file*/
		Material mp = null;    /* pointer to present material */
		Lights lp = null;      /* pointer to present light source */
		Triangle tp= null;    /* pointer to present triangle */
		Sphere sp = null;      /* pointer to present sphere */
		Cylinder cyl = null;      /* pointer to present cylinder */
		Disk disk = null;      /* pointer to present disk */
		BoundingParallelpiped bv = null;
		View viewpos = new View();
		MaterialSphere mps = null;
		MaterialTriangle mpt = null;
		MaterialCylinder mpc = null;
		boolean clearCalled = false;
		double d;                /* value of d in the plane equation*/
		double crs[] = new double[3];           /* normal to plane in which the triangle lies*/
		double tmp1[] = new double[3];          /* temporary vector for plane calculation*/
		double tmp2[] = new double[3];          /* temporary vector for plane calculation*/
		double tmp3[] = new double[3];          /* temporary vector for plane calculation*/

		FileReader fr = new FileReader(fp);

		mp=null;                        /* initialise material pointer */

		while ((c=fr.read())!=-1){      /* outer while */

			/*read in white spaces*/
			while (c==' '||c=='\n'||c=='\t'||c=='\r')
				c=fr.read();

			if (c == -1) break;    /* quit read EOF reached */

			/* check for command */
			if ((c < 'a' || c > 'z') && c != -1){

				System.out.println("\n I expected a command, sorry I cannot continue \n");
				System.exit(1);
			}


			i=0;    /* initialise character counter */

			StringBuffer buf= new StringBuffer();
			while (c != ';' && c != -1){   /* read in a line from file */

				if (i >= (MAXBUF-2)){
					System.out.println("input line exceeds the softwares limit of "+MAXBUF+" characters \n");
					System.exit(1);
				}

				buf.append((char)c);
				c=fr.read();
			}

			com=com_id(buf.toString());        /* tokenise command into a value */
			String parts[];
			switch (com){


			case 0: /* remark */

				i=command[com].length(); /* echo text line */
				//System.out.println("\nremark %s "+buf.toString().substring(i));
				break;


			case 1: /* view point */

				//System.out.println("\nview point ");
				/* check viewpoint has not */
				/* already been defined */
				if(eptrs.vi != null){
					System.out.println("\nError viewpoint has already been defined\n");
					System.exit(1);
				}

				/* read in first co-ordinate */

				eptrs.vi = viewpos;

				i = command[com].length();
				parts =buf.toString().substring(i).split(",");

				for (cono = 0; cono < 3; cono++) {
					viewpos.co1[cono] = Double.parseDouble(parts[cono].trim());
					// System.out.println(" "+viewpos.co1[cono]+", " );
				}

				/* read in second co-ordinate */

				for(cono=3;cono<6;cono++){
					viewpos.co2[cono-3] = Double.parseDouble(parts[cono].trim());
					// System.out.println(" "+viewpos.co2[cono-3]+", " );
				}

				/* read in magnification */

				viewpos.mag = Double.parseDouble(parts[cono++].trim());
				//System.out.println(" "+viewpos.mag+", " );

				/* read in rotation */

				viewpos.rot = (Math.PI / 180.0) *  Double.parseDouble(parts[cono++].replace(';', ' ').trim());
				// System.out.println(" "+viewpos.rot+", " );

				break;



			case 2: /* triangle */

				// System.out.println("\ntriangle ");

				if (eptrs.ti==null){
					tp = eptrs.ti = new Triangle();
					tp.next = null;
				}
				else {
					tp.next = new Triangle();
					tp = tp.next;
					tp.next = null;
				}

				i=command[com].length();
				parts =buf.toString().substring(i).split(",");

				for (cono = 0; cono < 3; cono++) {
					tp.co1[cono] = Double.parseDouble(parts[cono].trim());
					//System.out.println(" "+tp.co1[cono]+", " );
				}
				for (cono = 3; cono < 6; cono++) {
					tp.co2[cono-3] = Double.parseDouble(parts[cono].trim());
					//System.out.println(" "+tp.co2[cono-3]+", " );
				}
				for (cono = 6; cono < 9; cono++) {
					tp.co3[cono-6] = Double.parseDouble(parts[cono].trim());
					// System.out.println(" "+tp.co3[cono-6]+", " );
				}


				if (mp==null){
					System.out.println("\n error you have tried to define a triangle without first defining its material properties \n");
					System.exit(1);
				}

				tp.mat=mp;
				tp.matt = mpt;
				tp.mats = mps;
				/* this is the plane calculation  */

				VectorAlgebra.subv(tp.co2,tp.co1,tmp1);
				VectorAlgebra.subv(tp.co3,tp.co1,tmp2);
				VectorAlgebra.subv(tp.co3,tp.co2,tmp3);

				VectorAlgebra.normv(VectorAlgebra.crossv(tmp1,tmp2,crs));
				d = crs[0] * tp.co1[0] + crs[1] * tp.co1[1] + crs[2] * tp.co1[2];
				VectorAlgebra.copyv(tp.pl,crs);
				tp.pl[3]=d;

				/* edge plane 1 */

				VectorAlgebra.crossv(tp.pl,tmp1,crs);
				d = crs[0] * tp.co1[0] + crs[1] * tp.co1[1] + crs[2] * tp.co1[2];
				VectorAlgebra.copyv(tp.e1,crs);
				tp.e1[3]=d;

				/* edge plane 2 */

				VectorAlgebra.crossv(tmp2,tp.pl,crs);
				d = crs[0] * tp.co1[0] + crs[1] * tp.co1[1] + crs[2] * tp.co1[2];
				VectorAlgebra.copyv(tp.e2,crs);
				tp.e2[3]=d;

				/* edge plane 3 */

				VectorAlgebra.crossv(tp.pl,tmp3,crs);
				d = crs[0] * tp.co2[0] + crs[1] * tp.co2[1] + crs[2] * tp.co2[2];
				VectorAlgebra.copyv(tp.e3,crs);
				tp.e3[3]=d;

				if (bv != null && bv.containsTriangles == null){
					bv.containsTriangles = new ArrayList<Triangle>();
				}
				if (bv!= null) bv.containsTriangles.add(tp);
				break;



			case 3: /* sphere */

				//System.out.println("\nsphere ");

				if (eptrs.si==null){
					sp=eptrs.si=new Sphere();
					sp.next=null;
				}
				else{
					sp.next=new Sphere();
					sp=sp.next;
					sp.next=null;
				}
				i=command[com].length();
				parts =buf.toString().substring(i).split(",");

				for (cono = 0; cono < 4; cono++) {
					sp.r_cnt[cono] = Double.parseDouble(parts[cono].trim());
					//System.out.println(" "+sp.r_cnt[cono]+", " );
				}

				if (mp==null){
					System.out.println("\n error you have tried to define a sphere without first defining its material properties\n");
					System.exit(1);
				}

				sp.mat=mp;
				sp.mats=mps;

				if (bv != null && bv.containsSpheres == null){
					bv.containsSpheres = new ArrayList<Sphere>();
				}
				if (bv != null)bv.containsSpheres.add(sp);
				//if (bv != null)System.out.println(bv.toString());

				break;



			case 4: /* light source */

				//System.out.println("\nlight source  ");

				if (eptrs.li==null){
					lp=eptrs.li=new Lights();
					lp.next=null;
				}
				else{
					lp.next=new Lights();
					lp=lp.next;
					lp.next=null;
				}

				i=command[com].length();
				parts =buf.toString().substring(i).split(",");

				for (cono = 0; cono < 3; cono++) {
					lp.co[cono] = Double.parseDouble(parts[cono].trim());
					//System.out.println(" "+lp.co[cono]+", " );
				}
				lp.power= Double.parseDouble(parts[cono].trim());
				//System.out.println(" "+lp.power+"" );

				break;



			case 5: /* material properties */

				//System.out.println("\nmaterials ");
				mp = new Material();
				i=command[com].length();
				parts =buf.toString().substring(i).split(",");

				cono = 0;
				mp.refi = Double.parseDouble(parts[cono++].trim());
				//System.out.println(" "+mp.refi+", " );

				mp.mat1 = Double.parseDouble(parts[cono++].trim());
				//System.out.println(" "+mp.mat1+", " );

				mp.mat2 = Double.parseDouble(parts[cono++].trim());
				//System.out.println(" "+mp.mat2+", ");

				mp.pcentmir = Double.parseDouble(parts[cono++].trim());
				//System.out.println(" "+mp.pcentmir+", ");

				mp.smooth = Double.parseDouble(parts[cono++].trim());
				//System.out.println(" "+mp.smooth+", ");

				mp.Rd = Double.parseDouble(parts[cono].trim());
				//System.out.println(" "+mp.Rd);

				break;
			case 6: /* material properties sphere*/

				//System.out.println("\nmaterials sphere");
				i=command[com].length();
				parts =buf.toString().substring(i).split(",");
				cono = 0;
				mps = new MaterialSphere(
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						parts[cono++].trim(),
						parts[cono++].trim(),
						parts[cono++].trim().replace(";","")
						);
				break;
			case 7: /* material properties */

				//System.out.println("\nclear ");
				mps=null;
				break;
			case 8: /* material properties sky */
				eptrs.sky = mps;	
				break;
			case 9: /* material properties triangle*/
				//"ttx",              /* material */

				/*	MaterialTriangle(double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			String colourChannel, String file, String fileShiny){
				 */
				//System.out.println("\nmaterials sphere");
				i=command[com].length();
				parts =buf.toString().substring(i).split(",");
				cono = 0;
				mpt = new MaterialTriangle(
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						parts[cono++].trim(),
						parts[cono++].trim(),
						parts[cono++].trim().replace(";","")
						);
				break;
			case 10: /* material properties clear*/
				//"clt"              /* material */

				//System.out.println("\nclt ");
				mpt=null;
				break;
			case 11: /* cylinder*/
				i=command[com].length();
				parts =buf.toString().substring(i).split(",");
				cono = 0;
				Cylinder temp = new Cylinder(
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim().replace(";","")));
				if (eptrs.ci==null){
					cyl = eptrs.ci = temp;
					cyl.next = null;
				}
				else {
					cyl.next = temp;
					cyl = cyl.next;
					cyl.next = null;
				}
				cyl.mat=mp;
				cyl.mpc = mpc;
				if (bv != null && bv.containsCylinders == null){
					bv.containsCylinders = new ArrayList<Cylinder>();
				}
				if (bv!=null)bv.containsCylinders.add(cyl);

				break;
			case 12: /* material properties cylinder*/

				//System.out.println("\nmaterials sphere");
				i=command[com].length();
				parts =buf.toString().substring(i).split(",");
				cono = 0;
				mpc = new MaterialCylinder(
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						parts[cono++].trim(),
						parts[cono++].trim(),
						parts[cono++].trim().replace(";","")
						);

				break;
			case 13: /* material properties clear*/
				//"clc"              /* material */

				//System.out.println("\nclt ");
				mpc=null;
				break;
			case 14: /* disk*/
				i=command[com].length();
				parts =buf.toString().substring(i).split(",");
				cono = 0;
				Disk temp2 = new Disk(
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim()),
						Double.parseDouble(parts[cono++].trim().replace(";","")));
				if (eptrs.di==null){
					disk = eptrs.di = temp2;
					disk.next = null;
				}
				else {
					disk.next = temp2;
					disk = disk.next;
					disk.next = null;
				}
				disk.mat=mp;
				disk.mtt = mpt;
				
				if (bv != null && bv.containsDisks == null){
					bv.containsDisks = new ArrayList<Disk>();
				}
				if (bv != null)bv.containsDisks.add(disk);
				
				break;
			case 15: /* bounding volume */

				// System.out.println("\ntriangle ");


				double bvcords[][] = new double[5][3];
				i=command[com].length();
				parts =buf.toString().substring(i).replaceAll(";","").split(",");

				for (int i1 = 0; i1 < 5; i1++)
				for (int cono1 = 0; cono1 < 3; cono1++) {
					bvcords[i1][cono1] = Double.parseDouble(parts[cono1+i1*3].trim());
					//System.out.println(" "+tp.co1[cono]+", " );
				}


				/* this is the plane calculation  */

				double bvplanes[][] = new double[6][4];
				
				VectorAlgebra.subv(bvcords[0],bvcords[4],bvplanes[0]);
				bvplanes[0][3]= plane(bvplanes[0],bvcords[0]);
				VectorAlgebra.subv(bvcords[4],bvcords[0],bvplanes[1]);
				bvplanes[1][3]= plane(bvplanes[1],bvcords[4]);
				VectorAlgebra.subv(bvcords[1],bvcords[0],bvplanes[2]);
				bvplanes[2][3]= plane(bvplanes[2],bvcords[1]);
				VectorAlgebra.subv(bvcords[0],bvcords[1],bvplanes[3]);
				bvplanes[3][3]= plane(bvplanes[3],bvcords[0]);
				VectorAlgebra.subv(bvcords[2],bvcords[1],bvplanes[4]);
				bvplanes[4][3]= plane(bvplanes[4],bvcords[2]);
				VectorAlgebra.subv(bvcords[1],bvcords[2],bvplanes[5]);
				bvplanes[5][3]= plane(bvplanes[5],bvcords[1]);

				if (eptrs.bpi==null){
					bv = eptrs.bpi = new BoundingParallelpiped(
							bvplanes[0],
							bvplanes[1],
							bvplanes[2],
							bvplanes[3],
							bvplanes[4],
							bvplanes[5],
							null,
							null,
							null,
							null,
							null,
							null);
					bv.parentbv = null;
				}
				else {
					if (bv.containsbv == null){
						bv.containsbv = new ArrayList<BoundingParallelpiped>();
					}
					BoundingParallelpiped tempbv = new BoundingParallelpiped(
							bvplanes[0],
							bvplanes[1],
							bvplanes[2],
							bvplanes[3],
							bvplanes[4],
							bvplanes[5],
							bv,
							null,
							null,
							null,
							null,
							null);
					bv.containsbv.add(tempbv);
					bv = tempbv;
				}

				break;
			case 16: /* bounding volume end*/

				// System.out.println("\ntriangle ");

				bv = bv.parentbv;

				break;


			default:

				System.out.println("\ncommand unknown cannot finish reading in \n");
				System.exit(1);
			}/* end of switch */

		}/* end of outer while loop */
		fr.close();

	}

	private static double plane(double normal[], double coordinate[]){
		double d;
		d = normal[0] * coordinate[0] + normal[1] * coordinate[1] + normal[2] * coordinate[2];
		return d;
	}

	/*_________________________________________________________________________*/

	private static int com_id(String word)            /* tokenises commands, ie returns a number */
	/* representing a command */
	{
		int n;
		for (n=0;!word.startsWith(command[n]);n++)
			if (n>=(command.length-1))
				return(-1);
		return(n);
	}

}
