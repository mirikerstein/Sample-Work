package ravensproject;

import java.util.ArrayList;
import java.util.HashMap;

import ravensproject.VisualSets.AnswerScore;
import ravensproject.VisualSets.FigureMap;
import ravensproject.VisualSets.FigureMapFactory;
import ravensproject.VisualSets.SetFactory;
import ravensproject.content.*;
import ravensproject.ravensEnums.*;
import ravensproject.ravensEnums.*;
import ravensproject.setC.*;
import ravensproject.transformationTables.*;

public class Agent {
	
	//Default constructor
	public Agent() {

	}

	public int Solve(RavensProblem problem) {
		// Set the final answer to -1. If no match is found, the agent will
		// "skip" the problem.
		int finalAnswer = -1;

		if (problem.getName().startsWith("Basic Problem B")||
				problem.getName().startsWith("Test Problem B")) {
			// Create the Transformer Object
			Transformer t = new Transformer();

			System.out.println("Solve called on: " + problem.getName());

			// Create the RavensProbObj from the given problem
			RavensProbObj rp = new RavensProbObj(problem);

			// Correlate all the content of the individual frames within the
			// matrix problem
			rp.correlateTwoByTwo();

			// System.out.println(rp);

			// This creates the transformation objects that will be applied in
			// order to get the answer
			HashMap<String, TransformationObj> trans = t
					.createTransformationList(rp);

			// This is the generated answer, which must be matched against the
			// given choice
			RavensFigObj fig = t.applyTransformation(rp, trans);

			ArrayList<RavensFigObj> answers = new ArrayList<RavensFigObj>();
			answers.add(rp.getFigs().get("1"));
			answers.add(rp.getFigs().get("2"));
			answers.add(rp.getFigs().get("3"));
			answers.add(rp.getFigs().get("4"));
			answers.add(rp.getFigs().get("5"));
			answers.add(rp.getFigs().get("6"));

			for (int i = 0; i < 6; i++) {
				if (fig.answerPair(answers.get(i),true)) {
					finalAnswer = Integer.valueOf(answers.get(i).getName());
				}
			}

			// If the generated answer did not match any choices, regenerate
			// another answer
			// (based on an alternate transformation) and retest.
			if (finalAnswer < 0) {
				RavensFigObj figB = t.applyAlternateTransformation(rp, trans);
				for (int i = 0; i < 6; i++) {
					if (figB.answerPair(answers.get(i), true)) {
						finalAnswer = Integer.valueOf(answers.get(i).getName());
					}
				}
			}
		}

		// For problem set c
		if (problem.getName().startsWith("Basic Problem C")||
				problem.getName().startsWith("Test Problem C")) {
			// Create the Transformer Object
			TransformerC tC = new TransformerC();

			System.out.println("Solve called on: " + problem.getName());
			finalAnswer = tC.processCSet(problem);

			
		
		}
		
		//Problem Set D
		if(problem.getName().startsWith("Basic Problem D")||
				problem.getName().startsWith("Test Problem D")){
			System.out.println("Solve called on: " + problem.getName());
			//FigureMapFactory factory = new FigureMapFactory();
			//finalAnswer = factory.processSetE(problem);
			SetFactory factory = new SetFactory();
			//finalAnswer = factory.TopBottomMatch(problem);
			//AnswerScore ans = factory.totalPixelCount(problem);
			//finalAnswer = ans.getAnswer();
			//finalAnswer = factory.pixelDiff(problem);
			finalAnswer = factory.processSetD(problem);
		}
		
		//Problem Set E
		if(problem.getName().startsWith("Basic Problem E")||
				problem.getName().startsWith("Test Problem E")){
			System.out.println("Solve called on: " + problem.getName());
			FigureMapFactory factory = new FigureMapFactory();
			finalAnswer = factory.processSetE(problem);
		}
		
		System.out.println("ANSWER: " + finalAnswer);
		return finalAnswer;
	}
}
