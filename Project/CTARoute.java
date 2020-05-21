package project;
import java.util.ArrayList;

/*
 * Alex Negron
 * CTARoute is a class encapsulating different lists of CTAStations. This class is 
 * distinguished from CTALine for simplicity, since routes can be user defined 
 * whereas CTA lines are fixed objects in a CTA system
 */

public class CTARoute extends CTAStation{

	private String name;
	private ArrayList<CTAStation> stops;
	private int indexOnLine; 
	
	
	// Constructors 
	
	public CTARoute() {
		setName("");
		setStops( new ArrayList<CTAStation>() ) ;
		setIndexOnLine(0);
	}
	
	public CTARoute(ArrayList<CTAStation> stops) {
		setStops(stops);
	}
	
	public CTARoute(String name, ArrayList<CTAStation> stops) {
		setName(name);
		setStops(stops);
	}
	
	public CTARoute(String name, ArrayList<CTAStation> stops, int indexOnLine) {
		setName(name);
		setStops(stops);
		setIndexOnLine(indexOnLine);
	}
	
	// Setters 
	public void setName (String name) {
		this.name = name;
	}
	
	public void setStops (ArrayList<CTAStation> stops) {
		this.stops = stops;
	}
	
	public void setIndexOnLine(int index) {
		this.indexOnLine = index;
	}
	
	// Getters 
	public String getName() {
		return name;
	}
	
	public ArrayList<CTAStation> getStops(){
		return stops;
	}
	
	public int getIndexOnLine() {
		return indexOnLine;
	}
	
	// Equals method
	
	public boolean equals(Object o) {
		// There could be situations where routes are the same but named differently. 
		// For example, "My Route" might have Granville and Thorndale on it, and so 
		// may "Your Route." Therefore, what distinguishes two CTARoute objects is 
		// the elements of their array lists.
		
		if(!(o instanceof CTARoute) ) {
			return false;
		} 
		
		CTARoute someRoute = (CTARoute) o;
		
		if(this.getStops().size() != someRoute.getStops().size()) {
			return false;
		} else if (!this.getStops().equals(someRoute.getStops())) {
			return false;
		} else {
			return true;
		}
		
	
	}
	
	// to String method

	
	
	public String toString() {
		String str = "";
		for(int i = 0; i < this.getStops().size(); i++) {
			str = str + "\n" + this.getStops().get(i).getName();
		}
		return "Route name: " + "\"" + this.getName() +".\"" + " The stops in this route are: " 
				+ str;
	}
	
	
	
	// is this CTA station on the route? 
	public boolean isOnRoute(CTAStation station) {
		for (CTAStation s : stops) {
			if(s.equals(station)) {
				return true;
			} 
		}
		return false;
	}
	
	// is this CTA station at the end of the line?
	public boolean isEndpoint(CTAStation station) {
		int pos = station.getPositionsOnLine().get(this.getIndexOnLine());
		if(pos == 0 || pos == stops.size() -1) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
	// Add station methods
	
	
	public void addStation(CTAStation station) {
		this.getStops().add(station); // adds a station to the array list
	}
	
	
	// Remove a station: 
	public void removeStation(CTAStation station) {
		this.getStops().remove(station);
	}
	
	// Insert a station at a specified index:
	public void insertStation(int position, CTAStation station) {
		this.getStops().add(position, station);
	}
	
	// Link two CTARoutes (does not order)
	
	public CTARoute joinRouteTo(CTARoute route) {
		for(int i = 1; i < route.getStops().size(); i++) {
			this.getStops().add(route.getStops().get(i));
		}
		return this;
	}
	
	
	

	
	
	
	
	// Return the CTAStation with the specified name: 
	public CTAStation lookupStation(String name) {
		CTAStation targetStation = new CTAStation(); // default CTAStation, this is the one we're searching for
		targetStation.setName(name);
		
		// loop through each position in the ArrayList
		
		 for(int i = 0; i < this.getStops().size(); i++) {
			 
			 String str = this.getStops().get(i).getName();
			 
			 // If the name of the CTAStation at pos i equals name, we've found the targetStation
			 if(targetStation.getName().equalsIgnoreCase(str)) {
				  targetStation = this.getStops().get(i);
			 } 
		 } 
		 // Return that targetStation
		 return targetStation;
	}
	
	// Nearest station method (1): takes a (lat, lng) and returns the station
	// in the ArrayList "stops" that is closest:
	
	public CTAStation nearestStation(double lat, double lng) {
		double min = Double.MAX_VALUE; // huge double 
		
		// We need to make an array of distances from user's current
		// location to each of the stations in the route: 
		double[] Distances = new double[stops.size()];
		
		// We also make a GeoLocation to represent the location of the user 
		// at the given lat and lng: 
		GeoLocation userLoc = new GeoLocation(lat, lng);
		
		// Fill the Distances array with the distances to each station in the route: 
		for (int i = 0; i < Distances.length; i++) {
			double distance = userLoc.calcDistance(
					// lat of station at route index i 
					stops.get(i).getLat(), 
					
					// lng of station at route index i
					stops.get(i).getLng() );
			
			Distances[i] = distance; 
		}
		
		// Now, Distances[] is filled with distances, so we need to now
		// compute the minimum over each element in the array:
		
		int indexOfMin = 0; // This holds the position of the min element, which we find now:
		
		for(int i = 0; i < Distances.length; i++) {
			if(Distances[i] < min) {
				min = Distances[i]; // this is the min distance
				indexOfMin = i; //corresponds to the position in the arraylist where the closest station lies
			}
		}
		return stops.get(indexOfMin);			
	}
	
	// Nearest station method (2): takes a GeoLocation and returns the station in 
	// this.getStops() that is closest. 
	public CTAStation nearestStation(GeoLocation geoLoc) {
		
		double min = Double.MAX_VALUE;
		
		double[] Distances = new double[this.getStops().size()];
		
		for(int i = 0; i < Distances.length; i++) {
			
			double distance = geoLoc.calcDistance(
					
					this.getStops().get(i).getLat(),
					
					this.getStops().get(i).getLng()
					
					);
			
			Distances[i] = distance;
					
		}
		int indexOfMin = 0;
		
		for(int i = 0; i < Distances.length; i++) {
			
			if(Distances[i] < min) {
				min = Distances[i];
				indexOfMin = i;
			}
		}
		return this.getStops().get(indexOfMin);
	}
	
	
	
}
