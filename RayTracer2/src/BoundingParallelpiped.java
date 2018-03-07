import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BoundingParallelpiped {
	/* These are the primitive structures which make up a scene*/

	@Override
	public String toString() {
		return "BoundingParallelpiped [alpha=" + alpha + ", inside=" + inside + ", tested=" + tested + ", pl1="
				+ Arrays.toString(pl1) + ", pl2=" + Arrays.toString(pl2) + ", pl3=" + Arrays.toString(pl3) + ", pl4="
				+ Arrays.toString(pl4) + ", pl5=" + Arrays.toString(pl5) + ", pl6=" + Arrays.toString(pl6)
				+ ", parentbv=" + (parentbv!=null) + ", containsbv=" + (containsbv!=null) + ", containsCylinders=" + (containsCylinders!=null)
				+ ", containsSpheres=" + (containsSpheres!=null) + ", containsTriangles=" + (containsTriangles!=null)
				+ ", containsDisks=" + (containsDisks!= null) + "]";
	}

	public double alpha = -1.0;
	public boolean inside;
	public boolean tested;
	public double pl1[];
	public double pl2[];
	public double pl3[];
	public double pl4[];
	public double pl5[];
	public double pl6[];
	public BoundingParallelpiped parentbv;
	public ArrayList<BoundingParallelpiped> containsbv;
	public ArrayList<Cylinder> containsCylinders;
	public ArrayList<Sphere> containsSpheres;
	public ArrayList<Triangle> containsTriangles;
	public ArrayList<Disk> containsDisks;

	public BoundingParallelpiped(
			double pl1[],
			double pl2[],
			double pl3[],
			double pl4[],
			double pl5[],
			double pl6[],
			BoundingParallelpiped parentbv,
			ArrayList<BoundingParallelpiped> containsbv,
			ArrayList<Cylinder> containsCylinders,
			ArrayList<Sphere> containsSpheres,
			ArrayList<Triangle> containsTriangles,
			ArrayList<Disk> containsDisks
			){
		this.pl1=pl1;
		this.pl2=pl2;
		this.pl3=pl3;
		this.pl4=pl4;
		this.pl5=pl5;
		this.pl6=pl6;
		this.parentbv = parentbv;
		this.containsbv=containsbv;
		this.containsCylinders=containsCylinders;
		this.containsSpheres=containsSpheres;
		this.containsTriangles=containsTriangles;
		this.containsDisks=containsDisks;
	}
	
	  public static Comparator<BoundingParallelpiped> alphaComparator = new Comparator<BoundingParallelpiped>() {

			public int compare(BoundingParallelpiped s1, BoundingParallelpiped s2) {
			   double StudentName1 = s1.alpha;
			   double StudentName2 = s2.alpha;

			   //ascending order
			   return (StudentName1 > StudentName2)? 1 : (StudentName1== StudentName2)?0:-1;

			   //descending order
			   //return StudentName2.compareTo(StudentName1);
		    }};

}
