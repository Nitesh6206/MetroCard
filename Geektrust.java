import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

 class MetroStation{
	String name;
	int totaldiscount;
	int totalCollection;
	int Cardbalance;
	Map<String,Integer> passengerType;
	

	public MetroStation(String name){
		this.name=name;
		passengerType=new HashMap<>();
		passengerType.put("ADULT",0);
		passengerType.put("SENIOR_CITIZEN",0);
		passengerType.put("KID",0);
		totalCollection=0;
		totaldiscount=0;
		Cardbalance=0;

		
	}
	public int checkIn(String cardNumber, int balance, String passengertype, String Station,ArrayList<String>centralStation,ArrayList<String>airportStation) {
	    int TravelCharge = getTravelCharge(passengertype);
	    passengerType.put(passengertype, passengerType.get(passengertype) + 1);
	    int amount = 0;
	    Cardbalance = balance;

	    if (Station.equals("AIRPORT") ) {
			if(centralStation.contains(cardNumber)){
	        TravelCharge = (int)(TravelCharge * 0.5);
	        centralStation.remove(cardNumber);
	        // totalCollection += TravelCharge;
	        totaldiscount += TravelCharge;
	    } else{
			airportStation.add(cardNumber);
			// totalCollection+=TravelCharge;
		}
	}
		 if (Station.equals("CENTRAL") ) {
			if(airportStation.contains(cardNumber)){
	        TravelCharge = (int)(TravelCharge * 0.5);
	        airportStation.remove(cardNumber);  // Corrected line
	        // totalCollection += TravelCharge;
	        totaldiscount += TravelCharge;
	    } else  {
	       centralStation.add(cardNumber);
			// totalCollection+=TravelCharge; 
	    }
	} 

	    if (Cardbalance < TravelCharge) {
	        amount = TravelCharge - Cardbalance;
	        int serviceFee = (int)(amount * 0.02);
	        Cardbalance = 0;
	        totalCollection += serviceFee;
	    } else {
	        Cardbalance -=  TravelCharge;
			
	    }
		totalCollection+=TravelCharge;
		return Cardbalance;
	}


	 private int getTravelCharge(String passengerType) {
	        switch (passengerType) {
	            case "ADULT":
	                return 200;
	            case "SENIOR_CITIZEN":
	                return 100;
	            case "KID":
	                return 50;
	            default:
	                return 0;
	        }
	    }


}
public class Geektrust{
	public static ArrayList<String> centralStation=new ArrayList<>();
	public static ArrayList<String> airportStation=new ArrayList<>();
	public static void main(String[] agrs){
		MetroStation central=new MetroStation("CENTRAL");
		MetroStation airport=new MetroStation("AIRPORT");
		Map<String,Integer> cardInfo=new HashMap<>();
		try{

			File file = new File("input.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            Scanner sc = new Scanner(fileInputStream);


			while(sc.hasNextLine())
            	{
	  			String line=sc.nextLine();
					String[] commandParts = line.trim().split("\\s+");
	    			// String cmd=commandParts[0];
					// String cardNum=commandParts.length>1?commandParts[1]:"";
	    			// System.out.println(cmd+" card num="+cardNum);
					switch(commandParts[0]){
						case "BALANCE":
							String cardNumber=commandParts[1];
							int balance=Integer.parseInt(commandParts[2]);
//							MetroCard card=new MetroCard(cardNumber,balance);
							cardInfo.put(cardNumber,balance);
							break;
						case "CHECK_IN":
							 cardNumber = commandParts[1];
							   String PassengerType = commandParts[2];
							   String Station = commandParts[3];
							    Integer cardBalance = cardInfo.get(cardNumber);

							    if (cardBalance != null) {
							        balance = cardBalance.intValue();

							        if (commandParts[3].equals("CENTRAL")) {
							            int cb=central.checkIn(cardNumber, balance, PassengerType, Station,centralStation,airportStation);
										cardInfo.put(cardNumber,cb);
							        } else {
							            int cb=airport.checkIn(cardNumber, balance, PassengerType, Station,centralStation,airportStation);
										cardInfo.put(cardNumber,cb);
							        }
							    } else {
							        System.out.println("Error: Card information not found for card number " + cardNumber);
							    }
							    break;
						case "PRINT_SUMMARY":
							printSummary(central, airport);
                     		break;

				}
			}
		sc.close();
	}catch (FileNotFoundException e) {
        System.out.println("file error");
    //    e.printStackTrace();
        }
}
		 private static void printSummary(MetroStation centralStation, MetroStation airportStation) {
        System.out.println("TOTAL_COLLECTION CENTRAL " + centralStation.totalCollection + " " + centralStation.totaldiscount);
        printPassengerSummary(centralStation.passengerType);

        System.out.println("TOTAL_COLLECTION AIRPORT " + airportStation.totalCollection + " " + airportStation.totaldiscount);
        printPassengerSummary(airportStation.passengerType);
    }

    private static void printPassengerSummary(Map<String, Integer> passengerTypes) {
		System.out.println("PASSENGER_TYPE_SUMMARY");
        passengerTypes.entrySet().stream()
				.sorted((entry1, entry2) -> {
					if (entry1.getValue().equals(entry2.getValue())) {
						return entry1.getKey().compareTo(entry2.getKey());
					}
					return entry2.getValue().compareTo(entry1.getValue());
				})
				.forEach(entry -> {
					if (entry.getValue() != 0) {
						System.out.println(entry.getKey() + " " + entry.getValue());}});

    }

}