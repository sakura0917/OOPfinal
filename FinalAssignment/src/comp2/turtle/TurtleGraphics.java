package comp2.turtle;

//import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.*;
import uk.ac.leedsbeckett.oop.LBUGraphics;
import java.util.*;
import java.io.*;

public class TurtleGraphics extends LBUGraphics
{
private List<String> commandHistory = new ArrayList<>(); // ✅ Store all commands
private boolean notSaved = false; // Flag to track unsaved changes
//private boolean isUserCommand = false;
	public static void main(String[] args) {
    TurtleGraphics app = new TurtleGraphics();
    System.out.println("Please enter a command:");
    Scanner scan = new Scanner(System.in);
    boolean state = true;

    while (state) {
        String input = scan.nextLine();

        if (input.equalsIgnoreCase("about")) {
            app.about();
        } 
        
        else if (input.equalsIgnoreCase("exit")) {
            if (app.notSaved) {
                int choice = JOptionPane.showConfirmDialog(
                    null,
                    "You have unsaved changes. Do you want to save before exiting?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Save Image");
                    int saveChoice = chooser.showSaveDialog(null);
                    if (saveChoice == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        app.saveImage(file.getAbsolutePath());
                        System.out.println("Exiting program...");
                        state = false;
                    }
                } else if (choice == JOptionPane.NO_OPTION) {
                    System.out.println("Exiting program...");
                    state = false;
                }
                // If CANCEL, do nothing
            } else {
                System.out.println("Exiting program...");
                state = false;
            }
        } 
        
        else {
            System.out.println(input + " : Command not found!");
        }
    }
}



 public TurtleGraphics()
 {
	     //Mainframe
         JFrame MainFrame = new JFrame("TurtleGraphics");                //create a frame to display the turtle panel on
         MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Make sure the app exits when closed
         MainFrame.setLayout(new FlowLayout());  //not strictly necessary
         MainFrame.add(this);                                    //"this" is this object that extends turtle graphics so we are adding a turtle graphics panel to the frame
         MainFrame.pack();                                               //set the frame to a size we can see
         MainFrame.setVisible(true);  
         
         //Menu Bar
         JMenuBar menuBar= new JMenuBar();
         JMenu fileMenu1 = new JMenu("File");
         JMenu fileMenu2 = new JMenu("Help");
         
         //Menu Items
         JMenuItem saveItem = new JMenuItem("Save");
         JMenuItem loadItem = new JMenuItem("Load");
         JMenuItem infoItem = new JMenuItem("Info");
         JMenuItem saveCommandsItem = new JMenuItem("Save Commands");
         JMenuItem loadCommandsItem = new JMenuItem("Load Commands");
         
         infoItem.addActionListener(e -> displayCommands());

         
         //Saving Image
         saveItem.addActionListener(e -> {
             JFileChooser chooser = new JFileChooser();
             chooser.setDialogTitle("Save Image");
             int choice = chooser.showSaveDialog(MainFrame);
             if (choice == JFileChooser.APPROVE_OPTION) {
                 File file = chooser.getSelectedFile();
                 saveImage(file.getAbsolutePath());
             }
         });
         
         //Saving Commands
         saveCommandsItem.addActionListener(e -> {
             JFileChooser chooser = new JFileChooser();
             chooser.setDialogTitle("Save Commands as Text");
             int choice = chooser.showSaveDialog(MainFrame);
             if (choice == JFileChooser.APPROVE_OPTION) {
                 File file = chooser.getSelectedFile();
                 saveCommandsToFile(file.getAbsolutePath().replaceAll("\\.txt$", ""));
             }

         });



         loadCommandsItem.addActionListener(e -> {
             JFileChooser chooser = new JFileChooser();
             chooser.setDialogTitle("Load Commands");
             int choice = chooser.showOpenDialog(MainFrame);
             if (choice == JFileChooser.APPROVE_OPTION) {
                 File file = chooser.getSelectedFile();
                 loadCommandsFromFile(file.getAbsolutePath());
             }

         });

         loadItem.addActionListener(e -> {
             JFileChooser chooser = new JFileChooser();
             chooser.setDialogTitle("Load Image");
             int choice = chooser.showOpenDialog(MainFrame);
             if (choice == JFileChooser.APPROVE_OPTION) {
                 File file = chooser.getSelectedFile();
                 loadImage(file.getAbsolutePath());
             }
         });

         fileMenu1.add(saveItem);
         fileMenu1.add(loadItem);
         fileMenu2.add(infoItem);
         fileMenu1.add(saveCommandsItem);
         fileMenu1.add(loadCommandsItem);
         menuBar.add(fileMenu1);
         menuBar.add(fileMenu2);
         MainFrame.setJMenuBar(menuBar);
        
         // Initialize turtle
         //about(); // Show version info
         reset(); // Reset turtle to default position
         setPenColour(Color.red);
         setStroke(2);
         setPenState(true);
         displayMessage("Welcome to Sakura's Turtle Graphics Program!");          
      // Add this method to your TurtleGraphics class
         

         // Then modify the "creative" case in your processCommand method:
 
 }
 
 private void showCreativeOptions() {
     Object[] options = {"Mandala", "Star"};
     int choice = JOptionPane.showOptionDialog(
         null,
         "Choose a creative design to draw:",
         "Creative Options",
         JOptionPane.DEFAULT_OPTION,
         JOptionPane.QUESTION_MESSAGE,
         null,
         options,
         options[0]
     );
     
     if (choice == 0) { // Mandala
         clearInterface();
         drawFlower();
         notSaved=true;
         
     } else if (choice == 1) { // Star
         clearInterface();
         drawStars(100); // You can set a default size or prompt for one
         notSaved=true;
     }
 }
 
 
 
 
 private void saveImage(String filename) {

	 try {

	 BufferedImage image = getBufferedImage();
	 ImageIO.write(image, "PNG", new File(filename + ".png"));
	 notSaved = false; // Reset flag after successful save
	 displayMessage("Drawing saved as " + filename + ".png");
	 } catch (Exception e) {
	 displayMessage("Error saving: " + e.getMessage());
	 }
	 }  	 
 
 private void loadImage(String filename) {
	 try {
		 BufferedImage image = ImageIO.read(new File(filename));
		 setBufferedImage(image);
		 displayMessage("Drawing loaded from " + filename);
	 } catch (Exception e) {
		 displayMessage("Error loading: " + e.getMessage());
	 }
 }

