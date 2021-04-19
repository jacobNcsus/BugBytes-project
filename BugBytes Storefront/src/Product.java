import java.sql.*;
//Daniel, depricated 


public class Product {
	
	private Connection myConn;
	private String query;
	
	private String url = "jdbc:mysql://localhost:3306/shop_test";
	private String username = "shopMgr"; // shopMgr, or student
	private String password = "csc131"; // csc131, or student
	
	public Product()
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
		Product c = new Product();
		//c.insert();
		//c.read();
		//c.update(); 
		//c.read();
		//c.delete();
		//c.read(); 
		
		c.all();
		c.printAisle("alcohol");
		c.printAisle("bakery");
		c.printAisle("breakfast");
		c.printAisle("dairy");
		c.printAisle("meat_seafood");
		c.printAisle("produce");
		
		c.close();
	}
	
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
	
	public void all() 
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
	

/*
 * &	public void insert()
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			query = "insert into products "
					+ " (PRODUCT_ID, PRODUCT_TYPE, NAME, PRICE, QUANTITY, REORDER)"
					+ " values ('ALC06', 'Alcohol', 'Rum', 14.99, 30, 5)"; 
			myStmt.executeUpdate(query); 
			
			System.out.println("Insert complete."); 
		
			myStmt.close(); 
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void update()
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			query = "update product "
					+ " set QUANTITY=27"
					+ " where PRODUCT_ID = 'ALC01'"; 
			myStmt.executeUpdate(query); 
			
			System.out.println("Update complete."); 

			myStmt.close(); 

		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void delete()
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			query = "delete from products where NAME='Gin'"; 
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
	*/

}
