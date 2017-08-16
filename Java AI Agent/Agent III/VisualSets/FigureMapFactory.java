package ravensproject.VisualSets;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import ravensproject.RavensFigure;
import ravensproject.RavensProblem;

public class FigureMapFactory {
	
	//Fields
	private int dim;
	
	public FigureMapFactory(){
		this.dim = 184;
	}
	
	
	//Sums two FigureMaps and produces a new FigureMap
	public FigureMap sumAandB(FigureMap a, FigureMap b){
		FigureMap sum = new FigureMap();
		for(int i = 0; i<dim; i++){
			for(int j = 0; j<dim; j++){
				Point name = new Point(i,j); 
				if(a.get(name).equals("FILLED")||b.get(name).equals("FILLED")){
					sum.put(name, "FILLED");
				}else{
					sum.put(name, "EMPTY");
				}
			}
		}
		return sum;
	}
	
	//Subtracts two FigureMaps and produces a new FigureMap
	public FigureMap minusAandB(FigureMap a, FigureMap b){
		FigureMap sum = new FigureMap();
		for(int i = 0; i<dim; i++){
			for(int j = 0; j<dim; j++){
				Point name = new Point(i,j); 
				if(b.get(name).equals("FILLED")){
					sum.put(name, "EMPTY");
				}else if(b.get(name).equals("EMPTY")&&a.get(name).equals("FILLED")){
					sum.put(name, "FILLED");
				}else{
					sum.put(name, "EMPTY");
				}
			}
		}
		return sum;
	}
	
	//Only pixels that are filled in both
		public FigureMap onlyAandB(FigureMap a, FigureMap b){
			FigureMap sum = new FigureMap();
			for(int i = 0; i<dim; i++){
				for(int j = 0; j<dim; j++){
					Point name = new Point(i,j); 
					if(a.get(name).equals("FILLED")&&b.get(name).equals("FILLED")){
						sum.put(name, "FILLED");
					}else{
						sum.put(name, "EMPTY");
					}
				}
			}
			return sum;
		}
		

		
	//A Pixels + B Pixels, but not A&B Pixels -- aka XOR
		public FigureMap xor(FigureMap a, FigureMap b){
			FigureMap sum = new FigureMap();
			for(int i = 0; i<dim; i++){
				for(int j = 0; j<dim; j++){
					Point name = new Point(i,j); 
					if(a.get(name).equals("FILLED")&&b.get(name).equals("FILLED")){
						sum.put(name, "EMPTY");
					}else if(a.get(name).equals("FILLED")||b.get(name).equals("FILLED")){
						sum.put(name, "FILLED");
					}else{
						sum.put(name, "EMPTY");
					}
				}
			}
			return sum;
		}
		
		//Left a, right b
		public FigureMap leftARightB(FigureMap a, FigureMap b){
			FigureMap sum = new FigureMap();
				
			//Get left half from a
				for(int i = 0; i<dim/2; i++){
					for(int j = 0; j<dim; j++){
						Point name = new Point(i,j);
						String fill = a.get(name);
						sum.put(name, fill);
					}
				}
				
				//Get right half from b
				for(int i = dim/2; i<dim; i++){
					for(int j = 0; j<dim; j++){
						Point name = new Point(i,j);
						String fill = b.get(name);
						sum.put(name, fill);
					}
				}
				return sum;
		}
	
		//Top A bottom B
		public FigureMap topABottomB(FigureMap a, FigureMap b){
			FigureMap sum = new FigureMap();
				
			//Get left half from a
				for(int i = 0; i<dim; i++){
					for(int j = 0; j<dim/2; j++){
						Point name = new Point(i,j);
						String fill = a.get(name);
						sum.put(name, fill);
					}
				}
				
				//Get right half from b
				for(int i = 0; i<dim; i++){
					for(int j = dim/2; j<dim; j++){
						Point name = new Point(i,j);
						String fill = b.get(name);
						sum.put(name, fill);
					}
				}
				return sum;
		}
		
	//Checks if two figure maps are equals
	public int doesAEqualB(FigureMap a, FigureMap b){
		int numDiff = 0;
		for(int i = 0; i<dim; i++){
			for(int j = 0; j<dim; j++){
				Point name = new Point(i,j);
				String aVal = a.get(name);
				String bVal = b.get(name);
				if(aVal.equals(bVal)){
					//System.out.println("WE ARE EQUAL PIXELS");
				}else{
					numDiff++;
				}
			}
		}
		//System.out.println("NUM DIFF: "+numDiff);
		return numDiff;
	}

