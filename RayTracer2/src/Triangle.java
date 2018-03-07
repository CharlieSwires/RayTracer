
public class Triangle {
	/* These are the primitive structures which make up a scene*/

	/* The triangle.*/
	/* this consists of three, three dimensional vertexes;                */
	/* cartesian equation of plane in which the triange lies;             */
	/* e1, e2, e3 contain values for planes passing through triangle edges*/
	/* a pointer to the material of the triangle;                         */
	/* and finally a pointer to the next triangle                         */

	        public double co1[];
	        public double co2[];
	        public double co3[];
	        public double pl[];
	        public double e1[];
	        public double e2[];
	        public double e3[];
	        public Material mat;
	        public Triangle next;
			public MaterialTriangle matt;
			public MaterialSphere mats;

	        public Triangle(){
		        co1 = new double[3];
		        co2 = new double[3];
		        co3 = new double[3];
		        pl = new double[4];
		        e1 = new double[4];
		        e2 = new double[4];
		        e3 = new double[4];
		        mat = null;
		        matt = null;
		        mats = null;
		        next= null;
	
	        }
}
