package main;

import java.util.HashMap;

import db.Database;
import domain.Category;
import exception.db.DatabaseException;
import exception.domain.DomainException;

public class MainTest {

	/**
	 * @param args
	 * @throws DatabaseException 
	 */
	public static void main(String[] args) throws DatabaseException {
		try {
			Category zuivel = new Category("zuivel");
			Category zuivel2 = new Category("zuivel");
			Database db = Database.getInstance();
			db.addCategory(zuivel);
			db.addCategory(zuivel2);
			//HashMap<Category, String> map = new HashMap<Category, String>();
			//map.put(zuivel, "");
			System.out.println(zuivel.equals(zuivel2));
			//System.out.println(db.categoryProductDatabases.containsKey(zuivel2));
			//System.out.println(map.containsKey(zuivel2));
		} catch (DomainException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
