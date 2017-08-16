package ravensproject.transformationTables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ravensproject.ravensEnums.*;
import ravensproject.content.*;

public class Transformer {

	// Fields
	private AlignmentTarget alignmentTar;
	private AlignmentTransformation alignmentTrans;
	private FillTarget fillTar;
	private FillTransformation fillTrans;
	private PacmanReflectionTarget pacmanTar;
	private PacmanReflectionTransformation pacmanTrans;
	private TriangleTransformation triangleTrans;
	private TriangleTarget triangleTar;
	private HeartTransformation heartTrans;
	private HeartTarget heartTar;
	private RightTriangleTarget rightTriTar;
	private RightTriangleTransformation rightTriTrans;
	private ShapeTransformations shapeTrans;
	private SizeTarget sizeTar;
	private SizeTransformation sizeTrans;
	private String[] newNames={"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	private int newNamesPos;
	
	public Transformer() {
		this.newNamesPos = 0;
		this.alignmentTar = new AlignmentTarget();
		this.alignmentTrans = new AlignmentTransformation();
		this.triangleTrans = new TriangleTransformation();
		this.triangleTar = new TriangleTarget();
		this.heartTrans = new HeartTransformation();
		this.heartTar = new HeartTarget();
		this.fillTar = new FillTarget();
		this.fillTrans = new FillTransformation();
		this.pacmanTar = new PacmanReflectionTarget();
		this.pacmanTrans = new PacmanReflectionTransformation();
		this.rightTriTar = new RightTriangleTarget();
		this.rightTriTrans = new RightTriangleTransformation();
		this.shapeTrans = new ShapeTransformations();
		this.sizeTar = new SizeTarget();
		this.sizeTrans = new SizeTransformation();
	}

	// Receives a RavensProbObj, and creates the TransformationObjs for the
	// problem
	public HashMap<String, TransformationObj> createTransformationList(
			RavensProbObj prob) {
		ArrayList<ContentObj> contA = new ArrayList<ContentObj>(prob.getFigs()
				.get("A").getContents().values());
		ArrayList<ContentObj> contB = new ArrayList<ContentObj>(prob.getFigs()
				.get("B").getContents().values());

		// Pair corresponding aliased ContentObjs in a hash map
		HashMap<ContentObj, ContentObj> aliasPairs = new HashMap<ContentObj, ContentObj>();
		for (int a = 0; a < contA.size(); a++) {
			for (int b = 0; b < contB.size(); b++) {
				if (contA.get(a).getAlias().equals(contB.get(b).getAlias())) {
					aliasPairs.put(contA.get(a), contB.get(b));
				}
			}
		}

		// List any unaliased content in A
		ArrayList<ContentObj> unaliasedA = new ArrayList<ContentObj>();
		for (ContentObj c : contA) {
			if (!aliasPairs.containsKey(c)) {
				unaliasedA.add(c);
			}
		}

		// List any unaliased content in B
		ArrayList<ContentObj> unaliasedB = new ArrayList<ContentObj>();
		for (ContentObj c : contB) {
			if (!aliasPairs.containsValue(c)) {
				unaliasedB.add(c);
			}
		}

		// Now have three sets of ContentObjs to deal with:
		// 1. all aliased pairs
		// 2. all unaliased obj from A
		// 3. all unaliased obj from B

		HashMap<String, TransformationObj> allTransObj = new HashMap<String, TransformationObj>();

		// Iterate through aliased pairs, create their transformationObj and add
		// to map
		Iterator it = aliasPairs.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<ContentObj, ContentObj> pair = (Map.Entry<ContentObj, ContentObj>) it
					.next();
			ContentObj cA = pair.getKey();
			
			ContentObj cB = pair.getValue();
			TransformationObj to = createTransObj(cA, cB);
			allTransObj.put(cA.getAlias(), to);
		}

		// Any unaliased obj from A was deleted. So create a transobj, set
		// deleted true, and add to map
		for (ContentObj c : unaliasedA) {
			TransformationObj t = new TransformationObj();
			t.setAlias(c.getAlias());
			t.setaPointer(c);
			t.setbPointer(null);
			allTransObj.put(c.getAlias(), t);
		}

		// Any unaliased obj from B was added. So create a transobj, set added
		// true, and add to map
		for (ContentObj c : unaliasedB) {
			TransformationObj t = new TransformationObj();
			t.setAlias(c.getAlias());
			t.setaPointer(null);
			t.setbPointer(c);
			allTransObj.put(c.getAlias(), t);
		}

		return allTransObj;
	}

