//TODO: RUD-methodes, C reeds gedaan
package service;

import db.Database;
import domain.Product;
import domain.list.ExpiryList;
import exception.db.DatabaseException;
import exception.service.ServiceException;

/**
 * A Facade class. This class allows you to add products to the database of all
 * products.
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
	 * A method which returns the amount of products this DateValidator knows.
	 * I.e. the amount of products in the eanDatabase.
	 * 
	 * @return The amount of products known.
	 */
	public int getNumberOfProducts() {
		return mEanDatabase.size();
	}

	/**
	 * A CREATE method which adds a product to the database of all products.
	 * 
	 * @param category
	 *            The category of the product
	 * @param article
	 *            The product
	 * @throws ServiceException
	 *             If there is already a product in the eanDatabase with this
	 *             EAN. I.e. the DatabaseException from the Database is caught
	 *             and thrown in a ServiceException.
	 */
	public void addProduct(String category, Product article)
			throws ServiceException {
		try {
			mEanDatabase.addProduct(category, article);
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
	 * @return The product with the given ean from the given category
	 * @throws ServiceException
	 *             If there is no product in the category database with this
	 *             EAN. I.e. there is no key in the HashMap with the value of
	 *             the EAN.
	 */
	public Product getProduct(String category, Long ean)
			throws ServiceException {
		try {
			return mEanDatabase.getProduct(category, ean);
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