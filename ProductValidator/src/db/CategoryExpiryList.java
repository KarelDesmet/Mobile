package db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domain.ExpiryProduct;
import exception.domain.DomainException;

public class CategoryExpiryList {

	protected List<ExpiryProduct> articles;
	protected int current;
	
	public CategoryExpiryList(){
		articles = new ArrayList<ExpiryProduct>();
	}
	
	public int size(){
		return articles.size();
	}
	
	public boolean contains(ExpiryProduct record){
		for(int i = 0; i < size(); i++){
			if(record.equals(articles.get(i))){
				return true;
			}
		}
		return false;
	}
	
	public void addProduct(ExpiryProduct expiryProduct) throws DomainException{
		expiryProduct.setSpot(current);
		articles.add(expiryProduct);			
	}
	
	public ArrayList<ExpiryProduct> getExpiryProducts(Date expiryDate){
		ArrayList<ExpiryProduct> result = new ArrayList<ExpiryProduct>();
		for(int i = 0; i < size(); i++){
			if(articles.get(i).getExpiryDate().equals(expiryDate)){
				result.add(articles.get(i));
			}
		}
		return result;
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
		current += 1;
	}
	
	public void previous(){
		current -= 1;
	}
	
	public void remove(){
		
	}
	
	public void cancelRemove(){
		
	}
}
