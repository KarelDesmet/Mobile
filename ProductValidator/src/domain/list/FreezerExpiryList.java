package domain.list;

import domain.db.FreezerDatabase;


public class FreezerExpiryList extends ExpiryList {

	private static FreezerExpiryList instance = null;
	private FreezerDatabase freezer;
	
	private FreezerExpiryList(){
		freezer = FreezerDatabase.getInstance();
	}

	public static FreezerExpiryList getInstance() {
		if(instance == null) {
			instance = new FreezerExpiryList();
		}
		return instance;
	}

	public int getCurrentFreezerCount(){
		current = articles.size(); //weet niet goed de specifieke bedoeling hier
		return current;
	}
}
