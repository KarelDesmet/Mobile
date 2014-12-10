package domain.list;

import domain.db.CheeseDatabase;


public class CheeseExpiryList extends ExpiryList {

	private static CheeseExpiryList instance = null;
	private CheeseDatabase cheese;
	
	private CheeseExpiryList(){
		cheese = CheeseDatabase.getInstance();
	}

	public static CheeseExpiryList getInstance() {
		if(instance == null) {
			instance = new CheeseExpiryList();
		}
		return instance;
	}
	
	public int getCurrentFridgeCount(){
		current = articles.size(); //weet niet goed de specifieke bedoeling hier
		return current;
	}
}
