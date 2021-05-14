package testCases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.*;

import main.*;

/**
 * 
 * @author  Sikander Ghafary
 * @version 05/12/2021
 */

public class CartNodeTester {
	
	Item i1 = new Item("PROD06", "produce", "lettuce", 2.50001, 1);	// Item Constructor i1 
	CartNode cart = new CartNode(i1);
	
	@Test
	public void addTest() {
		cart.add(i1);
	}
	
	@Test
	public void removeTest() {
		cart.remove();
		
	}
	
	@Test
	public void getPreviousTest() {
		cart.getPrevious();
		
	}

	@Test
	public void setPreviousTest() {
		cart.setPrevious(cart);
		
	}

	@Test
	public void getValueTest() {
		cart.getValue();
		
	}

	@Test
	public void setValueTest() {
		cart.setValue(i1);
		
	}

	@Test
	public void hasNextTest() {
		
		cart.hasNext();
	}

	@Test
	public void getNextTest() {
		cart.getNext();
		
	}

	@Test
	public void setNextTest() {
		cart.setNext(cart);
		
	}
	
}
