package domain.list;

import domain.db.DiaryDatabase;


public class DiaryExpiryList extends ExpiryList {

	private static DiaryExpiryList instance = null;
	private DiaryDatabase diary;
	
	private DiaryExpiryList(){
		diary = DiaryDatabase.getInstance();
	}

	public static DiaryExpiryList getInstance() {
		if(instance == null) {
			instance = new DiaryExpiryList();
		}
		return instance;
	}
	
	public int getCurrentFridgeCount(){
		current = articles.size(); //weet niet goed de specifieke bedoeling hier
		return current;
	}
}
