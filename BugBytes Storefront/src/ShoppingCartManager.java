import java.util.Scanner;

/**
 * 	Implements a menu to interact with the virtual store via console input
 *
 * 	@author Alexander Gunby, Sikander Ghafary, Jacob Normington
 * 	@version 5/4/2021
 */
public class ShoppingCartManager
{
	private static Scanner in;
	private static Storefront store;
	private static ShoppingCart cart;
	private static boolean loggedIn = false;
	private static boolean reset = true; //determines whether or not the database should be reset upon use
	
	/**
	 * 	Begins the manager to create your shopping cart by system input. 
	 */
	public static void main(String args[])
	{
		in = new Scanner(System.in);
        store = new Storefront(); //use storefront methods instead of Connector
        System.out.println("BugBytes Shopping Center");
        welcome();
        
        //arrive here only when the user has quit
        String fairwell = "Thank you for shopping with us today";
        if (loggedIn)
        {
        	fairwell += ", " + cart.getCustomerName(); 
        }
        System.out.println(fairwell + "!");
        System.out.println("We hope to see you again soon.");
        in.close();
	}
	
	private static void welcome()
	{
        System.out.println("Would you like to:");
        System.out.println("1 - login");
        System.out.println("2 - sign up for a new account");
        System.out.println("3 - view anonymously");
        System.out.println("4 - quit");
        String answer = in.nextLine();
        
        switch (answer)
        {
        	case "1":
        	{
        		login();
        		break;
        	}
        	case "2":
        	{
        		signUp();
        		break;
        	}
        	case "3":
        	{
        		store.login("default", "Jagannadha", "Chidella");
        		cart = new ShoppingCart(1, "default", reset);
        		reset = false; //you don't want to reset the database during a session
        		loggedIn = false;
        		System.out.printf("\n\tWelcome to the BugBytes Shopping Center\n\n "+
    				    "\tPlease look around for anything that interests you!\n\n");
        		printMenu();
        	}
        	case "4":
        	{
        		break; //terminate's session
        	}
        	default:
        	{
        		System.out.println("Invalid answer. Please enter 1, 2, or 3. \n");
        		welcome();
        	}
        }
        //user has quit
        //System.out.println("quit from welcome");
	}
	
	private static void login()
	{
		System.out.println("Please fill in the following information to log into your account:");
		System.out.print("Username: ");
		String username = in.nextLine();
		System.out.print("First Name: ");
		String first = in.nextLine();
		System.out.print("Last Name: ");
		String last = in.nextLine();
		System.out.println(); //spacing
		
		int custID = store.login(username, first, last);
		if (custID < 1) //account does not exist
		{
			System.out.println("No such account exists.");
			if (retry())
				login();
			else
				welcome();
		}
		cart = new ShoppingCart(custID, first + " " + last, reset);
		reset = false; //you don't want to reset the database during a session
		loggedIn = true;
		if (first.equalsIgnoreCase("Sai") && last.equalsIgnoreCase("Suresh"))
		{
			store.requestAuthorization("shopMgr", "csc131");
			System.out.println("user4 has been granted store admin privileges.");
		}
		System.out.printf("\n\tWelcome %s %s to the BugBytes Shopping Center\n\n"+
			    "Please look around for anything that interests you!\n\n", first,last);
		printMenu();
	}
	
	private static void signUp() 
	{
		System.out.println("Please fill in the following information to create an account:");
		System.out.print("First Name: ");
		String first = in.nextLine();
		System.out.print("Last Name: ");
		String last = in.nextLine();
		System.out.print("Email: ");
		String email = in.nextLine();
		System.out.print("Phone Number: ");
		String phone = in.nextLine();
		System.out.print("Preferred Username: ");
		String username = in.nextLine();
		
		try
		{
			store.signUp(username, first, last, email, phone);
			int custID = store.login(username, first, last);
			cart = new ShoppingCart(custID, first + " " + last, reset);
			reset = false; //you don't want to reset the database during a session
			loggedIn = true;
			System.out.printf("\n\tWelcome %s %s to the BugBytes Shopping Center\n\n"+
				    "Please look around for anything that interests you!\n\n", first,last);
			printMenu();
		}
		catch (Exception e)
		{
			System.out.println("Sign up attempt failed.");
			if(retry())
				signUp();
			else
				welcome();
		}
	}
    
