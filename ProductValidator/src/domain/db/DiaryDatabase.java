package domain.db;


public class DiaryDatabase extends ProductDatabase {

	private static DiaryDatabase instance = null;
	
	private DiaryDatabase(){
		//articles downloaden
	}

	public static DiaryDatabase getInstance() {
		if(instance == null) {
			instance = new DiaryDatabase();
		}
		return instance;
	}
}
