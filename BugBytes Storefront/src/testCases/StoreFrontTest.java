package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


/**
 * 	A test class for the BugBytes Item class.
 * 	
 * 	@author 	Sikander Ghafary
 *	@version	05/10/2021
 */

 class StoreFrontTest {

		main.Storefront store = new main.Storefront();	// calling instance of 
			
	@Test
	void nonParameters()  {
		
		store.printInventory();
		store.printAisles();
		store.printCarts();
		store.printCustomers();
		store.printOrders();
		store.printOrderDetails();
		store.isAdmin();
		

	}
	
	@Test
	void printAisleTest() {
		String aisleName = "Alcohol";
		store.printAisle(aisleName);
		
	}
	
	@Test
	void printMyCartTest() {
	}
	

}
