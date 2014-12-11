package db;

import java.util.HashMap;
import java.util.Map;

import domain.Product;
import exception.db.DatabaseException;

public class CategoryProductDatabase {

	private Map<Long, Product> articles;

	public CategoryProductDatabase() {
		articles = new HashMap<Long, Product>();
		try {
			addProduct(new Product(4008118757355L, "Perforator", "Locher"));
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A method which returns the number of products in this CategoryDatabase.
	 * 
	 * @return number of products in this CategoryDatabase
	 */
	public int size() {
		return articles.size();
	}

	/**
	 * A method which puts the given product in the HashMap of articles with the
	 * EAN as it's key.
	 * 
	 * @param product
	 *            The product to be added
	 * @throws DatabaseException
	 *             If there is already a product in the database with this EAN.
	 *             I.e. the ean of the product already exists as a key in the
	 *             HashMap
	 */
	public void addProduct(Product product) throws DatabaseException {
		if (articles.containsKey(product.getEan())) {
			throw new DatabaseException(
					"There is already a product with this EAN.");
		}
		articles.put(product.getEan(), product);
	}

	public void deleteProduct(Long ean) {
		articles.remove(ean);
	}

	public Product getProduct(Long ean) {
		return articles.get(ean);
	}

	/**
	 * This method implements how a categoryProductDatabase is represented as a
	 * String. I.e. all the products it contains on a separate line.
	 * 
	 * @return The String representation of a CategoryProductDatabase
	 */
	@Override
	public String toString() {
		String result = "";
		for (Long key : articles.keySet()) {
			result += articles.get(key) + "\n";
		}
		return result;
	}
}
