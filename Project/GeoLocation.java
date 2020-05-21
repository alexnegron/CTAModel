package project;
/*
 * Alex Negron
 * 
 * GeoLoation is a class that makes location objects that encode information concerning coordinate
 * locations given by a latitude and a longitude double. It also includes methods for calculating 
 * distances based on geo locations and lat/longs.
 * 
 */

public class GeoLocation {

	
	// Instance Variables
	private double lat;
	private double lng;
	
	// Constructors 
	public GeoLocation() {
		this.lat = 0.0;
		this.lng = 0.0;
	}
	
	public GeoLocation(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	// Getters 
	public double getLat() {
		return lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	// Setters
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	// toString method
	public String toString() {
		return("(" + lat + ", " + lng +")");
	}
	
	// Equals method
	public boolean equals(Object o) {
		if( !(o instanceof GeoLocation) ) {
			return false;
		}
		
		GeoLocation geoLocation = (GeoLocation)o;
		
		if( this.getLat() != geoLocation.getLat() ) {
			return false;
		} else if( this.getLng() != geoLocation.getLng() ) {
			return false;
		} else {
			return true;
		}
	}
	
	// (1) Takes a GeoLocation and calculates the distance to it 
	public double calcDistance(GeoLocation geoLoc) {
		double distance = 
				Math.sqrt(
					Math.pow(geoLoc.lat - this.lat, 2)
					+
					Math.pow(geoLoc.lng - this.lng ,2)
				);
		return distance;
	}
	
	// (2) Takes a coorindate location in latitude and longitude and calculates the distance to it 
	public double calcDistance(double lat, double lng) {
		double distance = 
				Math.sqrt(
					Math.pow(lat - this.lat, 2)
					+
					Math.pow(lng - this.lng ,2)
			);
	return distance;
	}
}
