import java.util.Scanner;
/**
 * Implements a menu to manipulate a shopping cart via console input
 *
 * @author Jacob Normington
 * @version 4/30/2020 
 */
public class ShoppingCartManager
{
   private static Scanner in;
   private static Connector c; 
   private static ShoppingCart cart;
	
	/**
    * Begins the manager to create your shopping cart by system input
    */
   public static void main(String args[])
   {
        in = new Scanner(System.in);
        c = Connector.getCon();
        
        System.out.println("Shopping Cart Manager");
        System.out.println("...");
        System.out.println("Please enter your name.");
        String name = in.nextLine();
        System.out.println("Customer Name: " + name);
        System.out.println("...");
        cart = new ShoppingCart(1);
        printMenu(cart);
        
        in.close();
   }
    
   /**
    * Outputs a menu of options that can be used to manipulate a
    * shopping cart
    *
    * @param   cart  the shopping cart to be modified
    * @return        none
    */
   private static void printMenu(ShoppingCart cart)
   {
      System.out.println();
      System.out.println("MENU");
      System.out.println("a - Add item to cart");
      System.out.println("d - Remove item from cart");
      System.out.println("c - Change item quantity");
      System.out.println("i - Output item's descriptions");
      System.out.println("o - Output shopping cart");
      System.out.println("q - Quit");
      System.out.println("");
      System.out.println("Choose an option: ");
      String next = in.nextLine();
      
      if(!next.equals( "q")) //quit
      {
         if(next.equals("a")) //add an item to cart
         {
            System.out.println("ADD ITEM TO CART");
            System.out.println("Enter the item name:");
            String name = in.nextLine();
            System.out.println("Enter the item description:");
            String description = in.nextLine();
            System.out.println("Enter the item price:");
            int price = java.lang.Integer.parseInt(in.next());
            System.out.println("Enter the item quantity:");
            int quantity = java.lang.Integer.parseInt(in.next());
            //cart.addItem(new Item(name, description, price, quantity));
         }
         if(next.equals("d")) //remove an item from cart
         {
            System.out.println("REMOVE ITEM FROM CART");
            System.out.println("Enter name of item to remove:");
            //cart.removeItem(in.nextLine());
         }
         if(next.equals("c")) //change item quantity
         {
            System.out.println("CHANGE ITEM QUANTITY");
            Item item = new Item();
            System.out.println("Enter the item name:");
            item.setName(in.nextLine());
            System.out.println("Enter the new quantity:");
            item.setQuantity(in.nextInt());
            //cart.modifyItem(item);
         }
         if(next.equals("i")) //outputs item's descriptions
         {
            System.out.println("OUTPUT ITEM'S DESCRIPTIONS");
            //cart.printDescription();
         }
         if(next.equals("o")) //outputs shopping cart
         {
            System.out.println("OUTPUT SHOPPING CART");
            System.out.println();
            cart.printTotal();
         }
         printMenu(cart);
      }
      in.close();
   }
   
   /*private static void select() 
    {
		System.out.printf("\nPlease select a product to add to cart: ");
		String selection = in.nextLine();
		
		//if (selection == )
		
	}
	
	private static void menu() 
	{
		c.read("products");
	}

	
	// customer logins in with email and password
	private static void login() 
	{
		System.out.printf("Please fill in the following information to create an account:\n\n");

		System.out.printf("First Name: ");
		String first = in.nextLine();
		
		System.out.printf("\nLast Name: ");
		String last = in.nextLine();
		
		System.out.printf("\nEmail: ");
		String email = in.nextLine();
		
		System.out.printf("\nPhone Number: ");
		String phone = in.nextLine();
		
		System.out.printf("\nPreferred Username: ");
		String username = in.nextLine();

		System.out.printf("\n\tWelcome %s %s to the BugBytes Shopping Center\n\n "+
						    "\tPlease look around for anything that interests you!\n\n", first,last);
		
	}
	*/
}
