import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class HW4Tests {
	PhotoLibrary testPL;
	Photograph beach;
	Photograph forest;
	Photograph mountains;
	Photograph desert;
	Photograph tundra;
	Photograph skyscraper;
	
	//initializes photographs and a PhotoLibrary before every test
	@Before
	public void intialize() {
		testPL = new PhotoLibrary("Kyle's",305);
		beach = new Photograph("Beach","beachpicture","2000-03-25",2);
		forest = new Photograph("Forest","forestpicture","2000-03-13",3);
		mountains = new Photograph("Mountains","mountainpicture","1999-05-02",0);
		desert = new Photograph("Desert","desertpicture","1998-01-13",2);
		tundra = new Photograph("Tundra","tundrapicture","2003-11-24",5);
		skyscraper = new Photograph("Skyscraper","skyscraperpicture","2004-02-23",4);
		testPL.addPhoto(skyscraper);
		testPL.addPhoto(beach);
		testPL.addPhoto(forest);
		testPL.addPhoto(mountains);
		testPL.addPhoto(desert);
		testPL.addPhoto(tundra);
		
	}
	//tests the PhotoLibrary class's getPhotos method
	@Test
	public void testGetPhotos() {
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(skyscraper);
		testMatch.add(tundra);
		ArrayList<Photograph> testPLx = testPL.getPhotos(4);
		assertTrue(testMatch.size() == testPLx.size() && 
		     testMatch.containsAll(testPLx) && testPLx.containsAll(testMatch));
		 


	}
	//tests the PhotoLibrary class's getPhotos method
	@Test
	public void testGetPhotos2() {
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(beach);
		testMatch.add(forest);
		testMatch.add(desert);
		testMatch.add(skyscraper);
		testMatch.add(tundra);
		ArrayList<Photograph> testPLx = testPL.getPhotos(2);
		assertTrue(testMatch.size() == testPLx.size() && 
				testMatch.containsAll(testPLx) && testPLx.containsAll(testMatch));

	}
	//tests the PhotoLibrary class's getPhotosBetween a specific month method
	@Test
	public void testGetPhotosInMonth() {
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(beach);
		testMatch.add(forest);
		ArrayList<Photograph> testPLx = testPL.getPhotosInMonth(03,2000);
		assertTrue(testMatch.size() == testPLx .size() && 
		testMatch.containsAll(testPLx) && testPLx.containsAll(testMatch));
	}
	//tests the PhotoLibrary class's getPhotosBetween a specific month method
	@Test
	public void testGetPhotosInMonth2() {
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(tundra);
		ArrayList<Photograph> testPLx = testPL.getPhotosInMonth(11,2003);
		assertTrue(testMatch.size() == testPLx .size() && 
				testMatch.containsAll(testPLx) && testPLx.containsAll(testMatch));
	}
	//tests the PhotoLibrary class's getPhotosBetween a specific date method
	@Test
	public void testGetPhotosBetween() throws Exception {
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(skyscraper);
		testMatch.add(beach);
		testMatch.add(forest);
		testMatch.add(tundra);
		ArrayList<Photograph> testPLx = testPL.getPhotosBetween("2000-01-01", "2004-02-29");
		assertTrue(testMatch.size() == testPLx.size() && 
				testMatch.containsAll(testPLx) && testPLx.containsAll(testMatch));

	}
	//tests the PhotoLibrary class's getPhotosBetween a specific date method
	@Test
	public void testGetPhotosBetween2() throws ParseException {
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(beach);
		ArrayList<Photograph> testPLx = testPL.getPhotosBetween("200-03-14", "2000-03-26");
		/*
		assertTrue(testMatch.size() == testPLx.size() && 
		 *	    testMatch.containsAll(testPLx) && testPLx.containsAll(testMatch));
		 */
		assertEquals(null, testPLx);

	}
	//tests the PhotoLibrary class's removePhoto method
	@Test
	public void testRemovePhoto() {
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(skyscraper);
		testMatch.add(beach);
		testMatch.add(forest);
		testMatch.add(tundra);
		testMatch.add(desert);
		testPL.removePhoto(mountains);
		ArrayList<Photograph> testPLx = testPL.getPhotos();
		assertTrue(testMatch.size() == testPLx.size() && 
				testMatch.containsAll(testPLx) && testPLx.containsAll(testMatch));

	}
	//tests the PhotoLibrary class's removePhoto method
	@Test
	public void testRemovePhoto2() {
		Photograph jungle = new Photograph("Jungle","junglepicture");
		assertFalse(testPL.removePhoto(jungle));
	}
	
	//tests the PhotoLibrary class's similarity method
	@Test
	public void testSimilarity() {
		PhotoLibrary testMatch = new PhotoLibrary("test", 200);
		testMatch.addPhoto(skyscraper);
		testMatch.addPhoto(beach);
		assertEquals(1.0,PhotoLibrary.similarity(testMatch, testPL),.001);	
	}
	
	//tests the PhotoLibrary class's similarity method
	@Test
	public void testSimilarity2() {
		PhotoLibrary testMatch = new PhotoLibrary("test", 200);
		testMatch.addPhoto(skyscraper);
		testMatch.addPhoto(beach);
		Photograph jungle = new Photograph("Jungle","junglepicture");
		testMatch.addPhoto(jungle);
		assertEquals(.666,PhotoLibrary.similarity(testMatch, testPL),.001);	
	}
	//tests the Photograph class's compare method
	@Test
	public void testCompareTo() {
		ArrayList<Photograph> testPLphotos = testPL.getPhotos();
		Collections.sort(testPLphotos); 
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(desert);
		testMatch.add(mountains);
		testMatch.add(forest);
		testMatch.add(beach);
		testMatch.add(tundra);
		testMatch.add(skyscraper);
		assertTrue(testPLphotos.equals(testMatch)); 
		
		
	}
	//tests the Photograph class's compare method
	@Test
	public void testCompareTo2() {
		Photograph jungle = new Photograph("Jungle","junglepicture","1998-01-13",3);
		ArrayList<Photograph> testPLphotos = testPL.getPhotos();
		testPLphotos.add(jungle);
		Collections.sort(testPLphotos); 
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(desert);
		testMatch.add(jungle);
		testMatch.add(mountains);
		testMatch.add(forest);
		testMatch.add(beach);
		testMatch.add(tundra);
		testMatch.add(skyscraper);
		assertTrue(testPLphotos.equals(testMatch)); 
	}
	
	//tests the CompareByCaption class's compare method
	@Test
	public void testCompareByCaption() {
		CompareByCaption compareCaption = new CompareByCaption();
		ArrayList<Photograph> testPLphotos = testPL.getPhotos();
		Collections.sort(testPLphotos,compareCaption); 
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(beach);
		testMatch.add(desert);
		testMatch.add(forest);
		testMatch.add(mountains);
		testMatch.add(skyscraper);
		testMatch.add(tundra);
		assertTrue(testPLphotos.equals(testMatch));
	}
	
	//tests the CompareByCaption class's compare method
	@Test
	public void testCompareByCaption2() {
		CompareByCaption compareCaption = new CompareByCaption();
		Photograph skyscraper2 = new Photograph("skyscraper2","skyscraperpicture","1998-01-13",5);
		Photograph skyscraper3 = new Photograph("skyscraper3","skyscraperpicture","1998-01-13",3);
		testPL.addPhoto(skyscraper2);
		testPL.addPhoto(skyscraper3);
		ArrayList<Photograph> testPLphotos = testPL.getPhotos();
		Collections.sort(testPLphotos,compareCaption); 
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(beach);
		testMatch.add(desert);
		testMatch.add(forest);
		testMatch.add(mountains);
		testMatch.add(skyscraper2);
		testMatch.add(skyscraper);
		testMatch.add(skyscraper3);
		testMatch.add(tundra);
		assertTrue(testPLphotos.equals(testMatch));
	}
	
	//tests the CompareByRating class's compare method
	@Test
	public void testCompareByRating() {
		CompareByRating compareRating = new CompareByRating();
		ArrayList<Photograph> testPLphotos = testPL.getPhotos();
		Collections.sort(testPLphotos,compareRating); 
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(tundra);
		testMatch.add(skyscraper);
		testMatch.add(forest);
		testMatch.add(beach);
		testMatch.add(desert);
		testMatch.add(mountains);
		assertTrue(testPLphotos.equals(testMatch));
		
	}
	
	//tests the CompareByRating class's compare method
	@Test
	public void testCompareByRating2() {
		testPL.removePhoto(desert);
		CompareByRating compareRating = new CompareByRating();
		ArrayList<Photograph> testPLphotos = testPL.getPhotos();
		Collections.sort(testPLphotos,compareRating); 
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(tundra);
		testMatch.add(skyscraper);
		testMatch.add(forest);
		testMatch.add(beach);
		testMatch.add(mountains);
		System.out.println(testPLphotos);
		System.out.println(testMatch);
		assertTrue(testPLphotos.equals(testMatch));
	}
	
	
	
}
