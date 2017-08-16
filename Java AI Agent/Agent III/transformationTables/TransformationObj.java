package ravensproject.transformationTables;

import java.util.ArrayList;
import ravensproject.*;
import ravensproject.content.ContentObj;
import ravensproject.ravensEnums.Alignment;
import ravensproject.ravensEnums.Fill;
import ravensproject.ravensEnums.Shape;
import ravensproject.ravensEnums.Size;

public class TransformationObj {

	private ContentObj aPointer;
	private ContentObj bPointer;
	private String alias;
	private String shapeTrans;
	private String sizeTrans;
	private String widthTrans;
	private String heightTrans;
	private String fillTrans;
	private String alignmentTrans;
	private String movementTrans;
	private String reflectionTrans;
	private int leftTrans;
	private int aboveTrans;
	private double angleTrans;
	private boolean unchanged;
	
	public TransformationObj(){
		this.alias = "";
		this.shapeTrans="";
		this.sizeTrans="";
		this.widthTrans="";
		this.heightTrans="";
		this.fillTrans = "";
		this.alignmentTrans="";
		this.movementTrans = "";
		this.angleTrans = 0;
		this.reflectionTrans = "";
		this.unchanged = false;
		this.leftTrans = 0;
		this.aboveTrans = 0;
	}
	
	public String toString(){
		return "Alias: "+this.alias+
				" Shape Transformation: "+this.shapeTrans+
				" Size Transformation: "+this.sizeTrans+
				" Width Transformation: "+this.widthTrans+
				" Height Transformation: "+this.heightTrans+
				" Fill Transformation: "+this.fillTrans+
				" Alignment Transformation: "+this.alignmentTrans+
				" Movement Transformation: "+this.movementTrans+
				" Angle Transformation: "+this.angleTrans+
				" Above Trans: "+this.aboveTrans+
				" Left Trans: "+this.leftTrans+
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

	public boolean getUnchanged() {
		return unchanged;
	}

	public void setUnchanged(boolean unchanged) {
		this.unchanged = unchanged;
	}

	public String getWidthTrans() {
		return widthTrans;
	}

	public void setWidthTrans(String widthTrans) {
		this.widthTrans = widthTrans;
	}

	public String getHeightTrans() {
		return heightTrans;
	}

	public void setHeightTrans(String heightTrans) {
		this.heightTrans = heightTrans;
	}

	public int getLeftTrans() {
		return leftTrans;
	}

	public void setLeftTrans(int leftTrans) {
		this.leftTrans = leftTrans;
	}

	public int getAboveTrans() {
		return aboveTrans;
	}

	public void setAboveTrans(int aboveTrans) {
		this.aboveTrans = aboveTrans;
	}
	
	
}
