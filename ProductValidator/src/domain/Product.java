package domain;
//TODO
public class Product extends Identifier {

	//TODO
	private String name;
	//TODO
	private String brand;

	//TODO
	public Product() {
		this((long) 0, "default_product_name", "default_brand_name");
	}

	//TODO
	public Product(Long ean, String name, String brand) {
		setEan(ean);
		setName(name);
		setBrand(brand);
	}

	//TODO
	public String getName() {
		return name;
	}

	//TODO
	public void setName(String name) {
		this.name = name;
	}

	//TODO
	public String getBrand() {
		return brand;
	}

	//TODO
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
