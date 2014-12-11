package service;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import service.DateValidator;
import domain.Product;

/**
 * 
 */

/**
 * @author Pieter Declercq
 *
 */
public class DateValidatorTest {

	private DateValidator dateValidator;
	private int numberOfProducts;
	private Product eggs;
	
	@Before
	public void initialize(){
		dateValidator = new DateValidator();
		numberOfProducts = dateValidator.getNumberOfProducts();
		eggs = new Product(5414121001733L, "Eieren 12 stuks", "Rollie's");
	}
	
	@Test
	public void test_addProduct_Adds_product_to_eanDatabase() {
		dateValidator.addProduct("zuivel", eggs);
		assertTrue(dateValidator.getNumberOfProducts() == numberOfProducts+1);
	}

}