private void saveCommandsToFile(String filename) {
	 try (FileWriter writer = new FileWriter(filename + ".txt")) {
	 for (String cmd : commandHistory) {
	 writer.write(cmd + "\n");
	 }
	 displayMessage("Commands saved to " + filename + ".txt");
	 } catch (IOException e) {
	 displayMessage("Failed to save commands: " + e.getMessage());
	 }
}


private void loadCommandsFromFile(String filename) {
	 try (Scanner scanner = new Scanner(new FileReader(filename))) {
	 while (scanner.hasNextLine()) {
	 String command = scanner.nextLine().trim();
	 if (!command.isEmpty()) {
	 processCommand(command);
	 }
	 }

	 displayMessage("Commands loaded and executed from " + filename);
	 } catch (IOException e) {
	 displayMessage("Error loading commands: " + e.getMessage());
	 }
}

@Override
public void about() {
super.about();
writeName();
}

public void displayCommands() {
    StringBuilder commandsText = new StringBuilder();
    commandsText.append("Commands Available :\n\n");
    

    commandsText.append("Drawing Controls:\n");
    commandsText.append("  penup           - Stop drawing while moving\n");
    commandsText.append("  pendown         - Start drawing again\n");
    commandsText.append("  black/red/green/white - Change pen color\n");
    commandsText.append("  pen <r> <g> <b> - Use custom RGB color\n");
    commandsText.append("  penwidth <n>    - Change pen thickness\n");
    commandsText.append("  clear           - Erase the drawing\n");
    commandsText.append("  reset           - Go back to starting position\n\n");

    commandsText.append("Movement:\n");
    commandsText.append("  move <n>        - Move forward by n pixels\n");
    commandsText.append("  reverse <n>     - Move backward by n pixels\n");
    commandsText.append("  right <n>       - Turn right by n degrees\n");
    commandsText.append("  left <n>        - Turn left by n degrees\n\n");
    
    commandsText.append("Shapes:\n");
    commandsText.append("  triangle <size> - Draw an equilateral triangle\n");
    commandsText.append("  polygon <sides> <size> - Draw a regular polygon\n");
    commandsText.append("  square <size>   - Draw a square\n");
    commandsText.append("  star <size>     - Draw a star\n");
    commandsText.append("  creative        - Draw a flower mandala\n\n");

    commandsText.append("Menu:\n");
    commandsText.append("  save/load       - Save/open a drawing\n");
    commandsText.append("  save commands/load commands - Save/run command list\n");
    commandsText.append("  info       - Show the list of Commands\n\n");

    commandsText.append("Other:\n");
    commandsText.append("  sakura          - Write the word 'SAKURA'\n");
    commandsText.append("  about           - Draw a simple graphic on the canvas, Report the version number\n");
    commandsText.append("  help            - Show the list of Commands\n");

    JOptionPane.showMessageDialog(null, commandsText.toString(), "Help", JOptionPane.INFORMATION_MESSAGE);
}


 public void writeName() {
	 penSize=10;
	 setStroke(2);
	//S from SAKURA
	clear();
	reset();
	setPenState(false);           // Don't draw while moving
	forward(-100);
	right(90);         // Turn around to face left
	forward(200);      // Adjust this value based on your canvas size
	left(90);
	setPenState(true);
	right(90);
	forward(50);
	left(90);
	forward(50);
	left(90);
	forward(50);
	right(90);
	forward(50);
	right(90);
	forward(50);
	setPenState(false);
	left(180);
	forward(80); // space between letters
	left(90);
	
	//A from SAKURA
	setPenState(true);
	setPenColour(Color.YELLOW);
	forward(100);
	right(90);
	forward(50);
	right(90);
	forward(100);
	forward(-50);
	right(90);
	forward(50);
	setPenState(false);
	left(90);
	forward(50);
	left(90);
	forward(80); // space between letters
	left(90);
	
	//K from SAKURA
	setPenState(true);
	setPenColour(Color.GREEN);
	forward(100);
	setPenState(false);
	forward(-50);
	setPenState(true);
	right(40);
	forward(50);
	setPenState(false);
	forward(-50);
	setPenState(true);
	right(105);
	forward(50);
	setPenState(false);
	left(50);
	forward(30);
	
	//U from SAKURA
	setPenColour(Color.BLUE);
	left(95);
	forward(90);
	setPenState(true);
	forward(-100);
	right(90);
	forward(50);
	left(90);
	forward(100);
	setPenState(false);
	forward(-100);
	right(90);
	forward(30);
	left(90);
	
	//R from SAKURA
	setPenColour(Color.MAGENTA);
	setPenState(true);
	forward(100);
	right(90);
	forward(50);
	right(90);
	forward(50);
	right(90);
	forward(50);
	right(225);
	forward(70);
	setPenState(false);
	left(45);
	forward(35);
	left(90);
	
	//A from SAKURA
	setPenState(true);
	setPenColour(Color.YELLOW);
	forward(100);
	right(90);
	forward(50);
	right(90);
	forward(100);
	forward(-50);
	right(90);
	forward(50);
	setPenState(false);
	left(90);
	forward(50);
	left(90);
	forward(80); // space between letters
	left(90);
	
	
 }
 
 public void triangle(int side1, int side2, int side3) {
	    reset();
	    setStroke(2);// pen width set to 2
	   
	    // Calculate the cosine of angles B and C using the law of Cosines
	    double cosB = (side1 * side1 + side3 * side3 - side2 * side2)/(2.0 * side1 * side3);
	    double cosC = (side1 * side1 + side2 * side2 - side3 * side3)/(2.0 * side1 * side2);
	   
	    //convert the cosins values to angles in degress
	    double angleB = Math.toDegrees(Math.acos(cosB));
	    double angleC = Math.toDegrees(Math.acos(cosC));
	   
	    /*setPenState(false);
	    forward(150);
	    left(90);*/
	   
	    //equilateral triangle
	    right(90);
	    setPenState(true);
	    forward(side3);
	    right((int) Math.round(180-angleB));
	    forward(side1);
	    right((int) Math.round(180-angleC));
	    forward(side2);
	    setPenState(false);
	    reset();
	    }
 private boolean isInteger(String s) {
	    try {
	        Integer.parseInt(s);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
 
 private boolean validateParameter(String a) {
	    if (a == null) {
	        displayMessage("Parameter missing! Please enter in this format: command <steps>");
	        return false;
	    } 
	    else if (!isInteger(a)) {
	        displayMessage("Invalid Parameter! Parameter can not be non numeric!");
	        return false;
	    }
	    else {
	        return true;
	    }
	}


 private void drawSquare(int size) {
	 setPenState(true);
	 for (int i = 0; i < 4; i++) {
	 forward(size);
	 right(90);
	 }
	 }


 /*private void drawEquilateralTriangle(int size) {
	    setPenState(true);
	    for (int i = 0; i < 3; i++) {
	        forward(size);
	        right(120); // Each internal angle in an equilateral triangle is 60°, so we turn 180 - 60 = 120
	    }
	}
	*/

private void drawPolygon(int sides, int size) {
	 if (sides < 3) {
	 displayMessage("Polygon must have at least 3 sides");
	 return;
	 }
	 setPenState(true);
	 int angle = 360 / sides;
	 for (int i = 0; i < sides; i++) {
	 forward(size);
	 right(angle);
	 }
	 }

private void setPenRGB(String redStr, String greenStr, String blueStr) {
	 try {
	 int red = Integer.parseInt(redStr);
	 int green = Integer.parseInt(greenStr);
	 int blue = Integer.parseInt(blueStr);
	 // Validate each component is between 0-255
	 if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
	 throw new IllegalArgumentException("RGB values must be between 0-255");
	 }
	 Color color = new Color(red, green, blue);
	 setPenColour(color);
	 displayMessage("Pen color set to RGB(" + red + ", " + green + ", " + blue + ")");
	 } catch (NumberFormatException e) {
	 displayMessage("Invalid RGB values - must be numbers between 0-255");
	 } catch (IllegalArgumentException e) {
	 displayMessage(e.getMessage());
	 }
	 }

public void drawFlower() {
	    clear();
	    reset();
	    setTurtleSpeed(1);
	    setStroke(2);
	    setPenState(true);
	    setPenColour(Color.white);

	    for (int i = 0; i < 12; i++) {
	        drawPolygon(6, 50);
	        right(30);
	    }
	    setBackground_Col(Color.black);
	    
	}




private void drawStars(int size) {
	
	clear();
	reset();
    setBackground_Col(Color.black);
	
	    setPenState(true);
	    setPenColour(Color.WHITE);
	    for (int i = 0; i < 5; i++) {
	        forward(size);
	        right(144); // External angle for 5-point star
	    }
	}

 public void processCommand(String command)      //this method must be provided because LBUGraphics will call it when it's JTextField is used
 {
	  commandHistory.add(command);
	  System.out.println(command);
 //String parameter is the text typed into the LBUGraphics JTextfield
 //lands here if return was pressed or "ok" JButton clicked
	 String[] parts = command.trim().split("\\s+"); // splits by one or more spaces

     String w1 = null;
     String w2 = null;
     String w3=null;
     String w4=null;
     
     if (parts.length == 1) {
         w1 = parts[0];
         
         
     } else if (parts.length == 2) {
         w1 = parts[0];
         w2 = parts[1];
     } else if (parts.length == 3){
         w1 = parts[0];
         w2 = parts[1];
         w3 = parts[2];
     }
     else if(parts.length== 4){
    	 w1 = parts[0];
    	 w2 = parts[1];
    	 w3 = parts[2];
    	 w4 = parts[3]; 
     }
     
     if (w1 == null) { 
    	 displayMessage("No command detected, Re-try!");
    	 return;}
	 w1=w1.toLowerCase();
	 
	 
	 switch(w1)
	 {
	 
	 case "penup":
		 setPenState(false);
		 displayMessage("Pen Lifted!");
		 break;
		 
	 case "pendown":
		 setPenState(true);
		 displayMessage("Pen state set down!");
		 break;
		 
	 case "black":
 		setPenColour(Color.BLACK);
 		displayMessage("Pen colour set to black!");
           break;
           
 	 case "green":
 		setPenColour(Color.GREEN);
 		displayMessage("Pen colour set to green!");
           break;
           
 	 case "red":
 		setPenColour (Color.RED);
 		displayMessage("Pen colour set to red!");
           break;
           
 	 case "white":
 		 setPenColour(Color.WHITE);
  		displayMessage("Pen colour set to white!");
           break;
           
 	 case "reset":
 		 reset();
  		displayMessage("The canvas has been reset!");
           break;
           
 	case "clear":
 	    if (notSaved) {
 	        String[] options = {"Save", "Don't Save", "Cancel"};
 	        int result = JOptionPane.showOptionDialog(null,
 	                "Do you want to save your changes?",
 	                "Unsaved Changes",
 	                JOptionPane.DEFAULT_OPTION,
 	                JOptionPane.WARNING_MESSAGE,
 	                null,
 	                options,
 	                options[0]);

 	        if (result == 0) { // Save
 	            displayMessage("Please save your file!");
 	        } else if (result == 1) { // Don't Save
 	            clear();
 	            notSaved = false;
 	            displayMessage("The canvas has been cleared!");
 	        } else { // Cancel or closed dialog
 	            displayMessage("Clear cancelled!");
 	        }
 	    } else {
 	        clear();
 	        notSaved = false;
 	        displayMessage("The canvas has been cleared!");
 	    }
 	    break;

 	 case "help":
 		 displayCommands(); 
           break;
          
 	case "about":
 	    about();
 	    break;

 	case "sakura":
 	    writeName();
 	    break;
 	    
 	 case "triangle":
         if(parts.length ==2) {
         int side = Integer.parseInt(parts[1]);
         triangle(side, side, side);
        
         }else if (parts.length ==4) {
         int side1 = Integer.parseInt(parts[1]);
         int side2 = Integer.parseInt(parts[2]);
         int side3 = Integer.parseInt(parts[3]);
         triangle(side1, side2, side3);
         } else {
         displayMessage("Invalid command! 'triangle' command expects 1 or 3 parameters.");
         }
         break;
 	
 	case "left":
 	    if (validateParameter(w2)) {
 	        int parameter = Integer.parseInt(w2);
 	        if (parameter>=0 && parameter<=360 ) {
 	        clearInterface();
 	        left(parameter);
 	        }
 	        else
 	        {
 	       	displayMessage("Please enter a valid angle(0 to 360)");
 	        }
 	    }
 	    break;
 	    
 	

 	case "right":
 		
 		if (validateParameter(w2)) {
        int parameter = Integer.parseInt(w2);
        if (parameter>=0 && parameter<=360 ) {
        clearInterface();
        right(parameter);
        }
        else
        {
       	displayMessage("Please enter a valid angle(0 to 360)");
        }
 	    }
 	    break;

 	case "move":
 	    if (validateParameter(w2)) {
 	        int parameter = Integer.parseInt(w2);
 	        if (parameter<0) {
 	        	displayMessage("Distance cannot be negative! Please enter a positive value.");
 	        }
 	        else {
 	        clearInterface();
 	        forward(parameter);
 	        notSaved=true;
 	        }
 	    }
 	    break;

 	case "reverse":
 		if (validateParameter(w2)) {
 	        int parameter = Integer.parseInt(w2);
 	        if (parameter<0) {
 	        	displayMessage("Distance cannot be negative! Please enter a positive value.");
 	        }
 	        else {
 	        clearInterface();	
 	        forward(-parameter);
 	        notSaved=true;
 	        }
 	    }
 	    break;
 	    
 	case "square":
 		if (validateParameter(w2)) {
        int parameter = Integer.parseInt(w2);
        if (parameter<0) {
        	displayMessage("side cannot be negative! Please enter a positive value.");
        }
        else {
        clearInterface();	
        drawSquare(parameter);
        notSaved=true;
        }
 		}
        break;

 	case "polygon":
 		if (validateParameter(w2) && validateParameter(w3)) {
        int parameter1 = Integer.parseInt(w2);
        int parameter2 = Integer.parseInt(w3);
        if (parameter1<3) {
        	displayMessage("Polygon has atleast 3 sides!");
        	
        }
        else if(parameter2<0 ||parameter2>360){
        	displayMessage("Please enter a valid angle(0 to 360)");
        }
        else {
        clearInterface();
        drawPolygon(parameter1,parameter2);
        notSaved=true;
        }
 		}
        break;
           
 	case "penwidth":
        if (parts.length == 2 && isInteger(parts[1])) {
            int width = Integer.parseInt(parts[1]);
            setStroke(width);
            displayMessage("Pen width set to " + width);
        } else {
            displayMessage("Usage: penwidth <value>");
        }
        break;

 	case "pen":
 	    if (parts.length == 4 && validateParameter(parts[1]) && validateParameter(parts[2]) && validateParameter(parts[3])) {
 	        setPenRGB(parts[1], parts[2], parts[3]);
 	        displayMessage("Pen color set to RGB(" + parts[1] + ", " + parts[2] + ", " + parts[3] + ")");
 	    } else {
 	        displayMessage("RGB values must be between 0 and 255.");
 	    }
 	    break;

  		 
 	 case "creative":
         showCreativeOptions();
         break;
    	 
     case "star":
    	 clearInterface();
    	 if (validateParameter(w2)) {
    	 int parameter=Integer.parseInt(w2);
    	 drawStars(parameter);
    	 notSaved=true;}
    	 else {
      		 displayMessage("Syntax: Star <size> "); 
    	 }
    	 break;
    	 
     case "mandala":
    	 clearInterface();
    	 drawFlower();
    	 notSaved=true;
    	 break;
    	 
         
	 default:
	        displayMessage("Invalid command!");
	 }
 }
}
