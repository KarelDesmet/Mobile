package db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import domain.Product;
import exception.domain.DomainException;

public class DbReader {
	
	private String fileName = "File.txt";
	private String line = null;

	public DbReader(String category){
		fileName = category + fileName;
	}

	public Map<Long, Product> load() {
		Map<Long, Product> products = new HashMap<Long, Product>();
		
		try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("\t");
                Long ean = Long.parseLong(data[0]);
                String naam = data[1];
                String merk = data[2];
                String datum = data[3];
                try {
					Product product = new Product(ean, naam, merk);
					products.put(ean, product);
				} catch (DomainException e) {
					e.printStackTrace();
				}
            }    
            bufferedReader.close();            
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" + fileName + "'");                   
        }
		
		return products;
	}	
}