	// Given two ContentObjs, create a TransformationObj
	public TransformationObj createTransObj(ContentObj a, ContentObj b) {
		TransformationObj trans = new TransformationObj();
		// Alias
		trans.setAlias(a.getAlias());

		// Shape
		String shapeA = a.getShape().name();
		String shapeB = b.getShape().name();
		if (!shapeA.equals(shapeB)) {
			trans.setShapeTrans(this.shapeTrans.getShapeTrans(shapeB));
		} else {
			trans.setShapeTrans("unchanged");
		}

		// Size
		String sizeA = a.getSize().name();
		String sizeB = b.getSize().name();
		trans.setSizeTrans(this.sizeTrans.getSizeTrans(sizeA, sizeB));

		// Fill
		String fillA = a.getFill().name();
		String fillB = b.getFill().name();
		trans.setFillTrans(this.fillTrans.getFillTrans(fillA, fillB));

		// Alignment
		String alA = a.getAlignment().name();
		String alB = b.getAlignment().name();
		trans.setAlignmentTrans(this.alignmentTrans.getAlignmentTrans(alA, alB));

		// Shape Reflections
		
		Double refA =  a.getAngle();
		Double refB =  b.getAngle();

		if (a.getShape().equals(Shape.PACMAN)) {
		trans.setReflectionTrans(this.pacmanTrans.getPacmanReflectionTrans(refA, refB));
		} else if (a.getShape().equals(Shape.RIGHT_TRIANGLE)) {
			trans.setReflectionTrans(this.rightTriTrans.getRightTriangleTrans(
					refA, refB));
		} else if(a.getShape().equals(Shape.HEART)){
			trans.setReflectionTrans(this.heartTrans.getHeartTrans(refA, refB));
		}else if(a.getShape().equals(Shape.TRIANGLE)){
			trans.setReflectionTrans(this.triangleTrans.getTriangleTrans(refA, refB));
		}else{
				trans.setReflectionTrans("unchanged");
		}

		// Angle
		double angA = a.getAngle();
		double angB = b.getAngle();
		double angChange = angB - angA;
		trans.setAngleTrans(angChange);

		// Pointers
		trans.setaPointer(a);
		trans.setbPointer(b);

		return trans;
	}
	
