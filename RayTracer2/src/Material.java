
public class Material {
	/* material properties, these are the interface characteristics of the*/
	/* primitives */
	        public double refi;      /* refractive index */
	        public double mat1;      /* absorbance of material +ve normal side */
	        public double mat2;      /* absorbance of material -ve normal side */
	        public double pcentmir;  /* percentage perfact mirror */
	        public double smooth;    /* Phong smoothness value for a surface */
	        public double Rd;        /* Diffuse reflectance of the material */
	        
	        public Material(){
	        	refi = 0.0;
	        	mat1 = 0.0;
	        	mat2 = 0.0;
	        	pcentmir = 0.0;
	        	smooth = 0.0;
	        	Rd = 0.0;
	        }
}
