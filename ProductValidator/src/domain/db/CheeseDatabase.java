package domain.db;


public class CheeseDatabase extends ProductDatabase {

	private static CheeseDatabase instance = null;
	
	private CheeseDatabase(){
		//articles downloaden
	}

	public static CheeseDatabase getInstance() {
		if(instance == null) {
			instance = new CheeseDatabase();
		}
		return instance;
	}
}
