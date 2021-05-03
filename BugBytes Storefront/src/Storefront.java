import java.util.StringTokenizer;

/**
 * A representation of a virtual storefront pulled from the shop_test database. 
 *
 * @author Jacob Normington
 * @version 5/2/2021
 */
public class Storefront 
{
	private Connector c; //Connector ensures this is synchronized with ShoppingCart
	private int[] productNums; 
	
	/**
	 * Default constructor of class Storefront, creates a new connection to the store front. 
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
	 * Prints the entire store inventory. 
	 */
	public void printInventory()
	{
		c.read("products", -1); //whole inventory
	}
	
	/**
	 * Prints the list of aisles in the store. 
	 */
	public void printAisles()
	{
		Connector.showShop();
	}
	/**
	 * Prints the contents of an aisle. 
	 * 
	 * @param	aisle	a String representing one of the aisles in the store
	 */
	public void printAisle(String aisle)
	{
		c.read(aisle, -1); //whole inventory
	}
	
	/**
	 * Prints out all the items in every user's cart. Requires authorization. 
	 */
	public void printCarts()
	{
		c.read("cart", -1); //all carts
	}
	/**
	 * Prints out the items in a single user's cart.
	 * 
	 * @param 	custID	a positive integer customer id
	 */
	public void printMyCart(int custID)
	{
		c.read("cart", custID); //only one cart
	}
	
	/**
	 * Prints out the list of customers' accounts. Requires authorization. 
	 */
	public void printCustomers()
	{
		c.read("customer", -1); //all customers
	}
	/**
	 * Prints out the information of a specified customer. 
	 * 
	 * @param 	custID	a positive integer customer id
	 */
	public void printCustomer(int custID)
	{
		c.read("customer", custID); //one customer
	}
	
	/**
	 * Prints out the list of orders issued by all customers. Requires authorization. 
	 */
	public void printOrders()
	{
		c.read("order", -1); //all orders
	}
	/**
	 * Prints out the list of orders issued by one customer.  
	 * 
	 * @param 	custID	a positive integer customer id
	 */
	public void printMyOrders(int custID)
	{
		c.read("order", custID); //orders from 1 customer
	}
	
	/**
	 * Prints out the details of all orders issued by all customers. Requires authorization. 
	 */
	public void printOrderDetails()
	{
		c.read("order_details", -1); //all orders
	}
	/**
	 * Prints out the contents of a specified order.   
	 * 
	 * @param 	orderID		a positive integer order id number
	 */
	public void printMyOrderDetails(int orderID)
	{
		c.read("order_details", orderID); //only as part of one order
	}
	
	//add methods
	/**
	 * Adds a new item to the store's inventory. Requires authorization. 
	 * 
	 * @param 	item		an Item to be added
	 * @param 	quantity	the number of the item added into stock
	 * @param 	restock		the quantity at which this item should be restocked
	 */
	public void addInventory(Item item, int quantity, int restock)
	{
		int index = 0;
		for (int i = 0; i < Connector.aisles.length; i++)
		{
			if (Connector.aisles[i].equals(item.getCategory()))
			{
				index = i;
				break;
			}
		}
		String prodID = "";
		switch(item.getCategory())
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
			throw new ArrayIndexOutOfBoundsException();
		}
		
		c.insert(prodID, item.getCategory(), item.getName(), item.getPrice(), quantity, restock);
	}
	//cart method in ShoppingCart
	/**
	 * 	Creates a new customer profile in the database. 
	 *
	 * 	@param		username	the customer's username
	 *				firstName	the customer's first name
	 *				lastName	the customer's last name
	 *				email		the customer's email address
	 *				phone		the customer's phone number
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
    		System.out.println(); //for structuring
    	}
    }
	
	//update methods
	
	//remove methods
    /**
     * 	Removes an item from the store's inventory. Requires authorization.
     *  
     * 	@param 	name	the name of the item to be removed
     */
    public void removeInventory(String name)
    {
    	c.delete(name); //this is probably insufficient

    }
	
	//other
    /**
     * 	Determines whether this connection has admin privileges or not. 
     * 
     * 	@return	true, if this connection has admin privileges, false otherwise
     */
    public boolean isAdmin()
    {
    	return c.admin();
    }
    
    /**
     * 	Attempts to secure administrator privileges on this connection based on an issued login.
     * 
     * 	@param 	username	a String username required to access admin privileges
     * 	@param 	password	a String password required to access admin privileges
     */
	public void requestAuthorization(String username, String password)
	{
		c.authorize(username, password);
	}
	
	/**
	 * Determines whether a customer's information matches the database, or if username is reserved. 
	 * 
	 * @param 	username	the customer's username
	 * 			firstName	the customer's first name
	 * 			lastName	the customer's last name
	 * @return		a String description of the customer's validity, customer id if valid
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
			int custID = 0;
			StringTokenizer token = new StringTokenizer(cust, ":");
			token.nextToken(); //removes meaningless information
			String num = token.nextToken();
			num = num.substring(1);
			System.out.println(num);
			custID = Integer.parseInt(num);
			
			if (custID > 1) //does not include default user
				System.out.println("Login successful");
			return custID;
		}
	}
}
