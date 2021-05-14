package testCases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.*;

/**
 * 	A test class for the BugBytes ShoppingCart class. Incomplete
 * 	
 * 	@author 	Jacob Normington, Sikander Ghafary
 *	@version	05/13/2021
 */

class ShoppingCartTest 
{
	ShoppingCart s = new ShoppingCart(1,"username",false,null); //also resets database before anything else can happen
	ShoppingCart s2 = new ShoppingCart(2,"jacob",false,null);

	@Test
	public void checkOldCartZeroTest() 
	{
		s.clearCart();
		s2.clearCart();
		ShoppingCart oldZero = new ShoppingCart(2,"username",false, s);
		assertEquals(oldZero.getCartSize(),0); //if this fails, it means Jacob's saved cart is not empty
	}
	
	@Test
	public void checkOldCartOneTest() 
	{
		s.clearCart();
		s2.clearCart();
		s.addToCart("Gallon_Fat Free Milk", 1);
		ShoppingCart oldOne = new ShoppingCart(2,"username",false, s);
		assertEquals(oldOne.getCartSize(),1);
		s.clearCart();
	}
	
	@Test
	public void checkOldCartMoreTest() 
	{
		s.clearCart();
		s2.clearCart();
		s.addToCart("Gallon_Fat Free Milk", 2);
		s.addToCart("Cabbage", 5);
		ShoppingCart oldTwo = new ShoppingCart(2,"username",false, s);
		assertEquals(oldTwo.getCartSize(),7);
		s.clearCart();
	}
	
	@Test
	public void checkOldCartTest_conflictFirst() 
	{
		s.clearCart();
		s2.clearCart();
		s2.addToCart("Gallon_Fat Free Milk", 1);
		s.addToCart("Gallon_Fat Free Milk", 2); //should keep the one in oldCart
		s.addToCart("Cabbage", 5);
		ShoppingCart oldFirst = new ShoppingCart(2,"username",false, s);
		assertEquals(oldFirst.getCartSize(),7);
		assertEquals(2,oldFirst.getQuantity("Gallon_Fat Free Milk"));
		s.clearCart();
	}
	
	@Test
	public void checkOldCartTest_conflictLast() 
	{
		s.clearCart();
		s2.clearCart();
		s2.addToCart("Cabbage", 1);
		s.addToCart("Gallon_Fat Free Milk", 2); //should keep the one in oldCart
		s.addToCart("Cabbage", 5);
		ShoppingCart oldSecond = new ShoppingCart(2,"username",false, s);
		assertEquals(oldSecond.getCartSize(),7);
		assertEquals(5,oldSecond.getQuantity("Cabbage"));
		s.clearCart();
	}
	
	@Test
	public void resetDatabaseTest() 
	{
		s.clearCart();
		s.addToCart("Chicken", 2);
		ShoppingCart reset = new ShoppingCart(1,"username",true);
		assertTrue(s.containsName("Chicken"));
		assertFalse(reset.containsName("Chicken"));
	}
	
