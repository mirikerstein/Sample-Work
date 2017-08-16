package transformationTables;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class TriangleTarget extends HashMap <AngleTransSet, Double>{
	private ArrayList<AngleTransSet>sets;
	
	public TriangleTarget(){
		this.sets = new ArrayList<AngleTransSet>();
		fillSets();
		this.put(this.sets.get(0), 0.0);
		this.put(this.sets.get(1), 360.0);
		this.put(this.sets.get(2), 0.0);
		this.put(this.sets.get(3), 0.0);
		this.put(this.sets.get(4), 180.0);
		this.put(this.sets.get(5), 270.0);
		this.put(this.sets.get(6), 90.0);
		this.put(this.sets.get(7), 315.0);
		this.put(this.sets.get(8), 45.0);
		this.put(this.sets.get(9), 135.0);
		this.put(this.sets.get(10), 225.0);
		this.put(this.sets.get(11), 0.0);
		this.put(this.sets.get(12), 180.0);
		this.put(this.sets.get(13), 0.0);
		this.put(this.sets.get(14), 180.0);
		this.put(this.sets.get(15), 360.0);
		this.put(this.sets.get(16), 90.0);
		this.put(this.sets.get(17), 270.0);
		this.put(this.sets.get(18), 135.0);
		this.put(this.sets.get(19), 45.0);
		this.put(this.sets.get(20), 315.0);
		this.put(this.sets.get(21), 225.0);
	}
	
	//Given the start and the transformation, return the target
			public Double getTriangleTar(Double a, String b){
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
			this.sets.add(new AngleTransSet(0,"horizontal"));
			this.sets.add(new AngleTransSet(0,"horizontal"));
			this.sets.add(new AngleTransSet(360,"horizontal"));
			this.sets.add(new AngleTransSet(360,"horizontal"));
			this.sets.add(new AngleTransSet(180,"horizontal"));
			this.sets.add(new AngleTransSet(90,"horizontal"));
			this.sets.add(new AngleTransSet(270,"horizontal"));
			this.sets.add(new AngleTransSet(45,"horizontal"));
			this.sets.add(new AngleTransSet(315,"horizontal"));
			this.sets.add(new AngleTransSet(225,"horizontal"));
			this.sets.add(new AngleTransSet(135,"horizontal"));//horizontal
			this.sets.add(new AngleTransSet(90,"vertical"));//vertical
			this.sets.add(new AngleTransSet(0,"vertical"));
			this.sets.add(new AngleTransSet(180,"vertical"));
			this.sets.add(new AngleTransSet(360,"vertical"));
			this.sets.add(new AngleTransSet(180,"vertical"));
			this.sets.add(new AngleTransSet(90,"vertical"));
			this.sets.add(new AngleTransSet(270,"vertical"));
			this.sets.add(new AngleTransSet(45,"vertical"));
			this.sets.add(new AngleTransSet(135,"vertical"));
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
