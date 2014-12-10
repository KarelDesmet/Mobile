package domain.db;

import java.util.HashMap;
import java.util.Map;

import domain.Product;


public class Database{

	private static Database instance = null;
	private Map<String, CategoryProductDatabase> categoryProductDatabases = new HashMap<String, CategoryProductDatabase>();
	
	private Database(){
		
	}

	public static Database getInstance() {
		if(instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
	public void addProduct(String category, Product article){
		
	}
	
	public void deleteProduct(String category, Long ean){
		
	}
	
	public Product getProduct(String category, Long ean){
		return null;
	}
	
	public void addCategoryProductDatabase(String category){
		
	}
}
