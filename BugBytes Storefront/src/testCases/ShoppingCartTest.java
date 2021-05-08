package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 	A test class for the BugBytes ShoppingCart class. Incomplete
 * 	
 * 	@author 	Jacob Normington
 *	@version	5/8/2021
 */
class ShoppingCartTest 
{
	@Test
	void constructor_lowID()
	{
		assertThrows(IllegalArgumentException.class, () -> {
			new main.ShoppingCart(0, "username", false);
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
}
