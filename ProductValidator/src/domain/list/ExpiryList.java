package domain.list;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domain.ExpiryProduct;

public class ExpiryList {

	protected List<ExpiryProduct> articles;
	protected int current;
	
	public void addProduct(ExpiryProduct expiryProduct){
		articles.add(expiryProduct);			
	}
	
	public void deleteProduct(ExpiryProduct expiryProduct){
		articles.remove(expiryProduct);			
	}
	
	public List<ExpiryProduct> getProducts(Date expiryDate){
		List<ExpiryProduct> productsByDate = new ArrayList<ExpiryProduct>();
		for(ExpiryProduct ep : articles){
			if(ep.getExpiryDate().equals(expiryDate)){
				productsByDate.add(ep);
			}
		}
		return productsByDate;
	}
	
	public void sortProducts(){
		
	}
	
	public void next(){
		
	}
	
	public void previous(){
		
	}
	
	public void remove(){
		
	}
	
	public void cancelRemove(){
		
	}
}
