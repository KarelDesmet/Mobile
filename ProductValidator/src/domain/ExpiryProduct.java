package domain;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import exception.domain.DomainException;

/**
 * A class which represents a record about a product which goes overdue at a
 * certain date. It tells us which Product it is, when it goes bad, where it is
 * located and if it's removed from the store or not.
 * 
 * @author Pieter Declercq
 */
public class ExpiryProduct {

	/**
	 * The kind of Product which this expiryProduct is about.
	 */
	private Product article;

	/**
	 * The format of the date of expiry.
	 */
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * The date when the product is to be removed from the store.
	 */
	private Date expiryDate;

	/**
	 * The spot where the item is located. The number of the fridge, aisle, etc.
	 */
	private int spot;

	/**
	 * The boolean which tells us if true the product is removed from the store
	 * and is still in the store if false.
	 */
	private boolean removed;

	/**
	 * A constructor which will set a record for the expiryList independent of
	 * the spot where it is in the store.
	 * 
	 * @param article
	 *            The product which is about to expire
	 * @param expiryDate
	 *            The date when it is going to expire
	 * @throws DomainException
	 *             If the format from the date isn't right
	 */
	public ExpiryProduct(Product article, String expiryDate)
			throws DomainException {
		this(article, expiryDate, -1);
	}

	/**
	 * The constructor which will be used the most. It creates a record for the
	 * expiryList which isn't yet removed.
	 * 
	 * @param article
	 *            The product which is about to expire
	 * @param expiryDate
	 *            The date when it is going to expire
	 * @param spot
	 *            The spot where it is located in the store
	 * @param removed
	 *            True if removed from the store, false otherwise.
	 * @throws DomainException
	 *             If the format from the date isn't right
	 */
	public ExpiryProduct(Product article, String expiryDate, int spot)
			throws DomainException {
		this(article, expiryDate, spot, false);
	}

	/**
	 * A constructor which creates an record for the ExpiryList.
	 * 
	 * @param article
	 *            The product which is about to expire
	 * @param expiryDate
	 *            The date when it is going to expire
	 * @param spot
	 *            The spot where it is located in the store
	 * @param removed
	 *            True if removed from the store, false otherwise.
	 * @throws DomainException
	 *             If the format from the date isn't right
	 */
	public ExpiryProduct(Product article, String expiryDate, int spot,
			boolean removed) throws DomainException {
		setArticle(article);
		setSpot(spot);
		setRemoved(removed);
		setExpiryDate(expiryDate);
	}

	/**
	 * A method which return about which article this expiryProduct is about.
	 * 
	 * @return The product which is about to expire.
	 */
	public Product getArticle() {
		return article;
	}

	/**
	 * A method which sets the article which is about to expire to a product.
	 * 
	 * @param article
	 *            The product which is about to expire
	 */
	public void setArticle(Product article) {
		this.article = article;
	}

	/**
	 * A method which returns the date when the product is to be removed from
	 * the store.
	 * 
	 * @return The expiryDate of the product
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * A method which sets the expiryDate to the given string date.
	 * 
	 * @param date
	 *            The date when the product is to be removed
	 * @throws DomainException
	 *             If the given date is not in the correct format
	 */
	public void setExpiryDate(String date) throws DomainException {
		try {
			this.expiryDate = format.parse(date);
		} catch (ParseException e) {
			throw new DomainException("The date is not in the correct format",
					e);
		}
		// TODO: Correcte implementatie?
	}

	/**
	 * The spot represents the place where it is located in the store.
	 * 
	 * @return The number of the fridge
	 */
	public int getSpot() {
		return spot;
	}

	/**
	 * The spot where the product is located in the store
	 * 
	 * @param spot
	 *            Which fridge/aisle/freezer/...
	 * @throws DomainException
	 *             If the parameter is negative
	 */
	public void setSpot(int spot) throws DomainException {
		if (spot < 0) {
			throw new DomainException("There are no negative places");
		}
		this.spot = spot;
	}

	/**
	 * A method which tells us if a product is still in the store or not.
	 * 
	 * @return True if it is removed from the store, false otherwise.
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * A method to remove or add a product in the store.
	 * 
	 * @param removed
	 *            True if the product is no longer in the store. False
	 *            otherwise.
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
}