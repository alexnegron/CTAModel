/*
 * Alex Negron 5/11/20
 * 
 * CTAStation is a class that makes CTA station objects encoding information relevant to a CTA station. 
 * It inherits its coordinate location from the GeoLocation class. The instance variables include the 
 * station's name, wheelchair accessibility, "location" (i.e., elevated, subway etc), and the station's
 * position on the corresponding Red/Green/Brown/etc lines. This station position is important to the 
 * ordering of the stations. 
 * 
 */
package project;

import java.util.ArrayList;

public class CTAStation extends GeoLocation {
	
	private String name;
	private String location;
	private boolean wheelchair;
	private ArrayList<Integer> positionsOnLine = new ArrayList<Integer>();
	private int indexOnLine;
	
	
	// Constructors: 
	public CTAStation() {
		super();
		name = "Station";
		location = "surface"; // choose default location to be at surface level
		wheelchair = false;
		for(int i=0; i < positionsOnLine.size(); i++) {
			positionsOnLine.set(i, -1); // default position is set to -1, i.e, not on the line
		}
		indexOnLine = -1;
	}
	public CTAStation(String name, double lat, double lng, String location,
						boolean wheelchair, ArrayList<Integer> positionsOnLine) {
		super(lat, lng);	
		this.name = name;
		this.location = location;
		this.wheelchair = wheelchair;
		this.positionsOnLine = positionsOnLine;
	}
	// Setters 
	public void setName(String name) {

		this.name = name;
	}
	public void setLocation(String location) {
		this.location = location;
	}
		
	
	public void setWheelchair(boolean wheelchair) {
		this.wheelchair = wheelchair;
	}
	
	public void setPositionOnLine(ArrayList<Integer> positionOnLine) {
		this.positionsOnLine = positionOnLine;
	}
	
	// Getters 
	
	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public boolean isWheelchair() {
		return wheelchair;
	}
	
	public ArrayList<Integer> getPositionsOnLine(){
		return positionsOnLine;
	}

	public int getIndexOnLine() {
		return indexOnLine;
	}
	public void setIndexOnLine(int indexOnLine) {
		this.indexOnLine = indexOnLine;
	}
	// toString method : prints name and positions on line
	public String toString() {
		return this.getName() + " " + this.getPositionsOnLine();
	}
	
	// printInfo : prints more information in a nice format than toString()
	public String getInfo() {
		String str = "Station name: " + this.getName() + 
					 "\n\tLocation relative to ground: " + this.getLocation() + 
					 "\n\tCoordinates: (" + this.getLat() + ", " + this.getLng() + ")" +
					 "\n\tPositions on lines: " + 
					 	"\n\t\t Red: " + this.getPositionsOnLine().get(0) +
					 	"\n\t\t Green: " + this.getPositionsOnLine().get(1) +
					 	"\n\t\t Blue: " + this.getPositionsOnLine().get(2) +
					 	"\n\t\t Brown: " + this.getPositionsOnLine().get(3) +
					 	"\n\t\t Purple: " + this.getPositionsOnLine().get(4) +
					 	"\n\t\t Pink: " + this.getPositionsOnLine().get(5) + 
					 	"\n\t\t Orange: " + this.getPositionsOnLine().get(6) + 
					 	"\n\t\t Yellow: " + this.getPositionsOnLine().get(7) ;
		return str;
	}
	
