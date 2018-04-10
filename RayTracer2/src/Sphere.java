
public class Sphere {

/* The Sphere */
/* r_cnt contains the sphere's central position and radius respectively; */
/* a pointer to the sphere's material;                                   */
/* a pointer to the next sphere in the linked list;                      */

        public double r_cnt[];
        public Material mat;
        public MaterialSphere mats;
        public Sphere next;
        
        public Sphere(){
        	r_cnt = new double[4];
        	mat = null;
        	mats = null;
        	next = null;
        }
}
