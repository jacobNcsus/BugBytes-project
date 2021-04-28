import java.sql.*;

/**
 * Creates a connection to the shopping
 *
 * @author Jacob Normington, Daniel Beauchamp, Youser Alalusi
 * @version 4/28/2021
 */
public class Connector 
{
	private Connection myConn;
	private String query;
	
	private static final String url = "jdbc:mysql://localhost:3306/shop_test";
	private static final String username = "shopMgr";
    private static final String password = "csc131"; 
    
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
	 * @return		a Connector object connected to the shop_test database
	 */
	public static Connector getCon()
	{
		return singleton;
	}
	
	public static void main(String[] args)
	{
		Connector c = Connector.getCon();
		//c.insert();
		//c.read();
		//c.update(); 
		//c.read();
		//c.delete();
		//System.out.println("Result should be: MEA05 -- " + c.readItem("Pork", "PRODUCT_ID")); 
		
		//c.printAll();
		//c.printAisle("alcohol");
		//c.printAisle("bakery");
		//c.printAisle("breakfast");
		//c.printAisle("dairy");
		//c.printAisle("meat_seafood");
		//c.printAisle("produce");
		
		c.signUp(1, "user1", "first", "last", "foo@bar.com", "18000000000");
		c.printCart(1);
		c.addToCart(1, "ALC01", 2);
		c.printCart(1);
		c.updateCart(1, "ALC01", 5);
		c.printCart(1);
        c.removeFromCart(1, "ALC01");
        c.printCart(1);
        c.CONFIRM_ORDER(1);
		
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
	 * @param	aisle	a String object of the aisle to be displayed, either 'Alcohol', 'Bakery', 'Breakfast', 'Dairy', 'Meat_seafood', or 'Produce'
	 */
	public void printAisle(String aisle) 
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from " + aisle + ";");
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
		printAisle("products");
	}
	
	/**
	 * Adds a new item to inventory
	 *
	 * @throws	IllegalArgumentException	if type is not one of the six aisles
	 * 			IllegalArgumentException	if the price entered has more than two decimal points
	 * 			IllegalArgumentException	if price is non-positive
	 * @param	PRODUCT_ID			a series of alphanumeric characters representing a unique product
	 * 			PRODUCT_TYPE		the product type or category
	 * 			PRODUCT_NAME		the name of the item
	 * 			PRICE				the price of the item
	 * 			QUANTITY_IN_STOCK	the number of item in stock
	 * 			REORDER				the amount of stock at which this item should be restocked
	 * @return        		0 on failure, or 1 on success
	 */
	private int insert(String PRODUCT_ID, String PRODUCT_TYPE, String PRODUCT_NAME, double PRICE, int QUANTITY_IN_STOCK, int REORDER)
	{
		if ( !(PRODUCT_TYPE.equals("'Alcohol'") 
				|| PRODUCT_TYPE.equals("'Bakery'") 
				|| PRODUCT_TYPE.equals("'Breakfast'") 
				|| PRODUCT_TYPE.equals("'Dairy'") 
				|| PRODUCT_TYPE.equals("'Meat_seafood'") 
				|| PRODUCT_TYPE.equals("'Produce'") ) )
		{
			throw new IllegalArgumentException("Product type invalid, please choose 'Alcohol', 'Bakery', 'Dairy', 'Meat_seafood', or 'Produce'.");
		}
		
		if ( !((int) (PRICE*100) == PRICE*100) ) //if price has more than two digits
		{
			throw new IllegalArgumentException("Price invalid, please use a value with no more than two decimal places.");
		}
		
		if ( !(PRICE <= 0) ) //if price is non-positive
		{
			throw new IllegalArgumentException("Price invalid, please enter a price greater than zero.");
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
			myStmt.setDouble(4, PRICE);
			myStmt.setInt(5, QUANTITY_IN_STOCK);
			myStmt.setInt(6, REORDER);
			// 4. Execute a SQL query
			int rows = myStmt.executeUpdate(); 
			// 5. Process result set
			System.out.println("Insert complete. \n Rows affected " + rows); 
		
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
	private void delete() //demo
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			query = "delete from employees where last_name='Brown'"; 
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
	 * @param	custID	a positive integer, the id number of the customer
	 */
	public void printCart(int custID) 
	{
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
	 * @param	custID		a positive integer, the id number of the customer
	 * 			prodID		a series of alphanumeric characters representing a unique product
	 * 			quantity	the number of this item to add
	 */
	public void addToCart(int custID, String prodID, int quantity) 
	{
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
        	myStmt.setDouble(4, price * quantity);
        	
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
	 * @param	custID		a positive integer, the id number of the customer
	 * 			prodID		a series of alphanumeric characters representing a unique product
	 */
    public void removeFromCart(int custID, String prodID) 
    {
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
	 * Changes the quantity of an item in a user's cart.
	 *
	 * @param	custID		a positive integer, the id number of the customer
	 * 			prodID		a series of alphanumeric characters representing a unique product
	 * 			quantity	the number of this item you now want to have
	 */
    private void updateCart(int custID, String prodID, int quantity) //really just changes quantity
	{
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
    
    public void CONFIRM_ORDER(int custID) 
    {
        
        try 
        {
            Statement myStmt = myConn.createStatement();
            String statementText = "SELECT c.PRODUCT_ID, p.PRODUCT_NAME, c.QUANTITY_ORDERED, p.QUANTITY_IN_STOCK AS stockRemaining FROM cart c LEFT JOIN products p ON c.PRODUCT_ID = p.PRODUCT_ID WHERE CUSTOMER_ID_CART=\"" + custID+ "\"";
            ResultSet myRs = myStmt.executeQuery(statementText);
            while(myRs.next()) 
            {
                System.out.println("Inside the while loop");
                if (myRs.getInt("stockRemaining") < myRs.getInt("QUANTITY_IN_STOCK")) 
                {
                    System.out.println("Insufficient inventory");
                } 
                else 
                {
                    System.out.println("Order placed");
                    // call order.placeOrder(custID) method
                }
            }
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
    }
    
    /**
	 *	Creates a new customer profile in the database. 
	 */
    private void signUp(int id, String username, String firstName, String lastName, String email, String phone)
    {
    	try 
        {
    		// 2. Create a statement
    		Statement myStmt = myConn.createStatement();
    		// 3. Execute a SQL query
    		ResultSet myRs = myStmt.executeQuery("select * from customer WHERE CUSTOMER_ID='" + id +"';");
    		// 4. Process the result set 
    		myRs.next();
    		if (myRs.getString("CUSTOMER_ID") != null) 
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
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    }
}
