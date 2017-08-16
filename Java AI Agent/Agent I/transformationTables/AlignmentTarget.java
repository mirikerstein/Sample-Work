package transformationTables;

import java.util.HashMap;

@SuppressWarnings("serial")
public class AlignmentTarget extends HashMap <String,String>{
	public AlignmentTarget(){
		this.put("TOP,unchanged", "TOP");
		this.put("TOP,flip vertical", "BOTTOM");
		this.put("TOP,clockwise", "RIGHT");
		this.put("TOP,counter", "LEFT");
		this.put("TOP,slide", "TOP_RIGHT");
		this.put("TOP,flip slide", "BOTTOM_RIGHT");
		this.put("TOP,slide", "TOP_LEFT");
		this.put("TOP,flip slide", "BOTTOM_LEFT");
		
		this.put("BOTTOM,flip vertical", "TOP");
		this.put("BOTTOM,unchanged", "BOTTOM");
		this.put("BOTTOM,counter", "RIGHT");
		this.put("BOTTOM,clockwise", "LEFT");
		this.put("BOTTOM,flip slide", "TOP_RIGHT");
		this.put("BOTTOM,slide", "BOTTOM_RIGHT");
		this.put("BOTTOM,flip slide", "TOP_LEFT");
		this.put("BOTTOM,slide", "BOTTOM_LEFT");
		
		this.put("RIGHT,counter", "TOP");
		this.put("RIGHT,clockwise", "BOTTOM");
		this.put("RIGHT,unchanged", "RIGHT");
		this.put("RIGHT,flip horizontal", "LEFT");
		this.put("RIGHT,slide", "TOP_RIGHT");
		this.put("RIGHT,slide", "BOTTOM_RIGHT");
		this.put("RIGHT,flip slide", "TOP_LEFT");
		this.put("RIGHT,flip slide", "BOTTOM_LEFT");
		
		this.put("LEFT,clockwise", "TOP");
		this.put("LEFT,counter", "BOTTOM");
		this.put("LEFT,flip horizontal", "RIGHT");
		this.put("LEFT,unchanged", "LEFT");
		this.put("LEFT,flip slide", "TOP_RIGHT");
		this.put("LEFT,flip slide", "BOTTOM_RIGHT");
		this.put("LEFT,slide", "TOP_LEFT");
		this.put("LEFT,slide", "BOTTOM_LEFT");
		
		this.put("TOP_RIGHT,slide", "TOP");
		this.put("TOP_RIGHT,flip slide", "BOTTOM");
		this.put("TOP_RIGHT,slide", "RIGHT");
		this.put("TOP_RIGHT,flip slide", "LEFT");
		this.put("TOP_RIGHT,unchanged", "TOP_RIGHT");
		this.put("TOP_RIGHT,flip vertical", "BOTTOM_RIGHT");
		this.put("TOP_RIGHT,flip horizontal", "TOP_LEFT");
		this.put("TOP_RIGHT,flip xy", "BOTTOM_LEFT");
		
		this.put("BOTTOM_RIGHT,flip slide", "TOP");
		this.put("BOTTOM_RIGHT,slide", "BOTTOM");
		this.put("BOTTOM_RIGHT,slide", "RIGHT");
		this.put("BOTTOM_RIGHT,flip slide", "LEFT");
		this.put("BOTTOM_RIGHT,flip vertical", "TOP_RIGHT");
		this.put("BOTTOM_RIGHT,unchanged", "BOTTOM_RIGHT");
		this.put("BOTTOM_RIGHT,flip xy", "TOP_LEFT");
		this.put("BOTTOM_RIGHT,flip horizontal", "BOTTOM_LEFT");
		
		this.put("TOP_LEFT,slide", "TOP");
		this.put("TOP_LEFT,flip slide", "BOTTOM");
		this.put("TOP_LEFT,flip slide", "RIGHT");
		this.put("TOP_LEFT,slide", "LEFT");
		this.put("TOP_LEFT,flip horizontal", "TOP_RIGHT");
		this.put("TOP_LEFT,flip xy", "BOTTOM_RIGHT");
		this.put("TOP_LEFT,flip unchanged", "TOP_LEFT");
		this.put("TOP_LEFT,flip vertical", "BOTTOM_LEFT");
		
		this.put("BOTTOM_LEFT,flip slide", "TOP");
		this.put("BOTTOM_LEFT,slide", "BOTTOM");
		this.put("BOTTOM_LEFT,flip slide", "RIGHT");
		this.put("BOTTOM_LEFT,slide", "LEFT");
		this.put("BOTTOM_LEFT,flip xy", "TOP_RIGHT");
		this.put("BOTTOM_LEFT,flip horizontal", "BOTTOM_RIGHT");
		this.put("BOTTOM_LEFT,flip vertical", "TOP_LEFT");
		this.put("BOTTOM_LEFT,unchanged", "BOTTOM_LEFT");
	}
	
	//Given the start and the transformation, return the target
		public String getAlignmentTar(String a, String b){
			try{
			String key = a+","+b;
			String val = this.get(key);
			return val;
			}catch(Exception e){
				return "unspecified";
			}
		}

}
