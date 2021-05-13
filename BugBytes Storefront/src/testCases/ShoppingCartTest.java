package testCases;


import static org.junit.Assert.assertEquals;

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
	ShoppingCart s = new ShoppingCart(1,"username",false);
	Connector c;

	@Test
	public void checkOldCartTest() {

	}
	
	@Test
	public void custIDLessThanOneTest() {
		ShoppingCart less = new ShoppingCart(0,"username",false);
		int result = less.getCustomerId();
		assertEquals(0,result);
		
	}
	
	@Test
	public void custIDEqualToOneTest() {
		ShoppingCart one = new ShoppingCart(1,"username",true);
		int result = one.getCustomerId();
		assertEquals(1,result);
	}
	
	@Test
	public void custIDGreaterThanOneTest() {
		ShoppingCart greater = new ShoppingCart(2,"username",false);
		int result = greater.getCustomerId();
		assertEquals(2,result);
	}

	
	@Test
	public void getCustomerNameTest() {
		s.getCustomerName();
	}
	
	@Test
	public void getCustomerIdTest() {
		s.getCustomerId();
	}
	
	@Test
	public void getCartSizeTest() {
		s.getCartSize();
	}
	
	@Test
	public void addToCartTest() {
		s.addToCart("username",1);
	}
	
	@Test
	public void removeFromCartTest() {	
		s.removeFromCart("username");

	}
	
	@Test
	public void containsTest() {
		boolean result = s.contains("123");
		assertEquals(s.getCartSize(),0);
	}
	
	
	
	
	
	
	
	
	
}
