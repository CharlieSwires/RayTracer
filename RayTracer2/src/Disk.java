
public class Disk {
	double outerRadius;
	double innerRadius;
	double position[] = new double[3];
	double ux[] = new double[3];
	double uy[] = new double[3];
	double uz[] = new double[3];
	double d;
	public Disk next;
    public Material mat;
	public MaterialTriangle mtt;
	double pl[]=new double[4];
	
	public Disk(double or, double ir,
			double posx, double posy, double posz,
			double uxx, double uxy, double uxz,
			double uyx, double uyy, double uyz,
			double uzx, double uzy, double uzz
			){
		outerRadius = or;
		innerRadius = ir;
		position[0] = posx;position[1] = posy;position[2] = posz;
		ux[0] = uxx; ux[1] = uxy; ux[2] = uxz;
		uy[0] = uyx; uy[1] = uyy; uy[2] = uyz;
		uz[0] = uzx; uz[1] = uzy; uz[2] = uzz;
		//Plane equation ax+by+cz = d
		d = uz[0]*position[0]+uz[1]*position[1]+uz[2]*position[2];
		VectorAlgebra.copyv( pl, uz);
		pl[3] = d;
		this.next = null;
	}
	public double rdcol(double ri[],double rd[])         /*works out if a ray intersects a sphere */

	/* ray start point, direction and sphere */

	{
		double alpha = VectorAlgebra.rpcol(ri, rd, pl);
		double col[] = new double[3];
		col = VectorAlgebra.addv(ri, VectorAlgebra.mulv(alpha, rd, col), col);
		double tempx[] = new double[3];
		double x = VectorAlgebra.dotv(ux, VectorAlgebra.subv(col,position,tempx));
		double tempy[] = new double[3];
		double y = VectorAlgebra.dotv(uy, VectorAlgebra.subv(col,position,tempy));
		double rr = x*x + y*y;
		if (rr <= outerRadius * outerRadius && rr >= innerRadius * innerRadius)
			return alpha;
		else return -1.0;
	}

}
