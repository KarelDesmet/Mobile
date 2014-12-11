package test;

import service.DateValidator;
import domain.Product;

public class tester {

	public static void main(String[] args) {
		DateValidator d = new DateValidator();
		d.addProduct("charcuterie", new Product(9781780161389L,
				"Linear Algebra", "Pearson"));
		System.out.println(d.getEanDatabase());
	}

}
