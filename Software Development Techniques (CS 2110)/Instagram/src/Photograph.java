import java.io.File;

/* Kyle Cheng kwc9ap
 * CS 2110-HW4
 * Stone
 */

public class Photograph implements Comparable<Photograph>{
	private String caption;
	private final String filename;
	private String dateTaken; //YYYY-MM-DD format
	private int rating; //Numerical rating of 0 to 5
	private File imageFile; 

	public void setDateTaken(String dateTaken) {
		this.dateTaken = dateTaken;
	}

	public void setRating(int rating) {
		if(rating>=0 && rating<=5)
		this.rating = rating;
	}

	public Photograph(String caption, String filename) {
		this.caption = caption;
		this.filename = filename;
		this.imageFile= new File(filename);
		this.dateTaken = "";
		this.rating = 0; 
	}
	public Photograph(String filename, String caption, String dateTaken, int rating) {
		this.filename=filename;
		this.caption=caption;
		this.imageFile = new File(filename);
		this.dateTaken=dateTaken;
		if(rating>=0||rating<=5)
			this.rating = rating;
		else
			System.out.println("rating must be between 0 and 5 inclusive");
	}

	@Override
	public int hashCode() {
		return (this.caption + "---" + this.filename).hashCode();
	}
	public String getDateTaken() {
		return dateTaken;
	}

	public int getRating() {
		return rating;
	}
	public String getCaption() {
		return caption;
	}

	/**
	 * @return the imageFile
	 */
	public File getImageFile() {
		return imageFile;
	}

	/**
	 * @param imageFile the imageFile to set
	 */
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}

	public String getFilename() {
		return filename;
	}

	public boolean equals(Object o) {
		if (o==null) { 
			return false; }
		if(this.getClass()!=o.getClass())
			return false;
		Photograph p = ((Photograph)o);
		if(p.caption.equals(this.caption) && p.filename.equals(this.filename))
			return true;
		else
			return false;
	}

	public String toString() {
		return "[caption=" + caption + ", filename=" + filename + "]";
	}
	//compares two photographs based on dateTaken. If dateTaken is the same, photograph captions alphabetically compared
	public int compareTo(Photograph p) {
		if ((this.getDateTaken().compareTo(p.getDateTaken()))<= -1)
			return -1;
		if ((this.getDateTaken().compareTo(p.getDateTaken()))>= 1)
			return 1;
		if ((this.getDateTaken().compareTo(p.getDateTaken()))== 0) {
			if (this.getCaption().compareTo(p.getCaption()) <= -1)
				return -1;
			if ((this.getCaption().compareTo(p.getCaption()))>= 1)
				return 1;
			if ((this.getCaption().compareTo(p.getCaption())) == 0)
				return 0;
		}
	
		System.out.println("Error occured when comparing Photograph "+this+" and "+p);
		return 0;
	}

}
