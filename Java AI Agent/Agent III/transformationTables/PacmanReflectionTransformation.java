package ravensproject.transformationTables;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class PacmanReflectionTransformation extends HashMap <AngleSet, String>{
	
	private ArrayList<AngleSet>sets;
	
	public PacmanReflectionTransformation(){
		
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
		this.put(sets.get(20), "vertical");
	}

	//Given two angles, return the transformation
	public String getPacmanReflectionTrans(Double a, Double b){
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
		AngleSet a1 = new AngleSet(0,180);
		AngleSet a2 = new AngleSet(180,0);
		AngleSet a3 = new AngleSet(45,135);
		AngleSet a4 = new AngleSet(135,45);
		AngleSet a5 = new AngleSet(225,315);
		AngleSet a6 = new AngleSet(315,225);
		AngleSet a7 = new AngleSet(90,90);
		AngleSet a8 = new AngleSet(270,270);
		AngleSet a9 = new AngleSet(180,360);
		AngleSet a10 = new AngleSet(360,180);
		AngleSet a11 = new AngleSet(315,45);
		AngleSet a12 = new AngleSet(45,315);
		AngleSet a13 = new AngleSet(360,360);
		AngleSet a14 = new AngleSet(90,270);
		AngleSet a15 = new AngleSet(270,90);
		AngleSet a16 = new AngleSet(135,225);
		AngleSet a17 = new AngleSet(225,135);
		AngleSet a18 = new AngleSet(180,180);
		AngleSet a19 = new AngleSet(0,0);
		AngleSet a20 = new AngleSet(0,360);
		AngleSet a21 = new AngleSet(360,0);
		this.sets.add(a1);
		this.sets.add(a2);
		this.sets.add(a3);
		this.sets.add(a4);
		this.sets.add(a5);
		this.sets.add(a6);
		this.sets.add(a7);
		this.sets.add(a8);
		this.sets.add(a9);
		this.sets.add(a10);
		this.sets.add(a11);
		this.sets.add(a12);
		this.sets.add(a13);
		this.sets.add(a14);
		this.sets.add(a15);
		this.sets.add(a16);
		this.sets.add(a17);
		this.sets.add(a18);
		this.sets.add(a19);
		this.sets.add(a20);
		this.sets.add(a21);
	}

}
