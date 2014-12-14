package test;

import java.text.ParseException;

import db.CategoryProductDatabase;
import db.Database;
import exception.db.DatabaseException;

public class fileReaderTester {

	public static void main(String[] args) throws ParseException {
		try {
			CategoryProductDatabase db = new CategoryProductDatabase("charcuterie");
			System.out.println(db);
			
			
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

}