	// Equals method
	public boolean equals(Object o) {
		if(!super.equals(o)) {
			return false;
		}
		if(!(o instanceof CTAStation)) {
			return false;
		}
		
		CTAStation station = (CTAStation)o;
		
		// Checks if the station fields are equal
		if(!(name.equalsIgnoreCase(station.getName()))) {
			return false;
		} 

		if(!(location.equalsIgnoreCase(station.getLocation()))) {
			return false;
		}
		
		if(wheelchair != station.isWheelchair()) {
			return false;
		} 
		
		for(int i = 0; i < this.getPositionsOnLine().size(); i++ ) {
			if(this.getPositionsOnLine().get(i) != station.getPositionsOnLine().get(i)) {
				return false;
			}
		}
		return true;
	}	
	
	
	// lineList : makes an array of integers corresponding to the line colors in the file
	public ArrayList<String> lineList(){
		ArrayList<String> lineList = new ArrayList<String>();
		if(this.getPositionsOnLine().get(0) > -1) {
			lineList.add("red");
		} else {
			lineList.add("null");
		}
		if(this.getPositionsOnLine().get(1) > -1) {
			lineList.add("green");
		} else {
			lineList.add("null");
		}
		if(this.getPositionsOnLine().get(2) > -1) {
			lineList.add("blue");
		} else {
			lineList.add("null");
		}
		if(this.getPositionsOnLine().get(3) > -1) {
			lineList.add("brown");
		} else {
			lineList.add("null");
		}
		if(this.getPositionsOnLine().get(4) > -1) {
			lineList.add("purple");
		} else {
			lineList.add("null");
		}
		if(this.getPositionsOnLine().get(5) > -1) {
			lineList.add("pink");
		} else {
			lineList.add("null");
		}
		if(this.getPositionsOnLine().get(6) > -1) {
			lineList.add("orange");
		} else {
			lineList.add("null");
		}
		if(this.getPositionsOnLine().get(7) > -1) {
			lineList.add("yellow");
		} else {
			lineList.add("null");
		}
		return lineList;
	}
	
	// Check if two stations are on the same line or not
		public boolean isOnSameLine(CTAStation station) {
			
			for(int i = 0; i < this.getPositionsOnLine().size(); i++) {
				
				if( (this.getPositionsOnLine().get(i) != -1) && 
						(station.getPositionsOnLine().get(i) != -1) ) {
					return true;
				} 
			}
			return false;
			
		}
	
