
public class Contactpt {
	/* This structure holds the information from the object collision routine */
	public double norm[]; /* normal of the nearest surface which the ray */
	/* hit */
	public double cc[]; /* the co-ordinate of the collision */
	public Material mat; /* a pointer to the material in the collision */
	public MaterialSphere mats;
	public MaterialTriangle matt;
	public MaterialSphere matts;
	public MaterialCylinder mpc;
	public double frTransmission;

	public Contactpt() {
		norm = new double[3];
		cc = new double[3];
		mat = null;
		mats = null;
		matt = null;
		matts = null;
		mpc = null;
		frTransmission = 1.0;
	}

	public void copy(Contactpt bc) {
		this.norm = bc.norm;
		this.cc = bc.cc;
		this.mat = bc.mat;
		this.mats = bc.mats;
		this.matt = bc.matt;
		this.matts = bc.matts;
		this.mpc = bc.mpc;
		this.frTransmission = bc.frTransmission;
	}

}
