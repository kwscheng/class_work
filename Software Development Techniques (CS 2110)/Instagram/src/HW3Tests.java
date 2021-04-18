import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class HW3Tests {
	PhotoLibrary testPL;
	Photograph beach;
	Photograph forest;
	Photograph mountains;
	Photograph desert;
	Photograph tundra;
	Photograph skyscraper;
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
	
	@Test
	public void testGetPhotosBetween() throws Exception {
		ArrayList<Photograph> testMatch = new ArrayList<Photograph>();
		testMatch.add(skyscraper);
		testMatch.add(beach);
		testMatch.add(forest);
		testMatch.add(tundra);
		assertEquals(testMatch, testPL.getPhotosBetween("2000-01-01", "2004-02-29"));
		
	}
	@Test
	public void testGetDateTaken(){
		Photograph test = new Photograph("test","testpicture","2004-05-30",3);
		assertEquals("2004-05-30",test.getDateTaken());
		
	}
	@Test
	public void testNumPhotographs() {
		Album test = new Album ("test album");
		assertTrue(test.addPhoto(desert));
	}
	

}
