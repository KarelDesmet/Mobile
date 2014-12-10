package domain.db;

import java.util.Map;

import domain.Product;

public class CategoryProductDatabase {

	private Map<Long,Product> articles;
	
	public void addProduct(Product product){
		articles.put(product.getEan(), product);
	}
	
	public void deleteProduct(Long ean){
		articles.remove(ean);
	}
	
	public Product getProduct(Long ean){
		return articles.get(ean);
	}
}
