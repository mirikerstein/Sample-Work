package ravensproject.VisualSets;

import java.util.ArrayList;

import ravensproject.RavensProblem;



public class SetFactory extends FigureMapFactory{

	private int dim;
	
	//Constructor
	public SetFactory(){
		this.dim = 184;
	}
	
	
	/*Pixel Comparison Rule -- solves Probs 1, 2, 3, 4, 5, 6, 11
	 * First, attempt to score the pixel disparity between images on the top row and images on the bottom row.
	 * Lowest pixel differences are assigned as pairs. 
	 * PixelDiff is stored.
	 * One image from Row 1 remains unmatched.
	 * Match that image against all the answer images, and choose the one with  the pixel disparity that is closest to PixelDiff
	 */
	public AnswerScore topBottomMatch(RavensProblem problem){
		int answer = -1;
		ArrayList<Integer> pixelDiffs = new ArrayList<Integer>();
		
		//Create FigureMaps of the top row
		FigureMap mapA = new FigureMap(problem.getFigures().get("A"));
		FigureMap mapB = new FigureMap(problem.getFigures().get("B"));
		FigureMap mapC = new FigureMap(problem.getFigures().get("C"));
		
		//Put the top row in a list
		ArrayList<FigureMap>topRow = new ArrayList<FigureMap>();
		topRow.add(mapA);
		topRow.add(mapB);
		topRow.add(mapC);
		
		//Create FigureMaps of the bottom row
		FigureMap mapG = new FigureMap(problem.getFigures().get("G"));
		FigureMap mapH = new FigureMap(problem.getFigures().get("H"));
		
		//Create FigureMaps of all the answers and put them in a list
		ArrayList<FigureMap>answers = new ArrayList<FigureMap>();
		FigureMap map1 = new FigureMap(problem.getFigures().get("1"));
		FigureMap map2 = new FigureMap(problem.getFigures().get("2"));
		FigureMap map3 = new FigureMap(problem.getFigures().get("3"));
		FigureMap map4 = new FigureMap(problem.getFigures().get("4"));
		FigureMap map5 = new FigureMap(problem.getFigures().get("5"));
		FigureMap map6 = new FigureMap(problem.getFigures().get("6"));
		FigureMap map7 = new FigureMap(problem.getFigures().get("7"));
		FigureMap map8 = new FigureMap(problem.getFigures().get("8"));
		answers.add(map1);
		answers.add(map2);
		answers.add(map3);
		answers.add(map4);
		answers.add(map5);
		answers.add(map6);
		answers.add(map7);
		answers.add(map8);
		
		/*The possible pairs are:
		 * A, G
		 * A, H
		 * B, G
		 * B, H
		 * C, G
		 * C, H
		 */
		
		//Create a 2D array to hold the pairs
		FigureMap[][]pairs = new FigureMap[6][2];
		
		//Put the pairs into the array
		pairs[0][0] = mapA;
		pairs[0][1] = mapG;
	
		pairs[1][0] = mapA;
		pairs[1][1] = mapH;
		
		pairs[2][0] = mapB;
		pairs[2][1] = mapG;
		
		pairs[3][0] = mapB;
		pairs[3][1] = mapH;
		
		pairs[4][0] = mapC;
		pairs[4][1] = mapG;
		
		pairs[5][0] = mapC;
		pairs[5][1] = mapH;
		
		//Create an array to hold the scores
		int[]disparities = new int[6];

		
		//Get the score of each pair and put it in corresponding location
		for(int i = 0; i<6; i++){
				disparities[i] = doesAEqualB(pairs[i][0], pairs[i][1]);
				//System.out.println("PAIR "+i+": Disparity: "+disparities[i]);
		}
		
		//From the current list, get the lowest scoring pair
				//Set the aliases of that pair
				//Remove that pair from the list
				for (int i = 0; i < 2; i++) {
					//System.out.println("ROUND "+i);
					
					//What is the lowest score on the list?
					int lowestScore = disparities[0];
					int lowestScorePos = 0;
					
					//System.out.println("LOWEST SCORE AT ROUND BEGIN: "+lowestScore);
					//Loop through and locate lowest score
					for (int j = 0; j < 6; j++) {
						//System.out.println("DISPARITIES["+j+"]="+disparities[j]);
						if (disparities[j] < lowestScore) {
							//System.out.println("I AM LOWER!");
							lowestScore = disparities[j];
							lowestScorePos = j;
						}
					}
					//System.out.println("LOWEST SCORE FOR THIS ROUND: "+lowestScore);
					
					//Put the pixel disparity of the lowest scoring pair, i.e. the "MATCH", into the list of pixeldiffs
					pixelDiffs.add(disparities[lowestScorePos]);
					
					//System.out.println("PAIR: "+pairs[lowestScorePos][0].getName()+","+pairs[lowestScorePos][1].getName()+" DISPARITY: "+disparities[lowestScorePos]);
					
					//Remove the top row figure that has been matched, from the top row list.
					//Also, set the score of any such row to 100000000.
					for(int k = 0; k<topRow.size(); k++){
						if(topRow.get(k).equals(pairs[lowestScorePos][0])||
								topRow.get(k).equals(pairs[lowestScorePos][1])){
							//System.out.println("REMOVE CALLED?");
							topRow.remove(k);
						}
					}
					
					//System.out.println("PAIR TO REMOVE: "+pairs[lowestScorePos][0].getName()+","+pairs[lowestScorePos][1].getName());
					String nameA = pairs[lowestScorePos][0].getName();
					String nameB = pairs[lowestScorePos][1].getName();
					for (int k = 0; k < 6; k++) {
						//System.out.println("PAIR IN K: "+pairs[k][0].getName()+","+pairs[k][1].getName());
						String knameA = pairs[k][0].getName();
						String knameB = pairs[k][1].getName();
						if (knameA.equals(nameA)||
								knameB.equals(nameA)||
								knameA.equals(nameB)||
								knameB.equals(nameB)){
							//System.out.println("I CONTAIN ONE OF THE MATCH.");
							disparities[k] = 1000000;
						}
					}
				}
				
				/*
				 * At this point, the matches have been paired, and the unmatched figure has been identified.
				 * The disparities between the pairs are stored in pixelDiffs.
				 * Now, the following must be done:
				 * 1. Pair the unmatchedFig against all the answers, storing the disparities
				 * 2. Choose the answer whose disparity is closest to the two disparities that are in pixelDiffs.
				 */
				
				FigureMap unmatched = topRow.get(0);
				//System.out.println("UNMATCHED FIGURE: "+unmatched.getName());
				int[]answerDiffs = new int[8];
				
				for(int m = 0; m<8; m++){
					answerDiffs[m] = doesAEqualB(unmatched, answers.get(m));
					//System.out.println("ANSWER DIFF for "+m+": "+answerDiffs[m]);
				}
				
				int avgPixelDiff = (pixelDiffs.get(0)+pixelDiffs.get(1))/2;
				int [] dev = new int[8];
				for(int m = 0; m<8; m++){
					int num = answerDiffs[m];
					if(num>avgPixelDiff){
					dev[m] = answerDiffs[m] - avgPixelDiff;
					}else{
						dev[m] = avgPixelDiff - num;
					}
				}
				
				//Get the lowest deviation from the average
				int lowestDev = dev[0];
				int lowestDevPos = 0;
				for(int n = 0; n<8; n++){
					if(dev[n]<lowestDev){
						lowestDev = dev[n];
						lowestDevPos = n;
					}
				}
			//	System.out.println("AVG PIXEL DIFF: "+ avgPixelDiff+" LOWEST DEV: "+lowestDev);
				answer = lowestDevPos+1;
				AnswerScore ans = new AnswerScore(answer, lowestDev);
		return ans;
	}
	
	
	/*
	 * Total Pixel Count method: for problems 7,8,9,10, 12.
	 * Gets total Pixel Count for Row One.
	 * Gets total Pixel Count for Row Two.
	 * Gets total Pixel Count for Row Three.
	 * Adds each answer choice PC to Row Three.
	 * Chooses whichever one gets it closest to the Average of Row One and Row Two
	 */
	
