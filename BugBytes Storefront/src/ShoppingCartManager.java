
import java.util.Scanner;

import main.Item;
import main.ShoppingCart;
import main.Storefront;

/**
 * 	Implements a menu to interact with the virtual store via console input
 *
 * 	@author Alexander Gunby, Sikander Ghafary, Jacob Normington
 * 	@version 5/11/2021
 */
public class ShoppingCartManager
{
	private static Scanner in;
	private static Storefront store;
	private static ShoppingCart cart;
	private static boolean loggedIn = false;
	private static boolean reset = false; //determines whether or not the database should be reset upon use
	
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
        if (cart != null && cart.getCustomerId() > 1)
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
        System.out.print("Choose an option: ");
        String answer = in.nextLine();
        System.out.println();
        
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
        		System.out.println();
        		break; //terminate's session
        	}
        	default:
        	{
        		System.out.println("Invalid answer. Please enter 1, 2, 3, or 4. \n");
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
			if (retry())
				login();
			else
				welcome();
		}
		else
		{
			cart = new ShoppingCart(custID, first + " " + last, reset, cart);
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
			cart = new ShoppingCart(custID, first + " " + last, reset, cart);
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
   		System.out.println("Viewing:");
	  	System.out.println("a - View All Inventory");
     	System.out.println("g - Go to an Aisle");
     	System.out.println("-----");
     	System.out.println("Account:");
     	System.out.println("c - View Cart");
     	if(!loggedIn)
     	{
         	System.out.println("l - Return to Login Screen");
     	}
     	else
     	{
     		System.out.println("m - My Account Information");
     		System.out.println("o - My Previous Orders");
     		System.out.println("s - Sign Out");
     	}
     	if(store.isAdmin())
     	{
     		System.out.println("-----");
         	System.out.println("Administration:");
         	System.out.println("i - Add Item to Inventory");
         	System.out.println("r - Remove Item from Inventory");
         	System.out.println("u - Update an Item in the Inventory");
         	System.out.println("n - View All Customer Information");
         	System.out.println("v - View All Orders");
     	}
     	System.out.print("Choose an option: ");
     	String nextInv = in.nextLine();
     	System.out.println(); //spacing
     	
     	switch (nextInv)
      	{
      		case "a": //print all
      		{
      			store.printInventory();
      			makeOrder("an"); //question, if user wants to add something to the cart
      			break;
      		}
      		case "g": //go to an aisle
      		{
      			goTo();
      			break;
      		}
      		case "c":
      		{
      			if (cart.getCartSize() == 0)
      			{
      				System.out.println("Cart is empty: Choose another option \n");
      				printMenu();
      			}
      			else
      				printCartMenu();
      			break;
      		}
      		case "l":
      		{
      			if(loggedIn)
      			{
      				System.out.println("You are already logged in \n");
      				printMenu();
      			}
      			else
      				welcome();
      			break;
      		}
      		case "m":
      		{
      			if(loggedIn)
      				accountMenu(false);
      			else
      			{
      				System.out.println("This is not possible without logging in. \n");
      				printMenu();
      			}
      			break;
      		}
      		case "o":
      		{
      			if(loggedIn)
      				orderMenu(false);
      			else
      			{
      				System.out.println("This is not possible without logging in. \n");
      				printMenu();
      			}
      			break;
      		}
      		case "s":
      		{
      			if(loggedIn)
      			{
      				loggedIn = false;
      				store.requestAuthorization("", ""); //removes authorization
      				cart = null; //releases all information connected to your cart
          			welcome();
      			}
      			else
      			{
      				System.out.println("This is not possible without logging in. \n");
      				printMenu();
      			}
      			break;
      		}
      		case "i":    //adds item to database
      		{
      			System.out.print("Product Type (Ex: Alcohol,Bakery,Breakfast,Dairy,Meat_seafood,Produce): ");
            	String category = in.nextLine();
            	System.out.print("Product Name: ");
            	String name = in.nextLine(); 
            	System.out.print("Product Price: ");
            	double price = Double.parseDouble(in.nextLine().trim());
            	System.out.print("Product Quantity: ");
            	int quantity = Integer.parseInt(in.nextLine().trim());
            	System.out.print("Quantity at Which the Product Should Be Restocked: ");
           	 	int restock = Integer.parseInt(in.nextLine().trim());
           	 	System.out.println(); //spacing
           	 	store.addInventory(new Item(category, name, price), quantity, restock);
    	 		printMenu(); 
    	 		break;
      		}
      		case "r":	//remove item from database
      		{
      			System.out.print("Product Name: ");
            	String name = in.nextLine();
            	System.out.println(); //spacing
            	store.removeInventory(name);
            	printMenu(); 
            	break;
      		}
      		case "u":
      		{
      			System.out.print("Product Name: ");
            	String name = in.nextLine();
            	System.out.println(); //spacing
            	
            	System.out.println("Would you like to:");
        		System.out.println("1 - Move Product to a Different Aisle");
        		System.out.println("2 - Change Product's Name");
        		System.out.println("3 - Change Product's Price");
        		System.out.println("4 - Change Product's Reorder Quantity");
        		System.out.println("5 - Return to Main Menu");
        	    System.out.print("Choose an option: ");
        	    String toChange = in.nextLine();
        	    System.out.println("New Value: ");
        	    String answer = in.nextLine();
        	    System.out.println(); //spacing
        	    
        	    switch(toChange)
        	    {
	        	    case "1": 
	    	    	{
	    	    		store.moveItem(name, answer);
	    	    		printMenu();
	    	    		break;
	    	    	}
	    	    	case "2":
	    	        {
	    	        	store.changeProductName(name, answer);
	    	        	printMenu();
	    	        	break;
	    	        }
	    	    	case "3":
	    	        {
	    	        	try
	    	        	{
	    	        		double price = Double.parseDouble(answer.trim());
	    	        		store.setUnitPrice(name, price);
	    	        	}
	    	        	catch (NumberFormatException e)
	    	        	{
	    	        		System.out.println("Price Invalid, Answer Cannot be Understood. Returning to Main Menu. \n");
	    	        	}
	    	        	printMenu();
	    	    		break;
	    	        }
	    	    	case "4":
	    	        {
	    	        	try
	    	        	{
	    	        		int reorder = Integer.parseInt(answer.trim());
	    	        		store.setReorder(name, reorder);
	    	        	}
	    	        	catch (NumberFormatException e)
	    	        	{
	    	        		System.out.println("Reorder Invalid, Answer Cannot be Understood. Returning to Main Menu. \n");
	    	        	}
	    	        	printMenu();
	    	    		break;
	    	        }
	    	    	case "5":
	    	        {
	    	        	System.out.println("Returning to Main Menu. \n");
	    	        	printMenu();
	    	        	break;
	    	        }
	    	        default:
	    	        {
	    	        	System.out.println("Invalid answer. Please enter 1, 2, 3, or 4. Returning to Main Menu. \n");
	    	        	printMenu();
	    		   		break;
	    	        }
        	    }
      			break;
      		}
      		case "n":
      		{
      			accountMenu(true);
      			break;
      		}
      		case "v":
      		{
      			orderMenu(true);
      			break;
      		}
      		default: 
      		{
      			System.out.println("Invalid answer. Please try again. \n\n");
      			printMenu();
      			break;
      		}
      	}
     	//user has quit
   	}
   	
   	/**
   	 * 	Ask user which aisle they would like to go to, then displays the aisle. 
   	 */
   	private static void goTo()
   	{
   		System.out.println("Bugbytes Store Aisles:");
   		System.out.println("1 - View Alcohol Inventory");
   		System.out.println("2 - View Bakery Inventory");
     	System.out.println("3 - View Breakfast Inventory");
     	System.out.println("4 - View Dairy Inventory");
     	System.out.println("5 - View Meat / Seafood Inventory");
     	System.out.println("6 - View Produce Inventory");
     	System.out.print("Choose an option: ");
     	String choice = in.nextLine();
     	System.out.println(); //spacing
     	
     	switch(choice)
     	{
     		case "1": //print alcohol aisle
     		{
     			store.printAisle("Alcohol");
     			makeOrder("an"); //question
     			break;
     		}
     		case "2":
     		{
     			store.printAisle("Bakery");
     			makeOrder("an"); //question
     			break;
     		}
     		case "3": 
     		{
     			store.printAisle("Breakfast");
     			makeOrder("an"); //question
     			break;
     		}
     		case "4":
     		{
	  			store.printAisle("Dairy");
	  			makeOrder("an"); //question
	  			break;
	  		}
	  		case "5": 
	  		{
	  			store.printAisle("Meat_seafood");
	  			makeOrder("an"); //question
	  			break;
	  		}
	  		case "6":
	  		{
	  			store.printAisle("Produce");
	  	   		makeOrder("an"); //question
	  			break;
	  		}
	  		default:
	  		{
	  			System.out.println("Answer cannot be understood. Please enter 1, 2, 3, 4, 5, or 6.");
   				if(retry())
   					goTo();
   				else
   					printMenu();
   				return;
	  		}
     	}
   	}
   	
   	private static void printCartMenu()
   	{
   		cart.printTotal();
   		System.out.println("Cart Menu Options:");
   		if(loggedIn)
   			System.out.println("1 - confirm order");
   		System.out.println("2 - remove item from cart");
   		System.out.println("3 - change quantity order for item");
   		System.out.println();
   		System.out.println("4 - return to menu");
   		System.out.println();
   		System.out.println("5 - delete cart"); //mainly used for testing 
   		System.out.print("Choose an option: ");
   		int nextOrder = 0;
   		try
   		{
   			nextOrder = Integer.parseInt(in.nextLine().trim());
   		}
   		catch (NumberFormatException e)
   		{
   			System.out.println("Invalid answer. Please enter 1, 2, 3, or 4. \n");
    		printCartMenu();
    		return;
   		}
   		
   		System.out.println(); //spacing
   		
   		switch (nextOrder)
   		{
   			case 1:
   			{
   				if(loggedIn)
   				{
   					System.out.println("Confirm order? Y/N");
   	   				System.out.print("Answer: ");
   	   				String nextConfirm = in.nextLine();
   	   				if (nextConfirm.equalsIgnoreCase("y"))
   	   				{
   	   					store.checkout(cart);
   	   					printMenu();
   	   				}
   	   				else if (nextConfirm.equalsIgnoreCase("n"))
   	   				{
   	   					System.out.println("Returning to Cart Menu: ");
   	   					printCartMenu();
   	   				}
   	   				else 
   	   				{
   	   					System.out.println("Incorrect input: Please input a Y or N");
   	   					printCartMenu();
   	   				}
   				}
   				else
   				{
   					System.out.println("This is not possible without logging in. \n");
   					printCartMenu();
   				}
   				
   				return;
   			}
   			
   			case 2: //remove item from cart
   			{
   				System.out.print("Name of item to be removed: ");
   				String name = in.nextLine();
   				System.out.println(); //spacing
   				if(cart.containsName(name))
   				{
   					cart.removeFromCart(name);
   				}
   				else
   				{
   					System.out.println("You do not have an item called: " + name + "\n");
   				}
   				
   				printCartMenu();
   				return;
   			}
   			
   			case 3: //change quantity of item from cart
   			{
   				System.out.print("Name of item to be edited: ");
   				String name = in.nextLine();
   				System.out.print("New quantity: ");
   				int quantity = Integer.parseInt(in.nextLine().trim());
   				System.out.println(); //spacing
   				if(cart.containsName(name))
   				{
   					cart.changeCartQuantity(name, quantity);
   				}
   				else
   				{
   					System.out.println("You do not have an item called: " + name + "\n");
   				}
   				
   				printCartMenu();
   				return;
   			}

   			case 4: //return to menu 
   			{
   				printMenu();
   				return;
   			}
   		
   			case 5: // clears cart
   			{
   				cart.clearCart();
   				return;
   			}
   		
   			default: 
   				System.out.println("Insert proper command: " );
   				return;
   		}
   	}
   	
   	/**
   	 * 	Asks user if they would like to add an item to their cart. 
   	 * 
   	 * 	@param 	alt		is just used to change the message slightly
   	 */
   	private static void makeOrder(String alt)
   	{
   		System.out.println("Would you like to add " + alt + " item into your cart?"); // Adds item to cart, errors with calling itself within the if statements. 
   		System.out.print("Answer: ");
   		String answer = in.nextLine();
   		System.out.println(); //spacing
   		
   		switch (answer)
   		{
   			case "y": 
   				answer = "y";
   				break;
   			case "yes": 
   				answer = "y";
   				break;
   			case "Y": 
   				answer = "y";
   				break;
   			case "Yes": 
   				answer = "y";
   				break;
   			case "YES": 
   				answer = "y";
   				break;

   			case "n": 
   				answer = "n";
   				break;
   			case "no": 
   				answer = "n";
   				break;
   			case "N": 
   				answer = "n";
   				break;
   			case "No": 
   				answer = "n";
   				break;
   			case "NO": 
   				answer = "n";
   				break;
   				
   			default:
   			{
   				System.out.println("Answer cannot be understood.");
   				if(retry())
   					makeOrder(alt);
   				else
   					printMenu();
   				return;
   			}
   		}
   			
   		switch(answer)
   		{
   			case "y":
   			{
   				System.out.print("Name of item to be added: ");	//Insert name of Product
   				String nextItem = in.nextLine();
   				System.out.print("Quantity: ");
   				int quantity = Integer.parseInt(in.nextLine().trim());
   				try
   				{
   					cart.addToCart(nextItem, quantity);
   	   				makeOrder("another");
   	   				return;
   				}
   				catch (Exception e)
   				{
   					System.out.println(e.getMessage());
   					if(retry())
   	   					makeOrder("an");
   	   				else
   	   					printMenu();
   	   				return;
   				}
   			}
   			case "n":
   			{
   				System.out.println("Returning to Menu.\n");
   				printMenu();
   				return;
   			}
   			default:
   			{
   				//do nothing, this is impossible
   			}
   		}
   	}
   	
   	/**
   	 * 	Prints user information, and queries and needed updates. 
   	 * 
   	 * 	@param 	all		if all customers' information should be used
   	 */
   	private static void accountMenu(boolean all)
   	{
   		if(all)
   			store.printCustomers();
   		else
   			store.printCustomer(cart.getCustomerId());
   		
   		System.out.println("Would you like to update account information?");
   		System.out.print("Answer: ");
   		String answer = in.nextLine();
   		System.out.println(); //spacing
   		
   		switch (answer)
   		{
   			case "y": 
   				answer = "y";
   				break;
   			case "yes": 
   				answer = "y";
   				break;
   			case "Y": 
   				answer = "y";
   				break;
   			case "Yes": 
   				answer = "y";
   				break;
   			case "YES": 
   				answer = "y";
   				break;

   			case "n": 
   				answer = "n";
   				break;
   			case "no": 
   				answer = "n";
   				break;
   			case "N": 
   				answer = "n";
   				break;
   			case "No": 
   				answer = "n";
   				break;
   			case "NO": 
   				answer = "n";
   				break;
   				
   			default:
   			{
   				System.out.println("Answer cannot be understood.");
   				if(retry())
   					accountMenu(all);
   				else
   					printMenu();
   				return;
   			}
   		}
   			
   		switch(answer)
   		{
   			case "y":
   			{
   				changeAccountInfo(all);
   				break;
   			}
   			case "n":
   			{
   				System.out.println("Returning to Menu.\n");
   				printMenu();
   				break;
   			}
   			default:
   			{
   				//do nothing, this is impossible
   			}
   		}
   	}
   	
   	/**
   	 * 	Handles user's intent to change the information associated with an account.
   	 * 
   	 * 	@param 	all		if all customers' information should be used
   	 */
   	private static void changeAccountInfo(boolean all)
   	{
   		int custID = cart.getCustomerId(); //by default, your cart
		if(all)
	   	{
			System.out.println("Which user needs to be changed: ");
		   	String user = in.nextLine();
		   	System.out.println(); //spacing
		   	try 
		   	{
		   		custID = Integer.parseInt(user.trim());
		   	}
		   	catch (NumberFormatException e)
		   	{
		   		System.out.println("'" + user + "' is not a valid customer ID. Returning to Main Menu. \n");
		   		printMenu();
		   	}
	   	}
			
		System.out.println("Would you like to:");
		System.out.println("1 - Change Your Registered Email Adress");
		System.out.println("2 - Change Your Registered Phone Number");
		System.out.println("3 - Remove Your Account");
		System.out.println("4 - Return to Main Menu");
	    System.out.print("Choose an option: ");
	    String toChange = in.nextLine();
	    System.out.println(); //spacing
	        
		switch (toChange)
	    {
	    	case "1": 
	    	{
	    		System.out.print("New Entry: ");
	    		String value = in.nextLine();
	    		System.out.println(); //spacing
	    		store.changeEmail(custID, value);
	    		accountMenu(all);
	    		break;
	    	}
	    	case "2":
	        {
	        	System.out.print("New Entry: ");
	    		String value = in.nextLine();
	    		System.out.println(); //spacing
	        	store.changePhone(custID, value);
	        	accountMenu(all);
	        	break;
	        }
	    	case "3":
	        {
	        	System.out.print("Are You Sure? Please answer exactly 'yes'.");
	        	System.out.print("Answer: ");
	    		String value = in.nextLine();
	    		
	    		if(value.equalsIgnoreCase("yes"))
	    		{
	    			store.removeAccount(custID); //preserves the contents of your cart on the frontend
	    			loggedIn = false;
      				store.requestAuthorization("", ""); //removes authorization
          			welcome();
	    		}
	    		else
	    		{
	    			System.out.println("You did not answer exactly 'yes'. Account not removed. \n");
	    			accountMenu(all);
	    		}
	    		break;
	        }
	    	case "4":
	        {
	        	printMenu();
	        	break;
	        }
	        default:
	        {
	        	System.out.println("Invalid answer. Please enter 1, 2, or 3. \n");
		   		changeAccountInfo(all);
		   		break;
	        }
	    }
   	}
   	
   	/**
   	 * 	Prints the user's orders, and queries and needed updates.
   	 * 
   	 * 	@param 	all		if all customers' information should be used
   	 */
   	private static void orderMenu(boolean all)
   	{
   		if(all)
   			store.printOrders();
   		else
   			store.printMyOrders(cart.getCustomerId());
   		
   		System.out.println("Would you like to cancel an order?");
   		System.out.print("Answer: ");
   		String answer = in.nextLine();
   		System.out.println(); //spacing
   		
   		switch (answer)
   		{
   			case "y": 
   				answer = "y";
   				break;
   			case "yes": 
   				answer = "y";
   				break;
   			case "Y": 
   				answer = "y";
   				break;
   			case "Yes": 
   				answer = "y";
   				break;
   			case "YES": 
   				answer = "y";
   				break;

   			case "n": 
   				answer = "n";
   				break;
   			case "no": 
   				answer = "n";
   				break;
   			case "N": 
   				answer = "n";
   				break;
   			case "No": 
   				answer = "n";
   				break;
   			case "NO": 
   				answer = "n";
   				break;
   				
   			default:
   			{
   				System.out.println("Answer cannot be understood.");
   				if(retry())
   					orderMenu(all);
   				else
   					printMenu();
   				return;
   			}
   		}
   			
   		switch(answer)
   		{
   			case "y":
   			{
   				System.out.print("Order ID: ");
   		   		String order = in.nextLine();
   		   		try
   		   		{
   		   			int orderID = Integer.parseInt(order.trim());
	   		   		System.out.print("Are You Sure? Please answer exactly 'yes'.");
	   		   		System.out.print("Answer: ");
	   		   		String value = in.nextLine();
	    		
	   		   		if(value.equalsIgnoreCase("yes"))
	   		   		{
	   		   			store.cancelOrder(orderID);
		    			printMenu();
		    		}
		    		else
		    		{
		    			System.out.println("You did not answer exactly 'yes'. Order not cancelled. \n");
		    			orderMenu(all);
		    		}
   		   		}
   		   		catch (NumberFormatException e)
   		   		{
   		   			System.out.println("'" + order + "' is not a valid order ID. No order cancelled. Please try again. \n");
		   			orderMenu(all);
   		   		}
   				break;
   			}
   			case "n":
   			{
   				orderDetails();
   				break;
   			}
   			default:
   			{
   				//do nothing, this is impossible
   			}
   		}
   	}
   	
   	/**
   	 * 	Displays the details of an order. 
   	 */
   	private static void orderDetails()
   	{
   		System.out.println("Would you like to view the contents of an order?");
   		System.out.print("Answer: ");
   		String answer = in.nextLine();
   		System.out.println(); //spacing
   		
   		switch (answer)
   		{
   			case "y": 
   				answer = "y";
   				break;
   			case "yes": 
   				answer = "y";
   				break;
   			case "Y": 
   				answer = "y";
   				break;
   			case "Yes": 
   				answer = "y";
   				break;
   			case "YES": 
   				answer = "y";
   				break;

   			case "n": 
   				answer = "n";
   				break;
   			case "no": 
   				answer = "n";
   				break;
   			case "N": 
   				answer = "n";
   				break;
   			case "No": 
   				answer = "n";
   				break;
   			case "NO": 
   				answer = "n";
   				break;
   				
   			default:
   			{
   				System.out.println("Answer cannot be understood.");
   				if(retry())
   					orderDetails();
   				else
   					printMenu();
   				return;
   			}
   		}
   			
   		switch(answer)
   		{
   			case "y":
   			{
   				System.out.print("Order ID: ");
   		   		String order = in.nextLine();
   		   		try
   		   		{
   		   			int orderID = Integer.parseInt(order.trim());
   		   			store.printMyOrderDetails(orderID);
   		   			System.out.print("Click any key to return to main menu: ");
   		   			in.nextLine();
   		   			System.out.println(); //spacing
   		   			printMenu();
   		   		}
   		   		catch (NumberFormatException e)
   		   		{
   		   			e.printStackTrace();
   		   			System.out.println("'" + order + "' is not a valid order ID. Pealse try again. \n");
		   			orderDetails();
   		   		}
   				break;
   			}
   			case "n":
   			{
   				System.out.println("Returning to Menu.\n");
   				printMenu();
   				break;
   			}
   			default:
   			{
   				//do nothing, this is impossible
   			}
   		}
   	}
   	
   	/**
   	 * 	Asks the user to retry. Should be preceded by a description of the error. 
   	 * 
   	 * 	@return		a boolean describing whether the user wishes to try again
   	 */
   	private static boolean retry()
   	{
   		System.out.println("Would you like to try again? Y/N");
   		System.out.print("Answer: ");
		String answer = in.nextLine();
		System.out.println(); //spacing
		
		switch (answer)
		{
			case "y": return true;
			case "yes": return true;
			case "Y": return true;
			case "Yes": return true;
			case "YES": return true;

			case "n": return false;
			case "no": return false;
			case "N": return false;
			case "No": return false;
			case "NO": return false;
			
			default:
			{
				System.out.println("Answer cannot be understood. Returning to main menu.");
				return false;
			}
		}
   	}
}

/*
 * Stuff Alexander did: 
 *	Edited addToCart in ShoppingCart class. The method auto translates Name of Product to the ID code. 
 *
 *	makeOrder() method: Asks if you want to add an item. If yes then allows you to do so. (Only visible/possible when loggedIn)
 * 	CartMenu() method: Does cart actions 
 * 
 *  I did some of the Switch things with Numbers because i got errors when i did it with letters.
 * 
 * ERRORS found by Alex: 	
 * 	-If you View anonymously then try to log in errors occur. Something about CartNode.getValue() and this.current is null
 * 	-When log in , add item to cart, log out, logging in again ERROR results 
 * 	
 * 	-adding an item that was already added before causes an error. 
 * 
 * 
 *  -removeFromCart doesn't work if it deletes either the head or tail. Some error code happens. 
 */