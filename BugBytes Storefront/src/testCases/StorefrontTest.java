package testCases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import main.*;

/**
 * 	A test class for the BugBytes ShoppingCart class. Incomplete
 * 	
 * 	@author 	Sikander Ghafary
 *	@version	05/13/2021
 */

public class StorefrontTest 
{
	/*
	PrintStream oldStream = System.out; //saves value
	java.io.File outFile = new java.io.File("lib\\StorefrontTest.out");
	PrintStream printStream = new PrintStream(outFile);
	System.setOut(printStream);
	Scanner printScanner = new Scanner(outFile); */
	
	Storefront store = new Storefront();
	Connector c = Connector.getCon(); //to verify certain fields
	
	@Test
	public void printInventoryTest() 
	{
		store.printInventory();
	}
	
	@Test
	public void printAislesTest() 
	{
		store.printAisles();
	}
	
	@Test
	public void printSingleAisleTest() 
	{
		store.printAisle("Alcohol");
	}
	
	@Test
	public void printCartsTest() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		store.printCarts();
	}
	
	@Test
	public void printMyCartTest() 
	{
		ShoppingCart cart = new ShoppingCart(1,"username",false);
		cart.addToCart("Cabbage", 2);
		cart.addToCart("Doughnut", 12);
		System.out.println("Cart should have - 2 Cabbage, 12 Doughnuts");
		store.printMyCart(1);
		cart.clearCart();
	}
	
	@Test
	public void printCustomersTest() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		store.printCustomers();
	}
	
	@Test
	public void printCustomerTest() 
	{
		store.printCustomer(1);
	}
	
	@Test
	public void printOrdersTest() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		store.printOrders();
	}
	
	@Test
	public void printMyOrdersTest() 
	{
		store.printMyOrders(2); //Jacob has a lot of orders
	}
	
	@Test
	public void printOrderDetailsTest() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		store.printOrderDetails();
	}
	
	@Test
	public void printMyOrderDetailsTest() 
	{
		store.printMyOrderDetails(2); //Jacob has a lot of orders
	}
	
	@Test
	public void addInventoryTest_notAllowed() 
	{
		if(store.isAdmin())
			store.requestAuthorization("", "");
		
		Item item = new Item("alcohol", "TestItem", 2.50001);
		Exception exception = assertThrows(SecurityException.class, () -> {
			store.addInventory(item,10,5);
	    });
		String expectedMessage = "Ordinary customers are not permitted to add new items to inventory.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void addInventoryTest_nonAisle() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("nonsense", "TestItem", 2.50001);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			store.addInventory(item,10,5);
	    });
		String expectedMessage = "Product type invalid, please choose 'Alcohol', 'Bakery', 'Dairy', 'Meat_seafood', or 'Produce'.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void addInventoryTest_badPrice() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("alcohol", "TestItem", -0.1);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			store.addInventory(item,10,5);
	    });
		String expectedMessage = "Price invalid, please enter a price greater than zero.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void addInventoryTest_badQuantity() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("alcohol", "TestItem", 2.50);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			store.addInventory(item,0,5);
	    });
		String expectedMessage = "Quantity invalid, please enter a quantity greater than zero.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void addInventoryTest_badReorder() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("alcohol", "TestItem", 2.50);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			store.addInventory(item,5,-10);
	    });
		String expectedMessage = "Reorder invalid, please enter a reorder greater than zero.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void addInventoryTest_alcohol() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("Alcohol", "TestItem", 2.50001);
		store.addInventory(item,10,5);
		
		int size = c.getHighestProductID("Alcohol");
		String prodID = "ALC";
		if(size < 10)
			prodID += "0";
		prodID += size;
		assertEquals("Alcohol",c.readItem(prodID,"t"));
		assertEquals("TestItem",c.readItem(prodID,"n"));
		assertEquals("2.50",c.readItem(prodID,"p"));
		assertEquals("10",c.readItem(prodID,"q"));
		assertEquals("5",c.readItem(prodID,"r"));
		store.removeInventory("TestItem");
		assertEquals(null,c.readItem(prodID,"q")); //null if not found
	}
	
	@Test
	public void addInventoryTest_bakery() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("Bakery", "TestItem", 2.50001);
		store.addInventory(item,10,5);
		
		int size = c.getHighestProductID("Bakery");
		String prodID = "BAKE";
		if(size < 10)
			prodID += "0";
		prodID += size;
		assertEquals("Bakery",c.readItem(prodID,"t"));
		assertEquals("TestItem",c.readItem(prodID,"n"));
		assertEquals("2.50",c.readItem(prodID,"p"));
		assertEquals("10",c.readItem(prodID,"q"));
		assertEquals("5",c.readItem(prodID,"r"));
		store.removeInventory("TestItem");
		assertEquals(null,c.readItem(prodID,"q")); //null if not found
	}
	
	@Test
	public void addInventoryTest_breakfast() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("Breakfast", "TestItem", 2.50001);
		store.addInventory(item,10,5);
		
		int size = c.getHighestProductID("Breakfast");
		String prodID = "BREAK";
		if(size < 10)
			prodID += "0";
		prodID += size;
		assertEquals("Breakfast",c.readItem(prodID,"t"));
		assertEquals("TestItem",c.readItem(prodID,"n"));
		assertEquals("2.50",c.readItem(prodID,"p"));
		assertEquals("10",c.readItem(prodID,"q"));
		assertEquals("5",c.readItem(prodID,"r"));
		store.removeInventory("TestItem");
		assertEquals(null,c.readItem(prodID,"q")); //null if not found
	}
	
	@Test
	public void addInventoryTest_dairy() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("Dairy", "TestItem", 2.50001);
		store.addInventory(item,10,5);
		
		int size = c.getHighestProductID("Dairy");
		String prodID = "DIAR";
		if(size < 10)
			prodID += "0";
		prodID += size;
		assertEquals("Dairy",c.readItem(prodID,"t"));
		assertEquals("TestItem",c.readItem(prodID,"n"));
		assertEquals("2.50",c.readItem(prodID,"p"));
		assertEquals("10",c.readItem(prodID,"q"));
		assertEquals("5",c.readItem(prodID,"r"));
		store.removeInventory("TestItem");
		assertEquals(null,c.readItem(prodID,"q")); //null if not found
	}
	
	@Test
	public void addInventoryTest_meat_seafood() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("Meat_seafood", "TestItem", 2.50001);
		store.addInventory(item,10,5);
		
		int size = c.getHighestProductID("Meat_seafood");
		String prodID = "MEA";
		if(size < 10)
			prodID += "0";
		prodID += size;
		assertEquals("Meat_seafood",c.readItem(prodID,"t"));
		assertEquals("TestItem",c.readItem(prodID,"n"));
		assertEquals("2.50",c.readItem(prodID,"p"));
		assertEquals("10",c.readItem(prodID,"q"));
		assertEquals("5",c.readItem(prodID,"r"));
		store.removeInventory("TestItem");
		assertEquals(null,c.readItem(prodID,"q")); //null if not found
	}
	
	@Test
	public void addInventoryTest_produce() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		Item item = new Item("produce", "TestItem", 2.50001);
		store.addInventory(item,10,5);
		
		int size = c.getHighestProductID("Produce");
		String prodID = "PROD";
		if(size < 10)
			prodID += "0";
		prodID += size;
		assertEquals("Produce",c.readItem(prodID,"t"));
		assertEquals("TestItem",c.readItem(prodID,"n"));
		assertEquals("2.50",c.readItem(prodID,"p"));
		assertEquals("10",c.readItem(prodID,"q"));
		assertEquals("5",c.readItem(prodID,"r"));
		store.removeInventory("TestItem");
		assertEquals(null,c.readItem(prodID,"q")); //null if not found
	}
	
	
	@Test
	public void signUpTest_failure() 
	{
		store.signUp("user4","Sai","Surseh","email","9168568535");
	}
	
	@Test
	public void signUpTest_success() 
	{
		store.signUp("TestUser","firstname","lastname","email","9168568535");
		store.removeAccount(c.getHighestCustomerID());
	}
	
	@Test
	public void checkoutTest_empty() 
	{
		ShoppingCart cart = new ShoppingCart(1, "Jagannadha Chidella", false);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			store.checkout(cart);
	    });
		String expectedMessage = "Cart is empty, no order to be made.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void checkoutTest_success() {
		ShoppingCart cart = new ShoppingCart(3, "Alexander Gunby", false);
		cart.addToCart("Cake", 1);
		cart.addToCart("Salmon", 2);
		store.checkout(cart);
		int id = c.getHighestOrderID()+1;
		assertEquals(0,cart.getCartSize());
		store.cancelOrder(id);
	}
	
	@Test
	public void moveItemTest_notAllowed() 
	{
		if(store.isAdmin())
			store.requestAuthorization("", "");
		
		Exception exception = assertThrows(SecurityException.class, () -> {
			store.moveItem("Shredded_Pepper Jack","produce");
	    });
		String expectedMessage = "Ordinary customers are not permitted to alter the store's inventory.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void moveItemTest() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		store.moveItem("Shredded_Pepper Jack","PrOdUcE");
		assertEquals("Produce",c.readItem(c.getProductID("Shredded_Pepper Jack"),"t"));
		store.moveItem("Shredded_Pepper Jack","dairy");
	}
	
	@Test
	public void setUnitPriceTest() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		store.setUnitPrice("Shredded_Pepper Jack",7.00);
		assertEquals("7.00",c.readItem(c.getProductID("Shredded_Pepper Jack"),"p"));
		store.setUnitPrice("Shredded_Pepper Jack",4.99);
	}
	
	@Test
	public void setReorderTest() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		store.setReorder("Shredded_Pepper Jack",7);
		assertEquals("7",c.readItem(c.getProductID("Shredded_Pepper Jack"),"r"));
		store.setUnitPrice("Shredded_Pepper Jack",5);
	}
	
	@Test
	public void changeProductNameTest() 
	{
		if(!store.isAdmin())
			store.requestAuthorization("shopMgr", "csc131");
		
		store.changeProductName("Shredded_Pepper Jack","nonsense");
		assertEquals("DIAR07",c.getProductID("nonsense"));
		store.changeProductName("nonsense","Shredded_Pepper Jack");
	}
	
	@Test
	public void changeEmailTest() {
		//store.changeEmail(1,"email");
	}
	
	@Test
	public void changePhoneTest() {
		//store.changePhone(1,"phoneNumber");
	}
	
	@Test
	public void removeAccountTest() {
		//store.removeAccount(1);
	}
	
	@Test
	public void cancelOrderTest() {
		//store.cancelOrder(1);
	}

}
