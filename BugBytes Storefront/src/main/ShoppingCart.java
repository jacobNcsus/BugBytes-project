	package main;
/**
 * 	A ShoppingCart is an List of Item objects, with 
 * 	functionality similar to that used in virtual storefronts.
 *
 * 	@author Jacob Normington
 * 	@version 5/5/2021
 */
public class ShoppingCart
{
	private static double TAX_RATE = 0.07;
	private static double SHIPPING_RATE = 10.00;
	private static final String scriptName = "BugBytes_shop_script.sql";
	
	private String customerName;
	private int id; //user id
	private Connector c; 
    
	private CartNode head; //beginning of list
	private CartNode tail; //end of list
	private CartNode current; //an iterator
	private int size; //the total number of items in the cart


	public static void main(String[] args)
	{
		ShoppingCart cart = new ShoppingCart(1, "Jagannadha Chidella", false); //true to test if scriptRunner works
		cart.addToCart("Whiskey", 1);
		cart.printTotal();
	}
   
	/**
     * 	Constructor of a shopping cart matching a specified user. 1 for default user.
     * 
     * 	@param custID	the user's customer id number
     * 	@param name		the user's first and last name
     */
	private ShoppingCart(int custID, String name)
	{
	   customerName = name;
	   id = custID;
	   c=Connector.getCon();
	   
	   head = null;  
	   tail = head; 
	   size = 0;
   }
   
   	/**
   	 * 	Constructor of a shopping cart matching a specified user, and, if requested, 
   	 * 	resetting the associated database. The cart is then filled with any items saved
   	 * 	to your cart in the store's database from previous sessions.
   	 * 	
   	 * 	@throws IllegalArgumentException	if custID is non-positive
   	 * 	@param	custID		the user's customer id number
   	 * 	@param	name		the user's first and last name
   	 * 	@param	reset		whether the parent database should be reset
   	 */
   	public ShoppingCart(int custID, String name, boolean reset) throws IllegalArgumentException {
   		this(custID, name);
   		
   		if (custID < 1) {
   			throw new IllegalArgumentException("Customer id must be positive.");
   			
   		} else if (custID == 1) {
   			c.emptyCart(1);
   			customerName = "default";
   		}
   		
   		if (reset) {
   			c.runScript("lib\\" + scriptName);
   			//System.out.println(System.getProperty("user.dir"));
   			System.out.println("Database initialized.\n");
   			
   		} else if (custID > 1) { //It's a waste of time to update the cart if you first reset the database, it will be empty.
   			update();
   		}
   	}
   	
   	/**
   	 * 	Constructor used to create a new cart from an existing cart, particularly when signing after 
   	 * 	browsing as a default user. Exactly the same as ShoppingCart(int, String, boolean), except that 
   	 * 	the cart will then be populated with the contents of an existing cart. In the case that the same
   	 * 	item exists in the new user's cart and oldCart, the item in OldCart is used. 
   	 * 
   	 * 	@param	custID		the user's customer id number
   	 * 	@param	name		the user's first and last name
   	 * 	@param	reset		whether the parent database should be reset
   	 * 	@param	oldCart		a ShoppingCart holding items to be added to this cart
   	 */
   	public ShoppingCart(int custID, String name, boolean reset, ShoppingCart oldCart)
   	{
   		this(custID, name, reset);
   		if (oldCart != null && oldCart.getCartSize() > 1)
   		{
   			Item current = oldCart.first();
   			String productName = current.getName();
	   		int oldCartQuantity = current.getQuantity();
	   		if(containsName(productName)) //if cart have current, remove saved item, then add current quantity
	   		{
	   			removeFromCart(productName);
	   			addToCart(productName, oldCartQuantity);
	   		}
	   		else //if cart does not have current, add it
	   		{
	   			addToCart(productName, oldCartQuantity);
	   		}
	   			
   	   		while(oldCart.hasNext()) //does not include first
   	   		{
   	   			current = oldCart.next();
   	   			productName = current.getName();
   	   			oldCartQuantity = current.getQuantity();
   	   			if(contains(productName)) //if cart have current, remove saved item, then add current quantity
   	   			{
   	   				removeFromCart(productName);
   	   				addToCart(productName, oldCartQuantity);
   	   			}
   	   			else //if cart does not have current, add it
   	   			{
   	   				addToCart(productName, oldCartQuantity);
   	   			}
   	   		}
   		}
   	}
   