   	/**
   	 * 	Outputs a menu of options for use in the store, must have
   	 */
   	private static void printMenu()
   	{
   		//System.out.println();
   		System.out.println("Viewing:");
	  	System.out.println("a - View all Inventory");
     	System.out.println("w - View alcohol Inventory");
     	System.out.println("b - View bakery Inventory");
     	System.out.println("f - View breakfast Inventory");
     	System.out.println("d - View dairy Inventory");
     	System.out.println("m - View meat/seafood Inventory");
     	System.out.println("p - View produce Inventory");
     	if(!loggedIn)
     	{
     		System.out.println("");
         	System.out.println("l - return to login screen");
     	}
     	else
     	{
     		System.out.println("");
         	System.out.println("s - sign out");
     	}
     	if(store.isAdmin())
     	{
     		System.out.println("");
         	System.out.println("Administration:");
         	System.out.println("i - Add Item to Inventory");
         	System.out.println("r - Remove Item from Inventory");
     	}
     	System.out.println("Choose an option: ");
     	String nextInv = in.nextLine();
     	
     	switch (nextInv)
      	{
      		case "q": //quit
      		{
      			break;
      		}
      		case "a": //print all
      		{
      			store.printInventory();
      			//question
      			break;
      		}
      		case "w": //print alcohol aisle
      		{
      			store.printAisle("Alcohol");
      			//question
      			break;
      		}
      		case "b":
      		{
      			store.printAisle("Bakery");
      			//question
      			break;
      		}
      		case "f": 
      		{
      			store.printAisle("Breakfast");
      			//question
      			break;
      		}
      		case "d":
      		{
      			store.printAisle("Dairy");
      			//question
      			break;
      		}
      		case "m": 
      		{
      			store.printAisle("Meat_seafood");
      			//question
      			break;
      		}
      		case "p":
      		{
      			store.printAisle("Produce");
      			//question
      			break;
      		}
      		case "l":
      		{
      			welcome();
      			break;
      		}
      		case "s":
      		{
      			loggedIn = false;
      			welcome();
      			break;
      		}
      		case "i":
      		{
      			System.out.println("Insert the type of Product: Ex(Alcohol,Bakery,Breakfast,Dairy,Meat_seafood,Produce)");
            	String s1 = in.nextLine();
            	System.out.println("Insert name of Product");
            	String s2 = in.nextLine(); 
            	System.out.println("Insert price of Product");
            	double d1 = in.nextDouble();
            	System.out.println("Insert quantity of Product");
            	int 	i1 = in.nextInt();
            	System.out.println("Insert quantity at which the Product should be restocked");
           	 	int 	i2 = in.nextInt();
           	 	store.addInventory(new Item(s1, s2, d1), i1, i2);
    	 		printMenu(); //goes back to main menu
      		}
      		case "r":
      		{
      			System.out.println("Insert name of Product to be removed");
            	String name = in.nextLine();
            	store.removeInventory(name);
            	printMenu(); //goes back to main menu
      		}
      		default: 
      		{
      			System.out.println("Invalid answer. Please try again. \n\n");
      			printMenu();
      		}
      	}
     	//user has quit
     	//System.out.println("quit from printMenu");
   	}
    
   	private static boolean retry()
   	{
   		System.out.println("Would you like to try again?");
		String answer = in.nextLine();
		switch (answer)
		{
			case "y": return true;
			case "yes": return true;
			case "Y": return true;
			case "Yes": return true;

			case "n": return false;
			case "no": return false;
			case "N": return false;
			case "No": return false;
			
			default:
			{
				System.out.println("Answer cannot be understood. Returning to main menu.");
				if (loggedIn)
					printMenu();
				else
					welcome();
				return false; //ends session
			}
		}
   	}
}