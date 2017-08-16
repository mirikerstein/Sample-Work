package ravensproject.content;

import java.util.HashMap;
import ravensproject.setC.*;
import ravensproject.*;

public class RavensProbObj {

	//Data fields
	private String name;
	private String type;
	private HashMap<String,RavensFigObj>figs;
	private Scorer scorer;
	private ScorerC scorerC;
	
	//Default constructor
	public RavensProbObj(){
		this.name = null;
		this.type = null;
		this.figs = null;
		this.scorer = null;
		this.scorerC = null;
	}
	
	//Given a RavensProblem, creates a RavensProbObj
	public RavensProbObj(RavensProblem rp){
		this.name = rp.getName();
		this.type = rp.getProblemType();
		this.figs = new HashMap<String, RavensFigObj>();
		HashMap<String, RavensFigure>map = rp.getFigures();
		for(RavensFigure rf:map.values()){
			RavensFigObj obj = new RavensFigObj(rf);
			this.figs.put(obj.getName(), obj);
		}
		this.scorer = new Scorer();
		this.scorerC = new ScorerC();
	}
	
	//Correlates the figures within the individual images of a problem
	public void correlateTwoByTwo(){
		this.scorer.correlateAWithB(this);
		this.scorer.correlateAWithC(this);
	}
	
	public void correlateSetC(){
		this.scorerC.correlateAWithC(this);
		this.scorerC.correlateAWithG(this);
	}
	
	//Get the figures
	public HashMap<String,RavensFigObj>getFigs(){
		return this.figs;
	}
	
	//toString
	public String toString(){
		StringBuilder print = new StringBuilder("Content Obj:");
		print.append("\n");
		for(RavensFigObj rf:this.figs.values()){
			if(rf.getName().equals("A")||rf.getName().equals("C")||rf.getName().equals("G")){
			for(ContentObj c: rf.getContents().values()){
				print.append(c.aliasSetToString());
			}
		}
		}
		return print.toString();
	}
}
