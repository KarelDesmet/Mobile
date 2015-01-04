package main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.domain.Product;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;
import com.pieter.declercq.datevalidator.exception.service.ServiceException;

public class MainTest {

	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws DatabaseException 
	 */
	public static void main(String[] args) throws ServiceException {

		TxtDatabaseReader t = new TxtDatabaseReader();
		t.read();
		//try {
			System.out.println(t.mDateValidator.getAllExpiryProducts());
			//t.mDateValidator.addProduct(new Product(1234567891240L, "test", 1, new Category("zuivel")));
			System.out.println(t.mDateValidator.getNumberOfExpiryProducts());
			t.writeProductDatabase();
			
			t.read();
			System.out.println(t.mDateValidator.getNumberOfExpiryProducts());
		//} catch (ServiceException | DomainException e) {
			//e.printStackTrace();
		//}
	}
}
