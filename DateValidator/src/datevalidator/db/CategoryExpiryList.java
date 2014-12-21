package com.pieter.declercq.datevalidator.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.pieter.declercq.datevalidator.domain.ExpiryProduct;
import com.pieter.declercq.datevalidator.exception.db.DatabaseException;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;

//TODO
public class CategoryExpiryList {

	//TODO
	protected List<ExpiryProduct> articles;
	
	//TODO
	protected int current;
	
	//TODO
	public CategoryExpiryList(){
		articles = new ArrayList<ExpiryProduct>();
	}
	
	//TODO
	public int size(){
		return articles.size();
	}
	
	//TODO
	public boolean contains(ExpiryProduct record){
		for(int i = 0; i < size(); i++){
			if(record.equals(articles.get(i))){
				return true;
			}
		}
		return false;
	}
	
	//TODO
	public void addProduct(ExpiryProduct expiryProduct) throws DomainException{
		expiryProduct.setSpot(current);
		articles.add(expiryProduct);			
	}
	
	//TODO
	public ArrayList<ExpiryProduct> getExpiryProducts(Date expiryDate){
		ArrayList<ExpiryProduct> result = new ArrayList<ExpiryProduct>();
		for(int i = 0; i < size(); i++){
			if(articles.get(i).getExpiryDate().equals(expiryDate)){
				result.add(articles.get(i));
			}
		}
		return result;
	}
	
	//TODO
		public List<ExpiryProduct> getAllExpiryProducts(){
			return articles;
		}
	
	//TODO
	public void deleteProduct(ExpiryProduct expiryProduct){
		articles.remove(expiryProduct);			
	}
	
	//TODO
	public List<ExpiryProduct> getProducts(Date expiryDate){
		List<ExpiryProduct> productsByDate = new ArrayList<ExpiryProduct>();
		for(ExpiryProduct ep : articles){
			if(ep.getExpiryDate().equals(expiryDate)){
				productsByDate.add(ep);
			}
		}
		return productsByDate;
	}
	
	//TODO
	public ExpiryProduct getProduct(ExpiryProduct expiryProduct) throws DatabaseException{
		for(ExpiryProduct ep : articles){
			if(ep.equals(expiryProduct)){
				return ep;
			}
		}
		throw new DatabaseException("this record was not found");
	}
	
	//TODO
	public void next(){
		current += 1;
	}
	
	//TODO
	public void previous(){
		current -= 1;
	}
	
	//TODO
	public void remove(ExpiryProduct expiryProduct) throws DatabaseException{
		getProduct(expiryProduct).setRemoved(true);
	}
	
	//TODO
	public void cancelRemove(ExpiryProduct expiryProduct) throws DatabaseException{
		getProduct(expiryProduct).setRemoved(false);
	}
}
