//TODO: @After werkende krijgen...
//TODO: category-exception
//TODO: update-test
package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import domain.Product;
import exception.domain.DomainException;
import exception.service.ServiceException;

/**
 * @author Pieter Declercq
 * 
 */
public class DateValidatorTest {

	private DateValidator dateValidator;
	private int numberOfProducts;
	private Product eggs;
	private ArrayList<Product> toDeleteAfterTest;

	@Before
	public void initialize() throws DomainException {
		dateValidator = new DateValidator();
		numberOfProducts = dateValidator.getNumberOfProducts();
		eggs = new Product(5414121001733L, "Eieren 12 stuks", "Rollie's");
	}

	@Test(expected = ServiceException.class)
	public void test_getProduct_ServiceException_If_no_product_with_this_ean_exists()
			throws ServiceException {
		dateValidator.getProduct("zuivel", eggs.getEan());
	}

	@Test
	public void test_getProduct_Returns_object() {
		try {
			dateValidator.addProduct("zuivel", eggs);
			assertEquals(dateValidator.getProduct("zuivel", eggs.getEan()),
					eggs);
			dateValidator.deleteProduct("zuivel", eggs.getEan());
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("An exception was thrown!");
		}

	}

	@Test
	public void test_addProduct_Adds_product_to_eanDatabase() {
		try {
			dateValidator.addProduct("zuivel", eggs);
			assertTrue(dateValidator.getNumberOfProducts() == numberOfProducts + 1);
			dateValidator.deleteProduct("zuivel", eggs.getEan());
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("An exception was thrown!");
		}
	}

	@Test
	public void test_deleteProduct_Deletes_product_from_eanDatabase() {
		try {
			dateValidator.addProduct("zuivel", eggs);
			dateValidator.deleteProduct("zuivel", eggs.getEan());
			assertTrue(dateValidator.getNumberOfProducts() == numberOfProducts);
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("An exception was thrown!");
		}
	}
	
	@Test(expected = ServiceException.class)
	public void test_deleteProduct_ServiceException_If_no_product_with_this_ean_exists() throws ServiceException{
		dateValidator.deleteProduct("zuivel", eggs.getEan());
	}
	
	@Test(expected = ServiceException.class)
	public void test_addProduct_ServiceException_If_product_with_this_ean_already_exists()
			throws ServiceException {
		dateValidator.addProduct("zuivel", eggs);
		dateValidator.addProduct("zuivel", eggs);
		System.out.println("test");
		fail("There was no exception thrown!");
	}

}