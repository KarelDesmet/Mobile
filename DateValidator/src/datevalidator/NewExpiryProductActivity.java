package com.pieter.declercq.datevalidator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pieter.declercq.datevalidator.domain.ExpiryProduct;
import com.pieter.declercq.datevalidator.domain.Product;
import com.pieter.declercq.datevalidator.service.DateValidator;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Pieter on 18/12/2014.
 */
public class NewExpiryProductActivity extends Activity {

    DateValidator service = new DateValidator();
    List<ExpiryProduct> epList = new ArrayList<ExpiryProduct>();
    int currentProduct = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        currentProduct = epList.size();
        if(currentProduct>0){
        	previousProduct();
        }
        final TextView frigo = (TextView) findViewById(R.id.frigo);
        frigo.setText(1);

        Button scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//Scan product
            	Toast.makeText(getApplicationContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	final EditText ean = (EditText) findViewById(R.id.barcodeInput);
            	final EditText naam = (EditText) findViewById(R.id.naamInput);
            	final EditText hope = (EditText) findViewById(R.id.hopeInput);
            	final Category category = null;//category
            	Product p = new Product(ean.toString(), naam.toString(), hope.toString(), category);
            	DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
            	final int day = dp.getDayOfMonth();
            	final int month = dp.getMonth();
            	final int year =  dp.getYear();
            	ExpiryProduct product = new ExpiryProduct(p, day, month, year);
            	final TextView frigo = (TextView) findViewById(R.id.frigo);
            	product.setSpot(Integer.parseInt((String) frigo.getText()));
            	service.addExpiryProduct(product);
            	epList.add(product);
            	currentProduct++;
            	clearView();
            }
        });
        
        Button removeButton = (Button) findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	final EditText ean = (EditText) findViewById(R.id.barcodeInput);
            	final EditText naam = (EditText) findViewById(R.id.naamInput);
            	final EditText hope = (EditText) findViewById(R.id.hopeInput);
            	final Category category = null;//category
            	Product p = new Product(ean.toString(), naam.toString(), hope.toString(), category);
            	DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
            	final int day = dp.getDayOfMonth();
            	final int month = dp.getMonth();
            	final int year =  dp.getYear();
            	ExpiryProduct product = new ExpiryProduct(p, day, month, year);
            	service.deleteExpiryProduct(product);
            	epList.remove();
            	clearView();
            }
        });
        
        Button previousProductButton = (Button) findViewById(R.id.previousProductButton);
        previousProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        
        Button previousFrigo = (Button) findViewById(R.id.previousFrigo);
        previousFrigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView frigo = (TextView) findViewById(R.id.frigo);
                int cf = Integer.parseInt((String) frigo.getText());
                if(cf>1){
                	cf--;
                	frigo.setText(cf);
                } else{
                	Toast.makeText(getApplicationContext(), "There is no previous fridge", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        Button nextFrigo = (Button) findViewById(R.id.nextFrigo);
        nextFrigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	final TextView frigo = (TextView) findViewById(R.id.frigo);
                int cf = Integer.parseInt((String) frigo.getText());
                cf++;
                frigo.setText(cf);
            }
        });

    }
    
    public void previousProduct(){
    	if(currentProduct>0){
        	currentProduct--;
        	ExpiryProduct ep = epList.get(currentProduct);
        	Product p = ep.getProduct();
        	EditText ean = (EditText) findViewById(R.id.barcodeInput);
        	EditText naam = (EditText) findViewById(R.id.naamInput);
        	EditText hope = (EditText) findViewById(R.id.hopeInput);
        	DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        	ean.setText(p.getEan());
        	naam.setText(p.getName());
        	hope.setText(p.getHope());
        	Date date = ep.getDate();
        	dp.updateDate(date.getYear(), date.getMonth(), date.getDay());
        	final TextView frigo = (TextView) findViewById(R.id.frigo);
        	frigo.setText(ep.getSpot());
    	} else{
    		Toast.makeText(getApplicationContext(), "There is no previous product", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void clearView(){
    	EditText ean = (EditText) findViewById(R.id.barcodeInput);
    	EditText naam = (EditText) findViewById(R.id.naamInput);
    	EditText hope = (EditText) findViewById(R.id.hopeInput);
    	ean.setText("");
    	naam.setText("");
    	hope.setText("");
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        //to category picker activity
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
