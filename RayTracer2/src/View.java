
public class View {
	/* view point, this defines the viewing position, direction, magnification */
	/* and rotation */

	        double co1[];   /* viewing position */
	        double co2[];   /* co-ordinate in the required direction of view */
	        double mag;      /* magnification, 0.5 gives a wide angle view */
	        double rot;      /* rotation of image */
	        
	        public View(){
		        co1= new double[3];   /* viewing position */
		        co2= new double[3];   /* co-ordinate in the required direction of view */
		        mag = 0.0;      /* magnification, 0.5 gives a wide angle view */
		        rot= 0.0;      /* rotation of image */	        	
	        }
	        /* contains view from file */

}
