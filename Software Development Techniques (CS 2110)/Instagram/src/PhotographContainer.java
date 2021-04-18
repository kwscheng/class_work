/* Kyle Cheng kwc9ap
 * CS 2110-HW4
 * Stone
 */

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class PhotographContainer {
	
	protected String name;
	protected ArrayList<Photograph> photos;
	
	public PhotographContainer() {
		this.name="";
		this.photos = new ArrayList<Photograph>();
	}
	
	//creates a PhotographContainer with the given String as its name.
	public PhotographContainer(String name) {
		this.name = name;
		this.photos = new ArrayList<Photograph>(); 
	}
	
	//returns name of PhotographContainer
	public String getName() {
		return name;
	}
	
	/*sets hashcode of this object to its name.
	 * Thus, the equals method will be comparing the 
	 * names of the PhotographContainer
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

//sets name to a given String
	public void setName(String name) {
		this.name = name;
	}

	//returns an ArrayList of the photographs in this container
	public ArrayList<Photograph> getPhotos() {
		return photos;
	}
	
	/*adds a given Photograph to the photos ArrayList<Photograph> of 
	* this Photograph Container
	*/
	public boolean addPhoto(Photograph p) {
		if(p==null)
			return false;
		if (photos.contains(p)) {
			return false;
		}
		else {
			photos.add(p);
			return true;
		}
	//returns true or false whether this PhotographContainer contains a given photo
	}
	public boolean hasPhoto(Photograph p) {
		return photos.contains(p);
	}
	
	/*removes a given Photograph from the photos ArrayList<Photograph> 
	* of this PhotographContainer provided that photos contains the given Photograp.
	* Returns true if the Photograph is in photos.
	* Returns false if not
	*/
	public boolean removePhoto(Photograph p) {
		if (photos.contains(p)) {
			photos.remove(p);
			return true;
		}
		else 
			return false;
	}
	//returns the number of Photographs in the photos ArrayList<Photograph> of this PhotographContainer
	public int numPhotographs() {
		return photos.size();
	}
	
	public ArrayList<Photograph> getPhotos(int rating) {
		if((0>rating)||(rating>5))
			return null;
		ArrayList<Photograph> photosRated = new ArrayList<>();
		for (Photograph x : photos) {
			if (x.getRating() >= rating)
				photosRated.add(x); 
		}
		if (photosRated.size()==0)
			return new ArrayList<Photograph>();
		else
			return photosRated;
	}
	
	public Photograph getPhoto(File filename) {
		for(Photograph x: photos)
			if (x.getImageFile().equals(filename))
				return x; 
		return new Photograph("filename does not exist", "filename does not exist"); //returns a blank Photograph if the passed file does not correspond 
																					 //to a Photograph in this PhotographContainer's photos
	}

	public ArrayList<Photograph> getPhotosInYear(int year){
		try {
			if (year<=0 || year>=9999)
				return null;
			ArrayList<Photograph> photosYear = new ArrayList<>();
			for (Photograph x : photos)
				if (x.getDateTaken().substring(0,4).equals(Integer.toString(year)))
					photosYear.add(x);
			if (photosYear.size()==0)
				return new ArrayList<Photograph>();
			else
				return photosYear;
		}
		catch(Exception e){
			return null; //in case input is not correctly formatted, method will return null
		}
	}

	public ArrayList<Photograph> getPhotosInMonth(int month, int year){
		try {
			if (month <= 0 || month > 12 || year<=0 || year>=9999)
				return null;
			ArrayList<Photograph> photosMonth = new ArrayList<Photograph>();
			ArrayList<Photograph> photosYear = this.getPhotosInYear(year);
			DecimalFormat formatter = new DecimalFormat("00"); /*this is necessary because if user tries to pass the Month "March" for example, or "03" as an int, 
			 													*Java will assigned the int a value of 3, which messes with the YYYY-MM-DD format. This way, 
			 													*the double digit integrity of month can be maintained
			 													*/
			String monthFormatted = formatter.format(month);
			for (Photograph x : photosYear) 
				if (x.getDateTaken().substring(5,7).equals(monthFormatted))
					photosMonth.add(x);		
			if (photosMonth.size()==0) {
				return new ArrayList<Photograph>();
			}
			return photosMonth;
		}
		catch(Exception e) {
			return null;
		}
	}

	public ArrayList<Photograph> getPhotosBetween(String beginDate, String endDate) {
		try {
			SimpleDateFormat year_Month_Day = new SimpleDateFormat("yyyy-MM-dd"); 
			//Above creates a SimpleDateFormat that allows the creation of a Date object in YYYY-MM-DD format
			Date begin = year_Month_Day.parse(beginDate); //parse converts a string into a date object
			Date end = year_Month_Day.parse(endDate);
			int beginYear = Integer.parseInt(beginDate.substring(0,4));
			int beginMonth = Integer.parseInt(beginDate.substring(5,7));
			int beginDay = Integer.parseInt(beginDate.substring(8,10));
			int endYear = Integer.parseInt(endDate.substring(0,4));
			int endMonth = Integer.parseInt(endDate.substring(5,7));
			int endDay = Integer.parseInt(endDate.substring(8,10));

			if(beginYear< 0 || beginYear>9999 || beginMonth<= 0 || beginMonth > 12 || beginDay<=0 || beginDay>31 ||
					endYear< 0 || endYear>9999 || endMonth<= 0 || endMonth > 12 || endDay<=0 || endDay>31 || begin.after(end))
				return null;
			ArrayList<Photograph> photosBetween = new ArrayList<>();
			for (Photograph x : photos) {
				Date date = year_Month_Day.parse(x.getDateTaken());
				if (date.after(begin) && date.before(end))
					photosBetween.add(x);
			}		
			return photosBetween;
		}
		catch(Exception e) {
			return null;
		}
	}

	
	//standard equals method comparing the names of two PhotographContainers
	public boolean equals(Object o) {
		if (o==null) { 
			return false; }
		if(this.getClass()!=o.getClass())
			return false;
		PhotographContainer p = ((PhotographContainer)o);
		if(p.name.equals(this.name) && p.name.equals(this.name))
			return true;
		else
			return false;
	}


	//toString returning name and list of Photographs of this PhotographContainer 
	@Override
	public String toString() {
		return "PhotographContainer [name=" + name + ", photos=" + photos + "]";
	}
}
