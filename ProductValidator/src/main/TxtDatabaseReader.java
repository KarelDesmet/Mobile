package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.pieter.declercq.datevalidator.db.DBReader;
import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.domain.ExpiryProduct;
import com.pieter.declercq.datevalidator.domain.Product;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;
import com.pieter.declercq.datevalidator.exception.service.ServiceException;
import com.pieter.declercq.datevalidator.service.DateValidator;

public class TxtDatabaseReader implements DBReader {

	public TxtDatabaseReader() {

	}

	public DateValidator mDateValidator = new DateValidator();

	public void readProductDatabase() {
		try {

			BufferedReader reader = new BufferedReader(new FileReader(new File(
					"ProductDatabase.txt")));
			// reader = new BufferedReader(new
			// InputStreamReader(getAssets().open(databaseFileName)));
			String mLine = "";

			while ((mLine = reader.readLine()) != null) {
				String[] values = mLine.split("\\t");
				String ean = values[0];
				String name = values[1];
				String hope = values[2];
				String category = values[3];
				ArrayList<Category> categories = mDateValidator.getCategories();
				if (!categories.contains(new Category(category))) {
					mDateValidator.addCategory(new Category(category));
				}
				mDateValidator.addProduct(new Product(Long.parseLong(ean),
						name, Integer.parseInt(hope), new Category(category)));
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(e);
		} catch (DomainException e) {
			System.out.println(e);
		} catch (ServiceException e) {
			System.out.println(e);
		}

	}
	
    public void readExpiryList() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("ExpiryList.txt")));
            //reader = new BufferedReader(new InputStreamReader(getAssets().open(expiryListFileName)));
            String mLine = "";

            while ((mLine = reader.readLine()) != null) {
                String[] values = mLine.split("\\t");
                Category category = new Category(values[0]);
                Long ean = Long.parseLong(values[1]);
                int day = Integer.parseInt(values[2]);
                int month = Integer.parseInt(values[3]);
                int year = Integer.parseInt(values[4]);
                int spot = Integer.parseInt(values[5]);
                boolean removed = Boolean.parseBoolean(values[6]);
                ArrayList<Category> categories = mDateValidator.getCategories();
                if (!categories.contains(category)){
                    mDateValidator.addCategory(category);
                }
                Product p = mDateValidator.getProduct(category, ean);
                ExpiryProduct ep = new ExpiryProduct(p, day, month, year, spot, removed);
                mDateValidator.addExpiryProduct(ep);

            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e);
        } catch (DomainException e){
        	System.out.println(e);
        } catch (ServiceException e){
        	System.out.println(e);
        }
    }

    public void read(){
    	mDateValidator.clear();
    	readProductDatabase();
    	readExpiryList();
    }
    
    public void writeProductDatabase(){
        try{
            File file = new File("ProductDatabase.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            String ean = "";
            String name = "";
            String hope = "";
            String category = "";

            for(Product p : mDateValidator.getAllProducts()){
                String content = "";
                ean = String.valueOf(p.getEan());
                name = p.getName();
                hope = String.valueOf(p.getHope());
                category = p.getCategory().getName();

                content += ean;
                content += "\t";
                content += name;
                content += "\t";
                content += hope;
                content += "\t";
                content += category;

                bw.write(content);
                bw.newLine();
            }

            bw.close();

        } catch (Exception e){

        }
    }

}
