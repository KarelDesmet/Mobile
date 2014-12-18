package com.pieter.declercq.datevalidator.exception.db;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 3509955185714270471L;

	public DatabaseException(String message, Throwable cause){
		super(message, cause);
	}
	
	public DatabaseException(String message){
		super(message);
	}
	
	public DatabaseException(Throwable cause){
		super(cause);
	}
	
	public DatabaseException(){
		super();
	}

}
