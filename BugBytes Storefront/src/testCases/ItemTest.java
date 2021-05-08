package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 	A test class for the BugBytes Item class.
 * 	
 * 	@author 	Jacob Normington
 *	@version	5/8/2021
 */
class ItemTest {

	@Test
	void constructor_default() 
	{
		main.Item i = new main.Item("produce", "lettuce", 2.50001);
		
		assertEquals(i.getProductId(), null);
		assertEquals(i.getCategory(), "produce");
		assertEquals(i.getName(), "lettuce");
		assertEquals(i.getPrice(), 2.50); //includes rounding off trailing digits
		assertEquals(i.getQuantity(), 0);
		i.getTotalCost();
	}
	
	@Test
	void constructor_complete() 
	{
		main.Item i = new main.Item("PROD06", "produce", "lettuce", 2.50001, 1);
		
		assertEquals(i.getProductId(), "PROD06");
		assertEquals(i.getCategory(), "produce");
		assertEquals(i.getName(), "lettuce");
		assertEquals(i.getPrice(), 2.50); //includes rounding off trailing digits
		assertEquals(i.getQuantity(), 1);
	}
	
	@Test
	void setQuantity()
	{
		main.Item i = new main.Item("PROD06", "produce", "lettuce", 2.50001, 1);
		
		i.setQuantity(3);
		assertEquals(i.getQuantity(), 3);
	}
	
	@Test
	void getTotalCost_correct()
	{
		main.Item i = new main.Item("PROD06", "produce", "lettuce", 2.50001, 1);
		
		assertEquals(i.getTotalCost(), 2.50);
	}
	
	@Test
	void getTotalCost_default()
	{
		main.Item i = new main.Item("produce", "lettuce", 2.50001);
		
		assertEquals(i.getTotalCost(), 0);
	}
	
	@Test
	void string()
	{
		main.Item i = new main.Item("PROD06", "produce", "lettuce", 2.50001, 1);
		
		assertNotEquals(i.toString(), "lettuce 1 @ 2.50");
	}
}
