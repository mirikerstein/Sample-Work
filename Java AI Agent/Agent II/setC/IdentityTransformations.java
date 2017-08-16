package ravensproject.setC;

import java.util.ArrayList;

import ravensproject.content.ContentObj;
import ravensproject.content.RavensFigObj;
import ravensproject.content.RavensProbObj;
import ravensproject.ravensEnums.Fill;
import ravensproject.ravensEnums.Shape;
import ravensproject.ravensEnums.Size;

/*This class deals with special case 'identity transformations', in
 * which the entire image is treated as one identity, and not as individual figures. 
 */

public class IdentityTransformations {

	//Fields
	private int multHorAnalogy;
	private int multVertAnalogy;
	private boolean overlap;
	RavensFigObj figA;
	RavensFigObj figC;
	RavensFigObj figG;
	private Shape identityShape;
	private Size identitySize;
	private Fill identityFill;
	private RavensProbObj prob;
	private int expectedHorQty;
	private int expectedVertQty;
	
	//Given a RavensProbObj, creates the transformation obj
	public IdentityTransformations(RavensProbObj rp){
		this.prob = rp;
		
		//Set the RavensFigObjs that will be used
		this.figA = rp.getFigs().get("A");
		this.figC = rp.getFigs().get("C");
		this.figG = rp.getFigs().get("G");
		
		//Set the IDENTITY fields
		this.identityShape = figA.getContents().get("a").getShape();
		this.identitySize = figA.getContents().get("a").getSize();
		this.identityFill = figA.getContents().get("a").getFill();
		
		//Set the overlap value to false. If it are true, it will be reset
		//when the multipliers are calculated.
		this.overlap = false;
		
		//Get the mult for the horizontal analogy
		this.multHorAnalogy = horMult();
		
		//Get the mult for the vert analogy
		this.multVertAnalogy = vertMult();
		
		//Expected quantities in answer
		this.expectedHorQty = figG.getContents().values().size()*this.multHorAnalogy;
		this.expectedVertQty = figC.getContents().values().size()*this.multVertAnalogy;
		
	}
	
	//Horizontal mult
	private int horMult(){
		int horMult = 0;
		for(ContentObj c:figC.getContents().values()){
			if(matchUp(c)){
				horMult++;
				if(c.getOverlap().size()>0){
					this.overlap = true;
				}
			}
		}
		return horMult;
	}
	
	//Vertical mult
	private int vertMult(){
		int vertMult = 0;
		for(ContentObj c:figG.getContents().values()){
			if(matchUp(c)){
				vertMult++;
				if(c.getOverlap().size()>0){
					this.overlap = true;
				}
			}
		}
		return vertMult;
	}
	
	//Determines if a shape matches the identity
	private boolean matchUp(ContentObj c){
		if(c.getShape().equals(this.identityShape)&&
				c.getSize().equals(this.identitySize)&&
				c.getFill().equals(this.identityFill)){
			return true;
		}else{
			return false;
		}
	}
	
	//Applies the transformation
	public int applyIdentityTransformation(){
		int answer = -1;
		
		//Create the list of all possible answers
		ArrayList<RavensFigObj> answerFigs = new ArrayList<RavensFigObj>();
		answerFigs.add(this.prob.getFigs().get("1"));
		answerFigs.add(this.prob.getFigs().get("2"));
		answerFigs.add(this.prob.getFigs().get("3"));
		answerFigs.add(this.prob.getFigs().get("4"));
		answerFigs.add(this.prob.getFigs().get("5"));
		answerFigs.add(this.prob.getFigs().get("6"));
		answerFigs.add(this.prob.getFigs().get("7"));
		answerFigs.add(this.prob.getFigs().get("8"));
		
		//For each answer, check if it...
		//1. matches the identity attributes
		//2. matches the overlap attribute
		//3. matches the expected quantity
		for(RavensFigObj fig:answerFigs){
			if(ansQty(fig)==this.expectedHorQty&&
					ansOverlap(fig)==this.overlap&&
					allMatch(fig)){
				answer = Integer.valueOf(fig.getName());
			}
		}
		return answer;
	}
	
	//Given a RavensFigOb, get the qty
	public int ansQty(RavensFigObj f){
		int total = 0;
		for(ContentObj c:f.getContents().values()){
			total++;
		}
		return total;
	}
	//Given a RavensFigOb, get overlap
	public boolean ansOverlap(RavensFigObj f){
		boolean aoverlap = false;
		for(ContentObj c:f.getContents().values()){
			if(c.getOverlap().size()>0){
				aoverlap = true;
			}
		}
		return aoverlap;
	}
	
	//Given a RavensFigObj, check if each content obj matches the identity attributes
	private boolean allMatch(RavensFigObj f){
		boolean all = true;
		for(ContentObj c:f.getContents().values()){
			if(!matchUp(c)){
				all = false;
			}
		}
		return all;
	}
	
}
