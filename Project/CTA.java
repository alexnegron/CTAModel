/*
 * Alex Negron 5/11/2020
 * 
 * CTA is a class that encapsulates the data for defining a CTA system, namely, all the objects in the csv data provided.
 * The primary use of this class is that the methods defined here are used heavily in the application class,
 * where each function that comprises the menu options is called on a CTA object. 
 */

package project;

import java.util.ArrayList;

public class CTA extends CTALine{

	
	private ArrayList<CTALine> allLines;
	private ArrayList<CTAStation> allStations;
	private ArrayList<CTAStation> allConnections;
	
	//Constructors
	public CTA() {
		allLines = new ArrayList<CTALine>();
		allStations = new ArrayList<CTAStation>();
		allConnections = new ArrayList<CTAStation>();
	}
	
	public CTA(ArrayList<CTALine> allLines, ArrayList<CTAStation> allStations, 
			ArrayList<CTAStation> connectors ) {
		this.allLines = allLines;
		this.allStations = allStations;
		this.allConnections = connectors;
	}
	
	// Setters
	public void setallLines(ArrayList<CTALine> allLines) {
		this.allLines = allLines;
	}
	
	public void setallStations(ArrayList<CTAStation> allStations) {
		this.allStations = allStations;
	}
	
	public void setConnectors(ArrayList<CTAStation> connectors) {
		this.allConnections = connectors;
	}
	
	// Getters
	public ArrayList<CTALine> getallLines(){
		return allLines;
	}
	
	public ArrayList<CTAStation> getAllStations(){
		return allStations;
	}
	
	public ArrayList<CTAStation> getAllConnections(){
		return allConnections;
	}
	
	
	
	// Add station 
	public void addStationToallStations(CTAStation station) {
		allStations.add(station);
	}
	
	// Looks up a station on CTA object with the given name and line color;
	// overrides the analogous method from the CTARoute class.
	public CTAStation lookupStation(String name, String lineColor) {
		CTAStation target = new CTAStation();
		for(CTAStation s : this.getAllStations()) {
			if(s.getName().equalsIgnoreCase(name) &&
					s.lineList().contains(lineColor)) {
				target = s;
			}
		}
		return target;
	}
	
	// Looks up the connection between station1 and station2. Since there could be several 
	// stations that connect, it makes sense to choose the connection closest to 
	// station 1 (take the first transfer you reach)
	public CTAStation lookupConnection(CTAStation station1, CTAStation station2) {
		
		ArrayList<CTAStation> possibleConnections = new ArrayList<CTAStation>();
		
		//CTARoute possibleConnections = new CTARoute(); // make this a CTARoute since we have a nearest station method 
													   // for CTARoutes
		for(CTAStation t : this.getAllConnections()) {
			if(t.isOnSameLine(station1) && t.isOnSameLine(station2)) {
				possibleConnections.add(t);
			}
		}
		ArrayList<Double> distances = new ArrayList<Double>();
		for(CTAStation s : possibleConnections) {
			distances.add(station1.calcDistance(s.getLat(), s.getLng()));
		}
		double min = Double.MAX_VALUE;
		int indexOfMin = 0;
		for(int i = 0; i < distances.size(); i++) {
			if(distances.get(i) < min) {
				min = distances.get(i);
				indexOfMin = i;
			}
		}
		return possibleConnections.get(indexOfMin);
	}
	
	// whichLine : returns an int representing the line on which the whole route belongs 
	// (helper function for a sorter below; gets the index for positionsOnLine() we want to sort over)
	public int whichLine(CTARoute route) {
		int index = 0;
		for(int i = 0; i < this.getallLines().size(); i++) {
			if(this.getallLines().get(i).getLineStops().containsAll(route.getStops())) {
				index = i;
			}
		}
		return index;
	}
	
