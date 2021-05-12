package testCases;

import static org.junit.Assert.*;

import org.junit.Before;

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
	
	@Test
	public void addInventoryTest() {
		Item item = new Item("PROD06", "produce", "lettuce", 2.50001, 1);	// Item Constructor i1 
		store.addInventory(item,1,5);

	}
	
	@Test
	public void signUpTest() {
		store.signUp("username","firstname","lastname","email","9168568535");
	}
	
	@Test
	public void checkoutTest() {
		ShoppingCart cart = new ShoppingCart(1, "Jagannadha Chidella", false); //true to test if scriptRunner works
		store.checkout(cart);
		
		
	}
	
	@Test
	public void moveItemTest() {
		store.moveItem("lettuce","produce");
	}
	
	@Test
	public void changeEmailTest() {
		store.changeEmail(1,"email");
	}
	
	@Test
	public void changePhoneTest() {
		store.changePhone(1,"phoneNumber");
	}
	
	@Test
	public void removeAccountTest() {
		store.removeAccount(1);
	}
	
	@Test
	public void cancelOrderTest() {
		store.cancelOrder(1);
	}
	
	@Test
	public void isAdminTest() {
		store.isAdmin();
	}
	
	@Test
	public void requestAuthorizationTest() {
		store.requestAuthorization("Sai","Suresh");
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
