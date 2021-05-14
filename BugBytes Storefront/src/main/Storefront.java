package main;
/**
 * 	A representation of a virtual storefront pulled from the shop_test database. 
 *
 * 	@author Jacob Normington
 * 	@version 5/11/2021
 */
public class Storefront 
{
	private Connector c; //Connector ensures this is synchronized with ShoppingCart
	private int[] productNums; 
	
	/**
	 * 	Default constructor of class Storefront, creates a new connection to the store front. 
	 */
	public Storefront ()
	{
		c = Connector.getCon();
		
		int numAisles = Connector.aisles.length;
		productNums = new int[numAisles];
		for (int i = 0; i < numAisles; i++)
		{
			productNums[i] = c.getHighestProductID(Connector.aisles[i]);
			//System.out.println(productNums[i]);
		}
	}
	
	//print methods
	/**
	 * 	Prints the entire store inventory. 
	 */
	public void printInventory()
	{
		c.read("products", -1); //whole inventory
	}
	
	/**
	 * 	Prints the list of aisles in the store. 
	 */
	public void printAisles()
	{
		Connector.showShop();
	}
	/**
	 * 	Prints the contents of an aisle. 
	 * 
	 * 	@param		aisle	a String representing one of the aisles in the store
	 */
	public void printAisle(String aisle)
	{
		c.read(aisle, -1); //whole aisle
	}
	
	/**
	 * 	Prints out all the items in every user's cart. Requires authorization. 
	 */
	public void printCarts()
	{
		c.read("cart", -1); //all carts
	}
	/**
	 * 	Prints out the items in a single user's cart.
	 * 
	 * 	@param 		custID	a positive integer customer id
	 */
	public void printMyCart(int custID)
	{
		c.read("cart", custID); //only one cart
	}
	
	/**
	 * 	Prints out the list of customers' accounts. Requires authorization. 
	 */
	public void printCustomers()
	{
		c.read("customer", -1); //all customers
	}
	/**
	 * 	Prints out the information of a specified customer. 
	 * 
	 * 	@param 		custID	a positive integer customer id
	 */
	public void printCustomer(int custID)
	{
		c.read("customer", custID); //one customer
	}
	
	/**
	 * 	Prints out the list of orders issued by all customers. Requires authorization. 
	 */
	public void printOrders()
	{
		c.read("order", -1); //all orders
	}
	/**
	 * 	Prints out the list of orders issued by one customer.  
	 * 
	 * 	@param 		custID	a positive integer customer id
	 */
	public void printMyOrders(int custID)
	{
		c.read("order", custID); //orders from 1 customer
	}
	
	/**
	 * 	Prints out the details of all orders issued by all customers. Requires authorization. 
	 */
	public void printOrderDetails()
	{
		c.read("order_details", -1); //all orders
	}
	/**
	 * 	Prints out the contents of a specified order.   
	 * 
	 * 	@param 		orderID		a positive integer order id number
	 */
	public void printMyOrderDetails(int orderID)
	{
		c.read("order_details", orderID); //only as part of one order
	}
	
	//add methods
	/**
	 * 	Adds a new item to the store's inventory. Requires authorization. 
	 * 
	 * 	@param 		item		an Item to be added
	 * 	@param 		quantity	the number of the item added into stock
	 * 	@param 		restock		the quantity at which this item should be restocked
	 */
	public void addInventory(Item item, int quantity, int restock)
	{
		String name = item.getName();
		Connector.capitalizeFirstLetter(name);
		String aisle = item.getCategory();
		
		int index = 0;
		for (int i = 0; i < Connector.aisles.length; i++)
		{
			if (Connector.aisles[i].equalsIgnoreCase(aisle))
			{
				index = i;
				aisle = Connector.aisles[i];
				break;
			}
		}
		String prodID = "";
		switch(aisle)
		{
			case "Alcohol":
			{
				prodID = "ALC";
				break;
			}
			case "Bakery":
			{
				prodID = "BAKE";
				break;
			}
			case "Breakfast":
			{
				prodID = "BREAK";
				break;
			}
			case "Dairy":
			{
				prodID = "DIAR";
				break;
			}
			case "Meat_seafood":
			{
				prodID = "MEA";
				break;
			}
			case "Produce":
			{
				prodID = "PROD";
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Product type invalid, please choose 'Alcohol', 'Bakery', 'Dairy', 'Meat_seafood', or 'Produce'.");
			}
		}
		productNums[index]++;
		if (productNums[index] < 10)
		{
			prodID += "0"+productNums[index];
		}
		else if (productNums[index] < 100)
		{
			prodID += productNums[index];
		}
		else
		{
			throw new ArrayIndexOutOfBoundsException("This aisle is full, cannot add item.");
		}
		c.insert(prodID, aisle, name, item.getPrice(), quantity, restock);
	}
	
