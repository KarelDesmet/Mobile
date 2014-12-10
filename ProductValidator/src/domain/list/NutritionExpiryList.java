package domain.list;

import domain.db.NutritionDatabase;


public class NutritionExpiryList extends ExpiryList {

	private static NutritionExpiryList instance = null;
	private NutritionDatabase nutrition;
	
	private NutritionExpiryList(){
		nutrition = NutritionDatabase.getInstance();
	}

	public static NutritionExpiryList getInstance() {
		if(instance == null) {
			instance = new NutritionExpiryList();
		}
		return instance;
	}
	
	public int getCurrentAisleCount(){
		current = articles.size(); //weet niet goed de specifieke bedoeling hier
		return current;
	}
}
