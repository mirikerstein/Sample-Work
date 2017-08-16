package ravensproject.content;

import java.util.ArrayList;
import java.util.HashMap;

import ravensproject.*;
import ravensproject.ravensEnums.*;

public class ContentObj {

	// Fields, based on attributes provided in verbal problems
	private String name;
	private String alias;
	private Shape shape;
	private Size size;
	private Size width;
	private Size height;
	private Fill fill;
	private Alignment alignment;
	private ArrayList<String> inside;
	private ArrayList<String> above;
	private ArrayList<String> left;
	private ArrayList<String> overlap;
	private Double angle;

	// Default constructor - initializes the two ArrayLists, and sets all other
	// fields to null
	public ContentObj() {
		this.name = null;
		this.alias = null;
		this.shape = null;
		this.size = null;
		this.fill = null;
		this.width = null;
		this.height = null;
		this.alignment = null;
		this.inside = new ArrayList<String>();
		this.above = new ArrayList<String>();
		this.left = new ArrayList<String>();
		this.overlap = new ArrayList<String>();
		this.angle = 0.0;
	}

	// Constructs a ContentObj given a RavensFigure
	// This ContentObj will not have ANY null fields, so appropriate
	// substitutions will be made
	// if a specific attribute is not present in the verbal problem.
	public ContentObj(RavensObject raven) {
		HashMap<String, String> map = raven.getAttributes();
		// Set name field
		this.name = raven.getName();
		// Leave alias unspecified
		this.alias = "unspecified";
		// Set shape, size, and fill
		this.shape = enumShape(map.get("shape"));
		if (map.get("size") != null) {
			this.size = enumSize(map.get("size"));
		} else {
			this.size = Size.UNSPECIFIED;
		}
		this.fill = enumFill(map.get("fill"));
		// Set the alignment, after checking if attribute is present
		if (map.get("alignment") != null) {
			this.alignment = enumAlignment(map.get("alignment"));
		} else {
			this.alignment = Alignment.UNSPECIFIED;
		}
		// Set the inside field, after checking if the attribute is present
		if (map.get("inside") != null) {
			this.inside = arrangeInside(map.get("inside"));
		} else {
			this.inside = new ArrayList<String>();
		}
		// Set the above field, after checking if the attribute is present
		if (map.get("above") != null) {
			this.above = arrangeAbove(map.get("above"));
		} else {
			this.above = new ArrayList<String>();
		}

		// Set the left of field, after checking if the attribute is present
		if (map.get("left-of") != null) {
			this.left = arrangeLeft(map.get("left-of"));
		} else {
			this.left = new ArrayList<String>();
		}

		// Set the above field, after checking if the attribute is present
		if (map.get("overlaps") != null) {
			this.overlap = arrangeOverlap(map.get("overlaps"));
		} else {
			this.overlap = new ArrayList<String>();
		}
		// Set the angle, after checking if the attribute is present.
		// If the attribute is not present, the value (rather than null) will be
		// 613
		if (map.get("angle") != null) {
			this.angle = Double.valueOf(map.get("angle"));
		} else {
			this.angle = 613.0;
		}

		// Sets the width and the height. If no specific width and height are
		// specified, they are set to the same
		// size as the general size dimension given.

		if (map.get("width") != null) {
			this.width = enumSize(map.get("width"));
		} else {
			this.width = this.size;
		}

		if (map.get("height") != null) {
			this.height = enumSize(map.get("height"));
		} else {
			this.height = this.size;
		}

	}

	// CHANGE ENUM METHODS BACK TO PRIVATE AFTER TESTING

	// Given a string attribute from the verbal problem, returns the
	// corresponding Shape enum
	public Shape enumShape(String attribute) {
		Shape shape = null;
		if (attribute.equals("right triangle")) {
			shape = Shape.RIGHT_TRIANGLE;
		} else if (attribute.equals("pac-man")) {
			shape = Shape.PACMAN;
		} else if (attribute.equals("rectangle")){
			shape = Shape.SQUARE;
		}else{
			shape = Shape.valueOf(attribute.toUpperCase());
		}

		return shape;
	}

	// Given a string attribute from the verbal problem, returns the
	// corresponding Fill enum
	public Fill enumFill(String attribute) {
		Fill fill = null;
		String[] splitAttribute = attribute.split("-");
		if (splitAttribute.length == 1) {
			fill = Fill.valueOf(attribute.toUpperCase());
		} else {
			String newAttribute = splitAttribute[0] + "_" + splitAttribute[1];
			fill = Fill.valueOf(newAttribute.toUpperCase());
		}
		return fill;
	}

