package domain;

import exception.domain.DomainException;

/**
 * This class represents an article. It has an EAN as Identifier, a name which
 * consists of at least 5 letters and a brandwhich consists of at least 3
 * letters.
 * 
 * @author Pieter Declercq
 * 
 */
public class Product extends Identifier {

	/**
	 * The name of the product.
	 */
	private String name;

	/**
	 * The brand of the product.
	 */
	private String brand;

	/**
	 * The category of the product.
	 */
	private Category category;

	/**
	 * The default constructor of a product. A default Product has a EAN of 0, a
	 * name "default_product_name" and a brand "default_brand_name".
	 * 
	 * @throws DomainException
	 *             If the default value for the EAN doesn't have 13 ciphers. If
	 *             the default value for the name doesn't have 5 letters. If the
	 *             default value for the brand doesn't have 3 letters. If the
	 *             name of the default category contains less than 1 letter.
	 */
	public Product() throws DomainException {
		this(000000000000000L, "default_product_name", "default_brand_name",
				new Category("all_products"));
	}

	/**
	 * The constructor for a product with a given EAN, name and brand. A EAN
	 * consists of 13 ciphers, a name of at least 5 letters and a brand of at
	 * least 3 letters.
	 * 
	 * @param ean
	 *            The EAN of the product
	 * @param name
	 *            The name of the product
	 * @param brand
	 *            The brand of the product
	 * @throws DomainException
	 *             If the EAN doesn't have 13 ciphers. If the name has less then
	 *             5 letters. If the brand has less then 3 letters.
	 */
	public Product(Long ean, String name, String brand, Category category)
			throws DomainException {
		setEan(ean);
		setName(name);
		setBrand(brand);
		setCategory(category);
	}

	/**
	 * A method which returns the name of the product.
	 * 
	 * @return The name of the product.
	 */
	public String getName() {
		return name;
	}

	/**
	 * A method which sets the name of the product to the given name.
	 * 
	 * @param name
	 *            The new name of the Product
	 * @throws DomainException
	 *             If the name doesn't contain at least 5 letters
	 */
	public void setName(String name) throws DomainException {
		if (name.length() < 5) {
			throw new DomainException(
					"The name must contain at least 5 letters");
		}
		this.name = name;
	}

	/**
	 * A method which returns the brand of the product.
	 * 
	 * @return The brand of the product
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * A method which sets the brand of the product to the given name.
	 * 
	 * @param brand
	 *            The new brand of the product
	 * @throws DomainException
	 *             If the brand contains less then 3 letters
	 */
	public void setBrand(String brand) throws DomainException {
		if (brand.length() < 3) {
			throw new DomainException(
					"The brand must contain at least 3 letters");
		}
		this.brand = brand;
	}

	/**
	 * A method which returns the category of the product.
	 * 
	 * @return the category of the product
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * A method which sets the category of the product to a given category.
	 * 
	 * @param category
	 *            the category of the product
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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