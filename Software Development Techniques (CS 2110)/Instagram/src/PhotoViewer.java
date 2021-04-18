//kwc9ap HW 5 CS 2110

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PhotoViewer extends JFrame {
	//instantiates and occasionally declares the components that will be added to the GUI
	private PhotographContainer imageAlbum; 
	private JButton exit = new JButton();
	private JButton previous;
	private JButton next;
	private JLabel caption;
	private JLabel dateTaken;
	private JLabel imageHolder;
	private int currentImageIndex;
	private JLabel thumbZero = new JLabel();
	private JLabel thumbOne = new JLabel();
	private JLabel thumbTwo = new JLabel();
	private JLabel thumbThree = new JLabel();
	private JLabel thumbFour = new JLabel();
	private ArrayList<ImageIcon> bufferedImages = new ArrayList<>();
	private JRadioButton rating1 = new JRadioButton();
	private JRadioButton rating2 = new JRadioButton();
	private JRadioButton rating3 = new JRadioButton();
	private JRadioButton rating4 = new JRadioButton();
	private JRadioButton rating5 = new JRadioButton();
	private JButton sortByRatingButton= new JButton();
	private JButton sortByCaptionButton= new JButton();
	private JButton sortByDateButton= new JButton();

	//runs createAndShowGUI method
	public static void main(String [] args) {
		PhotoViewer myViewer = new PhotoViewer();

		String imageDirectory = "images/";

		Photograph p1 = new Photograph(imageDirectory+"park.jpg","Sunny day in the park :)", "2016-05-10", 4);
		Photograph p2 = new Photograph(imageDirectory+"wheat.jpg","Wheat field",  "2016-05-11", 3);
		Photograph p3 = new Photograph(imageDirectory+"sunset.jpg","A nice sunset", "2016-05-12", 1);
		Photograph p4 = new Photograph(imageDirectory+"greenery.jpg","Trees", "2016-05-13", 5);
		Photograph p5 = new Photograph(imageDirectory+"unicornwater.png","Rainbow colored water", "2016-05-14", 5);

		myViewer.imageAlbum = new PhotoLibrary("Test Library", 1);
		myViewer.imageAlbum.addPhoto(p1);
		myViewer.imageAlbum.addPhoto(p2);
		myViewer.imageAlbum.addPhoto(p3);
		myViewer.imageAlbum.addPhoto(p4);
		myViewer.imageAlbum.addPhoto(p5);
		Collections.sort(myViewer.imageAlbum.photos);


		javax.swing.SwingUtilities.invokeLater( () -> myViewer.createAndShowGUI() );

	}
	//does what the method says, creates and shows the GUI
	public void createAndShowGUI() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponentsToPane(getContentPane());
		this.pack();
		this.setVisible(true);
	}
	//sets up the GUI
	public void addComponentsToPane(Container screen) {

		//lines 93 to 99 creates the photographs that draw from the images folder
		String imageDirectory = "images/";
		Photograph p1 = new Photograph(imageDirectory+"park.jpg","Sunny day in the park :)", "2016-05-10", 4);
		Photograph p2 = new Photograph(imageDirectory+"wheat.jpg","Wheat field",  "2016-05-11", 3);
		Photograph p3 = new Photograph(imageDirectory+"sunset.jpg","A nice sunset", "2016-05-12", 1);
		Photograph p4 = new Photograph(imageDirectory+"greenery.jpg","Trees", "2016-05-13", 5);
		Photograph p5 = new Photograph(imageDirectory+"unicornwater.png","Rainbow colored water", "2016-05-14", 5); 

		//lines 101 to 107 creates a PhotoViewer instance (myViewer) and adds the above photographs to myViewer's imageAlbum
		PhotoViewer myViewer = new PhotoViewer();
		myViewer.imageAlbum = new PhotoLibrary("Test Library", 1);
		myViewer.imageAlbum.addPhoto(p1);
		myViewer.imageAlbum.addPhoto(p2);
		myViewer.imageAlbum.addPhoto(p3);
		myViewer.imageAlbum.addPhoto(p4);
		myViewer.imageAlbum.addPhoto(p5);

		//lines 110 to 114 creates the main GUI panel and sets it to take up the entire screen
		JPanel mainPanel = new JPanel();	
		mainPanel.setLayout(new BorderLayout(10,10));
		mainPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainPanel.setPreferredSize(new Dimension(((int)screenSize.getWidth()),(int)(screenSize.getHeight())));

		//following two lines creates the thumbnail region
		JPanel thumbnails = new JPanel();
		thumbnails.setLayout(new FlowLayout(FlowLayout.CENTER, 75, 25));

		//following two lines creates the region holding next and previous buttons
		JPanel nextPrevious = new JPanel();
		nextPrevious.setLayout(new BoxLayout(nextPrevious,BoxLayout.Y_AXIS));

		//following three lines creates the photograph rating region
		JPanel ratingButtons = new JPanel();
		ratingButtons.setLayout(new BoxLayout(ratingButtons,BoxLayout.Y_AXIS));
		ratingButtons.setPreferredSize(new Dimension(200,400));

		//following two lines creates the region holding the sort and exit buttons
		JPanel sortingButtons = new JPanel();
		sortingButtons.setLayout(new FlowLayout(FlowLayout.CENTER,75,25));

		//following code block sets texts of sortingButtons components
		exit.setText("Close GUI");
		sortByCaptionButton.setText("Sort by Caption");
		sortByDateButton.setText("Sort by Date");
		sortByRatingButton.setText("Sort by Rating");

		//following code block adds ActionListeners to sortingButtons components
		exit.addActionListener(new closeListener());
		sortByCaptionButton.addActionListener(new sortingListener(new CompareByCaption()));
		sortByRatingButton.addActionListener(new sortingListener(new CompareByRating()));
		sortByDateButton.addActionListener(new ActionListener() {
			//an anonymous ActionListener class is created for the sortByDateButton, since the date is sorted through the Photgraph.compareTo() method, not an outside Comparator

			public void actionPerformed(ActionEvent e) {

				currentImageIndex=0; //sets main image to first image of the new sorted array 
				Collections.sort(imageAlbum.photos);

				//lines 152 to 203 make the necessary changes to the thumbnail and main image once the Photographs have been sorted
				ArrayList<ImageIcon> bufferedImages = new ArrayList<>();
				ImageIcon buffP1; 
				ImageIcon buffP2;
				ImageIcon buffP3;
				ImageIcon buffP4;
				ImageIcon buffP5;

				try {
					//bufferImage is a helper method written down below, simply returns an ImageIcon when passed a Photograph
					buffP1 = bufferImage(imageAlbum.photos.get(0)); 
					buffP2 = bufferImage(imageAlbum.photos.get(1)); 
					buffP3 = bufferImage(imageAlbum.photos.get(2)); 
					buffP4 = bufferImage(imageAlbum.photos.get(3));
					buffP5 = bufferImage(imageAlbum.photos.get(4)); 

					bufferedImages.add(buffP1);
					bufferedImages.add(buffP2);
					bufferedImages.add(buffP3);
					bufferedImages.add(buffP4);
					bufferedImages.add(buffP5);

					//sets main image to the first Photograph after the Photographs have been sorted
					File imageFile = imageAlbum.photos.get(currentImageIndex).getImageFile();
					BufferedImage sortBi;
					try {
						sortBi = ImageIO.read(imageFile);
						imageHolder.setIcon(new ImageIcon(sortBi.getScaledInstance(400,400,400)));
					} catch (IOException e1) {
						imageHolder.setText("Error loading image");
					};

					//updates the thumbnails after the Photographs have been sorted 
					thumbZero.setIcon(bufferedImages.get(currentImageIndex));
					thumbOne.setIcon(bufferedImages.get(((((currentImageIndex+1)%5)+5)%5))); 
					thumbTwo.setIcon(bufferedImages.get(((((currentImageIndex+2)%5)+5)%5))); 
					thumbThree.setIcon(bufferedImages.get(((((currentImageIndex+3)%5)+5)%5))); 
					thumbFour.setIcon(bufferedImages.get(((((currentImageIndex+4)%5)+5)%5))); 


				}
				catch(IOException exc){
					System.out.println("Error loading image, IOException");
				}
				catch(Exception exc) {
					System.out.println("Error loading image "+e);
				}
				//updates the thumbnails after the Photographs have been sorted 
				thumbZero.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(0).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(0).getCaption()+"</p>"+imageAlbum.photos.get(0).getDateTaken()+"<html");
				thumbOne.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(1).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(1).getCaption()+"</p>"+imageAlbum.photos.get(1).getDateTaken()+"<html");
				thumbTwo.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(2).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(2).getCaption()+"</p>"+imageAlbum.photos.get(2).getDateTaken()+"<html");
				thumbThree.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(3).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(3).getCaption()+"</p>"+imageAlbum.photos.get(3).getDateTaken()+"<html");
				thumbFour.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(4).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(4).getCaption()+"</p>"+imageAlbum.photos.get(4).getDateTaken()+"<html");


			}

		});

		sortingButtons.add(sortByCaptionButton);
		sortingButtons.add(sortByDateButton);
		sortingButtons.add(sortByRatingButton);
		sortingButtons.add(exit);


		rating1.setText("Change rating to 1");
		rating2.setText("Change rating to 2");
		rating3.setText("Change rating to 3");
		rating4.setText("Change rating to 4");
		rating5.setText("Change rating to 5");

		rating1.addActionListener(new ratingListener(1));
		rating2.addActionListener(new ratingListener(2));
		rating3.addActionListener(new ratingListener(3));
		rating4.addActionListener(new ratingListener(4));
		rating5.addActionListener(new ratingListener(5));


		ButtonGroup ratingGroup = new ButtonGroup();
		ratingGroup.add(rating1);
		ratingGroup.add(rating2);
		ratingGroup.add(rating3);
		ratingGroup.add(rating4);
		ratingGroup.add(rating5);

		imageHolder = new JLabel();
		imageHolder.setPreferredSize(new Dimension(200,200));
		imageHolder.setHorizontalAlignment(JLabel.CENTER);

		ImageIcon buffP1; 
		ImageIcon buffP2;
		ImageIcon buffP3;
		ImageIcon buffP4;
		ImageIcon buffP5;

		int currentImageIndex = 0;

		try {

			buffP1 = bufferImage(myViewer.imageAlbum.photos.get(0)); 
			buffP2 = bufferImage(myViewer.imageAlbum.photos.get(1)); 
			buffP3 = bufferImage(myViewer.imageAlbum.photos.get(2)); 
			buffP4 = bufferImage(myViewer.imageAlbum.photos.get(3));
			buffP5 = bufferImage(myViewer.imageAlbum.photos.get(4)); 

			bufferedImages.add(buffP1);
			bufferedImages.add(buffP2);
			bufferedImages.add(buffP3);
			bufferedImages.add(buffP4);
			bufferedImages.add(buffP5);

			thumbZero.setIcon(bufferedImages.get(currentImageIndex));
			thumbOne.setIcon(bufferedImages.get(((((currentImageIndex+1)%5)+5)%5))); 
			thumbTwo.setIcon(bufferedImages.get(((((currentImageIndex+2)%5)+5)%5))); 
			thumbThree.setIcon(bufferedImages.get(((((currentImageIndex+3)%5)+5)%5))); 
			thumbFour.setIcon(bufferedImages.get(((((currentImageIndex+4)%5)+5)%5))); 


		}
		catch(IOException e){
			System.out.println("Error loading image, IOException");
		}
		catch(Exception e) {
			System.out.println("Error loading image "+e);
		}
		thumbnails.add(thumbZero);
		thumbnails.add(thumbOne); 
		thumbnails.add(thumbTwo); 
		thumbnails.add(thumbThree);
		thumbnails.add(thumbFour); 

		thumbZero.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(0).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(0).getCaption()+"</p>"+imageAlbum.photos.get(0).getDateTaken()+"<html");
		thumbOne.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(1).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(1).getCaption()+"</p>"+imageAlbum.photos.get(1).getDateTaken()+"<html");
		thumbTwo.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(2).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(2).getCaption()+"</p>"+imageAlbum.photos.get(2).getDateTaken()+"<html");
		thumbThree.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(3).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(3).getCaption()+"</p>"+imageAlbum.photos.get(3).getDateTaken()+"<html");
		thumbFour.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(4).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(4).getCaption()+"</p>"+imageAlbum.photos.get(4).getDateTaken()+"<html");

		thumbZero.setHorizontalTextPosition(JLabel.CENTER);
		thumbZero.setVerticalTextPosition(JLabel.BOTTOM);
		thumbOne.setHorizontalTextPosition(JLabel.CENTER);
		thumbOne.setVerticalTextPosition(JLabel.BOTTOM);
		thumbTwo.setHorizontalTextPosition(JLabel.CENTER);
		thumbTwo.setVerticalTextPosition(JLabel.BOTTOM);
		thumbThree.setHorizontalTextPosition(JLabel.CENTER);
		thumbThree.setVerticalTextPosition(JLabel.BOTTOM);
		thumbFour.setHorizontalTextPosition(JLabel.CENTER);
		thumbFour.setVerticalTextPosition(JLabel.BOTTOM);

		thumbZero.addMouseListener(new thumbnailListener(0));
		thumbOne.addMouseListener(new thumbnailListener(1));
		thumbTwo.addMouseListener(new thumbnailListener(2));
		thumbThree.addMouseListener(new thumbnailListener(3));
		thumbFour.addMouseListener(new thumbnailListener(4));


		File previousImage = imageAlbum.photos.get(currentImageIndex).getImageFile();
		BufferedImage previousBi;
		try {
			previousBi = ImageIO.read(previousImage);
			imageHolder.setIcon(new ImageIcon(previousBi.getScaledInstance(400,400,400)));
		} catch (IOException e1) {
			imageHolder.setText("Error loading image");
		}		
		caption = new JLabel(myViewer.imageAlbum.getPhotos().get(currentImageIndex).getCaption());
		dateTaken = new JLabel("This picture was taken on: "+myViewer.imageAlbum.getPhotos().get(currentImageIndex).getDateTaken());
		next = new JButton("  Next  ");
		previous = new JButton("Previous");
		next.addActionListener(new nextListener()); 
		previous.addActionListener(new previousListener());
		Dimension nextpreviousMinSize = new Dimension(200,400);
		next.setPreferredSize(nextpreviousMinSize);
		previous.setPreferredSize(nextpreviousMinSize);
		next.setAlignmentX(CENTER_ALIGNMENT);
		next.setAlignmentY(CENTER_ALIGNMENT);
		previous.setAlignmentX(CENTER_ALIGNMENT);
		next.setAlignmentY(CENTER_ALIGNMENT);
		nextPrevious.setMinimumSize(new Dimension(1000,1000));

		nextPrevious.add(next);
		nextPrevious.add(previous);

		ratingButtons.add(rating1);
		ratingButtons.add(rating2);
		ratingButtons.add(rating3);
		ratingButtons.add(rating4);
		ratingButtons.add(rating5);


		mainPanel.add(imageHolder, BorderLayout.CENTER);
		mainPanel.add(thumbnails, BorderLayout.PAGE_END);
		mainPanel.add(sortingButtons,BorderLayout.PAGE_START);
		mainPanel.add(nextPrevious, BorderLayout.LINE_END);
		mainPanel.add(ratingButtons,BorderLayout.LINE_START);
		screen.add(mainPanel);
	}
	//ActionListener class for the next button
	class nextListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			currentImageIndex++;
			currentImageIndex%=5;
			File imageFile = imageAlbum.photos.get(currentImageIndex).getImageFile();
			BufferedImage nextBi;
			try {
				nextBi = ImageIO.read(imageFile);
				imageHolder.setIcon(new ImageIcon(nextBi.getScaledInstance(400,400,400)));
			} catch (IOException e1) {
				imageHolder.setText("Error loading image");
			}

			caption.setText(imageAlbum.getPhotos().get(currentImageIndex).getCaption());
			dateTaken.setText(imageAlbum.getPhotos().get(currentImageIndex).getDateTaken());
		}

	}
	//ActionListener class for the previous button
	class previousListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			currentImageIndex--;
			currentImageIndex = (((currentImageIndex%5)+5)%5);
			File previousImage = imageAlbum.photos.get(currentImageIndex).getImageFile();
			BufferedImage previousBi;
			try {
				previousBi = ImageIO.read(previousImage);
				imageHolder.setIcon(new ImageIcon(previousBi.getScaledInstance(400,400,400)));
			} catch (IOException e1) {
				imageHolder.setText("Error loading image");
			}
			caption.setText(imageAlbum.getPhotos().get(currentImageIndex).getCaption());
			dateTaken.setText(imageAlbum.getPhotos().get(currentImageIndex).getDateTaken());

		}
	}
	//ActionListener class for the rating buttons
	class ratingListener implements ActionListener {
		public int rate;
		public ratingListener(int rate){
			this.rate=rate;
		}
		public void actionPerformed(ActionEvent e) {
			imageAlbum.photos.get(currentImageIndex).setRating(rate);

			thumbZero.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(0).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(0).getCaption()+"</p>"+imageAlbum.photos.get(0).getDateTaken()+"<html");
			thumbOne.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(1).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(1).getCaption()+"</p>"+imageAlbum.photos.get(1).getDateTaken()+"<html");
			thumbTwo.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(2).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(2).getCaption()+"</p>"+imageAlbum.photos.get(2).getDateTaken()+"<html");
			thumbThree.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(3).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(3).getCaption()+"</p>"+imageAlbum.photos.get(3).getDateTaken()+"<html");
			thumbFour.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(4).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(4).getCaption()+"</p>"+imageAlbum.photos.get(4).getDateTaken()+"<html");

		}
	}

	//ActionListener class for the other two sort buttons, sortByRating and sortByCaption
	//The implementation for sortingListener is very similar to the implementation for the sortByDate listener. View sortByDate listener comments to understand sortingListener
	class sortingListener implements ActionListener{
		public Comparator comparator;

		public sortingListener(Comparator c) {
			this.comparator = c;
		}

		public void actionPerformed(ActionEvent e) {
			currentImageIndex=0;
			Collections.sort(imageAlbum.photos, comparator);
			ArrayList<ImageIcon> bufferedImages = new ArrayList<>();
			ImageIcon buffP1; 
			ImageIcon buffP2;
			ImageIcon buffP3;
			ImageIcon buffP4;
			ImageIcon buffP5;

			try {

				buffP1 = bufferImage(imageAlbum.photos.get(0)); 
				buffP2 = bufferImage(imageAlbum.photos.get(1)); 
				buffP3 = bufferImage(imageAlbum.photos.get(2)); 
				buffP4 = bufferImage(imageAlbum.photos.get(3));
				buffP5 = bufferImage(imageAlbum.photos.get(4)); 

				bufferedImages.add(buffP1);
				bufferedImages.add(buffP2);
				bufferedImages.add(buffP3);
				bufferedImages.add(buffP4);
				bufferedImages.add(buffP5);

				File imageFile = imageAlbum.photos.get(currentImageIndex).getImageFile();
				BufferedImage sortBi;
				try {
					sortBi = ImageIO.read(imageFile);
					imageHolder.setIcon(new ImageIcon(sortBi.getScaledInstance(400,400,400)));
				} catch (IOException e1) {
					imageHolder.setText("Error loading image");
				};

				thumbZero.setIcon(bufferedImages.get(currentImageIndex));
				thumbOne.setIcon(bufferedImages.get(((((currentImageIndex+1)%5)+5)%5))); 
				thumbTwo.setIcon(bufferedImages.get(((((currentImageIndex+2)%5)+5)%5))); 
				thumbThree.setIcon(bufferedImages.get(((((currentImageIndex+3)%5)+5)%5))); 
				thumbFour.setIcon(bufferedImages.get(((((currentImageIndex+4)%5)+5)%5))); 


			}
			catch(IOException exc){
				System.out.println("Error loading image, IOException");
			}
			catch(Exception exc) {
				System.out.println("Error loading image "+e);
			}
			thumbZero.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(0).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(0).getCaption()+"</p>"+imageAlbum.photos.get(0).getDateTaken()+"<html");
			thumbOne.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(1).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(1).getCaption()+"</p>"+imageAlbum.photos.get(1).getDateTaken()+"<html");
			thumbTwo.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(2).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(2).getCaption()+"</p>"+imageAlbum.photos.get(2).getDateTaken()+"<html");
			thumbThree.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(3).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(3).getCaption()+"</p>"+imageAlbum.photos.get(3).getDateTaken()+"<html");
			thumbFour.setText("<html>"+"<p>"+"Rating: "+String.valueOf(imageAlbum.photos.get(4).getRating())+"</p>"+"<p>"+"Caption: "+imageAlbum.photos.get(4).getCaption()+"</p>"+imageAlbum.photos.get(4).getDateTaken()+"<html");

		}
	}
	//ActionListener class for the close button
	class closeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);

		}

	}
	//MouseListener class that does nothing except for when mousePressed
	class thumbnailListener implements MouseListener{
		public int thumbnailNumber;

		public thumbnailListener(int thumbnailNumber) {
			this.thumbnailNumber = thumbnailNumber;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		//changes main image to the selected thumbnail
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			currentImageIndex=thumbnailNumber;
			File imageFile = imageAlbum.photos.get(currentImageIndex).getImageFile();
			BufferedImage thumbnailBi;
			try {
				thumbnailBi = ImageIO.read(imageFile);
				imageHolder.setIcon(new ImageIcon(thumbnailBi.getScaledInstance(400,400,400)));
			} catch (IOException e1) {
				imageHolder.setText("Error loading image");
			};
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
	//bufferImage method that returns ImageIcon given an image
	public static ImageIcon bufferImage(Photograph image) throws IOException {
		BufferedImage bi;
		bi = ImageIO.read(image.getImageFile());
		ImageIcon buffered = new ImageIcon(bi.getScaledInstance(100,100,100));
		return buffered;
	}

	class WindowRunnable implements Runnable {
		public void run() {
			PhotoViewer.createAndShowGUI();
		}
	}
}
