package testCases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;

import main.*;

import org.junit.Test;

/**
 * 	A test class for the BugBytes ShoppingCart class. Incomplete
 * 	
 * 	@author 	Sikander Ghafary
 *	@version	05/13/2021
 */

public class StorefrontTest {
	Storefront store = new Storefront();
	private Connector c;
	
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
	public void addAlcoholTest() {
		Item item = new Item("ALC01", "Alcohol", "Whiskey", 10.99, 1);
		store.addInventory(item,1,5);
	}
	
	@Test
	public void addBakeryTest() {
		Item item = new Item("BAKE01", "Bakery", "Bread", 2.99, 1);	// Item Constructor i1 
		store.addInventory(item,1,5);
	}
	
	@Test
	public void addBreakfastTest() {
		Item item = new Item("BREAK01", "Breakfast", "lettuce", 2.50001, 1);
		store.addInventory(item,1,5);
	}
	
	@Test
	public void addDairyTest() {
		Item item = new Item("DIAR01", "Dairy", "lettuce", 2.50001, 1);
		store.addInventory(item,1,5);
	}
	
	@Test
	public void addMeat_seafoodTest() {
		Item item = new Item("MEA01", "Meat_seafood", "lettuce", 2.50001, 1);
		store.addInventory(item,1,5);
	}
	
	@Test
	public void addProduceTest() {
		Item item = new Item("PROD06", "produce", "lettuce", 2.50001, 1);	// Item Constructor i1 
		store.addInventory(item,1,5);
	}
	
	
	@Test
	public void signUpTest() {
		store.signUp("username","firstname","lastname","email","9168568535");
		store.signUp("username","Sai","Surseh","email","9168568535");

	}
	
	@Test
	public void checkoutTest() {
		ShoppingCart cart = new ShoppingCart(1, "Jagannadha Chidella", false); //true to test if scriptRunner works
		store.checkout(cart);
	}
	
	@Test
	public void checkOut() {
		ShoppingCart cart1 = new ShoppingCart(1, "Jagannadha Chidella", false); //true to test if scriptRunner works
		cart1.addToCart("Lettuce",5);
		ShoppingCart cart2 = new ShoppingCart(1, "Jagannadha Chidella", false,cart1); //true to test if scriptRunner works

		store.checkout(cart2);
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
	
	@Test
	public void removeInventoryTest() {
		c.admin();
		store.requestAuthorization("", ""); //removes authorization
		store.removeInventory("PROD06");
	}
	
	
	
	
	
	
	
	
	
	
}
