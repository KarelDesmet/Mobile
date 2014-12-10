package domain.list;

import domain.db.Database;


public class ExpiryList extends CategoryExpiryList {

	private static ExpiryList instance = null;
	private Database charcuterie;
	
	private ExpiryList(){
		charcuterie = Database.getInstance();
	}

	public static ExpiryList getInstance() {
		if(instance == null) {
			instance = new ExpiryList();
		}
		return instance;
	}

	public int getCurrentFridgeCount(){
		current = articles.size(); //weet niet goed de specifieke bedoeling hier
		return current;
	}
}
