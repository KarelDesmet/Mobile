package domain.db;


public class FreezerDatabase extends ProductDatabase {

	private static FreezerDatabase instance = null;
	
	private FreezerDatabase(){
		//articles downloaden
	}

	public static FreezerDatabase getInstance() {
		if(instance == null) {
			instance = new FreezerDatabase();
		}
		return instance;
	}
}
