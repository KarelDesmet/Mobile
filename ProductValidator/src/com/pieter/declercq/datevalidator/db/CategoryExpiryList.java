package com.pieter.declercq.datevalidator.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
		articles.add(expiryProduct);			
	}
	
	//TODO
	public ArrayList<ExpiryProduct> getExpiryProducts(Date expiryDate){
		ArrayList<ExpiryProduct> result = new ArrayList<ExpiryProduct>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for(int i = 0; i < size(); i++){
			if(sdf.format(articles.get(i).getExpiryDate()).equals(sdf.format(expiryDate))){
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
    public int getSpot(){
        return this.current;
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

    public Date getMinExpiryDate(){
        if (articles.size() > 0) {
            Date date = articles.get(0).getExpiryDate();
            for (int i = 1; i < articles.size(); i++) {
                if (articles.get(i).getExpiryDate().before(date)) {
                    date = articles.get(i).getExpiryDate();
                }
            }
            return date;
        } else {
            return new Date();
        }
    }

    public Date getMaxExpiryDate(){
        if (articles.size() > 0) {
            Date date = articles.get(0).getExpiryDate();
            for (int i = 1; i < articles.size(); i++) {
                if (articles.get(i).getExpiryDate().after(date)) {
                    date = articles.get(i).getExpiryDate();
                }
            }
            return date;
        } else {
            return new Date();
        }
    }

    public void cleanExpiryProducts() throws DatabaseException {
        if (articles.size() > 0){
            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, -2);
            dt = c.getTime();
            for (int i = 0; i < articles.size(); i++) {
                if (articles.get(i).getExpiryDate().before(dt)) {
                    remove(articles.get(i));
                }
            }
        }
    }
}
