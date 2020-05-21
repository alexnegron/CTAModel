/*
 * Alex Negron 5/11/2020
 */

/*
 * CTA_Application : Main application class. Includes the user menu options, allowing the user to make choices 
 * about what they want to do at each step. This class also features several methods to assist in running the 
 * menu. These methods implement methods written in other classes.
 */

package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CTA_Application extends CTA{

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String inputFile = "/Users/alexnegron/git/cs201-202001-anegron1/src/project/CTAStops.csv";
		// Read from file:
		ArrayList<CTAStation> allStations = retrieveStations(inputFile);
		ArrayList<CTALine> allLines = retrieveLines(allStations, inputFile);
		ArrayList<CTAStation> allConnections = retrieveConnections(allStations);
		// Build CTA system: 
		CTA cta = new CTA(allLines, allStations, allConnections);
		
		
		// Menu 
		boolean quit = false;
		do {
			System.out.println("Choose from the following: "
					+ "\n1. Create new station and add it to the CTA."
					+ "\n2. Delete station from the CTA."
					+ "\n3. Modify existing station."
					+ "\n4. Search for a station by name." 
					+ "\n5. Find the nearest station to a location."
					+ "\n6. Make a route between two stations."
					+ "\n7. Exit."
					);
			
			char ch;
			
			try {
				ch = input.nextLine().charAt(0);
			} catch(Exception e) {
				ch = ' ';
			}
			
			switch(ch) {
			case '1':
				create_and_Add_Station_To_Line(cta, input);
				break;
			case '2':
				delete_Station_From_CTA(cta, input);
				break;
			case '3':
				modify_Station_On_CTA(cta, input);
				break;
			case '4':
				search_For_Station_By_Name(cta, input);
				break;
			case '5':
				search_For_Nearest_Station(cta, input);
				break;
			case '6':
				make_Route_Between_Stations(cta, input);
				break;
			case '7':
				System.out.println("Done.");
				quit = true;
				break;
			}
		} while(!quit);
		input.close();
		
	}
	//--------------------------------------------------------------------------------------------------
	// The methods below are used to read data from input file and perform the tasks in the menu. 
	//--------------------------------------------------------------------------------------------------
	
	// Read all the stations from a CSV
	public static ArrayList<CTAStation> retrieveStations(String fileName){
		boolean isFound = false;
		do {
			
			try {
				File file = new File(fileName);
				Scanner scan = new Scanner(file);
				
				ArrayList<CTAStation> ctaStations = new ArrayList<CTAStation>();
				
				while(scan.hasNextLine()) {
					String thisLine = scan.nextLine();
					String[] arr = thisLine.split(",");
					if( !(arr[0].equalsIgnoreCase("name")
							|| (arr[0].equalsIgnoreCase("null") ) ) ) {
						ArrayList<Integer> ctaLines = new ArrayList<Integer>();
						for(int i = 0; i < arr.length-5;i++) {
							String ctaLineIndex = arr[5+i];
							ctaLines.add(Integer.parseInt(ctaLineIndex));
						}
						CTAStation thisStation = new CTAStation(
								arr[0], //name
								Double.parseDouble(arr[1]), //lat
								Double.parseDouble(arr[2]), //lng
								arr[3], //location
								Boolean.parseBoolean(arr[4]), //wheelchair
								ctaLines //positions on cta lines
								);
						ctaStations.add(thisStation);
					}
				}
				scan.close();
				return ctaStations;	
			} catch(FileNotFoundException e) {
				System.out.println("File not found.");
			}		
		} while(!isFound);
		return new ArrayList<CTAStation>();
	}
	
	// Read all the lines from a CSV
	public static ArrayList<CTALine> retrieveLines(ArrayList<CTAStation> allStations, String fileName){
		try {
			File inputFile = new File(fileName);
			Scanner scan = new Scanner(inputFile);
			
			ArrayList<CTALine> allLines = new ArrayList<CTALine>();
			
			int lineCounter = 0;
			
			while(scan.hasNextLine()) {
				String[] lineArray = scan.nextLine().split(",");
				if(lineCounter == 0) {
					
					for(int i = 0; i < lineArray.length - 5; i++) {
						
						allLines.add(new CTALine()); // put something in it so we can use the getters
						allLines.get(i).setColorIndex(i);
						allLines.get(i).setLineColor(lineArray[5+i].split(":")[0]);
						
						
					}
				}
				if(lineCounter == 1) {
					for(int i = 0; i < lineArray.length - 5; i++) {
						if(!lineArray[5+i].equals("Null")) {
							allLines.get(i).setFinalTwoWayStop(allLines.get(i).lookupStation(lineArray[5+i]));
						}
					}
				}
				lineCounter++;
			}
			
			for(int i = 0; i < allLines.size(); i++) {
				CTARoute stops = new CTARoute();
				for(CTAStation s : allStations) {
					if(s.getPositionsOnLine().get(i) != -1) {
						stops.addStation(s);
					}
				}
				//stops.stationSort();
				allLines.get(i).setLineStops(stops.getStops());
				sortLine(allLines.get(i));
			}
			scan.close();
			return allLines;
		} catch(FileNotFoundException e) {
			System.out.println("File not found.");
			return new ArrayList<CTALine>();
		}
	}
	
	// Retrieve all the connection stations 
	public static ArrayList<CTAStation> retrieveConnections(ArrayList<CTAStation> allStations){
		ArrayList<CTAStation> connections = new ArrayList<CTAStation>();
		
		for(CTAStation s : allStations) {
			int count = 0;
			for(int i = 0; i < s.getPositionsOnLine().size(); i++) {
				if(s.getPositionsOnLine().get(i) != -1) {
					count++;
				}
			}
			if(count > 1) {
			connections.add(s);
			}
		}
		return connections;
	}
	
	// Get string from user
	public static String userString(Scanner input) {
		boolean isValid = false;
		
		do {
			try {
				String str = input.nextLine();
				return str;
			} catch(Exception e) {
				System.out.println("Invalid input.");
			}
		} while(!isValid);
		return null;
	}
	
	// Get yes/no answer from the user and make it a boolean
	public static boolean userBoolean(Scanner input) {
		boolean isValid = false;
		do {
			try {
				char ans = input.nextLine().charAt(0);
				
				switch(Character.toLowerCase(ans)) {
				
				case 'y':
					return true;
					
				case 'n':
					return false;
				default:
					System.out.println("Invalid input.");
					break;
				}
			} catch(Exception e) {
				System.out.println("Invalid input.");
			}
		} while(!isValid);
		return false;
	}
	
	// Get a double from the user
	public static double userDouble(Scanner input) {
		boolean bool = false;
		do {
			try {
				double dbl = input.nextDouble();
				return dbl;
			} catch(Exception e) {
				input.nextLine();
				System.out.println("Invalid input.");
			}
		} while(!bool);
		return 0.0;
	}
	
	// Create new station and add to CTA  : 
	public static void create_and_Add_Station_To_Line(CTA cta, Scanner input) {
		CTAStation stationToAdd = new CTAStation();
		System.out.println("Enter the name of the station."); 
		String newStationName = userString(input);	// name
		System.out.println("Enter " + newStationName + "'s latitude.");
		double newLat = userDouble(input); // latitude 
		System.out.println("Enter " + newStationName + "'s longitude.");
		double newLng = userDouble(input); // longitude
		input.nextLine();
		System.out.println("Enter " + newStationName + "'s 'location' (track location).");
		String newLocation = userString(input); //location
		System.out.println("Does " + newStationName + " have wheelchair access? (y/n)");
		boolean newWheelchair = userBoolean(input); //wheelchair
		
		// now we have to figure out the positionsOnLine list 
		System.out.println("Which line is " + newStationName + " on?");
		String lineColor = userString(input);
		CTALine lineToAddOnto = cta.lookupLine(lineColor);
		
		stationToAdd.setName(newStationName);		// set all the fields gotten from the user so far
		stationToAdd.setLat(newLat);				// just need to update the positions array now
		stationToAdd.setLng(newLng);
		stationToAdd.setLocation(newLocation);
		stationToAdd.setWheelchair(newWheelchair);
		
		System.out.println("Is " + newStationName + " to be added at either end of the " 
								+  lineColor + " line? (y/n)");
		boolean isInteriorOfLine = userBoolean(input);
		
		if(!isInteriorOfLine) {
			System.out.println("Between which stations do you want to add " + newStationName 
					+ " on the " + lineColor + " line? (They must be adjacent stations.)" 
					+ "\nEnter the first station: ");
			String station1Name = userString(input);
			
			System.out.println("Enter the second station: ");
			String station2Name = userString(input);
			
			CTAStation station1 = cta.lookupStation(station1Name, lineColor);
			CTAStation station2 = cta.lookupStation(station2Name, lineColor);
			
			cta.addStationBetween(stationToAdd, station1, station2, lineColor);
			
			System.out.println("The CTA system has successfully been updated with your new station." +"\n");
			//break;
		} else {
		
			System.out.println("To which station do you want to add " + newStationName 
							+"? You can add it to either: "+ "\n"+
							lineToAddOnto.getLineStops().get(0).getName() + " (the start of the " 
							+ lineColor + " line)" + "\t or \t"
							+ lineToAddOnto.getLineStops().get(lineToAddOnto.getLineStops().size()-1).getName()
							+ " (the end of the " 
							+ lineColor + " line).");
			String stationToAddOntoName = userString(input);
			CTAStation stationToAddOnto = cta.lookupStation(stationToAddOntoName, lineColor);
			cta.addStationEitherEnd(stationToAdd, stationToAddOnto, lineColor);
			System.out.println("The CTA system has successfully been updated with your new station." +"\n");
		}
	}
	
	// Delete station from the CTA
	public static void delete_Station_From_CTA(CTA cta, Scanner input) {
		System.out.println("What is the name of the station you want to delete?");
		String station = userString(input);
		System.out.println("What is the name of the line from which you want to delete station " 
				+ station + "?");
		String line = userString(input);
		
		CTAStation stationToDelete = cta.lookupStation(station, line);
		CTALine lineToRemoveFrom = cta.lookupLine(line);
		
		lineToRemoveFrom.removeStation(stationToDelete);
		System.out.println(lineToRemoveFrom);
	}
	
	// Modify station on the CTA
	public static void modify_Station_On_CTA(CTA cta, Scanner input) {
		System.out.println("What is the name of the station you want to modify?");
		String station = userString(input);
		System.out.println("What line is this station on?");
		String line = userString(input);
		CTAStation stationToModify = cta.lookupStation(station, line);
		boolean stop = false;
		do {
			System.out.println("Which field do you want to modify? "
					+ "\n1. " + stationToModify.getName() + "'s name."
					+ "\n2. " + stationToModify.getName() + "'s latitude."
					+ "\n3. " + stationToModify.getName() + "'s longitude."
					+ "\n4. " + stationToModify.getName() + "'s wheelchair accessibility."
					+ "\n5. " + stationToModify.getName() + "'s positions on lines."
					+ "\n6. Done with modifications.");
			char ch;
			try {
				ch = input.nextLine().charAt(0);
			} catch(Exception e) {
				ch = ' ';
			}
			switch(ch) {
			case '1':
				System.out.println("Enter the new name: ");
				String newName = userString(input);
				stationToModify.setName(newName);
				break;
			case '2':
				System.out.println("Sorry, we cannot pick up stations and move them!");
				break;
			case '3':
				System.out.println("Sorry, we cannot pick up stations and move them!");
				break;
			case '4':
				System.out.println("Do you want the new station to be wheelchair accessible? (y/n)");
				boolean wheelchair = userBoolean(input);
				stationToModify.setWheelchair(wheelchair);
				break;
			case '5':
				System.out.println("Sorry, we cannot pick up stations and move them!");
			case '6':
				stop = true;
				break;
			}
		} while(!stop);
	}
	
	// Search for a station by name
	public static void search_For_Station_By_Name(CTA cta, Scanner input) {
		System.out.println("Enter the name of the station you want: ");
		String targetStationName = userString(input);
		CTAStation targetStation = new CTAStation();
		ArrayList<CTAStation> stationsWithTargetName = new ArrayList<CTAStation>();
		for(CTAStation s : cta.getAllStations()) {
			if(s.getName().equalsIgnoreCase(targetStationName)) {
				stationsWithTargetName.add(s);
			}
		}
		if(stationsWithTargetName.size() == 1) {
			targetStation = stationsWithTargetName.get(0);
			System.out.println(targetStation.getInfo());
		}else {
			System.out.println("Multiple stations have that name. These stations are: ");
			for(int i = 0; i < stationsWithTargetName.size(); i++) {
				System.out.println( "(" + (i+1) + ")" + stationsWithTargetName.get(i).getInfo());
			}
		}
	}
	
	// Search for station nearest to a location
	public static void search_For_Nearest_Station(CTA cta, Scanner input) {
		System.out.println("Enter the latitude: ");
		double lat = userDouble(input);
		
		System.out.println("Enter the longitude: ");
		double lng = userDouble(input);
		
		CTARoute allStations = new CTARoute();
		allStations.setStops(cta.getAllStations());
		CTAStation nearestStation = allStations.nearestStation(lat, lng);
		System.out.println("The station nearest to this location is: " 
		+ "\n" + nearestStation.getInfo());
		
	}
	// Make a route between two stations
	public static void make_Route_Between_Stations(CTA cta, Scanner input) {
		System.out.println("Enter the name of the station you want to start at: ");
		String startStation = userString(input);
		System.out.println("Enter the line color of the start station: ");
		String startLine = userString(input);
		System.out.println("Enter the name of the station you want to end at: ");
		String endStation = userString(input);
		System.out.println("Enter the line color of the end station:");
		String endLine = userString(input);
		
		CTAStation start = cta.lookupStation(startStation, startLine);
		CTAStation end = cta.lookupStation(endStation, endLine);
		
		CTARoute route = cta.makeRoute(start,end);
		
		System.out.println(route + "\n");
	}
}
		
