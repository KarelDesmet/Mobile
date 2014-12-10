package domain;

import java.util.Date;

public class ExpiryProduct {

	private Product article;
	private Date expiryDate;
	private int spot;
	private boolean removed;
	
	public ExpiryProduct(){
		
	}
	
	public Product getArticle() {
		return article;
	}
	
	public void setArticle(Product article) {
		this.article = article;
	}
	
	public Date getExpiryDate() {
		return expiryDate;
	}
	
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public int getSpot() {
		return spot;
	}
	
	public void setSpot(int spot) {
		this.spot = spot;
	}
	
	public boolean isRemoved() {
		return removed;
	}
	
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
}
