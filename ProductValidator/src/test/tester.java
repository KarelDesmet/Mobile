package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import service.DateValidator;
import domain.Product;
import exception.domain.DomainException;
import exception.service.ServiceException;

public class tester {

	public static void main(String[] args) throws ParseException {
		DateValidator d = new DateValidator();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			d.addProduct("charcuterie", new Product(9781780161389L,	"Linear Algebra", "Pearson"));
			d.deleteProduct("charcuterie", 9781780161389L);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (DomainException e) {
			e.printStackTrace();
		}
		System.out.println(d.getEanDatabase());
		System.out.println(format.parse("01-01-2000"));
	}

}
