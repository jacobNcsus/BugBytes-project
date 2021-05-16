package testCases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import main.Connector;

/**
 * 	A test class for the BugBytes Connector, any methods that are 
 * 	not covered by ShoppingCart and Storefront.
 * 
 * 	@author Jacob Normington
 * 	@version 5/13/2021
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectorTest 
{
	Connector c = Connector.getCon();
	
	@Test
	void readItemTest_columnInvalid() 
	{
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			c.readItem("ALC01", "dfindf");
	    });
		String expectedMessage = "Invalid position. Please choose either 'aisle', 'name', 'price', 'quantity', or 'reorder'.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void readTest_allCarts_notAdmin()
	{
		c.authorize("", ""); //remove admin
		Exception exception = assertThrows(SecurityException.class, () -> {
			c.read("cart", -1);
	    });
		String expectedMessage = "Ordinary customers are not allowed to access other customers' carts.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void readTest_allCustomers_notAdmin()
	{
		c.authorize("", ""); //remove admin
		Exception exception = assertThrows(SecurityException.class, () -> {
			c.read("customer", -1);
	    });
		String expectedMessage = "Ordinary customers are not allowed to access other customers' information.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void readTest_allOrders_notAdmin()
	{
		c.authorize("", ""); //remove admin
		Exception exception = assertThrows(SecurityException.class, () -> {
			c.read("order", -1);
	    });
		String expectedMessage = "Ordinary customers are not allowed to access other customers' orders.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void readTest_allOrderDetails_notAdmin()
	{
		c.authorize("", ""); //remove admin
		Exception exception = assertThrows(SecurityException.class, () -> {
			c.read("order_details", -1);
	    });
		String expectedMessage = "Ordinary customers are not allowed to access more than one order.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void getHighestProductIdTest_nonAisle()
	{
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			c.getHighestProductID("dfindf");
	    });
		String expectedMessage = "Product type invalid, please choose 'Alcohol', 'Bakery', 'Dairy', 'Meat_seafood', or 'Produce'.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void updateTest_badName()
	{
		if(!c.admin())
			c.authorize("shopMgr", "csc131");
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			c.update("dfindf","t","doesntMatter");
	    });
		String expectedMessage = "No product of that name exists: dfindf.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void updateTest_badColumn()
	{
		if(!c.admin())
			c.authorize("shopMgr", "csc131");
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			c.update("Whiskey","dfindf","doesntMatter");
	    });
		String expectedMessage = "Invalid position. Please choose either 'aisle', 'name', 'price', 'quantity', or 'reorder'.";
	    String actualMessage = exception.getMessage();
	    
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void updateTest_quantity()
	{
		if(!c.admin())
			c.authorize("shopMgr", "csc131");
		c.update("Whiskey","q","21");
	}
	
	@AfterAll
	void closeTest() 
	{
		//c.close();
	}
}
