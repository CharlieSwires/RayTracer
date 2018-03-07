
public class Lights {
	/* light source, this is a point light source with radiosity equal in all */
	/* directions.*/
	        public double co[];         /* three dimensional co-ordinate */
	        public double power;         /* radiosity (arbitary units) */
	        public Lights next; /* pointer to the next light source*/

	        public Lights(){
	        	co = new double[3];
	        	power = 0.0;
	        	next = null;
	        }
}
