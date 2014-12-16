package db;

import java.util.HashMap;
import java.util.Map;

import domain.Category;
import domain.Product;
import exception.db.DatabaseException;
import exception.domain.DomainException;

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
	public Map<Category, CategoryProductDatabase> categoryProductDatabases;

	/**
	 * Private constructor to prevent others creating an instance.
	 */
	private Database() {
		categoryProductDatabases = new HashMap<Category, CategoryProductDatabase>();
		// try {
		// categoryProductDatabases.put("charcuterie", new
		// CategoryProductDatabase("charcuterie"));
		// categoryProductDatabases.put("diepvries", new
		// CategoryProductDatabase("diepvries"));
		// categoryProductDatabases.put("kaas", new
		// CategoryProductDatabase("kaas"));
		// categoryProductDatabases.put("voeding", new
		// CategoryProductDatabase("voeding"));
		// categoryProductDatabases.put("zuivel", new
		// CategoryProductDatabase("zuivel"));
		// } catch (DatabaseException e){
		// TODO: Delete default data
		// }
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
		for (Category key : categoryProductDatabases.keySet()) {
			size += categoryProductDatabases.get(key).size();
		}
		return size;
	}

	/**
	 * A method which wipes all data from this database.
	 */
	public void clear() {
		categoryProductDatabases.clear();
	}

	/**
	 * A method which returns the number of categories the database contains
	 * 
	 * @return the number of categories in the database
	 */
	public int getNumberOfCategories() {
		return categoryProductDatabases.keySet().size();
	}

	/**
	 * A method which adds new category and prepares it's database.
	 * 
	 * @param category
	 *            The new category to be added
	 * @throws DatabaseException
	 *             If one tries to add a category who already exists.
	 */
	public void addCategory(Category category) throws DatabaseException {
		if (categoryProductDatabases.containsKey(category)) {
			throw new DatabaseException(
					"There is already a category with this name");
		}
		categoryProductDatabases.put(category, new CategoryProductDatabase());
	}

	/**
	 * A method which gets the requested category object from the database.
	 * 
	 * @param category
	 *            the requested category
	 * @throws ServiceExcpetion
	 *             If the requested category doesn't exist in the database
	 */
	public Category getCategory(Category category) throws DatabaseException {
		for (Category key : categoryProductDatabases.keySet()) {
			if (key.equals(category)) {
				return key;
			}
		}
		throw new DatabaseException("This category isn't in the database");
	}

	/**
	 * A method which updates a category.
	 * 
	 * @param oldCategory
	 *            The old category
	 * @param newCategory
	 *            The updated category
	 * @throws DatabaseException
	 *             If there already exists a category like the updated one. If
	 *             there is no category like the old one. If the name of the
	 *             updated category has not at least one letter.
	 */
	public void updateCategory(Category oldCategory, Category updatedCategory)
			throws DatabaseException {
		if (categoryProductDatabases.containsKey(updatedCategory)) {
			throw new DatabaseException(
					"There is already a category with this name");
		}
		try {
			getCategory(oldCategory).update(updatedCategory);
		} catch (DomainException e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * A method which deletes a category from the database.
	 * 
	 * @param category
	 *            The category to be deleted
	 * @throws DatabaseException
	 *             If there is no category with the given name
	 */
	public void deleteCategory(Category category)
			throws DatabaseException {
		if (!categoryProductDatabases.containsKey(category)) {
			throw new DatabaseException("There is no such category");
		}
		categoryProductDatabases.remove(category);
	}

	/**
	 * A method which gets the categoryProductDatabase which corresponds with
	 * the given category.
	 * 
	 * @param category
	 *            The category of the categoryProductDatabase requested
	 * @return The categoryProductDatabase
	 * @throws DatabaseException
	 *             If there is no corresponding CategoryProductDatabase for the
	 *             requested category. I.e. there is no key for that category.
	 */
	public CategoryProductDatabase getCategoryProductDatabase(Category category)
			throws DatabaseException {
		return categoryProductDatabases.get(getCategory(category));
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
	public void addProduct(Product article) throws DatabaseException {
		CategoryProductDatabase categoryProductDb = getCategoryProductDatabase(article
				.getCategory());
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
	public Product getProduct(Category category, Long ean)
			throws DatabaseException {
		CategoryProductDatabase categoryProductDb = getCategoryProductDatabase(category);
		return categoryProductDb.getProduct(ean);
	}

	/**
	 * A method which updates a product.
	 * 
	 * @param oldArticle
	 *            the info of the old product
	 * @param updatedArticle
	 *            the updated info
	 * @throws DatabaseException
	 *             If the EAN of the updated article doesn't have 13 ciphers. If
	 *             the name of the updated article has less then 5 letters. If
	 *             the brand of the updated article has less then 3 letters. If
	 *             the old product doesn't exist. If the category of one of the
	 *             two product doesn't exist.
	 */
	public void updateProduct(Product oldArticle, Product updatedArticle)
			throws DatabaseException {
			deleteProduct(oldArticle.getCategory(), oldArticle.getEan());
			addProduct(updatedArticle);
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
	public void deleteProduct(Category category, Long ean)
			throws DatabaseException {
		CategoryProductDatabase categoryProductDb = getCategoryProductDatabase(category);
		categoryProductDb.deleteProduct(ean);
	}

	/**
	 * A method which merges two categories. The second category is ammended
	 * into the first one. If there are products with the same EAN, the product
	 * from the second category are kept.
	 * 
	 * @param categoryToBeAmmended
	 *            The category to be amended.
	 * @param addToThis
	 *            The category to which to other category will be added
	 * @throws DatabaseException
	 *             If at least one of the two categories given doesn't exist
	 */
	public void mergeCategories(Category categoryToBeAmmended, Category addToThis)
			throws DatabaseException {
		CategoryProductDatabase addToThisDb = getCategoryProductDatabase(addToThis);
		CategoryProductDatabase oldDb = getCategoryProductDatabase(categoryToBeAmmended);
		for (Long key : oldDb.keySet()) {
			if (addToThisDb.containsEan(key)) {
				addToThisDb.updateProduct(addToThisDb.getProduct(key),
						oldDb.getProduct(key));
			} else {
				addToThisDb.addProduct(oldDb.getProduct(key));
			}
		}
		deleteCategory(categoryToBeAmmended);
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
		for (Category key : categoryProductDatabases.keySet()) {
			result += key.getName().toUpperCase() + "\n";
			result += categoryProductDatabases.get(key) + "\n";
		}
		return result;
	}

	// TODO:
	/**
	 * //Class to load data into the local Database class object private
	 * DbReader reader;
	 * 
	 * //Commentaar private void loadProducts(){ articles = reader.load(); }
	 * 
	 * //Commentaar public void saveProducts(){
	 * 
	 * }
	 */
}