	// isBetween : tests whether this station is between two other stations
	public boolean isBetween(CTAStation station1, CTAStation station2) {
			if(this.lineList().contains("red") && station1.lineList().contains("red") && station2.lineList().contains("red") ) {
				if(station1.getPositionsOnLine().get(0) < station2.getPositionsOnLine().get(0)) {
					
					if(this.getPositionsOnLine().get(0) > station2.getPositionsOnLine().get(0) || 
							this.getPositionsOnLine().get(0) < station1.getPositionsOnLine().get(0)) {
						return false;
					} else {
						return true;
					}
				} else {
					
					if(this.getPositionsOnLine().get(0) < station2.getPositionsOnLine().get(0) || 
							this.getPositionsOnLine().get(0) > station1.getPositionsOnLine().get(0)) {
						return false;
					} else {
						return true;
					}
				}
			}
			if(this.lineList().contains("green") && station1.lineList().contains("green") && station2.lineList().contains("green") ) {
				if(station1.getPositionsOnLine().get(1) < station2.getPositionsOnLine().get(1)) {
					
					if(this.getPositionsOnLine().get(1) > station2.getPositionsOnLine().get(1) || 
							this.getPositionsOnLine().get(1) < station1.getPositionsOnLine().get(1)) {
						return false;
					} else {
						return true;
					}
				} else {
					
					if(this.getPositionsOnLine().get(1) < station2.getPositionsOnLine().get(1) || 
							this.getPositionsOnLine().get(1) > station1.getPositionsOnLine().get(1)) {
						return false;
					} else {
						return true;
					}
				}
			}
			if(this.lineList().contains("blue") && station1.lineList().contains("blue") && station2.lineList().contains("blue") ) {
				if(station1.getPositionsOnLine().get(2) < station2.getPositionsOnLine().get(2)) {
				
					if(this.getPositionsOnLine().get(2) > station2.getPositionsOnLine().get(2) || 
							this.getPositionsOnLine().get(2) < station1.getPositionsOnLine().get(2)) {
						return false;
					} else {
						return true;
					}
				} else {
					
					if(this.getPositionsOnLine().get(2) < station2.getPositionsOnLine().get(2) || 
							this.getPositionsOnLine().get(2) > station1.getPositionsOnLine().get(2)) {
						return false;
					} else {
						return true;
					}
				}
			}
			if(this.lineList().contains("brown") && station1.lineList().contains("brown") && station2.lineList().contains("brown") ) {
				if(station1.getPositionsOnLine().get(3) < station2.getPositionsOnLine().get(3)) {
				
					if(this.getPositionsOnLine().get(3) > station2.getPositionsOnLine().get(3) || 
							this.getPositionsOnLine().get(3) < station1.getPositionsOnLine().get(3)) {
						return false;
					} else {
						return true;
					}
				} else {
					
					if(this.getPositionsOnLine().get(3) < station2.getPositionsOnLine().get(3) || 
							this.getPositionsOnLine().get(3) > station1.getPositionsOnLine().get(3)) {
						return false;
					} else {
						return true;
					}
				}
			}
			if(this.lineList().contains("purple") && station1.lineList().contains("purple") && station2.lineList().contains("purple") ) {
				if(station1.getPositionsOnLine().get(4) < station2.getPositionsOnLine().get(4)) {
				
					if(this.getPositionsOnLine().get(4) > station2.getPositionsOnLine().get(4) || 
							this.getPositionsOnLine().get(4) < station1.getPositionsOnLine().get(4)) {
						return false;
					} else {
						return true;
					}
				} else {
					
					if(this.getPositionsOnLine().get(4) < station2.getPositionsOnLine().get(4) || 
							this.getPositionsOnLine().get(4) > station1.getPositionsOnLine().get(4)) {
						return false;
					} else {
						return true;
					}
				}
			}
			if(this.lineList().contains("pink") && station1.lineList().contains("pink") && station2.lineList().contains("pink") ) {
				if(station1.getPositionsOnLine().get(5) < station2.getPositionsOnLine().get(5)) {
				
					if(this.getPositionsOnLine().get(5) > station2.getPositionsOnLine().get(5) || 
							this.getPositionsOnLine().get(5) < station1.getPositionsOnLine().get(5)) {
						return false;
					} else {
						return true;
					}
				} else {
					
					if(this.getPositionsOnLine().get(5) < station2.getPositionsOnLine().get(5) || 
							this.getPositionsOnLine().get(5) > station1.getPositionsOnLine().get(5)) {
						return false;
					} else {
						return true;
					}
				}
			}
			if(this.lineList().contains("orange") && station1.lineList().contains("orange") && station2.lineList().contains("orange") ) {
				if(station1.getPositionsOnLine().get(6) < station2.getPositionsOnLine().get(6)) {
				
					if(this.getPositionsOnLine().get(6) > station2.getPositionsOnLine().get(6) || 
							this.getPositionsOnLine().get(6) < station1.getPositionsOnLine().get(6)) {
						return false;
					} else {
						return true;
					}
				} else {
					
					if(this.getPositionsOnLine().get(6) < station2.getPositionsOnLine().get(6) || 
							this.getPositionsOnLine().get(6) > station1.getPositionsOnLine().get(6)) {
						return false;
					} else {
						return true;
					}
				}
			}
			if(this.lineList().contains("yellow") && station1.lineList().contains("yellow") && station2.lineList().contains("yellow") ) {
				if(station1.getPositionsOnLine().get(7) < station2.getPositionsOnLine().get(7)) {
				
					if(this.getPositionsOnLine().get(7) > station2.getPositionsOnLine().get(7) || 
							this.getPositionsOnLine().get(7) < station1.getPositionsOnLine().get(7)) {
						return false;
					} else {
						return true;
					}
				} else {
					
					if(this.getPositionsOnLine().get(7) < station2.getPositionsOnLine().get(7) || 
							this.getPositionsOnLine().get(7) > station1.getPositionsOnLine().get(7)) {
						return false;
					} else {
						return true;
					}
				}
			}
			else {
				return false;
			}
			
		}
		
}
