package com.pieter.declercq.datevalidator.exception.domain;

public class DomainException extends Exception {

	private static final long serialVersionUID = -392489517170101115L;

	public DomainException(String message, Throwable cause){
		super(message, cause);
	}
	
	public DomainException(String message){
		super(message);
	}
	
	public DomainException(Throwable cause){
		super(cause);
	}
	
	public DomainException(){
		super();
	}
	
	public static void main(String[] args) {
	}
}
