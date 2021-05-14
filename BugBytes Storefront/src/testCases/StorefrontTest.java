package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;

import main.*;

import org.junit.Test;

/**
 * 	A test class for the BugBytes ShoppingCart class. Incomplete
 * 	
 * 	@author 	Sikander Ghafary
 *	@version	05/13/2021
 */

public class StorefrontTest 
{
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
	public void signUpTest() {
		//store.signUp("username","firstname","lastname","email","9168568535");
		//store.signUp("username","Sai","Surseh","email","9168568535");

	}
	
	@Test
	public void checkoutTest() {
		//ShoppingCart cart = new ShoppingCart(1, "Jagannadha Chidella", false); 
		//store.checkout(cart);
	}
	
	@Test
	public void checkOut() {
		ShoppingCart cart1 = new ShoppingCart(1, "Jagannadha Chidella", false); 
		//cart1.addToCart("Lettuce",5);
		ShoppingCart cart2 = new ShoppingCart(1, "Jagannadha Chidella", false,cart1);

		//store.checkout(cart2);
	}
	
	@Test
	public void moveItemTest() {
		//store.moveItem("lettuce","produce");
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
	
	@Test
	public void requestAuthorizationTest_failure() {
		store.requestAuthorization("Sai","Suresh");
		assertFalse(store.isAdmin());
	}
	
	@Test
	public void requestAuthorizationTest_success() {
		store.requestAuthorization("shopMgr","csc131");
		assertTrue(store.isAdmin());
	}
}
