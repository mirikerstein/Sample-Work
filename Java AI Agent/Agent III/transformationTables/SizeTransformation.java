package ravensproject.transformationTables;

import java.util.HashMap;

@SuppressWarnings("serial")
public class SizeTransformation extends HashMap <String, String>{

	public SizeTransformation(){
		this.put("HUGE,HUGE", "unchanged");
		this.put("HUGE,VERY_LARGE", "shrunk1");
		this.put("HUGE,LARGE", "shrunk2");
		this.put("HUGE,MEDIUM", "shrunk3");
		this.put("HUGE,SMALL", "shrunk4");
		this.put("HUGE,VERY_SMALL", "shrunk5");
		
		this.put("VERY_LARGE,HUGE", "grew1");
		this.put("VERY_LARGE,VERY_LARGE", "unchanged");
		this.put("VERY_LARGE,LARGE", "shrunk1");
		this.put("VERY_LARGE,MEDIUM", "shrunk2");
		this.put("VERY_LARGE,SMALL", "shrunk3");
		this.put("VERY_LARGE,VERY_SMALL", "shrunk4");
		
		this.put("LARGE,HUGE", "grew2");
		this.put("LARGE,VERY_LARGE", "grew1");
		this.put("LARGE,LARGE", "unchanged");
		this.put("LARGE,MEDIUM", "shrunk1");
		this.put("LARGE,SMALL", "shrunk2");
		this.put("LARGE,VERY_SMALL", "shrunk3");
		
		this.put("MEDIUM,HUGE", "grew3");
		this.put("MEDIUM,VERY_LARGE", "grew2");
		this.put("MEDIUM,LARGE", "grew1");
		this.put("MEDIUM,MEDIUM", "unchanged");
		this.put("MEDIUM,SMALL", "shrunk1");
		this.put("MEDIUM,VERY_SMALL", "shrunk2");
		
		this.put("SMALL,HUGE", "grew4");
		this.put("SMALL,VERY_LARGE", "grew3");
		this.put("SMALL,LARGE", "grew2");
		this.put("SMALL,MEDIUM", "grew1");
		this.put("SMALL,SMALL", "unchanged");
		this.put("SMALL,VERY_SMALL", "shrunk1");
		
		this.put("VERY_SMALL,HUGE", "grew5");
		this.put("VERY_SMALL,VERY_LARGE", "grew4");
		this.put("VERY_SMALL,LARGE", "grew3");
		this.put("VERY_SMALL,MEDIUM", "grew2");
		this.put("VERY_SMALL,SMALL", "grew1");
		this.put("VERY_SMALL,VERY_SMALL", "unchanged");
		
		this.put("UNSPECIFIED,UNSPECIFIED", "unspecified");
		this.put("UNSPECIFIED,VERY_SMALL", "unspecified");
		this.put("UNSPECIFIED,SMALL", "unspecified");
		this.put("UNSPECIFIED,MEDIUM", "unspecified");
		this.put("UNSPECIFIED,LARGE", "unspecified");
		this.put("UNSPECIFIED,VERY_LARGE", "unspecified");
		this.put("UNSPECIFIED,HUGE", "unspecified");
		
		this.put("VERY_SMALL,UNSPECIFIED", "unspecified");
		this.put("SMALL,UNSPECIFIED", "unspecified");
		this.put("MEDIUM,UNSPECIFIED", "unspecified");
		this.put("LARGE,UNSPECIFIED", "unspecified");
		this.put("VERY_LARGE,UNSPECIFIED", "unspecified");
		this.put("HUGE,UNSPECIFIED", "unspecified");
	}
	
	//Given two sizes, return the transformation
	public String getSizeTrans(String a, String b){
		try{
		String key = a+","+b;
		String val = this.get(key);
		return val;
		}catch(Exception e){
			return "UNSPECIFIED";
		}
	}
}
