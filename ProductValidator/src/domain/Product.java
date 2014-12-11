package domain;

public class Product extends Identifier {

	private String name;
	private String brand;

	public Product() {
		this((long) 0, "default_product_name", "default_brand_name");
	}

	public Product(Long ean, String name, String brand) {
		setEan(ean);
		setName(name);
		setBrand(brand);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * This method implements how a product is represented as a String. A
	 * product is represented by his EAN, name and brand separated by tabs.
	 * 
	 * @return The String representation of a Product
	 */
	@Override
	public String toString() {
		String result = "";
		result = getEan() + "\t" + getName() + "\t" + getBrand();
		return result;
	}
}
