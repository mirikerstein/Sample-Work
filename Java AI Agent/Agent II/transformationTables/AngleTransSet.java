package ravensproject.transformationTables;

public class AngleTransSet {

	private double angle;
	private String trans;
	
	public AngleTransSet(double ang, String trans){
		this.angle = ang;
		this.trans = trans;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public String getTrans() {
		return trans;
	}

	public void setTrans(String trans) {
		this.trans = trans;
	}
	
	
}
