package main;
import java.io.IOException;
import java.sql.*;
import script.ScriptRunner;

/**
 * 	Creates a connection to the BugBytes virtual store. 
 *
 * 	@author Jacob Normington, Daniel Beauchamp, Youser Alalusi
 * 	@version 5/5/2021
 */
public class Connector 
{
	private static final String url = "jdbc:mysql://localhost:3306/shop_test";
	private static final String username = "shopMgr"; //"shopMgr"
    private static final String password = "csc131"; //"csc131"
    public static final String[] aisles = {"Alcohol", "Bakery", "Breakfast", "Dairy", "Meat_seafood", "Produce"};
    private static boolean admin = false; 
    
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
	 * 	Creates a singleton object of Connector in order to access the database. 
	 * 
	 * 	@return		a Connector object attached to the shop_test database
	 */
	public static Connector getCon()
	{
		return singleton;
	}
	
	/**
	 * 	Closes the connection to the database once it is no longer needed. 
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
	
	public static void main(String[] args)
	{
		Connector c = Connector.getCon();
		c.authorize(username, password);
		//Connector.showShop();
		
		c.clearOrders();
		c.emptyCart(2);
		//c.purgeLogins();
		//c.runScript("lib\\BugBytes_shop_script.sql");
		
		//c.read("products", -1);
		//c.read(aisles[0], -1);
		//c.read(aisles[1], -1);
		//c.read(aisles[2], -1);
		//c.read(aisles[3], -1);
		//c.read(aisles[4], -1);
		//c.read(aisles[5], -1);
		//c.insert("ALC07", aisles[0], "Hennesy", 20.00, 10, 5);
		//c.read("Alcohol", -1);
		//c.delete("Hennesy");
		//c.read("Alcohol", -1);
		//c.update("Whiskey", "a", "bakery");
		//c.read("bakery", -1);
		//c.update("Whiskey", "a", "alcohol");
		//c.read("alcohol", -1);
		
		//c.read("cart", -1);
		//c.read("cart", 2);
		//c.addToCart(2, "ALC01", 2);
		//c.read("cart", 2);
		//c.updateCart(2, "ALC01", 5);
		//c.read("cart", 2);
		//c.addToCart(2, "ALC02", 2);
		//c.addToCart(2, "ALC03", 1);
		//c.read("cart", 2);
		//c.CONFIRM_ORDER(2); //technically a method on cart, should be true
		//c.emptyCart(2); 
		//c.read("cart", 2); //checks that cart is now empty
		
		//c.read("customer", -1);
		//c.read("customer", 2);
		//c.signUp(8, "user8", "foo", "bar", "foo@bar.com", "18000000000");
		//c.read("customer", -1);
		//c.removeAccount(8);
		//c.read("customer", -1);
		//c.changeAccount(8, "e", "new@gmail.com");
		//c.read("customer", -1);

		//c.read("order", -1);
		//c.read("order", 2);
		//int orderID = c.placeOrder(2, 10.00, 5.4544, 93.3744); //rounds off the last two decimals
		//c.read("order", -1);
		//c.addToOrder(orderID, 1, "ALC01", 5, 10.9913);
		//c.addToOrder(orderID, 2, "ALC02", 2, 5.991);
		//c.addToOrder(orderID, 3, "ALC03", 1, 10.991);
		//System.out.println(); //has to be done manually
		//c.read("order_details", orderID);
		//c.read("Alcohol", -1); //check if correct changes to products table have been made
		//c.read("order_details", -1);
		//c.clearOrders();
		//c.read("order_details", -1);
		
		System.out.println("Done.");
		c.close();
	}

	/**
	 * 	Reads an item from the inventory
	 *
	 * 	@throws 	IllegalArgumentException	if column is invalid
	 * 	@param		id		a series of alphanumeric characters representing a unique product
	 * 	@param		column 	the quantity you want to find
	 * 	@return        	a String representation of the column value
	 */
	public String readItem(String id, String column) 
	{
		if (column.equalsIgnoreCase("a") || column.equalsIgnoreCase("aisle") || column.equalsIgnoreCase("'aisle'")
    			||column.equalsIgnoreCase("t") || column.equalsIgnoreCase("type") || column.equalsIgnoreCase("'type'")
    			||column.equalsIgnoreCase("c") || column.equalsIgnoreCase("category") || column.equalsIgnoreCase("'category'")
    			||column.equalsIgnoreCase("product_type") || column.equalsIgnoreCase("'product_type'"))
    	{
    		column = "PRODUCT_TYPE";
    	}
    	else if (column.equalsIgnoreCase("n") || column.equalsIgnoreCase("name") || column.equalsIgnoreCase("'name'")
    			||column.equalsIgnoreCase("product name") || column.equalsIgnoreCase("'product name'")
    			||column.equalsIgnoreCase("product_name") || column.equalsIgnoreCase("'product_name'"))
    	{
    		column = "PRODUCT_NAME";
    	}
    	else if (column.equalsIgnoreCase("p") || column.equalsIgnoreCase("price") || column.equalsIgnoreCase("'price'")
    			||column.equalsIgnoreCase("unit price") || column.equalsIgnoreCase("'unit price'"))
    	{
    		column = "PRICE";
    	}
    	else if (column.equalsIgnoreCase("q") || column.equalsIgnoreCase("quantity") || column.equalsIgnoreCase("'quantity'")
    			||column.equalsIgnoreCase("quantity in stock") || column.equalsIgnoreCase("'quantity in stock'") 
    			||column.equalsIgnoreCase("quantity_in_stock") || column.equalsIgnoreCase("'quantity_in_stock'"))
    	{
    		column = "QUANTITY_IN_STOCK";
    	}
    	else if (column.equalsIgnoreCase("r") || column.equalsIgnoreCase("reorder") || column.equalsIgnoreCase("'reorder'"))
    	{
    		column = "REORDER";
    	}
    	else
    	{
    		throw new IllegalArgumentException("Invalid position. Please choose either 'aisle', 'name', 'price', 'quantity', or 'reorder'.");
    	}
		
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
	 * 	Prints to console some section of the database in structured text. 
	 * 
	 * 	@throws		IllegalArgumentException	if tableView does not represent a valid table (products, cart, customer, some aisle, etc.)
	 * 	@throws		SecurityException			if this connection does not have admin privileges and read is used for a restricted function
	 * 	@param		tableView	a String describing the table to be read
	 * 	@param		id			an optional parameter for either a user or order id, negative to print all 
	 */
	public void read(String tableView, int id) 
	{
		try
		{
			if (tableView.equalsIgnoreCase("products")) //display entire inventory
			{
				query = "select * from products";

				Statement myStmt = myConn.createStatement();
				ResultSet myRs = myStmt.executeQuery(query);
			
				System.out.println("\tStore Inventory:");
				System.out.printf("%6s%30s%16s\n"
					+ "-------------------------------------------------------\n"
					,"Product","Price","Quantity");
				while(myRs.next())
				{ 
						System.out.printf("%-29s$ %6.2f%12s", 
								myRs.getString("PRODUCT_NAME"), myRs.getDouble("PRICE"), myRs.getInt("QUANTITY_IN_STOCK"));
						System.out.println();
				}
				System.out.println("\n"); //spacing, two lines
				myStmt.close(); 
			}
			
			else if (isAisle(tableView)) //display an aisle of the shop
			{
				query = "select * from products WHERE PRODUCT_TYPE= ?";

				PreparedStatement myStmt = myConn.prepareStatement(query);
				myStmt.setString(1, tableView); //1-indexed
				ResultSet myRs = myStmt.executeQuery();
			
				System.out.println("\t" + tableView + " Aisle:");
				System.out.printf("%6s%30s%16s\n"
					+ "-------------------------------------------------------\n"
					,"Product","Price","Quantity");
				while(myRs.next())
				{ 
						System.out.printf("%-29s$ %6.2f%12s", 
								myRs.getString("PRODUCT_NAME"), myRs.getDouble("PRICE"), myRs.getInt("QUANTITY_IN_STOCK"));
						System.out.println();
				} 
				System.out.println("\n"); //spacing, two lines
				myStmt.close(); 
			}
			
			else if (tableView.equalsIgnoreCase("cart")) //display a user's cart
			{
				if (id <= 0) //in this case customer id
				{
					if (!admin)
					{
						throw new SecurityException("Ordinary customers are not allowed to access other customers' carts.");
					}
					
					query = "SELECT c.*, p.PRODUCT_NAME, cust.FIRST_NAME "
							+ "FROM cart c "
							+ "LEFT JOIN customer cust ON c.CUSTOMER_ID_CART = cust.CUSTOMER_ID "
							+ "LEFT JOIN products p ON c.PRODUCT_ID = p.PRODUCT_ID ";
					System.out.println("\tAll User's Carts: ");
				}
				else 
				{
					query = "SELECT c.*, p.PRODUCT_NAME, cust.FIRST_NAME "
							+ "FROM cart c "
							+ "LEFT JOIN customer cust ON c.CUSTOMER_ID_CART = cust.CUSTOMER_ID "
							+ "LEFT JOIN products p ON c.PRODUCT_ID = p.PRODUCT_ID "
							+ "WHERE CUSTOMER_ID_CART=? AND CUSTOMER_ID_CART=?";
				}

				PreparedStatement myStmt = myConn.prepareStatement(query);
				if (id > 0)
				{
					myStmt.setInt(1, id); //1-indexed
					myStmt.setInt(2, id);
				}
				ResultSet myRs = myStmt.executeQuery();
			
				if(!myRs.next() ) //false if the list is empty
				{
					System.out.println("Cart is empty. \n");
				}
				else //your cart is not empty
				{
					if (id > 0)
						System.out.println("\t" + myRs.getString("FIRST_NAME") + "'s Shopping Cart:");
					
					System.out.printf("%-8s%10s%21s%13s\n"
						+ "-------------------------------------------------------\n"
						,"Customer", "Product","Quantity","Total Cost");
					do
					{ 
							System.out.printf("%-11s%-21s%-13s$ %6.2f", 
									"user" + myRs.getString("CUSTOMER_ID_CART"), myRs.getString("PRODUCT_NAME"), myRs.getInt("QUANTITY_ORDERED"), myRs.getDouble("TOTAL_COST"));
							System.out.println();
					} while(myRs.next());
					System.out.println("\n"); //spacing, two lines
				}
				myStmt.close(); 
			}
			
			else if (tableView.equalsIgnoreCase("customer")) //displays the list of customers
			{
				if (id <= 0) //in this case customer id
				{
					if (!admin)
					{
						throw new SecurityException("Ordinary customers are not allowed to access other customers' information.");
					}
					
					query = "select * from customer";
					System.out.println("\tCustomer List: ");
				}
				else 
				{
					query = "select * from customer WHERE CUSTOMER_ID='" + id + "'";
					System.out.println("\tYour Account Information: ");
				}

				Statement myStmt = myConn.createStatement();
				ResultSet myRs = myStmt.executeQuery(query);
			
				if(!myRs.next() ) //false if the list is empty
				{
					System.out.println("There are no customers. \n");
				}
				else //list is not empty
				{
					System.out.printf("%-3s%-15s%-15s%-15s%-20s%-15s \n"
							+ "----------------------------------------------------------------------------------\n"
							,"ID","Username","First Name","Last Name","Email","Phone Number");
					do
					{ 
							System.out.printf("%-3s%-15s%-15s%-15s%-20s%-15s \n", 
									myRs.getString("CUSTOMER_ID"), myRs.getString("USERNAME"), myRs.getString("FIRST_NAME"), myRs.getString("LAST_NAME"), myRs.getString("EMAIL"), myRs.getString("PHONE"));
					} while(myRs.next());
					System.out.println("\n"); //spacing, two lines
				}
				myStmt.close(); 
			}
			
			else if (tableView.equalsIgnoreCase("order")) //display the list of orders
			{
				if (id <= 0) //in this case customer id
				{
					if (!admin)
					{
						throw new SecurityException("Ordinary customers are not allowed to access other customers' orders.");
					}
					
					query = "select * from shop_test.order";
					System.out.println("\tOrder List: ");
				}
				else 
				{
					query = "select * from shop_test.order WHERE CUSTOMER_ID='" + id + "'";
					System.out.println("\tYour Orders: ");
				}
				Statement myStmt = myConn.createStatement();
				ResultSet myRs = myStmt.executeQuery(query);
				
				if(!myRs.next() ) //false if the list is empty
				{
					System.out.println("There are no orders. \n");
				}
				else //only if the list has entries
				{
					System.out.printf("%-10s%-15s%-15s%-12s%-8s%-15s \n"
							+ "------------------------------------------------------------------------\n"
							,"Order ID","Customer ID","Date Issued","Shipping","Tax","Total Cost");
					do
					{ 
						String date = myRs.getString("ORDER_DATE").substring(0,11); //only the date, not time issued
						System.out.printf("%-10s%-15s%-15s%-12s%-8s%-15s \n", 
								myRs.getString("ORDER_ID"), myRs.getString("CUSTOMER_ID"), date, myRs.getString("SHIPPING_COST"), myRs.getString("TAX"), myRs.getString("TOTAL_COST"));
					} while(myRs.next());
					System.out.println("\n"); //spacing, two lines
				}
				myStmt.close(); 
			}
			
			else if (tableView.equalsIgnoreCase("order_details")) //display the order details associated with an order
			{
				if (id <= 0) //only this time is it order id
				{
					if (!admin)
					{
						throw new SecurityException("Ordinary customers are not allowed to access more than one order.");
					}
					System.out.println("\tAll Orders ");
					query = "select * from shop_test.order";
					Statement myStmt = myConn.createStatement();
					ResultSet myRs = myStmt.executeQuery(query);
					if(!myRs.next()) //no order matching
					{
						System.out.println("There are no orders in the database. \n");
						myStmt.close();
						return;
					}
					else
					{
						System.out.printf("%-10s%-15s%-15s%-12s%-8s%-15s \n"
								+ "------------------------------------------------------------------------\n"
								,"Order ID","Customer ID","Date Issued","Shipping","Tax","Total Cost");
						String date = myRs.getString("ORDER_DATE").substring(0,11); //only the date, not time issued
						System.out.printf("%-10s%-15s%-15s%-12s%-8s%-15s \n", 
								myRs.getString("ORDER_ID"), myRs.getString("CUSTOMER_ID"), date, myRs.getString("SHIPPING_COST"), myRs.getString("TAX"), myRs.getString("TOTAL_COST"));
					}
					System.out.println("\n\tAll Order Contents:");
					query = "select * from order_details";
				}
				else 
				{
					System.out.println("\tOrder " + id);
					query = "select * from shop_test.order WHERE ORDER_ID ='" + id + "'";
					Statement myStmt = myConn.createStatement();
					ResultSet myRs = myStmt.executeQuery(query);
					if(!myRs.next()) //no order matching
					{
						System.out.println("There are no orders matching search parameters. \n");
						myStmt.close();
						return;
					}
					else
					{
						System.out.printf("%-10s%-15s%-15s%-12s%-8s%-15s \n"
								+ "------------------------------------------------------------------------\n"
								,"Order ID","Customer ID","Date Issued","Shipping","Tax","Total Cost");
						String date = myRs.getString("ORDER_DATE").substring(0,11); //only the date, not time issued
						System.out.printf("%-10s%-15s%-15s%-12s%-8s%-15s \n", 
								myRs.getString("ORDER_ID"), myRs.getString("CUSTOMER_ID"), date, myRs.getString("SHIPPING_COST"), myRs.getString("TAX"), myRs.getString("TOTAL_COST"));
					}
					System.out.println("\n\tContents of order " + id + ": ");
					query = "select * from order_details WHERE ORDER_ID='" + id + "'";
				}
				Statement myStmt = myConn.createStatement();
				ResultSet myRs = myStmt.executeQuery(query);
				
				if(!myRs.next()) //false if the list is empty
				{
					System.out.println("There are no orders matching search parameters. \n"); //this should be impossible?
				}
				else //only if the list has entries
				{
					System.out.printf("%-10s%-15s%-10s%-12s \n"
							+ "-------------------------------------------\n"
							,"Order ID","Product ID","Quantity","Price");
					do
					{
						System.out.printf("%-10s%-15s%-10s%-12s \n", 
								myRs.getString("ORDER_ID"), myRs.getString("PRODUCT_ID"), myRs.getString("ORDERED_QUANTITY"), myRs.getString("PRICE"));
					} while(myRs.next());
					System.out.println("\n"); //spacing, two lines
				}
				myStmt.close(); 
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
	 * 	Finds the highest product number issued to any item in a given category. 
	 * 
	 * 	@throws		IllegalArgumentException	if aisle is not one of the valid aisles
	 * 	@param 		aisle 	a String representing one of the six aisles
	 * 	@return		the product number
	 */
	public int getHighestProductID(String aisle)
	{
		if (!isAisle(aisle))
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
			int num = 0;
			while(myRs.next())
			{
				String prodID = myRs.getString("PRODUCT_ID");
				//System.out.println(prodID.substring(prodID.length()-2));
				int id = Integer.parseInt(prodID.substring(prodID.length()-2)); //the last two digits
				if (id > num)
					num = id;
			}
			myStmt.close();
			return num; //will be the last number
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
		return 0; //failure
	}
	
	/**
	 * Adds a new item to inventory. Requires authorization. 
	 *
	 * 	@throws		SecurityException			if this connection does not have admin privileges
	 * 	@throws		IllegalArgumentException	if type is not one of the six aisles
	 * 	@throws		IllegalArgumentException	if price is non-positive
	 * 	@throws		IllegalArgumentException	if quantity is non-positive
	 * 	@param		PRODUCT_ID			a series of alphanumeric characters representing a unique product
	 * 	@param		PRODUCT_TYPE		the product type or category
	 * 	@param		PRODUCT_NAME		the name of the item
	 * 	@param		PRICE				the price of the item, rounded to two decimal places
	 * 	@param		QUANTITY_IN_STOCK	the number of item in stock
	 * 	@param		REORDER				the amount of stock at which this item should be restocked
	 * 	@return        		0 on failure, or 1 on success
	 */
	public int insert(String PRODUCT_ID, String PRODUCT_TYPE, String PRODUCT_NAME, double PRICE, int QUANTITY_IN_STOCK, int REORDER)
	{
		if (!admin)
		{
			throw new SecurityException("Ordinary customers are not permitted to add new items to inventory.");
		}
		
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
			System.out.println("Error. Item could not be added. Please try again later. \n");
			return 0;
		}
	}
	
	/**
	 * 	Updates an item in the inventory. Requires authorization.
	 * 
	 * 	@throws		IllegalArgumentException	if column is invalid
	 * 	@throws		SecurityException			if this connection does not have admin privileges
	 * 	@param 		name		the name of the item to be changed
	 * 	@param		column		the field you wish to change, either aisle, name, price, quantity, or reorder
	 * 	@param		value		the new value proposed for this field
	 */
	public void update(String name, String column, String value)
	{
		int type; //-1 for String, 0 for double, 1 for int
		if (column.equalsIgnoreCase("a") || column.equalsIgnoreCase("aisle") || column.equalsIgnoreCase("'aisle'")
    			||column.equalsIgnoreCase("t") || column.equalsIgnoreCase("type") || column.equalsIgnoreCase("'type'")
    			||column.equalsIgnoreCase("c") || column.equalsIgnoreCase("category") || column.equalsIgnoreCase("'category'"))
    	{
    		query = "UPDATE products SET PRODUCT_TYPE=? WHERE PRODUCT_NAME=?";
    		type = -1; //string
    		System.out.println("Changing product type to " + value);
    	}
    	else if (column.equalsIgnoreCase("n") || column.equalsIgnoreCase("name") || column.equalsIgnoreCase("'name'")
    			||column.equalsIgnoreCase("product name") || column.equalsIgnoreCase("'product name'"))
    	{
    		query = "UPDATE products SET PRODUCT_NAME=? WHERE PRODUCT_NAME=?";
    		type = -1; //string
    		System.out.println("Changing product name to " + value);
    	}
    	else if (column.equalsIgnoreCase("p") || column.equalsIgnoreCase("price") || column.equalsIgnoreCase("'price'")
    			||column.equalsIgnoreCase("unit price") || column.equalsIgnoreCase("'unit price'"))
    	{
    		query = "UPDATE products SET PRICE=? WHERE PRODUCT_NAME=?";
    		type = 0; //double
    		System.out.println("Changing product's unit price to " + value);
    	}
    	else if (column.equalsIgnoreCase("q") || column.equalsIgnoreCase("quantity") || column.equalsIgnoreCase("'quantity'")
    			|| column.equalsIgnoreCase("quantity_in_stock") || column.equalsIgnoreCase("'quantity_in_stock'"))
    	{
    		query = "UPDATE products SET QUANTITY_IN_STOCK=? WHERE PRODUCT_NAME=?";
    		type = 1; //int
    		System.out.println("Changing product's quantity to " + value);
    	}
    	else if (column.equalsIgnoreCase("r") || column.equalsIgnoreCase("reorder") || column.equalsIgnoreCase("'reorder'"))
    	{
    		query = "UPDATE products SET REORDER=? WHERE PRODUCT_NAME=?";
    		type = 1; //int
    		System.out.println("Changing product's reorder value to " + value);
    	}
    	else
    	{
    		throw new IllegalArgumentException("Invalid position. Please choose either 'aisle', 'name', 'price', 'quantity', or 'reorder'.");
    	}
		
		if (!admin)
		{
			throw new SecurityException("Ordinary customers are not permitted to alter the store's inventory.");
		}
		
		try
    	{
			PreparedStatement myStmt = myConn.prepareStatement(query);
    		if (type < 0) { //is a string
    			myStmt.setString(1, value);
    		} else if (type == 0) { //is a double
    			myStmt.setDouble(1, Double.parseDouble(value));
    		} else { //is an int
    			myStmt.setInt(1, Integer.parseInt(value));
    		}
    		myStmt.setString(2, name);
    		int rows = myStmt.executeUpdate(); 
    		
    		if (rows == 1)
    		{
    			System.out.println("Update complete. \n");
    		}
    		else if (rows < 1)
    		{
    			System.out.println("Error. Could not complete update. Please try again later. \n");
    		}
    		else
    		{
    			//error, somehow edited more than one entry
    			System.out.println("Update complete. \n");
    		}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
	}
	
	/**
	 * 	Removes an item from the inventory. Requires authorization.
	 * 
	 * 	@throws		SecurityException	if this connection does not have admin privileges
	 * 	@param		name		the name of the item to be removed
	 */
	public void delete(String name)
	{
		if (!admin)
		{
			throw new SecurityException("Ordinary customers are not permitted to remove items from inventory.");
		}
		
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			query = "delete from products where PRODUCT_NAME ='"+ name + "'"; 
			int rowsAffected = myStmt.executeUpdate(query); 
			if (rowsAffected < 1)
				System.out.println("No matching item was found. Nothing removed.");
			else
				System.out.println("Item " + name + " removed \n");

			myStmt.close(); 

		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	/**
	 * 	Adds a new item to a user's shopping cart. 
	 *
	 * 	@throws		IllegalArgumentException	if custID is non-positive
	 * 	@throws		IllegalArgumentException	if quantity is non-positive
	 * 	@param		custID		a positive integer, the id number of the customer
	 * 	@param		prodID		a series of alphanumeric characters representing a unique product
	 * 	@param		quantity	the number of this item to add
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
	 * 	Removes an item from a user's shopping cart. 
	 *
	 * 	@throws		IllegalArgumentException	if custID is non-positive
	 * 	@param		custID		a positive integer, the id number of the customer
	 * 	@param		prodID		a series of alphanumeric characters representing a unique product
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
     * 	Executes a union on a given cart and its associated place in the database, 
     * 	assigns both places to that union. 
     * 	
     * 	@param 		cart	a ShoppingCart object specifying a user's cart
     */
    public void fillCart(ShoppingCart cart)
    {
    	boolean empty = cart.getCartSize()==0;
    	int custID = cart.getCustomerId();
    	
    	//fill cart from database
    	try
    	{
    		Statement myStmt = myConn.createStatement();
    		query = "SELECT p.PRODUCT_ID, p.PRODUCT_TYPE, p.PRODUCT_NAME, p.PRICE, c.QUANTITY_ORDERED FROM cart c LEFT JOIN products p ON c.PRODUCT_ID=p.PRODUCT_ID WHERE CUSTOMER_ID_CART='" + cart.getCustomerId() + "'";
    		ResultSet myRs = myStmt.executeQuery(query);
    		if (!myRs.next()) //false if set is empty
    		{
    			//System.out.println("No update to be made forward.");
    		}
    		else
    		{
    			do
        		{
        			if(!cart.contains(myRs.getString("PRODUCT_ID")))
        			{
        				removeFromCart(custID, myRs.getString("PRODUCT_ID")); //clears from database
            			cart.addToCart(myRs.getString("PRODUCT_ID"), myRs.getInt("QUANTITY_ORDERED")); //adds to both
            			removeFromCart(custID, myRs.getString("PRODUCT_ID")); //because you are using addToCart rather than addItem
        			}
        			else
        				removeFromCart(custID, myRs.getString("PRODUCT_ID")); //clears from database
        		} while (myRs.next());
    		}
    	}
    	catch (SQLException e)
    	{
    		System.out.println("Error executing query. Cart could not be properly filled.");
    		return;
    	}
    	
    	if(!empty) //fill emptied database from cart (which is already the union of cart U database)
    	{
    		Item current = cart.first();
    		addToCart(custID, current.getProductId(), current.getQuantity());
    		while(cart.hasNext())
    		{
    			current = cart.next();
    			addToCart(custID, current.getProductId(), current.getQuantity());
    		} 
    	}
    	System.out.println("\n");
    }
    
    /**
	 * 	Removes all items from a user's shopping cart. 
	 *
	 * 	@throws		IllegalArgumentException	if custID is non-positive
	 * 	@param		custID		a positive integer, the id number of the customer
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
            if (custID > 1) //not default user
			{
            	System.out.println("Cart emptied. \n");
			}
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    }
    
    /**
	 * 	Changes the quantity of an item in a user's cart.
	 *
	 * 	@throws		IllegalArgumentException	if custID is non-positive
	 * 	@throws		IllegalArgumentException	if quantity is non-positive
	 * 	@param		custID		a positive integer, the id number of the customer
	 * 	@param		prodID		a series of alphanumeric characters representing a unique product
	 * 	@param		quantity	the number of this item you now want to have
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
	 * 	Final method to confirm an order is valid, and then turn it in to the server. 
	 *
	 * 	@throws		IllegalArgumentException	if custID is non-positive
	 * 	@param		custID		a positive integer, the id number of the customer
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
	 * 	Finds the highest id number of any order issued. 
	 * 
	 *	@return		the highest recorded order id number
	 */
	public int getHighestOrderID()
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from shop_test.order");
			// 4. Process the result set 
			int num = 0;
			while(myRs.next())
			{
				int id = myRs.getInt("ORDER_ID");
				if (id > num)
					num = id;
			}
			myStmt.close();
			return num; //will be the last number
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
		return 0; //failure
	}
    
    /**
	 * 	Issues an order to the database. 
	 *
	 * 	@throws		IllegalArgumentException	if custID is non-positive
	 * 	@throws		IllegalArgumentException	if shipping is negative
	 * 	@throws		IllegalArgumentException	if tax is negative
	 * 	@throws		IllegalArgumentException	if total is negative
	 * 	@param		custID		a positive integer, the id number of the customer
	 * 	@param		shipping	a double value representing the shipping price to be collected, rounded to two decimal places
	 * 	@param		tax			a double value representing the tax to be collected, rounded to two decimal places
	 * 	@param		total		a double value representing the total price to be charged for the order, rounded to two decimal places
	 * 	@return			the order id generated for this new order
	 */
    public int placeOrder(int custID, double shipping, double tax, double total) 
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
        	
        	int orderID = getHighestOrderID()+1; //does not account for multiple orders
        	myStmt.setInt(1, orderID); 
        	myStmt.setInt(2, custID);
        	myStmt.setDate(3, new java.sql.Date(System.currentTimeMillis())); //order date is now
        	myStmt.setDouble(4, round(shipping, 2));
        	myStmt.setDouble(5, round(tax, 2));
        	myStmt.setDouble(6, round(total, 2));
        	
            myStmt.executeUpdate();
            System.out.println("Order placed");
            myStmt.close(); 
            return orderID;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    	return 0; //failure
    }
    
    /**
	 * 	Adds a new item to an order, and updates inventory. Follow block by a newline. 
	 *
	 * 	@throws 	IllegalArgumentException	if orderID is non-positive
	 * 	@throws		IllegalArgumentException	if lineNumber is non-positive
	 * 	@throws		IllegalArgumentException	if quantity is non-positive
	 * 	@throws		IllegalArgumentException	if price is negative
	 * 	@param		orderID		a positive integer uniquely identifying the order this is a part of
	 * 	@param		lineNumber	a positive integer representing which item in the order this is
	 * 	@param		prodID		a series of alphanumeric characters representing a unique product
	 * 	@param		quantity	a positive integer representing the number of an item to purchase
	 * 	@param		price		a double representing the unit price of an item, rounded to two decimal places
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
    	int inStock = Integer.parseInt(readItem(prodID, "quantity"));
    	
    	try //update product quantity
    	{
    		query = "UPDATE products SET QUANTITY_IN_STOCK=? WHERE PRODUCT_ID=?";
    		PreparedStatement myStmt = myConn.prepareStatement(query);
    		myStmt.setInt(1, inStock-quantity);
    		myStmt.setString(2, prodID);
    		int rows = myStmt.executeUpdate();
    		if (rows < 1)
    		{
    			System.out.println("Failed to find item identified by" + prodID + ". Please check if product id is correct and try again later.");
    			return;
    		}
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
        
        try //add new entry to order_details
        {
    		query = "INSERT INTO order_details (ORDER_ID, ORDER_LINE_NUMBER, PRODUCT_ID, ORDERED_QUANTITY, PRICE) VALUES (?,?,?,?,?)";
        	PreparedStatement myStmt = myConn.prepareStatement(query);
        	
        	myStmt.setInt(1, orderID); 
        	myStmt.setInt(2, lineNumber);
        	myStmt.setString(3, prodID); 
        	myStmt.setInt(4, quantity);
        	myStmt.setDouble(5, round(price, 2)*quantity); //should be total price, not unit price
        	
            myStmt.executeUpdate();
            System.out.println("Item " + lineNumber + " added to order: " + orderID);
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 	Cancels an order previously made and removes it from the database.
     * 
     * 	@throws 	IllegalArgumentException	if orderID is non-positive
     * 	@param 		orderID		a positive integer identifier of the order to be removed
     */
    public void cancelOrder(int orderID)
    {
    	if (orderID < 1)
    	{
    		throw new IllegalArgumentException("Invalid order id. Please use a positive order id.");
    	}
    	
    	try 
        {
            //delete from order_details
            query = "DELETE FROM order_details WHERE ORDER_ID=?";
            PreparedStatement myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, orderID); //1-indexed
            int orderSize = myStmt.executeUpdate();
            if (orderSize < 1)
            {
            	System.out.println("Error in cancellation. Please check if order id is correct and try again later.\n");
            }
            else //only if you removed some order_details
            {
            	//delete from order
                query = "DELETE FROM shop_test.order WHERE ORDER_ID=?";
                myStmt = myConn.prepareStatement(query);
                myStmt.setInt(1, orderID); //1-indexed
                int rows = myStmt.executeUpdate();
                
                if (rows < 1)
                {
                	System.out.println("Error in cancellation. Please check if order id is correct and try again later.\n");
                }
                else
                {
                	System.out.println("Order cancelled.\n");
                }
            }
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    }
    
    /**
	 * 	Removes all orders from the store database. 
	 * 
	 * 	@throws		SecurityException	if this connection does not have admin privileges
	 */
	public void clearOrders() 
    {
        if(!admin)
        {
        	throw new SecurityException("Ordinary customers are not allowed to purge the order list.");
        }
		
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
	 *	@param		id			customer id
	 *	@param		username	the customer's username
	 *	@param		firstName	the customer's first name
	 *	@param		lastName	the customer's last name
	 *	@param		email		the customer's email address
	 *	@param		phone		the customer's phone number
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
     * 	Changes the email address or phone number registered to a user's account. 
     * 
     * 	@throws		IllegalArgumentException	if column is invalid
     * 	@param 		custID	a positive integer identifier of the account to be removed
     * 	@param		column	the quantity to be changed, either email or phone
     * 	@param		value	the value to be assigned in that position
     */
    public void changeAccount(int custID, String column, String value)
    {
    	if (column.equalsIgnoreCase("e") || column.equalsIgnoreCase("email") || column.equalsIgnoreCase("'email'")
    			||column.equalsIgnoreCase("email address") || column.equalsIgnoreCase("'email address'"))
    	{
    		query = "UPDATE customer SET EMAIL=? WHERE CUSTOMER_ID=?";
    		System.out.println("Changing email address to " + value);
    	}
    	else if (column.equalsIgnoreCase("p") || column.equalsIgnoreCase("phone") || column.equalsIgnoreCase("'phone'")
    			||column.equalsIgnoreCase("phone number") || column.equalsIgnoreCase("'phone number'"))
    	{
    		query = "UPDATE customer SET PHONE=? WHERE CUSTOMER_ID=?";
    		System.out.println("Changing phone number to " + value);
    	}
    	else
    	{
    		throw new IllegalArgumentException("Invalid position. Please choose either 'email' or 'phone'.");
    	}
    	
    	try
    	{
    		PreparedStatement myStmt = myConn.prepareStatement(query);
    		myStmt.setString(1, value);
    		myStmt.setInt(2, custID);
    		int rows = myStmt.executeUpdate(); 
    		if (rows > 0)
    		{
    			System.out.println("Update complete. \n");
    		}
    		else if (rows <=0)
    		{
    			System.out.println("Error. Could not complete update. Please check that the customer id is correct and try again later. \n");
    		}
    		else
    		{
    			//error, somehow edited more than one entry
    		}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    /**
     * 	Removes an account from the database.
     * 
     * 	@throws 	IllegalArgumentException	if orderID is non-positive	
     * 	@param 		custID	a positive integer identifier of the account to be removed
     */
    public void removeAccount(int custID)
    {
    	if (custID < 1)
        {
        	throw new IllegalArgumentException("Invalid customer id. Please use an customer id greater than zero.");
        }
    	
    	try 
        {
    		//remove customer's cart, if any, first, in order to satisfy constraint with customer
    		Statement myStmt = myConn.createStatement();
            query = "DELETE FROM cart WHERE CUSTOMER_ID_CART='" + custID + "'"; 
            myStmt.executeUpdate(query);
            
            //remove customer
            myStmt = myConn.createStatement();
            query = "DELETE FROM customer WHERE CUSTOMER_ID=\"" + custID + "\""; 
            int rows = myStmt.executeUpdate(query);
            if (rows < 1)
            {
            	System.out.println("This account does not exist. Please check if customer id is correct and try again later.\n");
            }
            else if (rows == 1)
            {
            	System.out.println("Your account has been removed.\n");
            }
            else
            {
            	//Error. Somehow removed more than one customer. This should be impossible?
            	System.out.println("Deleted more than one account with same custID. \n");
            }
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    }
    
    /**
	 * 	Determines whether a customer's information matches the database, or if username is reserved. 
	 * 
	 * 	@param 		username	the customer's username
	 * 				firstName	the customer's first name
	 * 				firstName	the customer's last name
	 * 	@return			0 if username does not exist in database, negative if username is taken, positive customer id if matching account exists
	 */
	public int isCustomer(String username, String firstName, String lastName)
	{
		try
		{
			query = "select * from customer";

			Statement myStmt = myConn.createStatement();
			ResultSet myRs = myStmt.executeQuery(query);

			while(myRs.next())
			{ 
				if(username.equals(myRs.getString("USERNAME")))
				{
					if(firstName.equals(myRs.getString("FIRST_NAME")) && lastName.equals(myRs.getString("LAST_NAME")))
					{
						int ret = myRs.getInt("CUSTOMER_ID");
						myStmt.close();
						return ret;
					}
					else
					{
						myStmt.close();
						return -1;
					}
				}
			} 
			//username is never found
			myStmt.close(); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 	Finds the customer id of a customer.
	 * 
	 *	@param 		username	the customer's username
	 * 	@return			the customer's id number
	 */
	public int getCustomerID(String username)
	{
		try
		{
			// 2. Create a statement
			PreparedStatement myStmt = myConn.prepareStatement("select * from customer where USERNAME = ?");
			myStmt.setString(1, username); //1 specifies the first parameter in the query
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery();
			// 4. Process the result set 
			if (myRs.next()) //false if empty
			{
				System.out.println("No customer by that username exists.");
				return 0; //failure
			}
			int ret = myRs.getInt("CUSTOMER_ID");
			myStmt.close();
			return ret;
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
		return 0; //failure
	}
	
	/**
	 * 	Finds the highest customer id number in the database. 
	 * 
	 * 	@return			the highest recorded customer id number
	 */
	public int getHighestCustomerID()
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from customer");
			// 4. Process the result set 
			int num = 0;
			while(myRs.next())
			{
				int id = myRs.getInt("CUSTOMER_ID");
				if (id > num)
					num = id;
			}
			myStmt.close();
			return num; //will be the last number
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
		return 0; //failure
	}

    /**
	 * 	Removes all login information from the store database. Dangerous.
	 * 
	 * 	@throws		SecurityException	if this connection does not have admin privileges
	 */
	public void purgeLogins() 
    {
        if(!admin)
        {
        	throw new SecurityException("Ordinary users are not allowed to purge the logins list.");
        }
		
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
    
    /**
     * 	Determines whether this connection has admin privileges or not. 
     * 
     * 	@return			true, if this connection has admin privileges, false otherwise
     */
    public boolean admin()
    {
    	return admin;
    }
    
    /**
     * 	Attempts to secure administrator privileges on this connection based on an issued login.
     * 
     * 	@param 		username	a String username required to access admin privileges
     * 	@param 		password	a String password required to access admin privileges
     */
    public void authorize(String username, String password)
    {
    	admin = username.equals(Connector.username) && password.equals(Connector.password); 
    }
    
    /**
     *	Runs an sql script at a given file path. Used for initializing the database.
     */
    public void runScript(String pathname)
    {
    	try
    	{
    		ScriptRunner runner = new ScriptRunner(myConn, false, false);
    		runner.runScript(new java.io.BufferedReader(new java.io.FileReader(pathname)));
    		//System.out.println("Script " + pathname + "successfully run \n");
    	}
    	catch (IOException e) 
    	{
    		System.err.println("File " + pathname + " not found: " + e);
		} 
    	catch (SQLException e) 
    	{
			System.err.println("Error connecting to database: " + e);
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
     * 	Prints the aisles in the shop. 
     */
    public static void showShop()
    {
    	System.out.println("BugBytes Storefront Aisles: ");
    	for (String aisle : aisles)
    	{
    		System.out.println("\t" + aisle);
    	}
    	System.out.println(); 
    }
    
    /**
     * 	Evaluates if a string parameter is one of the valid aisles. Not case sensitive. 
     * 
     * 	@param		s	a string to be checked against
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
    
    public String getIDFromName(String s) 
    {
    	try
    	{
    	String ret = "No String ;-;";
    	// 2. Create a statement
    	PreparedStatement myStmt = myConn.prepareStatement("SELECT PRODUCT_ID from PRODUCTS where PRODUCT_NAME = ?");
    	myStmt.setString(1, s); //1 specifies the first parameter in the query
    	// 3. Execute a SQL query
    	ResultSet myRs = myStmt.executeQuery();
    	// 4. Process the result set 	
    	while(myRs.next())	
		{
    		ret = myRs.getString("PRODUCT_ID");
		}
		myStmt.close();
		ret.toString();
		return ret;	
    		
    		
    		
	
    	}
    	catch (Exception e)
    	{
    	e.printStackTrace();
    	}
    	return "Something Went Wrong I thinks";
    }
}
