/**
 * Menu: Does Menu stuff. 
 * 	
 * Note:  -Need to have SQL downlaoded/set up
 *        -Login will be done via database
 *        -View Shop displays default database information that came with SQL****
 *        ****Case 2 View Shop: Probably wont work since I set up an account in SQL with Username: Alex, Pw:Password1! Needs to be set up to Sai's database
 *        	-Watch videos in Dev Team workspace to learn more
 *        -Purpose of While Loop:
 *        		-Once you are completed with an case allows you to return to menu. Unless you choose End Application. 
 *        
 *        
 * 
 * 				
 *
 * @author Alexander Gunby
 * @version 3/29/21
 */ 


/*public class InventoryItem extends Item 
{
	
}
*/

import java.sql.DriverManager;
import java.util.Scanner;
import java.sql.*;

public class Menu {

	Scanner ChoiceNum = new Scanner(System.in);
	
	
	
	public static void main(String[] args) throws Exception {
		
		int choice = 0;
		
		Menu test1 = new Menu();
		
		while (choice != 4) {
		test1.MenuIntro();
		choice = test1.MenuDecision(choice);
		}
		
		
		

	}
	
	public void MenuIntro() {
		System.out.println("Shopping Application");
		System.out.println("Insert number command:\n");
		System.out.println("1: Login");
		System.out.println("2: View shop!");
		System.out.println("3: View Shopping Cart");
		System.out.println("4: End Application");
	}
	public int MenuDecision(int choice) throws Exception{
		
		choice = ChoiceNum.nextInt();
		
		
		// Login/View Shopping cart is on back-burner 
		//Will currently take two rows of info(Name/District) from a default database(from sql) and display it. 
		// probably wont work unless you set up the account to database with my username/password (uncertain)
		switch (choice) {
		case 1: 
			System.out.println("Login Stuff"); // Might not be needed (part of Connection to database?)
			break;
		case 2:
			
			System.out.println("Printing out Shop\n");
			
			// get connection to data base
			//User is "Alex" Password is "Password1!" done in database
			Connection myCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/world","Alex","Password1!");
			//Create a Statement 
			Statement myStmt = myCon.createStatement();
			//Execute my SQL query 
			ResultSet myRs = myStmt.executeQuery("select * from city");//test
			
			//Process the Result set
			while (myRs.next()) {
				System.out.println(myRs.getString("Name")+","+myRs.getString("District"));
			}
			
			break;
		case 3:
			System.out.println("Viewing Shopping Cart"); //Do later, lack proper knowledge. Will implement database functions?
			break;
		case 4:
			System.out.println("Ending Application, Goodbye!");
			return choice;
			
		default:
			System.out.println("Please insert proper input!");
			MenuDecision(choice);
		}
		
		return choice;
	}
		
}
