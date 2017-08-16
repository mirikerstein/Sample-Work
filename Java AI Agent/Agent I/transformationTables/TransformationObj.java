package transformationTables;

import java.util.ArrayList;

import content.ContentObj;
import ravensEnums.Alignment;
import ravensEnums.Fill;
import ravensEnums.Shape;
import ravensEnums.Size;

public class TransformationObj {

	private ContentObj aPointer;
	private ContentObj bPointer;
	private String alias;
	private String shapeTrans;
	private String sizeTrans;
	private String fillTrans;
	private String alignmentTrans;
	private String movementTrans;
	private String reflectionTrans;
	private double angleTrans;
	
	public TransformationObj(){
		this.alias = "";
		this.shapeTrans="";
		this.sizeTrans="";
		this.fillTrans = "";
		this.alignmentTrans="";
		this.movementTrans = "";
		this.angleTrans = 0;
		this.reflectionTrans = "";
	}
	
	public String toString(){
		return "Alias: "+this.alias+
				" Shape Transformation: "+this.shapeTrans+
				" Size Transformation: "+this.sizeTrans+
				" Fill Transformation: "+this.fillTrans+
				" Alignment Transformation: "+this.alignmentTrans+
				" Movement Transformation: "+this.movementTrans+
				" Angle Transformation: "+this.angleTrans+
				" Reflection Transformation: "+this.reflectionTrans;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getShapeTrans() {
		return shapeTrans;
	}

	public void setShapeTrans(String shapeTrans) {
		this.shapeTrans = shapeTrans;
	}

	public String getSizeTrans() {
		return sizeTrans;
	}

	public void setSizeTrans(String sizeTrans) {
		this.sizeTrans = sizeTrans;
	}

	public String getFillTrans() {
		return fillTrans;
	}

	public void setFillTrans(String fillTrans) {
		this.fillTrans = fillTrans;
	}

	public String getAlignmentTrans() {
		return alignmentTrans;
	}

	public void setAlignmentTrans(String alignmentTrans) {
		this.alignmentTrans = alignmentTrans;
	}

	public String getMovementTrans() {
		return movementTrans;
	}

	public void setMovementTrans(String movementTrans) {
		this.movementTrans = movementTrans;
	}

	public double getAngleTrans() {
		return angleTrans;
	}

	public void setAngleTrans(double angleTrans) {
		this.angleTrans = angleTrans;
	}

	public String getReflectionTrans() {
		return reflectionTrans;
	}

	public void setReflectionTrans(String reflectionTrans) {
		this.reflectionTrans = reflectionTrans;
	}

	public ContentObj getaPointer() {
		return aPointer;
	}

	public void setaPointer(ContentObj aPointer) {
		this.aPointer = aPointer;
	}

	public ContentObj getbPointer() {
		return bPointer;
	}

	public void setbPointer(ContentObj bPointer) {
		this.bPointer = bPointer;
	}
	
	
}
