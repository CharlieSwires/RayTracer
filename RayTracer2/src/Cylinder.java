
public class Cylinder {
	
	double radius;
	double position[] = new double[3];
	double ux[] = new double[3];
	double uy[] = new double[3];
	double uz[] = new double[3];
	double iu[][] = new double[3][3];
	double iuInv[][] = new double[3][3];
	double determinant;
	
	double plane1[] = new double[4];
	double plane2[] = new double[4];
	public Cylinder next;
    public Material mat;
	public MaterialCylinder mpc;
	public double[] uxInv = new double[3];
	public double[] uyInv = new double[3];
	public double[] uzInv= new double[3];
	
	public Cylinder(double r,
			double posx, double posy, double posz,
			double alpha,
			double beta,
			double gamma,
			double plane1x, double plane1y, double plane1z, double plane1h,
			double plane2x, double plane2y, double plane2z, double plane2h
			){
		radius = r;
		position[0] = posx;position[1] = posy;position[2] = posz;

		plane1[0] = plane1x; plane1[1] = plane1y; plane1[2]=plane1z; plane1[3]=plane1h;
		plane2[0] = plane2x; plane2[1] = plane2y; plane2[2]=plane2z; plane2[3]=plane2h;
		double sina = Math.sin(alpha); double cosa = Math.cos(alpha);
		double sinb = Math.sin(beta); double cosb = Math.cos(beta);
		double sinc = Math.sin(gamma); double cosc = Math.cos(gamma);
		double Rx[][] = {{1.0,0.0,0.0},{0.0,cosa,-sina},{0.0,sina,cosa}};
		double Ry[][] = {{cosb,0.0,sinb},{0.0,1.0,0.0},{-sinb,0.0,cosb}};
		double Rz[][] = {{cosc,-sinc,0.0},{sinc, cosc,0.0},{0.0,0.0,1.0}};
		double temp[][] = new double[3][3];
		VectorAlgebra.matrix3x3Multiply(Rz, Ry, temp);
		VectorAlgebra.matrix3x3Multiply(temp,Rx,iu);
		double [] txv = {1.0,0.0,0.0};
//		VectorAlgebra.matrixMultiplyVect(iu, txv, ux);
		VectorAlgebra.copyv(ux, txv);
		double [] tyv = {0.0,1.0,0.0};
//		VectorAlgebra.matrixMultiplyVect(iu, tyv, uy);
		VectorAlgebra.copyv(uy, tyv);
		double [] tzv = {0.0,0.0,1.0};
//		VectorAlgebra.matrixMultiplyVect(iu, tzv, uz);
		VectorAlgebra.copyv(uz, tzv);
		double tempv[] = new double[3];
		sina = Math.sin(-alpha); cosa = Math.cos(-alpha);
		sinb = Math.sin(-beta); cosb = Math.cos(-beta);
		sinc = Math.sin(-gamma); cosc = Math.cos(-gamma);
		double RxInv[][] = {{1.0,0.0,0.0},{0.0,cosa,-sina},{0.0,sina,cosa}};
		double RyInv[][] = {{cosb,0.0,sinb},{0.0,1.0,0.0},{-sinb,0.0,cosb}};
		double RzInv[][] = {{cosc,-sinc,0.0},{sinc, cosc,0.0},{0.0,0.0,1.0}};
		VectorAlgebra.matrix3x3Multiply(RxInv, RyInv, temp);
		VectorAlgebra.matrix3x3Multiply(temp,RzInv,iuInv);
		VectorAlgebra.matrixMultiplyVect(iuInv,plane1,tempv);
		VectorAlgebra.copyv(plane1, tempv);
		VectorAlgebra.matrixMultiplyVect(iuInv,plane2,tempv);
		VectorAlgebra.copyv(plane2, tempv);
		VectorAlgebra.matrixMultiplyVect(iuInv, txv, uxInv);
		VectorAlgebra.matrixMultiplyVect(iuInv, tyv, uyInv);
		VectorAlgebra.matrixMultiplyVect(iuInv, tzv, uzInv);
		this.next = null;
	}
	public double rccol(double ri[],double rd[])         /*works out if a ray intersects a sphere */

	/* ray start point, direction and sphere */

	{
	        double a,b,c;            /* quadratic equn coefficients */
	        double col1,col2;        /* two collision values */
	        double rdir[] = new double[3];
	        VectorAlgebra.subv(ri, position, rdir);
	        double xa = rd[0]*iu[0][0]+rd[1]*iu[0][1]+rd[2]*iu[0][2];
	        double ya = rd[0]*iu[1][0]+rd[1]*iu[1][1]+rd[2]*iu[1][2];
	        double za = rd[0]*iu[2][0]+rd[1]*iu[2][1]+rd[2]*iu[2][2];
	        double xaa= xa*ux[0]+ya*ux[1]+za*ux[2];
	        double yaa= xa*uy[0]+ya*uy[1]+za*uy[2];
	        a = xaa*xaa+yaa*yaa;
	        double xc = rdir[0]*iu[0][0]+rdir[1]*iu[0][1]+rdir[2]*iu[0][2];
	        double yc = rdir[0]*iu[1][0]+rdir[1]*iu[1][1]+rdir[2]*iu[1][2];
	        double zc = rdir[0]*iu[2][0]+rdir[1]*iu[2][1]+rdir[2]*iu[2][2];
	        double xcc= xc*ux[0]+yc*ux[1]+zc*ux[2];
	        double ycc= xc*uy[0]+yc*uy[1]+zc*uy[2];
	        b = (xaa*xcc+yaa*ycc) * 2.0;
	        c = xcc*xcc+ycc*ycc-radius*radius;

	        if ((b*b)<(4*a*c))      /* roots complex, no collision */
	                return(-1.0);

	        	                                /* otherwise roots real calculate */

	        col1=(-b+Math.sqrt(b*b-4*a*c))/2/a;
	        col2=(-b-Math.sqrt(b*b-4*a*c))/2/a;
	                                /* return lowest positive root */
	        double cc1[] =  new double[3];
	        cc1 = VectorAlgebra.addv(VectorAlgebra.mulv(col1, rd, cc1),rdir,cc1);
	        
	        double h11 = plane1[0]*cc1[0] + plane1[1]*cc1[1] + plane1[2]*cc1[2];
	        double h12 = plane2[0]*cc1[0] + plane2[1]*cc1[1] + plane2[2]*cc1[2];
	        double cc2[] =  new double[3];
	        cc2 = VectorAlgebra.addv(VectorAlgebra.mulv(col2, rd, cc2),rdir,cc2);
	        
	        double h21 = plane1[0]*cc2[0] + plane1[1]*cc2[1] + plane1[2]*cc2[2];
	        double h22 = plane2[0]*cc2[0] + plane2[1]*cc2[1] + plane2[2]*cc2[2];
	        
	        if (h21 > plane1[3] && h22 < plane2[3] && h11 > plane1[3] && h12 < plane2[3] && col1 < col2 && col2 > 0.0 && col1 > 0.0){
	        	return col1 ;
	        }
	        else if (h21 > plane1[3] && h22 < plane2[3] && h11 > plane1[3] && h12 < plane2[3] && col1 > col2 && col2 > 0.0 && col1 > 0.0){
	        	return col2 ;
	        }
	        else if (h21 > plane1[3] && h22 < plane2[3] && col2 > 0.0){
	        	return col2;
	        }
	        else if (h11 > plane1[3] && h12 < plane2[3] && col1 > 0.0){
	        	return col1;
	        }
	        else return -1.0;
	}

}
