package transformationTables;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class TriangleTransformation extends HashMap <AngleSet, String>{
	
	private ArrayList<AngleSet>sets;
	
	public TriangleTransformation(){
		
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
		this.put(sets.get(10), "horizontal");
		this.put(sets.get(11), "vertical");
		this.put(sets.get(12), "vertical");
		this.put(sets.get(13), "vertical");
		this.put(sets.get(14), "vertical");
		this.put(sets.get(15), "vertical");
		this.put(sets.get(16), "vertical");
		this.put(sets.get(17), "vertical");
		this.put(sets.get(18), "vertical");
		this.put(sets.get(19), "vertical");
		this.put(sets.get(20), "vertical");
		this.put(sets.get(21), "vertical");
	}

	//Given two angles, return the transformation
	public String getTriangleTrans(Double a, Double b){
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
		this.sets.add(new AngleSet(0,0));
		this.sets.add(new AngleSet(0,360));
		this.sets.add(new AngleSet(360,0));
		this.sets.add(new AngleSet(360,0));
		this.sets.add(new AngleSet(180,180));
		this.sets.add(new AngleSet(90,270));
		this.sets.add(new AngleSet(270,90));
		this.sets.add(new AngleSet(45,315));
		this.sets.add(new AngleSet(315,45));
		this.sets.add(new AngleSet(225,135));
		this.sets.add(new AngleSet(135,225));//horizontal
		this.sets.add(new AngleSet(90,0));//vertical
		this.sets.add(new AngleSet(0,180));
		this.sets.add(new AngleSet(180,0));
		this.sets.add(new AngleSet(360,180));
		this.sets.add(new AngleSet(180,360));
		this.sets.add(new AngleSet(90,90));
		this.sets.add(new AngleSet(270,270));
		this.sets.add(new AngleSet(45,135));
		this.sets.add(new AngleSet(135,45));
		this.sets.add(new AngleSet(225,315));
		this.sets.add(new AngleSet(315,225));
	}
}