   	/**
     * 	Returns the name of this cart's owner
     *
     * 	@return    a String representation of customer's name
     */
   	public String getCustomerName()
   	{
   		return customerName;
   	}
   	
   	/**
     * 	Returns the id of this cart's owner
     *
     * 	@return    a positive integer uniquely identifying the cart
     */
   	public int getCustomerId()
   	{
   		return id; 
   	}
   
   	/**
   	 * 	Returns the number of items in the cart
   	 *
   	 * 	@return    total number of items in cart
   	 */
   	public int getCartSize()
   	{
   		return size;
   	}
   
   	/**
   	 * 	Adds a new item to the shopping cart at the end of the list
   	 * 	Not reflected in database. 
   	 *
   	 * 	@param  item   an item to be added to the cart
   	 */
   	private void addItem(Item item)
   	{
   		if (head == null)
      	{
    	  	head = new CartNode(item);
    	  	tail = head; 
      	}
      	else
      	{
    	  	tail.add(item);
    	  	tail = tail.getNext(); 
      	}
      	size += item.getQuantity();
   	}
   
   	/**
   	 * 	Removes an item from the cart.
   	 * 	Not reflected in database.
   	 *
   	 * 	@param		prodID		a string representing the item in the database
   	 * 	@return				the item removed from the list, or null if not found
   	 */
   	private Item removeItem(String prodID)
   	{
   		prodID = prodID.toUpperCase();
   		
   		if (size > 0)
   		{
   			CartNode node = head;
   			if (head.getValue().getProductId().equalsIgnoreCase(prodID)) //remove first
   			{
   				head = head.getNext();
   				if(node.getNext() != null) //there is a second node
   				{
   					node.getNext().setPrevious(null); //remove link from next node
   				}
   				size -= node.getValue().getQuantity();
   				return node.getValue();
   			}
   			
   			while(node.hasNext()) //does not include first or last element
   			{
   				if (node != head)
   				{
   					if (node.getValue().getProductId().equalsIgnoreCase(prodID))
   	   				{ 
   	   					node.remove();
   	   					size -= node.getValue().getQuantity(); 
   	   					return node.getValue();
   	   				}
   				}
   				node = node.getNext(); 
   			}
   			
   			if (node != head && tail.getValue().getProductId().equalsIgnoreCase(prodID)) //remove last
   			{
   				tail = tail.getPrevious(); 
   				node.getPrevious().setNext(null); //remove link from previous node
   				size -= node.getValue().getQuantity(); 
   				return node.getValue(); 
   			}
   		}
   		System.out.println("Item not found in cart. Nothing removed.");
   		return null;
   	}
   
   	/**
   	 * 	Alters the quantity of an item in the cart. 
   	 * 	Not reflected in database.
   	 * 
   	 * 	@param	prodID		a string representing the item in the database
   	 * 	@param	amount		the number of item user wants to buy
   	 */
   	private void changeQuantity(String prodID, int amount)
   	{
   		prodID = prodID.toUpperCase();
   		
   		if (size > 0)
   		{
   			CartNode node = head; 
   			while(node.hasNext()) //does not include last element
   			{
   				if (node.getValue().getProductId().equalsIgnoreCase(prodID))
   				{ 
   					size -= node.getValue().getQuantity();
   					node.getValue().setQuantity(amount);
   					size += amount;
   					return;
   				}
   				node = node.getNext(); 
   			}
   			if (tail.getValue().getProductId().equalsIgnoreCase(prodID))
   			{
   				size -= node.getValue().getQuantity();
   				tail.getValue().setQuantity(amount);
   				size += amount;
   				return;
   			}
   		}
   		System.out.println("Item not found in cart. No change.");
   	}
   
   	/**
   	 *	Adds an item to the shopping cart from the database. Not case sensitive. 
   	 * 
   	 * 	@throws 	IllegalArgumentException	if no such item exists in the database
   	 * 	@param		name		the name of the item to be added
   	 * 	@param		quantity	the quantity of that item to be purchased
   	 * 	@return        		none
   	 */
   	public void addToCart(String name, int quantity)
   	{
   		Connector.capitalizeFirstLetter(name);
   		
   		//find item
   		String prodID = c.getProductID(name);	// converts to prodID from Product_name 
   		if (prodID == null)
   			throw new IllegalArgumentException("No product of that name exists: " + name + ".");
   		String category = c.readItem(prodID, "PRODUCT_TYPE");
   		double price = Double.parseDouble(c.readItem(prodID, "PRICE")); 

   		addItem(new Item(prodID, category, name, price, quantity)); //update cart
   		
   		c.addToCart(id, prodID, quantity); //update database
   	}
   
