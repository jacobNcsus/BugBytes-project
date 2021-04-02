import java.sql.*;

public class Connector 
{
	private Connection myConn;
	private String query;
	
	private String url = "jdbc:mysql://localhost:3306/demo";
	private String username = "student";
	private String password = "student"; 
	
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
		c.read(); 
	}
	
	public void read() 
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			ResultSet myRs = myStmt.executeQuery("select * from employees");
			// 4. Process the result set 
			while(myRs.next())
			{
				System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
			}
			
			myStmt.close(); 
			myConn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public void insert()
	{
		try
		{
			// 2. Create a statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute a SQL query
			query = "insert into employees "
					+ " (last_name, first_name, email)"
					+ " values ('Brown', 'David', 'david.brown@foo.com')"; 
			myStmt.executeUpdate(query); 
			
			System.out.println("Insert complete."); 
		
			myStmt.close(); 
			myConn.close();
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
			query = "update employees "
					+ " set email='demo@luv2code.com'"
					+ " where id=9"; 
			myStmt.executeUpdate(query); 
			
			System.out.println("Update complete."); 

			myStmt.close(); 
			myConn.close();

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
			query = "delete from employees where last_name='Brown'"; 
			int rowsAffected = myStmt.executeUpdate(query); 
			
			System.out.println("Rows affected: " + rowsAffected);
			System.out.println("Delete complete."); 

			myStmt.close(); 
			myConn.close();

		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}

}
