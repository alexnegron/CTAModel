/*
 * Alex Negron 5/11/2020
 * 
 * CTALine is a class that extends CTARoute. The main purpose for this class is to help distinguish
 * between routes and lines, even though a line technically is a route. Since lines have 
 * special properties we want to refer to many times in these classes, we set CTALine as its own 
 * class.
 */


package project;

import java.util.ArrayList;

public class CTALine extends CTARoute {

	
	private String lineColor; // names : red, green, etc
	private int colorIndex; // red: 0, green : 1, etc
	private ArrayList<CTAStation> lineStops; // all the stop on this particular line
	private CTAStation finalTwoWayStop; 
	
	
	

	// Constructors
	public CTALine() {
		this.lineColor = "";
		this.colorIndex = -1;
	}
	
	public CTALine(String lineColor, int colorIndex, ArrayList<CTAStation> lineStops) {
		this.lineColor = lineColor;
		this.colorIndex = colorIndex;
		this.lineStops = lineStops;
	}
	// Getters & Setters 
	public String getLineColor() {
		return lineColor;
	}
	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}
	public int getColorIndex() {
		return colorIndex;
	}
	public void setColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
	}
	public ArrayList<CTAStation> getLineStops() {
		return lineStops;
	}
	public void setLineStops(ArrayList<CTAStation> lineStops) {
		this.lineStops = lineStops;
	}
	public CTAStation getFinalTwoWayStop() {
		return finalTwoWayStop;
	}

	public void setFinalTwoWayStop(CTAStation finalTwoWayStop) {
		this.finalTwoWayStop = finalTwoWayStop;
	}
	
	public static void sortLine(CTALine line) {
		int index = line.getColorIndex();
		int n = line.getLineStops().size();
		for(int i = 0; i < n-1; i++) {
			for(int j = 0; j < n - i - 1; j++) {
				if(line.getLineStops().get(j).getPositionsOnLine().get(index) 
						> line.getLineStops().get(j+1).getPositionsOnLine().get(index)) {
					CTAStation temp = line.getLineStops().get(j);
					line.getLineStops().set(j, line.getLineStops().get(j+1));
					line.getLineStops().set(j+1, temp);
				}
			}
		}
	}
	
	public void removeStation(CTAStation station) {
		int index = this.getLineStops().indexOf(station);
		this.getLineStops().remove(index); // removes the station
		// now we have to shift the indices of all the following stations
		for(int i = index; i < this.getLineStops().size(); i++) {
			this.getLineStops().get(i).getPositionsOnLine().set(this.getColorIndex(), 
					this.getLineStops().get(i).getPositionsOnLine().get(this.getColorIndex())-1);
		}
	}

	@Override
	public String toString() {
		return "CTALine [lineColor=" + lineColor + ", colorIndex=" + colorIndex + ", lineStops=" + lineStops
				+ ", finalTwoWayStop=" + finalTwoWayStop + "]";
	}
	
	
	
	
	
	
	
}
