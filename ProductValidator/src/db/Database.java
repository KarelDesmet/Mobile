package db;

import java.util.HashMap;
import java.util.Map;

import domain.Product;
import exception.db.DatabaseException;

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
	 * categoryProductDatabase with it's corresponding category. An exception
	 * occurs when a product exist in that category with that EAN. The check
	 * happens at those levels and not here in order to allow initial errors of
	 * category to be corrected at a later time without aborting the process.
	 * 
	 * @param category
	 *            The category of the product
	 * @param article
	 *            The article to be added to the database
	 * @throws DatabaseException
	 *             If there is already a product in the CategoryProductDatabase
	 *             with this EAN. I.e. the DomainException from the
	 *             CategoryProductDatabase is caught and thrown again.
	 */
	public void addProduct(String category, Product article)
			throws DatabaseException {
		CategoryProductDatabase categoryProductDb = categoryProductDatabases
				.get(category);
		categoryProductDb.addProduct(article);
	}

	/**
	 * A method which returns the product with the given EAN from the given
	 * category.
	 * 
	 * @param category
	 *            The category of the product, where it should be
	 * @param ean
	 *            The EAN of the product
	 * @return The product with the given EAN from the given category
	 * @throws DatabaseException
	 *             If there is no product in the category database with this
	 *             EAN. I.e. there is no key in the HashMap with the value of
	 *             the EAN.
	 */
	public Product getProduct(String category, Long ean)
			throws DatabaseException {
		CategoryProductDatabase categoryProductDb = categoryProductDatabases
				.get(category);
		return categoryProductDb.getProduct(ean);
	}

	public void deleteProduct(String category, Long ean) {

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
