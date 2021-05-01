import java.sql.*;

/**
 * Creates a connection to the shopping
 *
 * @author Jacob Normington, Daniel Beauchamp, Youser Alalusi
 * @version 4/28/2021
 */
public class Connector 
{
	private static final String url = "jdbc:mysql://localhost:3306/shop_test";
	private static final String username = "shopMgr";
    private static final String password = "csc131"; 
    public static final String[] aisles = {"Alcohol", "Bakery", "Breakfast", "Dairy", "Meat_seafood", "Produce"};
    
    private Connection myConn;
	private String query;
    
    private static Connector singleton = new Connector(); 
	
	private Connector() //do not use
	{
		try
		{
			// 1. Get a connection to the database
			myConn = DriverManager.getConnection(url, username, password);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a singleton object of Connector in order to access the database. 
	 * 
	 * @return		a Connector object attached to the shop_test database
	 */
	public static Connector getCon()
	{
		return singleton;
	}
	
	public static void main(String[] args)
	{
		Connector c = Connector.getCon();

		//c.printAll();
		//c.printAisle(aisles[0]);
		//c.printAisle(aisles[1]);
		//c.printAisle(aisles[2]);
		//c.printAisle(aisles[3]);
		//c.printAisle(aisles[4]);
		//c.printAisle(aisles[5]);
		
		c.clearOrders();
		c.emptyCart(1);
		c.purgeLogins();
		c.signUp(1, "user1", "first", "last", "foo@bar.com", "18000000000");
		//c.printCart(1);
		c.addToCart(1, "ALC01", 2);
		//c.printCart(1);
		c.updateCart(1, "ALC01", 5);
		//c.printCart(1);
		c.addToCart(1, "ALC02", 2);
		c.addToCart(1, "ALC03", 1);
		//c.printCart(1);
        //c.CONFIRM_ORDER(1);
        //c.placeOrder(1, 10.00, 5.4544, 93.3744); //rounds off the last two decimals
        //c.addToOrder(1, 1, "ALC01", 5, 10.9913);
        //c.addToOrder(1, 2, "ALC02", 2, 5.991);
        //c.addToOrder(1, 3, "ALC03", 1, 10.991);
        //System.out.println();
        //c.emptyCart(1); 
        //c.printCart(1); //checks that cart is now empty
		
		//Connector.showShop();
		
		//c.insert("ALC07", aisles[0], "Hennesy", 20.00, 10, 5); 
		//c.printAisle(aisles[0]);
		//c.printAll();
		
		c.read("products");
		c.read("Alcohol");
		c.read("Meat_seafood");
		c.read("cart");
		c.read("customer");
		c.read("order");
		c.read("order_details");

		c.close();
	}
	
	/**
	 * Closes the connection to the database once it is no longer needed. 
	 */
	public void close()
	{
		try
		{
			myConn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	/**
	 * Reads an item from the inventory
	 *
	 * @param	id		a series of alphanumeric characters representing a unique product
	 * 			column 	the quantity you want to find
	 * @return        	a String representation of the column value
	 */
	public String readItem(String id, String column) 
	{
		try
		{
			// 2. Create a statement
			PreparedStatement myStmt = myConn.prepareStatement("select * from products where PRODUCT_ID = ?");
			myStmt.setString(1, id); //1 specifies the first parameter in the query
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery();
			// 4. Process the result set 
			myRs.next();
			String ret = myRs.getString(column);
			
			myStmt.close();
			return (String) ret;
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
		return null;
	}
	
	/**
	 * Prints out an aisle/category from the shop database. 
	 *
	 * @throws	IllegalArgumentException	if aisle is not one of the valid aisles
	 * @param	aisle	a String object of the aisle to be displayed, either 'Alcohol', 'Bakery', 'Breakfast', 'Dairy', 'Meat_seafood', or 'Produce'
	 */
	public void printAisle(String aisle) 
	{
		if ( ! isAisle(aisle) & ! aisle.equalsIgnoreCase("products"))
		{
			throw new IllegalArgumentException("Product type invalid, please choose 'Alcohol', 'Bakery', 'Dairy', 'Meat_seafood', or 'Produce'.");
		}
		
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from products where PRODUCT_TYPE='" + aisle + "'");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("PRODUCT_NAME") + ", " 
			+ myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY_IN_STOCK") + ", " + myRs.getString("REORDER"));
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	/**
	 * Prints to console every product in the inventory. 
	 */
	public void printAll() 
	{
		try
		{  
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from products");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("PRODUCT_NAME") + ", " 
			+ myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY_IN_STOCK") + ", " + myRs.getString("REORDER"));
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	} 
	
	/**
	 * Prints to console some section of the database in structured text. 
	 * 
	 * @throws	IllegalArgumentException	if tableView does not represent a valid table (products, cart, customer, some aisle, etc.)
	 * @param	tableView	a String describing the table to be read
	 */
	public void read(String tableView) 
	{
		try
		{
			if (tableView.equals("products")) //display entire inventory
			{
				query = "select * from products";

				Statement myStmt = myConn.createStatement();
				ResultSet myRs = myStmt.executeQuery(query);
			
				System.out.println("Store Inventory:");
				System.out.printf("%15s%21s%16s\n"
					+ "-------------------------------------------------------\n"
					,"Product","Price","Quantity");
				while(myRs.next())
				{ 
						System.out.printf("%-29s$ %6.2f%12s", 
								myRs.getString("PRODUCT_NAME"), myRs.getDouble("PRICE"), myRs.getInt("QUANTITY_IN_STOCK"));
						System.out.println();
				}
				System.out.println();
				myStmt.close(); 
			}
			
			else if (isAisle(tableView)) //display an aisle of the shop
			{
				query = "select * from products WHERE PRODUCT_TYPE= ?";

				PreparedStatement myStmt = myConn.prepareStatement(query);
				myStmt.setString(1, tableView); //1-indexed
				ResultSet myRs = myStmt.executeQuery();
			
				System.out.println(tableView + " Aisle:");
				System.out.printf("%15s%21s%16s\n"
					+ "-------------------------------------------------------\n"
					,"Product","Price","Quantity");
				while(myRs.next())
				{ 
						System.out.printf("%-29s$ %6.2f%12s", 
								myRs.getString("PRODUCT_NAME"), myRs.getDouble("PRICE"), myRs.getInt("QUANTITY_IN_STOCK"));
						System.out.println();
				} 
				System.out.println();
				myStmt.close(); 
			}
			
			else if (tableView.equals("cart")) //display a user's cart
			{
				query = "select * from cart WHERE CUSTOMER_ID_CART= ?";
				query = "SELECT c.PRODUCT_ID, c.QUANTITY_ORDERED, c.TOTAL_COST, cust.USERNAME FROM cart c LEFT JOIN customer cust ON c.CUSTOMER_ID_CART = cust.CUSTOMER_ID WHERE CUSTOMER_ID_CART=?";

				PreparedStatement myStmt = myConn.prepareStatement(query);
				myStmt.setInt(1, 1); //1-indexed
				ResultSet myRs = myStmt.executeQuery();
			
				if(!myRs.next() ) //false if the list is empty
				{
					System.out.println("Cart is empty \n");
				}
				else //your cart is not empty
				{
					System.out.println(myRs.getString("USERNAME") + "'s Shopping Cart:");
					System.out.printf("%15s%21s%16s\n"
						+ "-------------------------------------------------------\n"
						,"Product","Quantity","Total Cost");
					do
					{ 
							System.out.printf("%-31s%-13s$ %6.2f", 
									myRs.getString("PRODUCT_ID"), myRs.getInt("QUANTITY_ORDERED"), myRs.getDouble("TOTAL_COST"));
							System.out.println();
					} while(myRs.next());
					System.out.println();
				}
				myStmt.close(); 
			}
			
			else if (tableView.equals("customer")) //displays the list of customers
			{
				query = "select * from customer";

				Statement myStmt = myConn.createStatement();
				ResultSet myRs = myStmt.executeQuery(query);
			
				if(!myRs.next() ) //false if the list is empty
				{
					System.out.println("There are no customers \n");
				}
				else //list is not empty
				{
					System.out.println("Customer List: ");
					System.out.printf("%-3s%-15s%-15s%-15s%-20s%-15s \n"
							+ "----------------------------------------------------------------------------------\n"
							,"ID","Username","First Name","Last Name","Email","Phone Number");
					do
					{ 
							System.out.printf("%-3s%-15s%-15s%-15s%-20s%-15s", 
									myRs.getString("CUSTOMER_ID"), myRs.getString("USERNAME"), myRs.getString("FIRST_NAME"), myRs.getString("LAST_NAME"), myRs.getString("EMAIL"), myRs.getString("PHONE"));
							System.out.println();
					} while(myRs.next());
					System.out.println();
				}
				myStmt.close(); 
			}
			
			else if (tableView.equals("order")) //display the list of orders
			{
				query = "select * from shop_test.order";

				Statement myStmt = myConn.createStatement();
				ResultSet myRs = myStmt.executeQuery(query);
			
				String output = "";
				
				if(!myRs.next() ) //false if the list is empty
				{
					System.out.println("There are no orders \n");
				}
				else //only if the list has entries
				{
					do
					{ 
							output = myRs.getInt("ORDER_ID") + " " +
									 myRs.getInt("CUSTOMER_ID") + " " +	
									 myRs.getString("ORDER_DATE") + " " +
									 myRs.getDouble("SHIPPING_COST") + " " +
									 myRs.getDouble("TAX") + " " +
									 myRs.getDouble("TOTAL_COST");
							System.out.println(output + "\n");
					} while(myRs.next());
				}
				myStmt.close(); 
			}
			
			else if (tableView.equals("order_details")) //display the order details associated with an order
			{
				System.out.println("Not finished");
			}
			
			else 
				throw new IllegalArgumentException("Invalid table choice.");
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}

	/**
	 * Adds a new item to inventory
	 *
	 * @throws	IllegalArgumentException	if type is not one of the six aisles
	 * 			IllegalArgumentException	if price is non-positive
	 * 			IllegalArgumentException	if quantity is non-positive
	 * @param	PRODUCT_ID			a series of alphanumeric characters representing a unique product
	 * 			PRODUCT_TYPE		the product type or category
	 * 			PRODUCT_NAME		the name of the item
	 * 			PRICE				the price of the item, rounded to two decimal places
	 * 			QUANTITY_IN_STOCK	the number of item in stock
	 * 			REORDER				the amount of stock at which this item should be restocked
	 * @return        		0 on failure, or 1 on success
	 */
	private int insert(String PRODUCT_ID, String PRODUCT_TYPE, String PRODUCT_NAME, double PRICE, int QUANTITY_IN_STOCK, int REORDER)
	{
		if ( ! isAisle(PRODUCT_TYPE))
		{
			throw new IllegalArgumentException("Product type invalid, please choose 'Alcohol', 'Bakery', 'Dairy', 'Meat_seafood', or 'Produce'.");
		}
		

		if (PRICE <= 0) //if price is non-positive

		{
			throw new IllegalArgumentException("Price invalid, please enter a price greater than zero.");
		}
		

		if (QUANTITY_IN_STOCK <= 0) //if quantity is non-positive

		{
			throw new IllegalArgumentException("Quantity invalid, please enter a quantity greater than zero.");
		}
		
		//other checks
		
		try //this doesn't maintain the integrity of the product id system
		{
			// 2. prepare a statement
			query = "insert into products "
					+ " (PRODUCT_ID, PRODUCT_TYPE, PRODUCT_NAME, PRICE, QUANTITY_IN_STOCK, REORDER)"
					+ " values (?, ?, ?, ?, ?, ?)";
			PreparedStatement myStmt = myConn.prepareStatement(query);
			// 3. Set the parameters
			myStmt.setString(1, PRODUCT_ID);
			myStmt.setString(2, PRODUCT_TYPE);
			myStmt.setString(3, PRODUCT_NAME);
			myStmt.setDouble(4, round(PRICE, 2));
			myStmt.setInt(5, QUANTITY_IN_STOCK);
			myStmt.setInt(6, REORDER);
			// 4. Execute a SQL query
			int rows = myStmt.executeUpdate(); 
			
			
			System.out.println("Insert complete. \n"); 
		
			myStmt.close(); 
			return rows; 
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
		return 0; 
	}
	
	/**
	 * 	An example method to change a value in a sample database
	 */
	private void update() //demo
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			query = "update employees "
					+ " set email='demo@luv2code.com'"
					+ " where id=9"; 
			myStmt.executeUpdate(query); 
			
			System.out.println("Update complete."); 

			myStmt.close(); 

		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	/**
	 * 	An example method to remove an entry from a sample database
	 */
	public void delete(String productID) //demo
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			query = "delete from products where PRODUCT_ID ='"+ productID + "'"; 
			int rowsAffected = myStmt.executeUpdate(query); 
			
			System.out.println("Rows affected: " + rowsAffected);
			System.out.println("Delete complete."); 

			myStmt.close(); 

		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	/**
	 * Reads an item from a user's shopping cart. 
	 *
	 * @param	name	the product id of the item 
	 * 			column 	the quantity you want to find
	 * @return        	a String representation of the column value
	 */
	public String readCart(String id, String column) 
	{
		return readItem(id, column); 
	}
	
	/**
	 * Reads an item from a user's shopping cart. 
	 *
	 * @throws	IllegalArgumentException	if custID is non-positive
	 * @param	custID	a positive integer, the id number of the customer
	 */
	public void printCart(int custID) 
	{
		if (custID < 1)
        {
        	throw new IllegalArgumentException("Invalid customer id. Please use an customer id greater than zero.");
        }
		
		System.out.println("User " + custID + "'s cart: ");
		
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from cart WHERE CUSTOMER_ID_CART='" + custID +"';");
			// 4. Process the result set 
			if (myRs.next() == false) 
			{ 
				System.out.println("Cart is empty"); 
			} 
			else 
			{ 
				do 
				{ 
					System.out.println(myRs.getString("CUSTOMER_ID_CART") + ", " + myRs.getString("PRODUCT_ID") + ", " + myRs.getString("QUANTITY_ORDERED") + ", " 
							+ myRs.getString("TOTAL_COST"));
				} while (myRs.next()); 
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	/**
	 * Adds a new item to a user's shopping cart. 
	 *
	 * @throws	IllegalArgumentException	if custID is non-positive
	 * 			IllegalArgumentException	if quantity is non-positive
	 * @param	custID		a positive integer, the id number of the customer
	 * 			prodID		a series of alphanumeric characters representing a unique product
	 * 			quantity	the number of this item to add
	 */
	public void addToCart(int custID, String prodID, int quantity) 
	{
		if (custID < 1)
        {
        	throw new IllegalArgumentException("Invalid customer id. Please use an customer id greater than zero.");
        }
		if (quantity < 1)
        {
        	throw new IllegalArgumentException("Invalid quantity. Please use a quantity greater than zero.");
        }
		
		// Retrieve product details
        Double price = 0.0;
        try 
        {
            Statement myStmt = myConn.createStatement();
            query = "SELECT * FROM products WHERE PRODUCT_ID=\"" + prodID + "\"";
            ResultSet myRs = myStmt.executeQuery(query);
            myRs.next();
            price = myRs.getDouble("PRICE"); 
            myStmt.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        // Add to cart
        try 
        {
            query = "INSERT INTO cart (CUSTOMER_ID_CART, PRODUCT_ID, QUANTITY_ORDERED, TOTAL_COST) VALUES (?,?,?,?)";
        	PreparedStatement myStmt = myConn.prepareStatement(query);
        	
        	myStmt.setInt(1, custID);
        	myStmt.setString(2, prodID);
        	myStmt.setInt(3, quantity);
        	myStmt.setDouble(4, round(price * quantity, 2));
        	
            myStmt.executeUpdate();
            System.out.println("Item successfully added to cart.\n");
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    }
    
	/**
	 * Removes an item from a user's shopping cart. 
	 *
	 * @throws	IllegalArgumentException	if custID is non-positive
	 * @param	custID		a positive integer, the id number of the customer
	 * 			prodID		a series of alphanumeric characters representing a unique product
	 */
    public void removeFromCart(int custID, String prodID) 
    {
    	if (custID < 1)
        {
        	throw new IllegalArgumentException("Invalid customer id. Please use an customer id greater than zero.");
        }
    	
    	try 
        {
            Statement myStmt = myConn.createStatement();
            query = "DELETE FROM cart WHERE PRODUCT_ID=\"" + prodID + "\" AND CUSTOMER_ID_CART=\"" + custID + "\""; 
            myStmt.executeUpdate(query);
            System.out.println("Item successfully removed from cart.\n");
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    }
    
    /**
	 * Removes all items from a user's shopping cart. 
	 *
	 * @throws	IllegalArgumentException	if custID is non-positive
	 * @param	custID		a positive integer, the id number of the customer
	 */
    public void emptyCart(int custID) 
    {
    	if (custID < 1)
        {
        	throw new IllegalArgumentException("Invalid customer id. Please use an customer id greater than zero.");
        }
    	
    	try 
        {
            Statement myStmt = myConn.createStatement();
            query = "DELETE FROM cart WHERE CUSTOMER_ID_CART=\"" + custID + "\""; 
            myStmt.executeUpdate(query);
            System.out.println("Cart emptied \n");
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    }
    
    /**
	 * Changes the quantity of an item in a user's cart.
	 *
	 * @throws	IllegalArgumentException	if custID is non-positive
	 * 			IllegalArgumentException	if quantity is non-positive
	 * @param	custID		a positive integer, the id number of the customer
	 * 			prodID		a series of alphanumeric characters representing a unique product
	 * 			quantity	the number of this item you now want to have
	 */
    public void updateCart(int custID, String prodID, int quantity) //really just changes quantity
	{
    	if (custID < 1)
        {
        	throw new IllegalArgumentException("Invalid customer id. Please use an customer id greater than zero.");
        }
		if (quantity < 1)
        {
        	throw new IllegalArgumentException("Invalid quantity. Please use a quantity greater than zero.");
        }
    	
    	try
		{
			query = "UPDATE cart SET QUANTITY_ORDERED=? WHERE PRODUCT_ID=? AND CUSTOMER_ID_CART=?"; //first, set quantity
        	PreparedStatement myStmt = myConn.prepareStatement(query);
        	
        	myStmt.setInt(1, quantity);
        	myStmt.setString(2, prodID);
        	myStmt.setInt(3, custID);
        	
            myStmt.executeUpdate();
            
            
            Double price = 0.0;
            query = "select * from products where PRODUCT_ID = ?"; // Retrieve product details
            myStmt = myConn.prepareStatement(query); 
         	myStmt.setString(1, prodID); //1 specifies the first parameter in the query
         	ResultSet myRs = myStmt.executeQuery();
         	myRs.next();
         	price = myRs.getDouble("PRICE");
            

            query = "UPDATE cart SET TOTAL_COST=? WHERE PRODUCT_ID=? AND CUSTOMER_ID_CART=?"; //then you have to update total price
        	myStmt = myConn.prepareStatement(query);
        	
        	myStmt.setDouble(1, price*quantity);
        	myStmt.setString(2, prodID);
        	myStmt.setInt(3, custID);
        	
            myStmt.executeUpdate();
            
            System.out.println("Update complete.\n");
            myStmt.close(); 

		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
    
    /**
	 * Final method to confirm an order is valid, and then turn it in to the server. 
	 *
	 * @throws	IllegalArgumentException	if custID is non-positive
	 * @param	custID		a positive integer, the id number of the customer
	 */
    public void CONFIRM_ORDER(int custID) 
    {
    	if (custID < 1)
        {
        	throw new IllegalArgumentException("Invalid customer id. Please use an customer id greater than zero.");
        }
    	
        try 
        {
            Statement myStmt = myConn.createStatement();
            query = "SELECT c.PRODUCT_ID, p.PRODUCT_NAME, c.QUANTITY_ORDERED, p.QUANTITY_IN_STOCK AS stockRemaining FROM cart c LEFT JOIN products p ON c.PRODUCT_ID = p.PRODUCT_ID WHERE CUSTOMER_ID_CART=\"" + custID+ "\"";
            ResultSet myRs = myStmt.executeQuery(query);
            while(myRs.next()) 
            {
                //System.out.println("Inside the while loop");
                if (myRs.getInt("stockRemaining") < myRs.getInt("QUANTITY_ORDERED")) 
                {
                    System.out.println("Insufficient inventory, restocking items");
                    System.out.println("Please try again later \n");
                    return;
                }
                else
                {
                	System.out.println(myRs.getString("PRODUCT_NAME") + ", " + myRs.getString("QUANTITY_ORDERED") + ": In stock");
                }
            }
            System.out.println("Order confirmed \n");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    /**
	 * Issues an order to the database. 
	 *
	 * @throws	IllegalArgumentException	if custID is non-positive
	 * 			IllegalArgumentException	if shipping is negative
	 * 			IllegalArgumentException	if tax is negative
	 * 			IllegalArgumentException	if total is negative
	 * @param	custID		a positive integer, the id number of the customer
	 * 			shipping	a double value representing the shipping price to be collected, rounded to two decimal places
	 * 			tax			a double value representing the tax to be collected, rounded to two decimal places
	 * 			total		a double value representing the total price to be charged for the order, rounded to two decimal places
	 */
    public void placeOrder(int custID, double shipping, double tax, double total) 
    {
    	if (custID < 1)
        {
        	throw new IllegalArgumentException("Invalid customer id. Please use an customer id greater than zero.");
        }
    	if (shipping < 0)
        {
        	throw new IllegalArgumentException("Invalid shipping. Please use a positive shipping price.");
        }
    	if (tax < 0)
        {
        	throw new IllegalArgumentException("Invalid tax cost. Please use a positive tax.");
        }
    	if (total < 0)
        {
        	throw new IllegalArgumentException("Invalid total cost. Please use a positive total.");
        }
        
    	try 
        {
        	query = "INSERT INTO shop_test.order (ORDER_ID, CUSTOMER_ID, ORDER_DATE, SHIPPING_COST, TAX, TOTAL_COST) VALUES (?,?,?,?,?,?)";
        	PreparedStatement myStmt = myConn.prepareStatement(query);
        	
        	myStmt.setInt(1, 1); //does not account for multiple orders
        	myStmt.setInt(2, custID);
        	myStmt.setDate(3, new java.sql.Date(System.currentTimeMillis())); //order date is now
        	myStmt.setDouble(4, round(shipping, 2));
        	myStmt.setDouble(5, round(tax, 2));
        	myStmt.setDouble(6, round(total, 2));
        	
            myStmt.executeUpdate();
            System.out.println("Order placed");
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    /**
	 * Adds a new item to an order. Follow by a newline. 
	 *
	 * @throws 	IllegalArgumentException	if orderID is non-positive
	 * 			IllegalArgumentException	if lineNumber is non-positive
	 * 			IllegalArgumentException	if quantity is non-positive
	 * 			IllegalArgumentException	if price is negative
	 * @param	orderID		a positive integer uniquely identifying the order this is a part of
	 * 			lineNumber	a positive integer representing which item in the order this is
	 * 			prodID		a series of alphanumeric characters representing a unique product
	 * 			quantity	a positive integer representing the number of an item to purchase
	 * 			price		a double representing the unit price of an item, rounded to two decimal places
	 */
    public void addToOrder(int orderID, int lineNumber, String prodID, int quantity, double price) 
    {
        
        if (orderID < 1)
        {
        	throw new IllegalArgumentException("Invalid order id. Please use an order id greater than zero.");
        }
        if (lineNumber < 1)
        {
        	throw new IllegalArgumentException("Invalid order line. Please use an order line number greater than zero.");
        }
        if (quantity < 1)
        {
        	throw new IllegalArgumentException("Invalid quantity. Please use a quantity greater than zero.");
        }
        if (price < 0)
        {
        	throw new IllegalArgumentException("Invalid price. Please use a price greater than zero.");
        }
    	
    	try 
        {
        	query = "INSERT INTO order_details (ORDER_ID, ORDER_LINE_NUMBER, PRODUCT_ID, ORDERED_QUANTITY, PRICE) VALUES (?,?,?,?,?)";
        	PreparedStatement myStmt = myConn.prepareStatement(query);
        	
        	myStmt.setInt(1, orderID); 
        	myStmt.setInt(2, lineNumber);
        	myStmt.setString(3, prodID); 
        	myStmt.setInt(4, quantity);
        	myStmt.setDouble(5, round(price, 2)*quantity); //should be total price, not unit price
        	
            myStmt.executeUpdate();
            System.out.println("Item added to order: " + lineNumber);
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    /**
	 * Removes all orders from the store database. 
	 */
    private void clearOrders() 
    {
        
        try 
        {
        	Statement myStmt = myConn.createStatement();
        	query = "DELETE FROM shop_test.order";
            myStmt.executeUpdate(query);
            myStmt.close(); 
            
            myStmt = myConn.createStatement();
            query = "DELETE FROM order_details";
            myStmt.executeUpdate(query);
            myStmt.close();
            
            System.out.println("All orders cleared \n");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    /**
	 *	Creates a new customer profile in the database. 
	 *
	 *	@throws		IllegalArgumentException	if id is non-positive
	 */
    public void signUp(int id, String username, String firstName, String lastName, String email, String phone)
    {
    	if (id < 1)
        {
        	throw new IllegalArgumentException("Invalid id. Please use a customer id greater than zero.");
        }
    	
    	try 
        {
    		// 2. Create a statement
    		Statement myStmt = myConn.createStatement();
    		// 3. Execute a SQL query
    		ResultSet myRs = myStmt.executeQuery("select * from customer WHERE CUSTOMER_ID='" + id +"';");
    		// 4. Process the result set 
    		if (myRs.next()) //if anything is in the result set
    		{
    			System.out.println("This account already exists.\n");
    			return;
    		}

        }
    	catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    	
    	try //create new account
    	{
    		query = "INSERT INTO customer (CUSTOMER_ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PHONE) VALUES (?,?,?,?,?,?)";
        	PreparedStatement myStmt = myConn.prepareStatement(query);
        	
        	myStmt.setInt(1, id);
        	myStmt.setString(2, username);
        	myStmt.setString(3, firstName);
        	myStmt.setString(4, lastName);
        	myStmt.setString(5, email);
        	myStmt.setString(6, phone);
        	
            myStmt.executeUpdate();
            myStmt.close(); 
            System.out.println("New account registered: " + username + "\n"); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    }
    
    /**
	 * Removes all login information from the store database. 
	 */
    private void purgeLogins() 
    {
        
        try 
        {
        	Statement myStmt = myConn.createStatement();
        	query = "DELETE FROM customer";
            myStmt.executeUpdate(query);
            myStmt.close(); 
            
            System.out.println("All customer information removed \n");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    private static double round (double value, int places) 
    {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    /**
     * Prints the aisles in the shop. 
     */
    public static void showShop()
    {
    	System.out.println("Virtual Storefront Aisles: ");
    	for (String aisle : aisles)
    	{
    		System.out.println("\t" + aisle);
    	}
    	System.out.println(); 
    }
    
    /**
     * Evaluates if a string parameter is one of the valid aisles. Not case sensitive. 
     * 
     * @param	s	a string to be checked against
     */
    public static boolean isAisle(String s)
    {
    	for (String aisle : aisles)
    	{
    		if (s.equalsIgnoreCase(aisle))
    		{
    			return true;
    		}
    	}
    	return false; 
    }
}
