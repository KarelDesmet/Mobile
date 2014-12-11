package db;

import java.util.HashMap;
import java.util.Map;

import domain.Product;
import exception.db.DatabaseException;
import exception.service.ServiceException;

/**
 * A database class which acts as the container for the different databases for
 * each category. The database is split in different parts so that it provides
 * faster access to a certain product.
 * 
 * @author Pieter Declercq
 */
public class Database {

	/**
	 * The one and only instance of this database.
	 */
	private static Database _instance;

	/**
	 * The database who holds all different categories and their respective
	 * databases together.
	 */
	private Map<String, CategoryProductDatabase> categoryProductDatabases;

	/**
	 * Private constructor to prevent others creating an instance.
	 */
	private Database() {
		categoryProductDatabases = new HashMap<String, CategoryProductDatabase>();
		categoryProductDatabases.put("charcuterie",
				new CategoryProductDatabase());
		categoryProductDatabases
				.put("diepvries", new CategoryProductDatabase());
		categoryProductDatabases.put("kaas", new CategoryProductDatabase());
		categoryProductDatabases.put("voeding", new CategoryProductDatabase());
		categoryProductDatabases.put("zuivel", new CategoryProductDatabase());
	}

	/**
	 * Synchronised creator method to prevent multi-threading problems.
	 */
	private synchronized static void createInstance() {
		if (_instance == null) {
			_instance = new Database();
		}
	}

	/**
	 * The only way to access the instance of the this class.
	 * 
	 * @return The database of all known products
	 */
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
	 * A method which gets the categoryProductDatabase which corresponds with
	 * the given category.
	 * 
	 * @param category
	 *            The categoryProductDatabase requested
	 * @return The categoryProductDatabase
	 * @throws DatabaseException
	 *             If there is no corresponding CategoryProductDatabase for the
	 *             requested category. I.e. there is no key for that category.
	 */
	public CategoryProductDatabase getCategory(String category)
			throws DatabaseException {
		if (!categoryProductDatabases.containsKey(category)) {
			throw new DatabaseException(
					"There is no such category in the database");
		}
		return categoryProductDatabases.get(category);
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
	 *             CategoryProductDatabase is caught and thrown again. If there
	 *             is no corresponding CategoryProductDatabase for the requested
	 *             category. I.e. there is no key for that category.
	 */
	public void addProduct(String category, Product article)
			throws DatabaseException {
		CategoryProductDatabase categoryProductDb = getCategory(category);
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
	 *             the EAN. If there is no corresponding CategoryProductDatabase
	 *             for the requested category. I.e. there is no key for that
	 *             category.
	 */
	public Product getProduct(String category, Long ean)
			throws DatabaseException {
		CategoryProductDatabase categoryProductDb = getCategory(category);
		return categoryProductDb.getProduct(ean);
	}

	/**
	 * A method which deletes the product with the given EAN from the given
	 * category.
	 * 
	 * @param category
	 *            The category of the product, where it is.
	 * @param ean
	 *            The EAN of the product
	 * @throws DatabaseException
	 *             If there is no product in the database with this EAN. I.e.
	 *             there is no key in the HashMap with the value of the EAN. If
	 *             there is no corresponding CategoryProductDatabase for the
	 *             requested category. I.e. there is no key for that category.
	 */
	public void deleteProduct(String category, Long ean)
			throws DatabaseException {
		CategoryProductDatabase categoryProductDb = getCategory(category);
		categoryProductDb.deleteProduct(ean);
	}

	/**
	 * A method which adds new category and prepares it's database.
	 * 
	 * @param category
	 *            The new category to be added
	 * @throws DatabaseException
	 *             If one tries to add a category who already exists.
	 */
	public void addCategoryProductDatabase(String category)
			throws DatabaseException {
		if (categoryProductDatabases.containsKey(category)) {
			throw new DatabaseException(
					"There is already a category with this name");
		}
		categoryProductDatabases.put(category, new CategoryProductDatabase());
	}

	/**
	 * A method which changes the name of the category.
	 * 
	 * @param oldCategory
	 *            The old name of the category
	 * @param newCategory
	 *            The new name of the category
	 * @throws ServiceException
	 *             If there already exists a category with the new name. If
	 *             there is no category with the old name.
	 */
	public void renameCategoryProductDatabase(String oldCategory,
			String newCategory) throws DatabaseException {
		CategoryProductDatabase oldDb = getCategory(oldCategory);
		if (categoryProductDatabases.containsKey(newCategory)) {
			throw new DatabaseException(
					"There is already a category with this name");
		}
		categoryProductDatabases.put(newCategory, oldDb);
		deleteCategoryProductDatabase(oldCategory);
		// TODO: misschien toch beter Category-klasse?
	}

	/**
	 * A method which deletes a category from the database.
	 * 
	 * @param category
	 *            The category to be deleted
	 * @throws DatabaseException
	 *             If there is no category with the given name
	 */
	public void deleteCategoryProductDatabase(String category)
			throws DatabaseException {
		if (!categoryProductDatabases.containsKey(category)) {
			throw new DatabaseException("There is no such category");
		}
		categoryProductDatabases.remove(category);
	}

	/**
	 * A method which merges two categories. The second category is ammended
	 * into the first one. If there are products with the same EAN, the product
	 * from the second category are kept.
	 * 
	 * @param categoryToBeAmmended
	 *            The category to be amended.
	 * @param oldCategory
	 *            The category to which to other category will be added
	 * @throws DatabaseException
	 *             If at least one of the two categories given doesn't exist
	 */
	public void mergeCategories(String categoryToBeAmmended, String oldCategory)
			throws DatabaseException {
		CategoryProductDatabase updatedDb = getCategory(categoryToBeAmmended);
		CategoryProductDatabase oldDb = getCategory(oldCategory);
		for (Long key : oldDb.keySet()) {
			if (updatedDb.containsEan(key)) {
				updatedDb.updateProduct(updatedDb.getProduct(key),
						oldDb.getProduct(key));
			} else {
				updatedDb.addProduct(oldDb.getProduct(key));
			}
		}
		deleteCategoryProductDatabase(oldCategory);
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