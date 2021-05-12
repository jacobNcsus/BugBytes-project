package testCases;

import static org.junit.Assert.*;
import main.*;

import org.junit.Test;

/**
 * 	A test class for the BugBytes ShoppingCart class. Incomplete
 * 	
 * 	@author 	Sikander Ghafary
 *	@version	05/12/2021
 */

public class StoreFrontTester {
	Storefront store = new Storefront();

	@Test
	public void testingTest() {
		store.printInventory();
	}
	
	@Test
	public void printAislesTest() {
		store.printAisles();
	}
	
	@Test
	public void printSingleAisleTest() {
		store.printAisle("Alcohol");
	}
	
	@Test
	public void printCartsTest() {
		store.printCarts();
	}
	
	@Test
	public void printMyCartTest() {
		store.printMyCart(1);
	}
	
	@Test
	public void printCustomersTest() {
		store.printCustomers();
	}
	
	@Test
	public void printCustomerTest() {
		store.printCustomer(1);
	}
	
	@Test
	public void printOrdersTest() {
		store.printOrders();
	}
	
	@Test
	public void printMyOrdersTest() {
		store.printMyOrders(1);
	}
	
	@Test
	public void printOrderDetailsTest() {
		store.printOrderDetails();
	}
	
	@Test
	public void printMyOrderDetailsTest() {
		store.printMyOrderDetails(1);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
