package test;

import domain.list.CharcuterieExpiryList;

public class tester {

	public static void main(String[] args) {
		CharcuterieExpiryList list = CharcuterieExpiryList.getInstance();
		System.out.println(list.getCurrentFridgeCount());
		list.getCurrentFridgeCount();
		System.out.println(list.getCurrentFridgeCount());
	}

}
