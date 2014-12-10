package domain.db;


public class Database extends CategoryProductDatabase {

	private static Database instance = null;
	
	private Database(){
		//articles downloaden
	}

	public static Database getInstance() {
		if(instance == null) {
			instance = new Database();
		}
		return instance;
	}
}
