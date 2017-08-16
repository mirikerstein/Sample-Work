package transformationTables;

import java.util.HashMap;

@SuppressWarnings("serial")
public class AlignmentTransformation extends HashMap<String,String>{
	public AlignmentTransformation(){
		this.put("TOP,TOP", "unchanged");
		this.put("TOP,BOTTOM", "flip vertical");
		this.put("TOP,RIGHT", "clockwise");
		this.put("TOP,LEFT", "counter");
		this.put("TOP,TOP_RIGHT", "slide");
		this.put("TOP,BOTTOM_RIGHT", "flip slide");
		this.put("TOP,TOP_LEFT", "slide");
		this.put("TOP,BOTTOM_LEFT", "flip slide");
		
		this.put("BOTTOM,TOP", "flip vertical");
		this.put("BOTTOM,BOTTOM", "unchanged");
		this.put("BOTTOM,RIGHT", "counter");
		this.put("BOTTOM,LEFT", "clockwise");
		this.put("BOTTOM,TOP_RIGHT", "flip slide");
		this.put("BOTTOM,BOTTOM_RIGHT", "slide");
		this.put("BOTTOM,TOP_LEFT", "flip slide");
		this.put("BOTTOM,BOTTOM_LEFT", "slide");
		
		this.put("RIGHT,TOP", "counter");
		this.put("RIGHT,BOTTOM", "clockwise");
		this.put("RIGHT,RIGHT", "unchanged");
		this.put("RIGHT,LEFT", "flip horizontal");
		this.put("RIGHT,TOP_RIGHT", "slide");
		this.put("RIGHT,BOTTOM_RIGHT", "slide");
		this.put("RIGHT,TOP_LEFT", "flip slide");
		this.put("RIGHT,BOTTOM_LEFT", "flip slide");
		
		this.put("LEFT,TOP", "clockwise");
		this.put("LEFT,BOTTOM", "counter");
		this.put("LEFT,RIGHT", "flip horizontal");
		this.put("LEFT,LEFT", "unchanged");
		this.put("LEFT,TOP_RIGHT", "flip slide");
		this.put("LEFT,BOTTOM_RIGHT", "flip slide");
		this.put("LEFT,TOP_LEFT", "slide");
		this.put("LEFT,BOTTOM_LEFT", "slide");
		
		this.put("TOP_RIGHT,TOP", "slide");
		this.put("TOP_RIGHT,BOTTOM", "flip slide");
		this.put("TOP_RIGHT,RIGHT", "slide");
		this.put("TOP_RIGHT,LEFT", "flip slide");
		this.put("TOP_RIGHT,TOP_RIGHT", "unchanged");
		this.put("TOP_RIGHT,BOTTOM_RIGHT", "flip vertical");
		this.put("TOP_RIGHT,TOP_LEFT", "flip horizontal");
		this.put("TOP_RIGHT,BOTTOM_LEFT", "flip xy");
		
		this.put("BOTTOM_RIGHT,TOP", "flip slide");
		this.put("BOTTOM_RIGHT,BOTTOM", "slide");
		this.put("BOTTOM_RIGHT,RIGHT", "slide");
		this.put("BOTTOM_RIGHT,LEFT", "flip slide");
		this.put("BOTTOM_RIGHT,TOP_RIGHT", "flip vertical");
		this.put("BOTTOM_RIGHT,BOTTOM_RIGHT", "unchanged");
		this.put("BOTTOM_RIGHT,TOP_LEFT", "flip xy");
		this.put("BOTTOM_RIGHT,BOTTOM_LEFT", "flip horizontal");
		
		this.put("TOP_LEFT,TOP", "slide");
		this.put("TOP_LEFT,BOTTOM", "flip slide");
		this.put("TOP_LEFT,RIGHT", "flip slide");
		this.put("TOP_LEFT,LEFT", "slide");
		this.put("TOP_LEFT,TOP_RIGHT", "flip horizontal");
		this.put("TOP_LEFT,BOTTOM_RIGHT", "flip xy");
		this.put("TOP_LEFT,TOP_LEFT", "flip unchanged");
		this.put("TOP_LEFT,BOTTOM_LEFT", "flip vertical");
		
		this.put("BOTTOM_LEFT,TOP", "flip slide");
		this.put("BOTTOM_LEFT,BOTTOM", "slide");
		this.put("BOTTOM_LEFT,RIGHT", "flip slide");
		this.put("BOTTOM_LEFT,LEFT", "slide");
		this.put("BOTTOM_LEFT,TOP_RIGHT", "flip xy");
		this.put("BOTTOM_LEFT,BOTTOM_RIGHT", "flip horizontal");
		this.put("BOTTOM_LEFT,TOP_LEFT", "flip vertical");
		this.put("BOTTOM_LEFT,BOTTOM_LEFT", "unchanged");
		this.put("UNSPECIFIED,UNSPECIFIED", "UNSPECIFIED");
		
		
	}

	//Given two alignments, return the transformation
	public String getAlignmentTrans(String a, String b){
		try{
		String key = a+","+b;
		String val = this.get(key);
		return val;
		}catch(Exception e){
			return "unspecified";
		}
	}
}
