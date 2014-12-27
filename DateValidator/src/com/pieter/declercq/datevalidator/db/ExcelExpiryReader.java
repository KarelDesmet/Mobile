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
import com.pieter.declercq.datevalidator.domain.ExpiryProduct;
import com.pieter.declercq.datevalidator.domain.Product;
import com.pieter.declercq.datevalidator.exception.db.DatabaseException;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;

public class ExcelExpiryReader{

	private File file = new File("ExpiryListFinal.xls");
	private int row;
	private List<ExpiryProduct> list = new ArrayList<ExpiryProduct>();
	private Set<Category> categorien = new HashSet<Category>();
	
	public void read() throws BiffException, IOException, NoSuchAlgorithmException, NoSuchProviderException, DomainException, DatabaseException{
		Workbook workbook = Workbook.getWorkbook(file);
		Sheet blad = workbook.getSheet("Blad1");
		readProducten(blad);
		workbook.close();
	}

	public void readProducten(Sheet sheet) throws DomainException, BiffException, NoSuchAlgorithmException, NoSuchProviderException, IOException, DatabaseException {
		boolean stop = false;
		while (stop == false) {
			for (row = 1; row < sheet.getRows(); row++) {
				Cell ean = sheet.getCell(0, row);
				Cell day = sheet.getCell(1, row);
				Cell month = sheet.getCell(2, row);
				Cell year = sheet.getCell(3, row);
				Cell spot = sheet.getCell(4, row);
				Cell removed = sheet.getCell(5, row);
				String eanContent = ean.getContents();
				String dayContent = day.getContents();
				String monthContent = month.getContents();
				String yearContent = year.getContents();
				String spotContent = spot.getContents();
				String removedContent = removed.getContents();
				Long eanLong = Long.parseLong(eanContent);
				int dayInt = Integer.parseInt(dayContent);
				int monthInt = Integer.parseInt(monthContent);
				int yearInt = Integer.parseInt(yearContent);
				int spotInt = Integer.parseInt(spotContent);
				boolean rem = Boolean.parseBoolean(removedContent);
				Product article = Database.getInstance().getProductByEan(eanLong);
				ExpiryProduct ep = new ExpiryProduct(article, dayInt, monthInt, yearInt, spotInt, rem);
				categorien.add(ep.getCategory());
				list.add(ep);
				stop = true;
			}
		}
	}

	public List<ExpiryProduct> getProducten(){
		return list;
	}
	
	public Set<Category> getCategorien(){
		return categorien;
	}
}