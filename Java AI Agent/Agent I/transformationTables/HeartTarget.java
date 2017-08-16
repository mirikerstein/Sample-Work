package transformationTables;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class HeartTarget extends HashMap <AngleTransSet, Double>{
	private ArrayList<AngleTransSet>sets;
	
	public HeartTarget(){
		this.sets = new ArrayList<AngleTransSet>();
		fillSets();
		this.put(this.sets.get(0), 315.0);
		this.put(this.sets.get(1), 45.0);
		this.put(this.sets.get(2), 270.0);
		this.put(this.sets.get(3), 90.0);
		this.put(this.sets.get(4), 0.0);
		this.put(this.sets.get(5), 360.0);
		this.put(this.sets.get(6), 360.0);
		this.put(this.sets.get(7), 0.0);
		this.put(this.sets.get(8), 225.0);
		this.put(this.sets.get(9), 135.0);
		this.put(this.sets.get(10), 180.0);
		this.put(this.sets.get(11), 180.0);
		this.put(this.sets.get(12), 0.0);
		this.put(this.sets.get(13), 180.0);
		this.put(this.sets.get(14), 360.0);
		this.put(this.sets.get(15), 135.0);
		this.put(this.sets.get(16), 45.0);
		this.put(this.sets.get(17), 90.0);
		this.put(this.sets.get(18), 270.0);
		this.put(this.sets.get(19), 315.0);
		this.put(this.sets.get(19), 225.0);
	}
	
	//Given the start and the transformation, return the target
			public Double getHeartTar(Double a, String b){
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
			
	//Fill the list of sets
		private void  fillSets(){
			this.sets.add(new AngleTransSet(45,"horizontal"));
			this.sets.add(new AngleTransSet(315,"horizontal"));
			this.sets.add(new AngleTransSet(90,"horizontal"));
			this.sets.add(new AngleTransSet(270,"horizontal"));
			this.sets.add(new AngleTransSet(0,"horizontal"));
			this.sets.add(new AngleTransSet(0,"horizontal"));
			this.sets.add(new AngleTransSet(360,"horizontal"));
			this.sets.add(new AngleTransSet(360,"horizontal"));
			this.sets.add(new AngleTransSet(135,"horizontal"));
			this.sets.add(new AngleTransSet(225,"horizontal"));
			this.sets.add(new AngleTransSet(180,"horizontal"));
			this.sets.add(new AngleTransSet(0,"vertical"));
			this.sets.add(new AngleTransSet(180,"vertical"));
			this.sets.add(new AngleTransSet(360,"vertical"));
			this.sets.add(new AngleTransSet(180,"vertical"));
			this.sets.add(new AngleTransSet(45,"vertical"));
			this.sets.add(new AngleTransSet(135,"vertical"));
			this.sets.add(new AngleTransSet(90,"vertical"));
			this.sets.add(new AngleTransSet(270,"vertical"));
			this.sets.add(new AngleTransSet(225,"vertical"));
			this.sets.add(new AngleTransSet(315,"vertical"));
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
		

}
