package domain.db;


public class NutritionDatabase extends ProductDatabase {

	private static NutritionDatabase instance = null;
	
	private NutritionDatabase(){
		//articles downloaden
	}

	public static NutritionDatabase getInstance() {
		if(instance == null) {
			instance = new NutritionDatabase();
		}
		return instance;
	}
}