	//If the RavensFig created by applyTransformation() does not match any answers, use this
		//method to apply an alternate transformation, if available.
	//Given a RavensProblemObj and a TransformationObj list, produces a RavensFigureObj that applies the transformation to C
	public RavensFigObj applyAlternateTransformation(RavensProbObj prob, HashMap<String,TransformationObj> trans){
		RavensFigObj figA = prob.getFigs().get("A");
		ArrayList<ContentObj> contA = new ArrayList<ContentObj>(figA.getContents().values());
		RavensFigObj figB = prob.getFigs().get("B");
		ArrayList<ContentObj> contB = new ArrayList<ContentObj>(figB.getContents().values());
		RavensFigObj figC = prob.getFigs().get("C");
		ArrayList<ContentObj> contC = new ArrayList<ContentObj>(figC.getContents().values());
		
		//Hashmap for all contentobjs
				HashMap<String, ContentObj>contentForList = new HashMap<String, ContentObj>();
		
		//If there is an alias that appears in C and neither A nor B, then it must be transferred directly to
		//D. So, create list of aliases, find any that don't exist in C, and create a ContentObj for that.
		
		ArrayList<String>allAliases = new ArrayList<String>();
		for(ContentObj c:contA){
			allAliases.add(c.getAlias());
		}
		for(ContentObj c:contB){
			allAliases.add(c.getAlias());
		}
		
		for(ContentObj c:contC){
			boolean found = false;
			for(String al:allAliases){
				if(c.getAlias().equals(al)){
					found = true;
				}
			}
			if(!found){
				ContentObj forList = c;
				contentForList.put(newNames[newNamesPos], forList);
				newNamesPos++;
			}
		}
		
		
		
		
		for(TransformationObj t:trans.values()){
			//First, deal with pointers to A and no pointers to B. 
			//If a transObj points to A and not to B, that means it was deleted.
			//Therefore, do not create a contentobj for it in "D".
			if(t.getbPointer()==(null)){
				
			}
			
			//If a transObj points to B and not A, that means it was added.
			//Create a contentObj for it that mimics B
			if(t.getaPointer()==(null)){
				ContentObj co = t.getbPointer();
				ContentObj no = new ContentObj();
				no.setName(newNames[newNamesPos]);
				newNamesPos++;
				no.setAbove(co.getAbove());
				no.setInside(co.getInside());
				no.setAlias(co.getAlias());
				no.setAlignment(co.getAlignment());
				no.setAngle(co.getAngle());
				no.setFill(co.getFill());
				no.setShape(co.getShape());
				no.setSize(co.getSize());
				contentForList.put(no.getName(), no);
			}
			
			//If both pointers are there, then this is an aliased transformation.
			//Apply the transformation to C, with the corresponding alias.
			if(t.getaPointer()!=(null)&&t.getbPointer()!=(null)){
				ContentObj list = new ContentObj();
				ContentObj con = new ContentObj();
				for(ContentObj c:contC){
					if(c.getAlias().equals(t.getAlias())){
						con = c;
						
						//Name
						list.setName(newNames[newNamesPos]);
						newNamesPos++;
						
						//Alias
						list.setAlias(con.getAlias());
						
						//Shape
						String shapeChange = t.getShapeTrans();
						if(shapeChange.equalsIgnoreCase("unchanged")){
							list.setShape(con.getShape());
						}else{
							String ch = this.shapeTrans.getShapeTar(shapeChange);
							list.setShape(Shape.valueOf(ch));
						}
						
						//Size
						String sizeTrans = t.getSizeTrans();
						String sizeStart = con.getSize().name();
						String tarSize = this.sizeTar.getSizeTar(sizeStart, sizeTrans);
						list.setSize(Size.valueOf(tarSize));
						
						//Fill
						String fillTrans = t.getFillTrans();
						String fillStart = con.getFill().name();
						String tarFill = this.fillTar.getFillTar(fillStart, fillTrans);
						list.setFill(Fill.valueOf(tarFill));
						
						//Alignment
						String alTrans = t.getAlignmentTrans();
						String alStart = con.getAlignment().name();
						String tarAl = this.alignmentTar.getAlignmentTar(alStart, alTrans);
						if(tarAl==null){
							tarAl = "UNSPECIFIED";
						}
						list.setAlignment(Alignment.valueOf(tarAl));
						
						//Angle
						Double angChange = t.getAngleTrans();
						Double angTarget = con.getAngle()+angChange;
						if(angTarget>360){
							angTarget = angTarget - 360;
						}
						list.setAngle(angTarget);
						
						//Inside - MUST EDIT
						list.setInside(con.getInside());
						
						//Above - MUST EDIT
						list.setAbove(con.getAbove());
						
						contentForList.put(list.getName(), list);
						
					}
				}
				
				
			}//end if
			}//end for
			
			//Create the RavensFigure
			RavensFigObj answer = new RavensFigObj("ANSWER", contentForList);
			return answer;
		}//end method
	
	
	//Given a RavensProblemObj and a TransformationObj list, produces a RavensFigureObj that applies the transformation to C
		public RavensFigObj applyTransformation(RavensProbObj prob, HashMap<String,TransformationObj> trans){
			RavensFigObj figA = prob.getFigs().get("A");
			ArrayList<ContentObj> contA = new ArrayList<ContentObj>(figA.getContents().values());
			RavensFigObj figB = prob.getFigs().get("B");
			ArrayList<ContentObj> contB = new ArrayList<ContentObj>(figB.getContents().values());
			RavensFigObj figC = prob.getFigs().get("C");
			ArrayList<ContentObj> contC = new ArrayList<ContentObj>(figC.getContents().values());
			
			//Hashmap for all contentobjs
					HashMap<String, ContentObj>contentForList = new HashMap<String, ContentObj>();
			
			//If there is an alias that appears in C and neither A nor B, then it must be transferred directly to
			//D. So, create list of aliases, find any that don't exist in C, and create a ContentObj for that.
			
			ArrayList<String>allAliases = new ArrayList<String>();
			for(ContentObj c:contA){
				allAliases.add(c.getAlias());
			}
			for(ContentObj c:contB){
				allAliases.add(c.getAlias());
			}
			
			for(ContentObj c:contC){
				boolean found = false;
				for(String al:allAliases){
					if(c.getAlias().equals(al)){
						found = true;
					}
				}
				if(!found){
					ContentObj forList = c;
					contentForList.put(newNames[newNamesPos], forList);
					newNamesPos++;
				}
			}
			
			
			
			
			for(TransformationObj t:trans.values()){
				//First, deal with pointers to A and no pointers to B. 
				//If a transObj points to A and not to B, that means it was deleted.
				//Therefore, do not create a contentobj for it in "D".
				if(t.getbPointer()==(null)){
					
				}
				
				//If a transObj points to B and not A, that means it was added.
				//Create a contentObj for it that mimics B
				if(t.getaPointer()==(null)){
					ContentObj co = t.getbPointer();
					ContentObj no = new ContentObj();
					no.setName(newNames[newNamesPos]);
					newNamesPos++;
					no.setAbove(co.getAbove());
					no.setInside(co.getInside());
					no.setAlias(co.getAlias());
					no.setAlignment(co.getAlignment());
					no.setAngle(co.getAngle());
					no.setFill(co.getFill());
					no.setShape(co.getShape());
					no.setSize(co.getSize());
					contentForList.put(no.getName(), no);
				}
				
				//If both pointers are there, then this is an aliased transformation.
				//Apply the transformation to C, with the corresponding alias.
				if(t.getaPointer()!=(null)&&t.getbPointer()!=(null)){
					ContentObj list = new ContentObj();
					ContentObj con = new ContentObj();
					for(ContentObj c:contC){
						if(c.getAlias().equals(t.getAlias())){
							con = c;
							
							//Name
							list.setName(newNames[newNamesPos]);
							newNamesPos++;
							
							//Alias
							list.setAlias(con.getAlias());
							
							//Shape
							String shapeChange = t.getShapeTrans();
							if(shapeChange.equalsIgnoreCase("unchanged")){
								list.setShape(con.getShape());
							}else{
								String ch = this.shapeTrans.getShapeTar(shapeChange);
								list.setShape(Shape.valueOf(ch));
							}
							
							//Size
							String sizeTrans = t.getSizeTrans();
							String sizeStart = con.getSize().name();
							String tarSize = this.sizeTar.getSizeTar(sizeStart, sizeTrans);
							list.setSize(Size.valueOf(tarSize));
							
							//Fill
							String fillTrans = t.getFillTrans();
							String fillStart = con.getFill().name();
							String tarFill = this.fillTar.getFillTar(fillStart, fillTrans);
							list.setFill(Fill.valueOf(tarFill));
							
							//Alignment
							String alTrans = t.getAlignmentTrans();
							String alStart = con.getAlignment().name();
							String tarAl = this.alignmentTar.getAlignmentTar(alStart, alTrans);
							if(tarAl==null){
								tarAl = "UNSPECIFIED";
							}
							list.setAlignment(Alignment.valueOf(tarAl));
							
							//Angle
							String refTrans = t.getReflectionTrans();
							Double refStart = con.getAngle();
							Double refTarget = null;
							if(con.getShape().equals(Shape.PACMAN)){
								refTarget = this.pacmanTar.getPacmanReflectionTar(refStart, refTrans);
							}else if(con.getShape().equals(Shape.RIGHT_TRIANGLE)){
								refTarget = this.rightTriTar.getRightTriangleTar(refStart, refTrans);
							}else{
							refTarget = 613.0;
							}
				
							list.setAngle(refTarget);
							
							//Inside - MUST EDIT
							list.setInside(con.getInside());
							
							//Above - MUST EDIT
							list.setAbove(con.getAbove());
							
							contentForList.put(list.getName(), list);
							
						}
					}
					
					
				}//end if
				}//end for
				
				//Create the RavensFigure
				RavensFigObj answer = new RavensFigObj("ANSWER", contentForList);
				return answer;
			}//end method

		
	}


