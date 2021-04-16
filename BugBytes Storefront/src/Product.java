import java.sql.*;
//Test


public class Product {
	
	private Connection myConn;
	private String query;
	
	private String url = "jdbc:mysql://localhost:3306/shop_test";
	private String username = "shopMgr";
	private String password = "csc131"; 
	
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
		c.alcohol();
		c.bakery();
		c.breakfast();
		c.dairy();
		c.meat();
		c.produce();
		
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
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from products");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("NAME") + ", " + myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY") + ", " + myRs.getString("REORDER"));
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void alcohol() 
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from alcohol;");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("NAME") + ", " + myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY") + ", " + myRs.getString("REORDER"));
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void bakery() 
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from bakery;");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("NAME") + ", " + myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY") + ", " + myRs.getString("REORDER"));
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void breakfast() 
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from breakfast;");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("NAME") + ", " + myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY") + ", " + myRs.getString("REORDER"));
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void dairy() 
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from dairy;");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("NAME") + ", " + myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY") + ", " + myRs.getString("REORDER"));
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void meat() 
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from meat_seafood;");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("NAME") + ", " + myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY") + ", " + myRs.getString("REORDER"));
			}
			
			myStmt.close(); 
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void produce() 
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from produce;");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("NAME") + ", " + myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY") + ", " + myRs.getString("REORDER"));
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
