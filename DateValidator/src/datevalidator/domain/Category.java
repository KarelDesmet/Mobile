package com.pieter.declercq.datevalidator.domain;

import android.graphics.Color;

import com.pieter.declercq.datevalidator.exception.domain.DomainException;

/**
 * A class which represents a category in which a product can be sorted.d
 * 
 * @author Pieter Declercq
 */
public class Category {

	/**
	 * The name of the category.
	 */
	private String name;

    /**
     * The color associated with this category
     */
    private int color;

	/**
	 * Creates a category with the given string as name.
	 * 
	 * @param name
	 *            the name of the new category
	 * @throws DomainException
	 *             If the given name has no at least one letter
	 */
	public Category(String name) throws DomainException {
		this(name, Color.LTGRAY );
	}

    public Category(String name, int color ) throws DomainException {
        setName(name);
        setColor(color);
    }

	/**
	 * Returns the name of this category.
	 * 
	 * @return the name of the category
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the category
	 * 
	 * @param name
	 *            the new name of the category
	 * @throws DomainException
	 *             If the given name has not at least one letter
	 */
	public void setName(String name) throws DomainException {
		if (name.length() < 1) {
			throw new DomainException(
					"The name of the category must contain at least one letter.");
		}
		this.name = name;
	}

    //TODO
    public int getColor(){
        return this.color;
    }

    //TODO
    public void setColor(int color){
        this.color = color;
    }

	/**
	 * A method which sets the info of this category to the info of the updated
	 * Category.
	 * 
	 * @param updatedCategory
	 *            the category with the updated info
	 * @throws DomainException
	 *             if the name of the updated category has not at least one
	 *             letter
	 */
	public void update(Category updatedCategory) throws DomainException {
		setName(updatedCategory.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}