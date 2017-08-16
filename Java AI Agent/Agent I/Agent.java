package ravensproject;

import java.awt.Image;

import content.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import transformationTables.TransformationObj;
import transformationTables.Transformer;

public class Agent {

	//Default constructor
	public Agent() {

	}

	public int Solve(RavensProblem problem) {
		// Set the final answer to -1. If no match is found, the agent will
		// "skip" the problem.
		int finalAnswer = -1;

		if (problem.hasVerbal() && problem.getProblemType().equals("2x2")) {
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
				if (fig.answerPair(answers.get(i))) {
					finalAnswer = Integer.valueOf(answers.get(i).getName());
				}
			}
			System.out.println("ANSWER: " + finalAnswer);

			// If the generated answer did not match any choices, regenerate
			// another answer
			// (based on an alternate transformation) and retest.
			if (finalAnswer < 0) {
				RavensFigObj figB = t.applyAlternateTransformation(rp, trans);
				for (int i = 0; i < 6; i++) {
					if (figB.answerPair(answers.get(i))) {
						finalAnswer = Integer.valueOf(answers.get(i).getName());
					}
				}
			}
		}
		return finalAnswer;
	}
}
