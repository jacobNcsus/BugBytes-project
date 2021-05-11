package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Scanner;

/**
 * 	A test class for the BugBytes ShoppingCart class. Incomplete
 * 	
 * 	@author 	Jacob Normington, Sikander Ghafary
 *	@version	05/10/2021
 */
class ShoppingCartTest 
{
	main.ShoppingCart cart = new main.ShoppingCart(2,"username",false);
	
	@Test
	void constructor_lowID() 
	{
		assertThrows(IllegalArgumentException.class, () -> {
			new main.ShoppingCart(0,"username", false);
		});
	}
	
	@Test
	void constructor_defaultUser()
	{
		main.ShoppingCart s = new main.ShoppingCart(1, "username", false);
		
		assertEquals(s.getCustomerName(), "default");
		assertEquals(s.getCartSize(), 0);
	}
	
	@Test
	void constructor_reset()
	{
		//main.ShoppingCart s = new main.ShoppingCart(1, "username", true);
		//how do we ensure the script was run correctly, though?
	}
	
	@Test
	void constructor_normalUser()
	{
		main.ShoppingCart s = new main.ShoppingCart(2, "username", false);
		
		assertEquals(s.getCustomerName(), "username");
	}
	
	@Test
	void addItem() 
	{
		cart.addToCart("lettuce", 1);
		
		assertEquals(cart.containsName("lettuce"), true);
		assertEquals(cart.getQuantity("lettuce"), 1);
	}
	
	@Test
	void addItem_LowQuantity() 
	{
		assertThrows(IllegalArgumentException.class, () -> {
			cart.addToCart("lettuce", 0);
		});
	}
	
	@Test
	void addItem_DoesNotExist() 
	{
		//how do you do this?
	}
	
	@Test
	void removeItemTest() 
	{
		
	}
	
	@Test
	void changeQuantityTest() 
	{
		
	}
}