	//cart method in ShoppingCart
	
	/**
	 * 	Creates a new customer profile in the database. 
	 *
	 * 	@param		username	the customer's username
	 *	@param		firstName	the customer's first name
	 *	@param		lastName	the customer's last name
	 *	@param		email		the customer's email address
	 *	@param		phone		the customer's phone number
	 */
    public void signUp(String username, String firstName, String lastName, String email, String phone)
    {
    	String out = isCustomer(username, firstName, lastName);
    	System.out.println(out);
    	if (out.contains("There is no account registered with username: "))
    	{
    		c.signUp(c.getHighestCustomerID()+1, username, firstName, lastName, email, phone);
    	}
    	else
    	{
    		System.out.println("Sign up successful."); //for structuring
    	}
    }
	
    /**
	 * 	Checks out a cart and issues the associated order to the store.
	 * 
	 * 	@param 		cart	a ShoppingCart of items to be checked out
	 */
	public void checkout(ShoppingCart cart)
	{
		int custID = cart.getCustomerId();
		c.CONFIRM_ORDER(custID);
		   
		int orderID = c.placeOrder(custID, cart.getShipping(), cart.getTax(), cart.getTotalCost()); //issues a new order
		if (orderID < 1) //order was not placed
		{
			System.out.println("Checkout failed. Please try again later.");
			return;
		}
		   
		Item i = cart.first(); //populates order
		int line = 1;
		do
		{
			c.addToOrder(orderID, line, i.getProductId(), i.getQuantity(), i.getPrice());
			   
			i = cart.next();
			line++;
		} while (cart.hasNext()); //doesn't include last element
		c.addToOrder(orderID, line, i.getProductId(), i.getQuantity(), i.getPrice()); //last element
		
		System.out.println(); //for structuring, placeOrder and addToOrder should be in one block
		   
		cart.clearCart();
	}
	
	//update methods
	/**
	 * 	Changes the location of an item in the store. Requires authorization. 
	 * 
	 * 	@param 		name	the product's name
	 * 	@param 		aisle	the aisle it should be moved to
	 */
	public void moveItem(String name, String aisle)
	{
		c.update(name, "a", aisle); //changes product's type to aisle
	}
	/**
	 * 	Changes the name of an item in the store. Requires authorization.
	 * 
	 * 	@param 		oldName		the product's current name
	 * 	@param 		newName		a new name the product should have
	 */
	public void changeProductName(String oldName, String newName)
	{
		c.update(oldName, "n", newName); //changes product's name to newName
	}
	/**
	 *	Changes the price of an item in the store. Requires authorization.
	 * 
	 * 	@param 		name	the product's name
	 * 	@param 		price	the new price to be charged for each unit
	 */
	public void setUnitPrice(String name, double price)
	{
		c.update(name, "p", ""+price); //changes product's cost to price
	}
	//inventory stock is controlled on the backend
	/**
	 * 	Changes the quantity at which an item should be reordered. Requires authorization.
	 * 
	 * 	@param 		name		the product's name
	 * 	@param 		reorder		a positive integer at which quantity this item should be restocked
	 */
	public void setReorder(String name, int reorder)
	{
		c.update(name, "r", ""+reorder); //changes product's reorder quantity to reorder
	}
	
