package content;

import ravensproject.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RavensFigObj {
	
	//Data fields
	private String name;
	private HashMap<String, ContentObj>contents;

	//Default constructor, initialized to null
	public RavensFigObj(){
		this.name = null;
		this.contents = null;
	}
	
	//Given a RavensFigure, constructs a RavensFigObj
	public RavensFigObj(RavensFigure fig){
		this.name = fig.getName();
		this.contents = new HashMap<String,ContentObj>();
		HashMap<String,RavensObject>map = fig.getObjects();
		for(RavensObject ro:map.values()){
			ContentObj cont = new ContentObj(ro);
			this.contents.put(cont.getName(), cont);
		}
	}
	
	//Given a name and a HashMap of contentobject, creates a RavensFigObj
	public RavensFigObj(String name, HashMap<String,ContentObj>map){
		this.name = name;
		this.contents = map;
	}
	
	//Necessary accessor methods
	public String getName() {
		return name;
	}

	public HashMap<String,ContentObj> getContents() {
		return contents;
	}
	
	//Determines if two Figures are equal enough for answer
	public boolean answerPair(RavensFigObj b){
		boolean correct = false;
		ArrayList<ContentObj>contAns = new ArrayList<ContentObj>(this.contents.values());
		ArrayList<ContentObj>contB = new ArrayList<ContentObj>(b.contents.values());
		boolean[]matched = new boolean[contAns.size()];
		for(int i = 0; i<matched.length; i++){
			matched[i]=false;
		}
		if(contAns.size()!=contB.size()){
			correct = false;
			return correct;
		}
		

		boolean disregardAngle = false;
		for(ContentObj c:contAns){
			//If no reflection was applied to the problem, then disregard angle measurement
		if(c.getAngle().equals(613.2)){
				disregardAngle = true;
				break;
			}
		}
		for(int ans = 0; ans<contAns.size(); ans++){
			
			for(int j = 0; j<contB.size(); j++){
				if(contAns.get(ans).getAngle().equals(613.0)&&contB.get(j).getAngle().equals(0.0)||
						contAns.get(ans).getAngle().equals(0.0)&&contB.get(j).getAngle().equals(613.0)){
					contAns.get(ans).setAngle(0.0);
					contB.get(j).setAngle(0.0);
				}
				if(disregardAngle||contAns.get(ans).getAngle().equals(contB.get(j).getAngle()))
				if(contAns.get(ans).getShape().equals(contB.get(j).getShape())&&
						contAns.get(ans).getSize().equals(contB.get(j).getSize())&&
						contAns.get(ans).getFill().equals(contB.get(j).getFill())&&
						contAns.get(ans).getAlignment().equals(contB.get(j).getAlignment())){
					matched[ans]=true;
				}
			}
		}
		boolean allMatched = true;
		for(int i = 0; i<matched.length; i++){
			if(matched[i]==false){
				allMatched=false;
				break;
			}
		}
		correct = allMatched;
		return correct;
		}
		
	}
