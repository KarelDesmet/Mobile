package test;

import service.DateValidator;
import domain.Product;
import exception.service.ServiceException;

public class tester {

	public static void main(String[] args) {
		DateValidator d = new DateValidator();
		try {
			d.addProduct("charcuterie", new Product(9781780161389L,
					"Linear Algebra", "Pearson"));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		System.out.println(d.getEanDatabase());
	}

}