	@Test
	public void custIDLessThanOneTest() 
	{
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			new ShoppingCart(0,"username",false,null);
	    });
		String expectedMessage = "Customer id must be positive.";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void custIDEqualToOneTest() 
	{
		ShoppingCart one = new ShoppingCart(1,"username",false,null);
		assertEquals("default", one.getCustomerName());
		assertEquals(1, one.getCustomerId());
	}
	
	@Test
	public void custIDGreaterThanOneTest() 
	{
		ShoppingCart greater = new ShoppingCart(5,"username",false,null);
		assertEquals("username", greater.getCustomerName());
		assertEquals(5, greater.getCustomerId());
	}
	
	@Test
	public void getCartSizeTest_empty() 
	{
		s.clearCart();
		assertEquals(s.getCartSize(), 0);
	}
	
	@Test
	public void getCartSizeTest_one() 
	{
		s.clearCart();
		s.addToCart("Cabbage",1);
		assertEquals(s.getCartSize(), 1);
	}
	
	@Test
	public void addToCartTest_nonsense() 
	{	
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			s.addToCart("dfindf",1);
	    });
		String expectedMessage = "No product of that name exists: dfindf.";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void addToCartTest() 
	{
		s.clearCart();
		s.addToCart("Wine", 1);
		assertTrue(s.containsName("Wine"));
		assertEquals(1,s.getQuantity("Wine"));
		s.clearCart();
	}
	
	@Test
	public void addToCartTest_exists() 
	{
		s.clearCart();
		s.addToCart("Wine", 1);
		s.addToCart("Wine", 2);
		assertTrue(s.containsName("Wine"));
		assertEquals(3,s.getQuantity("Wine"));
		s.clearCart();
	}
	
	@Test
	public void removeFromCartTest_nonsense() 
	{	
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			s.removeFromCart("dfindf");
	    });
		String expectedMessage = "No product of that name exists: dfindf.";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void removeFromCartTest_dontHave() 
	{	
		s.clearCart();
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			s.removeFromCart("Wine");
	    });
		String expectedMessage = "You do not have a product called: Wine.";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void removeFromCartTest_only() 
	{	
		s.clearCart();
		s.addToCart("Wine", 1);
		s.removeFromCart("Wine");
		assertFalse(s.containsName("Wine"));
	}
	
	@Test
	public void removeFromCartTest_first() 
	{	
		s.clearCart();
		s.addToCart("Wine", 1);
		s.addToCart("Beef", 1);
		s.removeFromCart("Wine");
		assertFalse(s.containsName("Wine"));
	}
	
	@Test
	public void removeFromCartTest_second() 
	{	
		s.clearCart();
		s.addToCart("Wine", 1);
		s.addToCart("Beef", 1);
		s.addToCart("Cereal", 1);
		s.removeFromCart("Beef");
		assertFalse(s.containsName("Beef"));
	}
	
	@Test
	public void removeFromCartTest_last() 
	{	
		s.clearCart();
		s.addToCart("Wine", 1);
		s.addToCart("Beef", 1);
		s.addToCart("Cereal", 1);
		s.removeFromCart("Cereal");
		assertFalse(s.containsName("Cereal"));
	}
	
	@Test
	public void changeCartQuantityTest_nonsense() 
	{	
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			s.changeCartQuantity("dfindf",1);
	    });
		String expectedMessage = "No product of that name exists: dfindf.";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void changeCartQuantityTest_dontHave() 
	{	
		s.clearCart();
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			s.changeCartQuantity("Wine",1);
	    });
		String expectedMessage = "You do not have a product called: Wine.";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void changeCartQuantityTest_first() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		s.addToCart("Turkey", 3);
		s.changeCartQuantity("Chicken", 2);
		assertEquals(2,s.getQuantity("Chicken"));
		s.clearCart();
	}
	
	@Test
	public void changeCartQuantityTest_last() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		s.addToCart("Turkey", 3);
		s.changeCartQuantity("Turkey", 7);
		assertEquals(7,s.getQuantity("Turkey"));
		s.clearCart();
	}
	
	@Test
	public void containsTest_nonsense() 
	{	
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			s.containsName("dfindf");
	    });
		String expectedMessage = "No product of that name exists: dfindf.";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void containsTest_dontHave() 
	{
		s.clearCart();
		assertFalse(s.containsName("Chicken"));
		s.clearCart();
	}
	
	@Test
	public void containsTest_first() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		s.addToCart("Oatmeal", 1);
		assertTrue(s.containsName("Chicken"));
		s.clearCart();
	}
	
	@Test
	public void containsTest_last() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		s.addToCart("Oatmeal", 1);
		assertTrue(s.containsName("Oatmeal"));
		s.clearCart();
	}
	
	@Test
	public void getSubtotalTest_empty() 
	{
		s.clearCart();
		assertEquals(0,s.getSubtotal());
	}
	
	@Test
	public void getSubtotalTest_one() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		assertEquals(10.99,s.getSubtotal());
		s.clearCart();
	}
	
	@Test
	public void getSubtotalTest_more() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		s.addToCart("Sliced_Pepper Jack", 1);
		assertEquals(15.98,s.getSubtotal());
		s.clearCart();
	}
	
	@Test
	public void getTaxTest() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		assertEquals(0.77,s.getTax());
		s.clearCart();
	}
	
	@Test
	public void getShippingTest() 
	{
		assertEquals(10.0,s.getShipping());
	}
	
	@Test
	public void getTotalCostTest() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		s.addToCart("Sliced_Pepper Jack", 1);
		assertEquals(27.10,s.getTotalCost());
		s.clearCart();
	}
	
	@Test
	public void printTotalTest_empty() 
	{
		s.clearCart();
		s.printTotal();
	}
	
	@Test
	public void printTotalTest_full() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		s.addToCart("Sliced_Pepper Jack", 1);
		s.printTotal();
		s.clearCart();
	}
}