	// areOnSameLine : returns true if both stations belong to the same line
	public boolean areOnSameLine(CTAStation station1, CTAStation station2) {
		for(CTALine l : this.getallLines()) {
			if(l.getLineStops().contains(station1) && l.getLineStops().contains(station2)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "CTA [allLines=" + allLines + ", allStations=" + allStations + ", allConnections=" + allConnections
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CTA other = (CTA) obj;
		if (allConnections == null) {
			if (other.allConnections != null)
				return false;
		} else if (!allConnections.equals(other.allConnections))
			return false;
		if (allLines == null) {
			if (other.allLines != null)
				return false;
		} else if (!allLines.equals(other.allLines))
			return false;
		if (allStations == null) {
			if (other.allStations != null)
				return false;
		} else if (!allStations.equals(other.allStations))
			return false;
		return true;
	}

	// sourtRoute : sorts the route in reverse order with respect to the line indices
	public void sortRoute(CTARoute route) {
		int index = this.whichLine(route);
		int n = route.getStops().size();
		for(int i = 0; i < n-1; i++) {
			for(int j = 0; j < n - i - 1; j++) {
				if(route.getStops().get(j).getPositionsOnLine().get(index)
						> route.getStops().get(j+1).getPositionsOnLine().get(index)) {
					CTAStation temp = route.getStops().get(j);
					route.getStops().set(j, route.getStops().get(j+1));
					route.getStops().set(j+1, temp);
				}
					
			}
		}
	}
	
	// sortRouteReverse : sorts the route in reverse order with respect to the line indices
	public void sortRouteReverse(CTARoute route) {
		int index = this.whichLine(route);
		int n = route.getStops().size();
		for(int i = 0; i < n-1; i++) {
			for(int j = 0; j < n - i - 1; j++) {
				if(route.getStops().get(j).getPositionsOnLine().get(index)
						< route.getStops().get(j+1).getPositionsOnLine().get(index)) {
					CTAStation temp = route.getStops().get(j);
					route.getStops().set(j, route.getStops().get(j+1));
					route.getStops().set(j+1, temp);
				}
					
			}
		}
	}
	
	
	// addStationEitherEnd : adds a station to either end of a line and updates other positions
	public void addStationEitherEnd(CTAStation stationToAdd, CTAStation stationToAddAt, String lineColor) {
		CTALine line = this.lookupLine(lineColor);
		int index = line.getColorIndex();
		
		int indexOfEndStation = stationToAddAt.getPositionsOnLine().get(index);
		
		ArrayList<Integer> newPositions = new ArrayList<Integer>();
		for(int i = 0; i < 8; i++) { // default newPositions to all be -1
			newPositions.add(-1);
		}
		stationToAdd.setPositionOnLine(newPositions);
		
		if(indexOfEndStation == 0) { // adding to the beginning of line
			line.getLineStops().set(indexOfEndStation, stationToAdd);
			stationToAdd.getPositionsOnLine().set(index, 0);
			for(int i = 1; i < line.getLineStops().size(); i++) {
				line.getLineStops().get(i).getPositionsOnLine().set(index, 
						line.getLineStops().get(i).getPositionsOnLine().get(index) + 1);
			}
		} else if(indexOfEndStation == line.getLineStops().size()-1) { // adding to the end of a line
			line.getLineStops().add(stationToAdd);
			stationToAdd.getPositionsOnLine().set(index, line.getLineStops().size()-1);
		}
	}
	
	// addStationBetween : puts a station between two adjacent stations on a line and updates the other positions
	public void addStationBetween(CTAStation stationToAdd, CTAStation station1, CTAStation station2, String lineColor) {
		CTALine line = this.lookupLine(lineColor);
		int index = line.getColorIndex(); 
		int indexOfFirst = station1.getPositionsOnLine().get(index);
		int indexOfSecond = station2.getPositionsOnLine().get(index);
		
		ArrayList<Integer> newPositions = new ArrayList<Integer>();
		for(int i = 0; i < 8; i++) { // default newPositions to all be -1
			newPositions.add(-1);
		}
		stationToAdd.setPositionOnLine(newPositions);
		
		if(indexOfFirst < indexOfSecond) { //if first station is before second station
			line.getLineStops().add(indexOfSecond, stationToAdd);
			stationToAdd.getPositionsOnLine().set(index, indexOfSecond);
		} else { // if first station comes after second station
			line.getLineStops().add(indexOfFirst, stationToAdd);
			stationToAdd.getPositionsOnLine().set(index, indexOfFirst);
		}
		// Update all the other line positions
		for(int i = line.getLineStops().indexOf(stationToAdd) + 1; i < line.getLineStops().size(); i++) {
				line.getLineStops().get(i).getPositionsOnLine().set(index, 
				line.getLineStops().get(i).getPositionsOnLine().get(index) + 1);
		}
		
	}
	
	// lookupLine : returns the line with the given name
	public CTALine lookupLine(String name) {
		CTALine line = new CTALine();
		for(CTALine l : this.getallLines()) {
			if(l.getLineColor().equalsIgnoreCase(name)) {
				line = l;
			}
		}
		return line;
	}
	
	// makeRoute : makes a route between a start CTAStation and an end CTAStation. 
	// Handles two cases: 
	//	(1) The given stations belong to the same line 
	//	(2)	The given stations do not belong to the same line.
	
	public CTARoute makeRoute(CTAStation start, CTAStation end) {
		CTARoute newRoute = new CTARoute();
		
		if(this.areOnSameLine(start, end)) { 
			for(CTAStation station : this.getAllStations()) {
				
				if(station.isBetween(start, end)) {
					newRoute.addStation(station);
				}
			}
			if(start.getPositionsOnLine().get(getIndexOnLine()) < end.getPositionsOnLine().get(getIndexOnLine())) {
				this.sortRoute(newRoute);
			} else {
				this.sortRouteReverse(newRoute);
			}
		} else {
			CTAStation transferStation = this.lookupConnection(start, end);
			
			CTARoute firstLeg = makeRoute(start, transferStation);
			if(start.getPositionsOnLine().get(getIndexOnLine()) 
					< transferStation.getPositionsOnLine().get(getIndexOnLine())) {
				this.sortRoute(firstLeg);
			} else {
				this.sortRouteReverse(firstLeg);
			}
			CTARoute secondLeg = makeRoute(transferStation, end);
			if(end.getPositionsOnLine().get(getIndexOnLine()) 
					> transferStation.getPositionsOnLine().get(getIndexOnLine())) {
				this.sortRoute(secondLeg);
			} else {
				this.sortRouteReverse(secondLeg);
			}
			
			transferStation.setName(transferStation.getName() + " <- Transfer here");
			
			newRoute = firstLeg.joinRouteTo(secondLeg);
		}
		newRoute.setName(start.getName() + " to " + end.getName());
		return newRoute;
	}
	

}
