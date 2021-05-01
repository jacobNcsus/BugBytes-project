
import java.util.*;
import java.sql.*;


public class UI {

	public static void main(String[]arg) {
		
		login();
		
		menu();
		
		select();
	}
	
	public static void select() {
		
		Scanner s = new Scanner(System.in);

		System.out.printf("\nPlease select a product to add to cart: ");
		String selection = s.nextLine();
		
		//if (selection == )
		
	}
	
	public static void menu() {
		
		Connector c = new Connector();
		System.out.printf("%15s%21s%16s\n"
				+ "-------------------------------------------------------\n"
						 ,"Product","Price","Quantity");
		c.read("products");
		
	}

	
	// customer logins in with email and password
	public static void login() {
		
		Scanner s = new Scanner(System.in);
		
		System.out.printf("Please fill in the following information to create an account:\n\n");

		System.out.printf("First Name: ");
		String first = s.nextLine();
		
		System.out.printf("\nLast Name: ");
		String last = s.nextLine();
		
		System.out.printf("\nEmail: ");
		String email = s.nextLine();
		
		System.out.printf("\nPhone Number: ");
		String phone = s.nextLine();
		
		System.out.printf("\nPreferred Username: ");
		String username = s.nextLine();

		System.out.printf("\n\tWelcome %s %s to the BugBytes Shopping Center\n\n "+
						    "\tPlease look around for anything that interests you!\n\n", first,last);
		
	}
	
}