   	/**
   	 * 	Removes an item from the shopping cart. Not case sensitive.
   	 * 
   	 * 	@throws 	IllegalArgumentException	if no such item exists in the database	
   	 *	@param		name		the name of the item to be added
   	 * 	@return        	none
   	 */
   	public void removeFromCart(String name)
   	{
   		Connector.capitalizeFirstLetter(name);
   		
   		//find item
   		String prodID = c.getProductID(name);
   		if (prodID == null)
   			throw new IllegalArgumentException("No product of that name exists: " + name + ".");
   		
   		removeItem(prodID); //update cart
      
   		c.removeFromCart(id, prodID); //update database
   	}
   	
   	/**
   	 *	Changes the quantity of an item in the cart. Not case sensitive.
   	 *
   	 *	@throws 	IllegalArgumentException	if no such item exists in the database	
   	 *	@param		name		the name of the item to be added
   	 *	@param 			quantity	the number of this item the cart should have		
   	 */
   	public void changeCartQuantity(String name, int quantity)
   	{
   		Connector.capitalizeFirstLetter(name);
   		
   		//find item
   		String prodID = c.getProductID(name);
   		if (prodID == null)
   			throw new IllegalArgumentException("No product of that name exists: " + name + ".");
   		
   		changeQuantity(prodID, quantity); //update cart
	   	
	   	c.updateCart(id, prodID, quantity); //update database
   	}
   
   	/**
   	 * 	Determines whether the cart contains a certain item.
   	 * 
   	 * 	@param 		prodID		a String uniquely identifying one of the store's items
   	 * 	@return			true, if the cart contains such an item, false otherwise
   	 */
   	public boolean contains(String prodID)
   	{
   		prodID = prodID.toUpperCase();
   		
   		if (size > 0)
   		{
 		   CartNode node = head; 
 		   while(node.hasNext()) //does not include last element
 		   {
 			   if (node.getValue().getProductId().equalsIgnoreCase(prodID))
 		       { 
 				   return true;
 		       }
 			   node = node.getNext(); 
 		   }
 		   if (tail.getValue().getProductId().equalsIgnoreCase(prodID))
 		   {
 			   return true;
 		   }
   		}
   		return false;
   	}
   	
   	/**
   	 * 	Determines whether the cart contains a certain item. Not case sensitive.
   	 * 
   	 *	@throws 	IllegalArgumentException	if no such item exists in the database	
   	 * 	@param 		name		the product's name
   	 * 	@return			true, if the cart contains such an item, false otherwise
   	 */
   	public boolean containsName(String name)
   	{
   		Connector.capitalizeFirstLetter(name);
   		
   		String prodID = c.getProductID(name);
   		if (prodID == null)
   			throw new IllegalArgumentException("No product of that name exists: " + name + ".");
   		return contains(prodID);
   	}
   	
   	/**
   	 * 	Finds the quantity of a specific item in cart. Not case sensitive.
   	 * 
   	 * 	@param 		name	the item's name
   	 * 	@return			0, if the cart does not contain that item, or else the item's quantity
   	 */
   	public int getQuantity(String name)
   	{
   		Connector.capitalizeFirstLetter(name);
   		
   		if (size > 0)
   		{
 		   CartNode node = head; 
 		   while(node.hasNext()) //does not include last element
 		   {
 			   if (node.getValue().getName().equalsIgnoreCase(name))
 		       { 
 				   return node.getValue().getQuantity();
 		       }
 			   node = node.getNext(); 
 		   }
 		   if (tail.getValue().getName().equalsIgnoreCase(name))
 		   {
 			  return node.getValue().getQuantity();
 		   }
   		}
   		return 0;
   	}
   
   	/**
   	 *	Removes from the cart all items. 
   	 */
   	public void clearCart()
   	{
   		c.emptyCart(id); //clears database
	   
   		size = 0;
   		head.getNext().setPrevious(null); //remove reference from second
   		head = null; //remove reference from head
   		//garbage collector will delete all the stray nodes in frontend cart
   	}
   
   	/**
   	 * 	Searches through the database for changes which need to be made to the cart, 
   	 * 	then adds all contents of the cart to 
   	 */
   	private void update()
   	{
   		c.fillCart(this);
   	}
   
