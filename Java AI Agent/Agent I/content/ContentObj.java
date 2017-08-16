package content;

import java.util.ArrayList;
import java.util.HashMap;

import ravensproject.*;
import ravensEnums.*;

public class ContentObj {

	// Fields, based on attributes provided in verbal problems
	private String name;
	private String alias;
	private Shape shape;
	private Size size;
	private Fill fill;
	private Alignment alignment;
	private ArrayList<String> inside;
	private ArrayList<String> above;
	private Double angle;

	// Default constructor - initializes the two ArrayLists, and sets all other
	// fields to null
	public ContentObj() {
		this.name = null;
		this.alias = null;
		this.shape = null;
		this.size = null;
		this.fill = null;
		this.alignment = null;
		this.inside = new ArrayList<String>();
		this.above = new ArrayList<String>();
		this.angle = 0.0;
	}
	
	//Constructs a ContentObj given a RavensFigure
	//This ContentObj will not have ANY null fields, so appropriate substitutions will be made
	//if a specific attribute is not present in the verbal problem.
	public ContentObj(RavensObject raven){
		HashMap<String,String>map = raven.getAttributes();
		//Set name field
		this.name = raven.getName();
		//Leave alias unspecified
		this.alias = "unspecified";
		//Set shape, size, and fill
		this.shape = enumShape(map.get("shape"));
		this.size = enumSize(map.get("size"));
		this.fill = enumFill(map.get("fill"));
		//Set the alignment, after checking if attribute is present
		if(map.get("alignment")!=null){
			this.alignment = enumAlignment(map.get("alignment"));
		}else{
			this.alignment = Alignment.UNSPECIFIED;
		}
		//Set the inside field, after checking if the attribute is present
		if(map.get("inside")!=null){
			this.inside = arrangeInside(map.get("inside"));
		}else{
			this.inside = new ArrayList<String>();
		}
		//Set the above field, after checking if the attribute is present
		if(map.get("above")!=null){
			this.above = arrangeAbove(map.get("above"));
		}else{
			this.above = new ArrayList<String>();
		}
		//Set the angle, after checking if the attribute is present.
		//If the attribute is not present, the value (rather than null) will be 613
		if(map.get("angle")!=null){
			this.angle = Double.valueOf(map.get("angle"));
		}else{
			this.angle = 613.0;
		}
	}
	
	//CHANGE ENUM METHODS BACK TO PRIVATE AFTER TESTING
	
	//Given a string attribute from the verbal problem, returns the corresponding Shape enum
	public Shape enumShape(String attribute){
		Shape shape = null;
		if(attribute.equals("right triangle")){
			shape = Shape.RIGHT_TRIANGLE;
		}else if(attribute.equals("pac-man")){
			shape = Shape.PACMAN;
		}else{
			shape = Shape.valueOf(attribute.toUpperCase());
		}
		
		return shape;
	}
	
	//Given a string attribute from the verbal problem, returns the corresponding Fill enum
	public Fill enumFill(String attribute){
		Fill fill = null;
		String[]splitAttribute = attribute.split("-");
		if(splitAttribute.length == 1){
			fill = Fill.valueOf(attribute.toUpperCase());
		}else{
			String newAttribute = splitAttribute[0]+"_"+splitAttribute[1];
			fill = Fill.valueOf(newAttribute.toUpperCase());
		}
		return fill;
	}
	
	//Given a string attribute from the verbal problem, returns the corresponding alignment enum
	public Alignment enumAlignment(String attribute){
		Alignment alignment = null;
		String[]splitAttribute = attribute.split("-");
		if(splitAttribute.length == 1){
			alignment = Alignment.valueOf(attribute.toUpperCase());
		}else{
			String newAttribute = splitAttribute[0]+"_"+splitAttribute[1];
			alignment = Alignment.valueOf(newAttribute.toUpperCase());
		}
		return alignment;
	}
	
	//Given a string attribute from the verbal problem, returns the corresponding size enum
	public Size enumSize(String attribute){
		Size size = null;
		String[]splitAttribute = attribute.split(" ");
		if(splitAttribute.length == 1){
			size = Size.valueOf(attribute.toUpperCase());
		}else{
			String newAttribute = splitAttribute[0]+"_"+splitAttribute[1];
			size = Size.valueOf(newAttribute.toUpperCase());
		}
		return size;
	}
	
	//Creates the ArrayList of 'inside'
	public ArrayList<String> arrangeInside(String attribute){
		ArrayList<String>inside = new ArrayList<String>();
		String[]insideSplit = attribute.split(",");
		for(int i = 0; i<insideSplit.length; i++){
			inside.add(insideSplit[i]);
		}
		return inside;
	}
	
	//Creates the ArrayList of 'above'
	public ArrayList<String> arrangeAbove(String attribute){
		ArrayList<String>above = new ArrayList<String>();
		String[]aboveSplit = attribute.split(",");
		for(int i = 0; i<aboveSplit.length; i++){
			above.add(aboveSplit[i]);
		}
		return above;
	}

	//All necessary accessor methods
	
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
	
	public void setAlias(String alias){
		this.alias = alias;
	}
	
	public String toString(){
		String print = "Content Object Details:"+
	"\nName: "+this.name+
	"\nAlias: "+this.alias+
	"\nShape: "+this.shape+
	"\nSize: "+this.size+
	"\nFill: "+this.fill+
	"\nAlignment: "+this.alignment+
	"\nAngle: "+this.angle+
	"\nInside: "+this.inside+
	"\nAbove: "+this.above;
		return print;
	}
	
	public String aliasSetToString(){
		return "\nName: "+this.name+", Alias: "+this.alias;
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
	
	

}
