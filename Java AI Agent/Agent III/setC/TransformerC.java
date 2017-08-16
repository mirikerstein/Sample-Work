package ravensproject.setC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ravensproject.RavensProblem;
import ravensproject.ravensEnums.*;
import ravensproject.content.*;
import ravensproject.transformationTables.*;

public class TransformerC {

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
	private String[] newNames = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
			"j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
			"w", "x", "y", "z" };
	private int newNamesPos;

	public TransformerC() {
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
			RavensProbObj prob, String figA, String figB) {
		ArrayList<ContentObj> contA = new ArrayList<ContentObj>(prob.getFigs()
				.get(figA).getContents().values());
		ArrayList<ContentObj> contB = new ArrayList<ContentObj>(prob.getFigs()
				.get(figB).getContents().values());

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
			t.setUnchanged(false);
			t.setAlias(c.getAlias());
			t.setaPointer(c);
			t.setbPointer(null);
			allTransObj.put(c.getAlias(), t);
		}

		// Any unaliased obj from B was added. So create a transobj, set added
		// true, and add to map
		for (ContentObj c : unaliasedB) {
			TransformationObj t = new TransformationObj();
			t.setUnchanged(false);
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

		// Width
		String widthA = a.getWidth().name();
		String widthB = b.getWidth().name();
		trans.setWidthTrans(this.sizeTrans.getSizeTrans(widthA, widthB));

		// Height
		String heightA = a.getHeight().name();
		String heightB = b.getHeight().name();
		trans.setHeightTrans(this.sizeTrans.getSizeTrans(heightA, heightB));

		// Fill
		String fillA = a.getFill().name();
		String fillB = b.getFill().name();
		trans.setFillTrans(this.fillTrans.getFillTrans(fillA, fillB));

		// Alignment
		String alA = a.getAlignment().name();
		String alB = b.getAlignment().name();
		trans.setAlignmentTrans(this.alignmentTrans.getAlignmentTrans(alA, alB));

		// Shape Reflections

		Double refA = a.getAngle();
		Double refB = b.getAngle();

		if (a.getShape().equals(Shape.PACMAN)) {
			trans.setReflectionTrans(this.pacmanTrans.getPacmanReflectionTrans(
					refA, refB));
		} else if (a.getShape().equals(Shape.RIGHT_TRIANGLE)) {
			trans.setReflectionTrans(this.rightTriTrans.getRightTriangleTrans(
					refA, refB));
		} else if (a.getShape().equals(Shape.HEART)) {
			trans.setReflectionTrans(this.heartTrans.getHeartTrans(refA, refB));
		} else if (a.getShape().equals(Shape.TRIANGLE)) {
			trans.setReflectionTrans(this.triangleTrans.getTriangleTrans(refA,
					refB));
		} else {
			trans.setReflectionTrans("unchanged");
		}

		// Angle
		double angA = a.getAngle();
		double angB = b.getAngle();
		double angChange = angB - angA;
		trans.setAngleTrans(angChange);

		// left of
		int leftA = a.getLeft().size();
		int leftB = b.getLeft().size();
		int leftChange = leftB - leftA;
		trans.setLeftTrans(leftChange);

		// Above
		int aboveA = a.getAbove().size();
		int aboveB = b.getAbove().size();
		int aboveChange = aboveB - aboveA;
		trans.setAboveTrans(aboveChange);

		// Pointers
		trans.setaPointer(a);
		trans.setbPointer(b);

		if (trans.getShapeTrans().equalsIgnoreCase("unchanged")
				&& trans.getFillTrans().equalsIgnoreCase("unchanged")
				&& trans.getSizeTrans().equalsIgnoreCase("unchanged")
				&&
				// trans.getAlignmentTrans().equalsIgnoreCase("unchanged")&&
				trans.getReflectionTrans().equalsIgnoreCase("unchanged")
				&& trans.getAngleTrans() == 0.0 && trans.getLeftTrans() == 0
				&& trans.getAboveTrans() == 0) {
			trans.setUnchanged(true);
		}

		return trans;
	}

	// Checks if all the transObjs for an analogy are unchanged
	private boolean allUnchanged(HashMap<String, TransformationObj> trans) {
		boolean all = true;
		for (TransformationObj t : trans.values()) {
			if (t.getUnchanged() == false) {
				all = false;
			}
		}
		return all;
	}

	// Given a RavensProblemObj and a TransformationObj list, produces a
	// RavensFigureObj that applies the transformation to C
	public RavensFigObj applyTransformation(RavensProbObj prob,
			HashMap<String, TransformationObj> trans, String figuA,
			String figuB, String figuC) {
		RavensFigObj figA = prob.getFigs().get(figuA);
		ArrayList<ContentObj> contA = new ArrayList<ContentObj>(figA
				.getContents().values());
		RavensFigObj figB = prob.getFigs().get(figuB);
		ArrayList<ContentObj> contB = new ArrayList<ContentObj>(figB
				.getContents().values());
		RavensFigObj figC = prob.getFigs().get(figuC);
		ArrayList<ContentObj> contC = new ArrayList<ContentObj>(figC
				.getContents().values());

		// Hashmap for all contentobjs
		HashMap<String, ContentObj> contentForList = new HashMap<String, ContentObj>();

		// If all of the transObjs are unchanged, then the analogy is
		// "unchanged"
		// And the RavensFigObj to return is an exact copy of C.
		if (allUnchanged(trans)) {
			RavensFigObj copyFig = figC;
			return copyFig;
		}

		// If there is an alias that appears in C and neither A nor B, then it
		// must be transferred directly to
		// D. So, create list of aliases, find any that don't exist in C, and
		// create a ContentObj for that.

		ArrayList<String> allAliases = new ArrayList<String>();
		for (ContentObj c : contA) {
			allAliases.add(c.getAlias());
		}
		for (ContentObj c : contB) {
			allAliases.add(c.getAlias());
		}

		for (ContentObj c : contC) {
			boolean found = false;
			for (String al : allAliases) {
				if (c.getAlias().equals(al)) {
					found = true;
				}
			}
			if (!found) {
				ContentObj forList = c;
				contentForList.put(newNames[newNamesPos], forList);
				newNamesPos++;
			}
		}

		for (TransformationObj t : trans.values()) {
			// First, deal with pointers to A and no pointers to B.
			// If a transObj points to A and not to B, that means it was
			// deleted.
			// Therefore, do not create a contentobj for it in "D".
			if (t.getbPointer() == (null)) {

			}

			// If a transObj points to B and not A, that means it was added.
			// Create a contentObj for it that mimics B
			if (t.getaPointer() == (null)) {
				ContentObj co = t.getbPointer();
				ContentObj no = new ContentObj();
				no.setName(newNames[newNamesPos]);
				newNamesPos++;
				no.setAbove(co.getAbove());
				no.setInside(co.getInside());
				no.setAbove(co.getAbove());
				no.setLeft(co.getLeft());
				no.setAlias(co.getAlias());
				no.setAlignment(co.getAlignment());
				no.setAngle(co.getAngle());
				no.setFill(co.getFill());
				no.setShape(co.getShape());
				no.setSize(co.getSize());
				contentForList.put(no.getName(), no);
			}

			// If both pointers are there, then this is an aliased
			// transformation.
			// Apply the transformation to C, with the corresponding alias.
			if (t.getaPointer() != (null) && t.getbPointer() != (null)) {
				ContentObj list = new ContentObj();
				ContentObj con = new ContentObj();
				for (ContentObj c : contC) {
					if (c.getAlias().equals(t.getAlias())) {
						con = c;
						// Name
						list.setName(newNames[newNamesPos]);
						newNamesPos++;

						// Alias
						list.setAlias(con.getAlias());

						// Shape
						String shapeChange = t.getShapeTrans();
						if (shapeChange.equalsIgnoreCase("unchanged")) {
							list.setShape(con.getShape());
						} else {
							String ch = this.shapeTrans
									.getShapeTar(shapeChange);
							list.setShape(Shape.valueOf(ch));
						}

						// Size
						String sizeTrans = t.getSizeTrans();
						String sizeStart = con.getSize().name();
						String tarSize = this.sizeTar.getSizeTar(sizeStart,
								sizeTrans);
						list.setSize(Size.valueOf(tarSize));

						// Width
						String widthTrans = t.getWidthTrans();
						String widthStart = con.getWidth().name();
						String tarWidth = this.sizeTar.getSizeTar(widthStart,
								widthTrans);
						list.setWidth(Size.valueOf(tarWidth));

						// Height
						String heightTrans = t.getWidthTrans();
						String heightStart = con.getWidth().name();
						String tarHeight = this.sizeTar.getSizeTar(heightStart,
								heightTrans);
						list.setHeight(Size.valueOf(tarHeight));

						// If either width or height is UNSPECIFIED, then shape
						// is a square...
						if (list.getWidth().equals(Size.UNSPECIFIED)
								&& !list.getHeight().equals(Size.UNSPECIFIED)) {
							list.setWidth(list.getHeight());
						}
						if (list.getHeight().equals(Size.UNSPECIFIED)
								&& !list.getWidth().equals(Size.UNSPECIFIED)) {
							list.setHeight(list.getWidth());
						}

						// Fill
						String fillTrans = t.getFillTrans();
						String fillStart = con.getFill().name();
						String tarFill = this.fillTar.getFillTar(fillStart,
								fillTrans);
						list.setFill(Fill.valueOf(tarFill));

						// Alignment
						String alTrans = t.getAlignmentTrans();
						String alStart = con.getAlignment().name();
						String tarAl = this.alignmentTar.getAlignmentTar(
								alStart, alTrans);
						if (tarAl == null) {
							tarAl = "UNSPECIFIED";
						}
						list.setAlignment(Alignment.valueOf(tarAl));

						// Angle
						if (con.getAngle() < 613) {
							Double angChange = t.getAngleTrans();
							Double angTarget = con.getAngle() + angChange;
							if (angTarget > 360) {
								angTarget = angTarget - 360;
							}
							list.setAngle(angTarget);
						}

						// Inside - MUST EDIT
						list.setInside(con.getInside());

						// Above - MUST EDIT
						int numAbove = t.getAboveTrans()
								+ con.getAbove().size();
						for (int i = 0; i < numAbove; i++) {
							list.getAbove().add("a");
						}

						// LEFT
						int numLeft = t.getLeftTrans() + con.getLeft().size();
						for (int i = 0; i < numLeft; i++) {
							list.getLeft().add("a");
						}

						if (list.getWidth().equals(list.getHeight())) {
							list.setSize(list.getWidth());
						}

						contentForList.put(list.getName(), list);

					}
				}

			}// end if
		}// end for

		// Create the RavensFigure
		RavensFigObj answer = new RavensFigObj("ANSWER", contentForList);
		return answer;
	}// end method

	// All processing for solving a problem in set C
	public int processCSet(RavensProblem problem) {
		int answer = -1;
		boolean disregard = false;

		// First, create a RavensProbObj out of rp.
		RavensProbObj rp = new RavensProbObj(problem);

		// Create the list of answers
		ArrayList<RavensFigObj> answers = new ArrayList<RavensFigObj>();
		answers.add(rp.getFigs().get("1"));
		answers.add(rp.getFigs().get("2"));
		answers.add(rp.getFigs().get("3"));
		answers.add(rp.getFigs().get("4"));
		answers.add(rp.getFigs().get("5"));
		answers.add(rp.getFigs().get("6"));
		answers.add(rp.getFigs().get("7"));
		answers.add(rp.getFigs().get("8"));

		// Alias the figures
		rp.correlateSetC();

		// Produce a RavensFigObj for the analogy between A and C
		HashMap<String, TransformationObj> transA = this
				.createTransformationList(rp, "A", "C");
		for (TransformationObj t : transA.values()) {
		}
		RavensFigObj analogyA = this.applyTransformation(rp, transA, "A", "C",
				"G");

		// Produce a RavensFigObj for the analogy between A and G
		HashMap<String, TransformationObj> transB = this
				.createTransformationList(rp, "A", "G");
		RavensFigObj analogyB = this.applyTransformation(rp, transB, "A", "G",
				"C");

		// Find the value for the disregardPos flag for the answer
		for (TransformationObj to : transA.values()) {
			if (to.getaPointer() == null) {
				disregard = true;
			}
		}
		for (TransformationObj to : transB.values()) {
			if (to.getaPointer() == null) {
				disregard = true;
			}
		}

		// Check if the two RavensFigObjs match
		int confidence = checkConfidence(analogyA, analogyB, disregard);

		// If confident that the generated RavensFigObj is correct, find its
		// match in the suggested answers.
		if (confidence == 2) {

			for (int i = 0; i < 8; i++) {
				if (analogyA.answerPair(answers.get(i), disregard)) {
					answer = Integer.valueOf(answers.get(i).getName());
				}
			}
		}

		// If still haven't found an answer, consider if it is a special
		// 'identity case'
		if (answer == -1
				&& rp.getFigs().get("A").getContents().values().size() == 1) {
			IdentityTransformations id = new IdentityTransformations(rp);
			answer = id.applyIdentityTransformation();
		}

		// If still haven't found an answer, consider if it is a special rule
		// case
		/*
		 * Special Rule: if a horizontal and vertical analogy both determine
		 * that the same figures be added, add them only once and change the
		 * fill of those figures
		 */

		if (answer == -1) {
			// If the A to C analogy...
			if (sameTrans(transA, transB, analogyA, analogyB)) {
				for (ContentObj c : rp.getFigs().get("C").getContents()
						.values()) {
					if (c.getFill().equals(Fill.YES)) {
						c.setFill(Fill.NO);
					} else if (c.getFill().equals(Fill.NO)) {
						c.setFill(Fill.YES);
					}
				}
				RavensFigObj specialRuleAnswer = rp.getFigs().get("C");

				for (int i = 0; i < 8; i++) {
					if (specialRuleAnswer.specialRuleAnswerPair(
							specialRuleAnswer, answers.get(i))) {
						answer = Integer.valueOf(answers.get(i).getName());
					}
				}
			}
		}

		// Return the answer
		return answer;
	}

	// Returns an int value representing the Agent's confidence in its generated
	// response
	private int checkConfidence(RavensFigObj a, RavensFigObj b, boolean dis) {
		int confidence = 0;

		boolean answer = a.answerPair(b, dis);
		if (answer) {
			confidence = 2;
		}

		return confidence;
	}

	private boolean sameTrans(HashMap<String, TransformationObj> transA,
			HashMap<String, TransformationObj> transB, RavensFigObj analogyA,
			RavensFigObj analogyB) {

		boolean same = true;

		ArrayList<TransformationObj> listA = new ArrayList<TransformationObj>(
				transA.values());
		ArrayList<TransformationObj> listB = new ArrayList<TransformationObj>(
				transB.values());
		ArrayList<ContentObj> contA = new ArrayList<ContentObj>(analogyA
				.getContents().values());
		ArrayList<ContentObj> contB = new ArrayList<ContentObj>(analogyB
				.getContents().values());

		for (int i = 0; i < listA.size(); i++) {
			TransformationObj tA = listA.get(i);
			TransformationObj tB = listB.get(i);

			if (tA.getaPointer() == null && tB.getaPointer() == null
					&& contA.get(i).getShape().equals(contB.get(i).getShape())
					&& contA.get(i).getSize().equals(contB.get(i).getSize())
					&& contA.get(i).getFill().equals(contB.get(i).getFill())) {
			} else {
				same = false;
			}
		}
		return same;
	}
}
