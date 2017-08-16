package transformationTables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("serial")
public class ShapeTransformations extends HashMap<String, String> {
	public ShapeTransformations() {
		this.put("SQUARE", "became square");
		this.put("RECTANGLE", "became rectangle");
		this.put("CIRCLE", "became circle");
		this.put("ELLIPSE", "became ellipse");
		this.put("TRIANGLE", "became triangle");
		this.put("RIGHT_TRIANGLE", "became right triangle");
		this.put("PENTAGON", "became pentagon");
		this.put("HEXAGON", "became hexagon");
		this.put("OCTAGON", "became octagon");
		this.put("DIAMOND", "became diamond");
		this.put("HEART", "became heart");
		this.put("PLUS", "became plus");
		this.put("PACMAN", "became pacman");
		this.put("STAR", "became star");
		this.put("UNSPECIFIED", "became unspecified");
	}

	// Given a shape, returns the transformation
	public String getShapeTrans(String a) {
		try{
		return this.get(a);
		}catch(Exception e){
			return "unspecified";
		}
	}

	// Given a shape, returns the target
	public String getShapeTar(String a) {
		try{
		String tar = "";
		Iterator it = this.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = (Map.Entry<String,String>) it.next();
			String k = pair.getKey();
			String v = pair.getValue();
			if (v.equals(a)) {
				tar = k;
				break;
			}
		}
		return tar;
		}catch(Exception e){
			return "unspecified";
		}
	}
	}

