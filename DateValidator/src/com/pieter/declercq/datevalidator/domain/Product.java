package com.pieter.declercq.datevalidator.domain;

import com.pieter.declercq.datevalidator.exception.domain.DomainException;

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
	 * The hope of the product.
	 */
	private int hope;

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
	 *             default value for the hope isn't a postive int. If the name
	 *             of the default category contains less than 1 letter.
	 */
	public Product() throws DomainException {
		this(000000000000000L, "default_product_name", 0, new Category(
				"default_category"));
	}

	/**
	 * The constructor for a product with a given EAN, name and brand. A EAN
	 * consists of 13 ciphers, a name of at least 5 letters and a hope number
	 * which is a positive int.
	 * 
	 * @param ean
	 *            The EAN of the product
	 * @param name
	 *            The name of the product
	 * @param hope
	 *            The hope of the product
	 * @throws DomainException
	 *             If the EAN doesn't have 13 ciphers. If the name has less then
	 *             5 letters. If the hope is not a positive int.
	 */
	public Product(Long ean, String name, int hope, Category category)
			throws DomainException {
		setEan(ean);
		setName(name);
		setHope(hope);
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
	 * A method which returns the hope of the product.
	 * 
	 * @return The hope of the product
	 */
	public int getHope() {
		return hope;
	}

	/**
	 * A method which sets the hope of the product to the given integer.
	 * 
	 * @param hope
	 *            The new hope of the product
	 * @throws DomainException
	 *             If the hope is negative
	 */
	public void setHope(int hope) throws DomainException {
		if (hope < 1) {
			throw new DomainException("The hope must be a positive integer");
		}
		this.hope = hope;
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
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + hope;
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
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (hope != other.hope)
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
		result = getEan() + "\t" + getName() + "\t" + getHope() + "\t" + getCategory();
		return result;
	}
}