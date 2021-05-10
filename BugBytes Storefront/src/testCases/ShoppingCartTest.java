package testCases;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


/**
 * 	A test class for the BugBytes ShoppingCart class. Incomplete
 * 	
 * 	@author 	Jacob Normington, Sikander Ghafary
 *	@version	05/10/2021
 */
class ShoppingCartTest 
{
	main.ShoppingCart customer_0 = new main.ShoppingCart(0,"username");
	main.ShoppingCart customer_1 = new main.ShoppingCart(1,"username");
	main.ShoppingCart customer_2 = new main.ShoppingCart(2,"username",true);
	main.ShoppingCart customer_3 = new main.ShoppingCart(2,"username",false);
	main.Item item = new main.Item("PROD06", "produce", "lettuce", 2.50001, 1);	// Item Constructor i1 

	
	
	@Test
	void addItemTest() {

		customer_1.addItem(item);
		assertThrows(NullPointerException.class, () -> {
			customer_1.addItem(null);
			});
		
		}
	
	@Test
	void removeItemTest() {
		
	}
	
	@Test
	void changeQuantityTest() {
		
	}
	
	@Test
	void cartTest() {
		String category = "ALC01";
		customer_1.addToCart(category,1);
		customer_1.addItem(item);
		customer_1.removeFromCart(null);
		customer_1.changeCartQuantity(category,1);
		customer_1.clearCart(); // incomplete test
		customer_1.getSubtotal();
	}
	
		
		
	
	
	@Test
	void constructor_lowID() 
	{
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
				() -> {
					customer_1.getCustomerId();
 
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
