//TODO: @After werkende krijgen...
//TODO: category-exception
//TODO: update-test
package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.Category;
import domain.Product;
import exception.domain.DomainException;
import exception.service.ServiceException;

/**
 * @author Pieter Declercq
 * 
 */
public class DateValidatorTest {

	private DateValidator dateValidator;
	private int numberOfProducts, numberOfCategories;
	private Category zuivel, zuivel2, voeding;
	private Product eggs, sameEanAsEggs, eggsDifferentEan, chocolate;

	@Before
	public void initialize() throws DomainException {
		dateValidator = new DateValidator();
		numberOfProducts = dateValidator.getNumberOfProducts();
		numberOfCategories = dateValidator.getNumberOfCategories();
		zuivel = new Category("zuivel");
		zuivel2 = new Category("zuivel");
		voeding = new Category("voeding");
		eggs = new Product(5414121001733L, "Eieren 12 stuks", "Rollie's",
				zuivel);
		sameEanAsEggs = new Product(5414121001733L, "Haha, same Ean", "Brand",
				zuivel);
		eggsDifferentEan = new Product(5414121001732L, "Eieren 12 stuks",
				"Rollie's", zuivel);
		chocolate = new Product(7622210100085L, "Melkchocolade met nootjes",
				"Cote d\'Or", voeding);

	}

	@After
	public void destroy() {
		dateValidator.clear();
		dateValidator = null;
		numberOfCategories = 0;
		numberOfProducts = 0;
		zuivel = null;
		zuivel2 = null;
		voeding = null;
		eggs = null;
		sameEanAsEggs = null;
		eggsDifferentEan = null;
		chocolate = null;
	}

	@Test
	public void addCategory_Creates_new_category_in_database() {
		try {
			dateValidator.addCategory(zuivel);
			assertEquals(numberOfCategories + 1,
					dateValidator.getNumberOfCategories());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = ServiceException.class)
	public void addCategory_ServiceException_When_Category_already_exists_in_database()
			throws ServiceException {
		dateValidator.addCategory(zuivel);
		dateValidator.addCategory(zuivel2);
		System.out.println(dateValidator.getNumberOfCategories());
	}

	@Test
	public void getCategory_Returns_the_requested_category() {
		try {
			dateValidator.addCategory(zuivel);
			assertEquals(zuivel, dateValidator.getCategory(zuivel));
		} catch (ServiceException e) {
			fail(e.getMessage());
		}

	}

	@Test(expected = ServiceException.class)
	public void getCategory_ServiceException_When_no_such_category_exists()
			throws ServiceException {
		dateValidator.getCategory(zuivel);
	}

	@Test
	public void deleteCategory_Deletes_category_from_database() {
		try {
			dateValidator.addCategory(zuivel);
			dateValidator.deleteCategory(zuivel);
			assertEquals(numberOfCategories,
					dateValidator.getNumberOfCategories());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test(expected=ServiceException.class)
	public void deleteCategory_ServiceException_When_category_does_not_exist() throws ServiceException {
		dateValidator.deleteCategory(zuivel);
	}

	@Test
	public void updateCategory_Category_is_now_the_new_category() {
		try {
			dateValidator.addCategory(zuivel);
			dateValidator.updateCategory(zuivel, voeding);
			assertEquals(voeding, dateValidator.getCategory(voeding));
		} catch (ServiceException e) {
			fail(e.getMessage());
		}

	}

	@Test(expected = ServiceException.class)
	public void updateCategory_ServiceException_When_the_updated_category_already_exists()
			throws ServiceException {
		dateValidator.addCategory(voeding);
		dateValidator.addCategory(zuivel);
		dateValidator.updateCategory(zuivel, voeding);
	}

	@Test(expected = ServiceException.class)
	public void updateCategory_ServiceException_When_the_old_category_does_not_exists()
			throws ServiceException {
		dateValidator.updateCategory(zuivel, voeding);
	}

	@Test(expected = ServiceException.class)
	public void deleteCategory_ServiceException_When_no_such_category_exists()
			throws ServiceException {
		dateValidator.deleteCategory(zuivel);
	}

	@Test
	public void addProduct_Adds_product_to_eanDatabase() {
		try {
			dateValidator.addCategory(zuivel);
			dateValidator.addProduct(eggs);
			assertEquals(numberOfProducts + 1,
					dateValidator.getNumberOfProducts());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = ServiceException.class)
	public void addProduct_ServiceException_When_there_is_categoryDatabase_for_the_category_of_the_product()
			throws ServiceException {
		dateValidator.addProduct(eggs);
	}

	@Test(expected = ServiceException.class)
	public void addProduct_ServiceException_When_there_is_a_product_with_that_ean()
			throws ServiceException {
		dateValidator.addProduct(eggs);
		dateValidator.addProduct(sameEanAsEggs);
	}

	@Test
	public void getProduct_Returns_object() {
		try {
			dateValidator.addCategory(zuivel);
			dateValidator.addProduct(eggs);
			assertEquals(eggs, dateValidator.getProduct(zuivel, eggs.getEan()));
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = ServiceException.class)
	public void getProduct_ServiceException_If_no_product_with_this_ean_exists()
			throws ServiceException {
		dateValidator.addCategory(zuivel);
		dateValidator.getProduct(zuivel, eggs.getEan());
	}

	@Test
	public void deleteProduct_Deletes_product_from_eanDatabase() {
		try {
			dateValidator.addCategory(zuivel);
			dateValidator.addProduct(eggs);
			dateValidator.deleteProduct(zuivel, eggs.getEan());
			assertEquals(numberOfProducts, dateValidator.getNumberOfProducts());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = ServiceException.class)
	public void deleteProduct_ServiceException_If_no_product_with_this_ean_exists()
			throws ServiceException {
		dateValidator.addCategory(zuivel);
		dateValidator.deleteProduct(zuivel, eggs.getEan());
	}

	@Test(expected = ServiceException.class)
	public void deleteProduct_ServiceException_If_no_productDatebase_for_this_category_exists()
			throws ServiceException {
		dateValidator.deleteProduct(zuivel, eggs.getEan());
	}

	@Test
	public void updateProduct_Product_is_now_the_updated_article_different_category() {
		try {
			dateValidator.addCategory(zuivel);
			dateValidator.addCategory(voeding);
			dateValidator.addProduct(eggs);
			dateValidator.updateProduct(eggs, chocolate);
			assertEquals(chocolate,
					dateValidator.getProduct(voeding, chocolate.getEan()));
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void updateProduct_Product_is_now_the_updated_article_same_category()
			throws DomainException {
		try {
			dateValidator.addCategory(zuivel);
			dateValidator.addProduct(eggs);
			dateValidator.updateProduct(eggs, eggsDifferentEan);
			assertEquals(eggsDifferentEan,
					dateValidator.getProduct(zuivel, eggsDifferentEan.getEan()));
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void mergeCategories_Category_is_ammended(){
		try {
			dateValidator.addCategory(zuivel);
			dateValidator.addCategory(voeding);
			dateValidator.addProduct(eggs);
			dateValidator.addProduct(chocolate);
			numberOfCategories = dateValidator.getNumberOfCategories();
			numberOfProducts = dateValidator.getNumberOfProducts();
			dateValidator.mergeCategories(zuivel, voeding);
			assertEquals(numberOfCategories - 1, dateValidator.getNumberOfCategories());
			assertEquals(numberOfProducts, dateValidator.getNumberOfProducts());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}
}