	//cart methods in ShoppingCart
	
	/**
	 * 	Changes a customer's registered email address. 
	 * 
	 * 	@param 		custID	a positive integer uniquely identifying a customer
	 * 	@param 		email	the new email address
	 */
	public void changeEmail(int custID, String email)
	{
		c.changeAccount(custID, "e", email); //change customer's email address to email
	}
	/**
	 * 	Changes a customer's registered phone number 
	 * 
	 * 	@param 		custID			a positive integer uniquely identifying a customer
	 * 	@param 		phoneNumber		the new phone number to be used
	 */
	public void changePhone(int custID, String phoneNumber)
	{
		c.changeAccount(custID, "p", phoneNumber); //change customer's phone number to phoneNumber
	}
	
	//you cannot change an order once it is issued
	
	//remove methods
    /**
     * 	Removes an item from the store's inventory. Requires authorization.
     *  
     * 	@param 		name	the name of the item to be removed
     */
    public void removeInventory(String name)
    {
    	c.delete(name); //this is probably insufficient
    }
    
    //cart method in ShoppingCart
    
    /**
     * 	Removes a user's account from the database. 
     *  
     * 	@param 		custID	a positive integer identifier of the account to be removed
     */
    public void removeAccount(int custID)
    {
    	c.removeAccount(custID);
    }
    
    /**
     * 	Removes a user's account from the database. 
     *  
     * 	@param 		custID	a positive integer identifier of the account to be removed
     */
    public void cancelOrder(int orderID)
    {
    	c.cancelOrder(orderID);
    }
	
	//other
    /**
     * 	Determines whether this connection has admin privileges or not. 
     * 
     * 	@return			true, if this connection has admin privileges, false otherwise
     */
    public boolean isAdmin()
    {
    	return c.admin();
    }
    
    /**
     * 	Attempts to secure administrator privileges on this connection based on an issued login.
     * 
     * 	@param 		username	a String username required to access admin privileges
     * 	@param 		password	a String password required to access admin privileges
     */
	public void requestAuthorization(String username, String password)
	{
		c.authorize(username, password);
	}
	
	/**
	 * 	Determines whether a customer's information matches the database, or if username is reserved. 
	 * 
	 * 	@param 		username	the customer's username
	 * 	@param		firstName	the customer's first name
	 * 	@param		lastName	the customer's last name
	 * 	@return			a String description of the customer's validity, customer id if valid
	 */
	private String isCustomer(String username, String firstName, String lastName)
	{
		int ret = c.isCustomer(username, firstName, lastName);
		if (ret == 0) //username does not exist in database
		{
			return "There is no account registered with username: " + username;
		}
		else if (ret < 0) //username is taken
		{
			return "There is already a different account registered with username: " + username;
		}
		else //positive, matching account exists, ret is the customer's id
		{
			return "This account already exists. Your customer id is: " + ret;
		}
	}
	
	/**
	 * 	Logs into an existing account. 
	 * 
	 * 	@param 		username	the customer's username
	 * 	@param 		firstName	the customer's first name
	 * 	@param 		lastName	the customer's last name
	 * 	@return 		the customer's id number
	 */
	public int login(String username, String firstName, String lastName)
	{
		String cust = isCustomer(username, firstName, lastName);
		if (!cust.contains("This account already exists.")) //username is absent, or doesn't match
		{
			System.out.println(cust);
			return 0;
		}
		else //login does exist
		{
			int i;
			for (i = 0; i < cust.length(); i++) //removes useless data
			{
				if(cust.charAt(i)== ':')
				{
					break;
				}
			}
			String cid = cust.substring(i+1).trim(); //removes everything before and including colon, then removes the space surrounding custID
			int custID = Integer.parseInt(cid);
			//System.out.println(cust);
			if (custID > 1) //does not include default user
				System.out.println("Login successful.");
			return custID;
		}
	}
}
