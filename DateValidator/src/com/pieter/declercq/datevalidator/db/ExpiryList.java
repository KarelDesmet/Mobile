package com.pieter.declercq.datevalidator.db;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.domain.ExpiryProduct;
import com.pieter.declercq.datevalidator.exception.db.DatabaseException;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;

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

	private ExcelExpiryReader xlr = new ExcelExpiryReader();
	private ExcelExpiryWriter xlw = new ExcelExpiryWriter();
	
	/**
	 * The list who holds all different categories and their respective
	 * expiryList together.
	 */
	private Map<Category, CategoryExpiryList> categoryExpiryLists;
	private List<ExpiryProduct> expiryProducten = new ArrayList<ExpiryProduct>();

	/**
	 * Private constructor to prevent others creating an instance.
	 */
	private ExpiryList() {
		categoryExpiryLists = new HashMap<Category, CategoryExpiryList>();
/*		xlr.read();
		Set<Category> categorienSet = xlr.getCategorien();
		for(Category c : categorienSet){
			addCategory(c);
		}
		expiryProducten = xlr.getProducten();
		for(ExpiryProduct p : expiryProducten){
			addExpiryProduct(p);
		}
*/	}

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
	
	public Map<Category, CategoryExpiryList> getCategoryExpiryMap(){
		return categoryExpiryLists;
	}

	/**
	 * A method which returns the total amount of records in the expiryList.
	 * I.e. the sum of the amount of products in all the different
	 * categoryProductDatabases.
	 * 
	 * @return The total amount of products in the database.
	 */
	public int size() {
		int size = 0;
		for (Category key : categoryExpiryLists.keySet()) {
			size += categoryExpiryLists.get(key).size();
		}
		return size;
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
	 * @throws DatabaseException
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
	 * @param updatedCategory
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
	public void deleteCategory(Category category) throws DatabaseException {
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
	 *             If at least one of the two categories given doesn't exist. If
	 *             the spot where the new products are added in the expiryList
	 *             is less than zero.
	 */
	public void mergeCategories(Category categoryToBeAmmended,
			Category addToThis) throws DatabaseException {
		CategoryExpiryList addToThisList = getCategoryExpiryList(addToThis);
		CategoryExpiryList oldList = getCategoryExpiryList(categoryToBeAmmended);
		for (ExpiryProduct product : oldList.articles) {
			try {
				product.setSpot(0);
				addToThisList.addProduct(product);
			} catch (DomainException e) {
				throw new DatabaseException(e);
			}
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

	public boolean containsRecord(ExpiryProduct record)
			throws DatabaseException {
		if (getCategoryExpiryList(record.getCategory()).contains(record)) {
			return true;
		}
		return false;
	}

	/**
	 * A method which adds a record of a product which is about to expire to the
	 * list of all products who are going to expire.
	 * 
	 * @param article
	 *            The record to add
	 * @throws DatabaseException
	 *             If the list of where this product belongs doesn't exist in
	 *             the expiryList. If the exact same record exists in the
	 *             expiryList
	 */
	public void addExpiryProduct(ExpiryProduct article)
			throws DatabaseException {
		if (containsRecord(article)) {
			throw new DatabaseException(
					"This record already exists in the list");
		}
		CategoryExpiryList expiryList = getCategoryExpiryList(article
				.getCategory());
		try {
			expiryList.addProduct(article);
		} catch (DomainException e) {
			throw new DatabaseException(e);
		}
	}

    public ArrayList<ExpiryProduct> getAllExpiryProducts() throws DatabaseException {
        ArrayList<ExpiryProduct> result = new ArrayList<ExpiryProduct>();
        for (Category c : categoryExpiryLists.keySet()){
            for (ExpiryProduct ep : getCategoryExpiryList(c).getAllExpiryProducts()){
                result.add(ep);
            }

        }
        return result;
    }

    //TODO
    public int getSpot(Category category) throws DatabaseException {
        return getCategoryExpiryList(category).getSpot();
    }

	/**
	 * A method which selects only the products who expire at the given date.
	 * 
	 * @param expiryDate
	 *            the date on which the products expire
	 * @return The products who expire at the given date.
	 * @throws DatabaseException
	 *             If the requested Category doesn't exist.
	 */
	public Map<Category, ArrayList<ExpiryProduct>> getExpiryProducts(
			Date expiryDate) throws DatabaseException {
		HashMap<Category, ArrayList<ExpiryProduct>> result = new HashMap<Category, ArrayList<ExpiryProduct>>();
		for (Category key : categoryExpiryLists.keySet()) {
			result.put(key,
					getCategoryExpiryList(key).getExpiryProducts(expiryDate));
		}
		return result;
	}

	/**
	 * A method to delete a record from the expirylist
	 * 
	 * @param article
	 *            the record to remove
	 * @throws DatabaseException
	 *             If there is no category in the expiryList which is the same
	 *             as that of the record
	 */
	public void deleteExpiryProduct(ExpiryProduct article)
			throws DatabaseException {
		getCategoryExpiryList(article.getCategory()).deleteProduct(article);
	}
	
	/**
	 * A method to go to the next fridge / freezer / aisle ... of the expiryList
	 * of the given category
	 * 
	 * @param category the category of which list is being worked on
	 * @throws DatabaseException
 	 *             If the requested Category doesn't exist.
	 */
	public void next(Category category) throws DatabaseException{
		getCategoryExpiryList(category).next();
	}
	
	/**
	 * A method to go to the previous fridge / freezer / aisle ... of the expiryList
	 * of the given category
	 * 
	 * @param category the category of which list is being worked on
	 * @throws DatabaseException
 	 *             If the requested Category doesn't exist.
	 */
	public void previous(Category category) throws DatabaseException{
		getCategoryExpiryList(category).previous();
	}

	//TODO
	public void remove(ExpiryProduct expiryProduct) throws DatabaseException{
		CategoryExpiryList list = getCategoryExpiryList(expiryProduct.getCategory());
		list.remove(expiryProduct);		
	}
	
	//TODO
	public void cancelRemove(ExpiryProduct expiryProduct) throws DatabaseException{
		CategoryExpiryList list = getCategoryExpiryList(expiryProduct.getCategory());
		list.cancelRemove(expiryProduct);
	}
	
	//Gebruik deze methode om alle producten dat in de ExpiryList klasse zitten in een excel bestand te schrijven.
	public void writeToExcel() throws RowsExceededException, WriteException, IOException, BiffException, NoSuchAlgorithmException, NoSuchProviderException, DomainException, DatabaseException{
		xlw.write();
	}
	
}