package ravensproject.transformationTables;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class PacmanReflectionTarget extends HashMap<AngleTransSet,Double>{
	private ArrayList<AngleTransSet>sets;
	
	public PacmanReflectionTarget(){
		this.sets = new ArrayList<AngleTransSet>();
		fillSets();
		this.put(this.sets.get(0), 180.0);
		this.put(this.sets.get(1), 0.0);
		this.put(this.sets.get(2), 135.0);
		this.put(this.sets.get(3), 45.0);
		this.put(this.sets.get(4), 315.0);
		this.put(this.sets.get(5), 225.0);
		this.put(this.sets.get(6), 90.0);
		this.put(this.sets.get(7), 270.0);
		this.put(this.sets.get(8), 360.0);
		this.put(this.sets.get(9), 180.0);
		this.put(this.sets.get(10), 45.0);
		this.put(this.sets.get(11), 315.0);
		this.put(this.sets.get(12), 270.0);
		this.put(this.sets.get(13), 90.0);
		this.put(this.sets.get(14), 225.0);
		this.put(this.sets.get(15), 135.0);
		this.put(this.sets.get(16), 180.0);
		this.put(this.sets.get(17), 0.0);
		this.put(this.sets.get(18), 360.0);
		this.put(this.sets.get(19), 0.0);
		this.put(this.sets.get(20), 360.0);
	}
	
	private void fillSets(){
		this.sets.add(new AngleTransSet(0,"horizontal"));
		this.sets.add(new AngleTransSet(180,"horizontal"));
		this.sets.add(new AngleTransSet(45,"horizontal"));
		this.sets.add(new AngleTransSet(135,"horizontal"));
		this.sets.add(new AngleTransSet(225,"horizontal"));
		this.sets.add(new AngleTransSet(315,"horizontal"));
		this.sets.add(new AngleTransSet(90,"horizontal"));
		this.sets.add(new AngleTransSet(270,"horizontal"));
		this.sets.add(new AngleTransSet(180,"horizontal"));
		this.sets.add(new AngleTransSet(360,"horizontal"));
		this.sets.add(new AngleTransSet(315,"vertical"));
		this.sets.add(new AngleTransSet(45,"vertical"));
		this.sets.add(new AngleTransSet(360,"vertical"));
		this.sets.add(new AngleTransSet(90,"vertical"));
		this.sets.add(new AngleTransSet(270,"vertical"));
		this.sets.add(new AngleTransSet(135,"vertical"));
		this.sets.add(new AngleTransSet(225,"vertical"));
		this.sets.add(new AngleTransSet(180,"vertical"));
		this.sets.add(new AngleTransSet(0,"vertical"));
		this.sets.add(new AngleTransSet(0,"vertical"));
		this.sets.add(new AngleTransSet(360,"vertical"));
	}
	
	//Given two angles, find the set
			private AngleTransSet findAngleTransSet(Double a, String b){
				AngleTransSet found = null;
				for(AngleTransSet as:this.sets){
					if(as.getAngle()==(a)&&as.getTrans().equals(b)){
						found = as;
						break;
					}
				}
				return found;
			}
			
			//Given the start and the transformation, return the target
			public Double getPacmanReflectionTar(Double a, String b){
				try{
				AngleTransSet trans = findAngleTransSet(a,b);
				Double val = this.get(trans);
				if(val == null){
					val = 613.2;
				}
				return val;
				}catch(Exception e){
					return null;
				}
			}

}
