package service;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import service.DateValidator;
import domain.Product;
import exception.service.ServiceException;

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
		try {
			dateValidator.addProduct("zuivel", eggs);
			assertTrue(dateValidator.getNumberOfProducts() == numberOfProducts+1);
		} catch (ServiceException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test(expected=ServiceException.class)
	public void test_addProduct_ServiceException_If_product_with_this_ean_already_exists() throws ServiceException{
			dateValidator.addProduct("zuivel", eggs);
			dateValidator.addProduct("zuivel", eggs);
			fail("There was no exception thrown!");
	}

}
