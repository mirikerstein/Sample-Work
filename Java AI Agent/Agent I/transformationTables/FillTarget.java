package transformationTables;

import java.util.HashMap;

@SuppressWarnings("serial")
public class FillTarget extends HashMap<String,String>{
	public FillTarget(){
		this.put("YES,unchanged", "YES");
		this.put("YES,unfilled", "NO");
		this.put("YES,unfilled tb", "TOP_HALF");
		this.put("YES,unfilled tb", "BOTTOM HALF");
		this.put("YES,unfilled rl", "RIGHT_HALF");
		this.put("YES,unfilled rl", "LEFT_HALF");
		this.put("NO,filled", "YES");
		this.put("NO,unchanged", "NO");
		this.put("NO,filled tb", "TOP_HALF");
		this.put("NO,filled tb", "BOTTOM_HALF");
		this.put("NO,filled rl", "RIGHT_HALF");
		this.put("NO,filled rl", "LEFT_HALF");
		this.put("TOP_HALF,filled rest rl", "YES");
		this.put("TOP_HALF,unfilled rest rl", "NO");
		this.put("TOP_HALF,unchanged", "TOP_HALF");
		this.put("TOP_HALF,reversed tb", "BOTTOM HALF");
		this.put("TOP_HALF,rotated fill", "RIGHT_HALF");
		this.put("TOP_HALF,rotated fill", "LEFT_HALF");
		this.put("BOTTOM_HALF,filled rest tb", "YES");
		this.put("BOTTOM_HALF,unfilled rest tb", "NO");
		this.put("BOTTOM_HALF,reversed tb", "TOP_HALF");
		this.put("BOTTOM_HALF,unchanged", "BOTTOM_HALF");
		this.put("BOTTOM_HALF,rotated fill", "RIGHT_HALF");
		this.put("BOTTOM_HALF,rotated fill", "LEFT_HALF");
		this.put("RIGHT_HALF,filled rest tb", "YES");
		this.put("RIGHT_HALF,unfilled rest tb", "NO");
		this.put("RIGHT_HALF,rotated fill", "TOP_HALF");
		this.put("RIGHT_HALF,rotated fill", "BOTTOM_HALF");
		this.put("RIGHT_HALF,unchanged", "RIGHT_HALF");
		this.put("RIGHT_HALF,reversed rl", "LEFT_HALF");
		this.put("LEFT_HALF,filled rest rl", "YES");
		this.put("LEFT_HALF,unfilled rest rl", "NO");
		this.put("LEFT_HALF,rotated fill", "TOP_HALF");
		this.put("LEFT_HALF,rotated fill", "BOTTOM_HALF");
		this.put("LEFT_HALF,reversed rl", "RIGHT_HALF");
		this.put("LEFT_HALF,unchanged", "LEFT_HALF");
	}
	
	//Given the start and the transformation, return the target
			public String getFillTar(String a, String b){
				try{
				String key = a+","+b;
				String val = this.get(key);
				return val;
				}catch(Exception e){
					return "unspecified";
				}
			}
	}