	//All processing for Set E
	public int processSetE(RavensProblem problem){
		int finalAnswer = -1;
		int maxDisparity = 0;
		FigureMap answerMap = new FigureMap();
		
		
		//Get the figures and figure maps for A, B, and C
		//Get the two bottom figures and create their maps
		RavensFigure a = problem.getFigures().get("A");
		RavensFigure b = problem.getFigures().get("B");
		RavensFigure c = problem.getFigures().get("C");
		
		FigureMap mapA = new FigureMap(a);
		FigureMap mapB = new FigureMap(b);
		FigureMap mapC = new FigureMap(c);
		
		RavensFigure g = problem.getFigures().get("G");
		RavensFigure h = problem.getFigures().get("H");
		
		FigureMap mapG = new FigureMap(g);
		FigureMap mapH = new FigureMap(h);
		
		//Apply all the transformations to A and B, match the result against C, and
		//check which one has the lowest disparity
		
		int additionDis, minusDis, xorDis, onlyDis, topABottomBDis, leftARightBDis;
		
		//Addition
		FigureMap additionMap = sumAandB(mapA, mapB);
		additionDis = doesAEqualB(mapC, additionMap);
		//System.out.println("ADDITION DIS: "+additionDis);
		
		//Minus
		FigureMap minusMap = minusAandB(mapA, mapB);
		minusDis = doesAEqualB(mapC, minusMap);
		//System.out.println("MINUS DIS: "+minusDis);
		
		//XOR
		FigureMap xorMap = xor(mapA, mapB);
		xorDis = doesAEqualB(mapC, xorMap);
		//System.out.println("XOR DIS: "+xorDis);
		
		//Only 
		FigureMap onlyMap = onlyAandB(mapA, mapB);
		onlyDis = doesAEqualB(mapC, onlyMap);
		//System.out.println("ONLY DIS: "+onlyDis);
		
		//Left A Right B
		FigureMap leftARightBMap = leftARightB(mapA, mapB);
		leftARightBDis = doesAEqualB(mapC, leftARightBMap);
		//System.out.println("Left A Right B DIS: "+leftARightBDis);
		
		//Top A Bottom B
		FigureMap topABottomBMap = topABottomB(mapA, mapB);
		topABottomBDis = doesAEqualB(mapC, topABottomBMap);
	//	System.out.println("TOP A BOTTOM B DIS: "+topABottomBDis);
		
		//Addition Case
		if(additionDis<minusDis&&
				additionDis<=xorDis&&
				additionDis<onlyDis){
			maxDisparity = additionDis;
			answerMap = sumAandB(mapG, mapH);
		}
		
		//Subtraction Case
		if(minusDis<additionDis&&
				minusDis<=xorDis&&
				minusDis<onlyDis){
			maxDisparity = minusDis;
			answerMap = minusAandB(mapG, mapH);
		}
		
		//XOR Case
		if(xorDis<additionDis&&
				xorDis<minusDis&&
				xorDis<onlyDis){
			maxDisparity = xorDis;
			answerMap = xor(mapG, mapH);
		}
		
		//Only case
		if(onlyDis<additionDis&&
				onlyDis<minusDis&&
				onlyDis<xorDis){
			maxDisparity = onlyDis;
			answerMap = onlyAandB(mapG, mapH);
		}
		
		//Top A bottom B case
		if(topABottomBDis<additionDis&&
				topABottomBDis<onlyDis&&
				topABottomBDis<minusDis&&
				topABottomBDis<xorDis){
			maxDisparity = topABottomBDis;
			answerMap = topABottomB(mapG, mapH);
		}
		
		
		/*
		BufferedImage diffImage = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_ARGB);
		for(int x = 0 ; x < dim ; x++) {
			for(int y = 0 ; y < dim ; y++) {
				Point name = new Point(x,y);
				String fill = answerMap.get(name);
				if(fill.equals("EMPTY")){
					diffImage.setRGB(x, y, Color.WHITE.getRGB());
				}else{
					diffImage.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
			ImageIcon icon = new ImageIcon();
			icon.setImage(diffImage);
			JOptionPane.showMessageDialog(null, icon);
		*/
		
		//Get all the answers in a list
		RavensFigure fig1 = problem.getFigures().get("1");
		RavensFigure fig2 = problem.getFigures().get("2");
		RavensFigure fig3 = problem.getFigures().get("3");
		RavensFigure fig4 = problem.getFigures().get("4");
		RavensFigure fig5 = problem.getFigures().get("5");
		RavensFigure fig6 = problem.getFigures().get("6");
		RavensFigure fig7 = problem.getFigures().get("7");
		RavensFigure fig8 = problem.getFigures().get("8");
		
		ArrayList<RavensFigure>answers = new ArrayList<RavensFigure>();
		answers.add(fig1);
		answers.add(fig2);
		answers.add(fig3);
		answers.add(fig4);
		answers.add(fig5);
		answers.add(fig6);
		answers.add(fig7);
		answers.add(fig8);
		
		HashMap<Integer, Integer>disparities = new HashMap<Integer, Integer>();
		
		//For each answer choice, create a FigureMap and check its disparity against answerMap.
		for(RavensFigure rf:answers){
			FigureMap map = new FigureMap(rf);
			Integer dis = doesAEqualB(map, answerMap);
			Integer name = Integer.valueOf(rf.getName());
			disparities.put(name, dis);
		}
		
		//Find the answer with the lowest disparity
		int lowestNum = disparities.get(1);
		int lowestName = 1;
		
		for(Integer name:disparities.keySet()){
			Integer myName = name;
			Integer myNum = disparities.get(name);
			if(myNum<=lowestNum){
				lowestName = myName;
				lowestNum = myNum;
			}
		}
		finalAnswer = lowestName;
		return finalAnswer;
	}
}
