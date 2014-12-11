package db;

import java.util.HashMap;
import java.util.Map;

import domain.Product;

public class Database {

	private static Database _instance = null;
	private Map<String, CategoryProductDatabase> categoryProductDatabases = new HashMap<String, CategoryProductDatabase>();

	private Database() {
		categoryProductDatabases.put("charcuterie",
				new CategoryProductDatabase());
		categoryProductDatabases
				.put("diepvries", new CategoryProductDatabase());
		categoryProductDatabases.put("kaas", new CategoryProductDatabase());
		categoryProductDatabases.put("voeding", new CategoryProductDatabase());
		categoryProductDatabases.put("zuivel", new CategoryProductDatabase());
	}

	private synchronized static void createInstance() {
		if (_instance == null) {
			_instance = new Database();
		}
	}

	public static Database getInstance() {
		if (_instance == null) {
			createInstance();
		}
		return _instance;
	}

	/**
	 * A method which returns the total amount of products in the database. I.e.
	 * the sum of the amount of products in all the different
	 * categoryProductDatabases.
	 * 
	 * @return The total amount of products in the database.
	 */
	public int size() {
		int size = 0;
		for (String key : categoryProductDatabases.keySet()) {
			size += categoryProductDatabases.get(key).size();
		}
		return size;
	}

	/**
	 * A method which adds a given product to the database. It is stored in the
	 * categoryProductDatabase with it's corresponding category.
	 * 
	 * @param category
	 *            The category of the product
	 * @param article
	 *            The article to be added to the database
	 */
	public void addProduct(String category, Product article) {
		CategoryProductDatabase categoryProductDb = categoryProductDatabases.get(category);
		categoryProductDb.addProduct(article);
	}

	public void deleteProduct(String category, Long ean) {

	}

	public Product getProduct(String category, Long ean) {
		return null;
	}

	public void addCategoryProductDatabase(String category) {

	}

	/**
	 * This method implements how a Database is represented as a String. I.e.
	 * all the categories (=keys of the Map) of the Database and the contents of
	 * those categoryProductDatabase.
	 * 
	 * @return The String representation of a Database
	 */
	@Override
	public String toString() {
		String result = "";
		for (String key : categoryProductDatabases.keySet()) {
			result += key.toUpperCase() + "\n";
			result += categoryProductDatabases.get(key) + "\n";
		}
		return result;
	}
}
