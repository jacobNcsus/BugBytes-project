package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import main.*;
/**
 * 	A test class for the BugBytes Item class.
 * 	
 * 	@author 	Jacob Normington, Sikander Ghafary
 *	@version	05/09/2021
 */
class ItemTest {

	Item i1 = new Item("PROD06", "produce", "lettuce", 2.50001, 1);	// Item Constructor i1 
	Item i2 = new Item("produce", "lettuce", 2.50001);				// Item Constructor i2

	@Test
	void constructor_default() 
	{
		
		assertEquals(i2.getProductId(), null);
		assertEquals(i2.getCategory(), "produce");
		assertEquals(i2.getName(), "lettuce");
		assertEquals(i2.getPrice(), 2.50); //includes rounding off trailing digits
		assertEquals(i2.getQuantity(), 0);
		i2.getTotalCost();
	}
	
	@Test
	void constructor_complete() 
	{
		
		assertEquals(i1.getProductId(), "PROD06");
		assertEquals(i1.getCategory(), "produce");
		assertEquals(i1.getName(), "lettuce");
		assertEquals(i1.getPrice(), 2.50); //includes rounding off trailing digits
		assertEquals(i1.getQuantity(), 1);
	}
	
	@Test
	void setQuantity()
	{
		
		i1.setQuantity(3);
		assertEquals(i1.getQuantity(), 3);
	}
	
	@Test
	void getTotalCost_correct()
	{
		
		assertEquals(i1.getTotalCost(), 2.50);
	}
	
	@Test
	void getTotalCost_default()
	{
		
		assertEquals(i2.getTotalCost(), 0);
	}
	
	@Test
	void string()
	{
		
		assertNotEquals(i1.toString(), "lettuce 1 @ 2.50");
	}
}
