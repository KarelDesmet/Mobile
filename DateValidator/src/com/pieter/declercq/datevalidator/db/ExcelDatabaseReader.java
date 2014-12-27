package com.pieter.declercq.datevalidator.db;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.domain.Product;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;

public class ExcelDatabaseReader{

	private File file = new File("ProductDatabaseFinal.xls");
	private int row;
	private List<Product> list = new ArrayList<Product>();
	private Set<Category> categorien = new HashSet<Category>();

	public void read() throws BiffException, IOException, NoSuchAlgorithmException, NoSuchProviderException, DomainException{
		Workbook workbook = Workbook.getWorkbook(file);
		Sheet blad = workbook.getSheet("Blad1");
		readProducten(blad);
		workbook.close();
	}

	public void readProducten(Sheet sheet) throws DomainException {
		boolean stop = false;
		while (stop == false) {
			for (row = 1; row < sheet.getRows(); row++) {
				Cell ean = sheet.getCell(0, row);
				Cell naam = sheet.getCell(1, row);
				Cell hope = sheet.getCell(2, row);
				Cell category = sheet.getCell(3, row);
				String eanContent = ean.getContents();
				String naamContent = naam.getContents();
				String hopeContent = hope.getContents();
				String categoryContent = category.getContents();
				Long eanLong = Long.parseLong(eanContent);
				int hopeInt = Integer.parseInt(hopeContent);
				Category categoryC = new Category(categoryContent);
				categorien.add(categoryC);
				Product pr = new Product(eanLong, naamContent, hopeInt, categoryC);
				list.add(pr);
				stop = true;
			}
		}
	}

	public List<Product> getProducten(){
		return list;
	}
	
	public Set<Category> getCategorien(){
		return categorien;
	}
}