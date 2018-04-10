
public class RayGenerator {
    private static final double NEARZERO = 0.0001;
	static double sin1;              /* sine value of first z axis rotation*/
	static double cos1;              /* cos value of first z axis rotation */
	static double sin2;              /* sine value of x axis rotation */
	static double cos2;              /* cos value of x axis rotation */
	static double sin3;              /* sin value of second z axis rotation*/
	static double cos3;              /* cos value of second z axis rotation*/

	/******************************************************************************/
	/*                                                                            */
	/* TITLE :  Testbed Raytracer (final year project)                            */
	/* CREATOR: C.E.L. Swires                                                     */
	/* DATE:    17/3/1990                                                         */
	/*                                                                            */
	/* FILE: rg.c                                                                 */
	/*                                                                            */
	/* FUNCTION : raygen()                                                        */
	/*                                                                            */
	/* RETURNED VALUE : pointer to a raynode                                      */
	/*                                                                            */
	/* DESCRIPTION : This procedure rotates the scanning vector produced in main  */
	/* according to the view parameters. Following the three rotations a raynode  */
	/* is produced and then passed back to main.                                  */
	/*                                                                            */
	/* FUNCTIONS CALLED : dotv(), modv(), subv(), rotz(), rotx(), rayalloc()      */
	/*                                                                            */
	/* RELATED DOCUMENTS :                                                        */
	/*                                                                            */
	/******************************************************************************/

	public static Raynode raygen(double x,double y,double z,View viewp, int done_once)

	/* components of initial ray direction */
	/* pointer to view structure */
	/* flag indicating that the rotation values have*/
	/* have been calculated */

	{
	        Raynode ray;            /* pointer to current light ray */
	        double tmod;                     /* temp store for modulus */
	        double tv1[]=new double[3];                   /* temporary vector */
	        double tv2[]=new double[3];                   /* temporary vector */
	        double tv3[]=new double[3];                   /* temporary vector */

	        ray = new Raynode();               /* make a ray with starting position */
	        VectorAlgebra.copyv(ray.ri,viewp.co1);      /* from view point */

	        VectorAlgebra.subv(viewp.co2,viewp.co1,tv1);/* place in tv1 and tv2 the direction*/
	        VectorAlgebra.copyv(tv2,tv1);                 /* of view */

	        ray.rd[0]=x;                   /* set ray->rd to vector from main */
	        ray.rd[1]=y;
	        ray.rd[2]=z;

	        if (done_once < 0){             /* this works out the roll values */
	                sin3 = Math.sin(viewp.rot); /* if rot=pi radians the output  */
	                cos3 = Math.cos(viewp.rot); /* picture would be inverted */
	        }

	        VectorAlgebra.rotz(sin3,cos3,ray.rd,ray.rd);

	        if (done_once < 0){
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
	        VectorAlgebra.rotx(sin1,cos1,ray.rd,ray.rd);        /* rotate ray direction vec */

	        if (done_once < 0){
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
	        VectorAlgebra.rotz(sin2,cos2,ray.rd,ray.rd);        /* perform final rotation */
	        if (VectorAlgebra.modv(ray.rd)>0){
	        	VectorAlgebra.normv(ray.rd);
	        }
	        return(ray);
	}


}
