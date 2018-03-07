
public class Enviro {
	/* This structure is used to store the initial locations of all the objects*/
	        public Triangle ti; /* pointer to the first triangle*/
	        public Sphere si;   /* pointer to the first sphere */
	        public Cylinder ci;   /* pointer to the first sphere */
	        public Disk di;   /* pointer to the first disk */
	        public Lights li;   /* pointer to  the first light */
	        public View vi;     /* pointer to the view position */
	        public MaterialSphere sky;
	        public BoundingParallelpiped bpi;
	        public Enviro(){
	        	ti = null;
	        	si = null;
	        	ci = null;
	        	li = null;
	        	vi = null;
	        	sky = null;
	        	di = null;
	        	bpi = null;
	        }

}
