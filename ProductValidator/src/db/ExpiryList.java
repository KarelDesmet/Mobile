package db;

import java.util.HashMap;
import java.util.Map;

import domain.Category;
import domain.ExpiryProduct;
import exception.db.DatabaseException;
import exception.domain.DomainException;

/**
 * A class who represents the expiryList of all the products who are going to
 * expire in a certain period.
 * 
 * @author Pieter Declercq
 */
public class ExpiryList {

	/**
	 * The one and only instance of this expiryList.
	 */
	private static ExpiryList _instance;

	/**
	 * The list who holds all different categories and their respective
	 * expiryList together.
	 */
	private Map<Category, CategoryExpiryList> categoryExpiryLists;

	/**
	 * Private constructor to prevent others creating an instance.
	 */
	private ExpiryList() {
		categoryExpiryLists = new HashMap<Category, CategoryExpiryList>();
	}

	/**
	 * Synchronized creator method to prevent multi-threading problems.
	 */
	private synchronized static void createInstance() {
		if (_instance == null) {
			_instance = new ExpiryList();
		}
	}

	/**
	 * The only way to access the instance of the this class.
	 * 
	 * @return The expiryList of all products who are going to expire within a
	 *         certain time
	 */
	public static ExpiryList getInstance() {
		if (_instance == null) {
			createInstance();
		}
		return _instance;
	}

	/**
	 * A method which wipes all data from this list.
	 */
	public void clear() {
		categoryExpiryLists.clear();
	}
	
	/**
	 * A method which returns the number of categories the expiryList contains
	 * 
	 * @return the number of categories in the expiryList
	 */
	public int getNumberOfCategories() {
		return categoryExpiryLists.keySet().size();
	}
	
	/**
	 * A method which adds new category and prepares it's expiryList.
	 * 
	 * @param category
	 *            The new category to be added
	 * @throws DatabaseException
	 *             If one tries to add a category who already exists.
	 */
	public void addCategory(Category category) throws DatabaseException {
		if (categoryExpiryLists.containsKey(category)) {
			throw new DatabaseException(
					"There is already a category with this name");
		}
		categoryExpiryLists.put(category, new CategoryExpiryList());
	}

	/**
	 * A method which gets the requested category object from the database.
	 * 
	 * @param category
	 *            the requested category
	 * @throws DatabaseExcpetion
	 *             If the requested category doesn't exist in the database
	 */
	public Category getCategory(Category category) throws DatabaseException {
		for (Category key : categoryExpiryLists.keySet()) {
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
		if (categoryExpiryLists.containsKey(updatedCategory)) {
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
		if (!categoryExpiryLists.containsKey(category)) {
			throw new DatabaseException("There is no such category");
		}
		categoryExpiryLists.remove(category);
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
		CategoryExpiryList addToThisList = getCategoryExpiryList(addToThis);
		CategoryExpiryList oldList = getCategoryExpiryList(categoryToBeAmmended);
		for(ExpiryProduct product : oldList.articles){
			addToThisList.addProduct(product);
		}
		deleteCategory(categoryToBeAmmended);
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
	public CategoryExpiryList getCategoryExpiryList(Category category)
			throws DatabaseException {
		return categoryExpiryLists.get(getCategory(category));
	}


}
