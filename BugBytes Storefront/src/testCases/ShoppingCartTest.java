package testCases;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import main.*;

/**
 * 	A test class for the BugBytes ShoppingCart class. Incomplete
 * 	
 * 	@author 	Jacob Normington, Sikander Ghafary
 *	@version	05/10/2021
 */

class ShoppingCartTest 
{
	ShoppingCart s = new ShoppingCart(1,"username",false,null);
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
		s.addToCart("Gallon_Fat Free Milk", 1);
		ShoppingCart oldOne = new ShoppingCart(2,"username",false, s);
		assertEquals(oldOne.getCartSize(),1);
		s.clearCart();
	}
	
	@Test
	public void checkOldCartMoreTest() 
	{
		s.clearCart();
		s.addToCart("Gallon_Fat Free Milk", 2);
		s.addToCart("Cabbage", 5);
		ShoppingCart oldTwo = new ShoppingCart(2,"username",false, s);
		assertEquals(oldTwo.getCartSize(),7);
		s.clearCart();
	}
	
	@Test
	public void resetDatabaseTest() 
	{
		//ShoppingCart reset = new ShoppingCart(1,"username",true);
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
	public void addToCartTest() 
	{
		s.addToCart("username",1);
	}
	
	@Test
	public void removeFromCartTest() 
	{	
		s.removeFromCart("username");
	}
	
	@Test
	public void containsTest_success() 
	{
		s.clearCart();
		s.addToCart("Chicken", 1);
		assertTrue(s.containsName("Chicken"));
		s.clearCart();
	}
	
	@Test
	public void containsTest_failure() 
	{
		s.clearCart();
		assertTrue(!s.containsName("Chicken"));
		s.clearCart();
	}
}
