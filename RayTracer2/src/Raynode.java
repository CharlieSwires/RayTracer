
public class Raynode {
	/* This structure is used to store the light ray parameters. */
	        public double ri[]; /* starting point of ray */
	        public double rd[]; /* direction of ray */
	        int depth;   /* depth of ray, starts high then decrements until<1 */

	        public Raynode(){
	        	ri = new double[3];
	        	rd = new double[3];
	        	depth = 0;
	        }
}
