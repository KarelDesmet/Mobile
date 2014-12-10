package domain.list;

import domain.db.CharcuterieDatabase;


public class CharcuterieExpiryList extends ExpiryList {

	private static CharcuterieExpiryList instance = null;
	private CharcuterieDatabase charcuterie;
	
	private CharcuterieExpiryList(){
		charcuterie = CharcuterieDatabase.getInstance();
	}

	public static CharcuterieExpiryList getInstance() {
		if(instance == null) {
			instance = new CharcuterieExpiryList();
		}
		return instance;
	}

	public int getCurrentFridgeCount(){
		current = articles.size(); //weet niet goed de specifieke bedoeling hier
		return current;
	}
}
