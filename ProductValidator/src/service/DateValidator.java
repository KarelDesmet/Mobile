//TODO: onderkant klassendiagramma
package service;

import db.Database;
import domain.Category;
import domain.Product;
import domain.list.ExpiryList;
import exception.db.DatabaseException;
import exception.service.ServiceException;

/**
 * A Facade class. This class allows you to add, see, update and delete products
 * to the database of all products. You can also ask to show to total amount of
 * products it knows.
 * 
 * @author Pieter Declercq
 * 
 */
public class DateValidator {

	/**
	 * The Database which contains all the products.
	 */
	private Database mEanDatabase;

	/**
	 * The ExpiryList which contains all expiryProducts.
	 */
	private ExpiryList mExpiryList;

	/**
	 * The default constructor of this Facade-class.
	 */
	public DateValidator() {
		mEanDatabase = Database.getInstance();
		mExpiryList = ExpiryList.getInstance();
	}

	/**
	 * A method which clears all data from the database.
	 */
	public void clear() {
		mEanDatabase.clear();
	}

	/**
	 * A method which returns the number of categories the database contains
	 * 
	 * @return the number of categories in the database
	 */
	public int getNumberOfCategories() {
		return mEanDatabase.getNumberOfCategories();
	}

	/**
	 * A method which adds a database to store products of the new category.
	 * 
	 * @param category
	 *            The new category of products to be added
	 * @throws ServiceException
	 *             If one tries to add a category who already exists.
	 */
	public void addCategory(Category category) throws ServiceException {
		try {
			mEanDatabase.addCategory(category);
		} catch (DatabaseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * A method which gets the requested category object from the database.
	 * 
	 * @param category
	 *            the requested category
	 * @throws ServiceExcpetion
	 *             If the requested category doesn't exist in the database
	 */
	public Category getCategory(Category category) throws ServiceException {
		try {
			return mEanDatabase.getCategory(category);
		} catch (DatabaseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * A method which changes the name of a category.
	 * 
	 * @param oldCategory
	 *            The old category
	 * @param newCategory
	 *            The updated category
	 * @throws ServiceException
	 *             If there already exists a category like the updated one. If
	 *             there is no category like the old one. If the name of the
	 *             updated category has not at least one letter.
	 */
	public void updateCategory(Category oldCategory, Category updatedCategory)
			throws ServiceException {
		try {
			mEanDatabase.updateCategory(oldCategory, updatedCategory);
		} catch (DatabaseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * A method which deletes a category from the database of products.
	 * 
	 * @param category
	 *            The category to be deleted
	 * @throws ServiceException
	 *             If there is no category with the given name in the database
	 */
	public void deleteCategory(Category category) throws ServiceException {
		try {
			mEanDatabase.deleteCategory(category);
		} catch (DatabaseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * A CREATE method which adds a product to the database of all products.
	 * 
	 * @param article
	 *            The product
	 * @throws ServiceException
	 *             If there is already a product in the eanDatabase with this
	 *             EAN. If there is no database associated with the category of
	 *             the product.
	 */
	public void addProduct(Product article) throws ServiceException {
		try {
			mEanDatabase.addProduct(article);
		} catch (DatabaseException e) {
			throw new ServiceException(e);
		}
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
	 * @throws ServiceException
	 *             If there is no product in the category database with this
	 *             EAN. I.e. there is no key in the HashMap with the value of
	 *             the EAN.
	 */
	public Product getProduct(Category category, Long ean)
			throws ServiceException {
		try {
			return mEanDatabase.getProduct(category, ean);
		} catch (DatabaseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * A method which updates a product.
	 * 
	 * @param oldArticle
	 *            the info of the old product
	 * @param updatedArticle
	 *            the updated info
	 * @throws ServiceException
	 *             If the EAN of the updated article doesn't have 13 ciphers. If
	 *             the name of the updated article has less then 5 letters. If
	 *             the brand of the updated article has less then 3 letters. If
	 *             the old product doesn't exist. If the category of one of the
	 *             two product doesn't exist.
	 */
	public void updateProduct(Product oldArticle, Product updatedArticle)
			throws ServiceException {
		try {
			mEanDatabase.updateProduct(oldArticle, updatedArticle);
		} catch (DatabaseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * A method which deletes the product with the given EAN from the given
	 * category.
	 * 
	 * @param category
	 *            The category of the product, where it is.
	 * @param ean
	 *            The EAN of the product
	 * @throws ServiceException
	 *             If there is no product in the category database with this
	 *             EAN. I.e. there is no key in the HashMap with the value of
	 *             the EAN.
	 */
	public void deleteProduct(Category category, Long ean)
			throws ServiceException {
		try {
			mEanDatabase.deleteProduct(category, ean);
		} catch (DatabaseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * A method which returns the amount of products this DateValidator knows.
	 * I.e. the amount of products in the eanDatabase.
	 * 
	 * @return The amount of products known.
	 */
	public int getNumberOfProducts() {
		return mEanDatabase.size();
	}

	/**
	 * A method to merge to categories. The first category is ammended with the
	 * contents of the second. If there are products with the same EAN, the
	 * product from the second category are kept.
	 * 
	 * @param categoryToBeAmmended
	 *            The category to be amended.
	 * @param oldCategory
	 *            The category to which to other category will be added
	 * @throws DatabaseException
	 *             If at least one of the two categories given doesn't exist
	 */
	public void mergeCategories(Category categoryToBeAmmended, Category oldCategory) throws ServiceException {
		try {
			mEanDatabase.mergeCategories(categoryToBeAmmended, oldCategory);
		} catch (DatabaseException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * The getter which returns the value of the field mEanDatabase.
	 * 
	 * @return The Database which contains all the products.
	 */
	public Database getEanDatabase() {
		return mEanDatabase;
	}

	/**
	 * The setter which sets the value of the field mEanDatabase to the given
	 * parameter.
	 * 
	 * @param mEanDatabase
	 *            The Database which contains all the products.
	 */
	public void setEanDatabase(Database mEanDatabase) {
		this.mEanDatabase = mEanDatabase;
	}

	/**
	 * The getter which returns the value of the field mExpiryList.
	 * 
	 * @return The ExpiryList which contains all expiryProducts.
	 */
	public ExpiryList getExpiryList() {
		return mExpiryList;
	}

	/**
	 * The setter which sets the value of the field mExpiryList to the given
	 * parameter.
	 * 
	 * @param mExpiryList
	 *            The ExpiryList which contains all the ExpiryProducts.
	 */
	public void setExpiryList(ExpiryList mExpiryList) {
		this.mExpiryList = mExpiryList;
	}

}