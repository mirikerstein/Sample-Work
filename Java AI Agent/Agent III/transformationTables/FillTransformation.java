package ravensproject.transformationTables;

import java.util.HashMap;

@SuppressWarnings("serial")
public class FillTransformation extends HashMap<String, String>{
	public FillTransformation(){
		this.put("YES,YES", "unchanged");
		this.put("YES,NO", "unfilled");
		this.put("YES,TOP_HALF", "unfilled tb");
		this.put("YES,BOTTOM HALF", "unfilled tb");
		this.put("YES,RIGHT_HALF", "unfilled rl");
		this.put("YES,LEFT_HALF", "unfilled rl");
		this.put("NO,YES", "filled");
		this.put("NO,NO", "unchanged");
		this.put("NO,TOP_HALF", "filled tb");
		this.put("NO,BOTTOM_HALF", "filled tb");
		this.put("NO,RIGHT_HALF", "filled rl");
		this.put("NO,LEFT_HALF", "filled rl");
		this.put("TOP_HALF,YES", "filled rest rl");
		this.put("TOP_HALF,NO", "unfilled rest rl");
		this.put("TOP_HALF,TOP_HALF", "unchanged");
		this.put("TOP_HALF,BOTTOM HALF", "reversed tb");
		this.put("TOP_HALF,RIGHT_HALF", "rotated fill");
		this.put("TOP_HALF,LEFT_HALF", "rotated fill");
		this.put("BOTTOM_HALF,YES", "filled rest tb");
		this.put("BOTTOM_HALF,NO", "unfilled rest tb");
		this.put("BOTTOM_HALF,TOP_HALF", "reversed tb");
		this.put("BOTTOM_HALF,BOTTOM_HALF", "unchanged");
		this.put("BOTTOM_HALF,RIGHT_HALF", "rotated fill");
		this.put("BOTTOM_HALF,LEFT_HALF", "rotated fill");
		this.put("RIGHT_HALF,YES", "filled rest tb");
		this.put("RIGHT_HALF,NO", "unfilled rest tb");
		this.put("RIGHT_HALF,TOP_HALF", "rotated fill");
		this.put("RIGHT_HALF,BOTTOM_HALF", "rotated fill");
		this.put("RIGHT_HALF,RIGHT_HALF", "unchanged");
		this.put("RIGHT_HALF,LEFT_HALF", "reversed rl");
		this.put("LEFT_HALF,YES", "filled rest rl");
		this.put("LEFT_HALF,NO", "unfilled rest rl");
		this.put("LEFT_HALF,TOP_HALF", "rotated fill");
		this.put("LEFT_HALF,BOTTOM_HALF", "rotated fill");
		this.put("LEFT_HALF,RIGHT_HALF", "reversed rl");
		this.put("LEFT_HALF,LEFT_HALF", "unchanged");
	}
	
	//Given two fills, return the transformation
	public String getFillTrans(String a, String b){
		try{
		String key = a+","+b;
		String val = this.get(key);
		return val;
		}catch(Exception e){
			return "unspecified";
		}
	}

}
