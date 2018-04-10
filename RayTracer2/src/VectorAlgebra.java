
public class VectorAlgebra {

	/******************************************************************************/
	/*                                                                            */
	/* TITLE :  Testbed Raytracer (final year project)                            */
	/* CREATOR: C.E.L. Swires                                                     */
	/* DATE:    2/4/1990                                                          */
	/*                                                                            */
	/* FILE va.c                                                                  */
	/*                                                                            */
	/* DESCRIPTION : This file contains the vector algebra maths functions, which */
	/* the rest of the software uses to implement their functions.                */
	/*                                                                            */
	/* FUNCTIONS : dotv, mulv, addv, subv, crossv, modv, normv, reflect, refract, */
	/*             copyv, Fr, Ft, rscol, rpcol, rotx, roty, rotz                  */
	/*                                                                            */
	/* RELATED DOCUMENTS :                                                        */
	/*                                                                            */
	/******************************************************************************/


	public static double dotv(double a[],double b[])    /* dot product of two vectors */
	{
		double prod1,prod2,prod3;
		double sum;

		prod1 = a[0] * b[0];
		prod2 = a[1] * b[1];
		prod3 = a[2] * b[2];
		sum = prod1 + prod2 +prod3;

		return(sum);
	}

	/*____________________________________________________________________________*/

	public static double modv(double a[])     /* find the modulus of a vector */
	{
		double x,y,z;    /* stores for 1D values */

		x=a[0];
		y=a[1];
		z=a[2];

		return( Math.sqrt(x*x+y*y+z*z));
	}

	/*___________________________________________________________________________*/

	public static double [] addv(double a[],double b[],double c[])     /* add two vectors */
	{
		c[0] = a[0] + b[0];
		c[1] = a[1] + b[1];
		c[2] = a[2] + b[2];

		return (c);
	}

	/*__________________________________________________________________________*/

	public static double [] subv(double a[],double b[],double c[])    /* subtraction of two vectors */
	{
		c[0] = a[0] - b[0];
		c[1] = a[1] - b[1];
		c[2] = a[2] - b[2];

		return (c);
	}

	/*__________________________________________________________________________*/

	public static double [] mulv(double a,double b[],double c[])   /* multiply a vector by a scalar */
	{
		c[0] = a * b[0];
		c[1] = a * b[1];
		c[2] = a * b[2];

		return(c);
	}

	/*__________________________________________________________________________*/

	public static void copyv(double a[],double b[])     /* copy vector b in vector a */
	{
		a[0] = b[0];
		a[1] = b[1];
		a[2] = b[2];
	}

	/*___________________________________________________________________________*/

	public static double [] crossv(double a[],double b[],double c[])    /*find the cross vector from two others */
	{

		double x,y,z;    /* temporary stores for orthogonal values */

		x= a[1] * b[2] - a[2] * b[1];
		y= a[2] * b[0] - a[0] * b[2];
		z= a[0] * b[1] - a[1] * b[0];

		c[0]=x;
		c[1]=y;
		c[2]=z;

		/*the temporary variables are used to prevent*/
		/*problems if one of the input vectors matches*/
		/*the output file*/

		return (c);
	}

	/*___________________________________________________________________________*/

	public static double [] reflect(double i[],double n[],double r[])   /* reflect a vector from the plane with the normal n */
	/* i[] and n[] must be unit vectors */
	{
		double ref[] =subv(i,mulv(2*dotv(i,n),n,r),r);
		double n1[]=new double[3];
		copyv(n1,ref);
		if(modv(n1)>0){
			normv(n1);
		}
		return n1;
	}

	/*___________________________________________________________________________*/

	public static double [] normv(double a[])        /* make a vector into a unit vector */
	{
		double modulus;

		modulus = modv(a);
		if (modulus > 0) {
			mulv(1/modulus,a,a);
		}
		return(a);
	}

	/*___________________________________________________________________________*/

	public static double[][] matrix3x3Multiply (double[][] left, double[][] right, double[][] result){
		for (int column = 0 ; column < 3; column++){
			for (int row= 0;row < 3;row++){
				result[row][column]=left[row][0]*right[0][column]+left[row][1]*right[1][column]+left[row][2]*right[2][column];
			}
		}
		return result;
	}
	/*___________________________________________________________________________*/

	public static double[][] matrix3x3Clone (double[][] left, double[][] right){
		for (int column = 0 ; column < 3; column++){
			for (int row= 0;row < 3;row++){
				left[row][column]= right[row][column];
			}
		}
		return left;
	}
	/*___________________________________________________________________________*/

