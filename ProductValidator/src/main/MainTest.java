package main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import service.DateValidator;
import domain.Category;
import exception.db.DatabaseException;
import exception.domain.DomainException;

public class MainTest {

	/**
	 * @param args
	 * @throws DatabaseException 
	 */
	public static void main(String[] args) throws DatabaseException {
		/*try {
			Category zuivel = new Category("zuivel");
			Category zuivel2 = new Category("zuivel");
			ExpiryList exp = ExpiryList.getInstance();
			exp.addCategory(zuivel);
			ExpiryProduct record = new ExpiryProduct(new Product(5414121001733L, "Eieren 12 stuks", 1252, zuivel), 29, 12, 2014);
			ExpiryProduct record2 = new ExpiryProduct(new Product(5414121001733L, "Eieren 12 stuks", 1252, zuivel), 29, 12, 2014);
			exp.addExpiryProduct(record);			
			//HashMap<Category, String> map = new HashMap<Category, String>();
			//map.put(zuivel, "");
			//System.out.println(exp.containsRecord(record2));
			//System.out.println(db.categoryProductDatabases.containsKey(zuivel2));
			//System.out.println(map.containsKey(zuivel2));
			Date date1 = record.getExpiryDate();
			Date date2 = record2.getExpiryDate();
			System.out.println(date1.equals(date2));
			
		} catch (DomainException e) {
			e.printStackTrace();
		}
		
		*/
		try {
			DateValidator service = new DateValidator();
			System.out.println(service.getCategoriesSet());
			
		} catch (BiffException | NoSuchAlgorithmException
				| NoSuchProviderException | IOException | DomainException | WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