	public AnswerScore totalPixelCount(RavensProblem problem){
		int answer = -1;
		
		//Lists
		ArrayList<FigureMap>row1 = new ArrayList<FigureMap>();
		ArrayList<FigureMap>row2 = new ArrayList<FigureMap>();
		ArrayList<FigureMap>row3 = new ArrayList<FigureMap>();
		ArrayList<FigureMap>col1 = new ArrayList<FigureMap>();
		ArrayList<FigureMap>col2 = new ArrayList<FigureMap>();
		ArrayList<FigureMap>col3 = new ArrayList<FigureMap>();
		ArrayList<FigureMap>answers = new ArrayList<FigureMap>();
		
		//Row One FigureMaps
		FigureMap mapA = new FigureMap(problem.getFigures().get("A"));
		FigureMap mapB = new FigureMap(problem.getFigures().get("B"));
		FigureMap mapC = new FigureMap(problem.getFigures().get("C"));
		row1.add(mapA);
		row1.add(mapB);
		row1.add(mapC);
		
		//Row Two FigureMaps
		FigureMap mapD = new FigureMap(problem.getFigures().get("D"));
		FigureMap mapE = new FigureMap(problem.getFigures().get("E"));
		FigureMap mapF = new FigureMap(problem.getFigures().get("F"));
		row2.add(mapD);
		row2.add(mapE);
		row2.add(mapF);
		
		//Row Three FigureMaps
		FigureMap mapG = new FigureMap(problem.getFigures().get("G"));
		FigureMap mapH = new FigureMap(problem.getFigures().get("H"));
		row3.add(mapG);
		row3.add(mapH);
		
		col1.add(mapA);
		col1.add(mapD);
		col1.add(mapG);
		col2.add(mapB);
		col2.add(mapE);
		col2.add(mapH);
		col3.add(mapC);
		col3.add(mapF);

		//Answers FigureMaps
		FigureMap map1 = new FigureMap(problem.getFigures().get("1"));
		FigureMap map2 = new FigureMap(problem.getFigures().get("2"));
		FigureMap map3 = new FigureMap(problem.getFigures().get("3"));
		FigureMap map4 = new FigureMap(problem.getFigures().get("4"));
		FigureMap map5 = new FigureMap(problem.getFigures().get("5"));
		FigureMap map6 = new FigureMap(problem.getFigures().get("6"));
		FigureMap map7 = new FigureMap(problem.getFigures().get("7"));
		FigureMap map8 = new FigureMap(problem.getFigures().get("8"));
		answers.add(map1);
		answers.add(map2);
		answers.add(map3);
		answers.add(map4);
		answers.add(map5);
		answers.add(map6);
		answers.add(map7);
		answers.add(map8);
		
		//To store the value of PC in each row
		int row1Count = 0;
		int row2Count = 0; 
		int row3Count = 0;
		int col1Count = 0;
		int col2Count = 0;
		int col3Count = 0;
		
		for(FigureMap m:row1){
			row1Count+=m.getFilledCount();
		}
		
		for(FigureMap m:row2){
			row2Count+=m.getFilledCount();
		}
		
		for(FigureMap m:row3){
			row3Count+=m.getFilledCount();
		}
		
		for(FigureMap m:col1){
			col1Count+=m.getFilledCount();
		}
		
		for(FigureMap m:col2){
			col2Count+=m.getFilledCount();
		}
		
		for(FigureMap m:col3){
			col3Count+=m.getFilledCount();
		}
		
	//	System.out.println("ROW 1: "+row1Count+" ROW 2: "+row2Count+" ROW 3: "+row3Count);
		//The average PC in row 1 and 2
		int avg = (row1Count+row2Count)/2;
	//	System.out.println("AVERAGE ROW COUNT: "+avg);
		
		//System.out.println("COL 1: "+col1Count+" COL 2: "+col2Count+" COL 3: "+col3Count);
		//The average PC in row 1 and 2
		int avgB = (col1Count+col2Count)/2;
	//	System.out.println("AVERAGE COL COUNT: "+avgB);
		
		//List for all sums of row3 + answer option
		int[]answerCountsR = new int [8];
		int[]answerCountsC = new int [8];
		
		for(int i = 0; i<8; i++){
			answerCountsR[i] = answers.get(i).getFilledCount()+row3Count;
			answerCountsC[i] = answers.get(i).getFilledCount()+col3Count;
			int pos = i+1;
			//System.out.println("ANSWER ROW"+pos+": "+answerCountsR[i]);
			//System.out.println("ANSWER COL"+pos+": "+answerCountsC[i]);
		}
		
		//List for the differences between the average and the answerCount
		int [] answerDiffs = new int[8];
		int avgComb = avg+avgB;
		for(int i = 0; i<8; i++){
			int num = answerCountsR[i]+answerCountsC[i];
			if(avgComb>num){
				answerDiffs[i] = avgComb-num;
			}else{
				answerDiffs[i] = num-avgComb;
			}
		}
		
		//Find the lowest answer diff
		int lowestAnswer = answerDiffs[0];
		int lowestAnswerPos = 0;
		
		for(int i = 0; i<8; i++){
			if(answerDiffs[i]<lowestAnswer){
				lowestAnswer = answerDiffs[i];
				lowestAnswerPos = i;
			}
		}
		
		answer = lowestAnswerPos+1;
		AnswerScore ans = new AnswerScore(answer, lowestAnswer);
		return ans;
	}
	
