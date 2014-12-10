package domain.db;


public class CharcuterieDatabase extends ProductDatabase {

	private static CharcuterieDatabase instance = null;
	
	private CharcuterieDatabase(){
		//articles downloaden
	}

	public static CharcuterieDatabase getInstance() {
		if(instance == null) {
			instance = new CharcuterieDatabase();
		}
		return instance;
	}
}
