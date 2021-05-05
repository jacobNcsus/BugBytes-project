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
		ShoppingCart cart = new ShoppingCart(1, "Jagannadha Chidella", true); //test if scriptRunner works
		cart.addToCart("ALC01", 1);
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
	   if (custID < 1)
	   {
		   throw new IllegalArgumentException("Customer id must be positive.");
	   }
	   else if (custID == 1)
	   {
		   c.emptyCart(1);
		   customerName = "default";
	   }
	   else
	   {
		 update();
	   }
	   
	   head = null;  
	   tail = head; 
	   size = 0;
   }
   
   	/**
   	 * 	Constructor of a shopping cart matching a specified user, and, if requested, resetting the associated database.
   	 * 
   	 * 	@param	custID		the user's customer id number
   	 * 	@param	name		the user's first and last name
   	 * 	@param	reset		whether the parent database should be reset
   	 */
   	public ShoppingCart(int custID, String name, boolean reset)
   	{
   		this(custID, name);
   		if (reset)
   		{
   			c.runScript("lib\\" + scriptName);
   			//System.out.println(System.getProperty("user.dir"));
   			System.out.println("Database initialized.\n");
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
   	 * 	@param  id   	a string representing the item in the cart
   	 * 	@return       the item removed from the list, or null if not found
   	 */
   	private Item removeItem(String id)
   	{
   		if (size > 0)
   		{
   			CartNode node = head; 
   			while(node.hasNext()) //does not include last element
   			{
   				if (node.getValue().getProductId().equals(id))
   				{ 
   					if(node == head)
   					{
   						head = null; 
   						node.getNext().setPrevious(null); //remove link from next node
   					}
   					else
   						node.remove();
   					size -= node.getValue().getQuantity(); 
   					return node.getValue();
   				}
   				node = node.getNext(); 
   			}
   			if (tail.getValue().getProductId().equals(id))
   			{
   				tail = null; 
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
   	 * 	@param	id		a string representing the item in the database
   	 * 	@param	amount		the number of item user wants to buy
   	 */
   	private void changeQuantity(String prodID, int amount)
   	{
   		if (size > 0)
   		{
   			CartNode node = head; 
   			while(node.hasNext()) //does not include last element
   			{
   				if (node.getValue().getProductId().equals(prodID))
   				{ 
   					node.getValue().setQuantity(amount);
   					return;
   				}
   				node = node.getNext(); 
   			}
   			if (tail.getValue().getProductId().equals(prodID))
   			{
   				tail.getValue().setQuantity(amount);
   				return;
   			}
   		}
   		System.out.println("Item not found in cart. No change.");
   	}
   
   	/**
   	 *	Adds an item to the shopping cart from the database. 
   	 * 
   	 * 	@param	prodID		a string representing the item in the database
   	 * 	@param	quantity	the quantity of that item to be purchased
   	 * 	@return        	none
   	 */
   	public void addToCart(String prodID, int quantity)
   	{
   		//find item
   		String category = c.readItem(prodID, "PRODUCT_TYPE");
   		String name = c.readItem(prodID, "PRODUCT_NAME");
   		double price = Double.parseDouble(c.readItem(prodID, "PRICE")); 
	   
	   
   		addItem(new Item(prodID, category, name, price, quantity)); //update cart
	   
   		c.addToCart(id, prodID, quantity); //update database
   	}
   
   	/**
   	 * Removes an item from the shopping cart. 
   	 * 
   	 * @param   prodID		a string representing the item in the database
   	 * @return        	none
   	 */
   	public void removeFromCart(String prodID)
   	{
   		removeItem(prodID); //update cart
      
   		c.removeFromCart(id, prodID); //update database
   	}
   
   	/**
   	 *	Changes the quantity of an item in the cart. 
   	 *
   	 *	@param	prodID		a string representing the item in the database
   	 *	@param 	quantity	the number of this item the cart should have
   	 */
   	public void changeCartQuantity(String prodID, int quantity)
   	{
	   	changeQuantity(prodID, quantity); //update cart
	   	
	   	c.updateCart(id, prodID, quantity); //update database
   	}
   
   	/**
   	 * 	Determines whether the cart contains a certain item.
   	 * 
   	 * 	@param 		prodID	a String uniquely identifying one of the store's items
   	 * 	@return		true, if the cart contains such an item, false otherwise
   	 */
   	public boolean contains(String prodID)
   	{
   		if (size > 0)
 	   {
 		   CartNode node = head; 
 		   while(node.hasNext()) //does not include last element
 		   {
 			   if (node.getValue().getProductId().equals(prodID))
 		       { 
 				   return true;
 		       }
 			   node = node.getNext(); 
 		   }
 		   if (tail.getValue().getProductId().equals(prodID))
 		   {
 			   return true;
 		   }
 	   }
   		return false;
   	}
   
   	/**
   	 *	Removes from the cart all items. 
   	 */
   	public void clearCart()
   	{
   		c.emptyCart(id); //clears database
	   
   		head.getNext().setPrevious(null); //remove reference from second
   		head = null; //remove reference from head
   		//garbage collector will delete all the stray nodes in frontend cart
   	}
   
   	/**
   	 * 	Searches through the database for changes which need to be made to the cart, 
   	 * 	then adds all contents of the cart to 
   	 */
   	public void update()
   	{
   		c.fillCart(this);
   	}
   
   	/**
   	 * 	Checks out cart and clears its contents. 
   	 */
   	public void checkout()
   	{
   		c.CONFIRM_ORDER(id);
	   
   		c.placeOrder(id, getShipping(), getTax(), getTotalCost()); //issues a new order
	   
   		CartNode current = head; //populates order
   		int line = 0;
   		while (current.hasNext())
   		{
   			Item i = current.getValue();
   			c.addToOrder(1, line, i.getProductId(), i.getQuantity(), i.getPrice()); //does not account for more than one order
		   
   			current = current.getNext();
   			line++;
   		}
   		System.out.println(); //for structuring, placeOrder and addToOrder should be in one block
	   
   		c.emptyCart(id); //clears database
	   
   		head.getNext().setPrevious(null); //remove reference from second
   		head = null; //remove reference from head
   		//garbage collector will delete all the stray nodes in frontend cart
   	}
   
   	/**
   	 * 	Determines and returns the total cost of items in cart.
   	 *
   	 * 	@return    total check out price of the cart
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
	  	return cartPrice;
   	}
   
   	/**
     * 	Determines and returns the tax to be collected for all items in the cart. 
     *
     * 	@return    the total tax to be collected
     */
   	public double getTax()
   	{
   		return getSubtotal()*TAX_RATE;
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
   		return getSubtotal() * (1+TAX_RATE) + SHIPPING_RATE;
   	}
   
   	/**
   	 * 	Prints out the cart with item prices
   	 */
   	public void printTotal()
   	{
   		if(size == 0)
      	{
   			System.out.println("SHOPPING CART IS EMPTY");
      	}
      	else
      	{
    	  	System.out.println(customerName + "'s Shopping Cart");
          	System.out.println("Number of Items: " + getCartSize());
          	CartNode node = head; 
          	int total = 0;
          	while(node.hasNext())
          	{
        	  	System.out.println("\t" + node.getValue());
        	  	total += node.getValue().getTotalCost(); 
        	  	node = node.getNext(); 
          	}
          	System.out.println("\t" + node.getValue());
          	total += tail.getValue().getTotalCost(); 
          	System.out.println("Total: $" + total + "\n"); //spacing
      	}
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
