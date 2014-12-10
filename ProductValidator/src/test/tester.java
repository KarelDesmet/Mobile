package test;

import domain.list.ExpiryList;

public class tester {

	public static void main(String[] args) {
		ExpiryList list = ExpiryList.getInstance();
		System.out.println(list.getCurrentFridgeCount());
		list.getCurrentFridgeCount();
		System.out.println(list.getCurrentFridgeCount());
	}

}
