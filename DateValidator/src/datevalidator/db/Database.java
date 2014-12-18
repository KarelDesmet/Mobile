package datevalidator.db;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import datevalidator.domain.Category;
import datevalidator.domain.Product;
import datevalidator.exception.db.DatabaseException;
import datevalidator.exception.domain.DomainException;

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
	
	private ExcelDatabaseReader xlr = new ExcelDatabaseReader();
	private ExcelDatabaseWriter xlw = new ExcelDatabaseWriter();

	/**
	 * The database who holds all different categories and their respective
	 * databases together.
	 */
	private Map<Category, CategoryProductDatabase> categoryProductDatabases;

	private List<Product> producten = new ArrayList<Product>();
	
	/**
	 * Private constructor to prevent others creating an instance.
	 * @throws DomainException 
	 * @throws IOException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws BiffException 
	 * @throws DatabaseException 
	 */
	private Database() throws BiffException, NoSuchAlgorithmException, NoSuchProviderException, IOException, DomainException, DatabaseException {
		categoryProductDatabases = new HashMap<Category, CategoryProductDatabase>();
		xlr.read();
		Set<Category> categorienSet = xlr.getCategorien();
		for(Category c : categorienSet){
			addCategory(c);
		}
		producten = xlr.getProducten();
		for(Product p : producten){
			addProduct(p);
		}
	}
	
	public List<Product> getProducten(){
		return producten;
	}

	/**
	 * Synchronized creator method to prevent multi-threading problems.
	 * @throws DatabaseException 
	 * @throws DomainException 
	 * @throws IOException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws BiffException 
	 */
	private synchronized static void createInstance() throws BiffException, NoSuchAlgorithmException, NoSuchProviderException, IOException, DomainException, DatabaseException {
		if (_instance == null) {
			_instance = new Database();
		}
	}

	/**
	 * The only way to access the instance of the this class.
	 * 
	 * @return The database of all known products
	 * @throws DatabaseException 
	 * @throws DomainException 
	 * @throws IOException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws BiffException 
	 */
	public static Database getInstance() throws BiffException, NoSuchAlgorithmException, NoSuchProviderException, IOException, DomainException, DatabaseException {
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
	 * @throws DatabaseException
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
	 * @param updatedCategory
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
	
	//TODO
	public ArrayList<Category> getCategories(){
        ArrayList<Category> result = new ArrayList<Category>();
        for(Category c : categoryProductDatabases.keySet()){
            result.add(c);
        }
        return result;
	}
	
	public Set<Category> getCategoriesSet(){
		return categoryProductDatabases.keySet();
	}
	
	//Gebruik deze methode om alle producten dat in de Database klasse zitten in een excel bestand te schrijven.
	public void writeToExcel() throws RowsExceededException, WriteException, IOException, BiffException, NoSuchAlgorithmException, NoSuchProviderException, DomainException, DatabaseException{
		xlw.write();
	}
	
	//Gebruik deze methode om een product aan de Database klasse toe te voegen.
	public void addProductToDB(Product product){
		producten.add(product);
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

	public Product getProductByEan(Long eanLong) throws DomainException {
		Product prod = null;
		for(Product p : producten){
			if(p.getEan().equals(eanLong)){
				prod = p;
			}
		}
		return prod;
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