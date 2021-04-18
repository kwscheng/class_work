/* Kyle Cheng kwc9ap
 * CS 2110-HW4
 * Stone
 */
 

import java.util.ArrayList;
import java.util.HashSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class PhotoLibrary extends PhotographContainer {

	
	private final int id;
	private HashSet<Album> albums = new HashSet<>();

	public PhotoLibrary(String name, int id) {
		this.name=name;
		this.id=id;
	}


	public HashSet<Album> getAlbums() {
		return albums;
	}


	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public ArrayList<Photograph> getPhotos(){
		return photos;
	}

		public boolean createAlbum(String albumName) { //creates an album with the name passed
		for (Album x : albums)
			if (x.getName().equals(albumName))
				return false;
		Album x = new Album(albumName);
		albums.add(x);
		return true;
	}

	public boolean removeAlbum(String albumName) { //removes the album with the name passed 
		for (Album x : albums)
			if ((x.getName().equals(albumName)))
			{
				albums.remove(x);
				return true;
			}
		return false;
		
	}
	public boolean addPhotoToAlbum(Photograph p, String albumName) {
		if(getAlbumByName(albumName)==null)
			return false;
		if(p==null)
			return false;
		if(photos.contains(p)) {
		getAlbumByName(albumName).addPhoto(p);
		return true;
		}
		else
			return false;
	}
	public boolean removePhotoFromAlbum(Photograph p, String albumName) {
		for (Album album : this.albums) {
			if (album.getName().contentEquals(albumName)) {
				for( Photograph photo : album.getPhotos()) 
					if(photo.equals(p)) {
					album.removePhoto(p);
					return true;
					}
				return false;
			}
		}
		return false;
	}
	private Album getAlbumByName(String albumName) {
		for (Album x : albums)
			if(x.getName().equals(albumName))
				return x;
		return null;
	}
	
	@Override
	public boolean removePhoto(Photograph p) {
		for (Photograph photo : photos) {
			if (photo.equals(p)) {
				photos.remove(p);
				for (Album album : this.albums)
					if (album.removePhoto(p))
						album.removePhoto(p);
				return true;
			}
				
		}
			return false;

	}

	public boolean equals(Object x) {
		if (x==null) { 
			return false; }
		if(this.getClass()!=x.getClass())
			return false;
		PhotoLibrary p = (PhotoLibrary)x;
		return this.id==p.getId();
	}

	@Override
	public String toString() {
		return "PhotoLibrary [name=" + name + ", id=" + id + ", photos=" + photos + ", albums=" + albums + "]";
	}


	public static ArrayList<Photograph> commonPhotos(PhotoLibrary a, PhotoLibrary b){
		ArrayList<Photograph> commPhotos = new ArrayList<Photograph>();
		for( Photograph x : a.getPhotos()) {
			for( Photograph y: b.getPhotos()) {
				if(x.equals(y))    
					commPhotos.add(x);
			}	    
		}  
		/*above for loop goes through every photos of Person a and if Person b has the same photo, 
		the photo is added to an ArrayList of Photographs (commPhotos) made to hold the photos in common*/
		return commPhotos;

	}
	public static double similarity(PhotoLibrary a, PhotoLibrary b) {
		double smaller;
		if (a.photos.size()==0 || b.photos.size()==0)
			return 0.0;
		else {
			smaller = (double)(Math.min(a.photos.size(),b.photos.size()));
			return (commonPhotos(a,b).size())/smaller;
		}

	}
	public static void main(String[] args) {
		PhotoLibrary robert = new PhotoLibrary("Robert",0);
		PhotoLibrary thomas = new PhotoLibrary("Thomas",1);
		System.out.println(robert);
		System.out.println(thomas);
		Photograph beach = new Photograph("Beach","beachpicture","2000-03-25",2);
		Photograph forest = new Photograph("Forest","forestpicture","2000-03-13",3);
		Photograph mountains = new Photograph("Mountains","mountainpicture","1999-05-02",0);
		Photograph desert = new Photograph("Desert","desertpicture","1998-01-13",2);
		Photograph tundra = new Photograph("Tundra","tundrapicture","2003-11-24",5);
		Photograph skyscraper = new Photograph("Skyscraper","skyscraperpicture","2004-02-23",4);
		robert.addPhoto(beach);
		robert.addPhoto(forest);
		robert.addPhoto(skyscraper);
		thomas.addPhoto(mountains);
		thomas.addPhoto(desert);
		thomas.addPhoto(tundra);
		thomas.addPhoto(tundra); //this is to test if the takePhoto method will work correctly and not add a redundant tundra picture
		thomas.addPhoto(skyscraper);
		System.out.println(robert);
		System.out.println(thomas);
		System.out.println("Robert has "+robert.numPhotographs()+" pictures");
		System.out.println("Thomas has "+thomas.numPhotographs()+" pictures");
		Photograph eclipse = new Photograph("Eclipse","esclipsepicture");
		thomas.addPhoto(eclipse);
		if(thomas.hasPhoto(eclipse))
			System.out.println("Thomas has a picture of a eclipse");
		else
			System.out.println("Thomas does not have a picture of a eclipse");
		if(thomas.removePhoto(eclipse))
			System.out.println("Thomas has deleted the picture of the eclipse");
		if(!robert.removePhoto(eclipse))
			System.out.println("Robert never had a picture of a eclipse");
		if(thomas.hasPhoto(eclipse))
			System.out.println("Thomas has a picture of a eclipse");
		else
			System.out.println("Thonmas does not have a picture of a eclipse");
		PhotoLibrary robertTwo = new PhotoLibrary("Robert2ndAccount",0);
		if(robert.equals(robertTwo))
			System.out.println("Robert and Robert2ndAccount have the same id");
		else
			System.out.println("Robert and Robert2ndAccount do not have the same id");
		if(thomas.equals(robertTwo))
			System.out.println("Thomas and Robert2ndAccount have the same id");
		else
			System.out.println("Thomas and Robert2ndAccount do not have the same id");
		System.out.println("Robert and Thomas have a similarity rating of " +similarity(robert, thomas));
		robertTwo.addPhoto(mountains);
		robertTwo.addPhoto(desert);
		System.out.println("RobertTwo and Thomas have a similarity rating of " +similarity(robertTwo, thomas));

	}
}



