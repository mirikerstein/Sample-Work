package ravensproject.transformationTables;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class RightTriangleTransformation extends HashMap <AngleSet, String>{
	
	private ArrayList<AngleSet>sets;
	
	public RightTriangleTransformation(){
		
		this.sets = new ArrayList<AngleSet>();
		fillAngleSet();
		this.put(sets.get(0), "horizontal");
		this.put(sets.get(1), "horizontal");
		this.put(sets.get(2), "horizontal");
		this.put(sets.get(3), "horizontal");
		this.put(sets.get(4), "horizontal");
		this.put(sets.get(5), "horizontal");
		this.put(sets.get(6), "horizontal");
		this.put(sets.get(7), "horizontal");
		this.put(sets.get(8), "horizontal");
		this.put(sets.get(9), "horizontal");
		this.put(sets.get(10), "vertical");
		this.put(sets.get(11), "vertical");
		this.put(sets.get(12), "vertical");
		this.put(sets.get(13), "vertical");
		this.put(sets.get(14), "vertical");
		this.put(sets.get(15), "vertical");
		this.put(sets.get(16), "vertical");
		this.put(sets.get(17), "vertical");
		this.put(sets.get(18), "vertical");
		this.put(sets.get(19), "vertical");
	}

	//Given two angles, return the transformation
	public String getRightTriangleTrans(Double a, Double b){
		try{
		AngleSet key = findAngleSet(a, b);
		String val = this.get(key);
		if(val == null){
			val = "unspecified";
		}
		return val;
		}catch(Exception e){
			return "unspecified";
		}
	}
	private AngleSet findAngleSet(double a, double b){
		AngleSet found = null;
		for(AngleSet as:this.sets){
			if(as.getAng1()==(a)&&as.getAng2()==(b)){
				found = as;
				break;
			}
		}
		return found;
	}
	private void fillAngleSet(){
		this.sets.add(new AngleSet(0,270));
		this.sets.add(new AngleSet(270,0));
		this.sets.add(new AngleSet(360,270));
		this.sets.add(new AngleSet(270,360));
		this.sets.add(new AngleSet(180,90));
		this.sets.add(new AngleSet(90,180));
		this.sets.add(new AngleSet(45,225));
		this.sets.add(new AngleSet(225,45));
		this.sets.add(new AngleSet(135,135));
		this.sets.add(new AngleSet(315,315));
		this.sets.add(new AngleSet(0,90));
		this.sets.add(new AngleSet(90,0));
		this.sets.add(new AngleSet(360,90));
		this.sets.add(new AngleSet(90,360));
		this.sets.add(new AngleSet(45,45));
		this.sets.add(new AngleSet(225,225));
		this.sets.add(new AngleSet(180,270));
		this.sets.add(new AngleSet(270,180));
		this.sets.add(new AngleSet(135,315));
		this.sets.add(new AngleSet(315,135));
	}
}