	/*
	 * Pixel Diff Method
	 */
	
	public AnswerScore pixelDiff(RavensProblem problem){
		int answer = -1;
		
		//Get all figures into maps and lists
		//Lists
				ArrayList<FigureMap>row1 = new ArrayList<FigureMap>();
				ArrayList<FigureMap>row2 = new ArrayList<FigureMap>();
				ArrayList<FigureMap>row3 = new ArrayList<FigureMap>();
				ArrayList<FigureMap>col1 = new ArrayList<FigureMap>();
				ArrayList<FigureMap>col2 = new ArrayList<FigureMap>();
				ArrayList<FigureMap>col3 = new ArrayList<FigureMap>();
				ArrayList<FigureMap>answers = new ArrayList<FigureMap>();
				
				//Row One FigureMaps
				FigureMap mapA = new FigureMap(problem.getFigures().get("A"));
				FigureMap mapB = new FigureMap(problem.getFigures().get("B"));
				FigureMap mapC = new FigureMap(problem.getFigures().get("C"));
				row1.add(mapA);
				row1.add(mapB);
				row1.add(mapC);
				
				//Row Two FigureMaps
				FigureMap mapD = new FigureMap(problem.getFigures().get("D"));
				FigureMap mapE = new FigureMap(problem.getFigures().get("E"));
				FigureMap mapF = new FigureMap(problem.getFigures().get("F"));
				row2.add(mapD);
				row2.add(mapE);
				row2.add(mapF);
				
				//Row Three FigureMaps
				FigureMap mapG = new FigureMap(problem.getFigures().get("G"));
				FigureMap mapH = new FigureMap(problem.getFigures().get("H"));
				row3.add(mapG);
				row3.add(mapH);
				
				col1.add(mapA);
				col1.add(mapD);
				col1.add(mapG);
				col2.add(mapB);
				col2.add(mapE);
				col2.add(mapH);
				col3.add(mapC);
				col3.add(mapF);

				//Answers FigureMaps
				FigureMap map1 = new FigureMap(problem.getFigures().get("1"));
				FigureMap map2 = new FigureMap(problem.getFigures().get("2"));
				FigureMap map3 = new FigureMap(problem.getFigures().get("3"));
				FigureMap map4 = new FigureMap(problem.getFigures().get("4"));
				FigureMap map5 = new FigureMap(problem.getFigures().get("5"));
				FigureMap map6 = new FigureMap(problem.getFigures().get("6"));
				FigureMap map7 = new FigureMap(problem.getFigures().get("7"));
				FigureMap map8 = new FigureMap(problem.getFigures().get("8"));
				answers.add(map1);
				answers.add(map2);
				answers.add(map3);
				answers.add(map4);
				answers.add(map5);
				answers.add(map6);
				answers.add(map7);
				answers.add(map8);
				
				//Differences across rows and down columns
				int row1Diff = 0;
				int row2Diff = 0;
				int row3Diff = 0;
				
				int col1Diff = 0; 
				int col2Diff = 0;
				int col3Diff = 0;
				
		
				row1Diff = doesAEqualB(mapA,mapB)+doesAEqualB(mapB,mapC);
				row2Diff = doesAEqualB(mapD,mapE)+doesAEqualB(mapE,mapF);
				row3Diff = doesAEqualB(mapG,mapH);
				
				col1Diff = doesAEqualB(mapA,mapD)+doesAEqualB(mapD,mapG);
				col2Diff = doesAEqualB(mapB,mapE)+doesAEqualB(mapE,mapH);
				col3Diff = doesAEqualB(mapC,mapF);
				
				//Get the total for row 3 and col 3 when combined with each answer
				int[]answerCountsR = new int [8];
				int[]answerCountsC = new int [8];
				
				for(int i = 0; i<8; i++){
					answerCountsR[i] = row3Diff+doesAEqualB(mapH,answers.get(i));
					answerCountsC[i] = col3Diff+doesAEqualB(mapF,answers.get(i));
					int pos = i+1;
					//System.out.println("ANSWER ROW"+pos+": "+answerCountsR[i]);
					//System.out.println("ANSWER COL"+pos+": "+answerCountsC[i]);
				}
				
				//Find the one that's difference from the answer row and answer colum is lowest
				int[]answerDiffs = new int[8];
				for(int i = 0; i<8; i++){
					int row = answerCountsR[i];
					int col = answerCountsC[i];
					int rdiff = 0;
					int cdiff = 0;
					if(row>row1Diff){
						rdiff = row-row1Diff;
					}else{
						rdiff = row1Diff - row;
					}
					if(col>col1Diff){
						cdiff = col-col1Diff;
					}else{
						cdiff = col1Diff-col;
					}
					answerDiffs[i]=rdiff+cdiff;
					//System.out.println("ANSWER DIFF: "+answerDiffs[i]);
				}
				//Find the lowest answer diff
				int lowestAnswer = answerDiffs[0];
				int lowestAnswerPos = 0;
				
				for(int i = 0; i<8; i++){
					if(answerDiffs[i]<lowestAnswer){
						lowestAnswer = answerDiffs[i];
						lowestAnswerPos = i;
					}
				}
				
				answer = lowestAnswerPos+1;
				AnswerScore ans = new AnswerScore(answer, lowestAnswer);
				
		return ans;
	}
	
	//Process Set D
	public int processSetD(RavensProblem prob){
		int answer = -1;
		AnswerScore topBottom = topBottomMatch(prob);
		AnswerScore pixCount = totalPixelCount(prob);
		AnswerScore pixDiff = pixelDiff(prob);
		//System.out.println("TOP BOTTOM: "+topBottom.getScore()+" PIX COUNT: "+pixCount.getScore());
		
		if(topBottom.getScore()<pixCount.getScore()&&
				topBottom.getScore()<pixDiff.getScore()){
			answer = topBottom.getAnswer();
			//System.out.println("CHOOSING TOP BOTTOM");
		}
		
		if(pixCount.getScore()<topBottom.getScore()&&
				pixCount.getScore()<pixDiff.getScore()){
			answer = pixCount.getAnswer();
			//System.out.println("CHOOSING PIX COUNT");
		}
		
		//if(pixDiff.getScore()<topBottom.getScore()&&pixDiff.getScore()<pixCount.getScore()){
			//answer = pixDiff.getAnswer();
		//}
		
		return answer;
	}
}