	public static double[] matrixMultiplyVect (double[][] left, double[] right, double[] result){

		for (int row= 0;row < 3;row++){
			result[row]=left[row][0]*right[0]+left[row][1]*right[1]+left[row][2]*right[2];
		}

		return result;
	}

	/*___________________________________________________________________________*/

	public static double [] refract(double i[],double n[],double t[],double rind)   /* refract i[] placing the result in r[] */
	/* i[] and n[] should be unit vectors */

	/* incident, normal and transmitted vectors refractive index */

	{
		double cosi;     /* cosine of incident angle */
		double tn[]= new double[3];    /* temporary normal */
		double sin2r;    /* sine squared refraction angle */
		double sinr;     /* sine refraction angle */
		double cosr;     /* cosine refraction angle */
		double tni[]= new double[3];   /* tangential to incident -> tang component to ref*/
		double nmi[]=new double[3];   /* normal component of refracted ray */

		if (rind<0.00001) {        /* total reflection */
			t[1]=t[2]=t[0]=0;
			return (t);
		}

		cosi=(-dotv(i,n));      /* find the cosine of the angle of incidence */

		if (cosi<0){          /* the  angle of incidence is > 90 degrees */
			rind=1/rind;
			mulv(-1.0,n,tn);
			cosi=(-cosi);
		}
		else{
			copyv(tn,n);
		}
		sin2r=(1-cosi*cosi)/(rind*rind);   /* find sin squared angle of */
		/* refraction */

		if ((sin2r<=0.999999)&&(sin2r>=0.0)){     /* if total internal reflection has */
			/* not occured*/

			sinr=Math.sqrt(sin2r);        /* find sin and cos of refraction */
			/* angle */
			cosr=Math.sqrt(1-sin2r);
			subv(i,mulv((-cosi),tn,tni),tni);/* find tangential component */
			/* of incident */
			/* vector */
			mulv(sinr,normv(tni),tni);       /* find the tangential */
			/* component of the */
			/* refracted ray */

			mulv(cosr,tn,nmi);               /* find the  normal component*/
			/*  of the refracted ray*/

			subv(tni,nmi,t);                 /* combine the two components*/
			/*  to give the resultant ray*/

		}
		else{                            /* total internal reflection ,no */
			/* transmitted ray */
			t[1]=t[2]=t[0]=0;
		}
		if (modv(t)>0){
			normv(t);
		}
		return(t);
	}

	/*___________________________________________________________________________*/


	public static double Fr(double cosi,double refind)     /* find the Fresnel coeficient of reflection */

	/* cosine incident angle, refractive index */

	{
		double cosr;     /* cosine refraction angle */
		double cos2r;    /* squared cosine of refraction angle */
		double temprv;   /* Fresnel reflection coeff vertical polarisation */
		double temprh;   /* Fresnel reflection coeff horizontal polarisation */
		double absv;     /* absolute reflection coeff vertical */
		double absh;     /* absolute reflection coeff horizontal */

		if (refind<0.00000001){     /* refractive index is zero, only reflection */
			return(1.00);
		}


		cosi = -cosi;

		if (cosi<0){            /* incident ray in medium2 */
			refind=1/refind;
			cosi=(-cosi);
		}

		cos2r=1-(1-cosi*cosi)/refind/refind;  /* cosine square of refraction */
		/* angle*/

		if((cos2r<=1)&&(cos2r>0)){      /* if refraction has occured */
			cosr=Math.sqrt(cos2r);
			/* Fresnel equation */

			temprv=(cosi-refind*cosr)/(cosi+refind*cosr);
			temprh=(cosr-refind*cosi)/(cosr+refind*cosi);
			absv=temprv*temprv;
			absh=temprh*temprh;

			return Math.sqrt((absv+absh)/2.0);  /* average for unpolarised ray */
		}
		else{                       /* no refraction */
			return(1.00);
		}
	}

	/*___________________________________________________________________________*/

	public static double Ft(double cosi,double refind)          /* Fresnel transmittance value */

	/* cosine incident angle, refractive index */

	{

		return(1-Fr(cosi,refind));
	}

	/*__________________________________________________________________________*/

	public static double rpcol(double ri[],double rd[],double pl[])         /* works out if a ray intersects with a plane */

	/* ray start pt, ray direction, plane */

	{
		double norm[]= new double[3];
		double denom;     /* normal to plane, denominator val*/

		copyv(norm,pl);
		denom=dotv(rd,norm);

		if(denom==0)            /* ray parallel to plane */
			return(-1);

		/* ray collides with plane */
		return((pl[3]-dotv(norm,ri))/denom);
	}

