package db;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import domain.Product;
import exception.db.DatabaseException;

//TODO
public class CategoryProductDatabase {

	// TODO
	private Map<Long, Product> articles;

	// TODO
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
	 * A method which returns all the EAN's of this category.
	 * 
	 * @return A Set with all the keys (EAN - Long) of this category.
	 */
	public Set<Long> keySet() {
		return articles.keySet();
	}

	/**
	 * A method which returns a boolean. True if a product is known in this
	 * database. False otherwise.
	 * 
	 * @param ean
	 *            The EAN of the product
	 * @return If there is a product with the given ean in this category.
	 */
	public boolean containsEan(Long ean) {
		return articles.containsKey(ean);
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

	/**
	 * A method which returns the product with the given EAN.
	 * 
	 * @param ean
	 *            The EAN of the product
	 * @return The product with the given EAN
	 * @throws DatabaseException
	 *             If there is no product in the database with this EAN. I.e.
	 *             there is no key in the HashMap with the value of the EAN.
	 */
	public Product getProduct(Long ean) throws DatabaseException {
		if (!articles.containsKey(ean)) {
			throw new DatabaseException("There is no product with this EAN.");
		}
		return articles.get(ean);
	}

	/**
	 * A method which updates the info of a product in this category.
	 * 
	 * @param oldProduct
	 *            The old product
	 * @param newProduct
	 *            The updated product
	 * @throws DatabaseException
	 *             If there is no old product with that ean in this category
	 *             I.e. the product you want to update doesn't exist here.
	 */
	public void updateProduct(Product oldProduct, Product newProduct)
			throws DatabaseException {
		deleteProduct(oldProduct.getEan());
		addProduct(newProduct);
	}

	/**
	 * A method which deletes the product with the given EAN.
	 * 
	 * @param ean
	 *            The EAN of the product
	 * @throws DatabaseException
	 *             If there is no product in the database with this EAN. I.e.
	 *             there is no key in the HashMap with the value of the EAN.
	 */
	public void deleteProduct(Long ean) throws DatabaseException {
		if (!articles.containsKey(ean)) {
			throw new DatabaseException("There is no product with this EAN.");
		}
		articles.remove(ean);
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
