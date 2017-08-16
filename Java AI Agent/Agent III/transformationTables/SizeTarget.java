package ravensproject.transformationTables;

import java.util.HashMap;

public class SizeTarget extends HashMap <String, String>{
	public SizeTarget(){
		this.put("HUGE,unchanged", "HUGE");
		this.put("HUGE,shrunk1", "VERY_LARGE");
		this.put("HUGE,shrunk2", "LARGE");
		this.put("HUGE,shrunk3", "MEDIUM");
		this.put("HUGE,shrunk4", "SMALL");
		this.put("HUGE,shrunk5", "VERY_SMALL");
		
		this.put("VERY_LARGE,grew1", "HUGE");
		this.put("VERY_LARGE,unchanged", "VERY_LARGE");
		this.put("VERY_LARGE,shrunk1", "LARGE");
		this.put("VERY_LARGE,shrunk2", "MEDIUM");
		this.put("VERY_LARGE,shrunk3", "SMALL");
		this.put("VERY_LARGE,shrunk4", "VERY_SMALL");
		
		this.put("LARGE,grew2", "HUGE");
		this.put("LARGE,grew1", "VERY_LARGE");
		this.put("LARGE,unchanged", "LARGE");
		this.put("LARGE,shrunk1", "MEDIUM");
		this.put("LARGE,shrunk2", "SMALL");
		this.put("LARGE,shrunk3", "VERY_SMALL");
		
		this.put("MEDIUM,grew3", "HUGE");
		this.put("MEDIUM,grew2", "VERY_LARGE");
		this.put("MEDIUM,grew1", "LARGE");
		this.put("MEDIUM,unchanged", "MEDIUM");
		this.put("MEDIUM,shrunk1", "SMALL");
		this.put("MEDIUM,shrunk2", "VERY_SMALL");
		
		this.put("SMALL,grew4", "HUGE");
		this.put("SMALL,grew3", "VERY_LARGE");
		this.put("SMALL,grew2", "LARGE");
		this.put("SMALL,grew1", "MEDIUM");
		this.put("SMALL,unchanged", "SMALL");
		this.put("SMALL,shrunk1", "VERY_SMALL");
		
		this.put("VERY_SMALL,grew5", "HUGE");
		this.put("VERY_SMALL,grew4", "VERY_LARGE");
		this.put("VERY_SMALL,grew3", "LARGE");
		this.put("VERY_SMALL,grew2", "MEDIUM");
		this.put("VERY_SMALL,grew1", "SMALL");
		this.put("VERY_SMALL,unchanged", "VERY_SMALL");
		this.put("UNSPECIFIED,unchanged", "UNSPECIFIED");
		
		this.put("VERY_SMALL,unspecified", "UNSPECIFIED");
		this.put("SMALL,unspecified", "UNSPECIFIED");
		this.put("MEDIUM,unspecified", "UNSPECIFIED");
		this.put("LARGE,unspecified", "UNSPECIFIED");
		this.put("VERY_LARGE,unspecified", "UNSPECIFIED");
		this.put("HUGE,unspecified", "UNSPECIFIED");
		this.put("UNSPECIFIED,unspecified", "UNSPECIFIED");
		
		this.put("UNSPECIFIED,unchanged", "UNSPECIFIED");
		this.put("UNSPECIFIED,grew1", "UNSPECIFIED");
		this.put("UNSPECIFIED,grew2", "UNSPECIFIED");
		this.put("UNSPECIFIED,grew3", "UNSPECIFIED");
		this.put("UNSPECIFIED,grew4", "UNSPECIFIED");
		this.put("UNSPECIFIED,grew5", "UNSPECIFIED");
		this.put("UNSPECIFIED,shrunk1", "UNSPECIFIED");
		this.put("UNSPECIFIED,shrunk2", "UNSPECIFIED");
		this.put("UNSPECIFIED,shrunk3", "UNSPECIFIED");
		this.put("UNSPECIFIED,shrunk4", "UNSPECIFIED");
		this.put("UNSPECIFIED,shrunk5", "UNSPECIFIED");
	
		
	
	}

	//Given the start and the transformation, return the target
			public String getSizeTar(String a, String b){
				try{
				String key = a+","+b;
				String val = this.get(key);
				return val;
				}catch(Exception e){
					return "UNSPECIFIED";
				}
			}
}
