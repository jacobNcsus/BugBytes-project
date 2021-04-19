import java.sql.*;

/**
 * Creates a connection to the shopping
 *
 * @author Jacob Normington, Daniel Beauchamp, Youser Alalusi
 * @version 4/18/2021
 */
public class Connector 
{
	private Connection myConn;
	private String query;
	
	private String url = "jdbc:mysql://localhost:3306/shop_test";
	private String username = "shopMgr";
    private String password = "csc131"; 
	
	public Connector()
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
	
	public static void main(String[] args)
	{
		Connector c = new Connector();
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
		
		//c.printCart(1);
		c.addToCart(1, "ALC01", 2);
		c.printCart(1);
        c.removeFromCart(1, "ALC01");
        c.printCart(1);
        c.CONFIRM_ORDER(1);
		
		c.close();
	}
	
	/**
	    * Closes the connection to the database once it is no longer needed
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
	    * @param	name	the name of the item
	    * 			column 	the quantity you want to find
	    * @return        	a String representation of the column value
	    */
	public String readItem(String name, String column) 
	{
		try
		{
			// 2. Create a statement
			PreparedStatement myStmt = myConn.prepareStatement("select * from products where PRODUCT_NAME = ?");
			myStmt.setString(1, name); //1 specifies the first parameter in the query
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
	
	public void printAll() 
	{
		printAisle("products");
	}
	
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
	    * Adds a new item to inventory
	    *
	    * @throws	IllegalArgumentException	if type is not one of the six aisles
	    * 			IllegalArgumentException	if the price entered has more than two decimal points
	    * 			IllegalArgumentException	if price is non-positive
	    * @param	PRODUCT_ID			the name of the item
	    * 			PRODUCT_TYPE		the product type or category
	    * 			PRODUCT_NAME		the name of the item
	    * 			PRICE				the price of the item
	    * 			QUANTITY_IN_STOCK	the number of item in stock
	    * 			REORDER				the amount of stock at which this item should be restocked
	    * @return        		0 on failure, or 1 on success
	    */
	public int insert(String PRODUCT_ID, String PRODUCT_TYPE, String PRODUCT_NAME, double PRICE, int QUANTITY_IN_STOCK, int REORDER)
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
	
	public void update() //demo
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
	
	public void delete() //demo
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
	    * Reads an item from the cart
	    *
	    * @param	name	the name of the item
	    * 			column 	the quantity you want to find
	    * @return        	a String representation of the column value
	    */
	public String readCart(String name, String column) 
	{
		try
		{
			// 2. Create a statement
			PreparedStatement myStmt = myConn.prepareStatement("select * from cart where PRODUCT_NAME = ?");
			myStmt.setString(1, name); //1 specifies the first parameter in the query
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery();
			// 4. Process the result set 
			myRs.next();
			String ret = myRs.getString(column);
			
			myStmt.close();
			return ret;
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
		return null;
	}
	
	public void printCart(int custID) 
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from cart WHERE CUSTOMER_ID_CART='" + custID +"';");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("CUSTOMER_ID_CART") + ", " + myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_NAME") + ", " 
			+ myRs.getString("QUANTITY_IN_STOCK") + ", " + myRs.getString("TOTAL_COST"));
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void addToCart(int custID, String prodID, int quantity) 
	{
        // Retrieve product details
        String name = "";
        Double price = 0.0;
        try 
        {
            Statement myStmt = myConn.createStatement();
            String statementText = "SELECT * FROM products WHERE PRODUCT_ID=\"" + prodID + "\"";
            ResultSet myRs = myStmt.executeQuery(statementText);
            myRs.next();
            name = myRs.getString("PRODUCT_NAME");
            price = myRs.getDouble("PRICE");       
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        // Add to cart
        try 
        {
            Statement myStmt = myConn.createStatement();
            String statementText = "INSERT INTO cart(CUSTOMER_ID_CART, PRODUCT_ID, PRODUCT_NAME, QUANTITY_IN_STOCK, TOTAL_COST) VALUES(" 
            		+ custID + ", \"" + prodID + "\", \"" + name + "\", " + quantity + ", \"$" + (price * quantity) + "\")";
            myStmt.executeUpdate(statementText);
            myStmt.close(); 
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); 
        }
    }
    
    public void removeFromCart(int custID, String prodID) 
    {
        try 
        {
            Statement myStmt = myConn.createStatement();
            String statementText = "DELETE FROM cart WHERE PRODUCT_ID=\"" + prodID + "\" AND CUSTOMER_ID_CART=\"" + custID + "\""; 
            myStmt.executeUpdate(statementText);
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
            String statementText = "SELECT c.PRODUCT_ID, p.PRODUCT_NAME, c.QUANTITY_IN_STOCK, p.QUANTITY_IN_STOCK AS stockRemaining FROM cart c LEFT JOIN products p ON c.PRODUCT_ID = p.PRODUCT_ID WHERE CUSTOMER_ID_CART=\"" + custID+ "\"";
            ResultSet myRs = myStmt.executeQuery(statementText);
            while(myRs.next()) 
            {
                System.out.println();
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
}
