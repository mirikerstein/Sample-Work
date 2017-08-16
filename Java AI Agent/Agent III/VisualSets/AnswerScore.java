package ravensproject.VisualSets;

public class AnswerScore {
	private int answer;
	private int score;
	
	public AnswerScore(int a, int s){
		this.answer=a;
		this.score=s;
	}

	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