	// Given a string attribute from the verbal problem, returns the
	// corresponding alignment enum
	public Alignment enumAlignment(String attribute) {
		Alignment alignment = null;
		String[] splitAttribute = attribute.split("-");
		if (splitAttribute.length == 1) {
			alignment = Alignment.valueOf(attribute.toUpperCase());
		} else {
			String newAttribute = splitAttribute[0] + "_" + splitAttribute[1];
			alignment = Alignment.valueOf(newAttribute.toUpperCase());
		}
		return alignment;
	}

	// Given a string attribute from the verbal problem, returns the
	// corresponding size enum
	public Size enumSize(String attribute) {
		Size size = null;
		String[] splitAttribute = attribute.split(" ");
		if (splitAttribute.length == 1) {
			size = Size.valueOf(attribute.toUpperCase());
		} else {
			String newAttribute = splitAttribute[0] + "_" + splitAttribute[1];
			size = Size.valueOf(newAttribute.toUpperCase());
		}
		return size;
	}

	// Creates the ArrayList of 'inside'
	public ArrayList<String> arrangeInside(String attribute) {
		ArrayList<String> inside = new ArrayList<String>();
		String[] insideSplit = attribute.split(",");
		for (int i = 0; i < insideSplit.length; i++) {
			inside.add(insideSplit[i]);
		}
		return inside;
	}

	// Creates the ArrayList of 'above'
	public ArrayList<String> arrangeAbove(String attribute) {
		ArrayList<String> above = new ArrayList<String>();
		String[] aboveSplit = attribute.split(",");
		for (int i = 0; i < aboveSplit.length; i++) {
			above.add(aboveSplit[i]);
		}
		return above;
	}

	// Creates the ArrayList of 'left-of'
	public ArrayList<String> arrangeLeft(String attribute) {
		ArrayList<String> left = new ArrayList<String>();
		String[] aboveSplit = attribute.split(",");
		for (int i = 0; i < aboveSplit.length; i++) {
			left.add(aboveSplit[i]);
		}
		return left;
	}

	// Creates the ArrayList of 'overlap'
	public ArrayList<String> arrangeOverlap(String attribute) {
		ArrayList<String> overlap = new ArrayList<String>();
		String[] aboveSplit = attribute.split(",");
		for (int i = 0; i < aboveSplit.length; i++) {
			overlap.add(aboveSplit[i]);
		}
		return overlap;
	}

	// All necessary accessor methods

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}

	public Shape getShape() {
		return shape;
	}

	public Size getSize() {
		return size;
	}

	public Fill getFill() {
		return fill;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public ArrayList<String> getInside() {
		return inside;
	}

	public ArrayList<String> getAbove() {
		return above;
	}

	public Double getAngle() {
		return angle;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String toString() {
		String print = "Content Object Details:" + "\nName: " + this.name
				+ "\nAlias: " + this.alias + "\nShape: " + this.shape
				+ "\nSize: " + this.size + "\nWidth: "+this.width+"\nHeight: "+this.height+"\nFill: " + this.fill
				+ "\nAlignment: " + this.alignment + "\nAngle: " + this.angle
				+ "\nInside: " + this.inside + "\nAbove: " + this.above;
		return print;
	}

	public String aliasSetToString() {
		return "\nName: " + this.name + ", Alias: " + this.alias;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public void setFill(Fill fill) {
		this.fill = fill;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public void setInside(ArrayList<String> inside) {
		this.inside = inside;
	}

	public void setAbove(ArrayList<String> above) {
		this.above = above;
	}

	public void setAngle(Double angle) {
		this.angle = angle;
	}

	public Size getWidth() {
		return width;
	}

	public void setWidth(Size width) {
		this.width = width;
	}

	public Size getHeight() {
		return height;
	}

	public void setHeight(Size height) {
		this.height = height;
	}

	public ArrayList<String> getLeft() {
		return left;
	}

	public void setLeft(ArrayList<String> left) {
		this.left = left;
	}

	public ArrayList<String> getOverlap() {
		return overlap;
	}

	public void setOverlap(ArrayList<String> overlap) {
		this.overlap = overlap;
	}

}
