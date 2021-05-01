import java.sql.*;

public class Connector 
{
	private Connection myConn;
	private String query;
	
	private static final String url = "jdbc:mysql://localhost:3306/shop_test";
	private static final String username = "root";
	private static final String password = "Sikander8"; 
	
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
	
	public void read(String variable) 
	{
		try
		{
			if (variable == "products") {
				query = "select * from products";

			Statement myStmt = myConn.createStatement();
			ResultSet myRs = myStmt.executeQuery(query);
			
			String output = "";
			
				while(myRs.next())
				{ 
						System.out.printf("%-29s$ %6s%12s",myRs.getString(3),myRs.getDouble(4),myRs.getInt(5));
						System.out.println();
				}	
				myStmt.close(); 

			}
			
			if (variable == "customer") {
				query = "select * from customer";

			Statement myStmt = myConn.createStatement();
			ResultSet myRs = myStmt.executeQuery(query);
			
			String output = "";
			
				while(myRs.next())
				{ 
						output = myRs.getInt(1) + " " +
								 myRs.getString(2) + " " +	
								 myRs.getString(3) + " " +
								 myRs.getDouble(4) + " " +
								 myRs.getString(5) + " " +
								 myRs.getString(6);
						System.out.println(output);
				}	
				myStmt.close(); 

			}
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

		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}

}