   	/**
   	 * 	Determines and returns the total cost of items in cart.
   	 *
   	 * 	@return    the subtotal of cart's items before tax or shipping
   	 */
   	public double getSubtotal()
   	{
   		double cartPrice = 0;
      	if (size > 0)
      	{
		   	CartNode node = head; 
		   	while(node.hasNext()) //does not include last element
		   	{
			   	cartPrice += node.getValue().getTotalCost(); 
			   	node = node.getNext(); 
		   	}
		   	cartPrice += tail.getValue().getTotalCost(); 
      	}
	  	return Connector.round(cartPrice, 2);
   	}
   
   	/**
     * 	Determines and returns the tax to be collected for all items in the cart. 
     *
     * 	@return    the total tax to be collected
     */
   	public double getTax()
   	{
   		return Connector.round(getSubtotal()*TAX_RATE, 2);
   	}
   	
   	/**
     * 	Determines and returns the shipping and handling price to be collected. 
     *
     * 	@return    the total shipping cost to be charged
     */
   	public double getShipping()
   	{
   		return SHIPPING_RATE;
   	}
   	
   	/**
     * 	Determines and returns the final cost to be charged for items in the cart. 
     *
     * 	@return    the total cost of the user's shopping cart
     */
   	public double getTotalCost()
   	{
   		return Connector.round(getSubtotal() * (1+TAX_RATE) + SHIPPING_RATE, 2);
   	}
   
   	/**
   	 * 	Prints out the cart with item prices
   	 */
   	public void printTotal()
   	{
   		System.out.println(customerName + "'s Shopping Cart");
      	System.out.println("Number of Items: " + getCartSize());
   		
   		if(size != 0)
      	{
   			CartNode node = head; 
          	double subtotal = 0.0;
          	while(node.hasNext())
          	{
        	  	System.out.println("\t" + node.getValue());
        	  	subtotal += node.getValue().getTotalCost(); 
        	  	node = node.getNext(); 
          	}
          	System.out.println("\t" + node.getValue());
          	subtotal += tail.getValue().getTotalCost(); 
          	subtotal = Connector.round(subtotal, 2);
          	
          	System.out.println("Subtotal:  $" + subtotal);
          	double tax = Connector.round(subtotal*TAX_RATE, 2);
          	System.out.println("Tax:       $" + tax);
          	System.out.println("Shipping:  $" + SHIPPING_RATE);
          	double total = Connector.round(subtotal+tax+SHIPPING_RATE, 2);
          	System.out.println("Total:     $" + total); 
      	}
   		System.out.println(); //spacing
   	}
   
   	/**
   	 * 	Moves cursor to reference the first item in the cart.
   	 * 
   	 * 	@return		the first item
   	 */
   	public Item first()
   	{
   		current = head;
   		return current.getValue();
   	}
   
   	/**
   	 * 	Moves cursor to reference the next item in the cart.
   	 * 
   	 * 	@return		the next item
   	 */
   	public Item next()
   	{
   		current = current.getNext();
   		return current.getValue();
   	}
   
   	/**
   	 * 	Determines whether there are more items in the cart not yet read. 
   	 * 
   	 * 	@return		true, if there are one or more items in the cart after the cursor, false otherwise
   	 */
   	public boolean hasNext()
   	{
   		return current.hasNext();
   	}
}

/**
 * 	Node in a doubly-linked list of Item objects
 */
class CartNode
{
	private CartNode previous; 
	private Item value;
	private CartNode next; 
	
	public CartNode(Item val)
	{
		value = val; 
		previous = null; 
		next = null; 
	}
	
	public CartNode(Item val, CartNode previous)
	{
		value = val; 
		this.previous = previous; 
	}
	
	public void add(Item next)
	{
		this.next = new CartNode(next, this); 
	}
	
	/**
	 * Removes a node from the list
	 *
	 * @throws	IllegalArgumentException
	 */
	public void remove()
	{
		if (previous != null && next != null) //not the first element
		{
			previous.next = next; //link previous to next
			next.previous = previous; //link next back to previous
		}
		else
			throw new IllegalArgumentException("Cannot remove head or tail of list"); 
	}

	public CartNode getPrevious() {
		return previous;
	}
	public void setPrevious(CartNode previous) {
		this.previous = previous;
	}

	public Item getValue() {
		return value;
	}
	public void setValue(Item value) {
		this.value = value;
	}

	public boolean hasNext() {
		return next != null; 
	}
	public CartNode getNext() {
		return next;
	}
	public void setNext(CartNode next) {
		this.next = next;
	}

}