	/*__________________________________________________________________________*/
	private static final double VALID_COL= 1e-4;


	public static void bvcol(double ri[], double rd[], BoundingParallelpiped bv){
		double alpha = -1.0;            /* value returned by rpcol() */
		double colco[]=new double[3];         /* collision co-ordinate */
		double lhs;              /* left hand side cartesian eqn of a plane */
		double rhs;              /* right hand side cartesian eqn of a plane */

		bv.alpha = -1.0;
		bv.inside = false;
		if (dotv(bv.pl1,rd) > 0.0){
			alpha = rectcol(ri, rd, bv.pl1, bv.pl3,bv.pl4,bv.pl5,bv.pl6);
			bv.alpha = alpha;
			if (alpha > VALID_COL){
				bv.inside=true;
				return;
			}
//			alpha = rectcol(ri, rd, bv.pl2, bv.pl3,bv.pl4,bv.pl5,bv.pl6);
//			bv.alpha = alpha;
//			if (alpha > VALID_COL){
//				bv.inside=true;
//				return;
//			}
		}else{
			alpha = rectcol(ri, rd, bv.pl2, bv.pl3,bv.pl4,bv.pl5,bv.pl6);
			bv.alpha = alpha;
			if (alpha > VALID_COL){
				bv.inside=true;
				return;
			}
//			alpha = rectcol(ri, rd, bv.pl1, bv.pl3,bv.pl4,bv.pl5,bv.pl6);
//			bv.alpha = alpha;
//			if (alpha > VALID_COL){
//				bv.inside=true;
//				return;
//			}
		}
		if (dotv(bv.pl3,rd) > 0.0){
			alpha = rectcol(ri, rd, bv.pl3, bv.pl1,bv.pl2,bv.pl5,bv.pl6);
			bv.alpha = alpha;
			if (alpha > VALID_COL){
				bv.inside=true;
				return;
			}
//			alpha = rectcol(ri, rd, bv.pl4, bv.pl1,bv.pl2,bv.pl5,bv.pl6);
//			bv.alpha = alpha;
//			if (alpha > VALID_COL){
//				bv.inside=true;
//				return;
//			}
		}else{
			alpha = rectcol(ri, rd, bv.pl4, bv.pl1,bv.pl2,bv.pl5,bv.pl6);
			bv.alpha = alpha;
			if (alpha > VALID_COL){
				bv.inside=true;
				return;
			}
//			alpha = rectcol(ri, rd, bv.pl3, bv.pl1,bv.pl2,bv.pl5,bv.pl6);
//			bv.alpha = alpha;
//			if (alpha > VALID_COL){
//				bv.inside=true;
//				return;
//			}
		}
		if (dotv(bv.pl5,rd) > 0.0){
			alpha = rectcol(ri, rd, bv.pl5, bv.pl1,bv.pl2,bv.pl3,bv.pl4);
			bv.alpha = alpha;
			if (alpha > VALID_COL) {
				bv.inside=true;
				return;
			}
//			alpha = rectcol(ri, rd, bv.pl6, bv.pl1,bv.pl2,bv.pl3,bv.pl4);
//			bv.alpha = alpha;
//			if (alpha > VALID_COL){
//				bv.inside=true;
//				return;
//			}
		}else{
			alpha = rectcol(ri, rd, bv.pl6, bv.pl1,bv.pl2,bv.pl3,bv.pl4);
			bv.alpha = alpha;
			if (alpha > VALID_COL) {
				bv.inside=true;
				return;
			}
//			alpha = rectcol(ri, rd, bv.pl5, bv.pl1,bv.pl2,bv.pl3,bv.pl4);
//			bv.alpha = alpha;
//			if (alpha > VALID_COL){
//				bv.inside=true;
//				return;
//			}
			
		}
	}
	/*___________________________________________________________________________*/

