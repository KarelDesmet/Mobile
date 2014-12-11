package db;

import java.util.HashMap;
import java.util.Map;

import domain.Product;

public class CategoryProductDatabase {

	private Map<Long, Product> articles;

	public CategoryProductDatabase() {
		articles = new HashMap<Long, Product>();
		addProduct(new Product(4008118757355L, "Perforator", "Locher"));
	}

	public void addProduct(Product product) {
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
