package ravensproject.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Scorer {

	// Data fields
	private HashMap<String, Integer> weight;
	private String[] aliasNames;
	private int aliasPos;
	private String[] unaliasedNames;
	private int unaliasedPos;

	// Constructor
	public Scorer() {
		this.weight = new HashMap<String, Integer>();
		this.weight.put("shape", 1);
		this.weight.put("size", 1);
		this.weight.put("fill", 1);
		this.weight.put("alignment", 1);
		this.weight.put("inside", 1);
		this.weight.put("above", 1);
		this.weight.put("angle", 1);

		String aNames = "h i j k l m n o p q r s t u v w x y z";
		this.aliasNames = aNames.split(" ");

		this.aliasPos = 0;

		String unNames = "a,b,c,d,e,f,g,h,i,j";
		this.unaliasedNames = unNames.split(",");
		this.unaliasedPos = 0;
	}

	// Given a RavensProbObj, correlates the ContentObjs in the individual
	// figures
	// When content objects are correlated, their aliases are reset
	public void correlateAWithB(RavensProbObj rp) {
		this.aliasPos = 0;
		this.unaliasedPos = 0;
		HashMap<String, RavensFigObj> map = rp.getFigs();
		RavensFigObj figA = map.get("A");
		RavensFigObj figB = map.get("B");

		List<ContentObj> contA = new ArrayList<ContentObj>(figA.getContents()
				.values());
		List<ContentObj> contB = new ArrayList<ContentObj>(figB.getContents()
				.values());

		int totalCombos = contA.size() * contB.size();
		ContentObj[][] allPairs = new ContentObj[totalCombos][2];
		int[] scores = new int[totalCombos];
		int numInserted = 0;
		for (int i = 0; i < contA.size(); i++) {
			for (int j = 0; j < contB.size(); j++) {
				allPairs[numInserted][0] = contA.get(i);
				allPairs[numInserted][1] = contB.get(j);
				numInserted++;
			}
		}

		for (int i = 0; i < totalCombos; i++) {
			scores[i] = scoreTwoContentObj(allPairs[i][0], allPairs[i][1]);
		}

		for (int i = 0; i < totalCombos; i++) {
			int greatestScore = 0;
			int greatestScorePos = 0;
			for (int j = 0; j < totalCombos; j++) {
				if (scores[j] > greatestScore) {
					greatestScore = scores[j];
					greatestScorePos = j;
					allPairs[greatestScorePos][0].setAlias(aliasNames[aliasPos]);
					allPairs[greatestScorePos][1].setAlias(aliasNames[aliasPos]);
					this.aliasPos++;
				}
			}
			
			for (int k = 0; k < totalCombos; k++) {
				if (allPairs[k][0].equals(allPairs[greatestScorePos][0])
						|| allPairs[k][0].equals(allPairs[greatestScorePos][1])
						|| allPairs[k][1].equals(allPairs[greatestScorePos][0])
						|| allPairs[k][1].equals(allPairs[greatestScorePos][1])) {
					scores[k] = -1;
				}
			}
		}

		for (ContentObj ct : contA) {
			if (ct.getAlias().equalsIgnoreCase("unspecified")) {
				ct.setAlias(unaliasedNames[unaliasedPos]);
				unaliasedPos++;
			}
		}
		for (ContentObj ct : contB) {
			if (ct.getAlias().equalsIgnoreCase("unspecified")) {
				ct.setAlias(unaliasedNames[unaliasedPos]);
				unaliasedPos++;
			}
		}

	}

	// Correlates Figure A with Figure C
	public void correlateAWithC(RavensProbObj rp) {
		HashMap<String, RavensFigObj> map = rp.getFigs();
		RavensFigObj figA = map.get("A");
		RavensFigObj figC = map.get("C");

		List<ContentObj> contA = new ArrayList<ContentObj>(figA.getContents()
				.values());
		List<ContentObj> contC = new ArrayList<ContentObj>(figC.getContents()
				.values());

		//Arranges all the possible pairs
		int totalCombos = contA.size() * contC.size();
		ContentObj[][] allPairs = new ContentObj[totalCombos][2];
		int[] scores = new int[totalCombos];
		int numInserted = 0;
		for (int i = 0; i < contA.size(); i++) {
			for (int j = 0; j < contC.size(); j++) {
				allPairs[numInserted][0] = contA.get(i);
				allPairs[numInserted][1] = contC.get(j);
				numInserted++;
			}
		}

		//Gets the score for each possible pair
		for (int i = 0; i < totalCombos; i++) {
			scores[i] = scoreTwoContentObj(allPairs[i][0], allPairs[i][1]);
		}

		for (int i = 0; i < totalCombos; i++) {
			int greatestScore = 0;
			int greatestScorePos = 0;
			for (int j = 0; j < totalCombos; j++) {
				if (scores[j] > greatestScore) {
					greatestScore = scores[j];
					greatestScorePos = j;
					allPairs[greatestScorePos][1]
							.setAlias(allPairs[greatestScorePos][0].getAlias());
				}
			}
			

			for (int k = 0; k < totalCombos; k++) {
				if (allPairs[k][0].equals(allPairs[greatestScorePos][0])
						|| allPairs[k][0].equals(allPairs[greatestScorePos][1])
						|| allPairs[k][1].equals(allPairs[greatestScorePos][0])
						|| allPairs[k][1].equals(allPairs[greatestScorePos][1])) {
					scores[k] = -1;
				}
			}
		}
		
		//Sets all unspecified aliases
		for (ContentObj ct : contC) {
			if (ct.getAlias().equalsIgnoreCase("unspecified")) {
				ct.setAlias(unaliasedNames[unaliasedPos]);
				unaliasedPos++;
			}
		}

	}

	// Calculates the score between two ContentObjs
	public int scoreTwoContentObj(ContentObj a, ContentObj b) {
		int score = 0;

		// Shape
		if (a.getShape().equals(b.getShape())) {
			score += weight.get("shape");
		} else {
			score -= 1;
		}

		// Size
		if (a.getSize().equals(b.getSize())) {
			score += weight.get("size");
		} else {
			score -= 1;
		}

		// Fill
		if (a.getFill().equals(b.getFill())) {
			score += weight.get("fill");
		} else {
			score -= 1;
		}

		// Inside
		if (a.getInside().size() == (b.getInside().size())) {
			score += weight.get("inside");
		}

		// Above
		if (a.getAbove().size() == (b.getAbove().size())) {
			score += weight.get("above");
		}

		// Alignment
		if (a.getAlignment().equals(b.getAlignment())) {
			score += weight.get("alignment");
		}

		// Angle
		if (a.getAngle() == (b.getAngle())) {
			score += weight.get("angle");
		}
		
		return score;
	}

}