	private static double rectcol(double[] ri, double[] rd, double[] pl1, 
			double[] pl3, double[] pl4, double[] pl5, double[] pl6) 
	{
		double alpha;            /* value returned by rpcol() */
		double colco[]=new double[3];         /* collision co-ordinate */
		double lhs;              /* left hand side cartesian eqn of a plane */
		double rhs;              /* right hand side cartesian eqn of a plane */

		alpha = VectorAlgebra.rpcol (ri, rd, pl1); /* find rays collision with plane */
		if (alpha <= 0.0) return (-1.0);

		VectorAlgebra.mulv (alpha, rd, colco);        /*using alpha value workout collision*/
		VectorAlgebra.addv (colco, ri, colco);        /* co-ordinate */

		/* work out if collision is above */
		/* edge1 bounding plane */

		lhs = pl3[0] * colco[0] + pl3[1] * colco[1] + pl3[2] * colco[2];
		rhs = pl3[3];

		if (lhs > rhs) return (-1.0);     /* no collision pt below */

		/* work out if collision is above */
		/* edge2 bounding plane */

		lhs = pl4[0] * colco[0] + pl4[1] * colco[1] + pl4[2] * colco[2];
		rhs = pl4[3];

		if (lhs > rhs) return (-1.0);     /* no collision pt below */

		/* work out if collision is above */
		/* edge3 bounding plane */

		lhs = pl5[0] * colco[0] + pl5[1] * colco[1] + pl5[2] * colco[2];
		rhs = pl5[3];
		if (lhs > rhs) return (-1.0);     /* no collision pt below */

		lhs = pl6[0] * colco[0] + pl6[1] * colco[1] + pl6[2] * colco[2];
		rhs = pl6[3];
		if (lhs > rhs) return (-1.0);     /* no collision pt below */

		return alpha;
	}

	public static double rscol(double ri[],double rd[],double sp[])         /*works out if a ray intersects a sphere */

	/* ray start point, direction and sphere */

	{
		double a,b,c;            /* quadratic equn coefficients */
		double col1,col2;        /* two collision values */
		double difx,dify,difz;   /* difference between sphere centre and ray */
		/* start */

		difx = ri[0]-sp[0];
		dify = ri[1]-sp[1];
		difz = ri[2]-sp[2];

		a = rd[0] * rd[0] + rd[1] * rd[1] + rd[2] * rd[2];
		b = (rd[0] * difx + rd[1] * dify + rd[2] * difz) * 2;
		c = difx * difx + dify * dify + difz * difz - sp[3] * sp[3];

		if ((b*b)<(4*a*c))      /* roots complex, no collision */
			return(-1);

		/* otherwise roots real calculate */

		col1=(-b+Math.sqrt(b*b-4*a*c))/2/a;
		col2=(-b-Math.sqrt(b*b-4*a*c))/2/a;

		/* return lowest positive root */
		if(col1>0 && col1<col2) return(col1);
		return(col2);
	}

	/*____________________________________________________________________________*/

	public static void rotx(double s,double c,double r1[],double r2[])         /* rotation x component invariant */

	/* sine, cosine of rotation angle first 3D vector, resultant 3D vector */

	{

		double x,y,z;    /* temp stores used to prevent problems if r1=r2 */

		x=r1[0];
		y=r1[2]*s+r1[1]*c;
		z=r1[2]*c-r1[1]*s;
		r2[0]=x;
		r2[1]=y;
		r2[2]=z;
	}

	/*___________________________________________________________________________*/

	public static void roty(double s,double c,double r1[],double r2[])         /* rotation y component invariant */

	/* sine, cosine of rotation angle first 3D vector, resultant 3D vector */

	{
		double x,y,z;    /* temp stores used to prevent problems if r1=r2 */

		x=r1[0]*c-r1[2]*s;
		y=r1[1];
		z=r1[0]*s+r1[2]*c;
		r2[0]=x;
		r2[1]=y;
		r2[2]=z;
	}

	/*___________________________________________________________________________*/

	public static void rotz(double s,double c,double r1[],double r2[])         /* rotation z component invariant */

	/* sine, cosine of rotation angle first 3D vector, resultant 3D vector */

	{
		double x,y,z;    /* temp stores used to prevent problems if r1=r2 */

		x=r1[0]*c-r1[1]*s;
		y=r1[0]*s+r1[1]*c;
		z=r1[2];
		r2[0]=x;
		r2[1]=y;
		r2[2]=z;
	}

	public static void main(String args[]){

		double ri[]={0.0,0.0,0.0};
		double rd[]={1.0,0.0,0.0};
		double pl1[]={1.0,0.0,0.0,10.0};
		double pl2[]={0.0,1.0,0.0,10.0};
		double pl3[]={0.0,-1.0,0.0,10.0};
		double pl4[]={0.0,0.0,1.0,10.0};
		double pl5[]={0.0,0.0,-1.0,10.0};

		System.out.println("alpha ="+rectcol(ri, rd, pl1, 
				pl2, pl3, pl4,pl5)); 
		System.out.println("alpha ="+rpcol(ri, rd, pl1)); 


	}
}
