/* Kyle Cheng kwc9ap
 * CS 2110-HW4
 * Stone
 */
import java.util.Comparator;

public class CompareByCaption implements Comparator<Photograph> {


	@Override
	public int compare(Photograph o1, Photograph o2) {
		if (o1.getCaption().compareTo(o2.getCaption()) <= -1)
			return -1;
		if ((o1.getCaption().compareTo(o2.getCaption()))>= 1)
			return 1;
		if (o1.getCaption().compareTo(o2.getCaption()) == 0) {
			if(o1.getRating()>o2.getRating())
				return -1;
			if(o1.getRating()<o2.getRating())
				return 1;
			if(o1.getRating()==o2.getRating())
				return 0;
		}
		System.out.println("Error occured when comparing Photograph "+o1+" and "+o2);
		return 0;
	}
	

}
